package sg.edu.np.mad.NP_Eats_Team03;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.NP_Eats_Team03.ml.Model;

public class HomeFragment extends Fragment {

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";
    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerViewItem;
    private MenuAdapter menuAdapter;
    private ArrayList<Item> itemList = new ArrayList<>();
    private CartViewModel cartViewModel;
    private SearchView searchView;
    private ListView listView;
    ArrayList<String> reccomendedList = new ArrayList<>();//For searches

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public String[] setReccomendedList() {
        String[] top_6_searches = new String[6];
        try {
            // Example scaled input data
            float[] new_X_scaled = new float[]{
                    13, 3, 6, 14, 7, 20, 4, 18, 2, 13, 17, 11, 8, 3, 15,
                    13, 3, 6, 14, 7, 20, 4, 18, 2, 13, 17, 11, 8, 3, 15};


            // Prepare input data as ByteBuffer
            int inputSize = new_X_scaled.length; // Number of elements
            int bufferSize = 4 * inputSize; // 4 bytes per float
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bufferSize);
            byteBuffer.order(ByteOrder.nativeOrder());
            for (float val : new_X_scaled) {
                byteBuffer.putFloat(val);
            }
            byteBuffer.rewind(); // Rewind buffer to beginning

            // Load the model and run inference
            try {
                Model model = Model.newInstance(getContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, inputSize}, DataType.FLOAT32);
                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                Model.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                // Get the predictions
                float[] predictions = outputFeature0.getFloatArray();
                Integer[] indices = new Integer[predictions.length];
                for (int i = 0; i < predictions.length; i++) {
                    indices[i] = i;
                }
                Arrays.sort(indices, (i1, i2) -> Float.compare(predictions[i2], predictions[i1]));


                for (int i = 0; i < 6; i++) {
                    top_6_searches[i] = "P" + String.format("%02d", indices[i] + 1);
                    for (Item item : itemList) {
                        if (item.itemId.equals(top_6_searches[i])) {
                            reccomendedList.add(item.name);
                            Log.e(TAG, "predictSearches: " + item.name);
                        }

                    }
                }

                // Display or use top_6_searches as needed
                Log.d(TAG, "Top 6 Predicted Searches: " + Arrays.toString(top_6_searches));

                // Releases model resources if no longer used.
                model.close();
            } catch (IOException e) {
                Log.e(TAG, "Error loading TensorFlow Lite model: " + e);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error preparing or scaling data: " + e);
        }
        return  top_6_searches;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve parameters if any
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        retrievemenuItems(FirebaseFirestore.getInstance());

        orderHistoryCount(FirebaseFirestore.getInstance(), new OnOrderHistoryListener() {
            @Override
            public void onOrderHistoryLoaded(float[] orderCounts) {
                // Use orderCounts here
                Log.e(TAG, "onOrderHistoryLoaded: " + Arrays.toString(orderCounts));
                // You may want to update UI components with this data here
            }
        });

        Log.e(TAG, "onCreateView: " + reccomendedList.toString());
        // Initialize ArrayAdapter with context and recommendedList
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, reccomendedList);


        recyclerViewItem = view.findViewById(R.id.horizontalRV);
        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        menuAdapter = new MenuAdapter(getContext(), itemList, new MenuAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(Item item) {
                cartViewModel.addToCart(item, 1);
            }
        });

        LinearLayoutManager itemLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewItem.setLayoutManager(itemLayoutManager);
        recyclerViewItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItem.setAdapter(menuAdapter);

        ArrayList<String> recyclerStoreList = new ArrayList<>();
        recyclerStoreList.add("Prata boy");
        recyclerStoreList.add("Oishii daily");
        recyclerStoreList.add("Coffee connect");
        recyclerStoreList.add("Acai den");

        RecyclerView recyclerViewStore = view.findViewById(R.id.storelistRV);
        StoreAdapter storeAdapter = new StoreAdapter(recyclerStoreList);
        LinearLayoutManager storeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewStore.setLayoutManager(storeLayoutManager);
        recyclerViewStore.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStore.setAdapter(storeAdapter);

        return view;
    }

    public void retrievemenuItems(FirebaseFirestore db) {
        String[] reccomendations = setReccomendedList();

        for(String string: reccomendations){
            Log.d(TAG, "retrievemenuItems: " + string);
        }
        db.collection("Stores").document("Prata-Boy").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot ds = task.getResult();
                if (ds.exists()) {
                    ArrayList<HashMap<String, Object>> menuItems = (ArrayList<HashMap<String, Object>>) ds.get("menuItems");
                    if (menuItems != null) {
                        for (HashMap<String, Object> item : menuItems) {
                            String name = (String) item.get("itemName");
                            String itemId = (String) item.get("itemID");
                            String description = (String) item.get("itemDescription");
                            Object obj = item.get("itemPrice");

                            double price;
                            if (obj instanceof Double) {
                                price = (Double) obj;
                            } else if (obj instanceof Long) {
                                price = ((Long) obj).doubleValue();
                            } else {
                                continue;
                            }

                            Item currentItem = new Item(itemId, name, description, price);


                            for (String id : reccomendations){
                                if (id.equals(currentItem.itemId)){
                                    itemList.add(currentItem);
                                    Log.d(TAG, "onComplete: " + id);
                                }

                            }


                        }
                        menuAdapter.notifyDataSetChanged();
                        setReccomendedList();

                    }
                } else {
                    Log.d(TAG, "The document does not exist");
                }
            }
        });
    }
    public void orderHistoryCount(FirebaseFirestore db, OnOrderHistoryListener listener) {
        String email = loadEmailFromSharedPreferences();
        final float[] orderCounts = new float[30]; // Assuming there are 30 items (P01 to P15 repeated)

        db.collection("Customer").document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> orderHistory = (List<Map<String, Object>>) document.get("order history");
                    if (orderHistory != null) {
                        for (Map<String, Object> order : orderHistory) {
                            List<String> orderIDs = (List<String>) order.get("items"); // Adjust based on your structure
                            if (orderIDs != null) {
                                for (int i = 0; i < 30; i++) { // Iterate through all 30 indices
                                    for (String orderId : orderIDs) {
                                        String itemId = "P" + String.format("%02d", i % 15 + 1); // P01 to P15 repeated
                                        if (itemId.equals(orderId)) {
                                            orderCounts[i]++;

                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }

            // Notify listener with orderCounts when Firestore query completes
            if (listener != null) {
                listener.onOrderHistoryLoaded(orderCounts);
            }
        });
    }

    // Interface to handle order history loaded event
    public interface OnOrderHistoryListener {
        void onOrderHistoryLoaded(float[] orderCounts);
    }




}

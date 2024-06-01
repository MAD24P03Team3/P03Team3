package sg.edu.np.mad.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerViewItem;
    private ItemAdapter itemAdapter;
    private ArrayList<Item> itemList = new ArrayList<>();

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

        recyclerViewItem = view.findViewById(R.id.horizontalRV);
        itemAdapter = new ItemAdapter(itemList);
        LinearLayoutManager itemLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewItem.setLayoutManager(itemLayoutManager);
        recyclerViewItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItem.setAdapter(itemAdapter);

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

        retrievemenuItems(FirebaseFirestore.getInstance());

        return view;
    }

    public void retrievemenuItems(FirebaseFirestore db) {
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
                            itemList.add(currentItem);
                        }
                        itemAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "The document does not exist");
                }
            }
        });
    }
}

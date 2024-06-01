package sg.edu.np.mad.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnerHomepage extends Fragment {
    private static final String PREFS_NAME = "owner";
    private static final String KEY_NAME = "email";

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }
    private List<Item> orderList;
    private RecyclerView recyclerView;
    private Owner_Order_Adapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public OwnerHomepage() {
        // Required empty public constructor
    }

    public static OwnerHomepage newInstance() {
        return new OwnerHomepage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // You can perform initial setup here if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the order list and adapter
        orderList = new ArrayList<>();
        adapter = new Owner_Order_Adapter(orderList);
        recyclerView.setAdapter(adapter);

        // Retrieve email from SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString(KEY_NAME, null);

        if (email != null) {
            db = FirebaseFirestore.getInstance();
            fetchStoreNameAndOrders(email);
        }
    }

    private void fetchStoreNameAndOrders(String email) {
        db.collection("Owner").document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String storeName = document.getString("storeName");
                    fetchOrders(storeName);
                } else {
                    // Handle the case where the owner document does not exist
                }
            } else {
                // Handle task failure
            }
        });
    }

    private void fetchOrders(String storeName) {
        db.collection("Stores").document(storeName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Get the list of current orders (array of itemIDs)
                    List<String> currentOrders = (List<String>) document.get("currentOrders");

                    // Get the list of menu items (array of maps containing item details)
                    List<Map<String, Object>> menuItems = (List<Map<String, Object>>) document.get("menuItems");

                    // If either currentOrders or menuItems is null, return
                    if (currentOrders == null || menuItems == null) {
                        return;
                    }

                    // Iterate through each order in currentOrders
                    for (String itemID : currentOrders) {
                        // Iterate through each menu item to find a match
                        for (Map<String, Object> menuItem : menuItems) {
                            // Check if the itemID matches any item in menuItems
                            if (menuItem.get("itemID").equals(itemID)) {
                                // If match found, create an Item object and add it to the orderList
                                String name = (String) menuItem.get("itemName");
                                String description = (String) menuItem.get("itemDescription");
                                double price = (double) menuItem.get("itemPrice");
                                orderList.add(new Item(itemID, name, description, price));
                                break; // Exit inner loop since match found
                            }
                        }
                    }

                    // Notify the adapter after all items are added
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case where the store document does not exist
                }
            } else {
                // Handle task failure
            }
        });
    }


}

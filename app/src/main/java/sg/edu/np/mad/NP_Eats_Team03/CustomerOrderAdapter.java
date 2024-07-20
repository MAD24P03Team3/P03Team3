package sg.edu.np.mad.NP_Eats_Team03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.CustomerOrderViewHolder> {
    private List<CustomerOrder> customerOrderList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CustomerOrderAdapter(List<CustomerOrder> customerOrderList) {
        this.customerOrderList = customerOrderList;
    }

    @NonNull
    @Override
    public CustomerOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_customer_orders, parent, false);
        return new CustomerOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderViewHolder holder, int position) {
        CustomerOrder currentCustomerOrder = customerOrderList.get(position);
        holder.customerEmail.setText(currentCustomerOrder.email);

        // Setup inner RecyclerView for orders
        Owner_Order_Adapter orderAdapter = new Owner_Order_Adapter(currentCustomerOrder.orders);
        holder.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewOrders.setAdapter(orderAdapter);

        // Set the click listener for the "Completed" button
        holder.completeButton.setOnClickListener(v -> {
            addOrderToHistory(currentCustomerOrder.email, currentCustomerOrder.orders);
            // Remove the completed order from the local list
            customerOrderList.remove(position);
            // Notify adapter that item has been removed
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return customerOrderList.size();
    }

    public static class CustomerOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView customerEmail;
        public RecyclerView recyclerViewOrders;
        public Button completeButton;

        public CustomerOrderViewHolder(View itemView) {
            super(itemView);
            customerEmail = itemView.findViewById(R.id.customerEmail);
            recyclerViewOrders = itemView.findViewById(R.id.recycler_orders);
            completeButton = itemView.findViewById(R.id.completeButton);
        }
    }

    private void addOrderToHistory(String email, List<Item> orders) {
        db.collection("Customer").document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> orderHistory = (List<Map<String, Object>>) document.get("order history");
                    if (orderHistory == null) {
                        orderHistory = new ArrayList<>();
                    }

                    Map<String, Object> newOrder = new HashMap<>();
                    List<String> orderIDs = new ArrayList<>();
                    for (Item item : orders) {
                        orderIDs.add(item.itemId);
                    }
                    newOrder.put("items", orderIDs);
                    orderHistory.add(newOrder);

                    db.collection("Customer").document(email)
                            .update("order history", orderHistory)
                            .addOnSuccessListener(aVoid -> {
                                removeOrderFromCurrentOrders(email);
                            })
                            .addOnFailureListener(e -> {
                                // Failed to add order to history
                            });
                } else {
                    // Customer document does not exist
                }
            } else {
                // Failed to retrieve customer document
            }
        });
    }

    private void removeOrderFromCurrentOrders(String email) {
        db.collection("Stores").document("Prata-Boy").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> currentOrders = (List<Map<String, Object>>) document.get("currentOrders");
                    if (currentOrders != null) {
                        for (int i = 0; i < currentOrders.size(); i++) {
                            Map<String, Object> order = currentOrders.get(i);
                            if (order.containsKey(email)) {
                                currentOrders.remove(i);
                                break;
                            }
                        }

                        db.collection("Stores").document("Prata-Boy")
                                .update("currentOrders", currentOrders)
                                .addOnSuccessListener(aVoid -> {
                                    // Order removed from current orders successfully
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to remove order from current orders
                                });
                    }
                } else {
                    // Store document does not exist
                }
            } else {
                // Failed to retrieve store document
            }
        });
    }
}

package sg.edu.np.mad.NP_Eats_Team03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.CustomerOrderViewHolder> {
    private List<CustomerOrder> customerOrderList;

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
    }

    @Override
    public int getItemCount() {
        return customerOrderList.size();
    }

    public static class CustomerOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView customerEmail;
        public RecyclerView recyclerViewOrders;

        public CustomerOrderViewHolder(View itemView) {
            super(itemView);
            customerEmail = itemView.findViewById(R.id.customerEmail);
            recyclerViewOrders = itemView.findViewById(R.id.recycler_orders);
        }
    }
}

package sg.edu.np.mad.NP_Eats_Team03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.NP_Eats_Team03.R;

public class Owner_Order_Adapter extends RecyclerView.Adapter<Owner_Order_Adapter.OrderViewHolder> {
    private List<Item> orderList;

    public Owner_Order_Adapter(List<Item> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_owner_homepage, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Item currentItem = orderList.get(position);
        holder.tvTitle.setText(currentItem.name);
        holder.tvDesc.setText(currentItem.description);
        holder.tvPrice.setText(String.format("$%.2f",currentItem.price));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvTitle;
        public TextView tvDesc;
        public TextView tvPrice;

        public OrderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}

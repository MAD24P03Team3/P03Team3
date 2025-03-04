package sg.edu.np.mad.NP_Eats_Team03;
import sg.edu.np.mad.NP_Eats_Team03.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<Item> itemList;

    public ItemAdapter(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemName.setText(item.name);
        holder.itemDescription.setText(item.description);
        holder.itemPrice.setText(String.format("$%.2f",item.price));

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = getAdapterPosition();
                //if (position != RecyclerView.NO_POSITION) {
                //    onItemAddListener.onItemAdd(menuData.get(position));
                //}
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDescription;
        TextView itemPrice;
        MaterialButton addButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvTitle);
            itemDescription = itemView.findViewById(R.id.tvDesc);
            itemPrice = itemView.findViewById(R.id.tvPrice);
            addButton = itemView.findViewById(R.id.Add);
        }
    }

    // Define a callback interface
    public interface OnAddButtonClickListener {
        void onAddButtonClick(Item item);
    }

    private OnAddButtonClickListener listener;

    public void setOnAddButtonClickListener(OnAddButtonClickListener listener) {
        this.listener = listener;
    }
}

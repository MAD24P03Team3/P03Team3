package sg.edu.np.mad.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<Item> itemArrayList;
    public CartAdapter(ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
    }

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_cart, parent, false);
        return new CartAdapter.CartViewHolder(item);
    }

    public void onBindViewHolder(CartViewHolder holder, int position) {
        Item item = itemArrayList.get(position);
        holder.contentName.setText(item.name);
    }

    public int getItemCount() { return itemArrayList.size(); }
    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView contentName;
        public CartViewHolder(View itemView) {
            super(itemView);
            contentName = itemView.findViewById(R.id.contentName);
        }
    }
}

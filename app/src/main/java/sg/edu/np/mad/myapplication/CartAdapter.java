package sg.edu.np.mad.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<Item> cart;
    Item item;

    public CartAdapter(ArrayList<Item> input_cart) {
        cart = input_cart;
    }

    //OnCartUpdatedListener onCartUpdatedListener;
    //OnCartDeletedListener onCartDeletedListener;

    /*public interface OnCartUpdatedListener {
        void onCartUpdated(ArrayList<Item> cartList);
    }

    public CartAdapter(OnCartUpdatedListener listener) {
        this.onCartUpdatedListener = listener;
    }*/

    /*public CartAdapter(OnCartDeletedListener listener) {
        this.onCartDeletedListener = listener;
    }

    public interface OnCartDeletedListener {
        void onCartDeleted(ArrayList<Item> cartList);
    }*/

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_cart, parent, false);
        return new CartAdapter.CartViewHolder(item);
    }

    public void onBindViewHolder(CartViewHolder holder, int position) {
        item = cart.get(position);
        holder.contentName.setText(item.name);
        holder.contentDetails.setText(item.description);
        holder.contentPrice.setText("$" + item.price);
    }

    public int getItemCount() { return cart.size(); }

    public ArrayList<Item> getCartList() {
        return cart;
    }

    /*public void updateCartList(ArrayList<Item> new_cartList) {
        cart.clear();
        cart.addAll(new_cartList);
        notifyDataSetChanged();
        if (onCartUpdatedListener != null) {
            onCartUpdatedListener.onCartUpdated(cart);
        }
    }*/

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contentName;
        TextView contentDetails;
        TextView contentPrice;
        ImageButton contentDelete;
        public CartViewHolder(View itemView) {
            super(itemView);
            contentName = itemView.findViewById(R.id.contentName);
            contentDetails = itemView.findViewById(R.id.contentDetails);
            contentPrice = itemView.findViewById(R.id.contentPrice);
            contentDelete = itemView.findViewById(R.id.contentDelete);

            contentDelete.setOnClickListener(this);
        }

        public void onClick(View v){
            if (v == contentDelete) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    cart.remove(position);
                    notifyItemRemoved(position);
                }
            }
        }
    }
}
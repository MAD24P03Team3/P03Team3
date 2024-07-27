package sg.edu.np.mad.NP_Eats_Team03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Item> cart;
    private OnItemDeleteListener onItemDeleteListener;
    private OnQuantityChangeListener onQuantityChangeListener;


    public interface OnItemDeleteListener {
        void onItemDelete(int position);
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int quantity);
    }

    public CartAdapter(ArrayList<Item> input_cart, OnItemDeleteListener input_onItemDeleteListener, OnQuantityChangeListener input_onQuantityChangeListener){
        cart = input_cart;
        onItemDeleteListener = input_onItemDeleteListener;
        onQuantityChangeListener = input_onQuantityChangeListener;
    }

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cart, parent, false);
        return new CartViewHolder(view, onItemDeleteListener);
    }

    public void onBindViewHolder(CartViewHolder holder, int position) {
        Item item = cart.get(position);
        //holder.contentImage.setImageDrawable(item.);
        holder.contentName.setText(item.name);
        holder.contentDetails.setText(item.description);
        holder.contentPrice.setText("$" + (item.price * item.quantity));
        holder.quantityText.setText(String.valueOf(item.quantity));
    }

    public int getItemCount() { return cart.size(); }

    public void updateCart(ArrayList<Item> input_cart) {
        cart = input_cart;
        notifyDataSetChanged();
    }

    //public ArrayList<Item> getCartItems() { return cart; }

    /*public void updateCartList(ArrayList<Item> new_cartList) {
        cart.clear();
        cart.addAll(new_cartList);
        notifyDataSetChanged();
        if (onCartUpdatedListener != null) {
            onCartUpdatedListener.onCartUpdated(cart);
        }
    }*/

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView contentImage;
        TextView contentName;
        TextView contentDetails;
        TextView contentPrice;
        ImageButton contentDelete;
        ImageButton increaseQuantity;
        ImageButton decreaseQuantity;
        TextView quantityText;

        public CartViewHolder(View itemView, final OnItemDeleteListener onItemDeleteListener) {
            super(itemView);
            contentImage = itemView.findViewById(R.id.contentImage);
            contentName = itemView.findViewById(R.id.contentName);
            contentDetails = itemView.findViewById(R.id.contentDetails);
            contentPrice = itemView.findViewById(R.id.contentPrice);
            contentDelete = itemView.findViewById(R.id.contentDelete);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decreaseQuantity);
            quantityText = itemView.findViewById(R.id.quantityText);

            contentDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    if (onItemDeleteListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemDeleteListener.onItemDelete(position);
                        }
                    }
                }
            });

            increaseQuantity.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (onQuantityChangeListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            int currentQuantity = Integer.parseInt(quantityText.getText().toString());
                            onQuantityChangeListener.onQuantityChange(position, currentQuantity + 1);
                        }
                    }
                }
            });

            decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (onQuantityChangeListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            int currentQuantity = Integer.parseInt(quantityText.getText().toString());
                            if (currentQuantity > 1) {
                                onQuantityChangeListener.onQuantityChange(position, currentQuantity - 1);
                            }
                        }
                    }
                }
            });
        }
    }
}
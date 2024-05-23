package sg.edu.np.mad.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    ArrayList<String> data;
    public CartAdapter(ArrayList<String> data) {
        this.data = data;
    }

    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_cart, parent, false);
        return new CartAdapter.CartViewHolder(item);
    }

    public void onBindViewHolder(CartViewHolder holder, int position) {
        String store = data.get(position);
        holder.dataName.setText(store);
    }

    public int getItemCount() { return data.size(); }
    public class CartViewHolder extends RecyclerView.ViewHolder{
        TextView dataName;
        public CartViewHolder(View itemView) {
            super(itemView);
            dataName = itemView.findViewById(R.id.contentName);
        }
    }
}

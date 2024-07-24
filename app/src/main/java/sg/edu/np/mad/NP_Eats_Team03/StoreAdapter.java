package sg.edu.np.mad.NP_Eats_Team03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    ArrayList<String> data;
    public StoreAdapter(ArrayList<String> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_store, null, false);
        return new StoreViewHolder(item);
    }
    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        String store = data.get(position);
        holder.tvTitle.setText(store);
    }
    @Override
    public int getItemCount() { return data.size(); }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}

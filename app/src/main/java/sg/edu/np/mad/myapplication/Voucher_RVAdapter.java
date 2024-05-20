package sg.edu.np.mad.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class VoucherRVAdapter extends RecyclerView.Adapter<VoucherRVAdapter.MyHolder> {
    ArrayList<String> data;
    public VoucherRVAdapter(ArrayList<String> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, null, false);
        return new MyHolder(itemview);
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvTitle.setText(data.get(position));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}

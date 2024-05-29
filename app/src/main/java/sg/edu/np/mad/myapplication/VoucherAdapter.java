package sg.edu.np.mad.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherViewHolder> {
    private ArrayList<String> data;
    public VoucherAdapter(ArrayList<String> input_data) {
        data = input_data;
    }

    @Override
    @NonNull
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_voucher, null, false);
        return new VoucherViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        String voucher = data.get(position);
        holder.voucherDesc.setText(voucher);
    }

    @Override
    public int getItemCount() { return data.size(); }



}
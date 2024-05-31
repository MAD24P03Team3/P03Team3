package sg.edu.np.mad.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>{

    private ArrayList<Voucher> voucherArrayList;
    public VoucherAdapter(ArrayList<Voucher> input_data) {
        voucherArrayList = input_data;
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
        Voucher voucher = voucherArrayList.get(position);
        holder.voucherName.setText(voucher.voucherID);
        holder.voucherDesc.setText(voucher.description);
    }

    @Override
    public int getItemCount() { return voucherArrayList.size(); }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView voucherName;
        TextView voucherDesc;
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherName = itemView.findViewById(R.id.voucherName);
            voucherDesc = itemView.findViewById(R.id.voucherDescription);

            //TODO
            itemView.findViewById(R.id.elevatedButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Voucher button", "Voucher button onClick: ");
                }
                //Customer currentCustomer = Customer.getCurrrentCustomer();
            });
        }
    }
}
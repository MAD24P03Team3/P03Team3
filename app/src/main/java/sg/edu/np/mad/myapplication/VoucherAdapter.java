package sg.edu.np.mad.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>{

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    private Context context;
    private ArrayList<Voucher> voucherArrayList;

    public VoucherAdapter(Context context, ArrayList<Voucher> input_data) {
        this.context = context;
        this.voucherArrayList = input_data;
    }

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
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
        holder.voucherName.setText(voucher.voucherName);
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

            itemView.findViewById(R.id.elevatedButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String customerEmail = loadEmailFromSharedPreferences();
                    Log.d("Voucher Adapter", "Customer email: " + customerEmail);
                    Log.d("Voucher button", "Voucher button onClick: ");
                }
            });
        }
    }
}

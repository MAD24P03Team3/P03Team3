package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyVoucherAdapter extends RecyclerView.Adapter<MyVoucherAdapter.VoucherViewHolder> {

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    private Context context;
    ArrayList<String> vouchers;
    private ArrayList<Voucher> voucherArrayList;
    private TextView pointsTextView; // Reference to pointsTextView in RewardsFragment

    public MyVoucherAdapter(ArrayList<Voucher> voucherArrayList) {
        this.voucherArrayList = voucherArrayList;
    }

    public MyVoucherAdapter(Context context, ArrayList<Voucher> input_data) {
        this.context = context;
        this.voucherArrayList = input_data;
    }

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }

    @Override
    @NonNull
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.recycler_myrewards, parent, false);
        return new VoucherViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = voucherArrayList.get(position);
        holder.bind(voucher);
    }

    @Override
    public int getItemCount() {
        return voucherArrayList.size();
    }

    public class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView voucherName;
        TextView voucherDesc;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            voucherName = itemView.findViewById(R.id.voucherName);
            voucherDesc = itemView.findViewById(R.id.voucherDescription);
        }

        public void bind(Voucher voucher) {
            voucherName.setText(voucher.voucherName);
            voucherDesc.setText(voucher.description);

            String customerEmail = loadEmailFromSharedPreferences();
            Log.d("Voucher Adapter", "Customer email: " + customerEmail);

            retrieveCustomerVouchers(customerEmail, voucher);
        }

        private void retrieveCustomerVouchers(String email, Voucher voucher) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Customer").document(email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> customerData = document.getData();
                        if (customerData != null) {
                            vouchers = (ArrayList<String>) customerData.get("vouchers");

                            if (vouchers != null) {
                                // Log the retrieved vouchers list
                                Log.d("Firestore Update", "Vouchers for email " + email + ": " + vouchers.toString());
                            } else {
                                Log.d("Firestore Update", "No vouchers found for email: " + email);
                            }
                        } else {
                            Log.d("Firestore Update", "Document data is null for email: " + email);
                        }
                    } else {
                        Log.d("Firestore Update", "Document not found for email: " + email);
                    }
                } else {
                    Log.d("Firestore Update", "Get failed with ", task.getException());
                }
            });
        }

        private void updatePointsTextView(int points) {
            // Update pointsTextView with new points value
            pointsTextView.setText(String.valueOf(points));
        }
    }
}

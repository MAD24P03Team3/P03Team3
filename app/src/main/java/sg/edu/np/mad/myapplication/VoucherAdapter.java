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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

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

            itemView.findViewById(R.id.elevatedButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String customerEmail = loadEmailFromSharedPreferences();
                    Log.d("Voucher Adapter", "Customer email: " + customerEmail);
                    Log.d("Voucher button", "Voucher button onClick: " + voucher.voucherID);

                    updateCustomerVouchers(customerEmail, voucher);
                }
            });
        }

        private void updateCustomerVouchers(String email, Voucher voucher) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Customer").document(email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> customerData = document.getData();
                        if (customerData != null) {
                            ArrayList<String> vouchers = (ArrayList<String>) customerData.get("vouchers");
                            int points = ((Long) customerData.get("points")).intValue();

                            if (vouchers == null) {
                                vouchers = new ArrayList<>();
                            }

                            // Add the new voucherID to the vouchers list
                            vouchers.add(voucher.voucherID);
                            Log.d("Vouchers", "Vouchers: " + voucher.voucherID);


                            // Deduct the points by the voucher points
                            points -= voucher.points;
                            Log.d("points", "points: " + points);


                            // Update the customer data
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("vouchers", vouchers);
                            updateData.put("points", points);

                            db.collection("Customer").document(email)
                                    .update(updateData)
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore Update", "DocumentSnapshot successfully updated!"))
                                    .addOnFailureListener(e -> Log.w("Firestore Update", "Error updating document", e));
                        }
                    } else {
                        Log.d("Firestore Update", "Document not found for email: " + email);
                    }
                } else {
                    Log.d("Firestore Update", "Get failed with ", task.getException());
                }
            });
        }

    }
}

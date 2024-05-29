package sg.edu.np.mad.myapplication;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Voucher {
    String storeID;
    String voucherID;
    double discount;
    Date valid;
    Date expiry;
    String description;

    public Voucher(String input_storeID, String input_voucherID, double input_discount, Date input_valid, Date input_expiry, String input_description){
        storeID = input_storeID;
        voucherID = input_voucherID;
        discount = input_discount;
        valid = input_valid;
        expiry = input_expiry;
        description = input_description;
    }

    public static Task<ArrayList<Voucher>> RetrieveVouchers(FirebaseFirestore db) {
        TaskCompletionSource<ArrayList<Voucher>> taskCompletionSource = new TaskCompletionSource<>();

        // Get a reference to the "Voucher" collection
        CollectionReference vouchersRef = db.collection("Voucher");

        vouchersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Voucher> vouchers = new ArrayList<>();
                Log.d("RetrieveVouchers", "Start Getting documents");
                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        Map<String, Object> voucherDetails = document.getData();
                        String storeID = (String) voucherDetails.get("storeID");
                        String voucherId = (String) voucherDetails.get("voucherID");

                        Number discountNumber = (Number) voucherDetails.get("discount");
                        Double discount = discountNumber != null ? discountNumber.doubleValue() : null;

                        Timestamp validityTimestamp = (Timestamp) voucherDetails.get("validity");
                        Timestamp expireTimestamp = (Timestamp) voucherDetails.get("expire");
                        Date validityDate = validityTimestamp != null ? validityTimestamp.toDate() : null;
                        Date expireDate = expireTimestamp != null ? expireTimestamp.toDate() : null;

                        String description = (String) voucherDetails.get("description");

                        vouchers.add(new Voucher(storeID, voucherId, discount, validityDate, expireDate, description));
                        Log.d("RetrieveVouchers", "Added voucher: " + voucherId);
                    } catch (Exception e) {
                        Log.e("RetrieveVouchers", "Error processing document: " + document.getId(), e);
                    }
                }
                taskCompletionSource.setResult(vouchers);
            } else {
                Log.w("RetrieveVouchers", "Error getting documents: ", task.getException());
                taskCompletionSource.setResult(new ArrayList<>()); // Return empty list on error
            }
        });

        return taskCompletionSource.getTask();
    }
}

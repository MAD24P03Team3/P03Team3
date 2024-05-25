package sg.edu.np.mad.myapplication;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class RetrieveVouchers {

    public static Task<ArrayList<Voucher>> retrieveVouchers(FirebaseFirestore db) {
        TaskCompletionSource<ArrayList<Voucher>> taskCompletionSource = new TaskCompletionSource<>();

        // Get a reference to the "Voucher" collection
        CollectionReference vouchersRef = db.collection("Voucher");

        vouchersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Voucher> vouchers = new ArrayList<>();
                Log.w("RetrieveVouchers", "Start Getting documents: ", task.getException());
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> voucherDetails = document.getData();
                    String storeID = (String) voucherDetails.get("storeID");
                    String voucherId = (String) voucherDetails.get("voucherID");
                    Double discount = (Double) voucherDetails.get("discount");
                    Date validityString = (Date) voucherDetails.get("validity");
                    Date expireString = (Date) voucherDetails.get("expire");
                    String description = (String) voucherDetails.get("description");
                    vouchers.add(new Voucher(storeID, voucherId, discount, validityString, expireString, description));
                    Log.w("RetrieveVouchers", "Getting documents: ", task.getException());

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

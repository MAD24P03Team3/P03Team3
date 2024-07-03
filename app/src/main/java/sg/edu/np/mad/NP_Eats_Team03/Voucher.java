package sg.edu.np.mad.NP_Eats_Team03;

import android.os.Parcel;
import android.os.Parcelable;
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

public class Voucher implements Parcelable {
    String storeID;
    String voucherID;
    String voucherName;
    double points;
    double discount;
    Date valid;
    Date expiry;
    String description;

    public Voucher(String input_storeID, String input_voucherID, String Name, double inputPoints, double input_discount, Date input_valid, Date input_expiry, String input_description){
        storeID = input_storeID;
        voucherID = input_voucherID;
        voucherName = Name;
        points = inputPoints;
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
                        String voucherName = (String) voucherDetails.get("name");

                        Number pointsNumber = (Number) voucherDetails.get("points");
                        Double points = pointsNumber != null ? pointsNumber.doubleValue() : null;


                        Number discountNumber = (Number) voucherDetails.get("discount");
                        Double discount = discountNumber != null ? discountNumber.doubleValue() : null;

                        Timestamp validityTimestamp = (Timestamp) voucherDetails.get("validity");
                        Timestamp expireTimestamp = (Timestamp) voucherDetails.get("expire");
                        Date validityDate = validityTimestamp != null ? validityTimestamp.toDate() : null;
                        Date expireDate = expireTimestamp != null ? expireTimestamp.toDate() : null;

                        String description = (String) voucherDetails.get("description");

                        vouchers.add(new Voucher(storeID, voucherId, voucherName, points, discount,  validityDate, expireDate, description));
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
    protected Voucher(Parcel in) {
        storeID = in.readString();
        voucherID = in.readString();
        voucherName = in.readString();
        points = in.readDouble();
        discount = in.readDouble();
        valid = new Date(in.readLong());
        expiry = new Date(in.readLong());
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(storeID);
        dest.writeString(voucherID);
        dest.writeString(voucherName);
        dest.writeDouble(points);
        dest.writeDouble(discount);
        dest.writeLong(valid != null ? valid.getTime() : -1);
        dest.writeLong(expiry != null ? expiry.getTime() : -1);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };
}

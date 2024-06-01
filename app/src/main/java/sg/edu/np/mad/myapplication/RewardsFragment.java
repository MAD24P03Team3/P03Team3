package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RewardsFragment extends Fragment {
    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    private FirebaseFirestore db;
    private TextView pointsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        pointsTextView = view.findViewById(R.id.points);

        ArrayList<Voucher> recycler_VoucherList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_items);
        VoucherAdapter voucherAdapter = new VoucherAdapter(getContext(), recycler_VoucherList);
        LinearLayoutManager voucherLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(voucherLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(voucherAdapter);

        db = FirebaseFirestore.getInstance();

        Voucher.RetrieveVouchers(db).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.w("RewardsActivity", "Retrieved vouchers", task.getException());
                ArrayList<Voucher> vouchers = task.getResult();
                for (Voucher voucher : vouchers) {
                    recycler_VoucherList.add(voucher);
                }
                voucherAdapter.notifyDataSetChanged();
            }
        });

        loadCustomerPointsFromDatabase();

        return view;
    }

    private void loadCustomerPointsFromDatabase() {
        String customerEmail = loadEmailFromSharedPreferences();
        db.collection("customer").document(customerEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long points = document.getLong("points");
                    if (points != null) {
                        pointsTextView.setText(String.valueOf(points));
                    }
                } else {
                    Log.d("Firestore", "No such document");
                }
            } else {
                Log.d("Firestore", "Get failed with ", task.getException());
            }
        });
    }

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No email found");
    }
}

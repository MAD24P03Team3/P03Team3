package sg.edu.np.mad.NP_Eats_Team03;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Map;

import sg.edu.np.mad.NP_Eats_Team03.R;

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
        VoucherAdapter voucherAdapter = new VoucherAdapter(getContext(), recycler_VoucherList, pointsTextView, new VoucherAdapter.OnPointsRedeemListener() {
            @Override
            public void onPointsRedeem() {
                updateCustomerPoints(loadEmailFromSharedPreferences());
            }
        });
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

        view.findViewById(R.id.rewardsButton).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CustomerMyRewards.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void loadCustomerPointsFromDatabase() {
        String customerEmail = loadEmailFromSharedPreferences();
        db.collection("Customer").document(customerEmail).get().addOnCompleteListener(task -> {
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

    private void updateCustomerPoints(String email) {
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

                        pointsTextView.setText(String.valueOf(points));
                    }
                }
            }
        });
    }
}

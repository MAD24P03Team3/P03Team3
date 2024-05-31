package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RewardsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.fragment_rewards, container, false);

        Date validDate = new Date(2024, Calendar.MAY, 1);
        Date expiryDate = new Date(2025, Calendar.MAY, 1);
        ArrayList<Voucher> recycler_VoucherList = new ArrayList<>();
        recycler_VoucherList.add(new Voucher("001", "P00004", 2.0, validDate, expiryDate, "Come to prata boy to use this voucher for $2 off any purchase."));
        recycler_VoucherList.add(new Voucher("002", "P00005", 5.0, validDate, expiryDate, "Come to prata boy to use this voucher for $5 off any purchase."));
        recycler_VoucherList.add(new Voucher("003", "P00006", 10.0, validDate, expiryDate, "Come to prata boy to use this voucher for $10 off any purchase."));

        RecyclerView recyclerView = View.findViewById(R.id.recycler_items);
        VoucherAdapter VoucherAdapter = new VoucherAdapter(recycler_VoucherList);
        LinearLayoutManager voucherLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(voucherLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(VoucherAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Voucher.RetrieveVouchers(db).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.w("RewardsActivity", "Retrieved vouchers", task.getException());
                ArrayList<Voucher> vouchers = task.getResult();
                for (Voucher voucher : vouchers) {
                    recycler_VoucherList.add(voucher);
                }
                VoucherAdapter.notifyDataSetChanged();
            }
        });
        return View;
    }
}
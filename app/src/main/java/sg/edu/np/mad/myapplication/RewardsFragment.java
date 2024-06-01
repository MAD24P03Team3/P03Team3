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

        ArrayList<Voucher> recycler_VoucherList = new ArrayList<>();

        RecyclerView recyclerView = View.findViewById(R.id.recycler_items);
        VoucherAdapter VoucherAdapter = new VoucherAdapter(getContext(), recycler_VoucherList);
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
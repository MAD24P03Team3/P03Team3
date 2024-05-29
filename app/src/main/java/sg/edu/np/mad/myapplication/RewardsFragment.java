package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        recycler_VoucherList.add(new Voucher("P000001", "001", 2.0, validDate, expiryDate, "Come to prata boy to use this voucher for $2 off any purchase."));
        recycler_VoucherList.add(new Voucher("P000002", "002", 5.0, validDate, expiryDate, "Come to prata boy to use this voucher for $5 off any purchase."));
        recycler_VoucherList.add(new Voucher("P000003", "003", 10.0, validDate, expiryDate, "Come to prata boy to use this voucher for $10 off any purchase."));

        RecyclerView recyclerView = View.findViewById(R.id.recycler_items);
        VoucherAdapter VoucherAdapter = new VoucherAdapter(recycler_VoucherList);
        LinearLayoutManager voucherLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(voucherLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(VoucherAdapter);

        return View;
    }
}
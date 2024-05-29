package sg.edu.np.mad.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RewardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rewards);

        ArrayList<String> voucherName = new ArrayList<>();

        /*RecyclerView recyclerView = findViewById(R.id.recycler_items);
        VoucherAdapter rv_Items_Adapter = new VoucherAdapter(voucherName);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rv_Items_Adapter);*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*RetrieveVouchers.retrieveVouchers(db).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.w("RewardsActivity", "Retrieved vouchers", task.getException());
                ArrayList<Voucher> vouchers = task.getResult();
                for (Voucher voucher : vouchers) {
                    String description = voucher.description;
                    voucherName.add(description);
                    Log.w("RewardsActivity", description);
                }
                rv_Items_Adapter.notifyDataSetChanged();
            } else {

                Log.w("RewardsActivity", "Error retrieving vouchers", task.getException());
            }
        });*/


    }
}

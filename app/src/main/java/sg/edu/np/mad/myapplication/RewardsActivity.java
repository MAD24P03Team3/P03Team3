package sg.edu.np.mad.myapplication;

import android.os.Bundle;

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

        ArrayList<String> voucherData = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        voucherData.add("item 1");
        voucherData.add("item 2");
        voucherData.add("item 3");
        voucherData.add("item 4");

        RecyclerView recyclerView = findViewById(R.id.recycler_items);
        VoucherAdapter rv_Items_Adapter = new VoucherAdapter(voucherData);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rv_Items_Adapter);
    }
}

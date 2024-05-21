package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RewardsViewHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rewards);
        RecyclerView recyclerView = findViewById(R.id.recycler_items);

        ArrayList<String> voucherData = new ArrayList<>();
        voucherData.add("item 1");
        voucherData.add("item 2");
        voucherData.add("item 3");
        voucherData.add("item 4");

        LinearLayoutManager linearLayoutManagerItems = new LinearLayoutManager(RewardsViewHolder.this, LinearLayoutManager.VERTICAL, false);
        VoucherRVAdapter rv_Items_Adapter = new VoucherRVAdapter(voucherData);
        recyclerView.setLayoutManager(linearLayoutManagerItems);
        recyclerView.setAdapter(rv_Items_Adapter);


    }
}

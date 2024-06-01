package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class CustomerMyRewards extends AppCompatActivity {

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";
    private FirebaseFirestore db;

    private ArrayList<Voucher> voucherList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyVoucherAdapter MyVoucherAdapter;
    private TextView pointsTextView;

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No email found");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_my_rewards);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton backButton = findViewById(R.id.rewardsButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerMyRewards.this, MainActivity2.class);
            startActivity(intent);
            finish(); // Optional: Close the current activity so it is removed from the back stack.
        });

        recyclerView = findViewById(R.id.recycler_items);
        db = FirebaseFirestore.getInstance();

        MyVoucherAdapter = new MyVoucherAdapter(this, voucherList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(MyVoucherAdapter);

        loadVouchers();
    }


    private void loadVouchers() {
        String customerEmail = loadEmailFromSharedPreferences();
        db.collection("Customer").document(customerEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long points = document.getLong("points");

                    ArrayList<String> voucherIDs = (ArrayList<String>) document.get("vouchers");
                    if (voucherIDs != null && !voucherIDs.isEmpty()) {
                        db.collection("Vouchers").whereIn("voucherID", voucherIDs).get().addOnCompleteListener(voucherTask -> {
                            if (voucherTask.isSuccessful()) {
                                for (QueryDocumentSnapshot voucherDocument : voucherTask.getResult()) {
                                    String storeID = voucherDocument.getString("storeID");
                                    String voucherID = voucherDocument.getString("voucherID");
                                    String voucherName = voucherDocument.getString("voucherName");
                                    double voucherPoints = voucherDocument.getDouble("points");
                                    double discount = voucherDocument.getDouble("discount");
                                    Date valid = voucherDocument.getDate("valid");
                                    Date expiry = voucherDocument.getDate("expiry");
                                    String description = voucherDocument.getString("description");

                                    Voucher voucher = new Voucher(storeID, voucherID, voucherName, voucherPoints, discount, valid, expiry, description);
                                    voucherList.add(voucher);
                                }
                                MyVoucherAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("Firestore", "Error getting vouchers: ", voucherTask.getException());
                            }
                        });
                    }
                } else {
                    Log.d("Firestore", "No such document");
                }
            } else {
                Log.d("Firestore", "Get failed with ", task.getException());
            }
        });
    }
}

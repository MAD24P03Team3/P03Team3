package sg.edu.np.mad.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import sg.edu.np.mad.myapplication.databinding.ActivityMainBinding;

public  class MainActivity extends AppCompatActivity {
    //product items recycler view
    RecyclerView rv_Items;
    ArrayList<String> rv_Items_Data;
    LinearLayoutManager linearLayoutManagerItems;
    RVAdapter rv_Items_Adapter;

    //store list recycler view
    RecyclerView rv_Store;
    ArrayList<String> rv_Store_Data;
    LinearLayoutManager linearLayoutManagerStore;
    StoreRVAdapter rv_Store_Adapter;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*
        bottomNavView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.page_1://account
                    Intent Page1 = new Intent(Account.this,main_activity.class);
                    startActivity(Page1);
                    break;
                case R.id.page_2://menu
                    Intent Page1 = new Intent(Menu.this,main_activity.class);
                    startActivity(Page1);
                    break;
                case R.id.page_3://home
                    Intent Page1 = new Intent(MainActivity.this,main_activity.class);
                    startActivity(Page1);
                    break;
                case R.id.page_4://rewards
                    Intent Page1 = new Intent(Rewards.this,main_activity.class);
                    startActivity(Page1);
                    break;
                case R.id.page_4://cart
                    Intent Page1 = new Intent(Cart.this,main_activity.class);
                    startActivity(Page1);
                    break;
            };
        });

         */



        //item recycler view
        setContentView(R.layout.activity_main);
        rv_Items = findViewById(R.id.horizontalRV);

        rv_Items_Data = new ArrayList<>();
        rv_Items_Data.add("item 1");
        rv_Items_Data.add("item 2");
        rv_Items_Data.add("item 3");
        rv_Items_Data.add("item 4");

        linearLayoutManagerItems = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_Items_Adapter = new RVAdapter(rv_Items_Data);
        rv_Items.setLayoutManager(linearLayoutManagerItems);
        rv_Items.setAdapter(rv_Items_Adapter);

        //store recycler view
        rv_Store = findViewById(R.id.storelistRV);

        rv_Store_Data = new ArrayList<>();
        rv_Store_Data.add("Prata boy");
        rv_Store_Data.add("Oishii daily");
        rv_Store_Data.add("Coffee connect");
        rv_Store_Data.add("Acai den");

        linearLayoutManagerStore = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_Store_Adapter = new StoreRVAdapter(rv_Store_Data);
        rv_Store.setLayoutManager(linearLayoutManagerStore);
        rv_Store.setAdapter(rv_Store_Adapter);

    }
}

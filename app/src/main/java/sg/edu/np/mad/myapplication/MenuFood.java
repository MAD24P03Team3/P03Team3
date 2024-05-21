package sg.edu.np.mad.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MenuFood extends AppCompatActivity {

    ArrayList<Item> menuData = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Item> menuItems = new ArrayList<Item>();
    private String[] titles = new String[]{"Menu","Drinks","Praffles"};
    ViewPageAdapter viewPageAdapter;
    TabLayout tabLayout;

    ViewPager2 viewPager2;


//    private void makeModel(){
//        String[] menuNames = getResources().getStringArray(R.array.names_of_product);
//        String [] menuDesc = getResources().getStringArray(R.array.desc);
//        int img = R.drawable.img4;
//        for(int i = 0; i<menuNames.length; i++){
//            menuData.add(new menuModel(menuNames[i],menuDesc[i],img));
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foodmenu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;







        });

        //set up the tab layouts

        viewPager2 = findViewById(R.id.view_pageFood);
        tabLayout = findViewById(R.id.tabs);
        viewPageAdapter = new ViewPageAdapter(this);
        viewPager2.setAdapter(viewPageAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> tab.setText(titles[position])).attach();



        Log.d("End of the activity","Success");






    }


}
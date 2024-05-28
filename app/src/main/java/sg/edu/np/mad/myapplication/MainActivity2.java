package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.myapplication.databinding.ActivityMainBinding;
import sg.edu.np.mad.myapplication.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    //ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityMain2Binding binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        //binding.navView

        binding.navView.setOnItemSelectedListener(bottomNavView -> {
            /*switch (bottomNavView.getItemId()) {
                case R.id.navigation_Home:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new HomeFragment())
                            .commit();
                    break;

                case R.id.navigation_Cart:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, new CartFragment())
                            .commit();
                    break;
            }*/
            if (bottomNavView.getItemId() == R.id.navigation_Home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new HomeFragment())
                        .commit();
            }
            if (bottomNavView.getItemId() == R.id.navigation_Cart) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new CartFragment())
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nav_host_fragment, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
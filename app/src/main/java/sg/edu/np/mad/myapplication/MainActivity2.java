package sg.edu.np.mad.myapplication;

import static sg.edu.np.mad.myapplication.R.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.myapplication.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //todo
        //setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = binding.navView;

        /*
        bottomNavigationView.setOnItemSelectedListener(bottomNavView -> {
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
            return false;
        });

         */

        //binding.bot
        bottomNavigationView.setOnItemSelectedListener(bottomNavView -> {
            switch (bottomNavView.getItemId()) {
                case R.id.navigation_Home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.navigation_Menu:
                    replaceFragment(new MenuFragment());
                    break;

                case R.id.navigation_Cart:
                    replaceFragment(new CartFragment());
                    break;

                case R.id.navigation_Rewards:
                    replaceFragment(new RewardsFragment());
                    break;
                case R.id.navigation_Account:
                    replaceFragment(new AccountFragment());
                    break;
                default:
                    return false;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    //.add(R.layout.fragment_cart, container, null)
                    .add(R.id.nav_host_fragment, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

}
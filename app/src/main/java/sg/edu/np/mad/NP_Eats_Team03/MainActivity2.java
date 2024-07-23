package sg.edu.np.mad.NP_Eats_Team03;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import sg.edu.np.mad.NP_Eats_Team03.Navigation.MapBoxRouteHandler;
import sg.edu.np.mad.NP_Eats_Team03.R;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import sg.edu.np.mad.NP_Eats_Team03.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    //ActivityMain2Binding binding;
    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String customerEmail = loadEmailFromSharedPreferences();
        Log.d("MainActivity2", "Customer email: " + customerEmail);


        // Load the customer instance from SharedPreferences
        Customer customer = Customer.loadInstance(this);
        if (customer != null) {
            Log.d("MainActivity2", "Customer name: " + customer.name);
        } else {
            Log.d("MainActivity2", "Customer is null");
        }
        // Retrieve the customer object

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

        binding.navView.setOnItemSelectedListener(bottomNavView -> {
            /*switch (bottomNavView.getItemId()) {
                default:
                    return false;

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
            Fragment switchFragment = null;

            if (bottomNavView.getItemId() == R.id.navigation_Home) {
                switchFragment =  new HomeFragment();
            }
            else if (bottomNavView.getItemId() == R.id.navigation_Menu) {
                switchFragment =  new MenuFragment();
            }
            else if (bottomNavView.getItemId() == R.id.navigation_Cart) {
                switchFragment =  new CartFragment();
            }
            else if (bottomNavView.getItemId() == R.id.navigation_Rewards) {
                switchFragment =  new RewardsFragment();
            }

            else if (bottomNavView.getItemId() == R.id.navigation_Account) {
                switchFragment =  new AccountFragment();
            }


            if (switchFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, switchFragment)
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
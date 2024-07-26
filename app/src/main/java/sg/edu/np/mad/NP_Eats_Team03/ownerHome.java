package sg.edu.np.mad.NP_Eats_Team03;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import sg.edu.np.mad.NP_Eats_Team03.R;

public class ownerHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the bottom nav view
        BottomNavigationView bnv = findViewById(R.id.bottomnavbar);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // begin fragment transaction to replace fragment container with specific fragments
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if(menuItem.getItemId() == R.id.owner_Home){
                    ft.replace(R.id.fragmenu, new OwnerHomepage());
                }
                else if(menuItem.getItemId() == R.id.owner_Menu){
                    ft.replace(R.id.fragmenu, new OwnerMenu());
                }
                else if(menuItem.getItemId() == R.id.owner_Account){
                    ft.replace(R.id.fragmenu, new owner_account());//Todo account
                }

                ft.commit();
                return true;
            }
        });

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmenu, new OwnerHomepage())
                    .commit();
        }
    }
}

package sg.edu.np.mad.NP_Eats_Team03;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.ar.sceneform.Sceneform;
//import com.google.ar.sceneform.ux.ArFragment;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.RouteLeg;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    ArrayList<String> directionList;
    DirectionsAdapter da;

    private static final String KEY_Directions = "Directions";
    private static final String PREFS_NAME = "Directions";
    private String TAG = "NavigationActivity";

//     fix this logic
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ar_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView directionsRecycler = findViewById(R.id.directionRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        directionsRecycler.setLayoutManager(layoutManager);
        retrieveAllDirections(directionsRecycler);


//        try{
//            if(Sceneform.isSupported(this)){
//                // Get the view of the fragment
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                ArFragment arFragment = new NavigationARFragment();
//                fragmentTransaction.add(R.id.arFragment, arFragment);
//                fragmentTransaction.commit();
//                Toast.makeText(this,"Sucessfully loaded arFragment",Toast.LENGTH_SHORT).show();
//
//            }
//        }
//        catch(Error error){
//            Log.d(TAG,"Sceneform is not supported: " + error.getMessage());
//        }



    }

    // Get the view of the recyclerView then inflate
    public void retrieveAllDirections(RecyclerView dr) {
        ArrayList<String> dirList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_Directions, null);
        DirectionsRoute routes = DirectionsRoute.fromJson(json);
        List<RouteLeg> legs = routes.legs();
        for (RouteLeg leg : legs) {
            List<LegStep> steps = leg.steps();
            for (LegStep step : steps) {
                String direction = step.maneuver().instruction();
                Log.d("This is me", direction);
                dirList.add(direction);
            }
        }
        DirectionsAdapter da = new DirectionsAdapter(dirList, this);
        dr.setAdapter(da);
    }
}

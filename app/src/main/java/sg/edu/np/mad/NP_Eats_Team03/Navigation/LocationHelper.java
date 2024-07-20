package sg.edu.np.mad.NP_Eats_Team03.Navigation;

import android.app.Activity;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;

public class LocationHelper {
    Activity activity;
    FusedLocationProviderClient fusedLocationProviderClient;

    public LocationHelper(Activity activity, FusedLocationProviderClient fusedLocationProviderClient) {
        this.activity = activity;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public  getCurrentLocation(CurrentLocationRequest request, CancellationToken cancellationToken){

    }


//   permission requests should be handles in the at runtime inside activity





}

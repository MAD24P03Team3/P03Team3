package sg.edu.np.mad.NP_Eats_Team03.Navigation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationHelper{
    private Context context;

    private String PREFS_NAME = "USER_LOCATION";

    private String KEY_LOCATION = "Location";


    private FusedLocationProviderClient fusedLocationProviderClient;

    public Location currentLocation;

    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    public LocationHelper (Context context, FusedLocationProviderClient fusedLocationProviderClient, Location currentLocation){
        this.context = context;
        this.currentLocation = null;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getCurrentLocation(CurrentLocationRequest request, CancellationToken cancellationToken) {


        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            setCurrentLocation(location);
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("LocationHelper", "Current location: " + latitude + ", " + longitude);
//                          Implement a location callback
                            SharedPreferences sharedPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreference.edit();
                            editor.putString(KEY_LOCATION,latitude + "," + longitude);
                            editor.commit();

                        }
                        Log.w("LocationHelper", "No current location could be found");
                    }
                });
    }

    // call this on onResume()
    private void startLocationUpdates() {
        Task<Void> locationUpdateTask = fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper());
        locationUpdateTask.addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("LocationHelper", "Location update successful");
            }
        });

        locationUpdateTask.addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("LocationHelper", "Location update not sucessful");
            }
        });

    }

    // call this in onPause activity

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public void setCurrentLocation(Location currentLocation) {
        if(currentLocation != null){
            this.currentLocation = currentLocation;
        }

    }

    public Location getCurrentLocation() {
        return currentLocation;
    }



    //   permission requests should be handles in the at runtime inside activity
}





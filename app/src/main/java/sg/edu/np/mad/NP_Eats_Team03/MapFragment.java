package sg.edu.np.mad.NP_Eats_Team03;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import com.mapbox.bindgen.Expected;
import com.mapbox.bindgen.None;
import com.mapbox.maps.AnnotatedFeature;
import com.mapbox.maps.MapLoaded;
import com.mapbox.maps.MapLoadedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraBounds;
import com.mapbox.maps.CameraBoundsOptions;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.CoordinateBounds;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.MapboxStyleManager;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor;
import com.mapbox.maps.plugin.annotation.Annotation;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationManager;
import com.mapbox.maps.plugin.annotation.AnnotationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.delegates.MapDelegateProvider;
import com.mapbox.maps.plugin.delegates.listeners.OnMapLoadedListener;


public class MapFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private MapView mapView;

    private Point [] storeLocations;

    private MapboxMap map;
    private Integer reqCode = 3;
    private Activity activity;

    Point userStart;

    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // Initialize the map
        mapView = view.findViewById(R.id.mapView);
        map = mapView.getMapboxMap();
//      Change the style of the map to dark mode
        map.loadStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
//          create the annotations on the map
//          Intialize a new annotation plugin

            AnnotationPlugin annotationPlugin = mapView.getPlugin("MAPBOX_ANNOTATION_PLUGIN_ID");
            AnnotationConfig ac = new AnnotationConfig();
            PointAnnotationManager pointAnnotationManager = (PointAnnotationManager) annotationPlugin.createAnnotationManager(AnnotationType.PointAnnotation,ac);


//          create a point Annotation
            activity = getActivity();
            if(activity != null){
                InitializePoints(storeLocations,pointAnnotationManager);
            }





            }
        });




        return view;


    }


//    implement the onMapReady interface
//    Handle the different lifecycles of the map (navigation, addedPoint on map, different location)

//    Create an alert dialog informing users that they cannot use the application outside of the specified bounds


    //    Enable location services for the android device
    public boolean checkLocation() {
        return true;
    }

    public boolean askLocationPermission() {
        // init a new variable that stores both the result of the FINE and COARSE PERMISSIONS, Use ternary opertor to check
        boolean resultOfFineLocation = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean resultOfCoarseLocation = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (resultOfCoarseLocation && resultOfFineLocation) {
//            request both permissions since permission not granted
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(activity, permissions, 3);
            return false;
        } else {
            return true;
        }


    }

    //   retrive the current gps of user
    public void lastLocal() {
        askLocationPermission();
        if (askLocationPermission() == true) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object (convert to long lat of user)
                                double locationLongitude = location.getLongitude();
                                double locationLatitude = location.getLatitude();
                                userStart = Point.fromLngLat(locationLongitude,locationLatitude);

                            }
                        }
                    });
        }
    }






   public void InitializePoints(Point [] stores, PointAnnotationManager pa){
//    Test with three stores (PRATA BOY, COFFEE CONNECT AND OISHI)
//       Coffee Connect Address
//       Convert the drawable content to a bitmap
       // issue is bitmap is null
       try{Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.users);

           Point c = Point.fromLngLat(1.3331722062601192, 103.77491355276209);
           Point prata = Point.fromLngLat(1.3335828064696371, 103.7751671051295);
           Point Oishi = Point.fromLngLat(1.3318849730782238, 103.77570807725307);
           stores = new Point[]{c, prata, Oishi};
           if(icon != null){
               for(Point point : stores){
                   PointAnnotationOptions options = new PointAnnotationOptions()
                           .withPoint(point)
                           .withIconImage(icon)
                           .withIconSize(4.0f)
                           .withIconAnchor(IconAnchor.CENTER);

                   pa.create(options);
               }
           }
           else{
               Toast.makeText(getContext(),"Unable to retrive bitmap icon",Toast.LENGTH_SHORT).show();
           }
       }
       catch(Exception e){
           Log.v("Error for bitmap",e.getMessage());
       }





   }
}
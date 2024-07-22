package sg.edu.np.mad.NP_Eats_Team03;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import sg.edu.np.mad.NP_Eats_Team03.Navigation.LocationHelper;
import sg.edu.np.mad.NP_Eats_Team03.Navigation.MapBoxRouteHandler;


public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    private Point [] storeLocations;

    private MapboxMap map;
    private Integer reqCode = 3;

    private CameraOptions co;

    private String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Integer REQ_CODE = 200;

    private Point userLocation;


    private Location userStart;

    private MapBoxRouteHandler routeHandler;

    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        requestPermission();
        routeHandler = new MapBoxRouteHandler(this);
        routeHandler.retrivefromMapBox(this,Point.fromLngLat(103.77491355276209,1.3331722062601192),Point.fromLngLat(103.77570807725307, 1.3318849730782238));





        mapView = findViewById(R.id.mapView);
        map = mapView.getMapboxMap();
//      Change the style of the map to dark mode

        map.loadStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                //create the annotations on the map
//          Intialize a new annotation plugin
                Point origin = Point.fromLngLat(103.772830242,1.333498666);
//              center the coordinates at ngee ann poly
                AnnotationPlugin annotationPlugin = mapView.getPlugin("MAPBOX_ANNOTATION_PLUGIN_ID");
                AnnotationConfig ac = new AnnotationConfig();
                PointAnnotationManager pointAnnotationManager = (PointAnnotationManager) annotationPlugin.createAnnotationManager(AnnotationType.PointAnnotation, ac);


//          create a point Annotation
                InitializePoints(storeLocations,pointAnnotationManager);

                CameraOptions co = (new
                     CameraOptions.Builder())
                     .center(origin).zoom(17.00).build();


//              set a listener to each point annotation


             map.setCamera(co);




            }
        });
    }




//    implement the onMapReady interface
//    Handle the different lifecycles of the map (navigation, addedPoint on map, different location)

//    Create an alert dialog informing users that they cannot use the application outside of the specified bounds


    public void requestPermission(){
        if((ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            Toast.makeText(this,"Permission granted", Toast.LENGTH_SHORT).show();
            LocationHelper locationHelper = new LocationHelper(this,fusedLocationClient,null);
            CurrentLocationRequest currentLocationRequest = new CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build();
            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
            CancellationToken cancellationToken = cancellationTokenSource.getToken();
            // check why can't access location after async concluded
            locationHelper.getCurrentLocation(currentLocationRequest,cancellationToken);

            if(userStart != null){
                userLocation = Point.fromLngLat(locationHelper.currentLocation.getLongitude(),locationHelper.currentLocation.getLatitude());
                Toast.makeText(this,"Longitude: " + userLocation.longitude() + "Latitude" + userLocation.latitude(),Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"User location not found",Toast.LENGTH_SHORT).show();
            }

        }
        else{
//           // request the permission
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},reqCode);
        }
    }



    // it works it shows on map
    public void InitializePoints(Point [] stores, PointAnnotationManager pa){
//    Test with three stores (PRATA BOY, COFFEE CONNECT AND OISHI)
//       Coffee Connect Address
//       Convert the drawable content to a bitmap
        // issue is bitmap is null
        try{
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(),R.drawable.users);
            Bitmap customIcon = Bitmap.createScaledBitmap(icon,50,50,false);

            Point c = Point.fromLngLat( 103.77491355276209,1.3331722062601192);
            Point prata = Point.fromLngLat(103.7751671051295, 1.3335828064696371);
            Point Oishi = Point.fromLngLat(103.77570807725307, 1.3318849730782238);
            stores = new Point[]{c, prata, Oishi};
            if(icon != null){
                for(Point point : stores){
                    PointAnnotationOptions options = new PointAnnotationOptions()
                            .withPoint(point)
                            .withIconImage(customIcon);

                    pa.create(options);
                }
            }
            else{
                Toast.makeText(this,"Unable to retrive bitmap icon",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            Log.v("Error for bitmap",e.getMessage());
        }





    }
}



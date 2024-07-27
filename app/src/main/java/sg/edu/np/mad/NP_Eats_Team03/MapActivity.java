package sg.edu.np.mad.NP_Eats_Team03;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.opengl.GLES20;
import android.os.Bundle;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.gson.GeoJsonAdapterFactory;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.NP_Eats_Team03.Navigation.LocationHelper;
import sg.edu.np.mad.NP_Eats_Team03.Navigation.MapBoxRouteHandler;


public class MapActivity extends AppCompatActivity {

    private MapView mapView;

    private Point [] storeLocations;

    private MapboxMap map;
    private Integer reqCode = 3;

    private static final String KEY_Directions = "Directions";
    private static final String PREFS_NAME = "Directions";

    private String LOC_PREFS_NAME = "USER_LOCATION";

    private String KEY_LOCATION = "Location";

    private CameraOptions co;

    private Point c = Point.fromLngLat( 103.77491355276209,1.3331722062601192);
    private Point prata = Point.fromLngLat(103.7751671051295, 1.3335828064696371);
    private Point Oishi = Point.fromLngLat(103.77570807725307, 1.3318849730782238);

    private String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Integer REQ_CODE = 200;

    private Point OishiLocation = Point.fromLngLat(103.77570807725307, 1.3318849730782238);


    private String userStart;

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

        userStart = getSharedPreferences(LOC_PREFS_NAME,Context.MODE_PRIVATE).getString(KEY_LOCATION,null);

        SharedPreferences sp2 = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sp2.getString(KEY_Directions,"null");
        mapView = findViewById(R.id.mapView);
        map = mapView.getMapboxMap();
//      Change the style of the map to dark mode

        map.loadStyle(Style.SATELLITE_STREETS, style -> {


            //create the annotations on the map
//          Intialize a new annotation plugin
            // Integrate google's location provider with MapBox's
            LocationComponentPlugin locationComponentPlugin = mapView.getPlugin("MAPBOX_LOCATION_COMPONENT_PLUGIN_ID");

            Point origin = Point.fromLngLat(103.772830242,1.333498666);
//              center the coordinates at ngee ann poly
            AnnotationPlugin annotationPlugin = mapView.getPlugin("MAPBOX_ANNOTATION_PLUGIN_ID");
            AnnotationConfig ac = new AnnotationConfig();
            PointAnnotationManager pointAnnotationManager = (PointAnnotationManager) annotationPlugin.createAnnotationManager(AnnotationType.PointAnnotation, ac);
            InitializePoints(storeLocations,pointAnnotationManager);

            CameraOptions co = (new
                    CameraOptions.Builder())
                    .center(origin).zoom(17.00).build();


//              set a listener to each point annotation


            map.setCamera(co);

//          get the AnnotationClickListenerPlugin


            pointAnnotationManager.addClickListener(new OnPointAnnotationClickListener() {
                @Override
                public boolean onAnnotationClick(@NonNull PointAnnotation pointAnnotation) {
                    Point point = Point.fromLngLat(pointAnnotation.getGeometry().longitude(),pointAnnotation.getGeometry().latitude());

                    if (point.equals(OishiLocation)) {
                        Gson localGson = new GsonBuilder().registerTypeAdapterFactory(GeoJsonAdapterFactory.create()).create();
                        String destination = localGson.toJson(Oishi);
                        Log.d("MapAcitivity","This is oishi");
                        // inflate the fragment for oishi
                        showBottomSheet(R.layout.restraunt_modal,userStart,destination);

                    }

                    // handle the specific annotation clicked
                    return false;
                }
            });

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
            locationHelper.getCurrentLocation(currentLocationRequest, cancellationToken);

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

    public void openGLVersion(){
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            Log.e("OpenGL Error", "Error code: " + error);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void showBottomSheet(Integer layoutresid,String start, String destination){
        BottomSheetDialogFragment bottomSheetDialogFragment = MapModal.newInstance(layoutresid,start,destination);
        bottomSheetDialogFragment.show(getSupportFragmentManager(),"NIL");

    }
}



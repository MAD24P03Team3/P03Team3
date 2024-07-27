package sg.edu.np.mad.NP_Eats_Team03.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.api.directions.v5.DirectionsAdapterFactory;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.geojson.GeoJson;
import com.mapbox.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.utils.PolylineUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.NP_Eats_Team03.R;


public class MapBoxRouteHandler implements
    MapCallback{
//    This is where we will define the methods used to retrieve the list of coord given start and end coord


    String accessToken;

    Context context;


    private static final String KEY_Directions = "Directions";
    private static final String PREFS_NAME = "Directions";

    Point origin;

    Point destination;

   List<DirectionsRoute> currentRote;

    public MapBoxRouteHandler(Context context) {
        this.context = context;
    }

    //   Rest API to handle route function declare this method as static
    public void retrivefromMapBox(Context context, Point origin, Point destination) {
        ArrayList<Point> startEnd = new ArrayList<>();
        startEnd.add(origin);
        startEnd.add(destination);
        // convert longitude and latitude to a string in format of (long,lat) to 4 d.p.
        String mapAccessToken = context.getString(R.string.mapbox_access_token);
        RouteOptions routeOptions = RouteOptions.builder()
//                Should accept a list of points
                .coordinatesList(startEnd)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .steps(true)
                .build();

        // Create MapboxDirections client using RouteOptions
        MapboxDirections client = MapboxDirections.builder()
                .accessToken(mapAccessToken)
                .routeOptions(routeOptions)
                .build();

//        Lat long out of range
        // send the request
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                Response status code
//                Return response body and log successful response
                  Log.d("MapHandler","Http status: " + response.code());
                  if(response.body() == null){
                     Log.d("MapHandler","Unable to resolve request no routes found" + response.code());

                  }
                  else if (response.body().routes().size() == 0){
                      Log.d("MapHandler","Unable to find any routes");
                  }
                  // implement a callback that retrieves the stored coordinates
                  responseReturn(response);

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void responseReturn(Response<DirectionsResponse> response) {
        Response<DirectionsResponse> storedResponse = response;
        // store the response as sharedPrefrences

        DirectionsRoute dr = response.body().routes().get(0);
        Log.d("MapHandler",dr.toString());
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapterFactory(DirectionsAdapterFactory.create())
                        .create();
        String json = gson.toJson(dr);
        Log.d("MapHandler","This is my json: " + json);
        editor.putString(KEY_Directions,json);
        editor.commit();




    }







//    public void initializetest(Context context){
//        origin = Point.fromLngLat(103.940379,1.315141);
//        destination = Point.fromLngLat(103.961953, 1.334653);
//
//        retrivefromMapBox(context,origin,destination);
//    }
}

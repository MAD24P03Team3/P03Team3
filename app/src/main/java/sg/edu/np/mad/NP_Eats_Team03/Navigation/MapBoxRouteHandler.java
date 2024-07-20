package sg.edu.np.mad.NP_Eats_Team03.Navigation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.geojson.Point;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.utils.PolylineUtils;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.NP_Eats_Team03.R;


public class MapBoxRouteHandler implements
    MapCallback{
//    This is where we will define the methods used to retrieve the list of coord given start and end coord

    ArrayList<Coordinates> pathCoordinaties;
    String accessToken;

    Context context;


    Point origin;

    Point destination;

    public MapBoxRouteHandler() {
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
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
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

    // preprocess the data to just find the route part of the response
    @Override
    public void responseReturn(Response<DirectionsResponse> response) {
        Response<DirectionsResponse> storedResponse = response;
//        get the route property in the json response
        DirectionsRoute route = response.body().routes().get(0);
        // Decode the polyline with precision 6
        List<Point> points = PolylineUtils.decode(route.geometry(), 6);


        // Print the decoded points (latitude, longitude)
        for (Point point : points) {
            Log.d("Response route","Coordinates" + point);
        }

    }


    public void initializetest(Context context){
        origin = Point.fromLngLat(103.940379,1.315141);
        destination = Point.fromLngLat(103.961953, 1.334653);

        retrivefromMapBox(context,origin,destination);
    }
}

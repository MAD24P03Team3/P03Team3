package sg.edu.np.mad.NP_Eats_Team03;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.geojson.Point;

import sg.edu.np.mad.NP_Eats_Team03.Navigation.MapBoxRouteHandler;

public class MapModal extends BottomSheetDialogFragment {

    private static final String KEY_Directions = "Directions";
    private static final String PREFS_NAME = "Directions";

    public MapModal() {
    }

//    allows for data passed to fragements to be stored when it is recreated (fragment lifecycyle)
    public static MapModal newInstance(Integer layoutresID, String start, String end) {

        Bundle args = new Bundle();
        args.putInt("LayoutResID",layoutresID);
        args.putString("Start",start);
        args.putString("Destination",end);
        MapModal fragment = new MapModal();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Integer layoutresid = getArguments().getInt("LayoutResID");
        return inflater.inflate(layoutresid,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.startNavigating);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveRouteAndStartNavigation();
            }
        });
    }

    private void retrieveRouteAndStartNavigation() {
        try {
            String[] coordinates = getArguments().getString("Start").split(",");
            Point userLocation = Point.fromLngLat(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]));
            Point destination = Point.fromJson(getArguments().getString("Destination"));
            MapBoxRouteHandler mapBoxRouteHandler = new MapBoxRouteHandler(getContext());
            mapBoxRouteHandler.retrivefromMapBox(getContext(), userLocation, destination);
            startNavigationActivity();
        } catch (Exception e) {
            Log.d("MapModal",e.getMessage());
        }
    }

    private void startNavigationActivity() {
        Intent intent = new Intent(getActivity(), NavigationActivity.class);
        startActivity(intent);
    }}

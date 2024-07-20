package sg.edu.np.mad.NP_Eats_Team03;
import static sg.edu.np.mad.NP_Eats_Team03.bottomModal.TAG;

import android.app.ActivityManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import sg.edu.np.mad.NP_Eats_Team03.Navigation.MapBoxRouteHandler;

public class arTest extends AppCompatActivity implements
        FragmentOnAttachListener,
        BaseArFragment.OnSessionConfigurationListener,
        ArFragment.OnViewCreatedListener {

    private ArFragment arFragment;
    private Renderable model;

    private MapBoxRouteHandler mbh;

    private LocationManager local;

    private LocationListener localList;

    private ViewRenderable viewRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ar_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportFragmentManager().addFragmentOnAttachListener(this);



        try {
            if (savedInstanceState == null) {
                if (Sceneform.isSupported(this)) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.arFragment, ArFragment.class, null)
                            .commit();

                }
            }
        } catch (Error error) {
            Log.v("This is my error", error.toString());
        }


    }

    @Override
    public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        if (fragment.getId() == R.id.arFragment) {
            arFragment = (ArFragment) fragment;
            arFragment.setOnTapArPlaneListener(
                    (HitResult hr, Plane plane, MotionEvent Me) -> {
                        if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING)
                            return;
                        Anchor anchor = hr.createAnchor();
                        try {
                            if (anchor != null) {
                                loadModel(anchor, arFragment);
                            } else {
                                Log.d("AttachingFragment", "unable to anchor");
                            }

                        } catch (Error error) {
                            Log.d("AttachingFragment", error.getMessage());
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
            );
            arFragment.setOnSessionConfigurationListener(this);
            arFragment.setOnViewCreatedListener(this);
        }
    }

    @Override
    public void onSessionConfiguration(Session session, Config config) {
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC);
        }
    }

    @Override
    public void onViewCreated(ArSceneView arSceneView) {
        arFragment.setOnViewCreatedListener(null);

        // Fine adjust the maximum frame rate
        arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL);
    }

    public void loadModel(Anchor a, ArFragment fragment) throws IOException {
        WeakReference<arTest> weakActivity = new WeakReference<>(this);

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().

        /*This is an asynchronus function, hence we use .thenAccept to handle the sucessful
         * completion of the task.
         * 1. we locate the source of our model
         * 2. We intialize the methods for building the model
         * 4. After have built model we assign the model to our renderable varibale in the activity*/
        ModelRenderable.builder()
                .setSource(this, Uri.parse("sampledata/scene.gltf"))
                .setIsFilamentGltf(true)
                .setAsyncLoadEnabled(true)
                .build()
                .thenAccept(
                        modelRenderable -> {
                            arTest activity = weakActivity.get();
                            if (activity != null) {
                                // assigns the model variable in the activity to my 3d model
                                activity.model = modelRenderable;
//                                Call place object method here
                                placeObj(fragment, a, activity.model);
                            }
                        })
                .exceptionally(throwable -> {
                    Toast.makeText(
                            this, "Unable to load model", Toast.LENGTH_LONG).show();
                    return null;
                });




    }

    public void placeObj(ArFragment fragment, Anchor anchor, Renderable model) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(model);
        node.setParent(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }

    public void GetGPS(LocationManager localMan) {
        if (!localMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // create an alert dialog to say no GPS and enable it
            buildAlertMessageNoGps();
        }




    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

//    private File loadFile(String filePth) throws FileNotFoundException {
//        File file = new File(getFilesDir(),filePth);
//        if(!file.exists()){
//
//            Toast.makeText(this, filePth + "does not exists",Toast.LENGTH_SHORT).show();
//            throw new FileNotFoundException("File not found");
//        }
//        return file;
//    }


}





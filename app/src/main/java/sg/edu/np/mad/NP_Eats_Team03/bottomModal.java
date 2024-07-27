package sg.edu.np.mad.NP_Eats_Team03;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.firebase.firestore.FieldValue.arrayUnion;
import static com.google.gson.internal.$Gson$Types.arrayOf;

import sg.edu.np.mad.NP_Eats_Team03.R;
import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class bottomModal extends BottomSheetDialogFragment{

    ArrayList<String> itemid = new ArrayList<String>();


    private ActivityResultLauncher<String> pickImage;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String TAG = "Bottom modal";
    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private static final int PICK_IMAGE_REQUEST = 2;

    public interface HandleCallback{

        void onCallback(ArrayList<String> itemList);

    }

    private void openGallery() {
        pickImage.launch("image/*");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.menumodal,container,false);
        Button editbtn = rootview.findViewById(R.id.editimg);
        ImageView image = rootview.findViewById(R.id.pimg);
        Button submit = rootview.findViewById(R.id.submitpopup);
        Button editImage = rootview.findViewById(R.id.editimg);

        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    Log.d("This is my image", uri.toString());
                    image.setImageURI(uri);
                }
            }
        });

        EditText etname = rootview.findViewById(R.id.etfoodname);
        EditText etdesc = rootview.findViewById(R.id.etfooddesc);
        EditText etPrice = rootview.findViewById(R.id.etprice);
        EditText etcat = rootview.findViewById(R.id.etfoodcat);

        // Handle the user interraction when creating a new menuItem
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString();
                String desc = etdesc.getText().toString();
                String itcat = etcat.getText().toString();
                Double price = Double.parseDouble(etPrice.getText().toString());
                // retrive the relevant input fields
                getAllItemData(new HandleCallback(){
                    @Override
                    // override the callback method in the interface
                    public void onCallback(ArrayList<String> itemList) {
                        // Handle the retrieved list
                        itemid = itemList;
                        int lastind = itemList.size() -1;
                        String lastimd = itemList.get(lastind);
                        int newitemnum = parseInt(lastimd.substring(1,3)) + 1;
                        String newItemId = "P" + newitemnum;
                        // create a new HashMap to store the item Objects as a Map in FIreStore
                        HashMap<String,Object> data = new HashMap<>();
                        data.put("itemDesc",desc);
                        data.put("itemID",newItemId);
                        data.put("itemName",name);
                        data.put("itemPrice",price);
                        data.put("itemCategory",itcat);

                        addNewData(db,data);
                    }
                },db);
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for access to camera or device storage
                requestPermissionToReadStorage();

            }
        });

       return rootview;
    }

    public void getAllItemData(bottomModal.HandleCallback callback, FirebaseFirestore db){
        ArrayList<String> itemids = new ArrayList<String>();
        db.collection("Stores").document("Prata-Boy")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot docs = task.getResult();
                            ArrayList<Map<String,Object>> mi = (ArrayList<Map<String, Object>>) docs.get("menuItems");
                            for(Map<String,Object> o: mi){
                                String itemId = (String)o.get("itemID");
                                itemids.add(itemId);
                            }
                            callback.onCallback(itemids);
                        }
                        else{
                            Log.d("Error","Not working");
                        }
                    }
                });
        // specify the document to retrive item objects in a specific collection
        // handle data retrival  <Task> onSucess, onFailure


    }

    public void addNewData(FirebaseFirestore db, HashMap<String,Object> data){
        DocumentReference dr = db.collection("Stores").document("Prata-Boy");
        // arrayUnion which appends the data to the end of the array
        dr.update("menuItems",arrayUnion(data)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Success","Successfully updated the field ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Unsuccessful", "Unable to update the field");
            }
        });


    }




    public void requestPermissionToReadStorage(){
        // check if the permission to read device storage is granted
        if(ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};

            ActivityCompat.requestPermissions(getActivity(),permissions,PICK_IMAGE_REQUEST);
        }
        else{
            openGallery();
            // Store the uri of the image

        }
    }




}

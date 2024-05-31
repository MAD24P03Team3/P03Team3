package sg.edu.np.mad.myapplication;

import static com.google.firebase.firestore.FieldValue.arrayUnion;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String TAG = "Bottom modal";

    public interface HandleCallback{

        void onCallback(ArrayList<String> itemList);

    }
    public ArrayList<String> onCallBack(ArrayList<String>itemd){
        return itemd;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootview = inflater.inflate(R.layout.menumodal,container,false);


        Button editbtn = rootview.findViewById(R.id.editimg);
        Button submit = rootview.findViewById(R.id.submitpopup);

        EditText etname = rootview.findViewById(R.id.etfoodname);
        EditText etdesc = rootview.findViewById(R.id.etfooddesc);
        EditText etPrice = rootview.findViewById(R.id.etprice);
        EditText etcat = rootview.findViewById(R.id.etfoodcat);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString();
                String desc = etdesc.getText().toString();
                String itcat = etcat.getText().toString();
                Double price = Double.parseDouble(etPrice.getText().toString());
                getAllItemData(new HandleCallback(){
                    @Override
                    // override the callback method
                    public void onCallback(ArrayList<String> itemList) {
                        // Handle the retrieved list
                        itemid = itemList;
                        int lastind = itemList.size() -1;
                        String lastimd = itemList.get(lastind);
                        int newitemnum = parseInt(lastimd.substring(1,3)) + 1;
                        String newItemId = "P" + newitemnum;
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

        //TODO: 1. Create new item objects with user data
        /*    : 2. Go through all the documents objects retrive all itemId put it in a list
              : 3. New itemId would be the last index in the list value increment by one
         */

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
}

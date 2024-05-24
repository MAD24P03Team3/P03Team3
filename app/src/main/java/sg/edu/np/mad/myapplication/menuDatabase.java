package sg.edu.np.mad.myapplication;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


public class menuDatabase {


    // TODO:
    // *Methods for retrieval of database
    //
    // *for other methods do database query (to show only specific results)

    public static void retriveAllMenu(FirebaseFirestore db, ArrayList<Item> md){
        DocumentReference dr = db.collection("Stores").document("Prata-Boy");
        dr.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                // Retrive the field in the document
                ArrayList<Map<String,String>> Menuitems = (ArrayList<Map<String,String>>) documentSnapshot.get("menuItems");

                // convert all the hashmaps to classes of itemclass
                for(Map<String,String> item: Menuitems){
                    String name = item.get("name");
                    String itemDesc = item.get("itemDesc");
                    String itemID = item.get("itemID");
                    Double price = Double.parseDouble(item.get("price"));
                    md.add(new Item(itemID,name,itemDesc,price));


                }




                // log successful retrieval
                Log.d("This is my map",Menuitems.toString());
            }


        });

    }

}

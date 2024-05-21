package sg.edu.np.mad.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


public class MenuDatabase {


    // TODO:
    // *Methods for retrieval of database
    //
    // *for other methods do database query (to show only specific results)

//    public static void retriveAllMenu(FirebaseFirestore db,ArrayList<Item>menuItem){
//        ArrayList<Item> md = new ArrayList<Item>();
//        DocumentReference dr = db.collection("Stores").document("Prata-Boy");
//        // your OnSuccess method has to be implmented handle asynchronisity of code
//        dr.get().addOnSuccessListener(documentSnapshot -> {
//
//            if(documentSnapshot.exists()){
//                // Retrive the field in the document
//                int StoreId = Integer.parseInt(documentSnapshot.getString("StoreId"));
//
//                ArrayList<Map<String,String>> Menuitems = (ArrayList<Map<String,String>>) documentSnapshot.get("menuItems");
//
//                // convert all the hashmaps to classes of itemclass
//                for(Map<String,String> item: Menuitems){
//                    String name = item.get("name");
//                    String itemDesc = item.get("itemDesc");
//                    Double price = Double.parseDouble(item.get("price"));
//                    menuItem.add(new Item(StoreId,name,itemDesc,1,price));
//
//
//
//
//                }
//
//
//
//                // log successful retrieval
//                Log.d("MenuDatabase","Success");
//
//            }
//
//
//
//
//
//
//
//
//        });
//
//    }

    // Correct method for database retrieval
    public static void retrieveMenuData(FirebaseFirestore db, ArrayList<Item>mi, MenuAdapter menuAdapter){
        DocumentReference dr = db.collection("Stores").document("Prata-Boy");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists()){
                        int storeId = Integer.parseInt(ds.getString("StoreId"));
                        ArrayList<Map<String,String>> Menuitems = (ArrayList<Map<String,String>>) ds.get("menuItems");
                        for(Map<String,String> item: Menuitems){
                            String name = item.get("name");
                            String itemDesc = item.get("itemDesc");
                            Double price = Double.parseDouble(item.get("price"));
                            mi.add(new Item(storeId,name,itemDesc,1,price));




                        }
                        // Changes made to the recyclerVuew Adapter (to reflect new data updated)
                        menuAdapter.notifyDataSetChanged();




                    }
                }
                Log.d("End of array", String.valueOf(mi.size()));





            }
        });


    }


}

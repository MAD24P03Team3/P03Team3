package sg.edu.np.mad.NP_Eats_Team03;

import static com.google.firebase.firestore.FieldValue.arrayUnion;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

// this class is used to encapsulate the business logic for CRUD to firestore database
public class FireStoreHandler {
    private FirebaseFirestore db;

    public FireStoreHandler(FirebaseFirestore db, String email) {
        this.db = db;
    }

    public void retriveData(String collectionPath, String documentName, HandleDataRead hr){
        ArrayList<Item> itemList = new ArrayList<>();
        db.collection(collectionPath).document(documentName)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        if (task.isSuccessful()){
                            if(ds.exists()){
                                // proceed to retrieve the menuitems
                                ArrayList<HashMap<String,Object>> menuItems = (ArrayList<HashMap<String,Object>>) ds.get("menuItems");
                                for (HashMap<String,Object> item :menuItems){
                                    Double price;
                                    Long lprice;
                                    Object obj =item.get("itemPrice");
                                    String name = (String) item.get("itemName");
                                    String itemId = (String) item.get("itemID");

                                    String description = (String) item.get("itemDescription");

                                    if(obj instanceof Double){
                                        price = (Double) obj;
                                        Item currentItem = new Item(itemId,name,description,price);
                                        itemList.add(currentItem);
                                    }

                                    if(obj instanceof Long){
                                        lprice = (Long) obj;
                                        price = lprice.doubleValue();
                                        Item currentItem = new Item(itemId,name,description,price);
                                        itemList.add(currentItem);
                                    }
                                }
                                hr.onSuccess(itemList);
                            }
                            else{
                                hr.onFailure("Document does not exists");

                            }
                        }
                        else{
                            hr.onFailure("Failed to resolve task: " + task.getException().getMessage());
                        }


                    }
                });
    }

    public void writeDataLikes(String collectionPath,String email, Item item, HandleDataWrite hw){
        if(item != null){
            // Add the liked item object to the database

            db.collection("Customer").document(email)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    HashMap<String,Object> likedData = new HashMap<>();
                                    likedData.put("likedItemID",item.itemId);
                                    likedData.put("likedItemName",item.name);
                                    likedData.put("likedItemDesc",item.description);
                                    likedData.put("likePrice",item.price);

                                    DocumentReference dr = document.getReference();
                                    if(likedData != null){
                                        dr.update("Likes",arrayUnion(likedData)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                hw.onSucess("Data written sucessfully");
                                            }
                                        });
                                    }
                                    else{
                                        hw.onFailure("Hashmap is empty");
                                    }
                                }
                                else{
                                    hw.onFailure("Document does not exists");
                                }
                            }
                            else{
                                hw.onFailure("Unable to retrieve document");
                            }
                        }
                    });
        }
        else{
            hw.onFailure("Item is empty");
        }
    }



}

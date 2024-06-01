package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MenuFragment extends Fragment {

    public String TAG = "MenuFragment";

    public void retrievemenuItems(FirebaseFirestore db, HandleCallBack hc){
        ArrayList<Item> itemList = new ArrayList<>();
        // retrive the document
        db.collection("Stores").document("Prata-Boy")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        if(ds.exists()){
                            // proceed to retrive the menuitems
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
                            hc.arrayCallBack(itemList);
                        }
                        else{
                            Log.d(TAG,"The document does not exists");

                        }
                    }
                });

    }

    ArrayList<Item> menuData = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Item> menuItems = new ArrayList<Item>();
    private String[] titles = new String[]{"Menu","Drinks","Praffles"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);


        // TODO Part 1
        //  1. set up all the views in the fragment
        /*  2. handle the database retriveal of all menu items by querying the database
           */


        retrievemenuItems(db, new HandleCallBack() {

            @Override
            // handle the retrieval from the callback
            public void arrayCallBack(ArrayList<Item> listofitmes) {
                menuData = listofitmes;
                MenuAdapter ma = new MenuAdapter(getContext(),menuData);
                RecyclerView menurecycler = view.findViewById(R.id.menurecycler);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
                menurecycler.setLayoutManager(layoutManager);
                menurecycler.setItemAnimator(new DefaultItemAnimator());
                menurecycler.setAdapter(ma);

            }
        });








        return view;
    }
}
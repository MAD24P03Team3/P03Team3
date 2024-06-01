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
        db.collection("Stores").document("Prata-Boy")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        if(ds.exists()){
                            // proceed to retrive the menuitems
                            ArrayList<HashMap<String,Object>> menuItems = (ArrayList<HashMap<String,Object>>) ds.get("menuItems");
                            for (HashMap<String,Object> item :menuItems){
                                String name = (String) item.get("itemName");
                                String itemId = (String) item.get("itemID");
                                Double price = (Double) item.get("itemPrice");
                                String description = (String) item.get("itemDesc");

                                Item currentItem = new Item(itemId,name,description,price);
                                itemList.add(currentItem);

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
            Part 2 : Inside menu Adapter
        *   3. handle the adding items to liked fragment (when like button clicked store the menuItem in db
        *   4. Retrive the menuItems and store it as item objects in the arrayList of user class*/
        retrievemenuItems(db, new HandleCallBack() {
            @Override
            // handle the retrieval from the callback
            public void arrayCallBack(ArrayList<Item> listofitmes) {
                menuData = listofitmes;
                MenuAdapter ma = new MenuAdapter(menuData);
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
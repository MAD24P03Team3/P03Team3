package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
    private CartViewModel cartViewModel;

    public void retrievemenuItems(FirebaseFirestore db, HandleCallBack hc){
        ArrayList<Item> itemList = new ArrayList<>();
        // retrieve the document
        db.collection("Stores").document("Prata-Boy")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // TODO Part 1
        //  1. set up all the views in the fragment
        //  2. handle the database retrieval of all menu items by querying the database

        retrievemenuItems(db, new HandleCallBack() {

            @Override
            // handle the retrieval from the callback
            public void arrayCallBack(ArrayList<Item> listofitmes) {
                menuData = listofitmes;
                cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
                MenuAdapter menuAdapter = new MenuAdapter(getContext(), menuData, new MenuAdapter.OnItemAddListener() {
                    @Override
                    public void onItemAdd(Item item) {
                        cartViewModel.addToCart(item);
                    }
                    //Fixing merging error
                });

                RecyclerView menuRecycler = view.findViewById(R.id.menurecycler);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
                menuRecycler.setLayoutManager(layoutManager);
                menuRecycler.setItemAnimator(new DefaultItemAnimator());
                menuRecycler.setAdapter(menuAdapter);
            }
        });

        return view;
    }
}
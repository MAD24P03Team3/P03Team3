package sg.edu.np.mad.NP_Eats_Team03;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuFragment extends Fragment {

    public String TAG = "MenuFragment";
    private CartViewModel cartViewModel;
    private List<Item> itemListF = new ArrayList<>();
    private MenuAdapter menuAdapter;
    private SearchView searchView;

    public void filterList(String text) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : itemListF) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            menuAdapter.setFilteredList(filteredList);
        }
    }

    public void retrieveMenuItems(FirebaseFirestore db, HandleCallBack hc) {
        ArrayList<Item> itemList = new ArrayList<>();
        // retrieve the document
        db.collection("Stores").document("Prata-Boy")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        if (ds.exists()) {
                            // proceed to retrieve the menu items
                            ArrayList<HashMap<String, Object>> menuItems = (ArrayList<HashMap<String, Object>>) ds.get("menuItems");
                            for (HashMap<String, Object> item : menuItems) {
                                Double price;
                                Long lprice;
                                Object obj = item.get("itemPrice");
                                String name = (String) item.get("itemName");
                                String itemId = (String) item.get("itemID");
                                String description = (String) item.get("itemDescription");

                                if (obj instanceof Double) {
                                    price = (Double) obj;
                                    Item currentItem = new Item(itemId, name, description, price);
                                    itemList.add(currentItem);
                                }

                                if (obj instanceof Long) {
                                    lprice = (Long) obj;
                                    price = lprice.doubleValue();
                                    Item currentItem = new Item(itemId, name, description, price);
                                    itemList.add(currentItem);
                                }
                            }
                            hc.arrayCallBack(itemList);
                        } else {
                            Log.d(TAG, "The document does not exist");
                        }
                    }
                });
    }

    ArrayList<Item> menuData = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        searchView = view.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        retrieveMenuItems(db, new HandleCallBack() {
            @Override
            public void arrayCallBack(ArrayList<Item> listofitmes) {
                menuData = listofitmes;
                itemListF.addAll(listofitmes); // Save the original list for filtering
                cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
                menuAdapter = new MenuAdapter(getContext(), menuData, new MenuAdapter.OnItemAddListener() {
                    @Override
                    public void onItemAdd(Item item) {
                        cartViewModel.addToCart(item);
                    }
                });

                RecyclerView menuRecycler = view.findViewById(R.id.menurecycler);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                menuRecycler.setLayoutManager(layoutManager);
                menuRecycler.setItemAnimator(new DefaultItemAnimator());
                menuRecycler.setAdapter(menuAdapter);
            }
        });

        return view;
    }

    public interface HandleCallBack {
        void arrayCallBack(ArrayList<Item> listofitmes);
    }
}

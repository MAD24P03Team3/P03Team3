package sg.edu.np.mad.NP_Eats_Team03;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import sg.edu.np.mad.NP_Eats_Team03.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class MenuFragment extends Fragment {

    public String TAG = "MenuFragment";
    private MenuViewModel menuViewModel;
    private CartViewModel cartViewModel;

    private MutableLiveData<ArrayList<Item>> liveMenuData;

    private FireStoreHandler dbHandler;

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }
    ArrayList<Item> menuData = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        dbHandler = new FireStoreHandler(db,loadEmailFromSharedPreferences());
        liveMenuData = new MutableLiveData<>();
        ViewModelProvider.Factory factory = new MenuViewModelFactory(dbHandler, liveMenuData);
        menuViewModel = new ViewModelProvider(this,factory).get(MenuViewModel.class);
        menuViewModel.getMenuData("Stores","Prata-Boy");
        // Observe LiveData from ViewModel
        menuViewModel.getMenuItemsLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> menuItems) {
                // Update adapter with new data
                cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
                MenuAdapter menuAdapter = new MenuAdapter(getContext(), menuItems, new MenuAdapter.OnItemAddListener() {
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
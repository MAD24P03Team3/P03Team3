package sg.edu.np.mad.NP_Eats_Team03;

import static android.content.Context.MODE_PRIVATE;
import sg.edu.np.mad.NP_Eats_Team03.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class AccountFragment extends Fragment {

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";

    private static final String TAG = "AccountFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Item> items = new ArrayList<>();



    public AccountFragment() {
        // Required empty public constructor
    }


    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle any arguments if necessary
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String email = loadEmailFromSharedPreferences();
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        retrivelikedItems(db, new HandleCallBack() {
            @Override
            public void arrayCallBack(ArrayList<Item> listofitmes) {
                items = listofitmes;
                Log.d(TAG,String.valueOf(listofitmes.size()));
                RecyclerView likesRecycler = view.findViewById(R.id.likedrecycler);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                likesRecycler.setLayoutManager(layoutManager);
                likesRecycler.setItemAnimator(new DefaultItemAnimator());
                LikedAdapter likeadapt = new LikedAdapter(items,email);
                likesRecycler.setAdapter(likeadapt);
            }
        },email);

        return view;
    }

    private void logout() {
        // Clear the email from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.remove(KEY_NAME);
        //Removing because fails with biometrics
        editor.apply();

        // Sign out from FirebaseAuth
        FirebaseAuth.getInstance().signOut();

        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public void retrivelikedItems(FirebaseFirestore db, HandleCallBack hc, String email ){
        ArrayList<Item> itemList = new ArrayList<>();
        db.collection("Customer").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();

                    if(ds.exists()){
                        Object object = ds.get("Likes");
                        ArrayList<HashMap<String,Object>> itemliked = (ArrayList<HashMap<String,Object>>) ds.get("Likes");
                        if(itemliked != null){
                            for (HashMap<String,Object> items : itemliked){
                                Double price;
                                Long lprice;
                                Object obj = items.get("likePrice");
                                String name = (String) items.get("likedItemName");
                                String itemId = (String) items.get("likedItemID");

                                String description = (String) items.get("likedItemDesc");

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
                            Log.d(TAG,"ItemList is empty");
                        }


                    }
                    else{
                        Log.d(TAG,"Document does not exists");
                    }
                }
            }
        });
    }
}

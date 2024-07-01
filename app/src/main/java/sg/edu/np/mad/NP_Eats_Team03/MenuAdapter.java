package sg.edu.np.mad.NP_Eats_Team03;

import static android.content.Context.MODE_PRIVATE;
import sg.edu.np.mad.NP_Eats_Team03.R;
import static com.google.firebase.firestore.FieldValue.arrayUnion;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;




public class MenuAdapter extends RecyclerView.Adapter <MenuAdapter.MyViewHolder> {

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";
    private ArrayList<Item> menuData;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context Context;
    private String TAG = "MenuAdapter";
    private OnItemAddListener onItemAddListener;



    public void addtoliked(int i, ArrayList<Item> menuData){
        Item item = menuData.get(i);
        if(item != null){
            // Add the liked item object to the database

            db.collection("Customer").document(loadEmailFromSharedPreferences())
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
                                                Log.d(TAG,"Document sucessfully updated");
                                            }
                                        });
                                    }
                                    else{
                                        Log.d(TAG,"The hashmap is empty" + task.getException());
                                    }
                                }
                                else{
                                    Log.d(TAG,"The document does not exists" + task.getException());
                                }
                            }
                            else{
                                Log.d(TAG,"Unable to retrive document" + task.getException());
                            }
                        }
                    });
        }
        else{
            Log.d(TAG,"Item is empty");
        }
    }

    // retrieve the user email from shared preferences
    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = Context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }

    //Fixing merging error
    public interface OnItemAddListener {
        void onItemAdd(Item item);
    }

    // Constructor that has these variables
    public MenuAdapter(Context context,ArrayList<Item> menuData, OnItemAddListener input_onItemAddListener){
        this.menuData = menuData;
        this.Context = context;
        onItemAddListener = input_onItemAddListener;
//        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        return new MyViewHolder(v);
    }

    @NonNull


    //assign values to views as they come back on the screen based on the screen position,

    // When the user scrolls it gets the position of items display items that fit on screen

    // And scraps the rest of the view
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // set the text of the Text view name, get the position of the element in arrayList, return that item
        holder.tvName.setText(menuData.get(position).name);
        Double price = menuData.get(position).price;
        holder.tvItemDescription.setText("");
        holder.tvItemPrice.setText(String.format("$%.2f",price));
        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition();
                addtoliked(index,menuData);
            }
        });
    }

    @Override
    //number of items displayed in the recycler view
    public int getItemCount() {
        return menuData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // retreiving views from recyler_view layout

        ImageView imageView;
        TextView tvName, tvItemPrice, tvItemDescription;
        Button plus,liked;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.tvTitle);
            tvItemPrice = itemView.findViewById(R.id.tvPrice);
            tvItemDescription = itemView.findViewById(R.id.tvDesc);
            liked = itemView.findViewById(R.id.Like);
            plus = itemView.findViewById(R.id.Add);

            // TODO Part 2 : Inside menu Adapter
            //  1. Retieve all the views for the menurecycler (add to cart, liked items)
            //  2. likes -> retrive the coressponding item object and then add then store it in share preference
            //      - User click -> Add the item to a likedlist
            //  3. Add to cart handled by Jake

            plus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemAddListener.onItemAdd(menuData.get(position));
                    }
                }
            });
        }
    }
}

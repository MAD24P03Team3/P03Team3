package sg.edu.np.mad.NP_Eats_Team03;

import static android.content.Context.MODE_PRIVATE;
import static com.google.firebase.firestore.FieldValue.arrayUnion;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";
    private ArrayList<Item> menuData;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private String TAG = "MenuAdapter";
    private OnItemAddListener onItemAddListener;

    public MenuAdapter(ArrayList<Item> menuData) {
        this.menuData = menuData;
    }

    public void setFilteredList(ArrayList<Item> filteredList) {
        this.menuData = filteredList;
        notifyDataSetChanged();
    }

    public void addToLiked(int i, ArrayList<Item> menuData) {
        Item item = menuData.get(i);
        if (item != null) {
            db.collection("Customer").document(loadEmailFromSharedPreferences())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    HashMap<String, Object> likedData = new HashMap<>();
                                    likedData.put("likedItemID", item.itemId);
                                    likedData.put("likedItemName", item.name);
                                    likedData.put("likedItemDesc", item.description);
                                    likedData.put("likePrice", item.price);

                                    DocumentReference dr = document.getReference();
                                    if (likedData != null) {
                                        dr.update("Likes", arrayUnion(likedData)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "Document successfully updated");

                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "The hashmap is empty" + task.getException());
                                    }
                                } else {
                                    Log.d(TAG, "The document does not exist" + task.getException());
                                }
                            } else {
                                Log.d(TAG, "Unable to retrieve document" + task.getException());
                            }
                        }
                    });
        } else {
            Log.d(TAG, "Item is empty");
        }
    }

    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "No name found");
    }

    public interface OnItemAddListener {
        void onItemAdd(Item item);
    }

    public MenuAdapter(Context context, ArrayList<Item> menuData, OnItemAddListener inputOnItemAddListener) {
        this.menuData = menuData;
        this.context = context;
        this.onItemAddListener = inputOnItemAddListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item currentItem = menuData.get(position);
        holder.tvName.setText(currentItem.name);
        Double price = currentItem.price;
        holder.tvItemDescription.setText(currentItem.description);
        holder.tvItemPrice.setText(String.format("$%.2f", price));

        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition();
                addToLiked(index, menuData);
                Toast.makeText(holder.itemView.getContext(), "Item liked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemAddListener.onItemAdd(menuData.get(position));
                    Toast.makeText(holder.itemView.getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Add button clicked for item: " + menuData.get(position).name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName, tvItemPrice, tvItemDescription;
        Button plus, liked;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.tvTitle);
            tvItemPrice = itemView.findViewById(R.id.tvPrice);
            tvItemDescription = itemView.findViewById(R.id.tvDesc);
            liked = itemView.findViewById(R.id.Like);
            plus = itemView.findViewById(R.id.Add);
        }
    }
}

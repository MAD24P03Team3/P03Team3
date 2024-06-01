package sg.edu.np.mad.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class LikedAdapter extends RecyclerView.Adapter <LikedAdapter.myViewHolder>{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;



    public ArrayList<Item> itemList;
    public LikedAdapter(ArrayList<Item> items, String email) {
        this.itemList = items;
        this.email = email;
    }

    @NonNull
    @Override
    public LikedAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_likes, parent, false);
        return new LikedAdapter.myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedAdapter.myViewHolder holder, int position) {
        holder.tvName.setText(itemList.get(position).name);
        holder.tvDescription.setText(itemList.get(position).description);
        Double price = itemList.get(position).price;
        holder.tvPrice.setText(String.format("$%.2f",price));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition();
                if(index != RecyclerView.NO_POSITION){
                    itemList.remove(index);
                    // update the size of the data passed to the recyclerview adapter
                    notifyItemRemoved(index);
                    notifyDataSetChanged();
                    notifyItemRangeChanged(index, itemList.size());



                    // index = 0, you removed 1 orignal size = 10 now it is 9 so its indexed 0-9 : 9
                    // update the recyclerviewadapter
                    updateTheDb(db,itemList,email);


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        // retrive all the views related in the recycler view
        ImageView image;

        Button delete;

        TextView tvName,tvDescription,tvPrice;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.Name);
            tvDescription = itemView.findViewById(R.id.Description);
            tvPrice = itemView.findViewById(R.id.price);
            delete = itemView.findViewById(R.id.unlike);

        }
    }

    public void updateTheDb(FirebaseFirestore db, ArrayList<Item> itemList, String email) {
        db.collection("Customer").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    if (ds.exists()) {
                        // iterate through the itemList objects to convert each item object to maps kvp and add it to the array
                        ArrayList<HashMap<String, Object>> itemLiked = new ArrayList<>();
                        for (Item item : itemList) {
                            HashMap<String, Object> itemMap = new HashMap<>();
                            itemMap.put("itemName", item.name);
                            itemMap.put("itemID", item.itemId);
                            itemMap.put("itemDescription", item.description);
                            itemMap.put("itemPrice", item.price);
                            itemLiked.add(itemMap);
                        }

                        // Update the Likes =field which stores an array
                        db.collection("Customer").document(email).update("Likes", itemLiked)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("LikedAdapter", "Successfully updated the database");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("LikedAdapter", "Failed to update the database", e);
                                    }
                                });
                    } else {
                        Log.d("LikedAdapter", "The document does not exist");
                    }
                } else {
                    Log.d("LikedAdapter", "Unable to retrieve from path specified", task.getException());
                }
            }
        });
    }

}

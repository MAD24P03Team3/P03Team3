package sg.edu.np.mad.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter <MenuAdapter.MyViewHolder> {


    private ArrayList<Item> menuData;



    // Constructor that has these variables
    public MenuAdapter(ArrayList<Item> menuData){
        this.menuData = menuData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrecyler, parent, false);
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
        holder.tvItemPrice.setText(price.toString());

    }

    @Override
    //number of items displayed in the recycler view
    public int getItemCount() {
        return menuData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // retreiving views from recyler_view layout

        ImageView imageView;
        TextView tvName,  tvItemPrice, tvDesc;

        Button plus;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.foodimg);
            tvName = itemView.findViewById(R.id.foodname);
            tvItemPrice = itemView.findViewById(R.id.itemPrice);
            plus = itemView.findViewById(R.id.plus);

        }
    }
}

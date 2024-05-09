package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class menu_recyclerviewAdapter extends RecyclerView.Adapter <menu_recyclerviewAdapter.MyViewHolder> {

    Context context;
    ArrayList<menuModel> menuData;

    // Constructor that has these variables
    public menu_recyclerviewAdapter(Context context, ArrayList<menuModel> menuData){
        this.context = context;
        this.menuData = menuData;
    }
    @NonNull
    @Override

    // This is where you will inflate layout and give look to each row
    public menu_recyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_layout_menu, parent, false);
        return new menu_recyclerviewAdapter.MyViewHolder(view);
    }

    @Override

    //assign values to views as they come back on the screen based on the screen position,

    // When the user scrolls it gets the position of items display items that fit on screen

    // And scraps the rest of the view
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // set the text of the Text view name, get the position of the element in arrayList, return that item
        holder.tvName.setText(menuData.get(position).getItemName());
        holder.tvDesc.setText(menuData.get(position).getItemdesc());
        holder.imageView.setImageResource(menuData.get(position).getImage());

    }

    @Override
    //number of items displayed in the recycler view
    public int getItemCount() {
        return menuData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // retreiving views from recyler_view layout

        ImageView imageView;
        TextView tvName, tvDesc;

        Button plus;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.foodimg);
            tvName = itemView.findViewById(R.id.foodname);
            tvDesc = itemView.findViewById(R.id.fooddesc);
            plus = itemView.findViewById(R.id.elevatedButton);

        }
    }
}

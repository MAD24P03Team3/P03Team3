package sg.edu.np.mad.NP_Eats_Team03;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.MyViewHolder>{
    private ArrayList<String> directions;
    private Context context;
    public DirectionsAdapter(ArrayList<String> directions, Context context) {
        this.directions = directions; this.context = context;
    }

    @NonNull
    @Override
    public DirectionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the recycler view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.directions_recycler, parent, false);
        return new DirectionsAdapter.MyViewHolder(v);

    };

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DirectionsAdapter.MyViewHolder holder, int position) {
        // Set all your things here

        String currentDirection = directions.get(position);
        String substring = currentDirection.substring(0,10);
        Toast.makeText(context,substring,Toast.LENGTH_SHORT).show();
        if ("Turn right".equals(substring) || "Walk right".equals(substring) || "Bear right".equals(substring)) {
            holder.directionInstruction.setText(currentDirection);
            holder.directionImage.setImageDrawable(context.getDrawable(R.drawable.right));
        }
        else {
            holder.directionImage.setImageDrawable(context.getDrawable(R.drawable.left));
            holder.directionInstruction.setText(currentDirection);
        }





        // here you set the random images i guess


    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView directionImage;
        TextView directionInstruction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            directionImage = itemView.findViewById(R.id.directionImage);
            directionInstruction = itemView.findViewById(R.id.directionInstruction);
        }
    }
}

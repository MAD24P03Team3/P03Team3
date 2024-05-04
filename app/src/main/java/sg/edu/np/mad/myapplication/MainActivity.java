package sg.edu.np.mad.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<String> rvData;
    LinearLayoutManager linearLayoutManager;
    RVAdapter rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.horizontalRV);

        rvData = new ArrayList<>();
        rvData.add("item 1");
        rvData.add("item 2");
        rvData.add("item 3");
        rvData.add("item 4");
        rvData.add("item 5");
        rvData.add("item 6");

        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        rvAdapter = new RVAdapter(rvData);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(rvAdapter);
    }
    class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyHolder>{
        ArrayList<String> data;
        public RVAdapter(ArrayList<String> data){
            this.data = data;

        }
        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_items,null,false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.tvTitle.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            TextView tvTitle;
            public MyHolder(@NonNull View itemView){
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
            }
        }
    }
}
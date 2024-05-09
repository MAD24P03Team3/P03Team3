package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class foodmenu extends AppCompatActivity {

    ArrayList<menuModel> menuData = new ArrayList<>();

    private void makeModel(){
        String[] menuNames = getResources().getStringArray(R.array.names_of_product);
        String [] menuDesc = getResources().getStringArray(R.array.desc);
        int img = R.drawable.img4;
        for(int i = 0; i<menuNames.length; i++){
            menuData.add(new menuModel(menuNames[i],menuDesc[i],img));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foodmenu);
        RecyclerView recyclerView = findViewById(R.id.recyclemenu);

        makeModel();

        //pass our menuData and our context (application)

        menu_recyclerviewAdapter adapter = new menu_recyclerviewAdapter(this,menuData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class fullmenu extends Fragment {



    FirebaseFirestore db = FirebaseFirestore.getInstance();



    private RecyclerView.LayoutManager layoutManager;







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ArrayList<Item> ma = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_fullmenu, container, false);
        RecyclerView rc = view.findViewById(R.id.recyclemenu);
        layoutManager = new LinearLayoutManager(getContext());
        rc.setLayoutManager(layoutManager);
        MenuAdapter menuAdapter = new MenuAdapter(ma);
        rc.setAdapter(menuAdapter);
        rc.setLayoutManager(layoutManager);
        MenuDatabase.retrieveMenuData(db,ma,menuAdapter);

        return  view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);









    }
}
package sg.edu.np.mad.NP_Eats_Team03;

import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.edu.np.mad.NP_Eats_Team03.R;


public class OwnerMenu extends Fragment {

    // TODO implement methods for data retrival in
    //  the bottomdialog modal page or a seperate class put it inside the class
    private static final int STORAGE_PERMISSION_CODE = 1;

        // specify the document to retrive item objects in a specific collection
        // handle data retrival  <Task> onSucess, onFailure


//    public void requestImagePermissions(Activity context){
//        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //TODO Dynamic views handled by recyclerview (method to fetch data ftom


        //TODO
        /*
            1. Implemnet a new class (recyclerviewAdapter)
            2. Instantiate a new recycleview which inflates the layout from ownermenuitem
            3. bind data to the recyclerview in the adapter in the (OnBindViewHolder)

        */


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ownermenu, container, false);
        Button add = v.findViewById(R.id.additems);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Clicked button");
                Activity context = getActivity();
//                displayDialog(context);
                bottomModal modal = new bottomModal();
                modal.show(getChildFragmentManager(), bottomModal.TAG);
            }
        });
        return v;

    }

}
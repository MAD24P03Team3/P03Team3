package sg.edu.np.mad.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<String> recycler_ItemList = new ArrayList<>();
        recycler_ItemList.add("item 1");
        recycler_ItemList.add("item 2");
        recycler_ItemList.add("item 3");
        recycler_ItemList.add("item 4");

        RecyclerView recyclerView_Item = View.findViewById(R.id.horizontalRV);
        ItemAdapter itemAdapter = new ItemAdapter(recycler_ItemList);
        LinearLayoutManager itemLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView_Item.setLayoutManager(itemLayoutManager);
        recyclerView_Item.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Item.setAdapter(itemAdapter);

        ArrayList<String> recycler_StoreList = new ArrayList<>();
        recycler_StoreList.add("Prata boy");
        recycler_StoreList.add("Oishii daily");
        recycler_StoreList.add("Coffee connect");
        recycler_StoreList.add("Acai den");

        RecyclerView recyclerView_Store = View.findViewById(R.id.storelistRV);
        StoreAdapter storeAdapter = new StoreAdapter(recycler_StoreList);
        LinearLayoutManager storeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView_Store.setLayoutManager(storeLayoutManager);
        recyclerView_Store.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Store.setAdapter(storeAdapter);

        return View;
    }
}
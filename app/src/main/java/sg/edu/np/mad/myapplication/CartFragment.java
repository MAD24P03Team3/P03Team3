package sg.edu.np.mad.myapplication;

import static java.lang.Math.round;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private static final String CART_KEY = "cart";

    CartAdapter cartAdapter;

    ArrayList<Item> cart;

    //RecyclerView recyclerView_CartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);

        /*if (savedInstanceState != null) {
            cart = savedInstanceState.getParcelableArrayList("cart");
        } else {
            cart = new ArrayList<Item>();
        }*/

        RecyclerView recyclerView = view.findViewById(R.id.contentRecycler);
        cartAdapter = new CartAdapter(cart = getCartItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        double subtotal = 0;
        for (Item item : cart) {
            subtotal += item.price;
        }
        TextView contentTotal = view.findViewById(R.id.contentTotal);
        contentTotal.setText("$" + Math.round(subtotal));

        /*cartAdapter.OnCartUpdatedListener() {
            @Override
            public void onCartUpdated(ArrayList<Item> cartArrayList) {
                double subtotal = 0;
                for (Item item : cartArrayList){
                    subtotal += item.price;
                }
                //TODO
                TextView contentTotal = view.findViewById(R.id.contentTotal);
                contentTotal.setText("$" + Math.round(subtotal));
            }
        });*/

        //OnCartUpdatedListener(CartArrayList);

        /*RecyclerView recyclerView = view.findViewById(R.id.contentRecycler);
        //CartAdapter userAdapter = new CartAdapter(CartArrayList);
        cartAdapter = new CartAdapter(new CartAdapter.OnCartUpdatedListener() {
                    @Override
                    public void onCartUpdated(ArrayList<Item> cartArrayList) {
                        double subtotal = 0;
                        for (Item item : cartArrayList){
                            subtotal += item.price;
                        }
                        //TODO
                        TextView contentTotal = view.findViewById(R.id.contentTotal);
                        contentTotal.setText("$" + Math.round(subtotal));
                }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        ArrayList<Item> CartArrayList = getCartItems();
        cartAdapter.updateCartList(CartArrayList);*/
    }

    private ArrayList<Item> getCartItems() {
        ArrayList<Item> cart = new ArrayList<>();
        cart.add(new Item("Store01", "Oishii Daily", "Sushi", 2.4));
        cart.add(new Item("Store01", "Oishii Daily", "Salmon", 3.4));
        cart.add(new Item("Store01", "Oishii Daily", "Tuna", 4.4));
        cart.add(new Item("Store01", "Oishii Daily", "Yuzu Tea", 1.2));
        return cart;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
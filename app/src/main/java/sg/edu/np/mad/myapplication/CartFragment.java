package sg.edu.np.mad.myapplication;

import static java.lang.Math.round;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private TextView contentTotal;
    private Button checkoutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        contentTotal = view.findViewById(R.id.contentTotal);
        recyclerView = view.findViewById(R.id.contentRecycler);
        checkoutButton = view.findViewById(R.id.contentButton01); // Added

        cartAdapter = new CartAdapter(new ArrayList<Item>(), new CartAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                cartViewModel.removeFromCart(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        cartViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                cartAdapter.updateCart(items);
            }
        });
        cartViewModel.getSubtotal().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                contentTotal.setText(String.format("$%.2f", total));
            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() { // Added
            @Override
            public void onClick(View v) {
                showCheckoutConfirmationDialog(); // Added
            }
        });
    }
    private void showCheckoutConfirmationDialog() { // Added
        new AlertDialog.Builder(getContext())
                .setTitle("Checkout Confirmation")
                .setMessage("Are you sure you want to checkout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addItemsToDatabase(); // Added
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void addItemsToDatabase() { // Added Todo
        // Add logic to add items to the database
        ArrayList<Item> items = cartViewModel.getCart().getValue();
        if (items != null) {
            // Example of adding items to Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for (Item item : items) {
                db.collection("Orders").add(item)
                        .addOnSuccessListener(documentReference -> {
                            // Handle success
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                        });
            }
            // Clear the cart after adding to the database
            //cartViewModel.clearCart(); // Added
        }
    }
}
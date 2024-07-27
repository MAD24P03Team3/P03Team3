package sg.edu.np.mad.NP_Eats_Team03;

import static java.lang.Math.round;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private TextView contentTotal;
    private Button checkoutButton;

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("CartFragement", "onCreateView: " + loadEmailFromSharedPreferences());
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        contentTotal = view.findViewById(R.id.contentTotal);
        recyclerView = view.findViewById(R.id.contentRecycler);
        checkoutButton = view.findViewById(R.id.contentButton01);

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
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckoutConfirmationDialog();
            }
        });
    }
    private void showCheckoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Checkout")
                .setMessage("Are you sure you want to checkout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String userEmail = loadEmailFromSharedPreferences();
                        cartViewModel.addToDatabase(userEmail, new Runnable() {
                            @Override
                            public void run() {
                                showToast("Order placed successfully");
                                cartViewModel.clearCart();
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                showToast("Failed to place order");
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private String loadEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("customer", Context.MODE_PRIVATE);
        Log.d("CartFragment", "loadEmailFromSharedPreferences: ");
        return sharedPreferences.getString("email", "No email found");
    }

    private void showToast(String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            //new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
        }
    }

}
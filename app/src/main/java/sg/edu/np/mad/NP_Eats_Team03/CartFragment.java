package sg.edu.np.mad.NP_Eats_Team03;

import static java.lang.Math.round;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private ImageView contentImage;
    private TextView contentTotal;
    private Button checkoutButton;
    private Button orderMoreButton;
    private TextView emptyCartMessage;
    private String selectedPaymentMethod;

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("CartFragment", "onCreateView: " + loadEmailFromSharedPreferences());
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);

        contentImage = view.findViewById(R.id.contentImage);
        contentTotal = view.findViewById(R.id.contentTotal);
        recyclerView = view.findViewById(R.id.contentRecycler);
        checkoutButton = view.findViewById(R.id.contentButton01);
        emptyCartMessage = view.findViewById(R.id.emptyCartMessage);
        orderMoreButton = view.findViewById(R.id.orderMoreButton);

        cartAdapter = new CartAdapter(new ArrayList<Item>(), new CartAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                cartViewModel.removeFromCart(position);
            }
        }, new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(int position, int quantity) {
                cartViewModel.updateQuantity(position, quantity);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // Not handling move actions
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the swiped item
                int position = viewHolder.getAdapterPosition();
                // Remove the item from the ViewModel
                cartViewModel.removeFromCart(position);
                // Notify the adapter about item removal
                cartAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                // Draw red background
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    if (dX < 0) { // Swiping left
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    }
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);
        cartViewModel.getCart().observe(getViewLifecycleOwner(), items -> {
            cartAdapter.updateCart(items);
            toggleEmptyCartMessage(items.isEmpty());
        });
//        cartViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<ArrayList<Item>>() {
//            @Override
//            public void onChanged(ArrayList<Item> items) {
//                cartAdapter.updateCart(items);
//            }
//        });
        cartViewModel.getSubtotal().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                contentTotal.setText(String.format("$%.2f", total));
            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentMethodDialog();
            }
        });

        orderMoreButton.setOnClickListener(v -> {
            Fragment orderMoreFragment = new MenuFragment(); // Replace OrderMoreFragment with your target fragment
            bottomNavigationView.setSelectedItemId(R.id.navigation_Menu);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, orderMoreFragment) // Replace fragment_container with the ID of the container where fragments are displayed
                    .addToBackStack(null) // Optional: add to back stack to enable back navigation
                    .commit();
        });
    }

    private void toggleEmptyCartMessage(boolean isEmpty) {
        if (isEmpty) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //contentTotal.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
            orderMoreButton.setVisibility(View.VISIBLE);
        } else {
            emptyCartMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            contentTotal.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            orderMoreButton.setVisibility(View.GONE);
        }
    }

    private void showPaymentMethodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_payment_method, null);
        builder.setView(dialogView)
                .setTitle("Select Payment Method")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup radioGroup = dialogView.findViewById(R.id.paymentMethodRadioGroup);
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        if (selectedId == -1) {
                            // No radio button selected
                            Toast.makeText(getContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
                        } else {
                            // A radio button is selected
                            RadioButton selectedRadioButton = dialogView.findViewById(selectedId);
                            selectedPaymentMethod = selectedRadioButton.getText().toString();
                            showCheckoutConfirmationDialog();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCheckoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Checkout")
                .setMessage("Are you sure you want to checkout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String userEmail = loadEmailFromSharedPreferences();
                        Log.d("CartFragment", "Email before calling addToDatabase: " + userEmail);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Log.d("CartFragment", "loadEmailFromSharedPreferences: " + sharedPreferences.getString(KEY_NAME, "No email found"));
        return sharedPreferences.getString(KEY_NAME, "No email found");
    }

    private void showToast(String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            //new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
        }
    }

}
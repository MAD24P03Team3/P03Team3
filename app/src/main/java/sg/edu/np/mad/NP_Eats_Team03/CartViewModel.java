package sg.edu.np.mad.NP_Eats_Team03;
import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.firestore.FieldPath;

import sg.edu.np.mad.NP_Eats_Team03.R;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Item>> cart;

    private static final String PREFS_NAME = "customer";
    private static final String KEY_NAME = "email";


    public CartViewModel() {
        cart = new MutableLiveData<>(new ArrayList<Item>());
        TestCart();
    }

    public LiveData<ArrayList<Item>> getCart() {
        return cart;
    }

    public void addToCart(Item item) {
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null) {
            currentCart.add(item);
            cart.setValue(currentCart);
        }
    }

    public void removeFromCart(int position) {
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null) {
            currentCart.remove(position);
            cart.setValue(currentCart);
        }
    }

    public LiveData<Double> getSubtotal() {
        return Transformations.map(cart, cartList -> {
            double subtotal = 0.0;
            for (Item item : cartList) {
                subtotal += item.getPrice();
            }
            return subtotal;
        });
    }

    public void clearCart() {
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null) {
            currentCart.clear();
            cart.setValue(currentCart);
        }
    }


    public void addToDatabase(String userEmail, Runnable onSuccess, Runnable onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Item> currentCart = cart.getValue();

        if (currentCart != null) {
            // Create a list to store item IDs
            ArrayList<String> itemIds = new ArrayList<>();

            for (Item item : currentCart) {
                itemIds.add(item.itemId); // Add itemId to the list
            }

            // Create the path to the document
            DocumentReference docRef = db.collection("Stores").document("Prata-Boy");

            // Use a final array to hold currentOrders to make it effectively final
            final List<Map<String, Object>>[] currentOrdersHolder = new List[1];

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                List<Map<String, Object>> currentOrders = new ArrayList<>();

                if (documentSnapshot.exists()) {
                    currentOrders = (List<Map<String, Object>>) documentSnapshot.get("currentOrders");

                    if (currentOrders == null) {
                        currentOrders = new ArrayList<>();
                    }
                }

                // Add a new map for the userEmail with the items
                Map<String, Object> newOrder = new HashMap<>();
                newOrder.put(userEmail, new HashMap<String, Object>() {{
                    put("items", itemIds);
                }});
                currentOrders.add(newOrder);

                // Store the modified currentOrders in the final array holder
                currentOrdersHolder[0] = currentOrders;

                // Update the document with the modified currentOrders array
                docRef.update("currentOrders", currentOrdersHolder[0])
                        .addOnSuccessListener(aVoid -> {
                            if (onSuccess != null) {
                                onSuccess.run();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // If update fails, try to set the document
                            docRef.set(new HashMap<String, Object>() {{
                                        put("currentOrders", currentOrdersHolder[0]);
                                    }}, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> {
                                        if (onSuccess != null) {
                                            onSuccess.run();
                                        }
                                    })
                                    .addOnFailureListener(e2 -> {
                                        if (onFailure != null) {
                                            onFailure.run();
                                        }
                                    });
                        });
            }).addOnFailureListener(e -> {
                if (onFailure != null) {
                    onFailure.run();
                }
            });
        }
    }








    public void TestCart() {
        ArrayList<Item> startcart = new ArrayList<>();

        cart.setValue(startcart);
    }
    //TODO:Add clear cart, Add cart to database
}

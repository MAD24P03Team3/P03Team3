package sg.edu.np.mad.NP_Eats_Team03;

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
        //TestCart();
    }

    public LiveData<ArrayList<Item>> getCart() {
        return cart;
    }

//    public void addToCart(Item item) {
//        ArrayList<Item> currentCart = cart.getValue();
//        if (currentCart != null) {
//            currentCart.add(item);
//            cart.setValue(currentCart);
//        }
//    }

    public void addToCart(Item item, int quantity) {
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null) {
            boolean itemExists = false;
            for (Item cartItem : currentCart) {
                if (cartItem.itemId.equals(item.itemId)) {
                    cartItem.quantity += quantity; // Update quantity if item already in cart
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                item.quantity = quantity; // Set initial quantity if item is new
                currentCart.add(item);
            }
            cart.setValue(currentCart);
        }
    }

    public LiveData<Double> getSubtotal() {
        return Transformations.map(cart, input -> {
            double total = 0;
            for (Item item : input) {
                total += item.price * item.quantity;
            }
            return total;
        });
    }

    public void removeFromCart(int position) {
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null) {
            currentCart.remove(position);
            cart.setValue(currentCart);
        }
    }

//    public LiveData<Double> getSubtotal() {
//        return Transformations.map(cart, cartList -> {
//            double subtotal = 0.0;
//            for (Item item : cartList) {
//                subtotal += item.getPrice();
//            }
//            return subtotal;
//        });
//    }

    public void updateQuantity(int position, int quantity) {
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null && position < currentCart.size()) {
            Item item = currentCart.get(position);
            item.quantity = quantity;
            cart.setValue(currentCart);
        }
    }

//    public void clearCart() {
//        ArrayList<Item> currentCart = cart.getValue();
//        if (currentCart != null) {
//            currentCart.clear();
//            cart.setValue(currentCart);
//        }
//    }

    public void clearCart() {
        cart.setValue(new ArrayList<>());
    }

//    public void addToDatabase(String userEmail, Runnable onSuccess, Runnable onFailure) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        ArrayList<Item> currentCart = cart.getValue();
//
//        if (currentCart != null) {
//            // Create a list to store item IDs
//            ArrayList<String> itemIds = new ArrayList<>();
//
//            for (Item item : currentCart) {
//                itemIds.add(item.itemId); // Add itemId to the list
//            }
//
//            // Create the path to the document
//            DocumentReference docRef = db.collection("Stores").document("Prata-Boy");
//
//            // Use a final array to hold currentOrders to make it effectively final
//            final List<Map<String, Object>>[] currentOrdersHolder = new List[1];
//
//            docRef.get().addOnSuccessListener(documentSnapshot -> {
//                List<Map<String, Object>> currentOrders = new ArrayList<>();
//                int largestOrderId = 0;
//
//                if (documentSnapshot.exists()) {
//                    currentOrders = (List<Map<String, Object>>) documentSnapshot.get("currentOrders");
//
//                    if (currentOrders == null) {
//                        currentOrders = new ArrayList<>();
//                    } else {
//                        // Find the largest order ID
//                        for (Map<String, Object> order : currentOrders) {
//                            for (Object value : order.values()) {
//                                if (value instanceof Map) {
//                                    Map<String, Object> orderDetails = (Map<String, Object>) value;
//                                    if (orderDetails.containsKey("orderId")) {
//                                        try {
//                                            int orderId = Integer.parseInt(orderDetails.get("orderId").toString());
//                                            if (orderId > largestOrderId) {
//                                                largestOrderId = orderId;
//                                            }
//                                        } catch (NumberFormatException e) {
//                                            // Ignore if not a number
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//                // Generate new order ID
//                int newOrderId = largestOrderId + 1;
//
//                // Add a new map for the userEmail with the items and orderId
//                Map<String, Object> newOrder = new HashMap<>();
//                newOrder.put(userEmail, new HashMap<String, Object>() {{
//                    put("orderId", newOrderId);
//                    put("items", itemIds);
//                }});
//                currentOrders.add(newOrder);
//
//                // Store the modified currentOrders in the final array holder
//                currentOrdersHolder[0] = currentOrders;
//
//                // Update the document with the modified currentOrders array
//                docRef.update("currentOrders", currentOrdersHolder[0])
//                        .addOnSuccessListener(aVoid -> {
//                            if (onSuccess != null) {
//                                onSuccess.run();
//                            }
//                        })
//                        .addOnFailureListener(e -> {
//                            // If update fails, try to set the document
//                            docRef.set(new HashMap<String, Object>() {{
//                                        put("currentOrders", currentOrdersHolder[0]);
//                                    }}, SetOptions.merge())
//                                    .addOnSuccessListener(aVoid -> {
//                                        if (onSuccess != null) {
//                                            onSuccess.run();
//                                        }
//                                    })
//                                    .addOnFailureListener(e2 -> {
//                                        if (onFailure != null) {
//                                            onFailure.run();
//                                        }
//                                    });
//                        });
//            }).addOnFailureListener(e -> {
//                if (onFailure != null) {
//                    onFailure.run();
//                }
//            });
//        }
//    }

    public void addToDatabase(String userEmail, Runnable onSuccess, Runnable onFailure) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Item> currentCart = cart.getValue();
        if (currentCart != null && !currentCart.isEmpty()) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("userEmail", userEmail);
            orderData.put("items", currentCart);
            orderData.put("total", getSubtotal().getValue());

            db.collection("orders")
                    .add(orderData)
                    .addOnSuccessListener(documentReference -> onSuccess.run())
                    .addOnFailureListener(e -> onFailure.run());
        }
    }

    public void TestCart() {
        ArrayList<Item> startcart = new ArrayList<>();
        cart.setValue(startcart);
    }
}


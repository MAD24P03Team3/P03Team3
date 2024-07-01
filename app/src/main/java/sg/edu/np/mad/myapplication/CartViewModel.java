package sg.edu.np.mad.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Item>> cart;

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

    public void TestCart() {
        ArrayList<Item> startcart = new ArrayList<>();

        cart.setValue(startcart);
    }
}

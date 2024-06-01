package sg.edu.np.mad.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    public void TestCart() {
        ArrayList<Item> startcart = new ArrayList<>();
        startcart.add(new Item("Store01", "Oishii Daily", "Sushi", 2.4));
        startcart.add(new Item("Store01", "Oishii Daily", "Salmon", 3.4));
        startcart.add(new Item("Store01", "Oishii Daily", "Tuna", 4.4));
        startcart.add(new Item("Store01", "Oishii Daily", "Yuzu Tea", 1.2));
        cart.setValue(startcart);
    }
}

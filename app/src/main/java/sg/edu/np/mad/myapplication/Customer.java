package sg.edu.np.mad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Customer extends User {
    private static Customer instance;
    String email;

    String password;
    ArrayList<Item> likes;
    ArrayList<Item> cart;
    ArrayList<Order> currentOrder;
    ArrayList<Order> orderHistory;
    Rewards rewards;

    String cid;

    private static final String PREFS_NAME = "CustomerPrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CID = "cid";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_CURRENT_ORDER = "current_order";
    private static final String KEY_ORDER_HISTORY = "order_history";

    // Private constructor to enforce singleton pattern
    private Customer(String name, String email, String password, String cid) {
        super(name,email);
        this.cid = cid;
        this.likes = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.currentOrder = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    // Singleton instance getter
    public static synchronized Customer getInstance(String name, String email, String password, String cid) {
        if (instance == null) {
            instance = new Customer(name, email, password, cid);
        }
        return instance;
    }

    // Method to initialize instance with SharedPreferences data
    public static synchronized void loadInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (instance == null) {
            String name = prefs.getString(KEY_NAME, null);
            String email = prefs.getString(KEY_EMAIL, null);
            String password = prefs.getString(KEY_PASSWORD, null);
            String cid = prefs.getString(KEY_CID, null);
            if (name != null && email != null && password != null && cid != null) {
                instance = new Customer(name, email, password, cid);
            }
        }
    }

    // Method to save instance to SharedPreferences
    public void saveInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_CID, cid);
        editor.apply();

    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}

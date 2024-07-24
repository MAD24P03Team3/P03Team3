package sg.edu.np.mad.NP_Eats_Team03;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class Customer extends User {
    private static Customer instance;
    String email;
    ArrayList<Item> likes;
    ArrayList<Item> cart;
    ArrayList<Order> currentOrder;
    ArrayList<Order> orderHistory;
    Rewards rewards;
    String cid;

    public static final String PREFS_NAME = "CustomerPrefs";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CID = "cid";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_CURRENT_ORDER = "current_order";
    public static final String KEY_ORDER_HISTORY = "order_history";

    // Private constructor to enforce singleton pattern
    public Customer(String name, String email, String password, String cid) {
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
    public static synchronized Customer loadInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_NAME, null);
        String email = prefs.getString(KEY_EMAIL, null);
        String password = prefs.getString(KEY_PASSWORD, null);
        String cid = prefs.getString(KEY_CID, null);
        if (name != null && email != null && password != null && cid != null) {
            instance = new Customer(name, email, password, cid);
            Log.d("Customer", "Loaded Customer: " + name + ", " + email + ", " + cid);
            return instance;
        }
        Log.d("Customer", "No customer data found in SharedPreferences");
        return null; // If no data found in SharedPreferences
    }

    // Method to save instance to SharedPreferences
    public void saveInstance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        //editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_CID, cid);
        editor.apply();

        Log.d("Customer", "Saved Customer: " + name + ", " + email + ", " + cid);
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}

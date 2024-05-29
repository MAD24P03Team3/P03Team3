package sg.edu.np.mad.myapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Customer extends User{
    private static Customer instance;
    private Customer currentCustomer;
    ArrayList<Item> likes;
    ArrayList<Item> cart;
    ArrayList<Order> currentOrder;
    ArrayList<Order> orderHistory;
    Rewards rewards;

    String cid;


    public Customer(String name, String studentId, String password, String cid) {
        super(name,studentId, password);
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public static synchronized Customer getInstance(String name, String studentId, String password, String cid) {
        if (instance == null) {
            instance = new Customer(name, studentId, password, cid);
        }
        return instance;
    }

    public Customer getCurrrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrrentCustomer(Customer customer) {
        Customer currrentCustomer = customer;
    }

    /*USAGE
        Customer currentCustomer = Customer.getInstance();
        currentCustomer.setCurrrentCustomer(Customer);
        String data = currentCustomer.getCurrrentCustomer();

     */


}

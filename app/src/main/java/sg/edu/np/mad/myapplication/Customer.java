package sg.edu.np.mad.myapplication;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Customer extends User{
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


}

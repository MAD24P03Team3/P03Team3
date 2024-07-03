package sg.edu.np.mad.NP_Eats_Team03;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Order {
    int orderID;
    int storeID;
    String remarks;
    DateTimeFormatter timeReceived;
    DateTimeFormatter timeFulfilled;
    ArrayList<Item> itemList;

    public Order(int input_orderID, int input_storeID, String input_remarks){
        orderID = input_orderID;
        storeID = input_storeID;
        remarks = input_remarks;
    }
}

package sg.edu.np.mad.myapplication;

import java.util.Date;

public class Voucher {
    int storeID;
    int discount;
    Date valid;
    Date expiry;
    String description;

    public Voucher(int input_storeID, int input_discount, Date input_valid, Date input_expiry, String input_description){
        storeID = input_storeID;
        discount = input_discount;
        valid = input_valid;
        expiry = input_expiry;
        description = input_description;
    }
}

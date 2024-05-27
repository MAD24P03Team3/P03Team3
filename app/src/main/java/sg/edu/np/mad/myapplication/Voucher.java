package sg.edu.np.mad.myapplication;

import java.util.Date;

public class Voucher {
    String storeID;
    String voucherID;
    double discount;
    Date valid;
    Date expiry;
    String description;

    public Voucher(String input_storeID, String input_voucherID, double input_discount, Date input_valid, Date input_expiry, String input_description){
        storeID = input_storeID;
        voucherID = input_voucherID;
        discount = input_discount;
        valid = input_valid;
        expiry = input_expiry;
        description = input_description;
    }
}

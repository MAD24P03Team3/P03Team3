package sg.edu.np.mad.myapplication;

public class Item {
    String storeID;
    String name;
    String description;
    int quantity;
    double price;

    public Item(String input_storeID, String input_name, String input_description, double input_price){
        storeID = input_storeID;
        name = input_name;
        description = input_description;
        price = input_price;

    }
}

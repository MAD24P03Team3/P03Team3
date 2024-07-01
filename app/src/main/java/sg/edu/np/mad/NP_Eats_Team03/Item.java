package sg.edu.np.mad.NP_Eats_Team03;

public class Item {
    String storeID;

    String itemId;
    String name;
    String description;
    int quantity;
    double price;

    public Item(String input_itemID, String input_name, String input_description, double input_price){
        itemId = input_itemID;
        name = input_name;
        description = input_description;
        price = input_price;
    }

    public double getPrice() {
        return price;
    }
}

package sg.edu.np.mad.myapplication;

public class Item {
    int storeID;
    String name;
    String description;
    int quantity;

    public Item(int input_storeID, String input_name, String input_description, int input_quantity){
        storeID = input_storeID;
        name = input_name;
        description = input_description;
        quantity = input_quantity;
    }
}

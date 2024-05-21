package sg.edu.np.mad.myapplication;

public class MenuModel {
    String itemdesc;
    String itemName;

    Double Price;

    int image;


    public MenuModel(String itemName, String itemdesc, Double price) {
        this.itemdesc = itemdesc;
        this.itemName = itemName;
        this.Price = price;
    }

    // create accessors

    public String getItemdesc(){
        return itemdesc;
    }

    public String getItemName(){
        return itemName;
    }

    public int getImage() {
        return image;
    }
}

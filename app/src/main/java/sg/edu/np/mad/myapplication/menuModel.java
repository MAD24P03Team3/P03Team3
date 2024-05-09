package sg.edu.np.mad.myapplication;

public class menuModel {
    String itemdesc;
    String itemName;

    int image;


    public menuModel(String itemName, String itemdesc, int image) {
        this.itemdesc = itemdesc;
        this.itemName = itemName;
        this.image = image;
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

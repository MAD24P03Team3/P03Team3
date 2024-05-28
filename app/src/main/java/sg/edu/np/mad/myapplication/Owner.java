package sg.edu.np.mad.myapplication;

class Owner extends User {
    int storeID;
    Item item;
    public Owner(String name, String input_studentId, String input_password, int input_storeID, Item input_item) {
        super(name, input_studentId, input_password);
        storeID = input_storeID;
        item = input_item;
    }
}

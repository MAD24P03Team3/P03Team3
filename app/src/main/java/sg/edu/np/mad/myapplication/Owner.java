package sg.edu.np.mad.myapplication;

class Owner extends User {
    int storeID;
    Item item;
    public Owner(String name, String input_studentId, int input_storeID, Item input_item) {
        super(name, input_studentId);
        storeID = input_storeID;
        item = input_item;
    }
}

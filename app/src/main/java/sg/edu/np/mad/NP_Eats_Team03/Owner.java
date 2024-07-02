package sg.edu.np.mad.NP_Eats_Team03;

class Owner extends User {

    private static final String PREFS_NAME = "CustomerPrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CID = "cid";

    int storeID;
    public Owner(String name, String input_studentId, int input_storeID) {
        super(name, input_studentId);
        storeID = input_storeID;
    }

}

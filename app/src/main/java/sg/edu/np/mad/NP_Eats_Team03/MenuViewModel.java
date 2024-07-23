package sg.edu.np.mad.NP_Eats_Team03;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MenuViewModel extends ViewModel {
    private FireStoreHandler dbHandler;
    private static final String TAG = "MyViewModel";

    private MutableLiveData<ArrayList<Item>> menuItems;

    public MenuViewModel(FireStoreHandler dbHandler, MutableLiveData<ArrayList<Item>> menuItems){
        this.dbHandler = dbHandler;
        this.menuItems = menuItems;
    }

    // Getter method for MutableLiveData
    public MutableLiveData<ArrayList<Item>> getMenuItemsLiveData() {
        return menuItems;
    }

    // handle the data retrieval
    public void getMenuData(String collectionPath, String documentName){
        dbHandler.retriveData(collectionPath, documentName, new HandleDataRead(){
            @Override
            public void onSuccess(ArrayList<Item> listofitmes) {

                menuItems.postValue(listofitmes);
            }

            @Override
            public void onFailure(String logmessage) {
                // where we log the message
                Log.v(TAG,logmessage);
            }
        });
    }

    public void writeLikesData(String collectionPath, String email, Item item){
       dbHandler.writeDataLikes(collectionPath,email,item,new HandleDataWrite(){

           @Override
           public void onFailure(String message) {
                Log.v(TAG,message);
           }

           @Override
           public void onSucess(String message) {
               Log.v(TAG,message);
           }
       });
    }

}

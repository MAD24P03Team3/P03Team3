package sg.edu.np.mad.NP_Eats_Team03;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MenuViewModelFactory implements ViewModelProvider.Factory{
    private FireStoreHandler dbHandler;
    private MutableLiveData<ArrayList<Item>> menuItems;

    public MenuViewModelFactory(FireStoreHandler dbHandler, MutableLiveData<ArrayList<Item>> menuItems) {
        this.dbHandler = dbHandler;
        this.menuItems = menuItems;
        // Initialize other ViewModel logic here if needed
    }



    // ViewModel methods and logic here

    //   t is a placeholder for the MenuViewModel parameters
    // This mehod is to instantiate a new MenuViewModel class as it is a subclass of the superclass MenuViewModel
    // ViewModel factory is needed as ViewModelProviders do not create or instantiate ViewModels as they do not specify the parameters for the object

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MenuViewModel.class)) {
            //noinspection unchecked
            return (T) new MenuViewModel(dbHandler, menuItems);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

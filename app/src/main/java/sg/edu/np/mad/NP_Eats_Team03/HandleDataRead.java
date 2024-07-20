package sg.edu.np.mad.NP_Eats_Team03;

import java.util.ArrayList;

public interface HandleDataRead {
    void onSuccess(ArrayList<Item> listofitmes);
    void onFailure(String logmessage);
}

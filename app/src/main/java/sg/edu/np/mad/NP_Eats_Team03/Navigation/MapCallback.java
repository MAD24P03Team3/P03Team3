package sg.edu.np.mad.NP_Eats_Team03.Navigation;
import retrofit2.Callback;
import retrofit2.Response;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
public interface MapCallback {
    public void responseReturn(Response<DirectionsResponse> response);
}

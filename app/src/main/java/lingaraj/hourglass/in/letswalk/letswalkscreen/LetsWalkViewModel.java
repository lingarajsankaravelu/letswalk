package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.location.Address;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class LetsWalkViewModel extends androidx.lifecycle.ViewModel {


  private final Repository repository;
  private MutableLiveData<Address> decodedAddress = new MutableLiveData<Address>();
  private MutableLiveData<Boolean> network_loader = new MutableLiveData<>();
  private MutableLiveData<String> messages = new MutableLiveData<>();
  private MutableLiveData<List<PolylineOptions>> poly_lines = new MutableLiveData<>();

  public MutableLiveData<Address> getDecodedAddress() {
    return decodedAddress;
  }

  public MutableLiveData<Boolean> getNetwork_loader() {
    return network_loader;
  }

  public MutableLiveData<String> getMessages() {
    return messages;
  }

  public MutableLiveData<List<PolylineOptions>> getPoly_lines() {
    return poly_lines;
  }

  @Inject
  public LetsWalkViewModel(Repository repository) {
    this.repository = repository;
  }




  public void drawRoute(Map<Integer,Marker> markerMap,String apiKey){
    network_loader.postValue(true);
    repository.parseRouteInformation(markerMap,apiKey ,new RepoCallbacks() {
      @Override public void onSuccess(List<PolylineOptions> polyLines) {
        poly_lines.postValue(polyLines);
        network_loader.postValue(false);
      }

      @Override public void onFailure() {
        network_loader.postValue(false);
        messages.postValue( "Error Connecting to network! Try again");

      }
    });
  }

  public void decodeMarkerLocation(double latitude, double longitude) {
    repository.reverseGeocode(latitude, longitude, new GeoDecoderCallback() {
      @Override
      public void decodedAddressDetails(Address address) {
        Log.d("ViewModel:",new Gson().toJson(address));
        decodedAddress.postValue(address);
      }
    });

  }
}

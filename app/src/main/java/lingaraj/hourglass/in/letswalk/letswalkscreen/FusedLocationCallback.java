package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.location.Location;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class FusedLocationCallback extends LocationCallback {

  private final LocationResultCallbacks callbacks;


  public FusedLocationCallback(LocationResultCallbacks callbacks) {
     this.callbacks = callbacks;
  }

  @Override public void onLocationResult(LocationResult locationResult) {
    super.onLocationResult(locationResult);
    this.callbacks.onLocationResult(locationResult);
  }

  @Override
  public void onLocationAvailability(LocationAvailability locationAvailability) {
    super.onLocationAvailability(locationAvailability);
    this.callbacks.onLocationAvailability(locationAvailability);
  }

   interface LocationResultCallbacks {
     void onLocationChanged(Location location);

     void onLocationResult(LocationResult locationResult);
    void onLocationAvailability(LocationAvailability locationAvailability);
  }




}

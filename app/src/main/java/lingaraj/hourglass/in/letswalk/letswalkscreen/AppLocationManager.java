package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import lingaraj.hourglass.in.letswalk.utils.MarshmallowPermissions;

public class AppLocationManager extends LiveData<Location>
    implements
    GoogleApiClient.ConnectionCallbacks,
    FusedLocationCallback.LocationResultCallbacks {


  private Activity activity;
  private final String TAG = "FUSED_LOC";
  private final LocationRequest location_request;
  private final LocationRequestCallback loc_request_callback;
  private final FusedLocationCallback fusedLocationCallback;
  private final GoogleApiClient google_api_client;
  private  FusedLocationProviderClient fused_loc_provider = null;
  private final LocationSettingsRequest.Builder builder;
  private boolean is_loc_available = false;


  public AppLocationManager(Activity activity,LocationRequestCallback locationRequestCallback){
    this.activity = activity;
    this.loc_request_callback = locationRequestCallback;
    this.fusedLocationCallback = new FusedLocationCallback(this);
    this.google_api_client = new GoogleApiClient.Builder(activity)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .build();
    this.location_request = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(30 * 1000)
        .setFastestInterval(5 * 1000);
    this.builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(location_request)
        .setAlwaysShow(true);
    Log.d(TAG,"Constructor called");

  }




  @Override
  protected void onActive() {
    if (!google_api_client.isConnected()){
      google_api_client.connect();
    }
    else {
      requestLocationUpdate();
    }
  }



  @Override
  protected void onInactive() {
    if (google_api_client.isConnected()) {
      this.is_loc_available = false;
      if (this.fused_loc_provider!=null){
        this.fused_loc_provider.removeLocationUpdates(fusedLocationCallback);
        this.fused_loc_provider = null;
      }
      this.google_api_client.disconnect();
    }
  }
  @Override
  public void onConnected(@Nullable Bundle connectionHint) {
    Log.d(TAG, "connected to google api client");
    if (is_loc_available){
     requestLocationUpdate();
    }
    else {
      promptUserLocationRequest();
    }
  }

  public void requestLocationUpdate() {
    if(is_loc_available && hasActiveObservers() &&  google_api_client.isConnected() && fused_loc_provider==null) {
      fused_loc_provider = new FusedLocationProviderClient(activity);
      fused_loc_provider.requestLocationUpdates(location_request,fusedLocationCallback, Looper.myLooper());
    }

  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d(TAG,"Location Changed:"+"Lat/Lng:"+location.getLatitude()+"/"+location.getLongitude());
    setValue(location);
  }


  @Override
  public void onConnectionSuspended(int cause) {
    Log.w(TAG, "On Connection suspended " + cause);
  }

  @Override public void onLocationResult(LocationResult locationResult) {
    //Fused Location Callback
   Location location =  locationResult.getLastLocation();
   if (location!=null){
     Log.d(TAG,"Last Know Location");
     setValue(location);
   }

  }

  @Override public void onLocationAvailability(LocationAvailability locationAvailability) {
    is_loc_available = locationAvailability.isLocationAvailable();
    if (!is_loc_available){
      promptUserLocationRequest();

    }
    else {
      requestLocationUpdate();
    }
    Log.d(TAG,"Location Availability:"+is_loc_available);
  }

  private void promptUserLocationRequest() {
    if (!MarshmallowPermissions.arePermissionsGranted(activity)){
      Log.d(TAG,"Location Access Permission yet to be provided by user, discarding user location request");
      return;
    }

    Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this.activity).checkLocationSettings(builder.build());
    task.addOnFailureListener(new OnFailureListener() {
      @Override public void onFailure(@NonNull Exception e) {
        Log.d(TAG,"Location request failed");
        loc_request_callback.onLocationRequestCancelled();
        onInactive();
      }
    });
    task.addOnCanceledListener(new OnCanceledListener() {
      @Override public void onCanceled() {
        Log.d(TAG,"Location request cancelled by user");
        loc_request_callback.onLocationRequestCancelled();
        onInactive();
      }
    });
    task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
      @Override public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        try {
          LocationSettingsResponse response = task.getResult(ApiException.class);
          if (response!=null && response.getLocationSettingsStates().isLocationUsable()){
            is_loc_available = response.getLocationSettingsStates().isLocationUsable();
            requestLocationUpdate();
          }
          else {
            is_loc_available = false;
            loc_request_callback.onLocationRequestCancelled();
          }
        }
        catch (ApiException exception){
          if (exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
            try {
              ResolvableApiException resolvable = (ResolvableApiException) exception;
              resolvable.startResolutionForResult(activity, LetsWalkActivity.REQUEST_LOCATION);
            }
            catch (Exception e){
              Log.d(TAG,e.toString());
            }
          }
          onInactive();
        }
      }
    });

    Log.d(TAG,"Prompted User Location");

  }
}
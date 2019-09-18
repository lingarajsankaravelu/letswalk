package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pnikosis.materialishprogress.ProgressWheel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import lingaraj.hourglass.in.letswalk.Constants;
import lingaraj.hourglass.in.letswalk.di.BaseViewModelFactory;
import lingaraj.hourglass.in.letswalk.di.injectors.Injector;
import lingaraj.hourglass.in.letswalk.di.injectors.FragmentInjector;
import lingaraj.hourglass.in.letswalk.utils.MarshmallowPermissions;
import lingaraj.hourglass.in.ridegmap.R;

public class LetsWalkActivity extends FragmentActivity implements
    LocationRequestCallback,
    OnMapReadyCallback,
     GoogleMap.OnMapLongClickListener,
    GoogleMap.OnMarkerDragListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

  public static final int REQUEST_LOCATION = 199;
  private final String TAG = "MAPSACT";
  private int marker_count = 1;
  private Map<Integer, Marker> marker_map = new HashMap<>();
  private SupportMapFragment map_fragment;
  private GoogleMap mMap;
  private LatLng previous_know_loc = null;
  private LetsWalkViewModel view_model;
  private String map_api_key = null;
  private ProgressDialog progress;
  private AppLocationManager loc_manager;
  private ViewSwitcher view_switcher;
  private ProgressWheel wheel;
  private TextView error;
  private Button retry;
  private FrameLayout bottom_sheet;
  @Inject FragmentInjector fragmentInjector;
  @Inject BaseViewModelFactory view_model_factory;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    Injector.inject(this);
    loc_manager = new AppLocationManager(this, this);
    map_api_key = this.getString(R.string.google_maps_key);
    view_model = view_model_factory.create(LetsWalkViewModel.class);
    init();
    setMapFragment();
    setBottomSheet();
    showLoader();
    showMap();
    setLiveDataRelays();
    setViewModelRelays();
  }

  private void showMap() {
    if (view_switcher.getDisplayedChild()==0){
      view_switcher.showNext();

      Log.d(TAG,"Showing Map");


    }

  }

  private void setLiveDataRelays() {
    loc_manager.observe(this, new Observer<Location>() {
      @Override public void onChanged(Location location) {
           if (location==null){
             Log.d(TAG,"Recieved Null as response");
           }
           else {
             showMap();
             setLastKnowLocation(location);
           }
      }
    });
  }

  private void setViewModelRelays() {
    view_model.getMessages().observe(this, new Observer<String>() {
      @Override public void onChanged(String message) {
        displayMessage(message);
      }
    });

    view_model.getNetwork_loader().observe(this, new Observer<Boolean>() {
      @Override public void onChanged(Boolean showNetworkLoader) {
        if (showNetworkLoader){
           showDefaultProgressLoader();
        }
        else {
            hideDefaultProgressLoader();
        }
      }
    });
    view_model.getPoly_lines().observe(this, new Observer<List<PolylineOptions>>() {
      @Override public void onChanged(List<PolylineOptions> polylineOptions) {
        drawPathOnMap(polylineOptions);
      }

    });

  }

  private void displayMessage(String message) {
    Toast.makeText(LetsWalkActivity.this,message,Toast.LENGTH_SHORT).show();
  }

  private void hideDefaultProgressLoader() {
    this.progress.dismiss();
  }

  private void showDefaultProgressLoader() {
     if (this.progress!=null){
       this.progress.show();
     }
     Log.d(TAG,"Showing Progress Loader");
  }

  private void drawPathOnMap(List<PolylineOptions> polylineOptions) {
    for (PolylineOptions path :polylineOptions ) {
       mMap.addPolyline(path);
    }
  }

  private void init() {
    map_fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_for_ride);
    view_switcher = findViewById(R.id.view_switcher);
    wheel = findViewById(R.id.wheel);
    error = findViewById(R.id.error);
    retry = findViewById(R.id.retry);
    bottom_sheet = findViewById(R.id.bottom_sheet_frame);
    progress = new ProgressDialog(this);
    progress.setMessage("Getting things ready");
    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

  }

  private void showLoader(){
    wheel.setVisibility(View.VISIBLE);
    error.setVisibility(View.GONE);
    retry.setVisibility(View.GONE);
    if (view_switcher.getDisplayedChild()==1){
      view_switcher.showPrevious();
    }
    Log.d(TAG,"Show Loader");
  }

  private void checkRequiredPermission() {
    if (MarshmallowPermissions.arePermissionsGranted(this)) {
     loc_manager.onActive();
    }
    else {
      MarshmallowPermissions.requestPermissions(this);
    }
  }


  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setOnMapLongClickListener(this);
    mMap.setOnMarkerDragListener(this);
    checkRequiredPermission();
  }

  private void setLastKnowLocation(Location location) {
     this.previous_know_loc = new LatLng(location.getLatitude(),location.getLongitude());
     if (mMap!=null){
       mMap.setMyLocationEnabled(true);
       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(previous_know_loc,16f));
       Log.d(TAG,"Last Known location updated");
     }
  }


  @Override public void onMarkerDragStart(Marker marker) {

  }

  @Override public void onMarkerDrag(Marker marker) {

  }

  @Override public void onMarkerDragEnd(Marker marker) {
      Integer marker_no = (Integer) marker.getTag();
      if (marker_no!=null){
        marker_map.put(marker_no,marker);
        Log.d(TAG,"Marker Dragged Ended and map updated for Marker:"+marker_no);
      }
  }

  private void calculateWalkingRoute() {
    if (marker_count>2){
      AlertDialog.Builder builder = new AlertDialog.Builder(LetsWalkActivity.this);

      builder.setMessage("Do you wish to get Walking Direction?");

      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which){
          view_model.drawRoute(marker_map,map_api_key);
          dialog.dismiss();
        }
      });

      builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which){
           dialog.dismiss();
        }
      });
      builder.show();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    // If request is cancelled, the result arrays are empty.
    if (requestCode == MarshmallowPermissions.LOCATION_PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        loc_manager.onInactive();
        loc_manager.onActive();
      }
      else {
        showPermissionError();
      }
    }

  }

  private void setMapFragment(){
    if (map_fragment!=null){
      map_fragment.getMapAsync(this);
    }


  }

  private void showPermissionError(){
    wheel.setVisibility(View.GONE);
    error.setVisibility(View.VISIBLE);
    retry.setVisibility(View.VISIBLE);
    if (view_switcher.getDisplayedChild()==1){
      view_switcher.showPrevious();
    }
    Log.d(TAG,"Showing Error");
  }

  public void retry(View view) {
    Log.d(TAG,"Retry");
    checkRequiredPermission();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_LOCATION) {
      switch (resultCode) {
        case Activity.RESULT_OK: {
          loc_manager.onActive();
          break;
        }
        case Activity.RESULT_CANCELED: {
          showPermissionError();
          break;
        }
        default: {
          break;
        }
      }
    }
  }

  @Override
  public void onLocationRequestCancelled() {
    showPermissionError();
  }

  @Override
  public void onMapLongClick(LatLng latLng) {
    marker_count++;
    Marker marker = mMap.addMarker(new MarkerOptions()
        .position(new LatLng(latLng.latitude,latLng.longitude))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .draggable(true));
    marker.showInfoWindow();
    marker.setTag(marker_count);
    marker_map.put(marker_count,marker);
    view_model.decodeMarkerLocation(latLng.latitude,latLng.longitude);
    calculateWalkingRoute();
    Log.d(TAG,"Lat/Lng:"+marker.getPosition());

  }

  private void setBottomSheet() {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    FavouriteLocationsFragment favourites_frag = new FavouriteLocationsFragment();
    fragmentTransaction.add(bottom_sheet.getId(),favourites_frag).commit();
    Log.d(TAG,"Frag loaded to bottom sheet");
    bottom_sheet.setVisibility(View.VISIBLE);

  }

  public FragmentInjector getFragmentInjector() {
    return fragmentInjector;
  }


}

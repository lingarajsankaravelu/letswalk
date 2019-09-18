package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.patloew.rxlocation.RxLocation;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import javax.inject.Inject;
import lingaraj.hourglass.in.letswalk.letswalkscreen.parser.PolyLineParser;
import lingaraj.hourglass.in.letswalk.rest.DirectionAPIEndpoints;

public class Repository {

  private final String TAG = this.getClass().getSimpleName();
  private RxLocation rxLocation;
  private final String MODE = "walking";
  private Disposable disposable;
  private final DirectionAPIEndpoints maps_api;

  @Inject
  public Repository(RxLocation rxLocation,DirectionAPIEndpoints endpoints) {
    this.rxLocation = rxLocation;
    this.maps_api =  endpoints;
  }




  public void parseRouteInformation(Map<Integer, Marker> markerMap, String apiKey,final RepoCallbacks repoCallbacks){

    List<Integer> markers = new ArrayList<Integer>(markerMap.keySet());
    Collections.sort(markers);
    int size = markers.size();
    String origin = latLngToString(markerMap.get(markers.get(0)).getPosition());
    String destination = latLngToString(markerMap.get(markers.get(size-1)).getPosition());
    StringBuilder way_points_builder = new StringBuilder();
    for (int i = 1; i < size - 1; i++) {
      LatLng latLng = markerMap.get(markers.get(i)).getPosition();
     /* if (i>1) {
        way_points_builder.append(",");
      }
      else {
        way_points_builder.append("\"").append(latLng.latitude).append(",").append(latLng.longitude).append("\"");

      }*/
     way_points_builder.append(latLng.toString());

    }

    Log.d(TAG,"Way Points:\n"+way_points_builder.toString());
    disposable = maps_api.getJson(origin, destination, way_points_builder.toString(), MODE,apiKey)
        .map(PolyLineParser::new)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<PolyLineParser>() {
          @Override public void onSuccess(PolyLineParser polyLineParser) {
            repoCallbacks.onSuccess(polyLineParser.getPolyLines());
          }

          @Override public void onError(Throwable e) {
             repoCallbacks.onFailure();
          }
        });

  }



  public void reverseGeocode(double lat, double lng,final GeoDecoderCallback callback) {
    Log.d(TAG,"Preparing to reverse geo Decode:"+"Lat/Lng"+lat+"/"+lng);

     disposable = rxLocation.geocoding()
        .fromLocation(lat,lng)
        .observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(address -> {
          Log.d(TAG,"Address recieved for lat lng");
          callback.decodedAddressDetails(address);},throwable -> {
          Log.d(TAG,"Unable to process address for lat lng:"+throwable.toString());
        });
    /*
   disposable =  Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
       List<Address> addresses =  geo_coder.getFromLocation(lat, lng, 1);
       int len = addresses.size();
       Address address = addresses.get(len-1);
       Log.d(TAG,new Gson().toJson(address));
       callback.decodedAddressDetails(address);
       }
    }).observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(()->{Log.d(TAG,"Reverse GeoCode ran to completion");},throwable -> {
          Log.d(TAG,throwable.toString());
        }); */
  }


  private String latLngToString(LatLng latLng){
    return String.valueOf(latLng.latitude+","+latLng.longitude);

  }

  public void clear(){
    if (this.disposable!=null){
      this.disposable.dispose();
    }
  }







}

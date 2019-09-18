package lingaraj.hourglass.in.letswalk.letswalkscreen.parser;

import android.graphics.Color;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;
import lingaraj.hourglass.in.letswalk.GoogleAPIStatusException;
import lingaraj.hourglass.in.letswalk.rest.response.DirectionResults;

public class PolyLineParser {


  private final String TAG = this.getClass().getSimpleName();
  private List<PolylineOptions> poly_lines = new ArrayList<>();

  public List<PolylineOptions> getPolyLines() {
    return poly_lines;
  }

  public PolyLineParser(DirectionResults directionResults) throws GoogleAPIStatusException {
    String STATUS_OK = "OK";
    if (directionResults.getStatus().equals(STATUS_OK)){
      parse(directionResults);
    }
    else {
      throw  new GoogleAPIStatusException();
    }

  }

  private void parse(DirectionResults directionResults) {
         if (directionResults.getRoutes().size()>=1){
           String poly_line_points = directionResults.getRoutes().get(0).getOverviewPolyLine().getPoints();
           List<LatLng> latLngs = decodePoly(poly_line_points);
           for (int z = 0; z < latLngs.size() - 1; z++) {
             LatLng src = latLngs.get(z);
             LatLng dest = latLngs.get(z + 1);
             PolylineOptions poly_line_option =new PolylineOptions()
                 .add(new LatLng(src.latitude, src.longitude),
                     new LatLng(dest.latitude, dest.longitude))
                 .width(5).color(Color.GREEN).geodesic(true);
             poly_lines.add(poly_line_option);
           }
         }
  }

  private List<LatLng> decodePoly(String encoded) {

    List<LatLng> poly = new ArrayList<LatLng>();
    int index = 0, len = encoded.length();
    int lat = 0, lng = 0;

    while (index < len) {
      int b, shift = 0, result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lat += dlat;

      shift = 0;
      result = 0;
      do {
        b = encoded.charAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);
      int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
      lng += dlng;

      LatLng p = new LatLng((((double) lat / 1E5)),
          (((double) lng / 1E5)));
      poly.add(p);
    }

    return poly;
  }

}

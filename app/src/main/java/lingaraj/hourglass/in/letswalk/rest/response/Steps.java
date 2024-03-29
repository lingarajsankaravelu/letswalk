package lingaraj.hourglass.in.letswalk.rest.response;

import java.io.Serializable;

public class Steps implements Serializable {
  private Location start_location;
  private Location end_location;
  private OverviewPolyLine polyline;

  public Location getStart_location() {
    return start_location;
  }

  public Location getEnd_location() {
    return end_location;
  }

  public OverviewPolyLine getPolyline() {
    return polyline;
  }
}

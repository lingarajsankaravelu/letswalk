package lingaraj.hourglass.in.letswalk.rest.response.distanceresponse;

import java.io.Serializable;

public class Element implements Serializable {

  private Distance distance;
  private Distance duration;
  private String status;

  public Distance getDistance() {
    return distance;
  }

  public Distance getDuration() {
    return duration;
  }

  public String getStatus() {
    return status;
  }
}

package lingaraj.hourglass.in.letswalk.rest.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class OverviewPolyLine implements Serializable {

  @SerializedName("points")
  public String points;

  public String getPoints() {
    return points;
  }
}

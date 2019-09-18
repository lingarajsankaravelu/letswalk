package lingaraj.hourglass.in.letswalk.rest.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class DirectionResults implements Serializable {

  private String status;
  @SerializedName("routes")
  private List<Route> routes;

  public String getStatus() {
    return status;
  }


  public List<Route> getRoutes() {
    return routes;
  }}




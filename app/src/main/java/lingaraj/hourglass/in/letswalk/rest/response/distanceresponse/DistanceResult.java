package lingaraj.hourglass.in.letswalk.rest.response.distanceresponse;

import java.io.Serializable;
import java.util.List;

public class DistanceResult implements Serializable {
  private String status;
  private List<String> destination_address;
  private List<String> origin_addresses;
  private List<Row> rows;

  public String getStatus() {
    return status;
  }

  public List<String> getDestination_address() {
    return destination_address;
  }

  public List<String> getOrigin_addresses() {
    return origin_addresses;
  }

  public List<Row> getRows() {
    return rows;
  }
}

package lingaraj.hourglass.in.letswalk.rest.response.distanceresponse;

import java.io.Serializable;

class Distance implements Serializable {
  private String text;
  private long value;

  public String getText() {
    return text;
  }

  public long getValue() {
    return value;
  }
}

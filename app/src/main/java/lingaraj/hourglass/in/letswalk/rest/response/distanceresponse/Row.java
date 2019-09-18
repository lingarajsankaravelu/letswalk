package lingaraj.hourglass.in.letswalk.rest.response.distanceresponse;

import java.io.Serializable;
import java.util.List;

public class Row implements Serializable {
  private List<Element> elements;

  public List<Element> getElements() {
    return elements;
  }
}

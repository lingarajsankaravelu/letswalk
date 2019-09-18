package lingaraj.hourglass.in.letswalk.rest.response;

import java.io.Serializable;
import java.util.List;

public class Legs implements Serializable {
  private List<Steps> steps;

  public List<Steps> getSteps() {
    return steps;
  }
}


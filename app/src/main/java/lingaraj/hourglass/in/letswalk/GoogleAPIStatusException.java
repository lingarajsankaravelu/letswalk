package lingaraj.hourglass.in.letswalk;

public class GoogleAPIStatusException extends Exception {
  public GoogleAPIStatusException() {
    super("GOOGLE MAPS API Status does not return okay");
  }
}

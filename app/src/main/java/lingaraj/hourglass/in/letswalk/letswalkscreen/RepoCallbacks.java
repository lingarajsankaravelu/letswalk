package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.location.Address;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.List;

public interface RepoCallbacks {
  void onSuccess(List<PolylineOptions> polyLines);
  void onFailure();
}

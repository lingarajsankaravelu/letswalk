package lingaraj.hourglass.in.letswalk.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MarshmallowPermissions {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 103;
    private static final String[] LOCATIONS_PERMISSIONS = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };

    public static boolean arePermissionsGranted(Activity activity){
        int result1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return result1 == PackageManager.PERMISSION_GRANTED
            && result2 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Activity activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(activity, "Please Provided Location Permission. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        }
        else {
            ActivityCompat.requestPermissions(activity, LOCATIONS_PERMISSIONS,
                LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

}

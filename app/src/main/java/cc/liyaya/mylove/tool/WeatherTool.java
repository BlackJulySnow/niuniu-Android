package cc.liyaya.mylove.tool;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

import cc.liyaya.mylove.MyApplication;

public class WeatherTool {

    private static String TAG = "WeatherTool";

    public static Location getLocation(Activity activity) {
        Location location = null;
        LocationManager locationManager = (LocationManager) MyApplication.getContext().getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Log.e(TAG, "null");
        } else {
//            LocationManager.getLastKnownLocation("gps");
            List<String> providers = locationManager.getProviders(true);
            Log.i(TAG, String.valueOf(providers.size()));
            if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
            for (String provider : providers) {
                Log.e(TAG, provider);
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()) {
                    Log.i(TAG, String.valueOf(l.getLatitude()));
                    Log.i(TAG, String.valueOf(l.getLongitude()));
                    location = l;
                }
            }
        }
        return location;
    }
    public static String Location2String(Location location){
        if (location == null)
            return "113.53,28.01";
        else
            return String.format("%.2f",location.getLongitude()) + "," + String.format("%.2f",location.getLatitude());
    }
}

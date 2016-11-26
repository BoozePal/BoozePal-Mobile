package hu.boozepalmobile.boozepal.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class BoozePalLocation implements LocationListener {

    private final Context context;

    protected LocationManager locationManager;

    Location location;

    public BoozePalLocation(Context context){
        this.context = context;
        getLocation();
    }

    public Location getLocation(){

        Location location = null;

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  null;
        }

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        Boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(gpsEnabled){
            Log.d("BoozePalLocation", "GPS enabled");
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1,
                    1, this);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    location = location;
                }
            }
        }
        else if(!gpsEnabled && networkEnabled){
            Log.d("BoozePalLocation", "GPS not enabled, network enabled");
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1,
                    1, this);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    location = location;
                }
            }
        }

        return location;
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

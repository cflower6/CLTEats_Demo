package uncc.ryan.clteatsdemo;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Ryrid on 3/31/2017.
 */

public class GetCurrentLocationTask implements LocationListener {
    public String thisLocation;
    double thisLatitude, thisLongitude;

    @Override
    public void onLocationChanged(Location location){
        thisLatitude = location.getLatitude();
        thisLongitude = location.getLongitude();

        thisLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle){
        //auto-generated
    }

    @Override
    public void onProviderEnabled(String s){
        //auto-generated
    }

    @Override
    public void onProviderDisabled(String s){
        //auto-generated
    }

    public double getThisLatitude() {
        return thisLatitude;
    }

    public double getThisLongitude() {
        return thisLongitude;
    }
}

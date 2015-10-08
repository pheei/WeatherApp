package com.example.hpishepei.weatherapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LocationUpdate implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        Log.i("Message: ", "Location changed, " + location.getAccuracy() + " , " + location.getLatitude() + "," + location.getLongitude());
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

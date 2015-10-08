package com.example.hpishepei.weatherapp.asynctask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.hpishepei.weatherapp.Constants;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LocationAsyncTask extends AsyncTask<String, Integer, String> {

    private LocationManager mLocationManager;
    private Context mContext;

    private LocationUpdateListener mLocationUpdateListener;

    public interface LocationUpdateListener{
        public void LocationUpdated(String location);
        public void LocationUpdateFail();
    }

    public LocationAsyncTask(Context context){
        mContext = context;
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }


    @Override
    protected String doInBackground(String... params) {


        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("Message: ", "Location changed, " + location.getAccuracy() + " , " + location.getLatitude() + "," + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null;
        }


        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.TWENTY_MINUTES, 0, locationListener);

        String currentLocation = mLocationManager.NETWORK_PROVIDER;

        return currentLocation;
    }
}

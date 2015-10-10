package com.example.hpishepei.weatherapp.asynctask;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.hpishepei.weatherapp.Constants;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LocationAsyncTask extends AsyncTask<String, Integer, String> {
    private Context mContext;

    private LocationManager mLocationManager;
    private Location mLocation;
    private LocationUpdateListener mLocationUpdateListener;

    public interface LocationUpdateListener {
        public void LocationUpdated(String location);

        public void LocationUpdateFail();
    }

    public LocationAsyncTask(Context context) {
        mContext = context;
    }


    public void setmLocationUpdateListener(LocationUpdateListener listener) {
        mLocationUpdateListener = listener;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected String doInBackground(String... params) {

        //Criteria criteria = new Criteria();
        //criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setAltitudeRequired(false);

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //String provider = mLocationManager.getBestProvider(criteria, false);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("lll","11111111111");

                String latitude = Double.toString(location.getLatitude());
                String longtitude = Double.toString(location.getLongitude());
                Log.i("lll","New Location: "+"latitude:"+latitude+" longtitude:"+longtitude);
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
        });




        Location oldLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (oldLocation!=null){
            String latitude = Double.toString(oldLocation.getLatitude());
            String longtitude = Double.toString(oldLocation.getLongitude());
            Log.i("lll","Old Location: "+"latitude:"+latitude+" longtitude:"+longtitude);
        }

        //MyLocationListener myLocationListener = new MyLocationListener();

        //myLocationListener.onLocationChanged(oldLocation);

/**
         mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("lll","11111111111");

                String latitude = Double.toString(location.getLatitude());
                String longtitude = Double.toString(location.getLongitude());
                Log.i("lll","New Location: "+"latitude:"+latitude+" longtitude:"+longtitude);

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
        });
*/



        /**
        Log.i("lll","1");

        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);


        String provider = mLocationManager.getBestProvider(criteria,false);
        Location location = mLocationManager.getLastKnownLocation(provider);


        Log.i("lll","2");
        Log.i("lll",Double.toString(location.getAltitude()));



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
        mLocationManager.requestLocationUpdates(provider, Constants.TWENTY_MINUTES, 0, locationListener);


        Log.i("lll",Double.toString(location.getLatitude())+" "+Double.toString(location.getLongitude()));
        return Double.toString(location.getLatitude())+" "+Double.toString(location.getLongitude());
         */

        return null;
    }

    private class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            String latitude = Double.toString(location.getLatitude());
            String longtitude = Double.toString(location.getLongitude());
            Log.i("lll","new Location: "+"latitude:"+latitude+" longtitude:"+longtitude);
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
}

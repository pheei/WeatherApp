package com.example.hpishepei.weatherapp.function;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by hpishepei on 10/13/15.
 */
public class LocationFinder implements LocationListener {
    private static final String TAG = "LocationFinder";
    private Context mContext;
    private LocationDetector mLocationDetector;

    private final int TIMEOUT_IN_MS = 10000; //10 second timeout

    private boolean mIsDetectingLocation = false;

    //built into Android
    private LocationManager mLocationManager;

    public enum FailureReason{
        NO_PERMISSION,
        TIMEOUT
    }

    public interface LocationDetector{
        void locationFound(Location location);
        void locationNotFound(FailureReason failureReason);
    }

    public LocationFinder(Context context, LocationDetector locationDetector){
        mContext = context;
        mLocationDetector = locationDetector;
    }

    public void detectLocation(){
        if(mIsDetectingLocation == false){
            mIsDetectingLocation = true;

            if(mLocationManager == null){
                mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            }


            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 23) {
                startTimer();

                mLocationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);

            }
            else {
                endLocationDetection();
                mLocationDetector.locationNotFound(FailureReason.NO_PERMISSION);
            }
        }
        else{
            Log.d(TAG, "already trying to detect location");
        }
    }

    public void cancel(){
        endLocationDetection();
        mLocationDetector.locationNotFound(FailureReason.NO_PERMISSION);
    }

    private void endLocationDetection(){
        if(mIsDetectingLocation) {
            mIsDetectingLocation = false;

            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 23) {
                mLocationManager.removeUpdates(this);
            }
        }
    }


    private void startTimer(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mIsDetectingLocation){
                    fallbackOnLastKnownLocation();
                }
            }
        }, TIMEOUT_IN_MS);

    }


    private void fallbackOnLastKnownLocation(){
        Location lastKnownLocation = null;

        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < 23) {
            lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //endLocationDetection();
        }

        if(lastKnownLocation != null){
            //endLocationDetection();
            mIsDetectingLocation = false;
            mLocationDetector.locationFound(lastKnownLocation);
        }
        else{
            //endLocationDetection();
            mIsDetectingLocation = false;
            mLocationDetector.locationNotFound(FailureReason.TIMEOUT);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationDetector.locationFound(location);
        mIsDetectingLocation = false;

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}

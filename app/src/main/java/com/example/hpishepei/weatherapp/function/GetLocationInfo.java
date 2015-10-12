package com.example.hpishepei.weatherapp.function;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.activity.WeatherPageActivity;

/**
 * Created by hpishepei on 10/11/15.
 */
public class GetLocationInfo {
    private ProgressDialog mProgressDialog = null;
    private Context mContext;
    private Criteria mCriteria;

    public LocationManager mLocationManager;

    public GetLocationInfo(Context context){
        mContext = context;
    }

    public void FetchCordinates(){

        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        //mCriteria = new Criteria();
        //mCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        //String provider = mLocationManager.getBestProvider(mCriteria,true);
        MyLocationListener myLocationListener = new MyLocationListener();
        /**
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null){
            myLocationListener.onLocationChanged(location);
        }
         */
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        //mLocationManager.requestLocationUpdates(provider,0,0,new lO);


        //Toast.makeText(mContext, ("Longitude=" + WeatherPageActivity.sLongitude+", Latitude="+WeatherPageActivity.sLatitude+", provider:"+mLocationManager.getBestProvider(mCriteria,true)),Toast.LENGTH_SHORT).show();



    }

    public class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {

            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getString(R.string.fetch_location_label));
            mProgressDialog.setCancelable(true);
            mProgressDialog.setIndeterminate(true);

            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgressDialog.dismiss();
                    Toast.makeText(mContext, mContext.getString(R.string.location_update_cancel_label), Toast.LENGTH_SHORT).show();
                    //mLocationManager.removeUpdates(myLocationListener);
                }
            });

            mProgressDialog.dismiss();

            WeatherPageActivity.sLatitude = location.getLatitude();
            WeatherPageActivity.sLongitude = location.getLongitude();
            Toast.makeText(mContext, ("Longitude=" + WeatherPageActivity.sLongitude+", Latitude="+WeatherPageActivity.sLatitude+", provider:"+mLocationManager.getBestProvider(mCriteria,true)),Toast.LENGTH_SHORT).show();

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

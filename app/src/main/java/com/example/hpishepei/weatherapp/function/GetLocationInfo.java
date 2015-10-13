package com.example.hpishepei.weatherapp.function;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hpishepei.weatherapp.Constants;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.activity.WeatherPageActivity;
import com.example.hpishepei.weatherapp.model.Weather;

/**
 * Created by hpishepei on 10/11/15.
 */
public class GetLocationInfo {
    private ProgressDialog mProgressDialog = null;
    private Context mContext;
    private Criteria mCriteria;
    private double mLatitude;
    private double mLongitude;

    private LocationManager mLocationManager;
    private MyLocationListener mMyLocationListener;


    public GetLocationInfo(Context context){
        mContext = context;
    }

    public void FetchCordinates(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                Toast.makeText(mContext, mContext.getString(R.string.location_update_cancel_label), Toast.LENGTH_SHORT).show();
            }
        });
        mProgressDialog.setMessage(mContext.getString(R.string.fetch_location_label));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        mMyLocationListener = new MyLocationListener();

        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);


        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.TWENTY_MINUTES, 0, mMyLocationListener);




        //mLocationManager.requestLocationUpdates(provider,0,0,new lO);


        //Toast.makeText(mContext, ("Longitude=" + WeatherPageActivity.sLongitude+", Latitude="+WeatherPageActivity.sLatitude+", provider:"+mLocationManager.getBestProvider(mCriteria,true)),Toast.LENGTH_SHORT).show();



    }

    public class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {

            mProgressDialog.dismiss();

            WeatherPageActivity.sLatitude = location.getLatitude();

            WeatherPageActivity.sLongitude = location.getLongitude();

            Toast.makeText(mContext, ("Longitude=" + WeatherPageActivity.sLongitude+", Latitude="+WeatherPageActivity.sLatitude+", provider:"),Toast.LENGTH_SHORT).show();
            Log.i("lll", Double.toString(WeatherPageActivity.sLatitude)+"," +Double.toString(WeatherPageActivity.sLongitude));

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

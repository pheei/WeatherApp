package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LoadWeatherAsyncTask extends AsyncTask<String, Integer, String> {


    private Context mContext;
    private JsonObject mGeo;
    private JsonObject mCon;
    private JsonObject mFore;
    private JsonObject mHourly;
    private WeatherUpdateListener mWeatherUpdateListener;
    private String mFlag;
    private boolean mIsFetchingInfo = false;

    private final int TIMEOUT_IN_MS = 10000; //10 second timeout



    public LoadWeatherAsyncTask(Context context, WeatherUpdateListener weatherUpdateListener) {
        mContext = context;
        mWeatherUpdateListener = weatherUpdateListener;
    }

    public interface WeatherUpdateListener{
        public void updateCompleted(JsonObject geoJson, JsonObject conditionJson, JsonObject forecastJson, JsonObject hourlyJson);
        public void updateFail();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mWeatherUpdateListener.updateCompleted(mGeo,mCon,mFore,mHourly);
    }

    @Override
    protected String doInBackground(String... params) {
        String input = params[0];
        mIsFetchingInfo = true;


        try {
            //startTimer();
            mGeo = GetInfoFromJson.getInfoByFeature("geolookup", input, mContext);
            mCon = GetInfoFromJson.getInfoByFeature("conditions", input, mContext);
            mFore = GetInfoFromJson.getInfoByFeature("forecast", input, mContext);
            mHourly = GetInfoFromJson.getInfoByFeature("hourly", input, mContext);


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mIsFetchingInfo = false;
        return null;


    }

    private void startTimer(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mIsFetchingInfo){
                    mWeatherUpdateListener.updateFail();
                }
            }
        }, TIMEOUT_IN_MS);

    }
}

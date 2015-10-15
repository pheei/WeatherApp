package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.google.gson.JsonObject;

/**
 * Created by hpishepei on 10/15/15.
 */
public class LoadInfoAsyncTask extends AsyncTask<String, Integer, String>{


    private Context mContext;
    private WeatherUpdateListener mWeatherUpdateListener;
    private String mCoordinate;
    private JsonObject geoJson;
    private JsonObject conditionsJson;
    private JsonObject forecastJson;
    private JsonObject hourlyJson;


    public LoadInfoAsyncTask(Context context, WeatherUpdateListener weatherUpdateListener) {
        mContext = context;
        mWeatherUpdateListener = weatherUpdateListener;
    }

    public interface WeatherUpdateListener{
        public void updateCompleted(JsonObject a, JsonObject b, JsonObject c, JsonObject d);
        public void updateFail();
    }



    @Override
    protected String doInBackground(String... params) {

        mCoordinate  = params[0];
        geoJson = new JsonObject();
        conditionsJson = new JsonObject();
        forecastJson = new JsonObject();
        hourlyJson = new JsonObject();

        try {
            geoJson = GetInfoFromJson.getInfoByFeature("geolookup",mCoordinate,mContext);
            conditionsJson = GetInfoFromJson.getInfoByFeature("conditions",mCoordinate,mContext);
            forecastJson = GetInfoFromJson.getInfoByFeature("forecast",mCoordinate,mContext);
            hourlyJson = GetInfoFromJson.getInfoByFeature("hourly",mCoordinate,mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }




        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (geoJson==null||conditionsJson==null||forecastJson==null||hourlyJson==null){
            mWeatherUpdateListener.updateFail();
        }
        else {
            mWeatherUpdateListener.updateCompleted(geoJson,conditionsJson,forecastJson,hourlyJson);
        }
    }
}

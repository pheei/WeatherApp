package com.example.hpishepei.weatherapp.function;

import android.content.Context;
import android.util.Log;

import com.example.hpishepei.weatherapp.model.WeatherInfo;
import com.google.gson.JsonObject;

/**
 * Created by hpishepei on 10/15/15.
 */
public class UpdateInfo {
    private Context mContext;
    private String mCoordinate;
    private WeatherInfo mWeatherInfo;

    public UpdateInfo(Context context, String coordinate){
        mContext = context;
        mCoordinate = coordinate;
        mWeatherInfo = WeatherInfo.getInstance(context);
    }

    public void update(){
        Log.i("lll", "1");

        JsonObject geoInfo = new JsonObject();
        try {

            geoInfo = GetInfoFromJson.getInfoByFeature("geolookup", mCoordinate, mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String city = GetInfoFromJson.getCityNameFromJSON(geoInfo);

        String zip = GetInfoFromJson.getZipFromJSON(geoInfo);

        mWeatherInfo.setmCity(city);
        mWeatherInfo.setmZip(zip);
        Log.i("lll", "2");



        JsonObject conditionsInfo = new JsonObject();
        try {

            conditionsInfo = GetInfoFromJson.getInfoByFeature("conditions", mCoordinate, mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        GetInfoFromJson.setCurrentCondition(mContext, conditionsInfo);
        Log.i("lll",        WeatherInfo.getInstance(mContext).getmCurrentFeelsC());

        Log.i("lll", "3");

        JsonObject hourlyInfo = new JsonObject();
        try {

            hourlyInfo = GetInfoFromJson.getInfoByFeature("hourly", mCoordinate, mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("lll", "4");
        Log.i("lll", hourlyInfo.toString());

        GetInfoFromJson.setHourlyForecast(mContext, hourlyInfo);




        JsonObject forecastInfo = new JsonObject();
        try {

            forecastInfo = GetInfoFromJson.getInfoByFeature("forecast", mCoordinate, mContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        GetInfoFromJson.setForecast(mContext, forecastInfo);



    }

}

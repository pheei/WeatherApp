package com.example.hpishepei.weatherapp.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by hpishepei on 10/7/15.
 */
public class WeatherList {

    private static WeatherList ourInstance;
    private Context mContext;
    private ArrayList<Weather> mWeatherList;



    public static WeatherList getInstance(Context c)
    {
        if (ourInstance == null){
            ourInstance = new WeatherList(c.getApplicationContext());
        }
        return ourInstance;
    }


    private WeatherList(Context c) {
        mContext = c;
        mWeatherList = new ArrayList<Weather>();
        for (int i = 0 ; i < 3 ; i++){
            Weather w = new Weather();
            w.setmWeekday("Monday");
            w.setmHighestTempC("30");
            w.setmLowestTempC("14");
            w.setmCityName("Washington, DC");
            w.setmWeather("Cloudy");
            w.setmSummeryC("Partly cloudy in the morning, then clear. High of 68F. Breezy. Winds from the West at 10 to 25 mph.");
        }
    }

    public ArrayList<Weather> getmWeatherList() {
        return mWeatherList;
    }
}

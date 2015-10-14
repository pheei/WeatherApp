package com.example.hpishepei.weatherapp.function;

import android.content.Context;

import com.example.hpishepei.weatherapp.model.Weather;
import com.example.hpishepei.weatherapp.model.WeatherList;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 10/8/15.
 */
public class GetWeatherInfo {



    public static JsonObject getInfoByFeature(String feature, String input, Context context) throws ExecutionException, InterruptedException {
        String query = "http://api.wunderground.com/api/84d866aae1376b97/";
        query += feature + "/q/" + input + ".json";
        return Ion.with(context).load(query).asJsonObject().get();
    }

    public static String getCityNameFromJSON(JsonObject jsonObject){
        String state = jsonObject.getAsJsonObject("location").get("state").toString();
        String city = jsonObject.getAsJsonObject("location").get("city").toString();
        String result = city + ", " + state;
        result = result.replace("\"","");
        return result;

    }


    public static String getZipFromJSON(JsonObject jsonObject){
        String zip = jsonObject.getAsJsonObject("location").get("zip").toString();
        zip = zip.replace("\"","");
        return zip;

    }

    public static Weather getTodayWeatherFromJSON(JsonObject jsonObject, Context context){
        Weather weather = WeatherList.getInstance(context).getmWeatherList().get(0);
        weather.set
    }

}

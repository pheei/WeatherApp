package com.example.hpishepei.weatherapp.function;

import android.content.Context;

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




}

package com.example.hpishepei.weatherapp.function;

import android.content.Context;

import com.example.hpishepei.weatherapp.Constants;
import com.example.hpishepei.weatherapp.model.Forecast;
import com.example.hpishepei.weatherapp.model.HourlyForecast;
import com.example.hpishepei.weatherapp.model.WeatherInfo;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 10/8/15.
 */
public class GetInfoFromJson {



    public static JsonObject getInfoByFeature(String feature, String input, Context context) throws ExecutionException, InterruptedException {
        String query = "http://api.wunderground.com/api/"+ Constants.API_KEY + "/";
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


    public static JsonObject getAllInfo(String input, Context context) throws ExecutionException, InterruptedException {
        String query = "http://api.wunderground.com/api/"+Constants.API_KEY+"/geolookup/conditions/forecast/hourly/q/";
        query += input + ".json";
        return Ion.with(context).load(query).asJsonObject().get();
    }


    public static String getZipFromJSON(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("location").get("zip").toString();
        result = result.replace("\"","");
        return result;
    }

    public static void setCurrentCondition(Context context, JsonObject jsonObject){
        String[] list = new String[]{"temp_c","temp_f","weather","relative_humidity","wind_degrees","feelslike_f","feelslike_c","UV","precip_1hr_in","precip_1hr_metric","precip_today_metric","precip_today_in","observation_time","wind_kph","wind_mph"};
        ArrayList<String> result = new ArrayList<String>();
        String s = "";
        for (int i = 0 ; i < list.length; i++){
            s = jsonObject.getAsJsonObject("current_observation").get(list[i]).toString();
            result.add(s.replace("\"", ""));
        }

        WeatherInfo info = WeatherInfo.getInstance(context);
        info.setmCurrentTempC(result.get(0)+"°");

        info.setmCurrentTempF(result.get(1) + "°");

        info.setmCurrentCondition(result.get(2));

        info.setmCurrentHumidity(result.get(3));

        info.setmCurrentWindMph(result.get(4));

        info.setmCurrentFeelsF(result.get(5) + "°");
        info.setmCurrentFeelsC(result.get(6) + "°");
        info.setmCurrentUV(result.get(7));

        info.setmCurrentPreHrIn(result.get(8) + " in");
        info.setmCurrentPreHrMetric(result.get(9) + " mm");
        info.setmCurrentPreDayMetric(result.get(10) + " mm");

        info.setmCurrentPreDayIn(result.get(11) + " in");
        info.setmCurrentUpdateTime(result.get(12));

        info.setmCurrentWindKph(result.get(13) + " kph");
        info.setmCurrentWindMph(result.get(14) + " mph");


    }


    public static void setHourlyForecast(Context context, JsonObject jsonObject){
        WeatherInfo info = WeatherInfo.getInstance(context);
        HourlyForecast[] list = info.getmHourlyForecastList();
        String s = "";
        for (int i = 0; i<list.length; i++){
            list[i] = new HourlyForecast();
            s = jsonObject.getAsJsonArray("hourly_forecast").get(i).getAsJsonObject().getAsJsonObject("FCTTIME").get("hour").toString();
            s = s.replace("\"", "");
            list[i].setmTime(s);
            s = jsonObject.getAsJsonArray("hourly_forecast").get(i).getAsJsonObject().getAsJsonObject("temp").get("english").toString();
            s = s.replace("\"", "");
            list[i].setmTempF(s);
            s = jsonObject.getAsJsonArray("hourly_forecast").get(i).getAsJsonObject().getAsJsonObject("temp").get("metric").toString();
            s = s.replace("\"", "");
            list[i].setmTempC(s);
            s = jsonObject.getAsJsonArray("hourly_forecast").get(i).getAsJsonObject().get("icon_url").toString();
            s = s.replace("\"", "");
            list[i].setmIconUrl(s);
        }
    }

    public static void setForecast(Context context, JsonObject jsonObject){
        WeatherInfo info = WeatherInfo.getInstance(context);
        Forecast[] list = info.getmForecastList();


        String s = "";
        for (int i = 0; i<list.length; i++){
            list[i] = new Forecast();
            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("txt_forecast").getAsJsonArray("forecastday").get(i*2).getAsJsonObject().get("title").toString();
            s = s.replace("\"", "");


            list[i].setmWeekday(s);

            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("txt_forecast").getAsJsonArray("forecastday").get(i*2).getAsJsonObject().get("fcttext").toString();
            s = s.replace("\"", "");

            list[i].setmDescriptionF(s);
            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("txt_forecast").getAsJsonArray("forecastday").get(i*2).getAsJsonObject().get("fcttext_metric").toString();
            s = s.replace("\"", "");

            list[i].setmDescriptionC(s);
            //s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("txt_forecast").getAsJsonArray("forecastday").get(i*2).getAsJsonObject().get("icon_url").toString();
            //list[i].setmIconUrl(s);

            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday").get(i).getAsJsonObject().getAsJsonObject("high").get("fahrenheit").toString();
            s = s.replace("\"", "");

            list[i].setmHighestTempF(s);
            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday").get(i).getAsJsonObject().getAsJsonObject("high").get("celsius").toString();
            s = s.replace("\"", "");

            list[i].setmHighestTempC(s);

            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday").get(i).getAsJsonObject().getAsJsonObject("low").get("fahrenheit").toString();
            s = s.replace("\"", "");
            list[i].setmLowestTempF(s);

            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday").get(i).getAsJsonObject().getAsJsonObject("low").get("celsius").toString();
            s = s.replace("\"", "");
            list[i].setmLowestTempC(s);

            s = jsonObject.getAsJsonObject("forecast").getAsJsonObject("simpleforecast").getAsJsonArray("forecastday").get(i).getAsJsonObject().get("icon_url").toString();
            s = s.replace("\"", "");
            list[i].setmIconUrl(s);


        }

    }

}

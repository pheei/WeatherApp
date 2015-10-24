package com.example.hpishepei.weatherapp.function;

import android.content.Context;
import android.util.Log;

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
        Log.i("lll",state);
        Log.i("lll",city);
        String result = city + ", " + state;
        result = result.replace("\"","");
        return result;

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
            Log.i("lll",list[i]+","+s);
            result.add(s.replace("\"", ""));
        }

        WeatherInfo info = WeatherInfo.getInstance(context);
        info.setmCurrentTempC(result.get(0)+"°");
        Log.i("lll", "1");

        info.setmCurrentTempF(result.get(1) + "°");
        Log.i("lll", "2");

        info.setmCurrentCondition(result.get(2));
        Log.i("lll", "3");

        info.setmCurrentHumidity(result.get(3));
        Log.i("lll", "4");

        info.setmCurrentWindMph(result.get(4));
        Log.i("lll", "5");

        info.setmCurrentFeelsF(result.get(5) + "°");
        info.setmCurrentFeelsC(result.get(6) + "°");
        info.setmCurrentUV(result.get(7));
        Log.i("lll", "6");

        info.setmCurrentPreHrIn(result.get(8) + " in");
        info.setmCurrentPreHrMetric(result.get(9) + " mm");
        info.setmCurrentPreDayMetric(result.get(10) + " mm");
        Log.i("lll", "7");

        info.setmCurrentPreDayIn(result.get(11) + " in");
        info.setmCurrentUpdateTime(result.get(12));

        info.setmCurrentWindKph(result.get(13) + " kph");
        info.setmCurrentWindMph(result.get(14) + " mph");


        Log.i("lll","herehherherh");
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
        Log.i("lll",Integer.toString(list.length));
        //list[0] = new Forecast();
        //list[0].setmWeekday("Thursday");

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

    /**
    public static String getCurrentTempC(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("temp_c").toString();
        result = result.replace("\"","");
        result += "°";
        return result;
    }

    public static String getCurrentTempF(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("temp_f").toString();
        result = result.replace("\"","");
        result += "°";
        return result;
    }

    public static String getCurrentCondition(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("weather").toString();
        result = result.replace("\"","");
        return result;
    }

    public static String getCurrentHumidity(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("relative_humidity").toString();
        result = result.replace("\"","");
        return result;
    }

    public static String getCurrentWind(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("wind_string").toString();
        result = result.replace("\"","");
        return result;
    }

    public static String getCurrentFeelsF(JsonObject jsonObject){
        String feel = jsonObject.getAsJsonObject("current_observation").get("feelslike_f").toString();
        feel = feel.replace("\"","");
        feel += "°";
        return feel;
    }

    public static String getCurrentFeelsC(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("feelslike_c").toString();
        result = result.replace("\"","");
        result += "°";
        return result;
    }


    public static String getCurrentUV(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("UV").toString();
        result = result.replace("\"","");
        return result;
    }

    public static String getCurrentPreHrIn(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("precip_1hr_in").toString();
        result = result.replace("\"","");
        result += " inch";
        return result;
    }

    public static String getCurrentPreHrMetric(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("precip_1hr_metric").toString();
        result = result.replace("\"","");
        result += " mm";
        return result;
    }

    public static String getCurrentPreDayMetric(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("precip_today_metric").toString();
        result = result.replace("\"","");
        result += " mm";
        return result;
    }

    public static String getCurrentPreDayIn(JsonObject jsonObject){
        String result = jsonObject.getAsJsonObject("current_observation").get("precip_today_in").toString();
        result = result.replace("\"","");
        result += " inch";
        return result;
    }

     */
}

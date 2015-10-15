package com.example.hpishepei.weatherapp.function;

import android.content.Context;

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
        String result = jsonObject.getAsJsonObject("location").get("zip").toString();
        result = result.replace("\"","");
        result += "°";
        return result;
    }

    public static void setCurrentCondition(Context context, JsonObject jsonObject){
        String[] list = new String[]{"temp_c","temp_f","weather","relative_humidity","wind_string","feelslike_f","feelslike_c","UV","precip_1hr_in","precip_1hr_metric","precip_today_metric","precip_today_in","observation_time"};
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
        info.setmCurrentWind(result.get(4));
        info.setmCurrentTempF(result.get(5) + "°");
        info.setmCurrentTempC(result.get(6)+"°");
        info.setmCurrentUV(result.get(7));
        info.setmCurrentPreHrIn(result.get(8)+" inch");
        info.setmCurrentPreDayMetric(result.get(9)+" mm");
        info.setmCurrentPreDayMetric(result.get(10)+" mm");
        info.setmCurrentPreDayIn(result.get(11)+" inch");
        info.setmCurrentUpdateTime(result.get(12));
    }


    public static void setForecast(Context context, JsonObject jsonObject){
        WeatherInfo info = WeatherInfo.getInstance(context);

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

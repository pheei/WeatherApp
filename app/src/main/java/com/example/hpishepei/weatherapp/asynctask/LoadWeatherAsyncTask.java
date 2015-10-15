package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LoadWeatherAsyncTask extends AsyncTask<String, Integer, JsonObject> {


    private Context mContext;
    private WeatherUpdateListener mWeatherUpdateListener;


    public LoadWeatherAsyncTask(Context context, WeatherUpdateListener weatherUpdateListener) {
        mContext = context;
        mWeatherUpdateListener = weatherUpdateListener;
    }

    public interface WeatherUpdateListener{
        public void updateCompleted(JsonObject jsonObject);
        public void updateFail();
    }


    @Override
    protected void onPostExecute(JsonObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (jsonObject.get("error")==null){
            mWeatherUpdateListener.updateCompleted(jsonObject);
        }
        else {
            mWeatherUpdateListener.updateFail();
        }
    }

    @Override
    protected JsonObject doInBackground(String... params) {
        String feature = params[0];
        String input = params[1];
        JsonObject info = new JsonObject();

        try {

            info = GetInfoFromJson.getInfoByFeature(feature, input, mContext);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return info;




    }
}

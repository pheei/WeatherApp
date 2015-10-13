package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.hpishepei.weatherapp.function.GetWeatherInfo;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LoadWeatherAsyncTask extends AsyncTask<String, Integer, String> {


    private Context mContext;
    private WeatherUpdateListener mWeatherUpdateListener;


    public LoadWeatherAsyncTask(Context context, WeatherUpdateListener weatherUpdateListener) {
        mContext = context;
        mWeatherUpdateListener = weatherUpdateListener;
    }

    public interface WeatherUpdateListener{
        public void updateCompleted();
        public void updateFail();
    }


    @Override
    protected String doInBackground(String... params) {

        String feature = params[0];
        String input = params[1];
        JsonObject info = new JsonObject();
        try {
            info = GetWeatherInfo.getInfoByFeature(feature,input,mContext);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return null;


        /**
        try {
            URL url = new URL("http://api.wunderground.com/api/84d866aae1376b97/conditions/q/20009.json");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            Log.i("lll", "Response Code: " + conn.getResponseCode());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = IOUtils.toString(in, "UTF-8");
            Log.i("lll",response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
         */

    }
}

package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hpishepei on 10/7/15.
 */
public class LoadWeatherAsyncTask extends AsyncTask<String, Integer, String> {


    private Context mContext;

    public LoadWeatherAsyncTask(Context context) {
        mContext = context;
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://api.wunderground.com/api/84d866aae1376b97/conditions/q/20009.json");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            Log.i("weatherinfo", "Response Code: " + conn.getResponseCode());
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String response = IOUtils.toString(in, "UTF-8");
            Log.i("weatherinfo",response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}

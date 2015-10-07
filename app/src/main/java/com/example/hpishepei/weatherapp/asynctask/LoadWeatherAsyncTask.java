package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;

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



        return null;
    }
}

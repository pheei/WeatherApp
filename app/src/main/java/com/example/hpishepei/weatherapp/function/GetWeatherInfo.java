package com.example.hpishepei.weatherapp.function;

import android.content.Context;

import com.example.hpishepei.weatherapp.asynctask.LoadWeatherAsyncTask;

/**
 * Created by hpishepei on 10/8/15.
 */
public class GetWeatherInfo {
    Context mContext;

    public GetWeatherInfo(Context context){
        mContext = context;
    }


    public String loadWeatherInfo(){
        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(mContext);
        task.execute();

        return null;
    }

}

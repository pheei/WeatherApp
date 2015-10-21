package com.example.hpishepei.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hpishepei on 10/8/15.
 */
public class ChangePreferences {
    private SharedPreferences sharedPreferences;
    private Context mContext;

    public ChangePreferences(Context context){
        mContext = context;
        sharedPreferences = context.getSharedPreferences(Constants.My_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getNotationSetting(){
        return sharedPreferences.getString(Constants.NOTATION_SETTING,"F");
    }

    public void setNotationSetting(String notation){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.NOTATION_SETTING, notation);
        editor.commit();
    }

}

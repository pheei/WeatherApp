package com.example.hpishepei.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by hpishepei on 10/8/15.
 */
public class ChangePreferences {
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private Set<String> mSet;

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

    public Set<String> getCitySet(){
        return sharedPreferences.getStringSet(Constants.City_SETTING, null);
    }

    public void setCitySet(Set<String> citySet){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(Constants.City_SETTING, citySet);
        editor.commit();
    }

    public String getDayNumber(){
        return sharedPreferences.getString(Constants.Days_SETTING,"4");
    }
    public void setDayNumber(String days){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.Days_SETTING, days);
        editor.commit();
    }

}

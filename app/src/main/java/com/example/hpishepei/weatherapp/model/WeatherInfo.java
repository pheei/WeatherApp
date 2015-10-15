package com.example.hpishepei.weatherapp.model;

import android.content.Context;

/**
 * Created by hpishepei on 10/14/15.
 */
public class WeatherInfo {

    private static WeatherInfo ourInstance;
    private Context mContext;

    private Forecast[] mForecastList = new Forecast[4];
    private HourlyForecast[] mHourlyForecastList = new HourlyForecast[24];

    private String mCity;
    private String mZip;
    private String mCurrentTempC;
    private String mCurrentTempF;
    private String mCurrentCondition;
    private String mCurrentHumidity;
    private String mCurrentWind;
    private String mCurrentFeelsC;
    private String mCurrentFeelsF;
    private String mCurrentUV;
    private String mCurrentPreHrIn;
    private String mCurrentPreHrMetric;
    private String mCurrentPreDayIn;
    private String mCurrentPreDayMetric;
    private String mCurrentUpdateTime;










    private WeatherInfo(Context c) {
        mContext = c;
    }

    public static WeatherInfo getInstance(Context c)
    {
        if (ourInstance == null){
            ourInstance = new WeatherInfo(c.getApplicationContext());
        }
        return ourInstance;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmZip() {
        return mZip;
    }

    public void setmZip(String mZip) {
        this.mZip = mZip;
    }

    public String getmCurrentCondition() {
        return mCurrentCondition;
    }

    public void setmCurrentCondition(String mCurrentCondition) {
        this.mCurrentCondition = mCurrentCondition;
    }

    public String getmCurrentFeelsC() {
        return mCurrentFeelsC;
    }

    public void setmCurrentFeelsC(String mCurrentFeelsC) {
        this.mCurrentFeelsC = mCurrentFeelsC;
    }

    public String getmCurrentFeelsF() {
        return mCurrentFeelsF;
    }

    public void setmCurrentFeelsF(String mCurrentFeelsF) {
        this.mCurrentFeelsF = mCurrentFeelsF;
    }

    public String getmCurrentHumidity() {
        return mCurrentHumidity;
    }

    public void setmCurrentHumidity(String mCurrentHumidity) {
        this.mCurrentHumidity = mCurrentHumidity;
    }

    public String getmCurrentPreDayIn() {
        return mCurrentPreDayIn;
    }

    public void setmCurrentPreDayIn(String mCurrentPreDayIn) {
        this.mCurrentPreDayIn = mCurrentPreDayIn;
    }

    public String getmCurrentPreDayMetric() {
        return mCurrentPreDayMetric;
    }

    public void setmCurrentPreDayMetric(String mCurrentPreDayMetric) {
        this.mCurrentPreDayMetric = mCurrentPreDayMetric;
    }

    public String getmCurrentPreHrIn() {
        return mCurrentPreHrIn;
    }

    public void setmCurrentPreHrIn(String mCurrentPreHrIn) {
        this.mCurrentPreHrIn = mCurrentPreHrIn;
    }

    public String getmCurrentPreHrMetric() {
        return mCurrentPreHrMetric;
    }

    public void setmCurrentPreHrMetric(String mCurrentPreHrMetric) {
        this.mCurrentPreHrMetric = mCurrentPreHrMetric;
    }

    public String getmCurrentTempC() {
        return mCurrentTempC;
    }

    public void setmCurrentTempC(String mCurrentTempC) {
        this.mCurrentTempC = mCurrentTempC;
    }

    public String getmCurrentTempF() {
        return mCurrentTempF;
    }

    public void setmCurrentTempF(String mCurrentTempF) {
        this.mCurrentTempF = mCurrentTempF;
    }

    public String getmCurrentUV() {
        return mCurrentUV;
    }

    public void setmCurrentUV(String mCurrentUV) {
        this.mCurrentUV = mCurrentUV;
    }

    public String getmCurrentWind() {
        return mCurrentWind;
    }

    public void setmCurrentWind(String mCurrentWind) {
        this.mCurrentWind = mCurrentWind;
    }

    public String getmCurrentUpdateTime() {
        return mCurrentUpdateTime;
    }

    public void setmCurrentUpdateTime(String mCurrentUpdateTime) {
        this.mCurrentUpdateTime = mCurrentUpdateTime;
    }
}

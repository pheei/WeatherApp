package com.example.hpishepei.weatherapp.model;

/**
 * Created by hpishepei on 10/14/15.
 */
public class HourlyForecast {
    private String mTime;
    private String mIconUrl;
    private String mTempF;
    private String mTempC;

    public String getmIconUrl() {
        return mIconUrl;
    }

    public void setmIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public String getmTempC() {
        return mTempC;
    }

    public void setmTempC(String mTempC) {
        this.mTempC = mTempC;
    }

    public String getmTempF() {
        return mTempF;
    }

    public void setmTempF(String mTempF) {
        this.mTempF = mTempF;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}

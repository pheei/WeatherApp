package com.example.hpishepei.weatherapp.model;

/**
 * Created by hpishepei on 10/14/15.
 */
public class Forecast {
    private String mWeekday;
    private String mIconUrl;
    private String mHighestTempC;
    private String mHighestTempF;
    private String mLowestTempC;
    private String mLowestTempF;
    private String mDescriptionC;
    private String mDescriptionF;


    public String getmDescriptionC() {
        return mDescriptionC;
    }

    public void setmDescriptionC(String mDescriptionC) {
        this.mDescriptionC = mDescriptionC;
    }

    public String getmDescriptionF() {
        return mDescriptionF;
    }

    public void setmDescriptionF(String mDescriptionF) {
        this.mDescriptionF = mDescriptionF;
    }

    public String getmHighestTempC() {
        return mHighestTempC;
    }

    public void setmHighestTempC(String mHighestTempC) {
        this.mHighestTempC = mHighestTempC;
    }

    public String getmHighestTempF() {
        return mHighestTempF;
    }

    public void setmHighestTempF(String mHighestTempF) {
        this.mHighestTempF = mHighestTempF;
    }

    public String getmIconUrl() {
        return mIconUrl;
    }

    public void setmIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public String getmLowestTempC() {
        return mLowestTempC;
    }

    public void setmLowestTempC(String mLowestTempC) {
        this.mLowestTempC = mLowestTempC;
    }

    public String getmLowestTempF() {
        return mLowestTempF;
    }

    public void setmLowestTempF(String mLowestTempF) {
        this.mLowestTempF = mLowestTempF;
    }

    public String getmWeekday() {
        return mWeekday;
    }

    public void setmWeekday(String mWeekday) {
        this.mWeekday = mWeekday;
    }
}

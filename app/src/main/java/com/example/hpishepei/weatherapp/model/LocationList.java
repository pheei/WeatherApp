package com.example.hpishepei.weatherapp.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by hpishepei on 10/8/15.
 */
public class LocationList {


    private static LocationList ourInstance;
    private Context mContext;
    private ArrayList<Location> mLocationList;



    public static LocationList getInstance(Context c)
    {
        if (ourInstance == null){
            ourInstance = new LocationList(c.getApplicationContext());
        }
        return ourInstance;
    }


    private LocationList(Context c) {
        mContext = c;
        mLocationList = new ArrayList<Location>();


        Location loc = new Location();
        loc.setmCity("Washington");
        loc.setmZip("20009");
        mLocationList.add(loc);

        Location loc1 = new Location();
        loc1.setmCity("New York");
        loc1.setmZip("20010");
        mLocationList.add(loc1);


    }

    public ArrayList<Location> getmLocationList() {
        return mLocationList;
    }

    public void addLocation(String city, String zip){
        Location location = new Location();
        location.setmCity(city);
        location.setmZip(zip);
        mLocationList.add(location);

    }

}

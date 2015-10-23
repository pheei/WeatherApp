package com.example.hpishepei.weatherapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by hpishepei on 10/21/15.
 */
public class CityLookup extends AsyncTask<String, Integer, String> {
    private JsonObject GeoJson;
    private Context mContext;
    private CityLookupListener mCityLookupListener;

    public CityLookup(Context context, CityLookupListener cityLookupListener){
        mContext = context;
        mCityLookupListener = cityLookupListener;
    }

    public interface CityLookupListener{
        public void lookupCompleted(JsonObject jsonObject);
        public void lookupFail();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (GeoJson!=null){
            mCityLookupListener.lookupCompleted(GeoJson);
        }
        else {
            mCityLookupListener.lookupFail();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String city = params[0];
        GeoJson = new JsonObject();
        try {
            GeoJson = GetInfoFromJson.getInfoByFeature("geolookup", city, mContext);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}

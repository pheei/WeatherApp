package com.example.hpishepei.weatherapp.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.asynctask.LoadInfoAsyncTask;
import com.example.hpishepei.weatherapp.function.LocationFinder;
import com.example.hpishepei.weatherapp.model.Forecast;
import com.example.hpishepei.weatherapp.model.WeatherInfo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationFinder.LocationDetector,LoadInfoAsyncTask.WeatherUpdateListener{

    public static String NotationFlag;
    public Double mLongitude;
    public Double mLatitude;
    private String mCoordinate;

    private JsonObject mJsonObject;
    private JsonObject mGeoJson;
    private JsonObject mConditionJson;
    private JsonObject mForecastJson;
    private JsonObject mHourlyJson;

    private TextView mUpdateTime;
    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mTodaySummery;
    private ListView mListView;
    private TextView mWind;
    private TextView mFeels;
    private TextView mUV;
    private TextView mHumidity;
    private TextView mPreHr;
    private TextView mPreDay;


    private WeatherInfo mWeatherInfo;
    ChangePreferences preferences;

    @Override
    protected void onResume() {

        super.onResume();
        //updateView();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);


        //mWeatherInfo = WeatherInfo.getInstance(this);

        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocation();


    }

    /**
    private void updateAllInfo(){
        mCoordinate = Double.toString(mLatitude)+","+Double.toString(mLongitude);
        Log.i("lll","2");

        updateGeoInfo();
    }

    private void updateGeoInfo(){
        Log.i("lll","3");

        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(this,this);
        task.execute("geolookup", mCoordinate);
    }

    private void updateConditionInfo(){
        Log.i("lll","4");

        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(this,this);
        task.execute("conditions",mCoordinate );
    }


    private void updateHourlyForecastInfo(){
        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(this,this);
        task.execute("hourly", mCoordinate);
    }

    private void updateForecastInfo(){
        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(this,this);
        task.execute("forecast", mCoordinate);
    }

    private void updateInfoAuto(){
        String coordinate = mLatitude +","+ mLongitude;

        mWeatherInfo = WeatherInfo.getInstance(this);

        LoadWeatherAsyncTask task1 = new LoadWeatherAsyncTask(this,this);

        Log.i("lll",coordinate );

        task1.execute("geolookup", coordinate);


        /**
        mGeoJson = mJsonObject;


        String city = GetInfoFromJson.getCityNameFromJSON(mGeoJson);

        String zip = GetInfoFromJson.getZipFromJSON(mGeoJson);

        mWeatherInfor.setmCity(city);

        mWeatherInfor.setmZip(zip);


        LoadWeatherAsyncTask task2 = new LoadWeatherAsyncTask(this,this);

        task2.execute("conditions",coordinate );
        mConditionJson = mJsonObject;
        GetInfoFromJson.setCurrentCondition(this, mConditionJson);

        LoadWeatherAsyncTask task3 = new LoadWeatherAsyncTask(this,this);

        task3.execute("hourly", coordinate);
        mHourlyJson = mJsonObject;
        GetInfoFromJson.setHourlyForecast(this, mHourlyJson);

        LoadWeatherAsyncTask task4 = new LoadWeatherAsyncTask(this,this);

        task4.execute("forecast", coordinate);
        mForecastJson = mJsonObject;
        GetInfoFromJson.setForecast(this, mForecastJson);

         */

        /**
        mCondition = mJsonObject;
        mWeatherInfor.setmCurrentCondition(GetInfoFromJson.getCurrentCondition(mCondition));
        mWeatherInfor.setmCurrentTempC(GetInfoFromJson.getCurrentTempC(mCondition));
        mWeatherInfor.setmCurrentTempF(GetInfoFromJson.getCurrentTempF(mCondition));
        mWeatherInfor.setmCurrentHumidity(GetInfoFromJson.getCurrentHumidity(mCondition));
        mWeatherInfor.setmCurrentWind(GetInfoFromJson.getCurrentWind(mCondition));
        mWeatherInfor.setmCurrentFeelsC(GetInfoFromJson.getCurrentFeelsC(mCondition));
        mWeatherInfor.setmCurrentFeelsF(GetInfoFromJson.getCurrentFeelsF(mCondition));
        mWeatherInfor.setmCurrentUV(GetInfoFromJson.getCurrentUV(mCondition));
        mWeatherInfor.setmCurrentPreDayIn(GetInfoFromJson.getCurrentPreDayIn(mCondition));
        mWeatherInfor.setmCurrentPreDayMetric(GetInfoFromJson.getCurrentPreDayMetric(mCondition));
        mWeatherInfor.setmCurrentPreHrIn(GetInfoFromJson.getCurrentPreHrIn(mCondition));
        mWeatherInfor.setmCurrentPreHrMetric(GetInfoFromJson.getCurrentPreHrMetric(mCondition));

         */
   // }




    private class ForecastListAdapter extends ArrayAdapter<Forecast>{
        public ForecastListAdapter(ArrayList<Forecast> forecasts){
            super(WeatherPageActivity.this, 0, forecasts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = WeatherPageActivity.this.getLayoutInflater().inflate(R.layout.weather_list, null);
            }


            Forecast forecast = getItem(position);

            TextView listWeekday = (TextView)convertView.findViewById(R.id.list_weekday);
            listWeekday.setText(forecast.getmWeekday());


            //TODO: load image
            ImageView listWeather = (ImageView)convertView.findViewById(R.id.list_weather);
            listWeather.setImageResource(R.drawable.partlycloudy);

            TextView listTemp = (TextView)convertView.findViewById(R.id.list_temp);
            if (NotationFlag.equals("C")){
                listTemp.setText(forecast.getmLowestTempC()+"\u00B0 ~ "+forecast.getmHighestTempC()+"\u00B0");
            }
            else if (NotationFlag.equals("F")){
                listTemp.setText(forecast.getmLowestTempF()+"\u00B0 ~ "+forecast.getmHighestTempF()+"\u00B0");
            }


            return convertView;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(WeatherPageActivity.this, SettingActivity.class);
            startActivity(i);
            return true;
        }
        else if (id == R.id.refresh_button){
            LocationFinder locationFinder = new LocationFinder(WeatherPageActivity.this,WeatherPageActivity.this);
            locationFinder.detectLocation();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }




    public void updateView(){
        preferences = new ChangePreferences(this);
        NotationFlag = preferences.getNotationSetting();

        WeatherInfo info = WeatherInfo.getInstance(this);

        mUpdateTime = (TextView)findViewById(R.id.updated_time_TextView);
        mUpdateTime.setText(info.getmCurrentUpdateTime());

        mCurrentCityTextView = (TextView)findViewById(R.id.current_city);
        mCurrentCityTextView.setText(info.getmCity());

        mCurrentWeather = (TextView)findViewById(R.id.current_weather);
        mCurrentWeather.setText(info.getmCurrentCondition());

        mCurrentTemp = (TextView)findViewById(R.id.current_temperature);
        if (NotationFlag.equals("C")){
            mCurrentTemp.setText(info.getmCurrentTempC());
        }
        else if (NotationFlag.equals("F")){
            mCurrentTemp.setText(info.getmCurrentTempF());

        }

        mWind = (TextView)findViewById(R.id.current_wind);
        mWind.setText(info.getmCurrentWind());

        mFeels = (TextView)findViewById(R.id.current_feelslike);
        if (NotationFlag.equals("C")){
            mFeels.setText(info.getmCurrentFeelsC());
        }
        else if (NotationFlag.equals("F")){
            mFeels.setText(info.getmCurrentFeelsF());
        }

        mUV = (TextView)findViewById(R.id.current_solar);
        mUV.setText(info.getmCurrentUV());

        mHumidity = (TextView)findViewById(R.id.current_humidity);
        mHumidity.setText(info.getmCurrentHumidity());

        mPreHr = (TextView)findViewById(R.id.current_1hr_rain);
        if (NotationFlag.equals("C")){
            mPreHr.setText(info.getmCurrentPreHrMetric());
        }
        else if (NotationFlag.equals("F")){
            mPreHr.setText(info.getmCurrentPreHrIn());
        }

        mPreDay = (TextView)findViewById(R.id.current_day_rain);
        if (NotationFlag.equals("C")){
            mPreHr.setText(info.getmCurrentPreDayMetric());
        }
        else if (NotationFlag.equals("F")){
            mPreHr.setText(info.getmCurrentPreDayIn());
        }


        mListView = (ListView)findViewById(R.id.list_container);
        ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
        for (Forecast i : info.getmForecastList()){
            forecasts.add(i);
        }
        ForecastListAdapter adapter = new ForecastListAdapter(forecasts);
        mListView.setAdapter(adapter);


    }



    @Override
    public void locationFound(Location location) {
        mLongitude = location.getLongitude();
        mLatitude = location.getLatitude();
        mCoordinate = Double.toString(mLatitude)+","+Double.toString(mLongitude);
        Log.i("lll", mCoordinate);
        updateInfo();

        //updateAllInfo();
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Toast.makeText(this,this.getString(R.string.fail_label),Toast.LENGTH_SHORT).show();


    }

    public void updateInfo(){
        LoadInfoAsyncTask task = new LoadInfoAsyncTask(this,this);
        task.execute(mCoordinate);
    }

    @Override
    public void updateCompleted(JsonObject geo, JsonObject condition, JsonObject forecast, JsonObject hourly) {
        mGeoJson = geo;
        mConditionJson = condition;
        mForecastJson = forecast;
        mHourlyJson = hourly;
        Log.i("lll", "update done");
        //updateView();
    }

    @Override
    public void updateFail() {
        Log.i("lll", "update fail");


    }

    /*
    @Override
    public void updateCompleted(JsonObject jsonObject, String flag) {

        switch (flag){
            case "geolookup":
                mJsonObject = jsonObject;
                String city = GetInfoFromJson.getCityNameFromJSON(mGeoJson);
                String zip = GetInfoFromJson.getZipFromJSON(mGeoJson);
                mWeatherInfo.setmCity(city);
                mWeatherInfo.setmZip(zip);
                updateConditionInfo();

                break;
            case "conditions":
                mConditionJson = mJsonObject;
                GetInfoFromJson.setCurrentCondition(this, mConditionJson);
                updateHourlyForecastInfo();
                break;
            case "hourly":
                mHourlyJson = mJsonObject;
                GetInfoFromJson.setHourlyForecast(this, mHourlyJson);
                updateForecastInfo();
                break;
            case "forecast":
                mForecastJson = mJsonObject;
                GetInfoFromJson.setForecast(this, mForecastJson);
                updateView();
                break;
        }


    }

    @Override
    public void updateFail() {
        Log.i("lll","get infor fail");
    }

*/

}

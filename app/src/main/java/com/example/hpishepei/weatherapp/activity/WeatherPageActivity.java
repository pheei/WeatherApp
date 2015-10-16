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
import com.example.hpishepei.weatherapp.asynctask.LoadWeatherAsyncTask;
import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.example.hpishepei.weatherapp.function.LocationFinder;
import com.example.hpishepei.weatherapp.model.Forecast;
import com.example.hpishepei.weatherapp.model.WeatherInfo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationFinder.LocationDetector,LoadWeatherAsyncTask.WeatherUpdateListener{

    public static String NotationFlag;
    public static Double sLongitude = 0.0;
    public static Double sLatitude = 0.0;
    public String mCoordinate;

    private JsonObject mJsonObject;
    private JsonObject mGeoJson;
    private JsonObject mConditionJson;
    private JsonObject mForecastJson;
    private JsonObject mHourlyJson;

    private TextView mUpdateTimeTextView;
    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mCurrentWind;
    private TextView mCurrentFeel;
    private TextView mCurrentUV;
    private TextView mCurrentHumidity;
    private TextView mCurrentPreHr;
    private TextView mCurrentPreDay;



    private ListView mListView;


    private WeatherInfo mWeatherInfor;

    //ArrayList<Weather> mWeatherList;

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
        mWeatherInfor = WeatherInfo.getInstance(this);

        LocationFinder locationFinder = new LocationFinder(this,this);
        locationFinder.detectLocation();


        //updateInfoAuto();
        //updateView();

    }

    private void updateInfoAuto(){
        mWeatherInfor = WeatherInfo.getInstance(this);
        LocationFinder location = new LocationFinder(this,this);
        location.detectLocation();

        String cordinate = sLatitude+","+sLongitude;
        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(this,this);

        task.execute("geolookup",cordinate);
        mGeoJson = mJsonObject;
        String city = GetInfoFromJson.getCityNameFromJSON(mGeoJson);
        String zip = GetInfoFromJson.getZipFromJSON(mGeoJson);
        mWeatherInfor.setmCity(city);
        mWeatherInfor.setmZip(zip);

        task.execute("conditions", cordinate);
        mConditionJson = mJsonObject;
        GetInfoFromJson.setCurrentCondition(this,mConditionJson);

        task.execute("hourly", cordinate);
        mHourlyJson = mJsonObject;
        GetInfoFromJson.setHourlyForecast(this,mHourlyJson);

        task.execute("forecast", cordinate);
        mForecastJson = mJsonObject;
        GetInfoFromJson.setForecast(this,mForecastJson);


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

        mWeatherInfor = WeatherInfo.getInstance(this);

        mUpdateTimeTextView = (TextView)findViewById(R.id.updated_time_TextView);
        mUpdateTimeTextView.setText(mWeatherInfor.getmCurrentUpdateTime());

        mCurrentCityTextView = (TextView)findViewById(R.id.current_city);
        mCurrentCityTextView.setText(mWeatherInfor.getmCity());

        mCurrentWeather = (TextView)findViewById(R.id.current_weather);
        mCurrentWeather.setText(mWeatherInfor.getmCurrentCondition());

        mCurrentTemp = (TextView)findViewById(R.id.current_temperature);
        if (NotationFlag.equals("C")){
            mCurrentTemp.setText(mWeatherInfor.getmCurrentTempC());
        }
        else if (NotationFlag.equals("F")){
            mCurrentTemp.setText(mWeatherInfor.getmCurrentTempF());
        }

        mCurrentWind = (TextView)findViewById(R.id.current_wind);
        mCurrentWind.setText("Wind: " + mWeatherInfor.getmCurrentWind());;

        mCurrentFeel = (TextView)findViewById(R.id.current_feelslike);
        if (NotationFlag.equals("C")){
            mCurrentFeel.setText("Feels like: " + mWeatherInfor.getmCurrentFeelsC());
        }
        else if (NotationFlag.equals("F")){
            mCurrentFeel.setText("Feels like: " + mWeatherInfor.getmCurrentFeelsF());
        }

        mCurrentUV = (TextView)findViewById(R.id.current_solar);
        mCurrentUV.setText("UV index: " + mWeatherInfor.getmCurrentUV());

        mCurrentHumidity = (TextView)findViewById(R.id.current_humidity);
        mCurrentHumidity.setText("Humidity: " + mWeatherInfor.getmCurrentHumidity());

        mCurrentPreHr = (TextView)findViewById(R.id.current_1hr_rain);
        if (NotationFlag.equals("C")){
            mCurrentPreHr.setText("Rain 1hr: " + mWeatherInfor.getmCurrentPreHrMetric());
        }
        else if (NotationFlag.equals("F")){
            mCurrentPreHr.setText("Rain 1hr: " + mWeatherInfor.getmCurrentPreHrIn());
        }

        mCurrentPreDay = (TextView)findViewById(R.id.current_day_rain);
        if (NotationFlag.equals("C")){
            mCurrentPreDay.setText("Rain today: " + mWeatherInfor.getmCurrentPreDayMetric());
        }
        else if (NotationFlag.equals("F")){
            mCurrentPreDay.setText("Rain today: " + mWeatherInfor.getmCurrentPreDayIn());
        }




        mListView = (ListView)findViewById(R.id.list_container);
        ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
        for (Forecast i : mWeatherInfor.getmForecastList()){
            forecasts.add(i);
        }
        //ArrayAdapter<Weather> adapter = new ArrayAdapter<Weather>(this,android.R.layout.simple_list_item_1,mWeatherList);
        WeatherListAdapter adapter = new WeatherListAdapter(forecasts);
        mListView.setAdapter(adapter);


    }


    private class WeatherListAdapter extends ArrayAdapter<Forecast> {
        public WeatherListAdapter(ArrayList<Forecast> forecasts){
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
    public void locationFound(Location location) {
        sLongitude = location.getLongitude();
        sLatitude = location.getLatitude();
        mCoordinate = Double.toString(sLatitude)+","+Double.toString(sLongitude);
        Log.i("lll",Double.toString(sLatitude)+","+Double.toString(sLongitude));
        LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(this,this);
        task.execute(mCoordinate);

    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Toast.makeText(this,this.getString(R.string.fail_label),Toast.LENGTH_SHORT).show();


    }


    @Override
    public void updateCompleted(JsonObject geoJson, JsonObject conditionJson, JsonObject forecastJson, JsonObject hourlyJson) {
        Log.i("lll",geoJson.toString());
        Log.i("lll",conditionJson.toString());
        Log.i("lll", forecastJson.toString());
        Log.i("lll", hourlyJson.toString());

        mWeatherInfor.setmCity(GetInfoFromJson.getCityNameFromJSON(geoJson));
        mWeatherInfor.setmZip(GetInfoFromJson.getZipFromJSON(geoJson));
        GetInfoFromJson.setCurrentCondition(this, conditionJson);
        GetInfoFromJson.setForecast(this, forecastJson);
        GetInfoFromJson.setHourlyForecast(this, hourlyJson);

        Log.i("lll", "done!!!!!!");
        updateView();






    }

    @Override
    public void updateFail() {
        Log.i("lll","get infor fail");
    }



}

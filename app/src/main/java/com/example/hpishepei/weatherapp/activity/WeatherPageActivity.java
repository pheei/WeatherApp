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
import com.example.hpishepei.weatherapp.model.Weather;
import com.example.hpishepei.weatherapp.model.WeatherInfo;
import com.example.hpishepei.weatherapp.model.WeatherList;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationFinder.LocationDetector,LoadWeatherAsyncTask.WeatherUpdateListener{

    public static String NotationFlag;
    public static Double sLongitude = 0.0;
    public static Double sLatitude = 0.0;

    private JsonObject mJsonObject;
    private JsonObject mGeoJson;
    private JsonObject mConditionJson;
    private JsonObject mForecastJson;
    private JsonObject mHourlyJson;

    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mTodaySummery;
    private ListView mListView;


    private WeatherInfo mWeatherInfor;

    //ArrayList<Weather> mWeatherList;

    ChangePreferences preferences;

    @Override
    protected void onResume() {

        super.onResume();
        updateView();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);


        updateInfoAuto();
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



    private class WeatherListAdapter extends ArrayAdapter<Weather>{
        public WeatherListAdapter(ArrayList<Weather> mWeatherList){
            super(WeatherPageActivity.this, 0, mWeatherList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = WeatherPageActivity.this.getLayoutInflater().inflate(R.layout.weather_list, null);
            }


            Weather weather = getItem(position);

            TextView listWeekday = (TextView)convertView.findViewById(R.id.list_weekday);
            listWeekday.setText(weather.getmWeekday());


            ImageView listWeather = (ImageView)convertView.findViewById(R.id.list_weather);
            listWeather.setImageResource(R.drawable.partlycloudy);

            TextView listTemp = (TextView)convertView.findViewById(R.id.list_temp);
            if (NotationFlag.equals("C")){
                listTemp.setText(weather.getmLowestTempC()+"\u00B0 ~ "+weather.getmHighestTempC()+"\u00B0");
            }
            else if (NotationFlag.equals("F")){
                listTemp.setText(weather.getmLowestTempF()+"\u00B0 ~ "+weather.getmHighestTempF()+"\u00B0");
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



        mWeatherList = WeatherList.getInstance(this).getmWeatherList();
        Weather today = mWeatherList.get(0);



        mCurrentCityTextView = (TextView)findViewById(R.id.current_city);
        mCurrentCityTextView.setText(today.getmCityName());

        mCurrentWeather = (TextView)findViewById(R.id.current_weather);
        mCurrentWeather.setText(today.getmWeather());

        mCurrentTemp = (TextView)findViewById(R.id.current_temperature);
        if (NotationFlag.equals("C")){
            mCurrentTemp.setText(today.getmCurrentTempC());
        }
        else if (NotationFlag.equals("F")){
            mCurrentTemp.setText(today.getmCurrentTempF());

        }

        mTodaySummery = (TextView)findViewById(R.id.today_summery);
        if (NotationFlag.equals("C")){
            mTodaySummery.setText(today.getmSummeryC());
        }
        else if (NotationFlag.equals("F")){
            mTodaySummery.setText(today.getmSummeryF());
        }



        mListView = (ListView)findViewById(R.id.list_container);
        //ArrayAdapter<Weather> adapter = new ArrayAdapter<Weather>(this,android.R.layout.simple_list_item_1,mWeatherList);
        WeatherListAdapter adapter = new WeatherListAdapter(mWeatherList);
        mListView.setAdapter(adapter);







    }


    @Override
    public void locationFound(Location location) {
        sLongitude = location.getLongitude();
        sLatitude = location.getLatitude();
        Toast.makeText(this,this.getString(R.string.location_founded_label),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Toast.makeText(this,this.getString(R.string.fail_label),Toast.LENGTH_SHORT).show();


    }


    @Override
    public void updateCompleted(JsonObject jsonObject) {
        mJsonObject = jsonObject;
    }

    @Override
    public void updateFail() {
        Log.i("lll","get infor fail");
    }



}

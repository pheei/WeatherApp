package com.example.hpishepei.weatherapp.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.hpishepei.weatherapp.function.LocationFinder;
import com.example.hpishepei.weatherapp.model.Weather;
import com.example.hpishepei.weatherapp.model.WeatherList;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationFinder.LocationDetector {

    public static String NotationFlag;
    public static Double sLongitude = 0.0;
    public static Double sLatitude = 0.0;

    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mTodaySummery;
    private ListView mListView;



    ArrayList<Weather> mWeatherList;

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

        updateView();

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
        Toast.makeText(this,("Longitude:"+sLongitude+", Latitude:"+sLatitude),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        Toast.makeText(this,this.getString(R.string.fail_label),Toast.LENGTH_SHORT).show();


    }



}

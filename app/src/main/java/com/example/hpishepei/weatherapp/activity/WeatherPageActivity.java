package com.example.hpishepei.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.asynctask.LocationAsyncTask;
import com.example.hpishepei.weatherapp.model.Weather;
import com.example.hpishepei.weatherapp.model.WeatherList;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationAsyncTask.LocationUpdateListener{

    public static String NotationFlag;
    //public static Boolean AutoLocation;

    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mTodaySummery;
    private ListView mListView;

    ArrayList<Weather> mWeatherList;

    ChangePreferences preferences;

    @Override
    protected void onResume() {
        /**
        Log.i("lll","onResume!!!!!!");

        preferences = new ChangePreferences(this);
        NotationFlag = preferences.getNotationSetting();
        Log.i("ccc",WeatherPageActivity.NotationFlag);
        //notifyAll();

        mTodaySummery = (TextView)findViewById(R.id.today_summery);
        if (NotationFlag.equals("C")){
            mTodaySummery.setText(mWeatherList.get(0).getmSummeryC());
        }
        else if (NotationFlag.equals("F")){
            mTodaySummery.setText(mWeatherList.get(0).getmSummeryF());
        }
*/
        super.onResume();
        updateView();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("lll", "onCreate!!!!!!");


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

        return super.onOptionsItemSelected(item);
    }


    public void updateView(){
        preferences = new ChangePreferences(this);
        NotationFlag = preferences.getNotationSetting();

        //SharedPreferences sharedpreferences = getSharedPreferences(Constants.My_PREFERENCES, Context.MODE_PRIVATE);
        //NotationFlag = sharedpreferences.getString(Constants.NOTATION_SETTING, "C");
        //AutoLocation = sharedpreferences.getBoolean(Constants.AUTOLOCATION_SETTING, true);



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


        //LocationAsyncTask asyncTask = new LocationAsyncTask(this);
        //asyncTask.setmLocationUpdateListener(this);
        //asyncTask.execute();


        LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //String provider = mLocationManager.getBestProvider(criteria, false);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("lll","11111111111");

                String latitude = Double.toString(location.getLatitude());
                String longtitude = Double.toString(location.getLongitude());
                Log.i("lll","New Location: "+"latitude:"+latitude+" longtitude:"+longtitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });




        Location oldLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (oldLocation!=null){
            String latitude = Double.toString(oldLocation.getLatitude());
            String longtitude = Double.toString(oldLocation.getLongitude());
            Log.i("lll","Old Location: "+"latitude:"+latitude+" longtitude:"+longtitude);
        }



    }




    @Override
    public void LocationUpdated(String location) {

    }

    @Override
    public void LocationUpdateFail() {

    }
}

package com.example.hpishepei.weatherapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.asynctask.CityLookup;
import com.example.hpishepei.weatherapp.asynctask.LoadWeatherAsyncTask;
import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.example.hpishepei.weatherapp.function.LocationFinder;
import com.example.hpishepei.weatherapp.model.Forecast;
import com.example.hpishepei.weatherapp.model.WeatherInfo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationFinder.LocationDetector,LoadWeatherAsyncTask.WeatherUpdateListener,CityLookup.CityLookupListener{



    public static String NotationFlag;
    public static Double sLongitude = 0.0;
    public static Double sLatitude = 0.0;
    private boolean mHasInfo;
    public String mLocation;
    private String mCityName;
    private boolean mNormalMode;

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
    private TextView mDescription;

    private ProgressDialog mPrograssDialog;

    private LoadWeatherAsyncTask mLoadWeatherAsyncTask;
    private LocationFinder mLocationFinder;

    private ListView mListView;


    private WeatherInfo mWeatherInfor;


    ChangePreferences preferences;

    @Override
    protected void onResume() {

        super.onResume();
        if (mNormalMode){
            if (!WeatherInfo.sIsNull){
                mWeatherInfor = WeatherInfo.getInstance(this);
                updateView();
            }
            else {
                newUpdate();
            }
        }
        else {
            Log.i("kkk","good mode");
            CityLookup cityLookup = new CityLookup(this,this);
            mPrograssDialog = new ProgressDialog(this);
            mPrograssDialog.setIndeterminate(true);
            mPrograssDialog.setMessage(this.getString(R.string.fetch_city_label));
            mPrograssDialog.show();
            cityLookup.execute(mCityName);
        }



    }


    @Override
    public void lookupCompleted(JsonObject jsonObject) {
        mPrograssDialog.dismiss();
        JsonObject geoJson = jsonObject;
        Log.i("kkk","good mode2");

        if (geoJson.getAsJsonObject("location")==null){
            Toast.makeText(this,this.getString(R.string.zip_fail_label),Toast.LENGTH_SHORT).show();
        }
        else {

            mLocation = GetInfoFromJson.getZipFromJSON(geoJson);
            getWeatherInfo();
        }

    }

    @Override
    public void lookupFail() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);
        Log.i("kkk", "backhere");

        if (getIntent().getStringExtra(SettingActivity.CITY_TAG)!=null){
            mNormalMode = false;
            mCityName = getIntent().getStringExtra(SettingActivity.CITY_TAG);
            Log.i("kkk",mCityName);




        }
        else {
            mNormalMode = true;
        }

    }

    private void cityUpdate(){
        mHasInfo = false;
        mWeatherInfor = WeatherInfo.getInstance(this);
        getWeatherInfo();
    }

    private void newUpdate(){
        mHasInfo = false;
        mWeatherInfor = WeatherInfo.getInstance(this);
        mPrograssDialog = new ProgressDialog(this);
        mPrograssDialog.setIndeterminate(true);
        mPrograssDialog.setMessage(this.getString(R.string.fetch_location_label));
        mPrograssDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPrograssDialog.dismiss();
                Toast.makeText(WeatherPageActivity.this, WeatherPageActivity.this.getString(R.string.location_update_cancel_label), Toast.LENGTH_SHORT).show();
            }
        });
        mPrograssDialog.show();

        mLocationFinder = new LocationFinder(this,this);
        mLocationFinder.detectLocation();
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
            startActivityForResult(i,0);

            //startActivity(i);
            return true;
        }
        else if (id == R.id.refresh_button){
            newUpdate();
            //LocationFinder locationFinder = new LocationFinder(this,this);
            //locationFinder.detectLocation();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }





    public void updateView(){
        preferences = new ChangePreferences(this);
        NotationFlag = preferences.getNotationSetting();

        //mWeatherInfor = WeatherInfo.getInstance(this);

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
        WeatherListAdapter adapter = new WeatherListAdapter(forecasts);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecast forecast = new Forecast();
                forecast = (Forecast) parent.getItemAtPosition(position);
                mDescription = (TextView) findViewById(R.id.description_TextView);
                if (NotationFlag.equals("C")) {
                    mDescription.setText(forecast.getmWeekday() + " brief: " + forecast.getmDescriptionC());
                } else if (NotationFlag.equals("F")) {
                    mDescription.setText(forecast.getmWeekday() + " brief: " + forecast.getmDescriptionF());
                }
            }
        });
        mDescription = (TextView)findViewById(R.id.description_TextView);
        mDescription.setText(this.getString(R.string.description_hint));



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

    public void getWeatherInfo(){
        mLoadWeatherAsyncTask = new LoadWeatherAsyncTask(this,this);

        mPrograssDialog = new ProgressDialog(this);
        mPrograssDialog.setIndeterminate(true);
        mPrograssDialog.setMessage(this.getString(R.string.fetch_weatherinfo_label));
        mPrograssDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPrograssDialog.dismiss();
                Toast.makeText(WeatherPageActivity.this, WeatherPageActivity.this.getString(R.string.weatherinfo_update_cancel_label), Toast.LENGTH_SHORT).show();
            }
        });
        mPrograssDialog.show();

        mLoadWeatherAsyncTask.execute(mLocation);
    }

    @Override
    public void locationFound(Location location) {
        mPrograssDialog.dismiss();
        sLongitude = location.getLongitude();
        sLatitude = location.getLatitude();
        mLocation = Double.toString(sLatitude)+","+Double.toString(sLongitude);
        Log.i("lll", Double.toString(sLatitude) + "," + Double.toString(sLongitude));

        getWeatherInfo();
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {
        mPrograssDialog.dismiss();
        Toast.makeText(this,this.getString(R.string.location_fail_label),Toast.LENGTH_SHORT).show();


    }


    @Override
    public void updateCompleted(JsonObject geoJson, JsonObject conditionJson, JsonObject forecastJson, JsonObject hourlyJson) {

        mWeatherInfor.setmCity(GetInfoFromJson.getCityNameFromJSON(geoJson));
        mWeatherInfor.setmZip(GetInfoFromJson.getZipFromJSON(geoJson));
        GetInfoFromJson.setCurrentCondition(this, conditionJson);
        GetInfoFromJson.setForecast(this, forecastJson);
        GetInfoFromJson.setHourlyForecast(this, hourlyJson);

        Log.i("lll", "done!!!!!!");
        mHasInfo = true;
        mWeatherInfor = WeatherInfo.getInstance(this);
        updateView();
        mPrograssDialog.dismiss();

    }

    @Override
    public void updateFail() {

        mLoadWeatherAsyncTask.cancel(true);
        Log.i("lll","get infor fail");
        Toast.makeText(this,R.string.weather_fail_label,Toast.LENGTH_SHORT).show();
        mPrograssDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null){
            return;
        }
        if (mHasInfo == true && data.getBooleanExtra(SettingActivity.EXTRA_STATE_CHANGE,false)){
            updateView();
        }
    }
}

package com.example.hpishepei.weatherapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Set;

public class WeatherPageActivity extends AppCompatActivity implements LocationFinder.LocationDetector,LoadWeatherAsyncTask.WeatherUpdateListener,CityLookup.CityLookupListener{



    public static String NotationFlag;
    public static String mDayNum;
    public static Double sLongitude = 0.0;
    public static Double sLatitude = 0.0;
    private boolean mHasInfo;
    public String mLocation;
    private String mCityName;
    private boolean mNormalMode;
    private String mCityPicked;

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
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadWeatherAsyncTask!=null){
            mLoadWeatherAsyncTask.cancel();
        }
        if (mLocationFinder!=null){
            mLocationFinder.cancel();
        }
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (!WeatherInfo.sIsNull){
            mWeatherInfor = WeatherInfo.getInstance(this);
            updateView();
        }
        else {
            newUpdate();
        }


    }

    private String locationFormat(String location){
        int index = location.indexOf(",");
        return (location.substring(index+2,location.length())+"/"+location.substring(0,index));
    }

    @Override
    public void lookupCompleted(JsonObject jsonObject) {
        mPrograssDialog.dismiss();
        JsonObject geoJson = jsonObject;

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
        mCityPicked = "";
        mLocation = "";
    }

    private void currentCityUpdate(){

        mWeatherInfor = WeatherInfo.getInstance(this);
        mHasInfo = true;
        mLocation = locationFormat(mWeatherInfor.getmCity());

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
                mLocationFinder.cancel();
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
            return true;
        }
        else if (id == R.id.refresh_button){
            if (WeatherInfo.sIsNull){

                newUpdate();
            }
            else {
                currentCityUpdate();
            }

            return true;

        }
        else if (id == R.id.update_location_button){
            newUpdate();
        }

        return super.onOptionsItemSelected(item);
    }


    public void getWeatherInfo(){
        mLoadWeatherAsyncTask = new LoadWeatherAsyncTask(this,this);
        if (!isNetworkConnected()){
            Toast.makeText(this,this.getString(R.string.no_network_label),Toast.LENGTH_SHORT).show();
            return;
        }


        mPrograssDialog = new ProgressDialog(this);
        mPrograssDialog.setIndeterminate(true);
        mPrograssDialog.setMessage(this.getString(R.string.fetch_weatherinfo_label));
        mPrograssDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPrograssDialog.dismiss();
                Toast.makeText(WeatherPageActivity.this, WeatherPageActivity.this.getString(R.string.weatherinfo_update_cancel_label), Toast.LENGTH_SHORT).show();
                mLoadWeatherAsyncTask.cancel(true);
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

        mHasInfo = true;
        mWeatherInfor = WeatherInfo.getInstance(this);

        ChangePreferences preferences = new ChangePreferences(this);
        Set<String> set = preferences.getCitySet();

        set.add(mWeatherInfor.getmCity());
        preferences.setCitySet(set);

        updateView();
        mPrograssDialog.dismiss();

    }

    @Override
    public void updateFail() {

        mLoadWeatherAsyncTask.cancel(true);
        Toast.makeText(this,R.string.weather_fail_label,Toast.LENGTH_SHORT).show();
        mPrograssDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (data == null){
            return;
        }
        else if (mHasInfo == true && data.getBooleanExtra(SettingActivity.EXTRA_STATE_CHANGE,false) && data.getStringExtra(SettingActivity.CITY_TAG).equals("")){

            updateView();
        }
        else if ((data.getBooleanExtra(SettingActivity.EXTRA_STATE_CHANGE,false) && !data.getStringExtra(SettingActivity.CITY_TAG).equals(""))){

            mCityPicked = data.getStringExtra(SettingActivity.CITY_TAG);
            mLocation = locationFormat(mCityPicked);

            getWeatherInfo();
        }
    }

    public void updateView(){


        preferences = new ChangePreferences(this);
        NotationFlag = preferences.getNotationSetting();
        mDayNum = preferences.getDayNumber();
        int days = Integer.parseInt(mDayNum);


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
        if (NotationFlag.equals("C")){
            mCurrentWind.setText(this.getString(R.string.wind_label) + mWeatherInfor.getmCurrentWindKph());
        }
        else if (NotationFlag.equals("F")){
            mCurrentWind.setText(this.getString(R.string.wind_label) + mWeatherInfor.getmCurrentWindMph());
        }

        mCurrentFeel = (TextView)findViewById(R.id.current_feelslike);
        if (NotationFlag.equals("C")){
            mCurrentFeel.setText(this.getString(R.string.feel_label) + mWeatherInfor.getmCurrentFeelsC());
        }
        else if (NotationFlag.equals("F")){
            mCurrentFeel.setText(this.getString(R.string.feel_label) + mWeatherInfor.getmCurrentFeelsF());
        }

        mCurrentUV = (TextView)findViewById(R.id.current_solar);
        mCurrentUV.setText(this.getString(R.string.UV_label) + mWeatherInfor.getmCurrentUV());

        mCurrentHumidity = (TextView)findViewById(R.id.current_humidity);
        mCurrentHumidity.setText(this.getString(R.string.humidity_label) + mWeatherInfor.getmCurrentHumidity());

        mCurrentPreHr = (TextView)findViewById(R.id.current_1hr_rain);
        if (NotationFlag.equals("C")){
            mCurrentPreHr.setText(this.getString(R.string.rain_hr_label) + mWeatherInfor.getmCurrentPreHrMetric());
        }
        else if (NotationFlag.equals("F")){
            mCurrentPreHr.setText(this.getString(R.string.rain_hr_label) + mWeatherInfor.getmCurrentPreHrIn());
        }

        mCurrentPreDay = (TextView)findViewById(R.id.current_day_rain);
        if (NotationFlag.equals("C")){
            mCurrentPreDay.setText(this.getString(R.string.rain_day_label) + mWeatherInfor.getmCurrentPreDayMetric());
        }
        else if (NotationFlag.equals("F")){
            mCurrentPreDay.setText(this.getString(R.string.rain_day_label) + mWeatherInfor.getmCurrentPreDayIn());
        }


        mListView = (ListView)findViewById(R.id.list_container);
        ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
        for (int i = 0; i < days; i++){
            forecasts.add(mWeatherInfor.getmForecastList()[i]);
        }
        /**
        for (Forecast i : mWeatherInfor.getmForecastList()){
            forecasts.add(i);
        }
         */
        WeatherListAdapter adapter = new WeatherListAdapter(forecasts);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecast forecast = new Forecast();
                forecast = (Forecast) parent.getItemAtPosition(position);
                mDescription = (TextView) findViewById(R.id.description_TextView);
                if (NotationFlag.equals("C")) {
                    mDescription.setText(forecast.getmWeekday() + WeatherPageActivity.this.getString(R.string.brief) + forecast.getmDescriptionC());
                } else if (NotationFlag.equals("F")) {
                    mDescription.setText(forecast.getmWeekday() + WeatherPageActivity.this.getString(R.string.brief) + forecast.getmDescriptionF());
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

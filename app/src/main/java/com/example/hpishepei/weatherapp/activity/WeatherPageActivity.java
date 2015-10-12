package com.example.hpishepei.weatherapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.asynctask.LocationAsyncTask;
import com.example.hpishepei.weatherapp.model.Weather;
import com.example.hpishepei.weatherapp.model.WeatherList;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity implements LocationAsyncTask.LocationUpdateListener{

    public static String NotationFlag;
    public static Double sLongitude = 0.0;
    public static Double sLatitude = 0.0;
    //public static Boolean AutoLocation;

    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mTodaySummery;
    private ListView mListView;
    private Button mRefreshButton;


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

        //GetLocationInfo getLocationInfo = new GetLocationInfo(this);
        //getLocationInfo.FetchCordinates();

        //GPSTracker gpsTracker = new GPSTracker(this);



        FetchCordinates fetchCordinates = new FetchCordinates();
        fetchCordinates.execute();


        //LocationAsyncTask asyncTask = new LocationAsyncTask(this);
        //asyncTask.setmLocationUpdateListener(this);
        //asyncTask.execute();

/**
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

 */




    }



    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;

        public double lati = 0.0;
        public double longi = 0.0;

        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mVeggsterLocationListener);

            progDailog = new ProgressDialog(WeatherPageActivity.this);
            progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();

        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");
            progDailog.dismiss();
            mLocationManager.removeUpdates(mVeggsterLocationListener);
        }

        @Override
        protected void onPostExecute(String result) {
            progDailog.dismiss();

            Toast.makeText(WeatherPageActivity.this,
                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (this.lati == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {

                    // LocatorService.myLatitude=location.getLatitude();

                    // LocatorService.myLongitude=location.getLongitude();

                    lati = location.getLatitude();
                    longi = location.getLongitude();

                } catch (Exception e) {
                    // progDailog.dismiss();
                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
                    // , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");

            }

        }

    }

     
    @Override
    public void LocationUpdated(String location) {

    }

    @Override
    public void LocationUpdateFail() {

    }

}

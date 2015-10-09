package com.example.hpishepei.weatherapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hpishepei.weatherapp.Constants;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.model.Location;
import com.example.hpishepei.weatherapp.model.LocationList;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    Button mFahrenheitButton;
    Button mCelsiusButton;
    ArrayList<Location> mLocationList;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.LOCATION_PREFERENCES, Context.MODE_PRIVATE);


        mLocationList = LocationList.getInstance(this).getmLocationList();


        mFahrenheitButton = (Button)findViewById(R.id.fahrenheit_Button);
        mFahrenheitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherPageActivity.NotationFlag = "F";
                mFahrenheitButton.setBackgroundColor(Color.rgb(0, 128, 255));
                mCelsiusButton.setBackgroundColor(Color.rgb(128, 128, 128));

            }
        });

        mCelsiusButton = (Button)findViewById(R.id.celsius_Button);
        mCelsiusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherPageActivity.NotationFlag = "C";
                mCelsiusButton.setBackgroundColor(Color.rgb(0,128,255));
                mFahrenheitButton.setBackgroundColor(Color.rgb(128,128,128));
            }
        });


        mListView = (ListView)findViewById(R.id.setting_list_container);
        //ArrayAdapter<Weather> adapter = new ArrayAdapter<Weather>(this,android.R.layout.simple_list_item_1,mWeatherList);
        SettingListAdapter adapter = new SettingListAdapter(mLocationList);
        mListView.setAdapter(adapter);
    }

    private class SettingListAdapter extends ArrayAdapter<Location>{
        public SettingListAdapter(ArrayList<Location> mLocaionList){
            super(SettingActivity.this, 0, mLocationList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = SettingActivity.this.getLayoutInflater().inflate(R.layout.weather_list, null);
            }


            Location location = getItem(position);

            TextView cityName = (TextView)convertView.findViewById(R.id.setting_city_name);
            cityName.setText(location.getmCity());
            return convertView;
        }

    }

}



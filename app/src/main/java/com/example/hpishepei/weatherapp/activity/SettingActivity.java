package com.example.hpishepei.weatherapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.model.Location;
import com.example.hpishepei.weatherapp.model.LocationList;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    Switch mNotationSwitch;
    ArrayList<Location> mLocationList;
    ListView mListView;
    ChangePreferences preferences;

    @Override
    protected void onResume() {
        System.out.print("onResume!!!!!!");

        super.onResume();
        preferences = new ChangePreferences(this);
        WeatherPageActivity.NotationFlag = preferences.getNotationSetting();
    }

    @Override
    protected void onPause() {
        System.out.print("onPause!!!!!!");
        preferences = new ChangePreferences(this);
        preferences.setNotationSetting(WeatherPageActivity.NotationFlag);
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);


        mLocationList = LocationList.getInstance(this).getmLocationList();

        Log.i("lll",mLocationList.get(0).getmCity());
        preferences = new ChangePreferences(this);

        WeatherPageActivity.NotationFlag = preferences.getNotationSetting();

        //String locations = sharedpreferences.getString(Constants.LOCATION_SETTING,"");
        //mLocationList = LocationList.getInstance(this).getmLocationList();

        mNotationSwitch = (Switch)findViewById(R.id.notation_switch);
        if (WeatherPageActivity.NotationFlag.equals("F")){
            mNotationSwitch.setChecked(true);
        }
        else{
            mNotationSwitch.setChecked(false);
        }
        mNotationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChangePreferences pref = new ChangePreferences(SettingActivity.this);
                if (isChecked){
                    pref.setNotationSetting("F");
                    WeatherPageActivity.NotationFlag = "F";
                    Log.i("ccc",WeatherPageActivity.NotationFlag);
                }
                else {
                    pref.setNotationSetting("C");
                    WeatherPageActivity.NotationFlag = "C";
                    Log.i("ccc",WeatherPageActivity.NotationFlag);


                }
            }
        });

/**
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
 */
        mListView = (ListView)findViewById(R.id.setting_list_container);

        //ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1,mLocationList);
        SettingListAdapter adapter = new SettingListAdapter(mLocationList);
        mListView.setAdapter(adapter);


    }


    private class SettingListAdapter extends ArrayAdapter<Location>{
        public SettingListAdapter(ArrayList<Location> mLocationList){
            super(SettingActivity.this, 0, mLocationList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = SettingActivity.this.getLayoutInflater().inflate(R.layout.setting_list, null);
            }


            Location location = getItem(position);

            TextView cityName = (TextView)convertView.findViewById(R.id.setting_city_name);
            cityName.setText(location.getmCity());
            return convertView;
        }
    }

}



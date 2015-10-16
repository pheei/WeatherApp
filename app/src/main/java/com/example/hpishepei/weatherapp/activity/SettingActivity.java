package com.example.hpishepei.weatherapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.asynctask.LoadWeatherAsyncTask;
import com.example.hpishepei.weatherapp.model.Location;
import com.example.hpishepei.weatherapp.model.LocationList;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements LoadWeatherAsyncTask.WeatherUpdateListener{


    private Switch mNotationSwitch;
    private ArrayList<Location> mLocationList;
    private ListView mListView;
    private ChangePreferences preferences;

    private EditText mZipEditText;
    private String mInputZip;
    private Boolean mIsStateChanged;
    public static final String EXTRA_STATE_CHANGE = "com.example.hpishepei.weatherapp.settingactivity.result";

    private Button mAddButton;


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
        setStateChangeResult(false);
        updateView();


    }



    public void updateView(){
        mLocationList = LocationList.getInstance(this).getmLocationList();

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
                setStateChangeResult(true);
                if (isChecked){
                    pref.setNotationSetting("F");
                    WeatherPageActivity.NotationFlag = "F";
                    Log.i("ccc", WeatherPageActivity.NotationFlag);
                }
                else {
                    pref.setNotationSetting("C");
                    WeatherPageActivity.NotationFlag = "C";
                    Log.i("ccc",WeatherPageActivity.NotationFlag);


                }
            }
        });

        mListView = (ListView)findViewById(R.id.setting_list_container);

        //ArrayAdapter<Location> adapter = new ArrayAdapter<Location>(this,android.R.layout.simple_list_item_1,mLocationList);
        SettingListAdapter adapter = new SettingListAdapter(mLocationList);
        mListView.setAdapter(adapter);


        mZipEditText = (EditText)findViewById(R.id.zip_enter_EditText);
        mZipEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInputZip = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAddButton = (Button)findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(SettingActivity.this,SettingActivity.this);
                task.execute("geolookup",mInputZip);
            }
        });
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


            final Location location = getItem(position);

            TextView cityName = (TextView)convertView.findViewById(R.id.setting_city_name);
            cityName.setText(location.getmCity());


            ImageView deleteButton = (ImageView)convertView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationList.getInstance(SettingActivity.this).deleteLocation(location);
                    updateView();

                }
            });


            return convertView;
        }
    }

    private void setStateChangeResult(boolean isStateChanged){
        Intent i = new Intent();
        i.putExtra(EXTRA_STATE_CHANGE,isStateChanged);
        setResult(RESULT_OK,i);

    }

    @Override
    public void updateCompleted(JsonObject jsonObject1, JsonObject jsonObject2, JsonObject jsonObject3, JsonObject jsonObject4) {

    }

    @Override
    public void updateFail() {

    }


}



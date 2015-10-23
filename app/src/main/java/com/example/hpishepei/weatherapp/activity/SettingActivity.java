package com.example.hpishepei.weatherapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hpishepei.weatherapp.ChangePreferences;
import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.asynctask.CityLookup;
import com.example.hpishepei.weatherapp.function.GetInfoFromJson;
import com.example.hpishepei.weatherapp.model.Location;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.widget.AdapterView.OnItemClickListener;

public class SettingActivity extends AppCompatActivity implements CityLookup.CityLookupListener{


    private Switch mNotationSwitch;
    private ArrayList<Location> mLocationList;
    private ListView mListView;
    private ChangePreferences preferences;

    private EditText mZipEditText;
    private String mInputZip;
    private Boolean mIsStateChanged;
    public static final String EXTRA_STATE_CHANGE = "com.example.hpishepei.weatherapp.settingactivity.result";
    public static final String CITY_TAG = "com.example.hpishepei.weatherapp.settingactivity.cityname";

    private Button mAddButton;
    private ArrayList<String> mCityList;

    private JsonObject mGeoJson;
    private ProgressDialog mProgressDialog;


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
        Log.i("lll", "set content");

        getList();
        Log.i("lll", Integer.toString(mCityList.size()));



        setStateChangeResult(false);
        updateView();


    }

    private void getList(){
        preferences = new ChangePreferences(this);
        mCityList = new ArrayList<String>();
        Set<String> set = preferences.getCitySet();
        if (set!=null){
            for (String i : set){
                mCityList.add(i);
            }
        }

    }

    private void saveList(){
        //mCitySet.addAll(mCityList);
        ChangePreferences changePreferences = new ChangePreferences(this);
        Set<String> set = new HashSet<String>(mCityList);
        changePreferences.setCitySet(set);
    }


    public void updateView(){
        //mLocationList = LocationList.getInstance(this).getmLocationList();

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

        //SettingListAdapter adapter = new SettingListAdapter(mLocationList);
        SettingListAdapter adapter = new SettingListAdapter(mCityList);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = (String)parent.getItemAtPosition(position);
                Intent i = new Intent(SettingActivity.this, WeatherPageActivity.class);
                i.putExtra(CITY_TAG,city);
                startActivity(i);
            }
        });


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
                finish();
                /**
                CityLookup task = new CityLookup(SettingActivity.this,SettingActivity.this);
                task.execute(mInputZip);
                mProgressDialog = new ProgressDialog(SettingActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage(SettingActivity.this.getString(R.string.fetch_city_label));
                mProgressDialog.show();
                 */


                /**
                mGeoJson =

                mCityList.add(mInputZip);
                Log.i("lll", "add");

                saveList();
                Log.i("lll", "save");

                getList();
                Log.i("lll", "get");

                updateView();
                 */
            }
        });

        /**
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadWeatherAsyncTask task = new LoadWeatherAsyncTask(SettingActivity.this,SettingActivity.this);
                task.execute("geolookup",mInputZip);
            }
        });
         */
    }




    private class SettingListAdapter extends ArrayAdapter<String>{
        public SettingListAdapter(ArrayList<String> mCitylist){
            super(SettingActivity.this, 0, mCitylist);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = SettingActivity.this.getLayoutInflater().inflate(R.layout.setting_list, null);
            }
            final String city = getItem(position);

            TextView cityName = (TextView)convertView.findViewById(R.id.setting_city_name);
            cityName.setText(city);


            ImageView deleteButton = (ImageView)convertView.findViewById(R.id.delete_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCityList.remove(city);
                    saveList();
                    getList();
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
    public void lookupCompleted(JsonObject jsonObject) {
        mProgressDialog.dismiss();
        JsonObject geoJson = jsonObject;
        if (geoJson.getAsJsonObject("location")==null){
            Toast.makeText(this,this.getString(R.string.zip_fail_label),Toast.LENGTH_SHORT).show();
        }
        else {

            String city = GetInfoFromJson.getCityNameFromJSON(geoJson);
            mCityList.add(city);
            saveList();
            getList();
            updateView();
        }
    }

    @Override
    public void lookupFail() {
        mProgressDialog.dismiss();
    }

}



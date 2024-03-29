package com.example.hpishepei.weatherapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
    private EditText mDays;
    private Button mUpdate;

    private EditText mZipEditText;
    private String mInputZip;
    private Boolean mIsStateChanged;
    private Boolean mIsNormalMode;
    private String mCityPicked;
    private String mDayNum;
    private String mInputDays;
    public static final String EXTRA_STATE_CHANGE = "com.example.hpishepei.weatherapp.settingactivity.result";
    public static final String CITY_TAG = "com.example.hpishepei.weatherapp.settingactivity.cityname";

    private Button mAddButton;
    private ArrayList<String> mCityList;

    private JsonObject mGeoJson;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onResume() {
        super.onResume();
        preferences = new ChangePreferences(this);
        WeatherPageActivity.NotationFlag = preferences.getNotationSetting();
    }

    @Override
    protected void onPause() {
        preferences = new ChangePreferences(this);
        preferences.setNotationSetting(WeatherPageActivity.NotationFlag);
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        mCityPicked = "";

        getList();

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
        ChangePreferences changePreferences = new ChangePreferences(this);
        Set<String> set = new HashSet<String>(mCityList);
        changePreferences.setCitySet(set);
    }


    public void updateView(){

        preferences = new ChangePreferences(this);

        WeatherPageActivity.NotationFlag = preferences.getNotationSetting();


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
                }
                else {
                    pref.setNotationSetting("C");
                    WeatherPageActivity.NotationFlag = "C";


                }
            }
        });


        mDays = (EditText)findViewById(R.id.number_of_days_EditText);
        mDayNum = preferences.getDayNumber();
        mDays.setText("  "+mDayNum+"  ");
        mInputDays = mDayNum;
        mDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInputDays = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mUpdate = (Button)findViewById(R.id.update_days_button);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mInputDays = mInputDays.replace(" ","");

                if (mInputDays.equals("1") || mInputDays.equals("2")|| mInputDays.equals("3")|| mInputDays.equals("4")){
                    preferences.setDayNumber(mInputDays);
                    Toast.makeText(SettingActivity.this,SettingActivity.this.getString(R.string.days_updated_label),Toast.LENGTH_SHORT).show();
                    updateView();
                }
                else {

                    Toast.makeText(SettingActivity.this,SettingActivity.this.getString(R.string.days_number_label),Toast.LENGTH_SHORT).show();
                    updateView();
                }

            }
        });



        mListView = (ListView)findViewById(R.id.setting_list_container);

        SettingListAdapter adapter = new SettingListAdapter(mCityList);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCityPicked = (String)parent.getItemAtPosition(position);
                setStateChangeResult(true);
                finish();
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

                CityLookup task = new CityLookup(SettingActivity.this,SettingActivity.this);
                task.execute(mInputZip);
                mProgressDialog = new ProgressDialog(SettingActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage(SettingActivity.this.getString(R.string.fetch_city_label));
                mProgressDialog.show();

            }
        });

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
        i.putExtra(CITY_TAG,mCityPicked);
        setResult(RESULT_OK, i);

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



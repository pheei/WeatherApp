package com.example.hpishepei.weatherapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.hpishepei.weatherapp.R;
import com.example.hpishepei.weatherapp.model.Weather;
import com.example.hpishepei.weatherapp.model.WeatherList;

import java.util.ArrayList;

public class WeatherPageActivity extends AppCompatActivity {

    private String NotationFlag = "C";

    private TextView mCurrentCityTextView;
    private TextView mCurrentWeather;
    private TextView mCurrentTemp;
    private TextView mTodaySummery;

    ArrayList<Weather> mWeatherList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);


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






    }

}

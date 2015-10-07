package com.example.hpishepei.weatherapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.hpishepei.weatherapp.R;

public class WeatherPageActivity extends AppCompatActivity {

    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);

        location = (TextView)findViewById(R.id.location);
        location.setText("dc");


    }

}

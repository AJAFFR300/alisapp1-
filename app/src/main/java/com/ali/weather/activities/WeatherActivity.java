package com.ali.weather.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.ali.weather.R;
import com.ali.weather.databinding.ActivityWeatherBinding;
import com.ali.weather.fragments.CurrentFragment;
import com.ali.weather.fragments.ForecastFragment;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;

import java.util.Arrays;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    ActivityWeatherBinding binding;
    WeatherItem item;
    List<String> namelist;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_weather);
        item = getIntent().getParcelableExtra("data");
        init();
        setListeners();
        loadFragments();
    }

    public void init(){
        namelist = Arrays.asList(Constants.IDS);
        binding.location.setText(Constants.NAMES[namelist.indexOf(item.id)]);


    }

    public void setListeners(){
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setSelected(0);
                transaction = getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                transaction.show((CurrentFragment) fragmentManager.findFragmentByTag("Fragment1"));
                transaction.hide((ForecastFragment) fragmentManager.findFragmentByTag("Fragment2"));
                transaction.commit();
            }
        });

        binding.forecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setSelected(1);
                transaction = getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                transaction.hide((CurrentFragment) fragmentManager.findFragmentByTag("Fragment1"));
                transaction.show((ForecastFragment) fragmentManager.findFragmentByTag("Fragment2"));
                transaction.commit();
            }
        });
    }

    public void loadFragments(){
        CurrentFragment currentFragment = new CurrentFragment();
        ForecastFragment forecastFragment = new ForecastFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, currentFragment, "Fragment1");
        transaction.add(R.id.container, forecastFragment, "Fragment2");
        transaction.hide(forecastFragment);
        currentFragment.item = item;
        forecastFragment.item = item;
        transaction.commit();
    }
}
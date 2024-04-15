package com.ali.weather.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ali.weather.R;
import com.ali.weather.databinding.FragmentCurrentWeatherBinding;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;
import com.ali.weather.utilities.Utils;
import com.ali.weather.viewmodels.WeatherViewModel;


public class CurrentFragment extends Fragment {

    WeatherViewModel weatherViewModel;
    Activity activity;
    public WeatherItem item;
    PrefManager prefManager;
    FragmentCurrentWeatherBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_weather,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListeners();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void init(){
        prefManager = new PrefManager(activity);
        weatherViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(WeatherViewModel.class);
    }

    public void setListeners(){
        if (prefManager.getString(Constants.SELECTED_SCALE) != null){
            if (prefManager.getString(Constants.SELECTED_SCALE).equals("Celcius")){
                binding.setIsCelsius(true);
            }else{
                binding.setIsCelsius(false);;
            }
        }else{
            binding.setIsCelsius(true);
        }

        weatherViewModel.getWeatherInfo(item.id).removeObservers(getViewLifecycleOwner());
        weatherViewModel.getWeatherInfo(item.id).observe(getViewLifecycleOwner(),new Observer<WeatherItem>() {
            @Override
            public void onChanged(WeatherItem weatherItem) {
                binding.setWeather(weatherItem);
                binding.weatherIcon.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), Utils.getImage(weatherItem.getWeather()),null));
            }
        });
    }
}

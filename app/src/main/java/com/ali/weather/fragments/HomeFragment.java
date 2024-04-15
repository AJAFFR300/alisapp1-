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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ali.weather.R;
import com.ali.weather.databinding.FragmentHomeBinding;
import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;
import com.ali.weather.utilities.Utils;
import com.ali.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    
    Activity activity;
    WeatherViewModel weatherViewModel;
    PrefManager prefManager;
    List<ForecastItem> forecasts = new ArrayList<>();
    com.ali.weather.databinding.FragmentHomeBinding binding;
    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = androidx.databinding.DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListeners();
        setData();
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

        binding.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.getInt(Constants.SELECTED_LOCATION) == 0){
                    prefManager.setInt(Constants.SELECTED_LOCATION,Constants.IDS.length);
                }
                int index = prefManager.getInt(Constants.SELECTED_LOCATION) - 1;
                prefManager.setInt(Constants.SELECTED_LOCATION,index);
                setData();
            }
        });

        binding.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefManager.getInt(Constants.SELECTED_LOCATION) == (Constants.IDS.length-1)){
                    prefManager.setInt(Constants.SELECTED_LOCATION,-1);
                }
                int index = prefManager.getInt(Constants.SELECTED_LOCATION) + 1;
                prefManager.setInt(Constants.SELECTED_LOCATION,index);
                setData();
            }
        });

        binding.day1Tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsBottomSheet detailsBottomSheet =
                        DetailsBottomSheet.newInstance(forecasts.get(0));
                detailsBottomSheet.show(getChildFragmentManager(),
                        DetailsBottomSheet.TAG);
                detailsBottomSheet.setOnItemClickListener(new DetailsBottomSheet.OnItemClick() {
                    @Override
                    public void onDismiss() {
                        detailsBottomSheet.dismiss();
                    }
                });
            }
        });

        binding.day2Tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsBottomSheet detailsBottomSheet =
                        DetailsBottomSheet.newInstance(forecasts.get(1));
                detailsBottomSheet.show(getChildFragmentManager(),
                        DetailsBottomSheet.TAG);
                detailsBottomSheet.setOnItemClickListener(new DetailsBottomSheet.OnItemClick() {
                    @Override
                    public void onDismiss() {
                        detailsBottomSheet.dismiss();
                    }
                });
            }
        });

        binding.day3Tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsBottomSheet detailsBottomSheet =
                        DetailsBottomSheet.newInstance(forecasts.get(2));
                detailsBottomSheet.show(getChildFragmentManager(),
                        DetailsBottomSheet.TAG);
                detailsBottomSheet.setOnItemClickListener(new DetailsBottomSheet.OnItemClick() {
                    @Override
                    public void onDismiss() {
                        detailsBottomSheet.dismiss();
                    }
                });
            }
        });

    }

    public void setData(){

        if (prefManager.getString(Constants.SELECTED_SCALE) != null){
            if (prefManager.getString(Constants.SELECTED_SCALE).equals("Celcius")){
                binding.setIsCelsius(true);
            }else{
                binding.setIsCelsius(false);;
            }
        }else{
            binding.setIsCelsius(true);
        }

        binding.setLocation(Constants.NAMES[prefManager.getInt(Constants.SELECTED_LOCATION)]);

        weatherViewModel.getWeatherInfo(Constants.IDS[prefManager.getInt(Constants.SELECTED_LOCATION)]).removeObservers(getViewLifecycleOwner());
        weatherViewModel.getWeatherInfo(Constants.IDS[prefManager.getInt(Constants.SELECTED_LOCATION)]).observe(getViewLifecycleOwner(),new Observer<WeatherItem>() {
            @Override
            public void onChanged(WeatherItem weatherItem) {
                if (weatherItem != null){
                    binding.setWeather(weatherItem);
                    binding.weatherIcon.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(),Utils.getImage(weatherItem.getWeather()),null));
                }
            }
        });

        weatherViewModel.getForecastInfo(Constants.IDS[prefManager.getInt(Constants.SELECTED_LOCATION)]).removeObservers(getViewLifecycleOwner());
        weatherViewModel.getForecastInfo(Constants.IDS[prefManager.getInt(Constants.SELECTED_LOCATION)]).observe(getViewLifecycleOwner(),new Observer<List<ForecastItem>>() {
            @Override
            public void onChanged(List<ForecastItem> forecastItems) {
                if (forecastItems != null && forecastItems.size()>0){
                    forecasts = forecastItems;
                    binding.day1.setText(forecastItems.get(0).getDay());
                    binding.day1Image.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(),Utils.getImage(forecastItems.get(0).getWeather()),null));
                    binding.day1Min.setText(forecastItems.get(0).getMinimumTemperature() != null ? Utils.getSplittedTemperature(forecastItems.get(0).getMinimumTemperature())[binding.getIsCelsius() ? 0 : 1] : "--");
                    binding.day1Max.setText(forecastItems.get(0).getMaximumTemperature() != null ? Utils.getSplittedTemperature(forecastItems.get(0).getMaximumTemperature())[binding.getIsCelsius() ? 0 : 1] : "--");

                    if (forecastItems.size() >= 2) {
                        binding.day2.setText(forecastItems.get(1).getDay());
                        binding.day2Min.setText(forecastItems.get(1).getMinimumTemperature() != null ? Utils.getSplittedTemperature(forecastItems.get(1).getMinimumTemperature())[binding.getIsCelsius() ? 0 : 1] : "--");
                        binding.day2Max.setText(forecastItems.get(1).getMaximumTemperature() != null ? Utils.getSplittedTemperature(forecastItems.get(1).getMaximumTemperature())[binding.getIsCelsius() ? 0 : 1] : "--");
                        binding.day2Image.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), Utils.getImage(forecastItems.get(1).getWeather()), null));
                    }

                    if (forecastItems.size() >= 3) {
                        binding.day3.setText(forecastItems.get(2).getDay());
                        binding.day3Min.setText(forecastItems.get(2).getMinimumTemperature() != null ? Utils.getSplittedTemperature(forecastItems.get(2).getMinimumTemperature())[binding.getIsCelsius() ? 0 : 1] : "--");
                        binding.day3Max.setText(forecastItems.get(2).getMaximumTemperature() != null ? Utils.getSplittedTemperature(forecastItems.get(2).getMaximumTemperature())[binding.getIsCelsius() ? 0 : 1] : "--");
                        binding.day3Image.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), Utils.getImage(forecastItems.get(2).getWeather()), null));
                    }
                }
            }
        });
    }
}

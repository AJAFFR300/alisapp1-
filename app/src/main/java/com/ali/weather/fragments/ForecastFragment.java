package com.ali.weather.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.ali.weather.adapter.ForecastAdapter;
import com.ali.weather.databinding.FragmentWeatherForecastBinding;
import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.Utils;
import com.ali.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;


public class ForecastFragment extends Fragment {

    Activity activity;
    public WeatherItem item;
    ForecastAdapter adapter;
    List<ForecastItem> data;
    WeatherViewModel weatherViewModel;
    FragmentWeatherForecastBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_forecast,container,false);
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
        weatherViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(WeatherViewModel.class);

        data = new ArrayList<>();
        adapter = new ForecastAdapter(activity,data);
        binding.recycler.setAdapter(adapter);
    }

    public void setListeners(){

        weatherViewModel.getForecastInfo(item.id).removeObservers(getViewLifecycleOwner());
        weatherViewModel.getForecastInfo(item.id).observe(getViewLifecycleOwner(),new Observer<List<ForecastItem>>() {
            @Override
            public void onChanged(List<ForecastItem> forecastItems) {
                data.addAll(forecastItems);
                adapter.notifyDataSetChanged();
            }
        });
    }
}

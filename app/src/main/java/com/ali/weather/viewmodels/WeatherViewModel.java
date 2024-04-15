package com.ali.weather.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.repositories.WeatherRepository;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository weatherRepository;
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(application);
    }

    public void updateDatabase(){
        weatherRepository.updateDatabase();
    }
    public LiveData<WeatherItem> getWeatherInfo(String id){
        return weatherRepository.getWeatherInfo(id);
    }

    public LiveData<List<ForecastItem>> getForecastInfo(String id){
        return weatherRepository.getForecastInfo(id);
    }

    public LiveData<List<WeatherItem>> getAllWeatherInfo(){
        return weatherRepository.getAllWeatherInfo();
    }

    public ForecastItem getItemInfo(String id){
        return weatherRepository.getItemInfo(id);
    }
}

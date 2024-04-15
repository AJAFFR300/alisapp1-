package com.ali.weather.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.ali.weather.model.WeatherItem;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("Select * FROM WeatherItem where id=:id")
    LiveData<WeatherItem> getWeatherInfo(String id);

    @Query("Select * FROM WeatherItem")
    LiveData<List<WeatherItem>> getAllWeatherInfo();

    @Insert(onConflict = REPLACE)
    void insertInfo(WeatherItem info);

}

package com.ali.weather.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;

import java.util.List;

@Dao
public interface ForecastDao {

    @Query("Select * FROM ForecastItem where cityId =:id")
    LiveData<List<ForecastItem>> getForecastItemInfo(String id);

    @Insert(onConflict = REPLACE)
    void insertInfo(ForecastItem info);

    @Query("Select * FROM ForecastItem where id =:id")
    ForecastItem getItemInfo(String id);

}

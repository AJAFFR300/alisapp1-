package com.ali.weather.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;


@Database(entities = {WeatherItem.class, ForecastItem.class}, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public static final String DATABASE_NAME = "weather_room";
    private static RoomDatabase instance = null;

    public static synchronized RoomDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),RoomDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract WeatherDao weatherDao();
    public abstract ForecastDao forecastDao();
}

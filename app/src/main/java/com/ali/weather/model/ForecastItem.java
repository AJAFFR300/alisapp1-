package com.ali.weather.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ForecastItem {

    @PrimaryKey
    @NonNull
    public String id = "";
    @ColumnInfo
    public String cityId;
    @ColumnInfo
    public String day;
    @ColumnInfo
    public String weather;
    @ColumnInfo
    public String minimumTemperature;
    @ColumnInfo
    public String maximumTemperature;
    @ColumnInfo
    public String wind;
    @ColumnInfo
    public String windDirection;
    @ColumnInfo
    public String humidity;
    @ColumnInfo
    public String visibility;
    @ColumnInfo
    public String pressure;
    @ColumnInfo
    public String pressure_type;
    @ColumnInfo
    public String uv;
    @ColumnInfo
    public String pollution;
    @ColumnInfo
    public String sunRise;
    @ColumnInfo
    public String sunSet;
    @ColumnInfo
    public String latitude;
    @ColumnInfo
    public String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public String getDay() {
        return day;
    }

    public String getWeather() {
        return weather;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public String getWind() {
        return wind;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public String getPressure_type() {
        return pressure_type;
    }

    public String getUv() {
        return uv;
    }

    public String getPollution() {
        return pollution;
    }

    public String getSunRise() {
        return sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}

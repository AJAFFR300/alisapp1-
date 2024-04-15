package com.ali.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class WeatherItem implements Parcelable {

    @PrimaryKey
    @NonNull public  String id = "";
    @ColumnInfo
    public String weather;
    @ColumnInfo
    public String temperature;
    @ColumnInfo
    public String wind;
    @ColumnInfo
    public String windDirection;
    @ColumnInfo
    public String humidity;
    @ColumnInfo
    public String pressure;
    @ColumnInfo
    public String pressure_type;
    @ColumnInfo
    public String latitude;
    @ColumnInfo
    public String longitude;

    public WeatherItem() {
    }

    protected WeatherItem(Parcel in) {
        id = Objects.requireNonNull(in.readString());
        weather = in.readString();
        temperature = in.readString();
        wind = in.readString();
        windDirection = in.readString();
        humidity = in.readString();
        pressure = in.readString();
        pressure_type = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<WeatherItem> CREATOR = new Creator<WeatherItem>() {
        @Override
        public WeatherItem createFromParcel(Parcel in) {
            return new WeatherItem(in);
        }

        @Override
        public WeatherItem[] newArray(int size) {
            return new WeatherItem[size];
        }
    };

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getWeather() {
        return weather;
    }

    public String getTemperature() {
        return temperature;
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

    public String getPressure() {
        return pressure;
    }

    public String getPressure_type() {
        return pressure_type;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(weather);
        dest.writeString(temperature);
        dest.writeString(wind);
        dest.writeString(windDirection);
        dest.writeString(humidity);
        dest.writeString(pressure);
        dest.writeString(pressure_type);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}

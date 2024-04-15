package com.ali.weather.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.core.content.res.ResourcesCompat;

import com.ali.weather.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String[] getSplittedTemperature(String temperatureString){
        String[] data = {"",""};
        if (temperatureString == null){
            return data;
        }
        Pattern pattern = Pattern.compile("(\\d+°C) \\((\\d+°F)\\)");
        Matcher matcher = pattern.matcher(temperatureString);
        if (matcher.find()) {
            String celsiusTemperature = matcher.group(1); // "14°C"
            String fahrenheitTemperature = matcher.group(2); // "57°F"
            data[0] = celsiusTemperature;
            data[1] = fahrenheitTemperature;
        }
        return data;
    }

    public static int getImage(String weather){
        if (weather.toLowerCase().contains("fog")
                || weather.toLowerCase().contains("haze")
                || weather.toLowerCase().contains("hazy")){
            return R.drawable.foggy;
        }else if (weather.toLowerCase().contains("rain")
                || weather.toLowerCase().contains("showers")
                || weather.toLowerCase().contains("drizzle")){
            return R.drawable.heavy_rain;
        }else if (weather.toLowerCase().contains("sunny")
                || weather.toLowerCase().contains("clear sky")){
            return R.drawable.new_moon;
        }else if (weather.toLowerCase().contains("clouds")
                || weather.toLowerCase().contains("cloud")
                || weather.toLowerCase().contains("cloudy")){
            return R.drawable.cloudy_day;
        }else if (weather.toLowerCase().contains("snow")){
            return R.drawable.snow;
        }else{
            return R.drawable.cloudy_day;
        }
    }

    public static int getImageFavourites(String weather){
        if (weather.toLowerCase().contains("fog")
                || weather.toLowerCase().contains("haze")
                || weather.toLowerCase().contains("hazy")){
            return R.drawable.cloud;
        }else if (weather.toLowerCase().contains("rain")
                || weather.toLowerCase().contains("showers")
                || weather.toLowerCase().contains("drizzle")){
            return R.drawable.rainy;
        }else if (weather.toLowerCase().contains("sunny")
                || weather.toLowerCase().contains("clear sky")){
            return R.drawable.sun;
        }else if (weather.toLowerCase().contains("clouds")
                || weather.toLowerCase().contains("cloud")
                || weather.toLowerCase().contains("cloudy")){
            return R.drawable.cloudy;
        }else if (weather.toLowerCase().contains("snow")){
            return R.drawable.snowflake;
        }else{
            return R.drawable.cloudy;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}

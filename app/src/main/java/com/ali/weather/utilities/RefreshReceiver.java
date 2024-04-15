package com.ali.weather.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.lifecycle.ViewModelProvider;

import com.ali.weather.repositories.WeatherRepository;
import com.ali.weather.viewmodels.WeatherViewModel;

import java.util.Calendar;


public class RefreshReceiver extends BroadcastReceiver {

    RefreshAlarmManager alarmManager;
    WeatherRepository repository;
    PrefManager prefManager;
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        prefManager = new PrefManager(context);
        alarmManager = new RefreshAlarmManager(context);
        repository = new WeatherRepository(context);
        repository.updateDatabase();
        int code = intent.getIntExtra("code",0);

        Calendar alarmCalendar = Calendar.getInstance();
        String[] time;
        if (code == 102){

            time = prefManager.getString(Constants.SELECTED_TIME).split(":");
        }else{
            time = prefManager.getString(Constants.SELECTED_TIME2).split(":");
        }
        alarmCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        alarmCalendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        alarmCalendar.set(Calendar.SECOND, 0);
        alarmManager.setAlarm(code,alarmCalendar);
    }
}


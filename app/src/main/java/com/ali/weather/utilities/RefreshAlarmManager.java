package com.ali.weather.utilities;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class RefreshAlarmManager {

    private Context context;
    private AlarmManager alarmManager;

    public RefreshAlarmManager(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    }

    public void setAlarm(int requestCode, Calendar calendar){
        cancelAlarm(requestCode);
        Intent intent = new Intent(context,RefreshReceiver.class);
        intent.putExtra("code",requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DATE,1);
        }
        long alarmStartTime = calendar.getTimeInMillis();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent
        );
    }

    public void cancelAlarm(int requestCode){
        Intent intent = new Intent(context,RefreshReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);
    }
}

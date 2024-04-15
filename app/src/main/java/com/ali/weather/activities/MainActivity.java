package com.ali.weather.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ali.weather.R;
import com.ali.weather.databinding.ActivityMainBinding;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;
import com.ali.weather.utilities.RefreshAlarmManager;
import com.ali.weather.utilities.RefreshReceiver;
import com.ali.weather.utilities.Utils;
import com.ali.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    ActivityMainBinding binding;
    RefreshAlarmManager refreshAlarmManager;
    PrefManager prefManager;
    WeatherViewModel weatherViewModel;
    private static final int PERMISSION_CODE_ALARM = 1023;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();
        setListeners();
        checkAndRequestAlarmPermissions();
    }

    public void init(){
        weatherViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(WeatherViewModel.class);
        prefManager = new PrefManager(this);
        refreshAlarmManager = new RefreshAlarmManager(MainActivity.this);
        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }

    public void setListeners(){
        weatherViewModel.getAllWeatherInfo().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(List<WeatherItem> weatherItems) {
                binding.setIsLoading(false);
                if (weatherItems != null && weatherItems.size()>0){
                    weatherViewModel.getAllWeatherInfo().removeObservers(MainActivity.this);
                    binding.noInternetLayout.setVisibility(View.GONE);
                }else{
                    if (!Utils.isNetworkAvailable(MainActivity.this)){
                        binding.noInternetLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        binding.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkAvailable(MainActivity.this)){
                    binding.noInternetLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else{
                    binding.setIsLoading(true);
                    weatherViewModel.updateDatabase();
                }
            }
        });
    }

    public boolean checkAndRequestAlarmPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int exact = ContextCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM);

            if (exact != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.USE_EXACT_ALARM);
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            int schedule = ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM);
            if (schedule != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.SCHEDULE_EXACT_ALARM);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_CODE_ALARM);
            return false;
        }else {
            if (prefManager.getString(Constants.SELECTED_TIME) == null){
                setAlarm( "08:00" ,102);
                setAlarm("20:00",103);
            }
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE_ALARM: {
                Map<String, Integer> perms = new HashMap<>();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    perms.put(Manifest.permission.USE_EXACT_ALARM, PackageManager.PERMISSION_GRANTED);
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                    perms.put(Manifest.permission.SCHEDULE_EXACT_ALARM, PackageManager.PERMISSION_GRANTED);
                }

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (perms.get(Manifest.permission.USE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
                            setAlarm("08:00",102);
                            setAlarm("20:00",103);
                        }else {
                            explain("You need to give permissions to continue. Do you want to give permission?");
                        }
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                        if (perms.get(Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
                            setAlarm("08:00",102);
                            setAlarm("20:00",103);
                        }else {
                            explain("You need to give permissions to continue. Do you want to give permission?");
                        }
                    }

                }
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        checkAndRequestAlarmPermissions();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void setAlarm(String timeStr, int requestCode){
        Log.d("TAG", "setAlarm: ");
        prefManager.setString(requestCode == 102 ? Constants.SELECTED_TIME : Constants.SELECTED_TIME2,timeStr);
        Calendar alarmCalendar = Calendar.getInstance();
        String[] time = timeStr.split(":");
        alarmCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        alarmCalendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        alarmCalendar.set(Calendar.SECOND, 0);
        refreshAlarmManager.setAlarm(requestCode, alarmCalendar);
    }
}
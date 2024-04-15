package com.ali.weather.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.ali.weather.R;
import com.ali.weather.activities.MainActivity;
import com.ali.weather.databinding.FragmentSettingsBinding;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SettingFragment extends Fragment {

    Activity activity;
    FragmentSettingsBinding binding;
    MainActivity mainActivity;
    PrefManager prefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setListeners();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
        mainActivity = (MainActivity) context;
    }

    public void init(){
        prefManager = new PrefManager(activity);
        if (prefManager.getString(Constants.SELECTED_SCALE) != null){
            if (prefManager.getString(Constants.SELECTED_SCALE).equals("Celcius")){
                binding.scale.setSelection(0);
            }else{
                binding.scale.setSelection(1);
            }
        }
    }

    public void setListeners(){

        binding.scale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefManager.setString(Constants.SELECTED_SCALE , parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer1();
            }
        });

        binding.time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer2();
            }
        });
    }

    public void setTimer1(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                binding.time.setText(formatTime(hourOfDay + ":" + minute));
                if (mainActivity.checkAndRequestAlarmPermissions()){
                    mainActivity.setAlarm(formatTime(hourOfDay + ":" + minute),102);
                }
             }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                 if (which == DialogInterface.BUTTON_NEGATIVE) {
                       dialog.cancel();
                 }
            }
        });
        timePicker.show();
    }

    public void setTimer2(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                binding.time2.setText(formatTime(hourOfDay + ":" + minute));
                if (mainActivity.checkAndRequestAlarmPermissions()){
                    mainActivity.setAlarm(formatTime(hourOfDay + ":" + minute),103);
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    dialog.cancel();
                }
            }
        });
        timePicker.show();
    }

    public static String formatTime(String time){
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.US);
        try {
            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

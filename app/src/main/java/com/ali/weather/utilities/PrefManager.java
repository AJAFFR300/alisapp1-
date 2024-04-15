package com.ali.weather.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "Weather";


    public PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setString(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    public void setInt(String key, int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }
 }

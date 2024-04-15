package com.ali.weather.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ali.weather.R;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.Utils;
import com.ali.weather.viewmodels.WeatherViewModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isTrigger = false;
    WeatherViewModel weatherViewModel;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_splash);
       init();
    }

    public void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        };
        weatherViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(WeatherViewModel.class);
        if (Utils.isNetworkAvailable(SplashActivity.this)) {
            weatherViewModel.updateDatabase();
        }
    }

    @Override
    protected void onPause() {
       super.onPause();
       handler.removeCallbacks(runnable);
       handler.removeCallbacksAndMessages(null);
       isTrigger = false;
    }

    @Override
    protected void onResume() {
       super.onResume();
       if (!isTrigger){
           isTrigger = true;
           handler.postDelayed(runnable,2000);
       }
    }
}
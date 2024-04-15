package com.ali.weather.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ali.weather.R;
import com.ali.weather.activities.WeatherActivity;
import com.ali.weather.databinding.CustomInfoItemBinding;
import com.ali.weather.databinding.FragmentMapBinding;
import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;
import com.ali.weather.utilities.Utils;
import com.ali.weather.viewmodels.WeatherViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;


public class MapFragment extends Fragment {

    Activity activity;
    SupportMapFragment mapFragment;
    FragmentMapBinding binding;
    private GoogleMap mGoogleMap;
    private float ZOOM_LEVEL = 12;
    WeatherViewModel weatherViewModel;
    PrefManager prefManager;
    List<WeatherItem> weathers;
    List<String> namelist;
    private static final String TAG = "MapFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map,container,false);
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
    }

    public void init(){
        namelist = Arrays.asList(Constants.IDS);
        prefManager = new PrefManager(activity);
        weatherViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(WeatherViewModel.class);

        FragmentManager fragmentManager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
    }

    public void setListeners(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mGoogleMap = mMap;
                mGoogleMap.setBuildingsEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    private View infoWindow;

                    @Nullable
                    @Override
                    public View getInfoContents(@NonNull Marker marker) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        infoWindow = getLayoutInflater().inflate(R.layout.custom_info_item,
                                (FrameLayout) binding.getRoot().findViewById(R.id.map), false);

                        boolean isCalcius = false;
                        if (prefManager.getString(Constants.SELECTED_SCALE) != null){
                            isCalcius = prefManager.getString(Constants.SELECTED_SCALE).equals("Celcius");
                        }else{
                            isCalcius = true;
                        }

                        WeatherItem item = weathers.get(Integer.parseInt(String.valueOf(marker.getTag())));
                        TextView temp = infoWindow.findViewById(R.id.temp);
                        TextView location = infoWindow.findViewById(R.id.location);
                        TextView humidity = infoWindow.findViewById(R.id.humidity);
                        TextView wind = infoWindow.findViewById(R.id.wind);
                        ImageView icon = infoWindow.findViewById(R.id.icon);

                        temp.setText(Utils.getSplittedTemperature(item.getTemperature())[isCalcius ? 0 : 1]);
                        location.setText(Constants.NAMES[namelist.indexOf(item.id)]);
                        humidity.setText(item.getHumidity());
                        wind.setText(item.getWind());
                        icon.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), Utils.getImageFavourites(item.getWeather()), null));

                        return infoWindow;
                    }
                });

                mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        WeatherItem item = weathers.get(Integer.parseInt(String.valueOf(marker.getTag())));
                        startActivity(new Intent(activity, WeatherActivity.class).putExtra("data",item));
                    }
                });
                addMarkers();
            }
        });
    }

    public void addMarkers(){
        weatherViewModel.getAllWeatherInfo().observe(getViewLifecycleOwner(), new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(List<WeatherItem> weatherItems) {
                weathers = weatherItems;
                LatLng latLng = null;
                for (int i=0;i<weatherItems.size();i++){
                    MarkerOptions marker = new MarkerOptions();
                    ForecastItem forecastItem = weatherViewModel.getItemInfo(weatherItems.get(i).getId()+"1");
                    marker.position(new LatLng(Double.parseDouble(forecastItem.latitude), Double.parseDouble(forecastItem.longitude)));
                    marker.icon(getMarkerIcon(Constants.NAMES[namelist.indexOf(weatherItems.get(i).id)]));
                    mGoogleMap.addMarker(marker).setTag(i);
                    if (Constants.IDS[prefManager.getInt(Constants.SELECTED_LOCATION)].equals(weatherItems.get(i).id)){
                        latLng = new LatLng(Double.parseDouble(forecastItem.latitude), Double.parseDouble(forecastItem.longitude));
                    }
                }

                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,ZOOM_LEVEL));
            }
        });
    }

    private BitmapDescriptor getMarkerIcon(String text) {
        int markerSize = 150; // Adjust the size of the marker as needed
        int textSize = 30;   // Adjust the size of the text as needed

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ResourcesCompat.getColor(getResources(),R.color.text_color,null));
        paint.setStyle(Paint.Style.FILL);

        Bitmap bitmap = Bitmap.createBitmap(markerSize, markerSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(markerSize / 2, markerSize / 2, markerSize / 2, paint);

        paint.setColor(ResourcesCompat.getColor(getResources(),R.color.white,null));
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        float textWidth = paint.measureText(text);
        float x = (markerSize - textWidth) / 2;
        float y = (markerSize - textSize) / 2 + textSize;

        canvas.drawText(text, x, y, paint);

        Drawable drawable = new BitmapDrawable(activity.getResources(), bitmap);
        return BitmapDescriptorFactory.fromBitmap(drawableToBitmap(drawable));
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        }

        return bitmap;
    }
}

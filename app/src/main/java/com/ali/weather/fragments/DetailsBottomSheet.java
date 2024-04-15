package com.ali.weather.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ali.weather.R;
import com.ali.weather.databinding.BottomDetailsLayoutBinding;
import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DetailsBottomSheet extends BottomSheetDialogFragment{

    public static final String TAG = "BottomSheet";

    BottomDetailsLayoutBinding binding;
    Context context;
    OnItemClick onItemClick;
    public static ForecastItem weatherItem;


    public static DetailsBottomSheet newInstance(ForecastItem item) {
        weatherItem = item;
        return new DetailsBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_details_layout,container,false);
        return binding.getRoot();
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.sunrise.setText(weatherItem.getSunRise());
        binding.sunset.setText(weatherItem.getSunSet());
        binding.uv.setText(weatherItem.getUv());
        binding.visibility.setText(weatherItem.getVisibility());
        binding.wind.setText(weatherItem.getWind());
        binding.windDir.setText(weatherItem.getWindDirection());
        binding.pressure.setText(weatherItem.getPressure());
        binding.humidity.setText(weatherItem.getHumidity());
        binding.pollution.setText(weatherItem.getPollution());

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onDismiss();
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnItemClick{
        void onDismiss();
    }

    public void setOnItemClickListener(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
}

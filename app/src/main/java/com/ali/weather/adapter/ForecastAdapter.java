package com.ali.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.weather.R;
import com.ali.weather.databinding.CustomDetailsLayoutBinding;
import com.ali.weather.databinding.CustomLocationItemBinding;
import com.ali.weather.model.ForecastItem;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;
import com.ali.weather.utilities.Utils;

import java.util.Arrays;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;
    List<ForecastItem> data;
    OnItemClick onItemClick;
    PrefManager prefManager;
    public ForecastAdapter(Context context, List<ForecastItem> data) {
        this.context = context;
        this.data = data;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_details_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomDetailsLayoutBinding binding;
        public MyViewHolder(@NonNull CustomDetailsLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(ForecastItem weatherItem){
            binding.day.setText(weatherItem.getDay());
            binding.weather.setText(weatherItem.getWeather());
            binding.sunrise.setText(weatherItem.getSunRise());
            binding.sunset.setText(weatherItem.getSunSet());
            binding.uv.setText(weatherItem.getUv());
            binding.visibility.setText(weatherItem.getVisibility());
            binding.wind.setText(weatherItem.getWind());
            binding.windDir.setText(weatherItem.getWindDirection());
            binding.pressure.setText(weatherItem.getPressure());
            binding.humidity.setText(weatherItem.getHumidity());
            binding.pollution.setText(weatherItem.getPollution());
            if (prefManager.getString(Constants.SELECTED_SCALE) != null){
                if (prefManager.getString(Constants.SELECTED_SCALE).equals("Celcius")){
                    binding.min.setText(weatherItem.getMinimumTemperature() != null ? Utils.getSplittedTemperature(weatherItem.getMinimumTemperature())[0] : "--");
                    binding.max.setText(weatherItem.getMaximumTemperature() != null ? Utils.getSplittedTemperature(weatherItem.getMaximumTemperature())[0] : "--");
                }else{
                    binding.min.setText(weatherItem.getMinimumTemperature() != null ? Utils.getSplittedTemperature(weatherItem.getMinimumTemperature())[1] : "--");
                    binding.max.setText(weatherItem.getMaximumTemperature() != null ? Utils.getSplittedTemperature(weatherItem.getMaximumTemperature())[1] : "--");
                }
            }else{
                binding.min.setText(weatherItem.getMinimumTemperature() != null ? Utils.getSplittedTemperature(weatherItem.getMinimumTemperature())[0] : "--");
                binding.max.setText(weatherItem.getMaximumTemperature() != null ? Utils.getSplittedTemperature(weatherItem.getMaximumTemperature())[0] : "--");
            }

            binding.weatherIcon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), Utils.getImage(weatherItem.getWeather()), null));
        }
    }

    public interface OnItemClick{
        void onClick(WeatherItem item);
    }

    public void setOnItemClickListener(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
}

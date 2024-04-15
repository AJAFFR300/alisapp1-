package com.ali.weather.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ali.weather.R;
import com.ali.weather.activities.WeatherActivity;
import com.ali.weather.adapter.FavouriteLocationAdapter;
import com.ali.weather.databinding.FragmentFavouriteBinding;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment {

    WeatherViewModel weatherViewModel;
    Activity activity;
    FragmentFavouriteBinding binding;
    FavouriteLocationAdapter adapter;
    List<WeatherItem> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite,container,false);
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
        data = new ArrayList<>();
        adapter = new FavouriteLocationAdapter(activity,data);
        adapter.setOnItemClickListener(new FavouriteLocationAdapter.OnItemClick() {
            @Override
            public void onClick(WeatherItem item) {
                startActivity(new Intent(activity, WeatherActivity.class).putExtra("data",item));
            }
        });
        binding.recycler.setLayoutManager(new GridLayoutManager(activity,2));
        binding.recycler.setAdapter(adapter);
        weatherViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity.getApplication()).create(WeatherViewModel.class);
    }

    public void setListeners(){
        weatherViewModel.getAllWeatherInfo().observe(getViewLifecycleOwner(), new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(List<WeatherItem> weatherItems) {
                data.clear();
                data.addAll(weatherItems);
                adapter.notifyDataSetChanged();
            }
        });
    }
}

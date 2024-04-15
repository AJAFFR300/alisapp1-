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
import com.ali.weather.databinding.CustomLocationItemBinding;
import com.ali.weather.fragments.DetailsBottomSheet;
import com.ali.weather.model.WeatherItem;
import com.ali.weather.utilities.Constants;
import com.ali.weather.utilities.PrefManager;
import com.ali.weather.utilities.Utils;

import java.util.Arrays;
import java.util.List;

public class FavouriteLocationAdapter extends RecyclerView.Adapter<FavouriteLocationAdapter.MyViewHolder> {

    Context context;
    List<WeatherItem> data;
    OnItemClick onItemClick;
    List<String> namelist;
    PrefManager prefManager;
    public FavouriteLocationAdapter(Context context, List<WeatherItem> data) {
        this.context = context;
        this.data = data;
        namelist = Arrays.asList(Constants.IDS);
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_location_item,parent,false));
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

        CustomLocationItemBinding binding;
        public MyViewHolder(@NonNull CustomLocationItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(WeatherItem item){
            if (prefManager.getString(Constants.SELECTED_SCALE) != null){
                if (prefManager.getString(Constants.SELECTED_SCALE).equals("Celcius")){
                    binding.setIsCelsius(true);
                }else{
                    binding.setIsCelsius(false);;
                }
            }else{
                binding.setIsCelsius(true);
            }
            binding.setWeather(item);
            binding.setLocation(Constants.NAMES[namelist.indexOf(item.id)]);
            binding.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), Utils.getImageFavourites(item.getWeather()), null));
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onClick(item);
                }
            });
        }
    }

    public interface OnItemClick{
        void onClick(WeatherItem item);
    }

    public void setOnItemClickListener(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }
}

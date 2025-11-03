package com.example.myweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FavoriteLocationAdapter extends RecyclerView.Adapter<FavoriteLocationAdapter.ViewHolder> {
    private List<FavoriteLocation> favoriteList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FavoriteLocation location);
        void onRemoveClick(FavoriteLocation location);
    }

    public FavoriteLocationAdapter(List<FavoriteLocation> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteLocation location = favoriteList.get(position);
        
        holder.tvCityName.setText(location.getCityName());
        
        if (location.getCurrentTemp() != null && !location.getCurrentTemp().isEmpty()) {
            holder.tvTemperature.setText(location.getCurrentTemp());
        } else {
            holder.tvTemperature.setText("--°");
        }
        
        if (location.getWeatherDescription() != null && !location.getWeatherDescription().isEmpty()) {
            holder.tvDescription.setText(location.getWeatherDescription());
        } else {
            holder.tvDescription.setText("Không có dữ liệu");
        }

        // Load weather icon
        if (location.getIconCode() != null && !location.getIconCode().isEmpty()) {
            String iconUrl = "https://openweathermap.org/img/wn/" + location.getIconCode() + "@2x.png";
            Glide.with(holder.itemView.getContext())
                    .load(iconUrl)
                    .into(holder.ivWeatherIcon);
        }

        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(location);
            }
        });

        holder.ivRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName, tvTemperature, tvDescription;
        ImageView ivWeatherIcon, ivRemove;

        ViewHolder(View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
            ivRemove = itemView.findViewById(R.id.ivRemove);
        }
    }
}
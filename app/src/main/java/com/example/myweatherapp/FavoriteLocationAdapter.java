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

        // ===== ✅ SET BACKGROUND THEO THỜI TIẾT =====
        setWeatherBackground(holder, location);

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
    
    private void setWeatherBackground(ViewHolder holder, FavoriteLocation location) {
        String iconCode = location.getIconCode();
        String description = location.getWeatherDescription();
        
        // Lấy tên file GIF background
        String backgroundImageName = WeatherBackgroundManager.getBackgroundImageName(description, iconCode);
        int placeholderDrawable = WeatherBackgroundManager.getPlaceholderDrawable(description, iconCode);
        
        android.util.Log.d("FavoriteBackground", "Setting background for " + location.getCityName() + 
                          ": " + backgroundImageName + ", icon: " + iconCode);
        
        // Set placeholder trước
        holder.ivItemBackground.setImageResource(placeholderDrawable);
        
        // Thử load GIF background với animation
        try {
            Glide.with(holder.itemView.getContext())
                .asGif()
                .load("file:///android_asset/" + backgroundImageName)
                .placeholder(placeholderDrawable)
                .error(placeholderDrawable)
                .fallback(placeholderDrawable)
                .listener(new com.bumptech.glide.request.RequestListener<com.bumptech.glide.load.resource.gif.GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(com.bumptech.glide.load.engine.GlideException e, Object model, 
                                              com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.gif.GifDrawable> target, 
                                              boolean isFirstResource) {
                        android.util.Log.d("FavoriteBackground", "GIF load failed for " + location.getCityName() + 
                                          ", using placeholder");
                        return false;
                    }
                    
                    @Override
                    public boolean onResourceReady(com.bumptech.glide.load.resource.gif.GifDrawable resource, Object model, 
                                                 com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.gif.GifDrawable> target, 
                                                 com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        android.util.Log.d("FavoriteBackground", "GIF loaded successfully for " + location.getCityName());
                        
                        // Thêm animation fade in khi load thành công
                        android.view.animation.Animation fadeIn = android.view.animation.AnimationUtils
                                .loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
                        holder.ivItemBackground.startAnimation(fadeIn);
                        return false;
                    }
                })
                .into(holder.ivItemBackground);
        } catch (Exception e) {
            // Fallback sử dụng placeholder drawable
            android.util.Log.e("FavoriteBackground", "Exception loading background for " + 
                              location.getCityName() + ": " + e.getMessage());
            holder.ivItemBackground.setImageResource(placeholderDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName, tvTemperature, tvDescription;
        ImageView ivWeatherIcon, ivRemove, ivItemBackground;

        ViewHolder(View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivWeatherIcon = itemView.findViewById(R.id.ivWeatherIcon);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            ivItemBackground = itemView.findViewById(R.id.ivItemBackground);
        }
    }
}
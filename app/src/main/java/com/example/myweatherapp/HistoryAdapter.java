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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryWeather> historyList;

    public HistoryAdapter(List<HistoryWeather> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryWeather history = historyList.get(position);

        holder.tvDate.setText(history.getDate());
        holder.tvDayOfWeek.setText(history.getDayOfWeek());
        holder.tvDescription.setText(history.getDescription());
        holder.tvTemp.setText(history.getTemperature());
        holder.tvHighLow.setText(history.getTempHigh() + " " + history.getTempLow());

        // Load icon từ OpenWeatherMap
        String iconUrl = "https://openweathermap.org/img/wn/" + history.getIcon() + "@2x.png";
        Glide.with(holder.itemView.getContext())
                .load(iconUrl)
                .into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvDate, tvDayOfWeek, tvDescription, tvTemp, tvHighLow;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivHistoryIcon);
            tvDate = itemView.findViewById(R.id.tvHistoryDate);
            tvDayOfWeek = itemView.findViewById(R.id.tvHistoryDayOfWeek);
            tvDescription = itemView.findViewById(R.id.tvHistoryDescription);
            tvTemp = itemView.findViewById(R.id.tvHistoryTemp);
            tvHighLow = itemView.findViewById(R.id.tvHistoryHighLow);
        }
    }
}
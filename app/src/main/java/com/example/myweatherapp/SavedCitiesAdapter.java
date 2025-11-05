package com.example.myweatherapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;

public class SavedCitiesAdapter extends RecyclerView.Adapter<SavedCitiesAdapter.CityViewHolder> {

    private List<String> cityList;
    private OnItemClickListener listener; // Cái này của em đã có

    // --- CODE MỚI ĐỂ XÓA ---
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
    // -------------------------

    // Interface cũ của em (giữ nguyên)
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor (giữ nguyên)
    public SavedCitiesAdapter(List<String> cityList) {
        this.cityList = cityList;
    }

    // --- 1. SỬA LẠI CITYVIEWHOLDER ---
    public class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvSavedCityName;
        ImageView ivDeleteCity; // <-- Thêm biến cho nút xóa

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ
            tvSavedCityName = itemView.findViewById(R.id.tvSavedCityName);
            ivDeleteCity = itemView.findViewById(R.id.ivDeleteCity); // <-- Ánh xạ nút xóa

            // 1. Bắt sự kiện bấm vào CẢ HÀNG (để chọn)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // 2. Bắt sự kiện bấm vào NÚT XÓA
            ivDeleteCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    // onCreateViewHolder (giữ nguyên)
    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_city_item, parent, false);
        return new CityViewHolder(view);
    }

    // --- 2. SỬA LẠI ONBINDVIEWHOLDER ---
    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        String cityName = cityList.get(position);
        holder.tvSavedCityName.setText(cityName);

        // *** XÓA PHẦN NÀY ĐI ***
        // Vì em đã chuyển listener vào trong ViewHolder rồi
        // (code cũ của em: holder.itemView.setOnClickListener(...))
    }

    // getItemCount (giữ nguyên)
    @Override
    public int getItemCount() {
        return cityList.size();
    }
}
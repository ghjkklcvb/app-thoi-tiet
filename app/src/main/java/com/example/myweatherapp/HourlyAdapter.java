package com.example.myweatherapp;


// Chúng ta cần import (sử dụng) rất nhiều "phụ tùng" của Android
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Lớp Adapter này phải "kế thừa" (extends) từ lớp RecyclerView.Adapter
public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {

    // Đây là danh sách dữ liệu (sử dụng "Khuôn Dữ Liệu" ta vừa tạo)
    private List<HourlyForecast> forecastList;

    // Constructor (Hàm khởi tạo) của Adapter
    // Nó nhận danh sách dữ liệu từ bên ngoài (từ MainActivity)
    public HourlyAdapter(List<HourlyForecast> forecastList) {
        this.forecastList = forecastList;
    }

    // -- BƯỚC QUAN TRỌNG 1: TẠO "GIÁ ĐỠ" (ViewHolder) --
    // Lớp "ViewHolder" này đại diện cho 1 "khuôn" (hourly_forecast_item.xml)
    // Nó giữ các View (TextView, ImageView) bên trong file XML đó
    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHourTime;
        ImageView ivHourIcon;
        TextView tvHourTemp;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            // "Ánh xạ" (tìm) các View từ file XML bằng ID của chúng
            tvHourTime = itemView.findViewById(R.id.tvHourTime);
            ivHourIcon = itemView.findViewById(R.id.ivHourIcon);
            tvHourTemp = itemView.findViewById(R.id.tvHourTemp);
        }
    }

    // -- BƯỚC QUAN TRỌNG 2: PHƯƠNG THỨC "ĐÚC KHUÔN" --
    // Phương thức này được gọi khi RecyclerView cần tạo một "giá đỡ" (ViewHolder) mới
    // Nó sẽ "thổi phồng" (inflate) file XML của chúng ta lên
    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lấy file "khuôn" hourly_forecast_item.xml ra
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_forecast_item, parent, false);

        // Trả về một "giá đỡ" mới chứa file XML đó
        return new HourlyViewHolder(view);
    }

    // -- BƯỚC QUAN TRỌNG 3: PHƯƠNG THỨC "ĐỔ DỮ LIỆU VÀO KHUÔN" --
    // Phương thức này được gọi để hiển thị dữ liệu tại một vị trí (position) cụ thể
    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        // 1. Lấy dữ liệu từ danh sách tại vị trí (position)
        HourlyForecast currentItem = forecastList.get(position);

        // 2. "Đổ" dữ liệu vào các View (TextView) trong "giá đỡ"
        holder.tvHourTime.setText(currentItem.time);
        holder.tvHourTemp.setText(currentItem.temperature);

        // Lấy mã icon (ví dụ "01d") từ dữ liệu
        String iconCode = currentItem.icon;
        // Gọi hàm "dịch" icon (chúng ta sẽ viết ngay sau đây)
        holder.ivHourIcon.setImageResource(getIconResource(iconCode));
    }

    // -- BƯỚC QUAN TRỌNG 4: PHƯƠNG THỨC "ĐẾM" --
    // Phương thức này trả về tổng số mục trong danh sách
    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    // HÀM MỚI: "Dịch" mã icon (string) sang ID hình ảnh (int)
    private int getIconResource(String iconCode) {
        switch (iconCode) {
            case "01d": // Nắng
                return R.drawable.ic_sunny;
            case "01n": // Trăng
                return R.drawable.ic_moon;
            case "02d": // Nắng + Mây
            case "03d": // Mây
            case "04d": // Mây dày
                return R.drawable.ic_cloudy;
            case "02n": // Trăng + Mây
            case "03n": // Mây
            case "04n": // Mây dày
                return R.drawable.ic_cloudy; // (Tạm dùng chung icon mây cho ban đêm)
            case "09d": // Mưa rào
            case "10d": // Mưa
                return R.drawable.ic_rainy;
            case "09n":
            case "10n":
                return R.drawable.ic_rainy;
            case "11d": // Sấm sét
            case "11n":
                return R.drawable.ic_thunderstorm;
            // (Chúng ta bỏ qua các trường hợp tuyết, sương mù...)
            default: // Nếu không khớp
                return R.drawable.ic_cloudy;
        }
    }
}
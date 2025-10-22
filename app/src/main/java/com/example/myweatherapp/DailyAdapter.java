package com.example.myweatherapp;


// Import các "phụ tùng" cần thiết
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Kế thừa từ RecyclerView.Adapter
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {

    // Danh sách dữ liệu (dùng "Khuôn Dữ Liệu" DailyForecast)
    private List<DailyForecast> forecastList;

    // Constructor (Hàm khởi tạo)
    public DailyAdapter(List<DailyForecast> forecastList) {
        this.forecastList = forecastList;
    }

    // -- BƯỚC 1: TẠO "GIÁ ĐỠ" (ViewHolder) --
    // Lớp "ViewHolder" này đại diện cho "khuôn" daily_forecast_item.xml
    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayOfWeek;
        ImageView ivDayIcon;
        TextView tvDayTempHigh;
        TextView tvDayTempLow;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            // "Ánh xạ" (tìm) các View từ file XML bằng ID
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            ivDayIcon = itemView.findViewById(R.id.ivDayIcon);
            tvDayTempHigh = itemView.findViewById(R.id.tvDayTempHigh);
            tvDayTempLow = itemView.findViewById(R.id.tvDayTempLow);
        }
    }

    // -- BƯỚC 2: PHƯƠNG THỨC "ĐÚC KHUÔN" --
    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lấy file "khuôn" daily_forecast_item.xml ra
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_forecast_item, parent, false);

        // Trả về một "giá đỡ" mới
        return new DailyViewHolder(view);
    }

    // -- BƯỚC 3: PHƯƠNG THỨC "ĐỔ DỮ LIỆU VÀO KHUÔN" --
    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        // 1. Lấy dữ liệu từ danh sách tại vị trí (position)
        DailyForecast currentItem = forecastList.get(position);

        // 2. "Đổ" dữ liệu vào các View
        holder.tvDayOfWeek.setText(currentItem.day);
        holder.tvDayTempHigh.setText(currentItem.tempHigh);
        holder.tvDayTempLow.setText(currentItem.tempLow);

        // --- CODE MỚI ---
        String iconCode = currentItem.icon;
        holder.ivDayIcon.setImageResource(getIconResource(iconCode));
    }

    // -- BƯỚC 4: PHƯƠNG THỨC "ĐẾM" --
    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    // HÀM MỚI: "Dịch" mã icon (string) sang ID hình ảnh (int)
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
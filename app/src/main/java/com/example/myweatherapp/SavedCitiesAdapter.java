package com.example.myweatherapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SavedCitiesAdapter extends RecyclerView.Adapter<SavedCitiesAdapter.CityViewHolder> {

    // Dữ liệu là một danh sách các chuỗi (tên thành phố)
    private List<String> cityList;

    // 1. "Bản hợp đồng" (Interface)
    public interface OnItemClickListener {
        void onItemClick(int position); // Báo cho Activity biết vị trí (position) đã được bấm
    }

    private OnItemClickListener listener; // Biến để "giữ" hợp đồng

    // 2. Hàm để Activity "ký hợp đồng"
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor
    public SavedCitiesAdapter(List<String> cityList) {
        this.cityList = cityList;
    }

    // 1. Tạo "Giá đỡ" (ViewHolder)
    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvSavedCityName;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ TextView từ "khuôn" saved_city_item.xml
            tvSavedCityName = itemView.findViewById(R.id.tvSavedCityName);
        }
    }

    // 2. "Đúc khuôn" (inflate layout)
    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_city_item, parent, false);
        return new CityViewHolder(view);
    }

    // 3. "Đổ dữ liệu" vào khuôn
    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        // Lấy tên thành phố ở vị trí "position"
        String cityName = cityList.get(position);

        // Gán tên vào TextView
        holder.tvSavedCityName.setText(cityName);

        // 3. Bắt sự kiện bấm vào hàng
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        // Gọi "hợp đồng", gửi vị trí (position) ra ngoài
                        listener.onItemClick(adapterPosition);
                    }
                }
            }
        });
    }

    // 4. "Đếm" số lượng
    @Override
    public int getItemCount() {
        return cityList.size();
    }
}
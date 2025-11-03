package com.example.myweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements FavoriteLocationAdapter.OnItemClickListener {

    private RecyclerView rvFavorites;
    private FavoriteLocationAdapter favoriteAdapter;
    private List<FavoriteLocation> favoriteList;

    private TextView tvTitle;
    private ImageView ivBack;

    private FavoriteLocationManager favoriteManager;
    private final String API_KEY = "f79c108bad93ab54be45c05a5c21a541";
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Ánh xạ views
        rvFavorites = findViewById(R.id.rvHistory);
        tvTitle = findViewById(R.id.tvHistoryCityName);
        ivBack = findViewById(R.id.ivBack);

        // Đổi title
        tvTitle.setText("Địa điểm yêu thích");

        // Nút quay lại
        ivBack.setOnClickListener(v -> finish());

        // Khởi tạo manager và RecyclerView
        favoriteManager = new FavoriteLocationManager(this);
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteLocationAdapter(favoriteList);
        favoriteAdapter.setOnItemClickListener(this);
        
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        rvFavorites.setAdapter(favoriteAdapter);

        // Load danh sách yêu thích
        loadFavorites();
    }

    private void loadFavorites() {
        favoriteList.clear();
        favoriteList.addAll(favoriteManager.getFavorites());
        
        View emptyStateLayout = findViewById(R.id.emptyStateLayout);
        
        if (favoriteList.isEmpty()) {
            rvFavorites.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
            // Cập nhật thông tin thời tiết cho các địa điểm yêu thích
            updateWeatherForFavorites();
        }
        
        favoriteAdapter.notifyDataSetChanged();
    }

    private void updateWeatherForFavorites() {
        for (int i = 0; i < favoriteList.size(); i++) {
            FavoriteLocation location = favoriteList.get(i);
            fetchWeatherForLocation(location, i);
        }
    }

    private void fetchWeatherForLocation(FavoriteLocation location, int position) {
        String url = BASE_URL + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude()
                + "&appid=" + API_KEY + "&units=metric&lang=vi";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject root = new JSONObject(response);
                        JSONObject main = root.getJSONObject("main");
                        double temp = main.getDouble("temp");

                        JSONArray weatherArray = root.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");

                        DecimalFormat df = new DecimalFormat("#");
                        String tempFormatted = df.format(temp) + "°";

                        // Viết hoa chữ cái đầu
                        description = description.substring(0, 1).toUpperCase() + description.substring(1);

                        // Cập nhật thông tin
                        location.setCurrentTemp(tempFormatted);
                        location.setWeatherDescription(description);
                        location.setIconCode(icon);

                        // Cập nhật adapter
                        favoriteAdapter.notifyItemChanged(position);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Không làm gì nếu lỗi, giữ nguyên dữ liệu cũ
                });

        requestQueue.add(stringRequest);
    }

    // Implement FavoriteLocationAdapter.OnItemClickListener
    @Override
    public void onItemClick(FavoriteLocation location) {
        // Chọn địa điểm yêu thích và quay về MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SELECTED_CITY", location.getCityName());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onRemoveClick(FavoriteLocation location) {
        // Xóa khỏi yêu thích
        favoriteManager.removeFavorite(location.getCityName());
        loadFavorites(); // Refresh danh sách
        Toast.makeText(this, "Đã xóa " + location.getCityName() + " khỏi yêu thích", 
                Toast.LENGTH_SHORT).show();
    }
}
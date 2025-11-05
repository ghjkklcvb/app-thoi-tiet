package com.example.myweatherapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView; // <-- Quan trọng! Dùng cái này

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences; // <-- Thêm "sổ tay"
import java.util.Set; // <-- Dùng để lưu danh sách
import java.util.HashSet; // <-- Dùng để lưu danh sách
import android.widget.TextView;

public class ManageCitiesActivity extends AppCompatActivity implements SavedCitiesAdapter.OnItemClickListener, SavedCitiesAdapter.OnDeleteClickListener {

    private ImageView ivClose; // <-- Khai báo biến

    private SearchView searchView;
    private RecyclerView rvSavedCities;
    private SavedCitiesAdapter savedCitiesAdapter;
    private List<String> cityList;

    // --- BIẾN MỚI ---
    // Tên "quyển sổ"
    public static final String PREFS_NAME = "WeatherAppPrefs";
    // Tên "trang" chúng ta sẽ viết (key)
    public static final String KEY_SAVED_CITIES = "SavedCities";
    // ----------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cities);

        // --- CODE MỚI ---
        // 1. Ánh xạ
        ivClose = findViewById(R.id.ivClose);

        // 2. Bắt sự kiện click
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi hàm finish() để "kết thúc" (đóng) Activity này
                finish();
            }
        });

        // 1. Ánh xạ
        searchView = findViewById(R.id.searchView);
        rvSavedCities = findViewById(R.id.rvSavedCities);

        // 2. Khởi tạo danh sách và "cỗ máy đúc"
        cityList = new ArrayList<>();
        savedCitiesAdapter = new SavedCitiesAdapter(cityList);

        // 3. Lắp "cỗ máy" vào RecyclerView
        rvSavedCities.setAdapter(savedCitiesAdapter);

        savedCitiesAdapter.setOnItemClickListener(this); // "this" nghĩa là "Activity này"
        savedCitiesAdapter.setOnDeleteClickListener(this);

        loadCityList(); // Đọc danh sách vĩnh viễn từ "sổ tay"
        
        // 5. LẮNG NGHE THANH TÌM KIẾM
        setupSearchViewListener();



    }


    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate
    private void setupSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 1. Được gọi khi người dùng nhấn nút "Enter" (hoặc nút tìm)
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    String newCity = query;

                    // 1. (Nên làm) Kiểm tra xem thành phố đã có chưa
                    // Nếu chưa có thì mới thêm vào danh sách và lưu lại
                    if (!cityList.contains(newCity)) {
                        cityList.add(newCity);
                        // Báo adapter cập nhật (không cần dùng notifyDataSetChanged() lãng phí)
                        savedCitiesAdapter.notifyItemInserted(cityList.size() - 1);
                        saveCityList(); // Lưu danh sách mới
                    }

                    // 2. GỬI KẾT QUẢ VỀ MAINACTIVITY NGAY LẬP TỨC
                    // (Đây chính là logic copy từ hàm onItemClick của em)

                    // 2.1. Tạo "lá thư trả lời"
                    Intent resultIntent = new Intent();

                    // 2.2. Đính kèm "gói hàng" (tên thành phố MỚI)
                    resultIntent.putExtra("SELECTED_CITY", newCity);

                    // 2.3. "Dán tem" hợp lệ
                    setResult(RESULT_OK, resultIntent);

                    // 2.4. "Gửi thư" (đóng Activity này)
                    finish();
                }
                return true; // Báo rằng chúng ta đã xử lý
            }

            // 2. Được gọi mỗi khi người dùng GÕ TỪNG CHỮ
            @Override
            public boolean onQueryTextChange(String newText) {
                // Chúng ta không cần làm gì ở đây
                return false;
            }
        });
    }


    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate
    private void loadCityList() {
        // 1. Mở "quyển sổ"
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 2. Đọc "trang" SavedCities
        // SharedPreferences không lưu List, nhưng nó lưu "Set" (một tập hợp)
        Set<String> citySet = prefs.getStringSet(KEY_SAVED_CITIES, null);

        // 3. Xóa danh sách cũ (nếu có)
        cityList.clear();

        // 4. Nếu "trang" này có dữ liệu (không phải lần đầu mở app)
        if (citySet != null) {
            cityList.addAll(citySet);
        }

        // 5. Nếu "trang" này trống (lần đầu tiên mở app)
        if (cityList.isEmpty()) {
            cityList.add("Hanoi"); // Thêm "Hanoi" làm mặc định
            saveCityList(); // Và lưu lại vào "sổ" ngay
        }

        // 6. Báo Adapter cập nhật
        savedCitiesAdapter.notifyDataSetChanged();
    }


    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate
    private void saveCityList() {
        // 1. Mở "quyển sổ" và lấy "bút" (Editor)
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 2. Chuyển List của chúng ta sang Set (để lưu)
        Set<String> citySet = new HashSet<>(cityList);

        // 3. Viết "trang" mới
        editor.putStringSet(KEY_SAVED_CITIES, citySet);

        // 4. "Đóng sổ" (Lưu lại)
        editor.apply();
    }

    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate

    // Đây là hàm "hợp đồng" mà chúng ta phải thực thi
    @Override
    public void onItemClick(int position) {
        // 1. Lấy tên thành phố được bấm
        String selectedCity = cityList.get(position);

        // 2. Tạo một "lá thư trả lời" (Intent)
        Intent resultIntent = new Intent();

        // 3. Đính kèm "gói hàng" (tên thành phố) vào thư
        // "SELECTED_CITY" là cái nhãn, "selectedCity" là nội dung
        resultIntent.putExtra("SELECTED_CITY", selectedCity);

        // 4. "Dán tem" cho lá thư (báo là thư HỢP LỆ)
        setResult(RESULT_OK, resultIntent);

        // 5. "Gửi thư" (bằng cách đóng Activity này lại)
        finish();
    }



    // HÀM MỚI (để xóa)
    @Override
    public void onDeleteClick(int position) {
        // (Tùy chọn) Lấy tên để thông báo
        String deletedCity = cityList.get(position);

        // 1. Xóa thành phố khỏi danh sách
        cityList.remove(position);

        // 2. Báo cho Adapter biết (Rất quan trọng)
        savedCitiesAdapter.notifyItemRemoved(position);

        // 3. Cập nhật lại vị trí của các item còn lại
        savedCitiesAdapter.notifyItemRangeChanged(position, cityList.size());

        // 4. Lưu lại danh sách mới vào "sổ tay"
        saveCityList();

        // 5. (Tùy chọn) Thông báo cho người dùng
        android.widget.Toast.makeText(this, "Đã xóa " + deletedCity, android.widget.Toast.LENGTH_SHORT).show();
    }



}
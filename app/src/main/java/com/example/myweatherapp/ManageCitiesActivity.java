package com.example.myweatherapp;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View; // <-- Thêm import
import android.widget.ImageView; // <-- Thêm import

public class ManageCitiesActivity extends AppCompatActivity {

    private ImageView ivClose; // <-- Khai báo biến

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
        // --- KẾT THÚC CODE MỚI ---
    }
}
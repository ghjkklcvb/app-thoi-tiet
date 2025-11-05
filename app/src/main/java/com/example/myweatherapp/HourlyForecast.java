package com.example.myweatherapp;

public class HourlyForecast {

    String time;
    String temperature;
    String icon; // <-- BIẾN MỚI
    String description; // <-- THÊM WEATHER DESCRIPTION

    // Constructor (Hàm khởi tạo) đã được cập nhật
    public HourlyForecast(String time, String temperature, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon; // <-- GÁN BIẾN MỚI
        this.description = ""; // Default empty
    }

    // Constructor với description
    public HourlyForecast(String time, String temperature, String icon, String description) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.description = description;
    }
}
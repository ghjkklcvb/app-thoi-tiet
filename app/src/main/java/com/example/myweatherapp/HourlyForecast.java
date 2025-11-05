package com.example.myweatherapp;

public class HourlyForecast {

    String time;
    String temperature;
    String icon; // <-- BIẾN MỚI

    // Constructor (Hàm khởi tạo) đã được cập nhật
    public HourlyForecast(String time, String temperature, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon; // <-- GÁN BIẾN MỚI
    }
}
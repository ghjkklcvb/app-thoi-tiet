package com.example.myweatherapp;



public class DailyForecast {

    String day;
    String tempHigh;
    String tempLow;
    String icon; // <-- BIẾN MỚI

    // Constructor (Hàm khởi tạo) đã được cập nhật
    public DailyForecast(String day, String tempHigh, String tempLow, String icon) {
        this.day = day;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.icon = icon; // <-- GÁN BIẾN MỚI
    }
}
package com.example.myweatherapp;



public class DailyForecast {

    String day;
    String tempHigh;
    String tempLow;
    String icon; // <-- BIẾN MỚI
    String description; // <-- THÊM WEATHER DESCRIPTION
    int humidity;
    
    // Constructor (Hàm khởi tạo) đã được cập nhật
    public DailyForecast(String day, String tempHigh, String tempLow, String icon) {
        this.day = day;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.icon = icon; // <-- GÁN BIẾN MỚI
        this.description = ""; // Default empty
        this.humidity = 0;
    }
    
    public DailyForecast(String day, String tempHigh, String tempLow, String icon, int humidity) {
        this.day = day;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.icon = icon;
        this.description = ""; // Default empty
        this.humidity = humidity;
    }

    // Constructor với description
    public DailyForecast(String day, String tempHigh, String tempLow, String icon, String description, int humidity) {
        this.day = day;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
        this.icon = icon;
        this.description = description;
        this.humidity = humidity;
    }
}
package com.example.myweatherapp;

public class HistoryWeather {
    private String date;          // Ngày (ví dụ: "29/10/2025")
    private String dayOfWeek;     // Thứ (ví dụ: "Thứ Tư")
    private String temperature;   // Nhiệt độ (ví dụ: "27°")
    private String description;   // Mô tả (ví dụ: "Trời quang")
    private String icon;          // Mã icon (ví dụ: "01d")
    private String tempHigh;      // Nhiệt độ cao nhất
    private String tempLow;       // Nhiệt độ thấp nhất

    public HistoryWeather(String date, String dayOfWeek, String temperature,
                          String description, String icon, String tempHigh, String tempLow) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.temperature = temperature;
        this.description = description;
        this.icon = icon;
        this.tempHigh = tempHigh;
        this.tempLow = tempLow;
    }

    // Getters
    public String getDate() { return date; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getTemperature() { return temperature; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public String getTempHigh() { return tempHigh; }
    public String getTempLow() { return tempLow; }
}
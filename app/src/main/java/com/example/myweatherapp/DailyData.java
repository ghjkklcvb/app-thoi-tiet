package com.example.myweatherapp;

public class DailyData {
    private double tempMax;
    private double tempMin;
    private String description;
    private int count; // Để đếm số lần dữ liệu được tổng hợp (giúp tính trung bình)

    public DailyData(double tempMax, double tempMin, String description) {
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.description = description;
        this.count = 1;
    }

    // Getters
    public double getTempMax() {
        return tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public String getDescription() {
        return description;
    }

    // Setters (hoặc các phương thức để cập nhật dữ liệu)
    public void addData(double newTempMax, double newTempMin) {
        this.tempMax += newTempMax;
        this.tempMin += newTempMin;
        this.count++;
    }

    public double getAverageTempMax() {
        return this.tempMax / this.count;
    }

    public double getAverageTempMin() {
        return this.tempMin / this.count;
    }
}

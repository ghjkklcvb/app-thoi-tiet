package com.example.myweatherapp;

public class FavoriteLocation {
    private String cityName;
    private double latitude;
    private double longitude;
    private String currentTemp;
    private String weatherDescription;
    private String iconCode;

    public FavoriteLocation(String cityName, double latitude, double longitude) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public FavoriteLocation(String cityName, double latitude, double longitude, 
                          String currentTemp, String weatherDescription, String iconCode) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentTemp = currentTemp;
        this.weatherDescription = weatherDescription;
        this.iconCode = iconCode;
    }

    // Getters and Setters
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getCurrentTemp() { return currentTemp; }
    public void setCurrentTemp(String currentTemp) { this.currentTemp = currentTemp; }

    public String getWeatherDescription() { return weatherDescription; }
    public void setWeatherDescription(String weatherDescription) { this.weatherDescription = weatherDescription; }

    public String getIconCode() { return iconCode; }
    public void setIconCode(String iconCode) { this.iconCode = iconCode; }
}
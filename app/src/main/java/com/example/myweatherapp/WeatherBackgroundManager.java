package com.example.myweatherapp;

import android.content.Context;
import java.util.Calendar;

public class WeatherBackgroundManager {
    
    public static int getBackgroundResource(String weatherCondition, String iconCode) {
        boolean isNight = isNightTime(iconCode);
        
        // Xác định loại thời tiết từ icon code hoặc weather condition
        if (iconCode != null) {
            if (iconCode.contains("01")) { // Clear sky
                return isNight ? R.drawable.bg_sunny_night : R.drawable.bg_sunny_day;
            } else if (iconCode.contains("02") || iconCode.contains("03") || iconCode.contains("04")) { // Clouds
                return isNight ? R.drawable.bg_cloudy_night : R.drawable.bg_cloudy_day;
            } else if (iconCode.contains("09") || iconCode.contains("10")) { // Rain
                return isNight ? R.drawable.bg_rainy_night : R.drawable.bg_rainy_day;
            } else if (iconCode.contains("11")) { // Thunderstorm
                return isNight ? R.drawable.bg_thunderstorm_night : R.drawable.bg_thunderstorm_day;
            } else if (iconCode.contains("13")) { // Snow - sử dụng cloudy background
                return isNight ? R.drawable.bg_cloudy_night : R.drawable.bg_cloudy_day;
            } else if (iconCode.contains("50")) { // Mist/Fog - sử dụng cloudy background
                return isNight ? R.drawable.bg_cloudy_night : R.drawable.bg_cloudy_day;
            }
        }
        
        // Fallback dựa trên weather condition
        if (weatherCondition != null) {
            String condition = weatherCondition.toLowerCase();
            if (condition.contains("clear") || condition.contains("sunny")) {
                return isNight ? R.drawable.bg_sunny_night : R.drawable.bg_sunny_day;
            } else if (condition.contains("cloud") || condition.contains("overcast")) {
                return isNight ? R.drawable.bg_cloudy_night : R.drawable.bg_cloudy_day;
            } else if (condition.contains("rain") || condition.contains("drizzle")) {
                return isNight ? R.drawable.bg_rainy_night : R.drawable.bg_rainy_day;
            } else if (condition.contains("thunder") || condition.contains("storm")) {
                return isNight ? R.drawable.bg_thunderstorm_night : R.drawable.bg_thunderstorm_day;
            } else if (condition.contains("snow") || condition.contains("mist") || condition.contains("fog")) {
                return isNight ? R.drawable.bg_cloudy_night : R.drawable.bg_cloudy_day;
            }
        }
        
        // Default background
        return isNight ? R.drawable.bg_cloudy_night : R.drawable.bg_cloudy_day;
    }
    
    private static boolean isNightTime(String iconCode) {
        if (iconCode != null && iconCode.endsWith("n")) {
            return true;
        }
        
        // Fallback: kiểm tra thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour >= 18; // 6 PM to 6 AM is considered night
    }
    
    public static boolean isCurrentlyNight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour >= 18;
    }
}
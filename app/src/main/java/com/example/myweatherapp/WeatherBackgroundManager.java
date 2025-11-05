package com.example.myweatherapp;

import android.content.Context;
import java.util.Calendar;

public class WeatherBackgroundManager {
    
    public static String getBackgroundImageName(String weatherCondition, String iconCode) {
        // Xác định loại thời tiết từ icon code đầy đủ theo OpenWeatherMap API
        if (iconCode != null) {
            switch (iconCode) {
                // Group 2xx: Thunderstorm - Sấm sét
                case "11d": // Thunderstorm day
                    return "thunderstorm_day.gif";
                case "11n": // Thunderstorm night
                    return "thunderstorm_night.gif";
                
                // Group 3xx: Drizzle - Mưa phùn
                case "09d": // Shower rain day
                case "10d": // Rain day
                    return "drizzle_day.gif";
                case "09n": // Shower rain night
                case "10n": // Rain night
                    return "drizzle_night.gif";
                
                // Group 5xx: Rain - Mưa
                // Đã xử lý ở trên với 09d, 10d
                
                // Group 6xx: Snow - Tuyết
                case "13d": // Snow day
                    return "snow_day.gif";
                case "13n": // Snow night
                    return "snow_night.gif";
                
                // Group 7xx: Atmosphere - Khí quyển (sương mù, khói, bụi...)
                case "50d": // Mist day
                    return "mist_day.gif";
                case "50n": // Mist night
                    return "mist_night.gif";
                
                // Group 800: Clear - Trời quang
                case "01d": // Clear sky day
                    return "clear_day.gif";
                case "01n": // Clear sky night
                    return "clear_night.gif";
                
                // Group 80x: Clouds - Mây
                case "02d": // Few clouds day (11-25%)
                    return "few_clouds_day.gif";
                case "02n": // Few clouds night
                    return "few_clouds_night.gif";
                    
                case "03d": // Scattered clouds day (25-50%)
                    return "scattered_clouds_day.gif";
                case "03n": // Scattered clouds night
                    return "scattered_clouds_night.gif";
                    
                case "04d": // Broken clouds day (51-84%)
                case "04n": // Broken clouds night
                    return iconCode.endsWith("n") ? "broken_clouds_night.gif" : "broken_clouds_day.gif";
            }
        }
        
        // Fallback dựa trên weather condition (tiếng Việt và tiếng Anh)
        if (weatherCondition != null) {
            String condition = weatherCondition.toLowerCase();
            boolean isNight = isNightTime(iconCode);
            
            // Thunderstorm
            if (condition.contains("sấm") || condition.contains("sét") || condition.contains("thunder") || 
                condition.contains("storm") || condition.contains("giông")) {
                return isNight ? "thunderstorm_night.gif" : "thunderstorm_day.gif";
            }
            // Drizzle/Light Rain
            else if (condition.contains("phùn") || condition.contains("drizzle") || condition.contains("rào")) {
                return isNight ? "drizzle_night.gif" : "drizzle_day.gif";
            }
            // Heavy Rain
            else if (condition.contains("mưa") || condition.contains("rain")) {
                return isNight ? "drizzle_night.gif" : "drizzle_day.gif";
            }
            // Snow
            else if (condition.contains("tuyết") || condition.contains("snow")) {
                return isNight ? "snow_night.gif" : "snow_day.gif";
            }
            // Mist/Fog
            else if (condition.contains("sương") || condition.contains("mist") || condition.contains("fog") || 
                     condition.contains("khói") || condition.contains("smoke") || condition.contains("haze")) {
                return isNight ? "mist_night.gif" : "mist_day.gif";
            }
            // Clear
            else if (condition.contains("quang") || condition.contains("nắng") || condition.contains("clear") || 
                     condition.contains("sunny")) {
                return isNight ? "clear_night.gif" : "clear_day.gif";
            }
            // Few clouds
            else if (condition.contains("ít mây") || condition.contains("few clouds")) {
                return isNight ? "few_clouds_night.gif" : "few_clouds_day.gif";
            }
            // Scattered clouds
            else if (condition.contains("mây rải rác") || condition.contains("scattered")) {
                return isNight ? "scattered_clouds_night.gif" : "scattered_clouds_day.gif";
            }
            // Broken/Overcast clouds
            else if (condition.contains("mây") || condition.contains("cloud") || condition.contains("overcast")) {
                return isNight ? "broken_clouds_night.gif" : "broken_clouds_day.gif";
            }
        }
        
        // Default background
        boolean isNight = isNightTime(iconCode);
        return isNight ? "clear_night.gif" : "clear_day.gif";
    }
    
    // Method để lấy placeholder drawable khi không có GIF
    public static int getPlaceholderDrawable(String weatherCondition, String iconCode) {
        if (iconCode != null) {
            switch (iconCode) {
                // Thunderstorm
                case "11d":
                    return R.drawable.placeholder_thunderstorm_day;
                case "11n":
                    return R.drawable.placeholder_thunderstorm_night;
                
                // Drizzle/Rain
                case "09d": case "10d":
                    return R.drawable.placeholder_rainy_day;
                case "09n": case "10n":
                    return R.drawable.placeholder_rainy_night;
                
                // Snow
                case "13d":
                    return R.drawable.placeholder_snow_day;
                case "13n":
                    return R.drawable.placeholder_snow_night;
                
                // Mist/Atmosphere
                case "50d":
                    return R.drawable.placeholder_mist_day;
                case "50n":
                    return R.drawable.placeholder_mist_night;
                
                // Clear
                case "01d":
                    return R.drawable.placeholder_sunny_day;
                case "01n":
                    return R.drawable.placeholder_clear_night;
                
                // Few clouds
                case "02d":
                    return R.drawable.placeholder_few_clouds_day;
                case "02n":
                    return R.drawable.placeholder_few_clouds_night;
                
                // Scattered clouds
                case "03d":
                    return R.drawable.placeholder_scattered_clouds_day;
                case "03n":
                    return R.drawable.placeholder_scattered_clouds_night;
                
                // Broken clouds
                case "04d":
                    return R.drawable.placeholder_cloudy_day;
                case "04n":
                    return R.drawable.placeholder_broken_clouds_night;
            }
        }
        
        // Fallback dựa trên weather condition
        if (weatherCondition != null) {
            String condition = weatherCondition.toLowerCase();
            boolean isNight = isNightTime(iconCode);
            
            if (condition.contains("thunder") || condition.contains("storm")) {
                return isNight ? R.drawable.placeholder_thunderstorm_night : R.drawable.placeholder_thunderstorm_day;
            } else if (condition.contains("rain") || condition.contains("drizzle")) {
                return isNight ? R.drawable.placeholder_rainy_night : R.drawable.placeholder_rainy_day;
            } else if (condition.contains("snow")) {
                return isNight ? R.drawable.placeholder_snow_night : R.drawable.placeholder_snow_day;
            } else if (condition.contains("mist") || condition.contains("fog")) {
                return isNight ? R.drawable.placeholder_mist_night : R.drawable.placeholder_mist_day;
            } else if (condition.contains("clear")) {
                return isNight ? R.drawable.placeholder_clear_night : R.drawable.placeholder_sunny_day;
            } else if (condition.contains("cloud")) {
                return isNight ? R.drawable.placeholder_broken_clouds_night : R.drawable.placeholder_cloudy_day;
            }
        }
        
        // Default
        boolean isNight = isNightTime(iconCode);
        return isNight ? R.drawable.placeholder_clear_night : R.drawable.placeholder_sunny_day;
    }
    
    // Giữ lại method cũ để tương thích
    public static int getBackgroundResource(String weatherCondition, String iconCode) {
        return getPlaceholderDrawable(weatherCondition, iconCode);
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
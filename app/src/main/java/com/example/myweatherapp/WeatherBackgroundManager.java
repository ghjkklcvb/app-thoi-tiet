package com.example.myweatherapp;

import android.content.Context;
import java.util.Calendar;

public class WeatherBackgroundManager {

    public static String getBackgroundImageName(String weatherCondition, String iconCode) {

        
        // Xác định loại thời tiết từ icon code đầy đủ theo OpenWeatherMap API
        if (iconCode != null) {
            switch (iconCode) {
                // --- Giông sét ---
                case "11d":
                    return "thunderstorm_day.gif";
                case "11n":
                    return "thunderstorm_night.gif";

                // --- Mưa phùn / Mưa ---
                case "09d":
                case "10d":
                    return "drizzle_day.gif";
                case "09n":
                case "10n":
                    return "drizzle_night.gif";

                // --- Tuyết ---
                case "13d":
                    return "snow_day.gif";
                case "13n":
                    return "snow_night.gif";

                // --- Sương mù / khói ---
                case "50d":
                    return "mist_day.gif";
                case "50n":
                    return "mist_night.gif";

                // --- Trời quang ---
                case "01d":
                    return "clear_day.gif";
                case "01n":
                    return "clear_night.gif";

                // --- Ít mây ---
                case "02d":
                    return "few_clouds_day.gif";
                case "02n":
                    return "few_clouds_night.gif";

                // --- Mây cụm (rải rác) ---
                case "03d":
                    return "scattered_clouds_day.gif";
                case "03n":
                    return "scattered_clouds_night.gif";

                // --- Mây đen / u ám ---
                case "04d":
                    return "broken_clouds_day.gif";
                case "04n":
                    return "broken_clouds_night.gif";

                // --- Mây cụm (tùy chỉnh) ---
                case "05d":
                    return "clustered_clouds_day.gif";
                case "05n":
                    return "clustered_clouds_night.gif";
            }
        }

        // --- Fallback khi iconCode null ---
        if (weatherCondition != null) {
            String condition = weatherCondition.toLowerCase();
            boolean isNight = isNightTime(iconCode);

            // Giông sét
            if (condition.contains("sấm") || condition.contains("sét") || condition.contains("thunder") || condition.contains("giông")) {
                return isNight ? "thunderstorm_night.gif" : "thunderstorm_day.gif";
            }

            // Mưa phùn, mưa nhỏ
            else if (condition.contains("phùn") || condition.contains("drizzle") || condition.contains("rào")) {
                return isNight ? "drizzle_night.gif" : "drizzle_day.gif";
            }

            // Mưa lớn
            else if (condition.contains("mưa") || condition.contains("rain")) {
                return isNight ? "drizzle_night.gif" : "drizzle_day.gif";
            }

            // Tuyết
            else if (condition.contains("tuyết") || condition.contains("snow")) {
                return isNight ? "snow_night.gif" : "snow_day.gif";
            }

            // Sương mù, khói, bụi
            else if (condition.contains("sương") || condition.contains("mist") || condition.contains("fog")
                    || condition.contains("khói") || condition.contains("smoke") || condition.contains("haze")
                    || condition.contains("bụi") || condition.contains("dust")) {
                return isNight ? "mist_night.gif" : "mist_day.gif";
            }

            // Trời quang
            else if (condition.contains("nắng") || condition.contains("clear") || condition.contains("quang")) {
                return isNight ? "clear_night.gif" : "clear_day.gif";
            }

            // --- PHÂN BIỆT MÂY ---
            // Ít mây
            else if (condition.contains("ít mây") || condition.contains("few")) {
                return isNight ? "few_clouds_night.gif" : "few_clouds_day.gif";
            }
            // Mây cụm riêng biệt
            else if (condition.contains("mây cụm") || condition.contains("clustered")) {
                return isNight ? "clustered_clouds_night.gif" : "clustered_clouds_day.gif";
            }
            // Mây rải rác (scattered)
            else if (condition.contains("rải rác") || condition.contains("scattered")) {
                return isNight ? "scattered_clouds_night.gif" : "scattered_clouds_day.gif";
            }
            // Mây đen / u ám
            else if (condition.contains("u ám") || condition.contains("mây dày") || condition.contains("overcast") || condition.contains("broken")) {
                return isNight ? "broken_clouds_night.gif" : "broken_clouds_day.gif";
            }
            // Nếu chỉ có từ "mây" chung chung
            else if (condition.contains("mây") || condition.contains("cloud")) {
                return isNight ? "scattered_clouds_night.gif" : "scattered_clouds_day.gif";
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

                // Scattered clouds - mây cụm
                case "03d":
                    return R.drawable.placeholder_scattered_clouds_day;
                case "03n":
                    return R.drawable.placeholder_scattered_clouds_night;

                // Broken clouds - mây đen u ám
                case "04d":
                    return R.drawable.placeholder_cloudy_day;
                case "04n":
                    return R.drawable.placeholder_broken_clouds_night;

                // Clustered clouds - mây cụm
                case "05d":
                    return R.drawable.placeholder_clustered_clouds_day;
                case "05n":
                    return R.drawable.placeholder_clustered_clouds_night;
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
            } else if (condition.contains("clustered") || condition.contains("mây cụm")) {
                return isNight ? R.drawable.placeholder_clustered_clouds_night : R.drawable.placeholder_clustered_clouds_day;
            } else if (condition.contains("scattered")) {
                return isNight ? R.drawable.placeholder_scattered_clouds_night : R.drawable.placeholder_scattered_clouds_day;
            } else if (condition.contains("broken") || condition.contains("overcast")) {
                return isNight ? R.drawable.placeholder_broken_clouds_night : R.drawable.placeholder_cloudy_day;
            } else if (condition.contains("cloud")) {
                return isNight ? R.drawable.placeholder_scattered_clouds_night : R.drawable.placeholder_scattered_clouds_day;
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

    // Method để convert weather description thành icon code tùy chỉnh
    public static String getCustomIconCode(String weatherDescription, String originalIconCode) {
        if (weatherDescription == null) return originalIconCode;
        
        String description = weatherDescription.toLowerCase();
        boolean isNight = isNightTime(originalIconCode);
        
        // Kiểm tra nếu là mây cụm
        if (description.contains("mây cụm") || description.contains("clustered")) {
            return isNight ? "05n" : "05d";
        }
        
        // Trả về icon code gốc nếu không có custom mapping
        return originalIconCode;
    }
}
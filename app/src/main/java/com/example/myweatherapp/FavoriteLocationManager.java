package com.example.myweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteLocationManager {
    private static final String PREFS_NAME = "FavoriteLocations";
    private static final String KEY_FAVORITES = "favorites";
    private SharedPreferences prefs;
    private Gson gson;

    public FavoriteLocationManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addFavorite(FavoriteLocation location) {
        List<FavoriteLocation> favorites = getFavorites();
        
        // Kiểm tra xem đã tồn tại chưa
        for (FavoriteLocation fav : favorites) {
            if (fav.getCityName().equalsIgnoreCase(location.getCityName())) {
                return; // Đã tồn tại, không thêm nữa
            }
        }
        
        favorites.add(location);
        saveFavorites(favorites);
    }

    public void removeFavorite(String cityName) {
        List<FavoriteLocation> favorites = getFavorites();
        favorites.removeIf(fav -> fav.getCityName().equalsIgnoreCase(cityName));
        saveFavorites(favorites);
    }

    public boolean isFavorite(String cityName) {
        List<FavoriteLocation> favorites = getFavorites();
        for (FavoriteLocation fav : favorites) {
            if (fav.getCityName().equalsIgnoreCase(cityName)) {
                return true;
            }
        }
        return false;
    }

    public List<FavoriteLocation> getFavorites() {
        String json = prefs.getString(KEY_FAVORITES, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<FavoriteLocation>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private void saveFavorites(List<FavoriteLocation> favorites) {
        String json = gson.toJson(favorites);
        prefs.edit().putString(KEY_FAVORITES, json).apply();
    }
}
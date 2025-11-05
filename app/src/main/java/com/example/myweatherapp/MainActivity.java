package com.example.myweatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageView ivChartButton;
    private RecyclerView rvHourlyForecast;
    private HourlyAdapter hourlyAdapter;
    private List<HourlyForecast> hourlyForecastList;

    private RecyclerView rvDailyForecast;
    private DailyAdapter dailyAdapter;
    private List<DailyForecast> dailyForecastList;

    private TextView tvFeelsLikeTitle, tvFeelsLikeValue;
    private TextView tvHumidityTitle, tvHumidityValue;
    private TextView tvWindTitle, tvWindValue;
    private TextView tvVisibilityTitle, tvVisibilityValue;

    private TextView tvSunriseTitle, tvSunriseValue, tvSunriseDescription;
    private TextView tvUvTitle, tvUvValue, tvUvDescription;

    private TextView tvCityName, tvTemperature, tvDescription, tvHighLow;

    private ImageView ivManageCities;
    private ImageView ivHistoryButton;  // ✅ NÚT MỚI - LỊCH SỬ
    private ImageView ivRefreshLocation; // ✅ NÚT LÀM MỚI VỊ TRÍ
    private ImageView ivFavoriteButton; // ✅ NÚT YÊU THÍCH MỚI
    
    // ===== ✅ BIẾN MỚI CHO TÍNH NĂNG YÊU THÍCH =====
    private FavoriteLocationManager favoriteManager;
    private String currentWeatherIcon = "";
    private String currentWeatherDescription = "";

    private final String API_KEY = "56852ceecb78b34704e90b7c36712630";
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String BASE_URL_FORECAST = "https://api.openweathermap.org/data/2.5/forecast";

    // ===== ✅ BIẾN MỚI CHO GPS =====
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private String currentCityName = "HaNoi";

    public static final String PREFS_NAME = "WeatherAppPrefs";
    private static final String KEY_CURRENT_CITY = "CurrentCity";
    private static final int REQUEST_CODE_MANAGE_CITIES = 123;
    private double currentLat = 10.8231;
    private double currentLon = 106.6297;

    private String currentTempForHourly = "";
    private String currentIconForHourly = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===== PHẦN ÁNH XẠ =====
        tvCityName = findViewById(R.id.tvCityName);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDescription = findViewById(R.id.tvDescription);
        tvHighLow = findViewById(R.id.tvHighLow);

        View cardFeelsLike = findViewById(R.id.cardFeelsLike);
        tvFeelsLikeTitle = cardFeelsLike.findViewById(R.id.tvCardTitle);
        tvFeelsLikeValue = cardFeelsLike.findViewById(R.id.tvCardValue);

        View cardHumidity = findViewById(R.id.cardHumidity);
        tvHumidityTitle = cardHumidity.findViewById(R.id.tvCardTitle);
        tvHumidityValue = cardHumidity.findViewById(R.id.tvCardValue);

        View cardWind = findViewById(R.id.cardWind);
        tvWindTitle = cardWind.findViewById(R.id.tvCardTitle);
        tvWindValue = cardWind.findViewById(R.id.tvCardValue);

        View cardVisibility = findViewById(R.id.cardVisibility);
        tvVisibilityTitle = cardVisibility.findViewById(R.id.tvCardTitle);
        tvVisibilityValue = cardVisibility.findViewById(R.id.tvCardValue);

        View cardSunriseSunset = findViewById(R.id.cardSunriseSunset);
        tvSunriseTitle = cardSunriseSunset.findViewById(R.id.tvCardTitle);
        tvSunriseValue = cardSunriseSunset.findViewById(R.id.tvCardValue);
        tvSunriseDescription = cardSunriseSunset.findViewById(R.id.tvCardDescription);

        View cardUvIndex = findViewById(R.id.cardUvIndex);
        tvUvTitle = cardUvIndex.findViewById(R.id.tvCardTitle);
        tvUvValue = cardUvIndex.findViewById(R.id.tvCardValue);
        tvUvDescription = cardUvIndex.findViewById(R.id.tvCardDescription);

        ivManageCities = findViewById(R.id.ivManageCities);
        ivHistoryButton = findViewById(R.id.ivHistoryButton); // ✅ NÚT LỊCH SỬ
        ivRefreshLocation = findViewById(R.id.ivRefreshLocation); // ✅ NÚT LÀM MỚI
        ivFavoriteButton = findViewById(R.id.ivFavoriteButton); // ✅ NÚT YÊU THÍCH

        rvHourlyForecast = findViewById(R.id.rvHourlyForecast);
        rvDailyForecast = findViewById(R.id.rvDailyForecast);

        ivChartButton = findViewById(R.id.ivChartButton);
        ivChartButton.setOnClickListener(v -> openChartActivity());

        // ===== CÀI ĐẶT RECYCLERVIEW =====
        hourlyForecastList = new ArrayList<>();
        hourlyAdapter = new HourlyAdapter(hourlyForecastList);
        rvHourlyForecast.setAdapter(hourlyAdapter);

        dailyForecastList = new ArrayList<>();
        dailyAdapter = new DailyAdapter(dailyForecastList);
        rvDailyForecast.setAdapter(dailyAdapter);
        rvDailyForecast.setNestedScrollingEnabled(false);

        // ===== ✅ KHỞI TẠO GPS CLIENT VÀ FAVORITE MANAGER =====
        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
        favoriteManager = new FavoriteLocationManager(this);
// THAY ĐỔI LOGIC KHỞI ĐỘNG
        // 1. Tải thành phố đã lưu từ "sổ tay"
        String savedCity = loadCurrentCity(); // Mặc định là "Hanoi" nếu chưa có

        // 2. Lấy thời tiết cho thành phố đã lưu
        if (savedCity != null && !savedCity.isEmpty()) {
            fetchWeatherData(savedCity);
            fetchForecastData(savedCity);
        } else {
            // Nếu không có gì được lưu (lần đầu tiên), mới gọi GPS
            getCurrentLocationAndFetchWeather();
        }


        // ===== UV INDEX (dữ liệu giả) =====
        tvUvTitle.setText("CHỈ SỐ UV");
        tvUvValue.setText("5");
        tvUvDescription.setText("Mức độ vừa phải");
        tvUvDescription.setVisibility(View.VISIBLE);

        // ===== ✅ SỰ KIỆN CÁC NÚT =====
        ivManageCities.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ManageCitiesActivity.class);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_CITIES);
        });

        // ✅ NÚT YÊU THÍCH (thay thế nút lịch sử)
        ivHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, REQUEST_CODE_MANAGE_CITIES); // Sử dụng cùng request code
        });

        // ✅ NÚT LÀM MỚI VỊ TRÍ
        ivRefreshLocation.setOnClickListener(v -> {
            Toast.makeText(this, "Đang cập nhật vị trí...", Toast.LENGTH_SHORT).show();
            getCurrentLocationAndFetchWeather();
        });

        // ✅ NÚT YÊU THÍCH
        ivFavoriteButton.setOnClickListener(v -> {
            toggleFavorite();
        });
        

    }

    // ===== ✅ HÀM LẤY VỊ TRÍ HIỆN TẠI =====
    private void getCurrentLocationAndFetchWeather() {
        // Kiểm tra quyền truy cập vị trí
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Xin quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        // Lấy vị trí hiện tại
        fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationTokenSource().getToken())
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLon = location.getLongitude();
                        fetchCityNameFromCoordinates(currentLat, currentLon);
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại. Dùng vị trí mặc định.", Toast.LENGTH_SHORT).show();
                        fetchWeatherDataByCoordinates(currentLat, currentLon);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi GPS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    fetchWeatherDataByCoordinates(currentLat, currentLon);
                });
    }

    // ===== ✅ LẤY TÊN THÀNH PHỐ TỪ TỌA ĐỘ =====
    private void fetchCityNameFromCoordinates(double lat, double lon) {
        String url = "https://api.openweathermap.org/geo/1.0/reverse?lat=" + lat
                + "&lon=" + lon + "&limit=1&appid=" + API_KEY + "&lang=vi";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject location = jsonArray.getJSONObject(0);
                            currentCityName = location.optString("name", "Vị trí không xác định");

                            // Sau khi có tên thành phố, gọi API thời tiết
                            fetchWeatherDataByCoordinates(lat, lon);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        currentCityName = "Vị trí không xác định";
                        fetchWeatherDataByCoordinates(lat, lon);
                    }
                },
                error -> {
                    error.printStackTrace();
                    fetchWeatherDataByCoordinates(lat, lon);
                });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // ===== ✅ LẤY THỜI TIẾT THEO TỌA ĐỘ =====
    private void fetchWeatherDataByCoordinates(double lat, double lon) {
        String url = BASE_URL + "?lat=" + lat + "&lon=" + lon
                + "&appid=" + API_KEY + "&units=metric&lang=vi";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject root = new JSONObject(response);

                        String city = root.getString("name");
                        currentCityName = city;

                        JSONObject main = root.getJSONObject("main");
                        double temp = main.getDouble("temp");
                        double tempMax = main.getDouble("temp_max");
                        double tempMin = main.getDouble("temp_min");

                        JSONArray weatherArray = root.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");

                        double feelsLike = main.getDouble("feels_like");
                        int humidity = main.getInt("humidity");

                        JSONObject wind = root.getJSONObject("wind");
                        double windSpeed = wind.getDouble("speed");

                        int visibility = root.getInt("visibility");

                        JSONObject sys = root.getJSONObject("sys");
                        long sunriseTimestamp = sys.getLong("sunrise");
                        long sunsetTimestamp = sys.getLong("sunset");

                        DecimalFormat df = new DecimalFormat("#");
                        String tempFormatted = df.format(temp) + "°";

                        currentTempForHourly = tempFormatted;
                        currentIconForHourly = weather.getString("icon");
                        currentWeatherIcon = weather.getString("icon");
                        currentWeatherDescription = description;

                        String highLowFormatted = "C: " + df.format(tempMax) + "°   T: " + df.format(tempMin) + "°";

                        String windSpeedFormatted = df.format(windSpeed * 3.6) + " km/h";
                        String visibilityFormatted = df.format(visibility / 1000.0) + " km";
                        String feelsLikeFormatted = df.format(feelsLike) + "°";
                        String humidityFormatted = humidity + "%";

                        String sunriseTime = formatUnixTimestamp(sunriseTimestamp);
                        String sunsetTime = formatUnixTimestamp(sunsetTimestamp);

                        tvCityName.setText(city);
                        tvTemperature.setText(tempFormatted);

                        description = description.substring(0, 1).toUpperCase() + description.substring(1);
                        tvDescription.setText(description);

                        tvHighLow.setText(highLowFormatted);

                        tvFeelsLikeTitle.setText("CẢM GIÁC NHƯ");
                        tvFeelsLikeValue.setText(feelsLikeFormatted);

                        tvHumidityTitle.setText("ĐỘ ẨM");
                        tvHumidityValue.setText(humidityFormatted);

                        tvWindTitle.setText("GIÓ");
                        tvWindValue.setText(windSpeedFormatted);

                        tvVisibilityTitle.setText("TẦM NHÌN");
                        tvVisibilityValue.setText(visibilityFormatted);

                        tvSunriseTitle.setText("MẶT TRỜI MỌC");
                        tvSunriseValue.setText(sunriseTime);
                        tvSunriseDescription.setText("Mặt trời lặn: " + sunsetTime);
                        tvSunriseDescription.setVisibility(View.VISIBLE);

                        // ✅ CẬP NHẬT HÌNH NỀN ĐỘNG
                        updateBackgroundBasedOnWeather(description, weather.getString("icon"));
                        
                        // ✅ CẬP NHẬT TRẠNG THÁI NÚT YÊU THÍCH
                        updateFavoriteButton();

                        // Gọi API dự báo
                        fetchForecastDataByCoordinates(lat, lon);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    tvCityName.setText("Không thể tải dữ liệu");
                });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // ===== ✅ LẤY DỰ BÁO THEO TỌA ĐỘ =====
    private void fetchForecastDataByCoordinates(double lat, double lon) {
        String url = BASE_URL_FORECAST + "?lat=" + lat + "&lon=" + lon
                + "&appid=" + API_KEY + "&units=metric&lang=vi";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        hourlyForecastList.clear();
                        dailyForecastList.clear();

                        JSONObject root = new JSONObject(response);
                        JSONArray list = root.getJSONArray("list");
                        DecimalFormat df = new DecimalFormat("#");

// ✅ THAY BẰNG KHỐI CODE NÀY
// --- BƯỚC 1: THÊM "BÂY GIỜ" ---
                        if (!currentTempForHourly.isEmpty() && !currentIconForHourly.isEmpty()) {
                            hourlyForecastList.add(new HourlyForecast("Bây giờ", currentTempForHourly, currentIconForHourly));
                        }

// --- BƯỚC 2: DỰ BÁO HÀNG GIỜ (8 MỐC TIẾP THEO) ---
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date now = new Date();
                        int count = 0;

// (Copy y hệt vòng "for (int i = 0; i < list.length(); i++)"
// từ hàm fetchForecastData(String cityName) (dòng 747) dán vào đây)
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String dateTime = item.getString("dt_txt");
                            Date forecastTime = sdf.parse(dateTime);

                            if (forecastTime.after(now)) {
                                String time = dateTime.substring(11, 16);
                                JSONObject mainForecast = item.getJSONObject("main");
                                String temp = df.format(mainForecast.getDouble("temp")) + "°";
                                JSONArray weatherArray = item.getJSONArray("weather");
                                JSONObject weather = weatherArray.getJSONObject(0);
                                String icon = weather.getString("icon");
                                hourlyForecastList.add(new HourlyForecast(time, temp, icon));
                                count++;
                            }
                            if (count >= 8) break;
                        }

// --- BƯỚC 3: DỰ BÁO HÀNG NGÀY (HASHMAP) ---
// (Copy y hệt phần "HashMap<String, DailyData> dailyMap = ..."
// từ hàm fetchForecastData(String cityName) (dòng 768) dán vào đây)
                        java.util.HashMap<String, DailyData> dailyMap = new java.util.HashMap<>();
// ... (toàn bộ code xử lý HashMap và TreeMap) ...
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String dateTime = item.getString("dt_txt");
                            String date = dateTime.substring(0, 10);
                            JSONObject mainDay = item.getJSONObject("main");
                            float tempMax = (float) mainDay.getDouble("temp_max");
                            float tempMin = (float) mainDay.getDouble("temp_min");
                            int humidity = mainDay.getInt("humidity");
                            JSONArray weatherArray = item.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String icon = weather.getString("icon");
                            if (!dailyMap.containsKey(date)) {
                                dailyMap.put(date, new DailyData(tempMax, tempMin, icon, humidity));
                            } else {
                                DailyData existing = dailyMap.get(date);
                                if (tempMax > existing.tempMax) existing.tempMax = tempMax;
                                if (tempMin < existing.tempMin) existing.tempMin = tempMin;
                                existing.addHumidity(humidity);
                            }
                        }
                        java.util.TreeMap<String, DailyData> sortedMap = new java.util.TreeMap<>(dailyMap);
                        for (java.util.Map.Entry<String, DailyData> entry : sortedMap.entrySet()) {
                            String date = entry.getKey();
                            DailyData data = entry.getValue();
                            String day = getDayOfWeek(date);
                            String tempHigh = "C: " + df.format(data.tempMax) + "°";
                            String tempLow = "T: " + df.format(data.tempMin) + "°";
                            int avgHumidity = data.getAverageHumidity();
                            dailyForecastList.add(new DailyForecast(day, tempHigh, tempLow, data.icon, avgHumidity));
                        }

// --- BƯỚC 4: CẬP NHẬT GIAO DIỆN ---
                        hourlyAdapter.notifyDataSetChanged();
                        dailyAdapter.notifyDataSetChanged();
// ✅ KẾT THÚC KHỐI CODE THAY THẾ

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> error.printStackTrace());

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // ===== ✅ XỬ LÝ KẾT QUẢ XIN QUYỀN =====
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndFetchWeather();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối. Dùng vị trí mặc định.",
                        Toast.LENGTH_LONG).show();
                fetchWeatherDataByCoordinates(currentLat, currentLon);
            }
        }
    }

    private String getDayOfWeek(String dateString) {
        try {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayString = inFormat.format(today);

            if (dateString.equals(todayString)) {
                return "Hôm nay";
            }

            Date date = inFormat.parse(dateString);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
            String dayOfWeek = outFormat.format(date);
            return dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    private String formatUnixTimestamp(long timestamp) {
        try {
            Date date = new Date(timestamp * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void openChartActivity() {
        ArrayList<String> days = new ArrayList<>();
        ArrayList<Integer> temps = new ArrayList<>();
        ArrayList<Integer> humidities = new ArrayList<>();

        for (DailyForecast forecast : dailyForecastList) {
            days.add(forecast.day);
            String tempHigh = forecast.tempHigh.replace("C: ", "").replace("°", "");
            try {
                temps.add(Integer.parseInt(tempHigh));
            } catch (NumberFormatException e) {
                temps.add(25);
            }
            humidities.add(forecast.humidity);
        }

        Intent intent = new Intent(MainActivity.this, WeatherChartActivity.class);
        intent.putStringArrayListExtra("days", days);
        intent.putIntegerArrayListExtra("temps", temps);
        intent.putIntegerArrayListExtra("humidities", humidities);
        intent.putExtra("cityName", tvCityName.getText().toString());
        startActivity(intent);
    }

    private static class DailyData {
        float tempMax;
        float tempMin;
        String icon;
        int humiditySum;
        int humidityCount;

        DailyData(float tempMax, float tempMin, String icon, int humidity) {
            this.tempMax = tempMax;
            this.tempMin = tempMin;
            this.icon = icon;
            this.humiditySum = humidity;
            this.humidityCount = 1;
        }

        void addHumidity(int humidity) {
            this.humiditySum += humidity;
            this.humidityCount++;
        }

        int getAverageHumidity() {
            if (humidityCount == 0) return 0;
            return Math.round((float) humiditySum / humidityCount);
        }
    }

    private void fetchWeatherData(String cityName) {
        // 1. Tạo "địa chỉ" URL đầy đủ
        // units=metric để lấy nhiệt độ theo độ C
        String url = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=vi";

        // 2. Tạo một "yêu cầu" (Request) - Sử dụng VolleySingleton
        // Chúng ta yêu cầu máy chủ trả về dữ liệu dạng Chuỗi (StringRequest)
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // -- 5A. KHI THÀNH CÔNG (Máy chủ trả lời) --
                    @Override
                    public void onResponse(String response) {
                        // Dữ liệu máy chủ trả về là một chuỗi JSON
                        // Chúng ta cần "phân tích" (parse) chuỗi JSON này
                        try {
                            JSONObject root = new JSONObject(response);

                            // Lấy tên thành phố
                            String city = root.getString("name");


                            // ✅ THÊM ĐOẠN NÀY
                            // Lấy tọa độ và cập nhật biến toàn cục
                            JSONObject coord = root.getJSONObject("coord");
                            currentLat = coord.getDouble("lat");
                            currentLon = coord.getDouble("lon");
                            currentCityName = city; // Cập nhật cả tên

                            // Lấy đối tượng "main" (chứa nhiệt độ)
                            JSONObject main = root.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            double tempMax = main.getDouble("temp_max");
                            double tempMin = main.getDouble("temp_min");

                            // Lấy mảng "weather" (chứa mô tả)
                            JSONArray weatherArray = root.getJSONArray("weather");
                            // Lấy phần tử đầu tiên của mảng
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String description = weather.getString("description");

                            // 1. Lấy từ đối tượng "main"
                            double feelsLike = main.getDouble("feels_like");
                            int humidity = main.getInt("humidity");

                            // 2. Lấy từ đối tượng "wind"
                            JSONObject wind = root.getJSONObject("wind");
                            double windSpeed = wind.getDouble("speed"); // đơn vị là m/s

                            // 3. Lấy từ "root"
                            int visibility = root.getInt("visibility"); // đơn vị là mét

                            // 1. Lấy từ đối tượng "sys"
                            JSONObject sys = root.getJSONObject("sys");
                            long sunriseTimestamp = sys.getLong("sunrise");
                            long sunsetTimestamp = sys.getLong("sunset");

                            // Định dạng lại nhiệt độ (làm tròn)
                            DecimalFormat df = new DecimalFormat("#");
                            String tempFormatted = df.format(temp) + "°";

                            currentTempForHourly = tempFormatted;
                            currentIconForHourly = weather.getString("icon");
                            currentWeatherIcon = weather.getString("icon");
                            currentWeatherDescription = description;
                            String highLowFormatted = "C: " + df.format(tempMax) + "°   T: " + df.format(tempMin) + "°";

                            // --- CODE MỚI: ĐỊNH DẠNG DỮ LIỆU CHI TIẾT ---

                            // m/s -> km/h (nhân 3.6)
                            String windSpeedFormatted = df.format(windSpeed * 3.6) + " km/h";
                            // mét -> km (chia 1000)
                            String visibilityFormatted = df.format(visibility / 1000.0) + " km";
                            String feelsLikeFormatted = df.format(feelsLike) + "°";
                            String humidityFormatted = humidity + "%";

                            // --- CODE MỚI: ĐỊNH DẠNG DỮ LIỆU SUNRISE/SUNSET ---
                            String sunriseTime = formatUnixTimestamp(sunriseTimestamp);
                            String sunsetTime = formatUnixTimestamp(sunsetTimestamp);

                            // 6. Cập nhật Giao Diện (UI)
                            tvCityName.setText(city);
                            tvTemperature.setText(tempFormatted);

                            // Viết hoa chữ cái đầu của mô tả
                            description = description.substring(0, 1).toUpperCase() + description.substring(1);
                            tvDescription.setText(description);

                            tvHighLow.setText(highLowFormatted);

                            // --- CODE MỚI: CẬP NHẬT 4 THẺ CHI TIẾT ---

                            // Cập nhật thẻ "Cảm giác như"
                            tvFeelsLikeTitle.setText("CẢM GIÁC NHƯ");
                            tvFeelsLikeValue.setText(feelsLikeFormatted);

                            // Cập nhật thẻ "Độ ẩm"
                            tvHumidityTitle.setText("ĐỘ ẨM");
                            tvHumidityValue.setText(humidityFormatted);

                            // Cập nhật thẻ "Gió"
                            tvWindTitle.setText("GIÓ");
                            tvWindValue.setText(windSpeedFormatted);

                            // Cập nhật thẻ "Tầm nhìn"
                            tvVisibilityTitle.setText("TẦM NHÌN");
                            tvVisibilityValue.setText(visibilityFormatted);

                            // --- CODE MỚI: CẬP NHẬT THẺ SUNRISE/SUNSET ---
                            tvSunriseTitle.setText("MẶT TRỜI MỌC");
                            tvSunriseValue.setText(sunriseTime);
                            tvSunriseDescription.setText("Mặt trời lặn: " + sunsetTime);

                            // Quan trọng: Bật hiển thị cho dòng mô tả
                            tvSunriseDescription.setVisibility(View.VISIBLE);

                            // ✅ CẬP NHẬT HÌNH NỀN ĐỘNG
                            updateBackgroundBasedOnWeather(description, weather.getString("icon"));
                            
                            // ✅ CẬP NHẬT TRẠNG THÁI NÚT YÊU THÍCH
                            updateFavoriteButton();

                        } catch (JSONException e) {
                            // Nếu có lỗi khi phân tích JSON, in ra lỗi
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    // -- 5B. KHI THẤT BẠI (Mạng lỗi, URL sai, ...) --
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // In lỗi ra
                        error.printStackTrace();
                        tvCityName.setText("Không thể tải dữ liệu");
                    }
                });

        // 4. Thêm "yêu cầu" vào "hàng đợi" để nó bắt đầu chạy
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // ✅ HÀM ĐÃ ĐƯỢC VIẾT LẠI HOÀN CHỈNH
    private void fetchForecastData(String cityName) {
        // 1. Tạo URL bằng TÊN THÀNH PHỐ
        String url = BASE_URL_FORECAST + "?q=" + cityName
                + "&appid=" + API_KEY + "&units=metric&lang=vi";

        // 2. Gọi Volley sử dụng Singleton pattern

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Xóa dữ liệu cũ
                        hourlyForecastList.clear();
                        dailyForecastList.clear();

                        JSONObject root = new JSONObject(response);
                        JSONArray list = root.getJSONArray("list");
                        DecimalFormat df = new DecimalFormat("#");

                        // --- BƯỚC 1: THÊM "BÂY GIỜ" ---
                        // Lấy từ biến toàn cục, không cần gọi API mới
                        if (!currentTempForHourly.isEmpty() && !currentIconForHourly.isEmpty()) {
                            hourlyForecastList.add(new HourlyForecast("Bây giờ", currentTempForHourly, currentIconForHourly));
                        }

                        // --- BƯỚC 2: DỰ BÁO HÀNG GIỜ (8 MỐC TIẾP THEO) ---
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date now = new Date();
                        int count = 0;

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String dateTime = item.getString("dt_txt");
                            Date forecastTime = sdf.parse(dateTime);

                            if (forecastTime.after(now)) { // Chỉ lấy mốc thời gian sau "Bây giờ"
                                String time = dateTime.substring(11, 16); // Lấy "HH:mm"
                                JSONObject mainForecast = item.getJSONObject("main");
                                String temp = df.format(mainForecast.getDouble("temp")) + "°";
                                JSONArray weatherArray = item.getJSONArray("weather");
                                JSONObject weather = weatherArray.getJSONObject(0);
                                String icon = weather.getString("icon");
                                hourlyForecastList.add(new HourlyForecast(time, temp, icon));
                                count++;
                            }

                            if (count >= 8) break; // Chỉ lấy 8 mốc
                        }

                        // --- BƯỚC 3: DỰ BÁO HÀNG NGÀY (LOGIC CHÍNH XÁC DÙNG HASHMAP) ---
                        java.util.HashMap<String, DailyData> dailyMap = new java.util.HashMap<>();

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);
                            String dateTime = item.getString("dt_txt");
                            String date = dateTime.substring(0, 10); // Lấy "yyyy-MM-dd"

                            JSONObject mainDay = item.getJSONObject("main");
                            float tempMax = (float) mainDay.getDouble("temp_max");
                            float tempMin = (float) mainDay.getDouble("temp_min");
                            int humidity = mainDay.getInt("humidity");

                            JSONArray weatherArray = item.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String icon = weather.getString("icon");

                            if (!dailyMap.containsKey(date)) {
                                // Nếu ngày này chưa có, tạo mới
                                dailyMap.put(date, new DailyData(tempMax, tempMin, icon, humidity));
                            } else {
                                // Nếu ngày này đã có, cập nhật min/max
                                DailyData existing = dailyMap.get(date);
                                if (tempMax > existing.tempMax) existing.tempMax = tempMax;
                                if (tempMin < existing.tempMin) existing.tempMin = tempMin;
                                existing.addHumidity(humidity); // Thêm độ ẩm để tính trung bình
                            }
                        }

                        // Sắp xếp các ngày theo thứ tự
                        java.util.TreeMap<String, DailyData> sortedMap = new java.util.TreeMap<>(dailyMap);

                        // Đổ dữ liệu đã xử lý vào list
                        for (java.util.Map.Entry<String, DailyData> entry : sortedMap.entrySet()) {
                            String date = entry.getKey();
                            DailyData data = entry.getValue();

                            String day = getDayOfWeek(date);
                            String tempHigh = "C: " + df.format(data.tempMax) + "°";
                            String tempLow = "T: " + df.format(data.tempMin) + "°";
                            int avgHumidity = data.getAverageHumidity();

                            // Thêm vào list (phải đảm bảo constructor của DailyForecast khớp)
                            // Giả sử DailyForecast của em nhận (day, tempHigh, tempLow, icon, humidity)
                            dailyForecastList.add(new DailyForecast(day, tempHigh, tempLow, data.icon, avgHumidity));
                        }


                        // --- BƯỚC 4: CẬP NHẬT GIAO DIỆN ---
                        hourlyAdapter.notifyDataSetChanged();
                        dailyAdapter.notifyDataSetChanged();

                    } catch (Exception e) { // Bắt cả JSONException và ParseException
                        e.printStackTrace();
                        android.util.Log.e("FetchForecastError", "Lỗi parse JSON hoặc Date: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                    android.widget.Toast.makeText(MainActivity.this, "Không thể tải dự báo 5 ngày", android.widget.Toast.LENGTH_SHORT).show();
                });

        // 5. Thêm yêu cầu vào hàng đợi
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate
    private void saveCurrentCity(String city) {
        // 1. Mở "quyển sổ" và lấy "bút"
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        // 2. Viết vào "trang" thành phố hiện tại
        editor.putString(KEY_CURRENT_CITY, city);

        // 3. "Đóng sổ" (Lưu lại)
        editor.apply();
    }

    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate
    private String loadCurrentCity() {
        // 1. Mở "quyển sổ"
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 2. Đọc "trang"
        // Nếu không tìm thấy (lần đầu mở app), dùng "Hanoi" làm mặc định
        return prefs.getString(KEY_CURRENT_CITY, "Hanoi");
    }


    // Đặt hàm này BÊN NGOÀI (bên dưới) hàm onCreate

    // Đây là hàm sẽ tự động được gọi khi Màn 2 (ManageCitiesActivity) đóng lại
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 1. Kiểm tra xem có đúng là "nhiệm vụ 123" không?
        if (requestCode == REQUEST_CODE_MANAGE_CITIES) {

            // 2. Kiểm tra xem Màn 2 có "dán tem" HỢP LỆ (RESULT_OK) không?
            if (resultCode == RESULT_OK) {

                // 3. "Mở thư" và lấy "gói hàng" (tên thành phố)
                // Nhớ dùng đúng cái nhãn "SELECTED_CITY"
                String selectedCity = data.getStringExtra("SELECTED_CITY");

                if (selectedCity != null && !selectedCity.isEmpty()) {

                    // 4. LÀM MỚI GIAO DIỆN VỚI THÀNH PHỐ MỚI
                    fetchWeatherData(selectedCity);
                    fetchForecastData(selectedCity);

                    // 5. LƯU LẠI (để lần sau mở app nó nhớ)
                    saveCurrentCity(selectedCity);
                }
            } else {
                // ✅ THÊM: Khi quay về từ trang yêu thích mà không chọn gì
                // Cập nhật lại trạng thái nút yêu thích cho thành phố hiện tại
                updateFavoriteButton();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ✅ Cập nhật trạng thái nút yêu thích khi quay về activity
        // Delay một chút để đảm bảo dữ liệu đã được cập nhật
        new android.os.Handler().postDelayed(() -> {
            updateFavoriteButton();
        }, 100);
    }

    // ===== ✅ CÁC METHOD MỚI CHO TÍNH NĂNG YÊU THÍCH VÀ HÌNH NỀN =====
    
    private boolean checkAssetExists(String fileName) {
        try {
            getAssets().open(fileName).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void loadGifAlternative(android.widget.ImageView imageView, String fileName, int placeholder) {
        try {
            com.bumptech.glide.Glide.with(this)
                .asGif()
                .load("android_asset://" + fileName)
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView);
        } catch (Exception e) {
            imageView.setImageResource(placeholder);
        }
    }
    

    
    private void updateBackgroundBasedOnWeather(String weatherDescription, String iconCode) {
        String backgroundImageName = WeatherBackgroundManager.getBackgroundImageName(weatherDescription, iconCode);
        int placeholderDrawable = WeatherBackgroundManager.getPlaceholderDrawable(weatherDescription, iconCode);
        android.widget.ImageView backgroundImageView = findViewById(R.id.ivAnimatedBackground);
        
        if (backgroundImageView == null) return;
        
        // Set placeholder trước
        backgroundImageView.setImageResource(placeholderDrawable);
        
        // Kiểm tra file có tồn tại không
        if (!checkAssetExists(backgroundImageName)) {
            return; // Chỉ dùng placeholder
        }
        
        // Load ảnh động bằng Glide
        String assetPath = "file:///android_asset/" + backgroundImageName;
        
        try {
            com.bumptech.glide.Glide.with(this)
                .asGif()
                .load(assetPath)
                .placeholder(placeholderDrawable)
                .error(placeholderDrawable)
                .fallback(placeholderDrawable)
                .listener(new com.bumptech.glide.request.RequestListener<com.bumptech.glide.load.resource.gif.GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(com.bumptech.glide.load.engine.GlideException e, Object model, 
                                              com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.gif.GifDrawable> target, 
                                              boolean isFirstResource) {
                        return false; // Let Glide handle the error
                    }
                    
                    @Override
                    public boolean onResourceReady(com.bumptech.glide.load.resource.gif.GifDrawable resource, Object model, 
                                                 com.bumptech.glide.request.target.Target<com.bumptech.glide.load.resource.gif.GifDrawable> target, 
                                                 com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false; // Let Glide handle the success
                    }
                })
                .into(backgroundImageView);
        } catch (Exception e) {
            // Fallback - thử method alternative
            loadGifAlternative(backgroundImageView, backgroundImageName, placeholderDrawable);
        }
        
        // Animation cho header card
        View headerLayout = findViewById(R.id.llCityHeader);
        if (headerLayout != null) {
            android.view.animation.Animation scaleIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.scale_in);
            headerLayout.startAnimation(scaleIn);
        }
    }
    

    
    private void updateFavoriteButton() {
        // Kiểm tra null safety
        if (currentCityName == null || currentCityName.isEmpty() || ivFavoriteButton == null) {
            return;
        }
        
        boolean isFavorite = favoriteManager.isFavorite(currentCityName);
        if (isFavorite) {
            ivFavoriteButton.setImageResource(R.drawable.ic_star_filled);
        } else {
            ivFavoriteButton.setImageResource(R.drawable.ic_star_outline);
        }
    }
    
    private void toggleFavorite() {
        // Kiểm tra null safety
        if (currentCityName == null || currentCityName.isEmpty()) {
            Toast.makeText(this, "Chưa có thông tin thành phố", Toast.LENGTH_SHORT).show();
            return;
        }
        
        boolean isFavorite = favoriteManager.isFavorite(currentCityName);
        
        if (isFavorite) {
            // Xóa khỏi yêu thích
            favoriteManager.removeFavorite(currentCityName);
            ivFavoriteButton.setImageResource(R.drawable.ic_star_outline);
            Toast.makeText(this, "Đã xóa " + currentCityName + " khỏi yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm vào yêu thích
            FavoriteLocation favorite = new FavoriteLocation(
                currentCityName, 
                currentLat, 
                currentLon,
                currentTempForHourly != null ? currentTempForHourly : "",
                currentWeatherDescription != null ? currentWeatherDescription : "",
                currentWeatherIcon != null ? currentWeatherIcon : ""
            );
            favoriteManager.addFavorite(favorite);
            ivFavoriteButton.setImageResource(R.drawable.ic_star_filled);
            Toast.makeText(this, "Đã thêm " + currentCityName + " vào yêu thích", Toast.LENGTH_SHORT).show();
        }
    }


}

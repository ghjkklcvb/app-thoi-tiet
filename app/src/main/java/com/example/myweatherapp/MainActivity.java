package com.example.myweatherapp;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import java.text.DecimalFormat; // Dùng để làm tròn nhiệt độ



public class MainActivity extends AppCompatActivity {

    private RecyclerView rvHourlyForecast;
    private HourlyAdapter hourlyAdapter;
    private List<HourlyForecast> hourlyForecastList;


    // Các biến MỚI cho dự báo ngày
    private RecyclerView rvDailyForecast;
    private DailyAdapter dailyAdapter;
    private List<DailyForecast> dailyForecastList;

    // --- BIẾN MỚI CHO 4 THẺ ---
    private TextView tvFeelsLikeTitle, tvFeelsLikeValue;
    private TextView tvHumidityTitle, tvHumidityValue;
    private TextView tvWindTitle, tvWindValue;
    private TextView tvVisibilityTitle, tvVisibilityValue;

    // --- BIẾN MỚI CHO THẺ SUNRISE/SUNSET ---
    private TextView tvSunriseTitle, tvSunriseValue, tvSunriseDescription;

    // --- BIẾN MỚI CHO THẺ UV INDEX ---
    private TextView tvUvTitle, tvUvValue, tvUvDescription;


    // Biến MỚI cho thông tin tổng quan
    private TextView tvCityName, tvTemperature, tvDescription, tvHighLow;

    private ImageView ivManageCities;

    private final String API_KEY = "f79c108bad93ab54be45c05a5c21a541";
    // Đây là "địa chỉ" máy chủ
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private final String BASE_URL_FORECAST = "https://api.openweathermap.org/data/2.5/forecast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- BẮT ĐẦU PHẦN ÁNH XẠ ---
        // Ánh xạ các TextViews tổng quan
        tvCityName = findViewById(R.id.tvCityName);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDescription = findViewById(R.id.tvDescription);
        tvHighLow = findViewById(R.id.tvHighLow);

        // --- CODE MỚI: ÁNH XẠ CÁC THẺ CHI TIẾT ---

        // Thẻ 1: Cảm giác như
        View cardFeelsLike = findViewById(R.id.cardFeelsLike);
        tvFeelsLikeTitle = cardFeelsLike.findViewById(R.id.tvCardTitle);
        tvFeelsLikeValue = cardFeelsLike.findViewById(R.id.tvCardValue);

        // Thẻ 2: Độ ẩm
        View cardHumidity = findViewById(R.id.cardHumidity);
        tvHumidityTitle = cardHumidity.findViewById(R.id.tvCardTitle);
        tvHumidityValue = cardHumidity.findViewById(R.id.tvCardValue);

        // Thẻ 3: Gió
        View cardWind = findViewById(R.id.cardWind);
        tvWindTitle = cardWind.findViewById(R.id.tvCardTitle);
        tvWindValue = cardWind.findViewById(R.id.tvCardValue);

        // Thẻ 4: Tầm nhìn
        View cardVisibility = findViewById(R.id.cardVisibility);
        tvVisibilityTitle = cardVisibility.findViewById(R.id.tvCardTitle);
        tvVisibilityValue = cardVisibility.findViewById(R.id.tvCardValue);

        // --- CODE MỚI: ÁNH XẠ THẺ SUNRISE/SUNSET ---
        View cardSunriseSunset = findViewById(R.id.cardSunriseSunset);
        tvSunriseTitle = cardSunriseSunset.findViewById(R.id.tvCardTitle);
        tvSunriseValue = cardSunriseSunset.findViewById(R.id.tvCardValue);
        tvSunriseDescription = cardSunriseSunset.findViewById(R.id.tvCardDescription);


        // --- CODE MỚI: ÁNH XẠ THẺ UV INDEX ---
        View cardUvIndex = findViewById(R.id.cardUvIndex);
        tvUvTitle = cardUvIndex.findViewById(R.id.tvCardTitle);
        tvUvValue = cardUvIndex.findViewById(R.id.tvCardValue);
        tvUvDescription = cardUvIndex.findViewById(R.id.tvCardDescription);

        // --- CODE MỚI: ÁNH XẠ NÚT BẤM ---
        ivManageCities = findViewById(R.id.ivManageCities);

        // Ánh xạ RecyclerView "hourly"
        rvHourlyForecast = findViewById(R.id.rvHourlyForecast);
        // Ánh xạ RecyclerView "daily"
        rvDailyForecast = findViewById(R.id.rvDailyForecast);
        // --- KẾT THÚC PHẦN ÁNH XẠ ---


        // --- BẮT ĐẦU CÀI ĐẶT "HOURLY" ---
        // 1. Khởi tạo danh sách (rỗng)
        hourlyForecastList = new ArrayList<>();
        // 2. Khởi tạo Adapter với danh sách RỖNG
        hourlyAdapter = new HourlyAdapter(hourlyForecastList);
        // 3. Lắp Adapter vào RecyclerView
        rvHourlyForecast.setAdapter(hourlyAdapter);
        // --- KẾT THÚC CÀI ĐẶT "HOURLY" ---


        // --- BẮT ĐẦU CÀI ĐẶT "DAILY" ---
        // 1. Khởi tạo danh sách (rỗng)
        dailyForecastList = new ArrayList<>();
        // 2. Khởi tạo Adapter với danh sách RỖNG
        dailyAdapter = new DailyAdapter(dailyForecastList);
        // 3. Lắp Adapter vào RecyclerView
        rvDailyForecast.setAdapter(dailyAdapter);
        // 4. Tắt cuộn lồng (cho ScrollView)
        rvDailyForecast.setNestedScrollingEnabled(false);
        // --- KẾT THÚC CÀI ĐẶT "DAILY" ---


        // --- BẮT ĐẦU GỌI API ---
        // Gọi hàm lấy thời tiết HIỆN TẠI
        fetchWeatherData("Ho Chi Minh");
        // Gọi hàm lấy thời tiết DỰ BÁO
        fetchForecastData("Ho Chi Minh");
        // --- KẾT THÚC GỌI API ---

        // --- CODE MỚI: GÁN DỮ LIỆU GIẢ CHO THẺ UV ---
        tvUvTitle.setText("CHỈ SỐ UV");
        tvUvValue.setText("5"); // Gán cứng giá trị "5"
        tvUvDescription.setText("Mức độ vừa phải"); // Gán cứng mô tả


        tvUvDescription.setVisibility(View.VISIBLE);

        // --- CODE MỚI: BẮT SỰ KIỆN CLICK CHO NÚT ---
        ivManageCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi người dùng bấm vào nút này...

                // 1. Tạo một "Ý Định" (Intent)
                // Nói với Android: "Tôi muốn đi TỪ MainActivity ĐẾN ManageCitiesActivity"
                Intent intent = new Intent(MainActivity.this, ManageCitiesActivity.class);

                // 2. Thực thi "Ý Định" đó
                startActivity(intent);
            }
        });
    }

    private void fetchWeatherData(String cityName) {
        // 1. Tạo "địa chỉ" URL đầy đủ
        // units=metric để lấy nhiệt độ theo độ C
        String url = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=vi";

        // 2. Tạo một "hàng đợi" (RequestQueue)
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // 3. Tạo một "yêu cầu" (Request)
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
        requestQueue.add(stringRequest);
    }


    // Đặt hàm này bên ngoài (bên dưới) hàm onCreate
    private void fetchForecastData(String cityName) {
        // 1. URL lấy dự báo
        String url = BASE_URL_FORECAST + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=vi";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            hourlyForecastList.clear();
                            dailyForecastList.clear();

                            JSONObject root = new JSONObject(response);
                            JSONArray list = root.getJSONArray("list");
                            DecimalFormat df = new DecimalFormat("#");

                            // ---- 🔹 LẤY NHIỆT ĐỘ HIỆN TẠI ----
                            String currentUrl = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=vi";
                            RequestQueue currentQueue = Volley.newRequestQueue(MainActivity.this);

                            StringRequest currentRequest = new StringRequest(Request.Method.GET, currentUrl,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String currentResponse) {
                                            try {
                                                JSONObject currentRoot = new JSONObject(currentResponse);
                                                JSONObject main = currentRoot.getJSONObject("main");
                                                String currentTemp = df.format(main.getDouble("temp")) + "°";
                                                JSONArray currentWeatherArray = currentRoot.getJSONArray("weather");
                                                JSONObject currentWeather = currentWeatherArray.getJSONObject(0);
                                                String currentIcon = currentWeather.getString("icon");


                                                hourlyForecastList.add(new HourlyForecast("Bây giờ", currentTemp, currentIcon));
                                                // ---- 🔹 XỬ LÝ DỮ LIỆU DỰ BÁO ----
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                                Date now = new Date(); // thời điểm hiện tại

                                                int count = 0;
                                                for (int i = 0; i < list.length(); i++) {
                                                    JSONObject item = list.getJSONObject(i);
                                                    String dateTime = item.getString("dt_txt");
                                                    Date forecastTime = sdf.parse(dateTime);

                                                    // chỉ lấy những mốc thời gian SAU thời điểm hiện tại
                                                    if (forecastTime.after(now)) {
                                                        String time = dateTime.substring(11, 16);
                                                        JSONObject mainForecast = item.getJSONObject("main");
                                                        String temp = df.format(mainForecast.getDouble("temp")) + "°";
                                                        // Lấy mã icon từ API
                                                        JSONArray weatherArray = item.getJSONArray("weather");
                                                        JSONObject weather = weatherArray.getJSONObject(0);
                                                        String icon = weather.getString("icon");
                                                        hourlyForecastList.add(new HourlyForecast(time, temp, icon));
                                                        count++;
                                                    }

                                                    // chỉ lấy tối đa 8 mốc (để hiển thị gọn)
                                                    if (count >= 8) break;
                                                }

                                                // ---- 🔹 DỰ BÁO NGÀY ----
                                                for (int i = 0; i < list.length(); i++) {
                                                    JSONObject item = list.getJSONObject(i);
                                                    String dateTime = item.getString("dt_txt");
                                                    if (dateTime.contains("12:00:00")) {
                                                        String day = getDayOfWeek(dateTime.substring(0, 10));
                                                        JSONObject mainDay = item.getJSONObject("main");
                                                        String tempHigh = "C: " + df.format(mainDay.getDouble("temp_max")) + "°";
                                                        String tempLow = "T: " + df.format(mainDay.getDouble("temp_min")) + "°";
                                                        // Lấy mã icon từ API
                                                        JSONArray weatherArray = item.getJSONArray("weather");
                                                        JSONObject weather = weatherArray.getJSONObject(0);
                                                        String icon = weather.getString("icon");
                                                        dailyForecastList.add(new DailyForecast(day, tempHigh, tempLow, icon));
                                                    }
                                                }

                                                // ---- 🔹 CẬP NHẬT UI ----
                                                hourlyAdapter.notifyDataSetChanged();
                                                dailyAdapter.notifyDataSetChanged();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });

                            currentQueue.add(currentRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(stringRequest);
    }




    // Đặt hàm này bên ngoài (bên dưới) hàm onCreate
    private String getDayOfWeek(String dateString) { // dateString là "yyyy-MM-dd"
        try {
            // --- PHẦN NÂNG CẤP ---
            // 1. Lấy ngày hôm nay
            Date today = Calendar.getInstance().getTime();

            // 2. Định dạng ngày hôm nay về "yyyy-MM-dd" để so sánh
            // (Chúng ta dùng lại inFormat cho cả 2 việc)
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayString = inFormat.format(today);

            // 3. So sánh
            if (dateString.equals(todayString)) {
                return "Hôm nay"; // Nếu là hôm nay, trả về "Hôm nay" và DỪNG LẠI
            }
            // --- KẾT THÚC NÂNG CẤP ---

            // Nếu không phải hôm nay, làm như cũ:
            // Định dạng đầu vào (ví dụ: "2025-10-23")
            Date date = inFormat.parse(dateString);

            // Định dạng đầu ra (ví dụ: "Thứ Năm")
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));

            String dayOfWeek = outFormat.format(date);
            // Viết hoa chữ cái đầu
            return dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);

        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Trả về ngày gốc nếu có lỗi
        }
    }



    // Đặt hàm này bên ngoài (bên dưới) hàm onCreate
    private String formatUnixTimestamp(long timestamp) {
        try {
            // Chuyển giây (Unix) sang mili-giây (Java)
            Date date = new Date(timestamp * 1000L);

            // Định dạng đầu ra (ví dụ: "05:45")
            // "HH" là giờ 24h, "mm" là phút
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            return sdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Trả về rỗng nếu có lỗi
        }
    }




}
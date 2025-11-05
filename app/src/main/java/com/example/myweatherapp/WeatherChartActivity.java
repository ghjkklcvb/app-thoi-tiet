package com.example.myweatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WeatherChartActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvChartTitle;
    private TemperatureChartView chartView;
    private HumidityChartView humidityChartView;

    // 3 Cards thống kê
    private TextView tvAvgTemp, tvMaxTemp, tvMinTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_chart);

        ivBack = findViewById(R.id.ivBack);
        tvChartTitle = findViewById(R.id.tvChartTitle);
        chartView = findViewById(R.id.chartView);
        humidityChartView = findViewById(R.id.humidityChartView);

        tvAvgTemp = findViewById(R.id.tvAvgTemp);
        tvMaxTemp = findViewById(R.id.tvMaxTemp);
        tvMinTemp = findViewById(R.id.tvMinTemp);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ArrayList<String> days = getIntent().getStringArrayListExtra("days");
        ArrayList<Float> temps = getIntent().getIntegerArrayListExtra("temps") != null
                ? convertToFloat(getIntent().getIntegerArrayListExtra("temps"))
                : null;

        String cityName = getIntent().getStringExtra("cityName");


        tvChartTitle.setText("Biểu đồ - " + cityName);


        if (days != null && temps != null && days.size() == temps.size()) {
            chartView.setData(days, temps);
            calculateStats(temps);
        }


        ArrayList<Integer> humidityData = getIntent().getIntegerArrayListExtra("humidities");

        if (days != null && humidityData != null && days.size() == humidityData.size()) {
            humidityChartView.setData(days, humidityData);
        } else {
            if (days != null) {
                ArrayList<Integer> fallbackData = generateHumidityData(days.size());
                humidityChartView.setData(days, fallbackData);
            }
        }
    }

    private ArrayList<Float> convertToFloat(ArrayList<Integer> intList) {
        ArrayList<Float> floatList = new ArrayList<>();
        for (Integer i : intList) {
            floatList.add(i.floatValue());
        }
        return floatList;
    }

    private void calculateStats(ArrayList<Float> temps) {
        if (temps == null || temps.isEmpty()) return;
        float sum = 0;
        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        for (float temp : temps) {
            sum += temp;
            if (temp > max) max = temp;
            if (temp < min) min = temp;
        }

        float avg = sum / temps.size();


        tvAvgTemp.setText(Math.round(avg) + "°");
        tvMaxTemp.setText(Math.round(max) + "°");
        tvMinTemp.setText(Math.round(min) + "°");
    }

    private ArrayList<Integer> generateHumidityData(int count) {
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(70 + (int)(Math.random() * 25)); // 70-95%
        }
        return data;
    }
}
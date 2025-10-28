package com.example.myweatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class HumidityChartView extends View {

    private ArrayList<String> days;
    private ArrayList<Integer> humidityValues;

    private Paint barPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private Paint labelPaint;

    public HumidityChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(0xFF4FC3F7);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(28f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        gridPaint = new Paint();
        gridPaint.setColor(0x33FFFFFF);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);

        labelPaint = new Paint();
        labelPaint.setColor(0xFFAAAAAA);
        labelPaint.setTextSize(24f);
        labelPaint.setAntiAlias(true);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(ArrayList<String> days, ArrayList<Integer> humidityValues) {
        this.days = days;
        this.humidityValues = humidityValues;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (days == null || humidityValues == null || days.isEmpty()) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int paddingTop = 60;
        int paddingBottom = 60;
        int paddingLeft = 40;
        int paddingRight = 40;

        int gridLines = 4;
        for (int i = 0; i <= gridLines; i++) {
            float y = paddingTop + (height - paddingTop - paddingBottom) * i / gridLines;
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, gridPaint);
        }

        int dataCount = days.size();
        float chartWidth = width - paddingLeft - paddingRight;
        float barWidth = chartWidth / dataCount * 0.7f; // 70% để có khoảng cách
        float stepX = chartWidth / dataCount;

        for (int i = 0; i < dataCount; i++) {
            float x = paddingLeft + stepX * i + stepX / 2;
            int humidity = humidityValues.get(i);


            float barHeight = (humidity / 100f) * (height - paddingTop - paddingBottom);
            float barTop = height - paddingBottom - barHeight;

            canvas.drawRect(
                    x - barWidth / 2,
                    barTop,
                    x + barWidth / 2,
                    height - paddingBottom,
                    barPaint
            );

            canvas.drawText(humidity + "%", x, barTop - 10, textPaint);

            String dayText = formatDayLabel(days.get(i));
            String[] lines = dayText.split("\n");
            float yLabel = height - paddingBottom + 30;

            for (String line : lines) {
                canvas.drawText(line, x, yLabel, labelPaint);
                yLabel += 26;
            }
        }
    }

    private String formatDayLabel(String day) {
        if (day.equals("Hôm nay")) {
            return "Hôm nay";
        } else if (day.equals("Chủ Nhật")) {
            return "CN";
        } else if (day.startsWith("Thứ")) {
            String[] parts = day.split(" ");
            if (parts.length == 2) {
                return "T" + parts[1].charAt(0);
            }
        }
        return day;
    }
}
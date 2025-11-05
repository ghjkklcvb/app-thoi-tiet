package com.example.myweatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class TemperatureChartView extends View {

    private ArrayList<String> days;
    private ArrayList<Float> temperatures;

    private Paint linePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private Paint labelPaint;

    public TemperatureChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(0xFFFF6B6B);
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setColor(0xFFFF6B6B);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFFFFFFFF);
        textPaint.setTextSize(32f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        gridPaint = new Paint();
        gridPaint.setColor(0x33FFFFFF);
        gridPaint.setStrokeWidth(2f);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);

        labelPaint = new Paint();
        labelPaint.setColor(0xFFAAAAAA);
        labelPaint.setTextSize(26f);
        labelPaint.setAntiAlias(true);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(ArrayList<String> days, ArrayList<Float> temperatures) {
        this.days = days;
        this.temperatures = temperatures;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (days == null || temperatures == null || days.isEmpty()) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int paddingTop = 60;
        int paddingBottom = 80;
        int paddingLeft = 50;
        int paddingRight = 50;

        float maxTemp = Float.MIN_VALUE;
        float minTemp = Float.MAX_VALUE;
        for (float temp : temperatures) {
            if (temp > maxTemp) maxTemp = temp;
            if (temp < minTemp) minTemp = temp;
        }

        float tempRange = maxTemp - minTemp;
        if (tempRange < 5) {
            float center = (maxTemp + minTemp) / 2;
            maxTemp = center + 3;
            minTemp = center - 3;
            tempRange = 6;
        } else {
            maxTemp += tempRange * 0.1f;
            minTemp -= tempRange * 0.1f;
            tempRange = maxTemp - minTemp;
        }

        int gridLines = 5;
        Paint axisPaint = new Paint();
        axisPaint.setColor(0xFF888888);
        axisPaint.setTextSize(22f);
        axisPaint.setAntiAlias(true);
        axisPaint.setTextAlign(Paint.Align.RIGHT);

        for (int i = 0; i <= gridLines; i++) {
            float y = paddingTop + (height - paddingTop - paddingBottom) * i / gridLines;
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, gridPaint);


            float tempAtLine = maxTemp - (tempRange * i / gridLines);
            canvas.drawText(Math.round(tempAtLine) + "°", paddingLeft - 10, y + 8, axisPaint);
        }

        int dataCount = days.size();
        float chartWidth = width - paddingLeft - paddingRight;
        float stepX = chartWidth / (dataCount - 1f);

        Path path = new Path();
        boolean firstPoint = true;

        for (int i = 0; i < dataCount; i++) {
            float x = paddingLeft + i * stepX;
            float normalizedTemp = (temperatures.get(i) - minTemp) / tempRange;
            float y = height - paddingBottom - normalizedTemp * (height - paddingTop - paddingBottom);

            if (firstPoint) {
                path.moveTo(x, y);
                firstPoint = false;
            } else {
                path.lineTo(x, y);
            }

            canvas.drawCircle(x, y, 12f, pointPaint);

            textPaint.setTextSize(30f);
            textPaint.setColor(0xFFFFFFFF);
            canvas.drawText(Math.round(temperatures.get(i)) + "°", x, y - 25, textPaint);


            String dayText = formatDayLabel(days.get(i));
            String[] lines = dayText.split("\n");
            float yLabel = height - paddingBottom + 30;

            for (String line : lines) {
                canvas.drawText(line, x, yLabel, labelPaint);
                yLabel += 28;
            }
        }

        canvas.drawPath(path, linePaint);
    }
    private String formatDayLabel(String day) {
        if (day.equals("Hôm nay")) {
            return "Hôm nay";
        } else if (day.equals("Chủ Nhật")) {
            return "Chủ\nNhật";
        } else if (day.startsWith("Thứ")) {
            String[] parts = day.split(" ");
            if (parts.length == 2) {
                return parts[0] + "\n" + parts[1];
            }
        }
        return day;
    }
}
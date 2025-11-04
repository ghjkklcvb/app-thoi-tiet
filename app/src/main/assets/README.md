# Weather Background Images - OpenWeatherMap API Complete

Thêm các file ảnh động GIF sau vào thư mục này theo đầy đủ các trạng thái thời tiết của OpenWeatherMap API:

## 📋 Danh sách file cần thiết (16 files):

### ☀️ Clear Sky (01d/01n) - Trời quang:
- `clear_day.gif` - Trời quang ban ngày (nắng đẹp, bầu trời xanh)
- `clear_night.gif` - Trời quang ban đêm (trăng sáng, sao)

### ☁️ Few Clouds (02d/02n) - Ít mây (11-25%):
- `few_clouds_day.gif` - Ít mây ban ngày (nắng + vài đám mây)
- `few_clouds_night.gif` - Ít mây ban đêm (trăng + vài đám mây)

### ☁️ Scattered Clouds (03d/03n) - Mây rải rác (25-50%):
- `scattered_clouds_day.gif` - Mây rải rác ban ngày
- `scattered_clouds_night.gif` - Mây rải rác ban đêm

### ☁️ Broken Clouds (04d/04n) - Mây dày (51-84%):
- `broken_clouds_day.gif` - Mây dày ban ngày
- `broken_clouds_night.gif` - Mây dày ban đêm

### 🌧️ Shower Rain/Rain (09d/10d, 09n/10n) - Mưa:
- `drizzle_day.gif` - Mưa ban ngày (mưa rào, mưa phùn)
- `drizzle_night.gif` - Mưa ban đêm

### ⛈️ Thunderstorm (11d/11n) - Sấm sét:
- `thunderstorm_day.gif` - Sấm sét ban ngày (chớp, sét)
- `thunderstorm_night.gif` - Sấm sét ban đêm

### 🌨️ Snow (13d/13n) - Tuyết:
- `snow_day.gif` - Tuyết ban ngày (tuyết rơi)
- `snow_night.gif` - Tuyết ban đêm

### 🌫️ Mist/Atmosphere (50d/50n) - Sương mù:
- `mist_day.gif` - Sương mù ban ngày (mù, khói, bụi)
- `mist_night.gif` - Sương mù ban đêm

## 🎨 Gợi ý màu sắc và phong cách:

### Ban ngày:
- **Clear**: Vàng cam, xanh da trời sáng
- **Few clouds**: Xanh da trời + mây trắng
- **Scattered**: Xám nhạt, xanh nhạt
- **Broken**: Xám, xanh xám
- **Rain**: Xanh dương, xám xanh
- **Thunder**: Tím đậm, xám đen
- **Snow**: Trắng, xám nhạt
- **Mist**: Xám nhạt, trắng đục

### Ban đêm:
- **Clear**: Xanh đậm, tím đậm
- **Clouds**: Xám đậm, đen
- **Rain**: Xanh đậm, đen xanh
- **Thunder**: Đen, tím đen
- **Snow**: Xám đậm, xanh đậm
- **Mist**: Xám đậm, đen nhạt

## 🔍 Nguồn tìm ảnh:
- **Unsplash**: unsplash.com (tìm "weather animation", "rain gif", etc.)
- **Pexels**: pexels.com (video có thể convert sang GIF)
- **Giphy**: giphy.com (tìm "weather", "rain", "storm")
- **Pixabay**: pixabay.com (có cả video và GIF)

## 📐 Thông số kỹ thuật:
- **Kích thước**: 1080x1920 (9:16) hoặc 720x1280
- **Định dạng**: GIF (ưu tiên) hoặc WebP
- **Dung lượng**: < 5MB mỗi file
- **FPS**: 15-30 fps
- **Loop**: Infinite (lặp vô hạn)

## ⚙️ Lưu ý kỹ thuật:
- Đặt tên file chính xác như danh sách trên
- Ảnh sẽ hiển thị với alpha 0.8 (hơi trong suốt)
- ScaleType: centerCrop (tự động cắt vừa màn hình)
- Glide sẽ tự động cache và optimize
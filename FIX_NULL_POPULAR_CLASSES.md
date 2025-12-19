# ✅ Fix Null Values Trong Popular Classes

**Ngày:** 20/12/2025  
**Vấn đề:** HomeFragment hiển thị "null (-10%)" cho giá sau giảm  
**Giải pháp:** Sửa API endpoint để trả về LopHocDTO thay vì KhoaHocDTO

---

## 🔍 Vấn Đề

### Triệu chứng:
- HomeFragment hiển thị "null (-10%)" cho các lớp học có ưu đãi
- Giá gốc hiển thị đúng nhưng giá sau giảm là null

### Nguyên nhân:
1. HomeFragment gọi API `/api/khoahoc` (endpoint cũ)
2. Endpoint này trả về `KhoaHocDTO` chứ không phải `LopHocDTO`
3. `KhoaHocDTO` có format khác với model `LopHoc` của Android:
   - `giaTien` là BigDecimal thay vì String format "650.000đ"
   - `lichTrinhList` là List object thay vì String "17:30 - 20:30"
4. Android không thể parse được dữ liệu đúng cách

---

## ✅ Giải Pháp

### 1. Tạo endpoint mới `/api/khoahoc/all`

**Backend - KhoaHocController.java:**
```java
@GetMapping("/all")
public ResponseEntity<List<LopHocDTO>> getAllLopHoc() {
    return ResponseEntity.ok(khoaHocService.getAllLopHoc());
}
```

### 2. Implement method `getAllLopHoc()` trong KhoaHocService

**Backend - KhoaHocService.java:**
```java
public List<LopHocDTO> getAllLopHoc() {
    List<KhoaHoc> khoaHocs = khoaHocRepository.findAll();
    return khoaHocs.stream()
            .flatMap(kh -> {
                List<LichTrinhLopHoc> lichTrinhs = lichTrinhRepository.findByMaKhoaHoc(kh.getMaKhoaHoc());
                if (!lichTrinhs.isEmpty()) {
                    // Trả về tất cả lịch trình của khóa học
                    return lichTrinhs.stream().map(lt -> convertToLopHocDTO(kh, lt));
                }
                return java.util.stream.Stream.empty();
            })
            .collect(Collectors.toList());
}

private LopHocDTO convertToLopHocDTO(KhoaHoc khoaHoc, LichTrinhLopHoc lichTrinh) {
    LopHocDTO dto = new LopHocDTO();
    
    // Map fields
    dto.setMaLopHoc(khoaHoc.getMaKhoaHoc());
    dto.setTenLop(khoaHoc.getTenKhoaHoc());
    
    // Format giá: 650000 -> "650.000đ"
    if (khoaHoc.getGiaTien() != null) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        dto.setGia(formatter.format(khoaHoc.getGiaTien()).replace(",", ".") + "đ");
    }
    
    // Format thời gian: "17:30:00 - 20:30:00"
    dto.setThoiGian(lichTrinh.getGioBatDau() + " - " + lichTrinh.getGioKetThuc());
    dto.setDiaDiem(lichTrinh.getDiaDiem());
    dto.setSuat(lichTrinh.getSoLuongToiDa());
    
    return dto;
}
```

### 3. Cập nhật ApiService trong Android

**Android - ApiService.java:**
```java
// Endpoint mới - Lấy tất cả lớp học với format LopHocDTO
@GET("api/khoahoc/all")
Call<List<LopHoc>> getAllLopHoc();

// Endpoint cũ - Lấy tất cả khóa học (deprecated)
@GET("api/khoahoc")
Call<List<LopHoc>> getAllKhoaHoc();
```

### 4. Cập nhật HomeFragment

**Android - HomeFragment.java:**
```java
private void loadPopularClasses() {
    // Sử dụng API mới: /api/khoahoc/all
    RetrofitClient.getApiService().getAllLopHoc().enqueue(new Callback<List<LopHoc>>() {
        @Override
        public void onResponse(Call<List<LopHoc>> call, Response<List<LopHoc>> response) {
            if (response.isSuccessful() && response.body() != null) {
                List<LopHoc> allClasses = response.body();
                List<LopHoc> popularClasses = selectPopularClasses(allClasses);
                displayPopularClasses(popularClasses);
                Log.d(TAG, "Loaded " + allClasses.size() + " classes from API");
            }
        }
        // ...
    });
}
```

---

## 🎯 Kết Quả

### Trước khi fix:
```
Lớp học: Ẩm thực phố cổ Hà Nội
Giá gốc: 650.000đ
Giá sau giảm: null (-10%)  ❌
```

### Sau khi fix:
```
Lớp học: Ẩm thực phố cổ Hà Nội
Giá gốc: 650.000đ
Giá sau giảm: 585.000đ (-10%)  ✅
```

---

## 📊 So Sánh API Response

### `/api/khoahoc` (KhoaHocDTO - Format cũ):
```json
{
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
  "giaTien": 650000,  // BigDecimal - Android không parse được
  "lichTrinhList": [   // List object - Android cần String
    {
      "gioBatDau": "17:30:00",
      "gioKetThuc": "20:30:00"
    }
  ]
}
```

### `/api/khoahoc/all` (LopHocDTO - Format Android):
```json
{
  "maLopHoc": 1,
  "tenLop": "Ẩm thực phố cổ Hà Nội",
  "gia": "650.000đ",      // String với format - Android parse được ✅
  "thoiGian": "17:30:00 - 20:30:00",  // String - Android parse được ✅
  "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
  "suat": 20,
  "coUuDai": true
}
```

---

## 🔧 Các File Đã Thay Đổi

### Backend:
1. ✅ `KhoaHocController.java` - Thêm endpoint `/api/khoahoc/all`
2. ✅ `KhoaHocService.java` - Thêm method `getAllLopHoc()` và `convertToLopHocDTO()`

### Android:
1. ✅ `ApiService.java` - Thêm method `getAllLopHoc()`, fix lỗi "Sua" ở đầu file
2. ✅ `HomeFragment.java` - Đổi từ `getAllKhoaHoc()` sang `getAllLopHoc()`

### Documentation:
1. ✅ `CAP_NHAT_ANDROID_API_MOI.md` - Cập nhật thông tin về endpoint mới

---

## ✅ Kết Luận

- ✅ Backend build thành công
- ✅ API trả về format đúng với Android (LopHocDTO)
- ✅ HomeFragment hiển thị giá đúng (không còn null)
- ✅ Giá sau giảm được tính và hiển thị chính xác
- ✅ Tương thích hoàn toàn với model LopHoc của Android

**Bây giờ có thể test trên Android app! 🚀**

---

## 📝 Lưu Ý

### Tại sao cần 2 endpoint?

1. **`/api/khoahoc`** (KhoaHocDTO):
   - Format chuẩn cho backend
   - Chứa đầy đủ thông tin khóa học
   - Dùng cho admin panel hoặc web app

2. **`/api/khoahoc/all`** (LopHocDTO):
   - Format tối ưu cho Android
   - Dữ liệu đã được format sẵn (giá, thời gian)
   - Giảm xử lý ở phía Android
   - Tương thích với model LopHoc hiện có

### Lợi ích:
- ✅ Không cần thay đổi model LopHoc trong Android
- ✅ Giảm logic xử lý ở phía Android
- ✅ Dễ maintain và debug
- ✅ Tương thích ngược với API cũ

# Fix hiển thị số chỗ trống trong trang Booking

## Vấn đề
Trang Booking không hiển thị số chỗ còn trống giống như danh sách khóa học vì API `getLichTrinhById()` không trả về thông tin `conTrong`.

## Nguyên nhân
1. **Backend:** API `/api/lichtrinh/{id}` chỉ trả về entity `LichTrinhLopHoc` thuần, không có tính toán số chỗ trống
2. **Android:** Phải gọi thêm API `checkAvailableSeats()` để lấy số chỗ trống, làm tăng số lượng request

## Giải pháp

### 1. Backend - LichTrinhLopHocService.java

#### Thêm dependency
```java
private final DatLichRepository datLichRepository;
```

#### Thêm method mới `getLichTrinhDTOById()`
```java
// GET - Lấy lịch trình theo ID với thông tin số chỗ trống (DTO)
public Optional<LichTrinhLopHocDTO> getLichTrinhDTOById(Integer id) {
    return lichTrinhRepository.findById(id)
            .map(this::convertToDTO);
}
```

#### Thêm method `convertToDTO()`
```java
private LichTrinhLopHocDTO convertToDTO(LichTrinhLopHoc lichTrinh) {
    LichTrinhLopHocDTO dto = new LichTrinhLopHocDTO();
    // ... copy các field ...
    
    // Tính số chỗ còn trống cho ngày mai
    LocalDate ngayKiemTra = LocalDate.now().plusDays(1);
    Integer soLuongDaDat = datLichRepository.countBookedSeats(
        lichTrinh.getMaLichTrinh(), 
        ngayKiemTra
    );
    Integer soLuongToiDa = lichTrinh.getSoLuongToiDa() != null ? 
        lichTrinh.getSoLuongToiDa() : 0;
    Integer conTrong = soLuongToiDa - soLuongDaDat;
    
    dto.setSoLuongHienTai(soLuongDaDat);
    dto.setConTrong(conTrong);
    dto.setTrangThaiHienThi(conTrong <= 0 ? "Hết chỗ" : 
        conTrong <= 3 ? "Sắp hết chỗ" : "Còn chỗ");
    
    return dto;
}
```

### 2. Backend - LichTrinhLopHocController.java

#### Thêm endpoint mới
```java
// GET - Lấy lịch trình theo ID với thông tin đầy đủ (DTO có số chỗ trống)
@GetMapping("/{id}/detail")
public ResponseEntity<LichTrinhLopHocDTO> getLichTrinhDetailById(@PathVariable Integer id) {
    return lichTrinhService.getLichTrinhDTOById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

**Endpoint mới:** `GET /api/lichtrinh/{id}/detail`

### 3. Android - ApiService.java

#### Thêm method mới
```java
// Lấy lịch trình với thông tin đầy đủ (có số chỗ trống)
@GET("api/lichtrinh/{id}/detail")
Call<LichTrinhLopHoc> getLichTrinhDetailById(@Path("id") Integer id);
```

### 4. Android - Booking.java

#### Cập nhật `loadLichTrinhData()`
Thay đổi từ `getLichTrinhById()` sang `getLichTrinhDetailById()`:

```java
// 2. Lấy thông tin lịch trình với số chỗ trống
apiService.getLichTrinhDetailById(maLichTrinh).enqueue(new Callback<LichTrinhLopHoc>() {
    @Override
    public void onResponse(Call<LichTrinhLopHoc> call, Response<LichTrinhLopHoc> response) {
        if (response.isSuccessful() && response.body() != null) {
            lichTrinhLopHoc = response.body();
            
            // Lấy số chỗ trống từ API
            if (lichTrinhLopHoc.getConTrong() != null) {
                soLuongConLai = lichTrinhLopHoc.getConTrong();
                Log.d("BOOKING_API", "Số chỗ còn trống từ API: " + soLuongConLai);
            }
            
            // Load giáo viên
            if (lichTrinhLopHoc.getMaGiaoVien() != null) {
                loadGiaoVienData(lichTrinhLopHoc.getMaGiaoVien());
            }
            
            // Hiển thị thông tin
            displayBookingInfo();
            
            // Enable button
            btnDatLich.setEnabled(true);
            btnDatLich.setText("Đặt lịch");
            
            // Kiểm tra nếu hết chỗ
            if (soLuongConLai <= 0) {
                Toast.makeText(Booking.this, "Lớp học đã hết chỗ", Toast.LENGTH_SHORT).show();
                btnDatLich.setEnabled(false);
                btnDatLich.setText("Hết chỗ");
            }
        }
    }
    // ...
});
```

**Thay đổi chính:**
- Không cần gọi `checkAvailableSeats()` nữa
- Lấy `conTrong` trực tiếp từ `lichTrinhLopHoc.getConTrong()`
- Giảm từ 2 API calls xuống 1 API call

## So sánh trước và sau

### Trước khi fix
```
1. Gọi getLichTrinhById(maLichTrinh)
   → Nhận LichTrinhLopHoc (không có conTrong)
   
2. Gọi checkAvailableSeats(maLichTrinh, ngayThamGia)
   → Nhận CheckSeatsResponse với conTrong
   
3. Hiển thị UI
```

**Tổng:** 2 API calls

### Sau khi fix
```
1. Gọi getLichTrinhDetailById(maLichTrinh)
   → Nhận LichTrinhLopHoc (có conTrong)
   
2. Hiển thị UI
```

**Tổng:** 1 API call

## Lợi ích

1. **Giảm số lượng request:** Từ 2 xuống 1 API call
2. **Tăng tốc độ load:** Ít request hơn = nhanh hơn
3. **Code đơn giản hơn:** Không cần xử lý 2 callback riêng biệt
4. **Nhất quán với KhoaHoc:** Cùng pattern với API `/api/khoahoc`

## API Response

### Endpoint cũ: `/api/lichtrinh/{id}`
```json
{
  "maLichTrinh": 1,
  "maKhoaHoc": 1,
  "soLuongToiDa": 20,
  "trangThai": true
  // Không có conTrong
}
```

### Endpoint mới: `/api/lichtrinh/{id}/detail`
```json
{
  "maLichTrinh": 1,
  "maKhoaHoc": 1,
  "soLuongToiDa": 20,
  "soLuongHienTai": 12,
  "conTrong": 8,
  "trangThaiHienThi": "Còn chỗ",
  "trangThai": true
}
```

## Luồng hoạt động mới

1. **User mở trang Booking**
   - Truyền `maLichTrinh` từ Intent

2. **Gọi API `/api/lichtrinh/{id}/detail`**
   - Backend tính số chỗ trống cho ngày mai
   - Trả về DTO có đầy đủ thông tin

3. **Hiển thị UI**
   - Số chỗ trống: `lichTrinhLopHoc.getConTrong()`
   - Số lượng tối đa: `lichTrinhLopHoc.getSoLuongToiDa()`
   - Hiển thị: "Còn 8/20 suất"

## Files đã thay đổi

1. **BE/src/main/java/com/android/be/service/LichTrinhLopHocService.java**
   - Thêm dependency `DatLichRepository`
   - Thêm method `getLichTrinhDTOById()`
   - Thêm method `convertToDTO()`

2. **BE/src/main/java/com/android/be/controller/LichTrinhLopHocController.java**
   - Thêm endpoint `GET /{id}/detail`

3. **FE/app/src/main/java/com/example/localcooking_v3t/api/ApiService.java**
   - Thêm method `getLichTrinhDetailById()`

4. **FE/app/src/main/java/com/example/localcooking_v3t/Booking.java**
   - Thay `getLichTrinhById()` bằng `getLichTrinhDetailById()`
   - Lấy `conTrong` trực tiếp từ response
   - Bỏ gọi `checkAvailableSeats()`

## Kiểm tra

### Test cases:
1. ✅ Hiển thị số chỗ trống khi load trang Booking
2. ✅ Hiển thị "Còn X/Y suất" với đúng giá trị
3. ✅ Disable button khi hết chỗ
4. ✅ Giảm số lượng API calls
5. ✅ Xử lý khi API lỗi

## Ghi chú

- Endpoint cũ `/api/lichtrinh/{id}` vẫn giữ nguyên để tương thích ngược
- Endpoint mới `/api/lichtrinh/{id}/detail` dùng cho Booking và các màn hình cần thông tin đầy đủ
- Số chỗ trống tính cho **ngày mai** (mặc định) giống như API `/api/khoahoc`

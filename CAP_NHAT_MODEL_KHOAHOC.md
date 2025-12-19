# ✅ Cập Nhật Model KhoaHoc - Hoàn Chỉnh

**Ngày:** 20/12/2025  
**Trạng thái:** ✅ Hoàn thành

---

## 📋 Tổng Quan

Đã cập nhật toàn bộ hệ thống để sử dụng model `KhoaHoc` mới thay vì `LopHoc` cũ.

---

## 🎯 Cấu Trúc Model Mới

### 1. **KhoaHoc.java** (Android)
```java
public class KhoaHoc {
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private Double giaTien;  // Backend: BigDecimal → Android: Double
    private String hinhAnh;
    private Integer soLuongDanhGia;
    private Float saoTrungBinh;
    private Boolean coUuDai;
    private String ngayTao;
    
    // Danh sách lịch trình
    private List<LichTrinhLopHoc> lichTrinhList;
    
    // Helper methods
    public String getGiaFormatted();  // "650.000đ"
    public String getGiaSauGiam();    // "585.000đ" (nếu có ưu đãi)
    public String getDiaPhuong();     // Lấy từ lichTrinhList
    public int getHinhAnhResId(Context);
    public String getRatingText();
}
```

### 2. **LichTrinhLopHoc.java** (Android)
```java
public class LichTrinhLopHoc {
    private Integer maLichTrinh;
    private Integer maKhoaHoc;
    private Integer maGiaoVien;
    private String thuTrongTuan;  // "2,3,4,5,6,7,CN"
    private String gioBatDau;     // "17:30:00"
    private String gioKetThuc;    // "20:30:00"
    private String diaDiem;
    private Integer soLuongToiDa;
    private Boolean trangThai;
    
    // Helper methods
    public String getThoiGianFormatted();  // "17:30 - 20:30"
    public String getDiaPhuong();          // "Hà Nội"
    public boolean isHocVaoThu(String);
    public String getThuHocFormatted();    // "T2, T3, T4, T5, T6, T7, CN"
}
```

---

## 🔧 Backend API

### KhoaHocDTO (Backend)
```java
public class KhoaHocDTO {
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private BigDecimal giaTien;
    private String hinhAnh;
    private Integer soLuongDanhGia;
    private Float saoTrungBinh;
    private Boolean coUuDai;
    
    // Danh sách lịch trình (đầy đủ)
    private List<LichTrinhLopHocDTO> lichTrinhList;
    
    // Danh sách danh mục món ăn (đầy đủ)
    private List<DanhMucMonAnDTO> danhMucMonAnList;
}
```

### API Endpoints
```
GET /api/khoahoc                    → List<KhoaHocDTO>
GET /api/khoahoc/{id}               → KhoaHocDTO
GET /api/khoahoc/search?diaDiem=... → List<KhoaHocDTO>
```

---

## 📱 Android - Các File Đã Cập Nhật

### 1. **ApiService.java**
```java
// API mới - Trả về KhoaHoc
@GET("api/khoahoc")
Call<List<KhoaHoc>> getAllKhoaHocNew();

@GET("api/khoahoc/{id}")
Call<KhoaHoc> getKhoaHocByIdNew(@Path("id") Integer id);

// API lịch trình
@GET("api/lichtrinh")
Call<List<LichTrinhLopHoc>> getAllLichTrinh();

@GET("api/lichtrinh/khoahoc/{maKhoaHoc}")
Call<List<LichTrinhLopHoc>> getLichTrinhByKhoaHoc(@Path("maKhoaHoc") Integer maKhoaHoc);
```

### 2. **HomeFragment.java**
**Thay đổi:**
- ✅ Gọi `getAllKhoaHocNew()` thay vì `getAllLopHoc()`
- ✅ Sử dụng `khoaHoc.getTenKhoaHoc()` thay vì `lopHoc.getTenLop()`
- ✅ Sử dụng `khoaHoc.getGiaTien()` thay vì `lopHoc.getGia()`
- ✅ Lấy địa phương từ `lichTrinhList` thay vì `getDiaPhuong()`

**Code mới:**
```java
private void loadPopularClasses() {
    RetrofitClient.getApiService().getAllKhoaHocNew().enqueue(new Callback<List<KhoaHoc>>() {
        @Override
        public void onResponse(Call<List<KhoaHoc>> call, Response<List<KhoaHoc>> response) {
            if (response.isSuccessful() && response.body() != null) {
                List<KhoaHoc> allClasses = response.body();
                // Log để debug
                for (KhoaHoc kh : allClasses) {
                    Log.d(TAG, "KhoaHoc: " + kh.getTenKhoaHoc() + 
                          ", Gia: " + kh.getGiaTien() + 
                          ", UuDai: " + kh.getCoUuDai() +
                          ", LichTrinh: " + (kh.getLichTrinhList() != null ? kh.getLichTrinhList().size() : 0));
                }
                displayPopularClasses(selectPopularClasses(allClasses));
            }
        }
    });
}

private View createClassCard(KhoaHoc khoaHoc, int index) {
    // Tên khóa học
    tvName.setText(khoaHoc.getTenKhoaHoc());
    
    // Hình ảnh
    imageView.setImageResource(khoaHoc.getHinhAnhResId(requireContext()));
    
    // Giá tiền
    if (khoaHoc.getCoUuDai() != null && khoaHoc.getCoUuDai()) {
        // Giá gốc gạch ngang
        tvOriginalPrice.setText(khoaHoc.getGiaFormatted());
        
        // Giá sau giảm 10%
        double giaSauGiam = khoaHoc.getGiaTien() * 0.9;
        String giaSauGiamText = String.format("%,.0fđ (-10%%)", giaSauGiam).replace(",", ".");
        tvDiscountPrice.setText(giaSauGiamText);
    } else {
        tvPrice.setText(khoaHoc.getGiaFormatted());
    }
}
```

---

## 🗑️ Đã Xóa

### Backend:
- ❌ `LopHocDTO.java` - Không tồn tại
- ❌ `LopHocService.java` - Đã xóa
- ❌ `LopHocController.java` - Đã xóa

### Android:
- ❌ `LopHoc.java` - Đã thay bằng `KhoaHoc.java`
- ❌ Các method alias (`getMaLopHoc()`, `getTenLop()`, `getGia()`) - Đã xóa

---

## ✅ Kết Quả

### Backend:
- ✅ Chỉ sử dụng `KhoaHocDTO`
- ✅ API trả về đầy đủ `lichTrinhList` và `danhMucMonAnList`
- ✅ Không còn `LopHocDTO` nào

### Android:
- ✅ Sử dụng model `KhoaHoc` mới
- ✅ Có `LichTrinhLopHoc` để lưu thông tin lịch trình
- ✅ Code sạch sẽ, không có alias methods
- ✅ Hiển thị đúng:
  - Tên khóa học: `tenKhoaHoc`
  - Giá tiền: `giaTien` (format: "650.000đ")
  - Giá ưu đãi: Giảm 10% (format: "585.000đ (-10%)")

---

## 🧪 Test

### 1. Chạy Backend
```bash
cd BE
./gradlew bootRun
```

### 2. Test API
```bash
# Lấy tất cả khóa học
curl http://localhost:8080/api/khoahoc

# Kiểm tra response có:
# - lichTrinhList: không null
# - danhMucMonAnList: không null
```

### 3. Chạy Android App
- Mở HomeFragment
- Kiểm tra Logcat:
  ```
  KhoaHoc: Ẩm thực phố cổ Hà Nội, Gia: 650000.0, UuDai: true, LichTrinh: 1
  ```
- Kiểm tra UI:
  - Tên khóa học hiển thị đúng
  - Giá tiền hiển thị đúng format
  - Giá ưu đãi gạch ngang và hiển thị giá giảm

---

## 📝 Lưu Ý

### Quan hệ dữ liệu:
- 1 `KhoaHoc` → N `LichTrinhLopHoc`
- 1 `KhoaHoc` → N `DanhMucMonAn` → N `MonAn`

### Format giá:
- Backend: `BigDecimal` (650000)
- Android: `Double` (650000.0)
- Display: `String` ("650.000đ")

### Ưu đãi:
- Nếu `coUuDai = true`: Giảm 10%
- Hiển thị: ~~650.000đ~~ **585.000đ (-10%)**

---

**Hoàn thành! Hệ thống đã sử dụng model KhoaHoc mới hoàn toàn! 🎉**

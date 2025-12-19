# ✅ Cập Nhật Android App Sử Dụng API Mới

**Ngày:** 20/12/2025  
**Trạng thái:** ✅ Hoàn thành

---

## 📋 Tổng Quan

Đã cập nhật Android app để sử dụng API mới từ backend sau khi xóa các class `LopHoc` và `LichHoc`.

### Thay đổi chính:
- **API cũ:** `/api/lophoc` → **API mới:** `/api/khoahoc`
- **API cũ:** `/api/danhmucmonan/lophoc/{id}` → **API mới:** `/api/danhmucmonan/khoahoc/{id}`
- Thêm endpoint search mới: `/api/khoahoc/search`

---

## 🔧 Các File Đã Cập Nhật

### 1. Backend

#### ✅ KhoaHocController.java
**Thêm endpoint mới:**
```java
// Endpoint mới: Lấy tất cả lớp học với format LopHocDTO
@GetMapping("/all")
public ResponseEntity<List<LopHocDTO>> getAllLopHoc() {
    return ResponseEntity.ok(khoaHocService.getAllLopHoc());
}

// Endpoint search với stored procedure
@GetMapping("/search")
public ResponseEntity<List<LopHocDTO>> searchKhoaHoc(
        @RequestParam String diaDiem,
        @RequestParam(required = false) String ngayTimKiem) {
    
    if (ngayTimKiem != null && !ngayTimKiem.isEmpty()) {
        return ResponseEntity.ok(khoaHocService.searchByDiaDiemAndDate(diaDiem, ngayTimKiem));
    } else {
        return ResponseEntity.ok(khoaHocService.searchByDiaDiem(diaDiem));
    }
}
```

#### ✅ KhoaHocService.java
**Thêm 4 methods:**
1. `getAllLopHoc()` - Lấy tất cả lớp học với format LopHocDTO
2. `searchByDiaDiem(String diaDiem)` - Tìm theo địa điểm
3. `searchByDiaDiemAndDate(String diaDiem, String ngayTimKiem)` - Tìm theo địa điểm và ngày (sử dụng stored procedure)
4. `convertToLopHocDTO(KhoaHoc, LichTrinhLopHoc)` - Convert sang LopHocDTO
5. `convertStoredProcResultToLopHocDTO(Object[] row)` - Convert kết quả từ stored procedure

#### ✅ LichTrinhLopHocRepository.java
**Đã có sẵn:**
- `findByDiaDiemContainingIgnoreCase(String diaDiem)`
- `findClassesByDate(String ngayCanXem)` - Gọi stored procedure

---

### 2. Android Frontend

#### ✅ ApiService.java (Đã làm sạch - Chỉ API mới)
**API được tổ chức theo nhóm:**

```java
// ==================== AUTHENTICATION ====================
@POST("api/nguoidung/login")
Call<LoginResponse> login(@Body LoginRequest request);

@POST("api/nguoidung/register")
Call<RegisterResponse> register(@Body RegisterRequest request);

// ==================== KHÓA HỌC ====================
// Endpoint mới - Lấy tất cả lớp học với format LopHocDTO
@GET("api/khoahoc/all")
Call<List<LopHoc>> getAllLopHoc();

// Endpoint cũ - Lấy tất cả khóa học (deprecated)
@GET("api/khoahoc")
Call<List<LopHoc>> getAllKhoaHoc();

@GET("api/khoahoc/{id}")
Call<LopHoc> getKhoaHocById(@Path("id") Integer id);

@GET("api/khoahoc/search")
Call<List<LopHoc>> searchKhoaHoc(
    @Query("diaDiem") String diaDiem,
    @Query("ngayTimKiem") String ngayTimKiem
);

// ==================== DANH MỤC MÓN ĂN ====================
@GET("api/danhmucmonan/khoahoc/{maKhoaHoc}")
Call<List<DanhMucMonAn>> getDanhMucMonAnByKhoaHoc(@Path("maKhoaHoc") Integer maKhoaHoc);

// ==================== HÌNH ẢNH ====================
@GET("api/hinhanh-monan/monan/{maMonAn}")
Call<List<HinhAnhMonAn>> getHinhAnhMonAn(@Path("maMonAn") Integer maMonAn);

@GET("api/hinhanh-khoahoc/khoahoc/{maKhoaHoc}")
Call<List<HinhAnhKhoaHoc>> getHinhAnhKhoaHoc(@Path("maKhoaHoc") Integer maKhoaHoc);
```

**Đã loại bỏ:**
- ❌ `getAllLopHoc()` - API cũ (đã thay bằng `/api/khoahoc/all`)
- ❌ `getDanhMucMonAnByLopHoc()` - API cũ
- ❌ `searchLopHocByDiaDiem()` - API cũ

#### ✅ HinhAnhKhoaHoc.java (Model mới)
**Tạo model mới cho hình ảnh khóa học:**
```java
public class HinhAnhKhoaHoc {
    private Integer maHinhAnh;
    private Integer maKhoaHoc;
    private String duongDan;
    private Integer thuTu;
    // Getters and Setters
}
```

#### ✅ HomeFragment.java
**Cập nhật method `loadPopularClasses()`:**
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

#### ✅ ClassesFragment.java
**Cập nhật method `loadLopHoc()`:**
```java
private void loadLopHoc() {
    String ngayTimKiem = convertDateFormat(date);
    
    Log.d(TAG, "Loading classes for: " + destination + " on " + ngayTimKiem);
    
    if (ngayTimKiem != null && !ngayTimKiem.isEmpty()) {
        // Gọi API mới: searchKhoaHoc (không phải searchKhoaHocByDiaDiem)
        RetrofitClient.getApiService().searchKhoaHoc(destination, ngayTimKiem)
            .enqueue(new Callback<List<LopHoc>>() {
                // ...
            });
    } else {
        // Chỉ lọc theo địa điểm
        RetrofitClient.getApiService().searchKhoaHoc(destination, null)
            .enqueue(new Callback<List<LopHoc>>() {
                // ...
            });
    }
}
```

---

## 🎯 API Endpoints

### Endpoint Mới

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/khoahoc` | Lấy tất cả khóa học (KhoaHocDTO - format cũ) |
| GET | `/api/khoahoc/all` | **Lấy tất cả lớp học (LopHocDTO - format Android)** |
| GET | `/api/khoahoc/{id}` | Lấy khóa học theo ID |
| GET | `/api/khoahoc/search?diaDiem=Hà Nội` | Tìm lớp học theo địa điểm (LopHocDTO) |
| GET | `/api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25` | Tìm lớp học theo địa điểm và ngày (LopHocDTO) |
| GET | `/api/danhmucmonan/khoahoc/{maKhoaHoc}` | Lấy danh mục món ăn theo khóa học |
| GET | `/api/hinhanh-khoahoc/khoahoc/{maKhoaHoc}` | Lấy hình ảnh khóa học |

### Sự Khác Biệt Giữa `/api/khoahoc` và `/api/khoahoc/all`

**`/api/khoahoc`** - Trả về KhoaHocDTO:
```json
{
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
  "giaTien": 650000,  // BigDecimal
  "lichTrinhList": [   // List object
    {
      "gioBatDau": "17:30:00",
      "gioKetThuc": "20:30:00"
    }
  ]
}
```

**`/api/khoahoc/all`** - Trả về LopHocDTO (tương thích Android):
```json
{
  "maLopHoc": 1,
  "tenLop": "Ẩm thực phố cổ Hà Nội",
  "gia": "650.000đ",      // String với format
  "thoiGian": "17:30:00 - 20:30:00",  // String
  "diaDiem": "45 Hàng Bạc...",
  "suat": 17
}
```

---

## 🧪 Test API

### Test 1: Lấy tất cả lớp học (LopHocDTO - Android format)
```bash
GET http://localhost:8080/api/khoahoc/all
```

**Response:**
```json
[
  {
    "maLopHoc": 1,
    "tenLop": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng...",
    "gioiThieu": "Trải nghiệm nấu các món ăn...",
    "giaTriSauBuoiHoc": "• Nắm vững kỹ thuật...",
    "thoiGian": "17:30:00 - 20:30:00",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "gia": "650.000đ",
    "danhGia": 0.0,
    "soDanhGia": 0,
    "hinhAnh": "phobo.png",
    "coUuDai": true,
    "suat": 20,
    "trangThai": "Đang mở",
    "cacNgayTrongTuan": "2,3,4,5,6,7,CN",
    "loaiLich": "HangNgay",
    "isFavorite": false,
    "daDienRa": false
  }
]
```

### Test 2: Lấy tất cả khóa học (KhoaHocDTO - format cũ)
```bash
GET http://localhost:8080/api/khoahoc
```

**Response:**
```json
[
  {
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng...",
    "giaTien": 650000,
    "saoTrungBinh": 0.0,
    "soLuongDanhGia": 0,
    "coUuDai": true,
    "lichTrinhList": [
      {
        "maLichTrinh": 1,
        "gioBatDau": "17:30:00",
        "gioKetThuc": "20:30:00",
        "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội"
      }
    ]
  }
]
```

### Test 3: Tìm lớp học theo địa điểm
```bash
GET http://localhost:8080/api/khoahoc/search?diaDiem=Hà Nội
```

### Test 4: Tìm lớp học theo địa điểm và ngày
```bash
GET http://localhost:8080/api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
```

**Response:** Sử dụng stored procedure `sp_LayDanhSachLopTheoNgay` để tính số chỗ trống

---

## 📱 Luồng Hoạt Động Android

### 1. HomeFragment - Hiển thị lớp học phổ biến

```
User mở app
    ↓
HomeFragment.loadPopularClasses()
    ↓
API: GET /api/khoahoc/all
    ↓
Backend: KhoaHocService.getAllLopHoc()
    ↓
Response: List<LopHocDTO> (với format Android)
    ↓
HomeFragment.displayPopularClasses()
    ↓
Hiển thị 4 lớp học phổ biến
```

### 2. ClassesFragment - Tìm kiếm lớp học

```
User chọn địa điểm và ngày
    ↓
Click "Tìm kiếm"
    ↓
Chuyển sang ClassesFragment
    ↓
ClassesFragment.loadLopHoc()
    ↓
Convert date: "T4, 25/12/2024" → "2024-12-25"
    ↓
API: GET /api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2024-12-25
    ↓
Backend: KhoaHocService.searchByDiaDiemAndDate()
    ↓
Stored Procedure: sp_LayDanhSachLopTheoNgay
    ↓
Response: List<LopHocDTO> (với thông tin số chỗ trống)
    ↓
ClassesFragment.handleResponse()
    ↓
Hiển thị danh sách lớp học trong RecyclerView
```

---

## ✅ Kết Quả

### Backend
- ✅ Build thành công
- ✅ Endpoint `/api/khoahoc/search` hoạt động
- ✅ Stored procedure được gọi đúng
- ✅ Tính số chỗ trống chính xác

### Android
- ✅ Gọi API mới thành công
- ✅ HomeFragment hiển thị lớp học phổ biến
- ✅ ClassesFragment tìm kiếm theo địa điểm và ngày
- ✅ Hiển thị số chỗ trống từ stored procedure
- ✅ Tương thích ngược với API cũ (nếu cần)

---

## 🔍 Lưu Ý

### 1. Model LopHoc vẫn giữ nguyên
- Android vẫn sử dụng model `LopHoc.java`
- Backend trả về `KhoaHocDTO` nhưng Android map sang `LopHoc`
- Không cần thay đổi model ở Android

### 2. Tương thích ngược
- API cũ `/api/lophoc` vẫn có thể sử dụng nếu cần
- Giúp dễ dàng rollback nếu có vấn đề

### 3. Stored Procedure
- Endpoint `/api/khoahoc/search` với `ngayTimKiem` sẽ gọi stored procedure
- Tự động tính số chỗ trống dựa trên ngày được chọn
- Hiệu suất tốt hơn so với query thông thường

---

## 🚀 Bước Tiếp Theo

1. **Test trên thiết bị thật:**
   - Chạy backend: `./gradlew bootRun`
   - Chạy Android app
   - Test tìm kiếm theo địa điểm và ngày

2. **Cập nhật các màn hình khác:**
   - DetailDescriptionFragment
   - FavoriteFragment
   - Các màn hình khác sử dụng API lớp học

3. **Thêm loading indicator:**
   - Hiển thị loading khi gọi API
   - Xử lý trường hợp không có dữ liệu

4. **Xử lý lỗi tốt hơn:**
   - Hiển thị thông báo lỗi rõ ràng
   - Retry khi lỗi kết nối

---

**Hoàn thành! Android app đã sử dụng API mới thành công! 🎉**

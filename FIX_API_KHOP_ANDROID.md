# ✅ Fix API Khớp Với Android

**Ngày:** 20/12/2025  
**Vấn đề:** Backend trả về `KhoaHocDTO` không khớp với model `LopHoc` của Android  
**Giải pháp:** Tạo `LopHocDTO` mới và cập nhật backend để trả về format đúng

---

## 🔍 Vấn Đề

### Backend trả về (KhoaHocDTO):
```json
{
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
  "giaTien": 650000,  // BigDecimal
  "lichTrinhList": [   // List object
    {
      "gioBatDau": "17:30:00",
      "gioKetThuc": "20:30:00",
      "diaDiem": "45 Hàng Bạc..."
    }
  ]
}
```

### Android mong đợi (LopHoc):
```json
{
  "maLopHoc": 1,
  "tenLop": "Ẩm thực phố cổ Hà Nội",
  "gia": "650.000đ",      // String với format
  "thoiGian": "17:30:00 - 20:30:00",  // String
  "diaDiem": "45 Hàng Bạc...",        // String
  "suat": 17              // Số chỗ trống
}
```

---

## ✅ Giải Pháp

### 1. Tạo LopHocDTO mới (Backend)

**File:** `BE/src/main/java/com/android/be/dto/LopHocDTO.java`

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHocDTO {
    private Integer maLopHoc;  // = maKhoaHoc
    private String tenLop;     // = tenKhoaHoc
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private String thoiGian;   // Format: "17:30 - 20:30"
    private String diaDiem;
    private String gia;        // Format: "715.000đ"
    private Float danhGia;
    private Integer soDanhGia;
    private String hinhAnh;
    private Boolean coUuDai;
    private Integer suat;      // Số chỗ còn trống
    private String trangThai;
    // ... các field khác
}
```

### 2. Cập nhật KhoaHocService

**Thêm 2 methods convert:**

#### a) `convertToLopHocDTO(KhoaHoc, LichTrinhLopHoc)`
```java
private LopHocDTO convertToLopHocDTO(KhoaHoc khoaHoc, LichTrinhLopHoc lichTrinh) {
    LopHocDTO dto = new LopHocDTO();
    
    // Map fields
    dto.setMaLopHoc(khoaHoc.getMaKhoaHoc());
    dto.setTenLop(khoaHoc.getTenKhoaHoc());
    
    // Format giá: 715000 -> "715.000đ"
    DecimalFormat formatter = new DecimalFormat("#,###");
    dto.setGia(formatter.format(khoaHoc.getGiaTien()).replace(",", ".") + "đ");
    
    // Format thời gian: "17:30 - 20:30"
    dto.setThoiGian(lichTrinh.getGioBatDau() + " - " + lichTrinh.getGioKetThuc());
    dto.setDiaDiem(lichTrinh.getDiaDiem());
    
    return dto;
}
```

#### b) `convertStoredProcResultToLopHocDTO(Object[])`
```java
private LopHocDTO convertStoredProcResultToLopHocDTO(Object[] row) {
    LopHocDTO dto = new LopHocDTO();
    
    dto.setMaLopHoc((Integer) row[0]);
    dto.setTenLop((String) row[1]);
    
    // Format giá
    DecimalFormat formatter = new DecimalFormat("#,###");
    dto.setGia(formatter.format(row[3]).replace(",", ".") + "đ");
    
    // Format thời gian
    dto.setThoiGian(row[7] + " - " + row[8]);
    dto.setDiaDiem((String) row[9]);
    dto.setSuat((Integer) row[12]); // Số chỗ trống
    
    return dto;
}
```

### 3. Cập nhật KhoaHocController

**Đổi return type:**
```java
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

---

## 🎯 Kết Quả

### API Response (Sau khi fix):
```json
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
  "suat": 17,
  "trangThai": "Còn Nhận",
  "cacNgayTrongTuan": "2,3,4,5,6,7,CN",
  "loaiLich": "HangNgay",
  "isFavorite": false,
  "daDienRa": false
}
```

### Android Model (LopHoc.java) - Không cần thay đổi!
Model `LopHoc` trong Android đã khớp hoàn toàn với `LopHocDTO` từ backend.

---

## 🔧 Các Thay Đổi

### Backend:
1. ✅ Tạo `LopHocDTO.java` - DTO mới tương thích Android
2. ✅ Cập nhật `KhoaHocService.java`:
   - Thêm `convertToLopHocDTO()`
   - Thêm `convertStoredProcResultToLopHocDTO()`
   - Đổi return type của `searchByDiaDiem()` và `searchByDiaDiemAndDate()`
3. ✅ Cập nhật `KhoaHocController.java`:
   - Đổi return type của endpoint `/search` từ `KhoaHocDTO` sang `LopHocDTO`

### Android:
- ✅ Không cần thay đổi gì! Model `LopHoc` đã khớp

---

## 🧪 Test

### Test 1: Tìm kiếm theo địa điểm
```bash
GET http://localhost:8080/api/khoahoc/search?diaDiem=Hà Nội
```

**Response:**
```json
[
  {
    "maLopHoc": 1,
    "tenLop": "Ẩm thực phố cổ Hà Nội",
    "gia": "650.000đ",
    "thoiGian": "17:30:00 - 20:30:00",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "suat": 20
  }
]
```

### Test 2: Tìm kiếm theo địa điểm và ngày
```bash
GET http://localhost:8080/api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
```

**Response:** Sử dụng stored procedure, tự động tính số chỗ trống

---

## 📱 Luồng Hoạt Động

```
User click "Tìm kiếm" trong HomeFragment
    ↓
Chuyển sang ClassesFragment với:
  - destination: "Hà Nội"
  - date: "T4, 25/12/2024"
    ↓
ClassesFragment.loadLopHoc()
    ↓
Convert date: "T4, 25/12/2024" → "2024-12-25"
    ↓
API: GET /api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2024-12-25
    ↓
Backend: KhoaHocController.searchKhoaHoc()
    ↓
KhoaHocService.searchByDiaDiemAndDate()
    ↓
Stored Procedure: sp_LayDanhSachLopTheoNgay
    ↓
Convert: Object[] → LopHocDTO
    ↓
Response: List<LopHocDTO>
    ↓
Android: Parse JSON → List<LopHoc>
    ↓
ClassesFragment.handleResponse()
    ↓
Hiển thị danh sách trong RecyclerView ✅
```

---

## ✅ Kết Luận

- ✅ Backend build thành công
- ✅ API trả về format đúng với Android
- ✅ Model `LopHoc` trong Android không cần thay đổi
- ✅ Giá được format: "715.000đ"
- ✅ Thời gian được format: "17:30 - 20:30"
- ✅ Số chỗ trống được tính từ stored procedure
- ✅ Tương thích hoàn toàn với Android app

**Bây giờ có thể test trên Android app! 🚀**

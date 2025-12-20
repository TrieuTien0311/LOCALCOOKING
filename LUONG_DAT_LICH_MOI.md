# Luồng Đặt Lịch Mới - Tìm Kiếm Trước

## 📋 Tổng Quan

**Luồng mới:** User tìm kiếm khóa học theo địa điểm và ngày → Chọn khóa học → Chọn lịch trình → Đặt lịch

**Ưu điểm:**
- ✅ User chọn ngày trước → Chỉ hiển thị khóa học có lịch vào ngày đó
- ✅ Giảm bước thừa (không cần chọn ngày lại)
- ✅ UX tốt hơn: Tìm → Chọn → Đặt

---

## 🎯 Các Bước Chi Tiết

### **Bước 1: Tìm Kiếm Khóa Học**

**Màn hình:** `SearchActivity` hoặc `HomeFragment`

**Input:**
- Địa điểm: Dropdown (Hà Nội, Đà Nẵng, Huế, Cần Thơ)
- Ngày học: DatePicker (chọn ngày trong tương lai)

**API:**
```
GET /api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
```

**Response:**
```json
[
  {
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng",
    "giaTien": 650000,
    "hinhAnh": "phobo.png",
    "saoTrungBinh": 4.8,
    "soLuongDanhGia": 120
  }
]
```

**UI:** RecyclerView hiển thị danh sách khóa học với nút "Đặt lịch"

---

### **Bước 2: Chọn Lịch Trình**

**Màn hình:** `BookingStep1Activity`

**Data nhận từ Intent:**
```java
int maKhoaHoc = getIntent().getIntExtra("maKhoaHoc", 0);
String tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
String giaTien = getIntent().getStringExtra("giaTien");
String ngayTimKiem = getIntent().getStringExtra("ngayTimKiem"); // "2025-12-25"
```

**API:**
```
GET /api/lichtrinh/khoahoc/{maKhoaHoc}
```

**Response:**
```json
[
  {
    "maLichTrinh": 1,
    "thuTrongTuan": "2,3,4,5,6,7,CN",
    "gioBatDau": "17:30",
    "gioKetThuc": "20:30",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "soLuongToiDa": 20
  }
]
```

**Logic:**
- Hiển thị các lịch trình
- User chọn 1 lịch trình
- Tự động dùng `ngayTimKiem` làm `ngayThamGia`

**Kiểm tra chỗ trống:**
```
GET /api/lichtrinh/check-seats?maLichTrinh=1&ngayThamGia=2025-12-25
```

---

### **Bước 3: Điều Chỉnh Số Người**

**Màn hình:** `BookingStep2Activity`

**UI:**
- Nút [-] giảm số người (min = 1)
- Nút [+] tăng số người (max = soChoConLai)
- Hiển thị tổng tiền

---

### **Bước 4: Xác Nhận & Đặt Lịch**

**Màn hình:** `BookingStep3Activity`

**API:**
```
POST /api/datlich
```

**Request:**
```json
{
  "maHocVien": 4,
  "maLichTrinh": 1,
  "ngayThamGia": "2025-12-25",
  "soLuongNguoi": 2,
  "tongTien": 1300000,
  "tenNguoiDat": "Ngô Thị Thảo Vy",
  "emailNguoiDat": "thaovyn0312@gmail.com",
  "sdtNguoiDat": "0934567890"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Đặt lịch thành công",
  "data": {
    "maDatLich": 5,
    "trangThai": "Chờ Duyệt"
  }
}
```

---

## 📊 So Sánh Luồng Cũ vs Mới

| Tiêu chí | Luồng Cũ | Luồng Mới |
|----------|-----------|-----------|
| Bước 1 | Chọn khóa học | **Tìm kiếm** theo địa điểm + ngày |
| Bước 2 | Chọn lịch trình + ngày | Chọn khóa học |
| Bước 3 | Điều chỉnh số người | Chọn lịch trình |
| Bước 4 | Xác nhận | Điều chỉnh số người |
| Bước 5 | - | Xác nhận |
| **Ưu điểm** | Đơn giản | **Chính xác hơn, UX tốt hơn** |

---

## ✅ API Đã Có Sẵn

### Backend:
- ✅ `GET /api/khoahoc/search` - Tìm kiếm khóa học
- ✅ `GET /api/lichtrinh/khoahoc/{maKhoaHoc}` - Lấy lịch trình
- ✅ `GET /api/lichtrinh/check-seats` - Kiểm tra chỗ trống
- ✅ `POST /api/datlich` - Tạo đặt lịch

### Frontend:
- ✅ `ApiService` đã khai báo đầy đủ
- ✅ Models: `KhoaHoc`, `LichTrinhLopHoc`, `DatLichRequest`, `DatLichResponse`

---

## 🚀 Cần Implement

### Frontend:
1. **SearchActivity/Fragment:**
   - UI: Dropdown địa điểm + DatePicker
   - Call API `searchKhoaHoc()`
   - RecyclerView hiển thị kết quả

2. **BookingStep1Activity:**
   - Nhận data từ Intent
   - Call API `getLichTrinhByKhoaHoc()`
   - RecyclerView chọn lịch trình
   - Call API `checkAvailableSeats()`

3. **BookingStep2Activity:**
   - UI điều chỉnh số người
   - Tính tổng tiền

4. **BookingStep3Activity:**
   - Hiển thị thông tin xác nhận
   - Call API `createDatLich()`
   - Chuyển sang PaymentActivity

---

## 🎯 Kết Luận

Luồng mới tối ưu hơn cho trải nghiệm người dùng:
- User biết rõ khóa học nào có lịch vào ngày mình muốn
- Giảm thiểu bước thừa
- Tăng tỷ lệ chuyển đổi (conversion rate)

Backend đã sẵn sàng, chỉ cần implement UI/UX ở Frontend! 🚀

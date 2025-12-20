# API Đặt Lịch - Hoàn Chỉnh ✅

## 📋 Tổng Quan

API đặt lịch đã được implement đầy đủ với các chức năng:
- ✅ Kiểm tra chỗ trống trước khi đặt
- ✅ Validation số lượng người
- ✅ Quản lý trạng thái đặt lịch
- ✅ Hủy đặt lịch
- ✅ Lấy lịch sử đặt lịch theo học viên

---

## 🔗 Base URL
```
http://localhost:8080/api
```

---

## 📌 Endpoints Chính

### 1. Kiểm Tra Chỗ Trống
**GET** `/api/datlich/check-seats`

**Query Parameters:**
- `maLichTrinh` (required): ID lịch trình
- `ngayThamGia` (required): Ngày tham gia (format: YYYY-MM-DD)

**Request Example:**
```
GET /api/datlich/check-seats?maLichTrinh=1&ngayThamGia=2025-12-25
```

**Response Success:**
```json
{
  "success": true,
  "soChoConLai": 15,
  "message": "Còn chỗ trống"
}
```

**Response Hết Chỗ:**
```json
{
  "success": true,
  "soChoConLai": 0,
  "message": "Đã hết chỗ"
}
```

---

### 2. Tạo Đặt Lịch Mới
**POST** `/api/datlich`

**Request Body:**
```json
{
  "maHocVien": 4,
  "maLichTrinh": 1,
  "ngayThamGia": "2025-12-25",
  "soLuongNguoi": 2,
  "tongTien": 1300000,
  "tenNguoiDat": "Ngô Thị Thảo Vy",
  "emailNguoiDat": "thaovyn0312@gmail.com",
  "sdtNguoiDat": "0934567890",
  "ghiChu": "Muốn học buổi tối"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đặt lịch thành công",
  "data": {
    "maDatLich": 5,
    "maHocVien": 4,
    "maLichTrinh": 1,
    "ngayThamGia": "2025-12-25",
    "soLuongNguoi": 2,
    "tongTien": 1300000,
    "tenNguoiDat": "Ngô Thị Thảo Vy",
    "emailNguoiDat": "thaovyn0312@gmail.com",
    "sdtNguoiDat": "0934567890",
    "ngayDat": "2025-12-20T14:30:00",
    "trangThai": "Chờ Duyệt",
    "ghiChu": "Muốn học buổi tối"
  }
}
```

**Response Error (Không đủ chỗ):**
```json
{
  "success": false,
  "message": "Không đủ chỗ trống. Chỉ còn 1 chỗ"
}
```

---

### 3. Lấy Lịch Sử Đặt Lịch Theo Học Viên
**GET** `/api/datlich/hocvien/{maHocVien}`

**Request Example:**
```
GET /api/datlich/hocvien/4
```

**Response:**
```json
[
  {
    "maDatLich": 1,
    "maHocVien": 4,
    "maLichTrinh": 1,
    "ngayThamGia": "2025-12-22",
    "soLuongNguoi": 1,
    "tongTien": 650000,
    "tenNguoiDat": "Ngô Thị Thảo Vy",
    "emailNguoiDat": "thaovyn0312@gmail.com",
    "sdtNguoiDat": "0934567890",
    "ngayDat": "2025-12-20T10:30:00",
    "trangThai": "Đã Duyệt",
    "ghiChu": null
  }
]
```

---

### 4. Lấy Đặt Lịch Theo Trạng Thái
**GET** `/api/datlich/hocvien/{maHocVien}/trangthai/{trangThai}`

**Trạng thái hợp lệ:**
- `Chờ Duyệt`
- `Đã Duyệt`
- `Đã Hủy`
- `Hoàn Thành`

**Request Example:**
```
GET /api/datlich/hocvien/4/trangthai/Chờ Duyệt
```

---

### 5. Cập Nhật Đặt Lịch
**PUT** `/api/datlich/{id}`

**Request Body:**
```json
{
  "maHocVien": 4,
  "maLichTrinh": 1,
  "ngayThamGia": "2025-12-25",
  "soLuongNguoi": 3,
  "tongTien": 1950000,
  "tenNguoiDat": "Ngô Thị Thảo Vy",
  "emailNguoiDat": "thaovyn0312@gmail.com",
  "sdtNguoiDat": "0934567890",
  "trangThai": "Đã Duyệt",
  "ghiChu": "Cập nhật số người"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Cập nhật đặt lịch thành công",
  "data": { ... }
}
```

---

### 6. Hủy Đặt Lịch
**PUT** `/api/datlich/{id}/cancel`

**Response:**
```json
{
  "success": true,
  "message": "Hủy đặt lịch thành công",
  "data": {
    "maDatLich": 5,
    "trangThai": "Đã Hủy",
    ...
  }
}
```

---

### 7. Xóa Đặt Lịch
**DELETE** `/api/datlich/{id}`

**Response:** `204 No Content`

---

## 🔍 Endpoints Lịch Trình

### 1. Lấy Lịch Trình Theo Khóa Học
**GET** `/api/lichtrinh/khoahoc/{maKhoaHoc}`

**Response:**
```json
[
  {
    "maLichTrinh": 1,
    "maKhoaHoc": 1,
    "maGiaoVien": 1,
    "thuTrongTuan": "2,3,4,5,6,7,CN",
    "gioBatDau": "17:30:00",
    "gioKetThuc": "20:30:00",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "soLuongToiDa": 20,
    "trangThai": true
  }
]
```

---

### 2. Kiểm Tra Chỗ Trống (Stored Procedure)
**GET** `/api/lichtrinh/check-seats`

**Query Parameters:**
- `maLichTrinh`: ID lịch trình
- `ngayThamGia`: Ngày tham gia (YYYY-MM-DD)

**Response:** Array với thông tin chi tiết

---

## 🎯 Luồng Đặt Lịch Hoàn Chỉnh

```
1. User chọn khóa học
   ↓
2. Lấy lịch trình: GET /api/lichtrinh/khoahoc/{maKhoaHoc}
   ↓
3. User chọn lịch trình và ngày
   ↓
4. Kiểm tra chỗ trống: GET /api/datlich/check-seats
   ↓
5. User điều chỉnh số người
   ↓
6. Tạo đặt lịch: POST /api/datlich
   ↓
7. Nhận maDatLich → Chuyển sang thanh toán
```

---

## ✅ Validation Rules

### Khi Tạo Đặt Lịch:
1. ✅ Kiểm tra lịch trình có tồn tại
2. ✅ Kiểm tra lịch trình có đang hoạt động (trangThai = true)
3. ✅ Đếm số người đã đặt trong ngày
4. ✅ Kiểm tra: `soChoConLai >= soLuongNguoi`
5. ✅ Tự động set `ngayDat = GETDATE()`
6. ✅ Tự động set `trangThai = "Chờ Duyệt"`

### Khi Cập Nhật Đặt Lịch:
1. ✅ Nếu thay đổi `soLuongNguoi`, `maLichTrinh`, hoặc `ngayThamGia`
2. ✅ Kiểm tra lại chỗ trống
3. ✅ Trừ đi số người của đơn hiện tại trước khi tính

### Đếm Số Chỗ Đã Đặt:
- ✅ Chỉ đếm các đơn có `trangThai <> "Đã Hủy"`
- ✅ Tính theo `maLichTrinh` + `ngayThamGia`

---

## 🧪 Test với Postman

### Test 1: Kiểm tra chỗ trống
```bash
GET http://localhost:8080/api/datlich/check-seats?maLichTrinh=1&ngayThamGia=2025-12-25
```

### Test 2: Đặt lịch mới
```bash
POST http://localhost:8080/api/datlich
Content-Type: application/json

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

### Test 3: Lấy lịch sử đặt lịch
```bash
GET http://localhost:8080/api/datlich/hocvien/4
```

### Test 4: Hủy đặt lịch
```bash
PUT http://localhost:8080/api/datlich/1/cancel
```

---

## 📝 Notes

- Tất cả endpoints đều hỗ trợ CORS với `origins = "*"`
- Response thành công: HTTP 200 (GET, PUT) hoặc 201 (POST)
- Response lỗi: HTTP 400 (Bad Request) hoặc 404 (Not Found)
- Ngày tháng sử dụng format ISO: `YYYY-MM-DD`
- Trigger tự động tạo thông báo khi đặt lịch thành công

---

## 🚀 Status: HOÀN THÀNH ✅

Backend API đặt lịch đã sẵn sàng để Frontend tích hợp!

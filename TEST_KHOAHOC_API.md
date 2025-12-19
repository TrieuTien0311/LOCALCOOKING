# 🧪 Test API KhoaHoc - Kiểm Tra Dữ Liệu Đầy Đủ

## 📋 Mục Tiêu
Kiểm tra API `/api/khoahoc` trả về đầy đủ:
- ✅ Thông tin khóa học
- ✅ Danh sách lịch trình (`lichTrinhList`)
- ✅ Danh sách danh mục món ăn (`danhMucMonAnList`)
  - Mỗi khóa học có 3 danh mục
  - Tổng 4 món: 1 khai vị + 2 món chính + 1 tráng miệng

---

## 🔧 Cấu Trúc Dữ Liệu Mong Đợi

```json
{
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
  "moTa": "Khám phá hương vị...",
  "gioiThieu": "Trải nghiệm nấu...",
  "giaTriSauBuoiHoc": "• Nắm vững kỹ thuật...",
  "giaTien": 650000,
  "hinhAnh": "phobo.png",
  "soLuongDanhGia": 0,
  "saoTrungBinh": 0.0,
  "coUuDai": true,
  
  "lichTrinhList": [
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
  ],
  
  "danhMucMonAnList": [
    {
      "maDanhMuc": 1,
      "tenDanhMuc": "Món khai vị",
      "iconDanhMuc": "ic_appetizer.png",
      "thuTu": 1,
      "danhSachMon": [
        {
          "maMonAn": 1,
          "maKhoaHoc": 1,
          "maDanhMuc": 1,
          "tenMon": "Nem rán Hà Nội",
          "gioiThieu": "Nem rán giòn rụm...",
          "nguyenLieu": "Bánh đa nem, thịt lợn..."
        }
      ]
    },
    {
      "maDanhMuc": 2,
      "tenDanhMuc": "Món chính",
      "iconDanhMuc": "ic_main_dish.png",
      "thuTu": 2,
      "danhSachMon": [
        {
          "maMonAn": 2,
          "tenMon": "Phở bò Hà Nội",
          ...
        },
        {
          "maMonAn": 3,
          "tenMon": "Bún chả Hà Nội",
          ...
        }
      ]
    },
    {
      "maDanhMuc": 3,
      "tenDanhMuc": "Món tráng miệng",
      "iconDanhMuc": "ic_dessert.png",
      "thuTu": 3,
      "danhSachMon": [
        {
          "maMonAn": 4,
          "tenMon": "Chè ba màu",
          ...
        }
      ]
    }
  ]
}
```

---

## 🧪 Test Cases

### Test 1: Lấy tất cả khóa học
```bash
GET http://localhost:8080/api/khoahoc
```

**Kiểm tra:**
- ✅ Mỗi khóa học có `lichTrinhList` không null
- ✅ Mỗi khóa học có `danhMucMonAnList` không null
- ✅ `danhMucMonAnList` có 3 danh mục
- ✅ Tổng số món = 4 (1 + 2 + 1)

### Test 2: Lấy khóa học theo ID
```bash
GET http://localhost:8080/api/khoahoc/1
```

**Kiểm tra:**
- ✅ Trả về đầy đủ thông tin khóa học
- ✅ `lichTrinhList` có ít nhất 1 lịch trình
- ✅ `danhMucMonAnList` có 3 danh mục với 4 món

### Test 3: Tìm kiếm theo địa điểm
```bash
GET http://localhost:8080/api/khoahoc/search?diaDiem=Hà Nội
```

**Kiểm tra:**
- ✅ Trả về các khóa học ở Hà Nội
- ✅ Mỗi khóa học có đầy đủ `lichTrinhList` và `danhMucMonAnList`

---

## 🐛 Vấn Đề Hiện Tại

### Triệu chứng:
- `lichTrinhList`: null
- `danhMucMonAnList`: null
- `tenGiaoVien`: null
- `soLuongHienTai`: null
- `conTrong`: null
- `trangThaiHienThi`: null

### Nguyên nhân:
Backend không load đầy đủ dữ liệu khi convert sang DTO.

### Giải pháp:
✅ Đã cập nhật `KhoaHocService.convertToDTO()` để:
1. Load `lichTrinhList` từ `LichTrinhLopHocRepository`
2. Load `danhMucMonAnList` từ `DanhMucMonAnService`

---

## ✅ Kết Quả Mong Đợi

Sau khi fix:
- ✅ `lichTrinhList`: Có ít nhất 1 lịch trình
- ✅ `danhMucMonAnList`: Có 3 danh mục
- ✅ Mỗi danh mục có `danhSachMon` với số lượng đúng:
  - Món khai vị: 1 món
  - Món chính: 2 món
  - Món tráng miệng: 1 món

---

## 🚀 Cách Test

### 1. Chạy Backend
```bash
cd BE
./gradlew bootRun
```

### 2. Test bằng Postman hoặc curl
```bash
# Test 1: Lấy tất cả
curl http://localhost:8080/api/khoahoc

# Test 2: Lấy theo ID
curl http://localhost:8080/api/khoahoc/1

# Test 3: Tìm kiếm
curl "http://localhost:8080/api/khoahoc/search?diaDiem=Hà Nội"
```

### 3. Kiểm tra Response
- Mở JSON response
- Kiểm tra `lichTrinhList` không null
- Kiểm tra `danhMucMonAnList` không null
- Đếm số món trong mỗi danh mục

---

## 📝 Ghi Chú

### Cấu trúc Database:
- **KhoaHoc**: Thông tin khóa học (nội dung, giá, mô tả)
- **LichTrinhLopHoc**: Lịch trình (thời gian, địa điểm, giáo viên)
- **MonAn**: Món ăn (thuộc khóa học và danh mục)
- **DanhMucMonAn**: Danh mục món ăn (khai vị, món chính, tráng miệng)

### Quan hệ:
- 1 KhoaHoc → N LichTrinhLopHoc
- 1 KhoaHoc → N MonAn
- 1 DanhMucMonAn → N MonAn
- 1 MonAn → 1 KhoaHoc + 1 DanhMucMonAn

---

**Cập nhật:** 20/12/2025
**Trạng thái:** ✅ Đã fix - Đang test

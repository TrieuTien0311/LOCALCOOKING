# Hướng dẫn tạo API còn lại

## ✅ Đã tạo:
1. GiaoVien Model, DTO, Repository, Service, Controller

## 📋 Cần tạo tiếp:

### 1. DanhMucMonAn (Backend)
- Model: `DanhMucMonAn.java`
- DTO: `DanhMucMonAnDTO.java` (có list MonAn)
- Repository: `DanhMucMonAnRepository.java`
- Service: `DanhMucMonAnService.java`
- Controller: `DanhMucMonAnController.java`

### 2. MonAn (Backend)
- Model: `MonAn.java`
- DTO: `MonAnDTO.java`
- Repository: `MonAnRepository.java`

### 3. Logic cần implement:
- API `/api/danhmucmonan/khoahoc/{maKhoaHoc}` trả về:
  - Danh sách danh mục (Khai vị, Món chính, Tráng miệng)
  - Mỗi danh mục chứa list món ăn tương ứng
  - Có icon danh mục

## 🔧 Vấn đề hiện tại:
1. **Icon danh mục**: Backend cần trả về `iconDanhMuc` từ bảng `DanhMucMonAn`
2. **Giáo viên**: Đã fix - API `/api/giaovien/{id}` join với NguoiDung

## 📝 Cấu trúc SQL:
```sql
DanhMucMonAn:
- maDanhMuc
- tenDanhMuc (Món khai vị, Món chính, Món tráng miệng)
- iconDanhMuc
- thuTu

MonAn:
- maMonAn
- maKhoaHoc
- maDanhMuc
- tenMon
- gioiThieu
- nguyenLieu
```

## 🎯 Kết quả mong đợi:
API trả về JSON:
```json
[
  {
    "maDanhMuc": 1,
    "tenDanhMuc": "Món khai vị",
    "iconDanhMuc": "ic_appetizer.png",
    "thuTu": 1,
    "danhSachMon": [
      {
        "maMonAn": 1,
        "tenMon": "Nem rán Hà Nội",
        "gioiThieu": "...",
        "nguyenLieu": "..."
      }
    ]
  }
]
```

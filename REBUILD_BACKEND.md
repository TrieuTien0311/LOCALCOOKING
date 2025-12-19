# Hướng dẫn Rebuild Backend

## Vấn đề:
API `/api/giaovien` trả về 404 vì controller chưa được compile.

## Giải pháp:

### Cách 1: Rebuild trong IntelliJ IDEA
1. Click **Build** → **Rebuild Project**
2. Hoặc **Ctrl + Shift + F9** (Windows)
3. Đợi build xong
4. Restart Spring Boot application

### Cách 2: Rebuild bằng Gradle (Command Line)
```bash
cd BE
gradlew clean build
gradlew bootRun
```

### Cách 3: Trong IntelliJ - Gradle Tool Window
1. Mở **Gradle** tool window (bên phải)
2. Expand **BE** → **Tasks** → **build**
3. Double click **clean**
4. Double click **build**
5. Restart application

## Kiểm tra sau khi rebuild:

### 1. Test API Giáo viên:
```
GET http://localhost:8080/api/giaovien/1
```

Kết quả mong đợi:
```json
{
  "maGiaoVien": 1,
  "hoTen": "Bà Nguyễn Thị Thương",
  "chuyenMon": "...",
  ...
}
```

### 2. Test API Danh mục món ăn:
```
GET http://localhost:8080/api/danhmucmonan/khoahoc/1
```

Kết quả mong đợi:
```json
[
  {
    "maDanhMuc": 1,
    "tenDanhMuc": "Món khai vị",
    "iconDanhMuc": "ic_appetizer.png",
    "danhSachMon": [...]
  }
]
```

## Lưu ý:
- Phải **restart** Spring Boot application sau khi rebuild
- Nếu vẫn lỗi, check console log xem có lỗi compile không
- Đảm bảo tất cả dependencies trong `build.gradle` đã được download

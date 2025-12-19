# Test Case API Giảng viên

## Dữ liệu trong database:

### Giảng viên 1:
- **maGiaoVien**: 1
- **maNguoiDung**: 2
- **hoTen**: Nguyễn Văn An
- **chuyenMon**: Ẩm thực Việt Nam truyền thống
- **kinhNghiem**: 20 năm kinh nghiệm...
- **email**: nguyenvanan@gmail.com
- **soDienThoai**: 0912345678

### Giảng viên 2:
- **maGiaoVien**: 2
- **maNguoiDung**: 3
- **hoTen**: Trần Thị Bình
- **chuyenMon**: Bánh ngọt và tráng miệng
- **kinhNghiem**: 10 năm kinh nghiệm làm bánh Pháp...
- **email**: tranthibinh@gmail.com
- **soDienThoai**: 0923456789

---

## 🎯 Test trên Android App:

### Test Case 1: Xem lớp có Giảng viên 1 (Nguyễn Văn An)

**Các lớp có Giảng viên 1:**
- Khóa học 1: "Ẩm thực phố cổ Hà Nội" (17:30-20:30, Hà Nội)
- Khóa học 2: "Bún và miến Hà Nội" (08:30-11:30, Hà Nội)
- Khóa học 4: "Món nhậu Hà Nội" (14:00-17:00, Hà Nội)
- Khóa học 5: "Ẩm thực cung đình Huế" (17:30-20:30, Huế)
- Khóa học 7: "Món chay Huế" (08:30-11:30, Huế)
- Khóa học 9: "Hải sản Đà Nẵng" (17:30-20:30, Đà Nẵng)
- Khóa học 11: "Bún mắm và bún cá Đà Nẵng" (08:30-11:30, Đà Nẵng)
- Khóa học 12: "Bánh canh và cao lầu" (14:00-17:00, Đà Nẵng)
- Khóa học 13: "Ẩm thực miệt vườn Cần Thơ" (17:30-20:30, Cần Thơ)
- Khóa học 15: "Bánh và bún miền Tây" (08:30-11:30, Cần Thơ)

**Cách test:**
1. Chọn địa điểm: **Hà Nội**
2. Chọn ngày: **T2, 22/12/2025** (hoặc bất kỳ ngày nào)
3. Click vào lớp **"Ẩm thực phố cổ Hà Nội"** (17:30-20:30)
4. Xem tab "Mô tả" trong bottom sheet
5. **Kết quả mong đợi**: Hiển thị giảng viên **"Nguyễn Văn An"**

---

### Test Case 2: Xem lớp có Giảng viên 2 (Trần Thị Bình)

**Các lớp có Giảng viên 2:**
- Khóa học 3: "Bánh dân gian Hà Nội" (08:30-11:30, Hà Nội)
- Khóa học 6: "Bánh Huế truyền thống" (08:30-11:30, Huế)
- Khóa học 8: "Chè và tráng miệng Huế" (14:00-17:00, Huế)
- Khóa học 10: "Bánh xèo và nem lụi Đà Nẵng" (08:30-11:30, Đà Nẵng)
- Khóa học 14: "Bánh và bún miền Tây" (08:30-11:30, Cần Thơ)
- Khóa học 16: (14:00-17:00, Cần Thơ)

**Cách test:**
1. Chọn địa điểm: **Hà Nội**
2. Chọn ngày: **T4, 24/12/2025** (hoặc T5, T7)
3. Click vào lớp **"Bánh dân gian Hà Nội"** (08:30-11:30)
4. Xem tab "Mô tả" trong bottom sheet
5. **Kết quả mong đợi**: Hiển thị giảng viên **"Trần Thị Bình"**

---

## 📝 Tóm tắt nhanh:

### Để thấy Giảng viên 1 (Nguyễn Văn An):
- Chọn **Hà Nội** → Lớp **17:30-20:30** (Ẩm thực phố cổ)
- Hoặc **Huế** → Lớp **17:30-20:30** (Ẩm thực cung đình)

### Để thấy Giảng viên 2 (Trần Thị Bình):
- Chọn **Hà Nội** → Lớp **08:30-11:30** vào T4/T5/T7 (Bánh dân gian)
- Hoặc **Huế** → Lớp **08:30-11:30** vào T2/T4/T6 (Bánh Huế truyền thống)

---

## ✅ Kiểm tra:
- [ ] Lớp của Giảng viên 1 hiển thị "Nguyễn Văn An" (Nam)
- [ ] Lớp của Giảng viên 2 hiển thị "Trần Thị Bình" (Nữ)
- [ ] Chuyên môn hiển thị đúng
- [ ] Lịch sử kinh nghiệm hiển thị đúng

---

## 🔧 API Backend Test:

### Test Giảng viên 1:
```
GET http://localhost:8080/api/giaovien/1
```

### Test Giảng viên 2:
```
GET http://localhost:8080/api/giaovien/2
```


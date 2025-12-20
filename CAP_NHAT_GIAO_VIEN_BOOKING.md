# Cập nhật hiển thị thông tin giáo viên trong trang Đặt lịch

## Tổng quan
Đã cập nhật trang Booking để hiển thị đầy đủ thông tin giáo viên từ API, tương tự như trang danh sách khóa học.

## Các thay đổi đã thực hiện

### 1. Model GiaoVien (FE/app/src/main/java/com/example/localcooking_v3t/model/GiaoVien.java)
- ✅ Model đã tồn tại với đầy đủ các trường
- ✅ Thêm method `getHinhAnhResId()` để lấy resource ID của hình ảnh giáo viên

### 2. Booking Activity (FE/app/src/main/java/com/example/localcooking_v3t/Booking.java)

#### Thêm biến
```java
private GiaoVien giaoVien; // Thông tin giáo viên
```

#### Thêm import
```java
import com.example.localcooking_v3t.model.GiaoVien;
```

#### Thêm method `loadGiaoVienData()`
- Gọi API `getGiaoVienById()` để lấy thông tin giáo viên
- Được gọi sau khi load xong thông tin lịch trình
- Lấy `maGiaoVien` từ `LichTrinhLopHoc`

#### Thêm method `displayGiaoVienInfo()`
Hiển thị các thông tin:
- **Tên giáo viên** (`txtTenGV`) - từ `giaoVien.getHoTen()`
- **Chuyên môn** (`txtMoTaGV`) - từ `giaoVien.getChuyenMon()`
- **Kinh nghiệm** (`textView45`) - từ `giaoVien.getKinhNghiem()`
- **Lịch sử kinh nghiệm** (`textView46`) - từ `giaoVien.getLichSuKinhNghiem()`
- **Hình ảnh** (`imgGiaoVien`) - từ `giaoVien.getHinhAnh()`

#### Cập nhật method `loadLichTrinhData()`
- Thêm bước load thông tin giáo viên sau khi load lịch trình
- Gọi `loadGiaoVienData(lichTrinhLopHoc.getMaGiaoVien())`

#### Thêm tính năng mở rộng/thu gọn thông tin giáo viên
- Thêm click listener cho nút `btnDownTeacher`
- Toggle visibility của `txtAn` (phần lịch sử kinh nghiệm chi tiết)
- Xoay icon khi mở/đóng

### 3. API Service
- ✅ API `getGiaoVienById()` đã tồn tại trong `ApiService.java`
- ✅ Backend endpoint `/api/giaovien/{id}` đã sẵn sàng

### 4. Layout XML (activity_booking.xml)
- ✅ Layout đã có sẵn các view cần thiết:
  - `imgGiaoVien` - Hình ảnh giáo viên
  - `txtTenGV` - Tên giáo viên
  - `txtMoTaGV` - Mô tả/chuyên môn
  - `textView45` - Kinh nghiệm
  - `textView46` - Lịch sử kinh nghiệm chi tiết
  - `btnDownTeacher` - Nút mở rộng/thu gọn
  - `txtAn` - Container cho phần chi tiết (mặc định ẩn)

## Luồng hoạt động

1. **Khởi tạo Booking Activity**
   - Nhận dữ liệu từ Intent (maKhoaHoc, maLichTrinh, etc.)

2. **Load dữ liệu từ API**
   - Gọi `loadLichTrinhData()`
   - → Gọi `getLichTrinhById()` để lấy thông tin lịch trình
   - → Lấy `maGiaoVien` từ lịch trình
   - → Gọi `loadGiaoVienData(maGiaoVien)`
   - → Gọi `getGiaoVienById()` để lấy thông tin giáo viên
   - → Hiển thị thông tin giáo viên qua `displayGiaoVienInfo()`

3. **Hiển thị thông tin**
   - Thông tin giáo viên được hiển thị đầy đủ
   - Người dùng có thể mở rộng để xem lịch sử kinh nghiệm chi tiết

## Dữ liệu hiển thị

### Từ API GiaoVien
```json
{
  "maGiaoVien": 1,
  "maNguoiDung": 5,
  "hoTen": "Bà Nguyễn Thị Thương",
  "chuyenMon": "Đầu bếp trưởng - Chuyên gia ẩm thực cung đình Huế",
  "kinhNghiem": "20 năm kinh nghiệm",
  "lichSuKinhNghiem": "Bếp trưởng – Nhà hàng Cung Đình Huế (2008–2020)...",
  "hinhAnh": "giaovien1.png"
}
```

## Kiểm tra

### Các trường hợp cần test:
1. ✅ Hiển thị thông tin giáo viên khi có dữ liệu
2. ✅ Xử lý khi không có thông tin giáo viên (maGiaoVien = null)
3. ✅ Xử lý khi API lỗi
4. ✅ Hiển thị hình ảnh giáo viên
5. ✅ Mở rộng/thu gọn lịch sử kinh nghiệm
6. ✅ Không có lỗi syntax

## Kết quả
- ✅ Trang Booking đã hiển thị đầy đủ thông tin giáo viên
- ✅ Dữ liệu được load từ API giống như trang danh sách khóa học
- ✅ UI tương tác tốt với tính năng mở rộng/thu gọn
- ✅ Không có lỗi compile

## Ghi chú
- Hình ảnh giáo viên cần được đặt trong thư mục `res/drawable/`
- Tên file hình ảnh phải khớp với giá trị trong database (ví dụ: `giaovien1.png`)
- Nếu không tìm thấy hình ảnh, sẽ sử dụng hình mặc định `giaovien1`

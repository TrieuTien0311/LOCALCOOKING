# Cập nhật trang Booking - Lấy API dữ liệu lịch trình lớp học

## Tổng quan
Đã sửa lại trang đặt lịch (Booking.java) để lấy dữ liệu từ API thay vì nhận từ Intent. Điều này đảm bảo dữ liệu luôn chính xác và cập nhật theo thời gian thực.

## Các thay đổi

### 1. Backend (BE)

#### Tạo DTO mới: `CheckSeatsResponse.java`
- **Vị trí**: `BE/src/main/java/com/android/be/dto/CheckSeatsResponse.java`
- **Mục đích**: Wrap response từ stored procedure `sp_KiemTraChoTrong`
- **Các field**:
  - `maLichTrinh`: ID lịch trình
  - `maKhoaHoc`: ID khóa học
  - `tenKhoaHoc`: Tên khóa học
  - `tongCho`: Tổng số chỗ
  - `daDat`: Số người đã đặt
  - `conTrong`: Số chỗ còn trống
  - `trangThai`: Trạng thái ("Hết Chỗ", "Sắp Hết", "Còn Nhiều")

#### Cập nhật Controller: `LichTrinhLopHocController.java`
- **Endpoint**: `GET /api/lichtrinh/check-seats`
- **Parameters**:
  - `maLichTrinh`: ID lịch trình (required)
  - `ngayThamGia`: Ngày tham gia (format: yyyy-MM-dd) (required)
- **Response**: `CheckSeatsResponse` (JSON)
- **Xử lý**: Parse kết quả từ stored procedure và trả về DTO

### 2. Frontend (FE)

#### Cập nhật Model: `CheckSeatsResponse.java`
- **Vị trí**: `FE/app/src/main/java/com/example/localcooking_v3t/model/CheckSeatsResponse.java`
- **Thay đổi**: Thêm các field mới để khớp với backend response
- **Backward compatibility**: Giữ method `getSoChoConLai()` để tương thích với code cũ

#### Cập nhật Activity: `Booking.java`
- **Vị trí**: `FE/app/src/main/java/com/example/localcooking_v3t/Booking.java`
- **Các thay đổi chính**:

##### a. Thêm biến mới
```java
private LichTrinhLopHoc lichTrinhLopHoc; // Lưu thông tin lịch trình từ API
private int soLuongConLai = 0; // Khởi tạo = 0, sẽ lấy từ API
```

##### b. Thêm method `loadLichTrinhData()`
- Gọi API `getLichTrinhById()` để lấy thông tin chi tiết lịch trình
- Cập nhật thông tin `thoiGian` và `diaDiem` nếu chưa có từ Intent
- Sau đó gọi `checkAvailableSeats()` để kiểm tra số chỗ trống

##### c. Thêm method `checkAvailableSeats()`
- Gọi API `checkAvailableSeats()` với `maLichTrinh` và `ngayThamGia`
- Cập nhật `soLuongConLai` từ response
- Hiển thị thông tin lên UI
- Disable button "Đặt lịch" nếu hết chỗ

##### d. Cập nhật flow trong `onCreate()`
```java
// Cũ: displayBookingInfo() ngay sau receiveDataFromIntent()
// Mới: loadLichTrinhData() -> checkAvailableSeats() -> displayBookingInfo()
```

## Luồng hoạt động mới

1. **Khởi tạo Activity**
   - Nhận data cơ bản từ Intent (maKhoaHoc, maLichTrinh, tenKhoaHoc, ngayThamGia, giaTien)
   - Ánh xạ views
   - Gọi `loadLichTrinhData()`

2. **Load dữ liệu lịch trình**
   - Call API: `GET /api/lichtrinh/{maLichTrinh}`
   - Nhận response: `LichTrinhLopHoc`
   - Cập nhật thông tin `thoiGian`, `diaDiem` nếu chưa có
   - Gọi `checkAvailableSeats()`

3. **Kiểm tra số chỗ trống**
   - Call API: `GET /api/lichtrinh/check-seats?maLichTrinh={id}&ngayThamGia={date}`
   - Nhận response: `CheckSeatsResponse`
   - Cập nhật `soLuongConLai`
   - Gọi `displayBookingInfo()`

4. **Hiển thị thông tin**
   - Hiển thị tên khóa học, thời gian, ngày, địa điểm
   - Hiển thị số chỗ còn trống
   - Enable/disable button "Đặt lịch" dựa trên số chỗ trống

## API Endpoints sử dụng

### 1. Lấy thông tin lịch trình
```
GET /api/lichtrinh/{id}
Response: LichTrinhLopHoc
```

### 2. Kiểm tra số chỗ trống
```
GET /api/lichtrinh/check-seats?maLichTrinh={id}&ngayThamGia={date}
Response: CheckSeatsResponse
{
  "success": true,
  "message": "Kiểm tra chỗ trống thành công",
  "maLichTrinh": 1,
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
  "tongCho": 20,
  "daDat": 5,
  "conTrong": 15,
  "trangThai": "Còn Nhiều"
}
```

## Lợi ích

1. **Dữ liệu chính xác**: Luôn lấy dữ liệu mới nhất từ database
2. **Real-time**: Số chỗ trống được cập nhật theo thời gian thực
3. **Tránh lỗi**: Không phụ thuộc vào dữ liệu từ Intent có thể bị sai
4. **Dễ bảo trì**: Logic rõ ràng, dễ debug và mở rộng

## Testing

### Test case 1: Lớp còn nhiều chỗ
- Chọn lớp có nhiều chỗ trống (> 5)
- Kiểm tra: Button "Đặt lịch" enabled, hiển thị đúng số chỗ trống

### Test case 2: Lớp sắp hết chỗ
- Chọn lớp có ít chỗ trống (1-5)
- Kiểm tra: Button "Đặt lịch" enabled, hiển thị cảnh báo

### Test case 3: Lớp hết chỗ
- Chọn lớp đã hết chỗ (conTrong = 0)
- Kiểm tra: Button "Đặt lịch" disabled, hiển thị "Hết chỗ"

### Test case 4: Lỗi kết nối
- Tắt backend hoặc mất mạng
- Kiểm tra: Hiển thị thông báo lỗi, vẫn cho phép thao tác với dữ liệu từ Intent

## Build & Deploy

### Backend
```bash
cd BE
./gradlew clean build
./gradlew bootRun
```

### Frontend
- Build lại project trong Android Studio
- Chạy app trên emulator hoặc thiết bị thật
- Test các tính năng đặt lịch

## Notes

- Đảm bảo backend đang chạy trước khi test frontend
- Kiểm tra URL trong `RetrofitClient.java` đã đúng chưa
- Nếu test trên thiết bị thật, đảm bảo cùng mạng với backend

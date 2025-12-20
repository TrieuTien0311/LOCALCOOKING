# 🧪 Hướng Dẫn Test API Thông Báo

## ✅ Các File Đã Cập Nhật

### Backend (BE)
- `ThongBaoController.java` - Thêm 12 API endpoints
- `ThongBaoService.java` - Thêm các methods xử lý
- `ThongBaoRepository.java` - Thêm các queries
- `ThongBaoMapper.java` - Format thời gian tự động

### Frontend Android (FE)
- `model/ThongBaoDTO.java` - Model mới
- `model/UnreadCountResponse.java` - Model mới
- `model/MessageResponse.java` - Model mới
- `api/ApiService.java` - Thêm 8 API endpoints
- `Notice.java` - Cập nhật hỗ trợ API
- `NoticeFragment.java` - Tích hợp gọi API
- `NoticesAdapter.java` - Cập nhật load ảnh

---

## 🚀 Các Bước Test

### Bước 1: Chạy SQL để thêm dữ liệu mẫu

Mở SQL Server Management Studio và chạy file `INSERT_THONGBAO_DATA.sql`:

```sql
USE DatLichHocNauAn;
GO

-- Xóa dữ liệu cũ
DELETE FROM ThongBao;
GO

-- Thêm dữ liệu mẫu cho user ID = 4
INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao, hinhAnh, daDoc, ngayTao) VALUES
(4, N'Lớp học sắp diễn ra', N'Lớp "Ẩm thực phố cổ Hà Nội" của bạn sẽ bắt đầu vào ngày mai lúc 17:30.', N'LichHoc', N'hue.jpg', 0, DATEADD(MINUTE, -15, GETDATE())),
(4, N'Ưu đãi đặc biệt', N'Giảm 20% cho tất cả các lớp học trong tháng 10! Sử dụng mã: COOK10.', N'UuDai', N'voucher.png', 0, DATEADD(HOUR, -3, GETDATE())),
(4, N'Đặt lịch thành công', N'Chúc mừng! Bạn đã đặt chỗ thành công cho lớp "Ẩm thực phố cổ Hà Nội".', N'DatLich', N'hue.jpg', 1, DATEADD(HOUR, -1, GETDATE()));
GO
```

### Bước 2: Chạy Backend

```bash
cd BE
./gradlew bootRun
```

### Bước 3: Test API với Postman/Browser

```
# Lấy tất cả thông báo của user ID = 4
GET http://localhost:8080/api/thongbao/user/4

# Đếm số thông báo chưa đọc
GET http://localhost:8080/api/thongbao/user/4/unread-count

# Đánh dấu thông báo ID = 1 đã đọc
PUT http://localhost:8080/api/thongbao/1/mark-read
```

### Bước 4: Chạy Android App

1. Build và chạy app trên máy ảo hoặc điện thoại
2. Đăng nhập với tài khoản có ID = 4 (hoặc tài khoản đã có thông báo)
3. Vào tab Thông Báo
4. Kiểm tra danh sách thông báo hiển thị

---

## 🔍 Debug

### Kiểm tra Log Android

Mở Logcat và filter theo tag `NoticeFragment`:

```
D/NoticeFragment: maNguoiDung: 4
D/NoticeFragment: Loading thông báo cho user: 4
D/NoticeFragment: Response code: 200
D/NoticeFragment: Nhận được 3 thông báo
```

### Kiểm tra kết nối

Nếu không kết nối được:
1. Kiểm tra IP trong `RetrofitClient.java`
2. Đảm bảo backend đang chạy
3. Kiểm tra firewall

---

## 📱 Tài Khoản Test

| User ID | Email | Mật khẩu |
|---------|-------|----------|
| 4 | thaovyn0312@gmail.com | hv123 |
| 5 | nguyentrieutien2005py@gmail.com | hv123 |
| 6 | nguyenthithuong15112005@gmail.com | hv123 |

---

## ✅ Kết Quả Mong Đợi

1. ✅ Danh sách thông báo hiển thị từ server
2. ✅ Thời gian hiển thị dạng "X phút trước"
3. ✅ Phân biệt đã đọc/chưa đọc bằng màu sắc
4. ✅ Click vào thông báo sẽ đánh dấu đã đọc
5. ✅ Nếu không có dữ liệu từ server, hiển thị dữ liệu mẫu

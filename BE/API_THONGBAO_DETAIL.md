# 📢 API Thông Báo - Chi Tiết

## 🎯 Tổng Quan

API Thông Báo cung cấp đầy đủ chức năng quản lý thông báo cho người dùng, bao gồm:
- Lấy danh sách thông báo
- Đánh dấu đã đọc/chưa đọc
- Đếm số thông báo chưa đọc
- Lọc theo loại thông báo
- Xóa thông báo

---

## 🔗 Base URL
```
http://localhost:8080/api/thongbao
```

---

## 📋 Danh Sách API Endpoints

### 1. Lấy Tất Cả Thông Báo
```http
GET /api/thongbao
```

**Response:**
```json
[
  {
    "maThongBao": 1,
    "tieuDeTB": "Lớp học sắp diễn ra",
    "noiDungTB": "Lớp \"Ẩm thực phố cổ Hà Nội\" của bạn sẽ bắt đầu vào ngày mai. Đừng quên nhé!",
    "thoiGianTB": "15 phút trước",
    "anhTB": "hue.jpg",
    "trangThai": false,
    "loaiThongBao": "LichHoc"
  }
]
```

---

### 2. Lấy Thông Báo Theo ID
```http
GET /api/thongbao/{id}
```

**Ví dụ:**
```
GET /api/thongbao/1
```

**Response:**
```json
{
  "maThongBao": 1,
  "tieuDeTB": "Lớp học sắp diễn ra",
  "noiDungTB": "Lớp \"Ẩm thực phố cổ Hà Nội\" của bạn sẽ bắt đầu vào ngày mai. Đừng quên nhé!",
  "thoiGianTB": "15 phút trước",
  "anhTB": "hue.jpg",
  "trangThai": false,
  "loaiThongBao": "LichHoc"
}
```

---

### 3. Lấy Thông Báo Của Người Dùng
```http
GET /api/thongbao/user/{maNguoiNhan}
```

**Ví dụ:**
```
GET /api/thongbao/user/4
```

**Mô tả:** Lấy tất cả thông báo của người dùng, sắp xếp theo thời gian mới nhất.

**Response:**
```json
[
  {
    "maThongBao": 1,
    "tieuDeTB": "Lớp học sắp diễn ra",
    "noiDungTB": "Lớp \"Ẩm thực phố cổ Hà Nội\" của bạn sẽ bắt đầu vào ngày mai. Đừng quên nhé!",
    "thoiGianTB": "15 phút trước",
    "anhTB": "hue.jpg",
    "trangThai": false,
    "loaiThongBao": "LichHoc"
  },
  {
    "maThongBao": 2,
    "tieuDeTB": "Đặt lịch thành công",
    "noiDungTB": "Chúc mừng! Bạn đã đặt chỗ thành công cho lớp \"Ẩm thực phố cổ Hà Nội\" vào 9:00 ngày 20 tháng 10 năm 2025.",
    "thoiGianTB": "1 giờ trước",
    "anhTB": "hue.jpg",
    "trangThai": true,
    "loaiThongBao": "DatLich"
  }
]
```

---

### 4. Lấy Thông Báo Chưa Đọc
```http
GET /api/thongbao/user/{maNguoiNhan}/unread
```

**Ví dụ:**
```
GET /api/thongbao/user/4/unread
```

**Mô tả:** Chỉ lấy các thông báo chưa đọc (trangThai = false).

**Response:**
```json
[
  {
    "maThongBao": 1,
    "tieuDeTB": "Lớp học sắp diễn ra",
    "noiDungTB": "Lớp \"Ẩm thực phố cổ Hà Nội\" của bạn sẽ bắt đầu vào ngày mai. Đừng quên nhé!",
    "thoiGianTB": "15 phút trước",
    "anhTB": "hue.jpg",
    "trangThai": false,
    "loaiThongBao": "LichHoc"
  }
]
```

---

### 5. Đếm Số Thông Báo Chưa Đọc
```http
GET /api/thongbao/user/{maNguoiNhan}/unread-count
```

**Ví dụ:**
```
GET /api/thongbao/user/4/unread-count
```

**Mô tả:** Trả về số lượng thông báo chưa đọc (dùng để hiển thị badge).

**Response:**
```json
{
  "count": 3
}
```

---

### 6. Lấy Thông Báo Theo Loại
```http
GET /api/thongbao/user/{maNguoiNhan}/type/{loaiThongBao}
```

**Ví dụ:**
```
GET /api/thongbao/user/4/type/DatLich
```

**Các loại thông báo:**
- `DatLich` - Thông báo đặt lịch
- `LichHoc` - Thông báo lớp học
- `UuDai` - Thông báo ưu đãi
- `HuyLop` - Thông báo hủy lớp
- `ChungChi` - Thông báo chứng chỉ
- `HeThong` - Thông báo hệ thống

**Response:**
```json
[
  {
    "maThongBao": 2,
    "tieuDeTB": "Đặt lịch thành công",
    "noiDungTB": "Chúc mừng! Bạn đã đặt chỗ thành công cho lớp \"Ẩm thực phố cổ Hà Nội\".",
    "thoiGianTB": "1 giờ trước",
    "anhTB": "hue.jpg",
    "trangThai": true,
    "loaiThongBao": "DatLich"
  }
]
```

---

### 7. Tạo Thông Báo Mới
```http
POST /api/thongbao
```

**Request Body:**
```json
{
  "maNguoiNhan": 4,
  "tieuDe": "Ưu đãi đặc biệt",
  "noiDung": "Giảm 20% cho tất cả các lớp học trong tháng 10! Sử dụng mã: COOK10.",
  "loaiThongBao": "UuDai",
  "hinhAnh": "voucher.png",
  "daDoc": false
}
```

**Response:**
```json
{
  "maThongBao": 6,
  "maNguoiNhan": 4,
  "tieuDe": "Ưu đãi đặc biệt",
  "noiDung": "Giảm 20% cho tất cả các lớp học trong tháng 10! Sử dụng mã: COOK10.",
  "loaiThongBao": "UuDai",
  "hinhAnh": "voucher.png",
  "daDoc": false,
  "ngayTao": "2025-12-20T10:30:00"
}
```

---

### 8. Cập Nhật Thông Báo
```http
PUT /api/thongbao/{id}
```

**Request Body:**
```json
{
  "maNguoiNhan": 4,
  "tieuDe": "Ưu đãi đặc biệt (Cập nhật)",
  "noiDung": "Giảm 30% cho tất cả các lớp học trong tháng 10!",
  "loaiThongBao": "UuDai",
  "hinhAnh": "voucher.png",
  "daDoc": false
}
```

---

### 9. Đánh Dấu Đã Đọc
```http
PUT /api/thongbao/{id}/mark-read
```

**Ví dụ:**
```
PUT /api/thongbao/1/mark-read
```

**Mô tả:** Đánh dấu một thông báo là đã đọc.

**Response:**
```json
{
  "maThongBao": 1,
  "maNguoiNhan": 4,
  "tieuDe": "Lớp học sắp diễn ra",
  "noiDung": "Lớp \"Ẩm thực phố cổ Hà Nội\" của bạn sẽ bắt đầu vào ngày mai.",
  "loaiThongBao": "LichHoc",
  "hinhAnh": "hue.jpg",
  "daDoc": true,
  "ngayTao": "2025-12-20T09:15:00"
}
```

---

### 10. Đánh Dấu Tất Cả Đã Đọc
```http
PUT /api/thongbao/user/{maNguoiNhan}/mark-all-read
```

**Ví dụ:**
```
PUT /api/thongbao/user/4/mark-all-read
```

**Mô tả:** Đánh dấu tất cả thông báo của người dùng là đã đọc.

**Response:**
```json
{
  "message": "Đã đánh dấu tất cả thông báo là đã đọc"
}
```

---

### 11. Xóa Thông Báo
```http
DELETE /api/thongbao/{id}
```

**Ví dụ:**
```
DELETE /api/thongbao/1
```

**Response:** HTTP 204 No Content

---

### 12. Xóa Tất Cả Thông Báo Đã Đọc
```http
DELETE /api/thongbao/user/{maNguoiNhan}/delete-read
```

**Ví dụ:**
```
DELETE /api/thongbao/user/4/delete-read
```

**Mô tả:** Xóa tất cả thông báo đã đọc của người dùng.

**Response:**
```json
{
  "message": "Đã xóa tất cả thông báo đã đọc"
}
```

---

## 📊 Cấu Trúc Dữ Liệu

### ThongBaoDTO
```typescript
{
  maThongBao: Integer,      // ID thông báo
  tieuDeTB: String,         // Tiêu đề
  noiDungTB: String,        // Nội dung chi tiết
  thoiGianTB: String,       // Thời gian (format: "X phút trước")
  anhTB: String,            // Đường dẫn ảnh
  trangThai: Boolean,       // true = đã đọc, false = chưa đọc
  loaiThongBao: String      // Loại thông báo
}
```

---

## 🎨 Các Loại Thông Báo

| Loại | Mô Tả | Icon Gợi Ý |
|------|-------|------------|
| `DatLich` | Thông báo đặt lịch thành công | ✅ |
| `LichHoc` | Nhắc nhở lớp học sắp diễn ra | 📅 |
| `UuDai` | Thông báo ưu đãi, khuyến mãi | 🎁 |
| `HuyLop` | Thông báo hủy lớp học | ❌ |
| `ChungChi` | Thông báo chứng chỉ hoàn thành | 🏆 |
| `HeThong` | Thông báo hệ thống | ℹ️ |

---

## 🧪 Test API với Postman

### 1. Lấy thông báo của user ID = 4
```
GET http://localhost:8080/api/thongbao/user/4
```

### 2. Đếm số thông báo chưa đọc
```
GET http://localhost:8080/api/thongbao/user/4/unread-count
```

### 3. Đánh dấu thông báo ID = 1 đã đọc
```
PUT http://localhost:8080/api/thongbao/1/mark-read
```

### 4. Lấy thông báo ưu đãi
```
GET http://localhost:8080/api/thongbao/user/4/type/UuDai
```

---

## 💡 Gợi Ý Tích Hợp Android

### 1. Thêm vào ApiService.java
```java
// Lấy thông báo của người dùng
@GET("api/thongbao/user/{maNguoiNhan}")
Call<List<ThongBaoDTO>> getThongBaoByUser(@Path("maNguoiNhan") Integer maNguoiNhan);

// Đếm số thông báo chưa đọc
@GET("api/thongbao/user/{maNguoiNhan}/unread-count")
Call<UnreadCountResponse> getUnreadCount(@Path("maNguoiNhan") Integer maNguoiNhan);

// Đánh dấu đã đọc
@PUT("api/thongbao/{id}/mark-read")
Call<ThongBao> markAsRead(@Path("id") Integer id);

// Đánh dấu tất cả đã đọc
@PUT("api/thongbao/user/{maNguoiNhan}/mark-all-read")
Call<MessageResponse> markAllAsRead(@Path("maNguoiNhan") Integer maNguoiNhan);
```

### 2. Tạo Model ThongBaoDTO.java
```java
public class ThongBaoDTO {
    private Integer maThongBao;
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private String anhTB;
    private Boolean trangThai;
    private String loaiThongBao;
    
    // Getters & Setters
}
```

### 3. Cập nhật NoticeFragment.java
```java
private void loadThongBaoFromAPI() {
    Integer maNguoiDung = SharedPrefManager.getInstance(getContext()).getUserId();
    
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
    Call<List<ThongBaoDTO>> call = apiService.getThongBaoByUser(maNguoiDung);
    
    call.enqueue(new Callback<List<ThongBaoDTO>>() {
        @Override
        public void onResponse(Call<List<ThongBaoDTO>> call, Response<List<ThongBaoDTO>> response) {
            if (response.isSuccessful() && response.body() != null) {
                List<ThongBaoDTO> thongBaoList = response.body();
                // Cập nhật RecyclerView
                adapter.updateNotices(thongBaoList);
            }
        }
        
        @Override
        public void onFailure(Call<List<ThongBaoDTO>> call, Throwable t) {
            Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

---

## ✅ Tính Năng Đã Hoàn Thành

- ✅ Lấy danh sách thông báo theo người dùng
- ✅ Lọc thông báo chưa đọc
- ✅ Đếm số thông báo chưa đọc (hiển thị badge)
- ✅ Đánh dấu đã đọc/chưa đọc
- ✅ Lọc theo loại thông báo
- ✅ Xóa thông báo
- ✅ Format thời gian tự động ("X phút trước")
- ✅ Tương thích 100% với Android app hiện tại

---

## 🎉 Kết Luận

API Thông Báo đã được xây dựng hoàn chỉnh với đầy đủ tính năng cần thiết cho ứng dụng Android. Backend tự động xử lý format thời gian và sắp xếp theo thứ tự mới nhất, giúp Android app dễ dàng tích hợp.

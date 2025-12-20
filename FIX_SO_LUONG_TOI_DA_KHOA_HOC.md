# Fix hiển thị số lượng tối đa trong danh sách khóa học

## Vấn đề
Danh sách khóa học không hiển thị số lượng chỗ còn trống (số suất) vì backend không trả về thông tin `conTrong` khi gọi API `/api/khoahoc`.

## Nguyên nhân
1. **Backend:** Method `convertLichTrinhToDTO()` trong `KhoaHocService` không tính và set giá trị `conTrong`
2. **Android:** Model `LichTrinhLopHoc` không có field `conTrong` để nhận dữ liệu từ backend DTO

## Giải pháp đã thực hiện

### 1. Backend - KhoaHocService.java

#### Thêm dependency
```java
private final DatLichRepository datLichRepository;
```

#### Cập nhật method `convertLichTrinhToDTO()`
```java
private LichTrinhLopHocDTO convertLichTrinhToDTO(LichTrinhLopHoc lichTrinh) {
    // ... existing code ...
    
    // Tính số chỗ còn trống cho ngày mai (hoặc ngày gần nhất)
    LocalDate ngayKiemTra = LocalDate.now().plusDays(1);
    Integer soLuongDaDat = datLichRepository.countBookedSeats(
        lichTrinh.getMaLichTrinh(), 
        ngayKiemTra
    );
    Integer soLuongToiDa = lichTrinh.getSoLuongToiDa() != null ? 
        lichTrinh.getSoLuongToiDa() : 0;
    Integer conTrong = soLuongToiDa - soLuongDaDat;
    
    dto.setSoLuongHienTai(soLuongDaDat);
    dto.setConTrong(conTrong);
    
    // Set trạng thái hiển thị
    if (conTrong <= 0) {
        dto.setTrangThaiHienThi("Hết chỗ");
    } else if (conTrong <= 3) {
        dto.setTrangThaiHienThi("Sắp hết chỗ");
    } else {
        dto.setTrangThaiHienThi("Còn chỗ");
    }
    
    return dto;
}
```

**Giải thích:**
- Tính số chỗ còn trống cho **ngày mai** (vì không có ngày cụ thể khi gọi `/api/khoahoc`)
- Sử dụng `DatLichRepository.countBookedSeats()` để đếm số người đã đặt
- Tính `conTrong = soLuongToiDa - soLuongDaDat`
- Set trạng thái hiển thị dựa trên số chỗ còn trống

### 2. Android - LichTrinhLopHoc.java

#### Thêm field `conTrong`
```java
private Integer conTrong; // Alias cho soChoConTrong (từ backend DTO)
```

#### Cập nhật getter/setter
```java
public Integer getSoChoConTrong() { 
    // Ưu tiên lấy từ conTrong (từ backend DTO), nếu không có thì lấy soChoConTrong
    return conTrong != null ? conTrong : soChoConTrong; 
}

public Integer getConTrong() { return conTrong; }

public void setConTrong(Integer conTrong) { 
    this.conTrong = conTrong;
    // Đồng bộ với soChoConTrong
    this.soChoConTrong = conTrong;
}
```

**Giải thích:**
- Backend DTO trả về field `conTrong`
- Android model có cả `soChoConTrong` và `conTrong` để tương thích
- `getSoChoConTrong()` ưu tiên lấy từ `conTrong` (từ API mới)
- `setConTrong()` đồng bộ cả 2 field

## Luồng hoạt động

### Khi gọi API `/api/khoahoc`
1. Backend lấy danh sách khóa học từ database
2. Với mỗi khóa học, lấy danh sách lịch trình
3. Với mỗi lịch trình:
   - Tính số người đã đặt cho **ngày mai**
   - Tính số chỗ còn trống = `soLuongToiDa - soLuongDaDat`
   - Set vào DTO
4. Android nhận dữ liệu và hiển thị

### Hiển thị trong ClassAdapter
```java
// Hiển thị số suất còn lại
if (lopHoc.getSuat() != null) {
    holder.txtSuat.setText("Còn " + lopHoc.getSuat() + " suất");
}
```

`lopHoc.getSuat()` → `khoaHoc.getLichTrinhList().get(0).getSoChoConTrong()` → lấy từ `conTrong`

## Lưu ý quan trọng

### 1. Ngày tính số chỗ trống
- API `/api/khoahoc` (không có tham số ngày): tính cho **ngày mai**
- API `/api/khoahoc/search?ngayTimKiem=...`: tính cho **ngày được chỉ định**

### 2. Tại sao chọn ngày mai?
- Người dùng thường đặt lịch cho các ngày sắp tới
- Ngày hôm nay có thể đã qua giờ học
- Ngày mai là lựa chọn hợp lý nhất cho "số chỗ còn trống mặc định"

### 3. Trạng thái hiển thị
- **"Còn chỗ"**: > 3 chỗ trống
- **"Sắp hết chỗ"**: 1-3 chỗ trống
- **"Hết chỗ"**: 0 chỗ trống

## Kiểm tra

### Test cases cần kiểm tra:
1. ✅ Hiển thị số suất trong danh sách khóa học (HomeFragment)
2. ✅ Hiển thị số suất trong trang tìm kiếm (ClassesFragment)
3. ✅ Số suất chính xác với database
4. ✅ Cập nhật số suất khi có người đặt mới
5. ✅ Xử lý khi không có lịch trình
6. ✅ Xử lý khi soLuongToiDa = null

## Kết quả
- ✅ Backend tính và trả về số chỗ còn trống
- ✅ Android nhận và hiển thị đúng số suất
- ✅ Danh sách khóa học hiển thị "Còn X suất"
- ✅ Không có lỗi compile

## API Response mẫu

### Trước khi fix
```json
{
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực Huế",
  "lichTrinhList": [
    {
      "maLichTrinh": 1,
      "soLuongToiDa": 20,
      "conTrong": null  // ❌ Không có giá trị
    }
  ]
}
```

### Sau khi fix
```json
{
  "maKhoaHoc": 1,
  "tenKhoaHoc": "Ẩm thực Huế",
  "lichTrinhList": [
    {
      "maLichTrinh": 1,
      "soLuongToiDa": 20,
      "soLuongHienTai": 12,
      "conTrong": 8,  // ✅ Có giá trị
      "trangThaiHienThi": "Còn chỗ"
    }
  ]
}
```

## Files đã thay đổi
1. `BE/src/main/java/com/android/be/service/KhoaHocService.java`
   - Thêm dependency `DatLichRepository`
   - Cập nhật `convertLichTrinhToDTO()` để tính số chỗ trống

2. `FE/app/src/main/java/com/example/localcooking_v3t/model/LichTrinhLopHoc.java`
   - Thêm field `conTrong`
   - Cập nhật getter/setter để đồng bộ 2 field

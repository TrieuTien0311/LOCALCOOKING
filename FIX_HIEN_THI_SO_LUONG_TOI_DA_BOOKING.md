# Fix hiển thị số lượng tối đa trong trang Booking

## Vấn đề
Trang Booking (Đặt lịch) không hiển thị số lượng tối đa của lớp học, chỉ hiển thị số chỗ còn trống.

## Nguyên nhân
1. Layout XML không có TextView để hiển thị số lượng tối đa
2. Code chỉ hiển thị "Còn X suất" mà không có thông tin tổng số chỗ

## Giải pháp đã thực hiện

### 1. Cập nhật Layout XML (activity_booking.xml)

Thêm TextView mới để hiển thị sức chứa (số lượng tối đa):

```xml
<LinearLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginLeft="10dp"
    android:layout_marginTop="3dp"
    android:layout_marginRight="10dp"
    android:orientation="horizontal">

    <TextView
        android:id="@+id/textView69"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="Sức chứa:"
        android:textColor="@color/black"
        android:textSize="13sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/txt_SucChua_DL"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text=" 20 người"
        android:textColor="#7A7777"
        android:textSize="12sp" />
</LinearLayout>
```

**Vị trí:** Ngay dưới phần "Địa điểm"

### 2. Cập nhật Booking.java

#### Method `updateUI()`
Cập nhật để hiển thị cả số chỗ còn trống và tổng số chỗ:

```java
private void updateUI() {
    txtSoLuongDat.setText(String.valueOf(soLuong));
    
    // Hiển thị số chỗ còn trống và tổng số chỗ
    if (lichTrinhLopHoc != null && lichTrinhLopHoc.getSoLuongToiDa() != null) {
        int soLuongToiDa = lichTrinhLopHoc.getSoLuongToiDa();
        txtGioiHan.setText("Còn " + soLuongConLai + "/" + soLuongToiDa + " suất");
    } else {
        txtGioiHan.setText("Còn " + soLuongConLai + " suất");
    }
}
```

**Thay đổi:**
- Trước: `"Còn 8 suất"`
- Sau: `"Còn 8/20 suất"` (8 chỗ trống / 20 chỗ tối đa)

#### Method `displayBookingInfo()`
Thêm code để hiển thị sức chứa:

```java
// Sức chứa (số lượng tối đa)
TextView txtSucChua = findViewById(R.id.txt_SucChua_DL);
if (txtSucChua != null && lichTrinhLopHoc != null && 
    lichTrinhLopHoc.getSoLuongToiDa() != null) {
    txtSucChua.setText(" " + lichTrinhLopHoc.getSoLuongToiDa() + " người");
}
```

## Kết quả hiển thị

### Trước khi fix
```
Địa điểm: 23 Lê Duẩn - Đà Nẵng

[Nút giảm] [1] [Nút tăng]
Còn 8 suất
```

### Sau khi fix
```
Địa điểm: 23 Lê Duẩn - Đà Nẵng
Sức chứa: 20 người

[Nút giảm] [1] [Nút tăng]
Còn 8/20 suất
```

## Luồng dữ liệu

1. **Load lịch trình từ API**
   - Gọi `getLichTrinhById(maLichTrinh)`
   - Nhận `LichTrinhLopHoc` với field `soLuongToiDa`

2. **Kiểm tra số chỗ trống**
   - Gọi `checkAvailableSeats(maLichTrinh, ngayThamGia)`
   - Nhận `CheckSeatsResponse` với field `conTrong`
   - Set `soLuongConLai = conTrong`

3. **Hiển thị thông tin**
   - `txt_SucChua_DL`: Hiển thị `soLuongToiDa` (VD: "20 người")
   - `txt_GioiHan_DL`: Hiển thị `soLuongConLai/soLuongToiDa` (VD: "Còn 8/20 suất")

## Các trường hợp xử lý

### 1. Có đầy đủ dữ liệu
```
Sức chứa: 20 người
Còn 8/20 suất
```

### 2. Không có thông tin lịch trình
```
Sức chứa: (không hiển thị)
Còn 8 suất
```

### 3. Hết chỗ
```
Sức chứa: 20 người
Còn 0/20 suất
[Button "Hết chỗ" - disabled]
```

## Lợi ích

1. **Thông tin rõ ràng hơn:** Người dùng biết tổng số chỗ và số chỗ còn trống
2. **Dễ đánh giá:** Thấy được tỷ lệ lấp đầy (8/20 = 40% đã đặt)
3. **Tăng tính minh bạch:** Hiển thị đầy đủ thông tin về sức chứa lớp học

## Files đã thay đổi

1. **FE/app/src/main/res/layout/activity_booking.xml**
   - Thêm LinearLayout chứa TextView `txt_SucChua_DL`
   - Vị trí: Dưới phần "Địa điểm"

2. **FE/app/src/main/java/com/example/localcooking_v3t/Booking.java**
   - Cập nhật `updateUI()`: Hiển thị "Còn X/Y suất"
   - Cập nhật `displayBookingInfo()`: Hiển thị sức chứa

## Kiểm tra

### Test cases:
1. ✅ Hiển thị sức chứa khi có dữ liệu lịch trình
2. ✅ Hiển thị "Còn X/Y suất" với đầy đủ thông tin
3. ✅ Xử lý khi không có thông tin lịch trình
4. ✅ Xử lý khi soLuongToiDa = null
5. ✅ Cập nhật đúng khi tăng/giảm số lượng đặt

## Ghi chú

- Số lượng tối đa lấy từ `LichTrinhLopHoc.soLuongToiDa`
- Số chỗ còn trống lấy từ API `checkAvailableSeats`
- Hiển thị "Sức chứa" giúp người dùng biết quy mô lớp học
- Format "X/Y suất" giúp dễ so sánh và đánh giá

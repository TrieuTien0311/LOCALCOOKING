# Các Bước Thực Hiện Hiển Thị Slide Ảnh

## ✅ Đã Hoàn Thành (Backend + Android Model)

1. ✅ **Backend**: Thêm `hinhAnhList` vào `KhoaHocDTO.java`
2. ✅ **Backend**: Load dữ liệu từ `HinhAnhKhoaHocService`
3. ✅ **Android**: Tạo class `HinhAnhKhoaHoc.java`
4. ✅ **Android**: Thêm field `hinhAnhList` vào `KhoaHoc.java`
5. ✅ **Android**: Thêm màu indicators vào `colors.xml`

## 📝 Cần Làm Tiếp (Cập Nhật Booking.java)

### Bước 1: Mở File Booking.java

Đường dẫn: `FE/app/src/main/java/com/example/localcooking_v3t/Booking.java`

### Bước 2: Thêm Import

Thêm vào đầu file (sau các import hiện tại):

```java
import com.example.localcooking_v3t.model.HinhAnhKhoaHoc;
import java.util.List;
```

### Bước 3: Thêm Biến Vào Class

Tìm dòng khai báo biến (sau `private ApiService apiService;`), thêm:

```java
// THÊM MỚI: Quản lý slide ảnh
private List<HinhAnhKhoaHoc> hinhAnhList;
private int currentImageIndex = 0;
private ImageView imMonAn;
private ImageView btnPre, btnNext;
private ImageView[] circles; // Mảng 5 indicator
```

### Bước 4: Cập Nhật onCreate()

Tìm method `onCreate()`, sau dòng `txtDiaDiemHeader = findViewById(R.id.txtDiaDiem);`, thêm:

```java
// Ánh xạ views cho slide ảnh
imMonAn = findViewById(R.id.im_MonAn_DL);
btnPre = findViewById(R.id.btnPre);
btnNext = findViewById(R.id.btnNext);

// Ánh xạ 5 circles
circles = new ImageView[5];
circles[0] = findViewById(R.id.circle1);
circles[1] = findViewById(R.id.circle2);
circles[2] = findViewById(R.id.circle3);
circles[3] = findViewById(R.id.circle4);
circles[4] = findViewById(R.id.circle5);

// Xử lý nút Previous
btnPre.setOnClickListener(v -> {
    if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
        currentImageIndex--;
        if (currentImageIndex < 0) {
            currentImageIndex = hinhAnhList.size() - 1; // Quay vòng
        }
        displayCurrentImage();
    }
});

// Xử lý nút Next
btnNext.setOnClickListener(v -> {
    if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
        currentImageIndex++;
        if (currentImageIndex >= hinhAnhList.size()) {
            currentImageIndex = 0; // Quay vòng
        }
        displayCurrentImage();
    }
});
```

### Bước 5: Thay Thế Method displayKhoaHocInfo()

Tìm method `displayKhoaHocInfo()` (khoảng dòng 700), **THAY THẾ TOÀN BỘ** bằng code sau:

```java
/**
 * Hiển thị thông tin khóa học (hình ảnh, giáo viên, mô tả)
 */
private void displayKhoaHocInfo() {
    if (khoaHoc == null) return;
    
    Log.d("BOOKING_UI", "=== Displaying KhoaHoc Info ===");
    
    // THAY ĐỔI: Hiển thị slide ảnh thay vì 1 ảnh
    if (khoaHoc.getHinhAnhList() != null && !khoaHoc.getHinhAnhList().isEmpty()) {
        // Có danh sách ảnh slide -> hiển thị slide
        hinhAnhList = khoaHoc.getHinhAnhList();
        currentImageIndex = 0;
        displayCurrentImage();
        
        // Hiển thị nút Pre/Next
        btnPre.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        
        Log.d("BOOKING_UI", "Loaded " + hinhAnhList.size() + " images for slide");
    } else if (khoaHoc.getHinhAnh() != null) {
        // Không có slide -> hiển thị ảnh banner
        int resId = khoaHoc.getHinhAnhResId(this);
        if (imMonAn != null) {
            imMonAn.setImageResource(resId);
        }
        
        // Ẩn nút Pre/Next
        if (btnPre != null) btnPre.setVisibility(View.GONE);
        if (btnNext != null) btnNext.setVisibility(View.GONE);
        
        // Ẩn tất cả circles
        for (ImageView circle : circles) {
            if (circle != null) circle.setVisibility(View.GONE);
        }
        
        Log.d("BOOKING_UI", "No slide images, showing banner: " + khoaHoc.getHinhAnh());
    }
    
    // Giới thiệu lớp học
    TextView txtGioiThieu = findViewById(R.id.textView49);
    if (txtGioiThieu != null && khoaHoc.getGioiThieu() != null) {
        txtGioiThieu.setText(khoaHoc.getGioiThieu());
        Log.d("BOOKING_UI", "Set gioi thieu");
    }
    
    // Điểm đánh giá
    TextView txtDiem = findViewById(R.id.txt_Diem_DL);
    if (txtDiem != null && khoaHoc.getSaoTrungBinh() != null) {
        txtDiem.setText(String.format("%.1f", khoaHoc.getSaoTrungBinh()));
    }
    
    // Số lượng đánh giá
    TextView txtSLDanhGia = findViewById(R.id.txt_SLDanhGia_DL);
    if (txtSLDanhGia != null && khoaHoc.getSoLuongDanhGia() != null) {
        txtSLDanhGia.setText("(" + khoaHoc.getSoLuongDanhGia() + " đánh giá)");
    }
    
    Log.d("BOOKING_UI", "KhoaHoc info displayed");
}
```

### Bước 6: Thêm 2 Method Mới

Thêm vào **CUỐI FILE** (trước dấu `}` cuối cùng của class):

```java
/**
 * Hiển thị ảnh hiện tại trong slide
 */
private void displayCurrentImage() {
    if (hinhAnhList == null || hinhAnhList.isEmpty()) return;
    
    // Hiển thị ảnh
    HinhAnhKhoaHoc currentImage = hinhAnhList.get(currentImageIndex);
    int resId = currentImage.getHinhAnhResId(this);
    if (imMonAn != null) {
        imMonAn.setImageResource(resId);
    }
    
    Log.d("BOOKING_UI", "Displaying image " + (currentImageIndex + 1) + "/" + hinhAnhList.size() + ": " + currentImage.getDuongDan());
    
    // Cập nhật indicators (circles)
    updateIndicators();
}

/**
 * Cập nhật trạng thái các indicator (circles)
 */
private void updateIndicators() {
    if (hinhAnhList == null || hinhAnhList.isEmpty()) return;
    
    int totalImages = hinhAnhList.size();
    
    for (int i = 0; i < circles.length; i++) {
        if (circles[i] == null) continue;
        
        if (i < totalImages) {
            // Hiển thị circle nếu có ảnh tương ứng
            circles[i].setVisibility(View.VISIBLE);
            
            // Đổi màu circle: active = #BA5632, inactive = #DCA790
            if (i == currentImageIndex) {
                circles[i].setColorFilter(getResources().getColor(R.color.active_indicator));
            } else {
                circles[i].setColorFilter(getResources().getColor(R.color.inactive_indicator));
            }
        } else {
            // Ẩn circle nếu không có ảnh
            circles[i].setVisibility(View.GONE);
        }
    }
}
```

## 🧪 Test

### 1. Build Project

```bash
cd FE
./gradlew clean build
```

### 2. Chạy App

- Mở khóa học "Ẩm thực phố cổ Hà Nội" (maKhoaHoc = 1)
- Kiểm tra có 2 ảnh slide
- Nhấn nút Next/Pre để chuyển ảnh
- Kiểm tra indicators (circles) đổi màu đúng

### 3. Kiểm Tra Log

```
BOOKING_UI: Loaded 2 images for slide
BOOKING_UI: Displaying image 1/2: am_thuc_pho_co_ha_noi_2.jpg
BOOKING_UI: Displaying image 2/2: am_thuc_pho_co_ha_noi_3.jpg
```

## 🐛 Troubleshooting

### Lỗi: Không tìm thấy ảnh

**Nguyên nhân**: Tên file trong database không khớp với tên file trong `res/drawable`

**Giải pháp**:
1. Kiểm tra tên file trong database: `am_thuc_pho_co_ha_noi_2.jpg`
2. Kiểm tra file trong `FE/app/src/main/res/drawable/`
3. Đảm bảo tên khớp nhau (không phân biệt hoa thường)

### Lỗi: Circles không đổi màu

**Nguyên nhân**: Chưa thêm màu vào `colors.xml`

**Giải pháp**: Kiểm tra file `FE/app/src/main/res/values/colors.xml` có 2 dòng:
```xml
<color name="active_indicator">#BA5632</color>
<color name="inactive_indicator">#DCA790</color>
```

### Lỗi: NullPointerException

**Nguyên nhân**: Chưa ánh xạ đúng views

**Giải pháp**: Kiểm tra lại các dòng `findViewById()` trong `onCreate()`

## 📚 Tài Liệu Tham Khảo

- `HUONG_DAN_HIEN_THI_HINH_ANH_KHOA_HOC.md` - Hướng dẫn backend
- `HUONG_DAN_HIEN_THI_ANH_ANDROID.md` - Hướng dẫn Android chi tiết
- `FE_BOOKING_UPDATE_CODE.java` - Code mẫu đầy đủ

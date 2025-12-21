# Hướng Dẫn Cập Nhật Booking.java - Chi Tiết Từng Bước

## ⚠️ VẤN ĐỀ HIỆN TẠI

File `Booking.java` **CHƯA CÓ CODE LOAD ẢNH SLIDE**. Bạn cần thêm code để:
1. Load danh sách ảnh từ API (`hinhAnhList`)
2. Hiển thị ảnh với nút Pre/Next
3. Cập nhật indicators (circles)

## 📝 CÁC BƯỚC THỰC HIỆN

### BƯỚC 1: Thêm Import (Đầu File)

Mở file `FE/app/src/main/java/com/example/localcooking_v3t/Booking.java`

Tìm dòng:
```java
import java.util.Date;
```

Thêm NGAY SAU dòng đó:
```java
import com.example.localcooking_v3t.model.HinhAnhKhoaHoc;
import java.util.List;
```

### BƯỚC 2: Thêm Biến Vào Class

Tìm dòng:
```java
private ApiService apiService;
```

Thêm NGAY SAU dòng đó:
```java
    
// THÊM MỚI: Quản lý slide ảnh
private List<HinhAnhKhoaHoc> hinhAnhList;
private int currentImageIndex = 0;
private ImageView imMonAn;
private ImageView btnPre, btnNext;
private ImageView[] circles; // Mảng 5 indicator
```

### BƯỚC 3: Thêm Code Vào onCreate()

Tìm dòng:
```java
txtDiaDiemHeader = findViewById(R.id.txtDiaDiem);
```

Thêm NGAY SAU dòng đó:
```java
        
// THÊM MỚI: Ánh xạ views cho slide ảnh
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

### BƯỚC 4: Thay Thế Method displayKhoaHocInfo()

Tìm method `displayKhoaHocInfo()` (khoảng dòng 650-700).

**XÓA TOÀN BỘ** method cũ từ:
```java
private void displayKhoaHocInfo() {
    if (khoaHoc == null) return;
    
    Log.d("BOOKING_UI", "=== Displaying KhoaHoc Info ===");
    
    // Hình ảnh món ăn
    ImageView imMonAn = findViewById(R.id.im_MonAn_DL);
    if (imMonAn != null && khoaHoc.getHinhAnh() != null) {
        int resId = khoaHoc.getHinhAnhResId(this);
        imMonAn.setImageResource(resId);
        Log.d("BOOKING_UI", "Set image: " + khoaHoc.getHinhAnh());
    }
    
    // ... phần còn lại
}
```

**THAY BẰNG** code mới:
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
        if (btnPre != null) btnPre.setVisibility(View.VISIBLE);
        if (btnNext != null) btnNext.setVisibility(View.VISIBLE);
        
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
        if (circles != null) {
            for (ImageView circle : circles) {
                if (circle != null) circle.setVisibility(View.GONE);
            }
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

### BƯỚC 5: Thêm 2 Method Mới Vào Cuối File

Tìm dòng **CUỐI CÙNG** của class (trước dấu `}` cuối):
```java
    }

}  // <-- Dấu } này là cuối class Booking
```

Thêm NGAY TRƯỚC dấu `}` cuối:
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
    if (circles == null) return;
    
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

## ✅ KIỂM TRA SAU KHI CẬP NHẬT

### 1. Build Project
```bash
cd FE
./gradlew clean build
```

### 2. Kiểm Tra Log
Khi chạy app, bạn sẽ thấy log:
```
BOOKING_UI: === Displaying KhoaHoc Info ===
BOOKING_UI: Loaded 2 images for slide
BOOKING_UI: Displaying image 1/2: am_thuc_pho_co_ha_noi_2.jpg
```

### 3. Test Trên App
- Mở khóa học "Ẩm thực phố cổ Hà Nội"
- Kiểm tra có 2 ảnh hiển thị
- Nhấn nút Next → chuyển sang ảnh 2
- Nhấn nút Pre → quay lại ảnh 1
- Kiểm tra circles đổi màu đúng

## 🐛 NẾU GẶP LỖI

### Lỗi: Cannot resolve symbol 'HinhAnhKhoaHoc'
**Giải pháp**: Kiểm tra đã tạo file `HinhAnhKhoaHoc.java` chưa:
```
FE/app/src/main/java/com/example/localcooking_v3t/model/HinhAnhKhoaHoc.java
```

### Lỗi: Cannot resolve symbol 'active_indicator'
**Giải pháp**: Kiểm tra file `colors.xml` đã có 2 màu:
```xml
<color name="active_indicator">#BA5632</color>
<color name="inactive_indicator">#DCA790</color>
```

### Lỗi: NullPointerException at displayCurrentImage
**Giải pháp**: Kiểm tra đã ánh xạ đúng views trong `onCreate()`:
```java
imMonAn = findViewById(R.id.im_MonAn_DL);
btnPre = findViewById(R.id.btnPre);
btnNext = findViewById(R.id.btnNext);
```

### Lỗi: Không hiển thị ảnh (ảnh trắng)
**Nguyên nhân**: Tên file trong database không khớp với tên file trong `res/drawable`

**Giải pháp**:
1. Kiểm tra database: `am_thuc_pho_co_ha_noi_2.jpg`
2. Kiểm tra file trong `FE/app/src/main/res/drawable/`
3. Đảm bảo tên file khớp nhau (không có khoảng trắng, dấu đặc biệt)

## 📊 CÁCH HOẠT ĐỘNG

### Flow Load Ảnh:

1. **API Response** → Backend trả về:
```json
{
  "hinhAnh": "am_thuc_pho_co_ha_noi_1.jpg",
  "hinhAnhList": [
    {"duongDan": "am_thuc_pho_co_ha_noi_2.jpg", "thuTu": 1},
    {"duongDan": "am_thuc_pho_co_ha_noi_3.jpg", "thuTu": 2}
  ]
}
```

2. **Android Parse** → `KhoaHoc.java` nhận data:
```java
khoaHoc.getHinhAnhList() // List<HinhAnhKhoaHoc>
```

3. **Display** → `displayKhoaHocInfo()` kiểm tra:
- Nếu có `hinhAnhList` → hiển thị slide
- Nếu không → hiển thị `hinhAnh` (banner)

4. **Load Resource** → `getHinhAnhResId()` convert:
```java
"am_thuc_pho_co_ha_noi_2.jpg" 
→ R.drawable.am_thuc_pho_co_ha_noi_2
```

5. **Set Image** → `setImageResource()`:
```java
imMonAn.setImageResource(resId);
```

## 🎯 KẾT QUẢ MONG ĐỢI

Sau khi hoàn thành, bạn sẽ có:
- ✅ Slide ảnh với 2 ảnh từ database
- ✅ Nút Pre/Next hoạt động
- ✅ Indicators (circles) đổi màu
- ✅ Quay vòng khi đến ảnh cuối
- ✅ Fallback về ảnh banner nếu không có slide

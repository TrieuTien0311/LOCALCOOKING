# Hướng Dẫn Hiển Thị Hình Ảnh Khóa Học Trên Android

## 📋 Tổng Quan

Dựa trên database SQL, mỗi khóa học có:
- **1 ảnh banner** (từ bảng `KhoaHoc.hinhAnh`) - Hiển thị ở danh sách
- **2 ảnh slide** (từ bảng `HinhAnhKhoaHoc`) - Hiển thị ở màn hình chi tiết với nút Pre/Next

## 🎯 Yêu Cầu

### 1. Màn Hình Danh Sách (`item_class.xml`)
- Hiển thị **ảnh banner** ở `imgMonAn`
- Ví dụ: `am_thuc_pho_co_ha_noi_1.jpg`

### 2. Màn Hình Chi Tiết (`activity_booking.xml`)
- Hiển thị **slide 2 ảnh** ở `im_MonAn_DL`
- Nút `btnPre` và `btnNext` để chuyển ảnh
- Ví dụ: `am_thuc_pho_co_ha_noi_2.jpg`, `am_thuc_pho_co_ha_noi_3.jpg`
- Có 5 indicator (circle1-5) để hiển thị vị trí ảnh hiện tại

## 🔧 Bước 1: Cập Nhật Model Class

### KhoaHocDTO.java (Backend đã có)
```java
public class KhoaHocDTO {
    private String hinhAnh; // Ảnh banner
    private List<HinhAnhKhoaHocDTO> hinhAnhList; // Slide ảnh
    // ... các field khác
}
```

### HinhAnhKhoaHocDTO.java (Backend đã có)
```java
public class HinhAnhKhoaHocDTO {
    private Integer maHinhAnh;
    private Integer maKhoaHoc;
    private String duongDan; // Tên file ảnh
    private Integer thuTu; // Thứ tự hiển thị
}
```

### Tạo Model Tương Ứng Trên Android

**File: `FE/app/src/main/java/com/example/localcooking_v3t/model/HinhAnhKhoaHoc.java`**

```java
package com.example.localcooking_v3t.model;

import android.content.Context;

public class HinhAnhKhoaHoc {
    private Integer maHinhAnh;
    private Integer maKhoaHoc;
    private String duongDan;
    private Integer thuTu;
    
    // Constructors
    public HinhAnhKhoaHoc() {}
    
    public HinhAnhKhoaHoc(Integer maHinhAnh, Integer maKhoaHoc, String duongDan, Integer thuTu) {
        this.maHinhAnh = maHinhAnh;
        this.maKhoaHoc = maKhoaHoc;
        this.duongDan = duongDan;
        this.thuTu = thuTu;
    }
    
    // Getters & Setters
    public Integer getMaHinhAnh() { return maHinhAnh; }
    public void setMaHinhAnh(Integer maHinhAnh) { this.maHinhAnh = maHinhAnh; }
    
    public Integer getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(Integer maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }
    
    public String getDuongDan() { return duongDan; }
    public void setDuongDan(String duongDan) { this.duongDan = duongDan; }
    
    public Integer getThuTu() { return thuTu; }
    public void setThuTu(Integer thuTu) { this.thuTu = thuTu; }
    
    /**
     * Lấy resource ID từ tên file ảnh
     * VD: "am_thuc_pho_co_ha_noi_2.jpg" -> R.drawable.am_thuc_pho_co_ha_noi_2
     */
    public int getHinhAnhResId(Context context) {
        if (duongDan == null || duongDan.isEmpty()) {
            return R.drawable.hue; // Ảnh mặc định
        }
        
        // Loại bỏ extension .jpg, .png
        String imageName = duongDan.replace(".jpg", "").replace(".png", "");
        
        int resId = context.getResources().getIdentifier(
            imageName, 
            "drawable", 
            context.getPackageName()
        );
        
        return resId != 0 ? resId : R.drawable.hue;
    }
}
```

### Cập Nhật KhoaHoc.java

**File: `FE/app/src/main/java/com/example/localcooking_v3t/model/KhoaHoc.java`**

Thêm field `hinhAnhList`:

```java
package com.example.localcooking_v3t.model;

import java.math.BigDecimal;
import java.util.List;

public class KhoaHoc {
    private Integer maKhoaHoc;
    private String tenKhoaHoc;
    private String moTa;
    private String gioiThieu;
    private String giaTriSauBuoiHoc;
    private BigDecimal giaTien;
    private String hinhAnh; // Ảnh banner
    private Integer soLuongDanhGia;
    private Float saoTrungBinh;
    private Boolean coUuDai;
    private Double phanTramGiam;
    private BigDecimal giaSauGiam;
    
    // THÊM MỚI: Danh sách hình ảnh slide
    private List<HinhAnhKhoaHoc> hinhAnhList;
    
    // Getters & Setters (thêm cho hinhAnhList)
    public List<HinhAnhKhoaHoc> getHinhAnhList() { 
        return hinhAnhList; 
    }
    
    public void setHinhAnhList(List<HinhAnhKhoaHoc> hinhAnhList) { 
        this.hinhAnhList = hinhAnhList; 
    }
    
    // ... các getter/setter khác giữ nguyên
}
```

## 🔧 Bước 2: Cập Nhật Booking.java

### Thêm Biến Quản Lý Slide Ảnh

```java
public class Booking extends AppCompatActivity {
    
    // ... các biến hiện tại
    
    // THÊM MỚI: Quản lý slide ảnh
    private List<HinhAnhKhoaHoc> hinhAnhList;
    private int currentImageIndex = 0;
    private ImageView imMonAn;
    private ImageView btnPre, btnNext;
    private ImageView[] circles; // Mảng 5 indicator
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... code hiện tại
        
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
        
        // ... code hiện tại
    }
}
```

### Cập Nhật Method `displayKhoaHocInfo()`

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
        imMonAn.setImageResource(resId);
        
        // Ẩn nút Pre/Next
        btnPre.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        
        // Ẩn tất cả circles
        for (ImageView circle : circles) {
            circle.setVisibility(View.GONE);
        }
        
        Log.d("BOOKING_UI", "No slide images, showing banner: " + khoaHoc.getHinhAnh());
    }
    
    // Giới thiệu lớp học
    TextView txtGioiThieu = findViewById(R.id.textView49);
    if (txtGioiThieu != null && khoaHoc.getGioiThieu() != null) {
        txtGioiThieu.setText(khoaHoc.getGioiThieu());
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

/**
 * Hiển thị ảnh hiện tại trong slide
 */
private void displayCurrentImage() {
    if (hinhAnhList == null || hinhAnhList.isEmpty()) return;
    
    // Hiển thị ảnh
    HinhAnhKhoaHoc currentImage = hinhAnhList.get(currentImageIndex);
    int resId = currentImage.getHinhAnhResId(this);
    imMonAn.setImageResource(resId);
    
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
        if (i < totalImages) {
            // Hiển thị circle nếu có ảnh tương ứng
            circles[i].setVisibility(View.VISIBLE);
            
            // Đổi màu circle: active = #BA5632, inactive = #DCA790
            if (i == currentImageIndex) {
                circles[i].setColorFilter(getResources().getColor(R.color.active_indicator)); // #BA5632
            } else {
                circles[i].setColorFilter(getResources().getColor(R.color.inactive_indicator)); // #DCA790
            }
        } else {
            // Ẩn circle nếu không có ảnh
            circles[i].setVisibility(View.GONE);
        }
    }
}
```

## 🎨 Bước 3: Thêm Colors Vào `res/values/colors.xml`

```xml
<resources>
    <!-- ... màu hiện tại -->
    
    <!-- Màu cho indicators -->
    <color name="active_indicator">#BA5632</color>
    <color name="inactive_indicator">#DCA790</color>
</resources>
```

## 📱 Bước 4: Test Trên Android

### Test Case 1: Khóa Học Có 2 Ảnh Slide

**Dữ liệu từ SQL:**
```sql
-- Khóa học 1: Ẩm thực phố cổ Hà Nội
KhoaHoc.hinhAnh = 'am_thuc_pho_co_ha_noi_1.jpg' (banner)

HinhAnhKhoaHoc:
- duongDan = 'am_thuc_pho_co_ha_noi_2.jpg', thuTu = 1
- duongDan = 'am_thuc_pho_co_ha_noi_3.jpg', thuTu = 2
```

**Kết quả mong đợi:**
1. Màn hình danh sách: Hiển thị `am_thuc_pho_co_ha_noi_1.jpg`
2. Màn hình chi tiết:
   - Ảnh đầu tiên: `am_thuc_pho_co_ha_noi_2.jpg`
   - Nhấn Next: `am_thuc_pho_co_ha_noi_3.jpg`
   - Nhấn Next tiếp: Quay lại `am_thuc_pho_co_ha_noi_2.jpg`
   - Nhấn Pre: `am_thuc_pho_co_ha_noi_3.jpg`
   - Indicators: 2 circle hiển thị, 3 circle ẩn

### Test Case 2: Khóa Học Không Có Slide

**Kết quả mong đợi:**
- Hiển thị ảnh banner
- Nút Pre/Next ẩn
- Tất cả circles ẩn

## 🔍 Debug Tips

### 1. Kiểm Tra API Response

```java
Log.d("BOOKING_API", "hinhAnhList size: " + (khoaHoc.getHinhAnhList() != null ? khoaHoc.getHinhAnhList().size() : "null"));
if (khoaHoc.getHinhAnhList() != null) {
    for (HinhAnhKhoaHoc img : khoaHoc.getHinhAnhList()) {
        Log.d("BOOKING_API", "  - " + img.getDuongDan() + " (thuTu: " + img.getThuTu() + ")");
    }
}
```

### 2. Kiểm Tra Resource ID

```java
int resId = currentImage.getHinhAnhResId(this);
Log.d("BOOKING_UI", "Resource ID for " + currentImage.getDuongDan() + ": " + resId);
if (resId == 0 || resId == R.drawable.hue) {
    Log.e("BOOKING_UI", "Image not found in drawable!");
}
```

### 3. Kiểm Tra Tên File

Đảm bảo tên file trong database khớp với tên file trong `res/drawable`:
- Database: `am_thuc_pho_co_ha_noi_2.jpg`
- Drawable: `am_thuc_pho_co_ha_noi_2.jpg` hoặc `am_thuc_pho_co_ha_noi_2.png`

## ✅ Checklist

- [ ] Tạo class `HinhAnhKhoaHoc.java`
- [ ] Thêm field `hinhAnhList` vào `KhoaHoc.java`
- [ ] Thêm biến quản lý slide trong `Booking.java`
- [ ] Implement `displayCurrentImage()`
- [ ] Implement `updateIndicators()`
- [ ] Cập nhật `displayKhoaHocInfo()`
- [ ] Thêm màu indicators vào `colors.xml`
- [ ] Test với khóa học có 2 ảnh
- [ ] Test với khóa học không có slide
- [ ] Kiểm tra nút Pre/Next hoạt động đúng
- [ ] Kiểm tra indicators cập nhật đúng

## 🎯 Kết Quả Cuối Cùng

Khi hoàn thành, bạn sẽ có:
1. **Danh sách khóa học**: Hiển thị ảnh banner
2. **Chi tiết khóa học**: Slide 2 ảnh với nút Pre/Next
3. **Indicators**: Hiển thị vị trí ảnh hiện tại (tối đa 5 ảnh)
4. **Quay vòng**: Nhấn Next ở ảnh cuối -> quay về ảnh đầu
5. **Fallback**: Nếu không có slide -> hiển thị ảnh banner

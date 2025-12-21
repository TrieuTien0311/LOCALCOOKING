// ============================================================
// CODE MẪU ĐỂ CẬP NHẬT Booking.java
// Copy các đoạn code này vào file Booking.java hiện tại
// ============================================================

// ========== 1. THÊM IMPORT ==========
import com.example.localcooking_v3t.model.HinhAnhKhoaHoc;
import java.util.List;

// ========== 2. THÊM BIẾN VÀO CLASS Booking ==========
public class Booking extends AppCompatActivity {
    
    // ... các biến hiện tại ...
    
    // THÊM MỚI: Quản lý slide ảnh
    private List<HinhAnhKhoaHoc> hinhAnhList;
    private int currentImageIndex = 0;
    private ImageView imMonAn;
    private ImageView btnPre, btnNext;
    private ImageView[] circles; // Mảng 5 indicator
    
    // ... code tiếp theo ...
}

// ========== 3. THÊM VÀO onCreate() SAU KHI ÁNH XẠ VIEWS ==========
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // ... code hiện tại ...
    
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
    
    // ... code tiếp theo ...
}

// ========== 4. THAY THẾ METHOD displayKhoaHocInfo() ==========
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

// ========== 5. THÊM 2 METHOD MỚI VÀO CUỐI CLASS ==========
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

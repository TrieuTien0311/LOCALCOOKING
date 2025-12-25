package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.CheckSeatsResponse;
import com.example.localcooking_v3t.model.DatLich;
import com.example.localcooking_v3t.model.DatLichRequest;
import com.example.localcooking_v3t.model.DatLichResponse;
import com.example.localcooking_v3t.model.GiaoVien;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.model.LichTrinhLopHoc;
import com.example.localcooking_v3t.utils.SessionManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.localcooking_v3t.model.HinhAnhKhoaHoc;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking extends AppCompatActivity {

    private ImageView btnGiam, btnTang;
    private Button txtSoLuongDat, btnTiepTuc;
    private TextView txtGioiHan, txtThoiGianHeader, txtDiaDiemHeader;
    private int soLuong = 1;
    private int soLuongConLai = 10; // Số suất còn lại (sẽ lấy từ API), mặc định 10
    
    // Data từ Intent
    private Integer maKhoaHoc;
    private Integer maLichTrinh;
    private String tenKhoaHoc;
    private String ngayThamGia;
    private BigDecimal giaTien;
    private String thoiGian;
    private String diaDiem;
    
    // Data từ API
    private LichTrinhLopHoc lichTrinhLopHoc;
    private KhoaHoc khoaHoc; // Thêm để lấy thông tin đầy đủ
    private GiaoVien giaoVien; // Thông tin giáo viên
    
    private SessionManager sessionManager;
    private ApiService apiService;
    
    // Quản lý slide ảnh
    private List<HinhAnhKhoaHoc> hinhAnhList;
    private int currentImageIndex = 0;
    private ImageView imMonAn;
    private ImageView btnPre, btnNext;
    private ImageView[] circles;
    
    // Nút yêu thích
    private ImageView icFavDL;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo
        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        
        // Nhận data từ Intent
        receiveDataFromIntent();
        
        // Ánh xạ views
        ImageView btnBack = findViewById(R.id.btnBack);
        btnGiam = findViewById(R.id.btn_Giam_DL);
        btnTang = findViewById(R.id.btn_Tang_DL);
        txtSoLuongDat = findViewById(R.id.txt_SoLuongDat_DL);
        txtGioiHan = findViewById(R.id.txt_GioiHan_DL);
        btnTiepTuc = findViewById(R.id.btn_TiepTuc);
        txtThoiGianHeader = findViewById(R.id.txtThoiGian);
        txtDiaDiemHeader = findViewById(R.id.txtDiaDiem);
        
        // Ánh xạ views cho slide ảnh
        imMonAn = findViewById(R.id.im_MonAn_DL);
        btnPre = findViewById(R.id.btnPre);
        btnNext = findViewById(R.id.btnNext);
        
        // Ánh xạ 3 circles
        circles = new ImageView[3];
        circles[0] = findViewById(R.id.circle1);
        circles[1] = findViewById(R.id.circle2);
        circles[2] = findViewById(R.id.circle3);
        
        // Ánh xạ nút yêu thích
        icFavDL = findViewById(R.id.ic_fav_DL);
        if (icFavDL != null) {
            icFavDL.setOnClickListener(v -> toggleFavorite());
        }
        
        // Xử lý nút Previous với animation
        btnPre.setOnClickListener(v -> {
            if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
                currentImageIndex--;
                if (currentImageIndex < 0) {
                    currentImageIndex = hinhAnhList.size() - 1; // Quay vòng
                }
                displayCurrentImageWithAnimation(true); // true = slide từ trái sang
            }
        });
        
        // Xử lý nút Next với animation
        btnNext.setOnClickListener(v -> {
            if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
                currentImageIndex++;
                if (currentImageIndex >= hinhAnhList.size()) {
                    currentImageIndex = 0; // Quay vòng
                }
                displayCurrentImageWithAnimation(false); // false = slide từ phải sang
            }
        });
        
        // Thêm gesture swipe cho ảnh
        if (imMonAn != null) {
            imMonAn.setOnTouchListener(new View.OnTouchListener() {
                private float startX;
                private float startY;
                private static final int SWIPE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;
                
                @Override
                public boolean onTouch(View v, android.view.MotionEvent event) {
                    switch (event.getAction()) {
                        case android.view.MotionEvent.ACTION_DOWN:
                            startX = event.getX();
                            startY = event.getY();
                            return true;
                            
                        case android.view.MotionEvent.ACTION_UP:
                            float endX = event.getX();
                            float endY = event.getY();
                            float diffX = endX - startX;
                            float diffY = endY - startY;
                            
                            // Kiểm tra swipe ngang (không phải dọc)
                            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD) {
                                if (hinhAnhList != null && !hinhAnhList.isEmpty()) {
                                    if (diffX > 0) {
                                        // Swipe phải -> ảnh trước
                                        currentImageIndex--;
                                        if (currentImageIndex < 0) {
                                            currentImageIndex = hinhAnhList.size() - 1;
                                        }
                                        displayCurrentImageWithAnimation(true);
                                    } else {
                                        // Swipe trái -> ảnh sau
                                        currentImageIndex++;
                                        if (currentImageIndex >= hinhAnhList.size()) {
                                            currentImageIndex = 0;
                                        }
                                        displayCurrentImageWithAnimation(false);
                                    }
                                }
                                return true;
                            }
                            break;
                    }
                    return false;
                }
            });
        }
        
        // Xử lý nút Back - quay về trang danh sách khóa học
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Xử lý nút giảm số lượng
        btnGiam.setOnClickListener(v -> {
            if (soLuong > 1) {
                soLuong--;
                updateUI();
            }
        });

        // Xử lý nút tăng số lượng
        btnTang.setOnClickListener(v -> {
            if (soLuong < soLuongConLai) {
                soLuong++;
                updateUI();
            } else {
                Toast.makeText(this, "Chỉ còn " + soLuongConLai + " chỗ trống", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Xử lý tiếp tục
        btnTiepTuc.setOnClickListener(v -> {
            performBooking();
        });
        
        // Xử lý nút mở rộng/thu gọn thông tin giáo viên
        ImageView btnDownTeacher = findViewById(R.id.btnDownTeacher);
        LinearLayout txtAn = findViewById(R.id.txtAn);
        if (btnDownTeacher != null && txtAn != null) {
            btnDownTeacher.setOnClickListener(v -> {
                if (txtAn.getVisibility() == View.GONE) {
                    txtAn.setVisibility(View.VISIBLE);
                    btnDownTeacher.setRotation(180);
                } else {
                    txtAn.setVisibility(View.GONE);
                    btnDownTeacher.setRotation(0);
                }
            });
        }
        
        // Load dữ liệu từ API
        loadLichTrinhData();
        
        // Load trạng thái yêu thích
        loadFavoriteStatus();
        
        // Xử lý nút Chi tiết (header) - mở bottom sheet
        TextView btnChiTiet = findViewById(R.id.btnChiTiet);
        if (btnChiTiet != null) {
            btnChiTiet.setOnClickListener(v -> showDetailBottomSheet(0)); // Tab Mô tả
        }
        
        // Xử lý các tab ở bottom card
        TextView txtMoTa = findViewById(R.id.txt_MoTa_DL);
        TextView txtDanhGia = findViewById(R.id.txt_DanhGia_DL);
        TextView txtChinhSach = findViewById(R.id.txt_ChinhSach_DL);
        TextView txtUuDai = findViewById(R.id.txt_UuDai_DL);
        
        if (txtMoTa != null) {
            txtMoTa.setOnClickListener(v -> showDetailBottomSheet(0)); // Tab Mô tả
        }
        if (txtDanhGia != null) {
            txtDanhGia.setOnClickListener(v -> showDetailBottomSheet(1)); // Tab Đánh giá
        }
        if (txtChinhSach != null) {
            txtChinhSach.setOnClickListener(v -> showDetailBottomSheet(2)); // Tab Chính sách
        }
        if (txtUuDai != null) {
            txtUuDai.setOnClickListener(v -> showDetailBottomSheet(3)); // Tab Ưu đãi
        }
        
        // Xử lý click vào card Chi tiết
        View cvChiTiet = findViewById(R.id.cv_ChiTiet_DL);
        if (cvChiTiet != null) {
            cvChiTiet.setOnClickListener(v -> showDetailBottomSheet(0));
        }
    }
    
    /**
     * Hiển thị bottom sheet chi tiết với tab được chọn
     */
    private void showDetailBottomSheet(int tabIndex) {
        if (khoaHoc != null) {
            // Cập nhật trạng thái yêu thích vào khoaHoc
            khoaHoc.setIsFavorite(isFavorite);
            
            // Tạo bottom sheet với dữ liệu khóa học
            DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(khoaHoc, formatDateForDisplay(ngayThamGia));
            
            // Set listener để đồng bộ trạng thái yêu thích
            bottomSheet.setOnFavoriteChangedListener((changedKhoaHoc, newFavoriteStatus) -> {
                isFavorite = newFavoriteStatus;
                updateFavoriteIcon();
            });
            
            bottomSheet.show(getSupportFragmentManager(), "DetailBottomSheet");
            
            // Chuyển đến tab được chọn sau khi bottom sheet hiển thị
            // (TabLayout sẽ tự động chọn tab đầu tiên, cần delay để chuyển tab)
            if (tabIndex > 0) {
                new android.os.Handler().postDelayed(() -> {
                    // Bottom sheet đã hiển thị, có thể thêm logic chuyển tab nếu cần
                }, 300);
            }
        } else {
            // Nếu chưa có dữ liệu khóa học, tạo KhoaHoc từ dữ liệu Intent
            KhoaHoc tempKhoaHoc = new KhoaHoc();
            tempKhoaHoc.setMaKhoaHoc(maKhoaHoc);
            tempKhoaHoc.setTenKhoaHoc(tenKhoaHoc);
            tempKhoaHoc.setGiaTien(giaTien != null ? giaTien.doubleValue() : 0.0);
            tempKhoaHoc.setIsFavorite(isFavorite);
            
            // Tạo lịch trình tạm với địa điểm
            if (lichTrinhLopHoc != null) {
                List<LichTrinhLopHoc> lichTrinhList = new ArrayList<>();
                lichTrinhList.add(lichTrinhLopHoc);
                tempKhoaHoc.setLichTrinhList(lichTrinhList);
            } else if (maLichTrinh != null && maLichTrinh > 0) {
                LichTrinhLopHoc tempLichTrinh = new LichTrinhLopHoc();
                tempLichTrinh.setMaLichTrinh(maLichTrinh);
                tempLichTrinh.setGioBatDau(thoiGian != null ? thoiGian.split(" - ")[0] : "");
                tempLichTrinh.setGioKetThuc(thoiGian != null && thoiGian.contains(" - ") ? thoiGian.split(" - ")[1] : "");
                tempLichTrinh.setDiaDiem(diaDiem); // Set địa điểm vào lịch trình
                List<LichTrinhLopHoc> lichTrinhList = new ArrayList<>();
                lichTrinhList.add(tempLichTrinh);
                tempKhoaHoc.setLichTrinhList(lichTrinhList);
            }
            
            DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(tempKhoaHoc, formatDateForDisplay(ngayThamGia));
            
            // Set listener để đồng bộ trạng thái yêu thích
            bottomSheet.setOnFavoriteChangedListener((changedKhoaHoc, newFavoriteStatus) -> {
                isFavorite = newFavoriteStatus;
                updateFavoriteIcon();
            });
            
            bottomSheet.show(getSupportFragmentManager(), "DetailBottomSheet");
        }
    }
    
    /**
     * Format ngày từ "2025-01-15" sang "T4, 15/01/2025"
     */
    private String formatDateForDisplay(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "";
        
        try {
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                // Tính thứ trong tuần
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
                
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                String thu = "";
                switch (dayOfWeek) {
                    case Calendar.SUNDAY: thu = "CN"; break;
                    case Calendar.MONDAY: thu = "T2"; break;
                    case Calendar.TUESDAY: thu = "T3"; break;
                    case Calendar.WEDNESDAY: thu = "T4"; break;
                    case Calendar.THURSDAY: thu = "T5"; break;
                    case Calendar.FRIDAY: thu = "T6"; break;
                    case Calendar.SATURDAY: thu = "T7"; break;
                }
                
                return thu + ", " + parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        } catch (Exception e) {
            Log.e("BOOKING", "Error formatting date: " + e.getMessage());
        }
        return dateStr;
    }
    
    /**
     * Nhận data từ Intent
     */
    private void receiveDataFromIntent() {
        maKhoaHoc = getIntent().getIntExtra("maKhoaHoc", 0);
        maLichTrinh = getIntent().getIntExtra("maLichTrinh", 0);
        tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
        ngayThamGia = getIntent().getStringExtra("ngayThamGia");
        String giaTienStr = getIntent().getStringExtra("giaTien");
        thoiGian = getIntent().getStringExtra("thoiGian");
        diaDiem = getIntent().getStringExtra("diaDiem");
        
        // Parse giá tiền an toàn
        if (giaTienStr != null && !giaTienStr.isEmpty()) {
            try {
                giaTien = new BigDecimal(giaTienStr);
            } catch (NumberFormatException e) {
                Log.e("BOOKING", "Error parsing giaTien: " + giaTienStr, e);
                giaTien = BigDecimal.ZERO;
            }
        } else {
            giaTien = BigDecimal.ZERO;
        }
        
        // Log để debug
        Log.d("BOOKING", "maKhoaHoc: " + maKhoaHoc);
        Log.d("BOOKING", "maLichTrinh: " + maLichTrinh);
        Log.d("BOOKING", "tenKhoaHoc: " + tenKhoaHoc);
        Log.d("BOOKING", "ngayThamGia: " + ngayThamGia);
        Log.d("BOOKING", "giaTien: " + giaTien);
        Log.d("BOOKING", "thoiGian: " + thoiGian);
        Log.d("BOOKING", "diaDiem: " + diaDiem);
        
        // Nếu không có ngayThamGia, set mặc định là ngày mai
        if (ngayThamGia == null || ngayThamGia.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ngayThamGia = sdf.format(cal.getTime());
            Log.d("BOOKING", "ngayThamGia set default: " + ngayThamGia);
        }
        
        // Validate dữ liệu bắt buộc
        if (maKhoaHoc == null || maKhoaHoc == 0) {
            Toast.makeText(this, "Thiếu thông tin khóa học", Toast.LENGTH_SHORT).show();
            Log.e("BOOKING", "maKhoaHoc is null or 0");
        }
        
        if (maLichTrinh == null || maLichTrinh == 0) {
            Toast.makeText(this, "Thiếu thông tin lịch trình", Toast.LENGTH_SHORT).show();
            Log.e("BOOKING", "maLichTrinh is null or 0");
        }
    }
    
    /**
     * Load dữ liệu lịch trình từ API
     */
    private void loadLichTrinhData() {
        if (maLichTrinh == null || maLichTrinh == 0) {
            Log.e("BOOKING_API", "maLichTrinh is null or 0, skip loading");
            // Vẫn hiển thị UI với dữ liệu từ Intent
            displayBookingInfo();
            btnTiepTuc.setEnabled(true);
            btnTiepTuc.setText("Tiếp tục");
            return;
        }
        
        // Disable button trong khi load
        btnTiepTuc.setEnabled(false);
        btnTiepTuc.setText("Đang tải...");
        
        // 1. Lấy thông tin khóa học đầy đủ (bao gồm hình ảnh, giáo viên, mô tả)
        if (maKhoaHoc != null && maKhoaHoc != 0) {
            loadKhoaHocData();
        }
        
        // 2. Lấy thông tin lịch trình với số chỗ trống cho ngày cụ thể
        apiService.getLichTrinhDetailById(maLichTrinh, ngayThamGia).enqueue(new Callback<LichTrinhLopHoc>() {
            @Override
            public void onResponse(Call<LichTrinhLopHoc> call, Response<LichTrinhLopHoc> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lichTrinhLopHoc = response.body();
                    Log.d("BOOKING_API", "Loaded LichTrinh: " + lichTrinhLopHoc.getMaLichTrinh());
                    Log.d("BOOKING_API", "Ngày tham gia: " + ngayThamGia);
                    
                    // Cập nhật thông tin từ API nếu chưa có
                    if (thoiGian == null || thoiGian.isEmpty()) {
                        thoiGian = lichTrinhLopHoc.getThoiGianFormatted();
                    }
                    if (diaDiem == null || diaDiem.isEmpty()) {
                        diaDiem = lichTrinhLopHoc.getDiaDiem();
                    }
                    
                    // Lấy số chỗ trống từ API (đã tính cho ngày cụ thể)
                    if (lichTrinhLopHoc.getConTrong() != null) {
                        soLuongConLai = lichTrinhLopHoc.getConTrong();
                        Log.d("BOOKING_API", "Số chỗ còn trống cho ngày " + ngayThamGia + ": " + soLuongConLai);
                    }
                    
                    // 3. Load thông tin giáo viên
                    if (lichTrinhLopHoc.getMaGiaoVien() != null) {
                        loadGiaoVienData(lichTrinhLopHoc.getMaGiaoVien());
                    }
                    
                    // 4. Hiển thị thông tin
                    displayBookingInfo();
                    
                    // Enable button
                    btnTiepTuc.setEnabled(true);
                    btnTiepTuc.setText("Tiếp tục");
                    
                    // Kiểm tra nếu hết chỗ
                    if (soLuongConLai <= 0) {
                        Toast.makeText(Booking.this, "Lớp học đã hết chỗ", Toast.LENGTH_SHORT).show();
                        btnTiepTuc.setEnabled(false);
                        btnTiepTuc.setText("Hết chỗ");
                    }
                } else {
                    btnTiepTuc.setEnabled(true);
                    btnTiepTuc.setText("Tiếp tục");
                    Log.e("BOOKING_API", "Error loading LichTrinh: " + response.code());
                    // Vẫn hiển thị UI với dữ liệu từ Intent
                    displayBookingInfo();
                }
            }
            
            @Override
            public void onFailure(Call<LichTrinhLopHoc> call, Throwable t) {
                btnTiepTuc.setEnabled(true);
                btnTiepTuc.setText("Tiếp tục");
                Log.e("BOOKING_API", "Failed to load LichTrinh", t);
                // Vẫn hiển thị UI với dữ liệu từ Intent
                displayBookingInfo();
            }
        });
    }
    
    /**
     * Load thông tin khóa học đầy đủ từ API
     */
    private void loadKhoaHocData() {
        Log.d("BOOKING_API", "Loading KhoaHoc data for maKhoaHoc: " + maKhoaHoc);
        
        apiService.getKhoaHocById(maKhoaHoc).enqueue(new Callback<KhoaHoc>() {
            @Override
            public void onResponse(Call<KhoaHoc> call, Response<KhoaHoc> response) {
                if (response.isSuccessful() && response.body() != null) {
                    khoaHoc = response.body();
                    Log.d("BOOKING_API", "Loaded KhoaHoc: " + khoaHoc.getTenKhoaHoc());
                    Log.d("BOOKING_API", "coUuDai: " + khoaHoc.getCoUuDai());
                    Log.d("BOOKING_API", "phanTramGiam: " + khoaHoc.getPhanTramGiam());
                    Log.d("BOOKING_API", "giaSauGiam: " + khoaHoc.getGiaSauGiam());
                    
                    // Hiển thị thông tin khóa học lên UI
                    displayKhoaHocInfo();
                    
                    // GỌI LẠI displayBookingInfo() để cập nhật giá với thông tin ưu đãi
                    displayBookingInfo();
                } else {
                    Log.e("BOOKING_API", "Error loading KhoaHoc: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<KhoaHoc> call, Throwable t) {
                Log.e("BOOKING_API", "Failed to load KhoaHoc", t);
            }
        });
    }
    
    /**
     * Kiểm tra số chỗ trống từ API
     */

    
    /**
     * Cập nhật UI
     */
    private void updateUI() {
        txtSoLuongDat.setText(String.valueOf(soLuong));
        
        // Hiển thị số chỗ còn trống (không phải tổng số chỗ)
        if (soLuongConLai > 0) {
            txtGioiHan.setText("Tối đa " + soLuongConLai + " người");
        } else {
            txtGioiHan.setText("Hết chỗ");
        }
    }
    
    /**
     * Hiển thị thông tin đặt lịch lên UI
     */
    private void displayBookingInfo() {
        // === HEADER ===
        // Hiển thị tên khóa học ở header
        if (tenKhoaHoc != null && txtDiaDiemHeader != null) {
            txtDiaDiemHeader.setText(tenKhoaHoc);
            Log.d("BOOKING_UI", "Set txtDiaDiemHeader: " + tenKhoaHoc);
        }
        
        // Hiển thị giờ học và ngày ở header (VD: "17:30 - 20:30 - T5, 25/12/2025")
        if (txtThoiGianHeader != null) {
            StringBuilder displayText = new StringBuilder();
            
            // Thêm giờ học nếu có
            if (thoiGian != null && !thoiGian.isEmpty()) {
                displayText.append(thoiGian);
            }
            
            // Thêm ngày nếu có
            if (ngayThamGia != null && !ngayThamGia.isEmpty()) {
                String formattedDate = formatDate(ngayThamGia);
                if (displayText.length() > 0) {
                    displayText.append(" - ");
                }
                displayText.append(formattedDate);
            }
            
            // Hiển thị kết quả
            if (displayText.length() > 0) {
                txtThoiGianHeader.setText(displayText.toString());
                Log.d("BOOKING_UI", "Set txtThoiGianHeader: " + displayText.toString());
            } else {
                txtThoiGianHeader.setText("Chưa có thông tin");
                Log.d("BOOKING_UI", "No time or date available");
            }
        } else {
            Log.e("BOOKING_UI", "txtThoiGianHeader is null!");
        }
        
        // === THÔNG TIN KHÓA HỌC ===
        // Tên lớp học
        TextView txtTenLop = findViewById(R.id.txt_TenLop_DL);
        if (txtTenLop != null && tenKhoaHoc != null) {
            txtTenLop.setText(tenKhoaHoc);
        }
        
        // Xử lý hiển thị giá với ưu đãi
        TextView txtGiaTien = findViewById(R.id.txt_GiaTien_DL);
        TextView txtGiaCu = findViewById(R.id.txt_GiaCu_DL);
        Button tagGiamGia = findViewById(R.id.tagGiamGia);
        View gachChan = findViewById(R.id.gachChan);
        
        // Kiểm tra coUuDai từ KhoaHoc (đã load từ API)
        boolean coUuDai = (khoaHoc != null && khoaHoc.getCoUuDai() != null && khoaHoc.getCoUuDai());
        
        Log.d("BOOKING_UI", "=== Hiển thị giá ===");
        Log.d("BOOKING_UI", "coUuDai: " + coUuDai);
        Log.d("BOOKING_UI", "giaTien gốc: " + (giaTien != null ? giaTien.toString() : "null"));
        
        if (coUuDai) {
            // CÓ ƯU ĐÃI (coUuDai = 1): Hiển thị tag giảm giá, giá cũ gạch ngang, giá sau giảm
            
            Log.d("BOOKING_UI", "Có ưu đãi - hiển thị giá giảm");
            
            // Tính giá sau giảm và phần trăm giảm
            BigDecimal giaSauGiam = giaTien;
            Double phanTramGiam = null;
            
            // Ưu tiên lấy từ backend
            if (khoaHoc.getGiaSauGiam() != null) {
                giaSauGiam = BigDecimal.valueOf(khoaHoc.getGiaSauGiam());
                Log.d("BOOKING_UI", "Giá sau giảm từ backend: " + giaSauGiam);
                
                // Tính phần trăm giảm nếu backend không có
                if (khoaHoc.getPhanTramGiam() != null) {
                    phanTramGiam = khoaHoc.getPhanTramGiam();
                } else if (giaTien != null && giaTien.compareTo(BigDecimal.ZERO) > 0) {
                    // Tính: phanTramGiam = (giaTien - giaSauGiam) / giaTien * 100
                    double giam = giaTien.subtract(giaSauGiam).doubleValue();
                    phanTramGiam = (giam / giaTien.doubleValue()) * 100;
                }
            } else if (khoaHoc.getPhanTramGiam() != null) {
                // Tính giá sau giảm từ phần trăm
                phanTramGiam = khoaHoc.getPhanTramGiam();
                giaSauGiam = giaTien.multiply(BigDecimal.valueOf(100 - phanTramGiam))
                        .divide(BigDecimal.valueOf(100), 0, java.math.RoundingMode.HALF_UP);
                Log.d("BOOKING_UI", "Giá sau giảm tính toán: " + giaSauGiam + " (giảm " + phanTramGiam + "%)");
            }
            
            // 1. Hiển thị tag giảm giá
            if (tagGiamGia != null) {
                if (phanTramGiam != null && phanTramGiam > 0) {
                    tagGiamGia.setVisibility(View.VISIBLE);
                    tagGiamGia.setText(String.format("-%.0f%%", phanTramGiam));
                    Log.d("BOOKING_UI", "Hiển thị tag giảm: -" + String.format("%.0f", phanTramGiam) + "%");
                } else {
                    tagGiamGia.setVisibility(View.GONE);
                    Log.d("BOOKING_UI", "Không có phần trăm giảm, ẩn tag");
                }
            }
            
            // 2. Hiển thị giá gốc bị gạch (txt_GiaCu_DL)
            if (txtGiaCu != null && giaTien != null) {
                txtGiaCu.setVisibility(View.VISIBLE);
                txtGiaCu.setText(formatCurrency(giaTien));
                Log.d("BOOKING_UI", "Hiển thị giá cũ: " + formatCurrency(giaTien));
            }
            
            // 3. Hiển thị gạch chân
            if (gachChan != null) {
                gachChan.setVisibility(View.VISIBLE);
            }
            
            // 4. Hiển thị giá sau giảm (txt_GiaTien_DL)
            if (txtGiaTien != null) {
                txtGiaTien.setText(formatCurrency(giaSauGiam));
                Log.d("BOOKING_UI", "Hiển thị giá sau giảm: " + formatCurrency(giaSauGiam));
            }
            
            // Cập nhật giá tiền để tính tổng tiền đúng
            giaTien = giaSauGiam;
        } else {
            // KHÔNG CÓ ƯU ĐÃI (coUuDai = 0): Chỉ hiển thị giá gốc, ẩn tag và giá cũ
            
            Log.d("BOOKING_UI", "Không có ưu đãi - chỉ hiển thị giá gốc");
            
            // Ẩn tag giảm giá
            if (tagGiamGia != null) {
                tagGiamGia.setVisibility(View.INVISIBLE);
            }
            
            // Ẩn giá cũ
            if (txtGiaCu != null) {
                txtGiaCu.setVisibility(View.INVISIBLE);
            }
            
            // Ẩn gạch chân
            if (gachChan != null) {
                gachChan.setVisibility(View.INVISIBLE);
            }
            
            // Chỉ hiển thị giá gốc
            if (txtGiaTien != null && giaTien != null) {
                txtGiaTien.setText(formatCurrency(giaTien));
                Log.d("BOOKING_UI", "Hiển thị giá gốc: " + formatCurrency(giaTien));
            }
        }
        
        // Địa điểm
        TextView txtDiaDiem = findViewById(R.id.txt_DiaDiem_DL);
        if (txtDiaDiem != null && diaDiem != null) {
            txtDiaDiem.setText(diaDiem);
        }
        
        // Sức chứa (số lượng tối đa)
        TextView txtSucChua = findViewById(R.id.txt_SucChua_DL);
        if (txtSucChua != null && lichTrinhLopHoc != null && lichTrinhLopHoc.getSoLuongToiDa() != null) {
            txtSucChua.setText(" " + lichTrinhLopHoc.getSoLuongToiDa() + " người");
        }
        
        // Cập nhật số lượng còn lại
        updateUI();
    }
    
    /**
     * Format tiền tệ
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0₫";
        return String.format("%,.0f₫", amount.doubleValue()).replace(",", ".");
    }
    
    /**
     * Load thông tin giáo viên từ API
     */
    private void loadGiaoVienData(Integer maGiaoVien) {
        Log.d("BOOKING_API", "Loading GiaoVien data for maGiaoVien: " + maGiaoVien);
        
        apiService.getGiaoVienById(maGiaoVien).enqueue(new Callback<GiaoVien>() {
            @Override
            public void onResponse(Call<GiaoVien> call, Response<GiaoVien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    giaoVien = response.body();
                    Log.d("BOOKING_API", "Loaded GiaoVien: " + giaoVien.getHoTen());
                    
                    // Hiển thị thông tin giáo viên lên UI
                    displayGiaoVienInfo();
                } else {
                    Log.e("BOOKING_API", "Error loading GiaoVien: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<GiaoVien> call, Throwable t) {
                Log.e("BOOKING_API", "Failed to load GiaoVien", t);
            }
        });
    }
    
    /**
     * Hiển thị thông tin giáo viên lên UI
     */
    private void displayGiaoVienInfo() {
        if (giaoVien == null) return;
        
        Log.d("BOOKING_UI", "=== Displaying GiaoVien Info ===");
        
        // Tên giáo viên
        TextView txtTenGV = findViewById(R.id.txtTenGV);
        if (txtTenGV != null && giaoVien.getHoTen() != null) {
            txtTenGV.setText(giaoVien.getHoTen());
            Log.d("BOOKING_UI", "Set teacher name: " + giaoVien.getHoTen());
        }
        
        // Mô tả giáo viên (chuyên môn)
        TextView txtMoTaGV = findViewById(R.id.txtMoTaGV);
        if (txtMoTaGV != null && giaoVien.getChuyenMon() != null) {
            txtMoTaGV.setText(giaoVien.getChuyenMon());
            Log.d("BOOKING_UI", "Set teacher specialty: " + giaoVien.getChuyenMon());
        }
        
        // Kinh nghiệm
        TextView txtKinhNghiem = findViewById(R.id.textView45);
        if (txtKinhNghiem != null && giaoVien.getKinhNghiem() != null) {
            txtKinhNghiem.setText(giaoVien.getKinhNghiem());
            Log.d("BOOKING_UI", "Set teacher experience: " + giaoVien.getKinhNghiem());
        }
        
        // Lịch sử kinh nghiệm (phần ẩn)
        TextView txtLichSuKinhNghiem = findViewById(R.id.textView46);
        if (txtLichSuKinhNghiem != null && giaoVien.getLichSuKinhNghiem() != null) {
            // Tách lịch sử kinh nghiệm thành các dòng
            String lichSu = giaoVien.getLichSuKinhNghiem();
            txtLichSuKinhNghiem.setText(lichSu);
            Log.d("BOOKING_UI", "Set teacher history");
        }
        
        // Hình ảnh giáo viên (vẫn load từ drawable vì ảnh giáo viên lưu local)
        ImageView imgGiaoVien = findViewById(R.id.imgGiaoVien);
        if (imgGiaoVien != null) {
            int resId = giaoVien.getHinhAnhResId(this);
            imgGiaoVien.setImageResource(resId);
            Log.d("BOOKING_UI", "Set teacher image: " + giaoVien.getHinhAnh());
        }
        
        Log.d("BOOKING_UI", "GiaoVien info displayed");
    }
    
    /**
     * Hiển thị thông tin khóa học (hình ảnh, giáo viên, mô tả)
     */
    private void displayKhoaHocInfo() {
        if (khoaHoc == null) {
            Log.e("BOOKING_UI", "khoaHoc is NULL!");
            return;
        }
        
        Log.d("BOOKING_UI", "=== Displaying KhoaHoc Info ===");
        Log.d("BOOKING_UI", "KhoaHoc: " + khoaHoc.getTenKhoaHoc());
        Log.d("BOOKING_UI", "hinhAnh (banner): " + khoaHoc.getHinhAnh());
        Log.d("BOOKING_UI", "hinhAnhList: " + (khoaHoc.getHinhAnhList() != null ? khoaHoc.getHinhAnhList().size() + " images" : "NULL"));
        
        // Tạo danh sách ảnh: 1 ảnh đại diện + 2 ảnh từ HinhAnhKhoaHoc (không trùng) = 3 ảnh
        List<HinhAnhKhoaHoc> combinedImageList = new ArrayList<>();
        
        // Lấy tên ảnh đại diện để so sánh
        String bannerImageName = khoaHoc.getHinhAnh();
        
        // 1. Thêm ảnh đại diện (KhoaHoc.hinhAnh) làm ảnh đầu tiên
        if (bannerImageName != null && !bannerImageName.isEmpty()) {
            HinhAnhKhoaHoc bannerImage = new HinhAnhKhoaHoc();
            bannerImage.setDuongDan(bannerImageName);
            bannerImage.setMaKhoaHoc(khoaHoc.getMaKhoaHoc());
            combinedImageList.add(bannerImage);
            Log.d("BOOKING_UI", "Added banner image first: " + bannerImageName);
        }
        
        // 2. Thêm 2 ảnh từ HinhAnhKhoaHoc (bỏ qua ảnh trùng với ảnh đại diện)
        if (khoaHoc.getHinhAnhList() != null && !khoaHoc.getHinhAnhList().isEmpty()) {
            Log.d("BOOKING_UI", "Gallery images from API: " + khoaHoc.getHinhAnhList().size());
            int count = 0;
            for (HinhAnhKhoaHoc img : khoaHoc.getHinhAnhList()) {
                if (img.getDuongDan() != null && !img.getDuongDan().isEmpty()) {
                    // Bỏ qua ảnh trùng với ảnh đại diện
                    if (bannerImageName != null && img.getDuongDan().equals(bannerImageName)) {
                        Log.d("BOOKING_UI", "Skipped duplicate image: " + img.getDuongDan());
                        continue;
                    }
                    combinedImageList.add(img);
                    Log.d("BOOKING_UI", "Added gallery image: " + img.getDuongDan());
                    count++;
                    if (count >= 2) break; // Chỉ lấy tối đa 2 ảnh từ gallery (không trùng)
                }
            }
        } else {
            Log.w("BOOKING_UI", "No gallery images from API (hinhAnhList is null or empty)");
        }
        
        Log.d("BOOKING_UI", "Total combined images: " + combinedImageList.size() + " (max 3)");
        
        // Hiển thị slide ảnh
        if (!combinedImageList.isEmpty()) {
            hinhAnhList = combinedImageList;
            currentImageIndex = 0;
            displayCurrentImage();
            
            // Hiển thị nút Pre/Next
            if (btnPre != null) {
                btnPre.setVisibility(View.VISIBLE);
                Log.d("BOOKING_UI", "Show btnPre");
            }
            if (btnNext != null) {
                btnNext.setVisibility(View.VISIBLE);
                Log.d("BOOKING_UI", "Show btnNext");
            }
            
            Log.d("BOOKING_UI", "Total images for slide: " + hinhAnhList.size());
        } else {
            Log.w("BOOKING_UI", "No images found for slide");
            
            // Fallback: hiển thị ảnh mặc định
            if (imMonAn != null) {
                imMonAn.setImageResource(R.drawable.hue);
            }
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
    
    /**
     * Format ngày từ "2025-12-25" sang "T5, 25/12/2025"
     */
    private String formatDate(String dateStr) {
        try {
            String[] parts = dateStr.split("-");
            if (parts.length == 3) {
                String year = parts[0];
                String month = parts[1];
                String day = parts[2];
                
                // Tính thứ trong tuần
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = sdf.parse(dateStr);
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(date);
                
                int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
                String[] daysOfWeek = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};
                String dayName = daysOfWeek[dayOfWeek - 1];
                
                return dayName + ", " + day + "/" + month + "/" + year;
            }
        } catch (Exception e) {
            Log.e("BOOKING", "Error formatting date: " + e.getMessage());
        }
        return dateStr;
    }
    
    /**
     * Chuyển sang màn hình thanh toán (chưa đặt lịch)
     */
    private void performBooking() {
        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt lịch", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return;
        }
        
        // Validate data
        if (maLichTrinh == null || maLichTrinh == 0) {
            Toast.makeText(this, "Vui lòng chọn lịch trình", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ngayThamGia == null || ngayThamGia.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày tham gia", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (giaTien == null || giaTien.compareTo(BigDecimal.ZERO) <= 0) {
            Toast.makeText(this, "Giá tiền không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Tính tổng tiền (đã bao gồm giảm giá nếu có)
        BigDecimal tongTien = giaTien.multiply(BigDecimal.valueOf(soLuong));
        
        // Chuyển sang PaymentActivity với các thông tin cần thiết
        Intent intent = new Intent(Booking.this, Payment.class);
        
        // Thông tin cơ bản để đặt lịch sau
        intent.putExtra("maHocVien", sessionManager.getMaNguoiDung());
        intent.putExtra("maKhoaHoc", maKhoaHoc);
        intent.putExtra("maLichTrinh", maLichTrinh);
        intent.putExtra("tenKhoaHoc", tenKhoaHoc);
        intent.putExtra("soLuongDat", soLuong);
        intent.putExtra("tongTien", tongTien.doubleValue());
        intent.putExtra("giaDonVi", giaTien.doubleValue());
        
        // Thông tin hiển thị
        intent.putExtra("ngayThamGia", ngayThamGia);
        intent.putExtra("thoiGian", thoiGian);
        intent.putExtra("diaDiem", diaDiem);
        
        // Thông tin người đặt (từ session)
        intent.putExtra("tenNguoiDat", sessionManager.getHoTen());
        intent.putExtra("emailNguoiDat", sessionManager.getEmail());
        
        // Thông tin từ KhoaHoc (nếu có)
        if (khoaHoc != null) {
            intent.putExtra("hinhAnh", khoaHoc.getHinhAnh());
            intent.putExtra("moTa", khoaHoc.getMoTa());
            intent.putExtra("coUuDai", khoaHoc.getCoUuDai() != null ? khoaHoc.getCoUuDai() : false);
            if (khoaHoc.getPhanTramGiam() != null) {
                intent.putExtra("phanTramGiam", khoaHoc.getPhanTramGiam());
            }
            if (khoaHoc.getGiaSauGiam() != null) {
                intent.putExtra("giaSauGiam", khoaHoc.getGiaSauGiam());
            }
            if (khoaHoc.getSaoTrungBinh() != null) {
                intent.putExtra("saoTrungBinh", khoaHoc.getSaoTrungBinh());
            }
            if (khoaHoc.getSoLuongDanhGia() != null) {
                intent.putExtra("soLuongDanhGia", khoaHoc.getSoLuongDanhGia());
            }
        }
        
        startActivity(intent);
        // Không finish() để user có thể quay lại
    }
    
    /**
     * Hiển thị ảnh hiện tại trong slide
     */
    private void displayCurrentImage() {
        displayCurrentImageWithAnimation(false, false);
    }
    
    /**
     * Hiển thị ảnh hiện tại với animation
     * @param slideFromLeft true = slide từ trái sang phải, false = slide từ phải sang trái
     */
    private void displayCurrentImageWithAnimation(boolean slideFromLeft) {
        displayCurrentImageWithAnimation(slideFromLeft, true);
    }
    
    /**
     * Hiển thị ảnh hiện tại với hoặc không có animation
     */
    private void displayCurrentImageWithAnimation(boolean slideFromLeft, boolean withAnimation) {
        if (hinhAnhList == null || hinhAnhList.isEmpty() || imMonAn == null) {
            Log.e("BOOKING_UI", "Cannot display image: hinhAnhList or imMonAn is null");
            return;
        }
        
        // Lấy ảnh hiện tại
        HinhAnhKhoaHoc currentImage = hinhAnhList.get(currentImageIndex);
        
        // Tạo URL từ đường dẫn ảnh
        String imageUrl = RetrofitClient.BASE_URL + "uploads/courses/" + currentImage.getDuongDan();
        
        if (withAnimation) {
            // Animation fade out
            imMonAn.animate()
                    .alpha(0f)
                    .setDuration(150)
                    .withEndAction(() -> {
                        // Load ảnh từ URL
                        Glide.with(this)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                .placeholder(R.drawable.hue)
                                .error(R.drawable.hue))
                            .into(imMonAn);
                        
                        // Animation slide in
                        imMonAn.setTranslationX(slideFromLeft ? -100f : 100f);
                        imMonAn.animate()
                                .alpha(1f)
                                .translationX(0f)
                                .setDuration(200)
                                .start();
                    })
                    .start();
        } else {
            // Không animation - load trực tiếp từ URL
            Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                    .placeholder(R.drawable.hue)
                    .error(R.drawable.hue))
                .into(imMonAn);
        }
        
        // Cập nhật indicators
        updateIndicators();
        
        Log.d("BOOKING_UI", "Displaying image " + (currentImageIndex + 1) + "/" + hinhAnhList.size() + ": " + currentImage.getDuongDan());
    }
    
    /**
     * Cập nhật trạng thái indicators (circles)
     */
    private void updateIndicators() {
        if (circles == null || hinhAnhList == null || hinhAnhList.isEmpty()) {
            return;
        }
        
        // Hiển thị số circles bằng số ảnh (tối đa 5)
        int imageCount = Math.min(hinhAnhList.size(), 5);
        
        for (int i = 0; i < circles.length; i++) {
            if (circles[i] != null) {
                if (i < imageCount) {
                    // Hiển thị circle
                    circles[i].setVisibility(View.VISIBLE);
                    
                    // Đổi màu: active (đang xem) hoặc inactive
                    if (i == currentImageIndex) {
                        circles[i].setColorFilter(androidx.core.content.ContextCompat.getColor(this, R.color.active_indicator));
                    } else {
                        circles[i].setColorFilter(androidx.core.content.ContextCompat.getColor(this, R.color.inactive_indicator));
                    }
                } else {
                    // Ẩn circle thừa
                    circles[i].setVisibility(View.GONE);
                }
            }
        }
        
        Log.d("BOOKING_UI", "Updated indicators: " + (currentImageIndex + 1) + "/" + imageCount);
    }
    
    /**
     * Load trạng thái yêu thích từ API
     */
    private void loadFavoriteStatus() {
        Integer maHocVien = sessionManager.getMaNguoiDung();
        
        if (maHocVien == null || maHocVien == -1 || maKhoaHoc == null || maKhoaHoc == 0) {
            Log.d("BOOKING", "Skip loading favorite status: not logged in or no course");
            return;
        }
        
        apiService.checkFavorite(maHocVien, maKhoaHoc)
                .enqueue(new Callback<Map<String, Boolean>>() {
                    @Override
                    public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Boolean result = response.body().get("isFavorite");
                            isFavorite = result != null && result;
                            updateFavoriteIcon();
                            
                            // Cập nhật vào khoaHoc nếu có
                            if (khoaHoc != null) {
                                khoaHoc.setIsFavorite(isFavorite);
                            }
                            
                            Log.d("BOOKING", "Favorite status loaded: " + isFavorite);
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                        Log.e("BOOKING", "Error loading favorite status", t);
                    }
                });
    }
    
    /**
     * Toggle trạng thái yêu thích
     */
    private void toggleFavorite() {
        Integer maHocVien = sessionManager.getMaNguoiDung();
        
        if (maHocVien == null || maHocVien == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (maKhoaHoc == null || maKhoaHoc == 0) {
            Toast.makeText(this, "Không có thông tin khóa học", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Map<String, Integer> request = new HashMap<>();
        request.put("maHocVien", maHocVien);
        request.put("maKhoaHoc", maKhoaHoc);
        
        apiService.toggleFavorite(request)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Boolean result = (Boolean) response.body().get("isFavorite");
                            String message = (String) response.body().get("message");
                            
                            isFavorite = result != null && result;
                            updateFavoriteIcon();
                            
                            // Cập nhật vào khoaHoc nếu có
                            if (khoaHoc != null) {
                                khoaHoc.setIsFavorite(isFavorite);
                            }
                            
                            Toast.makeText(Booking.this, message, Toast.LENGTH_SHORT).show();
                            Log.d("BOOKING", "Favorite toggled: " + isFavorite);
                        } else {
                            Toast.makeText(Booking.this, "Không thể cập nhật yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Log.e("BOOKING", "Error toggling favorite", t);
                        Toast.makeText(Booking.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    /**
     * Cập nhật icon yêu thích
     */
    private void updateFavoriteIcon() {
        if (icFavDL != null) {
            if (isFavorite) {
                icFavDL.setImageResource(R.drawable.ic_heartredfilled);
            } else {
                icFavDL.setImageResource(R.drawable.ic_heart);
            }
        }
    }

}
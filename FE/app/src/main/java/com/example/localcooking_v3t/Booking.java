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
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking extends AppCompatActivity {

    private ImageView btnGiam, btnTang;
    private Button txtSoLuongDat, btnDatLich;
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
        btnDatLich = findViewById(R.id.btn_DatLich_DL);
        txtThoiGianHeader = findViewById(R.id.txtThoiGian);
        txtDiaDiemHeader = findViewById(R.id.txtDiaDiem);
        
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
        
        // Xử lý nút Đặt lịch
        btnDatLich.setOnClickListener(v -> {
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
            btnDatLich.setEnabled(true);
            btnDatLich.setText("Đặt lịch");
            return;
        }
        
        // Disable button trong khi load
        btnDatLich.setEnabled(false);
        btnDatLich.setText("Đang tải...");
        
        // 1. Lấy thông tin khóa học đầy đủ (bao gồm hình ảnh, giáo viên, mô tả)
        if (maKhoaHoc != null && maKhoaHoc != 0) {
            loadKhoaHocData();
        }
        
        // 2. Lấy thông tin lịch trình với số chỗ trống
        apiService.getLichTrinhDetailById(maLichTrinh).enqueue(new Callback<LichTrinhLopHoc>() {
            @Override
            public void onResponse(Call<LichTrinhLopHoc> call, Response<LichTrinhLopHoc> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lichTrinhLopHoc = response.body();
                    Log.d("BOOKING_API", "Loaded LichTrinh: " + lichTrinhLopHoc.getMaLichTrinh());
                    
                    // Cập nhật thông tin từ API nếu chưa có
                    if (thoiGian == null || thoiGian.isEmpty()) {
                        thoiGian = lichTrinhLopHoc.getThoiGianFormatted();
                    }
                    if (diaDiem == null || diaDiem.isEmpty()) {
                        diaDiem = lichTrinhLopHoc.getDiaDiem();
                    }
                    
                    // Lấy số chỗ trống từ API
                    if (lichTrinhLopHoc.getConTrong() != null) {
                        soLuongConLai = lichTrinhLopHoc.getConTrong();
                        Log.d("BOOKING_API", "Số chỗ còn trống từ API: " + soLuongConLai);
                    }
                    
                    // 3. Load thông tin giáo viên
                    if (lichTrinhLopHoc.getMaGiaoVien() != null) {
                        loadGiaoVienData(lichTrinhLopHoc.getMaGiaoVien());
                    }
                    
                    // 4. Hiển thị thông tin
                    displayBookingInfo();
                    
                    // Enable button
                    btnDatLich.setEnabled(true);
                    btnDatLich.setText("Đặt lịch");
                    
                    // Kiểm tra nếu hết chỗ
                    if (soLuongConLai <= 0) {
                        Toast.makeText(Booking.this, "Lớp học đã hết chỗ", Toast.LENGTH_SHORT).show();
                        btnDatLich.setEnabled(false);
                        btnDatLich.setText("Hết chỗ");
                    }
                } else {
                    btnDatLich.setEnabled(true);
                    btnDatLich.setText("Đặt lịch");
                    Log.e("BOOKING_API", "Error loading LichTrinh: " + response.code());
                    // Vẫn hiển thị UI với dữ liệu từ Intent
                    displayBookingInfo();
                }
            }
            
            @Override
            public void onFailure(Call<LichTrinhLopHoc> call, Throwable t) {
                btnDatLich.setEnabled(true);
                btnDatLich.setText("Đặt lịch");
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
                    
                    // Hiển thị thông tin khóa học lên UI
                    displayKhoaHocInfo();
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
    private void checkAvailableSeats() {
        if (maLichTrinh == null || ngayThamGia == null) {
            Log.e("BOOKING_API", "Cannot check seats: maLichTrinh=" + maLichTrinh + ", ngayThamGia=" + ngayThamGia);
            btnDatLich.setEnabled(true);
            btnDatLich.setText("Đặt lịch");
            displayBookingInfo();
            return;
        }
        
        Log.d("BOOKING_API", "=== Calling checkAvailableSeats ===");
        Log.d("BOOKING_API", "maLichTrinh: " + maLichTrinh);
        Log.d("BOOKING_API", "ngayThamGia: " + ngayThamGia);
        Log.d("BOOKING_API", "API URL: " + RetrofitClient.getBaseUrl() + "api/lichtrinh/check-seats?maLichTrinh=" + maLichTrinh + "&ngayThamGia=" + ngayThamGia);
        
        apiService.checkAvailableSeats(maLichTrinh, ngayThamGia).enqueue(new Callback<CheckSeatsResponse>() {
            @Override
            public void onResponse(Call<CheckSeatsResponse> call, Response<CheckSeatsResponse> response) {
                Log.d("BOOKING_API", "=== checkAvailableSeats Response ===");
                Log.d("BOOKING_API", "Response code: " + response.code());
                Log.d("BOOKING_API", "Response successful: " + response.isSuccessful());
                
                btnDatLich.setEnabled(true);
                btnDatLich.setText("Đặt lịch");
                
                if (response.isSuccessful() && response.body() != null) {
                    CheckSeatsResponse seatsResponse = response.body();
                    
                    Log.d("BOOKING_API", "Response body:");
                    Log.d("BOOKING_API", "  - success: " + seatsResponse.isSuccess());
                    Log.d("BOOKING_API", "  - message: " + seatsResponse.getMessage());
                    Log.d("BOOKING_API", "  - maLichTrinh: " + seatsResponse.getMaLichTrinh());
                    Log.d("BOOKING_API", "  - maKhoaHoc: " + seatsResponse.getMaKhoaHoc());
                    Log.d("BOOKING_API", "  - tenKhoaHoc: " + seatsResponse.getTenKhoaHoc());
                    Log.d("BOOKING_API", "  - tongCho: " + seatsResponse.getTongCho());
                    Log.d("BOOKING_API", "  - daDat: " + seatsResponse.getDaDat());
                    Log.d("BOOKING_API", "  - conTrong: " + seatsResponse.getConTrong());
                    Log.d("BOOKING_API", "  - trangThai: " + seatsResponse.getTrangThai());
                    
                    // Xử lý null safety cho conTrong
                    Integer conTrong = seatsResponse.getConTrong();
                    soLuongConLai = (conTrong != null) ? conTrong : 0;
                    
                    Log.d("BOOKING_API", "Số chỗ còn trống (final): " + soLuongConLai);
                    
                    // Hiển thị thông tin lên UI
                    displayBookingInfo();
                    
                    // Kiểm tra nếu hết chỗ
                    if (soLuongConLai <= 0) {
                        Toast.makeText(Booking.this, "Lớp học đã hết chỗ", Toast.LENGTH_SHORT).show();
                        btnDatLich.setEnabled(false);
                        btnDatLich.setText("Hết chỗ");
                    }
                } else {
                    Log.e("BOOKING_API", "Error checking seats: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("BOOKING_API", "Error body: " + errorBody);
                    } catch (Exception e) {
                        Log.e("BOOKING_API", "Cannot read error body", e);
                    }
                    // Set giá trị mặc định
                    soLuongConLai = 10; // Giả định có 10 chỗ
                    // Vẫn hiển thị UI với dữ liệu mặc định
                    displayBookingInfo();
                }
            }
            
            @Override
            public void onFailure(Call<CheckSeatsResponse> call, Throwable t) {
                btnDatLich.setEnabled(true);
                btnDatLich.setText("Đặt lịch");
                Log.e("BOOKING_API", "=== checkAvailableSeats Failed ===");
                Log.e("BOOKING_API", "Error message: " + t.getMessage());
                Log.e("BOOKING_API", "Error class: " + t.getClass().getName());
                t.printStackTrace();
                
                Toast.makeText(Booking.this, "Không thể kết nối server. Vui lòng kiểm tra kết nối mạng.", Toast.LENGTH_LONG).show();
                
                // Set giá trị mặc định để user vẫn có thể đặt
                soLuongConLai = 10;
                // Vẫn hiển thị UI với dữ liệu mặc định
                displayBookingInfo();
            }
        });
    }
    
    /**
     * Cập nhật UI
     */
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
        
        // Giá tiền
        TextView txtGiaTien = findViewById(R.id.txt_GiaTien_DL);
        if (txtGiaTien != null && giaTien != null) {
            txtGiaTien.setText(formatCurrency(giaTien));
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
        
        // Hình ảnh giáo viên
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
        if (khoaHoc == null) return;
        
        Log.d("BOOKING_UI", "=== Displaying KhoaHoc Info ===");
        
        // Hình ảnh món ăn
        ImageView imMonAn = findViewById(R.id.im_MonAn_DL);
        if (imMonAn != null && khoaHoc.getHinhAnh() != null) {
            int resId = khoaHoc.getHinhAnhResId(this);
            imMonAn.setImageResource(resId);
            Log.d("BOOKING_UI", "Set image: " + khoaHoc.getHinhAnh());
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
     * Thực hiện đặt lịch
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
        
        // Tạo request
        DatLichRequest request = new DatLichRequest();
        request.setMaHocVien(sessionManager.getMaNguoiDung());
        request.setMaLichTrinh(maLichTrinh);
        request.setNgayThamGia(ngayThamGia);
        request.setSoLuongNguoi(soLuong);
        
        // Tính tổng tiền
        BigDecimal tongTien = giaTien.multiply(BigDecimal.valueOf(soLuong));
        request.setTongTien(tongTien);
        
        // Thông tin người đặt
        request.setTenNguoiDat(sessionManager.getHoTen());
        request.setEmailNguoiDat(sessionManager.getEmail());
        request.setSdtNguoiDat(""); // Có thể để trống hoặc lấy từ profile
        request.setGhiChu("");
        
        // Disable button
        btnDatLich.setEnabled(false);
        btnDatLich.setText("Đang xử lý...");
        
        // Call API
        apiService.createDatLich(request).enqueue(new Callback<DatLichResponse>() {
            @Override
            public void onResponse(Call<DatLichResponse> call, Response<DatLichResponse> response) {
                btnDatLich.setEnabled(true);
                btnDatLich.setText("Đặt lịch");
                
                if (response.isSuccessful() && response.body() != null) {
                    DatLichResponse datLichResponse = response.body();
                    
                    if (datLichResponse.isSuccess()) {
                        DatLich datLich = datLichResponse.getData();
                        
                        Toast.makeText(Booking.this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                        
                        // Chuyển sang PaymentActivity
                        Intent intent = new Intent(Booking.this, Payment.class);
                        intent.putExtra("maDatLich", datLich.getMaDatLich());
                        intent.putExtra("tenKhoaHoc", tenKhoaHoc);
                        intent.putExtra("ngayThamGia", datLich.getNgayThamGia());
                        intent.putExtra("soLuongNguoi", datLich.getSoLuongNguoi());
                        intent.putExtra("tongTien", datLich.getTongTien().toString());
                        intent.putExtra("trangThai", datLich.getTrangThai());
                        intent.putExtra("thoiGian", thoiGian);
                        intent.putExtra("diaDiem", diaDiem);
                        
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Booking.this, datLichResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Booking.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<DatLichResponse> call, Throwable t) {
                btnDatLich.setEnabled(true);
                btnDatLich.setText("Đặt lịch");
                Toast.makeText(Booking.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("BOOKING_ERROR", "Error: " + t.getMessage(), t);
            }
        });
    }

}
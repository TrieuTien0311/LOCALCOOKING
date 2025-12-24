package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bill extends AppCompatActivity {

    // Views - Thông tin thanh toán
    private TextView txtSoTienCard, txtSoTien, txtGioHoc, txtNgayHoc, txtMaGiaoDic, txtPhuongThuc;
    private MaterialButton btnTrangThai;
    
    // Views - Thông tin hủy (thêm mới)
    private TextView txtLabelThoiGianHuy, txtThoiGianHuy;
    
    // Views - Thông tin đặt vé
    private ImageView imAnhHoc, imMaCodeBill;
    private TextView txtTenLopHoc, txtDiaDiemBill, txtNguoiDatBill, txtSDTBill;
    private TextView txtThoiGianHocBill, txtNgayHocBill, txtSoPaxBill, txtNoiDungBill;
    
    // Views - Header
    private TextView txtTieuDeHeader, txtSubtitleHeader;
    private View view26; // Đường kẻ ngang trước mã vạch
    
    // Flag để biết đây là Bill sau thanh toán thành công
    private boolean isFromPaymentSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadDataFromIntent();

        // Xử lý nút quay lại
        ImageView btnBack = findViewById(R.id.imageView6);
        btnBack.setOnClickListener(v -> {
            if (isFromPaymentSuccess) {
                // Nếu từ thanh toán thành công -> quay về trang chủ
                navigateToHome();
            } else {
                // Nếu từ lịch sử đặt lịch -> quay lại Activity trước đó
                finish();
            }
        });
    }
    
    /**
     * Quay về trang chủ (Header) và xóa hết các Activity trước đó
     */
    private void navigateToHome() {
        Intent intent = new Intent(Bill.this, Header.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        if (isFromPaymentSuccess) {
            // Nếu từ thanh toán thành công -> quay về trang chủ
            navigateToHome();
        } else {
            // Nếu từ lịch sử đặt lịch -> quay lại Activity trước đó
            super.onBackPressed();
        }
    }

    private void initViews() {
        // Thông tin thanh toán
        txtSoTienCard = findViewById(R.id.textView45);
        txtSoTien = findViewById(R.id.txtSoTien);
        txtGioHoc = findViewById(R.id.txtGioHoc);
        txtNgayHoc = findViewById(R.id.txtNgayHoc);
        txtMaGiaoDic = findViewById(R.id.txtMaGiaoDic);
        txtPhuongThuc = findViewById(R.id.txtPhuongThuc);
        btnTrangThai = findViewById(R.id.button3);
        
        // Thông tin đặt vé
        imAnhHoc = findViewById(R.id.imAnhHoc);
        imMaCodeBill = findViewById(R.id.imMaCodeBill);
        txtTenLopHoc = findViewById(R.id.txtTenLopHoc);
        txtDiaDiemBill = findViewById(R.id.txtDiaDiemBill);
        txtNguoiDatBill = findViewById(R.id.txtNguoiDatBill);
        txtSDTBill = findViewById(R.id.txtSDTBill);
        txtThoiGianHocBill = findViewById(R.id.txtThoiGianHocBill);
        txtNgayHocBill = findViewById(R.id.txtNgayHocBill);
        txtSoPaxBill = findViewById(R.id.txtSoPaxBill);
        txtNoiDungBill = findViewById(R.id.txtNoiDungBill);
        
        // Header
        txtTieuDeHeader = findViewById(R.id.txtDiaDiem); // "Hóa đơn của tôi"
        txtSubtitleHeader = findViewById(R.id.txtThoiGian); // "Chi tiết hóa đơn đặt lịch"
        view26 = findViewById(R.id.view26); // Đường kẻ ngang trước mã vạch
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        
        // Kiểm tra xem có phải từ thanh toán thành công không
        isFromPaymentSuccess = intent.getBooleanExtra("paymentSuccess", false);
        
        // Lấy trạng thái đơn
        String trangThai = intent.getStringExtra("trangThai");
        String thoiGianHuy = intent.getStringExtra("thoiGianHuy");
        
        // Thông tin thanh toán
        double tongTienThanhToan = intent.getDoubleExtra("tongTienThanhToan", 0);
        String transId = intent.getStringExtra("transId");
        String ngayThanhToan = intent.getStringExtra("ngayThanhToan");
        
        // Hiển thị số tiền
        String formattedMoney = formatTien(tongTienThanhToan) + "₫";
        if (txtSoTienCard != null) {
            txtSoTienCard.setText("-" + formattedMoney);
        }
        if (txtSoTien != null) {
            txtSoTien.setText(formattedMoney);
        }
        
        // Xử lý hiển thị theo trạng thái
        setupTrangThai(trangThai, thoiGianHuy);
        
        // Thời gian thanh toán
        if (ngayThanhToan != null && !ngayThanhToan.isEmpty()) {
            // Parse từ ngayThanhToan (format: "yyyy-MM-dd HH:mm:ss" hoặc tương tự)
            String[] parts = parseDateTime(ngayThanhToan);
            if (txtGioHoc != null) {
                txtGioHoc.setText(parts[0]); // Giờ
            }
            if (txtNgayHoc != null) {
                txtNgayHoc.setText(parts[1]); // Ngày
            }
        } else {
            // Dùng thời gian hiện tại nếu không có
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date now = new Date();
            
            if (txtGioHoc != null) {
                txtGioHoc.setText(timeFormat.format(now));
            }
            if (txtNgayHoc != null) {
                txtNgayHoc.setText(dateFormat.format(now));
            }
        }
        
        // Mã giao dịch
        if (txtMaGiaoDic != null && transId != null) {
            txtMaGiaoDic.setText(transId);
        } else if (txtMaGiaoDic != null) {
            txtMaGiaoDic.setText("N/A");
        }
        
        // Phương thức thanh toán
        if (txtPhuongThuc != null) {
            txtPhuongThuc.setText("Momo");
        }
        
        // Thông tin lớp học
        String tenKhoaHoc = intent.getStringExtra("tenKhoaHoc");
        String diaDiem = intent.getStringExtra("diaDiem");
        String thoiGian = intent.getStringExtra("thoiGian");
        String ngayThamGia = intent.getStringExtra("ngayThamGia");
        String hinhAnh = intent.getStringExtra("hinhAnh");
        String moTa = intent.getStringExtra("moTa");
        int soLuongDat = intent.getIntExtra("soLuongDat", 1);
        
        // Thông tin người đặt
        String tenNguoiDat = intent.getStringExtra("tenNguoiDat");
        String sdtNguoiDat = intent.getStringExtra("sdtNguoiDat");
        
        // Hiển thị thông tin lớp học
        if (txtTenLopHoc != null && tenKhoaHoc != null) {
            txtTenLopHoc.setText(tenKhoaHoc);
        }
        
        if (txtDiaDiemBill != null && diaDiem != null) {
            txtDiaDiemBill.setText(diaDiem);
        }
        
        if (txtThoiGianHocBill != null && thoiGian != null) {
            txtThoiGianHocBill.setText(thoiGian);
        }
        
        if (txtNgayHocBill != null && ngayThamGia != null) {
            txtNgayHocBill.setText(formatNgay(ngayThamGia));
        }
        
        if (txtSoPaxBill != null) {
            txtSoPaxBill.setText(soLuongDat + " người");
        }
        
        if (txtNoiDungBill != null && moTa != null) {
            txtNoiDungBill.setText(moTa);
        }
        
        // Thông tin người đặt
        if (txtNguoiDatBill != null && tenNguoiDat != null) {
            txtNguoiDatBill.setText(tenNguoiDat);
        }
        
        if (txtSDTBill != null && sdtNguoiDat != null) {
            txtSDTBill.setText(sdtNguoiDat);
        }
        
        // Hình ảnh
        if (imAnhHoc != null && hinhAnh != null && !hinhAnh.isEmpty()) {
            String name = hinhAnh.replace(".png", "").replace(".jpg", "");
            int resId = getResources().getIdentifier(name, "drawable", getPackageName());
            if (resId != 0) {
                imAnhHoc.setImageResource(resId);
            }
        }
    }
    
    /**
     * Xử lý hiển thị theo trạng thái đơn
     */
    private void setupTrangThai(String trangThai, String thoiGianHuy) {
        if (trangThai == null) {
            trangThai = "Đã hoàn thành"; // Mặc định
        }
        
        if (trangThai.contains("huỷ") || trangThai.contains("hủy") || trangThai.contains("Hủy")) {
            // === TRẠNG THÁI ĐÃ HỦY ===
            
            // 1. Đổi tiêu đề header (txtDiaDiem) thành "Chi tiết đặt lịch"
            if (txtTieuDeHeader != null) {
                txtTieuDeHeader.setText("Chi tiết đặt lịch");
            }
            
            // 2. Đổi subtitle header (txtThoiGian) thành "Hóa đơn của lịch đã hủy"
            if (txtSubtitleHeader != null) {
                txtSubtitleHeader.setText("Hóa đơn của lịch đã hủy");
            }
            
            // 3. Ẩn đường kẻ ngang trước mã vạch (view26)
            if (view26 != null) {
                view26.setVisibility(View.GONE);
            }
            
            // 4. Đổi button thành "Đã hủy" với màu đỏ nhạt
            if (btnTrangThai != null) {
                btnTrangThai.setText("Đã hủy");
                btnTrangThai.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                btnTrangThai.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#FFCDD2"))); // Đỏ nhạt
            }
            
            // 5. Ẩn mã vạch
            if (imMaCodeBill != null) {
                imMaCodeBill.setVisibility(View.GONE);
            }
            
            // 6. Hiển thị thời gian hủy (thêm vào TableLayout)
            if (thoiGianHuy != null && !thoiGianHuy.isEmpty()) {
                addThoiGianHuyRow(thoiGianHuy);
            }
            
        } else {
            // === TRẠNG THÁI THÀNH CÔNG (Đã hoàn thành / Đặt trước đã thanh toán) ===
            
            // Button mặc định: "Thành công" màu xanh
            if (btnTrangThai != null) {
                btnTrangThai.setText("Thành công");
                btnTrangThai.setTextColor(android.graphics.Color.parseColor("#28C533"));
                btnTrangThai.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#C2F9C9"))); // Xanh nhạt
            }
            
            // Hiển thị mã vạch
            if (imMaCodeBill != null) {
                imMaCodeBill.setVisibility(View.VISIBLE);
            }
        }
    }
    
    /**
     * Thêm dòng thời gian hủy vào TableLayout
     */
    private void addThoiGianHuyRow(String thoiGianHuy) {
        // Tìm TableLayout chứa thông tin thanh toán
        android.widget.TableLayout tableLayout = findViewById(R.id.tableLayoutThanhToan);
        if (tableLayout == null) {
            // Nếu không có ID, tìm theo cách khác hoặc bỏ qua
            return;
        }
        
        // Tạo TableRow mới
        android.widget.TableRow row = new android.widget.TableRow(this);
        row.setPadding(0, 12, 0, 12);
        
        // Label
        TextView label = new TextView(this);
        label.setText("Thời gian hủy");
        label.setTextColor(android.graphics.Color.parseColor("#7F7F7F"));
        label.setTextSize(15);
        
        // Spacer
        View spacer = new View(this);
        android.widget.TableRow.LayoutParams spacerParams = new android.widget.TableRow.LayoutParams(0, 0, 1f);
        spacer.setLayoutParams(spacerParams);
        
        // Value
        TextView value = new TextView(this);
        String[] parts = parseDateTime(thoiGianHuy);
        value.setText(parts[0] + " " + parts[1]); // "HH:mm dd/MM/yyyy" (bỏ dấu gạch ngang)
        value.setTextColor(android.graphics.Color.parseColor("#D32F2F")); // Màu đỏ
        value.setTypeface(null, android.graphics.Typeface.ITALIC); // Định dạng Italic
        
        row.addView(label);
        row.addView(spacer);
        row.addView(value);
        
        tableLayout.addView(row);
    }
    
    /**
     * Parse datetime string thành [giờ, ngày]
     */
    private String[] parseDateTime(String dateTimeStr) {
        String gio = "";
        String ngay = "";
        
        try {
            if (dateTimeStr.contains("T")) {
                // Format ISO: "2024-12-20T10:30:00"
                String[] parts = dateTimeStr.split("T");
                ngay = formatNgay(parts[0]);
                if (parts.length > 1) {
                    gio = parts[1].substring(0, 5); // "HH:mm"
                }
            } else if (dateTimeStr.contains(" ")) {
                // Format: "2024-12-20 10:30:00"
                String[] parts = dateTimeStr.split(" ");
                ngay = formatNgay(parts[0]);
                if (parts.length > 1) {
                    gio = parts[1].substring(0, 5); // "HH:mm"
                }
            } else {
                // Chỉ có ngày
                ngay = formatNgay(dateTimeStr);
            }
        } catch (Exception e) {
            ngay = dateTimeStr;
        }
        
        return new String[]{gio, ngay};
    }

    /**
     * Format ngày từ "2025-12-25" sang "25/12/2025"
     */
    private String formatNgay(String ngayStr) {
        try {
            String[] parts = ngayStr.split("-");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ngayStr;
    }

    /**
     * Format tiền với dấu chấm phân cách
     */
    private String formatTien(double tien) {
        return String.format("%,.0f", tien).replace(",", ".");
    }
}

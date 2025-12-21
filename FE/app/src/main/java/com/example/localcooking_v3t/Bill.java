package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bill extends AppCompatActivity {

    // Views - Thông tin thanh toán
    private TextView txtSoTienCard, txtSoTien, txtGioHoc, txtNgayHoc, txtMaGiaoDic, txtPhuongThuc;
    
    // Views - Thông tin đặt vé
    private ImageView imAnhHoc;
    private TextView txtTenLopHoc, txtDiaDiemBill, txtNguoiDatBill, txtSDTBill;
    private TextView txtThoiGianHocBill, txtNgayHocBill, txtSoPaxBill, txtNoiDungBill;

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

        // Xử lý nút quay lại - về trang chủ (Header)
        ImageView btnBack = findViewById(R.id.imageView6);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Bill.this, Header.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        // Thông tin thanh toán
        txtSoTienCard = findViewById(R.id.textView45);
        txtSoTien = findViewById(R.id.txtSoTien);
        txtGioHoc = findViewById(R.id.txtGioHoc);
        txtNgayHoc = findViewById(R.id.txtNgayHoc);
        txtMaGiaoDic = findViewById(R.id.txtMaGiaoDic);
        txtPhuongThuc = findViewById(R.id.txtPhuongThuc);
        
        // Thông tin đặt vé
        imAnhHoc = findViewById(R.id.imAnhHoc);
        txtTenLopHoc = findViewById(R.id.txtTenLopHoc);
        txtDiaDiemBill = findViewById(R.id.txtDiaDiemBill);
        txtNguoiDatBill = findViewById(R.id.txtNguoiDatBill);
        txtSDTBill = findViewById(R.id.txtSDTBill);
        txtThoiGianHocBill = findViewById(R.id.txtThoiGianHocBill);
        txtNgayHocBill = findViewById(R.id.txtNgayHocBill);
        txtSoPaxBill = findViewById(R.id.txtSoPaxBill);
        txtNoiDungBill = findViewById(R.id.txtNoiDungBill);
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        
        // Thông tin thanh toán
        double tongTienThanhToan = intent.getDoubleExtra("tongTienThanhToan", 0);
        String transId = intent.getStringExtra("transId");
        
        // Hiển thị số tiền
        String formattedMoney = formatTien(tongTienThanhToan) + "₫";
        if (txtSoTienCard != null) {
            txtSoTienCard.setText("-" + formattedMoney);
        }
        if (txtSoTien != null) {
            txtSoTien.setText(formattedMoney);
        }
        
        // Thời gian thanh toán (hiện tại)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date now = new Date();
        
        if (txtGioHoc != null) {
            txtGioHoc.setText(timeFormat.format(now));
        }
        if (txtNgayHoc != null) {
            txtNgayHoc.setText(dateFormat.format(now));
        }
        
        // Mã giao dịch
        if (txtMaGiaoDic != null && transId != null) {
            txtMaGiaoDic.setText(transId);
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

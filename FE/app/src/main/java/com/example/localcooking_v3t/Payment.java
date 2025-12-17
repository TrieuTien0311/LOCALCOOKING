package com.example.localcooking_v3t;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;

public class Payment extends AppCompatActivity {

    // Views
    private ShapeableImageView imgMonAn;
    private TextView txtTenLop, txtGiaTien, txtSoLuongDat, txtThoiGian, txtNgay, txtDiaDiem;
    private TextView txtTongTien, txtTongTien_CTiet, txtTongThanhToan, txtTienGiam;
    private Button btnConfirmPayment;
    private ImageView btnBack;
    private RadioGroup rdGroupPayment;
    private RadioButton rdMomo, rdThe;
    private ImageView txtTrangThai1, txtTrangThai2;

    // Dữ liệu nhận được
    private Class lopHoc;
    private int soLuongDat = 1;
    private double tongTien = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        nhanDuLieuTuIntent();
        capNhatGiaoDien();
        xuLySuKien();
    }

    private void initViews() {
        imgMonAn = findViewById(R.id.imgMonAn);
        txtTenLop = findViewById(R.id.txtTenLop);
        txtGiaTien = findViewById(R.id.txtGiaTien);
        txtSoLuongDat = findViewById(R.id.txtSoLuongDat);
        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtNgay = findViewById(R.id.txtNgay);
        txtDiaDiem = findViewById(R.id.txtDiaDiem);

        txtTongTien = findViewById(R.id.txtTongTien);
        txtTongTien_CTiet = findViewById(R.id.txtTongTien_CTiet);
        txtTongThanhToan = findViewById(R.id.txtTongThanhToan);
        txtTienGiam = findViewById(R.id.txtTienGiam);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        btnBack = findViewById(R.id.btn_Back);

        rdGroupPayment = findViewById(R.id.rdGroupPayment);
        rdMomo = findViewById(R.id.rdMomo);
        rdThe = findViewById(R.id.rdThe);
        txtTrangThai1 = findViewById(R.id.txtTrangThai1);
        txtTrangThai2 = findViewById(R.id.txtTrangThai2);
    }

    private void nhanDuLieuTuIntent() {
        lopHoc = getIntent().getParcelableExtra("lopHoc");
        soLuongDat = getIntent().getIntExtra("soLuongDat", 1);
        tongTien = getIntent().getDoubleExtra("tongTien", 0);

        if (lopHoc == null) {
            Toast.makeText(this, "Không nhận được thông tin lớp học!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void capNhatGiaoDien() {
        if (lopHoc == null) return;

        // Hiển thị thông tin lớp học
        txtTenLop.setText(lopHoc.getTenLop());
        txtGiaTien.setText(lopHoc.getGia());
        txtSoLuongDat.setText("SL: " + soLuongDat + " người");
        txtThoiGian.setText("Thời gian: " + lopHoc.getThoiGian());
        txtNgay.setText("Ngày: " + lopHoc.getNgay());
        txtDiaDiem.setText("Địa điểm: " + lopHoc.getDiaDiem());

        if (lopHoc.getHinhAnh() != 0) {
            imgMonAn.setImageResource(lopHoc.getHinhAnh());
        }

        // Tính toán tiền
        double giaGoc = lopHoc.getGiaSo();
        double tongGoc = giaGoc * soLuongDat;
        double giamGia = lopHoc.isCoUuDai() ? tongGoc * 0.21 : 0; // Giả sử giảm 21%
        double thanhToan = tongGoc - giamGia;

        // Cập nhật các TextView tiền
        txtTongTien.setText(formatTien(tongGoc) + "₫");
        txtTongTien_CTiet.setText(formatTien(tongGoc) + "₫");
        txtTongThanhToan.setText(formatTien(thanhToan) + "₫");
        txtTienGiam.setText("-" + formatTien(giamGia) + "₫");

        // Cập nhật tổng ở bottom
        TextView txtTotalAmount = findViewById(R.id.txtTotalAmount);
        TextView txtVAT = findViewById(R.id.txtVAT);
        if (txtTotalAmount != null) txtTotalAmount.setText(formatTien(thanhToan) + "₫");
        if (txtVAT != null && giamGia > 0) {
            txtVAT.setText("Tiết kiệm " + formatTien(giamGia) + "₫");
            txtVAT.setVisibility(View.VISIBLE);
        }
    }

    private void xuLySuKien() {
        btnBack.setOnClickListener(v -> finish());

        rdGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rdMomo) {
                txtTrangThai1.setVisibility(View.VISIBLE);
                txtTrangThai2.setVisibility(View.INVISIBLE);
            } else if (checkedId == R.id.rdThe) {
                txtTrangThai1.setVisibility(View.INVISIBLE);
                txtTrangThai2.setVisibility(View.VISIBLE);
            }
        });

        btnConfirmPayment.setOnClickListener(v -> {
            Toast.makeText(this, "Đặt lịch thành công! Cảm ơn bạn ❤️", Toast.LENGTH_LONG).show();
            // TODO: Gọi API đặt lịch, lưu vào database, gửi thông báo...
            finish(); // Quay về màn hình trước
        });
    }

    // Helper: Định dạng tiền đẹp
    private String formatTien(double tien) {
        return String.format("%,.0f", tien).replace(",", ".");
    }
}
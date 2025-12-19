package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.localcooking_v3t.model.LopHoc;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

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
    private TextInputEditText idName, idEmail, idPhone;
    private View mainLayout;

    // Dữ liệu nhận được
    private LopHoc lopHoc;
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
        setupClearFocusOnTouch();
    }

    private void initViews() {
        mainLayout = findViewById(R.id.main);
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

        idName = findViewById(R.id.idName);
        idEmail = findViewById(R.id.idEmail);
        idPhone = findViewById(R.id.idPhone);
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

        if (lopHoc.getHinhAnh() != null && !lopHoc.getHinhAnh().isEmpty()) {
            imgMonAn.setImageResource(lopHoc.getHinhAnhResId(this));
        }

        // Tính toán tiền
        double giaGoc = lopHoc.getGiaSo();
        double tongGoc = giaGoc * soLuongDat;
        double giamGia = (lopHoc.getCoUuDai() != null && lopHoc.getCoUuDai()) ? tongGoc * 0.21 : 0; // Giả sử giảm 21%
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

        // Xử lý click vào "Thêm ưu đãi" - chuyển đến Vouchers
        findViewById(R.id.cardView2).setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, Vouchers.class);
            startActivity(intent);
        });

        // Xử lý RadioButton Momo
        rdMomo.setOnClickListener(v -> {
            rdMomo.setChecked(true);
            rdThe.setChecked(false);
            txtTrangThai1.setVisibility(View.VISIBLE);
            txtTrangThai2.setVisibility(View.INVISIBLE);
        });

        // Xử lý RadioButton Thẻ
        rdThe.setOnClickListener(v -> {
            rdThe.setChecked(true);
            rdMomo.setChecked(false);
            txtTrangThai1.setVisibility(View.INVISIBLE);
            txtTrangThai2.setVisibility(View.VISIBLE);
        });

        // Nút thanh toán - chuyển đến Bill
        btnConfirmPayment.setOnClickListener(v -> {
            // Validate thông tin liên hệ
            String name = idName.getText() != null ? idName.getText().toString().trim() : "";
            String email = idEmail.getText() != null ? idEmail.getText().toString().trim() : "";
            String phone = idPhone.getText() != null ? idPhone.getText().toString().trim() : "";

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin liên hệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Payment.this, Bill.class);
            startActivity(intent);
        });
    }

    /**
     * Thiết lập clear focus khi chạm vào vùng ngoài EditText
     */
    private void setupClearFocusOnTouch() {
        if (mainLayout != null) {
            setupTouchListener(mainLayout);
        }
    }

    /**
     * Thiết lập touch listener đệ quy cho tất cả các view
     */
    private void setupTouchListener(View view) {
        // Nếu không phải EditText, thiết lập listener để clear focus
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clearFocusFromEditTexts();
                }
                return false;
            });
        }

        // Nếu là ViewGroup, đệ quy cho các view con
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setupTouchListener(child);
            }
        }
    }

    /**
     * Xóa focus khỏi tất cả EditText và ẩn bàn phím
     */
    private void clearFocusFromEditTexts() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
            // Ẩn bàn phím
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
        // Request focus vào main layout để EditText mất focus hoàn toàn
        if (mainLayout != null) {
            mainLayout.requestFocus();
        }
    }

    /**
     * Override dispatchTouchEvent để xử lý clear focus toàn cục
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = event.getRawX() + v.getLeft() - location[0];
                float y = event.getRawY() + v.getTop() - location[1];

                // Nếu chạm bên ngoài EditText đang focus
                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    clearFocusFromEditTexts();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // Helper: Định dạng tiền đẹp
    private String formatTien(double tien) {
        return String.format("%,.0f", tien).replace(",", ".");
    }
}
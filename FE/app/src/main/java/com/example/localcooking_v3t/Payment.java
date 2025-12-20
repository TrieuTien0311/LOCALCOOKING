package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ApDungUuDaiRequest;
import com.example.localcooking_v3t.model.ApDungUuDaiResponse;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.utils.SessionManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment extends AppCompatActivity {

    private static final String TAG = "Payment";
    private static final int REQUEST_VOUCHER = 1001;

    // Views
    private ShapeableImageView imgMonAn;
    private TextView txtTenLop, txtGiaTien, txtSoLuongDat, txtThoiGian, txtNgay, txtDiaDiem;
    private TextView txtTongTien, txtTongTien_CTiet, txtTongThanhToan, txtTienGiam;
    private TextView txtVoucherName;
    private Button btnConfirmPayment;
    private ImageView btnBack;
    private RadioGroup rdGroupPayment;
    private RadioButton rdMomo, rdThe;
    private ImageView txtTrangThai1, txtTrangThai2;
    private TextInputEditText idName, idEmail, idPhone;
    private View mainLayout;

    // Dữ liệu nhận được
    private KhoaHoc lopHoc;
    private int soLuongDat = 1;
    private double tongTien = 0;

    // Ưu đãi
    private Integer selectedMaUuDai;
    private String selectedMaCode;
    private Double soTienGiam = 0.0;
    private Double tongTienSauGiam = 0.0;

    private SessionManager sessionManager;
    private ActivityResultLauncher<Intent> voucherLauncher;

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

        sessionManager = new SessionManager(this);

        // Đăng ký launcher cho Vouchers activity
        voucherLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        selectedMaUuDai = data.getIntExtra(Vouchers.RESULT_MA_UU_DAI, -1);
                        selectedMaCode = data.getStringExtra(Vouchers.RESULT_MA_CODE);
                        String tenUuDai = data.getStringExtra(Vouchers.RESULT_TEN_UU_DAI);

                        if (selectedMaCode != null) {
                            // Gọi API để tính toán giảm giá
                            apDungMaUuDai(selectedMaCode);
                            if (txtVoucherName != null && tenUuDai != null) {
                                txtVoucherName.setText(tenUuDai);
                            }
                        }
                    }
                }
        );

        initViews();
        nhanDuLieuTuIntent();
        capNhatGiaoBan();
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

        // Tìm TextView hiển thị tên voucher (nếu có trong layout)
        try {
            txtVoucherName = findViewById(R.id.txtVoucherName);
        } catch (Exception e) {
            // View không tồn tại
        }
    }

    private void nhanDuLieuTuIntent() {
        lopHoc = getIntent().getParcelableExtra("lopHoc");
        soLuongDat = getIntent().getIntExtra("soLuongDat", 1);
        tongTien = getIntent().getDoubleExtra("tongTien", 0);
        tongTienSauGiam = tongTien;

        if (lopHoc == null) {
            Toast.makeText(this, "Không nhận được thông tin lớp học!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void capNhatGiaoBan() {
        // Cập nhật UI với thông tin ban đầu
        if (txtTongTien != null) txtTongTien.setText(formatTien(tongTien) + "đ");
        if (txtTongTien_CTiet != null) txtTongTien_CTiet.setText(formatTien(tongTien) + "đ");
        if (txtTongThanhToan != null) txtTongThanhToan.setText(formatTien(tongTien) + "đ");
        if (txtTienGiam != null) txtTienGiam.setText("-0đ");
        if (txtSoLuongDat != null) txtSoLuongDat.setText(String.valueOf(soLuongDat));
    }

    private void apDungMaUuDai(String maCode) {
        Integer maHocVien = sessionManager.getMaNguoiDung();
        if (maHocVien == null || maHocVien == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để sử dụng mã ưu đãi", Toast.LENGTH_SHORT).show();
            return;
        }

        ApDungUuDaiRequest request = new ApDungUuDaiRequest(
                maHocVien,
                maCode,
                tongTien,
                soLuongDat
        );

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ApDungUuDaiResponse> call = apiService.apDungUuDai(request);

        call.enqueue(new Callback<ApDungUuDaiResponse>() {
            @Override
            public void onResponse(Call<ApDungUuDaiResponse> call, Response<ApDungUuDaiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApDungUuDaiResponse result = response.body();
                    if (result.isThanhCong()) {
                        soTienGiam = result.getSoTienGiam();
                        tongTienSauGiam = result.getTongTienSauGiam();
                        selectedMaUuDai = result.getMaUuDai();

                        // Cập nhật UI
                        capNhatGiaUuDai();
                        Toast.makeText(Payment.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Payment.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        resetUuDai();
                    }
                } else {
                    Toast.makeText(Payment.this, "Không thể áp dụng mã ưu đãi", Toast.LENGTH_SHORT).show();
                    resetUuDai();
                }
            }

            @Override
            public void onFailure(Call<ApDungUuDaiResponse> call, Throwable t) {
                Log.e(TAG, "Error applying voucher", t);
                Toast.makeText(Payment.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetUuDai();
            }
        });
    }

    private void capNhatGiaUuDai() {
        if (txtTienGiam != null) {
            txtTienGiam.setText("-" + formatTien(soTienGiam) + "đ");
        }
        if (txtTongThanhToan != null) {
            txtTongThanhToan.setText(formatTien(tongTienSauGiam) + "đ");
        }
    }

    private void resetUuDai() {
        selectedMaUuDai = null;
        selectedMaCode = null;
        soTienGiam = 0.0;
        tongTienSauGiam = tongTien;

        if (txtTienGiam != null) txtTienGiam.setText("-0đ");
        if (txtTongThanhToan != null) txtTongThanhToan.setText(formatTien(tongTien) + "đ");
        if (txtVoucherName != null) txtVoucherName.setText("Thêm ưu đãi");
    }

    private void xuLySuKien() {
        btnBack.setOnClickListener(v -> finish());

        // Xử lý click vào "Thêm ưu đãi" - chuyển đến Vouchers
        findViewById(R.id.cardView2).setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, Vouchers.class);
            intent.putExtra(Vouchers.EXTRA_MA_HOC_VIEN, sessionManager.getMaNguoiDung());
            intent.putExtra(Vouchers.EXTRA_SO_LUONG_NGUOI, soLuongDat);
            intent.putExtra(Vouchers.EXTRA_TONG_TIEN, tongTien);
            voucherLauncher.launch(intent);
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

            // Xác nhận sử dụng mã ưu đãi nếu có
            if (selectedMaUuDai != null) {
                confirmUuDai(selectedMaUuDai);
            }

            Intent intent = new Intent(Payment.this, Bill.class);
            intent.putExtra("tongTienGoc", tongTien);
            intent.putExtra("soTienGiam", soTienGiam);
            intent.putExtra("tongTienThanhToan", tongTienSauGiam);
            intent.putExtra("maUuDai", selectedMaUuDai);
            startActivity(intent);
        });
    }

    private void confirmUuDai(Integer maUuDai) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.confirmUuDai(maUuDai);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "Voucher confirmed: " + maUuDai);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Failed to confirm voucher", t);
            }
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
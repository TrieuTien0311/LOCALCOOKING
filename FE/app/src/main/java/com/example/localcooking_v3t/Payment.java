package com.example.localcooking_v3t;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ApDungUuDaiRequest;
import com.example.localcooking_v3t.model.ApDungUuDaiResponse;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.model.MomoPaymentRequest;
import com.example.localcooking_v3t.model.MomoPaymentResponse;
import com.example.localcooking_v3t.utils.SessionManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;

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
    private TextView txtVoucherName; // Sử dụng txtChonUuDai trong layout
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

    // Momo
    private String currentOrderId;
    private Handler paymentCheckHandler;
    private Runnable paymentCheckRunnable;
    private boolean isCheckingPayment = false;

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

        // Sử dụng txtChonUuDai để hiển thị tên voucher đã chọn
        txtVoucherName = findViewById(R.id.txtChonUuDai);
    }

    private void nhanDuLieuTuIntent() {
        // Nhận các thông tin cơ bản
        soLuongDat = getIntent().getIntExtra("soLuongDat", 1);
        tongTien = getIntent().getDoubleExtra("tongTien", 0);
        tongTienSauGiam = tongTien;
        
        // Kiểm tra dữ liệu
        if (tongTien <= 0) {
            Toast.makeText(this, "Không nhận được thông tin giá tiền!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        Log.d(TAG, "=== Nhận dữ liệu từ Intent ===");
        Log.d(TAG, "Số lượng đặt: " + soLuongDat);
        Log.d(TAG, "Tổng tiền: " + tongTien);
        
        // Hiển thị thông tin lên UI
        hienThiThongTinLopHoc();
        
        // Cập nhật phần bottom (tổng tiền)
        capNhatGiaoBan();
    }
    
    /**
     * Cập nhật giá ở phần bottom
     */
    private void capNhatGiaoBan() {
        Log.d(TAG, "=== Cập nhật giá bottom ===");
        Log.d(TAG, "Tổng tiền: " + tongTien);
        Log.d(TAG, "Tổng tiền sau giảm: " + tongTienSauGiam);
        
        // Cập nhật UI với thông tin ban đầu
        if (txtTongTien != null) {
            txtTongTien.setText(formatTien(tongTien) + "đ");
            Log.d(TAG, "Set txtTongTien: " + formatTien(tongTien) + "đ");
        }
        
        if (txtTongTien_CTiet != null) {
            txtTongTien_CTiet.setText(formatTien(tongTien) + "đ");
            Log.d(TAG, "Set txtTongTien_CTiet: " + formatTien(tongTien) + "đ");
        }
        
        if (txtTongThanhToan != null) {
            txtTongThanhToan.setText(formatTien(tongTienSauGiam) + "đ");
            Log.d(TAG, "Set txtTongThanhToan: " + formatTien(tongTienSauGiam) + "đ");
        }
        
        if (txtTienGiam != null) {
            txtTienGiam.setText("-0đ");
        }
        
        // Không set txtSoLuongDat ở đây nữa vì đã set trong hienThiThongTinLopHoc()
    }
    
    /**
     * Hiển thị thông tin lớp học lên UI
     */
    private void hienThiThongTinLopHoc() {
        // Tên lớp học
        if (txtTenLop != null) {
            String tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
            if (tenKhoaHoc != null) {
                txtTenLop.setText(tenKhoaHoc);
            }
        }
        
        // Kiểm tra có ưu đãi giờ chót không
        boolean coUuDai = getIntent().getBooleanExtra("coUuDai", false);
        double giaDonVi = tongTien / soLuongDat;
        
        // Giá tiền (đơn giá) - hiển thị giá sau giảm nếu có ưu đãi
        if (txtGiaTien != null) {
            if (coUuDai) {
                // Có ưu đãi: hiển thị giá đã giảm
                txtGiaTien.setText(formatTien(giaDonVi) + "đ");
            } else {
                // Không ưu đãi: hiển thị giá gốc
                txtGiaTien.setText(formatTien(giaDonVi) + "đ");
            }
        }
        
        // Số lượng - thêm text "người"
        if (txtSoLuongDat != null) {
            txtSoLuongDat.setText("SL: " + soLuongDat + " người");
        }
        
        // Thời gian
        if (txtThoiGian != null) {
            String thoiGian = getIntent().getStringExtra("thoiGian");
            if (thoiGian != null) {
                txtThoiGian.setText("Thời gian: " + thoiGian);
            }
        }
        
        // Ngày tham gia
        if (txtNgay != null) {
            String ngayThamGia = getIntent().getStringExtra("ngayThamGia");
            if (ngayThamGia != null) {
                txtNgay.setText("Ngày: " + formatNgay(ngayThamGia));
            }
        }
        
        // Địa điểm
        if (txtDiaDiem != null) {
            String diaDiem = getIntent().getStringExtra("diaDiem");
            if (diaDiem != null) {
                txtDiaDiem.setText("Địa điểm: " + diaDiem);
            }
        }
        
        // Hình ảnh
        if (imgMonAn != null) {
            String hinhAnh = getIntent().getStringExtra("hinhAnh");
            if (hinhAnh != null && !hinhAnh.isEmpty()) {
                // Loại bỏ extension
                String name = hinhAnh.replace(".png", "").replace(".jpg", "");
                // Lấy resource ID
                int resId = getResources().getIdentifier(name, "drawable", getPackageName());
                if (resId != 0) {
                    imgMonAn.setImageResource(resId);
                } else {
                    // Hình mặc định
                    imgMonAn.setImageResource(getResources().getIdentifier("phobo", "drawable", getPackageName()));
                }
            }
        }
        
        Log.d(TAG, "Hiển thị thông tin lớp học:");
        Log.d(TAG, "- Tên: " + getIntent().getStringExtra("tenKhoaHoc"));
        Log.d(TAG, "- Số lượng: " + soLuongDat + " người");
        Log.d(TAG, "- Có ưu đãi: " + coUuDai);
        Log.d(TAG, "- Tổng tiền: " + tongTien);
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
            Log.e(TAG, "Error formatting date: " + e.getMessage());
        }
        return ngayStr;
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
        if (txtVoucherName != null) txtVoucherName.setText("Chọn để khám phá nhiều ưu đãi");
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

        // Nút thanh toán - Gọi Momo
        btnConfirmPayment.setOnClickListener(v -> {
            // Validate thông tin liên hệ
            String name = idName.getText() != null ? idName.getText().toString().trim() : "";
            String email = idEmail.getText() != null ? idEmail.getText().toString().trim() : "";
            String phone = idPhone.getText() != null ? idPhone.getText().toString().trim() : "";

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin liên hệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra phương thức thanh toán
            if (!rdMomo.isChecked() && !rdThe.isChecked()) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rdMomo.isChecked()) {
                // Thanh toán qua Momo
                createMomoPayment(name, email, phone);
            } else {
                // Thanh toán qua thẻ (chưa implement)
                Toast.makeText(this, "Thanh toán qua thẻ đang phát triển!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Tạo thanh toán Momo
     */
    private void createMomoPayment(String name, String email, String phone) {
        // Hiển thị loading
        btnConfirmPayment.setEnabled(false);
        btnConfirmPayment.setText("Đang xử lý...");

        // Tạo request
        MomoPaymentRequest request = new MomoPaymentRequest();
        request.setMaHocVien(sessionManager.getMaNguoiDung());
        request.setMaLichTrinh(getIntent().getIntExtra("maLichTrinh", 0));
        request.setSoTien(BigDecimal.valueOf(tongTienSauGiam));
        request.setTenKhoaHoc(getIntent().getStringExtra("tenKhoaHoc"));
        request.setNgayThamGia(getIntent().getStringExtra("ngayThamGia"));
        request.setSoLuongNguoi(soLuongDat);
        request.setTenNguoiDat(name);
        request.setEmailNguoiDat(email);
        request.setSdtNguoiDat(phone);
        
        if (selectedMaUuDai != null) {
            request.setMaUuDai(selectedMaUuDai);
            request.setSoTienGiam(BigDecimal.valueOf(soTienGiam));
        }

        Log.d(TAG, "Creating Momo payment: " + tongTienSauGiam);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.createMomoPayment(request).enqueue(new Callback<MomoPaymentResponse>() {
            @Override
            public void onResponse(Call<MomoPaymentResponse> call, Response<MomoPaymentResponse> response) {
                btnConfirmPayment.setEnabled(true);
                btnConfirmPayment.setText("Thanh toán");

                if (response.isSuccessful() && response.body() != null) {
                    MomoPaymentResponse momoResponse = response.body();
                    
                    if (momoResponse.isSuccess()) {
                        currentOrderId = momoResponse.getOrderId();
                        String payUrl = momoResponse.getPayUrl();
                        String deeplink = momoResponse.getDeeplink();
                        
                        Log.d(TAG, "Momo payment created: orderId=" + currentOrderId);
                        Log.d(TAG, "PayUrl: " + payUrl);
                        Log.d(TAG, "Deeplink: " + deeplink);
                        
                        // Mở Momo app hoặc web
                        openMomoPayment(deeplink, payUrl);
                        
                        // Bắt đầu kiểm tra trạng thái thanh toán
                        startPaymentStatusCheck();
                    } else {
                        Toast.makeText(Payment.this, 
                            "Lỗi: " + momoResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Payment.this, 
                        "Không thể tạo thanh toán Momo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MomoPaymentResponse> call, Throwable t) {
                btnConfirmPayment.setEnabled(true);
                btnConfirmPayment.setText("Thanh toán");
                Log.e(TAG, "Momo payment error", t);
                Toast.makeText(Payment.this, 
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Mở Momo web để thanh toán (Sandbox chỉ hỗ trợ web, không hỗ trợ app thật)
     */
    private void openMomoPayment(String deeplink, String payUrl) {
        // QUAN TRỌNG: Momo Sandbox chỉ hoạt động qua WEB
        // Deeplink sẽ mở app Momo thật nhưng giao dịch sandbox không tồn tại trên app thật
        // Nên luôn mở payUrl (web) để test sandbox
        
        try {
            if (payUrl != null && !payUrl.isEmpty()) {
                Log.d(TAG, "Opening Momo Sandbox web: " + payUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(payUrl));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không có link thanh toán", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Cannot open payment URL", e);
            Toast.makeText(this, "Không thể mở trang thanh toán", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Bắt đầu kiểm tra trạng thái thanh toán định kỳ
     */
    private void startPaymentStatusCheck() {
        if (isCheckingPayment) return;
        
        isCheckingPayment = true;
        paymentCheckHandler = new Handler(Looper.getMainLooper());
        
        paymentCheckRunnable = new Runnable() {
            int checkCount = 0;
            final int MAX_CHECKS = 60; // Kiểm tra tối đa 60 lần (5 phút)
            
            @Override
            public void run() {
                if (!isCheckingPayment || checkCount >= MAX_CHECKS) {
                    stopPaymentStatusCheck();
                    return;
                }
                
                checkCount++;
                checkPaymentStatus();
                
                // Kiểm tra lại sau 5 giây
                paymentCheckHandler.postDelayed(this, 5000);
            }
        };
        
        // Bắt đầu kiểm tra sau 3 giây
        paymentCheckHandler.postDelayed(paymentCheckRunnable, 3000);
    }

    /**
     * Dừng kiểm tra trạng thái thanh toán
     */
    private void stopPaymentStatusCheck() {
        isCheckingPayment = false;
        if (paymentCheckHandler != null && paymentCheckRunnable != null) {
            paymentCheckHandler.removeCallbacks(paymentCheckRunnable);
        }
    }

    /**
     * Kiểm tra trạng thái thanh toán
     */
    private void checkPaymentStatus() {
        if (currentOrderId == null) return;

        ApiService apiService = RetrofitClient.getApiService();
        apiService.checkMomoPaymentStatus(currentOrderId).enqueue(new Callback<MomoPaymentResponse>() {
            @Override
            public void onResponse(Call<MomoPaymentResponse> call, Response<MomoPaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MomoPaymentResponse status = response.body();
                    
                    if (status.isSuccess()) {
                        // Thanh toán thành công!
                        stopPaymentStatusCheck();
                        onPaymentSuccess(status);
                    } else if (status.getResultCode() != -1 && status.getResultCode() != 0) {
                        // Thanh toán thất bại
                        stopPaymentStatusCheck();
                        onPaymentFailed(status.getMessage());
                    }
                    // Nếu resultCode == -1 hoặc 0 nhưng chưa success, tiếp tục chờ
                }
            }

            @Override
            public void onFailure(Call<MomoPaymentResponse> call, Throwable t) {
                Log.e(TAG, "Error checking payment status", t);
            }
        });
    }

    /**
     * Xử lý khi thanh toán thành công
     */
    private void onPaymentSuccess(MomoPaymentResponse response) {
        Log.d(TAG, "Payment successful! TransId: " + response.getTransId());
        
        // Xác nhận sử dụng mã ưu đãi nếu có
        if (selectedMaUuDai != null) {
            confirmUuDai(selectedMaUuDai);
        }

        // Hiển thị dialog thành công
        new AlertDialog.Builder(this)
            .setTitle("🎉 Thanh toán thành công!")
            .setMessage("Cảm ơn bạn đã đặt lịch học.\n\nMã giao dịch: " + response.getTransId())
            .setPositiveButton("Xem hóa đơn", (dialog, which) -> {
                goToBill(response.getTransId());
            })
            .setCancelable(false)
            .show();
    }

    /**
     * Chuyển sang màn hình Bill với đầy đủ thông tin
     */
    private void goToBill(String transId) {
        Intent intent = new Intent(Payment.this, Bill.class);
        
        // Thông tin thanh toán
        intent.putExtra("tongTienGoc", tongTien);
        intent.putExtra("soTienGiam", soTienGiam);
        intent.putExtra("tongTienThanhToan", tongTienSauGiam);
        intent.putExtra("orderId", currentOrderId);
        intent.putExtra("transId", transId);
        intent.putExtra("paymentSuccess", true);
        
        // Thông tin lớp học
        intent.putExtra("tenKhoaHoc", getIntent().getStringExtra("tenKhoaHoc"));
        intent.putExtra("diaDiem", getIntent().getStringExtra("diaDiem"));
        intent.putExtra("thoiGian", getIntent().getStringExtra("thoiGian"));
        intent.putExtra("ngayThamGia", getIntent().getStringExtra("ngayThamGia"));
        intent.putExtra("hinhAnh", getIntent().getStringExtra("hinhAnh"));
        intent.putExtra("moTa", getIntent().getStringExtra("moTa"));
        intent.putExtra("soLuongDat", soLuongDat);
        
        // Thông tin người đặt
        String tenNguoiDat = idName.getText() != null ? idName.getText().toString().trim() : "";
        String sdtNguoiDat = idPhone.getText() != null ? idPhone.getText().toString().trim() : "";
        intent.putExtra("tenNguoiDat", tenNguoiDat);
        intent.putExtra("sdtNguoiDat", sdtNguoiDat);
        
        startActivity(intent);
        finish();
    }

    /**
     * Xử lý khi thanh toán thất bại
     */
    private void onPaymentFailed(String message) {
        Log.e(TAG, "Payment failed: " + message);
        
        new AlertDialog.Builder(this)
            .setTitle("❌ Thanh toán thất bại")
            .setMessage(message != null ? message : "Giao dịch không thành công. Vui lòng thử lại.")
            .setPositiveButton("Thử lại", (dialog, which) -> {
                // Reset để thử lại
                currentOrderId = null;
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Khi quay lại từ Momo web, dừng auto-check và hiển thị dialog hỏi kết quả
        if (currentOrderId != null) {
            stopPaymentStatusCheck(); // Dừng auto-check
            showPaymentResultDialog(); // Hiện dialog để user chọn
        }
    }

    /**
     * Hiển thị dialog hỏi kết quả thanh toán
     */
    private void showPaymentResultDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận thanh toán")
            .setMessage("Bạn đã thanh toán thành công chưa?")
            .setPositiveButton("Thành công", (dialog, which) -> {
                // Gọi API cập nhật trạng thái = 1 (thành công)
                simulatePaymentSuccess();
            })
            .setNegativeButton("Chưa thanh toán", (dialog, which) -> {
                // Giữ trạng thái = 0, quay lại màn hình thanh toán
                currentOrderId = null;
                Toast.makeText(this, "Giao dịch chưa hoàn tất", Toast.LENGTH_SHORT).show();
            })
            .setCancelable(false)
            .show();
    }

    /**
     * Giả lập thanh toán thành công (cho Sandbox testing)
     */
    private void simulatePaymentSuccess() {
        if (currentOrderId == null) {
            Toast.makeText(this, "Không có giao dịch để giả lập", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đang giả lập thanh toán...", Toast.LENGTH_SHORT).show();

        ApiService apiService = RetrofitClient.getApiService();
        apiService.simulateMomoSuccess(currentOrderId).enqueue(new Callback<java.util.Map<String, Object>>() {
            @Override
            public void onResponse(Call<java.util.Map<String, Object>> call, Response<java.util.Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = (Boolean) response.body().get("success");
                    if (success != null && success) {
                        // Kiểm tra lại trạng thái
                        checkPaymentStatus();
                    } else {
                        Toast.makeText(Payment.this, "Giả lập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<java.util.Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Simulate error", t);
                Toast.makeText(Payment.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPaymentStatusCheck();
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
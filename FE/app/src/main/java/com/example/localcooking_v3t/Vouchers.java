package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.localcooking_v3t.model.UuDaiDTO;
import com.example.localcooking_v3t.utils.SessionManager;

public class Vouchers extends AppCompatActivity {

    private static final String TAG = "Vouchers";
    public static final String EXTRA_MA_HOC_VIEN = "maHocVien";
    public static final String EXTRA_SO_LUONG_NGUOI = "soLuongNguoi";
    public static final String EXTRA_TONG_TIEN = "tongTien";
    
    // Extra keys cho voucher đã chọn trước đó (để giữ trạng thái khi quay lại)
    public static final String EXTRA_SELECTED_MA_UU_DAI = "selectedMaUuDai";
    public static final String EXTRA_SELECTED_MA_CODE = "selectedMaCode";

    // Result keys
    public static final String RESULT_MA_UU_DAI = "maUuDai";
    public static final String RESULT_MA_CODE = "maCode";
    public static final String RESULT_TEN_UU_DAI = "tenUuDai";
    public static final String RESULT_GIA_TRI_GIAM = "giaTriGiam";
    public static final String RESULT_LOAI_GIAM = "loaiGiam";
    public static final String RESULT_SO_TIEN_GIAM = "soTienGiam";
    public static final String RESULT_VOUCHER_REMOVED = "voucherRemoved"; // Flag để xóa voucher

    private VouchersFragment vouchersFragment;
    private UuDaiDTO selectedUuDai;
    
    // Voucher đã chọn trước đó (từ trang thanh toán)
    private Integer preSelectedMaUuDai;
    private String preSelectedMaCode;
    
    // Views cho frameApDung
    private FrameLayout frameApDung;
    private TextView txtVoucherCount;
    private TextView txtGiamLabel;
    private TextView txtSoTienGiam;
    private Button btnApDung;
    private EditText edtUuDai;
    private Button btnApDung1;
    
    // Dữ liệu
    private double tongTien = 0;
    private double soTienGiam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);
        
        // Set màu status bar
        setStatusBarColor();
        
        // Init views
        initViews();

        // Lấy dữ liệu từ Intent
        Integer maHocVien = null;
        int soLuongNguoi = 1;

        if (getIntent() != null) {
            maHocVien = getIntent().getIntExtra(EXTRA_MA_HOC_VIEN, -1);
            if (maHocVien == -1) {
                SessionManager sessionManager = new SessionManager(this);
                maHocVien = sessionManager.getMaNguoiDung();
            }
            soLuongNguoi = getIntent().getIntExtra(EXTRA_SO_LUONG_NGUOI, 1);
            tongTien = getIntent().getDoubleExtra(EXTRA_TONG_TIEN, 0);
            
            // Lấy thông tin voucher đã chọn trước đó (nếu có)
            preSelectedMaUuDai = getIntent().getIntExtra(EXTRA_SELECTED_MA_UU_DAI, -1);
            if (preSelectedMaUuDai == -1) preSelectedMaUuDai = null;
            preSelectedMaCode = getIntent().getStringExtra(EXTRA_SELECTED_MA_CODE);
        }
        
        Log.d(TAG, "tongTien from Intent: " + tongTien);
        Log.d(TAG, "preSelectedMaUuDai: " + preSelectedMaUuDai);
        Log.d(TAG, "preSelectedMaCode: " + preSelectedMaCode);

        // Xử lý nút quay lại
        ImageView btnBack = findViewById(R.id.imageView6);
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // Xử lý nút áp dụng (bottom)
        btnApDung.setOnClickListener(v -> {
            if (selectedUuDai != null) {
                // Có voucher được chọn -> trả về thông tin voucher
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_MA_UU_DAI, selectedUuDai.getMaUuDai());
                resultIntent.putExtra(RESULT_MA_CODE, selectedUuDai.getMaCode());
                resultIntent.putExtra(RESULT_TEN_UU_DAI, selectedUuDai.getTieuDe());
                resultIntent.putExtra(RESULT_GIA_TRI_GIAM, selectedUuDai.getGiaTriGiam());
                resultIntent.putExtra(RESULT_LOAI_GIAM, selectedUuDai.getLoaiGiam());
                resultIntent.putExtra(RESULT_SO_TIEN_GIAM, soTienGiam);
                resultIntent.putExtra(RESULT_VOUCHER_REMOVED, false);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else if (preSelectedMaUuDai != null || (preSelectedMaCode != null && !preSelectedMaCode.isEmpty())) {
                // Đã có voucher trước đó nhưng user untick -> xóa ưu đãi
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_VOUCHER_REMOVED, true);
                resultIntent.putExtra(RESULT_MA_UU_DAI, -1);
                resultIntent.putExtra(RESULT_SO_TIEN_GIAM, 0.0);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Đã xóa ưu đãi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Chưa có voucher nào trước đó và cũng chưa chọn -> hiện thông báo
                Toast.makeText(this, "Vui lòng chọn 1 ưu đãi để có thể áp dụng", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Xử lý nút áp dụng mã nhập tay (trong card)
        btnApDung1.setOnClickListener(v -> {
            String maCode = edtUuDai.getText().toString().trim();
            if (maCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã ưu đãi", Toast.LENGTH_SHORT).show();
                return;
            }
            // TODO: Gọi API kiểm tra mã ưu đãi
            Toast.makeText(this, "Đang kiểm tra mã: " + maCode, Toast.LENGTH_SHORT).show();
        });

        // Cập nhật UI ban đầu (chưa chọn voucher)
        updateBottomBar(false);

        // Thêm Fragment vào container
        if (savedInstanceState == null) {
            vouchersFragment = VouchersFragment.newInstance(maHocVien, soLuongNguoi, tongTien, preSelectedMaUuDai, preSelectedMaCode);
            vouchersFragment.setOnVoucherSelectedListener(uuDai -> {
                selectedUuDai = uuDai;
                Log.d(TAG, "Selected voucher: " + uuDai.getMaCode() + ", giaTriGiam: " + uuDai.getGiaTriGiam());
                
                // Tính số tiền giảm và cập nhật UI
                calculateDiscount(uuDai);
                updateBottomBar(true);
            });
            
            // Lắng nghe sự kiện bỏ chọn voucher (untick)
            vouchersFragment.setOnVoucherDeselectedListener(() -> {
                selectedUuDai = null;
                soTienGiam = 0;
                Log.d(TAG, "Voucher deselected");
                updateBottomBar(false);
            });

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, vouchersFragment);
            transaction.commit();
        }
    }
    
    private void initViews() {
        frameApDung = findViewById(R.id.frameApDung);
        txtVoucherCount = findViewById(R.id.textView32);
        txtGiamLabel = findViewById(R.id.textView36);
        txtSoTienGiam = findViewById(R.id.textView37);
        btnApDung = findViewById(R.id.btnApDung);
        edtUuDai = findViewById(R.id.edtUuDai);
        btnApDung1 = findViewById(R.id.btnApDung1);
    }
    
    /**
     * Tính số tiền giảm dựa trên loại giảm giá
     */
    private void calculateDiscount(UuDaiDTO uuDai) {
        if (uuDai == null || uuDai.getGiaTriGiam() == null) {
            soTienGiam = 0;
            return;
        }
        
        double giaTriGiam = uuDai.getGiaTriGiam();
        String loaiGiam = uuDai.getLoaiGiam();
        
        if ("PhanTram".equals(loaiGiam)) {
            // Giảm theo phần trăm
            soTienGiam = tongTien * giaTriGiam / 100;
            
            // Áp dụng giảm tối đa nếu có
            if (uuDai.getGiamToiDa() != null && soTienGiam > uuDai.getGiamToiDa()) {
                soTienGiam = uuDai.getGiamToiDa();
            }
        } else {
            // Giảm số tiền cố định
            soTienGiam = giaTriGiam;
        }
        
        // Đảm bảo không giảm quá tổng tiền
        if (soTienGiam > tongTien) {
            soTienGiam = tongTien;
        }
        
        Log.d(TAG, "Calculated discount: " + soTienGiam + " (loaiGiam: " + loaiGiam + ", giaTriGiam: " + giaTriGiam + ")");
    }
    
    /**
     * Cập nhật bottom bar hiển thị thông tin voucher đã chọn
     */
    private void updateBottomBar(boolean hasSelection) {
        if (hasSelection && selectedUuDai != null) {
            // Hiển thị thông tin voucher đã chọn
            txtVoucherCount.setText("1 Voucher đã được chọn");
            txtGiamLabel.setVisibility(View.VISIBLE);
            txtSoTienGiam.setVisibility(View.VISIBLE);
            txtSoTienGiam.setText("-" + formatCurrency(soTienGiam));
        } else {
            // Chưa chọn voucher
            txtVoucherCount.setText("Chưa chọn voucher");
            txtGiamLabel.setVisibility(View.GONE);
            txtSoTienGiam.setVisibility(View.GONE);
        }
        // Nút áp dụng luôn enable, khi nhấn mà chưa chọn sẽ hiện thông báo
    }
    
    /**
     * Format số tiền với dấu chấm phân cách
     */
    private String formatCurrency(double amount) {
        return String.format("%,.0f₫", amount).replace(",", ".");
    }
    
    private void setStatusBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(0xFFF5E6D3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
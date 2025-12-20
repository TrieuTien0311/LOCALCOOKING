package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.localcooking_v3t.model.UuDaiDTO;
import com.example.localcooking_v3t.utils.SessionManager;

public class Vouchers extends AppCompatActivity {

    private static final String TAG = "Vouchers";
    public static final String EXTRA_MA_HOC_VIEN = "maHocVien";
    public static final String EXTRA_SO_LUONG_NGUOI = "soLuongNguoi";
    public static final String EXTRA_TONG_TIEN = "tongTien";

    // Result keys
    public static final String RESULT_MA_UU_DAI = "maUuDai";
    public static final String RESULT_MA_CODE = "maCode";
    public static final String RESULT_TEN_UU_DAI = "tenUuDai";
    public static final String RESULT_GIA_TRI_GIAM = "giaTriGiam";
    public static final String RESULT_LOAI_GIAM = "loaiGiam";

    private VouchersFragment vouchersFragment;
    private UuDaiDTO selectedUuDai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vouchers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy dữ liệu từ Intent
        Integer maHocVien = null;
        int soLuongNguoi = 1;
        double tongTien = 0;

        if (getIntent() != null) {
            maHocVien = getIntent().getIntExtra(EXTRA_MA_HOC_VIEN, -1);
            if (maHocVien == -1) {
                SessionManager sessionManager = new SessionManager(this);
                maHocVien = sessionManager.getMaNguoiDung();
            }
            soLuongNguoi = getIntent().getIntExtra(EXTRA_SO_LUONG_NGUOI, 1);
            tongTien = getIntent().getDoubleExtra(EXTRA_TONG_TIEN, 0);
        }

        // Xử lý nút quay lại
        ImageView btnBack = findViewById(R.id.imageView6);
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // Xử lý nút áp dụng
        Button btnApDung = findViewById(R.id.btnApDung);
        btnApDung.setOnClickListener(v -> {
            if (selectedUuDai != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_MA_UU_DAI, selectedUuDai.getMaUuDai());
                resultIntent.putExtra(RESULT_MA_CODE, selectedUuDai.getMaCode());
                resultIntent.putExtra(RESULT_TEN_UU_DAI, selectedUuDai.getTieuDe());
                resultIntent.putExtra(RESULT_GIA_TRI_GIAM, selectedUuDai.getGiaTriGiam());
                resultIntent.putExtra(RESULT_LOAI_GIAM, selectedUuDai.getLoaiGiam());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng chọn một mã ưu đãi", Toast.LENGTH_SHORT).show();
            }
        });

        // Thêm Fragment vào container
        if (savedInstanceState == null) {
            vouchersFragment = VouchersFragment.newInstance(maHocVien, soLuongNguoi, tongTien);
            vouchersFragment.setOnVoucherSelectedListener(uuDai -> {
                selectedUuDai = uuDai;
                Log.d(TAG, "Selected voucher: " + uuDai.getMaCode());
            });

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, vouchersFragment);
            transaction.commit();
        }
    }
}
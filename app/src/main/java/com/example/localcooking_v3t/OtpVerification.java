package com.example.localcooking_v3t;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class OtpVerification extends AppCompatActivity {

    private ImageView btnBack, ivLogo;
    private TextView tvBack, tvAppName;
    private EditText otpBox1, otpBox2, otpBox3, otpBox4, otpBox5, otpBox6;
    private TextInputLayout tilMatKhauMoi, tilXacNhanMatKhau;
    private TextInputEditText idMatKhauMoi, idXacNhanMatKhau;
    private Button btnXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupStatusBar();

        // Ánh xạ View
        initViews();

        // Thiết lập Auto nhảy ô
        setupOTPInputs();

        setupListeners();
    }

    private void setupStatusBar() {
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#F5E6D3"));

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        ivLogo = findViewById(R.id.ivLogo);
        tvAppName = findViewById(R.id.tvAppName);
        otpBox1 = findViewById(R.id.otpBox1);
        otpBox2 = findViewById(R.id.otpBox2);
        otpBox3 = findViewById(R.id.otpBox3);
        otpBox4 = findViewById(R.id.otpBox4);
        otpBox5 = findViewById(R.id.otpBox5);
        otpBox6 = findViewById(R.id.otpBox6);
        tilMatKhauMoi = findViewById(R.id.tilMatKhauMoi);
        idMatKhauMoi = findViewById(R.id.idMatKhauMoi);
        tilXacNhanMatKhau = findViewById(R.id.tilXacNhanMatKhau);
        idXacNhanMatKhau = findViewById(R.id.idXacNhanMatKhau);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }

    private void setupListeners() {
        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpVerification.this, Login.class);
                startActivity(intent);
            }
        });
    }

    // Hàm hỗ trợ lấy chuỗi OTP từ 6 ô
    private String getOTPCode() {
        return otpBox1.getText().toString() +
                otpBox2.getText().toString() +
                otpBox3.getText().toString() +
                otpBox4.getText().toString() +
                otpBox5.getText().toString() +
                otpBox6.getText().toString();
    }

    // Hàm xử lý logic tự động chuyển ô khi nhập số
    private void setupOTPInputs() {
        otpBox1.addTextChangedListener(new GenericTextWatcher(otpBox1, otpBox2));
        otpBox2.addTextChangedListener(new GenericTextWatcher(otpBox2, otpBox3));
        otpBox3.addTextChangedListener(new GenericTextWatcher(otpBox3, otpBox4));
        otpBox4.addTextChangedListener(new GenericTextWatcher(otpBox4, otpBox5));
        otpBox5.addTextChangedListener(new GenericTextWatcher(otpBox5, otpBox6));
        otpBox6.addTextChangedListener(new GenericTextWatcher(otpBox6, null));
    }

    // Class hỗ trợ theo dõi nhập liệu để chuyển focus
    private class GenericTextWatcher implements TextWatcher {
        private final View currentView;
        private final View nextView;

        public GenericTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (text.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }
    }
}
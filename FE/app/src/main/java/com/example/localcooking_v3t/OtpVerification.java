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

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ResetPasswordRequest;
import com.example.localcooking_v3t.model.ResetPasswordResponse;
import com.example.localcooking_v3t.model.VerifyOtpRequest;
import com.example.localcooking_v3t.model.VerifyOtpResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerification extends AppCompatActivity {

    private ImageView btnBack, ivLogo;
    private TextView tvBack, tvAppName;
    private EditText otpBox1, otpBox2, otpBox3, otpBox4, otpBox5, otpBox6;
    private TextInputLayout tilMatKhauMoi, tilXacNhanMatKhau;
    private TextInputEditText idMatKhauMoi, idXacNhanMatKhau;
    private Button btnXacNhan;

    private String email;
    private String resetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);

        // Lấy email từ Intent
        email = getIntent().getStringExtra("email");

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupStatusBar();
        initViews();
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

        btnXacNhan.setOnClickListener(v -> {
            if (resetToken == null) {
                // Chưa xác thực OTP -> Xác thực OTP trước
                verifyOtp();
            } else {
                // Đã có resetToken -> Đặt mật khẩu mới
                resetPassword();
            }
        });
    }

    private void verifyOtp() {
        String otp = getOTPCode();

        if (otp.length() != 6) {
            Toast.makeText(this, "Vui lòng nhập đủ 6 số OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        btnXacNhan.setEnabled(false);
        btnXacNhan.setText("Đang xác thực...");

        VerifyOtpRequest request = new VerifyOtpRequest(email, otp);
        RetrofitClient.getApiService().verifyResetOtp(request).enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");

                if (response.isSuccessful() && response.body() != null) {
                    resetToken = response.body().getResetToken();
                    Toast.makeText(OtpVerification.this, "OTP hợp lệ! Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                    // Hiển thị phần nhập mật khẩu mới
                    tilMatKhauMoi.setVisibility(View.VISIBLE);
                    tilXacNhanMatKhau.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(OtpVerification.this, "OTP không đúng hoặc đã hết hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");
                Toast.makeText(OtpVerification.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword() {
        String newPassword = idMatKhauMoi.getText().toString().trim();
        String confirmPassword = idXacNhanMatKhau.getText().toString().trim();

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        btnXacNhan.setEnabled(false);
        btnXacNhan.setText("Đang xử lý...");

        ResetPasswordRequest request = new ResetPasswordRequest(resetToken, newPassword);
        RetrofitClient.getApiService().resetPassword(request).enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(OtpVerification.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    // Chuyển về màn hình đăng nhập
                    Intent intent = new Intent(OtpVerification.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(OtpVerification.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");
                Toast.makeText(OtpVerification.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getOTPCode() {
        return otpBox1.getText().toString() +
                otpBox2.getText().toString() +
                otpBox3.getText().toString() +
                otpBox4.getText().toString() +
                otpBox5.getText().toString() +
                otpBox6.getText().toString();
    }

    private void setupOTPInputs() {
        otpBox1.addTextChangedListener(new GenericTextWatcher(otpBox1, otpBox2));
        otpBox2.addTextChangedListener(new GenericTextWatcher(otpBox2, otpBox3));
        otpBox3.addTextChangedListener(new GenericTextWatcher(otpBox3, otpBox4));
        otpBox4.addTextChangedListener(new GenericTextWatcher(otpBox4, otpBox5));
        otpBox5.addTextChangedListener(new GenericTextWatcher(otpBox5, otpBox6));
        otpBox6.addTextChangedListener(new GenericTextWatcher(otpBox6, null));
    }

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
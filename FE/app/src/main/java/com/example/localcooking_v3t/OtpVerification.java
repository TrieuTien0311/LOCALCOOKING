package com.example.localcooking_v3t;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
    private View mainLayout;

    private String email;
    private String resetToken;
    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;

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
        setupClearFocusOnTouch();
        setupOTPInputs();
        setupPasswordToggles();
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
        mainLayout = findViewById(R.id.main);
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
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clearFocusFromEditTexts();
                }
                return false;
            });
        }

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
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
        if (mainLayout != null) {
            mainLayout.requestFocus();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = event.getRawX() + v.getLeft() - location[0];
                float y = event.getRawY() + v.getTop() - location[1];

                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    clearFocusFromEditTexts();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void setupPasswordToggles() {
        // Toggle cho Mật khẩu mới
        idMatKhauMoi.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idMatKhauMoi.getRight() - idMatKhauMoi.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible1) {
                        idMatKhauMoi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idMatKhauMoi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible1 = false;
                    } else {
                        idMatKhauMoi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idMatKhauMoi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible1 = true;
                    }
                    idMatKhauMoi.setSelection(idMatKhauMoi.getText().length());
                    return true;
                }
            }
            return false;
        });
        
        // Toggle cho Xác nhận mật khẩu
        idXacNhanMatKhau.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idXacNhanMatKhau.getRight() - idXacNhanMatKhau.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible2) {
                        idXacNhanMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idXacNhanMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible2 = false;
                    } else {
                        idXacNhanMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idXacNhanMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible2 = true;
                    }
                    idXacNhanMatKhau.setSelection(idXacNhanMatKhau.getText().length());
                    return true;
                }
            }
            return false;
        });
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
        EditText[] otpBoxes = {otpBox1, otpBox2, otpBox3, otpBox4, otpBox5, otpBox6};
        
        for (int i = 0; i < otpBoxes.length; i++) {
            final int index = i;
            otpBoxes[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Khi nhập 1 ký tự, chuyển sang ô tiếp theo
                    if (s.length() == 1 && index < otpBoxes.length - 1) {
                        otpBoxes[index + 1].requestFocus();
                    } 
                    // Khi xóa (backspace), quay về ô trước đó
                    else if (s.length() == 0 && index > 0) {
                        otpBoxes[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }
}
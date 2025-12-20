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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ForgotPasswordRequest;
import com.example.localcooking_v3t.model.ForgotPasswordResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvBackToLogin;
    private TextInputEditText idEmail;
    private Button btnGuiLienKet;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        mainLayout = findViewById(R.id.main);
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        idEmail = findViewById(R.id.idEmail);
        btnGuiLienKet = findViewById(R.id.btnGuiLienKet);

        // Thiết lập clear focus khi chạm ra ngoài
        setupClearFocusOnTouch();

        // Xử lý sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());
        tvBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện Quay lại trang đăng nhập
        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPassword.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nút Gửi OTP
        btnGuiLienKet.setOnClickListener(v -> sendForgotPasswordOtp());
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

    private void sendForgotPasswordOtp() {
        String email = idEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button khi đang gọi API
        btnGuiLienKet.setEnabled(false);
        btnGuiLienKet.setText("Đang gửi...");

        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        RetrofitClient.getApiService().forgotPassword(request).enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                btnGuiLienKet.setEnabled(true);
                btnGuiLienKet.setText("Gửi liên kết");

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình OTP, truyền email
                    Intent intent = new Intent(ForgotPassword.this, OtpVerification.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgotPassword.this, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                btnGuiLienKet.setEnabled(true);
                btnGuiLienKet.setText("Gửi liên kết");
                Toast.makeText(ForgotPassword.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
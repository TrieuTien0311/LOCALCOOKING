package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        idEmail = findViewById(R.id.idEmail);
        btnGuiLienKet = findViewById(R.id.btnGuiLienKet);

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
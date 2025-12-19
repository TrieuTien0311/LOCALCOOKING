package com.example.localcooking_v3t;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
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

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ChangePasswordRequest;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvQuenMatKhau;
    private TextInputEditText idEmail, idMatKhauHienTai, idMatKhauMoi, idXacNhanMatKhau;
    private Button btnGuiMaXacNhan;
    private ApiService apiService;
    
    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;
    private boolean isPasswordVisible3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
        idEmail = findViewById(R.id.idEmail);
        idMatKhauHienTai = findViewById(R.id.idMatKhauHienTai);
        idMatKhauMoi = findViewById(R.id.idMatKhauMoi);
        idXacNhanMatKhau = findViewById(R.id.idXacNhanMatKhau);
        btnGuiMaXacNhan = findViewById(R.id.btnGuiMaXacNhan);
        
        // Khởi tạo API service
        apiService = RetrofitClient.getClient().create(ApiService.class);
        
        // Lấy email từ SharedPreferences (nếu đã đăng nhập)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedEmail = prefs.getString("email", "");
        if (!savedEmail.isEmpty()) {
            idEmail.setText(savedEmail);
        }

        // Xử lý sự kiện Quên mật khẩu
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Thiết lập toggle password visibility
        setupPasswordToggles();
        
        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());
        tvBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Gửi mã xác nhận
        btnGuiMaXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = idEmail.getText().toString().trim();
                String currentPassword = idMatKhauHienTai.getText().toString().trim();
                String newPassword = idMatKhauMoi.getText().toString().trim();
                String confirmPassword = idXacNhanMatKhau.getText().toString().trim();

                // Validate input
                if (email.isEmpty()) {
                    Toast.makeText(ChangePassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ChangePassword.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi API gửi OTP
                sendOtpForChangePassword(email, currentPassword, newPassword, confirmPassword);
            }
        });
    }
    
    /**
     * Thiết lập toggle hiển thị/ẩn mật khẩu cho 3 ô input
     */
    private void setupPasswordToggles() {
        // Toggle cho Mật khẩu hiện tại
        idMatKhauHienTai.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idMatKhauHienTai.getRight() - idMatKhauHienTai.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible1) {
                        idMatKhauHienTai.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idMatKhauHienTai.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible1 = false;
                    } else {
                        idMatKhauHienTai.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idMatKhauHienTai.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible1 = true;
                    }
                    idMatKhauHienTai.setSelection(idMatKhauHienTai.getText().length());
                    return true;
                }
            }
            return false;
        });
        
        // Toggle cho Mật khẩu mới
        idMatKhauMoi.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idMatKhauMoi.getRight() - idMatKhauMoi.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible2) {
                        idMatKhauMoi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idMatKhauMoi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible2 = false;
                    } else {
                        idMatKhauMoi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idMatKhauMoi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible2 = true;
                    }
                    idMatKhauMoi.setSelection(idMatKhauMoi.getText().length());
                    return true;
                }
            }
            return false;
        });
        
        // Toggle cho Xác nhận mật khẩu mới
        idXacNhanMatKhau.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idXacNhanMatKhau.getRight() - idXacNhanMatKhau.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible3) {
                        idXacNhanMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idXacNhanMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible3 = false;
                    } else {
                        idXacNhanMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idXacNhanMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible3 = true;
                    }
                    idXacNhanMatKhau.setSelection(idXacNhanMatKhau.getText().length());
                    return true;
                }
            }
            return false;
        });
    }
    
    private void sendOtpForChangePassword(String email, String currentPassword, String newPassword, String confirmPassword) {
        // Disable button để tránh spam
        btnGuiMaXacNhan.setEnabled(false);
        btnGuiMaXacNhan.setText("Đang gửi...");
        
        ChangePasswordRequest request = new ChangePasswordRequest(email, currentPassword, newPassword, confirmPassword);
        
        // Debug log - Serialize to JSON to see what's being sent
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String jsonRequest = gson.toJson(request);
        
        android.util.Log.d("CHANGE_PASSWORD", "=== REQUEST DEBUG ===");
        android.util.Log.d("CHANGE_PASSWORD", "Email: " + email);
        android.util.Log.d("CHANGE_PASSWORD", "Current Password: " + currentPassword);
        android.util.Log.d("CHANGE_PASSWORD", "New Password: " + newPassword);
        android.util.Log.d("CHANGE_PASSWORD", "Confirm Password: " + confirmPassword);
        android.util.Log.d("CHANGE_PASSWORD", "JSON Request: " + jsonRequest);
        android.util.Log.d("CHANGE_PASSWORD", "API URL: " + RetrofitClient.getBaseUrl() + "nguoidung/change-password/send-otp");
        
        Call<ChangePasswordResponse> call = apiService.sendOtpForChangePassword(request);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                btnGuiMaXacNhan.setEnabled(true);
                btnGuiMaXacNhan.setText("Gửi mã xác nhận");
                
                // Debug log
                android.util.Log.d("CHANGE_PASSWORD", "Response code: " + response.code());
                android.util.Log.d("CHANGE_PASSWORD", "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    ChangePasswordResponse result = response.body();
                    android.util.Log.d("CHANGE_PASSWORD", "Success: " + result.isSuccess());
                    android.util.Log.d("CHANGE_PASSWORD", "Message: " + result.getMessage());
                    
                    if (result.isSuccess()) {
                        Toast.makeText(ChangePassword.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        
                        // Chuyển sang trang OTP để xác thực
                        Intent intent = new Intent(ChangePassword.this, ChangePasswordOtp.class);
                        intent.putExtra("email", email);
                        intent.putExtra("matKhauHienTai", currentPassword);
                        intent.putExtra("matKhauMoi", newPassword);
                        intent.putExtra("xacNhanMatKhauMoi", confirmPassword);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChangePassword.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Response không successful hoặc body null
                    android.util.Log.e("CHANGE_PASSWORD", "Response code: " + response.code());
                    
                    // Thử parse error body as ChangePasswordResponse
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            android.util.Log.e("CHANGE_PASSWORD", "Error body: " + errorBodyString);
                            
                            // Parse JSON error response
                            try {
                                com.google.gson.Gson gson = new com.google.gson.Gson();
                                ChangePasswordResponse errorResponse = gson.fromJson(errorBodyString, ChangePasswordResponse.class);
                                
                                if (errorResponse != null && errorResponse.getMessage() != null) {
                                    Toast.makeText(ChangePassword.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ChangePassword.this, "Lỗi " + response.code() + ": " + errorBodyString, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception parseError) {
                                Toast.makeText(ChangePassword.this, "Lỗi " + response.code() + ": " + errorBodyString, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ChangePassword.this, "Lỗi " + response.code() + ": " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        android.util.Log.e("CHANGE_PASSWORD", "Error reading error body", e);
                        Toast.makeText(ChangePassword.this, "Lỗi " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                btnGuiMaXacNhan.setEnabled(true);
                btnGuiMaXacNhan.setText("Gửi mã xác nhận");
                
                // Debug log
                android.util.Log.e("CHANGE_PASSWORD", "onFailure: " + t.getMessage(), t);
                
                String errorMsg = "Lỗi kết nối: " + t.getMessage();
                Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
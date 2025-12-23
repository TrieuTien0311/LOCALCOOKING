package com.example.localcooking_v3t;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    private View mainLayout;
    
    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;
    private boolean isPasswordVisible3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        
        // Set màu status bar giống với background (#F5E6D3)
        setStatusBarColor();

        // Ánh xạ view
        mainLayout = findViewById(R.id.main);
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

        // Thiết lập clear focus khi chạm ra ngoài
        setupClearFocusOnTouch();

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
                        Toast.makeText(ChangePassword.this, "✓ " + result.getMessage(), Toast.LENGTH_LONG).show();
                        
                        // Chuyển sang trang OTP để xác thực
                        Intent intent = new Intent(ChangePassword.this, ChangePasswordOtp.class);
                        intent.putExtra("email", email);
                        intent.putExtra("matKhauHienTai", currentPassword);
                        intent.putExtra("matKhauMoi", newPassword);
                        intent.putExtra("xacNhanMatKhauMoi", confirmPassword);
                        startActivity(intent);
                    } else {
                        // Hiển thị thông báo lỗi từ server
                        String errorMsg = "✗ Lỗi: " + result.getMessage();
                        Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
                        android.util.Log.e("CHANGE_PASSWORD", errorMsg);
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
                                    String errorMsg = "✗ Lỗi: " + errorResponse.getMessage();
                                    Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
                                } else {
                                    String errorMsg = "✗ Lỗi " + response.code() + ": " + errorBodyString;
                                    Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception parseError) {
                                String errorMsg = "✗ Lỗi " + response.code() + ": " + errorBodyString;
                                Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String errorMsg = "✗ Lỗi " + response.code() + ": " + response.message();
                            Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        android.util.Log.e("CHANGE_PASSWORD", "Error reading error body", e);
                        String errorMsg = "✗ Lỗi không xác định (Code: " + response.code() + ")";
                        Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                btnGuiMaXacNhan.setEnabled(true);
                btnGuiMaXacNhan.setText("Gửi mã xác nhận");
                
                // Debug log
                android.util.Log.e("CHANGE_PASSWORD", "onFailure: " + t.getMessage(), t);
                
                // Phân loại lỗi kết nối
                String errorMsg;
                if (t instanceof java.net.UnknownHostException) {
                    errorMsg = "Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    errorMsg = "Lỗi kết nối: Timeout. Server không phản hồi.";
                } else if (t instanceof java.net.ConnectException) {
                    errorMsg = "Lỗi kết nối: Không thể kết nối đến server. Vui lòng kiểm tra backend đã chạy chưa.";
                } else {
                    errorMsg = "Lỗi kết nối: " + t.getMessage();
                }
                
                Toast.makeText(ChangePassword.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Set màu status bar để đồng bộ với background
     */
    private void setStatusBarColor() {
        Window window = getWindow();
        // Màu #F5E6D3 (màu background của màn hình)
        window.setStatusBarColor(0xFFF5E6D3);
        
        // Đặt icon status bar màu tối (vì background sáng)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
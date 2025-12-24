package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ChangePasswordResponse;
import com.example.localcooking_v3t.model.ChangePasswordWithOtpRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordOtp extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack;
    private EditText otpBox1, otpBox2, otpBox3, otpBox4, otpBox5, otpBox6;
    private Button btnXacNhan;
    private ApiService apiService;
    private View mainLayout;
    
    private String email, matKhauHienTai, matKhauMoi, xacNhanMatKhauMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_otp);
        
        // Set màu status bar
        setStatusBarColor();

        initViews();
        setupClearFocusOnTouch();
        setupOtpInputs();
        setupClickListeners();
    }

    private void initViews() {
        mainLayout = findViewById(R.id.main);
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        otpBox1 = findViewById(R.id.otpBox1);
        otpBox2 = findViewById(R.id.otpBox2);
        otpBox3 = findViewById(R.id.otpBox3);
        otpBox4 = findViewById(R.id.otpBox4);
        otpBox5 = findViewById(R.id.otpBox5);
        otpBox6 = findViewById(R.id.otpBox6);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        
        // Khởi tạo API service
        apiService = RetrofitClient.getClient().create(ApiService.class);
        
        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        matKhauHienTai = intent.getStringExtra("matKhauHienTai");
        matKhauMoi = intent.getStringExtra("matKhauMoi");
        xacNhanMatKhauMoi = intent.getStringExtra("xacNhanMatKhauMoi");
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

    private void setupOtpInputs() {
        EditText[] otpBoxes = {otpBox1, otpBox2, otpBox3, otpBox4, otpBox5, otpBox6};
        
        for (int i = 0; i < otpBoxes.length; i++) {
            final int index = i;
            otpBoxes[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpBoxes.length - 1) {
                        otpBoxes[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otpBoxes[index - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupClickListeners() {
        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());
        tvBack.setOnClickListener(v -> finish());

        // Xử lý nút xác nhận
        btnXacNhan.setOnClickListener(v -> {
            String otp = getOtpCode();
            if (otp.length() != 6) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API xác thực OTP và đổi mật khẩu
            verifyOtpAndChangePassword(otp);
        });
    }

    private String getOtpCode() {
        return otpBox1.getText().toString() + 
               otpBox2.getText().toString() + 
               otpBox3.getText().toString() + 
               otpBox4.getText().toString() + 
               otpBox5.getText().toString() + 
               otpBox6.getText().toString();
    }
    
    private void verifyOtpAndChangePassword(String otp) {
        // Disable button để tránh spam
        btnXacNhan.setEnabled(false);
        btnXacNhan.setText("Đang xác thực...");
        
        ChangePasswordWithOtpRequest request = new ChangePasswordWithOtpRequest(
            email, matKhauHienTai, matKhauMoi, xacNhanMatKhauMoi, otp
        );
        
        // Debug log
        android.util.Log.d("CHANGE_PASSWORD_OTP", "=== REQUEST DEBUG ===");
        android.util.Log.d("CHANGE_PASSWORD_OTP", "Email: " + email);
        android.util.Log.d("CHANGE_PASSWORD_OTP", "OTP: " + otp);
        android.util.Log.d("CHANGE_PASSWORD_OTP", "API URL: " + RetrofitClient.getBaseUrl() + "api/nguoidung/change-password/verify");
        
        Call<ChangePasswordResponse> call = apiService.changePasswordWithOtp(request);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");
                
                // Debug log
                android.util.Log.d("CHANGE_PASSWORD_OTP", "Response code: " + response.code());
                android.util.Log.d("CHANGE_PASSWORD_OTP", "Response message: " + response.message());
                
                if (response.isSuccessful() && response.body() != null) {
                    ChangePasswordResponse result = response.body();
                    android.util.Log.d("CHANGE_PASSWORD_OTP", "Success: " + result.isSuccess());
                    android.util.Log.d("CHANGE_PASSWORD_OTP", "Message: " + result.getMessage());
                    
                    if (result.isSuccess()) {
                        Toast.makeText(ChangePasswordOtp.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
                        
                        // Chuyển về trang đăng nhập
                        Intent intent = new Intent(ChangePasswordOtp.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Hiển thị thông báo lỗi từ server
                        String errorMsg = "Lỗi: " + result.getMessage();
                        Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
                        android.util.Log.e("CHANGE_PASSWORD_OTP", errorMsg);
                    }
                } else {
                    // Response không successful hoặc body null
                    android.util.Log.e("CHANGE_PASSWORD_OTP", "Response code: " + response.code());
                    
                    // Thử parse error body
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyString = response.errorBody().string();
                            android.util.Log.e("CHANGE_PASSWORD_OTP", "Error body: " + errorBodyString);
                            
                            // Parse JSON error response
                            try {
                                com.google.gson.Gson gson = new com.google.gson.Gson();
                                ChangePasswordResponse errorResponse = gson.fromJson(errorBodyString, ChangePasswordResponse.class);
                                
                                if (errorResponse != null && errorResponse.getMessage() != null) {
                                    String errorMsg = "Lỗi xác thực: " + errorResponse.getMessage();
                                    Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
                                } else {
                                    String errorMsg = "Lỗi " + response.code() + ": " + errorBodyString;
                                    Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception parseError) {
                                String errorMsg = "Lỗi " + response.code() + ": " + errorBodyString;
                                Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String errorMsg = "Lỗi " + response.code() + ": " + response.message();
                            Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        android.util.Log.e("CHANGE_PASSWORD_OTP", "Error reading error body", e);
                        String errorMsg = "Lỗi không xác định (Code: " + response.code() + ")";
                        Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");
                
                // Debug log
                android.util.Log.e("CHANGE_PASSWORD_OTP", "onFailure: " + t.getMessage(), t);
                
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
                
                Toast.makeText(ChangePasswordOtp.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setStatusBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(0xFFF5E6D3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
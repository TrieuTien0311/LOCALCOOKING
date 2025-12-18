package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    
    private String email, matKhauHienTai, matKhauMoi, xacNhanMatKhauMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupOtpInputs();
        setupClickListeners();
    }

    private void initViews() {
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
        
        Call<ChangePasswordResponse> call = apiService.changePasswordWithOtp(request);
        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");
                
                if (response.isSuccessful() && response.body() != null) {
                    ChangePasswordResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(ChangePasswordOtp.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
                        
                        // Chuyển về trang đăng nhập
                        Intent intent = new Intent(ChangePasswordOtp.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordOtp.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordOtp.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận");
                Toast.makeText(ChangePasswordOtp.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
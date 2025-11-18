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

import com.google.android.material.textfield.TextInputEditText;

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

        // Xử lý sự kiện nút Back (icon)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý sự kiện nút Back (text)
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý sự kiện Quay lại trang đăng nhập
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện nút Gửi liên kết
        btnGuiLienKet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = idEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPassword.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý gửi email reset password ở đây
                    Toast.makeText(ForgotPassword.this, "Đã gửi liên kết đặt lại mật khẩu đến " + email, Toast.LENGTH_LONG).show();

                    // Chuyển về trang Login sau 2 giây
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ForgotPassword.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }
            }
        });
    }
}
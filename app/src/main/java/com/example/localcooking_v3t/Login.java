package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvForgotPassword, tvRegister;
    private TextInputEditText idEmail, idMatKhau;
    private CheckBox cbRememberMe;
    private Button btnDangNhap;
    private MaterialButton btnGG, btnFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        idEmail = findViewById(R.id.idEmail);
        idMatKhau = findViewById(R.id.idMatKhau);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnGG = findViewById(R.id.btnGG);
        btnFB = findViewById(R.id.btnFB);

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

        // Xử lý sự kiện Quên mật khẩu - CHUYỂN ĐÊN TRANG FORGOT PASSWORD
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện chuyển sang trang Đăng ký
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nút Đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = idEmail.getText().toString().trim();
                String password = idMatKhau.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý đăng nhập ở đây
                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý đăng nhập bằng Google
        btnGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Đăng nhập bằng Google", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý đăng nhập bằng Facebook
        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Đăng nhập bằng Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
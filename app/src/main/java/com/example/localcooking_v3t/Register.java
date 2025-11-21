package com.example.localcooking_v3t;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    // Khai báo các biến UI
    private ImageView btnBack, ivLogo;
    private TextView tvBack, tvAppName, tvLogin;
    private TextInputLayout tilTaiKhoan, tilEmail, tilMatKhau;
    private TextInputEditText idTaiKhoan, idEmail, idMatKhau;
    private CheckBox cbTerms;
    private Button btnTaoTaiKhoan;
    private MaterialButton btnGG, btnFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View
        initViews();

        // Thiết lập sự kiện
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        ivLogo = findViewById(R.id.ivLogo);
        tvAppName = findViewById(R.id.tvAppName);
        tilTaiKhoan = findViewById(R.id.tilTaiKhoan);
        idTaiKhoan = findViewById(R.id.idTaiKhoan);
        tilEmail = findViewById(R.id.tilEmail);
        idEmail = findViewById(R.id.idEmail);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        idMatKhau = findViewById(R.id.idMatKhau);
        cbTerms = findViewById(R.id.cbTerms);
        btnTaoTaiKhoan = findViewById(R.id.btnTaoTaiKhoan);
        btnGG = findViewById(R.id.btnGG);
        btnFB = findViewById(R.id.btnFB);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void setupListeners() {
        // Sự kiện nút Quay lại
        View.OnClickListener backListener = v -> finish();
        btnBack.setOnClickListener(backListener);
        tvBack.setOnClickListener(backListener);

        // Sự kiện nút Tạo tài khoản
        btnTaoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // Sự kiện chuyển sang màn hình Đăng nhập (Nếu đã có tài khoản)
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });
    }
}
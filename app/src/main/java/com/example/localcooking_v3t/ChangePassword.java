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

public class ChangePassword extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvQuenMatKhau;
    private TextInputEditText idMatKhauHienTai, idMatKhauMoi, idXacNhanMatKhau;
    private Button btnGuiMaXacNhan;

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
        idMatKhauHienTai = findViewById(R.id.idMatKhauHienTai);
        idMatKhauMoi = findViewById(R.id.idMatKhauMoi);
        idXacNhanMatKhau = findViewById(R.id.idXacNhanMatKhau);
        btnGuiMaXacNhan = findViewById(R.id.btnGuiMaXacNhan);

        // Xử lý sự kiện Quên mật khẩu
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nút Gửi mã xác nhận
        btnGuiMaXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = idMatKhauHienTai.getText().toString().trim();
                String newPassword = idMatKhauMoi.getText().toString().trim();
                String confirmPassword = idXacNhanMatKhau.getText().toString().trim();

                // Xử lý đổi mật khẩu ở đây
                Toast.makeText(ChangePassword.this, "Đã gửi mã xác nhận đến email của bạn", Toast.LENGTH_SHORT).show();

                // Chuyển sang trang OTP để xác thực
                Intent intent = new Intent(ChangePassword.this, ChangePasswordOtp.class);
                intent.putExtra("newPassword", newPassword);
                intent.putExtra("confirmPassword", confirmPassword);
                startActivity(intent);
                finish();
            }
        });
    }
}
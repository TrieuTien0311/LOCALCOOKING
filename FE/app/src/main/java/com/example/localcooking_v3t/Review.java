package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Review extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnGuiDanhGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.imageView6);
        btnGuiDanhGia = findViewById(R.id.button);
    }

    private void setupClickListeners() {
        // Xử lý nút quay lại - về trang chủ
        btnBack.setOnClickListener(v -> {
            navigateToHome();
        });

        // Xử lý nút gửi đánh giá - về trang chủ
        btnGuiDanhGia.setOnClickListener(v -> {
            Toast.makeText(this, "Cảm ơn bạn đã đánh giá! ⭐", Toast.LENGTH_SHORT).show();
            navigateToHome();
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(Review.this, Header.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
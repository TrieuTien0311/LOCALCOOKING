package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Review extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnGuiDanhGia;
    
    // Views để hiển thị thông tin đơn hàng
    private ImageView imgFood;
    private TextView txtTenKhoaHoc;
    private TextView txtThoiGian;
    private TextView txtDiaDiem;
    
    // Dữ liệu nhận từ Intent
    private String orderTitle;
    private int orderImage;
    private Integer maDatLich;
    private Integer maKhoaHoc;
    private String lich;
    private String diaDiem;

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
        getIntentData();
        displayData();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.imageView6);
        btnGuiDanhGia = findViewById(R.id.button);
        
        // Views hiển thị thông tin đơn hàng
        imgFood = findViewById(R.id.imgFood4);
        txtTenKhoaHoc = findViewById(R.id.textView18);
        txtThoiGian = findViewById(R.id.textView14);
        txtDiaDiem = findViewById(R.id.textView9);
    }
    
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            orderTitle = intent.getStringExtra("orderTitle");
            orderImage = intent.getIntExtra("orderImage", R.drawable.hue);
            maDatLich = intent.getIntExtra("maDatLich", -1);
            maKhoaHoc = intent.getIntExtra("maKhoaHoc", -1);
            lich = intent.getStringExtra("lich");
            diaDiem = intent.getStringExtra("diaDiem");
        }
    }
    
    private void displayData() {
        // Hiển thị hình ảnh
        if (orderImage != 0) {
            imgFood.setImageResource(orderImage);
        }
        
        // Hiển thị tên khóa học
        if (orderTitle != null && !orderTitle.isEmpty()) {
            txtTenKhoaHoc.setText(orderTitle);
        }
        
        // Hiển thị thời gian
        if (lich != null && !lich.isEmpty()) {
            txtThoiGian.setText(lich);
        }
        
        // Hiển thị địa điểm
        if (diaDiem != null && !diaDiem.isEmpty()) {
            txtDiaDiem.setText("Địa điểm: " + diaDiem);
        }
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
package com.example.localcooking_v3t;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class HelpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        
        // Thay đổi màu status bar
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#FFC59D"));
        
        // Đặt icon status bar thành màu tối (để dễ nhìn trên nền cam)
        WindowInsetsControllerCompat windowInsetsController = 
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }

        // Tìm các view
        ImageView imgBack = findViewById(R.id.imgBack);


        // Xử lý sự kiện click cho nút back (icon)
        imgBack.setOnClickListener(v -> {
            finish(); // Quay lại màn hình trước đó
        });


    }
}
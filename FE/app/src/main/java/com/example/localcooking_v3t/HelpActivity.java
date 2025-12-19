package com.example.localcooking_v3t;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HelpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Tìm các view
        ImageView imgBack = findViewById(R.id.imgBack);
        TextView txtBack = findViewById(R.id.txtBack);

        // Xử lý sự kiện click cho nút back (icon)
        imgBack.setOnClickListener(v -> {
            finish(); // Quay lại màn hình trước đó
        });

        // Xử lý sự kiện click cho text "Quay lại"
        txtBack.setOnClickListener(v -> {
            finish(); // Quay lại màn hình trước đó
        });
    }
}
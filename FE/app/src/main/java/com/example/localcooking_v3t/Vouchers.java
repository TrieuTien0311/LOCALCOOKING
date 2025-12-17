package com.example.localcooking_v3t;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class Vouchers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vouchers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xử lý nút quay lại
        ImageView btnBack = findViewById(R.id.imageView6);
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại Payment
        });

        // Xử lý nút áp dụng
        Button btnApDung = findViewById(R.id.btnApDung);
        btnApDung.setOnClickListener(v -> {
            finish(); // Quay lại Payment với voucher đã chọn
        });

        // Thêm Fragment vào container
        if (savedInstanceState == null) {
            VouchersFragment fragment = new VouchersFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }
    }
}
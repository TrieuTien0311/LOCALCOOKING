package com.example.localcooking_v3t;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

public class Classes extends AppCompatActivity {
    
    private TextView txtDiaDiem;
    private TextView txtThoiGian;
    private TextView btnThayDoi;
    private ActivityResultLauncher<Intent> calendarLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_classes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Ánh xạ views
        txtDiaDiem = findViewById(R.id.txtDiaDiem);
        txtThoiGian = findViewById(R.id.txtThoiGian);
        btnThayDoi = findViewById(R.id.btnThayDoi);
        ImageView btnBack = findViewById(R.id.btnBack);
        
        // Nhận dữ liệu từ HomeFragment
        Intent intent = getIntent();
        String destination = intent.getStringExtra("destination");
        String date = intent.getStringExtra("date");
        
        if (destination != null) {
            txtDiaDiem.setText(destination);
        }
        if (date != null) {
            txtThoiGian.setText(date);
        }
        
        // Setup calendar launcher
        calendarLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String selectedDate = result.getData().getStringExtra("selected_date");
                        if (selectedDate != null) {
                            txtThoiGian.setText(selectedDate);
                            
                            // Reload ClassesFragment với ngày mới
                            reloadClassesFragment(txtDiaDiem.getText().toString(), selectedDate);
                        }
                    }
                }
        );

        // Thêm Fragment vào container
        if (savedInstanceState == null) {
            ClassesFragment fragment = new ClassesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }

        // Xử lý sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());
        
        // Xử lý sự kiện nút Thay đổi
        btnThayDoi.setOnClickListener(v -> {
            Intent calendarIntent = new Intent(Classes.this, CalendarActivity.class);
            calendarIntent.putExtra("selected_date", txtThoiGian.getText().toString());
            calendarLauncher.launch(calendarIntent);
        });
    }
    
    /**
     * Reload ClassesFragment với địa điểm và ngày mới
     */
    private void reloadClassesFragment(String destination, String date) {
        // Cập nhật Intent để Fragment có thể lấy dữ liệu mới
        getIntent().putExtra("destination", destination);
        getIntent().putExtra("date", date);
        
        // Tạo Fragment mới và replace
        ClassesFragment fragment = new ClassesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}
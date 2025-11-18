package com.example.localcooking_v3t;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup rdGroupPayment;
    private RadioButton rdMomo, rdThe;
    private ImageView txtTrangThai1, txtTrangThai2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ các view
        rdGroupPayment = findViewById(R.id.rdGroupPayment);
        rdMomo = findViewById(R.id.rdMomo);
        rdThe = findViewById(R.id.rdThe);
        txtTrangThai1 = findViewById(R.id.txtTrangThai1);
        txtTrangThai2 = findViewById(R.id.txtTrangThai2);

        // Xử lý sự kiện thay đổi RadioButton
        rdGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdMomo) {
                    // Khi chọn Momo
                    txtTrangThai1.setVisibility(View.VISIBLE);
                    txtTrangThai2.setVisibility(View.INVISIBLE);
                } else if (checkedId == R.id.rdThe) {
                    // Khi chọn Thẻ
                    txtTrangThai1.setVisibility(View.INVISIBLE);
                    txtTrangThai2.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
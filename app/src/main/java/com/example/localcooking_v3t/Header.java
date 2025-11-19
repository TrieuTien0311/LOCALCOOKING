package com.example.localcooking_v3t;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class Header extends AppCompatActivity {

    private ImageView btnHome, btnFav, btnHis, btnNotice, btnPerson;
    private TextView txtTitle;
    private ImageView currentSelectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        btnHome = findViewById(R.id.btnHome);
        btnFav = findViewById(R.id.btnFav);
        btnHis = findViewById(R.id.btnHis);
        btnNotice = findViewById(R.id.btnNotice);
        btnPerson = findViewById(R.id.btnPerson);
        txtTitle = findViewById(R.id.txtTitle);

        // Set listener cho các button
        btnHome.setOnClickListener(v -> {
            selectButton(btnHome, "Trang chủ", R.drawable.ic_homefilled, R.drawable.ic_home);

            loadFragment(new HomeFragment());
        });

        btnFav.setOnClickListener(v -> {
            selectButton(btnFav, "Yêu thích", R.drawable.ic_heartfilled, R.drawable.ic_heart);
            loadFragment(new FavoriteFragment());
        });

        btnHis.setOnClickListener(v -> {
            selectButton(btnHis, "Lịch sử đặt lịch", R.drawable.ic_historyfilled, R.drawable.ic_history);
            loadFragment(new HomeFragment());
        });

        btnNotice.setOnClickListener(v -> {
            selectButton(btnNotice, "Thông báo", R.drawable.ic_noticefilled, R.drawable.ic_notice);
            loadFragment(new NoticeFragment());
        });

        btnPerson.setOnClickListener(v -> {
            selectButton(btnPerson, "Trang cá nhân", R.drawable.ic_personfilled, R.drawable.ic_person);
            loadFragment(new ProfileFragment());
        });

        // Mặc định chọn Trang chủ
        selectButton(btnHome, "Trang chủ", R.drawable.ic_homefilled, R.drawable.ic_home);
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {

        // Ẩn hoặc hiện Header
        FrameLayout headerLayout = findViewById(R.id.frameLayout);

        if (fragment instanceof HomeFragment) {
            headerLayout.setVisibility(View.GONE);   // Ẩn header
        } else {
            headerLayout.setVisibility(View.VISIBLE); // Hiện header
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }


    private void selectButton(ImageView selectedButton, String title, int filledIcon, int normalIcon) {

        resetAllButtons();

        selectedButton.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(0xFFFFC59D)
        );
        selectedButton.setImageResource(filledIcon);

        // Chỉ set title nếu KHÔNG phải Home
        if (!title.equals("Trang chủ")) {
            txtTitle.setText(title);
        }

        currentSelectedButton = selectedButton;
    }


    private void resetAllButtons() {
        resetButton(btnHome, R.drawable.ic_home);
        resetButton(btnFav, R.drawable.ic_heart);
        resetButton(btnHis, R.drawable.ic_history);
        resetButton(btnNotice, R.drawable.ic_notice);
        resetButton(btnPerson, R.drawable.ic_person);
    }

    private void resetButton(ImageView button, int normalIcon) {
        button.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
        button.setImageResource(normalIcon);
    }
}
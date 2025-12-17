package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvForgotPassword, tvRegister;
    private TextInputEditText idEmail, idMatKhau;
    private CheckBox cbRememberMe;
    private Button btnDangNhap;
    private MaterialButton btnGG, btnFB;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Cấu hình EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        mainLayout = findViewById(R.id.main);
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        idEmail = findViewById(R.id.idEmail);
        idMatKhau = findViewById(R.id.idMatKhau);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnGG = findViewById(R.id.btnGG);
        btnFB = findViewById(R.id.btnFB);

        // Thiết lập clear focus khi chạm ra ngoài
        setupClearFocusOnTouch();

        // --- Xử lý sự kiện ---
        // Nút quay lại - về Header (fragment home)
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Header.class);
                startActivity(intent);
                finish();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Header.class);
                startActivity(intent);
                finish();
            }
        });

        // Chuyển sang trang QMK
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Chuyển sang trang Home
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Header.class);
                startActivity(intent);
            }
        });

        // Chuyển sang trang Đăng ký
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Thiết lập clear focus khi chạm vào vùng ngoài EditText
     */
    private void setupClearFocusOnTouch() {
        if (mainLayout != null) {
            setupTouchListener(mainLayout);
        }
    }

    /**
     * Thiết lập touch listener đệ quy cho tất cả các view
     */
    private void setupTouchListener(View view) {
        // Nếu không phải EditText, thiết lập listener để clear focus
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clearFocusFromEditTexts();
                }
                return false;
            });
        }

        // Nếu là ViewGroup, đệ quy cho các view con
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setupTouchListener(child);
            }
        }
    }

    /**
     * Xóa focus khỏi tất cả EditText và ẩn bàn phím
     */
    private void clearFocusFromEditTexts() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
            // Ẩn bàn phím
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
        // Request focus vào main layout để EditText mất focus hoàn toàn
        if (mainLayout != null) {
            mainLayout.requestFocus();
        }
    }

    /**
     * Override dispatchTouchEvent để xử lý clear focus toàn cục
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = event.getRawX() + v.getLeft() - location[0];
                float y = event.getRawY() + v.getTop() - location[1];

                // Nếu chạm bên ngoài EditText đang focus
                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    clearFocusFromEditTexts();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
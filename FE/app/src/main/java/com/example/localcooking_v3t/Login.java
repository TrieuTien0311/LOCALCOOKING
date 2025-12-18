package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.LoginRequest;
import com.example.localcooking_v3t.model.LoginResponse;
import com.example.localcooking_v3t.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvForgotPassword, tvRegister;
    private TextInputEditText idEmail, idMatKhau;
    private CheckBox cbRememberMe;
    private Button btnDangNhap;
    private MaterialButton btnGG, btnFB;
    private View mainLayout;
    private boolean isPasswordVisible = false;
    private SessionManager sessionManager;

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
        
        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);
        
        // Debug: Hiển thị URL API đang sử dụng
        String apiUrl = RetrofitClient.getBaseUrl();
        android.util.Log.d("LOGIN_DEBUG", "API URL: " + apiUrl);

        // Thiết lập clear focus khi chạm ra ngoài
        setupClearFocusOnTouch();

        // Thiết lập toggle password visibility
        setupPasswordToggle();

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

        // Xử lý đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
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
     * Thiết lập toggle hiển thị/ẩn mật khẩu
     */
    private void setupPasswordToggle() {
        idMatKhau.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idMatKhau.getRight() - idMatKhau.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // Toggle password visibility
                    if (isPasswordVisible) {
                        // Ẩn mật khẩu
                        idMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible = false;
                    } else {
                        // Hiển thị mật khẩu
                        idMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible = true;
                    }
                    
                    // Đặt con trỏ về cuối text
                    idMatKhau.setSelection(idMatKhau.getText().length());
                    return true;
                }
            }
            return false;
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
    
    /**
     * Thực hiện đăng nhập
     */
    private void performLogin() {
        String email = idEmail.getText().toString().trim();
        String matKhau = idMatKhau.getText().toString().trim();
        
        // Validate input
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            idEmail.requestFocus();
            return;
        }
        
        if (matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            idMatKhau.requestFocus();
            return;
        }
        
        // Disable button để tránh click nhiều lần
        btnDangNhap.setEnabled(false);
        btnDangNhap.setText("Đang đăng nhập...");
        
        // Gọi API
        LoginRequest request = new LoginRequest(email, matKhau);
        Call<LoginResponse> call = RetrofitClient.getApiService().login(request);
        
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnDangNhap.setEnabled(true);
                btnDangNhap.setText("Đăng nhập");
                
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    if (loginResponse.isSuccess()) {
                        // Lưu session
                        sessionManager.createLoginSession(
                            loginResponse.getMaNguoiDung(),
                            loginResponse.getTenDangNhap(),
                            loginResponse.getHoTen(),
                            loginResponse.getEmail(),
                            loginResponse.getVaiTro()
                        );
                        
                        Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        
                        // Chuyển sang màn hình Home
                        navigateToHome();
                    } else {
                        Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnDangNhap.setEnabled(true);
                btnDangNhap.setText("Đăng nhập");
                Toast.makeText(Login.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Chuyển sang màn hình Home
     */
    private void navigateToHome() {
        Intent intent = new Intent(Login.this, Header.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
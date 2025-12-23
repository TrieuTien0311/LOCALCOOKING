package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvBack, tvLogin;
    private TextInputEditText idTenDangNhap, idHoTen, idEmail, idSoDienThoai, idMatKhau, idNhapLaiMatKhau;
    private CheckBox cbTerms;
    private Button btnDangKy;
    private MaterialButton btnGG, btnFB;
    private View mainLayout;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Set màu status bar
        setStatusBarColor();

        // Ánh xạ view
        mainLayout = findViewById(R.id.main);
        btnBack = findViewById(R.id.btnBack);
        tvBack = findViewById(R.id.tvBack);
        tvLogin = findViewById(R.id.tvLogin);
        idTenDangNhap = findViewById(R.id.idTenDangNhap);
        idHoTen = findViewById(R.id.idHoTen);
        idEmail = findViewById(R.id.idEmail);
        idSoDienThoai = findViewById(R.id.idSoDienThoai);
        idMatKhau = findViewById(R.id.idMatKhau);
        idNhapLaiMatKhau = findViewById(R.id.idNhapLaiMatKhau);
        cbTerms = findViewById(R.id.cbTerms);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnGG = findViewById(R.id.btnGG);
        btnFB = findViewById(R.id.btnFB);

        // Thiết lập clear focus khi chạm ra ngoài
        setupClearFocusOnTouch();

        // Thiết lập toggle password visibility
        setupPasswordToggle();
        
        // Thiết lập validation real-time
        setupPasswordValidation();

        // --- Xử lý sự kiện ---
        // Nút quay lại
        btnBack.setOnClickListener(v -> {
            finish();
        });

        tvBack.setOnClickListener(v -> {
            finish();
        });

        // Chuyển sang trang Đăng nhập
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Xử lý đăng ký
        btnDangKy.setOnClickListener(v -> {
            performRegister();
        });
    }

    /**
     * Thiết lập toggle hiển thị/ẩn mật khẩu
     */
    private void setupPasswordToggle() {
        // Toggle cho mật khẩu
        idMatKhau.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idMatKhau.getRight() - idMatKhau.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible) {
                        idMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isPasswordVisible = false;
                    } else {
                        idMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isPasswordVisible = true;
                    }
                    idMatKhau.setSelection(idMatKhau.getText().length());
                    return true;
                }
            }
            return false;
        });

        // Toggle cho nhập lại mật khẩu
        idNhapLaiMatKhau.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (idNhapLaiMatKhau.getRight() - idNhapLaiMatKhau.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isConfirmPasswordVisible) {
                        idNhapLaiMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        idNhapLaiMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye_hide_tt, 0);
                        isConfirmPasswordVisible = false;
                    } else {
                        idNhapLaiMatKhau.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        idNhapLaiMatKhau.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_eye, 0);
                        isConfirmPasswordVisible = true;
                    }
                    idNhapLaiMatKhau.setSelection(idNhapLaiMatKhau.getText().length());
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Thiết lập validation real-time cho mật khẩu
     */
    private void setupPasswordValidation() {
        // Validation cho mật khẩu
        idMatKhau.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                com.google.android.material.textfield.TextInputLayout tilMatKhau = 
                    findViewById(R.id.tilMatKhau);
                
                if (password.length() > 0 && password.length() < 6) {
                    tilMatKhau.setError("Mật khẩu phải có ít nhất 6 ký tự");
                    tilMatKhau.setErrorEnabled(true);
                } else {
                    tilMatKhau.setError(null);
                    tilMatKhau.setErrorEnabled(false);
                }
                
                // Kiểm tra lại mật khẩu nhập lại nếu đã có
                String confirmPassword = idNhapLaiMatKhau.getText().toString();
                if (confirmPassword.length() > 0) {
                    validateConfirmPassword();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Validation cho nhập lại mật khẩu
        idNhapLaiMatKhau.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateConfirmPassword();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
    
    /**
     * Kiểm tra mật khẩu nhập lại có khớp không
     */
    private void validateConfirmPassword() {
        String password = idMatKhau.getText().toString();
        String confirmPassword = idNhapLaiMatKhau.getText().toString();
        com.google.android.material.textfield.TextInputLayout tilNhapLaiMatKhau = 
            findViewById(R.id.tilNhapLaiMatKhau);
        
        if (confirmPassword.length() > 0 && !password.equals(confirmPassword)) {
            tilNhapLaiMatKhau.setError("Mật khẩu bạn nhập lại không khớp");
            tilNhapLaiMatKhau.setErrorEnabled(true);
        } else {
            tilNhapLaiMatKhau.setError(null);
            tilNhapLaiMatKhau.setErrorEnabled(false);
        }
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
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clearFocusFromEditTexts();
                }
                return false;
            });
        }

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
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
        if (mainLayout != null) {
            mainLayout.requestFocus();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                float x = event.getRawX() + v.getLeft() - location[0];
                float y = event.getRawY() + v.getTop() - location[1];

                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    clearFocusFromEditTexts();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Thực hiện đăng ký
     */
    private void performRegister() {
        String tenDangNhap = idTenDangNhap.getText().toString().trim();
        String hoTen = idHoTen.getText().toString().trim();
        String email = idEmail.getText().toString().trim();
        String soDienThoai = idSoDienThoai.getText().toString().trim();
        String matKhau = idMatKhau.getText().toString().trim();
        String nhapLaiMatKhau = idNhapLaiMatKhau.getText().toString().trim();

        // Validate input
        if (tenDangNhap.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            idTenDangNhap.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            idEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            idEmail.requestFocus();
            return;
        }

        if (matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            idMatKhau.requestFocus();
            return;
        }

        if (matKhau.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            idMatKhau.requestFocus();
            return;
        }

        if (nhapLaiMatKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            idNhapLaiMatKhau.requestFocus();
            return;
        }

        if (!matKhau.equals(nhapLaiMatKhau)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            idNhapLaiMatKhau.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API đăng ký
        btnDangKy.setEnabled(false);
        btnDangKy.setText("Đang đăng ký...");

        com.example.localcooking_v3t.model.RegisterRequest request = 
            new com.example.localcooking_v3t.model.RegisterRequest(
                tenDangNhap,
                matKhau,
                hoTen.isEmpty() ? null : hoTen,
                email,
                soDienThoai.isEmpty() ? null : soDienThoai
            );

        com.example.localcooking_v3t.api.RetrofitClient.getApiService()
            .register(request)
            .enqueue(new retrofit2.Callback<com.example.localcooking_v3t.model.RegisterResponse>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.localcooking_v3t.model.RegisterResponse> call, 
                                     retrofit2.Response<com.example.localcooking_v3t.model.RegisterResponse> response) {
                    btnDangKy.setEnabled(true);
                    btnDangKy.setText("Đăng ký");

                    if (response.isSuccessful() && response.body() != null) {
                        com.example.localcooking_v3t.model.RegisterResponse registerResponse = response.body();

                        if (registerResponse.isSuccess()) {
                            Toast.makeText(Register.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            
                            // Chuyển sang màn hình Login
                            Intent intent = new Intent(Register.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Register.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.localcooking_v3t.model.RegisterResponse> call, 
                                    Throwable t) {
                    btnDangKy.setEnabled(true);
                    btnDangKy.setText("Đăng ký");
                    Toast.makeText(Register.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
    
    private void setStatusBarColor() {
        Window window = getWindow();
        window.setStatusBarColor(0xFFF5E6D3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}

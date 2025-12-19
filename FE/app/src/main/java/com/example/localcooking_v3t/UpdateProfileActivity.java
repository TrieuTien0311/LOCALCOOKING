package com.example.localcooking_v3t;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.ProfileResponse;
import com.example.localcooking_v3t.model.UpdateProfileRequest;
import com.example.localcooking_v3t.model.UpdateProfileResponse;
import com.example.localcooking_v3t.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    // Views
    private ImageView btnBack;
    private EditText edtFullName, edtPhone, edtEmail;
    private TextView tvBirthday, tvCountryCode;
    private LinearLayout layoutBirthday;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale, rbOther;
    private Button btnSave;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Đổi màu status bar cho khớp với header
        setupStatusBar();

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);



        // Ánh xạ views
        initViews();

        // Load dữ liệu người dùng từ API
        loadUserDataFromApi();

        // Thiết lập listeners
        setupListeners();
    }

    private void setupStatusBar() {
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#FFBA86"));

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsController != null) {
            // true = icon màu tối (cho background sáng)
            windowInsetsController.setAppearanceLightStatusBars(true);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        tvCountryCode = findViewById(R.id.tvCountryCode);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupListeners() {
        // Nút back
        btnBack.setOnClickListener(v -> finish());

        // Nút lưu
        btnSave.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserDataFromApi() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Integer maNguoiDung = sessionManager.getMaNguoiDung();
        if (maNguoiDung == null || maNguoiDung == -1) {
            // Fallback: load từ session
            loadUserDataFromSession();
            return;
        }

        RetrofitClient.getApiService().getProfile(maNguoiDung).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ProfileResponse profile = response.body();
                    
                    if (profile.getHoTen() != null && !profile.getHoTen().isEmpty()) {
                        edtFullName.setText(profile.getHoTen());
                    }
                    if (profile.getEmail() != null && !profile.getEmail().isEmpty()) {
                        edtEmail.setText(profile.getEmail());
                    }
                    if (profile.getSoDienThoai() != null && !profile.getSoDienThoai().isEmpty()) {
                        edtPhone.setText(profile.getSoDienThoai());
                    }
                } else {
                    // Fallback: load từ session
                    loadUserDataFromSession();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                // Fallback: load từ session
                loadUserDataFromSession();
            }
        });
    }

    private void loadUserDataFromSession() {
        if (sessionManager.isLoggedIn()) {
            String tenDangNhap = sessionManager.getTenDangNhap();
            String email = sessionManager.getEmail();

            if (tenDangNhap != null && !tenDangNhap.isEmpty()) {
                edtFullName.setText(tenDangNhap);
            }
            if (email != null && !email.isEmpty()) {
                edtEmail.setText(email);
            }
        }
    }



    private void saveUserProfile() {
        String fullName = edtFullName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        // Validate
        if (fullName.isEmpty()) {
            edtFullName.setError("Vui lòng nhập họ tên");
            edtFullName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            edtPhone.requestFocus();
            return;
        }

        if (phone.length() < 9 || phone.length() > 11) {
            edtPhone.setError("Số điện thoại không hợp lệ");
            edtPhone.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        Integer maNguoiDung = sessionManager.getMaNguoiDung();
        if (maNguoiDung == null || maNguoiDung == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button
        btnSave.setEnabled(false);
        btnSave.setText("Đang lưu...");

        UpdateProfileRequest request = new UpdateProfileRequest(maNguoiDung, fullName, email, phone, null);

        RetrofitClient.getApiService().updateProfile(request).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                btnSave.setEnabled(true);
                btnSave.setText("Lưu");

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    // Cập nhật session với thông tin mới
                    sessionManager.updateUserInfo(fullName, email);
                    
                    Toast.makeText(UpdateProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String message = "Cập nhật thất bại";
                    if (response.body() != null) {
                        message = response.body().getMessage();
                    }
                    Toast.makeText(UpdateProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                btnSave.setEnabled(true);
                btnSave.setText("Lưu");
                Toast.makeText(UpdateProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

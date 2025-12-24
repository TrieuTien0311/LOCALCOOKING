package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localcooking_v3t.utils.SessionManager;
import com.example.localcooking_v3t.helper.GoogleSignInHelper;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private GoogleSignInHelper googleSignInHelper;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private Button btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        
        // Khởi tạo GoogleSignInHelper với Web Client ID
        String WEB_CLIENT_ID = "954091456874-iji1kkmljees7803o33p78gl34pl90ek.apps.googleusercontent.com";
        googleSignInHelper = new GoogleSignInHelper(requireContext(), WEB_CLIENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Ánh xạ view
        tvUserName = view.findViewById(R.id.textView2);
        tvUserEmail = view.findViewById(R.id.textView38);
        btnLogout = view.findViewById(R.id.button4);

        // Hiển thị thông tin người dùng
        loadUserInfo();

        // Xử lý click vào "Thông tin cá nhân"
        View infoLayout = view.findViewById(R.id.cDThongTin);
        infoLayout.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });

        // Xử lý click vào "Đổi mật khẩu"
        View changePasswordLayout = view.findViewById(R.id.cDMatKhau);
        changePasswordLayout.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });

        // Xử lý click vào "Trợ giúp và hỗ trợ"
        View helpLayout = view.findViewById(R.id.cDTroGiup);
        helpLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);
        });

        // Xử lý click vào "Lịch sử đặt lịch"
        View orderHistoryLayout = view.findViewById(R.id.cDLichSuDat);
        orderHistoryLayout.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                // Chuyển đến OrderHistoryFragment thông qua Header
                if (getActivity() instanceof Header) {
                    ((Header) getActivity()).navigateToOrderHistory();
                }
            } else {
                Toast.makeText(requireContext(), "Vui lòng đăng nhập để xem lịch sử đặt lịch", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });

        // Xử lý click vào "Mục yêu thích"
        View favoriteLayout = view.findViewById(R.id.cDYeuThich);
        favoriteLayout.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                // Chuyển đến FavoriteFragment thông qua Header
                if (getActivity() instanceof Header) {
                    ((Header) getActivity()).navigateToFavorite();
                }
            } else {
                Toast.makeText(requireContext(), "Vui lòng đăng nhập để xem mục yêu thích", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });

        // Xử lý nút "Đăng xuất"
        btnLogout.setOnClickListener(v -> {
            performLogout();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại thông tin khi quay lại fragment
        loadUserInfo();
    }

    /**
     * Load thông tin người dùng từ session
     */
    private void loadUserInfo() {
        if (sessionManager.isLoggedIn()) {
            String tenDangNhap = sessionManager.getTenDangNhap();
            String email = sessionManager.getEmail();

            // Hiển thị tên đăng nhập (luôn có giá trị)
            if (tenDangNhap != null && !tenDangNhap.isEmpty()) {
                tvUserName.setText(tenDangNhap);
            } else {
                tvUserName.setText("Người dùng");
            }

            // Hiển thị email
            if (email != null && !email.isEmpty()) {
                tvUserEmail.setText(email);
            } else {
                tvUserEmail.setText("Chưa có email");
            }

            btnLogout.setText("Đăng xuất");
        } else {
            tvUserName.setText("Khách");
            tvUserEmail.setText("Vui lòng đăng nhập");
            btnLogout.setText("Đăng nhập");
        }
    }

    /**
     * Thực hiện đăng xuất
     */
    private void performLogout() {
        if (sessionManager.isLoggedIn()) {
            // Kiểm tra nếu đăng nhập bằng Google thì sign out khỏi Google
            String loginMethod = sessionManager.getLoginMethod();
            if ("GOOGLE".equals(loginMethod)) {
                googleSignInHelper.signOut();
            }
            
            // Xóa session
            sessionManager.logout();

            Toast.makeText(requireContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();

            // Reload lại fragment để cập nhật UI
            loadUserInfo();

            // Chuyển về HomeFragment
            if (getActivity() instanceof Header) {
                ((Header) getActivity()).navigateToHome();
            }
        } else {
            // Nếu chưa đăng nhập, chuyển đến trang đăng nhập
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }
}

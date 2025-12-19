package com.example.localcooking_v3t;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.LopHoc;
import com.example.localcooking_v3t.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ImageView ivLogo, ivArrow;
    private TextView tvAppName, tvHello, tvCurrentLocation, tvDestination, tvDate;
    private TextView tvViewAll;
    private Button btnSearch;
    private LinearLayout layoutPopularClasses;
    private ActivityResultLauncher<Intent> calendarLauncher;
    private SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendarLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String selectedDate = result.getData().getStringExtra("selected_date");
                        if (tvDate != null) {
                            tvDate.setText(selectedDate);
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ view
        ivLogo = view.findViewById(R.id.ivLogo);
        ivArrow = view.findViewById(R.id.ivArrow);
        tvAppName = view.findViewById(R.id.tvAppName);
        tvHello = view.findViewById(R.id.tvHello);
        tvCurrentLocation = view.findViewById(R.id.tvCurrentLocation);
        tvDestination = view.findViewById(R.id.tvDestination);
        tvDate = view.findViewById(R.id.tvDate);
        tvViewAll = view.findViewById(R.id.tvViewAll);
        btnSearch = view.findViewById(R.id.btnSearch);
        layoutPopularClasses = view.findViewById(R.id.layoutPopularClasses);
        
        // Khởi tạo SessionManager
        sessionManager = new SessionManager(requireContext());
        
        // Hiển thị tên người dùng hoặc "Đăng nhập"
        updateUserDisplay();
        
        // Load lớp học phổ biến
        loadPopularClasses();

        // --- Xử lý sự kiện ---

        tvHello.setOnClickListener(v -> {
            if (!sessionManager.isLoggedIn()) {
                Intent intent = new Intent(requireContext(), Login.class);
                startActivity(intent);
            }
        });

        ivArrow.setOnClickListener(v -> {
            if (!sessionManager.isLoggedIn()) {
                Intent intent = new Intent(requireContext(), Login.class);
                startActivity(intent);
            }
        });

        tvDate.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CalendarActivity.class);
            calendarLauncher.launch(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Classes.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // Cập nhật lại hiển thị user khi quay lại fragment
        updateUserDisplay();

        Window window = requireActivity().getWindow();

        // Đặt lại màu Status Bar
        window.setStatusBarColor(Color.parseColor("#FFC59D"));

        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (windowInsetsController != null) {
            windowInsetsController.setAppearanceLightStatusBars(true);
        }
    }
    
    /**
     * Cập nhật hiển thị tên người dùng
     */
    private void updateUserDisplay() {
        if (sessionManager.isLoggedIn()) {
            String tenDangNhap = sessionManager.getTenDangNhap();
            if (tenDangNhap != null && !tenDangNhap.isEmpty()) {
                tvHello.setText("Chào " + tenDangNhap);
            } else {
                tvHello.setText("Chào bạn");
            }
            // Ẩn mũi tên khi đã đăng nhập
            ivArrow.setVisibility(View.GONE);
        } else {
            tvHello.setText("Đăng nhập");
            ivArrow.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Load 4 lớp học phổ biến từ 4 địa phương khác nhau
     */
    private void loadPopularClasses() {
        RetrofitClient.getApiService().getAllLopHoc().enqueue(new Callback<List<LopHoc>>() {
            @Override
            public void onResponse(Call<List<LopHoc>> call, Response<List<LopHoc>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LopHoc> allClasses = response.body();
                    List<LopHoc> popularClasses = selectPopularClasses(allClasses);
                    displayPopularClasses(popularClasses);
                } else {
                    Log.e(TAG, "Failed to load classes: " + response.code());
                    Toast.makeText(requireContext(), "Không thể tải lớp học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LopHoc>> call, Throwable t) {
                Log.e(TAG, "Error loading classes", t);
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Chọn 4 lớp học từ 4 địa phương khác nhau (2 có ưu đãi, 2 không có)
     */
    private List<LopHoc> selectPopularClasses(List<LopHoc> allClasses) {
        Map<String, List<LopHoc>> classByLocation = new HashMap<>();
        String[] locations = {"Hà Nội", "Huế", "Đà Nẵng", "Cần Thơ"};
        
        // Nhóm lớp học theo địa phương
        for (LopHoc lopHoc : allClasses) {
            String diaPhuong = lopHoc.getDiaPhuong();
            if (!diaPhuong.isEmpty()) {
                if (!classByLocation.containsKey(diaPhuong)) {
                    classByLocation.put(diaPhuong, new ArrayList<>());
                }
                classByLocation.get(diaPhuong).add(lopHoc);
            }
        }
        
        List<LopHoc> result = new ArrayList<>();
        List<LopHoc> withDiscount = new ArrayList<>();
        List<LopHoc> withoutDiscount = new ArrayList<>();
        
        // Chọn lớp từ mỗi địa phương
        for (String location : locations) {
            if (classByLocation.containsKey(location)) {
                List<LopHoc> classes = classByLocation.get(location);
                for (LopHoc lopHoc : classes) {
                    if (lopHoc.getCoUuDai() != null && lopHoc.getCoUuDai()) {
                        withDiscount.add(lopHoc);
                    } else {
                        withoutDiscount.add(lopHoc);
                    }
                }
            }
        }
        
        // Chọn 2 lớp có ưu đãi
        for (int i = 0; i < Math.min(2, withDiscount.size()); i++) {
            result.add(withDiscount.get(i));
        }
        
        // Chọn 2 lớp không có ưu đãi
        for (int i = 0; i < Math.min(2, withoutDiscount.size()); i++) {
            result.add(withoutDiscount.get(i));
        }
        
        // Nếu không đủ, bổ sung từ danh sách còn lại
        if (result.size() < 4) {
            for (LopHoc lopHoc : allClasses) {
                if (!result.contains(lopHoc)) {
                    result.add(lopHoc);
                    if (result.size() >= 4) break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Hiển thị 4 lớp học phổ biến
     */
    private void displayPopularClasses(List<LopHoc> classes) {
        if (layoutPopularClasses == null || classes.isEmpty()) return;
        
        layoutPopularClasses.removeAllViews();
        
        for (int i = 0; i < Math.min(classes.size(), 4); i++) {
            LopHoc lopHoc = classes.get(i);
            View classCard = createClassCard(lopHoc, i + 1);
            layoutPopularClasses.addView(classCard);
        }
    }
    
    /**
     * Tạo CardView cho lớp học với bố cục cân đối
     */
    private View createClassCard(LopHoc lopHoc, int index) {
        float density = getResources().getDisplayMetrics().density;
        
        // Tạo CardView với chiều cao cố định và margin để hiển thị đổ bóng
        CardView cardView = new CardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                (int) (160 * density),
                (int) (230 * density)  // Chiều cao cố định
        );
        cardParams.setMarginEnd((int) (12 * density));
        cardParams.topMargin = (int) (4 * density);      // Margin trên để hiện đổ bóng
        cardParams.bottomMargin = (int) (8 * density);   // Margin dưới để hiện đổ bóng
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(8 * density);
        cardView.setCardElevation(4 * density);
        cardView.setUseCompatPadding(true);  // Quan trọng: để hiển thị đổ bóng đầy đủ
        cardView.setId(View.generateViewId());
        cardView.setTag("Lop" + lopHoc.getMaLopHoc());
        
        // LinearLayout bên trong
        LinearLayout innerLayout = new LinearLayout(requireContext());
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setBackgroundColor(Color.WHITE);
        
        // ImageView
        ImageView imageView = new ImageView(requireContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (120 * density)
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.lophocphobien);
        innerLayout.addView(imageView);
        
        // TextView tên lớp
        TextView tvName = new TextView(requireContext());
        int padding = (int) (8 * density);
        tvName.setPadding(padding, padding, padding, (int) (4 * density));
        tvName.setText(lopHoc.getTenLop());
        tvName.setTextColor(Color.BLACK);
        tvName.setTextSize(12);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        tvName.setMaxLines(2);
        tvName.setMinLines(2);  // Đảm bảo luôn có 2 dòng
        tvName.setEllipsize(android.text.TextUtils.TruncateAt.END);
        innerLayout.addView(tvName);
        
        // LinearLayout cho giá
        LinearLayout priceLayout = new LinearLayout(requireContext());
        priceLayout.setOrientation(LinearLayout.VERTICAL);
        priceLayout.setPadding(padding, 0, padding, padding);
        
        if (lopHoc.getCoUuDai() != null && lopHoc.getCoUuDai()) {
            // Giá gốc (gạch ngang)
            TextView tvOriginalPrice = new TextView(requireContext());
            tvOriginalPrice.setText(lopHoc.getGia());
            tvOriginalPrice.setTextColor(Color.GRAY);
            tvOriginalPrice.setTextSize(11);
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceLayout.addView(tvOriginalPrice);
            
            // Giá sau giảm
            TextView tvDiscountPrice = new TextView(requireContext());
            tvDiscountPrice.setText(lopHoc.getGiaSauGiam() + " (-10%)");
            tvDiscountPrice.setTextColor(Color.parseColor("#E74C3C"));
            tvDiscountPrice.setTextSize(13);
            tvDiscountPrice.setTypeface(null, android.graphics.Typeface.BOLD);
            priceLayout.addView(tvDiscountPrice);
        } else {
            // Thêm TextView trống để giữ chiều cao đồng nhất
            TextView tvEmpty = new TextView(requireContext());
            tvEmpty.setText(" ");
            tvEmpty.setTextSize(11);
            priceLayout.addView(tvEmpty);
            
            // Giá bình thường
            TextView tvPrice = new TextView(requireContext());
            tvPrice.setText(lopHoc.getGia());
            tvPrice.setTextColor(Color.parseColor("#E74C3C"));
            tvPrice.setTextSize(13);
            tvPrice.setTypeface(null, android.graphics.Typeface.BOLD);
            priceLayout.addView(tvPrice);
        }
        
        innerLayout.addView(priceLayout);
        cardView.addView(innerLayout);
        
        // Click listener
        cardView.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Lớp: " + lopHoc.getTenLop(), Toast.LENGTH_SHORT).show();
        });
        
        return cardView;
    }
}
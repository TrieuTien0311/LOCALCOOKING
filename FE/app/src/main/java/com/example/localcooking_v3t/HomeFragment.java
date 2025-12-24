package com.example.localcooking_v3t;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
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
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.utils.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    
    private ImageView ivLogo, ivArrow;
    private TextView tvAppName, tvHello, tvCurrentLocation, tvDestination, tvDate;
    private TextView tvViewAll;
    private Button btnSearch;
    private LinearLayout layoutPopularClasses;
    private ActivityResultLauncher<Intent> calendarLauncher;
    private SessionManager sessionManager;
    
    // Location
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    
    // Danh sách 4 địa điểm
    private static final String[] DESTINATIONS = {"Hà Nội", "Huế", "Đà Nẵng", "Cần Thơ"};
    
    // Tọa độ gần đúng của 4 địa điểm (latitude, longitude)
    private static final double[][] DESTINATION_COORDS = {
        {21.0285, 105.8542},  // Hà Nội
        {16.4637, 107.5909},  // Huế
        {16.0544, 108.2022},  // Đà Nẵng
        {10.0452, 105.7469}   // Cần Thơ
    };

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
        
        // Location permission launcher
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    
                    if ((fineLocationGranted != null && fineLocationGranted) || 
                        (coarseLocationGranted != null && coarseLocationGranted)) {
                        getCurrentLocation();
                    } else {
                        Toast.makeText(requireContext(), "Cần cấp quyền vị trí để hiển thị vị trí hiện tại", Toast.LENGTH_SHORT).show();
                        tvCurrentLocation.setText("Hà Nội");
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
        
        // Khởi tạo Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        
        // Hiển thị tên người dùng hoặc "Đăng nhập"
        updateUserDisplay();
        
        // Load lớp học phổ biến
        loadPopularClasses();
        
        // Lấy vị trí hiện tại
        checkLocationPermissionAndGetLocation();

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

        // Hiển thị ngày hiện tại
        setCurrentDate();
        
        tvDate.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CalendarActivity.class);
            // Truyền ngày đang được chọn vào CalendarActivity
            intent.putExtra("selected_date", tvDate.getText().toString());
            calendarLauncher.launch(intent);
        });
        
        tvDestination.setOnClickListener(v -> showDestinationDialog());
        
        btnSearch.setOnClickListener(v -> {
            // Truyền dữ liệu sang Classes Activity
            Intent intent = new Intent(requireContext(), Classes.class);
            intent.putExtra("destination", tvDestination.getText().toString());
            intent.putExtra("date", tvDate.getText().toString());
            startActivity(intent);
        });
        
        // Setup RecyclerView món ăn đặc sắc với infinite scroll
        setupSpecialDishesRecyclerView(view);
        
        return view;
    }
    
    /**
     * Setup RecyclerView món ăn đặc sắc với infinite scroll 2 chiều
     */
    private void setupSpecialDishesRecyclerView(View view) {
        androidx.recyclerview.widget.RecyclerView recyclerSpecialDishes = view.findViewById(R.id.recyclerSpecialDishes);
        
        // Danh sách 6 ảnh món ăn
        List<Integer> dishImages = new java.util.ArrayList<>();
        dishImages.add(R.drawable.phobo);
        dishImages.add(R.drawable.comtam);
        dishImages.add(R.drawable.banhxeo);
        dishImages.add(R.drawable.nemnuong);
        dishImages.add(R.drawable.banhcuon);
        dishImages.add(R.drawable.buncha);
        
        SpecialDishAdapter adapter = new SpecialDishAdapter(dishImages);
        
        // LinearLayoutManager ngang
        androidx.recyclerview.widget.LinearLayoutManager layoutManager = 
            new androidx.recyclerview.widget.LinearLayoutManager(requireContext(), 
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false);
        
        recyclerSpecialDishes.setLayoutManager(layoutManager);
        recyclerSpecialDishes.setAdapter(adapter);
        
        // Scroll đến vị trí giữa để có thể scroll cả 2 chiều
        int middlePosition = Integer.MAX_VALUE / 2;
        // Đảm bảo vị trí bắt đầu từ item đầu tiên
        middlePosition = middlePosition - (middlePosition % dishImages.size());
        recyclerSpecialDishes.scrollToPosition(middlePosition);
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
     * Sử dụng API mới: /api/khoahoc (trả về KhoaHocDTO)
     */
    private void loadPopularClasses() {
        RetrofitClient.getApiService().getAllKhoaHoc().enqueue(new Callback<List<KhoaHoc>>() {
            @Override
            public void onResponse(Call<List<KhoaHoc>> call, Response<List<KhoaHoc>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<KhoaHoc> allClasses = response.body();
                    Log.d(TAG, "Loaded " + allClasses.size() + " classes from API");
                    
                    // Kiểm tra fragment còn attached không
                    if (!isAdded() || getContext() == null) {
                        Log.w(TAG, "Fragment not attached, skip displaying classes");
                        return;
                    }
                    
                    // Log chi tiết để debug
                    for (KhoaHoc kh : allClasses) {
                        Log.d(TAG, "KhoaHoc: " + kh.getTenKhoaHoc() + 
                              ", Gia: " + kh.getGiaTien() + 
                              ", UuDai: " + kh.getCoUuDai() +
                              ", HinhAnh: " + kh.getHinhAnh() +
                              ", LichTrinh: " + (kh.getLichTrinhList() != null ? kh.getLichTrinhList().size() : 0));
                    }
                    
                    List<KhoaHoc> popularClasses = selectPopularClasses(allClasses);
                    displayPopularClasses(popularClasses);
                } else {
                    // Kiểm tra fragment còn attached không
                    if (!isAdded() || getContext() == null) {
                        Log.w(TAG, "Fragment not attached, skip error handling");
                        return;
                    }
                    
                    Log.e(TAG, "Failed to load classes: " + response.code());
                    Toast.makeText(getContext(), "Không thể tải lớp học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KhoaHoc>> call, Throwable t) {
                // Kiểm tra fragment còn attached không
                if (!isAdded() || getContext() == null) {
                    Log.w(TAG, "Fragment not attached, skip error handling");
                    return;
                }
                
                Log.e(TAG, "Error loading classes", t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Chọn 4 lớp học từ 4 địa phương khác nhau (2 có ưu đãi, 2 không có)
     */
    private List<KhoaHoc> selectPopularClasses(List<KhoaHoc> allClasses) {
        Map<String, List<KhoaHoc>> classByLocation = new HashMap<>();
        String[] locations = {"Hà Nội", "Huế", "Đà Nẵng", "Cần Thơ"};
        
        // Nhóm lớp học theo địa phương (từ lịch trình)
        for (KhoaHoc khoaHoc : allClasses) {
            if (khoaHoc.getLichTrinhList() != null && !khoaHoc.getLichTrinhList().isEmpty()) {
                // Lấy địa phương từ lịch trình đầu tiên
                String diaDiem = khoaHoc.getLichTrinhList().get(0).getDiaDiem();
                if (diaDiem != null) {
                    String diaPhuong = "";
                    if (diaDiem.contains("Hà Nội")) diaPhuong = "Hà Nội";
                    else if (diaDiem.contains("Huế")) diaPhuong = "Huế";
                    else if (diaDiem.contains("Đà Nẵng")) diaPhuong = "Đà Nẵng";
                    else if (diaDiem.contains("Cần Thơ")) diaPhuong = "Cần Thơ";
                    
                    if (!diaPhuong.isEmpty()) {
                        if (!classByLocation.containsKey(diaPhuong)) {
                            classByLocation.put(diaPhuong, new ArrayList<>());
                        }
                        classByLocation.get(diaPhuong).add(khoaHoc);
                    }
                }
            }
        }
        
        List<KhoaHoc> result = new ArrayList<>();
        List<KhoaHoc> withDiscount = new ArrayList<>();
        List<KhoaHoc> withoutDiscount = new ArrayList<>();
        
        // Chọn lớp từ mỗi địa phương
        for (String location : locations) {
            if (classByLocation.containsKey(location)) {
                List<KhoaHoc> classes = classByLocation.get(location);
                for (KhoaHoc khoaHoc : classes) {
                    if (khoaHoc.getCoUuDai() != null && khoaHoc.getCoUuDai()) {
                        withDiscount.add(khoaHoc);
                    } else {
                        withoutDiscount.add(khoaHoc);
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
            for (KhoaHoc khoaHoc : allClasses) {
                if (!result.contains(khoaHoc)) {
                    result.add(khoaHoc);
                    if (result.size() >= 4) break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Hiển thị 4 lớp học phổ biến
     */
    private void displayPopularClasses(List<KhoaHoc> classes) {
        // Kiểm tra fragment còn attached không
        if (!isAdded() || getContext() == null) {
            Log.w(TAG, "Fragment not attached, skip displaying popular classes");
            return;
        }
        
        if (layoutPopularClasses == null || classes.isEmpty()) return;
        
        layoutPopularClasses.removeAllViews();
        
        for (int i = 0; i < Math.min(classes.size(), 4); i++) {
            KhoaHoc lopHoc = classes.get(i);
            View classCard = createClassCard(lopHoc, i + 1);
            if (classCard != null) {
                layoutPopularClasses.addView(classCard);
            }
        }
    }
    
    /**
     * Tạo CardView cho lớp học với bố cục cân đối
     */
    private View createClassCard(KhoaHoc khoaHoc, int index) {
        // Kiểm tra fragment còn attached không
        if (!isAdded() || getContext() == null) {
            Log.w(TAG, "Fragment not attached, skip creating class card");
            return null;
        }
        
        float density = getResources().getDisplayMetrics().density;
        
        // Tạo CardView với chiều cao cố định và margin để hiển thị đổ bóng
        CardView cardView = new CardView(getContext());
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
        cardView.setTag("KhoaHoc" + khoaHoc.getMaKhoaHoc());
        
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
        imageView.setImageResource(khoaHoc.getHinhAnhResId(requireContext()));
        innerLayout.addView(imageView);
        
        // TextView tên khóa học
        TextView tvName = new TextView(requireContext());
        int padding = (int) (8 * density);
        tvName.setPadding(padding, padding, padding, (int) (4 * density));
        tvName.setText(khoaHoc.getTenKhoaHoc());
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
        
        if (khoaHoc.getCoUuDai() != null && khoaHoc.getCoUuDai()) {
            // Giá gốc (gạch ngang)
            TextView tvOriginalPrice = new TextView(requireContext());
            tvOriginalPrice.setText(khoaHoc.getGiaFormatted());
            tvOriginalPrice.setTextColor(Color.GRAY);
            tvOriginalPrice.setTextSize(11);
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceLayout.addView(tvOriginalPrice);
            
            // Giá sau giảm (giảm 10%)
            TextView tvDiscountPrice = new TextView(requireContext());
            double giaSauGiam = khoaHoc.getGiaTien() * 0.9;
            String giaSauGiamText = String.format(Locale.getDefault(), "%,.0fđ (-10%%)", giaSauGiam).replace(",", ".");
            tvDiscountPrice.setText(giaSauGiamText);
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
            tvPrice.setText(khoaHoc.getGiaFormatted());
            tvPrice.setTextColor(Color.parseColor("#E74C3C"));
            tvPrice.setTextSize(13);
            tvPrice.setTypeface(null, android.graphics.Typeface.BOLD);
            priceLayout.addView(tvPrice);
        }
        
        innerLayout.addView(priceLayout);
        cardView.addView(innerLayout);
        
        // Click listener
        cardView.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Khóa học: " + khoaHoc.getTenKhoaHoc(), Toast.LENGTH_SHORT).show();
        });
        
        return cardView;
    }
    
    /**
     * Kiểm tra quyền và lấy vị trí hiện tại
     */
    private void checkLocationPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            // Yêu cầu quyền
            locationPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }
    
    /**
     * Lấy vị trí hiện tại của thiết bị
     */
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    // Kiểm tra fragment còn attached không
                    if (!isAdded() || getContext() == null) {
                        Log.w(TAG, "Fragment not attached, skip location update");
                        return;
                    }
                    
                    if (location != null) {
                        // Lưu tọa độ hiện tại
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                        
                        // Lấy được vị trí, chuyển đổi thành tên địa phương
                        getAddressFromLocation(location.getLatitude(), location.getLongitude());
                        
                        // Tìm và hiển thị địa điểm gần nhất cho tvDestination
                        String nearestDestination = findNearestDestination(currentLatitude, currentLongitude);
                        tvDestination.setText(nearestDestination);
                    } else {
                        // Không lấy được vị trí, hiển thị mặc định
                        tvCurrentLocation.setText("Hà Nội");
                        tvDestination.setText("Đà Nẵng");
                        Log.w(TAG, "Location is null");
                    }
                })
                .addOnFailureListener(e -> {
                    // Kiểm tra fragment còn attached không
                    if (!isAdded() || getContext() == null) {
                        Log.w(TAG, "Fragment not attached, skip error handling");
                        return;
                    }
                    
                    Log.e(TAG, "Failed to get location", e);
                    tvCurrentLocation.setText("Hà Nội");
                    tvDestination.setText("Đà Nẵng");
                    Toast.makeText(getContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                });
    }
    
    /**
     * Chuyển đổi tọa độ thành tên địa phương
     */
    private void getAddressFromLocation(double latitude, double longitude) {
        // Kiểm tra fragment còn attached không
        if (!isAdded() || getContext() == null) {
            Log.w(TAG, "Fragment not attached, skip geocoding");
            return;
        }
        
        Geocoder geocoder = new Geocoder(getContext(), new Locale("vi", "VN"));
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                
                // Lấy tên thành phố/tỉnh
                String city = address.getAdminArea(); // Tỉnh/Thành phố
                String locality = address.getLocality(); // Quận/Huyện
                
                if (city != null && !city.isEmpty()) {
                    tvCurrentLocation.setText(city);
                } else if (locality != null && !locality.isEmpty()) {
                    tvCurrentLocation.setText(locality);
                } else {
                    tvCurrentLocation.setText("Hà Nội");
                }
                
                Log.d(TAG, "Current location: " + city);
            } else {
                tvCurrentLocation.setText("Hà Nội");
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder error", e);
            tvCurrentLocation.setText("Hà Nội");
        }
    }
    
    /**
     * Tính khoảng cách giữa 2 điểm (đơn giản hóa bằng công thức Euclidean)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Sử dụng công thức Haversine để tính khoảng cách chính xác hơn
        double R = 6371; // Bán kính Trái Đất (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    /**
     * Tìm địa điểm gần nhất trong 4 địa điểm
     */
    private String findNearestDestination(double currentLat, double currentLon) {
        double minDistance = Double.MAX_VALUE;
        int nearestIndex = 0;
        
        for (int i = 0; i < DESTINATION_COORDS.length; i++) {
            double distance = calculateDistance(
                currentLat, currentLon,
                DESTINATION_COORDS[i][0], DESTINATION_COORDS[i][1]
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                nearestIndex = i;
            }
        }
        
        return DESTINATIONS[nearestIndex];
    }
    
    /**
     * Hiển thị ngày hiện tại
     */
    private void setCurrentDate() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        String dayOfWeek;
        switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.SUNDAY: dayOfWeek = "CN"; break;
            case java.util.Calendar.MONDAY: dayOfWeek = "T2"; break;
            case java.util.Calendar.TUESDAY: dayOfWeek = "T3"; break;
            case java.util.Calendar.WEDNESDAY: dayOfWeek = "T4"; break;
            case java.util.Calendar.THURSDAY: dayOfWeek = "T5"; break;
            case java.util.Calendar.FRIDAY: dayOfWeek = "T6"; break;
            case java.util.Calendar.SATURDAY: dayOfWeek = "T7"; break;
            default: dayOfWeek = "";
        }
        
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        int month = calendar.get(java.util.Calendar.MONTH) + 1;
        int year = calendar.get(java.util.Calendar.YEAR);
        
        String currentDate = String.format(Locale.getDefault(), "%s, %02d/%02d/%d", dayOfWeek, day, month, year);
        tvDate.setText(currentDate);
    }
    
    /**
     * Hiển thị dialog chọn địa điểm
     */
    private void showDestinationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn địa điểm học");
        
        // Tìm địa điểm gần nhất
        String nearestDestination = "";
        if (currentLatitude != 0 && currentLongitude != 0) {
            nearestDestination = findNearestDestination(currentLatitude, currentLongitude);
        }
        
        // Tạo danh sách với gợi ý
        String[] displayItems = new String[DESTINATIONS.length];
        for (int i = 0; i < DESTINATIONS.length; i++) {
            if (DESTINATIONS[i].equals(nearestDestination)) {
                displayItems[i] = DESTINATIONS[i] + " (Gần bạn nhất)";
            } else {
                displayItems[i] = DESTINATIONS[i];
            }
        }
        
        builder.setItems(displayItems, (dialog, which) -> {
            tvDestination.setText(DESTINATIONS[which]);
            Toast.makeText(requireContext(), "Đã chọn: " + DESTINATIONS[which], Toast.LENGTH_SHORT).show();
        });
        
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        
        builder.create().show();
    }
}
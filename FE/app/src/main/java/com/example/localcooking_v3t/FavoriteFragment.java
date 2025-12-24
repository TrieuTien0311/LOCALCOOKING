package com.example.localcooking_v3t;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.model.LichTrinhLopHoc;
import com.example.localcooking_v3t.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {
    private static final String TAG = "FavoriteFragment";

    private RecyclerView recyclerViewFavorite;
    private ClassAdapter adapter;
    private LinearLayout txtEmpty;
    private LinearLayout dateContainer;
    private CardView btnChonNgay;
    private int selectedPosition = 0;
    private String selectedDate; // Format: "2025-12-21"
    private Integer maHocVien;
    
    private List<KhoaHoc> allFavorites = new ArrayList<>(); // Tất cả khóa học yêu thích
    private Map<String, List<KhoaHoc>> favoritesByDate = new HashMap<>(); // Nhóm theo ngày

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView started");
        
        try {
            View view = inflater.inflate(R.layout.fragment_favorite, container, false);

            recyclerViewFavorite = view.findViewById(R.id.recyclerViewFavorite);
            txtEmpty = view.findViewById(R.id.txtEmpty);
            dateContainer = view.findViewById(R.id.dateContainer);
            btnChonNgay = view.findViewById(R.id.btnChonNgay);

            recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getContext()));

            // Lấy mã học viên từ session
            SessionManager sessionManager = new SessionManager(requireContext());
            maHocVien = sessionManager.getMaNguoiDung();
            
            Log.d(TAG, "MaHocVien: " + maHocVien);
            
            if (maHocVien == null || maHocVien == -1) {
                Log.w(TAG, "User not logged in");
                Toast.makeText(requireContext(), "Vui lòng đăng nhập để xem yêu thích", Toast.LENGTH_SHORT).show();
                showEmptyState();
                return view;
            }

            // Set ngày hiện tại
            Calendar today = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(today.getTime());
            
            Log.d(TAG, "Selected date: " + selectedDate);

            setupDatePickerBar();
            setupButtonChonNgay();
            loadFavorites();
            
            return view;
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            Toast.makeText(requireContext(), "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return inflater.inflate(R.layout.fragment_favorite, container, false);
        }
    }
    
    /**
     * Setup nút "Chọn ngày" để mở DatePicker
     */
    private void setupButtonChonNgay() {
        btnChonNgay.setOnClickListener(v -> showDatePicker());
    }
    
    /**
     * Hiển thị DatePicker giống HomeFragment
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            (view, year, month, dayOfMonth) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, dayOfMonth);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = sdf.format(selected.getTime());
                
                // Cập nhật date picker bar
                updateDatePickerBar(selected);
                
                // Load lại dữ liệu theo ngày mới
                filterFavoritesByDate(selectedDate);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Chỉ cho phép chọn từ hôm nay trở đi
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        
        datePickerDialog.show();
    }
    
    /**
     * Cập nhật date picker bar khi chọn ngày từ calendar
     */
    private void updateDatePickerBar(Calendar selectedCalendar) {
        // Tính khoảng cách từ hôm nay đến ngày được chọn
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        
        Calendar selected = (Calendar) selectedCalendar.clone();
        selected.set(Calendar.HOUR_OF_DAY, 0);
        selected.set(Calendar.MINUTE, 0);
        selected.set(Calendar.SECOND, 0);
        selected.set(Calendar.MILLISECOND, 0);
        
        long diffInMillis = selected.getTimeInMillis() - today.getTimeInMillis();
        int daysDiff = (int) (diffInMillis / (1000 * 60 * 60 * 24));
        
        if (daysDiff >= 0 && daysDiff < 30) {
            selectedPosition = daysDiff;
            
            // Cập nhật UI của date picker bar
            for (int j = 0; j < dateContainer.getChildCount(); j++) {
                View child = dateContainer.getChildAt(j);
                TextView tTitle = child.findViewById(R.id.txtDayTitle);
                TextView tDate = child.findViewById(R.id.txtDate);
                View tGach = child.findViewById(R.id.gachChan);
                updateDateItemAppearance(tTitle, tDate, tGach, j == selectedPosition);
            }
            
            // Cuộn đến vị trí
            scrollToPosition(selectedPosition);
        }
    }
    
    /**
     * Load danh sách khóa học yêu thích từ API
     */
    private void loadFavorites() {
        Log.d(TAG, "Loading favorites for maHocVien: " + maHocVien);
        
        try {
            RetrofitClient.getApiService().getFavoritesByHocVien(maHocVien)
                .enqueue(new Callback<List<KhoaHoc>>() {
                    @Override
                    public void onResponse(Call<List<KhoaHoc>> call, Response<List<KhoaHoc>> response) {
                        try {
                            Log.d(TAG, "Response code: " + response.code());
                            Log.d(TAG, "Response message: " + response.message());
                            
                            if (response.isSuccessful() && response.body() != null) {
                                allFavorites = response.body();
                                Log.d(TAG, "Loaded " + allFavorites.size() + " favorites");
                                
                                if (allFavorites.isEmpty()) {
                                    Log.d(TAG, "No favorites found");
                                    showEmptyState();
                                    return;
                                }
                                
                                // Nhóm theo ngày
                                groupFavoritesByDate();
                                
                                // Hiển thị theo ngày hiện tại
                                filterFavoritesByDate(selectedDate);
                            } else {
                                Log.e(TAG, "Failed to load favorites: " + response.code() + " - " + response.message());
                                if (response.errorBody() != null) {
                                    try {
                                        String errorBody = response.errorBody().string();
                                        Log.e(TAG, "Error body: " + errorBody);
                                    } catch (Exception e) {
                                        Log.e(TAG, "Cannot read error body", e);
                                    }
                                }
                                Toast.makeText(requireContext(), "Không thể tải danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                showEmptyState();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing response: " + e.getMessage(), e);
                            Toast.makeText(requireContext(), "Lỗi xử lý dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            showEmptyState();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<KhoaHoc>> call, Throwable t) {
                        Log.e(TAG, "Error loading favorites: " + t.getMessage(), t);
                        Log.e(TAG, "Error class: " + t.getClass().getName());
                        
                        String errorMessage;
                        if (t instanceof java.net.SocketTimeoutException) {
                            errorMessage = "Timeout - Backend không phản hồi. Kiểm tra backend có đang chạy không?";
                        } else if (t instanceof java.net.ConnectException) {
                            errorMessage = "Không thể kết nối tới backend. Kiểm tra IP: " + RetrofitClient.getBaseUrl();
                        } else if (t instanceof com.google.gson.JsonSyntaxException) {
                            errorMessage = "Backend trả về dữ liệu không đúng format JSON";
                        } else {
                            errorMessage = "Lỗi kết nối: " + t.getMessage();
                        }
                        
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                        showEmptyState();
                    }
                });
        } catch (Exception e) {
            Log.e(TAG, "Error calling API: " + e.getMessage(), e);
            Toast.makeText(requireContext(), "Lỗi gọi API: " + e.getMessage(), Toast.LENGTH_LONG).show();
            showEmptyState();
        }
    }
    
    /**
     * Nhóm các khóa học yêu thích theo ngày có lịch trình
     */
    private void groupFavoritesByDate() {
        favoritesByDate.clear();
        
        Log.d(TAG, "Grouping " + allFavorites.size() + " favorites by date");
        
        try {
            for (KhoaHoc khoaHoc : allFavorites) {
                if (khoaHoc == null) {
                    Log.w(TAG, "Null KhoaHoc found, skipping");
                    continue;
                }
                
                List<LichTrinhLopHoc> lichTrinhList = khoaHoc.getLichTrinhList();
                
                Log.d(TAG, "KhoaHoc: " + khoaHoc.getTenKhoaHoc());
                Log.d(TAG, "LichTrinhList: " + (lichTrinhList != null ? lichTrinhList.size() : "null"));
                
                if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
                    // Lấy thuTrongTuan từ lịch trình đầu tiên
                    String thuTrongTuan = lichTrinhList.get(0).getThuTrongTuan();
                    Log.d(TAG, "ThuTrongTuan: " + thuTrongTuan);
                    
                    if (thuTrongTuan == null || thuTrongTuan.isEmpty()) {
                        Log.w(TAG, "Empty thuTrongTuan for: " + khoaHoc.getTenKhoaHoc());
                        continue;
                    }
                    
                    // Tạo danh sách ngày trong 30 ngày tới mà khóa học có lịch
                    Calendar calendar = Calendar.getInstance();
                    for (int i = 0; i < 30; i++) {
                        if (isDateMatchSchedule(calendar, thuTrongTuan)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String dateKey = sdf.format(calendar.getTime());
                            
                            if (!favoritesByDate.containsKey(dateKey)) {
                                favoritesByDate.put(dateKey, new ArrayList<>());
                            }
                            favoritesByDate.get(dateKey).add(khoaHoc);
                            
                            Log.d(TAG, "Added to date: " + dateKey);
                        }
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                } else {
                    Log.w(TAG, "No LichTrinh for: " + khoaHoc.getTenKhoaHoc());
                }
            }
            
            Log.d(TAG, "Total dates with favorites: " + favoritesByDate.size());
        } catch (Exception e) {
            Log.e(TAG, "Error grouping favorites: " + e.getMessage(), e);
        }
    }
    
    /**
     * Kiểm tra ngày có khớp với lịch trình không
     */
    private boolean isDateMatchSchedule(Calendar calendar, String thuTrongTuan) {
        if (thuTrongTuan == null || thuTrongTuan.isEmpty()) return false;
        
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Chủ Nhật = 1, Thứ 2 = 2, ...
        if (dayOfWeek == 1) {
            return thuTrongTuan.contains("CN") || thuTrongTuan.contains("1");
        } else {
            return thuTrongTuan.contains(String.valueOf(dayOfWeek));
        }
    }
    
    /**
     * Lọc và hiển thị khóa học yêu thích theo ngày
     */
    private void filterFavoritesByDate(String date) {
        Log.d(TAG, "Filtering favorites for date: " + date);
        
        List<KhoaHoc> filteredList = favoritesByDate.get(date);
        
        Log.d(TAG, "Found " + (filteredList != null ? filteredList.size() : 0) + " favorites for this date");
        
        if (filteredList == null || filteredList.isEmpty()) {
            showEmptyState();
        } else {
            showFavoriteList(filteredList);
        }
    }
    
    /**
     * Hiển thị danh sách yêu thích
     */
    private void showFavoriteList(List<KhoaHoc> list) {
        txtEmpty.setVisibility(View.GONE);
        recyclerViewFavorite.setVisibility(View.VISIBLE);

        // Set tất cả khóa học là đã yêu thích (vì đây là màn hình yêu thích)
        for (KhoaHoc khoaHoc : list) {
            khoaHoc.setIsFavorite(true);
            
            // Kiểm tra xem lớp đã diễn ra chưa (15 phút trước giờ bắt đầu)
            boolean daDienRa = checkIfClassHasStarted(khoaHoc);
            khoaHoc.setDaDienRa(daDienRa);
        }

        // Format selectedDate từ "yyyy-MM-dd" sang "dd/MM/yyyy" để hiển thị
        String formattedDate = formatDateForDisplay(selectedDate);
        
        adapter = new ClassAdapter(list, formattedDate);
        recyclerViewFavorite.setAdapter(adapter);

        adapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onDatLichClick(KhoaHoc lopHoc) {
                // Chuyển sang màn hình đặt lịch (Booking)
                navigateToBooking(lopHoc);
            }

            @Override
            public void onChiTietClick(KhoaHoc lopHoc) {
                DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc, formattedDate);
                bottomSheet.show(getChildFragmentManager(), "DetailBottomSheet");
            }

            @Override
            public void onFavoriteClick(KhoaHoc lopHoc) {
                toggleFavorite(lopHoc);
            }
        });
        
        // Load số suất thực tế cho từng khóa học
        loadActualSlots(list);
    }
    
    /**
     * Load số suất thực tế cho từng khóa học bằng cách gọi API search
     */
    private void loadActualSlots(List<KhoaHoc> list) {
        for (KhoaHoc khoaHoc : list) {
            // Lấy địa điểm từ lịch trình
            String diaDiem = khoaHoc.getDiaDiem();
            if (diaDiem == null || diaDiem.isEmpty()) {
                continue;
            }
            
            // Gọi API search để lấy số suất cho ngày này
            RetrofitClient.getApiService().searchKhoaHoc(diaDiem, selectedDate)
                .enqueue(new Callback<List<KhoaHoc>>() {
                    @Override
                    public void onResponse(Call<List<KhoaHoc>> call, Response<List<KhoaHoc>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Tìm khóa học tương ứng trong response
                            for (KhoaHoc searchResult : response.body()) {
                                if (searchResult.getMaKhoaHoc().equals(khoaHoc.getMaKhoaHoc())) {
                                    // Cập nhật số suất
                                    Integer suat = searchResult.getSuat();
                                    if (suat != null && khoaHoc.getLichTrinhList() != null && !khoaHoc.getLichTrinhList().isEmpty()) {
                                        khoaHoc.getLichTrinhList().get(0).setSoChoConTrong(suat);
                                        adapter.notifyDataSetChanged();
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<KhoaHoc>> call, Throwable t) {
                        Log.e(TAG, "Error loading slots for course " + khoaHoc.getMaKhoaHoc(), t);
                    }
                });
        }
    }
    
    /**
     * Hiển thị trạng thái rỗng
     */
    private void showEmptyState() {
        txtEmpty.setVisibility(View.VISIBLE);
        recyclerViewFavorite.setVisibility(View.GONE);
    }
    
    /**
     * Toggle yêu thích
     */
    private void toggleFavorite(KhoaHoc khoaHoc) {
        Map<String, Integer> request = new HashMap<>();
        request.put("maHocVien", maHocVien);
        request.put("maKhoaHoc", khoaHoc.getMaKhoaHoc());
        
        RetrofitClient.getApiService().toggleFavorite(request)
            .enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Boolean isFavorite = (Boolean) response.body().get("isFavorite");
                        String message = (String) response.body().get("message");
                        
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        
                        // Reload lại danh sách
                        loadFavorites();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
    }
    
    /**
     * Chuyển sang màn hình đặt lịch (Booking)
     */
    private void navigateToBooking(KhoaHoc khoaHoc) {
        if (khoaHoc == null) {
            Toast.makeText(requireContext(), "Không có thông tin khóa học", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent intent = new Intent(requireContext(), Booking.class);
        
        // Truyền thông tin khóa học
        intent.putExtra("maKhoaHoc", khoaHoc.getMaKhoaHoc());
        intent.putExtra("tenKhoaHoc", khoaHoc.getTenLop());
        intent.putExtra("moTa", khoaHoc.getMoTa());
        intent.putExtra("giaTien", khoaHoc.getGia());
        intent.putExtra("hinhAnh", khoaHoc.getHinhAnh());
        
        // Truyền ngày được chọn (format dd/MM/yyyy)
        String formattedDate = formatDateForDisplay(selectedDate);
        intent.putExtra("ngayThamGia", selectedDate); // yyyy-MM-dd cho API
        intent.putExtra("ngayHienThi", formattedDate); // dd/MM/yyyy cho hiển thị
        
        // Truyền thông tin lịch trình nếu có
        List<LichTrinhLopHoc> lichTrinhList = khoaHoc.getLichTrinhList();
        if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
            LichTrinhLopHoc lichTrinh = lichTrinhList.get(0);
            intent.putExtra("maLichTrinh", lichTrinh.getMaLichTrinh());
            intent.putExtra("thoiGian", lichTrinh.getThoiGianFormatted());
            intent.putExtra("diaDiem", lichTrinh.getDiaDiem());
            
            if (lichTrinh.getSoChoConTrong() != null) {
                intent.putExtra("soChoConTrong", lichTrinh.getSoChoConTrong());
            }
        }
        
        // Truyền thông tin ưu đãi nếu có
        if (khoaHoc.getCoUuDai() != null && khoaHoc.getCoUuDai()) {
            intent.putExtra("coUuDai", true);
            if (khoaHoc.getPhanTramGiam() != null) {
                intent.putExtra("phanTramGiam", khoaHoc.getPhanTramGiam());
            }
            if (khoaHoc.getGiaSauGiam() != null) {
                intent.putExtra("giaSauGiam", khoaHoc.getGiaSauGiam());
            }
        }
        
        Log.d(TAG, "Navigate to Booking: " + khoaHoc.getTenKhoaHoc() + ", ngay: " + selectedDate);
        
        startActivity(intent);
    }
    
    /**
     * Kiểm tra xem lớp học đã bắt đầu chưa (15 phút trước giờ học)
     * Chỉ kiểm tra nếu ngày được chọn là hôm nay
     */
    private boolean checkIfClassHasStarted(KhoaHoc khoaHoc) {
        try {
            // Lấy ngày hiện tại
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String todayDate = sdf.format(now.getTime());
            
            // Nếu ngày được chọn không phải hôm nay, chưa diễn ra
            if (!selectedDate.equals(todayDate)) {
                return false;
            }
            
            // Lấy thời gian hiện tại
            int currentHour = now.get(Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(Calendar.MINUTE);
            int currentTotalMinutes = currentHour * 60 + currentMinute;
            
            // Lấy thời gian bắt đầu của lớp
            List<LichTrinhLopHoc> lichTrinhList = khoaHoc.getLichTrinhList();
            if (lichTrinhList != null && !lichTrinhList.isEmpty()) {
                String thoiGian = lichTrinhList.get(0).getThoiGianFormatted();
                if (thoiGian != null && !thoiGian.isEmpty()) {
                    // Parse "14:00 - 17:00" -> lấy "14:00"
                    String[] parts = thoiGian.split(" - ");
                    if (parts.length > 0) {
                        String[] timeParts = parts[0].trim().split(":");
                        if (timeParts.length == 2) {
                            int classHour = Integer.parseInt(timeParts[0]);
                            int classMinute = Integer.parseInt(timeParts[1]);
                            int classTotalMinutes = classHour * 60 + classMinute;
                            
                            // Đã diễn ra nếu thời gian hiện tại >= (giờ bắt đầu - 15 phút)
                            int classStartMinus15 = classTotalMinutes - 15;
                            return currentTotalMinutes >= classStartMinus15;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking if class has started", e);
        }
        
        return false;
    }
    
    /**
     * Format ngày từ "yyyy-MM-dd" sang "dd/MM/yyyy"
     */
    private String formatDateForDisplay(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }
        
        try {
            // Parse từ "yyyy-MM-dd"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            
            java.util.Date date = inputFormat.parse(dateStr);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error formatting date", e);
        }
        
        return dateStr;
    }

    private void setupDatePickerBar() {
        dateContainer.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < 30; i++) {
            Calendar currentDay = (Calendar) calendar.clone();
            final String dateKey = fullDateFormat.format(currentDay.getTime());

            String title;
            if (i == 0) title = "Hôm nay";
            else if (i == 1) title = "Ngày mai";
            else {
                String dayName = dayFormat.format(currentDay.getTime());
                title = dayName
                        .replace("Thứ Hai", "Thứ 2")
                        .replace("Thứ Ba", "Thứ 3")
                        .replace("Thứ Tư", "Thứ 4")
                        .replace("Thứ Năm", "Thứ 5")
                        .replace("Thứ Sáu", "Thứ 6")
                        .replace("Thứ Bảy", "Thứ 7")
                        .replace("Chủ nhật", "CN");
            }

            String dateStr = dateFormat.format(currentDay.getTime());

            View dateItem = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_date, dateContainer, false);

            TextView txtTitle = dateItem.findViewById(R.id.txtDayTitle);
            TextView txtDate = dateItem.findViewById(R.id.txtDate);
            View gachChan = dateItem.findViewById(R.id.gachChan);

            txtTitle.setText(title);
            txtDate.setText(dateStr);

            updateDateItemAppearance(txtTitle, txtDate, gachChan, i == selectedPosition);

            final int position = i;
            dateItem.setOnClickListener(v -> {
                selectedPosition = position;
                selectedDate = dateKey;

                // Cập nhật UI
                for (int j = 0; j < dateContainer.getChildCount(); j++) {
                    View child = dateContainer.getChildAt(j);
                    TextView tTitle = child.findViewById(R.id.txtDayTitle);
                    TextView tDate = child.findViewById(R.id.txtDate);
                    View tGach = child.findViewById(R.id.gachChan);
                    updateDateItemAppearance(tTitle, tDate, tGach, j == position);
                }

                scrollToPosition(position);
                
                // Lọc lại theo ngày mới
                filterFavoritesByDate(selectedDate);
            });

            dateContainer.addView(dateItem);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        dateContainer.post(() -> {
            HorizontalScrollView hsv = (HorizontalScrollView) dateContainer.getParent();
            hsv.scrollTo(0, 0);
        });
    }
    
    private void scrollToPosition(int position) {
        dateContainer.post(() -> {
            HorizontalScrollView hsv = (HorizontalScrollView) dateContainer.getParent();
            View targetItem = dateContainer.getChildAt(position);
            if (targetItem == null) return;

            int itemWidth = targetItem.getWidth();
            int hsvWidth = hsv.getWidth();
            int itemCenterX = targetItem.getLeft() + itemWidth / 2;
            int screenCenterX = hsvWidth / 2;
            int desiredScrollX = itemCenterX - screenCenterX + 10;
            int maxScrollX = dateContainer.getMeasuredWidth() - hsvWidth;
            int finalScrollX = Math.max(0, Math.min(desiredScrollX, maxScrollX));

            hsv.smoothScrollTo(finalScrollX, 0);
        });
    }

    private void updateDateItemAppearance(TextView title, TextView date, View gachChan, boolean isSelected) {
        if (isSelected) {
            title.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_dark));
            date.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_dark));
            gachChan.setVisibility(View.VISIBLE);
        } else {
            title.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text));
            date.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text));
            gachChan.setVisibility(View.INVISIBLE);
        }
    }
}
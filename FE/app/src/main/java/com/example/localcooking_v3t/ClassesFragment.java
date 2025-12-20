package com.example.localcooking_v3t;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassesFragment extends Fragment {
    private static final String TAG = "ClassesFragment";
    
    private RecyclerView recyclerViewLopHoc;
    private ClassAdapter classAdapter;
    private List<KhoaHoc> danhSachLopHoc = new ArrayList<>();
    private List<KhoaHoc> danhSachGoc = new ArrayList<>(); // Lưu danh sách gốc để lọc
    
    private String destination;
    private String date;
    
    private View btnSapXep, btnThoiGian, btnGiaCa;
    private Integer maHocVien;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);
        
        // Lấy mã học viên từ session
        SessionManager sessionManager = new SessionManager(requireContext());
        maHocVien = sessionManager.getMaNguoiDung();
        
        // Nhận dữ liệu từ Activity
        if (getActivity() != null && getActivity() instanceof Classes) {
            Classes activity = (Classes) getActivity();
            destination = activity.getIntent().getStringExtra("destination");
            date = activity.getIntent().getStringExtra("date");
        }
        
        // Ánh xạ views
        recyclerViewLopHoc = view.findViewById(R.id.recyclerViewLopHoc);
        btnSapXep = view.findViewById(R.id.btnSapXep);
        btnThoiGian = view.findViewById(R.id.btnThoiGian);
        btnGiaCa = view.findViewById(R.id.btnGiaCa);
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup filter buttons
        setupFilterButtons();
        
        // Load dữ liệu
        loadLopHoc();
        
        return view;
    }
    
    private void setupRecyclerView() {
        recyclerViewLopHoc.setLayoutManager(new LinearLayoutManager(requireContext()));
        classAdapter = new ClassAdapter(danhSachLopHoc, date);
        recyclerViewLopHoc.setAdapter(classAdapter);
        
        // Xử lý sự kiện click
        classAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onDatLichClick(KhoaHoc lopHoc) {
                Toast.makeText(requireContext(), "Đặt lịch: " + lopHoc.getTenLop(), Toast.LENGTH_SHORT).show();
                // TODO: Chuyển sang màn hình đặt lịch
            }

            @Override
            public void onChiTietClick(KhoaHoc lopHoc) {
                // Mở bottom sheet chi tiết với ngày được chọn
                DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc, date);
                bottomSheet.show(getChildFragmentManager(), "DetailBottomSheet");
            }

            @Override
            public void onFavoriteClick(KhoaHoc lopHoc) {
                toggleFavorite(lopHoc);
            }
        });
    }
    
    private void loadLopHoc() {
        // Chuyển đổi date từ format "T2, 20/12/2024" sang "2024-12-20"
        String ngayTimKiem = convertDateFormat(date);
        
        Log.d(TAG, "Loading classes for: " + destination + " on " + ngayTimKiem);
        
        if (ngayTimKiem != null && !ngayTimKiem.isEmpty()) {
            // Gọi API mới với địa điểm và ngày (sử dụng stored procedure)
            RetrofitClient.getApiService().searchKhoaHoc(destination, ngayTimKiem)
                .enqueue(new Callback<List<KhoaHoc>>() {
                    @Override
                    public void onResponse(Call<List<KhoaHoc>> call, Response<List<KhoaHoc>> response) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Call<List<KhoaHoc>> call, Throwable t) {
                        handleError(t);
                    }
                });
        } else {
            // Nếu không có ngày, chỉ lọc theo địa điểm
            RetrofitClient.getApiService().searchKhoaHoc(destination, null)
                .enqueue(new Callback<List<KhoaHoc>>() {
                    @Override
                    public void onResponse(Call<List<KhoaHoc>> call, Response<List<KhoaHoc>> response) {
                        handleResponse(response);
                    }

                    @Override
                    public void onFailure(Call<List<KhoaHoc>> call, Throwable t) {
                        handleError(t);
                    }
                });
        }
    }
    
    private void handleResponse(Response<List<KhoaHoc>> response) {
        if (response.isSuccessful() && response.body() != null) {
            List<KhoaHoc> filteredClasses = response.body();
            
            // Lọc các lớp học chưa bắt đầu (trước 15 phút)
            List<KhoaHoc> availableClasses = filterUpcomingClasses(filteredClasses);
            
            // Đặt tất cả lớp học là chưa diễn ra (không hiển thị overlay)
            for (KhoaHoc khoaHoc : availableClasses) {
                khoaHoc.setDaDienRa(false);
            }
            
            // Lưu danh sách gốc để lọc sau này
            danhSachGoc.clear();
            danhSachGoc.addAll(availableClasses);
            
            // Cập nhật dữ liệu
            danhSachLopHoc.clear();
            danhSachLopHoc.addAll(availableClasses);
            
            // Load trạng thái yêu thích cho từng khóa học
            loadFavoriteStatus();
            
            classAdapter.updateData(danhSachLopHoc);
            
            Log.d(TAG, "Loaded " + danhSachLopHoc.size() + " classes for " + destination + " on " + date);
        } else {
            Log.e(TAG, "Failed to load classes: " + response.code());
            Toast.makeText(requireContext(), "Không thể tải danh sách lớp học", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handleError(Throwable t) {
        Log.e(TAG, "Error loading classes", t);
        Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Chuyển đổi format ngày từ "T2, 20/12/2024" sang "2024-12-20"
     */
    private String convertDateFormat(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        try {
            // Tách phần ngày (bỏ thứ)
            String[] parts = dateStr.split(", ");
            if (parts.length < 2) return null;
            
            String[] dateParts = parts[1].split("/");
            if (dateParts.length != 3) return null;
            
            String day = dateParts[0];
            String month = dateParts[1];
            String year = dateParts[2];
            
            // Format: YYYY-MM-DD
            return String.format("%s-%s-%s", year, 
                month.length() == 1 ? "0" + month : month,
                day.length() == 1 ? "0" + day : day);
        } catch (Exception e) {
            Log.e(TAG, "Error converting date format", e);
            return null;
        }
    }
    
    /**
     * Lọc các lớp học chưa bắt đầu (trước 15 phút)
     * Chỉ hiển thị lớp nếu thời gian hiện tại chưa đến giờ bắt đầu - 15 phút
     */
    private List<KhoaHoc> filterUpcomingClasses(List<KhoaHoc> classes) {
        List<KhoaHoc> upcomingClasses = new ArrayList<>();
        
        // Lấy thời gian hiện tại
        java.util.Calendar now = java.util.Calendar.getInstance();
        int currentHour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(java.util.Calendar.MINUTE);
        int currentTotalMinutes = currentHour * 60 + currentMinute;
        
        // Lấy ngày hiện tại để so sánh
        String todayDate = String.format("%04d-%02d-%02d", 
            now.get(java.util.Calendar.YEAR),
            now.get(java.util.Calendar.MONTH) + 1,
            now.get(java.util.Calendar.DAY_OF_MONTH));
        
        String selectedDate = convertDateFormat(date);
        
        Log.d(TAG, "Current time: " + currentHour + ":" + currentMinute);
        Log.d(TAG, "Today: " + todayDate + ", Selected: " + selectedDate);
        
        for (KhoaHoc lopHoc : classes) {
            // Nếu ngày được chọn không phải hôm nay, hiển thị tất cả
            if (selectedDate != null && !selectedDate.equals(todayDate)) {
                upcomingClasses.add(lopHoc);
                continue;
            }
            
            // Nếu là hôm nay, kiểm tra thời gian
            if (lopHoc.getThoiGian() != null) {
                String[] times = lopHoc.getThoiGian().split(" - ");
                if (times.length > 0) {
                    try {
                        String[] startParts = times[0].trim().split(":");
                        if (startParts.length == 2) {
                            int classHour = Integer.parseInt(startParts[0]);
                            int classMinute = Integer.parseInt(startParts[1]);
                            int classTotalMinutes = classHour * 60 + classMinute;
                            
                            // Chỉ hiển thị nếu thời gian hiện tại < (giờ bắt đầu - 15 phút)
                            // Ví dụ: Lớp 14:00, chỉ hiển thị khi < 13:45
                            int classStartMinus15 = classTotalMinutes - 15;
                            
                            if (currentTotalMinutes < classStartMinus15) {
                                upcomingClasses.add(lopHoc);
                                Log.d(TAG, "Added class: " + lopHoc.getTenLop() + " at " + lopHoc.getThoiGian());
                            } else {
                                Log.d(TAG, "Filtered out class: " + lopHoc.getTenLop() + " at " + lopHoc.getThoiGian() + 
                                    " (current: " + currentTotalMinutes + ", start-15: " + classStartMinus15 + ")");
                            }
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing time for class: " + lopHoc.getTenLop(), e);
                        // Nếu không parse được thời gian, vẫn thêm vào để tránh mất dữ liệu
                        upcomingClasses.add(lopHoc);
                    }
                }
            } else {
                // Nếu không có thời gian, vẫn thêm vào
                upcomingClasses.add(lopHoc);
            }
        }
        
        Log.d(TAG, "Filtered: " + upcomingClasses.size() + " out of " + classes.size() + " classes");
        return upcomingClasses;
    }
    

    
    /**
     * Thiết lập các nút lọc
     */
    private void setupFilterButtons() {
        // Nút Sắp xếp
        btnSapXep.setOnClickListener(v -> {
            ArrangeBottomSheet bottomSheet = ArrangeBottomSheet.newInstance(0);
            bottomSheet.setOnSapXepListener(loaiSapXep -> {
                sapXepDanhSach(loaiSapXep);
            });
            bottomSheet.show(getChildFragmentManager(), "ArrangeBottomSheet");
        });
        
        // Nút Thời gian
        btnThoiGian.setOnClickListener(v -> {
            TimeBottomSheet bottomSheet = new TimeBottomSheet();
            bottomSheet.setOnFilterAppliedListener((startHour, startMinute, endHour, endMinute, sortType) -> {
                locTheoThoiGian(startHour, startMinute, endHour, endMinute, sortType);
            });
            bottomSheet.show(getChildFragmentManager(), "TimeBottomSheet");
        });
        
        // Nút Giá cả
        btnGiaCa.setOnClickListener(v -> {
            CostBottomSheet bottomSheet = new CostBottomSheet();
            bottomSheet.setOnFilterAppliedListener((minCost, maxCost, sortType) -> {
                locTheoGia(minCost, maxCost, sortType);
            });
            bottomSheet.show(getChildFragmentManager(), "CostBottomSheet");
        });
    }
    
    /**
     * Sắp xếp danh sách theo loại
     */
    private void sapXepDanhSach(int loaiSapXep) {
        List<KhoaHoc> sortedList = new ArrayList<>(danhSachLopHoc);
        
        switch (loaiSapXep) {
            case ArrangeBottomSheet.GIO_BAT_DAU_SOM_NHAT:
                // Sắp xếp theo giờ bắt đầu sớm nhất
                sortedList.sort((a, b) -> {
                    String timeA = a.getThoiGian() != null ? a.getThoiGian().split(" - ")[0] : "00:00";
                    String timeB = b.getThoiGian() != null ? b.getThoiGian().split(" - ")[0] : "00:00";
                    return timeA.compareTo(timeB);
                });
                break;
                
            case ArrangeBottomSheet.GIO_BAT_DAU_MUON_NHAT:
                // Sắp xếp theo giờ bắt đầu muộn nhất
                sortedList.sort((a, b) -> {
                    String timeA = a.getThoiGian() != null ? a.getThoiGian().split(" - ")[0] : "00:00";
                    String timeB = b.getThoiGian() != null ? b.getThoiGian().split(" - ")[0] : "00:00";
                    return timeB.compareTo(timeA);
                });
                break;
                
            case ArrangeBottomSheet.DANH_GIA_CAO_NHAT:
                // Sắp xếp theo đánh giá cao nhất
                sortedList.sort((a, b) -> {
                    Float ratingA = a.getDanhGia() != null ? a.getDanhGia() : 0f;
                    Float ratingB = b.getDanhGia() != null ? b.getDanhGia() : 0f;
                    return Float.compare(ratingB, ratingA);
                });
                break;
                
            case ArrangeBottomSheet.GIA_GIAM_DAN:
                // Sắp xếp theo giá giảm dần
                sortedList.sort((a, b) -> {
                    Double priceA = a.getGiaTien() != null ? a.getGiaTien() : 0.0;
                    Double priceB = b.getGiaTien() != null ? b.getGiaTien() : 0.0;
                    return Double.compare(priceB, priceA);
                });
                break;
                
            case ArrangeBottomSheet.GIA_TANG_DAN:
                // Sắp xếp theo giá tăng dần
                sortedList.sort((a, b) -> {
                    Double priceA = a.getGiaTien() != null ? a.getGiaTien() : 0.0;
                    Double priceB = b.getGiaTien() != null ? b.getGiaTien() : 0.0;
                    return Double.compare(priceA, priceB);
                });
                break;
                
            default:
                // Mặc định - không sắp xếp
                break;
        }
        
        danhSachLopHoc.clear();
        danhSachLopHoc.addAll(sortedList);
        classAdapter.updateData(danhSachLopHoc);
        
        Toast.makeText(requireContext(), "Đã áp dụng sắp xếp", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Lọc theo thời gian
     */
    private void locTheoThoiGian(int startHour, int startMinute, int endHour, int endMinute, int sortType) {
        List<KhoaHoc> filtered = new ArrayList<>();
        
        for (KhoaHoc lopHoc : danhSachGoc) {
            if (lopHoc.getThoiGian() != null) {
                String[] times = lopHoc.getThoiGian().split(" - ");
                if (times.length > 0) {
                    String[] startParts = times[0].split(":");
                    if (startParts.length == 2) {
                        int hour = Integer.parseInt(startParts[0]);
                        int minute = Integer.parseInt(startParts[1]);
                        
                        int lopHocMinutes = hour * 60 + minute;
                        int filterStartMinutes = startHour * 60 + startMinute;
                        int filterEndMinutes = endHour * 60 + endMinute;
                        
                        if (lopHocMinutes >= filterStartMinutes && lopHocMinutes <= filterEndMinutes) {
                            filtered.add(lopHoc);
                        }
                    }
                }
            }
        }
        
        // Áp dụng sắp xếp nếu có
        if (sortType == 1) {
            filtered.sort((a, b) -> {
                String timeA = a.getThoiGian() != null ? a.getThoiGian().split(" - ")[0] : "00:00";
                String timeB = b.getThoiGian() != null ? b.getThoiGian().split(" - ")[0] : "00:00";
                return timeA.compareTo(timeB);
            });
        } else if (sortType == 2) {
            filtered.sort((a, b) -> {
                String timeA = a.getThoiGian() != null ? a.getThoiGian().split(" - ")[0] : "00:00";
                String timeB = b.getThoiGian() != null ? b.getThoiGian().split(" - ")[0] : "00:00";
                return timeB.compareTo(timeA);
            });
        }
        
        danhSachLopHoc.clear();
        danhSachLopHoc.addAll(filtered);
        classAdapter.updateData(danhSachLopHoc);
        
        Toast.makeText(requireContext(), "Đã lọc " + filtered.size() + " lớp học", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Lọc theo giá
     */
    private void locTheoGia(int minCost, int maxCost, int sortType) {
        List<KhoaHoc> filtered = new ArrayList<>();
        
        for (KhoaHoc lopHoc : danhSachGoc) {
            Double gia = lopHoc.getGiaTien();
            if (gia != null && gia >= minCost && gia <= maxCost) {
                filtered.add(lopHoc);
            }
        }
        
        // Áp dụng sắp xếp nếu có
        if (sortType == 1) {
            filtered.sort((a, b) -> {
                Double priceA = a.getGiaTien() != null ? a.getGiaTien() : 0.0;
                Double priceB = b.getGiaTien() != null ? b.getGiaTien() : 0.0;
                return Double.compare(priceB, priceA);
            });
        } else if (sortType == 2) {
            filtered.sort((a, b) -> {
                Double priceA = a.getGiaTien() != null ? a.getGiaTien() : 0.0;
                Double priceB = b.getGiaTien() != null ? b.getGiaTien() : 0.0;
                return Double.compare(priceA, priceB);
            });
        }
        
        danhSachLopHoc.clear();
        danhSachLopHoc.addAll(filtered);
        classAdapter.updateData(danhSachLopHoc);
        
        Toast.makeText(requireContext(), "Đã lọc " + filtered.size() + " lớp học", Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Toggle yêu thích
     */
    private void toggleFavorite(KhoaHoc khoaHoc) {
        if (maHocVien == null || maHocVien == -1) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            return;
        }
        
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
                        
                        // Cập nhật trạng thái yêu thích trong danh sách
                        khoaHoc.setIsFavorite(isFavorite);
                        classAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(requireContext(), "Không thể cập nhật yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.e(TAG, "Error toggling favorite", t);
                    Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
    }
    
    /**
     * Load trạng thái yêu thích cho tất cả khóa học trong danh sách
     */
    private void loadFavoriteStatus() {
        if (maHocVien == null || maHocVien == -1) {
            Log.d(TAG, "User not logged in, skip loading favorite status");
            return;
        }
        
        // Gọi API check favorite cho từng khóa học
        for (KhoaHoc khoaHoc : danhSachLopHoc) {
            RetrofitClient.getApiService().checkFavorite(maHocVien, khoaHoc.getMaKhoaHoc())
                .enqueue(new Callback<Map<String, Boolean>>() {
                    @Override
                    public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Boolean isFavorite = response.body().get("isFavorite");
                            khoaHoc.setIsFavorite(isFavorite != null && isFavorite);
                            classAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                        Log.e(TAG, "Error checking favorite status for course " + khoaHoc.getMaKhoaHoc(), t);
                    }
                });
        }
    }
    
}

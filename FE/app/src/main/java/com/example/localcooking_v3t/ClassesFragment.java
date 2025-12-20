package com.example.localcooking_v3t;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);
        
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
                // Chuyển sang màn hình đặt lịch
                Intent intent = new Intent(requireContext(), Booking.class);
                intent.putExtra("khoaHocId", lopHoc.getMaKhoaHoc());
                intent.putExtra("tenLop", lopHoc.getTenLop());
                intent.putExtra("giaTien", lopHoc.getGiaTien());
                intent.putExtra("diaDiem", lopHoc.getDiaDiem());
                intent.putExtra("thoiGian", lopHoc.getThoiGian());
                intent.putExtra("ngayHoc", date); // Truyền ngày đã chọn
                startActivity(intent);
            }

            @Override
            public void onChiTietClick(KhoaHoc lopHoc) {
                // Mở bottom sheet chi tiết với ngày được chọn
                DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc, date);
                bottomSheet.show(getChildFragmentManager(), "DetailBottomSheet");
            }

            @Override
            public void onFavoriteClick(KhoaHoc lopHoc) {
                Toast.makeText(requireContext(), 
                    (lopHoc.getIsFavorite() != null && lopHoc.getIsFavorite()) ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích", 
                    Toast.LENGTH_SHORT).show();
                // TODO: Gọi API thêm/xóa yêu thích
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
            
            // Đặt tất cả lớp học là chưa diễn ra (không hiển thị overlay)
            for (KhoaHoc khoaHoc : filteredClasses) {
                khoaHoc.setDaDienRa(false);
            }
            
            // Lưu danh sách gốc để lọc sau này
            danhSachGoc.clear();
            danhSachGoc.addAll(filteredClasses);
            
            // Cập nhật dữ liệu
            danhSachLopHoc.clear();
            danhSachLopHoc.addAll(filteredClasses);
            
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
    
}

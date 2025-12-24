package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.DonDatLichDTO;
import com.example.localcooking_v3t.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessfulOrderFragment extends Fragment {

    private static final String TAG = "SuccessfulOrderFragment";
    
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> danhSachLichSuDatLich;
    private TextView txtEmpty;
    private ProgressBar progressBar;
    
    private SessionManager sessionManager;
    private ApiService apiService;

    public SuccessfulOrderFragment() {}

    public static SuccessfulOrderFragment newInstance() {
        return new SuccessfulOrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_successful_order, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotices);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Khởi tạo
        sessionManager = new SessionManager(requireContext());
        apiService = RetrofitClient.getApiService();
        danhSachLichSuDatLich = new ArrayList<>();
        
        // Setup adapter
        adapter = new OrderHistoryAdapter(danhSachLichSuDatLich);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderHistory order) {
                // Chuyển sang trang Bill để xem chi tiết hóa đơn
                goToBill(order);
            }
            
            @Override
            public void onHuyDatClick(OrderHistory lichSuDatLich) {
                // Không cần xử lý nút hủy cho đơn đã hoàn thành
            }

            @Override
            public void onDatLaiClick(OrderHistory lichSuDatLich) {
                // Xử lý chuyển sang trang đặt lại
                Toast.makeText(getContext(), "Đặt lại: " + lichSuDatLich.getTieuDe(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDanhGiaClick(OrderHistory lichSuDatLich) {
                // Xử lý chuyển sang trang đánh giá
                Intent intent = new Intent(getActivity(), Review.class);
                intent.putExtra("orderTitle", lichSuDatLich.getTieuDe());
                intent.putExtra("hinhAnhUrl", lichSuDatLich.getHinhAnhUrl());
                intent.putExtra("maDatLich", lichSuDatLich.getMaDatLich());
                intent.putExtra("maKhoaHoc", lichSuDatLich.getMaKhoaHoc());
                intent.putExtra("lich", lichSuDatLich.getLich());
                intent.putExtra("diaDiem", lichSuDatLich.getDiaDiem());
                intent.putExtra("isViewMode", lichSuDatLich.isDaDanhGia());
                startActivity(intent);
            }
            
            @Override
            public void onThanhToanClick(OrderHistory lichSuDatLich) {
                // Không cần xử lý cho đơn đã hoàn thành
            }
        });
        
        // Load dữ liệu từ API
        loadDonHoanThanh();

        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload khi quay lại (sau khi đánh giá)
        loadDonHoanThanh();
    }
    
    /**
     * Load danh sách đơn đã hoàn thành từ API
     */
    private void loadDonHoanThanh() {
        if (!sessionManager.isLoggedIn()) {
            Log.w(TAG, "User not logged in");
            showEmpty("Vui lòng đăng nhập để xem lịch sử");
            return;
        }
        
        Integer maHocVien = sessionManager.getMaNguoiDung();
        if (maHocVien == null || maHocVien == -1) {
            Log.e(TAG, "Invalid maHocVien");
            return;
        }
        
        showLoading(true);
        Log.d(TAG, "Loading đơn hoàn thành for maHocVien: " + maHocVien);
        
        apiService.getDonHoanThanh(maHocVien).enqueue(new Callback<List<DonDatLichDTO>>() {
            @Override
            public void onResponse(Call<List<DonDatLichDTO>> call, Response<List<DonDatLichDTO>> response) {
                if (!isAdded() || getContext() == null) return;
                
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<DonDatLichDTO> donList = response.body();
                    Log.d(TAG, "Loaded " + donList.size() + " đơn hoàn thành");
                    
                    // Chuyển đổi sang OrderHistory sử dụng constructor mới
                    danhSachLichSuDatLich.clear();
                    for (DonDatLichDTO don : donList) {
                        danhSachLichSuDatLich.add(new OrderHistory(don, getContext()));
                    }
                    
                    adapter.notifyDataSetChanged();
                    
                    // Hiển thị empty state nếu không có dữ liệu
                    if (danhSachLichSuDatLich.isEmpty()) {
                        showEmpty("Chưa có đơn hoàn thành nào");
                    } else {
                        hideEmpty();
                    }
                } else {
                    Log.e(TAG, "Error loading đơn hoàn thành: " + response.code());
                    showEmpty("Không thể tải dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<DonDatLichDTO>> call, Throwable t) {
                if (!isAdded() || getContext() == null) return;
                
                showLoading(false);
                Log.e(TAG, "Failed to load đơn hoàn thành", t);
                showEmpty("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    // Helper methods
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    
    private void showEmpty(String message) {
        if (txtEmpty != null) {
            txtEmpty.setText(message);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.GONE);
        }
    }
    
    private void hideEmpty() {
        if (txtEmpty != null) {
            txtEmpty.setVisibility(View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Chuyển sang trang Bill để xem chi tiết hóa đơn
     */
    private void goToBill(OrderHistory order) {
        Intent intent = new Intent(getActivity(), Bill.class);
        
        // Thông tin thanh toán
        intent.putExtra("tongTienThanhToan", order.getTongTienGoc() != null ? order.getTongTienGoc().doubleValue() : 0);
        intent.putExtra("transId", order.getTransId());
        intent.putExtra("orderId", order.getOrderId());
        intent.putExtra("ngayThanhToan", order.getNgayThanhToan());
        intent.putExtra("trangThai", order.getTrangThai());
        
        // Thông tin lớp học
        intent.putExtra("tenKhoaHoc", order.getTieuDe());
        intent.putExtra("diaDiem", order.getDiaDiem());
        intent.putExtra("thoiGian", order.getThoiGian());
        intent.putExtra("ngayThamGia", order.getNgayThamGia());
        intent.putExtra("hinhAnh", order.getHinhAnhPath());
        intent.putExtra("moTa", order.getMoTa());
        intent.putExtra("soLuongDat", order.getSoLuongNguoiInt());
        
        // Thông tin người đặt
        intent.putExtra("tenNguoiDat", order.getTenNguoiDat());
        intent.putExtra("sdtNguoiDat", order.getSdtNguoiDat());
        
        startActivity(intent);
    }
}

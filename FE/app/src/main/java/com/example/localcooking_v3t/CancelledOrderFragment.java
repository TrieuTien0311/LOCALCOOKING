package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class CancelledOrderFragment extends Fragment {

    private static final String TAG = "CancelledOrderFragment";
    
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> danhSachLichSuDatLich;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    
    private SessionManager sessionManager;
    private ApiService apiService;

    public CancelledOrderFragment() {}

    public static CancelledOrderFragment newInstance() {
        return new CancelledOrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cancelled_order, container, false);

        initViews(view);
        initServices();
        setupRecyclerView();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadDonDaHuy();
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewNotices);
        progressBar = view.findViewById(R.id.progressBar);
        txtEmpty = view.findViewById(R.id.txtEmpty);
    }
    
    private void initServices() {
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        danhSachLichSuDatLich = new ArrayList<>();
        adapter = new OrderHistoryAdapter(danhSachLichSuDatLich);
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderHistory order) {
                // Chuyển sang trang Bill để xem chi tiết hóa đơn (đơn đã hủy)
                goToBill(order);
            }
            
            @Override
            public void onHuyDatClick(OrderHistory order) {
                // Không cần xử lý
            }

            @Override
            public void onDatLaiClick(OrderHistory order) {
                // Không cần xử lý
            }

            @Override
            public void onDanhGiaClick(OrderHistory order) {
                // Không cần xử lý
            }
            
            @Override
            public void onThanhToanClick(OrderHistory order) {
                // Không cần xử lý
            }
        });
    }
    
    /**
     * Load danh sách đơn đã hủy từ API
     */
    private void loadDonDaHuy() {
        Integer maHocVien = sessionManager.getMaNguoiDung();
        if (maHocVien == null || maHocVien == -1) {
            showEmpty("Vui lòng đăng nhập để xem lịch sử");
            return;
        }
        
        showLoading(true);
        
        apiService.getDonDaHuy(maHocVien).enqueue(new Callback<List<DonDatLichDTO>>() {
            @Override
            public void onResponse(Call<List<DonDatLichDTO>> call, Response<List<DonDatLichDTO>> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<DonDatLichDTO> dtoList = response.body();
                    Log.d(TAG, "Loaded " + dtoList.size() + " đơn đã hủy");
                    
                    danhSachLichSuDatLich.clear();
                    for (DonDatLichDTO dto : dtoList) {
                        danhSachLichSuDatLich.add(new OrderHistory(dto, getContext()));
                    }
                    adapter.notifyDataSetChanged();
                    
                    if (danhSachLichSuDatLich.isEmpty()) {
                        showEmpty("Chưa có đơn đã hủy nào");
                    } else {
                        hideEmpty();
                    }
                } else {
                    Log.e(TAG, "API error: " + response.code());
                    showEmpty("Không thể tải dữ liệu");
                }
            }
            
            @Override
            public void onFailure(Call<List<DonDatLichDTO>> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "API failure", t);
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
     * Chuyển sang trang Bill để xem chi tiết hóa đơn (đơn đã hủy)
     */
    private void goToBill(OrderHistory order) {
        Intent intent = new Intent(getActivity(), Bill.class);
        
        // Thông tin thanh toán
        intent.putExtra("tongTienThanhToan", order.getTongTienGoc() != null ? order.getTongTienGoc().doubleValue() : 0);
        intent.putExtra("transId", order.getTransId());
        intent.putExtra("orderId", order.getOrderId());
        intent.putExtra("ngayThanhToan", order.getNgayThanhToan());
        intent.putExtra("trangThai", order.getTrangThai()); // "Đã huỷ"
        intent.putExtra("thoiGianHuy", order.getThoiGianHuy()); // Thời gian hủy
        
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

package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                intent.putExtra("orderImage", lichSuDatLich.getHinhAnh());
                intent.putExtra("maDatLich", lichSuDatLich.getMaDatLich());
                intent.putExtra("maKhoaHoc", lichSuDatLich.getMaKhoaHoc());
                intent.putExtra("lich", lichSuDatLich.getLich());
                intent.putExtra("diaDiem", lichSuDatLich.getDiaDiem());
                startActivity(intent);
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
            return;
        }
        
        Integer maHocVien = sessionManager.getMaNguoiDung();
        if (maHocVien == null || maHocVien == -1) {
            Log.e(TAG, "Invalid maHocVien");
            return;
        }
        
        Log.d(TAG, "Loading đơn hoàn thành for maHocVien: " + maHocVien);
        
        apiService.getDonHoanThanh(maHocVien).enqueue(new Callback<List<DonDatLichDTO>>() {
            @Override
            public void onResponse(Call<List<DonDatLichDTO>> call, Response<List<DonDatLichDTO>> response) {
                if (!isAdded() || getContext() == null) return;
                
                if (response.isSuccessful() && response.body() != null) {
                    List<DonDatLichDTO> donList = response.body();
                    Log.d(TAG, "Loaded " + donList.size() + " đơn hoàn thành");
                    
                    // Chuyển đổi sang OrderHistory
                    danhSachLichSuDatLich.clear();
                    for (DonDatLichDTO don : donList) {
                        OrderHistory order = convertToOrderHistory(don);
                        danhSachLichSuDatLich.add(order);
                    }
                    
                    adapter.notifyDataSetChanged();
                    
                    // Hiển thị empty state nếu không có dữ liệu
                    if (danhSachLichSuDatLich.isEmpty()) {
                        Log.d(TAG, "No completed orders found");
                    }
                } else {
                    Log.e(TAG, "Error loading đơn hoàn thành: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DonDatLichDTO>> call, Throwable t) {
                if (!isAdded() || getContext() == null) return;
                
                Log.e(TAG, "Failed to load đơn hoàn thành", t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Chuyển đổi DonDatLichDTO sang OrderHistory
     */
    private OrderHistory convertToOrderHistory(DonDatLichDTO don) {
        // Lấy resource ID của hình ảnh
        int hinhAnhResId = don.getHinhAnhResId(requireContext());
        
        OrderHistory order = new OrderHistory(
                hinhAnhResId,
                don.getTenKhoaHoc(),
                don.getSoLuongNguoiFormatted(),
                don.getLichFormatted(),
                don.getDiaDiem(),
                don.getTongTienFormatted(),
                don.getTrangThai()
        );
        
        // Set thêm thông tin để dùng khi đánh giá
        order.setMaDatLich(don.getMaDatLich());
        order.setMaKhoaHoc(don.getMaKhoaHoc());
        order.setDaDanhGia(don.getDaDanhGia() != null && don.getDaDanhGia());
        
        return order;
    }
}

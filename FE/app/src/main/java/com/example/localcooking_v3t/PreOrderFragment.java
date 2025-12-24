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
import androidx.appcompat.app.AlertDialog;
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

public class PreOrderFragment extends Fragment {

    private static final String TAG = "PreOrderFragment";
    
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> danhSachLichSuDatLich;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    
    private SessionManager sessionManager;
    private ApiService apiService;

    public PreOrderFragment() {}

    public static PreOrderFragment newInstance() {
        return new PreOrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pre_order, container, false);

        initViews(view);
        initServices();
        setupRecyclerView();
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload data khi quay lại fragment
        loadDonDatTruoc();
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
                // Xử lý click vào item
                handleItemClick(order);
            }
            
            @Override
            public void onHuyDatClick(OrderHistory order) {
                handleHuyDat(order);
            }

            @Override
            public void onDatLaiClick(OrderHistory order) {
                // Không cần xử lý trong PreOrder
            }

            @Override
            public void onDanhGiaClick(OrderHistory order) {
                // Không cần xử lý trong PreOrder
            }
            
            @Override
            public void onThanhToanClick(OrderHistory order) {
                handleThanhToan(order);
            }
        });
    }
    
    /**
     * Load danh sách đơn đặt trước từ API
     */
    private void loadDonDatTruoc() {
        Integer maHocVien = sessionManager.getMaNguoiDung();
        if (maHocVien == null || maHocVien == -1) {
            showEmpty("Vui lòng đăng nhập để xem lịch sử");
            return;
        }
        
        showLoading(true);
        
        apiService.getDonDatTruoc(maHocVien).enqueue(new Callback<List<DonDatLichDTO>>() {
            @Override
            public void onResponse(Call<List<DonDatLichDTO>> call, Response<List<DonDatLichDTO>> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<DonDatLichDTO> dtoList = response.body();
                    Log.d(TAG, "Loaded " + dtoList.size() + " đơn đặt trước");
                    
                    // Convert DTO sang OrderHistory
                    danhSachLichSuDatLich.clear();
                    for (DonDatLichDTO dto : dtoList) {
                        danhSachLichSuDatLich.add(new OrderHistory(dto, getContext()));
                    }
                    adapter.notifyDataSetChanged();
                    
                    if (danhSachLichSuDatLich.isEmpty()) {
                        showEmpty("Chưa có đơn đặt trước nào");
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
    
    /**
     * Xử lý hủy đặt lịch
     */
    private void handleHuyDat(OrderHistory order) {
        if (order.getDaThanhToan() != null && order.getDaThanhToan()) {
            // Đã thanh toán → Hủy và chuyển sang "Đã huỷ" (không hoàn tiền)
            showConfirmDialog(
                "Xác nhận hủy đơn",
                "⚠️ Lưu ý: Theo quy định của lớp học, đơn đã thanh toán sẽ KHÔNG được hoàn tiền khi hủy.\n\nBạn có chắc chắn muốn hủy đơn này?",
                () -> huyDonDaThanhToan(order.getMaDatLich())
            );
        } else {
            // Chưa thanh toán → Xóa luôn
            showConfirmDialog(
                "Xác nhận xóa đơn",
                "Đơn chưa thanh toán sẽ bị xóa vĩnh viễn. Bạn có chắc chắn?",
                () -> xoaDonChuaThanhToan(order.getMaDatLich())
            );
        }
    }
    
    /**
     * Xóa đơn chưa thanh toán
     */
    private void xoaDonChuaThanhToan(Integer maDatLich) {
        showLoading(true);
        
        apiService.xoaDonChuaThanhToan(maDatLich).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã xóa đơn thành công", Toast.LENGTH_SHORT).show();
                    loadDonDatTruoc(); // Reload danh sách
                } else {
                    Toast.makeText(getContext(), "Không thể xóa đơn", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showLoading(false);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Hủy đơn đã thanh toán (chuyển sang Đã huỷ)
     */
    private void huyDonDaThanhToan(Integer maDatLich) {
        showLoading(true);
        
        apiService.huyDonDaThanhToan(maDatLich).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), 
                        "Đã hủy đơn thành công", 
                        Toast.LENGTH_SHORT).show();
                    loadDonDatTruoc(); // Reload danh sách
                } else {
                    Toast.makeText(getContext(), "Không thể hủy đơn", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showLoading(false);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Xử lý thanh toán lại
     */
    private void handleThanhToan(OrderHistory order) {
        if (order.getMaDatLich() == null) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin đơn", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Mở Payment.java với dữ liệu đơn này
        Intent intent = new Intent(getContext(), Payment.class);
        intent.putExtra("maDatLich", order.getMaDatLich());
        intent.putExtra("tenKhoaHoc", order.getTieuDe());
        intent.putExtra("soLuongDat", order.getSoLuongNguoiInt());
        intent.putExtra("tongTien", order.getTongTienGoc() != null ? order.getTongTienGoc().doubleValue() : 0);
        intent.putExtra("maLichTrinh", order.getMaLichTrinh());
        intent.putExtra("ngayThamGia", order.getNgayThamGia());
        intent.putExtra("thoiGian", order.getThoiGian());
        intent.putExtra("diaDiem", order.getDiaDiem());
        intent.putExtra("hinhAnh", order.getHinhAnhPath());
        intent.putExtra("moTa", order.getMoTa());
        intent.putExtra("isRePayment", true); // Đánh dấu là thanh toán lại
        
        // Thông tin người đặt
        intent.putExtra("tenNguoiDat", order.getTenNguoiDat());
        intent.putExtra("emailNguoiDat", order.getEmailNguoiDat());
        intent.putExtra("sdtNguoiDat", order.getSdtNguoiDat());
        
        startActivity(intent);
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
    
    private void showConfirmDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(getContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Xác nhận", (dialog, which) -> onConfirm.run())
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    /**
     * Xử lý click vào item đơn đặt trước
     */
    private void handleItemClick(OrderHistory order) {
        if (order.getDaThanhToan() != null && order.getDaThanhToan()) {
            // === ĐÃ THANH TOÁN → Chuyển sang Bill ===
            goToBill(order);
        } else {
            // === CHƯA THANH TOÁN → Hiện popup hỏi ===
            showPaymentConfirmDialog(order);
        }
    }
    
    /**
     * Hiển thị popup xác nhận thanh toán
     */
    private void showPaymentConfirmDialog(OrderHistory order) {
        new AlertDialog.Builder(getContext())
            .setTitle("Đơn chưa thanh toán")
            .setMessage("Bạn muốn tiếp tục thanh toán cho đơn này không?")
            .setPositiveButton("Thanh toán", (dialog, which) -> {
                // Chuyển sang trang Payment
                handleThanhToan(order);
            })
            .setNegativeButton("Hủy", (dialog, which) -> {
                // Đóng dialog, không làm gì
                dialog.dismiss();
            })
            .show();
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

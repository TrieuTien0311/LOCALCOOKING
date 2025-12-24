package com.example.localcooking_v3t;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.UuDaiDTO;
import com.example.localcooking_v3t.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DetailVoucherFragment - Hiển thị danh sách voucher trong trang chi tiết
 * 
 * Logic hiển thị voucher (dùng API /display):
 * - Khách hàng MỚI (chưa có đơn nào): Hiển thị NEWUSER (30%) + GROUP (20%)
 * - Khách hàng CŨ (đã có đơn): Hiển thị GROUP (20%)
 * 
 * Hiển thị TẤT CẢ voucher để khách biết có ưu đãi (dù chưa đủ điều kiện)
 * API sử dụng: GET /api/uudai/display?maHocVien={id}
 */
public class DetailVoucherFragment extends Fragment {

    private static final String TAG = "DetailVoucherFragment";
    private static final String ARG_MA_HOC_VIEN = "maHocVien";

    private RecyclerView recyclerView;
    private VoucherAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private TextView txtUuDai;
    private LinearLayout containerLayout;

    private List<Voucher> danhSachVoucher;
    private List<UuDaiDTO> danhSachUuDaiDTO;

    private Integer maHocVien;

    public DetailVoucherFragment() {}

    /**
     * Tạo instance với tham số
     */
    public static DetailVoucherFragment newInstance(Integer maHocVien) {
        DetailVoucherFragment fragment = new DetailVoucherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MA_HOC_VIEN, maHocVien != null ? maHocVien : -1);
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailVoucherFragment newInstance() {
        return new DetailVoucherFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Lấy arguments
        if (getArguments() != null) {
            maHocVien = getArguments().getInt(ARG_MA_HOC_VIEN, -1);
            if (maHocVien == -1) maHocVien = null;
        }

        // Lấy maHocVien từ SessionManager nếu chưa có
        if (maHocVien == null && getContext() != null) {
            SessionManager sessionManager = new SessionManager(getContext());
            Integer userId = sessionManager.getMaNguoiDung();
            if (userId != null && userId != -1) {
                maHocVien = userId;
            }
        }
        
        Log.d(TAG, "onCreate - maHocVien: " + maHocVien);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_voucher, container, false);

        // Init views
        txtUuDai = view.findViewById(R.id.txtUuDai);
        containerLayout = view.findViewById(R.id.fragmentContainer);

        // Tạo RecyclerView động
        recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        containerLayout.addView(recyclerView);

        // Tạo ProgressBar động
        progressBar = new ProgressBar(requireContext());
        LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        progressParams.gravity = android.view.Gravity.CENTER;
        progressBar.setLayoutParams(progressParams);
        progressBar.setVisibility(View.GONE);
        containerLayout.addView(progressBar);

        // Tạo TextView empty động
        txtEmpty = new TextView(requireContext());
        LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        emptyParams.gravity = android.view.Gravity.CENTER;
        txtEmpty.setLayoutParams(emptyParams);
        txtEmpty.setText("Không có voucher khả dụng");
        txtEmpty.setTextColor(0xFF888888);
        txtEmpty.setTextSize(14);
        txtEmpty.setVisibility(View.GONE);
        containerLayout.addView(txtEmpty);

        // Init data
        danhSachVoucher = new ArrayList<>();
        danhSachUuDaiDTO = new ArrayList<>();

        // Init adapter (không hiển thị radio button)
        adapter = new VoucherAdapter(danhSachVoucher);
        adapter.setHienRadioButton(false);
        recyclerView.setAdapter(adapter);

        // Load dữ liệu từ API
        loadUuDaiFromApi();

        return view;
    }

    /**
     * Load danh sách voucher để HIỂN THỊ từ API
     * Sử dụng API /display để hiển thị TẤT CẢ voucher (dù chưa đủ điều kiện)
     */
    private void loadUuDaiFromApi() {
        showLoading(true);

        if (maHocVien == null) {
            Log.w(TAG, "maHocVien is null, loading all vouchers");
            loadAllUuDai();
            return;
        }

        Log.d(TAG, "Loading display vouchers for maHocVien=" + maHocVien);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        // Sử dụng API /display thay vì /available
        Call<List<UuDaiDTO>> call = apiService.getDisplayUuDai(maHocVien);

        call.enqueue(new Callback<List<UuDaiDTO>>() {
            @Override
            public void onResponse(Call<List<UuDaiDTO>> call, Response<List<UuDaiDTO>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    danhSachUuDaiDTO.clear();
                    danhSachUuDaiDTO.addAll(response.body());

                    Log.d(TAG, "Loaded " + danhSachUuDaiDTO.size() + " display vouchers");

                    // Cập nhật tiêu đề
                    updateTitle();

                    if (danhSachUuDaiDTO.isEmpty()) {
                        showEmpty(true);
                    } else {
                        showEmpty(false);
                        convertToVoucherList();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "Failed to load display vouchers: " + response.code());
                    loadAllUuDai(); // Fallback
                }
            }

            @Override
            public void onFailure(Call<List<UuDaiDTO>> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Error loading display vouchers", t);
                loadAllUuDai(); // Fallback
            }
        });
    }

    /**
     * Fallback: Load tất cả voucher
     */
    private void loadAllUuDai() {
        showLoading(true);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<UuDaiDTO>> call = apiService.getAllUuDai();

        call.enqueue(new Callback<List<UuDaiDTO>>() {
            @Override
            public void onResponse(Call<List<UuDaiDTO>> call, Response<List<UuDaiDTO>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    danhSachUuDaiDTO.clear();
                    danhSachUuDaiDTO.addAll(response.body());

                    Log.d(TAG, "Loaded " + danhSachUuDaiDTO.size() + " all vouchers (fallback)");

                    updateTitle();

                    if (danhSachUuDaiDTO.isEmpty()) {
                        showEmpty(true);
                    } else {
                        showEmpty(false);
                        convertToVoucherList();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "Failed to load all vouchers: " + response.code());
                    showEmpty(true);
                }
            }

            @Override
            public void onFailure(Call<List<UuDaiDTO>> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Error loading all vouchers", t);
                showEmpty(true);
            }
        });
    }

    /**
     * Convert UuDaiDTO sang Voucher để hiển thị
     */
    private void convertToVoucherList() {
        danhSachVoucher.clear();
        
        for (UuDaiDTO dto : danhSachUuDaiDTO) {
            // Chọn hình ảnh dựa vào giá trị giảm
            int hinhAnh = R.drawable.voucher_4;
            if (dto.getGiaTriGiam() != null) {
                if (dto.getGiaTriGiam() >= 30) {
                    hinhAnh = R.drawable.voucher_3;
                } else if (dto.getGiaTriGiam() >= 20) {
                    hinhAnh = R.drawable.voucher_5;
                } else {
                    hinhAnh = R.drawable.voucher_4;
                }
            }

            // Tạo mô tả
            String moTa = dto.getMoTa();
            if (moTa == null || moTa.isEmpty()) {
                if ("PhanTram".equals(dto.getLoaiGiam())) {
                    moTa = "Giảm " + dto.getGiaTriGiam().intValue() + "%";
                } else {
                    moTa = "Giảm " + formatTien(dto.getGiaTriGiam()) + "đ";
                }
            }

            Voucher voucher = new Voucher(
                    hinhAnh,
                    dto.getTieuDe() != null ? dto.getTieuDe() : dto.getMaCode(),
                    moTa,
                    dto.getHsd() != null ? dto.getHsd() : "Không giới hạn"
            );
            danhSachVoucher.add(voucher);
        }
    }

    /**
     * Cập nhật tiêu đề hiển thị số lượng voucher
     */
    private void updateTitle() {
        if (txtUuDai != null) {
            int count = danhSachUuDaiDTO.size();
            txtUuDai.setText("Mã ưu đãi (" + count + ")");
        }
    }

    /**
     * Hiển thị/ẩn loading
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Hiển thị/ẩn thông báo empty
     */
    private void showEmpty(boolean show) {
        if (txtEmpty != null) {
            txtEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private String formatTien(Double tien) {
        if (tien == null) return "0";
        return String.format("%,.0f", tien).replace(",", ".");
    }
}
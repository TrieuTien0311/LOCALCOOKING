package com.example.localcooking_v3t;

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
import com.example.localcooking_v3t.model.UuDaiDTO;
import com.example.localcooking_v3t.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VouchersFragment extends Fragment {

    private static final String TAG = "VouchersFragment";
    private static final String ARG_MA_HOC_VIEN = "maHocVien";
    private static final String ARG_SO_LUONG_NGUOI = "soLuongNguoi";
    private static final String ARG_TONG_TIEN = "tongTien";

    private RecyclerView recyclerView;
    private VoucherAdapter adapter;
    private ProgressBar progressBar;
    private TextView txtEmpty;

    private List<Voucher> danhSachVoucher;
    private List<UuDaiDTO> danhSachUuDaiDTO;

    private Integer maHocVien;
    private Integer soLuongNguoi = 1;
    private Double tongTien = 0.0;

    private OnVoucherSelectedListener voucherSelectedListener;
    private UuDaiDTO selectedUuDai;

    public interface OnVoucherSelectedListener {
        void onVoucherSelected(UuDaiDTO uuDai);
    }

    public void setOnVoucherSelectedListener(OnVoucherSelectedListener listener) {
        this.voucherSelectedListener = listener;
    }

    public VouchersFragment() {}

    public static VouchersFragment newInstance(Integer maHocVien, Integer soLuongNguoi, Double tongTien) {
        VouchersFragment fragment = new VouchersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MA_HOC_VIEN, maHocVien != null ? maHocVien : -1);
        args.putInt(ARG_SO_LUONG_NGUOI, soLuongNguoi != null ? soLuongNguoi : 1);
        args.putDouble(ARG_TONG_TIEN, tongTien != null ? tongTien : 0.0);
        fragment.setArguments(args);
        return fragment;
    }

    public static VouchersFragment newInstance() {
        return new VouchersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maHocVien = getArguments().getInt(ARG_MA_HOC_VIEN, -1);
            if (maHocVien == -1) maHocVien = null;
            soLuongNguoi = getArguments().getInt(ARG_SO_LUONG_NGUOI, 1);
            tongTien = getArguments().getDouble(ARG_TONG_TIEN, 0.0);
        }

        // Lấy maHocVien từ SessionManager nếu chưa có
        if (maHocVien == null && getContext() != null) {
            SessionManager sessionManager = new SessionManager(getContext());
            Integer userId = sessionManager.getMaNguoiDung();
            if (userId != null && userId != -1) {
                maHocVien = userId;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vouchers, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUuDai);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        danhSachVoucher = new ArrayList<>();
        danhSachUuDaiDTO = new ArrayList<>();

        adapter = new VoucherAdapter(danhSachVoucher);
        recyclerView.setAdapter(adapter);

        // Lắng nghe sự kiện chọn voucher
        adapter.setOnItemClickListener(uuDai -> {
            int position = danhSachVoucher.indexOf(uuDai);
            if (position >= 0 && position < danhSachUuDaiDTO.size()) {
                selectedUuDai = danhSachUuDaiDTO.get(position);
                if (voucherSelectedListener != null) {
                    voucherSelectedListener.onVoucherSelected(selectedUuDai);
                }
            }
            Toast.makeText(getContext(),
                    "Bạn đã chọn: " + uuDai.getTieuDe(),
                    Toast.LENGTH_SHORT).show();
        });

        // Load dữ liệu từ API
        loadUuDaiFromApi();

        return view;
    }

    private void loadUuDaiFromApi() {
        if (maHocVien == null) {
            Log.w(TAG, "maHocVien is null, loading all vouchers");
            loadAllUuDai();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<UuDaiDTO>> call = apiService.getAvailableUuDai(maHocVien, soLuongNguoi);

        call.enqueue(new Callback<List<UuDaiDTO>>() {
            @Override
            public void onResponse(Call<List<UuDaiDTO>> call, Response<List<UuDaiDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachUuDaiDTO.clear();
                    danhSachUuDaiDTO.addAll(response.body());
                    convertToVoucherList();
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + danhSachUuDaiDTO.size() + " available vouchers");
                } else {
                    Log.e(TAG, "Failed to load available vouchers: " + response.code());
                    loadAllUuDai(); // Fallback
                }
            }

            @Override
            public void onFailure(Call<List<UuDaiDTO>> call, Throwable t) {
                Log.e(TAG, "Error loading available vouchers", t);
                loadAllUuDai(); // Fallback
            }
        });
    }

    private void loadAllUuDai() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<UuDaiDTO>> call = apiService.getAllUuDai();

        call.enqueue(new Callback<List<UuDaiDTO>>() {
            @Override
            public void onResponse(Call<List<UuDaiDTO>> call, Response<List<UuDaiDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachUuDaiDTO.clear();
                    danhSachUuDaiDTO.addAll(response.body());
                    convertToVoucherList();
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + danhSachUuDaiDTO.size() + " all vouchers");
                } else {
                    Log.e(TAG, "Failed to load all vouchers: " + response.code());
                    taoDuLieuMau(); // Fallback to sample data
                }
            }

            @Override
            public void onFailure(Call<List<UuDaiDTO>> call, Throwable t) {
                Log.e(TAG, "Error loading all vouchers", t);
                taoDuLieuMau(); // Fallback to sample data
            }
        });
    }

    private void convertToVoucherList() {
        danhSachVoucher.clear();
        for (UuDaiDTO dto : danhSachUuDaiDTO) {
            // Chọn hình ảnh mặc định dựa vào loại giảm giá
            int hinhAnh = R.drawable.voucher;
            if (dto.getGiaTriGiam() != null) {
                if (dto.getGiaTriGiam() >= 30) {
                    hinhAnh = R.drawable.voucher_3;
                } else if (dto.getGiaTriGiam() >= 20) {
                    hinhAnh = R.drawable.voucher;
                } else {
                    hinhAnh = R.drawable.voucher_1;
                }
            }

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
            voucher.setDuocChon(dto.getDuocChon() != null && dto.getDuocChon());
            danhSachVoucher.add(voucher);
        }
    }

    private void taoDuLieuMau() {
        danhSachVoucher.clear();

        danhSachVoucher.add(new Voucher(
                R.drawable.voucher,
                "Dành cho hội viên",
                "Giảm 20%, thêm ưu đãi bên dưới",
                "10/10/2025"
        ));

        danhSachVoucher.add(new Voucher(
                R.drawable.voucher_1,
                "Dành cho tuần này",
                "Giảm 10%, thêm ưu đãi bên dưới",
                "Trong tuần này"
        ));

        danhSachVoucher.add(new Voucher(
                R.drawable.voucher_3,
                "Dành cho người mới",
                "Giảm 30%, thêm ưu đãi bên dưới",
                "Lần đầu đặt lịch"
        ));

        adapter.notifyDataSetChanged();
    }

    public UuDaiDTO getSelectedUuDai() {
        return selectedUuDai;
    }

    private String formatTien(Double tien) {
        if (tien == null) return "0";
        return String.format("%,.0f", tien).replace(",", ".");
    }
}

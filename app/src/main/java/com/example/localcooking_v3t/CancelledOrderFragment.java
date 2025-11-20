package com.example.localcooking_v3t;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class CancelledOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> danhSachLichSuDatLich;

    public CancelledOrderFragment() {}

    public static CancelledOrderFragment newInstance() {
        return new CancelledOrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_voucher, container, false);
        LinearLayout containerLayout = view.findViewById(R.id.fragmentContainer);

        recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        containerLayout.addView(recyclerView);

        taoDuLieuMau();

        adapter = new OrderHistoryAdapter(danhSachLichSuDatLich);
        recyclerView.setAdapter(adapter);

        // Chỉ hiển thị thông tin, không cần xử lý nút
        adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onHuyDatClick(OrderHistory lichSuDatLich) {
                // Không có nút hủy
            }

            @Override
            public void onDatLaiClick(OrderHistory lichSuDatLich) {
                // Không có nút đặt lại
            }

            @Override
            public void onDanhGiaClick(OrderHistory lichSuDatLich) {
                // Không có nút đánh giá
            }
        });

        return view;
    }

    private void taoDuLieuMau() {
        danhSachLichSuDatLich = new ArrayList<>();

        danhSachLichSuDatLich.add(new OrderHistory(
                R.drawable.phuyen_h,
                "Ẩm thực địa phương Đà Nẵng",
                "1 người",
                "08:00 - 11:00, 12/11/2024",
                "84 Phố Hàng Mã, Hà Nội",
                "830.000₫",
                "Đã hủy",
                "10:52 - T3, 11/11/2024"
        ));
    }
}
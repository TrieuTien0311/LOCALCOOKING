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

public class SuccessfulOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> danhSachLichSuDatLich;

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

        taoDuLieuMau();

        adapter = new OrderHistoryAdapter(danhSachLichSuDatLich);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onHuyDatClick(OrderHistory lichSuDatLich) {
                // Không cần xử lý nút hủy
            }

            @Override
            public void onDatLaiClick(OrderHistory lichSuDatLich) {
                // Xử lý chuyển sang trang đặt lại
            }

            @Override
            public void onDanhGiaClick(OrderHistory lichSuDatLich) {
                // Xử lý chuyển sang trang đánh giá
            }
        });

        return view;
    }

    private void taoDuLieuMau() {
        danhSachLichSuDatLich = new ArrayList<>();

        danhSachLichSuDatLich.add(new OrderHistory(
                R.drawable.phuyen_h,
                "Ẩm thực địa phương Phú Yên",
                "1 người",
                "13:00 - 16:00, 21/08/2024",
                "290 Ngô Quyền, Tp.HCM",
                "810.000₫",
                "Đã hoàn thành"
        ));

        danhSachLichSuDatLich.add(new OrderHistory(
                R.drawable.hanoi_h,
                "Ẩm thực thủ đô Hà Nội",
                "2 người",
                "08:00 - 11:00, 23/05/2025",
                "175 Lê Lợi, Huế",
                "1.450.000₫",
                "Đã hoàn thành"
        ));
    }
}

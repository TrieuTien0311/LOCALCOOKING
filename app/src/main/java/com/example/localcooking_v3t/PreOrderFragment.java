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

public class PreOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<OrderHistory> danhSachLichSuDatLich;

    public PreOrderFragment() {}

    public static PreOrderFragment newInstance() {
        return new PreOrderFragment();
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

        adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onHuyDatClick(OrderHistory lichSuDatLich) {
                // Xử lý hủy đơn đặt trước
            }

            @Override
            public void onDatLaiClick(OrderHistory lichSuDatLich) {
                // Không cần xử lý
            }

            @Override
            public void onDanhGiaClick(OrderHistory lichSuDatLich) {
                // Không cần xử lý
            }
        });

        return view;
    }

    private void taoDuLieuMau() {
        danhSachLichSuDatLich = new ArrayList<>();

        danhSachLichSuDatLich.add(new OrderHistory(
                R.drawable.hue_h,
                "Ẩm thực địa phương Huế",
                "1 người",
                "14:00 - 17:00, 02/10/2025",
                "23 Lê Duẩn, Đà Nẵng",
                "715.000₫",
                "Đặt trước"
        ));
    }
}

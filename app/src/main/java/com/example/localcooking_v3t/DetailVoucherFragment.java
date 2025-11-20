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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailVoucherFragment extends Fragment {

    private RecyclerView recyclerView;
    private VoucherAdapter adapter;

    private List<Voucher> danhSachVoucher;

    public DetailVoucherFragment() {}

    public static VouchersFragment newInstance() {
        return new VouchersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_voucher, container, false);

        LinearLayout containerLayout = view.findViewById(R.id.fragmentContainer);

        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        containerLayout.addView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo dữ liệu mẫu
        taoDuLieuMau();

        // Khởi tạo adapter
        adapter = new VoucherAdapter(danhSachVoucher);
        recyclerView.setAdapter(adapter);
        adapter.setHienRadioButton(false);

        return view;
    }

    private void taoDuLieuMau() {

        danhSachVoucher = new ArrayList<>();

        danhSachVoucher.add(new Voucher(
                R.drawable.voucher_4,
                "Ẩm thực địa phương Huế",
                "Giảm 15K, dành cho từ 1 người ",
                "10/10/2025"
        ));

        danhSachVoucher.add(new Voucher(
                R.drawable.voucher_5,
                "Ẩm thực địa phương Huế",
                "Giảm 40K, dành cho từ 2 người ",
                "20/10/2025"
        ));
    }
}
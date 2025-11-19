package com.example.localcooking_v3t;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VouchersFragment extends Fragment {

    private RecyclerView recyclerView;
    private VoucherAdapter adapter;

    private List<Voucher> danhSachGoc;
    private List<Voucher> danhSachVoucher;

    public VouchersFragment() {}

    public static VouchersFragment newInstance() {
        return new VouchersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vouchers, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUuDai);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo dữ liệu mẫu
        taoDuLieuMau();

        // Khởi tạo adapter
        adapter = new VoucherAdapter(danhSachVoucher);
        recyclerView.setAdapter(adapter);

        // Lắng nghe sự kiện chọn voucher
        adapter.setOnItemClickListener(uuDai -> {
            Toast.makeText(getContext(),
                    "Bạn đã chọn voucher: " + uuDai.getTieuDe(),
                    Toast.LENGTH_SHORT).show();

            xuLyChonMotVoucher(uuDai);
        });

        return view;
    }

    // ---------------------------------------------------------------------------------------------
    // TẠO DỮ LIỆU
    // ---------------------------------------------------------------------------------------------
    private void taoDuLieuMau() {
        danhSachGoc = new ArrayList<>();

        danhSachGoc.add(new Voucher(
                R.drawable.voucher,
                "Dành cho hội viên",
                "Giảm 20%, thêm ưu đãi bên dưới",
                "10/10/2025"
        ));

        danhSachGoc.add(new Voucher(
                R.drawable.voucher_1,
                "Dành cho tuần này",
                "Giảm 10%, thêm ưu đãi bên dưới",
                "Trong tuần này"
        ));

        danhSachGoc.add(new Voucher(
                R.drawable.voucher_3,
                "Dành cho người mới",
                "Giảm 30%, thêm ưu đãi bên dưới",
                "Lần đầu đặt lịch"
        ));

        // Copy sang danh sách hiển thị
        danhSachVoucher = new ArrayList<>(danhSachGoc);
    }

    // ---------------------------------------------------------------------------------------------
    // CHỈ CHO PHÉP CHỌN 1 VOUCHER
    // ---------------------------------------------------------------------------------------------
    private void xuLyChonMotVoucher(Voucher uuDai) {
        for (Voucher v : danhSachGoc) {
            v.setDuocChon(v.getTieuDe().equals(uuDai.getTieuDe()));
        }

        adapter.notifyDataSetChanged();
    }
}

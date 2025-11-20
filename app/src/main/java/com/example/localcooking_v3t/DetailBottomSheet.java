package com.example.localcooking_v3t;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailBottomSheet extends BottomSheetDialogFragment {

    private TextView tvTenLop, tvThoiGian, btnDong;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MaterialButton btnDatLich, btnFav, btnShare;

    private Class lopHoc; // Dữ liệu lớp học

    // Constructor nhận dữ liệu lớp học
    public static DetailBottomSheet newInstance(Class lopHoc) {
        DetailBottomSheet sheet = new DetailBottomSheet();
        Bundle args = new Bundle();
        sheet.setArguments(args);
        sheet.lopHoc = lopHoc;
        return sheet;
    }

    // THÊM ĐOẠN NÀY ĐỂ CÓ BO GÓC
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(d -> {
            View sheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (sheet != null) sheet.setBackgroundResource(android.R.color.transparent);
        });
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_detail, container, false);

        // Ánh xạ các view
        tvTenLop = view.findViewById(R.id.tvTenLop);
        tvThoiGian = view.findViewById(R.id.tvThoiGian);
        btnDong = view.findViewById(R.id.btnDong);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        btnDatLich = view.findViewById(R.id.btnDatLich);
        btnFav = view.findViewById(R.id.btnfav);
        btnShare = view.findViewById(R.id.btnShare);

        // Hiển thị dữ liệu lớp học nếu có
        if (lopHoc != null) {
            tvTenLop.setText(lopHoc.getTenLop());
            tvThoiGian.setText(lopHoc.getThoiGian() + ", " + lopHoc.getNgay());
        }

        // Xử lý sự kiện đóng
        btnDong.setOnClickListener(v -> dismiss());

        // Setup ViewPager2 với adapter
        DetailPagerAdapter adapter = new DetailPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Mô tả");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
                case 2:
                    tab.setText("Chính sách");
                    break;
                case 3:
                    tab.setText("Ưu đãi");
                    break;
            }
        }).attach();

        // Xử lý nút Đặt lịch
        btnDatLich.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Đặt lịch: " + lopHoc.getTenLop(),
                    Toast.LENGTH_SHORT).show();
            // TODO: Xử lý logic đặt lịch
        });

        // Xử lý nút Favorite
        btnFav.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Yêu thích: " + lopHoc.getTenLop(),
                    Toast.LENGTH_SHORT).show();
            // TODO: Xử lý logic thêm/xóa yêu thích
        });

        // Xử lý nút Share
        btnShare.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chia sẻ: " + lopHoc.getTenLop(),
                    Toast.LENGTH_SHORT).show();
            // TODO: Xử lý logic chia sẻ
        });

        return view;
    }

    // Adapter cho ViewPager2
    private class DetailPagerAdapter extends FragmentStateAdapter {

        public DetailPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    DetailDescriptionFragment descFragment = new DetailDescriptionFragment();
                    descFragment.setLopHoc(lopHoc);
                    return descFragment;
                case 1:
                    return new DetailEvaluateFragment();
                case 2:
                    return new DetailPolicyFragment();
                case 3:
                    return new DetailVoucherFragment();
                default:
                    return new DetailDescriptionFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4; // 4 tabs
        }
    }
}
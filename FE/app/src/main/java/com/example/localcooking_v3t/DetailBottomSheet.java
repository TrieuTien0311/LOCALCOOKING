package com.example.localcooking_v3t;

import android.app.Dialog;
import android.content.Intent;
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

import com.example.localcooking_v3t.model.KhoaHoc;
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

    private KhoaHoc lopHoc; // Dữ liệu lớp học
    private String selectedDate; // Ngày được chọn từ calendar

    // Constructor nhận dữ liệu lớp học
    public static DetailBottomSheet newInstance(KhoaHoc lopHoc) {
        DetailBottomSheet sheet = new DetailBottomSheet();
        Bundle args = new Bundle();
        sheet.setArguments(args);
        sheet.lopHoc = lopHoc;
        return sheet;
    }
    
    // Constructor nhận dữ liệu lớp học và ngày được chọn
    public static DetailBottomSheet newInstance(KhoaHoc lopHoc, String selectedDate) {
        DetailBottomSheet sheet = new DetailBottomSheet();
        Bundle args = new Bundle();
        sheet.setArguments(args);
        sheet.lopHoc = lopHoc;
        sheet.selectedDate = selectedDate;
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
            
            // Lấy thời gian
            String thoiGian = lopHoc.getThoiGian();
            
            // Sử dụng ngày được chọn từ calendar nếu có, nếu không thì tính từ lịch trình
            String ngayFormatted = "";
            if (selectedDate != null && !selectedDate.isEmpty()) {
                // Ngày đã được format sẵn dạng "T4, 15/01/2025"
                ngayFormatted = selectedDate;
            } else {
                // Tính ngày từ lịch trình
                String ngayBatDau = lopHoc.getNgayBatDau(); // Format: "2025-01-15"
                if (ngayBatDau != null && !ngayBatDau.isEmpty()) {
                    try {
                        String[] parts = ngayBatDau.split("-");
                        if (parts.length == 3) {
                            // Tính thứ trong tuần
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            cal.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
                            
                            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
                            String thu = "";
                            switch (dayOfWeek) {
                                case java.util.Calendar.SUNDAY: thu = "CN"; break;
                                case java.util.Calendar.MONDAY: thu = "T2"; break;
                                case java.util.Calendar.TUESDAY: thu = "T3"; break;
                                case java.util.Calendar.WEDNESDAY: thu = "T4"; break;
                                case java.util.Calendar.THURSDAY: thu = "T5"; break;
                                case java.util.Calendar.FRIDAY: thu = "T6"; break;
                                case java.util.Calendar.SATURDAY: thu = "T7"; break;
                            }
                            
                            ngayFormatted = thu + ", " + parts[2] + "/" + parts[1] + "/" + parts[0];
                        }
                    } catch (Exception e) {
                        ngayFormatted = ngayBatDau;
                    }
                }
            }
            
            tvThoiGian.setText(thoiGian + " - " + ngayFormatted);
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

        // Xử lý nút Đặt lịch - Chuyển sang Booking Activity
        btnDatLich.setOnClickListener(v -> {
            if (lopHoc != null) {
                // Đóng bottom sheet
                dismiss();

                // Chuyển sang Booking Activity
                Intent intent = new Intent(getActivity(), Booking.class);

                // Truyền dữ liệu lớp học
                intent.putExtra("tenLop", lopHoc.getTenLop());
                intent.putExtra("moTa", lopHoc.getMoTa());
                intent.putExtra("thoiGian", lopHoc.getThoiGian());
                
                // Format ngày
                String ngay = lopHoc.getNgayBatDau();
                if (ngay != null && !ngay.isEmpty()) {
                    try {
                        String[] parts = ngay.split("-");
                        if (parts.length == 3) {
                            ngay = parts[2] + "/" + parts[1] + "/" + parts[0];
                        }
                    } catch (Exception e) {
                        // Ignore
                    }
                }
                intent.putExtra("ngay", ngay);
                
                intent.putExtra("diaDiem", lopHoc.getDiaDiem());
                intent.putExtra("gia", lopHoc.getGia());
                intent.putExtra("giaSo", lopHoc.getGiaTien());
                intent.putExtra("danhGia", lopHoc.getDanhGia());
                intent.putExtra("soDanhGia", lopHoc.getSoDanhGia());
                intent.putExtra("hinhAnh", lopHoc.getHinhAnh());
                intent.putExtra("coUuDai", lopHoc.getCoUuDai());
                intent.putExtra("thoiGianKetThuc", "23:59:59"); // Mặc định
                intent.putExtra("suat", lopHoc.getSuat());

                startActivity(intent);
            }
        });

        // Xử lý nút Favorite
        btnFav.setOnClickListener(v -> {
            if (lopHoc != null) {
                // Toggle trạng thái yêu thích
                Boolean currentFavorite = lopHoc.getIsFavorite();
                lopHoc.setIsFavorite(currentFavorite == null ? true : !currentFavorite);

                // Cập nhật icon
                if (lopHoc.getIsFavorite() != null && lopHoc.getIsFavorite()) {
                    btnFav.setIconResource(R.drawable.ic_heartredfilled);
                    Toast.makeText(getContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    btnFav.setIconResource(R.drawable.ic_heart);
                    Toast.makeText(getContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý nút Share
        btnShare.setOnClickListener(v -> {
            if (lopHoc != null) {
                Toast.makeText(getContext(), "Chia sẻ: " + lopHoc.getTenLop(),
                        Toast.LENGTH_SHORT).show();
                // TODO: Xử lý logic chia sẻ
            }
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
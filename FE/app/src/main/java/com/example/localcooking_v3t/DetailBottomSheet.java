package com.example.localcooking_v3t;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.KhoaHoc;
import com.example.localcooking_v3t.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "DetailBottomSheet";

    private TextView tvTenLop, tvThoiGian, btnDong;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MaterialButton btnDatLich, btnFav, btnShare;

    private KhoaHoc lopHoc; // Dữ liệu lớp học
    private String selectedDate; // Ngày được chọn từ calendar
    
    // Callback để thông báo khi trạng thái yêu thích thay đổi
    private OnFavoriteChangedListener favoriteChangedListener;
    
    public interface OnFavoriteChangedListener {
        void onFavoriteChanged(KhoaHoc khoaHoc, boolean isFavorite);
    }
    
    public void setOnFavoriteChangedListener(OnFavoriteChangedListener listener) {
        this.favoriteChangedListener = listener;
    }

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
                // Debug log
                android.util.Log.d("DETAIL_BOTTOM_SHEET", "lopHoc: " + lopHoc.getTenLop());
                android.util.Log.d("DETAIL_BOTTOM_SHEET", "maKhoaHoc: " + lopHoc.getMaKhoaHoc());
                android.util.Log.d("DETAIL_BOTTOM_SHEET", "lichTrinhList: " + (lopHoc.getLichTrinhList() == null ? "NULL" : lopHoc.getLichTrinhList().size() + " items"));
                
                // Kiểm tra có lịch trình không
                if (lopHoc.getLichTrinhList() == null || lopHoc.getLichTrinhList().isEmpty()) {
                    Toast.makeText(getContext(), "Khóa học chưa có lịch trình. Vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
                    return;
                }
                
                // Lấy lịch trình đầu tiên
                Integer maLichTrinh = lopHoc.getLichTrinhList().get(0).getMaLichTrinh();
                if (maLichTrinh == null || maLichTrinh == 0) {
                    Toast.makeText(getContext(), "Lịch trình không hợp lệ. Vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
                    return;
                }
                
                // Đóng bottom sheet
                dismiss();

                // Chuyển sang Booking Activity
                Intent intent = new Intent(getActivity(), Booking.class);

                // Truyền dữ liệu theo format mới
                intent.putExtra("maKhoaHoc", lopHoc.getMaKhoaHoc());
                intent.putExtra("maLichTrinh", maLichTrinh);
                intent.putExtra("tenKhoaHoc", lopHoc.getTenLop());
                intent.putExtra("giaTien", String.valueOf(lopHoc.getGiaTien()));
                intent.putExtra("thoiGian", lopHoc.getThoiGian());
                intent.putExtra("diaDiem", lopHoc.getDiaDiem());
                
                // Sử dụng ngày được chọn từ calendar nếu có
                String ngayThamGia = "";
                if (selectedDate != null && !selectedDate.isEmpty()) {
                    // Convert từ "T4, 15/01/2025" sang "2025-01-15"
                    try {
                        String[] parts = selectedDate.split(", ");
                        if (parts.length == 2) {
                            String[] dateParts = parts[1].split("/");
                            if (dateParts.length == 3) {
                                ngayThamGia = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                            }
                        }
                    } catch (Exception e) {
                        // Nếu lỗi, dùng ngày bắt đầu
                        ngayThamGia = lopHoc.getNgayBatDau();
                    }
                } else {
                    // Dùng ngày bắt đầu từ lớp học
                    ngayThamGia = lopHoc.getNgayBatDau();
                }
                intent.putExtra("ngayThamGia", ngayThamGia);

                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Không có thông tin lớp học", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút Favorite
        btnFav.setOnClickListener(v -> {
            if (lopHoc != null) {
                toggleFavorite();
            }
        });
        
        // Cập nhật trạng thái yêu thích ban đầu
        updateFavoriteIcon();

        // Xử lý nút Share - Chia sẻ qua Messenger hoặc các app khác
        btnShare.setOnClickListener(v -> {
            if (lopHoc != null) {
                // Tạo deep link cho lớp học
                String deepLink = "https://localcooking.app/khoahoc/" + lopHoc.getMaKhoaHoc();
                
                String shareText = "🍳 Khám phá lớp học nấu ăn: " + lopHoc.getTenLop() + "\n\n" +
                        "⏰ Thời gian: " + lopHoc.getThoiGian() + "\n" +
                        "📍 Địa điểm: " + lopHoc.getDiaDiem() + "\n" +
                        "💰 Giá: " + String.format("%,.0f", lopHoc.getGiaTien()) + "₫\n\n" +
                        "👉 Xem chi tiết: " + deepLink;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Lớp học nấu ăn: " + lopHoc.getTenLop());
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                // Mở chooser để chọn app chia sẻ (Messenger, Zalo, Facebook, ...)
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
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
                    return DetailEvaluateFragment.newInstance(lopHoc != null ? lopHoc.getMaKhoaHoc() : -1);
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
    
    /**
     * Cập nhật icon yêu thích dựa trên trạng thái hiện tại
     */
    private void updateFavoriteIcon() {
        if (lopHoc != null && lopHoc.getIsFavorite() != null && lopHoc.getIsFavorite()) {
            btnFav.setIconResource(R.drawable.ic_heartredfilled);
        } else {
            btnFav.setIconResource(R.drawable.ic_heart);
        }
    }
    
    /**
     * Toggle trạng thái yêu thích và gọi API
     */
    private void toggleFavorite() {
        SessionManager sessionManager = new SessionManager(requireContext());
        Integer maHocVien = sessionManager.getMaNguoiDung();
        
        if (maHocVien == null || maHocVien == -1) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập để sử dụng chức năng này", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Map<String, Integer> request = new HashMap<>();
        request.put("maHocVien", maHocVien);
        request.put("maKhoaHoc", lopHoc.getMaKhoaHoc());
        
        RetrofitClient.getApiService().toggleFavorite(request)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Boolean isFavorite = (Boolean) response.body().get("isFavorite");
                            String message = (String) response.body().get("message");
                            
                            // Cập nhật trạng thái trong model
                            lopHoc.setIsFavorite(isFavorite);
                            
                            // Cập nhật icon
                            updateFavoriteIcon();
                            
                            // Thông báo cho listener (ClassesFragment)
                            if (favoriteChangedListener != null) {
                                favoriteChangedListener.onFavoriteChanged(lopHoc, isFavorite != null && isFavorite);
                            }
                            
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Favorite toggled: " + isFavorite);
                        } else {
                            Toast.makeText(getContext(), "Không thể cập nhật yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Log.e(TAG, "Error toggling favorite", t);
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
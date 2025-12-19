package com.example.localcooking_v3t;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.model.KhoaHoc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerViewFavorite;
    private ClassAdapter adapter;
    private LinearLayout txtEmpty;
    private LinearLayout dateContainer;
    private int selectedPosition = 0; // Ngày được chọn (0 = Hôm nay)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerViewFavorite = view.findViewById(R.id.recyclerViewFavorite);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        dateContainer = view.findViewById(R.id.dateContainer);

        recyclerViewFavorite.setLayoutManager(new LinearLayoutManager(getContext()));

        setupDatePickerBar();
        setupFavoriteData();

        return view;
    }

    private void setupDatePickerBar() {
        dateContainer.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));

        for (int i = 0; i < 30; i++) {
            Calendar currentDay = (Calendar) calendar.clone();

            String title;
            if (i == 0) title = "Hôm nay";
            else if (i == 1) title = "Ngày mai";
            else {
                String dayName = dayFormat.format(currentDay.getTime());
                title = dayName
                        .replace("Thứ Hai", "Thứ 2")
                        .replace("Thứ Ba", "Thứ 3")
                        .replace("Thứ Tư", "Thứ 4")
                        .replace("Thứ Năm", "Thứ 5")
                        .replace("Thứ Sáu", "Thứ 6")
                        .replace("Thứ Bảy", "Thứ 7")
                        .replace("Chủ nhật", "CN");
            }

            String dateStr = dateFormat.format(currentDay.getTime());

            View dateItem = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_date, dateContainer, false);

            TextView txtTitle = dateItem.findViewById(R.id.txtDayTitle);
            TextView txtDate = dateItem.findViewById(R.id.txtDate);
            View gachChan = dateItem.findViewById(R.id.gachChan);

            txtTitle.setText(title);
            txtDate.setText(dateStr);

            // Áp dụng trạng thái ban đầu
            updateDateItemAppearance(txtTitle, txtDate, gachChan, i == selectedPosition);

            // Xử lý click
            final int position = i;
            dateItem.setOnClickListener(v -> {
                selectedPosition = position;

                // Cập nhật lại tất cả item
                for (int j = 0; j < dateContainer.getChildCount(); j++) {
                    View child = dateContainer.getChildAt(j);
                    TextView tTitle = child.findViewById(R.id.txtDayTitle);
                    TextView tDate = child.findViewById(R.id.txtDate);
                    View tGach = child.findViewById(R.id.gachChan);
                    updateDateItemAppearance(tTitle, tDate, tGach, j == position);
                }

                // Cuộn mượt đến vị trí được chọn (căn giữa + lệch nhẹ sang phải 10dp)
                dateContainer.post(() -> {
                    HorizontalScrollView hsv = (HorizontalScrollView) dateContainer.getParent();
                    View targetItem = dateContainer.getChildAt(position);
                    if (targetItem == null) return;

                    int itemWidth = targetItem.getWidth();
                    int hsvWidth = hsv.getWidth();

                    int itemCenterX = targetItem.getLeft() + itemWidth / 2;
                    int screenCenterX = hsvWidth / 2;

                    int desiredScrollX = itemCenterX - screenCenterX + 10; // +10dp lệch phải nhẹ

                    int maxScrollX = dateContainer.getMeasuredWidth() - hsvWidth;
                    int finalScrollX = Math.max(0, Math.min(desiredScrollX, maxScrollX));

                    hsv.smoothScrollTo(finalScrollX, 0);
                });
            });

            dateContainer.addView(dateItem);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Cuộn về đầu khi mở lần đầu (Hôm nay hiện rõ)
        dateContainer.post(() -> {
            HorizontalScrollView hsv = (HorizontalScrollView) dateContainer.getParent();
            hsv.scrollTo(0, 0);
        });
    }

    private void updateDateItemAppearance(TextView title, TextView date, View gachChan, boolean isSelected) {
        if (isSelected) {
            title.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_dark));
            date.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_dark));
            gachChan.setVisibility(View.VISIBLE);
        } else {
            title.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text));
            date.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text));
            gachChan.setVisibility(View.INVISIBLE);
        }
    }

    private void setupFavoriteData() {
        List<KhoaHoc> favoriteList = new ArrayList<>();

        // TODO: Load from API instead of hardcoded data
        KhoaHoc lopChuaDienRa = new KhoaHoc();
        lopChuaDienRa.setTenKhoaHoc("Ẩm thực địa phương Huế");
        lopChuaDienRa.setMoTa("Khám phá tinh hoa ẩm thực cung đình với hương vị đặc trưng vị Huế");
        lopChuaDienRa.setGiaTien(715000.0);
        lopChuaDienRa.setSaoTrungBinh(4.9f);
        lopChuaDienRa.setSoLuongDanhGia(211);
        lopChuaDienRa.setHinhAnh("hue.png");
        lopChuaDienRa.setCoUuDai(true);
        lopChuaDienRa.setDaDienRa(false);
        lopChuaDienRa.setIsFavorite(true);
        
        // Tạo lịch trình cho lớp chưa diễn ra
        List<com.example.localcooking_v3t.model.LichTrinhLopHoc> lichTrinh1 = new ArrayList<>();
        com.example.localcooking_v3t.model.LichTrinhLopHoc lt1 = new com.example.localcooking_v3t.model.LichTrinhLopHoc();
        lt1.setGioBatDau("14:00:00");
        lt1.setGioKetThuc("17:00:00");
        lt1.setDiaDiem("23 Lê Duẩn - Đà Nẵng");
        lt1.setSoLuongToiDa(20);
        lt1.setSoChoConTrong(8);
        lichTrinh1.add(lt1);
        lopChuaDienRa.setLichTrinhList(lichTrinh1);
        
        favoriteList.add(lopChuaDienRa);

        KhoaHoc lopDaDienRa = new KhoaHoc();
        lopDaDienRa.setTenKhoaHoc("Ẩm thực địa phương Sài Gòn");
        lopDaDienRa.setMoTa("Hành trình khám phá phố phường qua những món ăn đường phố nổi tiếng");
        lopDaDienRa.setGiaTien(750000.0);
        lopDaDienRa.setSaoTrungBinh(4.6f);
        lopDaDienRa.setSoLuongDanhGia(89);
        lopDaDienRa.setHinhAnh("hue.png");
        lopDaDienRa.setCoUuDai(false);
        lopDaDienRa.setDaDienRa(true);
        lopDaDienRa.setIsFavorite(true);
        
        // Tạo lịch trình cho lớp đã diễn ra
        List<com.example.localcooking_v3t.model.LichTrinhLopHoc> lichTrinh2 = new ArrayList<>();
        com.example.localcooking_v3t.model.LichTrinhLopHoc lt2 = new com.example.localcooking_v3t.model.LichTrinhLopHoc();
        lt2.setGioBatDau("09:00:00");
        lt2.setGioKetThuc("13:00:00");
        lt2.setDiaDiem("23 Lê Duẩn - Đà Nẵng");
        lt2.setSoLuongToiDa(20);
        lt2.setSoChoConTrong(0);
        lichTrinh2.add(lt2);
        lopDaDienRa.setLichTrinhList(lichTrinh2);
        
        favoriteList.add(lopDaDienRa);

        if (favoriteList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewFavorite.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewFavorite.setVisibility(View.VISIBLE);

            adapter = new ClassAdapter(favoriteList, null); // null vì không cần hiển thị ngày cụ thể
            recyclerViewFavorite.setAdapter(adapter);

            adapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
                @Override public void onDatLichClick(KhoaHoc lopHoc) { }
                @Override public void onChiTietClick(KhoaHoc lopHoc) {
                    DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc);
                    bottomSheet.show(getChildFragmentManager(), "DetailBottomSheet");
                }
                @Override public void onFavoriteClick(KhoaHoc lopHoc) { }
            });
        }
    }
}
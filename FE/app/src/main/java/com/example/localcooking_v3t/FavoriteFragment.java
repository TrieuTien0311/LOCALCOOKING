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

import com.example.localcooking_v3t.model.LopHoc;

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
        List<LopHoc> favoriteList = new ArrayList<>();

        // TODO: Load from API instead of hardcoded data
        LopHoc lopChuaDienRa = new LopHoc();
        lopChuaDienRa.setTenLop("Ẩm thực địa phương Huế");
        lopChuaDienRa.setMoTa("Khám phá tinh hoa ẩm thực cung đình với hương vị đặc trưng vị Huế");
        lopChuaDienRa.setThoiGian("14:00 - 17:00");
        lopChuaDienRa.setNgay("02/10/2025");
        lopChuaDienRa.setDiaDiem("23 Lê Duẩn - Đà Nẵng");
        lopChuaDienRa.setGia("715.000₫");
        lopChuaDienRa.setDanhGia(4.9f);
        lopChuaDienRa.setSoDanhGia(211);
        lopChuaDienRa.setHinhAnh("hue.png");
        lopChuaDienRa.setCoUuDai(true);
        lopChuaDienRa.setThoiGianKetThuc("23:30:00");
        lopChuaDienRa.setSuat(8);
        lopChuaDienRa.setDaDienRa(false);
        lopChuaDienRa.setIsFavorite(true);
        favoriteList.add(lopChuaDienRa);

        LopHoc lopDaDienRa = new LopHoc();
        lopDaDienRa.setTenLop("Ẩm thực địa phương Sài Gòn");
        lopDaDienRa.setMoTa("Hành trình khám phá phố phường qua những món ăn đường phố nổi tiếng");
        lopDaDienRa.setThoiGian("09:00 - 13:00");
        lopDaDienRa.setNgay("15/08/2025");
        lopDaDienRa.setDiaDiem("23 Lê Duẩn - Đà Nẵng");
        lopDaDienRa.setGia("750.000₫");
        lopDaDienRa.setDanhGia(4.6f);
        lopDaDienRa.setSoDanhGia(89);
        lopDaDienRa.setHinhAnh("hue.png");
        lopDaDienRa.setCoUuDai(false);
        lopDaDienRa.setThoiGianKetThuc("23:30:00");
        lopDaDienRa.setSuat(0);
        lopDaDienRa.setDaDienRa(true);
        lopDaDienRa.setIsFavorite(true);
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
                @Override public void onDatLichClick(LopHoc lopHoc) { }
                @Override public void onChiTietClick(LopHoc lopHoc) {
                    DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc);
                    bottomSheet.show(getChildFragmentManager(), "DetailBottomSheet");
                }
                @Override public void onFavoriteClick(LopHoc lopHoc) { }
            });
        }
    }
}
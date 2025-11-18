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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> danhSachLopHoc;
    private List<Class> danhSachGoc; // Lưu danh sách gốc
    private int currentSortType = ArrangeBottomSheet.MAC_DINH;
    private int currentMinCost = 500000;
    private int currentMaxCost = 3000000;

    public ClassesFragment() {
        // Required empty public constructor
    }

    public static ClassesFragment newInstance() {
        return new ClassesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewLopHoc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo dữ liệu mẫu
        taoDuLieuMau();

        // Khởi tạo adapter
        adapter = new ClassAdapter(danhSachLopHoc);
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện adapter
        adapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onDatLichClick(Class lopHoc) {
                Toast.makeText(getContext(), "Đặt lịch: " + lopHoc.getTenLop(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChiTietClick(Class lopHoc) {
                Toast.makeText(getContext(), "Chi tiết: " + lopHoc.getTenLop(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFavoriteClick(Class lopHoc) {
                Toast.makeText(getContext(), "Yêu thích: " + lopHoc.getTenLop(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý các nút lọc
        LinearLayout btnSapXep = view.findViewById(R.id.btnSapXep);
        LinearLayout btnThoiGian = view.findViewById(R.id.btnThoiGian);
        LinearLayout btnGiaCa = view.findViewById(R.id.btnGiaCa);

        btnSapXep.setOnClickListener(v -> showSapXepBottomSheet());

        btnThoiGian.setOnClickListener(v -> {
            TimeBottomSheet sheet = new TimeBottomSheet();
            sheet.setOnFilterAppliedListener((sh, sm, eh, em, sortType) -> {
                locTheoThoiGian(sh, sm, eh, em);
                sapXepDanhSach(sortType == 1 ? ArrangeBottomSheet.GIO_BAT_DAU_SOM_NHAT :
                        sortType == 2 ? ArrangeBottomSheet.GIO_BAT_DAU_MUON_NHAT :
                                ArrangeBottomSheet.MAC_DINH);
            });
            sheet.show(getChildFragmentManager(), "TimeFilter");
        });
        btnGiaCa.setOnClickListener(v -> {
            CostBottomSheet sheet = new CostBottomSheet();
            sheet.setOnFilterAppliedListener((minCost, maxCost, sortType) -> {
                currentMinCost = minCost;
                currentMaxCost = maxCost;

                // Lọc theo giá
                locTheoGia(minCost, maxCost);

                // Sắp xếp theo loại
                sapXepDanhSach(sortType == 1 ? ArrangeBottomSheet.GIA_GIAM_DAN :
                        sortType == 2 ? ArrangeBottomSheet.GIA_TANG_DAN :
                                ArrangeBottomSheet.MAC_DINH);
            });
            sheet.show(getChildFragmentManager(), "CostFilter");
        });

        return view;
    }

    private void taoDuLieuMau() {
        danhSachGoc = new ArrayList<>();
        danhSachGoc.add(new Class(
                "Ẩm thực địa phương Huế",
                "Khám phá tinh hoa ẩm thực cung đình với hương vị đặc trưng vị Huế",
                "14:00 - 17:00",
                "02/10/2025",
                "23 Lê Duẩn - Đà Nẵng",
                "715.000₫",
                4.9f,
                211,
                R.drawable.hue,
                true,
                "23:30:00",
                8
        ));

        danhSachGoc.add(new Class(
                "Ẩm thực địa phương Hà Nội",
                "Món ăn đậm đà, tinh tế, đậm nét văn hóa Thăng An",
                "14:00 - 18:30",
                "02/10/2025",
                "23 Lê Duẩn - Đà Nẵng",
                "700.000₫",
                5.0f,
                128,
                R.drawable.hue,
                true,
                "23:30:00",
                19
        ));

        danhSachGoc.add(new Class(
                "Ẩm thực địa phương Đà Nẵng",
                "Món ăn đậm đà, mộc mạc, hòa quyện hương vị của biển",
                "08:00 - 10:45",
                "02/10/2025",
                "23 Lê Duẩn - Đà Nẵng",
                "800.000₫",
                4.8f,
                109,
                R.drawable.hue,
                false,
                "",
                11
        ));

        danhSachGoc.add(new Class(
                "Ẩm thực địa phương Phú Yên",
                "Món ăn đậm đà, mộc mạc, hòa quyện hương vị của biển",
                "08:00 - 10:45",
                "02/10/2025",
                "23 Lê Duẩn - Đà Nẵng",
                "1.000.000₫",
                4.8f,
                109,
                R.drawable.hue,
                false,
                "",
                11
        ));

        // Copy sang danh sách hiển thị
        danhSachLopHoc = new ArrayList<>(danhSachGoc);
    }

    private void showSapXepBottomSheet() {
        ArrangeBottomSheet bottomSheet = ArrangeBottomSheet.newInstance(currentSortType);
        bottomSheet.setOnSapXepListener(loaiSapXep -> {
            currentSortType = loaiSapXep;
            sapXepDanhSach(loaiSapXep);
        });
        bottomSheet.show(getChildFragmentManager(), "ArrangeBottomSheet");
    }
    private void locTheoThoiGian(int startHour, int startMinute, int endHour, int endMinute) {
        int startMinutes = startHour * 60 + startMinute;
        int endMinutes = endHour * 60 + endMinute;

        ArrayList<Class> filtered = new ArrayList<>();
        for (Class lop : danhSachGoc) {
            String thoiGian = lop.getThoiGian(); // ví dụ: "14:00 - 17:00"
            try {
                String startStr = thoiGian.split("-")[0].trim(); // "14:00 "
                String[] parts = startStr.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                int classStartMinutes = hour * 60 + minute;

                if (classStartMinutes >= startMinutes && classStartMinutes <= endMinutes) {
                    filtered.add(lop);
                }
            } catch (Exception e) {
                // Nếu lỗi format thì vẫn thêm để tránh mất dữ liệu
                filtered.add(lop);
            }
        }

        danhSachLopHoc = filtered;
        adapter.updateData(danhSachLopHoc);
    }
    private void sapXepDanhSach(int loaiSapXep) {
        switch (loaiSapXep) {
            case ArrangeBottomSheet.MAC_DINH:
                danhSachLopHoc = new ArrayList<>(danhSachGoc);
                break;

            case ArrangeBottomSheet.GIO_BAT_DAU_SOM_NHAT:
                Collections.sort(danhSachLopHoc, new Comparator<Class>() {
                    @Override
                    public int compare(Class o1, Class o2) {
                        return o1.getThoiGian().compareTo(o2.getThoiGian());
                    }
                });
                break;

            case ArrangeBottomSheet.GIO_BAT_DAU_MUON_NHAT:
                Collections.sort(danhSachLopHoc, new Comparator<Class>() {
                    @Override
                    public int compare(Class o1, Class o2) {
                        return o2.getThoiGian().compareTo(o1.getThoiGian());
                    }
                });
                break;

            case ArrangeBottomSheet.DANH_GIA_CAO_NHAT:
                Collections.sort(danhSachLopHoc, new Comparator<Class>() {
                    @Override
                    public int compare(Class o1, Class o2) {
                        return Float.compare(o2.getDanhGia(), o1.getDanhGia());
                    }
                });
                break;

            case ArrangeBottomSheet.GIA_GIAM_DAN:
                Collections.sort(danhSachLopHoc, new Comparator<Class>() {
                    @Override
                    public int compare(Class o1, Class o2) {
                        return Double.compare(o2.getGiaSo(), o1.getGiaSo());
                    }
                });
                break;

            case ArrangeBottomSheet.GIA_TANG_DAN:
                Collections.sort(danhSachLopHoc, new Comparator<Class>() {
                    @Override
                    public int compare(Class o1, Class o2) {
                        return Double.compare(o1.getGiaSo(), o2.getGiaSo());
                    }
                });
                break;
        }

        adapter.updateData(danhSachLopHoc);
    }
    private void locTheoGia(int minCost, int maxCost) {
        ArrayList<Class> filtered = new ArrayList<>();
        for (Class lop : danhSachGoc) {
            try {
                // Lấy giá số từ chuỗi giá (ví dụ: "715.000₫" -> 715000)
                double giaSo = lop.getGiaSo();

                if (giaSo >= minCost && giaSo <= maxCost) {
                    filtered.add(lop);
                }
            } catch (Exception e) {
                // Nếu lỗi format thì vẫn thêm để tránh mất dữ liệu
                filtered.add(lop);
            }
        }

        danhSachLopHoc = filtered;
        adapter.updateData(danhSachLopHoc);
    }
}
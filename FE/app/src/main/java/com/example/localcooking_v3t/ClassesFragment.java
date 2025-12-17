package com.example.localcooking_v3t;

import android.content.Intent;
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
    private List<Class> danhSachGoc;
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

        recyclerView = view.findViewById(R.id.recyclerViewLopHoc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taoDuLieuMau();

        adapter = new ClassAdapter(danhSachLopHoc);
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện adapter
        adapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
            @Override
            public void onDatLichClick(Class lopHoc) {
                Intent intent = new Intent(getActivity(), Booking.class);
                intent.putExtra("lopHoc", lopHoc); // Truyền cả object, có luôn món ăn!
                startActivity(intent);
            }

            @Override
            public void onChiTietClick(Class lopHoc) {
                DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc);
                bottomSheet.show(getChildFragmentManager(), "DetailBottomSheet");
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
                locTheoGia(minCost, maxCost);
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

        // ========== LỚP HỌC ẨM THỰC HUẾ ==========
        Class lopHue = new Class(
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
        );

        lopHue.addFoodToCategory(0, new Food(
                "Nem lụi Huế",
                "Món nem thơm ngon đặc trưng xứ Huế với hương vị đậm đà từ thịt heo băm nhuyễn, nướng trên than hồng, ăn kèm bánh tráng và rau sống.",
                "Thịt heo vai, bột năng, tỏi băm, hành tím, mè rang, bánh tráng, rau sống, dưa leo.",
                R.drawable.banhbeo
        ));

        lopHue.addFoodToCategory(0, new Food(
                "Bánh bột lọc trần",
                "Bánh trong suốt với nhân tôm thịt thơm ngon, ăn kèm nước mắm chua ngọt đặc trưng.",
                "Bột năng, bột gạo, tôm tươi, thịt ba chỉ, hành khô, nước mắm, đường.",
                R.drawable.banhbeo
        ));

        lopHue.addFoodToCategory(1, new Food(
                "Bánh bèo chén",
                "Món bánh nhỏ xinh đặc trưng xứ Huế, với lớp bánh mềm mịn, tôm khô thơm và mỡ hành béo ngậy, ăn kèm nước mắm chua ngọt đậm đà.",
                "Bột gạo, bột năng, tôm khô giã nhuyễn, hành phi, tóp mỡ, mắm nêm hoặc nước mắm pha.",
                R.drawable.banhbeo
        ));

        lopHue.addFoodToCategory(1, new Food(
                "Bún bò Huế",
                "Món bún đặc sản với nước dùng đậm đà từ xương hầm, thơm mùi sả, ớt, mắm ruốc, ăn kèm chả, giò heo và rau thơm.",
                "Bún to, xương ống, xương heo, thịt bò, chả cua, giò heo, sả, mắm ruốc, ớt, hành, ngò.",
                R.drawable.hue
        ));

        lopHue.addFoodToCategory(1, new Food(
                "Cơm hến",
                "Món cơm đặc sản Huế với hến xào thơm, ăn kèm rau sống, đậu phộng rang, mè và nước mắm chua ngọt.",
                "Cơm nguội, hến, tỏi phi, ớt, rau muống, rau thơm, đậu phộng rang, mè rang, mắm nêm.",
                R.drawable.banhbeo
        ));

        lopHue.addFoodToCategory(2, new Food(
                "Chè sen long nhãn",
                "Món chè thanh mát với hạt sen bùi béo, long nhãn ngọt thanh, nước đường trong vắt.",
                "Hạt sen tươi, long nhãn, đường phèn, nước cốt dừa, đá bào.",
                R.drawable.banhbeo
        ));

        lopHue.addFoodToCategory(2, new Food(
                "Chè Huế",
                "Món chè truyền thống với nhiều lớp nguyên liệu đa dạng, ngọt thanh, mát lạnh.",
                "Đậu xanh, đậu đỏ, hạt sen, thạch, trân châu, nước cốt dừa, đường phèn.",
                R.drawable.banhbeo
        ));

        danhSachGoc.add(lopHue);

        // ========== LỚP HỌC ẨM THỰC HÀ NỘI ==========
        Class lopHanoi = new Class(
                "Ẩm thực địa phương Hà Nội",
                "Món ăn đậm đà, tinh tế, đậm nét văn hóa Thăng Long",
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
        );

        lopHanoi.addFoodToCategory(0, new Food(
                "Chả cá Lã Vọng",
                "Món chả cá trứ danh Hà Nội với cá lăng tẩm gia vị, chiên thơm, ăn kèm bún, thì là và đậu phộng rang.",
                "Cá lăng, nghệ tươi, mắm tôm, dầu ăn, bún, thì là, hành lá, đậu phộng rang.",
                R.drawable.banhbeo
        ));

        lopHanoi.addFoodToCategory(0, new Food(
                "Nem rán Hà Nội",
                "Nem giòn rụm với nhân thịt, miến, mộc nhĩ thơm ngon, ăn kèm rau sống và nước mắm pha.",
                "Thịt heo băm, miến, mộc nhĩ, cà rót, bánh đa nem, rau sống, bún, nước mắm.",
                R.drawable.banhbeo
        ));

        lopHanoi.addFoodToCategory(0, new Food(
                "Phở cuốn",
                "Món ăn nhẹ với bánh phở cuộn thịt bò, rau thơm, ăn kèm nước chấm đậm đà.",
                "Bánh phở tươi, thịt bò xào, rau sống, đồ chua, nước mắm pha chua ngọt.",
                R.drawable.banhbeo
        ));

        lopHanoi.addFoodToCategory(1, new Food(
                "Phở Hà Nội",
                "Món phở truyền thống với nước dùng trong veo từ xương hầm, bánh phở dai mềm, thịt bò mềm ngọt.",
                "Bánh phở, xương bò, thịt bò, hành tây, gừng, hồi, quế, thảo quả, hành lá, ngò, chanh, ớt.",
                R.drawable.hue
        ));

        lopHanoi.addFoodToCategory(1, new Food(
                "Bún chả Hà Nội",
                "Món ăn đặc sản với chả nướng thơm, bún trắng, ăn kèm nước mắm chua ngọt và rau sống.",
                "Thịt heo ba chỉ, thịt nạc vai, bún, nước mắm, đường, tỏi, ớt, rau sống, dưa leo.",
                R.drawable.banhbeo
        ));

        lopHanoi.addFoodToCategory(1, new Food(
                "Bún ốc Hà Nội",
                "Món bún với nước dùng chua cay từ cà chua, ốc luộc thơm ngon, ăn kèm rau thơm.",
                "Bún, ốc nhồi, cà chua, dấm bỗng, me, mắm tôm, đậu hũ chiên, rau thơm.",
                R.drawable.banhbeo
        ));

        lopHanoi.addFoodToCategory(2, new Food(
                "Chè bưởi",
                "Món chè thanh mát với múi bưởi giòn, nước đường thanh mát, tô điểm bởi nước cốt dừa béo ngậy.",
                "Bưởi, đường phèn, nước cốt dừa, nước lá dứa, đá bào.",
                R.drawable.banhbeo
        ));

        lopHanoi.addFoodToCategory(2, new Food(
                "Sữa chua nếp cẩm",
                "Món tráng miệng độc đáo với sữa chua chua ngọt, nếp cẩm dẻo thơm và nước cốt dừa béo.",
                "Sữa chua, nếp cẩm, đường, nước cốt dừa, bột sắn dây.",
                R.drawable.banhbeo
        ));

        danhSachGoc.add(lopHanoi);

        // ========== LỚP HỌC ẨM THỰC ĐÀ NẴNG ==========
        Class lopDaNang = new Class(
                "Ẩm thực địa phương Đà Nẵng",
                "Hương vị miền Trung đậm đà với biển cả: mì Quảng, bún chả cá, bánh tráng cuốn thịt heo",
                "09:00 - 12:30",
                "03/10/2025",
                "23 Lê Duẩn - Đà Nẵng",
                "680.000₫",
                4.8f,
                189,
                R.drawable.hue,
                false,
                "23:30:00",
                12
        );

        lopDaNang.addFoodToCategory(0, new Food(
                "Bánh tráng cuốn thịt heo",
                "Món cuốn đặc trưng Đà Nẵng với thịt heo luộc, bánh tráng Đại Lộc, rau sống tươi mát và nước mắm nêm đậm đà.",
                "Thịt ba chỉ, bánh tráng, rau sống (xà lách, húng, diếp cá...), mắm nêm, tỏi, ớt, dứa.",
                R.drawable.banhbeo
        ));

        lopDaNang.addFoodToCategory(1, new Food(
                "Mì Quảng",
                "Món mì đặc sản với nước dùng đậm đà, tôm, thịt heo, trứng cút, ăn kèm bánh tráng giòn và rau sống.",
                "Mì sợi to, tôm tươi, thịt heo, trứng cút, đậu phộng rang, hành phi, bánh tráng, rau sống.",
                R.drawable.hue
        ));

        lopDaNang.addFoodToCategory(2, new Food(
                "Kem bơ Đà Nẵng",
                "Món kem bơ nổi tiếng với bơ sáp dẻo mịn, cốt dừa béo ngậy và chút muối lạc rang.",
                "Bơ sáp, sữa đặc, nước cốt dừa, đá bào, đậu phộng rang.",
                R.drawable.banhbeo
        ));

        danhSachGoc.add(lopDaNang);

        // ========== LỚP HỌC ẨM THỰC PHÚ YÊN ==========
        Class lopPhuYen = new Class(
                "Ẩm thực địa phương Phú Yên",
                "Khám phá hương vị biển khơi: mắt cá ngừ đại dương, bánh ướt thịt nướng, chả dông",
                "14:00 - 17:30",
                "04/10/2025",
                "23 Lê Duẩn - Đà Nẵng",
                "650.000₫",
                4.7f,
                93,
                R.drawable.hue,
                false,
                "23:30:00",
                10
        );

        lopPhuYen.addFoodToCategory(0, new Food(
                "Bánh ướt thịt nướng Phú Yên",
                "Bánh ướt mỏng mềm cuốn cùng thịt heo nướng thơm lừng, rau sống và nước mắm chua ngọt.",
                "Bánh ướt, thịt heo nướng lá chuối, rau sống, hành phi, nước mắm tỏi ớt.",
                R.drawable.banhbeo
        ));

        lopPhuYen.addFoodToCategory(1, new Food(
                "Mắt cá ngừ đại dương nấu tiêu xanh",
                "Món ăn thượng hạng từ mắt cá ngừ đại dương hầm mềm với tiêu xanh thơm nồng, bổ dưỡng.",
                "Mắt cá ngừ đại dương, tiêu xanh, gừng, hành tím, nước mắm, gia vị.",
                R.drawable.hue
        ));

        lopPhuYen.addFoodToCategory(2, new Food(
                "Bánh hồng Phú Yên",
                "Món bánh hồng dẻo ngọt từ nếp cái hoa vàng và dừa nạo, đặc sản làm quà nổi tiếng.",
                "Nếp cái hoa vàng, dừa nạo, đường, gừng, lá dứa.",
                R.drawable.banhbeo
        ));

        danhSachGoc.add(lopPhuYen);

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
            String thoiGian = lop.getThoiGian();
            try {
                String startStr = thoiGian.split("-")[0].trim();
                String[] parts = startStr.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                int classStartMinutes = hour * 60 + minute;

                if (classStartMinutes >= startMinutes && classStartMinutes <= endMinutes) {
                    filtered.add(lop);
                }
            } catch (Exception e) {
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
                double giaSo = lop.getGiaSo();

                if (giaSo >= minCost && giaSo <= maxCost) {
                    filtered.add(lop);
                }
            } catch (Exception e) {
                filtered.add(lop);
            }
        }

        danhSachLopHoc = filtered;
        adapter.updateData(danhSachLopHoc);
    }
}
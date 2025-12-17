package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Booking extends AppCompatActivity {

    // === TextViews ===
    private TextView txtDiaDiem, txtThoiGian, txtTenLop, txtGiaTien, txtGiaCu;
    private TextView txtDiem, txtSLDanhGia, txtDiaDiemFull, txtGioiHan;
    private TextView txtSoLuongDat, btnChiTiet;
    private View gachChan;

    // === ImageViews ===
    private ImageView btnBack, btnGiam, btnTang, imMonAn, icFav, btnDownTeacher;

    private ImageView  btnPre, btnNext;

    // === Buttons ===
    private Button btnDatLich, tagGiamGia;

    // === LinearLayouts ===
    private LinearLayout txtAn,ChiTiet;

    // === Biến trạng thái ===
    private boolean isExpanded = false;
    private int soLuongDat = 1;
    private int suatConLai;
    private double giaSo;

    // === Dữ liệu lớp học ===
    private Class lopHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ views
        initViews();

        // Nhận dữ liệu từ Intent
        nhanDuLieuTuIntent();

        // Xử lý sự kiện
        xuLySuKien();
    }

    private void initViews() {
        // === TextViews ===
        txtDiaDiem = findViewById(R.id.txtDiaDiem);
        txtThoiGian = findViewById(R.id.txtThoiGian);
        txtTenLop = findViewById(R.id.txt_TenLop_DL);
        txtGiaTien = findViewById(R.id.txt_GiaTien_DL);
        txtGiaCu = findViewById(R.id.txt_GiaCu_DL);
        txtDiem = findViewById(R.id.txt_Diem_DL);
        txtSLDanhGia = findViewById(R.id.txt_SLDanhGia_DL);
        txtDiaDiemFull = findViewById(R.id.txt_DiaDiem_DL);
        txtGioiHan = findViewById(R.id.txt_GioiHan_DL);
        txtSoLuongDat = findViewById(R.id.txt_SoLuongDat_DL);
        btnChiTiet = findViewById(R.id.btnChiTiet);


        // === ImageViews ===
        btnBack = findViewById(R.id.btnBack);
        imMonAn = findViewById(R.id.im_MonAn_DL);
        icFav = findViewById(R.id.ic_fav_DL);
        btnDownTeacher = findViewById(R.id.btnDownTeacher);
        btnGiam = findViewById(R.id.btn_Giam_DL);
        btnTang = findViewById(R.id.btn_Tang_DL);



        btnPre = findViewById(R.id.btnPre);
        btnNext = findViewById(R.id.btnNext);

        // === Buttons ===
        btnDatLich = findViewById(R.id.btn_DatLich_DL);
        tagGiamGia = findViewById(R.id.tagGiamGia);

        // === LinearLayouts ===
        txtAn = findViewById(R.id.txtAn);
        ChiTiet = findViewById(R.id.ChiTiet);
        gachChan=findViewById(R.id.gachChan);

        // === Ẩn các view không cần thiết ban đầu ===
        if (txtGiaCu != null) txtGiaCu.setVisibility(View.GONE);
        if (tagGiamGia != null) tagGiamGia.setVisibility(View.GONE);
        if (txtAn != null) txtAn.setVisibility(View.GONE);
    }

    private void nhanDuLieuTuIntent() {
        // === NHẬN TOÀN BỘ OBJECT CLASS ===
        lopHoc = getIntent().getParcelableExtra("lopHoc");

        if (lopHoc != null) {
            // === Hiển thị dữ liệu cơ bản ===
            txtDiaDiem.setText(lopHoc.getTenLop());
            txtThoiGian.setText(lopHoc.getThoiGian() + " - " + formatNgay(lopHoc.getNgay()));
            txtTenLop.setText(lopHoc.getTenLop());
            txtGiaTien.setText(lopHoc.getGia());
            txtDiem.setText(String.valueOf(lopHoc.getDanhGia()));
            txtSLDanhGia.setText("(" + lopHoc.getSoDanhGia() + " đánh giá)");
            txtDiaDiemFull.setText(lopHoc.getDiaDiem());
            txtGioiHan.setText("Còn " + lopHoc.getSuat() + " suất");
            txtSoLuongDat.setText(String.valueOf(soLuongDat));

            // === Hình ảnh ===
            if (lopHoc.getHinhAnh() != 0) {
                imMonAn.setImageResource(lopHoc.getHinhAnh());
            }

            // === Xử lý ưu đãi ===
            if (lopHoc.isCoUuDai()) {
                if (txtGiaCu != null) {
                    txtGiaCu.setVisibility(View.VISIBLE);
                    double giaCu = lopHoc.getGiaSo() / 0.79; // Giả sử giảm 21%
                    txtGiaCu.setText(String.format("%.0f₫", giaCu));
                }
                if (tagGiamGia != null) {
                    tagGiamGia.setVisibility(View.VISIBLE);
                    tagGiamGia.setText("-21%");
                }

            } else {
                if (txtGiaCu != null) txtGiaCu.setVisibility(View.INVISIBLE);
                if (tagGiamGia != null) tagGiamGia.setVisibility(View.INVISIBLE);
                gachChan.setVisibility(View.INVISIBLE);
            }

            // === Lưu giá số và suất còn lại ===
            giaSo = lopHoc.getGiaSo();
            suatConLai = lopHoc.getSuat();

            // === Cập nhật trạng thái favorite ===
            updateFavoriteIcon();

        } else {
            // Fallback nếu không nhận được dữ liệu
            // Có thể hiển thị Toast hoặc finish()
            android.util.Log.e("Booking", "Không nhận được dữ liệu lớp học từ Intent");
        }
    }


    private void updateFavoriteIcon() {
        if (icFav != null && lopHoc != null) {
            if (lopHoc.isFavorite()) {
                icFav.setImageResource(R.drawable.ic_heartredfilled);
                icFav.setColorFilter(null);
            } else {
                icFav.setImageResource(R.drawable.ic_heart);
                icFav.setColorFilter(0x7F7F7F7F); // Màu xám
            }
        }
    }

    private void xuLySuKien() {
        // === Nút quay lại ===
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // === Nút giảm số lượng ===
        if (btnGiam != null) {
            btnGiam.setOnClickListener(v -> {
                if (soLuongDat > 1) {
                    soLuongDat--;
                    txtSoLuongDat.setText(String.valueOf(soLuongDat));
                }
            });
        }

        // === Nút tăng số lượng ===
        if (btnTang != null) {
            btnTang.setOnClickListener(v -> {
                if (soLuongDat < suatConLai) {
                    soLuongDat++;
                    txtSoLuongDat.setText(String.valueOf(soLuongDat));
                }
            });
        }

        // === Nút đặt lịch ===
        if (btnDatLich != null) {
            btnDatLich.setOnClickListener(v -> {
                Intent paymentIntent = new Intent(Booking.this, Payment.class);
                if (lopHoc != null) {
                    paymentIntent.putExtra("lopHoc", lopHoc);           // Truyền toàn bộ lớp học
                    paymentIntent.putExtra("soLuongDat", soLuongDat);   // Số lượng người đặt
                    paymentIntent.putExtra("tongTien", giaSo * soLuongDat); // Tổng tiền thực tế
                }
                startActivity(paymentIntent);
            });
        }

        // === Icon favorite ===
        if (icFav != null && lopHoc != null) {
            icFav.setOnClickListener(v -> {
                // Toggle favorite
                lopHoc.setFavorite(!lopHoc.isFavorite());
                updateFavoriteIcon();

                // Có thể thông báo cho database hoặc SharedPreferences
                android.util.Log.d("Booking", "Favorite toggled: " + lopHoc.isFavorite());
            });
        }

        // === Nút Chi tiết header ===
        if (btnChiTiet != null) {
            btnChiTiet.setOnClickListener(v -> showDetailBottomSheet());
        }
        if (ChiTiet != null) {
            ChiTiet.setOnClickListener(v -> showDetailBottomSheet());
        }

        // === Nút expand/collapse giáo viên ===
        if (btnDownTeacher != null && txtAn != null) {
            btnDownTeacher.setOnClickListener(v -> {
                if (isExpanded) {
                    btnDownTeacher.animate().rotation(0).setDuration(300).start();
                    txtAn.setVisibility(View.GONE);
                    isExpanded = false;
                } else {
                    btnDownTeacher.animate().rotation(180).setDuration(300).start();
                    txtAn.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }
            });
        }

        // === Nút Previous/Next (nếu có carousel) ===
        if (btnPre != null && btnNext != null) {
            btnPre.setOnClickListener(v -> {
                // TODO: Xử lý previous image
            });
            btnNext.setOnClickListener(v -> {
                // TODO: Xử lý next image
            });
        }
    }

    // === Mở DetailBottomSheet với toàn bộ dữ liệu ===
    private void showDetailBottomSheet() {
        if (lopHoc != null) {
            DetailBottomSheet bottomSheet = DetailBottomSheet.newInstance(lopHoc);
            bottomSheet.show(getSupportFragmentManager(), "DetailBottomSheet");
        } else {
            // Fallback nếu không có dữ liệu
            android.util.Log.w("Booking", "Không có dữ liệu lớp học để hiển thị chi tiết");
        }
    }

    private String formatNgay(String ngay) {
        // Chuyển đổi format ngày nếu cần
        // Ví dụ: "02/10/2025" -> "T5, 02/10/2025"
        return "T5, " + ngay;
    }

    // === Helper methods để debug ===
    public Class getLopHoc() {
        return lopHoc;
    }

    public int getSoLuongDat() {
        return soLuongDat;
    }
}
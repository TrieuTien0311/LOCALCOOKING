package com.example.localcooking_v3t;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.DanhMucMonAn;
import com.example.localcooking_v3t.model.GiaoVien;
import com.example.localcooking_v3t.model.KhoaHoc;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDescriptionFragment extends Fragment {
    private static final String TAG = "DetailDescriptionFrag";

    private ImageView btnDownTeacher, imgGiaoVien;
    private LinearLayout txtAn;
    private TextView txtTenGV, txtMoTaGV, txtKinhNghiem, txtLichSuKinhNghiem;
    private TextView txtGioiThieu, txtGiaTriBuoiHoc;
    private boolean isExpanded = false;
    private RecyclerView rcvCategories;
    private CategoryAdapter categoryAdapter;
    private KhoaHoc lopHoc;

    public DetailDescriptionFragment() {
        // Required empty public constructor
    }

    public static DetailDescriptionFragment newInstance(KhoaHoc lopHoc) {
        DetailDescriptionFragment fragment = new DetailDescriptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.lopHoc = lopHoc;
        return fragment;
    }
    
    public void setLopHoc(KhoaHoc lopHoc) {
        this.lopHoc = lopHoc;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_description, container, false);

        // Khởi tạo các view
        btnDownTeacher = view.findViewById(R.id.btnDownTeacher);
        txtAn = view.findViewById(R.id.txtAn);
        rcvCategories = view.findViewById(R.id.rcvCategories);
        imgGiaoVien = view.findViewById(R.id.imgGiaoVien);
        txtTenGV = view.findViewById(R.id.txtTenGV);
        txtMoTaGV = view.findViewById(R.id.txtMoTaGV);
        txtKinhNghiem = view.findViewById(R.id.txtKinhNghiem);
        txtLichSuKinhNghiem = view.findViewById(R.id.txtLichSuKinhNghiem);
        txtGioiThieu = view.findViewById(R.id.txtGioiThieu);
        txtGiaTriBuoiHoc = view.findViewById(R.id.txtGiaTriBuoiHoc);

        // Xử lý sự kiện click expand/collapse thông tin giáo viên
        btnDownTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    btnDownTeacher.animate().rotation(0).setDuration(300).start();
                    txtAn.setVisibility(View.GONE);
                    isExpanded = false;
                } else {
                    btnDownTeacher.animate().rotation(180).setDuration(300).start();
                    txtAn.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        rcvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        // Hiển thị thông tin lớp học
        if (lopHoc != null) {
            displayLopHocInfo();
            
            // Gọi API để lấy danh mục món ăn
            if (lopHoc.getMaKhoaHoc() != null) {
                loadDanhMucMonAn(lopHoc.getMaKhoaHoc());
            }
            
            // Gọi API để lấy thông tin giáo viên
            loadGiaoVienInfo();
        }
    }
    
    private void displayLopHocInfo() {
        // Hiển thị giới thiệu
        if (lopHoc.getGioiThieu() != null && !lopHoc.getGioiThieu().isEmpty()) {
            txtGioiThieu.setText(lopHoc.getGioiThieu());
        }
        
        // Hiển thị giá trị sau buổi học
        if (lopHoc.getGiaTriSauBuoiHoc() != null && !lopHoc.getGiaTriSauBuoiHoc().isEmpty()) {
            txtGiaTriBuoiHoc.setText(lopHoc.getGiaTriSauBuoiHoc());
        }
    }
    
    private void loadGiaoVienInfo() {
        // Lấy maGiaoVien từ lịch trình đầu tiên
        if (lopHoc.getLichTrinhList() != null && !lopHoc.getLichTrinhList().isEmpty()) {
            Integer maGiaoVien = lopHoc.getLichTrinhList().get(0).getMaGiaoVien();
            
            if (maGiaoVien != null) {
                RetrofitClient.getApiService().getGiaoVienById(maGiaoVien)
                    .enqueue(new Callback<GiaoVien>() {
                        @Override
                        public void onResponse(Call<GiaoVien> call, Response<GiaoVien> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                displayGiaoVienInfo(response.body());
                            } else {
                                Log.e(TAG, "Failed to load teacher info: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<GiaoVien> call, Throwable t) {
                            Log.e(TAG, "Error loading teacher info", t);
                        }
                    });
            }
        }
    }
    
    private void displayGiaoVienInfo(GiaoVien giaoVien) {
        if (giaoVien.getHoTen() != null) {
            txtTenGV.setText(giaoVien.getHoTen());
        }
        
        if (giaoVien.getChuyenMon() != null) {
            txtMoTaGV.setText(giaoVien.getChuyenMon());
        }
        
        // Hiển thị kinh nghiệm từ API
        if (giaoVien.getKinhNghiem() != null && !giaoVien.getKinhNghiem().isEmpty()) {
            txtKinhNghiem.setText(giaoVien.getKinhNghiem());
        } else {
            txtKinhNghiem.setText("Chưa có thông tin kinh nghiệm");
        }
        
        if (giaoVien.getLichSuKinhNghiem() != null) {
            txtLichSuKinhNghiem.setText(giaoVien.getLichSuKinhNghiem());
        }
        
        // Load hình ảnh giáo viên từ API
        if (giaoVien.getHinhAnh() != null && !giaoVien.getHinhAnh().isEmpty()) {
            loadGiaoVienImage(giaoVien.getHinhAnh());
        } else {
            // Sử dụng ảnh mặc định nếu không có
            imgGiaoVien.setImageResource(R.drawable.giaovien1);
        }
    }
    
    /**
     * Load hình ảnh giáo viên từ drawable resource
     * @param hinhAnh Tên file ảnh (VD: "giaovien1.png" hoặc "giaovien1")
     */
    private void loadGiaoVienImage(String hinhAnh) {
        if (getContext() == null) return;
        
        try {
            // Loại bỏ extension nếu có
            String imageName = hinhAnh.replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
            
            // Lấy resource ID từ tên file
            int resourceId = getContext().getResources().getIdentifier(
                imageName, 
                "drawable", 
                getContext().getPackageName()
            );
            
            if (resourceId != 0) {
                imgGiaoVien.setImageResource(resourceId);
            } else {
                // Nếu không tìm thấy, dùng ảnh mặc định
                Log.w(TAG, "Image not found: " + imageName + ", using default");
                imgGiaoVien.setImageResource(R.drawable.giaovien1);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading teacher image", e);
            imgGiaoVien.setImageResource(R.drawable.giaovien1);
        }
    }

    private void loadDanhMucMonAn(Integer maLopHoc) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<DanhMucMonAn>> call = apiService.getDanhMucMonAnByKhoaHoc(maLopHoc);

        call.enqueue(new Callback<List<DanhMucMonAn>>() {
            @Override
            public void onResponse(Call<List<DanhMucMonAn>> call, Response<List<DanhMucMonAn>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DanhMucMonAn> danhMucList = response.body();
                    
                    // Cập nhật adapter với dữ liệu từ API
                    categoryAdapter = new CategoryAdapter(danhMucList);
                    rcvCategories.setAdapter(categoryAdapter);
                    
                    Log.d(TAG, "Loaded " + danhMucList.size() + " categories");
                } else {
                    Log.e(TAG, "Response not successful: " + response.code());
                    Toast.makeText(getContext(), "Không thể tải danh mục món ăn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DanhMucMonAn>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
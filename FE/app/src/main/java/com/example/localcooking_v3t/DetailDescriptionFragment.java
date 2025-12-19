package com.example.localcooking_v3t;

import android.os.Bundle;
import android.util.Log;
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
import com.example.localcooking_v3t.model.LopHoc;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDescriptionFragment extends Fragment {

    private ImageView btnDownTeacher;
    private LinearLayout txtAn;
    private boolean isExpanded = false;
    private RecyclerView rcvCategories;
    private CategoryAdapter categoryAdapter;
    private LopHoc lopHoc;

    public DetailDescriptionFragment() {
        // Required empty public constructor
    }

    public static DetailDescriptionFragment newInstance(LopHoc lopHoc) {
        DetailDescriptionFragment fragment = new DetailDescriptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.lopHoc = lopHoc;
        return fragment;
    }
    
    public void setLopHoc(LopHoc lopHoc) {
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

        // Gọi API để lấy danh mục món ăn theo lớp học
        if (lopHoc != null && lopHoc.getMaLopHoc() != null) {
            loadDanhMucMonAn(lopHoc.getMaLopHoc());
        }
    }

    private void loadDanhMucMonAn(Integer maLopHoc) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<DanhMucMonAn>> call = apiService.getDanhMucMonAnByLopHoc(maLopHoc);

        call.enqueue(new Callback<List<DanhMucMonAn>>() {
            @Override
            public void onResponse(Call<List<DanhMucMonAn>> call, Response<List<DanhMucMonAn>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DanhMucMonAn> danhMucList = response.body();
                    
                    // Cập nhật adapter với dữ liệu từ API
                    categoryAdapter = new CategoryAdapter(danhMucList);
                    rcvCategories.setAdapter(categoryAdapter);
                    
                    Log.d("DetailDescription", "Loaded " + danhMucList.size() + " categories");
                } else {
                    Log.e("DetailDescription", "Response not successful: " + response.code());
                    Toast.makeText(getContext(), "Không thể tải danh mục món ăn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DanhMucMonAn>> call, Throwable t) {
                Log.e("DetailDescription", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
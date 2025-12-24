package com.example.localcooking_v3t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.DanhGiaDTO;
import com.example.localcooking_v3t.model.HinhAnhDanhGiaDTO;
import com.example.localcooking_v3t.model.ThongKeDanhGiaDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEvaluateFragment extends Fragment {

    private static final String TAG = "DetailEvaluateFragment";
    private static final String ARG_MA_KHOA_HOC = "maKhoaHoc";

    private Integer maKhoaHoc;
    private ApiService apiService;

    // Views thống kê
    private TextView txtSao1;
    private ProgressBar prb1sao, prb2sao, prb3sao, prb4sao, prb5sao;
    private TextView txtSLNhanXet, txtSLHinhAnh, txtFilterSaoValue;
    private TextView txtEmpty;

    // Filter buttons
    private LinearLayout btnFilterNhanXet, btnFilterHinhAnh, btnFilterSao;
    private String currentFilter = null;
    private Integer currentSao = null;

    // RecyclerView đánh giá
    private RecyclerView recyclerDanhGia;
    private DanhGiaAdapter danhGiaAdapter;
    private List<DanhGiaDTO> danhGiaList = new ArrayList<>();

    public DetailEvaluateFragment() {}

    public static DetailEvaluateFragment newInstance(Integer maKhoaHoc) {
        DetailEvaluateFragment fragment = new DetailEvaluateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MA_KHOA_HOC, maKhoaHoc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maKhoaHoc = getArguments().getInt(ARG_MA_KHOA_HOC, -1);
        }
        apiService = RetrofitClient.getApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_evaluate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupFilterButtons();

        Log.d(TAG, "maKhoaHoc = " + maKhoaHoc);

        if (maKhoaHoc != null && maKhoaHoc > 0) {
            loadThongKeDanhGia();
            loadDanhGia();
        } else {
            showEmpty("Không có thông tin khóa học");
        }
    }

    private void initViews(View view) {
        txtSao1 = view.findViewById(R.id.txtSao1);

        prb1sao = view.findViewById(R.id.prb1sao);
        prb2sao = view.findViewById(R.id.prb2sao);
        prb3sao = view.findViewById(R.id.prb3sao);
        prb4sao = view.findViewById(R.id.prb4sao);
        prb5sao = view.findViewById(R.id.prb5sao);

        txtSLNhanXet = view.findViewById(R.id.txtSLNhanXet);
        txtSLHinhAnh = view.findViewById(R.id.txtSLHinhAnh);
        txtFilterSaoValue = view.findViewById(R.id.txtFilterSaoValue);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        btnFilterNhanXet = view.findViewById(R.id.btnFilterNhanXet);
        btnFilterHinhAnh = view.findViewById(R.id.btnFilterHinhAnh);
        btnFilterSao = view.findViewById(R.id.btnFilterSao);

        recyclerDanhGia = view.findViewById(R.id.recyclerDanhGia);
    }

    private void setupRecyclerView() {
        recyclerDanhGia.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerDanhGia.setNestedScrollingEnabled(false);

        danhGiaAdapter = new DanhGiaAdapter(danhGiaList, requireContext());
        danhGiaAdapter.setOnMediaClickListener((mediaList, position) -> {
            openMediaViewer(mediaList, position);
        });
        recyclerDanhGia.setAdapter(danhGiaAdapter);
    }

    private void setupFilterButtons() {
        if (btnFilterNhanXet != null) {
            btnFilterNhanXet.setOnClickListener(v -> {
                currentFilter = "co_nhan_xet".equals(currentFilter) ? null : "co_nhan_xet";
                currentSao = null;
                loadDanhGiaWithFilter();
                updateFilterButtonStyles();
            });
        }

        if (btnFilterHinhAnh != null) {
            btnFilterHinhAnh.setOnClickListener(v -> {
                currentFilter = "co_hinh_anh".equals(currentFilter) ? null : "co_hinh_anh";
                currentSao = null;
                loadDanhGiaWithFilter();
                updateFilterButtonStyles();
            });
        }

        if (btnFilterSao != null) {
            btnFilterSao.setOnClickListener(v -> showStarFilterDialog());
        }
    }

    private void updateFilterButtonStyles() {
        // Reset all backgrounds
        if (btnFilterNhanXet != null) {
            btnFilterNhanXet.setBackgroundResource(
                    "co_nhan_xet".equals(currentFilter) ? R.drawable.bg_filter_orange : R.drawable.bg_filter_gray);
        }
        if (btnFilterHinhAnh != null) {
            btnFilterHinhAnh.setBackgroundResource(
                    "co_hinh_anh".equals(currentFilter) ? R.drawable.bg_filter_orange : R.drawable.bg_filter_gray);
        }
        if (txtFilterSaoValue != null) {
            txtFilterSaoValue.setText(currentSao != null ? "(" + currentSao + " sao)" : "(Tất cả)");
        }
    }

    private void showStarFilterDialog() {
        String[] options = {"Tất cả", "5 sao", "4 sao", "3 sao", "2 sao", "1 sao"};
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Lọc theo số sao")
                .setItems(options, (dialog, which) -> {
                    currentFilter = null;
                    currentSao = which == 0 ? null : (6 - which);
                    loadDanhGiaWithFilter();
                    updateFilterButtonStyles();
                })
                .show();
    }

    private void loadThongKeDanhGia() {
        Log.d(TAG, "Loading thong ke for maKhoaHoc: " + maKhoaHoc);

        apiService.getThongKeDanhGia(maKhoaHoc).enqueue(new Callback<ThongKeDanhGiaDTO>() {
            @Override
            public void onResponse(Call<ThongKeDanhGiaDTO> call, Response<ThongKeDanhGiaDTO> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Thong ke loaded successfully");
                    displayThongKe(response.body());
                } else {
                    Log.e(TAG, "Failed to load thong ke: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ThongKeDanhGiaDTO> call, Throwable t) {
                Log.e(TAG, "Failed to load thong ke", t);
            }
        });
    }

    private void displayThongKe(ThongKeDanhGiaDTO thongKe) {
        // Điểm trung bình
        if (txtSao1 != null) {
            Double diem = thongKe.getDiemTrungBinh();
            txtSao1.setText(String.format("%.1f", diem != null ? diem : 0.0));
        }

        // Progress bars
        if (prb1sao != null) prb1sao.setProgress(thongKe.getPercent1Sao());
        if (prb2sao != null) prb2sao.setProgress(thongKe.getPercent2Sao());
        if (prb3sao != null) prb3sao.setProgress(thongKe.getPercent3Sao());
        if (prb4sao != null) prb4sao.setProgress(thongKe.getPercent4Sao());
        if (prb5sao != null) prb5sao.setProgress(thongKe.getPercent5Sao());

        // Số lượng
        if (txtSLNhanXet != null) {
            Integer sl = thongKe.getSoLuongCoNhanXet();
            txtSLNhanXet.setText("(" + (sl != null ? sl : 0) + ")");
        }
        if (txtSLHinhAnh != null) {
            Integer sl = thongKe.getSoLuongCoHinhAnh();
            txtSLHinhAnh.setText("(" + (sl != null ? sl : 0) + ")");
        }
    }

    private void loadDanhGia() {
        Log.d(TAG, "Loading danh gia for maKhoaHoc: " + maKhoaHoc);

        apiService.getDanhGiaByKhoaHoc(maKhoaHoc).enqueue(new Callback<List<DanhGiaDTO>>() {
            @Override
            public void onResponse(Call<List<DanhGiaDTO>> call, Response<List<DanhGiaDTO>> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    danhGiaList.clear();
                    danhGiaList.addAll(response.body());
                    danhGiaAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + danhGiaList.size() + " reviews");

                    if (danhGiaList.isEmpty()) {
                        showEmpty("Chưa có đánh giá nào");
                    } else {
                        hideEmpty();
                    }
                } else {
                    Log.e(TAG, "Failed to load danh gia: " + response.code());
                    showEmpty("Không thể tải đánh giá");
                }
            }

            @Override
            public void onFailure(Call<List<DanhGiaDTO>> call, Throwable t) {
                Log.e(TAG, "Failed to load danh gia", t);
                if (isAdded()) {
                    showEmpty("Lỗi kết nối");
                }
            }
        });
    }

    private void loadDanhGiaWithFilter() {
        Log.d(TAG, "Loading filtered danh gia: filter=" + currentFilter + ", sao=" + currentSao);

        apiService.getDanhGiaWithFilter(maKhoaHoc, currentFilter, currentSao)
                .enqueue(new Callback<List<DanhGiaDTO>>() {
                    @Override
                    public void onResponse(Call<List<DanhGiaDTO>> call, Response<List<DanhGiaDTO>> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            danhGiaList.clear();
                            danhGiaList.addAll(response.body());
                            danhGiaAdapter.notifyDataSetChanged();

                            if (danhGiaList.isEmpty()) {
                                showEmpty("Không có đánh giá phù hợp");
                            } else {
                                hideEmpty();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DanhGiaDTO>> call, Throwable t) {
                        Log.e(TAG, "Failed to load filtered danh gia", t);
                    }
                });
    }

    private void showEmpty(String message) {
        if (txtEmpty != null) {
            txtEmpty.setText(message);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        if (recyclerDanhGia != null) {
            recyclerDanhGia.setVisibility(View.GONE);
        }
    }

    private void hideEmpty() {
        if (txtEmpty != null) {
            txtEmpty.setVisibility(View.GONE);
        }
        if (recyclerDanhGia != null) {
            recyclerDanhGia.setVisibility(View.VISIBLE);
        }
    }

    private void openMediaViewer(List<HinhAnhDanhGiaDTO> mediaList, int position) {
        Intent intent = new Intent(requireContext(), MediaViewerActivity.class);
        intent.putExtra(MediaViewerActivity.EXTRA_MEDIA_LIST, new ArrayList<>(mediaList));
        intent.putExtra(MediaViewerActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }
}

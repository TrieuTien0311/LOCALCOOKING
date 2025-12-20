package com.example.localcooking_v3t;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.model.MessageResponse;
import com.example.localcooking_v3t.model.ThongBaoDTO;
import com.example.localcooking_v3t.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeFragment extends Fragment {

    private static final String TAG = "NoticeFragment";

    private RecyclerView recyclerView;
    private NoticesAdapter adapter;
    private List<Notice> noticeList;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    private ApiService apiService;
    private SessionManager sessionManager;
    private Integer maNguoiDung;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        // Khởi tạo views
        recyclerView = view.findViewById(R.id.recyclerViewNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách
        noticeList = new ArrayList<>();

        // Khởi tạo API service và session
        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(requireContext());

        // Lấy mã người dùng
        maNguoiDung = sessionManager.getMaNguoiDung();
        Log.d(TAG, "maNguoiDung: " + maNguoiDung);

        // Khởi tạo adapter
        adapter = new NoticesAdapter(noticeList);
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện click item
        adapter.setOnItemClickListener((notice, position) -> {
            // Đánh dấu đã đọc trên server
            if (notice.getMaThongBao() != null && !notice.isTrangThai()) {
                markAsRead(notice.getMaThongBao(), position);
            }
            Toast.makeText(getContext(),
                    "Đã đọc: " + notice.getTieuDeTB(),
                    Toast.LENGTH_SHORT).show();
        });

        // Load dữ liệu từ API
        if (maNguoiDung != null && maNguoiDung > 0) {
            loadThongBaoFromAPI();
        } else {
            Log.w(TAG, "Chưa đăng nhập, hiển thị dữ liệu mẫu");
            initSampleData();
        }

        return view;
    }

    // Load thông báo từ API
    private void loadThongBaoFromAPI() {
        Log.d(TAG, "Loading thông báo cho user: " + maNguoiDung);

        Call<List<ThongBaoDTO>> call = apiService.getThongBaoByUser(maNguoiDung);

        call.enqueue(new Callback<List<ThongBaoDTO>>() {
            @Override
            public void onResponse(Call<List<ThongBaoDTO>> call, Response<List<ThongBaoDTO>> response) {
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<ThongBaoDTO> thongBaoList = response.body();
                    Log.d(TAG, "Nhận được " + thongBaoList.size() + " thông báo");

                    noticeList.clear();

                    // Convert ThongBaoDTO sang Notice
                    for (ThongBaoDTO dto : thongBaoList) {
                        Notice notice = new Notice(
                                dto.getMaThongBao(),
                                dto.getTieuDeTB(),
                                dto.getNoiDungTB(),
                                dto.getThoiGianTB(),
                                dto.getAnhTB(),
                                dto.getTrangThai() != null ? dto.getTrangThai() : false,
                                dto.getLoaiThongBao()
                        );
                        noticeList.add(notice);
                    }

                    adapter.notifyDataSetChanged();

                    if (noticeList.isEmpty()) {
                        Log.d(TAG, "Không có thông báo, hiển thị dữ liệu mẫu");
                        initSampleData();
                    }
                } else {
                    Log.e(TAG, "API error: " + response.code() + " - " + response.message());
                    Toast.makeText(getContext(), "Không thể tải thông báo", Toast.LENGTH_SHORT).show();
                    // Fallback về dữ liệu mẫu
                    initSampleData();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoDTO>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Fallback về dữ liệu mẫu
                initSampleData();
            }
        });
    }

    // Đánh dấu đã đọc
    private void markAsRead(Integer maThongBao, int position) {
        Log.d(TAG, "Marking as read: " + maThongBao);

        Call<ThongBaoDTO> call = apiService.markAsRead(maThongBao);

        call.enqueue(new Callback<ThongBaoDTO>() {
            @Override
            public void onResponse(Call<ThongBaoDTO> call, Response<ThongBaoDTO> response) {
                if (response.isSuccessful()) {
                    // Cập nhật UI
                    noticeList.get(position).setTrangThai(true);
                    adapter.notifyItemChanged(position);
                    Log.d(TAG, "Đã đánh dấu đã đọc thành công");
                } else {
                    Log.e(TAG, "Mark as read failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ThongBaoDTO> call, Throwable t) {
                Log.e(TAG, "Mark as read error: " + t.getMessage());
            }
        });
    }

    // Đánh dấu tất cả đã đọc
    public void markAllAsRead() {
        if (maNguoiDung == null || maNguoiDung <= 0) return;

        Call<MessageResponse> call = apiService.markAllAsRead(maNguoiDung);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    // Cập nhật UI
                    for (Notice notice : noticeList) {
                        notice.setTrangThai(true);
                    }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Đã đánh dấu tất cả", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xóa tất cả thông báo đã đọc
    public void clearReadNotices() {
        if (maNguoiDung == null || maNguoiDung <= 0) {
            // Xử lý local nếu chưa đăng nhập
            List<Notice> unreadNotices = new ArrayList<>();
            for (Notice notice : noticeList) {
                if (!notice.isTrangThai()) {
                    unreadNotices.add(notice);
                }
            }
            noticeList.clear();
            noticeList.addAll(unreadNotices);
            adapter.notifyDataSetChanged();
            return;
        }

        Call<MessageResponse> call = apiService.deleteAllReadNotifications(maNguoiDung);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    // Reload dữ liệu
                    loadThongBaoFromAPI();
                    Toast.makeText(getContext(), "Đã xóa thông báo đã đọc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức thêm thông báo mới
    public void addNotice(Notice notice) {
        if (noticeList != null && adapter != null) {
            noticeList.add(0, notice); // Thêm vào đầu danh sách
            adapter.notifyItemInserted(0);
            recyclerView.smoothScrollToPosition(0);
        }
    }

    // Dữ liệu mẫu (fallback khi không có API)
    private void initSampleData() {
        noticeList.clear();

        noticeList.add(new Notice(
                "Lớp học sắp diễn ra",
                "Lớp \"Ẩm thực địa phương Huế\" của bạn sẽ bắt đầu vào ngày mai. Đừng quên nhé!",
                "15 phút trước",
                R.drawable.hue,
                false
        ));

        noticeList.add(new Notice(
                "Đặt lịch thành công",
                "Chúc mừng! bạn đã đặt chỗ thành công cho lớp \"Ẩm thực địa phương Huế\" vào 9:00 ngày 20 tháng 10 năm 2025.",
                "1 giờ trước",
                R.drawable.hue,
                true
        ));

        noticeList.add(new Notice(
                "Ưu đãi đặc biệt",
                "Giảm 20% cho tất cả các lớp học trong tháng 10! Sử dụng mã: COOK10. Áp dụng đến hết ngày 31/10",
                "3 giờ trước",
                R.drawable.ic_discount_background_tn,
                false
        ));

        noticeList.add(new Notice(
                "Lớp học bị hủy",
                "Rất tiếc! Lớp học \"Ẩm thực thủ đô Hà Nội\" ngày 15 tháng 9 đã bị hủy do không đủ số lượng học viên. Học phí sẽ được hoàn lại vào tài khoản của bạn",
                "1 ngày trước",
                R.drawable.hue,
                true
        ));

        noticeList.add(new Notice(
                "Chứng chỉ đã sẵn sàng",
                "Chúc mừng! Chứng chỉ hoàn thành khóa học của bạn đã sẵn sàng để tải xuống.",
                "1 ngày trước",
                R.drawable.hue,
                true
        ));

        adapter.notifyDataSetChanged();
    }

    // Refresh dữ liệu
    public void refreshData() {
        if (maNguoiDung != null && maNguoiDung > 0) {
            loadThongBaoFromAPI();
        }
    }
}

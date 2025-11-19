package com.example.localcooking_v3t;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoticeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticesAdapter adapter;
    private List<Notice> noticeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách thông báo mẫu
        initNoticeData();

        // Khởi tạo adapter
        adapter = new NoticesAdapter(noticeList);
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện click item
        adapter.setOnItemClickListener((notice, position) -> {
            Toast.makeText(getContext(),
                    "Đã đọc: " + notice.getTieuDeTB(),
                    Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void initNoticeData() {
        noticeList = new ArrayList<>();

        // Thêm dữ liệu mẫu (thay android.R.drawable... bằng drawable của bạn)
        noticeList.add(new Notice(
                "Lớp học sắp diễn ra",
                "Lớp \"Ẩm thực địa phương Huế\" của bạn sẽ bắt đầu vào ngày mai. Đừng quên nhé!",
                "15 phút trước",
                R.drawable.hue,
                false // Chưa đọc
        ));

        noticeList.add(new Notice(
                "Đặt lịch thành công",
                "Chúc mừng! bạn đã đặt chỗ thành công cho lớp “ Ẩm thực địa phương Huế”. vào 9:00 ngày 20 tháng 10 năm 2025. ",
                "1 giờ trước",
                R.drawable.hue,

        true // Đã đọc
        ));

        noticeList.add(new Notice(
                "Ưu đãi đặc biệt",
                "Giảm 20% cho tất cả các lớp học trong tháng 10! Sử dụng mã: COOK10. Áp dụng đến hết ngày 31/10",
                "3 giờ trước",
                R.drawable.ic_discount_background_tn,
                false // Chưa đọc
        ));

        noticeList.add(new Notice(
                "Lớp học bị hủy",
                "Rất tiếc! Lớp học ” Ẩm thực thủ đô Hà Nội” ngày 15 tháng 9 đã bị hủy do không đủ số lượng học. Học phí sẽ được hoàn lại vào tài khoản của bạn",
                "1 ngày trước",
                R.drawable.hue,
                true // Đã đọc
        ));
        noticeList.add(new Notice(
                "Chứng chỉ đã sẵn sàng",
                "Chúc mừng! Chứng chỉ hoàn thành khóa học của bạn đã sẵn sàng để tải xuống.",
                "1 ngày trước",
                R.drawable.hue,
                true // Đã đọc
        ));
    }

    // Phương thức thêm thông báo mới
    public void addNotice(Notice notice) {
        if (noticeList != null && adapter != null) {
            noticeList.add(0, notice); // Thêm vào đầu danh sách
            adapter.notifyItemInserted(0);
            recyclerView.smoothScrollToPosition(0);
        }
    }

    // Phương thức xóa tất cả thông báo đã đọc
    public void clearReadNotices() {
        if (noticeList != null && adapter != null) {
            List<Notice> unreadNotices = new ArrayList<>();
            for (Notice notice : noticeList) {
                if (!notice.isTrangThai()) {
                    unreadNotices.add(notice);
                }
            }
            noticeList.clear();
            noticeList.addAll(unreadNotices);
            adapter.notifyDataSetChanged();
        }
    }
}
# 📱 Hướng Dẫn Tích Hợp API Thông Báo Vào Android App

## 🎯 Mục Tiêu

Tích hợp API thông báo từ backend vào ứng dụng Android để:
- Hiển thị danh sách thông báo từ server
- Đánh dấu đã đọc/chưa đọc
- Hiển thị số lượng thông báo chưa đọc (badge)
- Xóa thông báo

---

## 📋 Các Bước Thực Hiện

### Bước 1: Tạo Model ThongBaoDTO

Tạo file `ThongBaoDTO.java` trong package `model`:

```java
package com.example.localcooking_v3t.model;

public class ThongBaoDTO {
    private Integer maThongBao;
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private String anhTB;
    private Boolean trangThai; // true = đã đọc, false = chưa đọc
    private String loaiThongBao;

    // Constructor
    public ThongBaoDTO() {}

    public ThongBaoDTO(Integer maThongBao, String tieuDeTB, String noiDungTB, 
                       String thoiGianTB, String anhTB, Boolean trangThai, String loaiThongBao) {
        this.maThongBao = maThongBao;
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTB = anhTB;
        this.trangThai = trangThai;
        this.loaiThongBao = loaiThongBao;
    }

    // Getters and Setters
    public Integer getMaThongBao() { return maThongBao; }
    public void setMaThongBao(Integer maThongBao) { this.maThongBao = maThongBao; }

    public String getTieuDeTB() { return tieuDeTB; }
    public void setTieuDeTB(String tieuDeTB) { this.tieuDeTB = tieuDeTB; }

    public String getNoiDungTB() { return noiDungTB; }
    public void setNoiDungTB(String noiDungTB) { this.noiDungTB = noiDungTB; }

    public String getThoiGianTB() { return thoiGianTB; }
    public void setThoiGianTB(String thoiGianTB) { this.thoiGianTB = thoiGianTB; }

    public String getAnhTB() { return anhTB; }
    public void setAnhTB(String anhTB) { this.anhTB = anhTB; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    public String getLoaiThongBao() { return loaiThongBao; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
}
```

---

### Bước 2: Tạo Response Models

Tạo file `UnreadCountResponse.java`:

```java
package com.example.localcooking_v3t.model;

public class UnreadCountResponse {
    private Long count;

    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}
```

Tạo file `MessageResponse.java`:

```java
package com.example.localcooking_v3t.model;

public class MessageResponse {
    private String message;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
```

---

### Bước 3: Cập Nhật ApiService

Thêm các endpoint vào `ApiService.java`:

```java
public interface ApiService {
    // ... các API khác ...

    // ========== API THÔNG BÁO ==========
    
    // Lấy tất cả thông báo của người dùng
    @GET("api/thongbao/user/{maNguoiNhan}")
    Call<List<ThongBaoDTO>> getThongBaoByUser(@Path("maNguoiNhan") Integer maNguoiNhan);
    
    // Lấy thông báo chưa đọc
    @GET("api/thongbao/user/{maNguoiNhan}/unread")
    Call<List<ThongBaoDTO>> getUnreadThongBao(@Path("maNguoiNhan") Integer maNguoiNhan);
    
    // Đếm số thông báo chưa đọc
    @GET("api/thongbao/user/{maNguoiNhan}/unread-count")
    Call<UnreadCountResponse> getUnreadCount(@Path("maNguoiNhan") Integer maNguoiNhan);
    
    // Lấy thông báo theo loại
    @GET("api/thongbao/user/{maNguoiNhan}/type/{loaiThongBao}")
    Call<List<ThongBaoDTO>> getThongBaoByType(
        @Path("maNguoiNhan") Integer maNguoiNhan,
        @Path("loaiThongBao") String loaiThongBao
    );
    
    // Đánh dấu đã đọc
    @PUT("api/thongbao/{id}/mark-read")
    Call<ThongBao> markAsRead(@Path("id") Integer id);
    
    // Đánh dấu tất cả đã đọc
    @PUT("api/thongbao/user/{maNguoiNhan}/mark-all-read")
    Call<MessageResponse> markAllAsRead(@Path("maNguoiNhan") Integer maNguoiNhan);
    
    // Xóa thông báo
    @DELETE("api/thongbao/{id}")
    Call<Void> deleteThongBao(@Path("id") Integer id);
    
    // Xóa tất cả thông báo đã đọc
    @DELETE("api/thongbao/user/{maNguoiNhan}/delete-read")
    Call<MessageResponse> deleteAllReadNotifications(@Path("maNguoiNhan") Integer maNguoiNhan);
}
```

---

### Bước 4: Cập Nhật Notice.java

Thêm field `maThongBao` và `loaiThongBao`:

```java
package com.example.localcooking_v3t;

public class Notice {
    private Integer maThongBao; // Thêm field này
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private String anhTB; // Đổi từ int sang String để nhận URL từ server
    private boolean trangThai;
    private String loaiThongBao; // Thêm field này

    // Constructor đầy đủ
    public Notice(Integer maThongBao, String tieuDeTB, String noiDungTB, 
                  String thoiGianTB, String anhTB, boolean trangThai, String loaiThongBao) {
        this.maThongBao = maThongBao;
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTB = anhTB;
        this.trangThai = trangThai;
        this.loaiThongBao = loaiThongBao;
    }

    // Constructor cũ (để tương thích)
    public Notice(String tieuDeTB, String noiDungTB, String thoiGianTB, 
                  int anhTB, boolean trangThai) {
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTB = String.valueOf(anhTB);
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public Integer getMaThongBao() { return maThongBao; }
    public void setMaThongBao(Integer maThongBao) { this.maThongBao = maThongBao; }

    public String getTieuDeTB() { return tieuDeTB; }
    public void setTieuDeTB(String tieuDeTB) { this.tieuDeTB = tieuDeTB; }

    public String getNoiDungTB() { return noiDungTB; }
    public void setNoiDungTB(String noiDungTB) { this.noiDungTB = noiDungTB; }

    public String getThoiGianTB() { return thoiGianTB; }
    public void setThoiGianTB(String thoiGianTB) { this.thoiGianTB = thoiGianTB; }

    public String getAnhTB() { return anhTB; }
    public void setAnhTB(String anhTB) { this.anhTB = anhTB; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    public String getLoaiThongBao() { return loaiThongBao; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
}
```

---

### Bước 5: Cập Nhật NoticeFragment.java

Thay thế phương thức `initNoticeData()` bằng `loadThongBaoFromAPI()`:

```java
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
import com.example.localcooking_v3t.api.ApiService;
import com.example.localcooking_v3t.api.RetrofitClient;
import com.example.localcooking_v3t.helper.SharedPrefManager;
import com.example.localcooking_v3t.model.ThongBaoDTO;
import com.example.localcooking_v3t.model.MessageResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticesAdapter adapter;
    private List<Notice> noticeList;
    private ApiService apiService;
    private Integer maNguoiDung;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        // Khởi tạo
        recyclerView = view.findViewById(R.id.recyclerViewNotices);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noticeList = new ArrayList<>();
        
        // Lấy API service
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        
        // Lấy ID người dùng từ SharedPreferences
        maNguoiDung = SharedPrefManager.getInstance(getContext()).getUserId();

        // Khởi tạo adapter
        adapter = new NoticesAdapter(noticeList);
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện click item
        adapter.setOnItemClickListener((notice, position) -> {
            // Đánh dấu đã đọc trên server
            markAsRead(notice.getMaThongBao(), position);
        });

        // Load dữ liệu từ API
        loadThongBaoFromAPI();

        return view;
    }

    // Load thông báo từ API
    private void loadThongBaoFromAPI() {
        Call<List<ThongBaoDTO>> call = apiService.getThongBaoByUser(maNguoiDung);
        
        call.enqueue(new Callback<List<ThongBaoDTO>>() {
            @Override
            public void onResponse(Call<List<ThongBaoDTO>> call, Response<List<ThongBaoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    noticeList.clear();
                    
                    // Convert ThongBaoDTO sang Notice
                    for (ThongBaoDTO dto : response.body()) {
                        Notice notice = new Notice(
                            dto.getMaThongBao(),
                            dto.getTieuDeTB(),
                            dto.getNoiDungTB(),
                            dto.getThoiGianTB(),
                            dto.getAnhTB(),
                            dto.getTrangThai(),
                            dto.getLoaiThongBao()
                        );
                        noticeList.add(notice);
                    }
                    
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải thông báo", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<List<ThongBaoDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Đánh dấu đã đọc
    private void markAsRead(Integer maThongBao, int position) {
        Call<ThongBao> call = apiService.markAsRead(maThongBao);
        
        call.enqueue(new Callback<ThongBao>() {
            @Override
            public void onResponse(Call<ThongBao> call, Response<ThongBao> response) {
                if (response.isSuccessful()) {
                    // Cập nhật UI
                    noticeList.get(position).setTrangThai(true);
                    adapter.notifyItemChanged(position);
                    Toast.makeText(getContext(), "Đã đọc", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ThongBao> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Đánh dấu tất cả đã đọc
    public void markAllAsRead() {
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
}
```

---

### Bước 6: Cập Nhật NoticesAdapter.java

Cập nhật để load ảnh từ URL:

```java
@Override
public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
    Notice notice = noticeList.get(position);

    holder.txtTitleTB.setText(notice.getTieuDeTB());
    holder.txtNoiDungTB.setText(notice.getNoiDungTB());
    holder.txtThoiGianTB.setText(notice.getThoiGianTB());
    
    // Load ảnh từ URL hoặc resource
    String anhTB = notice.getAnhTB();
    if (anhTB != null && !anhTB.isEmpty()) {
        // Nếu là URL, dùng Glide hoặc Picasso
        // Glide.with(holder.itemView.getContext()).load(anhTB).into(holder.imgThongBao);
        
        // Nếu là tên file trong drawable
        int resId = holder.itemView.getContext().getResources()
            .getIdentifier(anhTB.replace(".jpg", "").replace(".png", ""), 
                          "drawable", 
                          holder.itemView.getContext().getPackageName());
        if (resId != 0) {
            holder.imgThongBao.setImageResource(resId);
        } else {
            holder.imgThongBao.setImageResource(R.drawable.logo);
        }
    }

    // Thay đổi màu CardView dựa trên trạng thái
    if (!notice.isTrangThai()) {
        holder.cardView.setCardBackgroundColor(Color.parseColor("#E8E8E8"));
        holder.txtTitleTB.setTextColor(Color.parseColor("#000000"));
    } else {
        holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.txtTitleTB.setTextColor(Color.parseColor("#666666"));
    }

    // Xử lý sự kiện click
    holder.itemView.setOnClickListener(v -> {
        if (listener != null) {
            listener.onItemClick(notice, position);
        }
    });
}
```

---

### Bước 7: Hiển Thị Badge Số Thông Báo Chưa Đọc

Trong `MainActivity.java`, thêm phương thức để hiển thị badge:

```java
private void loadUnreadNotificationCount() {
    Integer maNguoiDung = SharedPrefManager.getInstance(this).getUserId();
    ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
    
    Call<UnreadCountResponse> call = apiService.getUnreadCount(maNguoiDung);
    call.enqueue(new Callback<UnreadCountResponse>() {
        @Override
        public void onResponse(Call<UnreadCountResponse> call, Response<UnreadCountResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                Long count = response.body().getCount();
                // Hiển thị badge trên icon thông báo
                if (count > 0) {
                    // Cập nhật badge UI
                    // badge.setNumber(count.intValue());
                    // badge.show();
                }
            }
        }
        
        @Override
        public void onFailure(Call<UnreadCountResponse> call, Throwable t) {
            // Handle error
        }
    });
}
```

---

## 🧪 Test Tích Hợp

### 1. Chạy Backend
```bash
cd BE
./gradlew bootRun
```

### 2. Thêm Dữ Liệu Mẫu
Chạy file `INSERT_THONGBAO_DATA.sql` trong SQL Server Management Studio.

### 3. Test API với Postman
```
GET http://localhost:8080/api/thongbao/user/4
```

### 4. Chạy Android App
- Build và chạy app
- Đăng nhập với user ID = 4
- Vào tab Thông Báo
- Kiểm tra hiển thị danh sách

---

## ✅ Checklist Hoàn Thành

- [ ] Tạo model ThongBaoDTO
- [ ] Cập nhật ApiService
- [ ] Cập nhật Notice.java
- [ ] Cập nhật NoticeFragment.java
- [ ] Cập nhật NoticesAdapter.java
- [ ] Test load dữ liệu từ API
- [ ] Test đánh dấu đã đọc
- [ ] Test xóa thông báo
- [ ] Hiển thị badge số thông báo chưa đọc

---

## 🎉 Kết Luận

Sau khi hoàn thành các bước trên, ứng dụng Android sẽ:
- ✅ Load thông báo từ server thay vì dữ liệu cứng
- ✅ Đồng bộ trạng thái đã đọc/chưa đọc với server
- ✅ Hiển thị thời gian tự động ("X phút trước")
- ✅ Hỗ trợ xóa và quản lý thông báo

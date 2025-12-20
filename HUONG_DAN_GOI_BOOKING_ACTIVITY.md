# Hướng Dẫn Gọi Booking Activity

## 📋 Tổng Quan

Khi user nhấn nút "Đặt lịch" từ danh sách khóa học, cần truyền đầy đủ thông tin sang `Booking` activity để hiển thị.

---

## 🎯 Cách Gọi Từ Adapter/Fragment

### 1. Từ ClassAdapter hoặc KhoaHocAdapter

```java
// Trong ViewHolder hoặc onBindViewHolder
btnDatLich.setOnClickListener(v -> {
    // Lấy thông tin khóa học
    KhoaHoc khoaHoc = khoaHocList.get(position);
    
    // Gọi API lấy lịch trình của khóa học
    getLichTrinhAndNavigateToBooking(khoaHoc);
});
```

### 2. Method getLichTrinhAndNavigateToBooking

```java
private void getLichTrinhAndNavigateToBooking(KhoaHoc khoaHoc) {
    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    
    // Lấy lịch trình theo khóa học
    apiService.getLichTrinhByKhoaHoc(khoaHoc.getMaKhoaHoc()).enqueue(new Callback<List<LichTrinhLopHoc>>() {
        @Override
        public void onResponse(Call<List<LichTrinhLopHoc>> call, Response<List<LichTrinhLopHoc>> response) {
            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                List<LichTrinhLopHoc> lichTrinhList = response.body();
                
                // Nếu chỉ có 1 lịch trình → Chuyển thẳng sang Booking
                if (lichTrinhList.size() == 1) {
                    LichTrinhLopHoc lichTrinh = lichTrinhList.get(0);
                    navigateToBooking(khoaHoc, lichTrinh, null);
                } else {
                    // Nếu có nhiều lịch trình → Hiển thị dialog chọn lịch trình
                    showLichTrinhDialog(khoaHoc, lichTrinhList);
                }
            } else {
                Toast.makeText(context, "Không tìm thấy lịch trình", Toast.LENGTH_SHORT).show();
            }
        }
        
        @Override
        public void onFailure(Call<List<LichTrinhLopHoc>> call, Throwable t) {
            Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
```

### 3. Method navigateToBooking

```java
private void navigateToBooking(KhoaHoc khoaHoc, LichTrinhLopHoc lichTrinh, String ngayThamGia) {
    Intent intent = new Intent(context, Booking.class);
    
    // Thông tin khóa học
    intent.putExtra("maKhoaHoc", khoaHoc.getMaKhoaHoc());
    intent.putExtra("tenKhoaHoc", khoaHoc.getTenKhoaHoc());
    intent.putExtra("giaTien", khoaHoc.getGiaTien().toString());
    
    // Thông tin lịch trình
    intent.putExtra("maLichTrinh", lichTrinh.getMaLichTrinh());
    intent.putExtra("thoiGian", lichTrinh.getGioBatDau() + " - " + lichTrinh.getGioKetThuc());
    intent.putExtra("diaDiem", lichTrinh.getDiaDiem());
    
    // Ngày tham gia (nếu có)
    if (ngayThamGia != null) {
        intent.putExtra("ngayThamGia", ngayThamGia);
    } else {
        // Mặc định là ngày mai
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        intent.putExtra("ngayThamGia", sdf.format(cal.getTime()));
    }
    
    // Số chỗ còn lại (có thể gọi API check-seats trước)
    intent.putExtra("soLuongConLai", lichTrinh.getSoLuongToiDa());
    
    context.startActivity(intent);
}
```

### 4. Dialog Chọn Lịch Trình (Nếu Có Nhiều)

```java
private void showLichTrinhDialog(KhoaHoc khoaHoc, List<LichTrinhLopHoc> lichTrinhList) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Chọn lịch trình");
    
    // Tạo danh sách hiển thị
    String[] items = new String[lichTrinhList.size()];
    for (int i = 0; i < lichTrinhList.size(); i++) {
        LichTrinhLopHoc lt = lichTrinhList.get(i);
        items[i] = lt.getGioBatDau() + " - " + lt.getGioKetThuc() + 
                   "\n" + lt.getDiaDiem();
    }
    
    builder.setItems(items, (dialog, which) -> {
        LichTrinhLopHoc selectedLichTrinh = lichTrinhList.get(which);
        navigateToBooking(khoaHoc, selectedLichTrinh, null);
    });
    
    builder.setNegativeButton("Hủy", null);
    builder.show();
}
```

---

## 🔄 Luồng Hoàn Chỉnh

```
User nhấn "Đặt lịch" trên card khóa học
    ↓
API: GET /api/lichtrinh/khoahoc/{maKhoaHoc}
    ↓
Nếu có 1 lịch trình → Chuyển thẳng sang Booking
    ↓
Nếu có nhiều lịch trình → Hiển thị dialog chọn
    ↓
User chọn lịch trình
    ↓
Chuyển sang Booking Activity với đầy đủ thông tin:
  - maKhoaHoc, tenKhoaHoc, giaTien
  - maLichTrinh, thoiGian, diaDiem
  - ngayThamGia, soLuongConLai
    ↓
Booking Activity hiển thị:
  - Header: Tên khóa học + Thời gian + Ngày
  - Nút tăng/giảm số lượng
  - Nút "Đặt lịch"
```

---

## 📱 Ví Dụ Đầy Đủ Trong Adapter

```java
public class KhoaHocAdapter extends RecyclerView.Adapter<KhoaHocAdapter.ViewHolder> {
    
    private Context context;
    private List<KhoaHoc> khoaHocList;
    private ApiService apiService;
    
    public KhoaHocAdapter(Context context, List<KhoaHoc> khoaHocList) {
        this.context = context;
        this.khoaHocList = khoaHocList;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KhoaHoc khoaHoc = khoaHocList.get(position);
        
        // Hiển thị thông tin khóa học
        holder.txtTenKhoaHoc.setText(khoaHoc.getTenKhoaHoc());
        holder.txtGiaTien.setText(formatCurrency(khoaHoc.getGiaTien()));
        
        // Xử lý nút đặt lịch
        holder.btnDatLich.setOnClickListener(v -> {
            getLichTrinhAndNavigateToBooking(khoaHoc);
        });
    }
    
    private void getLichTrinhAndNavigateToBooking(KhoaHoc khoaHoc) {
        apiService.getLichTrinhByKhoaHoc(khoaHoc.getMaKhoaHoc())
            .enqueue(new Callback<List<LichTrinhLopHoc>>() {
                @Override
                public void onResponse(Call<List<LichTrinhLopHoc>> call, 
                                     Response<List<LichTrinhLopHoc>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<LichTrinhLopHoc> lichTrinhList = response.body();
                        
                        if (!lichTrinhList.isEmpty()) {
                            if (lichTrinhList.size() == 1) {
                                // Chỉ có 1 lịch trình
                                navigateToBooking(khoaHoc, lichTrinhList.get(0), null);
                            } else {
                                // Nhiều lịch trình
                                showLichTrinhDialog(khoaHoc, lichTrinhList);
                            }
                        } else {
                            Toast.makeText(context, "Khóa học chưa có lịch trình", 
                                         Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                
                @Override
                public void onFailure(Call<List<LichTrinhLopHoc>> call, Throwable t) {
                    Toast.makeText(context, "Lỗi: " + t.getMessage(), 
                                 Toast.LENGTH_SHORT).show();
                }
            });
    }
    
    private void navigateToBooking(KhoaHoc khoaHoc, LichTrinhLopHoc lichTrinh, 
                                   String ngayThamGia) {
        Intent intent = new Intent(context, Booking.class);
        
        // Thông tin khóa học
        intent.putExtra("maKhoaHoc", khoaHoc.getMaKhoaHoc());
        intent.putExtra("tenKhoaHoc", khoaHoc.getTenKhoaHoc());
        intent.putExtra("giaTien", khoaHoc.getGiaTien().toString());
        
        // Thông tin lịch trình
        intent.putExtra("maLichTrinh", lichTrinh.getMaLichTrinh());
        intent.putExtra("thoiGian", lichTrinh.getGioBatDau() + " - " + 
                       lichTrinh.getGioKetThuc());
        intent.putExtra("diaDiem", lichTrinh.getDiaDiem());
        
        // Ngày tham gia
        if (ngayThamGia == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ngayThamGia = sdf.format(cal.getTime());
        }
        intent.putExtra("ngayThamGia", ngayThamGia);
        
        // Số chỗ còn lại
        intent.putExtra("soLuongConLai", lichTrinh.getSoLuongToiDa());
        
        context.startActivity(intent);
    }
    
    private void showLichTrinhDialog(KhoaHoc khoaHoc, 
                                     List<LichTrinhLopHoc> lichTrinhList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn lịch trình học");
        
        String[] items = new String[lichTrinhList.size()];
        for (int i = 0; i < lichTrinhList.size(); i++) {
            LichTrinhLopHoc lt = lichTrinhList.get(i);
            items[i] = "⏰ " + lt.getGioBatDau() + " - " + lt.getGioKetThuc() + 
                       "\n📍 " + lt.getDiaDiem();
        }
        
        builder.setItems(items, (dialog, which) -> {
            navigateToBooking(khoaHoc, lichTrinhList.get(which), null);
        });
        
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    
    private String formatCurrency(BigDecimal amount) {
        return String.format("%,dđ", amount.longValue());
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenKhoaHoc, txtGiaTien;
        Button btnDatLich;
        
        ViewHolder(View itemView) {
            super(itemView);
            txtTenKhoaHoc = itemView.findViewById(R.id.txtTenKhoaHoc);
            txtGiaTien = itemView.findViewById(R.id.txtGiaTien);
            btnDatLich = itemView.findViewById(R.id.btnDatLich);
        }
    }
}
```

---

## ✅ Tóm Tắt

### Data cần truyền sang Booking:
1. **maKhoaHoc** - ID khóa học
2. **tenKhoaHoc** - Tên khóa học (hiển thị ở header)
3. **giaTien** - Giá tiền (để tính tổng)
4. **maLichTrinh** - ID lịch trình đã chọn
5. **thoiGian** - Giờ học (VD: "17:30 - 20:30")
6. **diaDiem** - Địa điểm học
7. **ngayThamGia** - Ngày tham gia (format: "yyyy-MM-dd")
8. **soLuongConLai** - Số chỗ còn trống

### Booking Activity sẽ:
- ✅ Hiển thị tên khóa học ở header
- ✅ Hiển thị thời gian + ngày ở header (format: "17:30 - 20:30 - T5, 25/12/2025")
- ✅ Cho phép tăng/giảm số lượng người
- ✅ Gọi API đặt lịch khi nhấn nút "Đặt lịch"
- ✅ Chuyển sang Payment activity sau khi đặt lịch thành công

🚀 Hoàn tất!

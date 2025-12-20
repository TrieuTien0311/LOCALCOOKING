# Hướng Dẫn Implement Chức Năng Đặt Lịch

## ✅ Đã Hoàn Thành

### Backend
- ✅ Model `DatLich.java` - Đã cập nhật với `maLichTrinh` và `ngayThamGia`
- ✅ Repository `DatLichRepository.java` - Có method `countBookedSeats()`
- ✅ Service `DatLichService.java` - Có logic kiểm tra chỗ trống
- ✅ Controller `DatLichController.java` - Đầy đủ endpoints
- ✅ API `/api/datlich/check-seats` - Kiểm tra chỗ trống
- ✅ API `/api/datlich` POST - Tạo đặt lịch

### Frontend Models
- ✅ `DatLich.java`
- ✅ `DatLichRequest.java`
- ✅ `DatLichResponse.java`
- ✅ `LichTrinhLopHoc.java`
- ✅ `CheckSeatsResponse.java`

---

## 📝 Cần Làm Tiếp

### Bước 1: Cập nhật ApiService.java

Thêm các endpoints vào file `FE/app/src/main/java/com/example/localcooking_v3t/api/ApiService.java`:

```java
// Lấy lịch trình theo khóa học
@GET("api/lichtrinh/khoahoc/{maKhoaHoc}")
Call<List<LichTrinhLopHoc>> getLichTrinhByKhoaHoc(@Path("maKhoaHoc") int maKhoaHoc);

// Kiểm tra chỗ trống
@GET("api/datlich/check-seats")
Call<CheckSeatsResponse> checkAvailableSeats(
    @Query("maLichTrinh") int maLichTrinh,
    @Query("ngayThamGia") String ngayThamGia
);

// Tạo đặt lịch
@POST("api/datlich")
Call<DatLichResponse> createDatLich(@Body DatLichRequest request);

// Lấy đặt lịch theo học viên
@GET("api/datlich/hocvien/{maHocVien}")
Call<List<DatLich>> getDatLichByHocVien(@Path("maHocVien") int maHocVien);
```

---

### Bước 2: Tạo BookingStep1Activity (Chọn lịch trình & ngày)

**File:** `FE/app/src/main/java/com/example/localcooking_v3t/BookingStep1Activity.java`

**Layout:** `FE/app/src/main/res/layout/activity_booking_step1.xml`

**Chức năng:**
1. Nhận `maKhoaHoc`, `tenKhoaHoc`, `giaTien` từ Intent
2. Gọi API `getLichTrinhByKhoaHoc()` để lấy danh sách lịch trình
3. Hiển thị RecyclerView các lịch trình (thứ, giờ, địa điểm)
4. User chọn 1 lịch trình
5. Hiển thị CalendarView để chọn ngày
6. Chỉ cho phép chọn ngày khớp với `thuTrongTuan`
7. Gọi API `checkAvailableSeats()` khi chọn ngày
8. Hiển thị số chỗ còn lại
9. Chuyển sang `BookingStep2Activity`

**Code mẫu:**
```java
public class BookingStep1Activity extends AppCompatActivity {
    
    private RecyclerView rvLichTrinh;
    private CalendarView calendarView;
    private TextView tvChoConLai;
    private Button btnContinue;
    
    private int maKhoaHoc;
    private String tenKhoaHoc;
    private BigDecimal giaTien;
    
    private LichTrinhLopHoc selectedLichTrinh;
    private String selectedDate;
    private int soChoConLai = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step1);
        
        // Nhận data từ Intent
        maKhoaHoc = getIntent().getIntExtra("maKhoaHoc", 0);
        tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
        String giaTienStr = getIntent().getStringExtra("giaTien");
        giaTien = new BigDecimal(giaTienStr);
        
        // Init views
        rvLichTrinh = findViewById(R.id.rvLichTrinh);
        calendarView = findViewById(R.id.calendarView);
        tvChoConLai = findViewById(R.id.tvChoConLai);
        btnContinue = findViewById(R.id.btnContinue);
        
        // Load lịch trình
        loadLichTrinh();
        
        // Setup calendar
        setupCalendar();
        
        // Button continue
        btnContinue.setOnClickListener(v -> {
            if (selectedLichTrinh != null && selectedDate != null && soChoConLai > 0) {
                Intent intent = new Intent(this, BookingStep2Activity.class);
                intent.putExtra("maLichTrinh", selectedLichTrinh.getMaLichTrinh());
                intent.putExtra("tenKhoaHoc", tenKhoaHoc);
                intent.putExtra("giaTien", giaTien.toString());
                intent.putExtra("ngayThamGia", selectedDate);
                intent.putExtra("soChoConLai", soChoConLai);
                intent.putExtra("gioBatDau", selectedLichTrinh.getGioBatDau());
                intent.putExtra("gioKetThuc", selectedLichTrinh.getGioKetThuc());
                intent.putExtra("diaDiem", selectedLichTrinh.getDiaDiem());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Vui lòng chọn lịch trình và ngày", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadLichTrinh() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<LichTrinhLopHoc>> call = apiService.getLichTrinhByKhoaHoc(maKhoaHoc);
        
        call.enqueue(new Callback<List<LichTrinhLopHoc>>() {
            @Override
            public void onResponse(Call<List<LichTrinhLopHoc>> call, Response<List<LichTrinhLopHoc>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LichTrinhLopHoc> list = response.body();
                    // Setup RecyclerView adapter
                    LichTrinhAdapter adapter = new LichTrinhAdapter(list, lichTrinh -> {
                        selectedLichTrinh = lichTrinh;
                        // Cập nhật calendar để chỉ cho phép chọn ngày khớp với thuTrongTuan
                        updateCalendarAvailableDates();
                    });
                    rvLichTrinh.setAdapter(adapter);
                }
            }
            
            @Override
            public void onFailure(Call<List<LichTrinhLopHoc>> call, Throwable t) {
                Toast.makeText(BookingStep1Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupCalendar() {
        calendarView.setMinDate(System.currentTimeMillis()); // Chỉ cho phép chọn ngày trong tương lai
        
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Format: YYYY-MM-DD
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            
            // Kiểm tra chỗ trống
            if (selectedLichTrinh != null) {
                checkAvailableSeats();
            }
        });
    }
    
    private void checkAvailableSeats() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<CheckSeatsResponse> call = apiService.checkAvailableSeats(
            selectedLichTrinh.getMaLichTrinh(),
            selectedDate
        );
        
        call.enqueue(new Callback<CheckSeatsResponse>() {
            @Override
            public void onResponse(Call<CheckSeatsResponse> call, Response<CheckSeatsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CheckSeatsResponse result = response.body();
                    soChoConLai = result.getSoChoConLai();
                    
                    if (soChoConLai > 0) {
                        tvChoConLai.setText("Còn " + soChoConLai + " chỗ trống");
                        tvChoConLai.setTextColor(Color.GREEN);
                        btnContinue.setEnabled(true);
                    } else {
                        tvChoConLai.setText("Đã hết chỗ");
                        tvChoConLai.setTextColor(Color.RED);
                        btnContinue.setEnabled(false);
                    }
                }
            }
            
            @Override
            public void onFailure(Call<CheckSeatsResponse> call, Throwable t) {
                Toast.makeText(BookingStep1Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateCalendarAvailableDates() {
        // TODO: Implement logic để chỉ highlight các ngày khớp với thuTrongTuan
        // VD: Nếu thuTrongTuan = "2,4,6" → Chỉ highlight Thứ 2, 4, 6
    }
}
```

---

### Bước 3: Tạo BookingStep2Activity (Điều chỉnh số người)

**File:** `FE/app/src/main/java/com/example/localcooking_v3t/BookingStep2Activity.java`

**Code đã có trong `LUONG_DAT_LICH_DON_GIAN.md` (dòng 478-558)**

---

### Bước 4: Tạo BookingStep3Activity (Xác nhận thông tin)

**File:** `FE/app/src/main/java/com/example/localcooking_v3t/BookingStep3Activity.java`

**Chức năng:**
1. Hiển thị tóm tắt thông tin đặt lịch
2. Nhập thông tin người đặt (họ tên, email, SĐT, ghi chú)
3. Validation
4. Gọi API `POST /api/datlich`
5. Nhận `maDatLich` từ response
6. Chuyển sang `PaymentActivity`

**Code mẫu:**
```java
public class BookingStep3Activity extends AppCompatActivity {
    
    private EditText etHoTen, etEmail, etSdt, etGhiChu;
    private TextView tvTongTien;
    private Button btnXacNhan;
    
    private SessionManager sessionManager;
    private int maLichTrinh;
    private String ngayThamGia;
    private int soLuongNguoi;
    private BigDecimal tongTien;
    private String tenKhoaHoc;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step3);
        
        sessionManager = new SessionManager(this);
        
        // Nhận data từ Intent
        maLichTrinh = getIntent().getIntExtra("maLichTrinh", 0);
        ngayThamGia = getIntent().getStringExtra("ngayThamGia");
        soLuongNguoi = getIntent().getIntExtra("soLuongNguoi", 1);
        tongTien = new BigDecimal(getIntent().getStringExtra("tongTien"));
        tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
        
        // Init views
        etHoTen = findViewById(R.id.etHoTen);
        etEmail = findViewById(R.id.etEmail);
        etSdt = findViewById(R.id.etSdt);
        etGhiChu = findViewById(R.id.etGhiChu);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        
        // Pre-fill thông tin từ session
        etHoTen.setText(sessionManager.getHoTen());
        etEmail.setText(sessionManager.getEmail());
        
        // Hiển thị tổng tiền
        tvTongTien.setText(formatCurrency(tongTien));
        
        // Button xác nhận
        btnXacNhan.setOnClickListener(v -> createDatLich());
    }
    
    private void createDatLich() {
        String hoTen = etHoTen.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String sdt = etSdt.getText().toString().trim();
        String ghiChu = etGhiChu.getText().toString().trim();
        
        // Validation
        if (TextUtils.isEmpty(hoTen)) {
            etHoTen.setError("Vui lòng nhập họ tên");
            return;
        }
        
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return;
        }
        
        if (TextUtils.isEmpty(sdt) || sdt.length() < 10) {
            etSdt.setError("Số điện thoại không hợp lệ");
            return;
        }
        
        // Tạo request
        DatLichRequest request = new DatLichRequest();
        request.setMaHocVien(sessionManager.getMaNguoiDung());
        request.setMaLichTrinh(maLichTrinh);
        request.setNgayThamGia(ngayThamGia);
        request.setSoLuongNguoi(soLuongNguoi);
        request.setTongTien(tongTien);
        request.setTenNguoiDat(hoTen);
        request.setEmailNguoiDat(email);
        request.setSdtNguoiDat(sdt);
        request.setGhiChu(ghiChu);
        
        // Disable button
        btnXacNhan.setEnabled(false);
        btnXacNhan.setText("Đang xử lý...");
        
        // Call API
        ApiService apiService = RetrofitClient.getApiService();
        Call<DatLichResponse> call = apiService.createDatLich(request);
        
        call.enqueue(new Callback<DatLichResponse>() {
            @Override
            public void onResponse(Call<DatLichResponse> call, Response<DatLichResponse> response) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận đặt lịch");
                
                if (response.isSuccessful() && response.body() != null) {
                    DatLichResponse result = response.body();
                    
                    if (result.isSuccess()) {
                        Toast.makeText(BookingStep3Activity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        
                        // Chuyển sang PaymentActivity
                        DatLich datLich = result.getData();
                        Intent intent = new Intent(BookingStep3Activity.this, PaymentActivity.class);
                        intent.putExtra("maDatLich", datLich.getMaDatLich());
                        intent.putExtra("tenKhoaHoc", tenKhoaHoc);
                        intent.putExtra("ngayThamGia", datLich.getNgayThamGia());
                        intent.putExtra("soLuongNguoi", datLich.getSoLuongNguoi());
                        intent.putExtra("tongTien", datLich.getTongTien().toString());
                        intent.putExtra("trangThai", datLich.getTrangThai());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(BookingStep3Activity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingStep3Activity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<DatLichResponse> call, Throwable t) {
                btnXacNhan.setEnabled(true);
                btnXacNhan.setText("Xác nhận đặt lịch");
                Toast.makeText(BookingStep3Activity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private String formatCurrency(BigDecimal amount) {
        return String.format("%,dđ", amount.longValue());
    }
}
```

---

### Bước 5: Tạo PaymentActivity (Hiển thị thông tin đơn hàng)

**File:** `FE/app/src/main/java/com/example/localcooking_v3t/PaymentActivity.java`

**Chức năng:**
- Hiển thị thông tin đơn hàng đã đặt
- Hiển thị trạng thái: "Chờ Duyệt"
- TODO: Implement thanh toán sau

```java
public class PaymentActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
        // Nhận data từ Intent
        int maDatLich = getIntent().getIntExtra("maDatLich", 0);
        String tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
        String ngayThamGia = getIntent().getStringExtra("ngayThamGia");
        int soLuongNguoi = getIntent().getIntExtra("soLuongNguoi", 1);
        String tongTien = getIntent().getStringExtra("tongTien");
        String trangThai = getIntent().getStringExtra("trangThai");
        
        // Hiển thị thông tin
        // TODO: Bind data to views
        
        Toast.makeText(this, "Đặt lịch thành công! Mã đơn: " + maDatLich, Toast.LENGTH_LONG).show();
    }
}
```

---

## 📱 Layouts Cần Tạo

### 1. activity_booking_step1.xml
- RecyclerView cho danh sách lịch trình
- CalendarView
- TextView hiển thị số chỗ còn lại
- Button "Tiếp tục"

### 2. activity_booking_step2.xml
- TextView hiển thị thông tin lớp
- Button [-] và [+] để điều chỉnh số người
- TextView hiển thị số lượng
- TextView hiển thị tổng tiền
- Button "Tiếp tục"

### 3. activity_booking_step3.xml
- TextView hiển thị tóm tắt thông tin
- EditText: Họ tên, Email, SĐT, Ghi chú
- TextView hiển thị tổng tiền
- Button "Xác nhận đặt lịch"

### 4. activity_payment.xml
- TextView hiển thị thông tin đơn hàng
- TextView hiển thị trạng thái
- Button "Về trang chủ"

---

## 🎯 Checklist Hoàn Thành

- [x] Models (DatLich, DatLichRequest, DatLichResponse, LichTrinhLopHoc, CheckSeatsResponse)
- [ ] Cập nhật ApiService.java
- [ ] BookingStep1Activity + Layout
- [ ] BookingStep2Activity + Layout
- [ ] BookingStep3Activity + Layout
- [ ] PaymentActivity + Layout
- [ ] LichTrinhAdapter (RecyclerView adapter)
- [ ] Test luồng đầy đủ

---

## 🚀 Bắt Đầu Implement

1. Cập nhật `ApiService.java` với các endpoints mới
2. Tạo các layout XML
3. Tạo `BookingStep1Activity`
4. Tạo `BookingStep2Activity`
5. Tạo `BookingStep3Activity`
6. Tạo `PaymentActivity`
7. Test từng bước

Chúc bạn code thành công! 🎉

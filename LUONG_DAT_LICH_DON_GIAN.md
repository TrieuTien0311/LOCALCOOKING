# Luồng Đặt Lịch Đơn Giản (Không Thanh Toán)

## 📋 Tổng Quan

Luồng đặt lịch gồm 5 bước:
1. **Tìm kiếm khóa học** (chọn địa điểm và ngày)
2. **Chọn khóa học** (từ kết quả tìm kiếm)
3. **Chọn lịch trình** (chọn thứ, giờ học)
4. **Điều chỉnh số người** (tăng/giảm số lượng)
5. **Xác nhận thông tin** → Chuyển sang trang thanh toán

---

## 🎯 Luồng Chi Tiết

### **Bước 1: Tìm Kiếm Khóa Học**

**Màn hình:** `SearchActivity` hoặc `HomeFragment`

**UI:**
```
┌─────────────────────────────────────┐
│  Tìm kiếm khóa học                  │
├─────────────────────────────────────┤
│  Địa điểm:                          │
│  [Chọn địa điểm ▼]                  │
│  • Hà Nội                           │
│  • Đà Nẵng                          │
│  • Huế                              │
│  • Cần Thơ                          │
├─────────────────────────────────────┤
│  Ngày học:                          │
│  [Chọn ngày 📅]                     │
│  25/12/2025                         │
├─────────────────────────────────────┤
│         [Tìm kiếm]                  │
└─────────────────────────────────────┘
```

**API Call:**
```java
GET /api/khoahoc/search?diaDiem=Hà Nội&ngayTimKiem=2025-12-25
```

**Response:**
```json
[
  {
    "maKhoaHoc": 1,
    "tenKhoaHoc": "Ẩm thực phố cổ Hà Nội",
    "moTa": "Khám phá hương vị đặc trưng của ẩm thực phố cổ",
    "giaTien": 650000,
    "hinhAnh": "phobo.png",
    "saoTrungBinh": 4.8,
    "soLuongDanhGia": 120,
    "coUuDai": true
  },
  {
    "maKhoaHoc": 2,
    "tenKhoaHoc": "Món Huế đặc sản",
    "moTa": "Học nấu các món ăn truyền thống xứ Huế",
    "giaTien": 580000,
    "hinhAnh": "hue.png",
    "saoTrungBinh": 4.6,
    "soLuongDanhGia": 89,
    "coUuDai": false
  }
]
```

**Hiển thị kết quả:**
- RecyclerView hiển thị danh sách khóa học
- Mỗi card hiển thị: Tên, Giá, Đánh giá, Hình ảnh
- Nút "Đặt lịch" trên mỗi card

**Chuyển sang:** `BookingStep1Activity` (Chọn lịch trình) khi user nhấn "Đặt lịch"

---

### **Bước 2: Chọn Khóa Học**

**Action:** User nhấn nút "Đặt lịch" trên card khóa học

**Data truyền qua Intent:**
```java
Intent intent = new Intent(context, BookingStep1Activity.class);
intent.putExtra("maKhoaHoc", khoaHoc.getMaKhoaHoc());
intent.putExtra("tenKhoaHoc", khoaHoc.getTenKhoaHoc());
intent.putExtra("giaTien", khoaHoc.getGiaTien().toString());
intent.putExtra("hinhAnh", khoaHoc.getHinhAnh());
intent.putExtra("ngayTimKiem", ngayTimKiem); // Ngày user đã chọn ở bước 1
startActivity(intent);
```

---

### **Bước 3: Chọn Lịch Trình**

**Màn hình:** `BookingStep1Activity`

**Nhận data từ Intent:**
```java
int maKhoaHoc = getIntent().getIntExtra("maKhoaHoc", 0);
String tenKhoaHoc = getIntent().getStringExtra("tenKhoaHoc");
String giaTien = getIntent().getStringExtra("giaTien");
String ngayTimKiem = getIntent().getStringExtra("ngayTimKiem"); // "2025-12-25"
```

#### 3.1. Lấy danh sách lịch trình của khóa học

**API Call:**
```java
GET /api/lichtrinh/khoahoc/{maKhoaHoc}
```

**Response:**
```json
[
  {
    "maLichTrinh": 1,
    "maKhoaHoc": 1,
    "maGiaoVien": 1,
    "thuTrongTuan": "2,3,4,5,6,7,CN",
    "gioBatDau": "17:30",
    "gioKetThuc": "20:30",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "soLuongToiDa": 20,
    "trangThai": true
  },
  {
    "maLichTrinh": 2,
    "maKhoaHoc": 1,
    "maGiaoVien": 2,
    "thuTrongTuan": "2,4,6",
    "gioBatDau": "09:00",
    "gioKetThuc": "12:00",
    "diaDiem": "45 Hàng Bạc, Hoàn Kiếm, Hà Nội",
    "soLuongToiDa": 15,
    "trangThai": true
  }
]
```

#### 3.2. User chọn lịch trình

**UI:** RecyclerView hiển thị các lịch trình
```
┌─────────────────────────────────────┐
│  Chọn lịch trình học                │
├─────────────────────────────────────┤
│  ○ Thứ 2, 3, 4, 5, 6, 7, CN         │
│     17:30 - 20:30                   │
│     45 Hàng Bạc, Hoàn Kiếm, HN      │
│     Còn 20 chỗ                      │
├─────────────────────────────────────┤
│  ○ Thứ 2, 4, 6                      │
│     09:00 - 12:00                   │
│     45 Hàng Bạc, Hoàn Kiếm, HN      │
│     Còn 15 chỗ                      │
├─────────────────────────────────────┤
│         [Tiếp tục]                  │
└─────────────────────────────────────┘
```

**Logic:**
- User chọn 1 lịch trình
- Tự động sử dụng `ngayTimKiem` từ bước 1 làm `ngayThamGia`
- Kiểm tra ngày có khớp với `thuTrongTuan` không

#### 3.3. Kiểm tra chỗ trống

**API Call:**
```java
GET /api/lichtrinh/check-seats?maLichTrinh=1&ngayThamGia=2025-12-25
```

**Response:**
```json
{
  "success": true,
  "soChoConLai": 15,
  "message": "Còn chỗ trống"
}
```

**Chuyển sang:** `BookingStep2Activity` (Điều chỉnh số người)

---

### **Bước 4: Điều Chỉnh Số Người**

**Màn hình:** `BookingStep2Activity`

**UI:**
```
┌─────────────────────────────────────┐
│  Ẩm thực phố cổ Hà Nội              │
│  📅 25/12/2025 | ⏰ 17:30 - 20:30   │
│  📍 45 Hàng Bạc, Hoàn Kiếm, Hà Nội  │
├─────────────────────────────────────┤
│  Số lượng người:                    │
│                                     │
│      [-]    2    [+]                │
│                                     │
│  Còn 15 chỗ trống                   │
├─────────────────────────────────────┤
│  Giá: 650,000đ x 2 người            │
│  Tổng: 1,300,000đ                   │
├─────────────────────────────────────┤
│         [Tiếp tục]                  │
└─────────────────────────────────────┘
```

**Logic:**
```java
int soLuongNguoi = 1; // Mặc định
int soChoConLai = 15;

// Nút [-]: Giảm số người (min = 1)
btnMinus.setOnClickListener(v -> {
    if (soLuongNguoi > 1) {
        soLuongNguoi--;
        updateUI();
    }
});

// Nút [+]: Tăng số người (max = soChoConLai)
btnPlus.setOnClickListener(v -> {
    if (soLuongNguoi < soChoConLai) {
        soLuongNguoi++;
        updateUI();
    } else {
        Toast.makeText(this, "Chỉ còn " + soChoConLai + " chỗ", Toast.LENGTH_SHORT).show();
    }
});

// Tính tổng tiền
BigDecimal tongTien = giaTien.multiply(BigDecimal.valueOf(soLuongNguoi));
```

**Chuyển sang:** `BookingStep3Activity` (Xác nhận thông tin)

---

### **Bước 5: Xác Nhận Thông Tin**

**Màn hình:** `BookingStep3Activity`

**UI:**
```
┌─────────────────────────────────────┐
│  Xác nhận thông tin đặt lịch        │
├─────────────────────────────────────┤
│  Khóa học:                          │
│  Ẩm thực phố cổ Hà Nội              │
│                                     │
│  Ngày tham gia:                     │
│  25/12/2025                         │
│                                     │
│  Giờ học:                           │
│  17:30 - 20:30                      │
│                                     │
│  Địa điểm:                          │
│  45 Hàng Bạc, Hoàn Kiếm, Hà Nội     │
│                                     │
│  Số người:                          │
│  2 người                            │
├─────────────────────────────────────┤
│  Thông tin người đặt:               │
│                                     │
│  [Họ tên: Ngô Thị Thảo Vy      ]   │
│  [Email: thaovyn0312@gmail.com ]   │
│  [SĐT: 0934567890              ]   │
│  [Ghi chú (tùy chọn)           ]   │
├─────────────────────────────────────┤
│  Tổng tiền: 1,300,000đ              │
├─────────────────────────────────────┤
│         [Xác nhận đặt lịch]         │
└─────────────────────────────────────┘
```

**Data cần chuẩn bị:**
```java
// Lấy thông tin user từ SessionManager
SessionManager session = new SessionManager(this);
int maHocVien = session.getMaNguoiDung();
String hoTen = session.getHoTen();
String email = session.getEmail();
String soDienThoai = ""; // Có thể lấy từ profile hoặc để user nhập

// Data đặt lịch
DatLichRequest request = new DatLichRequest();
request.setMaHocVien(maHocVien);
request.setMaLichTrinh(maLichTrinh);
request.setNgayThamGia(ngayThamGia); // "2025-12-25"
request.setSoLuongNguoi(soLuongNguoi);
request.setTongTien(tongTien);
request.setTenNguoiDat(hoTen);
request.setEmailNguoiDat(email);
request.setSdtNguoiDat(soDienThoai);
request.setGhiChu(ghiChu);
```

**Validation:**
```java
// Kiểm tra thông tin bắt buộc
if (TextUtils.isEmpty(hoTen)) {
    Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
    return;
}

if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
    Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
    return;
}

if (TextUtils.isEmpty(soDienThoai) || soDienThoai.length() < 10) {
    Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
    return;
}
```

**API Call:**
```java
POST /api/datlich
Content-Type: application/json

{
  "maHocVien": 4,
  "maLichTrinh": 1,
  "ngayThamGia": "2025-12-25",
  "soLuongNguoi": 2,
  "tongTien": 1300000,
  "tenNguoiDat": "Ngô Thị Thảo Vy",
  "emailNguoiDat": "thaovyn0312@gmail.com",
  "sdtNguoiDat": "0934567890",
  "ghiChu": "Muốn học buổi tối"
}
```

**Response Success:**
```json
{
  "success": true,
  "message": "Đặt lịch thành công",
  "data": {
    "maDatLich": 5,
    "maHocVien": 4,
    "maLichTrinh": 1,
    "ngayThamGia": "2025-12-25",
    "soLuongNguoi": 2,
    "tongTien": 1300000,
    "tenNguoiDat": "Ngô Thị Thảo Vy",
    "emailNguoiDat": "thaovyn0312@gmail.com",
    "sdtNguoiDat": "0934567890",
    "ngayDat": "2025-12-20T14:30:00",
    "trangThai": "Chờ Duyệt",
    "ghiChu": "Muốn học buổi tối"
  }
}
```

**Chuyển sang:** `PaymentActivity` (Trang thanh toán)

---

## 🔄 Chuyển Sang Trang Thanh Toán

**Code:**
```java
// Sau khi API POST /api/datlich thành công
if (response.isSuccess()) {
    DatLich datLich = response.getData();
    
    // Chuyển sang PaymentActivity
    Intent intent = new Intent(BookingStep3Activity.this, PaymentActivity.class);
    
    // Truyền data qua Intent
    intent.putExtra("maDatLich", datLich.getMaDatLich());
    intent.putExtra("tenKhoaHoc", tenKhoaHoc);
    intent.putExtra("ngayThamGia", datLich.getNgayThamGia().toString());
    intent.putExtra("soLuongNguoi", datLich.getSoLuongNguoi());
    intent.putExtra("tongTien", datLich.getTongTien().toString());
    intent.putExtra("trangThai", datLich.getTrangThai());
    
    startActivity(intent);
    finish(); // Đóng màn hình đặt lịch
}
```

**Trong PaymentActivity:**
```java
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
    
    // Hiển thị thông tin đơn hàng
    // TODO: Implement payment logic sau
}
```

---

## 📱 Code Mẫu Android

### 1. Model DatLichRequest.java

```java
package com.example.localcooking_v3t.model;

import java.math.BigDecimal;

public class DatLichRequest {
    private Integer maHocVien;
    private Integer maLichTrinh;
    private String ngayThamGia; // Format: "YYYY-MM-DD"
    private Integer soLuongNguoi;
    private BigDecimal tongTien;
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    private String ghiChu;
    
    // Getters & Setters
    public Integer getMaHocVien() { return maHocVien; }
    public void setMaHocVien(Integer maHocVien) { this.maHocVien = maHocVien; }
    
    public Integer getMaLichTrinh() { return maLichTrinh; }
    public void setMaLichTrinh(Integer maLichTrinh) { this.maLichTrinh = maLichTrinh; }
    
    public String getNgayThamGia() { return ngayThamGia; }
    public void setNgayThamGia(String ngayThamGia) { this.ngayThamGia = ngayThamGia; }
    
    public Integer getSoLuongNguoi() { return soLuongNguoi; }
    public void setSoLuongNguoi(Integer soLuongNguoi) { this.soLuongNguoi = soLuongNguoi; }
    
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    
    public String getTenNguoiDat() { return tenNguoiDat; }
    public void setTenNguoiDat(String tenNguoiDat) { this.tenNguoiDat = tenNguoiDat; }
    
    public String getEmailNguoiDat() { return emailNguoiDat; }
    public void setEmailNguoiDat(String emailNguoiDat) { this.emailNguoiDat = emailNguoiDat; }
    
    public String getSdtNguoiDat() { return sdtNguoiDat; }
    public void setSdtNguoiDat(String sdtNguoiDat) { this.sdtNguoiDat = sdtNguoiDat; }
    
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
```

### 2. Model DatLichResponse.java

```java
package com.example.localcooking_v3t.model;

public class DatLichResponse {
    private boolean success;
    private String message;
    private DatLich data;
    
    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public DatLich getData() { return data; }
    public void setData(DatLich data) { this.data = data; }
}
```

### 3. Model DatLich.java

```java
package com.example.localcooking_v3t.model;

import java.math.BigDecimal;

public class DatLich {
    private Integer maDatLich;
    private Integer maHocVien;
    private Integer maLichTrinh;
    private String ngayThamGia;
    private Integer soLuongNguoi;
    private BigDecimal tongTien;
    private String tenNguoiDat;
    private String emailNguoiDat;
    private String sdtNguoiDat;
    private String ngayDat;
    private String trangThai;
    private String ghiChu;
    
    // Getters & Setters (tạo đầy đủ)
}
```

### 4. Model CheckSeatsResponse.java

```java
package com.example.localcooking_v3t.model;

public class CheckSeatsResponse {
    private boolean success;
    private int soChoConLai;
    private String message;
    
    // Getters & Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public int getSoChoConLai() { return soChoConLai; }
    public void setSoChoConLai(int soChoConLai) { this.soChoConLai = soChoConLai; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
```

### 5. ApiService.java (Thêm endpoints)

```java
public interface ApiService {
    
    // ... các API khác ...
    
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
}
```

### 6. BookingStep2Activity.java (Điều chỉnh số người)

```java
public class BookingStep2Activity extends AppCompatActivity {
    
    private TextView tvSoLuong, tvTongTien, tvChoConLai;
    private Button btnMinus, btnPlus, btnContinue;
    
    private int maLichTrinh;
    private String ngayThamGia;
    private BigDecimal giaTien;
    private int soLuongNguoi = 1;
    private int soChoConLai;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_step2);
        
        // Nhận data từ Intent
        maLichTrinh = getIntent().getIntExtra("maLichTrinh", 0);
        ngayThamGia = getIntent().getStringExtra("ngayThamGia");
        giaTien = new BigDecimal(getIntent().getStringExtra("giaTien"));
        soChoConLai = getIntent().getIntExtra("soChoConLai", 0);
        
        // Init views
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvChoConLai = findViewById(R.id.tvChoConLai);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnContinue = findViewById(R.id.btnContinue);
        
        // Setup listeners
        btnMinus.setOnClickListener(v -> {
            if (soLuongNguoi > 1) {
                soLuongNguoi--;
                updateUI();
            }
        });
        
        btnPlus.setOnClickListener(v -> {
            if (soLuongNguoi < soChoConLai) {
                soLuongNguoi++;
                updateUI();
            } else {
                Toast.makeText(this, "Chỉ còn " + soChoConLai + " chỗ", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnContinue.setOnClickListener(v -> {
            // Chuyển sang bước 3
            Intent intent = new Intent(this, BookingStep3Activity.class);
            intent.putExtra("maLichTrinh", maLichTrinh);
            intent.putExtra("ngayThamGia", ngayThamGia);
            intent.putExtra("soLuongNguoi", soLuongNguoi);
            intent.putExtra("tongTien", getTongTien().toString());
            startActivity(intent);
        });
        
        updateUI();
    }
    
    private void updateUI() {
        tvSoLuong.setText(String.valueOf(soLuongNguoi));
        tvTongTien.setText(formatCurrency(getTongTien()));
        tvChoConLai.setText("Còn " + soChoConLai + " chỗ trống");
    }
    
    private BigDecimal getTongTien() {
        return giaTien.multiply(BigDecimal.valueOf(soLuongNguoi));
    }
    
    private String formatCurrency(BigDecimal amount) {
        return String.format("%,dđ", amount.longValue());
    }
}
```

---

## 📊 Tóm Tắt Flow

```
User chọn địa điểm và ngày
    ↓
API: Tìm kiếm khóa học
    ↓
Hiển thị danh sách khóa học
    ↓
User chọn khóa học (nhấn "Đặt lịch")
    ↓
Lấy danh sách lịch trình của khóa học
    ↓
User chọn lịch trình (thứ, giờ)
    ↓
API: Check chỗ trống
    ↓
Điều chỉnh số người (+ / -)
    ↓
Nhập thông tin người đặt
    ↓
API: POST /api/datlich
    ↓
Nhận maDatLich
    ↓
Chuyển sang PaymentActivity
    ↓
(TODO: Thanh toán - làm sau)
```

---

## ✅ Checklist Implement

### Backend (Đã xong)
- ✅ Model DatLich
- ✅ Repository DatLichRepository
- ✅ Service DatLichService
- ✅ Controller DatLichController
- ✅ API check-seats
- ✅ API create đặt lịch

### Frontend (Cần làm)
- ⬜ Model: DatLichRequest, DatLichResponse, CheckSeatsResponse (đã có)
- ⬜ ApiService: Thêm endpoints đặt lịch
- ⬜ SearchActivity: Tìm kiếm khóa học theo địa điểm và ngày
- ⬜ BookingStep1Activity: Chọn lịch trình
- ⬜ BookingStep2Activity: Điều chỉnh số người
- ⬜ BookingStep3Activity: Xác nhận thông tin
- ⬜ PaymentActivity: Hiển thị thông tin đơn hàng (chưa thanh toán)

---

## 🎯 Kết Luận

Luồng này đơn giản, tập trung vào:
1. ✅ Tìm kiếm khóa học theo địa điểm và ngày
2. ✅ Chọn khóa học từ kết quả tìm kiếm
3. ✅ Chọn lịch trình (thứ, giờ học)
4. ✅ Kiểm tra chỗ trống
5. ✅ Điều chỉnh số người
6. ✅ Tạo đặt lịch
7. ✅ Chuyển data sang PaymentActivity

**Ưu điểm:**
- ✅ User chọn ngày trước → Chỉ hiển thị khóa học có lịch vào ngày đó
- ✅ Giảm bước thừa (không cần chọn ngày lại ở bước sau)
- ✅ UX tốt hơn: Tìm kiếm → Chọn → Đặt

**Không bao gồm:**
- ❌ Thanh toán online
- ❌ Áp dụng mã ưu đãi
- ❌ Tích hợp payment gateway

Những phần này sẽ làm sau trong PaymentActivity! 🚀

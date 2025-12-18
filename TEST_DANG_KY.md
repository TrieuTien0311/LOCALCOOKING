# Test Chức Năng Đăng Ký

## 📋 Checklist Trước Khi Test

### Backend
- [ ] Backend đang chạy: `cd BE && ./gradlew bootRun`
- [ ] Test endpoint: `http://localhost:8080/api/nguoidung`
- [ ] Database đang chạy (SQL Server)

### Frontend
- [ ] Sync Gradle thành công
- [ ] Rebuild Project thành công
- [ ] IP đã cấu hình đúng trong `RetrofitClient.java`
- [ ] Emulator hoặc điện thoại đã kết nối

---

## 🧪 Test Cases

### ✅ Test 1: Đăng ký thành công với đầy đủ thông tin
**Bước thực hiện:**
1. Mở app
2. Click "Đăng ký" ở màn hình Login
3. Nhập:
   - Tên đăng nhập: `testuser1`
   - Họ và tên: `Nguyễn Test`
   - Email: `testuser1@example.com`
   - Số điện thoại: `0987654321`
   - Mật khẩu: `123456`
   - Nhập lại mật khẩu: `123456`
4. Tick checkbox điều khoản
5. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Đăng ký thành công"
- ✅ Chuyển sang màn hình Login
- ✅ Kiểm tra database:
```sql
SELECT * FROM NguoiDung WHERE email = 'testuser1@example.com'
```
- ✅ Thấy record mới với vaiTro = "HocVien", trangThai = "HoatDong"

---

### ✅ Test 2: Đăng ký chỉ với thông tin bắt buộc
**Bước thực hiện:**
1. Nhập:
   - Tên đăng nhập: `testuser2`
   - Email: `testuser2@example.com`
   - Mật khẩu: `123456`
   - Nhập lại mật khẩu: `123456`
2. Bỏ trống: Họ tên, Số điện thoại
3. Tick checkbox
4. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Đăng ký thành công
- ✅ hoTen = null, soDienThoai = null trong database

---

### ❌ Test 3: Email đã tồn tại
**Bước thực hiện:**
1. Nhập email: `admin@localcooking.vn` (đã có trong DB)
2. Nhập đầy đủ thông tin khác
3. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Email đã được sử dụng"
- ✅ Không tạo record mới
- ✅ Vẫn ở màn hình Register

---

### ❌ Test 4: Tên đăng nhập đã tồn tại
**Bước thực hiện:**
1. Nhập tên đăng nhập: `admin` (đã có trong DB)
2. Nhập email mới: `newemail@example.com`
3. Nhập đầy đủ thông tin khác
4. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Tên đăng nhập đã được sử dụng"
- ✅ Không tạo record mới

---

### ❌ Test 5: Mật khẩu không khớp
**Bước thực hiện:**
1. Nhập mật khẩu: `123456`
2. Nhập lại mật khẩu: `654321`
3. Nhập đầy đủ thông tin khác

**Kết quả mong đợi:**
- ✅ Error message hiển thị real-time: "Mật khẩu bạn nhập lại không khớp"
- ✅ Không thể click "Đăng ký" (hoặc click sẽ báo lỗi)

---

### ❌ Test 6: Mật khẩu quá ngắn
**Bước thực hiện:**
1. Nhập mật khẩu: `123` (< 6 ký tự)
2. Tab ra ngoài

**Kết quả mong đợi:**
- ✅ Error message hiển thị: "Mật khẩu phải có ít nhất 6 ký tự"

---

### ❌ Test 7: Email không hợp lệ
**Bước thực hiện:**
1. Nhập email: `invalid-email` (không có @)
2. Nhập đầy đủ thông tin khác
3. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Email không hợp lệ"

---

### ❌ Test 8: Thiếu tên đăng nhập
**Bước thực hiện:**
1. Bỏ trống tên đăng nhập
2. Nhập đầy đủ thông tin khác
3. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Vui lòng nhập tên đăng nhập"
- ✅ Focus vào field tên đăng nhập

---

### ❌ Test 9: Thiếu email
**Bước thực hiện:**
1. Bỏ trống email
2. Nhập đầy đủ thông tin khác
3. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Vui lòng nhập email"
- ✅ Focus vào field email

---

### ❌ Test 10: Chưa tick điều khoản
**Bước thực hiện:**
1. Nhập đầy đủ thông tin
2. Không tick checkbox điều khoản
3. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Vui lòng đồng ý với điều khoản"

---

### 🔄 Test 11: Toggle show/hide password
**Bước thực hiện:**
1. Nhập mật khẩu: `123456`
2. Click icon mắt ở field mật khẩu
3. Click lại icon mắt

**Kết quả mong đợi:**
- ✅ Lần 1: Hiển thị mật khẩu dạng text
- ✅ Lần 2: Ẩn mật khẩu dạng dots
- ✅ Icon thay đổi giữa mắt mở/mắt đóng

---

### 🔄 Test 12: Real-time validation
**Bước thực hiện:**
1. Nhập mật khẩu: `12` → `123` → `1234` → `12345` → `123456`
2. Quan sát error message

**Kết quả mong đợi:**
- ✅ < 6 ký tự: Hiển thị error
- ✅ >= 6 ký tự: Ẩn error

---

### 🔄 Test 13: Đăng nhập sau khi đăng ký
**Bước thực hiện:**
1. Đăng ký thành công với:
   - Email: `testlogin@example.com`
   - Mật khẩu: `123456`
2. Ở màn hình Login, nhập email và mật khẩu vừa tạo
3. Click "Đăng nhập"

**Kết quả mong đợi:**
- ✅ Đăng nhập thành công
- ✅ Chuyển sang HomeFragment
- ✅ Hiển thị "Chào [tên đăng nhập]"

---

### 🌐 Test 14: Lỗi kết nối
**Bước thực hiện:**
1. Tắt Backend
2. Nhập đầy đủ thông tin
3. Click "Đăng ký"

**Kết quả mong đợi:**
- ✅ Toast: "Lỗi: [error message]"
- ✅ Button "Đăng ký" enable lại

---

### 🔙 Test 15: Quay lại từ Register
**Bước thực hiện:**
1. Ở màn hình Register
2. Click "Quay lại" hoặc nút Back

**Kết quả mong đợi:**
- ✅ Quay về màn hình Login
- ✅ Không mất dữ liệu đã nhập ở Login (nếu có)

---

### 📱 Test 16: Click "Đăng nhập" ở Register
**Bước thực hiện:**
1. Ở màn hình Register
2. Click text "Đăng nhập" ở dưới cùng

**Kết quả mong đợi:**
- ✅ Chuyển sang màn hình Login

---

## 🗄️ Kiểm Tra Database

### Query 1: Xem tất cả người dùng mới
```sql
SELECT * FROM NguoiDung 
WHERE ngayTao >= CAST(GETDATE() AS DATE)
ORDER BY ngayTao DESC
```

### Query 2: Đếm số người dùng theo vai trò
```sql
SELECT vaiTro, COUNT(*) as SoLuong
FROM NguoiDung
GROUP BY vaiTro
```

### Query 3: Xem người dùng vừa tạo
```sql
SELECT TOP 5 * FROM NguoiDung
ORDER BY maNguoiDung DESC
```

### Query 4: Kiểm tra email unique
```sql
SELECT email, COUNT(*) as SoLuong
FROM NguoiDung
GROUP BY email
HAVING COUNT(*) > 1
```
*(Không nên có kết quả)*

### Query 5: Kiểm tra tên đăng nhập unique
```sql
SELECT tenDangNhap, COUNT(*) as SoLuong
FROM NguoiDung
GROUP BY tenDangNhap
HAVING COUNT(*) > 1
```
*(Không nên có kết quả)*

---

## 🐛 Debug

### Xem Log Android
1. Mở Logcat trong Android Studio
2. Filter: `System.out` hoặc `Register`
3. Xem request/response

### Xem Log Backend
1. Xem console của `./gradlew bootRun`
2. Tìm dòng:
```
Hibernate: INSERT INTO NguoiDung ...
```

### Test API trực tiếp
```bash
curl -X POST http://localhost:8080/api/nguoidung/register \
  -H "Content-Type: application/json" \
  -d '{
    "tenDangNhap": "curltest",
    "matKhau": "123456",
    "hoTen": "Curl Test",
    "email": "curltest@example.com",
    "soDienThoai": "0123456789"
  }'
```

---

## ✅ Kết Quả Mong Đợi

Sau khi test xong:
- [ ] Tất cả test case PASS
- [ ] Không có crash
- [ ] Không có memory leak
- [ ] UI responsive
- [ ] Error message rõ ràng
- [ ] Database nhận đúng dữ liệu
- [ ] Có thể đăng nhập với tài khoản mới

---

## 📊 Báo Cáo Test

| Test Case | Kết Quả | Ghi Chú |
|-----------|---------|---------|
| Test 1: Đăng ký thành công | ⬜ PASS / ⬜ FAIL | |
| Test 2: Chỉ thông tin bắt buộc | ⬜ PASS / ⬜ FAIL | |
| Test 3: Email đã tồn tại | ⬜ PASS / ⬜ FAIL | |
| Test 4: Tên đăng nhập đã tồn tại | ⬜ PASS / ⬜ FAIL | |
| Test 5: Mật khẩu không khớp | ⬜ PASS / ⬜ FAIL | |
| Test 6: Mật khẩu quá ngắn | ⬜ PASS / ⬜ FAIL | |
| Test 7: Email không hợp lệ | ⬜ PASS / ⬜ FAIL | |
| Test 8: Thiếu tên đăng nhập | ⬜ PASS / ⬜ FAIL | |
| Test 9: Thiếu email | ⬜ PASS / ⬜ FAIL | |
| Test 10: Chưa tick điều khoản | ⬜ PASS / ⬜ FAIL | |
| Test 11: Toggle password | ⬜ PASS / ⬜ FAIL | |
| Test 12: Real-time validation | ⬜ PASS / ⬜ FAIL | |
| Test 13: Đăng nhập sau đăng ký | ⬜ PASS / ⬜ FAIL | |
| Test 14: Lỗi kết nối | ⬜ PASS / ⬜ FAIL | |
| Test 15: Quay lại | ⬜ PASS / ⬜ FAIL | |
| Test 16: Click "Đăng nhập" | ⬜ PASS / ⬜ FAIL | |

---

## 🎯 Tổng Kết

**Tổng số test:** 16
**PASS:** ___
**FAIL:** ___
**Tỷ lệ thành công:** ___%

**Ghi chú:**
_[Ghi chú về các vấn đề phát hiện]_

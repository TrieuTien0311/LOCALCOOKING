# Cập Nhật Chức Năng Đăng Xuất

## ✅ Đã Hoàn Thành

### 1. ProfileFragment.java
- Hiển thị thông tin người dùng đã đăng nhập (Họ tên, Email)
- Xử lý đăng xuất đúng cách:
  - Xóa session khi đăng xuất
  - Chuyển về trang Login
  - Clear activity stack
- Cập nhật UI khi quay lại fragment (onResume)

### 2. HomeFragment.java (Đã có sẵn)
- Hiển thị "Chào + tên đăng nhập" khi đã đăng nhập
- Hiển thị "Đăng nhập" khi chưa đăng nhập
- Ẩn/hiện mũi tên tùy theo trạng thái đăng nhập
- Tự động cập nhật khi quay lại (onResume)

### 3. Login.java (Đã có sẵn)
- Kiểm tra session khi mở app
- Tự động chuyển đến Home nếu đã đăng nhập
- Lưu session khi đăng nhập thành công

## 🎯 Luồng Hoạt Động

### Khi Đăng Nhập:
1. User nhập email/password → Click "Đăng nhập"
2. API kiểm tra thông tin
3. Nếu đúng:
   - Lưu session (maNguoiDung, tenDangNhap, hoTen, email, vaiTro)
   - Chuyển đến Header (HomeFragment)
   - HomeFragment hiển thị: "Chào [tên đăng nhập]"
   - ProfileFragment hiển thị: Họ tên + Email

### Khi Đăng Xuất:
1. User vào ProfileFragment → Click "Đăng xuất"
2. Xóa session (SessionManager.logout())
3. Chuyển về Login
4. Clear toàn bộ activity stack
5. Lần sau mở app:
   - Login kiểm tra session → Không có
   - Hiển thị màn hình đăng nhập
   - HomeFragment hiển thị: "Đăng nhập"

## 📱 Hiển Thị Theo Trạng Thái

### Khi CHƯA Đăng Nhập:
- **HomeFragment**: 
  - tvHello: "Đăng nhập"
  - ivArrow: Hiển thị
  - Click vào → Chuyển đến Login
  
- **ProfileFragment**:
  - tvUserName: "Khách"
  - tvUserEmail: "Vui lòng đăng nhập"
  - btnLogout: "Đăng nhập"
  - Click vào → Chuyển đến Login

### Khi ĐÃ Đăng Nhập:
- **HomeFragment**:
  - tvHello: "Chào admin" (hoặc tên đăng nhập khác)
  - ivArrow: Ẩn
  - Click vào → Không làm gì
  
- **ProfileFragment**:
  - tvUserName: "Nguyễn Văn An" (hoặc họ tên)
  - tvUserEmail: "nguyenvanan@gmail.com"
  - btnLogout: "Đăng xuất"
  - Click vào → Đăng xuất và về Login

## 🔄 Tự Động Cập Nhật

Cả HomeFragment và ProfileFragment đều có method `onResume()` để tự động cập nhật UI khi:
- Quay lại từ màn hình khác
- Chuyển tab trong Header
- Sau khi đăng nhập/đăng xuất

## 🧪 Test Case

### Test 1: Đăng nhập lần đầu
1. Mở app → Màn hình Login
2. Nhập: admin@localcooking.vn / admin123
3. Click "Đăng nhập"
4. ✅ Chuyển đến Home
5. ✅ Hiển thị "Chào admin"
6. ✅ Mũi tên bị ẩn

### Test 2: Xem Profile
1. Click tab "Trang cá nhân"
2. ✅ Hiển thị "Quản Trị Viên"
3. ✅ Hiển thị "admin@localcooking.vn"
4. ✅ Nút hiển thị "Đăng xuất"

### Test 3: Đăng xuất
1. Ở ProfileFragment → Click "Đăng xuất"
2. ✅ Toast: "Đã đăng xuất thành công"
3. ✅ Chuyển về Login
4. ✅ Không thể back về Home

### Test 4: Mở lại app sau khi đăng xuất
1. Đóng app
2. Mở lại app
3. ✅ Hiển thị màn hình Login
4. ✅ Không tự động đăng nhập

### Test 5: Mở lại app khi đang đăng nhập
1. Đăng nhập thành công
2. Đóng app (không đăng xuất)
3. Mở lại app
4. ✅ Tự động vào Home
5. ✅ Vẫn hiển thị "Chào admin"

### Test 6: Chuyển đổi giữa các tab
1. Đăng nhập thành công
2. Chuyển từ Home → Profile
3. ✅ Profile hiển thị đúng thông tin
4. Chuyển từ Profile → Home
5. ✅ Home vẫn hiển thị "Chào admin"

### Test 7: Đăng nhập nhiều tài khoản khác nhau
1. Đăng nhập: admin@localcooking.vn
2. ✅ Hiển thị "Chào admin"
3. Đăng xuất
4. Đăng nhập: levancuong@gmail.com / hv123
5. ✅ Hiển thị "Chào hocvien1"
6. Profile hiển thị: "Lê Văn Cường"

## 🔐 Session Management

SessionManager lưu trữ:
- `isLoggedIn`: true/false
- `maNguoiDung`: ID người dùng
- `tenDangNhap`: Tên đăng nhập
- `hoTen`: Họ và tên
- `email`: Email
- `vaiTro`: Admin/GiaoVien/HocVien

Dữ liệu được lưu trong SharedPreferences và tồn tại cho đến khi:
- User đăng xuất
- User xóa data app
- User gọi `sessionManager.logout()`

## 📝 Lưu Ý

1. **Session tồn tại vĩnh viễn** cho đến khi đăng xuất (giống Facebook, Instagram)
2. **Không có timeout** - User phải tự đăng xuất
3. **Mỗi lần đăng nhập** sẽ ghi đè session cũ
4. **ProfileFragment tự động cập nhật** khi chuyển tab
5. **HomeFragment tự động cập nhật** khi quay lại

## 🎉 Kết Quả

✅ Đăng nhập đúng tài khoản → Hiển thị đúng thông tin người đó
✅ Đăng xuất → Xóa session và về Login
✅ HomeFragment hiển thị "Đăng nhập" khi chưa đăng nhập
✅ HomeFragment hiển thị "Chào [tên]" khi đã đăng nhập
✅ ProfileFragment hiển thị thông tin người dùng hiện tại
✅ Tự động cập nhật UI khi chuyển tab

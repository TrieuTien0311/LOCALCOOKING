# Luồng Đăng Nhập & Đăng Xuất

## ✅ Đã Cập Nhật

### ProfileFragment.java
- Khi đăng xuất → Xóa session → Quay về **HomeFragment** (không phải Login)
- HomeFragment sẽ tự động hiển thị "Đăng nhập"

### Header.java
- Thêm method `navigateToHome()` để chuyển về HomeFragment

### Login.java
- Nút "Quay lại" → Về Header (HomeFragment)

---

## 🎯 Luồng Hoạt Động

### 1️⃣ Mở App Lần Đầu (Chưa Đăng Nhập)
```
Splash → Header → HomeFragment
├─ tvHello: "Đăng nhập"
├─ ivArrow: Hiển thị
└─ Click tvHello → Chuyển đến Login
```

### 2️⃣ Đăng Nhập Thành Công
```
Login → Nhập email/password → Click "Đăng nhập"
└─ API thành công → Lưu session → Header → HomeFragment
   ├─ tvHello: "Chào admin"
   └─ ivArrow: Ẩn
```

### 3️⃣ Xem Profile
```
HomeFragment → Click tab "Trang cá nhân" → ProfileFragment
├─ tvUserName: "Quản Trị Viên"
├─ tvUserEmail: "admin@localcooking.vn"
└─ btnLogout: "Đăng xuất"
```

### 4️⃣ Đăng Xuất
```
ProfileFragment → Click "Đăng xuất"
├─ Xóa session
├─ Toast: "Đã đăng xuất thành công"
├─ Cập nhật ProfileFragment:
│  ├─ tvUserName: "Khách"
│  ├─ tvUserEmail: "Vui lòng đăng nhập"
│  └─ btnLogout: "Đăng nhập"
└─ Chuyển về HomeFragment:
   ├─ tvHello: "Đăng nhập"
   └─ ivArrow: Hiển thị
```

### 5️⃣ Click "Đăng nhập" Trên Profile (Khi Chưa Đăng Nhập)
```
ProfileFragment → Click "Đăng nhập" (btnLogout)
└─ Chuyển đến Login
```

### 6️⃣ Click "Đăng nhập" Trên Home (Khi Chưa Đăng Nhập)
```
HomeFragment → Click "Đăng nhập" (tvHello)
└─ Chuyển đến Login
```

### 7️⃣ Quay Lại Từ Login (Không Đăng Nhập)
```
Login → Click "Quay lại"
└─ Về Header → HomeFragment
   ├─ tvHello: "Đăng nhập"
   └─ ivArrow: Hiển thị
```

---

## 📱 Hiển Thị Theo Trạng Thái

### Khi CHƯA Đăng Nhập:

**HomeFragment:**
- tvHello: **"Đăng nhập"**
- ivArrow: **Hiển thị**
- Click tvHello → Chuyển đến Login

**ProfileFragment:**
- tvUserName: **"Khách"**
- tvUserEmail: **"Vui lòng đăng nhập"**
- btnLogout: **"Đăng nhập"**
- Click btnLogout → Chuyển đến Login

---

### Khi ĐÃ Đăng Nhập:

**HomeFragment:**
- tvHello: **"Chào admin"** (hoặc tên khác)
- ivArrow: **Ẩn**
- Click tvHello → Không làm gì

**ProfileFragment:**
- tvUserName: **"Quản Trị Viên"** (hoặc họ tên)
- tvUserEmail: **"admin@localcooking.vn"**
- btnLogout: **"Đăng xuất"**
- Click btnLogout → Đăng xuất và về HomeFragment

---

## 🔄 Tự Động Cập Nhật

### HomeFragment.onResume()
```java
private void updateUserDisplay() {
    if (sessionManager.isLoggedIn()) {
        tvHello.setText("Chào " + tenDangNhap);
        ivArrow.setVisibility(View.GONE);
    } else {
        tvHello.setText("Đăng nhập");
        ivArrow.setVisibility(View.VISIBLE);
    }
}
```

### ProfileFragment.onResume()
```java
private void loadUserInfo() {
    if (sessionManager.isLoggedIn()) {
        tvUserName.setText(hoTen);
        tvUserEmail.setText(email);
        btnLogout.setText("Đăng xuất");
    } else {
        tvUserName.setText("Khách");
        tvUserEmail.setText("Vui lòng đăng nhập");
        btnLogout.setText("Đăng nhập");
    }
}
```

---

## 🧪 Test Case

### Test 1: Mở app lần đầu
1. Mở app
2. ✅ HomeFragment hiển thị "Đăng nhập"
3. ✅ Mũi tên hiển thị

### Test 2: Click "Đăng nhập" trên Home
1. Click "Đăng nhập"
2. ✅ Chuyển đến màn hình Login

### Test 3: Quay lại từ Login
1. Ở Login → Click "Quay lại"
2. ✅ Về HomeFragment
3. ✅ Vẫn hiển thị "Đăng nhập"

### Test 4: Đăng nhập thành công
1. Nhập email/password → Click "Đăng nhập"
2. ✅ Chuyển đến HomeFragment
3. ✅ Hiển thị "Chào admin"
4. ✅ Mũi tên bị ẩn

### Test 5: Xem Profile khi đã đăng nhập
1. Click tab "Trang cá nhân"
2. ✅ Hiển thị "Quản Trị Viên"
3. ✅ Hiển thị email
4. ✅ Nút hiển thị "Đăng xuất"

### Test 6: Đăng xuất
1. Click "Đăng xuất"
2. ✅ Toast: "Đã đăng xuất thành công"
3. ✅ ProfileFragment cập nhật: "Khách" + "Đăng nhập"
4. ✅ Chuyển về HomeFragment
5. ✅ HomeFragment hiển thị "Đăng nhập"
6. ✅ Mũi tên xuất hiện lại

### Test 7: Click "Đăng nhập" trên Profile (sau khi đăng xuất)
1. Ở ProfileFragment → Click "Đăng nhập"
2. ✅ Chuyển đến Login

### Test 8: Chuyển tab sau khi đăng xuất
1. Đăng xuất
2. Chuyển từ Home → Profile → Home
3. ✅ Home vẫn hiển thị "Đăng nhập"
4. ✅ Profile vẫn hiển thị "Khách"

### Test 9: Đăng nhập lại
1. Sau khi đăng xuất → Click "Đăng nhập"
2. Đăng nhập với tài khoản khác
3. ✅ Hiển thị đúng tên tài khoản mới

---

## 🎉 Kết Quả

✅ **Ban đầu**: HomeFragment hiển thị "Đăng nhập"
✅ **Đăng nhập**: HomeFragment hiển thị "Chào + tên"
✅ **Đăng xuất**: Về HomeFragment, hiển thị "Đăng nhập"
✅ **Click "Đăng nhập"**: Chuyển đến Login
✅ **Quay lại từ Login**: Về HomeFragment
✅ **Tự động cập nhật**: Khi chuyển tab hoặc quay lại

---

## 📝 Lưu Ý

1. **Session tồn tại vĩnh viễn** cho đến khi đăng xuất
2. **HomeFragment tự động cập nhật** trong `onResume()`
3. **ProfileFragment tự động cập nhật** trong `onResume()`
4. **Đăng xuất không clear activity stack** - chỉ xóa session và về Home
5. **Click "Quay lại" từ Login** → Về Header, không thoát app

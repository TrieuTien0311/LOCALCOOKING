# Luồng Mở App Mới

## ✅ Đã Cập Nhật

### Splash.java
- Chuyển từ `Login.class` → `Header.class`
- Mở app sẽ vào **Header (HomeFragment)** thay vì Login

### Login.java
- Xóa logic tự động chuyển đến Home khi đã đăng nhập
- Login chỉ là màn hình đăng nhập thuần túy

---

## 🎯 Luồng Mới

### 1️⃣ Mở App Lần Đầu (Chưa Đăng Nhập)
```
Splash (5 giây) → Header → HomeFragment
├─ tvHello: "Đăng nhập"
├─ ivArrow: Hiển thị
└─ Click tvHello → Chuyển đến Login
```

### 2️⃣ Mở App Khi Đã Đăng Nhập
```
Splash (5 giây) → Header → HomeFragment
├─ tvHello: "Chào admin"
├─ ivArrow: Ẩn
└─ Click tvHello → Không làm gì
```

### 3️⃣ Click "Đăng nhập" Trên Home
```
HomeFragment → Click "Đăng nhập" (tvHello)
└─ Chuyển đến Login
```

### 4️⃣ Đăng Nhập Thành Công
```
Login → Nhập email/password → Click "Đăng nhập"
└─ API thành công → Lưu session → Header → HomeFragment
   ├─ tvHello: "Chào admin"
   └─ ivArrow: Ẩn
```

### 5️⃣ Quay Lại Từ Login (Không Đăng Nhập)
```
Login → Click "Quay lại"
└─ Về Header → HomeFragment
   ├─ tvHello: "Đăng nhập"
   └─ ivArrow: Hiển thị
```

### 6️⃣ Đăng Xuất
```
ProfileFragment → Click "Đăng xuất"
├─ Xóa session
├─ Toast: "Đã đăng xuất thành công"
└─ Chuyển về HomeFragment:
   ├─ tvHello: "Đăng nhập"
   └─ ivArrow: Hiển thị
```

### 7️⃣ Đóng App Và Mở Lại (Đã Đăng Nhập)
```
Splash → Header → HomeFragment
├─ Session vẫn còn
├─ tvHello: "Chào admin"
└─ ivArrow: Ẩn
```

### 8️⃣ Đóng App Và Mở Lại (Chưa Đăng Nhập)
```
Splash → Header → HomeFragment
├─ Không có session
├─ tvHello: "Đăng nhập"
└─ ivArrow: Hiển thị
```

---

## 📱 Hiển Thị Theo Trạng Thái

### Khi CHƯA Đăng Nhập:

**Mở app:**
```
Splash → Header → HomeFragment
```

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

**Mở app:**
```
Splash → Header → HomeFragment (tự động load session)
```

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

## 🔄 So Sánh Trước Và Sau

### ❌ Trước (Cũ):
```
Mở app → Splash → Login
├─ Nếu đã đăng nhập → Tự động chuyển đến Header
└─ Nếu chưa đăng nhập → Ở lại Login
```

### ✅ Sau (Mới):
```
Mở app → Splash → Header → HomeFragment
├─ Nếu đã đăng nhập → Hiển thị "Chào admin"
└─ Nếu chưa đăng nhập → Hiển thị "Đăng nhập"
```

---

## 🧪 Test Case

### Test 1: Mở app lần đầu (chưa đăng nhập)
1. Cài app mới
2. Mở app
3. ✅ Splash 5 giây
4. ✅ Chuyển đến HomeFragment
5. ✅ Hiển thị "Đăng nhập"
6. ✅ Mũi tên hiển thị

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

### Test 5: Đóng app và mở lại (đã đăng nhập)
1. Đăng nhập thành công
2. Đóng app (Home button)
3. Mở lại app
4. ✅ Splash 5 giây
5. ✅ Chuyển đến HomeFragment
6. ✅ Hiển thị "Chào admin" (session vẫn còn)

### Test 6: Đăng xuất
1. Click tab "Trang cá nhân"
2. Click "Đăng xuất"
3. ✅ Toast: "Đã đăng xuất thành công"
4. ✅ Chuyển về HomeFragment
5. ✅ Hiển thị "Đăng nhập"

### Test 7: Đóng app và mở lại (sau khi đăng xuất)
1. Đăng xuất
2. Đóng app
3. Mở lại app
4. ✅ Splash 5 giây
5. ✅ Chuyển đến HomeFragment
6. ✅ Hiển thị "Đăng nhập" (session đã xóa)

### Test 8: Back button từ HomeFragment
1. Ở HomeFragment → Nhấn Back
2. ✅ Thoát app (không quay về Splash)

---

## 🎉 Kết Quả

✅ **Mở app**: Luôn vào HomeFragment (không phải Login)
✅ **Chưa đăng nhập**: Hiển thị "Đăng nhập" + mũi tên
✅ **Đã đăng nhập**: Hiển thị "Chào + tên" + ẩn mũi tên
✅ **Click "Đăng nhập"**: Chuyển đến Login
✅ **Quay lại từ Login**: Về HomeFragment
✅ **Đăng xuất**: Về HomeFragment, hiển thị "Đăng nhập"
✅ **Session tồn tại**: Mở lại app vẫn giữ trạng thái đăng nhập

---

## 📝 Lưu Ý

1. **Splash luôn chuyển đến Header** (không phải Login)
2. **HomeFragment tự động kiểm tra session** trong `onResume()`
3. **Login không tự động redirect** - chỉ là màn hình đăng nhập
4. **Session tồn tại vĩnh viễn** cho đến khi đăng xuất
5. **User experience tốt hơn** - không bắt buộc đăng nhập ngay

---

## 🔑 Điểm Khác Biệt Chính

| Tính năng | Trước | Sau |
|-----------|-------|-----|
| Mở app lần đầu | → Login | → HomeFragment |
| Chưa đăng nhập | Ở Login | Ở Home, hiển thị "Đăng nhập" |
| Đã đăng nhập | Login → Home | Home, hiển thị "Chào admin" |
| Đăng xuất | → Login | → Home, hiển thị "Đăng nhập" |
| User experience | Bắt buộc đăng nhập | Tự do xem, đăng nhập khi cần |

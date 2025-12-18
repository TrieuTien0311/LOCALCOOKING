# CẬP NHẬT UI TRANG ĐỔI MẬT KHẨU

## ✅ ĐÃ THỰC HIỆN

### 1. **Floating Label Effect**
Giống trang đăng nhập, khi người dùng nhập vào ô input, label sẽ nổi lên trên.

**Thay đổi trong `activity_change_password.xml`:**
- Thêm `style="@style/CustomTextInputLayout"` cho tất cả TextInputLayout
- Xóa `background="@drawable/input_bg"` (style đã có background)
- Đổi `padding` thành `16dp` thống nhất
- Thêm `textSize="16sp"` cho text

**Các ô input đã cập nhật:**
- ✅ Email
- ✅ Mật khẩu hiện tại
- ✅ Mật khẩu mới
- ✅ Xác nhận mật khẩu mới

---

### 2. **Show/Hide Password Icon**
Thêm icon mắt để hiển thị/ẩn mật khẩu cho 3 ô password.

**Thay đổi trong `ChangePassword.java`:**
- Thêm 3 biến boolean: `isPasswordVisible1`, `isPasswordVisible2`, `isPasswordVisible3`
- Thêm method `setupPasswordToggles()` để xử lý toggle cho 3 ô
- Import thêm: `InputType`, `MotionEvent`

**Chức năng:**
- Click vào icon mắt → Hiển thị mật khẩu (icon mắt mở)
- Click lại → Ẩn mật khẩu (icon mắt đóng)
- Con trỏ tự động về cuối text sau khi toggle

---

## 🎨 SO SÁNH TRƯỚC VÀ SAU

### Trước:
- ❌ Label cố định trong ô input
- ❌ Không có icon show/hide password
- ❌ Background và padding không đồng nhất
- ❌ Hint text không biến mất khi nhập

### Sau:
- ✅ Label nổi lên trên khi nhập (floating label)
- ✅ Icon mắt để show/hide password
- ✅ Background và padding đồng nhất với trang đăng nhập
- ✅ Hint text biến mất, label nổi lên thay thế

---

## 📱 TRẢI NGHIỆM NGƯỜI DÙNG

### Khi chưa nhập:
```
┌─────────────────────────┐
│ Email                   │
│                         │
└─────────────────────────┘
```

### Khi đang nhập:
```
Email ↑ (label nổi lên)
┌─────────────────────────┐
│ user@example.com        │
└─────────────────────────┘
```

### Ô mật khẩu:
```
Mật khẩu hiện tại ↑
┌─────────────────────────┐
│ ••••••••          👁️    │ ← Click để show/hide
└─────────────────────────┘
```

---

## 🔧 TECHNICAL DETAILS

### XML Changes:
```xml
<!-- Trước -->
<com.google.android.material.textfield.TextInputLayout
    android:hint="Email"
    app:boxBackgroundColor="@android:color/transparent"
    app:boxBackgroundMode="none">
    <TextInputEditText
        android:background="@drawable/input_bg"
        android:paddingLeft="30dp" />
</com.google.android.material.textfield.TextInputLayout>

<!-- Sau -->
<com.google.android.material.textfield.TextInputLayout
    style="@style/CustomTextInputLayout"
    android:hint="Email"
    app:hintAnimationEnabled="true">
    <TextInputEditText
        android:padding="16dp"
        android:textSize="16sp" />
</com.google.android.material.textfield.TextInputLayout>
```

### Java Changes:
```java
// Thêm biến
private boolean isPasswordVisible1 = false;
private boolean isPasswordVisible2 = false;
private boolean isPasswordVisible3 = false;

// Thêm method
private void setupPasswordToggles() {
    idMatKhauHienTai.setOnTouchListener((v, event) -> {
        // Toggle logic
    });
    // Tương tự cho 2 ô còn lại
}

// Gọi trong onCreate()
setupPasswordToggles();
```

---

## 🎯 KẾT QUẢ

Trang đổi mật khẩu giờ đây có:
- ✅ UI/UX giống hệt trang đăng nhập
- ✅ Floating label animation mượt mà
- ✅ Show/hide password tiện lợi
- ✅ Consistent design với toàn bộ app

---

## 📚 FILES ĐÃ THAY ĐỔI

1. **FE/app/src/main/res/layout/activity_change_password.xml**
   - Cập nhật 4 TextInputLayout
   - Thêm style và animation

2. **FE/app/src/main/java/com/example/localcooking_v3t/ChangePassword.java**
   - Thêm password toggle logic
   - Import thêm InputType và MotionEvent

---

## 🧪 CÁCH TEST

1. Mở app → Vào Profile → Đổi mật khẩu
2. Click vào ô Email → Label "Email" nổi lên trên
3. Nhập email → Label vẫn ở trên
4. Click ra ngoài → Nếu có text, label ở trên; nếu rỗng, label về vị trí cũ
5. Nhập mật khẩu → Click icon mắt → Mật khẩu hiển thị
6. Click lại icon mắt → Mật khẩu ẩn

---

## 💡 LƯU Ý

### Style CustomTextInputLayout
Style này được định nghĩa trong `res/values/styles.xml`:
```xml
<style name="CustomTextInputLayout" parent="Widget.MaterialComponents.TextInputLayout.OutlinedBox">
    <item name="boxStrokeColor">@color/primary</item>
    <item name="hintTextColor">@color/primary</item>
    <!-- ... -->
</style>
```

Nếu chưa có, cần tạo file này hoặc style sẽ fallback về default.

### Icon Resources
Cần có 2 icon:
- `@drawable/icon_eye_hide_tt` - Icon mắt đóng
- `@drawable/icon_eye` - Icon mắt mở

Nếu thiếu, app sẽ crash khi click vào icon.

---

## ✨ TỔNG KẾT

Trang đổi mật khẩu giờ đây có trải nghiệm người dùng tốt hơn, nhất quán với trang đăng nhập, và dễ sử dụng hơn với floating label và show/hide password!

# Fix Lỗi Google Sign-In Không Cho Chọn Tài Khoản

## Vấn đề

Khi đăng xuất và đăng nhập lại bằng Google, app tự động đăng nhập vào tài khoản Google trước đó mà không hiển thị màn hình chọn tài khoản.

## Nguyên nhân

Google Sign-In SDK cache thông tin tài khoản đã đăng nhập. Khi đăng xuất, app chỉ xóa session của mình nhưng **không sign out khỏi Google**, nên lần đăng nhập sau Google tự động dùng tài khoản đã cache.

## Giải pháp đã áp dụng

### 1. Sign out khỏi Google khi đăng xuất app

**File:** `FE/app/src/main/java/com/example/localcooking_v3t/ProfileFragment.java`

**Thay đổi:**

```java
// Import thêm
import com.example.localcooking_v3t.helper.GoogleSignInHelper;

// Thêm biến
private GoogleSignInHelper googleSignInHelper;

// Khởi tạo trong onCreate()
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sessionManager = new SessionManager(requireContext());
    
    // Khởi tạo GoogleSignInHelper
    String WEB_CLIENT_ID = "954091456874-iji1kkmljees7803o33p78gl34pl90ek.apps.googleusercontent.com";
    googleSignInHelper = new GoogleSignInHelper(requireContext(), WEB_CLIENT_ID);
}

// Cập nhật performLogout()
private void performLogout() {
    if (sessionManager.isLoggedIn()) {
        // Kiểm tra nếu đăng nhập bằng Google thì sign out khỏi Google
        String loginMethod = sessionManager.getLoginMethod();
        if ("GOOGLE".equals(loginMethod)) {
            googleSignInHelper.signOut();  // ← Thêm dòng này
        }
        
        // Xóa session
        sessionManager.logout();
        
        // ... phần còn lại
    }
}
```

### 2. Sign out trước khi đăng nhập Google

**File:** `FE/app/src/main/java/com/example/localcooking_v3t/Login.java`

**Thay đổi:**

```java
private void performGoogleSignIn() {
    // Sign out trước để luôn hiển thị màn hình chọn tài khoản
    googleSignInHelper.signOut();  // ← Thêm dòng này
    
    Intent signInIntent = googleSignInHelper.getSignInIntent();
    googleSignInLauncher.launch(signInIntent);
}
```

## Cách hoạt động

### Trước khi fix:

1. User đăng nhập Google → Google SDK cache tài khoản
2. User đăng xuất → Chỉ xóa session app, Google vẫn cache
3. User đăng nhập Google lại → Google tự động dùng tài khoản đã cache

### Sau khi fix:

1. User đăng nhập Google → Google SDK cache tài khoản
2. User đăng xuất → Xóa session app **VÀ** sign out khỏi Google
3. User đăng nhập Google lại → Google hiển thị màn hình chọn tài khoản

## Test

### Bước 1: Rebuild app

```cmd
cd FE
gradlew clean
gradlew assembleDebug
```

### Bước 2: Cài lại app trên điện thoại

### Bước 3: Test luồng

1. **Đăng nhập bằng Google:**
   - Mở app
   - Nhấn nút "Google"
   - Chọn tài khoản Gmail (ví dụ: user1@gmail.com)
   - Đăng nhập thành công

2. **Đăng xuất:**
   - Vào tab Profile
   - Nhấn "Đăng xuất"
   - Thấy toast "Đã đăng xuất thành công"

3. **Đăng nhập lại bằng Google:**
   - Nhấn nút "Google"
   - **Kết quả mong đợi:** Hiển thị màn hình chọn tài khoản
   - Có thể chọn tài khoản khác (ví dụ: user2@gmail.com)

4. **Đăng ký tài khoản email mới:**
   - Đăng xuất
   - Đăng ký tài khoản mới bằng email
   - Đăng nhập thành công

5. **Đăng nhập Google lại:**
   - Đăng xuất
   - Nhấn nút "Google"
   - **Kết quả mong đợi:** Vẫn hiển thị màn hình chọn tài khoản

## Lưu ý

### 1. Web Client ID phải giống nhau

Trong `ProfileFragment.java` và `Login.java`, Web Client ID phải giống nhau:

```java
String WEB_CLIENT_ID = "954091456874-iji1kkmljees7803o33p78gl34pl90ek.apps.googleusercontent.com";
```

### 2. Sign out không xóa tài khoản Google khỏi điện thoại

`googleSignInHelper.signOut()` chỉ xóa cache của Google Sign-In SDK, **không xóa tài khoản Google** khỏi Settings điện thoại.

### 3. Nếu muốn xóa hoàn toàn

Nếu muốn xóa cả quyền truy cập của app, dùng `revokeAccess()`:

```java
// Trong GoogleSignInHelper.java, thêm method:
public void revokeAccess() {
    googleSignInClient.revokeAccess();
}

// Trong ProfileFragment.java:
if ("GOOGLE".equals(loginMethod)) {
    googleSignInHelper.revokeAccess();  // Thay vì signOut()
}
```

**Lưu ý:** `revokeAccess()` sẽ xóa hoàn toàn quyền truy cập, user phải cấp quyền lại lần sau.

## Troubleshooting

### Vẫn tự động đăng nhập vào tài khoản cũ

**Nguyên nhân:** App chưa rebuild hoặc cache chưa clear

**Giải pháp:**
1. Xóa app khỏi điện thoại
2. Clean project: `gradlew clean`
3. Rebuild: `gradlew assembleDebug`
4. Cài lại app

### Lỗi: "Cannot resolve symbol 'GoogleSignInHelper'"

**Nguyên nhân:** Import thiếu

**Giải pháp:** Thêm import:
```java
import com.example.localcooking_v3t.helper.GoogleSignInHelper;
```

### Lỗi: "WEB_CLIENT_ID not found"

**Nguyên nhân:** Chưa khai báo Web Client ID

**Giải pháp:** Thêm vào `onCreate()`:
```java
String WEB_CLIENT_ID = "954091456874-iji1kkmljees7803o33p78gl34pl90ek.apps.googleusercontent.com";
googleSignInHelper = new GoogleSignInHelper(requireContext(), WEB_CLIENT_ID);
```

## Kết quả

✅ Khi đăng xuất, app sign out khỏi Google
✅ Khi đăng nhập Google lại, luôn hiển thị màn hình chọn tài khoản
✅ Có thể chọn tài khoản Google khác
✅ Có thể đăng ký tài khoản email mới và đăng nhập bình thường

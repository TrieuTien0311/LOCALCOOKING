# 🚀 Hướng Dẫn Hoàn Chỉnh Google Sign-In

## ✅ Đã Hoàn Thành

### Backend
- ✅ API `/api/nguoidung/google-login` trong `NguoiDungController.java`
- ✅ `GoogleAuthService.java` - Verify Google ID Token
- ✅ `NguoiDungService.loginWithGoogle()` - Xử lý đăng nhập/đăng ký
- ✅ `GoogleLoginRequest.java` và `GoogleLoginResponse.java`
- ✅ Database schema đã có trong `SQL_UPDATE_GOOGLE_LOGIN.sql`
- ✅ `application.properties` đã có config `google.client.id`

### Android
- ✅ `GoogleSignInHelper.java` - Helper xử lý Google Sign-In
- ✅ `GoogleLoginRequest.java` và `GoogleLoginResponse.java`
- ✅ `ApiService.googleLogin()` endpoint
- ✅ `Login.java` đã tích hợp Google Sign-In hoàn chỉnh
- ✅ `SessionManager` đã lưu avatarUrl và loginMethod
- ✅ `build.gradle.kts` đã có dependencies

---

## 🔧 Các Bước Cần Làm

### Bước 1: Chạy SQL Script (Backend)

Mở **SQL Server Management Studio** và chạy file:
```
BE/SQL_UPDATE_GOOGLE_LOGIN.sql
```

Script này sẽ:
- Thêm cột `googleId`, `loginMethod`, `avatarUrl` vào bảng `NguoiDung`
- Cho phép `matKhau` và `tenDangNhap` NULL (cho user đăng nhập Google)
- Tạo unique index cho `googleId`
- Cập nhật `loginMethod = 'EMAIL'` cho user hiện tại

---

### Bước 2: Tạo Google Cloud Project

#### 2.1. Truy cập Google Cloud Console
1. Vào: https://console.cloud.google.com/
2. Đăng nhập bằng tài khoản Google của bạn
3. Click **Select a project** > **New Project**
4. Tên project: `LocalCooking`
5. Click **Create**

#### 2.2. Enable Google Sign-In API
1. Vào **APIs & Services** > **Library**
2. Tìm kiếm: `Google Sign-In API`
3. Click vào kết quả
4. Click **Enable**

---

### Bước 3: Cấu Hình OAuth Consent Screen

1. Vào **APIs & Services** > **OAuth consent screen**
2. Chọn **External** (cho testing)
3. Click **Create**
4. Điền thông tin:
   - **App name:** `LocalCooking`
   - **User support email:** Email của bạn
   - **Developer contact information:** Email của bạn
5. Click **Save and Continue**
6. **Scopes:** Giữ mặc định (không cần thêm gì)
7. Click **Save and Continue**
8. **Test users:** Click **Add Users** và thêm email test của bạn
9. Click **Save and Continue**
10. Review và click **Back to Dashboard**

---

### Bước 4: Lấy SHA-1 Fingerprint

Mở **Command Prompt** trong thư mục `FE`:

#### Cho Debug (test trên máy ảo/điện thoại):
```cmd
cd FE
gradlew signingReport
```

Hoặc dùng keytool:
```cmd
keytool -list -v -keystore %USERPROFILE%\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
```

**Copy SHA-1 fingerprint** (dạng: `AA:BB:CC:DD:EE:...`)

Ví dụ:
```
SHA1: 3B:4C:8F:2A:1D:9E:7F:6A:5B:3C:4D:8E:9F:0A:1B:2C:3D:4E:5F:6A
```

---

### Bước 5: Tạo OAuth Client IDs

#### 5.1. Tạo Android Client ID

1. Vào **APIs & Services** > **Credentials**
2. Click **Create Credentials** > **OAuth client ID**
3. Application type: **Android**
4. Điền:
   - **Name:** `LocalCooking Android`
   - **Package name:** `com.example.localcooking_v3t`
   - **SHA-1 certificate fingerprint:** Paste SHA-1 vừa copy
5. Click **Create**
6. Click **OK** (không cần copy gì)

#### 5.2. Tạo Web Client ID (QUAN TRỌNG!)

1. Click **Create Credentials** > **OAuth client ID** lần nữa
2. Application type: **Web application**
3. **Name:** `LocalCooking Web Client`
4. Click **Create**
5. **COPY Web Client ID** (dạng: `123456789-abcdefg.apps.googleusercontent.com`)
   - Lưu vào notepad để dùng ở bước sau

---

### Bước 6: Cập Nhật Backend

Mở file `BE/src/main/resources/application.properties`:

Tìm dòng:
```properties
# Google OAuth Configuration
google.client.id=YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com
```

Thay bằng:
```properties
# Google OAuth Configuration
google.client.id=123456789-abcdefg.apps.googleusercontent.com
```

**Lưu ý:** Dùng **Web Client ID** vừa copy ở bước 5.2

---

### Bước 7: Cập Nhật Android

Mở file `FE/app/src/main/java/com/example/localcooking_v3t/Login.java`:

Tìm dòng:
```java
private static final String WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com";
```

Thay bằng:
```java
private static final String WEB_CLIENT_ID = "123456789-abcdefg.apps.googleusercontent.com";
```

**Lưu ý:** Dùng **Web Client ID** giống như backend!

---

### Bước 8: Kiểm Tra Firewall (Windows)

1. Mở **Windows Defender Firewall**
2. Click **Allow an app or feature through Windows Defender Firewall**
3. Click **Change settings**
4. Tìm **Java(TM) Platform SE binary** hoặc **OpenJDK Platform binary**
5. Tick cả **Private** và **Public**
6. Click **OK**

Hoặc tạm thời tắt firewall để test:
```
Control Panel > Windows Defender Firewall > Turn Windows Defender Firewall on or off
```

---

### Bước 9: Restart Backend

Mở terminal trong thư mục `BE`:
```cmd
cd BE
gradlew bootRun
```

Hoặc restart trong IDE (IntelliJ/Eclipse)

Kiểm tra log có dòng:
```
Started BeApplication in X.XXX seconds
```

---

### Bước 10: Rebuild Android App

1. **Clean Project:**
   - Android Studio > Build > Clean Project
   
2. **Rebuild:**
   - Build > Rebuild Project
   
3. **Cài lại app trên điện thoại:**
   - Run > Run 'app'
   - Hoặc build APK và cài thủ công

---

### Bước 11: Test Trên Điện Thoại

1. Mở app trên điện thoại
2. Nhấn nút **Google** (btnGG)
3. Chọn tài khoản Gmail có sẵn trên máy
4. Nhấn **Continue** hoặc **Đồng ý**
5. Kiểm tra:
   - ✅ App chuyển sang màn hình Home
   - ✅ Toast hiển thị "Chào mừng trở lại, [Tên]"
   - ✅ Session được lưu

---

## 🐛 Troubleshooting

### Lỗi: "Developer Error" hoặc "Sign in failed"

**Nguyên nhân:** SHA-1 không khớp hoặc package name sai

**Giải pháp:**
1. Kiểm tra SHA-1 có đúng không (chạy lại `gradlew signingReport`)
2. Kiểm tra package name: `com.example.localcooking_v3t`
3. Đợi 5-10 phút sau khi tạo OAuth Client (Google cần thời gian sync)
4. Xóa app và cài lại

### Lỗi: "API not enabled"

**Giải pháp:** 
- Vào Google Cloud Console
- Enable **Google Sign-In API**

### Lỗi: "Invalid token" (Backend log)

**Nguyên nhân:** Web Client ID trong backend không đúng

**Giải pháp:**
1. Kiểm tra `application.properties` có đúng Web Client ID không
2. Đảm bảo dùng **Web Client ID**, không phải Android Client ID
3. Restart backend sau khi đổi

### Lỗi: "Failed to connect to /192.168.137.1:8080"

**Nguyên nhân:** Backend không chạy hoặc IP sai

**Giải pháp:**
1. Kiểm tra backend đang chạy:
   ```cmd
   netstat -ano | findstr :8080
   ```
   Phải thấy dòng có `0.0.0.0:8080`

2. Kiểm tra IP trong `RetrofitClient.java`:
   ```java
   private static final String IP_MAY_TINH = "192.168.137.1";
   ```

3. Test từ trình duyệt điện thoại:
   ```
   http://192.168.137.1:8080/api/nguoidung
   ```

### Lỗi: Không lấy được ID Token

**Nguyên nhân:** Web Client ID trong Android không đúng

**Giải pháp:**
1. Kiểm tra `WEB_CLIENT_ID` trong `Login.java`
2. Phải dùng **Web Client ID**, không phải Android Client ID
3. Rebuild app

### Lỗi: "Cleartext HTTP traffic not permitted"

**Giải pháp:** Thêm vào `AndroidManifest.xml`:
```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

---

## 📱 Yêu Cầu Test Trên Điện Thoại Thật

- ✅ Điện thoại đã đăng nhập tài khoản Google
- ✅ Kết nối Mobile Hotspot từ máy tính
- ✅ Backend đang chạy (port 8080)
- ✅ Firewall đã cho phép Java
- ✅ IP trong `RetrofitClient.java` đúng với Mobile Hotspot

---

## 📝 Checklist Hoàn Chỉnh

- [ ] SQL script đã chạy (`SQL_UPDATE_GOOGLE_LOGIN.sql`)
- [ ] Google Cloud Project đã tạo
- [ ] Google Sign-In API đã enable
- [ ] OAuth Consent Screen đã config
- [ ] SHA-1 đã lấy và đăng ký
- [ ] Android Client ID đã tạo
- [ ] Web Client ID đã tạo và copy
- [ ] Backend `application.properties` đã cập nhật Web Client ID
- [ ] Android `Login.java` đã cập nhật Web Client ID
- [ ] Firewall đã cho phép Java
- [ ] Backend đang chạy
- [ ] App đã rebuild và cài lại
- [ ] Test thành công trên điện thoại

---

## 🎉 Kết Quả Mong Đợi

Khi đăng nhập thành công:

1. **Toast hiển thị:**
   - User mới: "Chào mừng bạn đến với LocalCooking!"
   - User cũ: "Chào mừng trở lại, [Tên]"

2. **Chuyển sang màn hình Home**

3. **Session lưu thông tin:**
   - `maNguoiDung`
   - `hoTen`
   - `email`
   - `vaiTro` = "HocVien"
   - `avatarUrl` (link ảnh Google)
   - `loginMethod` = "GOOGLE"

4. **Backend log:**
   ```
   POST /api/nguoidung/google-login
   Đăng nhập thành công: user@gmail.com
   ```

---

## 💡 Lưu Ý Quan Trọng

### 1. Web Client ID vs Android Client ID

- **Backend:** Dùng **Web Client ID**
- **Android:** Dùng **Web Client ID**
- **Android Client ID:** Chỉ để Google biết app của bạn (không dùng trong code)

### 2. SHA-1 Fingerprint

- **Debug SHA-1:** Cho test trên máy ảo/điện thoại
- **Release SHA-1:** Cho production (khi publish app)
- Có thể thêm nhiều SHA-1 cho cùng 1 OAuth Client

### 3. Test Users

- Trong **development mode**, chỉ test users mới đăng nhập được
- Publish app lên **production** để mọi người dùng được

### 4. Security

- **Không commit** Web Client ID lên Git public
- Dùng `BuildConfig` hoặc `local.properties` cho production
- Luôn verify token ở backend, không tin tưởng client

---

## 📚 Tài Liệu Tham Khảo

- `GOOGLE_SIGN_IN_SETUP.md` - Hướng dẫn chi tiết setup
- `API_GOOGLE_LOGIN.md` - Tài liệu API endpoint
- `FIX_MOBILE_HOTSPOT_CONNECTION.md` - Fix lỗi kết nối
- `SQL_UPDATE_GOOGLE_LOGIN.sql` - Script cập nhật database

---

## 🆘 Cần Hỗ Trợ?

Nếu gặp vấn đề:
1. Kiểm tra lại từng bước trong checklist
2. Xem phần Troubleshooting
3. Kiểm tra log trong Logcat (Android) và terminal (Backend)
4. Đảm bảo Web Client ID giống nhau ở cả Backend và Android

**Good luck! 🚀**

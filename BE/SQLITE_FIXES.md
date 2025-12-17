# ✅ SQLite Compatibility Fixes

## Vấn đề
SQLite không có kiểu dữ liệu DATE và TIME riêng biệt như SQL Server. Nó lưu tất cả dưới dạng TEXT.

## Giải pháp đã áp dụng

### 1. Đổi LocalDate → String
```java
// TRƯỚC
private LocalDate ngayDienRa;

// SAU  
private String ngayDienRa; // Format: YYYY-MM-DD
```

### 2. Đổi java.sql.Time → String
```java
// TRƯỚC
private java.sql.Time gioBatDau;

// SAU
private String gioBatDau; // Format: HH:MM hoặc HH:MM:SS
```

### 3. LocalDateTime vẫn OK
```java
// Giữ nguyên - SQLite hỗ trợ DATETIME
private LocalDateTime ngayTao;
```

## Format dữ liệu trong SQLite

| Java Type | SQLite Type | Format Example |
|-----------|-------------|----------------|
| String (date) | TEXT | `2025-02-10` |
| String (time) | TEXT | `14:00` hoặc `14:00:00` |
| LocalDateTime | TEXT | `2025-01-20 10:30:00` |

## Files đã sửa

1. ✅ `LopHoc.java` - ngayDienRa, gioBatDau, gioKetThuc
2. ✅ `LichHoc.java` - ngayHoc, gioBatDau, gioKetThuc
3. ✅ `UuDai.java` - ngayBatDau, ngayKetThuc
4. ✅ `LopHocResponse.java` - DTO tương ứng

## Test lại

```bash
# Restart server
./gradlew bootRun

# Test endpoint
curl http://localhost:8080/api/health
curl http://localhost:8080/api/lophoc
```

## Lưu ý khi insert data

```sql
-- Đúng format cho SQLite
INSERT INTO LopHoc (ngayDienRa, gioBatDau, gioKetThuc)
VALUES ('2025-02-10', '14:00', '17:00');

-- KHÔNG dùng DATE() hoặc TIME() functions
```

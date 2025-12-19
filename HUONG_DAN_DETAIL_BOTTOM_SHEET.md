# Hướng dẫn hoàn thiện Detail Bottom Sheet

## ✅ Đã hoàn thành:
1. **ClassAdapter** - Thêm click listener cho footer và khunggiua
2. **ClassesFragment** - Mở DetailBottomSheet khi click

## 📋 Còn lại cần làm:

### 1. Sửa DetailDescriptionFragment
- Nhận data KhoaHoc từ DetailBottomSheet
- Hiển thị thông tin giáo viên, giới thiệu, giá trị sau buổi học
- Hiển thị RecyclerView danh mục món ăn với CategoryAdapter

### 2. Tạo/Sửa CategoryAdapter
- Hiển thị danh mục món ăn (Món khai vị, Món chính, Món tráng miệng)
- Click để expand/collapse danh sách món ăn
- Chứa RecyclerView món ăn bên trong

### 3. Tạo/Sửa FoodAdapter  
- Hiển thị món ăn trong từng danh mục
- Swipe left/right để xem các món ăn
- Hiển thị hình ảnh, giới thiệu, nguyên liệu

### 4. Model cần có:
- `DanhMucMonAn` (tên danh mục, icon, danh sách món ăn)
- `MonAn` (tên món, giới thiệu, nguyên liệu, hình ảnh)

## 🎯 Kết quả mong đợi:
- Click vào item_class → Mở bottom sheet
- Tab "Mô tả" hiển thị đầy đủ thông tin lớp học và món ăn
- Tab "Chính sách", "Đánh giá", "Ưu đãi" giữ nguyên (không chỉnh)

## 📝 Ghi chú:
- Backend đã trả về `danhMucMonAnList` trong `KhoaHocDTO`
- Cần map từ backend model sang Android model
- Sử dụng nested RecyclerView (RecyclerView trong RecyclerView)

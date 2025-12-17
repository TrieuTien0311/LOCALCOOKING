PRAGMA foreign_keys = ON;
BEGIN TRANSACTION;

-- =========================
-- NGƯỜI DÙNG (USERS)
-- =========================
CREATE TABLE NguoiDung (
  maNguoiDung INTEGER PRIMARY KEY AUTOINCREMENT,
  tenDangNhap TEXT NOT NULL UNIQUE,
  matKhau TEXT NOT NULL,
  hoTen TEXT,
  email TEXT NOT NULL UNIQUE,
  soDienThoai TEXT,
  diaChi TEXT,
  vaiTro TEXT NOT NULL CHECK (vaiTro IN ('HocVien', 'GiaoVien', 'Admin')) DEFAULT 'HocVien',
  trangThai TEXT NOT NULL CHECK (trangThai IN ('HoatDong', 'BiKhoa')) DEFAULT 'HoatDong',
  ngayTao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lanCapNhatCuoi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- OTP
-- =========================
CREATE TABLE OTP (
  maOTP INTEGER PRIMARY KEY AUTOINCREMENT,
  maNguoiDung INTEGER,
  maXacThuc TEXT NOT NULL,
  loaiOTP TEXT NOT NULL CHECK (loaiOTP IN ('DangKy', 'QuenMatKhau', 'XacThuc')),
  thoiGianTao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  thoiGianHetHan DATETIME,
  daSuDung INTEGER NOT NULL DEFAULT 0,
  FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung) ON DELETE CASCADE
);

-- =========================
-- GIÁO VIÊN
-- =========================
CREATE TABLE GiaoVien (
  maGiaoVien INTEGER PRIMARY KEY AUTOINCREMENT,
  maNguoiDung INTEGER NOT NULL UNIQUE,
  chuyenMon TEXT,
  kinhNghiem TEXT,
  moTa TEXT,
  hinhAnh TEXT,
  FOREIGN KEY (maNguoiDung) REFERENCES NguoiDung(maNguoiDung) ON DELETE CASCADE
);

-- =========================
-- LỚP HỌC (CLASSES)
-- =========================
CREATE TABLE LopHoc (
  maLopHoc INTEGER PRIMARY KEY AUTOINCREMENT,
  tenLopHoc TEXT NOT NULL,
  moTa TEXT,
  maGiaoVien INTEGER,
  tenGiaoVien TEXT,
  soLuongToiDa INTEGER NOT NULL DEFAULT 20,
  soLuongHienTai INTEGER NOT NULL DEFAULT 0,
  giaTien REAL NOT NULL DEFAULT 0,
  thoiGian TEXT,
  diaDiem TEXT,
  trangThai TEXT NOT NULL CHECK (trangThai IN ('Sắp diễn ra', 'Đang diễn ra', 'Đã kết thúc', 'Đã hủy')) DEFAULT 'Sắp diễn ra',
  ngayDienRa DATE,
  gioBatDau TIME,
  gioKetThuc TIME,
  hinhAnh TEXT,
  coUuDai INTEGER NOT NULL DEFAULT 0,
  ngayTao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (maGiaoVien) REFERENCES GiaoVien(maGiaoVien) ON DELETE SET NULL
);

-- =========================
-- DANH MỤC MÓN ĂN (Category)
-- =========================
CREATE TABLE DanhMucMonAn (
  maDanhMuc INTEGER PRIMARY KEY AUTOINCREMENT,
  maLopHoc INTEGER NOT NULL,
  tenDanhMuc TEXT NOT NULL,
  thoiGian TEXT,
  iconDanhMuc TEXT,
  thuTu INTEGER DEFAULT 1,
  FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) ON DELETE CASCADE
);

-- =========================
-- MÓN ĂN (Food)
-- =========================
CREATE TABLE MonAn (
  maMonAn INTEGER PRIMARY KEY AUTOINCREMENT,
  maDanhMuc INTEGER NOT NULL,
  tenMon TEXT NOT NULL,
  gioiThieu TEXT,
  nguyenLieu TEXT,
  hinhAnh TEXT,
  FOREIGN KEY (maDanhMuc) REFERENCES DanhMucMonAn(maDanhMuc) ON DELETE CASCADE
);

-- =========================
-- HÌNH ẢNH LỚP HỌC
-- =========================
CREATE TABLE HinhAnhLopHoc (
  maHinhAnh INTEGER PRIMARY KEY AUTOINCREMENT,
  maLopHoc INTEGER NOT NULL,
  duongDan TEXT NOT NULL,
  thuTu INTEGER DEFAULT 1,
  FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) ON DELETE CASCADE
);

-- =========================
-- LỊCH HỌC (CLASS SESSIONS)
-- =========================
CREATE TABLE LichHoc (
  maLichHoc INTEGER PRIMARY KEY AUTOINCREMENT,
  maLopHoc INTEGER NOT NULL,
  ngayHoc DATE NOT NULL,
  gioBatDau TIME NOT NULL,
  gioKetThuc TIME NOT NULL,
  noiDung TEXT,
  trangThai TEXT NOT NULL CHECK (trangThai IN ('Chưa Học', 'Đã Học', 'Đã Hủy')) DEFAULT 'Chưa Học',
  FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) ON DELETE CASCADE
);

-- =========================
-- ĐẶT LỊCH (BOOKINGS)
-- =========================
CREATE TABLE DatLich (
  maDatLich INTEGER PRIMARY KEY AUTOINCREMENT,
  maHocVien INTEGER NOT NULL,
  maLopHoc INTEGER NOT NULL,
  soLuongNguoi INTEGER DEFAULT 1,
  tongTien REAL,
  tenNguoiDat TEXT,
  emailNguoiDat TEXT,
  sdtNguoiDat TEXT,
  ngayDat DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  trangThai TEXT NOT NULL CHECK (trangThai IN ('Chờ Duyệt', 'Đã Duyệt', 'Đã Hủy', 'Đã Từ Chối')) DEFAULT 'Chờ Duyệt',
  ghiChu TEXT,
  FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung) ON DELETE CASCADE,
  FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) ON DELETE CASCADE
);

-- =========================
-- THANH TOÁN (PAYMENTS)
-- =========================
CREATE TABLE ThanhToan (
  maThanhToan INTEGER PRIMARY KEY AUTOINCREMENT,
  maDatLich INTEGER NOT NULL,
  soTien REAL NOT NULL,
  phuongThuc TEXT NOT NULL CHECK (phuongThuc IN ('ChuyenKhoan', 'MoMo', 'The')),
  trangThai TEXT NOT NULL CHECK (trangThai IN ('Chưa Thanh Toán', 'Đã Thanh Toán', 'Hoàn Tiền')) DEFAULT 'Chưa Thanh Toán',
  ngayThanhToan DATETIME,
  maGiaoDich TEXT,
  ghiChu TEXT,
  FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich) ON DELETE CASCADE
);

-- =========================
-- ĐÁNH GIÁ (REVIEWS)
-- =========================
CREATE TABLE DanhGia (
  maDanhGia INTEGER PRIMARY KEY AUTOINCREMENT,
  maHocVien INTEGER NOT NULL,
  maLopHoc INTEGER NOT NULL,
  diemDanhGia INTEGER NOT NULL CHECK (diemDanhGia BETWEEN 1 AND 5),
  binhLuan TEXT,
  ngayDanhGia DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung) ON DELETE CASCADE,
  FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) ON DELETE CASCADE
);

-- =========================
-- THÔNG BÁO (NOTIFICATIONS)
-- =========================
CREATE TABLE ThongBao (
  maThongBao INTEGER PRIMARY KEY AUTOINCREMENT,
  maNguoiNhan INTEGER,
  tieuDe TEXT NOT NULL,
  noiDung TEXT NOT NULL,
  loaiThongBao TEXT NOT NULL DEFAULT 'Hệ Thống',
  hinhAnh TEXT,
  daDoc INTEGER NOT NULL DEFAULT 0,
  ngayTao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (maNguoiNhan) REFERENCES NguoiDung(maNguoiDung) ON DELETE CASCADE
);

-- =========================
-- YÊU THÍCH (FAVORITES)
-- =========================
CREATE TABLE YeuThich (
  maYeuThich INTEGER PRIMARY KEY AUTOINCREMENT,
  maHocVien INTEGER NOT NULL,
  maLopHoc INTEGER NOT NULL,
  ngayThem DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (maHocVien, maLopHoc),
  FOREIGN KEY (maHocVien) REFERENCES NguoiDung(maNguoiDung) ON DELETE CASCADE,
  FOREIGN KEY (maLopHoc) REFERENCES LopHoc(maLopHoc) ON DELETE CASCADE
);

-- =========================
-- ƯU ĐÃI (PROMOTIONS)
-- =========================
CREATE TABLE UuDai (
  maUuDai INTEGER PRIMARY KEY AUTOINCREMENT,
  maCode TEXT NOT NULL UNIQUE,
  tenUuDai TEXT NOT NULL,
  moTa TEXT,
  loaiGiam TEXT NOT NULL CHECK (loaiGiam IN ('PhanTram', 'SoTien')),
  giaTriGiam REAL NOT NULL,
  giamToiDa REAL,
  soLuong INTEGER,
  soLuongDaSuDung INTEGER NOT NULL DEFAULT 0,
  ngayBatDau DATE NOT NULL,
  ngayKetThuc DATE NOT NULL,
  hinhAnh TEXT,
  trangThai TEXT NOT NULL CHECK (trangThai IN ('Hoạt Động', 'Không Hoạt Động')) DEFAULT 'Hoạt Động',
  ngayTao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- LỊCH SỬ ƯU ĐÃI
-- =========================
CREATE TABLE LichSuUuDai (
  maLichSu INTEGER PRIMARY KEY AUTOINCREMENT,
  maUuDai INTEGER NOT NULL,
  maDatLich INTEGER NOT NULL,
  soTienGiam REAL NOT NULL DEFAULT 0,
  ngaySuDung DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (maUuDai) REFERENCES UuDai(maUuDai),
  FOREIGN KEY (maDatLich) REFERENCES DatLich(maDatLich) ON DELETE CASCADE
);

-- =========================
-- HÓA ĐƠN (INVOICES)
-- =========================
CREATE TABLE HoaDon (
  maHoaDon INTEGER PRIMARY KEY AUTOINCREMENT,
  maThanhToan INTEGER NOT NULL UNIQUE,
  soHoaDon TEXT NOT NULL UNIQUE,
  ngayXuatHoaDon DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  tongTien REAL NOT NULL,
  VAT REAL NOT NULL DEFAULT 0,
  thanhTien REAL NOT NULL,
  trangThai TEXT NOT NULL CHECK (trangThai IN ('Đã Xuất', 'Đã Hủy')) DEFAULT 'Đã Xuất',
  filePDF TEXT,
  FOREIGN KEY (maThanhToan) REFERENCES ThanhToan(maThanhToan) ON DELETE CASCADE
);

-- =========================
-- TRIGGERS
-- =========================

-- Auto cập nhật lanCapNhatCuoi
CREATE TRIGGER trg_NguoiDung_CapNhat
AFTER UPDATE ON NguoiDung
FOR EACH ROW
BEGIN
  UPDATE NguoiDung SET lanCapNhatCuoi = CURRENT_TIMESTAMP WHERE maNguoiDung = NEW.maNguoiDung;
END;

-- Kiểm tra lớp đầy trước khi duyệt
CREATE TRIGGER trg_DatLich_KiemTraDayLop
BEFORE UPDATE OF trangThai ON DatLich
FOR EACH ROW
WHEN NEW.trangThai = 'Đã Duyệt'
BEGIN
  SELECT CASE
    WHEN (SELECT soLuongHienTai FROM LopHoc WHERE maLopHoc = NEW.maLopHoc) >=
         (SELECT soLuongToiDa FROM LopHoc WHERE maLopHoc = NEW.maLopHoc)
    THEN RAISE(ABORT, 'Lớp học đã đầy')
  END;
END;

-- Tăng số lượng học viên khi đặt lịch được duyệt (INSERT)
CREATE TRIGGER trg_DatLich_TangSoLuong_Insert
AFTER INSERT ON DatLich
FOR EACH ROW
WHEN NEW.trangThai = 'Đã Duyệt'
BEGIN
  UPDATE LopHoc 
  SET soLuongHienTai = soLuongHienTai + COALESCE(NEW.soLuongNguoi, 1)
  WHERE maLopHoc = NEW.maLopHoc;
END;

-- Cập nhật số lượng học viên khi thay đổi trạng thái (UPDATE)
CREATE TRIGGER trg_DatLich_CapNhatSoLuong_Update
AFTER UPDATE OF trangThai ON DatLich
FOR EACH ROW
BEGIN
  -- Giảm khi từ Đã Duyệt -> Đã Hủy/Đã Từ Chối
  UPDATE LopHoc
  SET soLuongHienTai = soLuongHienTai - COALESCE(OLD.soLuongNguoi, 1)
  WHERE maLopHoc = NEW.maLopHoc
    AND OLD.trangThai = 'Đã Duyệt'
    AND NEW.trangThai IN ('Đã Hủy', 'Đã Từ Chối');

  -- Tăng khi từ khác -> Đã Duyệt
  UPDATE LopHoc
  SET soLuongHienTai = soLuongHienTai + COALESCE(NEW.soLuongNguoi, 1)
  WHERE maLopHoc = NEW.maLopHoc
    AND OLD.trangThai <> 'Đã Duyệt'
    AND NEW.trangThai = 'Đã Duyệt';
END;

-- Tạo thông báo khi đặt lịch
CREATE TRIGGER trg_DatLich_TaoThongBao
AFTER INSERT ON DatLich
FOR EACH ROW
BEGIN
  INSERT INTO ThongBao (maNguoiNhan, tieuDe, noiDung, loaiThongBao)
  SELECT 
    NEW.maHocVien,
    'Đặt lịch thành công',
    'Bạn đã đặt lịch học lớp ' || (SELECT tenLopHoc FROM LopHoc WHERE maLopHoc = NEW.maLopHoc),
    'LopHoc';
END;

-- Cập nhật số lượng ưu đãi đã sử dụng
CREATE TRIGGER trg_LichSuUuDai_CapNhatSoLuong
AFTER INSERT ON LichSuUuDai
FOR EACH ROW
BEGIN
  UPDATE UuDai
  SET soLuongDaSuDung = soLuongDaSuDung + 1
  WHERE maUuDai = NEW.maUuDai;
END;

-- OTP tự động set thời gian hết hạn +5 phút
CREATE TRIGGER trg_OTP_TuDongHetHan
AFTER INSERT ON OTP
FOR EACH ROW
WHEN NEW.thoiGianHetHan IS NULL
BEGIN
  UPDATE OTP
  SET thoiGianHetHan = datetime(NEW.thoiGianTao, '+5 minutes')
  WHERE maOTP = NEW.maOTP;
END;

COMMIT;

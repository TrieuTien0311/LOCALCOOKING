package com.example.localcooking_v3t;

public class Notice {
    private Integer maThongBao; // ID từ server
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private int anhTB; // Resource ID của ảnh (cho dữ liệu local)
    private String anhTBUrl; // URL ảnh từ server
    private boolean trangThai; // true = đã đọc, false = chưa đọc
    private String loaiThongBao;

    // Constructor cho dữ liệu local (giữ tương thích cũ)
    public Notice(String tieuDeTB, String noiDungTB, String thoiGianTB, int anhTB, boolean trangThai) {
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTB = anhTB;
        this.trangThai = trangThai;
    }

    // Constructor cho dữ liệu từ API
    public Notice(Integer maThongBao, String tieuDeTB, String noiDungTB, String thoiGianTB,
                  String anhTBUrl, boolean trangThai, String loaiThongBao) {
        this.maThongBao = maThongBao;
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTBUrl = anhTBUrl;
        this.trangThai = trangThai;
        this.loaiThongBao = loaiThongBao;
        this.anhTB = 0; // Không dùng resource ID
    }

    // Getters
    public Integer getMaThongBao() { return maThongBao; }
    public String getTieuDeTB() { return tieuDeTB; }
    public String getNoiDungTB() { return noiDungTB; }
    public String getThoiGianTB() { return thoiGianTB; }
    public int getAnhTB() { return anhTB; }
    public String getAnhTBUrl() { return anhTBUrl; }
    public boolean isTrangThai() { return trangThai; }
    public String getLoaiThongBao() { return loaiThongBao; }

    // Setters
    public void setMaThongBao(Integer maThongBao) { this.maThongBao = maThongBao; }
    public void setTieuDeTB(String tieuDeTB) { this.tieuDeTB = tieuDeTB; }
    public void setNoiDungTB(String noiDungTB) { this.noiDungTB = noiDungTB; }
    public void setThoiGianTB(String thoiGianTB) { this.thoiGianTB = thoiGianTB; }
    public void setAnhTB(int anhTB) { this.anhTB = anhTB; }
    public void setAnhTBUrl(String anhTBUrl) { this.anhTBUrl = anhTBUrl; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
}

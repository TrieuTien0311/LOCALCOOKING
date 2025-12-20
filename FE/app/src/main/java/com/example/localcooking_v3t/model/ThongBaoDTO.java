package com.example.localcooking_v3t.model;

public class ThongBaoDTO {
    private Integer maThongBao;
    private String tieuDeTB;
    private String noiDungTB;
    private String thoiGianTB;
    private String anhTB;
    private Boolean trangThai; // true = đã đọc, false = chưa đọc
    private String loaiThongBao;

    // Constructor mặc định
    public ThongBaoDTO() {}

    // Constructor đầy đủ
    public ThongBaoDTO(Integer maThongBao, String tieuDeTB, String noiDungTB,
                       String thoiGianTB, String anhTB, Boolean trangThai, String loaiThongBao) {
        this.maThongBao = maThongBao;
        this.tieuDeTB = tieuDeTB;
        this.noiDungTB = noiDungTB;
        this.thoiGianTB = thoiGianTB;
        this.anhTB = anhTB;
        this.trangThai = trangThai;
        this.loaiThongBao = loaiThongBao;
    }

    // Getters
    public Integer getMaThongBao() { return maThongBao; }
    public String getTieuDeTB() { return tieuDeTB; }
    public String getNoiDungTB() { return noiDungTB; }
    public String getThoiGianTB() { return thoiGianTB; }
    public String getAnhTB() { return anhTB; }
    public Boolean getTrangThai() { return trangThai; }
    public String getLoaiThongBao() { return loaiThongBao; }

    // Setters
    public void setMaThongBao(Integer maThongBao) { this.maThongBao = maThongBao; }
    public void setTieuDeTB(String tieuDeTB) { this.tieuDeTB = tieuDeTB; }
    public void setNoiDungTB(String noiDungTB) { this.noiDungTB = noiDungTB; }
    public void setThoiGianTB(String thoiGianTB) { this.thoiGianTB = thoiGianTB; }
    public void setAnhTB(String anhTB) { this.anhTB = anhTB; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
}

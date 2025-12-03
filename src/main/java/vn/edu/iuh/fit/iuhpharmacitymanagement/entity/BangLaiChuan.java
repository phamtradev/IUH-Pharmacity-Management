package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;

public class BangLaiChuan {

    private int id;
    private double giaNhapTu;
    private double giaNhapDen; // có thể = 0 hoặc < 0 để hiểu là không giới hạn trên
    private double tyLeLai;    // dạng thập phân: 0.2 = 20%
    private LoaiSanPham loaiSanPham; // null = áp dụng cho tất cả

    public BangLaiChuan() {
    }

    public BangLaiChuan(int id, double giaNhapTu, double giaNhapDen, double tyLeLai) {
        this(id, giaNhapTu, giaNhapDen, tyLeLai, null);
    }

    public BangLaiChuan(int id, double giaNhapTu, double giaNhapDen, double tyLeLai, LoaiSanPham loaiSanPham) {
        this.id = id;
        this.giaNhapTu = giaNhapTu;
        this.giaNhapDen = giaNhapDen;
        this.tyLeLai = tyLeLai;
        this.loaiSanPham = loaiSanPham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGiaNhapTu() {
        return giaNhapTu;
    }

    public void setGiaNhapTu(double giaNhapTu) {
        this.giaNhapTu = giaNhapTu;
    }

    public double getGiaNhapDen() {
        return giaNhapDen;
    }

    public void setGiaNhapDen(double giaNhapDen) {
        this.giaNhapDen = giaNhapDen;
    }

    public double getTyLeLai() {
        return tyLeLai;
    }

    public void setTyLeLai(double tyLeLai) {
        this.tyLeLai = tyLeLai;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    @Override
    public String toString() {
        return "BangLaiChuan{" +
                "id=" + id +
                ", giaNhapTu=" + giaNhapTu +
                ", giaNhapDen=" + giaNhapDen +
                ", tyLeLai=" + tyLeLai +
                ", loaiSanPham=" + loaiSanPham +
                '}';
    }
}



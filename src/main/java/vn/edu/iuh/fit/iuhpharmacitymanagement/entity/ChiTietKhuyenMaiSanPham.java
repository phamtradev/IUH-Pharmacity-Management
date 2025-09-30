/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietKhuyenMaiSanPham {
    private String maChiTietKhuyenMaiSanPham;
    private SanPham sanPham;
    private KhuyenMai khuyenMai;

    public ChiTietKhuyenMaiSanPham() {
    }

    public ChiTietKhuyenMaiSanPham(String maChiTietKhuyenMaiSanPham, SanPham sanPham, KhuyenMai khuyenMai) {
        this.maChiTietKhuyenMaiSanPham = maChiTietKhuyenMaiSanPham;
        this.sanPham = sanPham;
        this.khuyenMai = khuyenMai;
    }

    public String getMaChiTietKhuyenMaiSanPham() {
        return maChiTietKhuyenMaiSanPham;
    }

    public void setMaChiTietKhuyenMaiSanPham(String maChiTietKhuyenMaiSanPham) {
        this.maChiTietKhuyenMaiSanPham = maChiTietKhuyenMaiSanPham;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    @Override
    public String toString() {
        return "ChiTietKhuyenMaiSanPham{" +
                "maChiTietKhuyenMaiSanPham='" + maChiTietKhuyenMaiSanPham + '\'' +
                ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : null) +
                ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKhuyenMai() : null) +
                '}';
    }
}
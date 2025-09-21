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

    private SanPham sanPham;
    private KhuyenMai khuyenMai;

    public ChiTietKhuyenMaiSanPham() {
    }

    public ChiTietKhuyenMaiSanPham(SanPham sanPham, KhuyenMai khuyenMai) {
        this.sanPham = sanPham;
        this.khuyenMai = khuyenMai;
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
        return "ChiTietKhuyenMaiSanPham{" + "sanPham=" + sanPham + ", khuyenMai=" + khuyenMai + '}';
    }

}

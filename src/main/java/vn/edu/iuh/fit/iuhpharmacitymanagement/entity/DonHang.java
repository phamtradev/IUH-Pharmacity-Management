/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.PhuongThucThanhToan;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author User
 */
public class DonHang {
    private String maDonHang;
    private LocalDate ngayDatHang;
    private double thanhTien;
    private PhuongThucThanhToan phuongThucThanhToan;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;
    private List<ChiTietDonHang> chiTietDonHang;

    public DonHang() {
    }

    public DonHang(String maDonHang, LocalDate ngayDatHang) {
        this.maDonHang = maDonHang;
        this.ngayDatHang = ngayDatHang;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public LocalDate getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(LocalDate ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public List<ChiTietDonHang> getChiTietDonHang() {
        return chiTietDonHang;
    }

    public void setChiTietDonHang(List<ChiTietDonHang> chiTietDonHang) {
        this.chiTietDonHang = chiTietDonHang;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDonHang, ngayDatHang);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DonHang other = (DonHang) obj;
        return Objects.equals(this.maDonHang, other.maDonHang) &&
                Objects.equals(this.ngayDatHang, other.ngayDatHang);
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDonHang='" + maDonHang + '\'' +
                ", ngayDatHang=" + ngayDatHang +
                ", thanhTien=" + thanhTien +
                ", phuongThucThanhToan=" + phuongThucThanhToan +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : null) +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKhachHang() : null) +
                ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKhuyenMai() : null) +
                ", chiTietDonHang=" + (chiTietDonHang != null ? chiTietDonHang.size() : 0) +
                '}';
    }
}
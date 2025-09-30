/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class DonTraHang {
    private String maDonTraHang;
    private LocalDate ngayDatHang;
    private double thanhTien;
    private NhanVien nhanVien;
    private DonHang donHang;
    private List<ChiTietDonTraHang> chiTietDonTraHang;

    public DonTraHang() {
    }

    public DonTraHang(String maDonTra, LocalDate ngayDatHang) {
        this.maDonTraHang = maDonTra;
        this.ngayDatHang = ngayDatHang;
    }

    public String getMaDonTraHang() {
        return maDonTraHang;
    }

    public void setMaDonTraHang(String maDonTra) {
        this.maDonTraHang = maDonTra;
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

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public List<ChiTietDonTraHang> getChiTietDonTraHang() {
        return chiTietDonTraHang;
    }

    public void setChiTietDonTraHang(List<ChiTietDonTraHang> chiTietDonTraHang) {
        this.chiTietDonTraHang = chiTietDonTraHang;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDonTraHang, ngayDatHang);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DonTraHang other = (DonTraHang) obj;
        return Objects.equals(maDonTraHang, other.maDonTraHang) &&
                Objects.equals(ngayDatHang, other.ngayDatHang);
    }

    @Override
    public String toString() {
        return "DonTraHang{" +
                "maDonTra='" + maDonTraHang + '\'' +
                ", ngayDatHang=" + ngayDatHang +
                ", thanhTien=" + thanhTien +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : null) +
                ", donHang=" + (donHang != null ? donHang.getMaDonHang() : null) +
                ", chiTietDonTraHang=" + (chiTietDonTraHang != null ? chiTietDonTraHang.size() : 0) +
                '}';
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author User
 */
public class HangHong {
    private String maHangHong;
    private LocalDate ngayNhap;
    private double thanhTien;
    private NhanVien nhanVien;
    private List<ChiTietHangHong> chiTietHangHong;

    public HangHong() {
    }

    public HangHong(String maHangHong, LocalDate ngayNhap, double thanhTien,
                    NhanVien nhanVien, List<ChiTietHangHong> chiTietHangHong) {
        this.maHangHong = maHangHong;
        this.ngayNhap = ngayNhap;
        this.thanhTien = thanhTien;
        this.nhanVien = nhanVien;
        this.chiTietHangHong = chiTietHangHong;
    }

    public String getMaHangHong() {
        return maHangHong;
    }

    public void setMaHangHong(String maHangHong) {
        this.maHangHong = maHangHong;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
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

    public List<ChiTietHangHong> getChiTietHangHong() {
        return chiTietHangHong;
    }

    public void setChiTietHangHong(List<ChiTietHangHong> chiTietHangHong) {
        this.chiTietHangHong = chiTietHangHong;
    }

    @Override
    public String toString() {
        return "HangHong{" +
                "maHangHong='" + maHangHong + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", thanhTien=" + thanhTien +
                ", nhanVien=" + nhanVien +
                ", chiTietHangHong=" + chiTietHangHong +
                '}';
    }
}
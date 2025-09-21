/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author User
 */
public class DonNhapHang {
    private String maDonNhapHang;
    private LocalDate ngayNhap;

    public DonNhapHang() {
    }

    public DonNhapHang(String maDonNhapHang, LocalDate ngayNhap) {
        this.maDonNhapHang = maDonNhapHang;
        this.ngayNhap = ngayNhap;
    }

    public String getMaDonNhapHang() {
        return maDonNhapHang;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setMaDonNhapHang(String maDonNhapHang) {
        this.maDonNhapHang = maDonNhapHang;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.maDonNhapHang);
        hash = 79 * hash + Objects.hashCode(this.ngayNhap);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DonNhapHang other = (DonNhapHang) obj;
        if (!Objects.equals(this.maDonNhapHang, other.maDonNhapHang)) {
            return false;
        }
        return Objects.equals(this.ngayNhap, other.ngayNhap);
    }

    @Override
    public String toString() {
        return "DonNhapHang{" + "maDonNhapHang=" + maDonNhapHang + ", ngayNhap=" + ngayNhap + '}';
    }
    
    
}

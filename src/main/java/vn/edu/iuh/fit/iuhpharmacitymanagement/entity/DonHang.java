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
public class DonHang {
    private String maDonHang;
    private LocalDate ngayDatHang;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.maDonHang);
        hash = 37 * hash + Objects.hashCode(this.ngayDatHang);
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
        final DonHang other = (DonHang) obj;
        if (!Objects.equals(this.maDonHang, other.maDonHang)) {
            return false;
        }
        return Objects.equals(this.ngayDatHang, other.ngayDatHang);
    }

    @Override
    public String toString() {
        return "DonHang{" + "maDonHang=" + maDonHang + ", ngayDatHang=" + ngayDatHang + '}';
    }
    
    
}

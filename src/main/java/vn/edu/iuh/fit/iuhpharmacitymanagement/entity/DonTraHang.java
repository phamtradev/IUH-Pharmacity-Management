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
public class DonTraHang {
    private String maDonTra;
    private LocalDate ngayTraHang;

    public DonTraHang() {
    }

    public DonTraHang(String maDonTra, LocalDate ngayTraHang) {
        this.maDonTra = maDonTra;
        this.ngayTraHang = ngayTraHang;
    }

    public String getMaDonTra() {
        return maDonTra;
    }

    public void setMaDonTra(String maDonTra) {
        this.maDonTra = maDonTra;
    }

    public LocalDate getNgayTraHang() {
        return ngayTraHang;
    }

    public void setNgayTraHang(LocalDate ngayTraHang) {
        this.ngayTraHang = ngayTraHang;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.maDonTra);
        hash = 13 * hash + Objects.hashCode(this.ngayTraHang);
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
        final DonTraHang other = (DonTraHang) obj;
        if (!Objects.equals(this.maDonTra, other.maDonTra)) {
            return false;
        }
        return Objects.equals(this.ngayTraHang, other.ngayTraHang);
    }

    @Override
    public String toString() {
        return "DonTraHang{" + "maDonTra=" + maDonTra + ", ngayTraHang=" + ngayTraHang + '}';
    }
    
    
}

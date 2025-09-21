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
public class HangHong {
    private String mahangHong;
    private LocalDate ngayNhap;

    public HangHong() {
    }

    public HangHong(String mahangHong, LocalDate ngayNhap) {
        this.mahangHong = mahangHong;
        this.ngayNhap = ngayNhap;
    }

    public String getMahangHong() {
        return mahangHong;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setMahangHong(String mahangHong) {
        this.mahangHong = mahangHong;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.mahangHong);
        hash = 53 * hash + Objects.hashCode(this.ngayNhap);
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
        final HangHong other = (HangHong) obj;
        if (!Objects.equals(this.mahangHong, other.mahangHong)) {
            return false;
        }
        return Objects.equals(this.ngayNhap, other.ngayNhap);
    }

    @Override
    public String toString() {
        return "HangHong{" + "mahangHong=" + mahangHong + ", ngayNhap=" + ngayNhap + '}';
    }
    
    
}

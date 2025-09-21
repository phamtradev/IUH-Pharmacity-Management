/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.util.Objects;

/**
 *
 * @author User
 */
public class ChiTietDonTraHang {
    private int soLuong;
    private double donGia;
    private String lyDoTra;

    public ChiTietDonTraHang() {
    }

    public ChiTietDonTraHang(int soLuong, double donGia, String lyDoTra) {
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.lyDoTra = lyDoTra;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getLyDoTra() {
        return lyDoTra;
    }

    public void setLyDoTra(String lyDoTra) {
        this.lyDoTra = lyDoTra;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.soLuong;
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.donGia) ^ (Double.doubleToLongBits(this.donGia) >>> 32));
        hash = 23 * hash + Objects.hashCode(this.lyDoTra);
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
        final ChiTietDonTraHang other = (ChiTietDonTraHang) obj;
        if (this.soLuong != other.soLuong) {
            return false;
        }
        if (Double.doubleToLongBits(this.donGia) != Double.doubleToLongBits(other.donGia)) {
            return false;
        }
        return Objects.equals(this.lyDoTra, other.lyDoTra);
    }

    @Override
    public String toString() {
        return "ChiTietDonTraHang{" + "soLuong=" + soLuong + ", donGia=" + donGia + ", lyDoTra=" + lyDoTra + '}';
    }
    
    
}

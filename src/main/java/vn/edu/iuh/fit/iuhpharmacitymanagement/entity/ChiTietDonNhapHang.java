/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietDonNhapHang {

    private int soLuong;
    private double donGia;

    public ChiTietDonNhapHang() {
    }

    public ChiTietDonNhapHang(int soLuong, double donGia) {
        this.soLuong = soLuong;
        this.donGia = donGia;
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

    @Override
    public String toString() {
        return "ChiTietDonNhapHang{" + "soLuong=" + soLuong + ", donGia=" + donGia + '}';
    }

}

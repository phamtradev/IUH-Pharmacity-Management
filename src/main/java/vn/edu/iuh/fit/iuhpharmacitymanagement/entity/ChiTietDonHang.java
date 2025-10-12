/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietDonHang {
    private String maChiTietDonHang;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private double giamGia;
    private LoHang loHang;
    private DonHang donHang;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(String maChiTietDonHang, int soLuong, double donGia, double thanhTien, double giamGia, LoHang loHang, DonHang donHang) {
        this.maChiTietDonHang = maChiTietDonHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.giamGia = giamGia;
        this.loHang = loHang;
        this.donHang = donHang;
    }

    public String getMaChiTietDonHang() {
        return maChiTietDonHang;
    }

    public void setMaChiTietDonHang(String maChiTietDonHang) {
        this.maChiTietDonHang = maChiTietDonHang;
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

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) {
        this.loHang = loHang;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    @Override
    public String toString() {
        return "ChiTietDonHang{" +
                "maChiTietDonHang='" + maChiTietDonHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                ", giamGia=" + giamGia +
                ", loHang=" + (loHang != null ? loHang.getMaLoHang() : null) +
                ", donHang=" + (donHang != null ? donHang.getMaDonHang() : null) +
                '}';
    }
}
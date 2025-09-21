/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;

/**
 *
 * @author PhamTra
 */
public class KhuyenMai {

    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayBatDau;
    private String ngayKetThuc;
    private double giamGia;
    private boolean trangThai;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKhuyenMai,
            String tenKhuyenMai,
            LocalDate ngayBatDau,
            String ngayKetThuc,
            double giamGia,
            boolean trangThai) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giamGia = giamGia;
        this.trangThai = trangThai;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" + "maKhuyenMai=" + maKhuyenMai + ","
                + " tenKhuyenMai=" + tenKhuyenMai + ","
                + " ngayBatDau=" + ngayBatDau + ","
                + " ngayKetThuc=" + ngayKetThuc + ","
                + " giamGia=" + giamGia + ","
                + " trangThai=" + trangThai + '}';
    }

}

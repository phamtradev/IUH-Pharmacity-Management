package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.io.Serializable;
import java.util.Objects;
/**
 *
 * @author PhamTra
 */
public class ChiTietDonNhapHang {
    private String maChiTietDonNhapHang;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private DonNhapHang donNhapHang;
    private LoHang loHang;

    public ChiTietDonNhapHang() {
    }

    public ChiTietDonNhapHang(String maChiTietDonNhapHang, int soLuong, double donGia, double thanhTien, DonNhapHang donNhapHang, LoHang loHang) {
        this.maChiTietDonNhapHang = maChiTietDonNhapHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.donNhapHang = donNhapHang;
        this.loHang = loHang;
    }

    public String getMaChiTietDonNhapHang() {
        return maChiTietDonNhapHang;
    }

    public void setMaChiTietDonNhapHang(String maChiTietDonNhapHang) {
        this.maChiTietDonNhapHang = maChiTietDonNhapHang;
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

    public DonNhapHang getDonNhapHang() {
        return donNhapHang;
    }

    public void setDonNhapHang(DonNhapHang donNhapHang) {
        this.donNhapHang = donNhapHang;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) {
        this.loHang = loHang;
    }

    @Override
    public String toString() {
        return "ChiTietDonNhapHang{" +
                "maChiTietDonNhapHang='" + maChiTietDonNhapHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                ", donNhapHang=" + (donNhapHang != null ? donNhapHang.getMaDonNhapHang() : null) +
                ", loHang=" + (loHang != null ? loHang.getMaLoHang() : null) +
                '}';
    }
}
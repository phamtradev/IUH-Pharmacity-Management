package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 * @author PhamTra
 */
public class ChiTietHangHong {
    private String maChiTietHangHong;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    private LoHang loHang;
    private HangHong hangHong;

    public ChiTietHangHong() {
    }

    public ChiTietHangHong(String maChiTietHangHong, int soLuong, double donGia, double thanhTien, LoHang loHang, HangHong hangHong) {
        this.maChiTietHangHong = maChiTietHangHong;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.loHang = loHang;
        this.hangHong = hangHong;
    }

    public String getMaChiTietHangHong() {
        return maChiTietHangHong;
    }

    public void setMaChiTietHangHong(String maChiTietHangHong) {
        this.maChiTietHangHong = maChiTietHangHong;
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

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) {
        this.loHang = loHang;
    }

    public HangHong getHangHong() {
        return hangHong;
    }

    public void setHangHong(HangHong hangHong) {
        this.hangHong = hangHong;
    }
}

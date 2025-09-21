/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class SanPham {

    private String maSanPham;
    private String tenSanPham;
    private String soDangKy;
    private String hoatChat;
    private String lieuDung;
    private String cachDongGoi;
    private String quocGiaSanXuat;
    private String nhaSanXuat;
    private double giaNhap;
    private double giaBan;
    private boolean hoatDong;
    private double thueVAT = 0.1;
    private String hinhAnh;

    public SanPham() {
    }

    public SanPham(String maSanPham,
            String tenSanPham,
            String soDangKy,
            String hoatChat,
            String lieuDung,
            String cachDongGoi,
            String quocGiaSanXuat,
            String nhaSanXuat,
            double giaNhap,
            double giaBan,
            boolean hoatDong,
            String hinhAnh) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soDangKy = soDangKy;
        this.hoatChat = hoatChat;
        this.lieuDung = lieuDung;
        this.cachDongGoi = cachDongGoi;
        this.quocGiaSanXuat = quocGiaSanXuat;
        this.nhaSanXuat = nhaSanXuat;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.hoatDong = hoatDong;
        this.hinhAnh = hinhAnh;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getSoDangKy() {
        return soDangKy;
    }

    public void setSoDangKy(String soDangKy) {
        this.soDangKy = soDangKy;
    }

    public String getHoatChat() {
        return hoatChat;
    }

    public void setHoatChat(String hoatChat) {
        this.hoatChat = hoatChat;
    }

    public String getLieuDung() {
        return lieuDung;
    }

    public void setLieuDung(String lieuDung) {
        this.lieuDung = lieuDung;
    }

    public String getCachDongGoi() {
        return cachDongGoi;
    }

    public void setCachDongGoi(String cachDongGoi) {
        this.cachDongGoi = cachDongGoi;
    }

    public String getQuocGiaSanXuat() {
        return quocGiaSanXuat;
    }

    public void setQuocGiaSanXuat(String quocGiaSanXuat) {
        this.quocGiaSanXuat = quocGiaSanXuat;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = thueVAT;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "SanPham{" + "maSanPham=" + maSanPham + ","
                + " tenSanPham=" + tenSanPham + ","
                + " soDangKy=" + soDangKy + ","
                + " hoatChat=" + hoatChat + ","
                + " lieuDung=" + lieuDung + ","
                + " cachDongGoi=" + cachDongGoi + ","
                + " quocGiaSanXuat=" + quocGiaSanXuat + ","
                + " nhaSanXuat=" + nhaSanXuat + ","
                + " giaNhap=" + giaNhap + ","
                + " giaBan=" + giaBan + ","
                + " hoatDong=" + hoatDong + ","
                + " thueVAT=" + thueVAT + ","
                + " hinhAnh=" + hinhAnh + '}';
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author User
 */
public class HangHong {
    private String maHangHong;
    private LocalDate ngayNhap;
    private double thanhTien;
    private NhanVien nhanVien;
    private List<ChiTietHangHong> chiTietHangHong;
    public static final String MA_HANG_HONG_SAI ="Mã hàng hỏng phải bắt đầu bằng HHddmmyyyyxxx (dd là ngày phát hiện hàng hỏng, mm là tháng phát hiện hàng hỏng, yyyy là năm phát hiện hàng hỏng, xxx  là 3 số dương)";
    public static final String NGAY_NHAP_HANG_SAI ="Ngày nhập hàng hỏng phải nhỏ hơn hoặc bằng ngày hiện tại";
    public static final String NGAY_NHAP_HANG_NULL ="Ngày nhập hàng hỏng không được rỗng";
    public HangHong() {
    }

    public HangHong(String maHangHong, LocalDate ngayNhap, double thanhTien,
                    NhanVien nhanVien, List<ChiTietHangHong> chiTietHangHong) {
        this.maHangHong = maHangHong;
        this.ngayNhap = ngayNhap;
        this.thanhTien = thanhTien;
        this.nhanVien = nhanVien;
        this.chiTietHangHong = chiTietHangHong;
    }

    public String getMaHangHong() {
        return maHangHong;
    }

    public void setMaHangHong(String maHangHong) throws Exception {
        if(maHangHong.isBlank()){
            throw new Exception(MA_HANG_HONG_SAI);
        }
        this.maHangHong = maHangHong;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) throws Exception {
        if(ngayNhap==null){
            throw new Exception(NGAY_NHAP_HANG_NULL);
        }
        LocalDate hientai =LocalDate.now();
        if(ngayNhap.isAfter(hientai)){
            throw new Exception(NGAY_NHAP_HANG_SAI);
        }
        this.ngayNhap = ngayNhap;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<ChiTietHangHong> getChiTietHangHong() {
        return chiTietHangHong;
    }

    public void setChiTietHangHong(List<ChiTietHangHong> chiTietHangHong) {
        this.chiTietHangHong = chiTietHangHong;
    }

    @Override
    public String toString() {
        return "HangHong{" +
                "maHangHong='" + maHangHong + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", thanhTien=" + thanhTien +
                ", nhanVien=" + nhanVien +
                ", chiTietHangHong=" + chiTietHangHong +
                '}';
    }
}
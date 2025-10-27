/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public static final String MA_HANG_HONG_SAI ="Mã hàng hỏng không được rỗng và phải bắt đầu bằng HHddmmyyyyxxxx (dd là ngày phát hiện hàng hỏng, mm là tháng phát hiện hàng hỏng, yyyy là năm phát hiện hàng hỏng, xxxx  là 4 số dương)";
       private static final String REGEX_MA_HANG_HONG =
    "^HH(0[1-9]|[12][0-9]|3[01])" + 
    "(0[1-9]|1[0-2])" +                
    "(20)\\d{2}" +             
    "(?!0000)\\d{4}$";  
    
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
        if (maHangHong == null || !maHangHong.matches(REGEX_MA_HANG_HONG) || maHangHong.isBlank()) {
            throw new Exception(MA_HANG_HONG_SAI);
        }
        this.maHangHong = maHangHong;
    }
//    private static final String MA_HANG_HONG_PREFIX = "HH";
//    private static final String MA_HANG_HONG_SUFFIX_REGEX = "\\d{4}"; // 4 chữ số
//
//    public void setMaHangHong(String maHangHong) throws Exception {
//        String ngayThangNamHienTai = LocalDate.now()
//                .format(DateTimeFormatter.ofPattern("ddMMyyyy"));
//
//        String regex = "^" + MA_HANG_HONG_PREFIX + ngayThangNamHienTai + MA_HANG_HONG_SUFFIX_REGEX + "$";
//
//        if (maHangHong == null || maHangHong.isBlank() || !maHangHong.matches(regex)) {
//            throw new Exception(MA_HANG_HONG_SAI);
//        }
//
//        this.maHangHong = maHangHong;
//    }

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

    public void setThanhTien(double thanhTien) throws Exception {
        if(thanhTien<=0 ) throw new Exception("thành tiền phải lớn hơn 0, không được rỗng");
        this.thanhTien = thanhTien;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) throws Exception {
        if(nhanVien == null) throw new Exception("nhân viên không được rỗng");
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
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.PhuongThucThanhToan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author User
 */
public class DonHang {
    private String maDonHang;
    private LocalDate ngayDatHang;
    private double thanhTien;
    private PhuongThucThanhToan phuongThucThanhToan;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;
    private List<ChiTietDonHang> chiTietDonHang;
    
    public static final String MA_DON_HANG_SAI = "Mã đơn hàng phải có dạng DHddMMyyyyxxxx (Trong đó ddMMyyyy là ngày lập đơn, xxxx là 4 chữ số) ví dụ: DNH20102025";
    public static final String NGAY_DAT_HANG_RONG = "Ngày đặt hàng không được để trống";
    public static final String THANH_TIEN_SAI = "Thành tiền phải là một số dương";
    public static final String PHUONG_THUC_THANH_TOAN_RONG = "Phương thức thanh toán không được để trống";
    public static final String NHAN_VIEN_RONG = "Nhân viên không được để trống";
    public static final String KHACH_HANG_RONG = "Khách hàng không được để trống";
    public static final String KHUYEN_MAI_RONG = "Khuyến mãi không được để trống";
    public static final String CHI_TIET_DON_HANG_RONG = "Đơn hàng phải có ít nhất một chi tiết đơn hàng";
    
    public static final String MA_DON_HANG_PREFIX = "DH";
    public static final String MA_DON_HANG_SUFFIX_REGEX = "\\d{4}";

    public DonHang() {
    }

    public DonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }
    
    public DonHang(String maDonHang, LocalDate ngayDatHang) {
        this.maDonHang = maDonHang;
        this.ngayDatHang = ngayDatHang;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) throws Exception{
        String ngayThangNamHienTai = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String regex = "^" + MA_DON_HANG_PREFIX + ngayThangNamHienTai + MA_DON_HANG_SUFFIX_REGEX + "$";
        if (maDonHang == null || !maDonHang.matches(regex)) {
            throw new Exception(MA_DON_HANG_SAI);
        }
        this.maDonHang = maDonHang;
    }

    public LocalDate getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(LocalDate ngayDatHang) throws Exception{
        if(ngayDatHang == null){
            throw new Exception(NGAY_DAT_HANG_RONG);
        }
        this.ngayDatHang = ngayDatHang;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) throws Exception{
        if(thanhTien <= 0){
            throw new Exception(THANH_TIEN_SAI);
        }
        this.thanhTien = thanhTien;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) throws Exception{
        if(phuongThucThanhToan == null){
            throw new Exception(PHUONG_THUC_THANH_TOAN_RONG);
        }
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) throws Exception{
        if(nhanVien == null){
            throw new Exception(NHAN_VIEN_RONG);
        }
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) throws Exception{
        if(khachHang == null){
            throw new Exception(KHACH_HANG_RONG);
        }
        this.khachHang = khachHang;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) throws Exception{
        if(khuyenMai == null){
            throw new Exception(KHUYEN_MAI_RONG);
        }
        this.khuyenMai = khuyenMai;
    }

    public List<ChiTietDonHang> getChiTietDonHang() {
        return chiTietDonHang;
    }

    public void setChiTietDonHang(List<ChiTietDonHang> chiTietDonHang) throws Exception{
        if (chiTietDonHang == null || chiTietDonHang.isEmpty()) {
            throw new Exception(CHI_TIET_DON_HANG_RONG);
        }
        this.chiTietDonHang = chiTietDonHang;
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDonHang='" + maDonHang + '\'' +
                ", ngayDatHang=" + ngayDatHang +
                ", thanhTien=" + thanhTien +
                ", phuongThucThanhToan=" + phuongThucThanhToan +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : null) +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKhachHang() : null) +
                ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKhuyenMai() : null) +
                ", chiTietDonHang=" + (chiTietDonHang != null ? chiTietDonHang.size() : 0) +
                '}';
    }
}
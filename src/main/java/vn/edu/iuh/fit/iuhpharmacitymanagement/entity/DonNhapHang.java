/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class DonNhapHang {
    private String maDonNhapHang;
    private LocalDate ngayNhap;
    private double thanhTien;
    private NhanVien nhanVien;
    private NhaCungCap nhaCungCap;
    private List<ChiTietDonNhapHang> chiTietDonNhapHang;
    public static final String MA_DON_NHAP_HANG_SAI ="Mã đơn nhập hàng phải bắt đầu bằng DNHddmmyyyy (dd là ngày nhập, mm là tháng nhập, yyyy là năm nhập)";
    public static final String NGAY_NHAP_HANG_SAI ="Ngày nhập hàng phải nhỏ hơn hoặc bằng ngày hiện tại";
    public static final String NGAY_NHAP_HANG_NULL ="Ngày nhập hàng không được rỗng";
    public DonNhapHang() {
    }

    public DonNhapHang(String maDonNhapHang, LocalDate ngayNhap) {
        this.maDonNhapHang = maDonNhapHang;
        this.ngayNhap = ngayNhap;
    }

    public String getMaDonNhapHang() {
        return maDonNhapHang;
    }

    public void setMaDonNhapHang(String maDonNhapHang) throws Exception {
        if(maDonNhapHang.isBlank()){
            throw new Exception(MA_DON_NHAP_HANG_SAI);
        }
        this.maDonNhapHang=maDonNhapHang;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) throws Exception {
        if(ngayNhap==null){
            throw new Exception(NGAY_NHAP_HANG_NULL);
        }
        LocalDate hientai=LocalDate.now();
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

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public List<ChiTietDonNhapHang> getChiTietDonNhapHang() {
        return chiTietDonNhapHang;
    }

    public void setChiTietDonNhapHang(List<ChiTietDonNhapHang> chiTietDonNhapHang) {
        this.chiTietDonNhapHang = chiTietDonNhapHang;
    }

    @Override
    public String toString() {
        return "DonNhapHang{" +
                "maDonNhapHang='" + maDonNhapHang + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", thanhTien=" + thanhTien +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : null) +
", nhaCungCap=" + (nhaCungCap != null ? nhaCungCap.getMaNhaCungCap() : null) +
                ", chiTietDonNhapHang=" + (chiTietDonNhapHang != null ? chiTietDonNhapHang.size() : 0) +
                '}';
    }
}
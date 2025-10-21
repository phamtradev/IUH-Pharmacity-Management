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
public class DonTraHang {
    private String maDonTraHang;
    private LocalDate ngayTraHang;
    private double thanhTien;
    private NhanVien nhanVien;
    private DonHang donHang;
    private List<ChiTietDonTraHang> chiTietDonTraHang;
    public static final String MA_DON_TRA_HANG_SAI ="Mã đơn trả hàng phải bắt đầu bằng DTddmmyyyyxxx (dd là ngày trả, mm là tháng trả, yyyy là năm trả, xxx  là 3 số dương)";
    public static final String NGAY_TRA_HANG_SAI ="Ngày trả hàng phải nhỏ hơn hoặc bằng ngày hiện tại";
    public static final String NGAY_TRA_HANG_NULL ="Ngày trả hàng không được rỗng";
    public DonTraHang() {
    }

    public DonTraHang(String maDonTra, LocalDate ngayTraHang) {
        this.maDonTraHang = maDonTra;
        this.ngayTraHang = ngayTraHang;
    }

    public String getMaDonTraHang() {
        return maDonTraHang;
    }

    public void setMaDonTraHang(String maDonTra) throws Exception {
        if(maDonTra.isBlank()){
            throw new Exception(MA_DON_TRA_HANG_SAI);
        }   
        this.maDonTraHang = maDonTra;
    }

    public LocalDate getngayTraHang() {
        return ngayTraHang;
    }

    public void setngayTraHang(LocalDate ngayTraHang) throws Exception {
        if(ngayTraHang==null){
            throw new Exception(NGAY_TRA_HANG_NULL);
        }
        LocalDate hientai=LocalDate.now();
        if(ngayTraHang.isAfter(hientai)){
            throw new Exception (NGAY_TRA_HANG_SAI);
        }
        this.ngayTraHang = ngayTraHang;
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

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public List<ChiTietDonTraHang> getChiTietDonTraHang() {
        return chiTietDonTraHang;
    }

    public void setChiTietDonTraHang(List<ChiTietDonTraHang> chiTietDonTraHang) {
        this.chiTietDonTraHang = chiTietDonTraHang;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDonTraHang, ngayTraHang);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DonTraHang other = (DonTraHang) obj;
return Objects.equals(maDonTraHang, other.maDonTraHang) &&
                Objects.equals(ngayTraHang, other.ngayTraHang);
    }

    @Override
    public String toString() {
        return "DonTraHang{" +
                "maDonTra='" + maDonTraHang + '\'' +
                ", ngayTraHang=" + ngayTraHang +
                ", thanhTien=" + thanhTien +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : null) +
                ", donHang=" + (donHang != null ? donHang.getMaDonHang() : null) +
                ", chiTietDonTraHang=" + (chiTietDonTraHang != null ? chiTietDonTraHang.size() : 0) +
                '}';
    }

    public void setNgayTraHang(LocalDate toLocalDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getNgayTraHang() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
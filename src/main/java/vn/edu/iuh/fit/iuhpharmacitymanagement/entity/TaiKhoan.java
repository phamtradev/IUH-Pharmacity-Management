/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;



import java.util.Objects;

/**
 *
 * @author User
 */
public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private NhanVien nhanVien;
    
    public static final String TEN_DANG_NHAP_SAI = "Tên đăng nhập phải có dạng TDNxxxxx (5 chữ số dương)";
    public static final String MAT_KHAU_SAI = "Mật khẩu phải từ 6 ký tự trở lên, có ít nhất 1 chữ số và 1 ký tự đặc biệt (@#$^*)";
    public static final String NHAN_VIEN_RONG = "Nhân viên không được để trống";
    
    public static final String TEN_DANG_NHAP_REGEX = "^TDN\\d{5}$";
    public static final String MAT_KHAU_REGEX = "^(?=.*[0-9])(?=.*[@#$^*]).{6,}$";

    public TaiKhoan() {
    }

    public TaiKhoan(String tenDangNhap, String matKhau) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public TaiKhoan(String tenDangNhap, String matKhau, NhanVien nhanVien) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.nhanVien = nhanVien;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) throws Exception {
//        if (!tenDangNhap.matches(TEN_DANG_NHAP_REGEX)) {
//            throw new Exception(TEN_DANG_NHAP_SAI);
//        }
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) throws Exception {
        if (!matKhau.matches(MAT_KHAU_REGEX)) {
            throw new Exception(MAT_KHAU_SAI);
        }
        this.matKhau = matKhau;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) throws Exception {
        if (nhanVien == null) {
            throw new Exception(NHAN_VIEN_RONG);
        }
        this.nhanVien = nhanVien;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenDangNhap, matKhau);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TaiKhoan)) return false;
        TaiKhoan other = (TaiKhoan) obj;
        return Objects.equals(this.tenDangNhap, other.tenDangNhap)
                && Objects.equals(this.matKhau, other.matKhau);
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") +
                '}';
    }
}
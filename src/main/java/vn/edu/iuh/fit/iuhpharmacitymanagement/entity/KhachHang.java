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
public class KhachHang {
    private String maKhachHang;
    private String tenKhachHang;
    private String soDienThoai;
    private String diaChi;
    private String email;
    private String gioiTinh;
    
    public static final String MA_KHACH_HANG_SAI = "Mã khách hàng phải có dạng KHxxxxx (5 chữ số dương)";
    public static final String TEN_KHACH_HANG_RONG = "Tên khách hàng không được rỗng";
    public static final String SO_DIEN_THOAI_SAI = "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số";
    public static final String DIA_CHI_RONG = "Địa chỉ không được rỗng";
    public static final String EMAIL_SAI = "Email không đúng định dạng (phải chứa @ và dấu . hợp lệ)";
    
    public static final String MA_KHACH_HANG_REGEX = "^KH\\d{5}$";
    public static final String SO_DIEN_THOAI_REGEX = "^0[0-9]{9}$";
    public static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String tenKhachHang, String soDienThoai,
                     String diaChi, String email, String gioiTinh) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
        this.gioiTinh = gioiTinh;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) throws Exception {
        if (!maKhachHang.matches(MA_KHACH_HANG_REGEX)) {
            throw new Exception(MA_KHACH_HANG_SAI);
        }
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) throws Exception {
        if (tenKhachHang.isBlank()) {
            throw new Exception(TEN_KHACH_HANG_RONG);
        }
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) throws Exception {
        if (!soDienThoai.matches(SO_DIEN_THOAI_REGEX)) {
            throw new Exception(SO_DIEN_THOAI_SAI);
        }
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) throws Exception {
        if (diaChi.isBlank()) {
            throw new Exception(DIA_CHI_RONG);
        }
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (!email.matches(EMAIL_REGEX)) {
            throw new Exception(EMAIL_SAI);
        }
        this.email = email;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKhachHang, tenKhachHang, soDienThoai, diaChi, email, gioiTinh);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KhachHang other = (KhachHang) obj;
        return Objects.equals(maKhachHang, other.maKhachHang) &&
                Objects.equals(tenKhachHang, other.tenKhachHang) &&
                Objects.equals(soDienThoai, other.soDienThoai) &&
                Objects.equals(diaChi, other.diaChi) &&
                Objects.equals(email, other.email) &&
                Objects.equals(gioiTinh, other.gioiTinh);
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang='" + maKhachHang + '\'' +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", email='" + email + '\'' +
                ", gioiTinh='" + gioiTinh + '\'' +
                '}';
    }
}
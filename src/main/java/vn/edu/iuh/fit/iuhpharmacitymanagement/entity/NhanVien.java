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
public class NhanVien {

    private String maNhanVien;
    private String tenNhanVien;
    private String diaChi;
    private String soDienThoai;
    private String email;
    private String vaiTro;
    
    public static final String MA_NHAN_VIEN_SAI = "Mã nhân viên phải có dạng NVxxxxx (5 chữ số dương)";
    public static final String TEN_NHAN_VIEN_RONG = "Tên nhân viên không được rỗng";
    public static final String DIA_CHI_RONG = "Địa chỉ không được rỗng";
    public static final String SO_DIEN_THOAI_SAI = "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số";
    public static final String EMAIL_SAI = "Email không đúng định dạng (phải chứa @ và dấu . hợp lệ)";
    public static final String VAI_TRO_SAI = "Vai trò chỉ được là 'Nhân viên' hoặc 'Quản lý'";
    
    public static final String MA_NHAN_VIEN_REGEX = "^NV\\d{5}$";
    public static final String SO_DIEN_THOAI_REGEX = "^0[0-9]{9}$";
    public static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String diaChi, String soDienThoai, String email, String vaiTro) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.vaiTro = vaiTro;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) throws Exception  {
        if (!maNhanVien.matches(MA_NHAN_VIEN_REGEX)) {
            throw new Exception(MA_NHAN_VIEN_SAI);
        }
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) throws Exception {
        if (tenNhanVien.isBlank()) {
            throw new Exception(TEN_NHAN_VIEN_RONG);
        }
        this.tenNhanVien = tenNhanVien;
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

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) throws Exception {
        if (!soDienThoai.matches(SO_DIEN_THOAI_REGEX)) {
            throw new Exception(SO_DIEN_THOAI_SAI);
        }
        this.soDienThoai = soDienThoai;
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

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) throws Exception{
        if (!vaiTro.equalsIgnoreCase("Nhân viên") && !vaiTro.equalsIgnoreCase("Quản lý")) {
            throw new Exception(VAI_TRO_SAI);
        }
        this.vaiTro = vaiTro;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien, tenNhanVien, diaChi, soDienThoai, email, vaiTro);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NhanVien)) return false;
        NhanVien other = (NhanVien) obj;
        return Objects.equals(maNhanVien, other.maNhanVien)
                && Objects.equals(tenNhanVien, other.tenNhanVien)
                && Objects.equals(diaChi, other.diaChi)
                && Objects.equals(soDienThoai, other.soDienThoai)
                && Objects.equals(email, other.email)
                && Objects.equals(vaiTro, other.vaiTro);
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", vaiTro='" + vaiTro + '\'' +
                '}';
    }
}

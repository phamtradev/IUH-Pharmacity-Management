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
    private String gioiTinh; 
    private String anhNhanVien;
    private String cccd;
    
    public static final String MA_NHAN_VIEN_SAI = "Mã nhân viên phải có dạng NVxxxxx (5 chữ số dương)";
    public static final String TEN_NHAN_VIEN_RONG = "Tên nhân viên không được rỗng";
    public static final String DIA_CHI_RONG = "Địa chỉ không được rỗng";
    public static final String SO_DIEN_THOAI_SAI = "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số";
    public static final String EMAIL_SAI = "Email không đúng định dạng (phải chứa @ và dấu . hợp lệ)";
    public static final String VAI_TRO_SAI = "Vai trò chỉ được là 'Nhân viên' hoặc 'Quản lý'";
    
    public static final String MA_NHAN_VIEN_REGEX = "^NV\\d{5}$";
    public static final String SO_DIEN_THOAI_REGEX = "^0[0-9]{9}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(com|vn)$";

    public NhanVien() {
    }

    public NhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }
    
    public NhanVien(String maNhanVien, String tenNhanVien, String diaChi, String soDienThoai, String email, String vaiTro) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.vaiTro = vaiTro;
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String diaChi, String soDienThoai, String email, String vaiTro, String gioiTinh) {
        this(maNhanVien, tenNhanVien, diaChi, soDienThoai, email, vaiTro);
        this.gioiTinh = gioiTinh;
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String diaChi, String soDienThoai, String email, String vaiTro, String gioiTinh, String anhNhanVien) {
        this(maNhanVien, tenNhanVien, diaChi, soDienThoai, email, vaiTro, gioiTinh);
        this.anhNhanVien = anhNhanVien;
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String diaChi, String soDienThoai, String email, String vaiTro, String gioiTinh, String anhNhanVien, String cccd) {
        this(maNhanVien, tenNhanVien, diaChi, soDienThoai, email, vaiTro, gioiTinh, anhNhanVien);
        this.cccd = cccd;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || !maNhanVien.trim().matches(MA_NHAN_VIEN_REGEX)) {
            throw new IllegalArgumentException(MA_NHAN_VIEN_SAI);
        }
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) throws Exception{
         if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            throw new Exception(TEN_NHAN_VIEN_RONG);
        }
        this.tenNhanVien = tenNhanVien;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) throws Exception{
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new Exception(DIA_CHI_RONG);
        }
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) throws Exception{
        if (soDienThoai == null || !soDienThoai.trim().matches(SO_DIEN_THOAI_REGEX)) {
            throw new Exception(SO_DIEN_THOAI_SAI);
        }
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception{
        if (email == null || !email.trim().matches(EMAIL_REGEX)) {
            throw new Exception(EMAIL_SAI);
        }
        this.email = email;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) throws Exception{
         if (vaiTro == null || (!vaiTro.equals("Nhân viên") && !vaiTro.equals("Quản lý"))) {
            throw new Exception(VAI_TRO_SAI);
        }
        this.vaiTro = vaiTro;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getAnhNhanVien() {
        return anhNhanVien;
    }

    public void setAnhNhanVien(String anhNhanVien) {
        this.anhNhanVien = anhNhanVien;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien, tenNhanVien, diaChi, soDienThoai, email, vaiTro, gioiTinh, anhNhanVien, cccd);
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
                && Objects.equals(vaiTro, other.vaiTro)
                && Objects.equals(gioiTinh, other.gioiTinh)
                && Objects.equals(anhNhanVien, other.anhNhanVien)
                && Objects.equals(cccd, other.cccd);
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
                ", gioiTinh='" + gioiTinh + '\'' +
                ", anhNhanVien='" + anhNhanVien + '\'' +
                ", cccd='" + cccd + '\'' +
                '}';
    }
}

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
public class NhaCungCap {

    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String diaChi;
    private String soDienThoai;
    private String email;
    private String maSoThue;
    
    public static final String MA_NHA_CUNG_CAP_SAI = "Mã nhà cung cấp phải có dạng NCCXXXXX (XXXXX là số nguyên dương từ 0001 đến 9999)";
    public static final String TEN_NHA_CUNG_CAP_RONG = "Tên nhà cung cấp không được để trống";
    public static final String DIA_CHI_RONG = "Địa chỉ không được để trống";
    public static final String SO_DIEN_THOAI_SAI = "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số";
    public static final String EMAIL_SAI = "Email không đúng định dạng";
    public static final String MA_SO_THUE_SAI = "Mã số thuế phải gồm 10 số hoặc 13 ký tự dạng 0123456789-001";

    public static final String MA_NHA_CUNG_CAP_REGEX = "^NCC\\d{5}$";
    public static final String SO_DIEN_THOAI_REGEX = "^0[0-9]{9}$";
    public static final String EMAIL_REGEX = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
    public static final String MA_SO_THUE_REGEX = "^[0-9]{10}(-[0-9]{3})?$";
    
    public NhaCungCap() {
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String diaChi, String soDienThoai, String email, String maSoThue) {
        this.maNhaCungCap = maNhaCungCap;
        this.tenNhaCungCap = tenNhaCungCap;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.maSoThue = maSoThue;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) throws Exception {
        if (!maNhaCungCap.matches(MA_NHA_CUNG_CAP_REGEX)) {
            throw new Exception(MA_NHA_CUNG_CAP_SAI);
        }
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) throws Exception {
        if (tenNhaCungCap.isBlank()) {
            throw new Exception(TEN_NHA_CUNG_CAP_RONG);
        }
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) throws Exception {
        if (diaChi != null && diaChi.isBlank()) {
            throw new Exception(DIA_CHI_RONG);
        }
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) throws Exception {
        if (soDienThoai != null && !soDienThoai.isEmpty() && !soDienThoai.matches(SO_DIEN_THOAI_REGEX)) {
            throw new Exception(SO_DIEN_THOAI_SAI);
        }
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email != null && !email.isEmpty() && !email.matches(EMAIL_REGEX)) {
            throw new Exception(EMAIL_SAI);
        }
        this.email = email;
    }

    public String getMaSoThue() {
        return maSoThue;
    }

    public void setMaSoThue(String maSoThue) throws Exception {
        if (maSoThue != null && !maSoThue.isEmpty() && !maSoThue.matches(MA_SO_THUE_REGEX)) {
            throw new Exception(MA_SO_THUE_SAI);
        }
        this.maSoThue = maSoThue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhaCungCap, tenNhaCungCap, soDienThoai, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhaCungCap other = (NhaCungCap) obj;
        return Objects.equals(maNhaCungCap, other.maNhaCungCap)
                && Objects.equals(tenNhaCungCap, other.tenNhaCungCap)
                && Objects.equals(soDienThoai, other.soDienThoai)
                && Objects.equals(email, other.email);
    }

    @Override
    public String toString() {
        return "NhaCungCap{" +
                "maNhaCungCap='" + maNhaCungCap + '\'' +
                ", tenNhaCungCap='" + tenNhaCungCap + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", maSoThue='" + maSoThue + '\'' +
                '}';
    }
}

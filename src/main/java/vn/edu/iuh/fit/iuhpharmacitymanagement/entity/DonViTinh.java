/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class DonViTinh {
    private String maDonVi;
    private String tenDonVi;
    
    public static final String MA_DON_VI_SAI = "Mã đơn vị không hợp lệ. Mã phải có dạng DVxxxxx, với xxxxx là 5 chữ số (ví dụ: DV00001)";
    public static final String TEN_DON_VI_RONG = "Tên đơn vị không được để trống";
    
    public static final String MA_DON_VI_REGEX = "^DV\\d{5}$";

    public DonViTinh() {
    }

    public DonViTinh(String maDonVi, String tenDonVi) {
        this.maDonVi = maDonVi;
        this.tenDonVi = tenDonVi;
    }

    public String getMaDonVi() {
        return maDonVi;
    }

    public void setMaDonVi(String maDonVi) throws Exception{
        if (maDonVi == null || !maDonVi.matches(MA_DON_VI_REGEX)) {
            throw new Exception(MA_DON_VI_SAI);
        }
        this.maDonVi = maDonVi;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) throws Exception{
        if (tenDonVi == null || tenDonVi.trim().isEmpty()) {
            throw new Exception(TEN_DON_VI_RONG);
        }
        this.tenDonVi = tenDonVi;
    }

    @Override
    public String toString() {
        return "DonViTinh{" +
                "maDonVi='" + maDonVi + '\'' +
                ", tenDonVi='" + tenDonVi + '\'' +
                '}';
    }
}
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

    public DonViTinh() {
    }

    public DonViTinh(String maDonVi, String tenDonVi) {
        this.maDonVi = maDonVi;
        this.tenDonVi = tenDonVi;
    }

    public String getMaDonVi() {
        return maDonVi;
    }

    public void setMaDonVi(String maDonVi) {
        this.maDonVi = maDonVi;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
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
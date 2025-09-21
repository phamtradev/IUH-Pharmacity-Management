/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;

/**
 *
 * @author PhamTra
 */
public class LoHang {

    private String maLoHang;
    private String tenLoHang;
    private LocalDate hanSuDung;
    private int tonKho;
    private boolean trangThai;

    public LoHang() {
    }

    public LoHang(String maLoHang, String tenLoHang, LocalDate hanSuDung, int tonKho, boolean trangThai) {
        this.maLoHang = maLoHang;
        this.tenLoHang = tenLoHang;
        this.hanSuDung = hanSuDung;
        this.tonKho = tonKho;
        this.trangThai = trangThai;
    }

    public String getMaLoHang() {
        return maLoHang;
    }

    public void setMaLoHang(String maLoHang) {
        this.maLoHang = maLoHang;
    }

    public String getTenLoHang() {
        return tenLoHang;
    }

    public void setTenLoHang(String tenLoHang) {
        this.tenLoHang = tenLoHang;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "LoHang{" + "maLoHang=" + maLoHang + ","
                + " tenLoHang=" + tenLoHang + ","
                + " hanSuDung=" + hanSuDung + ","
                + " tonKho=" + tonKho + ","
                + " trangThai=" + trangThai + '}';
    }

}

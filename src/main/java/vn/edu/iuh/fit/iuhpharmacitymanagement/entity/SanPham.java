/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;

import java.util.List;

/**
 *
 * @author PhamTra
 */
public class SanPham {

    private String maSanPham;
    private String tenSanPham;
    private String soDangKy;
    private String hoatChat;
    private String lieuDung;
    private String cachDongGoi;
    private String quocGiaSanXuat;
    private String nhaSanXuat;
    private double giaNhap;
    private double giaBan;
    private boolean hoatDong;
    private double thueVAT = 0.1;
    private String hinhAnh;

    private LoaiSanPham loaiSanPham; // enum riêng
    private List<LoHang> danhSachLoHang;
    private DonViTinh donViTinh;

    public static final String MA_SAN_PHAM_SAI = "Mã sản phẩm phải có dạng SPxxxxx (5 chữ số dương)";
    public static final String TEN_THUOC_RONG = "Tên thuốc không được rỗng";
    public static final String SO_DANG_KY_RONG = "Số đăng ký không được rỗng";
    public static final String HOAT_CHAT_RONG = "Hoạt chất không được rỗng";
    public static final String LIEU_DUNG_RONG = "Liều lượng dùng không được rỗng";
    public static final String DONG_GOI_RONG = "Cách đóng gói không được rỗng";
    public static final String QUOC_GIA_RONG = "Quốc gia sản xuất không được rỗng";
    public static final String NHA_SAN_XUAT_RONG = "Nhà sản xuất không được rỗng";
    public static final String GIA_NHAP_SAI = "Giá nhập phải là số nguyên dương lớn hơn 0";
    public static final String GIA_BAN_SAI = "Giá bán phải là số nguyên dương lớn hơn 0 và phải lớn hơn giá nhập";
    public static final String LOAI_SAN_PHAM_RONG = "Loại sản phẩm không được để trống";

    public static final String MA_SAN_PHAM_REGEX = "^SP\\d{5}$";
    
    public SanPham() {
    }

    public SanPham(String maSanPham, String tenSanPham, String soDangKy, String hoatChat,
            String lieuDung, String cachDongGoi, String quocGiaSanXuat, String nhaSanXuat,
            double giaNhap, double giaBan, boolean hoatDong, String hinhAnh, LoaiSanPham loaiSanPham) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soDangKy = soDangKy;
        this.hoatChat = hoatChat;
        this.lieuDung = lieuDung;
        this.cachDongGoi = cachDongGoi;
        this.quocGiaSanXuat = quocGiaSanXuat;
        this.nhaSanXuat = nhaSanXuat;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.hoatDong = hoatDong;
        this.hinhAnh = hinhAnh;
        this.loaiSanPham = loaiSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) throws Exception {
        if (!maSanPham.matches(MA_SAN_PHAM_REGEX)) {
            throw new Exception(MA_SAN_PHAM_SAI);
        }
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) throws Exception {
        if(tenSanPham.isBlank()){
            throw new Exception(TEN_THUOC_RONG);
        }
        this.tenSanPham = tenSanPham;
    }

    public String getSoDangKy() {
        return soDangKy;
    }

    public void setSoDangKy(String soDangKy) throws Exception{
        if(soDangKy.isBlank()){
            throw new Exception(SO_DANG_KY_RONG);
        }
        this.soDangKy = soDangKy;
    }

    public String getHoatChat() {
        return hoatChat;
    }

    public void setHoatChat(String hoatChat) throws Exception {
        if (hoatChat.isBlank()) {
            throw new Exception(HOAT_CHAT_RONG);
        }
        this.hoatChat = hoatChat;
    }

    public String getLieuDung() {
        return lieuDung;
    }

    public void setLieuDung(String lieuDung) throws Exception {
        if (lieuDung.isBlank()) {
            throw new Exception(LIEU_DUNG_RONG);
        }
        this.lieuDung = lieuDung;
    }

    public String getCachDongGoi() {
        return cachDongGoi;
    }

    public void setCachDongGoi(String cachDongGoi) throws Exception {
        if (cachDongGoi.isBlank()) {
            throw new Exception(DONG_GOI_RONG);
        }
        this.cachDongGoi = cachDongGoi;
    }

    public String getQuocGiaSanXuat() {
        return quocGiaSanXuat;
    }

    public void setQuocGiaSanXuat(String quocGiaSanXuat) throws Exception {
        if (quocGiaSanXuat == null || quocGiaSanXuat.isBlank()) {
            throw new Exception(QUOC_GIA_RONG);
        }
        this.quocGiaSanXuat = quocGiaSanXuat;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) throws Exception {
        if (nhaSanXuat == null || nhaSanXuat.isBlank()) {
            throw new Exception(NHA_SAN_XUAT_RONG);
        }
        this.nhaSanXuat = nhaSanXuat;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) throws Exception {
        if (giaNhap <= 0) {
            throw new Exception(GIA_NHAP_SAI);
        }
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) throws Exception {
        if (giaBan <= 0 || giaBan <= this.giaNhap) {
            throw new Exception(GIA_BAN_SAI);
        }
        this.giaBan = giaBan;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }
    
     public String trangThaiBan() {
        return hoatDong ? "Đang bán" : "Ngưng bán";
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = 0.1; // thuế sp là 10%
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) throws Exception{
        if (loaiSanPham == null) {
            throw new Exception(LOAI_SAN_PHAM_RONG);
        }
        this.loaiSanPham = loaiSanPham;
    }

    public List<LoHang> getDanhSachLoHang() {
        return danhSachLoHang;
    }

    public void setDanhSachLoHang(List<LoHang> danhSachLoHang) {
        this.danhSachLoHang = danhSachLoHang;
    }

    public DonViTinh getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }

    @Override
    public String toString() {
        return "SanPham{"
                + "maSanPham='" + maSanPham + '\''
                + ", tenSanPham='" + tenSanPham + '\''
                + ", soDangKy='" + soDangKy + '\''
                + ", hoatChat='" + hoatChat + '\''
                + ", lieuDung='" + lieuDung + '\''
                + ", cachDongGoi='" + cachDongGoi + '\''
                + ", quocGiaSanXuat='" + quocGiaSanXuat + '\''
                + ", nhaSanXuat='" + nhaSanXuat + '\''
                + ", giaNhap=" + giaNhap
                + ", giaBan=" + giaBan
                + ", trangThai='" + trangThaiBan() + '\''
                + ", hoatDong=" + hoatDong
                + ", thueVAT=" + thueVAT
                + ", hinhAnh='" + hinhAnh + '\''
                + ", loaiSanPham=" + loaiSanPham
                + '}';
    }
}

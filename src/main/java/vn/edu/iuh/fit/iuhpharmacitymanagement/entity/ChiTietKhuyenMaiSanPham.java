/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietKhuyenMaiSanPham {

    private String maChiTietKhuyenMaiSanPham;
    private SanPham sanPham;
    private KhuyenMai khuyenMai;

    public static final String MA_CHI_TIET_KHUYEN_MAI_SAN_PHAM_SAI = "Mã chi tiết khuyến mãi sản phẩm không hợp lệ";
    public static final String SAN_PHAM_SAI = "Sản phẩm không hợp lệ";
    public static final String KHUYEN_MAI_SAI = "Khuyến mãi không hợp lệ";

    public ChiTietKhuyenMaiSanPham() {
    }

    public ChiTietKhuyenMaiSanPham(String maChiTietKhuyenMaiSanPham, SanPham sanPham, KhuyenMai khuyenMai) {
        this.maChiTietKhuyenMaiSanPham = maChiTietKhuyenMaiSanPham;
        this.sanPham = sanPham;
        this.khuyenMai = khuyenMai;
    }

    public String getMaChiTietKhuyenMaiSanPham() {
        return maChiTietKhuyenMaiSanPham;
    }

    public void setMaChiTietKhuyenMaiSanPham(String maChiTietKhuyenMaiSanPham) throws Exception{
        if(maChiTietKhuyenMaiSanPham == null){
            throw new Exception(MA_CHI_TIET_KHUYEN_MAI_SAN_PHAM_SAI);
        }
        this.maChiTietKhuyenMaiSanPham = maChiTietKhuyenMaiSanPham;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) throws Exception{
        if(sanPham == null){
            throw new Exception(SAN_PHAM_SAI);
        }
        this.sanPham = sanPham;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) throws Exception{
        if(khuyenMai == null){
            throw new Exception(KHUYEN_MAI_SAI);
        }
        this.khuyenMai = khuyenMai;
    }

    @Override
    public String toString() {
        return "ChiTietKhuyenMaiSanPham{"
                + "maChiTietKhuyenMaiSanPham='" + maChiTietKhuyenMaiSanPham + '\''
                + ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : null)
                + ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKhuyenMai() : null)
                + '}';
    }
}

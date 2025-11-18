package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 * @author PhamTra Chi tiết hàng hỏng - Composite key (maLoHang + maHangHong)
 */
public class ChiTietHangHong {

    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String lyDoXuatHuy; // Lý do xuất hủy

    private LoHang loHang;
    private HangHong hangHong;

    public static final String SO_LUONG_SAI = "Số lượng phải là số nguyên dương lớn hơn 0, không được rỗng";
    public static final String DON_GIA_SAI = "Đơn giá phải là số thực dương lớn hơn 0, không được rỗng";
    public static final String LO_HANG_RONG = "Lô hàng không được để trống";
    public static final String HANG_HONG_RONG = "Hàng hỏng không được để trống";

    public ChiTietHangHong() {
    }

    public ChiTietHangHong(int soLuong, double donGia, double thanhTien, LoHang loHang, HangHong hangHong) {
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.loHang = loHang;
        this.hangHong = hangHong;
        this.lyDoXuatHuy = "";
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) throws Exception {
        if (soLuong <= 0) {
            throw new Exception(SO_LUONG_SAI);
        }
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) throws Exception {
        if (donGia <= 0) {
            throw new Exception(DON_GIA_SAI);
        }
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = this.soLuong * this.donGia;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) throws Exception {
        if (loHang == null) {
            throw new Exception(LO_HANG_RONG);
        }
        this.loHang = loHang;
    }

    public HangHong getHangHong() {
        return hangHong;
    }

    public void setHangHong(HangHong hangHong) throws Exception {
        if (hangHong == null) {
            throw new Exception(HANG_HONG_RONG);
        }
        this.hangHong = hangHong;
    }

    public String getLyDoXuatHuy() {
        return lyDoXuatHuy;
    }

    public void setLyDoXuatHuy(String lyDoXuatHuy) {
        this.lyDoXuatHuy = lyDoXuatHuy;
    }

    @Override
    public String toString() {
        return "ChiTietHangHong{" + "soLuong=" + soLuong + ", donGia=" + donGia + ", thanhTien=" + thanhTien + ", loHang=" + (loHang != null ? loHang.getMaLoHang() : "null") + ", hangHong=" + (hangHong != null ? hangHong.getMaHangHong() : "null") + ", lyDoXuatHuy=" + lyDoXuatHuy + '}';
    }

}

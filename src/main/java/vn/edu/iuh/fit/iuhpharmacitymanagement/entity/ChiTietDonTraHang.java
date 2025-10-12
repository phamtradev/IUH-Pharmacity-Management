package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietDonTraHang {
    private String maChiTietDonTraHang;
    private int soLuong;
    private double donGia;
    private String lyDoTra;
    private double thanhTien;
    private SanPham sanPham;
    private DonTraHang donTraHang;

    public ChiTietDonTraHang() {
    }

    public ChiTietDonTraHang(String maChiTietDonTraHang, int soLuong, double donGia, String lyDoTra, double thanhTien,
            SanPham sanPham, DonTraHang donTraHang) {
        this.maChiTietDonTraHang = maChiTietDonTraHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.lyDoTra = lyDoTra;
        this.thanhTien = thanhTien;
        this.sanPham = sanPham;
        this.donTraHang = donTraHang;
    }

    public String getMaChiTietDonTraHang() {
        return maChiTietDonTraHang;
    }

    public void setMaChiTietDonTraHang(String maChiTietDonTraHang) {
        this.maChiTietDonTraHang = maChiTietDonTraHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getLyDoTra() {
        return lyDoTra;
    }

    public void setLyDoTra(String lyDoTra) {
        this.lyDoTra = lyDoTra;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public DonTraHang getDonTraHang() {
        return donTraHang;
    }

    public void setDonTraHang(DonTraHang donTraHang) {
        this.donTraHang = donTraHang;
    }

    @Override
    public String toString() {
        return "ChiTietDonTraHang{" +
                "maChiTietDonTraHang='" + maChiTietDonTraHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", lyDoTra='" + lyDoTra + '\'' +
                ", thanhTien=" + thanhTien +
                ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : null) +
                ", donTraHang=" + (donTraHang != null ? donTraHang.getMaDonTraHang() : null) +
                '}';
    }
}
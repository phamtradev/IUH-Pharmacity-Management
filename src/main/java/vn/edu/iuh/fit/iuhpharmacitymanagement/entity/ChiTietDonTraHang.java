package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietDonTraHang {
    // private String maChiTietDonTraHang;
    private int soLuong;
    private double donGia;
    private String lyDoTra;
    private double thanhTien;
    private String trangThaiXuLy;
    private SanPham sanPham;
    private DonTraHang donTraHang;

    public static final String MA_CHI_TIET_DON_TRA_HANG_RONG = "Mã chi tiết đơn trả hàng không được rỗng";
    public static final String SO_LUONG_SAI = "Số lượng phải lớn hơn 0, không được rỗng";
    public static final String DON_GIA_SAI = "Đơn giá phải lớn hơn 0, không được rỗng";
    public static final String LY_DO_TRA_RONG = "Lý do trả không được rỗng";
    public static final String THANH_TIEN_SAI = "Thành tiền không hợp lệ";
    public static final String SAN_PHAM_SAI = "Sản phẩm không hợp lệ";
    public static final String DON_TRA_HANG_SAI = "Đơn trả hàng không được rỗng";

    public ChiTietDonTraHang() {
        this.trangThaiXuLy = "Chưa xử lý"; // Mặc định
    }

    public ChiTietDonTraHang(int soLuong, double donGia, String lyDoTra, double thanhTien,
            SanPham sanPham, DonTraHang donTraHang) {
        // this.maChiTietDonTraHang = maChiTietDonTraHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.lyDoTra = lyDoTra;
        this.thanhTien = thanhTien;
        this.trangThaiXuLy = "Chưa xử lý"; // Mặc định
        this.sanPham = sanPham;
        this.donTraHang = donTraHang;
    }

    // public String getMaChiTietDonTraHang() {
    // return maChiTietDonTraHang;
    // }
    //
    // public void setMaChiTietDonTraHang(String maChiTietDonTraHang) throws
    // Exception{
    // if(maChiTietDonTraHang == null){
    // throw new Exception(MA_CHI_TIET_DON_TRA_HANG_RONG);
    // }
    // this.maChiTietDonTraHang = maChiTietDonTraHang;
    // }

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

    public String getLyDoTra() {
        return lyDoTra;
    }

    public void setLyDoTra(String lyDoTra) throws Exception {
        if (lyDoTra == null) {
            throw new Exception(LY_DO_TRA_RONG);
        }
        this.lyDoTra = lyDoTra;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) throws Exception {
        if (thanhTien <= 0) {
            throw new Exception(THANH_TIEN_SAI);
        }
        this.thanhTien = thanhTien;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) throws Exception {
        if (sanPham == null) {
            throw new Exception(SAN_PHAM_SAI);
        }
        this.sanPham = sanPham;
    }

    public DonTraHang getDonTraHang() {
        return donTraHang;
    }

    public void setDonTraHang(DonTraHang donTraHang) throws Exception {
        if (donTraHang == null) {
            throw new Exception(DON_TRA_HANG_SAI);
        }
        this.donTraHang = donTraHang;
    }

    public String getTrangThaiXuLy() {
        return trangThaiXuLy;
    }

    public void setTrangThaiXuLy(String trangThaiXuLy) {
        this.trangThaiXuLy = trangThaiXuLy;
    }

    @Override
    public String toString() {
        return "ChiTietDonTraHang{" +
        // "maChiTietDonTraHang='" + maChiTietDonTraHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", lyDoTra='" + lyDoTra + '\'' +
                ", thanhTien=" + thanhTien +
                ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : null) +
                ", donTraHang=" + (donTraHang != null ? donTraHang.getMaDonTraHang() : null) +
                '}';
    }
}
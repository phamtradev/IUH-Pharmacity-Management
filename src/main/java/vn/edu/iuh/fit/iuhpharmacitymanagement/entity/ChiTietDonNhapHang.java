package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietDonNhapHang {
    private String maChiTietDonNhapHang;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private DonNhapHang donNhapHang;
    private LoHang loHang;
    
    public static final String MA_CHI_TIET_DON_NHAP_HANG_RONG = "Mã chi tiết đơn nhập hàng không được rỗng";
    public static final String SO_LUONG_SAI = "Số lượng không hợp lệ";
    public static final String DON_GIA_SAI = "Đơn giá không hợp lệ";
    public static final String THANH_TIEN_SAI = "Thành tiền không hợp lệ";
    public static final String DON_NHAP_HANG_RONG = "Đơn nhập hàng không được rỗng";
    public static final String LO_HANG_RONG = "Lô hàng không được rỗng";
    
    public ChiTietDonNhapHang() {
    }

    public ChiTietDonNhapHang(String maChiTietDonNhapHang, int soLuong, double donGia, double thanhTien, DonNhapHang donNhapHang, LoHang loHang) {
        this.maChiTietDonNhapHang = maChiTietDonNhapHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.donNhapHang = donNhapHang;
        this.loHang = loHang;
    }

    public String getMaChiTietDonNhapHang() {
        return maChiTietDonNhapHang;
    }

    public void setMaChiTietDonNhapHang(String maChiTietDonNhapHang) throws Exception{
        if(maChiTietDonNhapHang == null){
            throw new Exception(MA_CHI_TIET_DON_NHAP_HANG_RONG);
        }
        this.maChiTietDonNhapHang = maChiTietDonNhapHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) throws Exception{
        if(soLuong < 0){
            throw new Exception(SO_LUONG_SAI);
        }
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) throws Exception{
        if(donGia < 0){
            throw new Exception(DON_GIA_SAI);
        }
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) throws Exception{
        if(thanhTien <= 0){
            throw new Exception(THANH_TIEN_SAI);
        }
        this.thanhTien = thanhTien;
    }

    public DonNhapHang getDonNhapHang() {
        return donNhapHang;
    }

    public void setDonNhapHang(DonNhapHang donNhapHang) throws Exception{
        if(donNhapHang == null){
            throw new Exception(DON_NHAP_HANG_RONG);
        }
        this.donNhapHang = donNhapHang;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) throws Exception{
        if(loHang == null){
            throw new Exception(LO_HANG_RONG);
        }
        this.loHang = loHang;
    }

    @Override
    public String toString() {
        return "ChiTietDonNhapHang{" +
                "maChiTietDonNhapHang='" + maChiTietDonNhapHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                ", donNhapHang=" + (donNhapHang != null ? donNhapHang.getMaDonNhapHang() : null) +
                ", loHang=" + (loHang != null ? loHang.getMaLoHang() : null) +
                '}';
    }
}
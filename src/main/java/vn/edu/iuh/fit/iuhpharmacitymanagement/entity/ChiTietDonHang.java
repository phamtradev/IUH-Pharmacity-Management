package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

public class ChiTietDonHang {

    private String maChiTietDonHang;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private double giamGia;
    private LoHang loHang;
    private DonHang donHang;

    public static final String MA_CHI_TIET_DON_HANG_RONG = "Mã chi tiết đơn hàng không được rỗng";
    public static final String SO_LUONG_SAI = "Số lượng không hợp lệ (phải > 0)";
    public static final String DON_GIA_SAI = "Đơn giá không hợp lệ (phải > 0)";
    public static final String THANH_TIEN_SAI = "Thành tiền không hợp lệ (phải > 0)";
    public static final String GIAM_GIA_SAI = "Giảm giá không hợp lệ (phải >= 0)";
    public static final String LO_HANG_SAI = "Lô hàng không hợp lệ (không được null)";
    public static final String DON_HANG_SAI = "Đơn hàng không hợp lệ (không được null)";

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(String maChiTietDonHang,
                          int soLuong,
                          double donGia,
                          double thanhTien,
                          double giamGia,
                          LoHang loHang,
                          DonHang donHang) throws Exception {
        this.maChiTietDonHang = maChiTietDonHang;
        setSoLuong(soLuong);
        setDonGia(donGia);
        setThanhTien(thanhTien);
        setGiamGia(giamGia);
        setLoHang(loHang);
        setDonHang(donHang);
    }

    public String getMaChiTietDonHang() {
        return maChiTietDonHang;
    }

    public void setMaChiTietDonHang(String maChiTietDonHang) throws Exception{
        if(maChiTietDonHang == null){
            throw new Exception(MA_CHI_TIET_DON_HANG_RONG);
        }
        this.maChiTietDonHang = maChiTietDonHang;
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

    public void setThanhTien(double thanhTien) throws Exception {
        if (thanhTien <= 0) {
            throw new Exception(THANH_TIEN_SAI);
        }
        this.thanhTien = thanhTien;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) throws Exception {
        if (giamGia < 0) {
            throw new Exception(GIAM_GIA_SAI);
        }
        this.giamGia = giamGia;
    }

    public LoHang getLoHang() {
        return loHang;
    }

    public void setLoHang(LoHang loHang) throws Exception {
        if (loHang == null) {
            throw new Exception(LO_HANG_SAI);
        }
        this.loHang = loHang;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) throws Exception {
        if (donHang == null) {
            throw new Exception(DON_HANG_SAI);
        }
        this.donHang = donHang;
    }

    @Override
    public String toString() {
        return "ChiTietDonHang{" +
                "maChiTietDonHang='" + maChiTietDonHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                ", giamGia=" + giamGia +
                ", loHang=" + (loHang != null ? loHang.getMaLoHang() : "null") +
                ", donHang=" + (donHang != null ? donHang.getMaDonHang() : "null") +
                '}';
    }
}

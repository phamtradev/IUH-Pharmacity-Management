package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

public class ChiTietDonHang {

    //private String maChiTietDonHang;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private double giamGiaSanPham;  // Giảm giá sản phẩm (từ khuyến mãi sản phẩm)
    private double giamGiaHoaDonPhanBo;  // Giảm giá hóa đơn phân bổ cho sản phẩm này
    private LoHang loHang;
    private DonHang donHang; 

    public static final String MA_CHI_TIET_DON_HANG_RONG = "Mã chi tiết đơn hàng không được rỗng";
    public static final String SO_LUONG_SAI = "Số lượng không hợp lệ phải lớn hơn 0, không được rỗng";
    public static final String DON_GIA_SAI = "Đơn giá không hợp lệ phải lớn hơn 0, không được rỗng";
    public static final String THANH_TIEN_SAI = "Thành tiền không hợp lệ phải lớn hơn 0, không được rỗng";
    public static final String GIAM_GIA_SAI = "Giảm giá không hợp lệ phải lớn hơn 0";
    public static final String GIAM_GIA_HOA_DON_PHAN_BO_SAI = "Giảm giá hóa đơn phân bổ không hợp lệ phải lớn hơn hoặc bằng 0";
    public static final String LO_HANG_SAI = "Lô hàng không hợp lệ (không được null)";
    public static final String DON_HANG_SAI = "Đơn hàng không hợp lệ (không được null)";

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(
                          int soLuong,
                          double donGia,
                          double thanhTien,
                          double giamGiaSanPham,
                          LoHang loHang,
                          DonHang donHang) throws Exception {
        //this.maChiTietDonHang = maChiTietDonHang;
        setSoLuong(soLuong);
        setDonGia(donGia);
        setThanhTien(thanhTien);
        setGiamGiaSanPham(giamGiaSanPham);
        setLoHang(loHang);
        setDonHang(donHang);
    }


//    public String getMaChiTietDonHang() {
//        return maChiTietDonHang;
//    }
//
//    public void setMaChiTietDonHang(String maChiTietDonHang) throws Exception{
//        if(maChiTietDonHang == null){
//            throw new Exception(MA_CHI_TIET_DON_HANG_RONG);
//        }
//        this.maChiTietDonHang = maChiTietDonHang;
//    }

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

    public double getGiamGiaSanPham() {
        return giamGiaSanPham;
    }

    public void setGiamGiaSanPham(double giamGiaSanPham) throws Exception {
        if (giamGiaSanPham < 0) {
            throw new Exception(GIAM_GIA_SAI);
        }
        this.giamGiaSanPham = giamGiaSanPham;
    }

    public double getGiamGiaHoaDonPhanBo() {
        return giamGiaHoaDonPhanBo;
    }

    public void setGiamGiaHoaDonPhanBo(double giamGiaHoaDonPhanBo) throws Exception {
        if (giamGiaHoaDonPhanBo < 0) {
            throw new Exception(GIAM_GIA_HOA_DON_PHAN_BO_SAI);
        }
        this.giamGiaHoaDonPhanBo = giamGiaHoaDonPhanBo;
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
//                "maChiTietDonHang='" + maChiTietDonHang + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                ", giamGiaSanPham=" + giamGiaSanPham +
                ", giamGiaHoaDonPhanBo=" + giamGiaHoaDonPhanBo +
                ", loHang=" + (loHang != null ? loHang.getMaLoHang() : "null") +
                ", donHang=" + (donHang != null ? donHang.getMaDonHang() : "null") +
                '}';
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

/**
 *
 * @author PhamTra
 */
public class ChiTietDonNhapHang {

    //private String maChiTietDonNhapHang;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private DonNhapHang donNhapHang;
    private LoHang loHang;

    private double tyLeChietKhau;
    private double tienChietKhau; //dẫn xuất
    private double thanhTienTinhThue; //dẫn xuất
    private double thueSuat;
    private double tienThue; //dẫn xuất
    private double thanhTienSauThue;

    public static final String MA_CHI_TIET_DON_NHAP_HANG_RONG = "Mã chi tiết đơn nhập hàng không được rỗng";
    public static final String SO_LUONG_SAI = "Số lượng không hợp lệ phải lớn hơn 0, không được rỗng";
    public static final String DON_GIA_SAI = "Đơn giá không hợp lệ phải lớn hơn hoặc bằng 0";
    public static final String THANH_TIEN_SAI = "Thành tiền không hợp lệ phải lớn hơn hoặc bằng 0";
    public static final String DON_NHAP_HANG_RONG = "Đơn nhập hàng không được rỗng";
    public static final String LO_HANG_RONG = "Lô hàng không được rỗng";

    public ChiTietDonNhapHang() {
    }

    //ok
    public ChiTietDonNhapHang(int soLuong, double donGia, double thanhTien, DonNhapHang donNhapHang, LoHang loHang) {
        // this.maChiTietDonNhapHang = maChiTietDonNhapHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.donNhapHang = donNhapHang;
        this.loHang = loHang;
    }

//    public String getMaChiTietDonNhapHang() {
//        return maChiTietDonNhapHang;
//    }
//
//    public void setMaChiTietDonNhapHang(String maChiTietDonNhapHang) throws Exception{
//        if(maChiTietDonNhapHang == null){
//            throw new Exception(MA_CHI_TIET_DON_NHAP_HANG_RONG);
//        }
//        this.maChiTietDonNhapHang = maChiTietDonNhapHang;
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
        if (donGia < 0) {
            throw new Exception(DON_GIA_SAI);
        }
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) throws Exception {
        if (thanhTien < 0) {
            throw new Exception(THANH_TIEN_SAI);
        }
        this.thanhTien = thanhTien;
    }

    public double getTyLeChietKhau() {
        return tyLeChietKhau;
    }

    public void setTyLeChietKhau(double tyLeChietKhau) throws Exception {
        if (tyLeChietKhau < 0 || tyLeChietKhau > 100) {
            throw new Exception("Phần trăm chiết khấu không hợp lệ (0 - 100)");
        }
        this.tyLeChietKhau = tyLeChietKhau;
    }

    public double getTienChietKhau() {
        return tienChietKhau;
    }

    public void setTienChietKhau(double tienChietKhau) throws Exception {
        if (tienChietKhau < 0) {
            throw new Exception("Tiền chiết khấu không hợp lệ");
        }
        this.tienChietKhau = tienChietKhau;
    }

    public double getThanhTienTinhThue() {
        return thanhTienTinhThue;
    }

    public void setThanhTienTinhThue(double thanhTienTinhThue) throws Exception {
        if (thanhTienTinhThue < 0) {
            throw new Exception("Thành tiền tính thuế không hợp lệ");
        }
        this.thanhTienTinhThue = thanhTienTinhThue;
    }

    public double getThueSuat() {
        return thueSuat;
    }

    public void setThueSuat(double thueSuat) throws Exception {
        if (thueSuat < 0 || thueSuat > 100) {
            throw new Exception("Phần trăm thuế không hợp lệ (0 - 100)");
        }
        this.thueSuat = thueSuat;
    }

    public double getTienThue() {
        return tienThue;
    }

    public void setTienThue(double tienThue) throws Exception {
        if (tienThue < 0) {
            throw new Exception("Tiền thuế không hợp lệ");
        }
        this.tienThue = tienThue;
    }

    public double getThanhTienSauThue() {
        return thanhTienSauThue;
    }

    public void setThanhTienSauThue(double thanhTienSauThue) throws Exception {
        if (thanhTienSauThue < 0) {
            throw new Exception("Thành tiền sau thuế không hợp lệ");
        }
        this.thanhTienSauThue = thanhTienSauThue;
    }

    public DonNhapHang getDonNhapHang() {
        return donNhapHang;
    }

    public void setDonNhapHang(DonNhapHang donNhapHang) throws Exception {
        if (donNhapHang == null) {
            throw new Exception(DON_NHAP_HANG_RONG);
        }
        this.donNhapHang = donNhapHang;
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

    @Override
    public String toString() {
        return "ChiTietDonNhapHang{"
                + //"maChiTietDonNhapHang='" + maChiTietDonNhapHang + '\'' +
                ", soLuong=" + soLuong
                + ", donGia=" + donGia
                + ", thanhTien=" + thanhTien
                + ", tyLeChietKhau=" + tyLeChietKhau
                + ", tienChietKhau=" + tienChietKhau
                + ", thanhTienTinhThue=" + thanhTienTinhThue
                + ", thueSuat=" + thueSuat
                + ", tienThue=" + tienThue
                + ", thanhTienSauThue=" + thanhTienSauThue
                + ", donNhapHang=" + (donNhapHang != null ? donNhapHang.getMaDonNhapHang() : null)
                + ", loHang=" + (loHang != null ? loHang.getMaLoHang() : null)
                + '}';
    }
}

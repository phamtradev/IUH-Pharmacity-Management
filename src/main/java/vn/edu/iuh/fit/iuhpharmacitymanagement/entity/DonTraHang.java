/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author PhamTra
 */
public class DonTraHang {

    private String maDonTraHang;
    private LocalDate ngayTraHang;
    private double thanhTien;
    private NhanVien nhanVien;
    private DonHang donHang;
    private List<ChiTietDonTraHang> chiTietDonTraHang;
    private String trangThaiXuLy;
    public static final String MA_DON_TRA_HANG_SAI = "Mã đơn trả hàng phải bắt đầu bằng DTddMMyyyyxxxx (dd là ngày trả, mm là tháng trả, yyyy là năm trả, xxxx là 4 số dương)";
    private static final String REGEX_MA_DON_TRA_HANG
            = "^DT(0[1-9]|[12][0-9]|3[01])"
            + "(0[1-9]|1[0-2])"
            + "(20)\\d{2}"
            + "(?!0000)\\d{4}$";
    public static final String NGAY_TRA_HANG_SAI = "Ngày trả hàng phải nhỏ hơn hoặc bằng ngày hiện tại";
    public static final String NGAY_TRA_HANG_NULL = "Ngày trả hàng không được rỗng";

    public DonTraHang() {
        this.trangThaiXuLy = "Chưa xử lý";
    }

    public DonTraHang(String maDonTra, LocalDate ngayTraHang) {
        this.maDonTraHang = maDonTra;
        this.ngayTraHang = ngayTraHang;
        this.trangThaiXuLy = "Chưa xử lý";
    }

    public DonTraHang(String maDonTraHang) {
        this.maDonTraHang = maDonTraHang;
        this.trangThaiXuLy = "Chưa xử lý";
    }

    public String getMaDonTraHang() {
        return maDonTraHang;
    }

    public void setMaDonTraHang(String maDonTraHang) throws Exception {
        if (maDonTraHang == null || maDonTraHang.isBlank() || !maDonTraHang.matches(REGEX_MA_DON_TRA_HANG)) {
            throw new Exception(MA_DON_TRA_HANG_SAI);
        }
        this.maDonTraHang = maDonTraHang;
    }

    public LocalDate getNgayTraHang() {
        return ngayTraHang;
    }

    public void setNgayTraHang(LocalDate ngayTraHang) throws Exception {
        if (ngayTraHang == null) {
            throw new Exception(NGAY_TRA_HANG_NULL);
        }
        LocalDate hientai = LocalDate.now();
        if (ngayTraHang.isAfter(hientai)) {
            throw new Exception(NGAY_TRA_HANG_SAI);
        }
        this.ngayTraHang = ngayTraHang;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) throws Exception {
        if (thanhTien <= 0) {
            throw new Exception("thành tiền phải lớn hơn 0, không được rỗng");
        }
        this.thanhTien = thanhTien;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) throws Exception {
        if (nhanVien == null) {
            throw new Exception("nhân viên không được để rỗng");
        }
        this.nhanVien = nhanVien;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) throws Exception {
        if (donHang == null) {
            throw new Exception("đơn hàng không được để rỗng");
        }

        this.donHang = donHang;
    }

    public List<ChiTietDonTraHang> getChiTietDonTraHang() {
        return chiTietDonTraHang;
    }

    public void setChiTietDonTraHang(List<ChiTietDonTraHang> chiTietDonTraHang) {
        this.chiTietDonTraHang = chiTietDonTraHang;
    }

    public String getTrangThaiXuLy() {
        return trangThaiXuLy;
    }

    public void setTrangThaiXuLy(String trangThaiXuLy) {
        this.trangThaiXuLy = trangThaiXuLy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDonTraHang, ngayTraHang);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DonTraHang other = (DonTraHang) obj;
        return Objects.equals(maDonTraHang, other.maDonTraHang)
                && Objects.equals(ngayTraHang, other.ngayTraHang);
    }

    @Override
    public String toString() {
        return "DonTraHang{"
                + "maDonTra='" + maDonTraHang + '\''
                + ", ngayTraHang=" + ngayTraHang
                + ", thanhTien=" + thanhTien
                + ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : null)
                + ", donHang=" + (donHang != null ? donHang.getMaDonHang() : null)
                + ", chiTietDonTraHang=" + (chiTietDonTraHang != null ? chiTietDonTraHang.size() : 0)
                + '}';
    }
}

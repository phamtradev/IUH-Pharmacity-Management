/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;

import java.time.LocalDate;

/**
 *
 * @author PhamTra
 */
public class KhuyenMai {

    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private double giamGia;
    private boolean trangThai;
    private LoaiKhuyenMai loaiKhuyenMai;
    private double giaToiThieu; // Giá tối thiểu đơn hàng để áp dụng khuyến mãi (chỉ dùng cho khuyến mãi đơn hàng)
    private double giaToiDa; // Giá giảm tối đa khi áp dụng khuyến mãi (0 = không giới hạn)
    private int soLuongToiDa; //  ap dung cho khuyen mai theo san pham

    public static final String MA_KHUYEN_MAI_SAI = "Mã khuyến mãi phải có dạng KMXXXXX (trong đó XXXXX là dãy số từ 00001 đến 99999)";
    public static final String TEN_KHUYEN_MAI_RONG = "Tên khuyến mãi không được để trống";
    public static final String NGAY_BAT_DAU_SAI = "Ngày bắt đầu phải lớn hơn ngày hiện tại";
    public static final String NGAY_KET_THUC_SAI = "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu";
    public static final String GIAM_GIA_SAI = "Giảm giá phải là số thực dương lớn hơn 0";
    public static final String LOAI_KHUYEN_MAI_RONG = "Loại khuyến mãi không được để trống";

    public static final String MA_KHUYEN_MAI_REGEX = "^KM\\d{5}$";

    public KhuyenMai() {
    }

    public KhuyenMai(String maKhuyenMai,
            String tenKhuyenMai,
            LocalDate ngayBatDau,
            LocalDate ngayKetThuc,
            double giamGia,
            boolean trangThai,
            LoaiKhuyenMai loaiKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giamGia = giamGia;
        this.trangThai = trangThai;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.giaToiThieu = 0; // Mặc định 0 (không yêu cầu giá tối thiểu)
        this.giaToiDa = 0; // Mặc định 0 (không giới hạn giảm giá tối đa)
        this.soLuongToiDa = 0; // Mặc định 0 (không giới hạn số lượng tối đa)
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) throws Exception {
        if (!maKhuyenMai.matches(MA_KHUYEN_MAI_REGEX)) {
            throw new Exception(MA_KHUYEN_MAI_SAI);
        }
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) throws Exception {
        if (tenKhuyenMai.isBlank()) {
            throw new Exception(TEN_KHUYEN_MAI_RONG);
        }
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    //tam go de test
    public void setNgayBatDau(LocalDate ngayBatDau) throws Exception {

        this.ngayBatDau = ngayBatDau;
    }

    // Method không validate - chỉ dùng cho DAO khi load từ database
    public void setNgayBatDauFromDatabase(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) throws Exception {
        if (this.ngayBatDau != null && ngayKetThuc.isBefore(this.ngayBatDau)) {
            throw new Exception(NGAY_KET_THUC_SAI);
        }
        this.ngayKetThuc = ngayKetThuc;
    }

    // Method không validate - chỉ dùng cho DAO khi load từ database
    public void setNgayKetThucFromDatabase(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(double giamGia) throws Exception {
        if (giamGia <= 0) {
            throw new Exception(GIAM_GIA_SAI);
        }
        this.giamGia = giamGia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String hienThiTrangThai() {
        return trangThai ? "Còn hạn khuyến mãi" : "Hết hạn khuyến mãi";
    }

    public LoaiKhuyenMai getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public void setLoaiKhuyenMai(LoaiKhuyenMai loaiKhuyenMai) throws Exception {
        if (loaiKhuyenMai == null) {
            throw new Exception(LOAI_KHUYEN_MAI_RONG);
        }
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public double getGiaToiThieu() {
        return giaToiThieu;
    }

    public void setGiaToiThieu(double giaToiThieu) {
        // Giá tối thiểu phải >= 0 (0 = không yêu cầu giá tối thiểu)
        if (giaToiThieu < 0) {
            this.giaToiThieu = 0;
        } else {
            this.giaToiThieu = giaToiThieu;
        }
    }

    public double getGiaToiDa() {
        return giaToiDa;
    }

    public void setGiaToiDa(double giaToiDa) {
        // Giá tối đa phải >= 0 (0 = không giới hạn giảm giá tối đa)
        if (giaToiDa < 0) {
            this.giaToiDa = 0;
        } else {
            this.giaToiDa = giaToiDa;
        }
    }

    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(int soLuongToiDa) {
        // Số lượng tối đa phải >= 0 (0 = không giới hạn số lượng tối đa)
        if (soLuongToiDa < 0) {
            this.soLuongToiDa = 0;
        } else {
            this.soLuongToiDa = soLuongToiDa;
        }
    }

    @Override
    public String toString() {
        return "KhuyenMai{"
                + "maKhuyenMai='" + maKhuyenMai + '\''
                + ", tenKhuyenMai='" + tenKhuyenMai + '\''
                + ", ngayBatDau=" + ngayBatDau
                + ", ngayKetThuc=" + ngayKetThuc
                + ", giamGia=" + giamGia
                + ", trangThai=" + hienThiTrangThai()
                + ", loaiKhuyenMai=" + loaiKhuyenMai
                + ", giaToiThieu=" + giaToiThieu
                + ", giaToiDa=" + giaToiDa
                + ", soLuongToiDa=" + soLuongToiDa
                + '}';
    }
}

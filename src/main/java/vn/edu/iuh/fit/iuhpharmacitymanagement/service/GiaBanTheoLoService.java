package vn.edu.iuh.fit.iuhpharmacitymanagement.service;

import java.util.List;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.BangLaiChuanBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.BangLaiChuanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 * Tính đơn giá bán theo từng lô dựa trên giá nhập thực tế và cấu hình lãi chuẩn.
 * Giá trả về đã bao gồm VAT để dùng trực tiếp cho hiển thị và lưu đơn hàng.
 */
public class GiaBanTheoLoService {

    private final BangLaiChuanBUS bangLaiChuanBUS;
    private List<BangLaiChuan> cachedCauHinh;

    public GiaBanTheoLoService() {
        this(new BangLaiChuanBUS(new BangLaiChuanDAO()));
    }

    public GiaBanTheoLoService(BangLaiChuanBUS bangLaiChuanBUS) {
        this.bangLaiChuanBUS = bangLaiChuanBUS;
    }

    /**
     * Làm mới cache cấu hình lãi (dùng khi người dùng thay đổi cấu hình ở màn khác).
     */
    public void refreshCauHinh() {
        cachedCauHinh = null;
    }

    /**
     * Tính đơn giá CHƯA VAT cho 1 lô.
     */
    public double tinhDonGiaChuaVAT(LoHang loHang, SanPham sanPham) {
        if (sanPham == null) {
            return 0;
        }
        double giaNhap = 0;
        if (loHang != null && loHang.getGiaNhapLo() > 0) {
            giaNhap = loHang.getGiaNhapLo();
        } else if (sanPham.getGiaNhap() > 0) {
            giaNhap = sanPham.getGiaNhap();
        }

        if (giaNhap <= 0) {
            return sanPham.getGiaBan();
        }

        double tyLeLai = timTyLeLaiTheoCauHinh(giaNhap, sanPham.getLoaiSanPham());
        if (tyLeLai <= 0) {
            double giaBan = sanPham.getGiaBan();
            return giaBan > 0 ? giaBan : giaNhap;
        }
        return giaNhap * (1 + tyLeLai);
    }

    /**
     * Tính đơn giá ĐÃ VAT cho 1 lô (dùng để hiển thị và lưu hóa đơn).
     */
    public double tinhDonGiaCoVAT(LoHang loHang, SanPham sanPham) {
        double donGiaChuaVAT = tinhDonGiaChuaVAT(loHang, sanPham);
        if (sanPham == null) {
            return donGiaChuaVAT;
        }
        double vat = sanPham.getThueVAT();
        return donGiaChuaVAT * (1 + Math.max(0, vat));
    }

    private List<BangLaiChuan> getCauHinhLai() {
        if (cachedCauHinh == null) {
            cachedCauHinh = bangLaiChuanBUS.layTatCa();
        }
        return cachedCauHinh;
    }

    private double timTyLeLaiTheoCauHinh(double giaNhap, LoaiSanPham loaiSanPham) {
        List<BangLaiChuan> cauHinh = getCauHinhLai();
        if (cauHinh == null || cauHinh.isEmpty()) {
            return 0;
        }
        double fallback = 0;
        for (BangLaiChuan b : cauHinh) {
            double tu = b.getGiaNhapTu();
            double den = b.getGiaNhapDen();
            boolean matchTu = giaNhap >= tu;
            boolean matchDen = (den <= 0) || (giaNhap <= den);
            if (matchTu && matchDen) {
                if (loaiSanPham != null && loaiSanPham.equals(b.getLoaiSanPham())) {
                    return b.getTyLeLai();
                }
                if (b.getLoaiSanPham() == null) {
                    fallback = b.getTyLeLai();
                }
            }
        }
        return fallback;
    }
}



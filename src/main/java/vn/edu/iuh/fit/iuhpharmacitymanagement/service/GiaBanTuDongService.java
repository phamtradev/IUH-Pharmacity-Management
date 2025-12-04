package vn.edu.iuh.fit.iuhpharmacitymanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.BangLaiChuanBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.BangLaiChuanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 * Dịch vụ tự động cập nhật giá bán dựa trên giá nhập FIFO và cấu hình lãi chuẩn.
 * Được kích hoạt khi giá nhập thay đổi (thêm/cập nhật lô hàng).
 */
public class GiaBanTuDongService {

    private final SanPhamDAO sanPhamDAO;
    private final LoHangDAO loHangDAO;
    private final BangLaiChuanBUS bangLaiChuanBUS;

    public GiaBanTuDongService() {
        this(new SanPhamDAO(), new LoHangDAO(), new BangLaiChuanBUS(new BangLaiChuanDAO()));
    }

    public GiaBanTuDongService(SanPhamDAO sanPhamDAO,
                               LoHangDAO loHangDAO,
                               BangLaiChuanBUS bangLaiChuanBUS) {
        this.sanPhamDAO = sanPhamDAO;
        this.loHangDAO = loHangDAO;
        this.bangLaiChuanBUS = bangLaiChuanBUS;
    }

    /**
     * Tính lại và ghi giá bán mới cho sản phẩm dựa trên cấu hình hiện hành.
     * @param maSanPham mã sản phẩm cần đồng bộ
     * @return true nếu có thay đổi và cập nhật thành công, false nếu không cần cập nhật.
     */
    public boolean capNhatGiaBanTuDong(String maSanPham) {
        if (maSanPham == null || maSanPham.isBlank()) {
            return false;
        }
        Optional<SanPham> sanPhamOpt = sanPhamDAO.findById(maSanPham);
        if (sanPhamOpt.isEmpty()) {
            return false;
        }

        SanPham sanPham = sanPhamOpt.get();
        double giaNhapFIFO = layGiaNhapFIFO(sanPham);
        if (giaNhapFIFO <= 0) {
            return false;
        }

        double giaBanDeXuat = tinhGiaBanTheoLaiChuan(giaNhapFIFO, sanPham.getLoaiSanPham());
        if (giaBanDeXuat <= 0) {
            return false;
        }

        if (Math.abs(sanPham.getGiaBan() - giaBanDeXuat) <= 0.01) {
            return false; // Không cần cập nhật vì giá đã giống nhau
        }

        try {
            sanPham.setGiaBan(giaBanDeXuat);
            return sanPhamDAO.update(sanPham);
        } catch (Exception ex) {
            System.err.println("[GiaBanTuDongService] Không thể cập nhật giá bán cho " + maSanPham + ": " + ex.getMessage());
            return false;
        }
    }

    private double layGiaNhapFIFO(SanPham sanPham) {
        List<LoHang> dsLo = loHangDAO.findByMaSanPham(sanPham.getMaSanPham());
        if (dsLo == null || dsLo.isEmpty()) {
            return sanPham.getGiaNhap();
        }

        LocalDate today = LocalDate.now();
        return dsLo.stream()
                .filter(lh -> lh.getTonKho() > 0
                        && lh.isTrangThai()
                        && lh.getGiaNhapLo() > 0
                        && (lh.getHanSuDung() == null || !lh.getHanSuDung().isBefore(today)))
                .sorted((l1, l2) -> {
                    int compare = l1.getHanSuDung().compareTo(l2.getHanSuDung());
                    if (compare == 0) {
                        return l1.getMaLoHang().compareTo(l2.getMaLoHang());
                    }
                    return compare;
                })
                .mapToDouble(LoHang::getGiaNhapLo)
                .findFirst()
                .orElse(sanPham.getGiaNhap());
    }

    private double tinhGiaBanTheoLaiChuan(double giaNhapChuaVAT, LoaiSanPham loaiSanPham) {
        if (giaNhapChuaVAT <= 0) {
            return 0;
        }
        List<BangLaiChuan> cauHinh = bangLaiChuanBUS.layTatCa();
        double tyLe = timTyLeLaiTheoCauHinh(giaNhapChuaVAT, loaiSanPham, cauHinh);
        if (tyLe <= 0) {
            return 0;
        }
        return giaNhapChuaVAT * (1 + tyLe);
    }

    /**
     * Tìm tỷ lệ lãi áp dụng cho giá nhập và loại sản phẩm.
     * Logic ưu tiên: Bảng cụ thể (theo loại sản phẩm) > Bảng tổng quát (null).
     * 
     * Ví dụ: Nếu có cả bảng cho tất cả (null) và bảng cho THUOC,
     * thì sản phẩm THUOC sẽ dùng bảng THUOC, không dùng bảng tổng quát.
     */
    private double timTyLeLaiTheoCauHinh(double giaNhap,
                                         LoaiSanPham loaiSanPham,
                                         List<BangLaiChuan> cauHinh) {
        if (cauHinh == null || cauHinh.isEmpty()) {
            return 0;
        }
        double fallback = 0; // Tỷ lệ lãi từ bảng tổng quát (null)
        
        for (BangLaiChuan b : cauHinh) {
            double tu = b.getGiaNhapTu();
            double den = b.getGiaNhapDen();
            boolean matchTu = giaNhap >= tu;
            boolean matchDen = (den <= 0) || (giaNhap <= den);
            
            if (matchTu && matchDen) {
                // Ưu tiên 1: Nếu có bảng cụ thể cho loại sản phẩm này → dùng ngay
                if (loaiSanPham != null && loaiSanPham.equals(b.getLoaiSanPham())) {
                    return b.getTyLeLai();
                }
                // Ưu tiên 2: Lưu bảng tổng quát (null) làm fallback
                if (b.getLoaiSanPham() == null) {
                    fallback = b.getTyLeLai();
                }
            }
        }
        // Nếu không tìm thấy bảng cụ thể, dùng bảng tổng quát
        return fallback;
    }
}


package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang;

import java.util.List;

/**
 * 
 * @author PhamTra
 */
public class ChiTietDonHangBUS {
    private ChiTietDonHangDAO chiTietDonHangDAO;

    public ChiTietDonHangBUS() {
        this.chiTietDonHangDAO = new ChiTietDonHangDAO();
    }

    /**
     * Thêm chi tiết đơn hàng mới
     * @param chiTietDonHang Chi tiết đơn hàng cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean themChiTietDonHang(ChiTietDonHang chiTietDonHang) {
        try {
            return chiTietDonHangDAO.insert(chiTietDonHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật chi tiết đơn hàng
     * @param chiTietDonHang Chi tiết đơn hàng cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatChiTietDonHang(ChiTietDonHang chiTietDonHang) {
        try {
            return chiTietDonHangDAO.update(chiTietDonHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chi tiết đơn hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy chi tiết đơn hàng theo mã đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return Chi tiết đơn hàng tìm được
     */
    public ChiTietDonHang layChiTietDonHangTheoMa(String maDonHang) {
        return chiTietDonHangDAO.findById(maDonHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng với mã = " + maDonHang));
    }

    /**
     * Lấy danh sách chi tiết đơn hàng theo mã đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return Danh sách chi tiết đơn hàng
     */
    public List<ChiTietDonHang> layDanhSachChiTietTheoMaDonHang(String maDonHang) {
        return chiTietDonHangDAO.findByIdList(maDonHang);
    }

    /**
     * Lấy tất cả chi tiết đơn hàng
     * @return Danh sách tất cả chi tiết đơn hàng
     */
    public List<ChiTietDonHang> layTatCaChiTietDonHang() {
        return chiTietDonHangDAO.findAll();
    }

    /**
     * Đếm số lượng chi tiết đơn hàng theo lô hàng
     * @param maLoHang Mã lô hàng
     * @return Số lượng chi tiết đơn hàng
     */
    public long demSoLuongTheoLoHang(String maLoHang) {
        return chiTietDonHangDAO.countByLoHang(maLoHang);
    }

    /**
     * Tính tổng tiền của đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return Tổng tiền của đơn hàng
     */
    public double tinhTongTienDonHang(String maDonHang) {
        List<ChiTietDonHang> danhSach = chiTietDonHangDAO.findByIdList(maDonHang);
        double tongTien = 0;
        
        for (ChiTietDonHang ct : danhSach) {
            tongTien += ct.getThanhTien();
        }
        
        return tongTien;
    }

    /**
     * Tính tổng số lượng sản phẩm trong đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return Tổng số lượng sản phẩm
     */
    public int tinhTongSoLuongSanPham(String maDonHang) {
        List<ChiTietDonHang> danhSach = chiTietDonHangDAO.findByIdList(maDonHang);
        int tongSoLuong = 0;
        
        for (ChiTietDonHang ct : danhSach) {
            tongSoLuong += ct.getSoLuong();
        }
        
        return tongSoLuong;
    }

    /**
     * Kiểm tra đơn hàng có chi tiết không
     * @param maDonHang Mã đơn hàng
     * @return true nếu có chi tiết, false nếu không
     */
    public boolean kiemTraDonHangCoChiTiet(String maDonHang) {
        List<ChiTietDonHang> danhSach = chiTietDonHangDAO.findByIdList(maDonHang);
        return !danhSach.isEmpty();
    }
}


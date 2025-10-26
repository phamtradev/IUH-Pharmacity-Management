package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonNhapHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonNhapHang;

import java.util.List;

/**
 * Business logic layer for ChiTietDonNhapHang
 * @author PhamTra
 */
public class ChiTietDonNhapHangBUS {
    private ChiTietDonNhapHangDAO chiTietDonNhapHangDAO;

    public ChiTietDonNhapHangBUS() {
        this.chiTietDonNhapHangDAO = new ChiTietDonNhapHangDAO();
    }

    /**
     * Thêm chi tiết đơn nhập hàng mới
     * @param chiTietDonNhapHang Chi tiết đơn nhập hàng cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean themChiTietDonNhapHang(ChiTietDonNhapHang chiTietDonNhapHang) {
        try {
            return chiTietDonNhapHangDAO.insert(chiTietDonNhapHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm chi tiết đơn nhập hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật chi tiết đơn nhập hàng
     * @param chiTietDonNhapHang Chi tiết đơn nhập hàng cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatChiTietDonNhapHang(ChiTietDonNhapHang chiTietDonNhapHang) {
        try {
            return chiTietDonNhapHangDAO.update(chiTietDonNhapHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chi tiết đơn nhập hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy chi tiết đơn nhập hàng theo mã đơn nhập hàng
     * @param maDonNhapHang Mã đơn nhập hàng
     * @return Chi tiết đơn nhập hàng tìm được
     */
    public ChiTietDonNhapHang layChiTietDonNhapHangTheoMa(String maDonNhapHang) {
        return chiTietDonNhapHangDAO.findById(maDonNhapHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn nhập hàng với mã = " + maDonNhapHang));
    }

    /**
     * Lấy tất cả chi tiết đơn nhập hàng
     * @return Danh sách tất cả chi tiết đơn nhập hàng
     */
    public List<ChiTietDonNhapHang> layTatCaChiTietDonNhapHang() {
        return chiTietDonNhapHangDAO.findAll();
    }

    /**
     * Tính tổng tiền của đơn nhập hàng
     * @param maDonNhapHang Mã đơn nhập hàng
     * @return Tổng tiền của đơn nhập hàng
     */
    public double tinhTongTienDonNhapHang(String maDonNhapHang) {
        List<ChiTietDonNhapHang> danhSach = layTatCaChiTietDonNhapHang();
        double tongTien = 0;
        
        for (ChiTietDonNhapHang ct : danhSach) {
            if (ct.getDonNhapHang().getMaDonNhapHang().equals(maDonNhapHang)) {
                tongTien += ct.getThanhTien();
            }
        }
        
        return tongTien;
    }

    /**
     * Tính tổng số lượng sản phẩm trong đơn nhập hàng
     * @param maDonNhapHang Mã đơn nhập hàng
     * @return Tổng số lượng sản phẩm
     */
    public int tinhTongSoLuongSanPham(String maDonNhapHang) {
        List<ChiTietDonNhapHang> danhSach = layTatCaChiTietDonNhapHang();
        int tongSoLuong = 0;
        
        for (ChiTietDonNhapHang ct : danhSach) {
            if (ct.getDonNhapHang().getMaDonNhapHang().equals(maDonNhapHang)) {
                tongSoLuong += ct.getSoLuong();
            }
        }
        
        return tongSoLuong;
    }

    /**
     * Kiểm tra đơn nhập hàng có chi tiết không
     * @param maDonNhapHang Mã đơn nhập hàng
     * @return true nếu có chi tiết, false nếu không
     */
    public boolean kiemTraDonNhapHangCoChiTiet(String maDonNhapHang) {
        List<ChiTietDonNhapHang> danhSach = layTatCaChiTietDonNhapHang();
        for (ChiTietDonNhapHang ct : danhSach) {
            if (ct.getDonNhapHang().getMaDonNhapHang().equals(maDonNhapHang)) {
                return true;
            }
        }
        return false;
    }
}


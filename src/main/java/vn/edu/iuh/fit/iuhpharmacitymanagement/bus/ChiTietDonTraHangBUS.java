package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonTraHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang;

import java.util.List;
import java.util.ArrayList;

/**
 * Business logic layer for ChiTietDonTraHang
 *
 * @author PhamTra
 */
public class ChiTietDonTraHangBUS {

    private ChiTietDonTraHangDAO chiTietDonTraHangDAO;

    public ChiTietDonTraHangBUS() {
        this.chiTietDonTraHangDAO = new ChiTietDonTraHangDAO();
    }

    /**
     * Thêm chi tiết đơn trả hàng mới
     *
     * @param chiTietDonTraHang Chi tiết đơn trả hàng cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean themChiTietDonTraHang(ChiTietDonTraHang chiTietDonTraHang) {
        try {
            return chiTietDonTraHangDAO.insert(chiTietDonTraHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm chi tiết đơn trả hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật chi tiết đơn trả hàng
     *
     * @param chiTietDonTraHang Chi tiết đơn trả hàng cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatChiTietDonTraHang(ChiTietDonTraHang chiTietDonTraHang) {
        try {
            return chiTietDonTraHangDAO.update(chiTietDonTraHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chi tiết đơn trả hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy chi tiết đơn trả hàng theo mã đơn trả hàng
     *
     * @param maDonTraHang Mã đơn trả hàng
     * @return Chi tiết đơn trả hàng tìm được
     */
    public ChiTietDonTraHang layChiTietDonTraHangTheoMa(String maDonTraHang) {
        return chiTietDonTraHangDAO.findById(maDonTraHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn trả hàng với mã = " + maDonTraHang));
    }

    /**
     * Lấy tất cả chi tiết đơn trả hàng
     *
     * @return Danh sách tất cả chi tiết đơn trả hàng
     */
    public List<ChiTietDonTraHang> layTatCaChiTietDonTraHang() {
        return chiTietDonTraHangDAO.findAll();
    }

    /**
     * Tính tổng tiền của đơn trả hàng
     *
     * @param maDonTraHang Mã đơn trả hàng
     * @return Tổng tiền của đơn trả hàng
     */
    public double tinhTongTienDonTraHang(String maDonTraHang) {
        List<ChiTietDonTraHang> danhSach = layTatCaChiTietDonTraHang();
        double tongTien = 0;

        for (ChiTietDonTraHang ct : danhSach) {
            if (ct.getDonTraHang().getMaDonTraHang().equals(maDonTraHang)) {
                tongTien += ct.getThanhTien();
            }
        }

        return tongTien;
    }

    /**
     * Tính tổng số lượng sản phẩm trong đơn trả hàng
     *
     * @param maDonTraHang Mã đơn trả hàng
     * @return Tổng số lượng sản phẩm
     */
    public int tinhTongSoLuongSanPham(String maDonTraHang) {
        List<ChiTietDonTraHang> danhSach = layTatCaChiTietDonTraHang();
        int tongSoLuong = 0;

        for (ChiTietDonTraHang ct : danhSach) {
            if (ct.getDonTraHang().getMaDonTraHang().equals(maDonTraHang)) {
                tongSoLuong += ct.getSoLuong();
            }
        }

        return tongSoLuong;
    }

    /**
     * Kiểm tra đơn trả hàng có chi tiết không
     *
     * @param maDonTraHang Mã đơn trả hàng
     * @return true nếu có chi tiết, false nếu không
     */
    public boolean kiemTraDonTraHangCoChiTiet(String maDonTraHang) {
        List<ChiTietDonTraHang> danhSach = layTatCaChiTietDonTraHang();
        for (ChiTietDonTraHang ct : danhSach) {
            if (ct.getDonTraHang().getMaDonTraHang().equals(maDonTraHang)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lấy danh sách chi tiết đơn trả hàng theo mã đơn trả
     *
     * @param maDonTraHang Mã đơn trả hàng
     * @return Danh sách chi tiết đơn trả hàng
     */
    public List<ChiTietDonTraHang> layChiTietTheoMaDonTra(String maDonTraHang) {
        try {
            return chiTietDonTraHangDAO.findByMaDonTra(maDonTraHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chi tiết đơn trả hàng: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lấy tất cả các sản phẩm từ đơn trả hàng CHƯA XỬ LÝ
     * (chưa tạo phiếu xuất hủy hoặc nhập lại kho)
     * 
     * @return Danh sách chi tiết đơn trả hàng có trạng thái "Chưa xử lý"
     */
    public List<ChiTietDonTraHang> layTatCaChiTietCanHuy() {
        return chiTietDonTraHangDAO.findAllWithDetails();
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonNhapHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonNhapHang;

import java.util.List;

public class DonNhapHangBUS {
    private DonNhapHangDAO donNhapHangDAO;

    public DonNhapHangBUS() {
        this.donNhapHangDAO = new DonNhapHangDAO();
    }

    public boolean taoDonNhapHang(DonNhapHang donNhapHang) {
        try {
            return donNhapHangDAO.insert(donNhapHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo đơn nhập hàng: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatDonNhapHang(DonNhapHang donNhapHang) {
        try {
            return donNhapHangDAO.update(donNhapHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật đơn nhập hàng: " + e.getMessage());
            return false;
        }
    }

    public DonNhapHang layDonNhapHangTheoMa(String maDonNhap) {
        return donNhapHangDAO.findById(maDonNhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nhập hàng với mã = " + maDonNhap));
    }

    public List<DonNhapHang> layTatCaDonNhapHang() {
        return donNhapHangDAO.findAll();
    }

    public String duKienMaDonNhapHangTiepTheo() {
        return donNhapHangDAO.duKienMaDonNhapHangTiepTheo();
    }

    public boolean xoaDonNhapHang(String maDonNhap) {
        try {
            return donNhapHangDAO.delete(maDonNhap);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa đơn nhập hàng: " + e.getMessage());
            return false;
        }
    }

    public List<DonNhapHang> timKiemTheoText(String text) {
        return donNhapHangDAO.timTheoText(text);
    }
    
    /**
     * Đếm số lượng đơn nhập hàng trong tuần hiện tại
     * @return số lượng đơn nhập hàng trong tuần
     */
    public int demDonNhapHangTrongTuan() {
        return donNhapHangDAO.countDonNhapHangTrongTuan();
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonTraHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang;

import java.util.List;

public class DonTraHangBUS {
    private DonTraHangDAO donTraHangDAO;

    public DonTraHangBUS() {
        this.donTraHangDAO = new DonTraHangDAO();
    }

    public boolean taoDonTraHang(DonTraHang donTraHang) {
        try {
            return donTraHangDAO.insert(donTraHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo đơn trả hàng: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatDonTraHang(DonTraHang donTraHang) {
        try {
            return donTraHangDAO.update(donTraHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật đơn trả hàng: " + e.getMessage());
            return false;
        }
    }

    public DonTraHang layDonTraHangTheoMa(String maDonTraHang) {
        return donTraHangDAO.findById(maDonTraHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn trả hàng với mã = " + maDonTraHang));
    }

    // Alias method cho layDonTraHangTheoMa
    public DonTraHang timDonTraTheoMa(String maDonTraHang) {
        return donTraHangDAO.findById(maDonTraHang).orElse(null);
    }

    public List<DonTraHang> layTatCaDonTraHang() {
        return donTraHangDAO.findAll();
    }

    public boolean xoaDonTraHang(String maDonTraHang) {
        try {
            return donTraHangDAO.delete(maDonTraHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa đơn trả hàng: " + e.getMessage());
            return false;
        }
    }

    public List<DonTraHang> timKiemTheoText(String text) {
        return donTraHangDAO.timTheoText(text);
    }
}

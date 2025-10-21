package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.HangHongDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.HangHong;

import java.util.List;

public class HangHongBUS {
    private HangHongDAO hangHongDAO;

    public HangHongBUS() {
        this.hangHongDAO = new HangHongDAO();
    }

    public boolean taoHangHong(HangHong hangHong) {
        try {
            return hangHongDAO.insert(hangHong);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo phiếu hàng hỏng: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatHangHong(HangHong hangHong) {
        try {
            return hangHongDAO.update(hangHong);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật phiếu hàng hỏng: " + e.getMessage());
            return false;
        }
    }

    public HangHong layHangHongTheoMa(String maHangHong) {
        return hangHongDAO.findById(maHangHong)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu hàng hỏng với mã = " + maHangHong));
    }

    public List<HangHong> layTatCaHangHong() {
        return hangHongDAO.findAll();
    }

    public boolean xoaHangHong(String maHangHong) {
        try {
            return hangHongDAO.delete(maHangHong);
        } catch (Exception e) {
            System.out.println("Lỗi khi xóa phiếu hàng hỏng: " + e.getMessage());
            return false;
        }
    }

    public List<HangHong> timKiemTheoText(String text) {
        return hangHongDAO.timTheoText(text);
    }
}

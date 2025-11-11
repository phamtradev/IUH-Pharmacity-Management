package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietHangHongDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietHangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.HangHong;

import java.util.List;

public class ChiTietHangHongBUS {
    private ChiTietHangHongDAO chiTietHangHongDAO;

    public ChiTietHangHongBUS() {
        this.chiTietHangHongDAO = new ChiTietHangHongDAO();
    }

    public boolean taoChiTietHangHong(ChiTietHangHong chiTietHangHong) {
        try {
            return chiTietHangHongDAO.insert(chiTietHangHong);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo chi tiết hàng hỏng: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatChiTietHangHong(ChiTietHangHong chiTietHangHong) {
        try {
            return chiTietHangHongDAO.update(chiTietHangHong);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chi tiết hàng hỏng: " + e.getMessage());
            return false;
        }
    }

    public ChiTietHangHong layChiTietHangHongTheoMa(String maChiTietHangHong) {
        return chiTietHangHongDAO.findById(maChiTietHangHong)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hàng hỏng với mã = " + maChiTietHangHong));
    }

    public List<ChiTietHangHong> layTatCaChiTietHangHong() {
        return chiTietHangHongDAO.findAll();
    }

    public List<ChiTietHangHong> layChiTietTheoMaHangHong(String maHangHong) {
        return chiTietHangHongDAO.findByMaHangHong(maHangHong);
    }
    
    public List<ChiTietHangHong> layChiTietTheoHangHong(HangHong hangHong) {
        return layChiTietTheoMaHangHong(hangHong.getMaHangHong());
    }
}


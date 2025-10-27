package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author PhamTra
 */
public class DonHangBUS {
    private DonHangDAO donHangDAO;
    private ChiTietDonHangDAO chiTietDonHangDAO;

    public DonHangBUS() {
        this.donHangDAO = new DonHangDAO();
        this.chiTietDonHangDAO = new ChiTietDonHangDAO();
    }

    /**
     * Tạo đơn hàng mới
     * @param donHang Đơn hàng cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean taoDonHang(DonHang donHang) {
        try {
            return donHangDAO.insert(donHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật thông tin đơn hàng
     * @param donHang Đơn hàng cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatDonHang(DonHang donHang) {
        try {
            return donHangDAO.update(donHang);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật đơn hàng: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy đơn hàng theo mã
     * @param maDonHang Mã đơn hàng cần tìm
     * @return Đơn hàng tìm được
     * @throws RuntimeException nếu không tìm thấy
     */
    public DonHang layDonHangTheoMa(String maDonHang) {
        return donHangDAO.findById(maDonHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với mã = " + maDonHang));
    }

    /**
     * Lấy tất cả đơn hàng
     * @return Danh sách tất cả đơn hàng
     */
    public List<DonHang> layTatCaDonHang() {
        return donHangDAO.findAll();
    }

    /**
     * Lấy danh sách đơn hàng theo khoảng thời gian
     * @param tuNgay Ngày bắt đầu
     * @param denNgay Ngày kết thúc
     * @return Danh sách đơn hàng trong khoảng thời gian
     */
    public List<DonHang> layDonHangTheoKhoangThoiGian(LocalDate tuNgay, LocalDate denNgay) {
        List<DonHang> tatCaDonHang = donHangDAO.findAll();
        List<DonHang> ketQua = new ArrayList<>();
        
        for (DonHang dh : tatCaDonHang) {
            LocalDate ngayDat = dh.getNgayDatHang();
            if (!ngayDat.isBefore(tuNgay) && !ngayDat.isAfter(denNgay)) {
                ketQua.add(dh);
            }
        }
        
        return ketQua;
    }

    /**
     * Tìm kiếm đơn hàng theo mã
     * @param maDonHang Mã đơn hàng cần tìm
     * @return Danh sách đơn hàng tìm được
     */
    public List<DonHang> timKiemTheoMa(String maDonHang) {
        List<DonHang> ketQua = new ArrayList<>();
        donHangDAO.findById(maDonHang).ifPresent(ketQua::add);
        return ketQua;
    }

    /**
     * Tìm kiếm đơn hàng theo mã khách hàng
     * @param maKhachHang Mã khách hàng
     * @return Danh sách đơn hàng của khách hàng
     */
    public List<DonHang> timKiemTheoMaKhachHang(String maKhachHang) {
        List<DonHang> tatCaDonHang = donHangDAO.findAll();
        List<DonHang> ketQua = new ArrayList<>();
        
        for (DonHang dh : tatCaDonHang) {
            if (dh.getKhachHang() != null && 
                dh.getKhachHang().getMaKhachHang().toLowerCase().contains(maKhachHang.toLowerCase())) {
                ketQua.add(dh);
            }
        }
        
        return ketQua;
    }

    /**
     * Tìm kiếm đơn hàng theo mã nhân viên
     * @param maNhanVien Mã nhân viên
     * @return Danh sách đơn hàng của nhân viên
     */
    public List<DonHang> timKiemTheoMaNhanVien(String maNhanVien) {
        List<DonHang> tatCaDonHang = donHangDAO.findAll();
        List<DonHang> ketQua = new ArrayList<>();
        
        for (DonHang dh : tatCaDonHang) {
            if (dh.getNhanVien() != null && 
                dh.getNhanVien().getMaNhanVien().toLowerCase().contains(maNhanVien.toLowerCase())) {
                ketQua.add(dh);
            }
        }
        
        return ketQua;
    }

    /**
     * Đếm số lượng đơn hàng
     * @return Số lượng đơn hàng
     */
    public int demSoLuongDonHang() {
        return donHangDAO.count();
    }

    /**
     * Kiểm tra đơn hàng có tồn tại không
     * @param maDonHang Mã đơn hàng cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    public boolean kiemTraTonTai(String maDonHang) {
        return donHangDAO.exists(maDonHang);
    }

    /**
     * Đếm số lượng đơn hàng theo khách hàng
     * @param maKhachHang Mã khách hàng
     * @return Số lượng đơn hàng
     */
    public long demSoLuongTheoKhachHang(String maKhachHang) {
        return donHangDAO.countByKhachHang(maKhachHang);
    }

    
    public long demSoLuongTheoKhuyenMai(String maKhuyenMai) {
        return donHangDAO.countByKhuyenMai(maKhuyenMai);
    }

    /**
     * Lấy chi tiết đơn hàng theo mã đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return Danh sách chi tiết đơn hàng
     */
    public List<ChiTietDonHang> layChiTietDonHang(String maDonHang) {
        return chiTietDonHangDAO.findByIdList(maDonHang);
    }
}


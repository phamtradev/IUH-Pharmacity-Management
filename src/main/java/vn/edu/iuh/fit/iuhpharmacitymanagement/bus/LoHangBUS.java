/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author User
 */
public class LoHangBUS {

    private final LoHangDAO loHangDAO;
    private final ChiTietDonHangDAO chiTietDonHangDAO;

    public LoHangBUS() {
        this.loHangDAO = new LoHangDAO();
        this.chiTietDonHangDAO = new ChiTietDonHangDAO();
    }

    //Lấy danh sách tất cả các lô hàng và tự động cập nhật trạng thái (hết hạn)
    public List<LoHang> getAllLoHang() {
        List<LoHang> danhSach = loHangDAO.findAll();
        danhSach.forEach(this::capNhatTrangThaiTuDong);
        return danhSach;
    }

    //Lấy danh sách các lô hàng của một sản phẩm
    public List<LoHang> getLoHangBySanPham(SanPham sanPham) {
        if (sanPham == null || sanPham.getMaSanPham() == null) {
            return List.of(); // Trả về danh sách rỗng nếu sản phẩm không hợp lệ
        }
        List<LoHang> danhSach = loHangDAO.findByMaSanPham(sanPham.getMaSanPham());
        danhSach.forEach(this::capNhatTrangThaiTuDong);
        return danhSach;
    }

    //Thêm một lô hàng mới, kiểm tra các quy tắc nghiệp vụ
    public boolean themLoHang(LoHang loHang) throws Exception {
        // Cho phép tạo nhiều lô cùng tên nhưng HSD khác nhau
        // (Ví dụ: Lô "A" HSD 01/2025 và Lô "A" HSD 06/2025 là 2 lô khác nhau)
        return loHangDAO.insert(loHang);
    }

    //Cập nhật thông tin một lô hàng
    public boolean capNhatLoHang(LoHang loHang) throws Exception {
        // Cho phép cập nhật tên lô (có thể trùng với lô khác vì HSD khác nhau)
        return loHangDAO.update(loHang);
    }

    //Xóa một lô hàng, kiểm tra các quy tắc nghiệp vụ trước khi xóa
    public boolean xoaLoHang(String maLoHang) throws Exception {
        Optional<LoHang> loHangOpt = loHangDAO.findById(maLoHang);
        if (loHangOpt.isEmpty()) {
            throw new Exception("Không tìm thấy lô hàng để xóa.");
        }
        LoHang loHang = loHangOpt.get();

        //Không được xóa lô hàng nếu vẫn còn tồn kho.
        if (loHang.getTonKho() > 0) {
            throw new Exception("Không thể xóa lô hàng vì vẫn còn " + loHang.getTonKho() + " sản phẩm tồn kho.");
        }

        //Không được xóa lô hàng nếu đã từng được bán.
        long soLuongGiaoDich = chiTietDonHangDAO.countByLoHang(maLoHang);
        if (soLuongGiaoDich > 0) {
            throw new Exception("Không thể xóa lô hàng này vì đã có lịch sử giao dịch.");
        }

        // Nếu tất cả các điềi kiện đều được thỏa mãn --> xóa
        return loHangDAO.delete(maLoHang);
    }

    //tự động cập nhật trạng thái của lô hàng.
    private void capNhatTrangThaiTuDong(LoHang loHang) {
        //Tự động cập nhật trạng thái hết hạn
        if (loHang.getHanSuDung().isBefore(LocalDate.now()) && loHang.isTrangThai()) {
            loHang.setTrangThai(false);
        }
    }

    /**
     * Kiểm tra tên lô hàng đã tồn tại chưa
     */
    public boolean isTenLoHangExists(String tenLoHang) {
        return loHangDAO.isTenLoHangExists(tenLoHang);
    }

    /**
     * Tìm lô hàng theo sản phẩm và hạn sử dụng (để cộng dồn)
     */
    public Optional<LoHang> findByMaSanPhamAndHanSuDung(String maSanPham, LocalDate hanSuDung) {
        return loHangDAO.findByMaSanPhamAndHanSuDung(maSanPham, hanSuDung);
    }

    /**
     * Cập nhật tồn kho (cộng dồn)
     */
    public boolean updateTonKho(String maLoHang, int themSoLuong) {
        return loHangDAO.updateTonKho(maLoHang, themSoLuong);
    }

    public List<LoHang> layTatCaLoHangHetHan() {
        return loHangDAO.timSanPhamHetHan();
    }

    public List<Map<String, Object>> layDanhSachCanHuyTuDonTra() {
        return loHangDAO.findForDisposalFromReturns();
    }
}

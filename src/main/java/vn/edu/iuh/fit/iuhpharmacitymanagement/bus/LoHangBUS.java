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
        //Tên lô hàng phải là duy nhất trong phạm vi một sản phẩm (1 sản phẩm chỉ thuộc 1 lô)
        List<LoHang> danhSachLoHangCuaSP = loHangDAO.findByMaSanPham(loHang.getSanPham().getMaSanPham());
        for (LoHang lh : danhSachLoHangCuaSP) {
            if (lh.getTenLoHang().equalsIgnoreCase(loHang.getTenLoHang())) {
                throw new Exception("Tên lô hàng '" + loHang.getTenLoHang() + "' đã tồn tại cho sản phẩm này.");
            }
        }
        return loHangDAO.insert(loHang);
    }

    //Cập nhật thông tin một lô hàng
    public boolean capNhatLoHang(LoHang loHang) throws Exception {
        //Tên mới không được trùng với một lô hàng khác của cùng sản phẩm
        List<LoHang> danhSachLoHangCuaSP = loHangDAO.findByMaSanPham(loHang.getSanPham().getMaSanPham());
        for (LoHang lh : danhSachLoHangCuaSP) {
            //Nếu tìm thấy một lô hàng có cùng tên, nhưng khác mã -> báo lỗi
            if (lh.getTenLoHang().equalsIgnoreCase(loHang.getTenLoHang()) && !lh.getMaLoHang().equals(loHang.getMaLoHang())) {
                throw new Exception("Tên lô hàng '" + loHang.getTenLoHang() + "' đã tồn tại cho sản phẩm này.");
            }
        }
        
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
}

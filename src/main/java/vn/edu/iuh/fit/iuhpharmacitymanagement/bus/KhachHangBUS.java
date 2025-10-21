/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.KhachHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;

/**
 *
 * @author User
 */
public class KhachHangBUS {
    
    private final KhachHangDAO khachHangDAO;
    private final DonHangDAO donHangDAO;

    public KhachHangBUS(KhachHangDAO khachHangDAO, DonHangDAO donHangDAO) {
        this.khachHangDAO = khachHangDAO;
        this.donHangDAO = donHangDAO;
    }
    
    public List<KhachHang> getAllKhachHang(){
        return khachHangDAO.findAll();
    }
    
    public List<KhachHang> findKhachHangByName(String ten) {
        return khachHangDAO.findByName(ten);
    }
    
    public Optional<KhachHang> findKhachHangById(String id) {
        return khachHangDAO.findById(id);
    }
    
    public boolean themKhachHang(KhachHang kh) throws Exception {
        //Số điện thoại là duy nhất
        if (khachHangDAO.findBySoDienThoai(kh.getSoDienThoai()).isPresent()) {
            throw new Exception("Số điện thoại '" + kh.getSoDienThoai() + "' đã được sử dụng.");
        }

        //Email là duy nhất (chỉ kiểm tra nếu email được cung cấp)
        if (kh.getEmail() != null && !kh.getEmail().trim().isEmpty()) {
            if (khachHangDAO.findByEmail(kh.getEmail()).isPresent()) {
                throw new Exception("Email '" + kh.getEmail() + "' đã được sử dụng.");
            }
        }
        
        return khachHangDAO.insert(kh);
    }
    
    //Cập nhật thông tin khách hàng
    public boolean capNhatKhachHang(KhachHang kh) throws Exception {
        // Kiểm tra SĐT mới có bị trùng với người khác không
        Optional<KhachHang> khachHangTheoSDT = khachHangDAO.findBySoDienThoai(kh.getSoDienThoai());
        if (khachHangTheoSDT.isPresent() && !khachHangTheoSDT.get().getMaKhachHang().equals(kh.getMaKhachHang())) {
            throw new Exception("Số điện thoại '" + kh.getSoDienThoai() + "' đã được sử dụng bởi một khách hàng khác.");
        }

        // Kiểm tra email mới có bị trùng với người khác không
        if (kh.getEmail() != null && !kh.getEmail().trim().isEmpty()) {
            Optional<KhachHang> khachHangTheoEmail = khachHangDAO.findByEmail(kh.getEmail());
            if (khachHangTheoEmail.isPresent() && !khachHangTheoEmail.get().getMaKhachHang().equals(kh.getMaKhachHang())) {
                throw new Exception("Email '" + kh.getEmail() + "' đã được sử dụng bởi một khách hàng khác.");
            }
        }

        return khachHangDAO.update(kh);
    }
    
    // Xóa 1 KH
    public boolean xoaKhachHang(String maKhachHang) throws Exception {
        //Không được xóa khách hàng nếu đã có đơn hàng
        long soLuongDonHang = donHangDAO.countByKhachHang(maKhachHang);
        if (soLuongDonHang > 0) {
            throw new Exception("Không thể xóa khách hàng này vì đã có " + soLuongDonHang + " đơn hàng liên quan.");
        }
        
        return khachHangDAO.delete(maKhachHang);
    }
}


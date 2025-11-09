/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;

/**
 *
 * @author LAPTOP ASUS
 */
public class ChiTietKhuyenMaiSanPhamBUS {
    private final ChiTietKhuyenMaiSanPhamDAO chiTietKhuyenMaiSanPhamDAO;

    public ChiTietKhuyenMaiSanPhamBUS(ChiTietKhuyenMaiSanPhamDAO chiTietKhuyenMaiSanPhamDAO) {
        this.chiTietKhuyenMaiSanPhamDAO = chiTietKhuyenMaiSanPhamDAO;
    }

    
    public boolean taoChiTietKhuyenMaiSanPham(ChiTietKhuyenMaiSanPham ctkmsp) {
        return chiTietKhuyenMaiSanPhamDAO.insert(ctkmsp);
    }

    
    public boolean capNhatChiTietKhuyenMaiSanPham(ChiTietKhuyenMaiSanPham ctkmsp) {
        return chiTietKhuyenMaiSanPhamDAO.update(ctkmsp);
    }

    
    public boolean xoaChiTietKhuyenMaiSanPham(String maSanPham, String maKhuyenMai) {
        return chiTietKhuyenMaiSanPhamDAO.delete(maSanPham, maKhuyenMai);
    }

    
    public Optional<ChiTietKhuyenMaiSanPham> timTheoMa(String maChiTietKM) {
        return chiTietKhuyenMaiSanPhamDAO.findById(maChiTietKM);
    }

    
    public ChiTietKhuyenMaiSanPham layTheoMa(String maChiTietKM) {
        return chiTietKhuyenMaiSanPhamDAO.findById(maChiTietKM)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết khuyến mãi sản phẩm với mã = " + maChiTietKM));
    }

    
    public List<ChiTietKhuyenMaiSanPham> layTatCa() {
        return chiTietKhuyenMaiSanPhamDAO.findAll();
    }

    
    public List<ChiTietKhuyenMaiSanPham> timTheoMaKhuyenMai(String maKhuyenMai) {
        return chiTietKhuyenMaiSanPhamDAO.findByMaKhuyenMai(maKhuyenMai);
    }

    
    public List<ChiTietKhuyenMaiSanPham> timTheoMaSanPham(String maSanPham) {
        return chiTietKhuyenMaiSanPhamDAO.findByMaSanPham(maSanPham);
    }

    
    public boolean kiemTraTonTai(String maSanPham, String maKhuyenMai) {
        return chiTietKhuyenMaiSanPhamDAO.existsBySanPhamVaKhuyenMai(maSanPham, maKhuyenMai);
    }

    
    public int demTongSoChiTietKhuyenMaiSanPham() {
        return chiTietKhuyenMaiSanPhamDAO.count();
    }

    /**
     * Kiểm tra xem sản phẩm có đang trong khuyến mãi nào đang hoạt động không
     * hoặc khuyến mãi mới sẽ overlap với khuyến mãi cũ
     * 
     * @param maSanPham Mã sản phẩm cần kiểm tra
     * @param ngayBatDau Ngày bắt đầu của khuyến mãi mới
     * @param ngayKetThuc Ngày kết thúc của khuyến mãi mới
     * @param maKhuyenMaiLoaiTru Mã khuyến mãi cần loại trừ (null nếu không loại trừ)
     * @return KhuyenMai đang conflict, hoặc null nếu không có conflict
     */
    public KhuyenMai kiemTraConflictKhuyenMai(String maSanPham, LocalDate ngayBatDau, 
                                               LocalDate ngayKetThuc, String maKhuyenMaiLoaiTru) {
        return chiTietKhuyenMaiSanPhamDAO.kiemTraConflictKhuyenMai(maSanPham, ngayBatDau, ngayKetThuc, maKhuyenMaiLoaiTru);
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;

import java.util.List;
import java.util.Optional;

public class SanPhamBUS {

    private final SanPhamDAO sanPhamDAO;

    public SanPhamBUS(SanPhamDAO sanPhamDAO) {
        this.sanPhamDAO = sanPhamDAO;
        
    }

    public boolean taoSanPham(SanPham sanPham) {
        return sanPhamDAO.insert(sanPham);
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        return sanPhamDAO.update(sanPham);
    }

    public Optional<SanPham> timSanPhamTheoMa(String maSanPham) {
        return sanPhamDAO.findById(maSanPham);
    }

    public SanPham laySanPhamTheoMa(String maSanPham) {
        return sanPhamDAO.findById(maSanPham)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với mã = " + maSanPham));
    }

    public List<SanPham> layTatCaSanPham() {
        return sanPhamDAO.findAll();
    }

    public List<SanPham> timSanPhamTheoTen(String tenSanPham) {
        return sanPhamDAO.findByName(tenSanPham);
    }

    public List<SanPham> timSanPhamTheoLoai(LoaiSanPham loaiSanPham) {
        return sanPhamDAO.findByLoai(loaiSanPham);
    }

    public List<SanPham> laySanPhamHoatDong() {
        return sanPhamDAO.findByHoatDong(true);
    }

    public boolean kiemTraTonTaiSanPham(String maSanPham) {
        return sanPhamDAO.findById(maSanPham).isPresent();
    }

    public int demTongSoSanPham() {
        return sanPhamDAO.findAll().size();
    }

    public int demSanPhamHoatDong() {
        return sanPhamDAO.findByHoatDong(true).size();
    }

    public String layMaSanPhamCuoi() {
        return sanPhamDAO.getLastMaSanPham();
    }
    
    public Optional<SanPham> timSanPhamTheoSoDangKy(String soDangKy) {
        return sanPhamDAO.findBySoDangKy(soDangKy);
    }
}

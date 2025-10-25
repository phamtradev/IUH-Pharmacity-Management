package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;

import java.util.List;
import java.util.Optional;

public class DonViTinhBUS {
    private final DonViTinhDAO donViTinhDAO;
    
    public DonViTinhBUS(DonViTinhDAO donViTinhDAO) {
        this.donViTinhDAO = donViTinhDAO;
    }
    
    public boolean taoDonViTinh(DonViTinh donViTinh) {
        return donViTinhDAO.insert(donViTinh);
    }
    
    public boolean capNhatDonViTinh(DonViTinh donViTinh) {
        return donViTinhDAO.update(donViTinh);
    }
    
    public Optional<DonViTinh> timDonViTinhTheoMa(String maDonVi) {
        return donViTinhDAO.findById(maDonVi);
    }
    
    public DonViTinh layDonViTinhTheoMa(String maDonVi) {
        return donViTinhDAO.findById(maDonVi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị tính với mã = " + maDonVi));
    }
    
    public List<DonViTinh> layTatCaDonViTinh() {
        return donViTinhDAO.findAll();
    }
    
    public Optional<DonViTinh> timDonViTinhTheoTen(String tenDonVi) throws Exception {
        return Optional.ofNullable(donViTinhDAO.findByName(tenDonVi));
    }
    
    public DonViTinh layDonViTinhTheoTen(String tenDonVi) throws Exception {
        DonViTinh donViTinh = donViTinhDAO.findByName(tenDonVi);
        if (donViTinh == null) {
            throw new RuntimeException("Không tìm thấy đơn vị tính với tên = " + tenDonVi);
        }
        return donViTinh;
    }
    
    public List<DonViTinh> timDonViTinhTheoTenGanDung(String tenDonVi) throws Exception {
        return donViTinhDAO.findByNameSearch(tenDonVi);
    }
    
    public boolean kiemTraTonTai(String maDonVi) {
        return donViTinhDAO.exists(maDonVi);
    }
    
    public boolean kiemTraTonTaiTheoTen(String tenDonVi) throws Exception {
        return donViTinhDAO.existsByName(tenDonVi);
    }
    
    public int demTongSoDonViTinh() {
        return donViTinhDAO.count();
    }
    
    public boolean xoaDonViTinh(String maDonVi) {
        return donViTinhDAO.delete(maDonVi);
    }
}


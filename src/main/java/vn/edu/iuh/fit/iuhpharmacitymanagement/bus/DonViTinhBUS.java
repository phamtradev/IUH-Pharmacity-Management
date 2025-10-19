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
    
    public DonViTinh timDonViTinhTheoTen(String tenDonVi) {
        return donViTinhDAO.findByName(tenDonVi);
    }
    
    public List<DonViTinh> timDonViTinhTheoTenGanDung(String tenDonVi) {
        return donViTinhDAO.findByNameSearch(tenDonVi);
    }
    
    public boolean kiemTraTonTai(String maDonVi) {
        return donViTinhDAO.exists(maDonVi);
    }
    
    public boolean kiemTraTonTaiTheoTen(String tenDonVi) {
        return donViTinhDAO.existsByName(tenDonVi);
    }
    
    public int demTongSoDonViTinh() {
        return donViTinhDAO.count();
    }
}


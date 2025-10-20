package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.NhanVienDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import java.util.List;

public class NhanVienBUS {
    private NhanVienDAO nhanVienDAO;
    
    public NhanVienBUS() {
        this.nhanVienDAO = new NhanVienDAO();
    }
    
    public boolean taoNhanVien(NhanVien nhanVien) {
        try {
            return nhanVienDAO.insert(nhanVien);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean capNhatNhanVien(NhanVien nhanVien) {
        try {
            return nhanVienDAO.update(nhanVien);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public NhanVien timNhanVienTheoMa(String maNhanVien) {
        return nhanVienDAO.findById(maNhanVien)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
    }
    
    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienDAO.findAll();
    }
    
    public List<NhanVien> timNhanVienTheoTuKhoa(String tuKhoa) {
        return nhanVienDAO.findByKeyword(tuKhoa);
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.NhaCungCapDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;
import java.util.List;

public class NhaCungCapBUS {
    private NhaCungCapDAO nhaCungCapDAO;
    
    public NhaCungCapBUS() {
        this.nhaCungCapDAO = new NhaCungCapDAO();
    }
    
    public boolean taoNhaCungCap(NhaCungCap nhaCungCap) {
        try {
            return nhaCungCapDAO.insert(nhaCungCap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean capNhatNhaCungCap(NhaCungCap nhaCungCap) {
        try {
            return nhaCungCapDAO.update(nhaCungCap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public NhaCungCap layNhaCungCapTheoMa(String maNhaCungCap) {
        return nhaCungCapDAO.findById(maNhaCungCap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với mã = " + maNhaCungCap));
    }
    
    public List<NhaCungCap> layTatCaNhaCungCap() {
        return nhaCungCapDAO.findAll();
    }
    
    public NhaCungCap layNhaCungCapTheoTen(String tenNhaCungCap) {
        return nhaCungCapDAO.timTheoTen(tenNhaCungCap);
    }
    
    public NhaCungCap layNhaCungCapTheoSoDienThoai(String soDienThoai) {
        return nhaCungCapDAO.timTheoSoDienThoai(soDienThoai);
    }
    
    public NhaCungCap layNhaCungCapTheoEmail(String email) {
        return nhaCungCapDAO.timTheoEmail(email);
    }
    
    public List<NhaCungCap> timKiemTheoText(String text) {
        return nhaCungCapDAO.timTheoText(text);
    }
    
    public boolean xoaNhaCungCap(String maNhaCungCap) {
        try {
            return nhaCungCapDAO.delete(maNhaCungCap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

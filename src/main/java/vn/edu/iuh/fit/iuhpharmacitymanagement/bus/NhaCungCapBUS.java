package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.NhaCungCapDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;
import java.util.List;

public class NhaCungCapBUS {
    private NhaCungCapDAO nhaCungCapDAO;
    private String lastErrorMessage = null;
    
    public NhaCungCapBUS() {
        this.nhaCungCapDAO = new NhaCungCapDAO();
    }
    
    public boolean taoNhaCungCap(NhaCungCap nhaCungCap) {
        try {
            boolean ok = nhaCungCapDAO.insert(nhaCungCap);
            if (!ok) {
                lastErrorMessage = "Không thể tạo nhà cung cấp (insert trả về false)"; // fallback
            } else {
                lastErrorMessage = null;
            }
            return ok;
        } catch (RuntimeException e) {
            // Capture detailed SQL error message from DAO
            lastErrorMessage = e.getMessage();
            System.err.println("NhaCungCapBUS.taoNhaCungCap error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            System.err.println("NhaCungCapBUS.taoNhaCungCap unexpected error: " + e.getMessage());
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
    
    public NhaCungCap layNhaCungCapTheoMaSoThue(String maSoThue) {
        return nhaCungCapDAO.timTheoMaSoThue(maSoThue);
    }

    public boolean xoaNhaCungCap(String maNhaCungCap) {
        try {
            boolean ok = nhaCungCapDAO.delete(maNhaCungCap);
            if (!ok) {
                lastErrorMessage = "Xóa không thành công (không có bản ghi bị xóa).";
            } else {
                lastErrorMessage = null;
            }
            return ok;
        } catch (java.sql.SQLException e) {
            // SQL Server foreign key violation error code = 547
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (e.getErrorCode() == 547 || msg.contains("REFERENCE") || msg.contains("REFERENCES")) {
                // Try to parse referenced table from message
                String table = "bảng tham chiếu";
                try {
                    java.util.regex.Matcher m = java.util.regex.Pattern.compile("table \"[^\"]*\\.([^\\\"]+)\"").matcher(msg);
                    if (m.find()) {
                        table = m.group(1);
                    } else {
                        // fallback: look for 'table \"' then next token
                        m = java.util.regex.Pattern.compile("table \"([^\"]+)\"").matcher(msg);
                        if (m.find()) {
                            table = m.group(1);
                        }
                    }
                } catch (Exception ex) {
                    // ignore parse errors
                }
                lastErrorMessage = "Không thể xóa nhà cung cấp vì còn dữ liệu liên quan ở " + table + ". Vui lòng xóa hoặc cập nhật các bản ghi liên quan trước khi xóa.";
            } else {
                lastErrorMessage = "Lỗi khi xóa nhà cung cấp: " + msg;
            }
            System.err.println("NhaCungCapBUS.xoaNhaCungCap SQLException: " + msg);
            return false;
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            System.err.println("NhaCungCapBUS.xoaNhaCungCap Exception: " + e.getMessage());
            return false;
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}

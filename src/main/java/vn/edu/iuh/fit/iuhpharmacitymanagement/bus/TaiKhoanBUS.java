package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.TaiKhoanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.TaiKhoan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.PasswordUtil;


public class TaiKhoanBUS {
    
    private TaiKhoanDAO taiKhoanDAO;
    
    public TaiKhoanBUS() {
        this.taiKhoanDAO = new TaiKhoanDAO();
    }
    
  
    public boolean taoTaiKhoan(TaiKhoan taiKhoan) {
        try {
            // Validate mật khẩu trước khi mã hóa
            if (!taiKhoan.getMatKhau().matches(TaiKhoan.MAT_KHAU_REGEX)) {
                throw new Exception(TaiKhoan.MAT_KHAU_SAI);
            }
            
            // Mã hóa mật khẩu trước khi lưu
            String matKhauGoc = taiKhoan.getMatKhau();
            String matKhauDaMaHoa = PasswordUtil.encode(matKhauGoc);
            taiKhoan.setMatKhauDaMaHoa(matKhauDaMaHoa);
            
            return taiKhoanDAO.insert(taiKhoan);
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo tài khoản: " + e.getMessage());
            return false;
        }
    }
    
   
    public boolean capNhatTaiKhoan(TaiKhoan taiKhoan) {
        try {
            // Kiểm tra xem có cập nhật mật khẩu không
            TaiKhoan taiKhoanCu = taiKhoanDAO.findById(taiKhoan.getTenDangNhap()).orElse(null);
            
            if (taiKhoanCu != null && !taiKhoan.getMatKhau().equals(taiKhoanCu.getMatKhau())) {
                // Validate mật khẩu mới trước khi mã hóa
                if (!taiKhoan.getMatKhau().matches(TaiKhoan.MAT_KHAU_REGEX)) {
                    throw new Exception(TaiKhoan.MAT_KHAU_SAI);
                }
                
                // Nếu mật khẩu thay đổi, mã hóa mật khẩu mới
                String matKhauDaMaHoa = PasswordUtil.encode(taiKhoan.getMatKhau());
                // Dùng setMatKhauDaMaHoa() để bypass validation
                taiKhoan.setMatKhauDaMaHoa(matKhauDaMaHoa);
            }
            
            return taiKhoanDAO.update(taiKhoan);
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật tài khoản: " + e.getMessage());
            return false;
        }
    }
    
  
    public TaiKhoan dangNhap(String maNhanVien, String matKhau) {
        TaiKhoan taiKhoan = taiKhoanDAO.dangNhap(maNhanVien);
        
        if (taiKhoan == null) {
            return null;
        }
        
        // Xác thực mật khẩu
        if (PasswordUtil.verify(matKhau, taiKhoan.getMatKhau())) {
            return taiKhoan;
        }
        
        return null;
    }
    
   
    public TaiKhoan layTaiKhoanTheoMaNhanVien(String maNhanVien) {
        return taiKhoanDAO.timTheoMaNhanVien(maNhanVien).orElse(null);
    }
    
   
    public TaiKhoan layTaiKhoanTheoTenDangNhap(String tenDangNhap) {
        return taiKhoanDAO.findById(tenDangNhap).orElse(null);
    }
    
  
    public boolean kiemTraTonTai(String tenDangNhap) {
        return taiKhoanDAO.tonTai(tenDangNhap);
    }
    
   
    public boolean kiemTraNhanVienCoTaiKhoan(String maNhanVien) {
        return taiKhoanDAO.tonTaiTheoMaNhanVien(maNhanVien);
    }
    
   
    public boolean doiMatKhau(String tenDangNhap, String matKhauCu, String matKhauMoi) {
        try {
            TaiKhoan taiKhoan = taiKhoanDAO.findById(tenDangNhap).orElse(null);
            
            if (taiKhoan == null) {
                return false;
            }
            
            // Xác thực mật khẩu cũ
            if (!PasswordUtil.verify(matKhauCu, taiKhoan.getMatKhau())) {
                return false;
            }
            
            // Validate mật khẩu mới trước khi mã hóa
            if (!matKhauMoi.matches(TaiKhoan.MAT_KHAU_REGEX)) {
                throw new Exception(TaiKhoan.MAT_KHAU_SAI);
            }
            
            // Mã hóa và cập nhật mật khẩu mới
            String matKhauMoiDaMaHoa = PasswordUtil.encode(matKhauMoi);
            taiKhoan.setMatKhauDaMaHoa(matKhauMoiDaMaHoa);
            
            return taiKhoanDAO.update(taiKhoan);
        } catch (Exception e) {
            System.out.println("Lỗi khi đổi mật khẩu: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean resetMatKhau(String tenDangNhap, String matKhauMacDinh) {
        try {
            TaiKhoan taiKhoan = taiKhoanDAO.findById(tenDangNhap).orElse(null);
            
            if (taiKhoan == null) {
                return false;
            }
            
            // Validate mật khẩu mới trước khi mã hóa
            if (!matKhauMacDinh.matches(TaiKhoan.MAT_KHAU_REGEX)) {
                throw new Exception(TaiKhoan.MAT_KHAU_SAI);
            }
            
            // Mã hóa và cập nhật mật khẩu mới
            String matKhauDaMaHoa = PasswordUtil.encode(matKhauMacDinh);
            taiKhoan.setMatKhauDaMaHoa(matKhauDaMaHoa);
            
            return taiKhoanDAO.update(taiKhoan);
        } catch (Exception e) {
            System.out.println("Lỗi khi reset mật khẩu: " + e.getMessage());
            return false;
        }
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.TaiKhoan;

/**
 * Class quản lý thông tin session của người dùng đăng nhập
 * @author PhamTra
 */
public class UserSession {
    private static UserSession instance;
    private TaiKhoan taiKhoan;
    private NhanVien nhanVien;
    
    private UserSession() {
        // Private constructor để implement Singleton pattern
    }
    
    /**
     * Lấy instance của UserSession (Singleton)
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    /**
     * Đăng nhập - Lưu thông tin người dùng vào session
     */
    public void login(TaiKhoan taiKhoan, NhanVien nhanVien) {
        this.taiKhoan = taiKhoan;
        this.nhanVien = nhanVien;
    }
    
    /**
     * Đăng xuất - Xóa thông tin session
     */
    public void logout() {
        this.taiKhoan = null;
        this.nhanVien = null;
    }
    
    /**
     * Kiểm tra xem người dùng đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return taiKhoan != null && nhanVien != null;
    }
    
    /**
     * Lấy thông tin tài khoản hiện tại
     */
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }
    
    /**
     * Lấy thông tin nhân viên hiện tại
     */
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    /**
     * Kiểm tra xem người dùng có phải là quản lý không
     */
    public boolean isQuanLy() {
        return nhanVien != null && "Quản lý".equalsIgnoreCase(nhanVien.getVaiTro());
    }
    
    /**
     * Lấy tên nhân viên hiện tại
     */
    public String getTenNhanVien() {
        return nhanVien != null ? nhanVien.getTenNhanVien() : "";
    }
    
    /**
     * Lấy mã nhân viên hiện tại
     */
    public String getMaNhanVien() {
        return nhanVien != null ? nhanVien.getMaNhanVien() : "";
    }
}



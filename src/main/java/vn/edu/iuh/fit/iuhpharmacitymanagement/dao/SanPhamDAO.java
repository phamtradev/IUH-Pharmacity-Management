/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author PhamTra
 */
public class SanPhamDAO implements DAOInterface<SanPham, String> {


    private final String SQL_THEM = 
            "INSERT INTO SanPham (maSanPham, tenSanPham, soDangKy, hoatChat, lieuDung, " +
            "cachDongGoi, quocGiaSanXuat, nhaSanXuat, giaNhap, giaBan, hoatDong, thueVAT, hinhAnh, loaiSanPham, maDonVi) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private final String SQL_CAP_NHAT = 
            "UPDATE SanPham SET tenSanPham = ?, soDangKy = ?, hoatChat = ?, lieuDung = ?, " +
            "cachDongGoi = ?, quocGiaSanXuat = ?, nhaSanXuat = ?, giaNhap = ?, giaBan = ?, hoatDong = ?, " +
            "thueVAT = ?, hinhAnh = ?, loaiSanPham = ?, maDonVi = ? WHERE maSanPham = ?";
    
    private final String SQL_TIM_THEO_MA = 
            "SELECT sp.*, dv.tenDonVi AS donViTinhTen " +
            "FROM SanPham sp " +
            "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi " +
            "WHERE sp.maSanPham = ?";
    
    private final String SQL_TIM_TAT_CA = 
            "SELECT sp.*, dv.tenDonVi AS donViTinhTen " +
            "FROM SanPham sp " +
            "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi";
    
    private final String SQL_TIM_THEO_TEN = 
            "SELECT sp.*, dv.tenDonVi AS donViTinhTen " +
            "FROM SanPham sp " +
            "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi " +
            "WHERE LOWER(sp.tenSanPham) LIKE LOWER(?)";
    
    private final String SQL_TIM_THEO_LOAI = 
            "SELECT sp.*, dv.tenDonVi AS donViTinhTen " +
            "FROM SanPham sp " +
            "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi " +
            "WHERE sp.loaiSanPham = ?";
    
    private final String SQL_TIM_THEO_HOAT_DONG = 
            "SELECT sp.*, dv.tenDonVi AS donViTinhTen " +
            "FROM SanPham sp " +
            "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi " +
            "WHERE sp.hoatDong = ?";
    
    private final String SQL_LAY_MA_CUOI = 
            "SELECT TOP 1 maSanPham FROM SanPham ORDER BY maSanPham DESC";
    
    private final String SQL_TIM_THEO_SO_DANG_KY = 
            "SELECT sp.*, dv.tenDonVi AS donViTinhTen " +
            "FROM SanPham sp " +
            "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi " +
            "WHERE sp.soDangKy = ?";
    

    @Override
    public boolean insert(SanPham sanPham) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {
            
            stmt.setString(1, sanPham.getMaSanPham());
            stmt.setString(2, sanPham.getTenSanPham());
            stmt.setString(3, sanPham.getSoDangKy());
            stmt.setString(4, sanPham.getHoatChat());
            stmt.setString(5, sanPham.getLieuDung());
            stmt.setString(6, sanPham.getCachDongGoi());
            stmt.setString(7, sanPham.getQuocGiaSanXuat());
            stmt.setString(8, sanPham.getNhaSanXuat());
            stmt.setDouble(9, sanPham.getGiaNhap());
            stmt.setDouble(10, sanPham.getGiaBan());
            stmt.setBoolean(11, sanPham.isHoatDong());
            stmt.setDouble(12, sanPham.getThueVAT());
            stmt.setString(13, sanPham.getHinhAnh());
            stmt.setString(14, sanPham.getLoaiSanPham() != null ? sanPham.getLoaiSanPham().name() : null);
            stmt.setString(15, sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getMaDonVi() : null);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(SanPham sanPham) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {
            
            stmt.setString(1, sanPham.getTenSanPham());
            stmt.setString(2, sanPham.getSoDangKy());
            stmt.setString(3, sanPham.getHoatChat());
            stmt.setString(4, sanPham.getLieuDung());
            stmt.setString(5, sanPham.getCachDongGoi());
            stmt.setString(6, sanPham.getQuocGiaSanXuat());
            stmt.setString(7, sanPham.getNhaSanXuat());
            stmt.setDouble(8, sanPham.getGiaNhap());
            stmt.setDouble(9, sanPham.getGiaBan());
            stmt.setBoolean(10, sanPham.isHoatDong());
            stmt.setDouble(11, sanPham.getThueVAT());
            stmt.setString(12, sanPham.getHinhAnh());
            stmt.setString(13, sanPham.getLoaiSanPham() != null ? sanPham.getLoaiSanPham().name() : null);
            stmt.setString(14, sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getMaDonVi() : null);
            stmt.setString(15, sanPham.getMaSanPham());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<SanPham> findById(String maSanPham) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {
            
            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<SanPham> findAll() {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                danhSachSanPham.add(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }
    
    /**
     * Chuyển đổi ResultSet thành đối tượng SanPham
     */
    private SanPham mapResultSetToSanPham(ResultSet rs) throws SQLException {
        SanPham sanPham = new SanPham();
        
        try {
            sanPham.setMaSanPham(rs.getString("maSanPham"));
            sanPham.setTenSanPham(rs.getString("tenSanPham"));
            sanPham.setSoDangKy(rs.getString("soDangKy"));
            sanPham.setHoatChat(rs.getString("hoatChat"));
            sanPham.setLieuDung(rs.getString("lieuDung"));
            sanPham.setCachDongGoi(rs.getString("cachDongGoi"));
            sanPham.setQuocGiaSanXuat(rs.getString("quocGiaSanXuat"));
            sanPham.setNhaSanXuat(rs.getString("nhaSanXuat"));
            sanPham.setGiaNhap(rs.getDouble("giaNhap"));
            sanPham.setGiaBan(rs.getDouble("giaBan"));
            sanPham.setHoatDong(rs.getBoolean("hoatDong"));
            sanPham.setThueVAT(rs.getDouble("thueVAT"));
            sanPham.setHinhAnh(rs.getString("hinhAnh"));
            
            // Set loại sản phẩm
            String loaiSanPhamStr = rs.getString("loaiSanPham");
            if (loaiSanPhamStr != null) {
                sanPham.setLoaiSanPham(LoaiSanPham.valueOf(loaiSanPhamStr));
            }
            
            // Set đơn vị tính
            String maDonVi = rs.getString("maDonVi");
            if (maDonVi != null) {
                DonViTinh donViTinh = new DonViTinh();
                donViTinh.setMaDonVi(maDonVi);
                String tenDonVi = rs.getString("donViTinhTen");
                donViTinh.setTenDonVi(tenDonVi);
                sanPham.setDonViTinh(donViTinh);
            }
        } catch (Exception e) {
            throw new SQLException("Lỗi khi mapping ResultSet sang SanPham", e);
        }
        
        return sanPham;
    }
    
    /**
     * Tìm sản phẩm theo tên (tìm kiếm gần đúng)
     */
    public List<SanPham> findByName(String tenSanPham) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN)) {
            
            String searchPattern = "%" + tenSanPham + "%";
            System.out.println("🔍 DEBUG - Tìm kiếm sản phẩm với pattern: " + searchPattern);
            stmt.setString(1, searchPattern);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                danhSachSanPham.add(mapResultSetToSanPham(rs));
            }
            System.out.println("✅ DEBUG - Tìm thấy " + danhSachSanPham.size() + " sản phẩm");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }
    
    /**
     * Tìm sản phẩm theo loại
     */
    public List<SanPham> findByLoai(LoaiSanPham loaiSanPham) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_LOAI)) {
            
            stmt.setString(1, loaiSanPham.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                danhSachSanPham.add(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }
    
    /**
     * Tìm sản phẩm đang hoạt động
     */
    public List<SanPham> findByHoatDong(boolean hoatDong) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_HOAT_DONG)) {
            
            stmt.setBoolean(1, hoatDong);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                danhSachSanPham.add(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }
    
    /**
     * Lấy mã sản phẩm mới nhất
     */
    public String getLastMaSanPham() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getString("maSanPham");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Kiểm tra sản phẩm có tồn tại không
     */
    public boolean exists(String maSanPham) {
        return findById(maSanPham).isPresent();
    }
    
    /**
     * Tìm sản phẩm theo số đăng ký
     */
    public Optional<SanPham> findBySoDangKy(String soDangKy) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_SO_DANG_KY)) {
            
            stmt.setString(1, soDangKy);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToSanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

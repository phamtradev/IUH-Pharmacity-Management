package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonViTinhDAO implements DAOInterface<DonViTinh, String> {

    private final String SQL_THEM = 
            "INSERT INTO DonViTinh (maDonVi, tenDonVi) VALUES (?, ?)";
    
    private final String SQL_CAP_NHAT = 
            "UPDATE DonViTinh SET tenDonVi = ? WHERE maDonVi = ?";
    
    private final String SQL_TIM_THEO_MA = 
            "SELECT * FROM DonViTinh WHERE maDonVi = ?";
    
    private final String SQL_TIM_TAT_CA = 
            "SELECT * FROM DonViTinh";
    
    private final String SQL_TIM_THEO_TEN = 
            "SELECT * FROM DonViTinh WHERE tenDonVi = ?";
    
    private final String SQL_TIM_THEO_TEN_GAN_DUNG = 
            "SELECT * FROM DonViTinh WHERE tenDonVi LIKE ?";
    
    private final String SQL_LAY_MA_CUOI = 
            "SELECT TOP 1 maDonVi FROM DonViTinh ORDER BY maDonVi DESC";

    @Override
    public boolean insert(DonViTinh donViTinh) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {
            
            if (donViTinh.getMaDonVi() == null || donViTinh.getMaDonVi().trim().isEmpty()) {
                donViTinh.setMaDonVi(taoMaDonVi());
            }
            
            stmt.setString(1, donViTinh.getMaDonVi());
            stmt.setString(2, donViTinh.getTenDonVi());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(DonViTinh donViTinh) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {
            
            stmt.setString(1, donViTinh.getTenDonVi());
            stmt.setString(2, donViTinh.getMaDonVi());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<DonViTinh> findById(String maDonVi) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {
            
            stmt.setString(1, maDonVi);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToDonViTinh(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<DonViTinh> findAll() {
        List<DonViTinh> danhSachDonVi = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                danhSachDonVi.add(mapResultSetToDonViTinh(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachDonVi;
    }

    private DonViTinh mapResultSetToDonViTinh(ResultSet rs) throws SQLException {
        DonViTinh donViTinh = new DonViTinh();
        
        donViTinh.setMaDonVi(rs.getString("maDonVi"));
        donViTinh.setTenDonVi(rs.getString("tenDonVi"));
        
        return donViTinh;
    }

    public DonViTinh findByName(String tenDonVi) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN)) {
            
            stmt.setString(1, tenDonVi);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToDonViTinh(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DonViTinh> findByNameSearch(String tenDonVi) {
        List<DonViTinh> danhSachDonVi = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN_GAN_DUNG)) {
            
            stmt.setString(1, "%" + tenDonVi + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                danhSachDonVi.add(mapResultSetToDonViTinh(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachDonVi;
    }

    private String taoMaDonVi() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String maCuoi = rs.getString("maDonVi");
                String phanSo = maCuoi.substring(3);
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("DVT%06d", soTiepTheo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "DVT000001";
    }

    public String getLastMaDonVi() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getString("maDonVi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists(String maDonVi) {
        return findById(maDonVi).isPresent();
    }

    public boolean existsByName(String tenDonVi) {
        return findByName(tenDonVi) != null;
    }

    public int count() {
        String sql = "SELECT COUNT(*) as total FROM DonViTinh";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

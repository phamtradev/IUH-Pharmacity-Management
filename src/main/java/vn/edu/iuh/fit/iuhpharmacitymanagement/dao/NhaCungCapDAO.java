package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NhaCungCapDAO implements DAOInterface<NhaCungCap, String> {
    
    private final String SQL_THEM = 
            "INSERT INTO NhaCungCap (maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email, maSoThue) VALUES (?, ?, ?, ?, ?, ?)";
    
    private final String SQL_CAP_NHAT = 
            "UPDATE NhaCungCap SET tenNhaCungCap = ?, diaChi = ?, soDienThoai = ?, email = ?, maSoThue = ? WHERE maNhaCungCap = ?";
    
    private final String SQL_TIM_THEO_MA = 
            "SELECT * FROM NhaCungCap WHERE maNhaCungCap = ?";
    
    private final String SQL_TIM_TAT_CA = 
            "SELECT * FROM NhaCungCap";
    
    private final String SQL_TIM_THEO_TEN = 
            "SELECT * FROM NhaCungCap WHERE tenNhaCungCap = ?";
    
    private final String SQL_TIM_THEO_SO_DIEN_THOAI = 
            "SELECT * FROM NhaCungCap WHERE soDienThoai = ?";
    
    private final String SQL_TIM_THEO_TEXT = 
            "SELECT * FROM NhaCungCap WHERE tenNhaCungCap LIKE ? OR email LIKE ? OR soDienThoai LIKE ?";
    
    private final String SQL_LAY_MA_CUOI = 
            "SELECT TOP 1 maNhaCungCap FROM NhaCungCap ORDER BY maNhaCungCap DESC";

    @Override
    public boolean insert(NhaCungCap nhaCungCap) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {
            
            if (nhaCungCap.getMaNhaCungCap() == null || nhaCungCap.getMaNhaCungCap().trim().isEmpty()) {
                try {
                    nhaCungCap.setMaNhaCungCap(taoMaNhaCungCap());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            
            stmt.setString(1, nhaCungCap.getMaNhaCungCap());
            stmt.setString(2, nhaCungCap.getTenNhaCungCap());
            stmt.setString(3, nhaCungCap.getDiaChi());
            stmt.setString(4, nhaCungCap.getSoDienThoai());
            stmt.setString(5, nhaCungCap.getEmail());
            stmt.setString(6, nhaCungCap.getMaSoThue());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(NhaCungCap nhaCungCap) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {
            
            stmt.setString(1, nhaCungCap.getTenNhaCungCap());
            stmt.setString(2, nhaCungCap.getDiaChi());
            stmt.setString(3, nhaCungCap.getSoDienThoai());
            stmt.setString(4, nhaCungCap.getEmail());
            stmt.setString(5, nhaCungCap.getMaSoThue());
            stmt.setString(6, nhaCungCap.getMaNhaCungCap());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<NhaCungCap> findById(String maNhaCungCap) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {
            
            stmt.setString(1, maNhaCungCap);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToNhaCungCap(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<NhaCungCap> findAll() {
        List<NhaCungCap> danhSachNhaCungCap = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                danhSachNhaCungCap.add(mapResultSetToNhaCungCap(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhaCungCap;
    }

    public NhaCungCap timTheoTen(String tenNhaCungCap) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN)) {
            
            stmt.setString(1, tenNhaCungCap);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNhaCungCap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NhaCungCap timTheoSoDienThoai(String soDienThoai) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_SO_DIEN_THOAI)) {
            
            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNhaCungCap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhaCungCap> timTheoText(String text) {
        List<NhaCungCap> danhSachNhaCungCap = new ArrayList<>();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEXT)) {
            
            String searchPattern = "%" + text + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                danhSachNhaCungCap.add(mapResultSetToNhaCungCap(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhaCungCap;
    }

    private NhaCungCap mapResultSetToNhaCungCap(ResultSet rs) throws SQLException {
        try {
            NhaCungCap nhaCungCap = new NhaCungCap();
            
            nhaCungCap.setMaNhaCungCap(rs.getString("maNhaCungCap"));
            nhaCungCap.setTenNhaCungCap(rs.getString("tenNhaCungCap"));
            nhaCungCap.setDiaChi(rs.getString("diaChi"));
            nhaCungCap.setSoDienThoai(rs.getString("soDienThoai"));
            nhaCungCap.setEmail(rs.getString("email"));
            nhaCungCap.setMaSoThue(rs.getString("maSoThue"));
            
            return nhaCungCap;
        } catch (Exception e) {
            throw new SQLException("Lỗi khi map dữ liệu từ ResultSet: " + e.getMessage(), e);
        }
    }

    private String taoMaNhaCungCap() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String maCuoi = rs.getString("maNhaCungCap");
                String phanSo = maCuoi.substring(3);
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("NCC%04d", soTiepTheo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "NCC0001";
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NhanVienDAO implements DAOInterface<NhanVien, String> {

    private final String SQL_THEM = "INSERT INTO NhanVien (maNhanVien, tenNhanVien, diaChi, soDienThoai, email, vaiTro, gioiTinh, anhNhanVien, cccd) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT = "UPDATE NhanVien SET tenNhanVien = ?, diaChi = ?, soDienThoai = ?, email = ?, vaiTro = ?, gioiTinh = ?, anhNhanVien = ?, cccd = ? WHERE maNhanVien = ?";

    private final String SQL_TIM_THEO_MA = "SELECT * FROM NhanVien WHERE maNhanVien = ?";

    private final String SQL_TIM_TAT_CA = "SELECT * FROM NhanVien";

    private final String SQL_TIM_THEO_TU_KHOA = "SELECT * FROM NhanVien WHERE tenNhanVien LIKE ? OR soDienThoai LIKE ? OR email LIKE ?";

    private final String SQL_LAY_MA_CUOI = "SELECT TOP 1 maNhanVien FROM NhanVien ORDER BY maNhanVien DESC";

    @Override
    public boolean insert(NhanVien nhanVien) {
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (nhanVien.getMaNhanVien() == null || nhanVien.getMaNhanVien().trim().isEmpty()) {
                try {
                    nhanVien.setMaNhanVien(taoMaNhanVien());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            stmt.setString(1, nhanVien.getMaNhanVien());
            stmt.setString(2, nhanVien.getTenNhanVien());
            stmt.setString(3, nhanVien.getDiaChi());
            stmt.setString(4, nhanVien.getSoDienThoai());
            stmt.setString(5, nhanVien.getEmail());
            stmt.setString(6, nhanVien.getVaiTro());
            stmt.setString(7, nhanVien.getGioiTinh());
            stmt.setString(8, nhanVien.getAnhNhanVien());
            stmt.setString(9, nhanVien.getCccd());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(NhanVien nhanVien) {
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, nhanVien.getTenNhanVien());
            stmt.setString(2, nhanVien.getDiaChi());
            stmt.setString(3, nhanVien.getSoDienThoai());
            stmt.setString(4, nhanVien.getEmail());
            stmt.setString(5, nhanVien.getVaiTro());
            stmt.setString(6, nhanVien.getGioiTinh());
            stmt.setString(7, nhanVien.getAnhNhanVien());
            stmt.setString(8, nhanVien.getCccd());
            stmt.setString(9, nhanVien.getMaNhanVien());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<NhanVien> findById(String maNhanVien) {
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<NhanVien> findAll() {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSachNhanVien.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    public List<NhanVien> findByKeyword(String keyword) {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TU_KHOA)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachNhanVien.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        // pass validation
        NhanVien nv = new NhanVien(
                rs.getString("maNhanVien"),
                rs.getString("tenNhanVien"),
                rs.getString("diaChi"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getString("vaiTro"),
                rs.getString("gioiTinh"));
        nv.setAnhNhanVien(rs.getString("anhNhanVien"));
        nv.setCccd(rs.getString("cccd"));
        return nv;
    }

    private String taoMaNhanVien() {
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maNhanVien");
                String phanSo = maCuoi.substring(2); // Bỏ "NV"
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("NV%05d", soTiepTheo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "NV00001";
    }

    public String getLastMaNhanVien() {
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maNhanVien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists(String maNhanVien) {
        return findById(maNhanVien).isPresent();
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) as total FROM NhanVien WHERE email = ?";

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhone(String soDienThoai) {
        String sql = "SELECT COUNT(*) as total FROM NhanVien WHERE soDienThoai = ?";

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int count() {
        String sql = "SELECT COUNT(*) as total FROM NhanVien";

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

    public boolean delete(String maNhanVien) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";

        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maNhanVien);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

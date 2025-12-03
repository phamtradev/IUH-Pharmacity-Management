package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BangLaiChuanDAO implements DAOInterface<BangLaiChuan, Integer> {

    private static final String SQL_FIND_ALL =
            "SELECT id, giaNhapTu, giaNhapDen, tyLeLai, loaiSanPham FROM BangLaiChuan ORDER BY giaNhapTu ASC";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, giaNhapTu, giaNhapDen, tyLeLai, loaiSanPham FROM BangLaiChuan WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO BangLaiChuan (giaNhapTu, giaNhapDen, tyLeLai, loaiSanPham) VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE BangLaiChuan SET giaNhapTu = ?, giaNhapDen = ?, tyLeLai = ?, loaiSanPham = ? WHERE id = ?";

    private static final String SQL_DELETE_ALL =
            "DELETE FROM BangLaiChuan";

    @Override
    public boolean insert(BangLaiChuan b) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_INSERT)) {

            stmt.setDouble(1, b.getGiaNhapTu());
            if (b.getGiaNhapDen() > 0) {
                stmt.setDouble(2, b.getGiaNhapDen());
            } else {
                stmt.setNull(2, Types.DECIMAL);
            }
            stmt.setDouble(3, b.getTyLeLai());
            setLoaiSanPhamParam(stmt, 4, b.getLoaiSanPham());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(BangLaiChuan b) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_UPDATE)) {

            stmt.setDouble(1, b.getGiaNhapTu());
            if (b.getGiaNhapDen() > 0) {
                stmt.setDouble(2, b.getGiaNhapDen());
            } else {
                stmt.setNull(2, Types.DECIMAL);
            }
            stmt.setDouble(3, b.getTyLeLai());
            setLoaiSanPhamParam(stmt, 4, b.getLoaiSanPham());
            stmt.setInt(5, b.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAll() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_DELETE_ALL)) {
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<BangLaiChuan> findById(Integer id) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BangLaiChuan> findAll() {
        List<BangLaiChuan> list = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BangLaiChuan mapRow(ResultSet rs) throws SQLException {
        BangLaiChuan b = new BangLaiChuan();
        b.setId(rs.getInt("id"));
        b.setGiaNhapTu(rs.getDouble("giaNhapTu"));
        double den = rs.getDouble("giaNhapDen");
        if (rs.wasNull()) {
            den = 0;
        }
        b.setGiaNhapDen(den);
        b.setTyLeLai(rs.getDouble("tyLeLai"));
        String loai = null;
        try {
            loai = rs.getString("loaiSanPham");
        } catch (SQLException ex) {
            // Cột có thể chưa tồn tại ở các schema cũ
        }
        if (loai != null && !loai.isBlank()) {
            try {
                b.setLoaiSanPham(LoaiSanPham.valueOf(loai));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return b;
    }

    private void setLoaiSanPhamParam(PreparedStatement stmt, int index, LoaiSanPham loaiSanPham) throws SQLException {
        if (loaiSanPham != null) {
            stmt.setString(index, loaiSanPham.name());
        } else {
            stmt.setNull(index, Types.VARCHAR);
        }
    }
}



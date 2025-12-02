package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO cho bảng BangLaiChuan (cấu hình lãi chuẩn theo khoảng giá nhập).
 *
 * SQL gợi ý:
 * <pre>
 * CREATE TABLE BangLaiChuan (
 *     id INT IDENTITY(1,1) PRIMARY KEY,
 *     giaNhapTu DECIMAL(18,0) NOT NULL,
 *     giaNhapDen DECIMAL(18,0) NULL,
 *     tyLeLai DECIMAL(5,4) NOT NULL -- 0.2000 = 20%
 * );
 * </pre>
 */
public class BangLaiChuanDAO implements DAOInterface<BangLaiChuan, Integer> {

    private static final String SQL_FIND_ALL =
            "SELECT id, giaNhapTu, giaNhapDen, tyLeLai FROM BangLaiChuan ORDER BY giaNhapTu ASC";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, giaNhapTu, giaNhapDen, tyLeLai FROM BangLaiChuan WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO BangLaiChuan (giaNhapTu, giaNhapDen, tyLeLai) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE BangLaiChuan SET giaNhapTu = ?, giaNhapDen = ?, tyLeLai = ? WHERE id = ?";

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
            stmt.setInt(4, b.getId());

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
        return b;
    }
}



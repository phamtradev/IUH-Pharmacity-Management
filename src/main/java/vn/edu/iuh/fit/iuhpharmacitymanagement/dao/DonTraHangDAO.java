package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonTraHangDAO implements DAOInterface<DonTraHang, String> {

    private final String SQL_THEM =
            "INSERT INTO DonTraHang (maDonTraHang, ngayTraHang, thanhTien, maNhanVien, maDonHang) VALUES (?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE DonTraHang SET ngayTraHang = ?, thanhTien = ?, maNhanVien = ?, maDonHang = ? WHERE maDonTraHang = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM DonTraHang WHERE maDonTraHang = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM DonTraHang";

    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maDonTraHang FROM DonTraHang ORDER BY maDonTraHang DESC";


    @Override
    public boolean insert(DonTraHang donTraHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (donTraHang.getMaDonTraHang() == null || donTraHang.getMaDonTraHang().trim().isEmpty()) {
                donTraHang.setMaDonTraHang(taoMaDonTraHangMoi());
            }

            stmt.setString(1, donTraHang.getMaDonTraHang());
            stmt.setDate(2, Date.valueOf(donTraHang.getNgayTraHang()));
            stmt.setDouble(3, donTraHang.getThanhTien());
            stmt.setString(4, donTraHang.getNhanVien() != null ? donTraHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(5, donTraHang.getDonHang() != null ? donTraHang.getDonHang().getMaDonHang() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    @Override
    public boolean update(DonTraHang donTraHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setDate(1, Date.valueOf(donTraHang.getNgayTraHang()));
            stmt.setDouble(2, donTraHang.getThanhTien());
            stmt.setString(3, donTraHang.getNhanVien() != null ? donTraHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(4, donTraHang.getDonHang() != null ? donTraHang.getDonHang().getMaDonHang() : null);
            stmt.setString(5, donTraHang.getMaDonTraHang());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<DonTraHang> findById(String maDonTraHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maDonTraHang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDonTraHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<DonTraHang> findAll() {
        List<DonTraHang> danhSach = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapResultSetToDonTraHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return danhSach;
    }

    private DonTraHang mapResultSetToDonTraHang(ResultSet rs) throws SQLException, Exception {
        DonTraHang dth = new DonTraHang();

        dth.setMaDonTraHang(rs.getString("maDonTraHang"));
        dth.setNgayTraHang(rs.getDate("ngayTraHang").toLocalDate());
        dth.setThanhTien(rs.getDouble("thanhTien"));

        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        dth.setNhanVien(nv);

        DonHang dh = new DonHang();
        dh.setMaDonHang(rs.getString("maDonHang"));
        dth.setDonHang(dh);

        return dth;
    }

    private String taoMaDonTraHangMoi() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maDonTraHang");
                int so = Integer.parseInt(maCuoi.substring(3)) + 1; // ví dụ: DTH00001
                return String.format("DTH%05d", so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "DTH00001";
    }

    public boolean exists(String maDonTraHang) {
        return findById(maDonTraHang).isPresent();
    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM DonTraHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(String maDonTraHang) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<DonTraHang> timTheoText(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

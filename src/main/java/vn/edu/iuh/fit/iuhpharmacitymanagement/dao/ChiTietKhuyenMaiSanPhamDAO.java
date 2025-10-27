/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 *
 * @author LAPTOP ASUS
 */
public class ChiTietKhuyenMaiSanPhamDAO implements DAOInterface<ChiTietKhuyenMaiSanPham, String> {

    private final String SQL_THEM =
            "INSERT INTO ChiTietKhuyenMaiSanPham (maSanPham, maKhuyenMai) VALUES (?, ?)";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM ChiTietKhuyenMaiSanPham";

    private final String SQL_XOA =
            "DELETE FROM ChiTietKhuyenMaiSanPham WHERE maSanPham = ? AND maKhuyenMai = ?";

    private final String SQL_TIM_THEO_MA_KHUYEN_MAI =
            "SELECT maSanPham, maKhuyenMai FROM ChiTietKhuyenMaiSanPham WHERE maKhuyenMai = ?";

    private final String SQL_TIM_THEO_MA_SAN_PHAM =
            "SELECT * FROM ChiTietKhuyenMaiSanPham WHERE maSanPham = ?";

    private final String SQL_KIEM_TRA_TON_TAI_SAN_PHAM_VA_KHUYEN_MAI =
            "SELECT COUNT(*) AS total FROM ChiTietKhuyenMaiSanPham WHERE maSanPham = ? AND maKhuyenMai = ?";


    @Override
    public boolean insert(ChiTietKhuyenMaiSanPham chiTietKhuyenMaiSanPham) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            stmt.setString(1, chiTietKhuyenMaiSanPham.getSanPham() != null ? chiTietKhuyenMaiSanPham.getSanPham().getMaSanPham() : null);
            stmt.setString(2, chiTietKhuyenMaiSanPham.getKhuyenMai() != null ? chiTietKhuyenMaiSanPham.getKhuyenMai().getMaKhuyenMai() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietKhuyenMaiSanPhamDAO.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }


    @Override
    public boolean update(ChiTietKhuyenMaiSanPham chiTietKhuyenMaiSanPham) {
        // Bảng ChiTietKhuyenMaiSanPham không có khóa chính riêng, không hỗ trợ update
        // Nếu cần update, xóa và insert lại
        return false;
    }


    @Override
    public Optional<ChiTietKhuyenMaiSanPham> findById(String maChiTietKhuyenMaiSanPham) {
        // Bảng ChiTietKhuyenMaiSanPham không có khóa chính riêng
        // Không hỗ trợ tìm theo ID đơn lẻ
        return Optional.empty();
    }


    @Override
    public List<ChiTietKhuyenMaiSanPham> findAll() {
        List<ChiTietKhuyenMaiSanPham> ds = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ds.add(mapResultSetToChiTietKhuyenMaiSanPham(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietKhuyenMaiSanPhamDAO.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ds;
    }


    private ChiTietKhuyenMaiSanPham mapResultSetToChiTietKhuyenMaiSanPham(ResultSet rs) throws SQLException, Exception {
        ChiTietKhuyenMaiSanPham chiTietKhuyenMaiSanPham = new ChiTietKhuyenMaiSanPham();

        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getString("maSanPham"));
        chiTietKhuyenMaiSanPham.setSanPham(sp);

        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        chiTietKhuyenMaiSanPham.setKhuyenMai(km);

        return chiTietKhuyenMaiSanPham;
    }



    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM ChiTietKhuyenMaiSanPham";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(String maSanPham, String maKhuyenMai) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_XOA)) {

            stmt.setString(1, maSanPham);
            stmt.setString(2, maKhuyenMai);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ChiTietKhuyenMaiSanPham> findByMaKhuyenMai(String maKhuyenMai) {
        List<ChiTietKhuyenMaiSanPham> ds = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA_KHUYEN_MAI)) {

            stmt.setString(1, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ds.add(mapResultSetToChiTietKhuyenMaiSanPham(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietKhuyenMaiSanPhamDAO.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ds;
    }

    public List<ChiTietKhuyenMaiSanPham> findByMaSanPham(String maSanPham) {
        List<ChiTietKhuyenMaiSanPham> ds = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA_SAN_PHAM)) {

            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ds.add(mapResultSetToChiTietKhuyenMaiSanPham(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietKhuyenMaiSanPhamDAO.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ds;
    }

    public boolean existsBySanPhamVaKhuyenMai(String maSanPham, String maKhuyenMai) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_KIEM_TRA_TON_TAI_SAN_PHAM_VA_KHUYEN_MAI)) {

            stmt.setString(1, maSanPham);
            stmt.setString(2, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

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
            "INSERT INTO ChiTietKhuyenMaiSanPham (maChiTietKhuyenMaiSanPham, maSanPham, maKhuyenMai) VALUES (?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE ChiTietKhuyenMaiSanPham SET maSanPham = ?, maKhuyenMai = ? WHERE maChiTietKhuyenMaiSanPham = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM ChiTietKhuyenMaiSanPham WHERE maChiTietKhuyenMaiSanPham = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM ChiTietKhuyenMaiSanPham";

    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maChiTietKhuyenMaiSanPham FROM ChiTietKhuyenMaiSanPham ORDER BY maChiTietKhuyenMaiSanPham DESC";


    @Override
    public boolean insert(ChiTietKhuyenMaiSanPham chiTietKhuyenMaiSanPham) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

         
            if (chiTietKhuyenMaiSanPham.getMaChiTietKhuyenMaiSanPham() == null || chiTietKhuyenMaiSanPham.getMaChiTietKhuyenMaiSanPham().trim().isEmpty()) {
                chiTietKhuyenMaiSanPham.setMaChiTietKhuyenMaiSanPham(taoMaMoi());
            }

            stmt.setString(1, chiTietKhuyenMaiSanPham.getMaChiTietKhuyenMaiSanPham());
            stmt.setString(2, chiTietKhuyenMaiSanPham.getSanPham() != null ? chiTietKhuyenMaiSanPham.getSanPham().getMaSanPham() : null);
            stmt.setString(3, chiTietKhuyenMaiSanPham.getKhuyenMai() != null ? chiTietKhuyenMaiSanPham.getKhuyenMai().getMaKhuyenMai() : null);

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
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, chiTietKhuyenMaiSanPham.getSanPham() != null ? chiTietKhuyenMaiSanPham.getSanPham().getMaSanPham() : null);
            stmt.setString(2, chiTietKhuyenMaiSanPham.getKhuyenMai() != null ? chiTietKhuyenMaiSanPham.getKhuyenMai().getMaKhuyenMai() : null);
            stmt.setString(3, chiTietKhuyenMaiSanPham.getMaChiTietKhuyenMaiSanPham());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Optional<ChiTietKhuyenMaiSanPham> findById(String maChiTietKhuyenMaiSanPham) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maChiTietKhuyenMaiSanPham);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToChiTietKhuyenMaiSanPham(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietKhuyenMaiSanPhamDAO.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
        }
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

        chiTietKhuyenMaiSanPham.setMaChiTietKhuyenMaiSanPham(rs.getString("maChiTietKhuyenMaiSanPham"));

        SanPham sp = new SanPham();
        sp.setMaSanPham(rs.getString("maSanPham"));
        chiTietKhuyenMaiSanPham.setSanPham(sp);

        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        chiTietKhuyenMaiSanPham.setKhuyenMai(km);

        return chiTietKhuyenMaiSanPham;
    }


    private String taoMaMoi() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maChiTietKhuyenMaiSanPham");
                int so = Integer.parseInt(maCuoi.substring(4)) + 1;
                return String.format("CTKM%05d", so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "CTKM00001";
    }


    public boolean exists(String maChiTietKhuyenMaiSanPham) {
        return findById(maChiTietKhuyenMaiSanPham).isPresent();
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
}

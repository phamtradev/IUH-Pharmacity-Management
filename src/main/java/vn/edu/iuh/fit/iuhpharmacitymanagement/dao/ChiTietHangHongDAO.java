/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author LAPTOP ASUS
 */
public class ChiTietHangHongDAO implements DAOInterface<ChiTietHangHong, String> {
  

    private final String SQL_THEM =
            "INSERT INTO ChiTietHangHong (maChiTietHangHong, soLuong, donGia, thanhTien, maLoHang, maHangHong) VALUES (?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE ChiTietHangHong SET soLuong = ?, donGia = ?, thanhTien = ?, maLoHang = ?, maHangHong = ? WHERE maChiTietHangHong = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM ChiTietHangHong WHERE maChiTietHangHong = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM ChiTietHangHong";

    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maChiTietHangHong FROM ChiTietHangHong ORDER BY maChiTietHangHong DESC";


    @Override
    public boolean insert(ChiTietHangHong chiTietHangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (chiTietHangHong.getMaChiTietHangHong() == null || chiTietHangHong.getMaChiTietHangHong().trim().isEmpty()) {
                chiTietHangHong.setMaChiTietHangHong(taoMaMoi());
            }

            stmt.setString(1, chiTietHangHong.getMaChiTietHangHong());
            stmt.setInt(2, chiTietHangHong.getSoLuong());
            stmt.setDouble(3, chiTietHangHong.getDonGia());
            stmt.setDouble(4, chiTietHangHong.getThanhTien());
            stmt.setString(5, chiTietHangHong.getLoHang() != null ? chiTietHangHong.getLoHang().getMaLoHang() : null);
            stmt.setString(6, chiTietHangHong.getHangHong() != null ? chiTietHangHong.getHangHong().getMaHangHong() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietHangHongDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    @Override
    public boolean update(ChiTietHangHong chiTietHangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setInt(1, chiTietHangHong.getSoLuong());
            stmt.setDouble(2, chiTietHangHong.getDonGia());
            stmt.setDouble(3, chiTietHangHong.getThanhTien());
            stmt.setString(4, chiTietHangHong.getLoHang() != null ? chiTietHangHong.getLoHang().getMaLoHang() : null);
            stmt.setString(5, chiTietHangHong.getHangHong() != null ? chiTietHangHong.getHangHong().getMaHangHong() : null);
            stmt.setString(6, chiTietHangHong.getMaChiTietHangHong());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<ChiTietHangHong> findById(String maChiTietHangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maChiTietHangHong);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToChiTietHangHong(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietHangHongDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<ChiTietHangHong> findAll() {
        List<ChiTietHangHong> ds = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ds.add(mapResultSetToChiTietHangHong(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(ChiTietHangHongDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return ds;
    }


    private ChiTietHangHong mapResultSetToChiTietHangHong(ResultSet rs) throws SQLException, Exception {
        ChiTietHangHong cthh = new ChiTietHangHong();

        cthh.setMaChiTietHangHong(rs.getString("maChiTietHangHong"));
        cthh.setSoLuong(rs.getInt("soLuong"));
        cthh.setDonGia(rs.getDouble("donGia"));
        cthh.setThanhTien(rs.getDouble("thanhTien"));

        LoHang loHang = new LoHang();
        loHang.setMaLoHang(rs.getString("maLoHang"));
        cthh.setLoHang(loHang);

        HangHong hangHong = new HangHong();
        hangHong.setMaHangHong(rs.getString("maHangHong"));
        cthh.setHangHong(hangHong);

        return cthh;
    }


    private String taoMaMoi() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maChiTietHangHong");
                int so = Integer.parseInt(maCuoi.substring(3)) + 1;
                return String.format("CTH%05d", so);         // CHÚ Ý HÔM SAU XEM LẠI DÒNG NÀY
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "CTH00001";       // DÒNG NÀY NỮA
    }


    public boolean exists(String maChiTietHangHong) {
        return findById(maChiTietHangHong).isPresent();
    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM ChiTietHangHong";
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
    
    
    
    
    
    
    
    

 

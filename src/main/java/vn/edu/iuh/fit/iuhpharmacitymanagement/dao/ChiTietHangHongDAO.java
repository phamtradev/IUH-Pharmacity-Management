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
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietHangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.HangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;

/**
 *
 * @author LAPTOP ASUS
 */
public class ChiTietHangHongDAO implements DAOInterface<ChiTietHangHong, String> {
  

    private final String SQL_THEM =
            "INSERT INTO ChiTietHangHong (soLuong, donGia, thanhTien, maLoHang, maHangHong) VALUES (?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE ChiTietHangHong SET soLuong = ?, donGia = ?, thanhTien = ? WHERE maLoHang = ? AND maHangHong = ?";

    private final String SQL_TIM_THEO_MA_HANG_HONG =
            "SELECT * FROM ChiTietHangHong WHERE maHangHong = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM ChiTietHangHong";


    @Override
    public boolean insert(ChiTietHangHong chiTietHangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            stmt.setInt(1, chiTietHangHong.getSoLuong());
            stmt.setDouble(2, chiTietHangHong.getDonGia());
            stmt.setDouble(3, chiTietHangHong.getThanhTien());
            stmt.setString(4, chiTietHangHong.getLoHang() != null ? chiTietHangHong.getLoHang().getMaLoHang() : null);
            stmt.setString(5, chiTietHangHong.getHangHong() != null ? chiTietHangHong.getHangHong().getMaHangHong() : null);

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

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<ChiTietHangHong> findById(String maHangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA_HANG_HONG)) {

            stmt.setString(1, maHangHong);
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
        ChiTietHangHong chiTietHangHong = new ChiTietHangHong();

        chiTietHangHong.setSoLuong(rs.getInt("soLuong"));
        chiTietHangHong.setDonGia(rs.getDouble("donGia"));
        chiTietHangHong.setThanhTien(rs.getDouble("thanhTien"));

        // Load đầy đủ thông tin LoHang (bao gồm SanPham và DonViTinh)
        String maLoHang = rs.getString("maLoHang");
        LoHangDAO loHangDAO = new LoHangDAO();
        LoHang loHang = loHangDAO.findById(maLoHang).orElse(new LoHang(maLoHang));
        chiTietHangHong.setLoHang(loHang);

        // Load đầy đủ thông tin HangHong (bao gồm NhanVien)
        String maHangHong = rs.getString("maHangHong");
        HangHongDAO hangHongDAO = new HangHongDAO();
        HangHong hangHong = hangHongDAO.findById(maHangHong).orElseGet(() -> {
            HangHong hh = new HangHong();
            try {
                hh.setMaHangHong(maHangHong);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return hh;
        });
        chiTietHangHong.setHangHong(hangHong);

        return chiTietHangHong;
    }


    public boolean exists(String maHangHong) {
        return findById(maHangHong).isPresent();
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
    
    
    
    
    
    
    
    

 

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

/**
 *
 * @author User
 */
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhachHangDAO implements DAOInterface<KhachHang, String> {

    private final String SQL_THEM =
            "INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diaChi, email, gioiTinh) VALUES (?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE KhachHang SET tenKhachHang = ?, soDienThoai = ?, diaChi = ?, email = ?, gioiTinh = ? WHERE maKhachHang = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM KhachHang WHERE maKhachHang = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM KhachHang";

    private final String SQL_TIM_THEO_TEN_GAN_DUNG =
            "SELECT * FROM KhachHang WHERE tenKhachHang LIKE ?";

    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maKhachHang FROM KhachHang ORDER BY maKhachHang DESC";

    @Override
    public boolean insert(KhachHang kh) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            //không có mã thì tự sinh
            if (kh.getMaKhachHang() == null || kh.getMaKhachHang().trim().isEmpty()) {
                kh.setMaKhachHang(taoMaKhachHang());
            }

            stmt.setString(1, kh.getMaKhachHang());
            stmt.setString(2, kh.getTenKhachHang());
            stmt.setString(3, kh.getSoDienThoai());
            stmt.setString(4, kh.getDiaChi());
            stmt.setString(5, kh.getEmail());
            stmt.setString(6, kh.getGioiTinh());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(KhachHang kh) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, kh.getTenKhachHang());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setString(3, kh.getDiaChi());
            stmt.setString(4, kh.getEmail());
            stmt.setString(5, kh.getGioiTinh());
            stmt.setString(6, kh.getMaKhachHang());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<KhachHang> findById(String maKH) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToKhachHang(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<KhachHang> findAll() {
        List<KhachHang> danhSach = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapResultSetToKhachHang(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public List<KhachHang> findByName(String tenGanDung) {
        List<KhachHang> danhSach = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN_GAN_DUNG)) {

            stmt.setString(1, "%" + tenGanDung + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSach.add(mapResultSetToKhachHang(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws Exception {
        KhachHang kh = new KhachHang();

        kh.setMaKhachHang(rs.getString("maKhachHang"));
        kh.setTenKhachHang(rs.getString("tenKhachHang"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));
        kh.setDiaChi(rs.getString("diaChi"));
        kh.setEmail(rs.getString("email"));
        kh.setGioiTinh(rs.getString("gioiTinh"));

        return kh;
    }

    private String taoMaKhachHang() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maKhachHang"); // VD: KH00010
                int so = Integer.parseInt(maCuoi.substring(2)); // Lấy phần số: 00010
                return String.format("KH%05d", so + 1); // Tăng lên 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KH00001";
    }
    
    // Tìm KH by Sđt
    public Optional<KhachHang> findBySoDienThoai(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToKhachHang(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //TÌm KH by Email
    public Optional<KhachHang> findByEmail(String email) {
        String sql = "SELECT * FROM KhachHang WHERE email = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToKhachHang(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // Xoas 1 kH dựa trên mã KH
    public boolean delete(String maKhachHang) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhachHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

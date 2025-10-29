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
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhuyenMaiDAO implements DAOInterface<KhuyenMai, String> {

    private final String SQL_THEM
            = "INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, giamGia, trangThai, loaiKhuyenMai) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT
            = "UPDATE KhuyenMai SET tenKhuyenMai = ?, ngayBatDau = ?, ngayKetThuc = ?, giamGia = ?, trangThai = ?, loaiKhuyenMai = ? WHERE maKhuyenMai = ?";

    private final String SQL_TIM_THEO_MA
            = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";

    private final String SQL_TIM_TAT_CA
            = "SELECT * FROM KhuyenMai";

    private final String SQL_TIM_THEO_TEN
            = "SELECT * FROM KhuyenMai WHERE tenKhuyenMai = ?";

    private final String SQL_TIM_THEO_TEN_GAN_DUNG
            = "SELECT * FROM KhuyenMai WHERE tenKhuyenMai LIKE ?";

    private final String SQL_LAY_MA_CUOI
            = "SELECT TOP 1 maKhuyenMai FROM KhuyenMai ORDER BY maKhuyenMai DESC";

    @Override
    public boolean insert(KhuyenMai khuyenMai) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (khuyenMai.getMaKhuyenMai() == null || khuyenMai.getMaKhuyenMai().trim().isEmpty()) {
                khuyenMai.setMaKhuyenMai(taoMaKhuyenMai());
            }

            stmt.setString(1, khuyenMai.getMaKhuyenMai());
            stmt.setString(2, khuyenMai.getTenKhuyenMai());
            stmt.setDate(3, Date.valueOf(khuyenMai.getNgayBatDau()));
            stmt.setDate(4, Date.valueOf(khuyenMai.getNgayKetThuc()));
            stmt.setDouble(5, khuyenMai.getGiamGia());
            stmt.setBoolean(6, khuyenMai.isTrangThai());
            stmt.setString(7, khuyenMai.getLoaiKhuyenMai().name());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ex) {
            System.getLogger(KhuyenMaiDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    @Override
    public boolean update(KhuyenMai khuyenMai) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, khuyenMai.getTenKhuyenMai());
            stmt.setDate(2, Date.valueOf(khuyenMai.getNgayBatDau()));
            stmt.setDate(3, Date.valueOf(khuyenMai.getNgayKetThuc()));
            stmt.setDouble(4, khuyenMai.getGiamGia());
            stmt.setBoolean(5, khuyenMai.isTrangThai());
            stmt.setString(6, khuyenMai.getLoaiKhuyenMai().name());
            stmt.setString(7, khuyenMai.getMaKhuyenMai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Xóa một khuyến mãi khỏi cơ sở dữ liệu dựa vào mã
    public boolean delete(String maKhuyenMai) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maKhuyenMai);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<KhuyenMai> findById(String maKhuyenMai) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(KhuyenMaiDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<KhuyenMai> findAll() {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSachKhuyenMai.add(mapResultSetToKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(KhuyenMaiDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return danhSachKhuyenMai;
    }

    private KhuyenMai mapResultSetToKhuyenMai(ResultSet rs) throws Exception {
        KhuyenMai khuyenMai = new KhuyenMai();

        khuyenMai.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        khuyenMai.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
        // Dùng method không validate khi load từ database (vì dữ liệu cũ có thể có ngày trong quá khứ)
        khuyenMai.setNgayBatDauFromDatabase(rs.getDate("ngayBatDau").toLocalDate());
        khuyenMai.setNgayKetThucFromDatabase(rs.getDate("ngayKetThuc").toLocalDate());
        khuyenMai.setGiamGia(rs.getDouble("giamGia"));
        khuyenMai.setTrangThai(rs.getBoolean("trangThai"));
        khuyenMai.setLoaiKhuyenMai(LoaiKhuyenMai.valueOf(rs.getString("loaiKhuyenMai")));

        return khuyenMai;
    }

    public KhuyenMai findByName(String tenKhuyenMai) throws Exception {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN)) {

            stmt.setString(1, tenKhuyenMai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToKhuyenMai(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KhuyenMai> findByNameSearch(String tenKhuyenMai) throws Exception {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN_GAN_DUNG)) {

            stmt.setString(1, "%" + tenKhuyenMai + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachKhuyenMai.add(mapResultSetToKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhuyenMai;
    }

    private String taoMaKhuyenMai() {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maKhuyenMai");
                String phanSo = maCuoi.substring(2); // Bỏ qua "KM"
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("KM%05d", soTiepTheo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KM00001";
    }

    public String getLastMaKhuyenMai() {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maKhuyenMai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists(String maKhuyenMai) {
        return findById(maKhuyenMai).isPresent();
    }

    public boolean existsByName(String tenKhuyenMai) throws Exception {
        return findByName(tenKhuyenMai) != null;
    }

    public int count() {
        String sql = "SELECT COUNT(*) as total FROM KhuyenMai";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

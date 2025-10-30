/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;

/**
 *
 * @author dohoainho
 */
public class ChiTietDonTraHangDAO implements DAOInterface<ChiTietDonTraHang, String> {

    @Override
    public boolean insert(ChiTietDonTraHang t) {
        String sql = "INSERT into chitietdontrahang(donGia, lyDoTra, soLuong, thanhTien, maDonTra, maSanPham) "
                + " VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setDouble(1, t.getDonGia());
            pre.setString(2, t.getLyDoTra());
            pre.setInt(3, t.getSoLuong());
            pre.setDouble(4, t.getThanhTien());
            pre.setString(5, t.getDonTraHang().getMaDonTraHang());
            pre.setString(6, t.getSanPham().getMaSanPham());

            int rowsAffected = pre.executeUpdate();
            System.out.println("      [DAO] Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("      [DAO] LỖI KHI INSERT CHI TIẾT ĐÔN TRẢ HÀNG:");
            System.err.println("      - Mã đơn trả: " + (t.getDonTraHang() != null ? t.getDonTraHang().getMaDonTraHang() : "NULL"));
            System.err.println("      - Mã sản phẩm: " + (t.getSanPham() != null ? t.getSanPham().getMaSanPham() : "NULL"));
            System.err.println("      - Lý do: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ChiTietDonTraHang t) {
        String sql = "update chitietdontrahang "
                + " set donGia = ?, lyDoTra = ?, soLuong = ?, thanhTien = ? "
                + " WHERE maDonTra = ? and maSanPham = ? ";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setDouble(1, t.getDonGia());
            pre.setString(2, t.getLyDoTra());
            pre.setInt(3, t.getSoLuong());
            pre.setDouble(4, t.getThanhTien());
            pre.setString(5, t.getDonTraHang().getMaDonTraHang());
            pre.setString(6, t.getSanPham().getMaSanPham());
            return pre.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<ChiTietDonTraHang> findById(String id) {
        ChiTietDonTraHang ct = new ChiTietDonTraHang();
        String sql = "select * from chitietdontrahang "
                + " WHERE maDonTra = ? ";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                double donGia = rs.getDouble("donGia");
                String lyDo = rs.getString("lyDoTra");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonTra = rs.getString("maDonTra");
                String masp = rs.getString("maSanPham");
                ct = new ChiTietDonTraHang(sl, donGia, lyDo, thanhTien, new SanPham(masp), new DonTraHang(maDonTra));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(ct);
    }

    @Override
    public List<ChiTietDonTraHang> findAll() {
        List<ChiTietDonTraHang> dsCt = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery("select * from chitietdontrahang")) {
            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                String lyDo = rs.getString("lyDoTra");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonTra = rs.getString("maDonTra");
                String masp = rs.getString("maSanPham");
                ChiTietDonTraHang ct = new ChiTietDonTraHang(sl, donGia, lyDo, thanhTien, new SanPham(masp), new DonTraHang(maDonTra));
                dsCt.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCt;
    }

    /**
     * Lấy danh sách chi tiết đơn trả hàng theo mã đơn trả
     *
     * @param maDonTra Mã đơn trả hàng
     * @return Danh sách chi tiết đơn trả hàng với thông tin sản phẩm đầy đủ
     */
    public List<ChiTietDonTraHang> findByMaDonTra(String maDonTra) {
        List<ChiTietDonTraHang> dsCt = new ArrayList<>();
        String sql = "SELECT ct.*, sp.tenSanPham, dv.tenDonVi, dv.maDonVi "
                + "FROM chitietdontrahang ct "
                + "LEFT JOIN SanPham sp ON ct.maSanPham = sp.maSanPham "
                + "LEFT JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi "
                + "WHERE ct.maDonTra = ?";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setString(1, maDonTra);
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                String lyDo = rs.getString("lyDoTra");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonTraResult = rs.getString("maDonTra");
                String masp = rs.getString("maSanPham");

                // Tạo sản phẩm với thông tin đầy đủ
                SanPham sp = new SanPham(masp);
                sp.setTenSanPham(rs.getString("tenSanPham"));

                // Tạo đơn vị tính
                String maDonVi = rs.getString("maDonVi");
                if (maDonVi != null && !maDonVi.trim().isEmpty()) {
                    DonViTinh dvt = new DonViTinh();
                    dvt.setMaDonVi(maDonVi);
                    dvt.setTenDonVi(rs.getString("tenDonVi"));
                    sp.setDonViTinh(dvt);
                }

                ChiTietDonTraHang ct = new ChiTietDonTraHang(sl, donGia, lyDo, thanhTien,
                        sp, new DonTraHang(maDonTraResult));
                dsCt.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCt;
    }

    // Lấy tất cả sản phẩm trong đơn trả
    public List<ChiTietDonTraHang> findAllWithDetails() {
        List<ChiTietDonTraHang> danhSach = new ArrayList<>();
        String sql = "SELECT ct.*, sp.tenSanPham, sp.giaNhap, dv.tenDonVi "
                + "FROM chitietdontrahang ct "
                + "JOIN SanPham sp ON ct.maSanPham = sp.maSanPham "
                + "JOIN DonViTinh dv ON sp.maDonVi = dv.maDonVi";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(rs.getString("maSanPham"));
                sanPham.setTenSanPham(rs.getString("tenSanPham"));
                sanPham.setGiaNhap(rs.getDouble("giaNhap"));

                DonViTinh dvt = new DonViTinh();
                dvt.setTenDonVi(rs.getString("tenDonVi"));
                sanPham.setDonViTinh(dvt);

                // Tạo đối tượng ChiTietDonTraHang
                ChiTietDonTraHang ctdt = new ChiTietDonTraHang();
                ctdt.setSanPham(sanPham);
                ctdt.setSoLuong(rs.getInt("soLuong"));
                ctdt.setLyDoTra(rs.getString("lyDoTra"));
                ctdt.setDonGia(rs.getDouble("donGia"));
                ctdt.setThanhTien(rs.getDouble("thanhTien"));

                // Tạo đối tượng DonTraHang (chỉ cần mã)
                String maDonTra = rs.getString("maDonTra");
                if (maDonTra != null) {
                    ctdt.setDonTraHang(new DonTraHang(maDonTra));
                }

                danhSach.add(ctdt);
            }
            System.out.println("[DAO] da tim thay " + danhSach.size() + " chi tiet dontra han");
        } catch (Exception e) {
            System.err.println("[DAO] loi khi thuc hien findAllWithDetails: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }
}

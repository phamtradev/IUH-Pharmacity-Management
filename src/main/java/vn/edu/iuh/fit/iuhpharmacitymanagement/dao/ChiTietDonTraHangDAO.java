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

/**
 *
 * @author dohoainho
 */
public class ChiTietDonTraHangDAO implements DAOInterface<ChiTietDonTraHang, String> {

    @Override
    public boolean insert(ChiTietDonTraHang t) {
        String sql = "INSERT into chitietdontrahang(donGia, lyDoTra, soLuong, thanhTien, return_order_id, maSanPham) "
                + " VALUES(?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement pre = con.prepareStatement(sql);
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
    public boolean update(ChiTietDonTraHang t) {
        String sql = "update chitietdontrahang "
                + " set donGia = ?, lyDoTra = ?, soLuong = ?, thanhTien = ? "
                + " WHERE return_order_id = ? and maSanPham = ? ";
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement pre = con.prepareStatement(sql);
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
                + " WHERE return_order_id = ? ";
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                double donGia = rs.getDouble("donGia");
                String lyDo = rs.getString("lyDoTra");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonTra = rs.getString("return_order_id");
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
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery("select * from chitietdontrahang");
            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                String lyDo = rs.getString("lyDoTra");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonTra = rs.getString("return_order_id");
                String masp = rs.getString("maSanPham");
                ChiTietDonTraHang ct = new ChiTietDonTraHang(sl, donGia, lyDo, thanhTien, new SanPham(masp), new DonTraHang(maDonTra));
                dsCt.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCt;
    }
    
}

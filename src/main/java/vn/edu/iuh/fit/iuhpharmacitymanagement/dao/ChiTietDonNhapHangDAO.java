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
import org.apache.commons.collections4.list.LazyList;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;

/**
 *
 * @author dohoainho
 */
//may cai chi tiet nay ch xu ly truong hop 1-n
public class ChiTietDonNhapHangDAO implements DAOInterface<ChiTietDonNhapHang, String> {

    @Override
    public boolean insert(ChiTietDonNhapHang t) {
        String sql = "INSERT into chitietdonnhaphang(donGia, soLuong, thanhTien, maDonNhapHang, maLoHang) VALUES(?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setDouble(1, t.getDonGia());
            pre.setInt(2, t.getSoLuong());
            pre.setDouble(3, t.getThanhTien());
            pre.setString(4, t.getDonNhapHang().getMaDonNhapHang());            
            pre.setString(5, t.getLoHang().getMaLoHang());            
            return pre.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ChiTietDonNhapHang t) {
        String sql = "UPDATE chitietdonnhaphang "
                + "SET donGia = ?, soLuong = ?, thanhTien = ? "
                + "WHERE maDonNhapHang = ? AND maLoHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setDouble(1, t.getDonGia());
            pre.setInt(2, t.getSoLuong());
            pre.setDouble(3, t.getThanhTien());
            pre.setString(4, t.getDonNhapHang().getMaDonNhapHang());            
            pre.setString(5, t.getLoHang().getMaLoHang());            
            return pre.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<ChiTietDonNhapHang> findById(String id) {
        ChiTietDonNhapHang ct = new ChiTietDonNhapHang();
        String sql = "select * from chitietdonnhaphang where maDonNhapHang = ? ";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareCall(sql)) {
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonNhap = rs.getString("maDonNhapHang");
                String maLoHang = rs.getString("maLoHang");
                ct = new ChiTietDonNhapHang(sl, donGia, thanhTien, new DonNhapHang(maDonNhap), new LoHang(maLoHang));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(ct);
    }

    @Override
    public List<ChiTietDonNhapHang> findAll() {
        List<ChiTietDonNhapHang> dsCt = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery("select * from chitietdonnhaphang")) {
            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                int sl = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maDonNhap = rs.getString("maDonNhapHang");
                String maLoHang = rs.getString("maLoHang");
                ChiTietDonNhapHang ct = new ChiTietDonNhapHang(sl, donGia, thanhTien, new DonNhapHang(maDonNhap), new LoHang(maLoHang));
                dsCt.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCt;
    }

}

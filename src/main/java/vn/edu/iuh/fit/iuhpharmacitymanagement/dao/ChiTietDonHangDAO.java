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
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;

/**
 *
 * @author dohoainho
 */
public class ChiTietDonHangDAO implements DAOInterface<ChiTietDonHang, String> {

    @Override
    public boolean insert(ChiTietDonHang t) {
        String sql = "insert into chitietdonhang(donGia, giamGiaSanPham, giamGiaHoaDonPhanBo, soLuong, thanhTien, maLoHang, maDonHang) values(?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            
            pre.setDouble(1, t.getDonGia());
            pre.setDouble(2, t.getGiamGiaSanPham());
            pre.setDouble(3, t.getGiamGiaHoaDonPhanBo());
            pre.setInt(4, t.getSoLuong());
            pre.setDouble(5, t.getThanhTien());
            pre.setString(6, t.getLoHang().getMaLoHang());
            pre.setString(7, t.getDonHang().getMaDonHang());
            
            return pre.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ChiTietDonHang t) {
        String sql = "update chitietdonhang "
                + "set donGia = ?, giamGiaSanPham = ?, giamGiaHoaDonPhanBo = ?, soLuong = ?, thanhTien = ? "
                + "where maLoHang like ? and maDonHang like ?";
                
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setDouble(1, t.getDonGia());
            pre.setDouble(2, t.getGiamGiaSanPham());
            pre.setDouble(3, t.getGiamGiaHoaDonPhanBo());
            pre.setInt(4, t.getSoLuong());
            pre.setDouble(5, t.getThanhTien());
            pre.setString(6, t.getLoHang().getMaLoHang());
            pre.setString(7, t.getDonHang().getMaDonHang());
            return pre.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }

    @Override
    public Optional<ChiTietDonHang> findById(String id) {
        LoHangDAO loHangDAO = new LoHangDAO();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement("select * from chitietdonhang where maDonHang = ?")) {
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            if(rs.next()){
                double donGia = rs.getDouble("donGia");
                double giamGia = rs.getDouble("giamGiaSanPham");
                double giamGiaHoaDonPhanBo = rs.getDouble("giamGiaHoaDonPhanBo");
                int soLuong = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maLo = rs.getString("maLoHang");
                String maDonHang = rs.getString("maDonHang");
                
                // Load đầy đủ thông tin LoHang (bao gồm SanPham)
                LoHang loHang = loHangDAO.findById(maLo).orElse(new LoHang(maLo));
                
                ChiTietDonHang ctdh = new ChiTietDonHang(soLuong, donGia, thanhTien, giamGia, 
                        loHang, new DonHang(maDonHang));
                ctdh.setGiamGiaHoaDonPhanBo(giamGiaHoaDonPhanBo);
                return Optional.of(ctdh);            
            }
        } catch (Exception e) {
            e.printStackTrace();      
            
        }
        return Optional.empty();
    }

    public ArrayList<ChiTietDonHang> findByIdList(String id) {
        ArrayList<ChiTietDonHang> dsCtdh = new ArrayList<>();
        LoHangDAO loHangDAO = new LoHangDAO();
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement("select * from chitietdonhang where maDonHang = ?")) {
            
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                double giamGia = rs.getDouble("giamGiaSanPham");
                double giamGiaHoaDonPhanBo = rs.getDouble("giamGiaHoaDonPhanBo");
                int soLuong = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maLo = rs.getString("maLoHang");
                String maDonHang = rs.getString("maDonHang");
                
                // Load đầy đủ thông tin LoHang (bao gồm SanPham)
                LoHang loHang = loHangDAO.findById(maLo).orElse(new LoHang(maLo));
                
                ChiTietDonHang ctdh = new ChiTietDonHang(soLuong, donGia, thanhTien, giamGia, 
                        loHang, new DonHang(maDonHang));
                ctdh.setGiamGiaHoaDonPhanBo(giamGiaHoaDonPhanBo);
                dsCtdh.add(ctdh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCtdh;
    }

    @Override
    public List<ChiTietDonHang> findAll() {
        List<ChiTietDonHang> dsCtdh = new ArrayList<>();
        LoHangDAO loHangDAO = new LoHangDAO();
        
        try (Connection con = ConnectDB.getConnection();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery("select * from chitietdonhang")) {
            
            while (rs.next()) {
                double donGia = rs.getDouble("donGia");
                double giamGia = rs.getDouble("giamGiaSanPham");
                double giamGiaHoaDonPhanBo = rs.getDouble("giamGiaHoaDonPhanBo");
                int soLuong = rs.getInt("soLuong");
                double thanhTien = rs.getDouble("thanhTien");
                String maLo = rs.getString("maLoHang");
                String maDonHang = rs.getString("maDonHang");
                
                // Load đầy đủ thông tin LoHang (bao gồm SanPham)
                LoHang loHang = loHangDAO.findById(maLo).orElse(new LoHang(maLo));
                
                ChiTietDonHang ctdh = new ChiTietDonHang(soLuong, donGia, thanhTien, giamGia, 
                        loHang, new DonHang(maDonHang));
                ctdh.setGiamGiaHoaDonPhanBo(giamGiaHoaDonPhanBo);
                dsCtdh.add(ctdh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCtdh;
    }

    //Đếm số lượng chi tiết đơn hàng (số lần bán) của một lô hàng cụ thể
    public long countByLoHang(String maLoHang) {
        String sql = "SELECT COUNT(*) as total FROM ChiTietDonHang WHERE maLoHang = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maLoHang);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
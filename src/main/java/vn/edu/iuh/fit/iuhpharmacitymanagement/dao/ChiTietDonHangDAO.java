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
import java.time.LocalDate;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;

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

    /**
     * Xóa chi tiết đơn hàng theo mã lô hàng và mã đơn hàng
     * @param maLoHang Mã lô hàng
     * @param maDonHang Mã đơn hàng
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean delete(String maLoHang, String maDonHang) {
        String sql = "DELETE FROM ChiTietDonHang WHERE maLoHang = ? AND maDonHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maLoHang);
            stmt.setString(2, maDonHang);
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả chi tiết đơn hàng theo mã đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteByMaDonHang(String maDonHang) {
        String sql = "DELETE FROM ChiTietDonHang WHERE maDonHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maDonHang);
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    /**
     * Truy vấn top sản phẩm bán chạy trong khoảng ngày với điều kiện loại sản phẩm (tùy chọn).
     */
    public List<ProductSalesSummary> findTopSellingProducts(LocalDate dateFrom, LocalDate dateTo, LoaiSanPham loaiSanPham, int limit) {
        List<ProductSalesSummary> summaries = new ArrayList<>();

        if (dateFrom == null || dateTo == null) {
            return summaries;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT sp.maSanPham, sp.tenSanPham, "
                        + "SUM(ct.soLuong) AS totalQuantity, "
                        + "SUM(ct.thanhTien) AS totalRevenue "
                        + "FROM ChiTietDonHang ct "
                        + "INNER JOIN DonHang dh ON ct.maDonHang = dh.maDonHang "
                        + "INNER JOIN LoHang lh ON ct.maLoHang = lh.maLoHang "
                        + "INNER JOIN SanPham sp ON lh.maSanPham = sp.maSanPham "
                        + "WHERE dh.ngayDatHang BETWEEN ? AND ?"
        );

        if (loaiSanPham != null) {
            sql.append(" AND sp.loaiSanPham = ?");
        }

        sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham ORDER BY totalRevenue DESC");

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql.toString())) {

            pre.setDate(1, java.sql.Date.valueOf(dateFrom));
            pre.setDate(2, java.sql.Date.valueOf(dateTo));
            if (loaiSanPham != null) {
                pre.setString(3, loaiSanPham.name());
            }

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                String productId = rs.getString("maSanPham");
                String productName = rs.getString("tenSanPham");
                long totalQuantity = rs.getLong("totalQuantity");
                double totalRevenue = rs.getDouble("totalRevenue");
                summaries.add(new ProductSalesSummary(productId, productName, totalQuantity, totalRevenue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (limit > 0 && summaries.size() > limit) {
            return new ArrayList<>(summaries.subList(0, limit));
        }

        return summaries;
    }

    /**
     * Truy vấn thống kê bán hàng theo loại sản phẩm trong khoảng ngày.
     */
    public List<CategorySalesSummary> findSalesByCategory(LocalDate dateFrom, LocalDate dateTo) {
        List<CategorySalesSummary> summaries = new ArrayList<>();

        if (dateFrom == null || dateTo == null) {
            return summaries;
        }

        String sql = "SELECT sp.loaiSanPham, "
                + "COALESCE(SUM(ct.soLuong), 0) AS totalQuantity, "
                + "COALESCE(SUM(ct.thanhTien), 0) AS totalRevenue, "
                + "COUNT(DISTINCT dh.maDonHang) AS orderCount "
                + "FROM ChiTietDonHang ct "
                + "INNER JOIN DonHang dh ON ct.maDonHang = dh.maDonHang "
                + "INNER JOIN LoHang lh ON ct.maLoHang = lh.maLoHang "
                + "INNER JOIN SanPham sp ON lh.maSanPham = sp.maSanPham "
                + "WHERE dh.ngayDatHang BETWEEN ? AND ? "
                + "GROUP BY sp.loaiSanPham "
                + "ORDER BY totalRevenue DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setDate(1, java.sql.Date.valueOf(dateFrom));
            pre.setDate(2, java.sql.Date.valueOf(dateTo));

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                String categoryName = rs.getString("loaiSanPham");
                LoaiSanPham category = categoryName == null ? null : LoaiSanPham.valueOf(categoryName);
                long totalQuantity = rs.getLong("totalQuantity");
                double totalRevenue = rs.getDouble("totalRevenue");
                long orderCount = rs.getLong("orderCount");

                summaries.add(new CategorySalesSummary(category, totalQuantity, totalRevenue, orderCount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return summaries;
    }

    /**
     * DTO chứa kết quả thống kê số lượng bán theo sản phẩm.
     */
    public static class ProductSalesSummary {
        private final String productId;
        private final String productName;
        private final long totalQuantity;
        private final double totalRevenue;

        public ProductSalesSummary(String productId, String productName, long totalQuantity, double totalRevenue) {
            this.productId = productId;
            this.productName = productName;
            this.totalQuantity = totalQuantity;
            this.totalRevenue = totalRevenue;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public long getTotalQuantity() {
            return totalQuantity;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }
    }

    /**
     * DTO chứa kết quả thống kê bán hàng theo loại sản phẩm.
     */
    public static class CategorySalesSummary {
        private final LoaiSanPham category;
        private final long totalQuantity;
        private final double totalRevenue;
        private final long orderCount;

        public CategorySalesSummary(LoaiSanPham category, long totalQuantity, double totalRevenue, long orderCount) {
            this.category = category;
            this.totalQuantity = totalQuantity;
            this.totalRevenue = totalRevenue;
            this.orderCount = orderCount;
        }

        public LoaiSanPham getCategory() {
            return category;
        }

        public long getTotalQuantity() {
            return totalQuantity;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public long getOrderCount() {
            return orderCount;
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import com.itextpdf.kernel.pdf.PdfDocument;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    private static final String TABLE_NAME = "chitietdonnhaphang";
    private static final List<String> EXTENDED_COLUMNS = Arrays.asList(
            "tyLeChietKhau",
            "tienChietKhau",
            "thanhTienTinhThue",
            "thueSuat",
            "tienThue",
            "thanhTienSauThue"
    );
    private static final Set<String> LOWER_EXTENDED_COLUMNS = lowerCaseColumns(EXTENDED_COLUMNS);

    @Override
    public boolean insert(ChiTietDonNhapHang t) {
        String sqlFull = "INSERT INTO " + TABLE_NAME + "("
                + "donGia, soLuong, thanhTien, tyLeChietKhau, tienChietKhau, "
                + "thanhTienTinhThue, thueSuat, tienThue, thanhTienSauThue, "
                + "maDonNhapHang, maLoHang) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlBasic = "INSERT INTO " + TABLE_NAME + "("
                + "donGia, soLuong, thanhTien, maDonNhapHang, maLoHang) "
                + "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection()) {
            if (hasExtendedColumns(con)) {
                try (PreparedStatement pre = con.prepareStatement(sqlFull)) {
                    pre.setDouble(1, t.getDonGia());
                    pre.setInt(2, t.getSoLuong());
                    pre.setDouble(3, t.getThanhTien());
                    pre.setDouble(4, t.getTyLeChietKhau());
                    pre.setDouble(5, t.getTienChietKhau());
                    pre.setDouble(6, t.getThanhTienTinhThue());
                    pre.setDouble(7, t.getThueSuat());
                    pre.setDouble(8, t.getTienThue());
                    pre.setDouble(9, t.getThanhTienSauThue());
                    pre.setString(10, t.getDonNhapHang().getMaDonNhapHang());
                    pre.setString(11, t.getLoHang().getMaLoHang());
                    return pre.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement pre = con.prepareStatement(sqlBasic)) {
                    pre.setDouble(1, t.getDonGia());
                    pre.setInt(2, t.getSoLuong());
                    pre.setDouble(3, t.getThanhTien());
                    pre.setString(4, t.getDonNhapHang().getMaDonNhapHang());
                    pre.setString(5, t.getLoHang().getMaLoHang());
                    return pre.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(ChiTietDonNhapHang t) {
        String sqlFull = "UPDATE " + TABLE_NAME + " "
                + "SET donGia = ?, soLuong = ?, thanhTien = ?, tyLeChietKhau = ?, "
                + "tienChietKhau = ?, thanhTienTinhThue = ?, thueSuat = ?, tienThue = ?, thanhTienSauThue = ? "
                + "WHERE maDonNhapHang = ? AND maLoHang = ?";
        String sqlBasic = "UPDATE " + TABLE_NAME + " "
                + "SET donGia = ?, soLuong = ?, thanhTien = ? "
                + "WHERE maDonNhapHang = ? AND maLoHang = ?";

        try (Connection con = ConnectDB.getConnection()) {
            if (hasExtendedColumns(con)) {
                try (PreparedStatement pre = con.prepareStatement(sqlFull)) {
                    pre.setDouble(1, t.getDonGia());
                    pre.setInt(2, t.getSoLuong());
                    pre.setDouble(3, t.getThanhTien());
                    pre.setDouble(4, t.getTyLeChietKhau());
                    pre.setDouble(5, t.getTienChietKhau());
                    pre.setDouble(6, t.getThanhTienTinhThue());
                    pre.setDouble(7, t.getThueSuat());
                    pre.setDouble(8, t.getTienThue());
                    pre.setDouble(9, t.getThanhTienSauThue());
                    pre.setString(10, t.getDonNhapHang().getMaDonNhapHang());
                    pre.setString(11, t.getLoHang().getMaLoHang());
                    return pre.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement pre = con.prepareStatement(sqlBasic)) {
                    pre.setDouble(1, t.getDonGia());
                    pre.setInt(2, t.getSoLuong());
                    pre.setDouble(3, t.getThanhTien());
                    pre.setString(4, t.getDonNhapHang().getMaDonNhapHang());
                    pre.setString(5, t.getLoHang().getMaLoHang());
                    return pre.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<ChiTietDonNhapHang> findById(String id) {
        ChiTietDonNhapHang ct = null;
        String sql = "select * from chitietdonnhaphang where maDonNhapHang = ? ";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareCall(sql)) {
            pre.setString(1, id);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                ct = mapResultSetToChiTiet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(ct);
    }

    @Override
    public List<ChiTietDonNhapHang> findAll() {
        List<ChiTietDonNhapHang> dsCt = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery("select * from chitietdonnhaphang")) {
            while (rs.next()) {
                dsCt.add(mapResultSetToChiTiet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCt;
    }

    /**
     * Lấy danh sách chi tiết đơn nhập hàng theo mã đơn nhập hàng
     * @param maDonNhapHang Mã đơn nhập hàng
     * @return Danh sách chi tiết đơn nhập hàng
     */
    public List<ChiTietDonNhapHang> findByMaDonNhapHang(String maDonNhapHang) {
        List<ChiTietDonNhapHang> dsCt = new ArrayList<>();
        String sql = "SELECT * FROM chitietdonnhaphang WHERE maDonNhapHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {
            pre.setString(1, maDonNhapHang);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                dsCt.add(mapResultSetToChiTiet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsCt;
    }

    private ChiTietDonNhapHang mapResultSetToChiTiet(ResultSet rs) throws Exception {
        double donGia = rs.getDouble("donGia");
        int sl = rs.getInt("soLuong");
        double thanhTien = rs.getDouble("thanhTien");
        String maDonNhap = rs.getString("maDonNhapHang");
        String maLoHang = rs.getString("maLoHang");

        ChiTietDonNhapHang ct = new ChiTietDonNhapHang(sl, donGia, thanhTien,
                new DonNhapHang(maDonNhap), new LoHang(maLoHang));

        ResultSetMetaData meta = rs.getMetaData();

        if (hasColumn(meta, "tyLeChietKhau")) {
            ct.setTyLeChietKhau(rs.getDouble("tyLeChietKhau"));
        } else {
            ct.setTyLeChietKhau(0);
        }

        if (hasColumn(meta, "tienChietKhau")) {
            ct.setTienChietKhau(rs.getDouble("tienChietKhau"));
        } else {
            ct.setTienChietKhau(0);
        }

        if (hasColumn(meta, "thanhTienTinhThue")) {
            ct.setThanhTienTinhThue(rs.getDouble("thanhTienTinhThue"));
        } else {
            ct.setThanhTienTinhThue(ct.getThanhTien());
        }

        if (hasColumn(meta, "thueSuat")) {
            ct.setThueSuat(rs.getDouble("thueSuat"));
        } else {
            ct.setThueSuat(0);
        }

        if (hasColumn(meta, "tienThue")) {
            ct.setTienThue(rs.getDouble("tienThue"));
        } else {
            ct.setTienThue(0);
        }

        if (hasColumn(meta, "thanhTienSauThue")) {
            ct.setThanhTienSauThue(rs.getDouble("thanhTienSauThue"));
        } else {
            ct.setThanhTienSauThue(ct.getThanhTien());
        }

        return ct;
    }

    private static boolean hasColumn(ResultSetMetaData meta, String columnLabel) {
        try {
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                if (columnLabel.equalsIgnoreCase(meta.getColumnLabel(i))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Set<String> lowerCaseColumns(List<String> columns) {
        Set<String> lower = new HashSet<>();
        for (String col : columns) {
            lower.add(col.toLowerCase());
        }
        return lower;
    }

    private boolean hasExtendedColumns(Connection con) {
        String sql = "SELECT TOP 0 * FROM " + TABLE_NAME;
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            Set<String> columns = new HashSet<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columns.add(meta.getColumnLabel(i).toLowerCase());
            }
            return columns.containsAll(LOWER_EXTENDED_COLUMNS);
        } catch (SQLException e) {
            System.err.println("Không thể kiểm tra schema bảng " + TABLE_NAME + ": " + e.getMessage());
        }
        return false;
    }

}

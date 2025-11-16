package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.SortTheoMa;

public class DonNhapHangDAO implements DAOInterface<DonNhapHang, String> {

    private final String SQL_THEM =
            "INSERT INTO DonNhapHang (maDonNhapHang, ngayNhap, thanhTien, maNhanVien, maNhaCungCap) VALUES (?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE DonNhapHang SET ngayNhap = ?, thanhTien = ?, maNhanVien = ?, maNhaCungCap = ? WHERE maDonNhapHang = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM DonNhapHang WHERE maDonNhapHang = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM DonNhapHang";

    @Override
    public boolean insert(DonNhapHang donNhapHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (donNhapHang.getMaDonNhapHang() == null || donNhapHang.getMaDonNhapHang().trim().isEmpty()) {
                String maMoi = taoMaDonNhapHangMoi();
                System.out.println("🔍 DEBUG: Mã đơn nhập hàng được tạo = [" + maMoi + "]");
                donNhapHang.setMaDonNhapHang(maMoi);
            }

            stmt.setString(1, donNhapHang.getMaDonNhapHang());
            stmt.setDate(2, Date.valueOf(donNhapHang.getNgayNhap()));
            stmt.setDouble(3, donNhapHang.getThanhTien());
            stmt.setString(4, donNhapHang.getNhanVien() != null ? donNhapHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(5, donNhapHang.getNhaCungCap() != null ? donNhapHang.getNhaCungCap().getMaNhaCungCap() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonNhapHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    @Override
    public boolean update(DonNhapHang donNhapHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setDate(1, Date.valueOf(donNhapHang.getNgayNhap()));
            stmt.setDouble(2, donNhapHang.getThanhTien());
            stmt.setString(3, donNhapHang.getNhanVien() != null ? donNhapHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(4, donNhapHang.getNhaCungCap() != null ? donNhapHang.getNhaCungCap().getMaNhaCungCap() : null);
            stmt.setString(5, donNhapHang.getMaDonNhapHang());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<DonNhapHang> findById(String maDonNhapHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maDonNhapHang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDonNhapHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonNhapHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<DonNhapHang> findAll() {
        List<DonNhapHang> danhSach = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapResultSetToDonNhapHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonNhapHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        danhSach.sort(new SortTheoMa().sortTheoMaTienTo3(DonNhapHang::getMaDonNhapHang));
        return danhSach;
    }

    private DonNhapHang mapResultSetToDonNhapHang(ResultSet rs) throws SQLException, Exception {
        DonNhapHang dnh = new DonNhapHang();

        dnh.setMaDonNhapHang(rs.getString("maDonNhapHang"));
        dnh.setNgayNhap(rs.getDate("ngayNhap").toLocalDate());
        dnh.setThanhTien(rs.getDouble("thanhTien"));

        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        dnh.setNhanVien(nv);

        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNhaCungCap(rs.getString("maNhaCungCap"));
        dnh.setNhaCungCap(ncc);

        return dnh;
    }

    private String taoMaDonNhapHangMoi() {
        LocalDate ngayHienTai = LocalDate.now();
        String ngayThangNam = String.format("%02d%02d%04d", 
                ngayHienTai.getDayOfMonth(), 
                ngayHienTai.getMonthValue(), 
                ngayHienTai.getYear());
        
        String prefixHienTai = "DNH" + ngayThangNam;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT TOP 1 maDonNhapHang FROM DonNhapHang WHERE maDonNhapHang LIKE ? ORDER BY maDonNhapHang DESC")) {
            
            stmt.setString(1, prefixHienTai + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maCuoi = rs.getString("maDonNhapHang");
                try {
                    // Lấy 4 số cuối: DNHddmmyyyyxxxx -> xxxx
                    String soSTT = maCuoi.substring(13);
                    int so = Integer.parseInt(soSTT) + 1;
                    return prefixHienTai + String.format("%04d", so);
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.err.println("Mã đơn nhập hàng không hợp lệ: " + maCuoi + ". Tạo mã mới.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu chưa có đơn nào trong ngày, tạo mã đầu tiên
        return prefixHienTai + "0001";
    }

    public boolean exists(String maDonNhapHang) {
        return findById(maDonNhapHang).isPresent();
    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM DonNhapHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(String maDonNhap) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<DonNhapHang> timTheoText(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    /**
     * Đếm số lượng đơn nhập hàng trong tuần hiện tại
     * @return số lượng đơn nhập hàng trong tuần
     */
    public int countDonNhapHangTrongTuan() {
        String sql = "SELECT COUNT(*) AS total FROM DonNhapHang " +
                     "WHERE DATEPART(WEEK, ngayNhap) = DATEPART(WEEK, GETDATE()) " +
                     "AND DATEPART(YEAR, ngayNhap) = DATEPART(YEAR, GETDATE())";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

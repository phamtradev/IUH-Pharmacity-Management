package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.SortTheoMa;

public class HangHongDAO implements DAOInterface<HangHong, String> {
    
    private final NhanVienDAO nhanVienDAO;

    private final String SQL_THEM =
            "INSERT INTO HangHong (maHangHong, ngayNhap, thanhTien, maNhanVien) VALUES (?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE HangHong SET ngayNhap = ?, thanhTien = ?, maNhanVien = ? WHERE maHangHong = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM HangHong WHERE maHangHong = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM HangHong";

    public HangHongDAO() {
        this.nhanVienDAO = new NhanVienDAO();
    }

    @Override
    public boolean insert(HangHong hangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            // Tự động sinh mã nếu chưa có
            if (hangHong.getMaHangHong() == null || hangHong.getMaHangHong().trim().isEmpty()) {
                hangHong.setMaHangHong(taoMaHangHongMoi());
            }

            stmt.setString(1, hangHong.getMaHangHong());
            stmt.setDate(2, Date.valueOf(hangHong.getNgayNhap()));
            stmt.setDouble(3, hangHong.getThanhTien());
            stmt.setString(4, hangHong.getNhanVien() != null ? hangHong.getNhanVien().getMaNhanVien() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(HangHongDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }


    @Override
    public boolean update(HangHong hangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setDate(1, Date.valueOf(hangHong.getNgayNhap()));
            stmt.setDouble(2, hangHong.getThanhTien());
            stmt.setString(3, hangHong.getNhanVien() != null ? hangHong.getNhanVien().getMaNhanVien() : null);
            stmt.setString(4, hangHong.getMaHangHong());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Optional<HangHong> findById(String maHangHong) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maHangHong);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToHangHong(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(HangHongDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }


    @Override
    public List<HangHong> findAll() {
        List<HangHong> danhSach = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapResultSetToHangHong(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(HangHongDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        danhSach.sort(new SortTheoMa().sortTheoMa(HangHong::getMaHangHong));
        return danhSach;
    }


    private HangHong mapResultSetToHangHong(ResultSet rs) throws SQLException, Exception {
        HangHong hh = new HangHong();

        hh.setMaHangHong(rs.getString("maHangHong"));
        hh.setNgayNhap(rs.getDate("ngayNhap").toLocalDate());
        hh.setThanhTien(rs.getDouble("thanhTien"));

        // Load đầy đủ thông tin nhân viên từ database
        String maNhanVien = rs.getString("maNhanVien");
        if (maNhanVien != null) {
            Optional<NhanVien> nhanVienOpt = nhanVienDAO.findById(maNhanVien);
            if (nhanVienOpt.isPresent()) {
                hh.setNhanVien(nhanVienOpt.get());
            }
        }

        return hh;
    }


    private String taoMaHangHongMoi() {
        // Format: HHddmmyyyyxxxx
        // HH = prefix
        // ddmmyyyy = ngày tháng năm hiện tại
        // xxxx = số thứ tự 4 chữ số
        LocalDate ngayHienTai = LocalDate.now();
        String ngayThangNam = String.format("%02d%02d%04d", 
                ngayHienTai.getDayOfMonth(), 
                ngayHienTai.getMonthValue(), 
                ngayHienTai.getYear());
        
        String prefixHienTai = "HH" + ngayThangNam;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT TOP 1 maHangHong FROM HangHong WHERE maHangHong LIKE ? ORDER BY maHangHong DESC")) {
            
            stmt.setString(1, prefixHienTai + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maCuoi = rs.getString("maHangHong");
                try {
                    // Lấy 4 số cuối: HHddmmyyyyxxxx -> xxxx
                    String soSTT = maCuoi.substring(12); // "HH" (2) + "ddmmyyyy" (8) = 10 → lấy từ index 12
                    int so = Integer.parseInt(soSTT) + 1;
                    return prefixHienTai + String.format("%04d", so);
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.err.println("Mã hàng hỏng không hợp lệ: " + maCuoi + ". Tạo mã mới.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu chưa có phiếu nào trong ngày, tạo mã đầu tiên
        return prefixHienTai + "0001";
    }


    public boolean exists(String maHangHong) {
        return findById(maHangHong).isPresent();
    }


    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM HangHong";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(String maHangHong) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<HangHong> timTheoText(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

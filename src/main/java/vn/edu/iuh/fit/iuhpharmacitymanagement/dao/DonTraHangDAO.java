package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonTraHangDAO implements DAOInterface<DonTraHang, String> {

    private final String SQL_THEM =
            "INSERT INTO DonTraHang (maDonTra, ngayTraHang, thanhTien, maNhanVien, maDonHang, trangThaiXuLy) VALUES (?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE DonTraHang SET ngayTraHang = ?, thanhTien = ?, maNhanVien = ?, maDonHang = ?, trangThaiXuLy = ? WHERE maDonTra = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT dth.*, nv.tenNhanVien, dh.maKhachHang, kh.tenKhachHang, kh.soDienThoai " +
            "FROM DonTraHang dth " +
            "LEFT JOIN NhanVien nv ON dth.maNhanVien = nv.maNhanVien " +
            "LEFT JOIN DonHang dh ON dth.maDonHang = dh.maDonHang " +
            "LEFT JOIN KhachHang kh ON dh.maKhachHang = kh.maKhachHang " +
            "WHERE dth.maDonTra = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT dth.*, nv.tenNhanVien, dh.maKhachHang, kh.tenKhachHang, kh.soDienThoai " +
            "FROM DonTraHang dth " +
            "LEFT JOIN NhanVien nv ON dth.maNhanVien = nv.maNhanVien " +
            "LEFT JOIN DonHang dh ON dth.maDonHang = dh.maDonHang " +
            "LEFT JOIN KhachHang kh ON dh.maKhachHang = kh.maKhachHang";

    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maDonTra FROM DonTraHang ORDER BY maDonTra DESC";


    @Override
    public boolean insert(DonTraHang donTraHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (donTraHang.getMaDonTraHang() == null || donTraHang.getMaDonTraHang().trim().isEmpty()) {
                donTraHang.setMaDonTraHang(taoMaDonTraHangMoi());
            }

            stmt.setString(1, donTraHang.getMaDonTraHang());
            stmt.setDate(2, Date.valueOf(donTraHang.getNgayTraHang()));
            stmt.setDouble(3, donTraHang.getThanhTien());
            stmt.setString(4, donTraHang.getNhanVien() != null ? donTraHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(5, donTraHang.getDonHang() != null ? donTraHang.getDonHang().getMaDonHang() : null);
            stmt.setString(6, donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy() : "Chưa xử lý");

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    @Override
    public boolean update(DonTraHang donTraHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setDate(1, Date.valueOf(donTraHang.getNgayTraHang()));
            stmt.setDouble(2, donTraHang.getThanhTien());
            stmt.setString(3, donTraHang.getNhanVien() != null ? donTraHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(4, donTraHang.getDonHang() != null ? donTraHang.getDonHang().getMaDonHang() : null);
            stmt.setString(5, donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy() : "Chưa xử lý");
            stmt.setString(6, donTraHang.getMaDonTraHang());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<DonTraHang> findById(String maDonTraHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maDonTraHang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDonTraHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<DonTraHang> findAll() {
        List<DonTraHang> danhSach = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapResultSetToDonTraHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return danhSach;
    }

    private DonTraHang mapResultSetToDonTraHang(ResultSet rs) throws SQLException, Exception {
        DonTraHang dth = new DonTraHang();

        dth.setMaDonTraHang(rs.getString("maDonTra"));
        dth.setNgayTraHang(rs.getDate("ngayTraHang").toLocalDate());
        dth.setThanhTien(rs.getDouble("thanhTien"));
        
        // Load trạng thái xử lý
        String trangThai = rs.getString("trangThaiXuLy");
        dth.setTrangThaiXuLy(trangThai != null ? trangThai : "Chưa xử lý");

        // Load thông tin Nhân viên (bao gồm cả tên)
        String maNhanVien = rs.getString("maNhanVien");
        if (maNhanVien != null && !maNhanVien.trim().isEmpty()) {
            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(maNhanVien);
            nv.setTenNhanVien(rs.getString("tenNhanVien"));  // Load tên từ JOIN
            dth.setNhanVien(nv);
        }

        // Load thông tin Đơn hàng (bao gồm cả khách hàng)
        String maDonHang = rs.getString("maDonHang");
        if (maDonHang != null && !maDonHang.trim().isEmpty()) {
            DonHang dh = new DonHang();
            dh.setMaDonHang(maDonHang);
            
            // Load thông tin Khách hàng từ JOIN
            String maKhachHang = rs.getString("maKhachHang");
            if (maKhachHang != null && !maKhachHang.trim().isEmpty()) {
                KhachHang kh = new KhachHang();
                kh.setMaKhachHang(maKhachHang);
                kh.setTenKhachHang(rs.getString("tenKhachHang"));  // Load tên từ JOIN
                kh.setSoDienThoai(rs.getString("soDienThoai"));    // Load SĐT từ JOIN
                dh.setKhachHang(kh);
            }
            
            dth.setDonHang(dh);
        }

        return dth;
    }

    private String taoMaDonTraHangMoi() {
        LocalDate ngayHienTai = LocalDate.now();
        String ngayThangNam = String.format("%02d%02d%04d", 
                ngayHienTai.getDayOfMonth(), 
                ngayHienTai.getMonthValue(), 
                ngayHienTai.getYear());
        
        String prefixHienTai = "DT" + ngayThangNam;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT TOP 1 maDonTra FROM DonTraHang WHERE maDonTra LIKE ? ORDER BY maDonTra DESC")) {
            
            stmt.setString(1, prefixHienTai + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maCuoi = rs.getString("maDonTra");
                try {
                    // Lấy 4 số cuối: DTddMMyyyyxxxx -> xxxx
                    String soSTT = maCuoi.substring(12);
                    int so = Integer.parseInt(soSTT) + 1;
                    return prefixHienTai + String.format("%04d", so);
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.err.println("Mã đơn trả hàng không hợp lệ: " + maCuoi + ". Tạo mã mới.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu chưa có đơn nào trong ngày, tạo mã đầu tiên
        return prefixHienTai + "0001";
    }

    public boolean exists(String maDonTraHang) {
        return findById(maDonTraHang).isPresent();
    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM DonTraHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean delete(String maDonTraHang) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<DonTraHang> timTheoText(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    /**
     * Tìm tất cả đơn trả hàng theo mã đơn hàng
     * @param maDonHang Mã đơn hàng
     * @return Danh sách đơn trả hàng
     */
    public List<DonTraHang> findByDonHang(String maDonHang) {
        List<DonTraHang> danhSach = new ArrayList<>();
        
        String sql = "SELECT dth.*, nv.tenNhanVien, dh.maKhachHang, kh.tenKhachHang, kh.soDienThoai " +
                     "FROM DonTraHang dth " +
                     "LEFT JOIN NhanVien nv ON dth.maNhanVien = nv.maNhanVien " +
                     "LEFT JOIN DonHang dh ON dth.maDonHang = dh.maDonHang " +
                     "LEFT JOIN KhachHang kh ON dh.maKhachHang = kh.maKhachHang " +
                     "WHERE dth.maDonHang = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maDonHang);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                danhSach.add(mapResultSetToDonTraHang(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonTraHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        return danhSach;
    }
}

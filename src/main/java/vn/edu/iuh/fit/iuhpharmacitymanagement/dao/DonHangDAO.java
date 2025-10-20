package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.PhuongThucThanhToan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**n
 * 
 * @author 
 */
public class DonHangDAO implements DAOInterface<DonHang, String> {

    private final String SQL_THEM =
            "INSERT INTO DonHang (maDonHang, ngayDatHang, thanhTien, phuongThucThanhToan, maNhanVien, maKhachHang, maKhuyenMai) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private final String SQL_CAP_NHAT =
            "UPDATE DonHang SET ngayDatHang = ?, thanhTien = ?, phuongThucThanhToan = ?, maNhanVien = ?, maKhachHang = ?, maKhuyenMai = ? WHERE maDonHang = ?";
            
    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM DonHang WHERE maDonHang = ?";
    
    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM DonHang";
    
    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maDonHang FROM DonHang ORDER BY maDonHang DESC";


    @Override
    public boolean insert(DonHang donHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {


            if (donHang.getMaDonHang() == null || donHang.getMaDonHang().trim().isEmpty()) {
                donHang.setMaDonHang(taoMaDonHangMoi());
            }

            stmt.setString(1, donHang.getMaDonHang());
            stmt.setDate(2, Date.valueOf(donHang.getNgayDatHang()));
            stmt.setDouble(3, donHang.getThanhTien());
            stmt.setString(4, donHang.getPhuongThucThanhToan().name());
            stmt.setString(5, donHang.getNhanVien() != null ? donHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(6, donHang.getKhachHang() != null ? donHang.getKhachHang().getMaKhachHang() : null);
            stmt.setString(7, donHang.getKhuyenMai() != null ? donHang.getKhuyenMai().getMaKhuyenMai() : null);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }


    @Override
    public boolean update(DonHang donHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setDate(1, Date.valueOf(donHang.getNgayDatHang()));
            stmt.setDouble(2, donHang.getThanhTien());
            stmt.setString(3, donHang.getPhuongThucThanhToan().name());
            stmt.setString(4, donHang.getNhanVien() != null ? donHang.getNhanVien().getMaNhanVien() : null);
            stmt.setString(5, donHang.getKhachHang() != null ? donHang.getKhachHang().getMaKhachHang() : null);
            stmt.setString(6, donHang.getKhuyenMai() != null ? donHang.getKhuyenMai().getMaKhuyenMai() : null);
            stmt.setString(7, donHang.getMaDonHang());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Optional<DonHang> findById(String maDonHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maDonHang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDonHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(DonHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }


    @Override
    public List<DonHang> findAll() {
        List<DonHang> danhSachDonHang = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSachDonHang.add(mapResultSetToDonHang(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) { 
            System.getLogger(DonHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return danhSachDonHang;
    }


    private DonHang mapResultSetToDonHang(ResultSet rs) throws SQLException, Exception {
        DonHang donHang = new DonHang();

        donHang.setMaDonHang(rs.getString("maDonHang"));
        donHang.setNgayDatHang(rs.getDate("ngayDatHang").toLocalDate());
        donHang.setThanhTien(rs.getDouble("thanhTien"));
        donHang.setPhuongThucThanhToan(PhuongThucThanhToan.valueOf(rs.getString("phuongThucThanhToan")));


        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        donHang.setNhanVien(nv);

        KhachHang kh = new KhachHang();
        kh.setMaKhachHang(rs.getString("maKhachHang"));
        donHang.setKhachHang(kh);

        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        donHang.setKhuyenMai(km);

        return donHang;
    }


    private String taoMaDonHangMoi() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maDonHang"); 
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                return String.format("DH%05d", so);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "DH00001";
    }


    public boolean exists(String maDonHang) {
        return findById(maDonHang).isPresent();
    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM DonHang";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

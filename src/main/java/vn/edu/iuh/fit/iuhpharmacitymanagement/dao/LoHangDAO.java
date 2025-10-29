package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoHangDAO implements DAOInterface<LoHang, String> {

    private final String SQL_THEM =
            "INSERT INTO LoHang (maLoHang, tenLoHang, hanSuDung, tonKho, trangThai, maSanPham) VALUES (?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT =
            "UPDATE LoHang SET tenLoHang = ?, hanSuDung = ?, tonKho = ?, trangThai = ?, maSanPham = ? WHERE maLoHang = ?";

    private final String SQL_TIM_THEO_MA =
            "SELECT * FROM LoHang WHERE maLoHang = ?";

    private final String SQL_TIM_TAT_CA =
            "SELECT * FROM LoHang";
            
    private final String SQL_TIM_THEO_MA_SP =
            "SELECT * FROM LoHang WHERE maSanPham = ?";

    private final String SQL_TIM_THEO_TEN_GAN_DUNG =
            "SELECT * FROM LoHang WHERE tenLoHang LIKE ?";

    private final String SQL_LAY_MA_CUOI =
            "SELECT TOP 1 maLoHang FROM LoHang ORDER BY maLoHang DESC";

    @Override
    public boolean insert(LoHang loHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (loHang.getMaLoHang() == null || loHang.getMaLoHang().trim().isEmpty()) {
                loHang.setMaLoHang(taoMaLoHang());
            }

            stmt.setString(1, loHang.getMaLoHang());
            stmt.setString(2, loHang.getTenLoHang());
            stmt.setDate(3, Date.valueOf(loHang.getHanSuDung()));
            stmt.setInt(4, loHang.getTonKho());
            stmt.setBoolean(5, loHang.isTrangThai());
            stmt.setString(6, loHang.getSanPham().getMaSanPham());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    @Override
    public boolean update(LoHang loHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, loHang.getTenLoHang());
            stmt.setDate(2, Date.valueOf(loHang.getHanSuDung()));
            stmt.setInt(3, loHang.getTonKho());
            stmt.setBoolean(4, loHang.isTrangThai());
            stmt.setString(5, loHang.getSanPham().getMaSanPham());
            stmt.setString(6, loHang.getMaLoHang());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(String maLoHang) {
        String sql = "DELETE FROM LoHang WHERE maLoHang = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maLoHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<LoHang> findById(String maLoHang) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

            stmt.setString(1, maLoHang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LoHang loHang = mapResultSetToLoHang(rs);
                // Chỉ return nếu không null
                if (loHang != null) {
                    return Optional.of(loHang);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }

    @Override
    public List<LoHang> findAll() {
        List<LoHang> danhSachLoHang = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LoHang loHang = mapResultSetToLoHang(rs);
                // Chỉ thêm vào danh sách nếu không null (bỏ qua lô hàng có dữ liệu không hợp lệ)
                if (loHang != null) {
                    danhSachLoHang.add(loHang);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return danhSachLoHang;
    }

    private LoHang mapResultSetToLoHang(ResultSet rs) throws Exception {
        LoHang loHang = new LoHang();

        loHang.setMaLoHang(rs.getString("maLoHang"));
        loHang.setTenLoHang(rs.getString("tenLoHang"));
        
        // Không đọc ngaySanXuat vì cột này không tồn tại trong database
        // loHang.setNgaySanXuat(rs.getDate("ngaySanXuat").toLocalDate());
        
        loHang.setHanSuDung(rs.getDate("hanSuDung").toLocalDate());
        loHang.setTonKho(rs.getInt("tonKho"));
        loHang.setTrangThai(rs.getBoolean("trangThai"));

        //lấy mã sản phẩm từ CSDL và dùng SanPhamDAO để tìm đối tượng SanPham tương ứng
        String maSanPham = rs.getString("maSanPham");
        
        // Kiểm tra maSanPham có NULL không
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            System.err.println("⚠️ Lô hàng " + loHang.getMaLoHang() + " có maSanPham NULL - BỎ QUA");
            return null; // Return null để skip lô hàng này
        }
        
        SanPhamDAO sanPhamDAO = new SanPhamDAO();
        Optional<SanPham> sanPhamOpt = sanPhamDAO.findById(maSanPham);
        
        if (!sanPhamOpt.isPresent()) {
            System.err.println("⚠️ Không tìm thấy sản phẩm " + maSanPham + " cho lô hàng " + loHang.getMaLoHang() + " - BỎ QUA");
            return null; // Return null để skip lô hàng này
        }
        
        loHang.setSanPham(sanPhamOpt.get()); // Chỉ set khi tìm thấy sản phẩm

        return loHang;
    }
    
    public List<LoHang> findByMaSanPham(String maSanPham) {
        List<LoHang> danhSachLoHang = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA_SP)) {

            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoHang loHang = mapResultSetToLoHang(rs);
                // Chỉ thêm vào danh sách nếu không null
                if (loHang != null) {
                    danhSachLoHang.add(loHang);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return danhSachLoHang;
    }

    public List<LoHang> findByNameSearch(String tenLoHang) throws Exception {
        List<LoHang> danhSachLoHang = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN_GAN_DUNG)) {

            stmt.setString(1, "%" + tenLoHang + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoHang loHang = mapResultSetToLoHang(rs);
                // Chỉ thêm vào danh sách nếu không null
                if (loHang != null) {
                    danhSachLoHang.add(loHang);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLoHang;
    }

    private String taoMaLoHang() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("maLoHang");
                String phanSo = maCuoi.substring(2); // Bỏ qua "LH"
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("LH%05d", soTiepTheo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "LH00001";
    }

    public String getLastMaLoHang() {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maLoHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int count() {
        String sql = "SELECT COUNT(*) as total FROM LoHang";

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
    
    /**
     * Kiểm tra tên lô hàng đã tồn tại chưa
     */
    public boolean isTenLoHangExists(String tenLoHang) {
        String sql = "SELECT COUNT(*) as total FROM LoHang WHERE tenLoHang = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, tenLoHang);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tìm lô hàng theo sản phẩm và hạn sử dụng (để cộng dồn)
     */
    public Optional<LoHang> findByMaSanPhamAndHanSuDung(String maSanPham, java.time.LocalDate hanSuDung) {
        String sql = "SELECT * FROM LoHang WHERE maSanPham = ? AND hanSuDung = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maSanPham);
            stmt.setDate(2, java.sql.Date.valueOf(hanSuDung));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                LoHang loHang = mapResultSetToLoHang(rs);
                if (loHang != null) {
                    return Optional.of(loHang);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return Optional.empty();
    }
    
    /**
     * Cập nhật tồn kho của lô hàng (cộng dồn)
     */
    public boolean updateTonKho(String maLoHang, int themSoLuong) {
        String sql = "UPDATE LoHang SET tonKho = tonKho + ? WHERE maLoHang = ?";
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, themSoLuong);
            stmt.setString(2, maLoHang);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
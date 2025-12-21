package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;

public class LoHangDAO implements DAOInterface<LoHang, String> {

    private final String SQL_THEM
            = "INSERT INTO LoHang (maLoHang, tenLoHang, hanSuDung, tonKho, trangThai, maSanPham, giaNhapLo) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final String SQL_CAP_NHAT
            = "UPDATE LoHang SET tenLoHang = ?, hanSuDung = ?, tonKho = ?, trangThai = ?, maSanPham = ?, giaNhapLo = ? WHERE maLoHang = ?";

    private final String SQL_TIM_THEO_MA
            = "SELECT * FROM LoHang WHERE maLoHang = ?";

    private final String SQL_TIM_TAT_CA
            = "SELECT * FROM LoHang";

    private final String SQL_TIM_THEO_MA_SP
            = "SELECT * FROM LoHang WHERE maSanPham = ?";

    private final String SQL_TIM_THEO_TEN_GAN_DUNG
            = "SELECT * FROM LoHang WHERE tenLoHang LIKE ?";

    private final String SQL_LAY_MA_CUOI
            = "SELECT TOP 1 maLoHang FROM LoHang ORDER BY maLoHang DESC";

    @Override
    public boolean insert(LoHang loHang) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (loHang.getMaLoHang() == null || loHang.getMaLoHang().trim().isEmpty()) {
                loHang.setMaLoHang(taoMaLoHang());
            }

            stmt.setString(1, loHang.getMaLoHang());
            stmt.setString(2, loHang.getTenLoHang());
            stmt.setDate(3, Date.valueOf(loHang.getHanSuDung()));
            stmt.setInt(4, loHang.getTonKho());
            stmt.setBoolean(5, loHang.isTrangThai());
            stmt.setString(6, loHang.getSanPham().getMaSanPham());
            stmt.setDouble(7, loHang.getGiaNhapLo());

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
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, loHang.getTenLoHang());
            stmt.setDate(2, Date.valueOf(loHang.getHanSuDung()));
            stmt.setInt(3, loHang.getTonKho());
            stmt.setBoolean(4, loHang.isTrangThai());
            stmt.setString(5, loHang.getSanPham().getMaSanPham());
            stmt.setDouble(6, loHang.getGiaNhapLo());
            stmt.setString(7, loHang.getMaLoHang());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maLoHang) {
        // Xóa cascade: Xóa các chi tiết hàng hỏng liên quan trước
        ChiTietHangHongDAO chiTietHangHongDAO = new ChiTietHangHongDAO();
        chiTietHangHongDAO.deleteByMaLoHang(maLoHang);
        
        // Sau đó mới xóa lô hàng
        String sql = "DELETE FROM LoHang WHERE maLoHang = ?";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maLoHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<LoHang> findById(String maLoHang) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA)) {

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

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA); ResultSet rs = stmt.executeQuery()) {

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
        loHang.setTonKhoNoValidation(rs.getInt("tonKho")); // Dùng NoValidation khi load từ DB
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
            System.err.println("Không tìm thấy sản phẩm " + maSanPham + " cho lô hàng " + loHang.getMaLoHang() + " - BỎ QUA");
            return null; // Return null để skip lô hàng này
        }

        loHang.setSanPham(sanPhamOpt.get()); // Chỉ set khi tìm thấy sản phẩm

        // Đọc giá nhập lô (nếu cột tồn tại)
        try {
            double giaNhapLo = rs.getDouble("giaNhapLo");
            loHang.setGiaNhapLo(giaNhapLo);
        } catch (SQLException ex) {
            // Nếu cột chưa tồn tại (schema cũ) thì giữ mặc định 0
            System.err.println("[LoHangDAO] Cột 'giaNhapLo' chưa tồn tại, sử dụng giá trị mặc định 0");
        }

        return loHang;
    }

    public List<LoHang> findByMaSanPham(String maSanPham) {
        List<LoHang> danhSachLoHang = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA_SP)) {

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

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN_GAN_DUNG)) {

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
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI); ResultSet rs = stmt.executeQuery()) {

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
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI); ResultSet rs = stmt.executeQuery()) {

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

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

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

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

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

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

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
     * Tìm tất cả lô hàng theo sản phẩm và hạn sử dụng
     * (Có thể có nhiều lô cùng HSD nhưng từ NCC khác nhau)
     */
    public List<LoHang> findAllByMaSanPhamAndHanSuDung(String maSanPham, java.time.LocalDate hanSuDung) {
        List<LoHang> danhSachLoHang = new ArrayList<>();
        String sql = "SELECT * FROM LoHang WHERE maSanPham = ? AND hanSuDung = ?";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maSanPham);
            stmt.setDate(2, java.sql.Date.valueOf(hanSuDung));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoHang loHang = mapResultSetToLoHang(rs);
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

    /**
     * Cập nhật tồn kho của lô hàng (cộng dồn)
     */
    public boolean updateTonKho(String maLoHang, int themSoLuong) {
        String sql = "UPDATE LoHang SET tonKho = tonKho + ? WHERE maLoHang = ?";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, themSoLuong);
            stmt.setString(2, maLoHang);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("UPDATE tonKho - Mã lô: " + maLoHang + ", Số lượng thêm: " + themSoLuong + ", Rows affected: " + rowsAffected);
            
            if (rowsAffected == 0) {
                System.err.println("Cảnh báo: Không có hàng nào được cập nhật! Có thể mã lô hàng không tồn tại: " + maLoHang);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật tồn kho: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lọc sp hết hsd (loại bỏ các lô hết hạn đã xuất hủy: HSD < hôm nay và tồn kho = 0)
    public List<LoHang> timSanPhamHetHan() {
        List<LoHang> danhSach = new ArrayList<>();
        //join cả 3 bảng LoHang, SanPham, và DonViTinh
        // Điều kiện: HSD <= 6 tháng kể từ hôm nay VÀ tồn kho > 0 VÀ trạng thái = true (còn hoạt động)
        // (Tự động loại bỏ các lô hết hạn đã xuất hủy: HSD < hôm nay và tồn kho = 0 hoặc trạng thái = false)
        String sql = "SELECT lh.maLoHang, lh.tenLoHang, lh.hanSuDung, lh.tonKho, lh.giaNhapLo, lh.trangThai, "
                + "       sp.maSanPham, sp.tenSanPham, sp.giaNhap, sp.hinhAnh, "
                + "       dvt.tenDonVi "
                + "FROM LoHang lh "
                + "JOIN SanPham sp ON lh.maSanPham = sp.maSanPham "
                + "JOIN DonViTinh dvt ON sp.maDonVi = dvt.maDonVi "
                + "WHERE lh.hanSuDung <= DATEADD(month, 6, GETDATE()) "
                + "  AND lh.tonKho > 0 "
                + "  AND lh.trangThai = 1 "
                + "  AND NOT (lh.hanSuDung < CAST(GETDATE() AS DATE) AND lh.tonKho = 0)";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Tạo đối tượng DonViTinh
                DonViTinh dvt = new DonViTinh();
                dvt.setTenDonVi(rs.getString("tenDonVi"));

                // Tạo đối tượng SanPham
                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(rs.getString("maSanPham"));
                sanPham.setTenSanPham(rs.getString("tenSanPham"));
                sanPham.setGiaNhap(rs.getDouble("giaNhap"));
                sanPham.setHinhAnh(rs.getString("hinhAnh")); // Load hinhAnh từ JOIN
                sanPham.setDonViTinh(dvt);

                // Tạo đối tượng LoHang
                LoHang loHang = new LoHang();
                loHang.setMaLoHang(rs.getString("maLoHang"));
                loHang.setTenLoHang(rs.getString("tenLoHang"));
                loHang.setHanSuDung(rs.getDate("hanSuDung").toLocalDate());
                loHang.setTonKhoNoValidation(rs.getInt("tonKho")); // Dùng NoValidation khi load từ DB
                loHang.setTrangThai(rs.getBoolean("trangThai")); // Set trạng thái
                // Đọc giá nhập lô nếu có trong SELECT
                try {
                    double giaNhapLo = rs.getDouble("giaNhapLo");
                    loHang.setGiaNhapLo(giaNhapLo);
                } catch (SQLException ex) {
                    // Nếu cột không tồn tại hoặc lỗi, giữ mặc định 0
                }
                loHang.setSanPham(sanPham); // Gán sản phẩm vào lô hàng

                danhSach.add(loHang);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return danhSach;
    }

    public List<Map<String, Object>> findForDisposalFromReturns() {
        List<Map<String, Object>> danhSach = new ArrayList<>();
        String sql = "WITH ReturnedProducts AS ("
                + "    SELECT DISTINCT maSanPham, lyDoTra "
                + "    FROM chitietdontrahang"
                + "), "
                + "RankedBatches AS ("
                + "    SELECT "
                + "        lh.maLoHang, "
                + "        lh.maSanPham, "
                + "        rp.lyDoTra, "
                + "        ROW_NUMBER() OVER(PARTITION BY lh.maSanPham ORDER BY lh.hanSuDung ASC) as rn "
                + "    FROM lohang lh "
                + "    INNER JOIN ReturnedProducts rp ON lh.maSanPham = rp.maSanPham "
                + "    WHERE lh.tonKho > 0"
                + ") "
                + "SELECT "
                + "    rb.lyDoTra, "
                + "    lh.maLoHang, lh.tenLoHang, lh.hanSuDung, lh.tonKho, "
                + "    sp.maSanPham, sp.tenSanPham, sp.giaNhap, sp.hinhAnh, "
                + "    dvt.tenDonVi "
                + "FROM RankedBatches rb "
                + "JOIN lohang lh ON rb.maLoHang = lh.maLoHang "
                + "JOIN sanpham sp ON lh.maSanPham = sp.maSanPham "
                + "JOIN donViTinh dvt ON sp.maDonVi = dvt.maDonVi "
                + "WHERE rb.rn = 1";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            System.out.println("--- DEBUG DAO: Đang truy vấn sản phẩm bị trả hàng ---");
            while (rs.next()) {
                // Map dữ liệu vào các đối tượng
                DonViTinh dvt = new DonViTinh();
                dvt.setTenDonVi(rs.getString("tenDonVi"));

                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(rs.getString("maSanPham"));
                sanPham.setTenSanPham(rs.getString("tenSanPham"));
                sanPham.setGiaNhap(rs.getDouble("giaNhap"));
                sanPham.setHinhAnh(rs.getString("hinhAnh")); // Load hinhAnh từ JOIN
                sanPham.setDonViTinh(dvt);

                LoHang loHang = new LoHang();
                loHang.setMaLoHang(rs.getString("maLoHang"));
                loHang.setTenLoHang(rs.getString("tenLoHang"));
                loHang.setHanSuDung(rs.getDate("hanSuDung").toLocalDate());
                loHang.setTonKhoNoValidation(rs.getInt("tonKho")); // Dùng NoValidation khi load từ DB
                loHang.setSanPham(sanPham);

                String lyDoTra = rs.getString("lyDoTra");

                // Đóng gói kết quả vào một Map
                Map<String, Object> item = new HashMap<>();
                item.put("loHang", loHang);
                item.put("lyDo", lyDoTra);
                danhSach.add(item);
            }
            System.out.println("DEBUG DAO: SQL Query da tim thay " + danhSach.size() + " san pham can huy tu don tra");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.getLogger(LoHangDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return danhSach;
    }
    
    /**
     * Tìm lô hàng theo số đăng ký sản phẩm và hạn sử dụng
     * (Số đăng ký đã unique cho mỗi sản phẩm từ mỗi NCC, không cần tham số maNCC)
     * @param soDangKy Số đăng ký sản phẩm
     * @param hanSuDung Hạn sử dụng (LocalDate)
     * @return Optional chứa LoHang nếu tìm thấy
     */
    public Optional<LoHang> timLoHangTheoSoDangKyVaHanSuDung(String soDangKy, LocalDate hanSuDung) {
        // JOIN với SanPham để lấy đầy đủ thông tin sản phẩm (bao gồm hinhAnh)
            String SQL = "SELECT lh.maLoHang, lh.tenLoHang, lh.hanSuDung, lh.tonKho, lh.trangThai, lh.maSanPham, " +
                        "       sp.tenSanPham, sp.soDangKy, sp.hoatChat, sp.lieuDung, sp.cachDongGoi, " +
                        "       sp.quocGiaSanXuat, sp.nhaSanXuat, sp.giaNhap, sp.giaBan, sp.hoatDong, " +
                        "       sp.thueVAT, sp.hinhAnh, sp.loaiSanPham, sp.maDonVi " +
                        "FROM LoHang lh " +
                        "INNER JOIN SanPham sp ON lh.maSanPham = sp.maSanPham " +
                        "WHERE sp.soDangKy = ? " +
                        "AND lh.hanSuDung = ?";
        
        System.out.println("🔍 [DAO] Tìm lô: Số ĐK = '" + soDangKy + "', HSD = " + hanSuDung);
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL)) {
            
            ps.setString(1, soDangKy);
            ps.setDate(2, Date.valueOf(hanSuDung)); // Convert LocalDate -> java.sql.Date
            
            // Debug log - thay thế ? bằng giá trị thực
            String debugSQL = SQL.replaceFirst("\\?", "'" + soDangKy + "'")
                                 .replaceFirst("\\?", "'" + Date.valueOf(hanSuDung) + "'");
            System.out.println("🔍 [DAO] SQL: " + debugSQL);
            
            // Debug: Kiểm tra có lô nào với số đăng ký này không
            try (PreparedStatement psDebug = con.prepareStatement(
                    "SELECT lh.maLoHang, lh.tenLoHang, lh.hanSuDung, sp.soDangKy " +
                    "FROM LoHang lh INNER JOIN SanPham sp ON lh.maSanPham = sp.maSanPham " +
                    "WHERE sp.soDangKy = ?")) {
                psDebug.setString(1, soDangKy);
                try (ResultSet rsDebug = psDebug.executeQuery()) {
                    System.out.println("📋 [DEBUG] Tất cả lô có số ĐK '" + soDangKy + "':");
                    while (rsDebug.next()) {
                        System.out.println("   - " + rsDebug.getString("maLoHang") + 
                                         " | " + rsDebug.getString("tenLoHang") + 
                                         " | HSD=" + rsDebug.getDate("hanSuDung"));
                    }
                }
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✅ [DAO] Tìm thấy lô: " + rs.getString("maLoHang") + " | " + rs.getString("tenLoHang"));
                    LoHang loHang = new LoHang();
                    try {
                        loHang.setMaLoHang(rs.getString("maLoHang"));
                        loHang.setTenLoHang(rs.getString("tenLoHang"));
                        loHang.setHanSuDung(rs.getDate("hanSuDung").toLocalDate());
                        loHang.setTonKhoNoValidation(rs.getInt("tonKho")); // Dùng NoValidation vì load từ DB
                        loHang.setTrangThai(rs.getBoolean("trangThai"));
                        
                        // Tạo đối tượng SanPham từ kết quả JOIN (đã có đầy đủ thông tin bao gồm hinhAnh)
                        SanPham sanPham = new SanPham();
                        sanPham.setMaSanPham(rs.getString("maSanPham"));
                        sanPham.setTenSanPham(rs.getString("tenSanPham"));
                        sanPham.setSoDangKy(rs.getString("soDangKy"));
                        sanPham.setHoatChat(rs.getString("hoatChat"));
                        sanPham.setLieuDung(rs.getString("lieuDung"));
                        sanPham.setCachDongGoi(rs.getString("cachDongGoi"));
                        sanPham.setQuocGiaSanXuat(rs.getString("quocGiaSanXuat"));
                        sanPham.setNhaSanXuat(rs.getString("nhaSanXuat"));
                        sanPham.setGiaNhap(rs.getDouble("giaNhap"));
                        sanPham.setGiaBan(rs.getDouble("giaBan"));
                        sanPham.setHoatDong(rs.getBoolean("hoatDong"));
                        sanPham.setThueVAT(rs.getDouble("thueVAT"));
                        sanPham.setHinhAnh(rs.getString("hinhAnh")); // QUAN TRỌNG: Load hinhAnh từ JOIN
                        
                        // Set loại sản phẩm
                        String loaiSanPhamStr = rs.getString("loaiSanPham");
                        if (loaiSanPhamStr != null) {
                            try {
                                sanPham.setLoaiSanPham(vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham.valueOf(loaiSanPhamStr));
                            } catch (Exception e) {
                                System.err.println("⚠️ [DAO] Lỗi khi set loaiSanPham: " + e.getMessage());
                            }
                        }
                        
                        // Load DonViTinh nếu có maDonVi
                        String maDonVi = rs.getString("maDonVi");
                        if (maDonVi != null) {
                            DonViTinhDAO donViTinhDAO = new DonViTinhDAO();
                            Optional<DonViTinh> donViTinhOpt = donViTinhDAO.findById(maDonVi);
                            if (donViTinhOpt.isPresent()) {
                                sanPham.setDonViTinh(donViTinhOpt.get());
                            }
                        }
                        
                        loHang.setSanPham(sanPham);
                        System.out.println("✅ [DAO] Đã load đầy đủ thông tin sản phẩm từ JOIN, hinhAnh = " + sanPham.getHinhAnh());
                        
                        return Optional.of(loHang);
                    } catch (Exception e) {
                        System.err.println("Lỗi khi set thuộc tính LoHang: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("❌ [DAO] KHÔNG tìm thấy lô nào!");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [DAO] Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("⚠ [DAO] Return empty");
        return Optional.empty();
    }
}

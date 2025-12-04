package vn.edu.iuh.fit.iuhpharmacitymanagement.dao;

import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.TaiKhoan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.PasswordUtil;

public class TaiKhoanDAO implements DAOInterface<TaiKhoan, String> {

    private final String SQL_THEM
            = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, maNhanVien) VALUES (?, ?, ?)";

    private final String SQL_CAP_NHAT
            = "UPDATE TaiKhoan SET matKhau = ?, maNhanVien = ? WHERE tenDangNhap = ?";

    private final String SQL_TIM_THEO_TEN_DANG_NHAP
            = "SELECT tk.tenDangNhap, tk.matKhau, tk.maNhanVien, "
            + "nv.tenNhanVien, nv.diaChi, nv.soDienThoai, nv.email, nv.vaiTro "
            + "FROM TaiKhoan tk "
            + "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien "
            + "WHERE tk.tenDangNhap = ?";

    private final String SQL_TIM_TAT_CA
            = "SELECT tk.tenDangNhap, tk.matKhau, tk.maNhanVien, "
            + "nv.tenNhanVien, nv.diaChi, nv.soDienThoai, nv.email, nv.vaiTro "
            + "FROM TaiKhoan tk "
            + "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien";

    private final String SQL_DANG_NHAP
            = "SELECT tk.tenDangNhap, tk.matKhau, tk.maNhanVien, "
            + "nv.tenNhanVien, nv.diaChi, nv.soDienThoai, nv.email, nv.vaiTro "
            + "FROM TaiKhoan tk "
            + "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien "
            + "WHERE tk.maNhanVien = ?";

    private final String SQL_TIM_THEO_MA_NHAN_VIEN
            = "SELECT tk.tenDangNhap, tk.matKhau, tk.maNhanVien, "
            + "nv.tenNhanVien, nv.diaChi, nv.soDienThoai, nv.email, nv.vaiTro "
            + "FROM TaiKhoan tk "
            + "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien "
            + "WHERE tk.maNhanVien = ?";

    private final String SQL_LAY_MA_CUOI
            = "SELECT TOP 1 tenDangNhap FROM TaiKhoan ORDER BY tenDangNhap DESC";

    @Override
    public boolean insert(TaiKhoan taiKhoan) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_THEM)) {

            if (taiKhoan.getTenDangNhap() == null || taiKhoan.getTenDangNhap().trim().isEmpty()) {
                try {
                    taiKhoan.setTenDangNhap(taoMaTaiKhoan());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            stmt.setString(1, taiKhoan.getTenDangNhap());
            stmt.setString(2, taiKhoan.getMatKhau());
            stmt.setString(3, taiKhoan.getNhanVien() != null ? taiKhoan.getNhanVien().getMaNhanVien() : null);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(TaiKhoan taiKhoan) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_CAP_NHAT)) {

            stmt.setString(1, taiKhoan.getMatKhau());
            stmt.setString(2, taiKhoan.getNhanVien() != null ? taiKhoan.getNhanVien().getMaNhanVien() : null);
            stmt.setString(3, taiKhoan.getTenDangNhap());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePass(TaiKhoan taiKhoan, NhanVien nv, String newPass) {
        //lay pass tư ham ỏ email phat sinh
        //EmailUtil email = new EmailUtil();
        //String newPass = email.ramdomPass(nv);        
        System.out.println("Mật khẩu mới (plain text): " + newPass);

        // Hash mật khẩu trước khi lưu vào database
        String hashedPassword = PasswordUtil.encode(newPass);
        System.out.println("Mật khẩu đã hash: " + hashedPassword);

        String sql = "UPDATE tk "
                + " set tk.matKhau = ? "
                + " from taikhoan tk "
                + " JOIN nhanvien nv on tk.maNhanVien = nv.maNhanVien "
                + " WHERE nv.maNhanVien = ?";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);  // Lưu mật khẩu đã hash
            stmt.setString(2, nv.getMaNhanVien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<TaiKhoan> findById(String tenDangNhap) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_TEN_DANG_NHAP)) {

            stmt.setString(1, tenDangNhap);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToTaiKhoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<TaiKhoan> findAll() {
        List<TaiKhoan> danhSachTaiKhoan = new ArrayList<>();

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_TAT_CA); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSachTaiKhoan.add(mapResultSetToTaiKhoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachTaiKhoan;
    }

    public TaiKhoan dangNhap(String maNhanVien) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_DANG_NHAP)) {

            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTaiKhoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<TaiKhoan> timTheoMaNhanVien(String maNhanVien) {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_TIM_THEO_MA_NHAN_VIEN)) {

            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToTaiKhoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private TaiKhoan mapResultSetToTaiKhoan(ResultSet rs) throws SQLException {
        try {
            TaiKhoan taiKhoan = new TaiKhoan();

            taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
            // Mật khẩu từ database đã được mã hóa, dùng setMatKhauDaMaHoa
            taiKhoan.setMatKhauDaMaHoa(rs.getString("matKhau"));

            String maNhanVien = rs.getString("maNhanVien");
            if (maNhanVien != null) {
                NhanVien nhanVien = new NhanVien();
                nhanVien.setMaNhanVien(maNhanVien);
                nhanVien.setTenNhanVien(rs.getString("tenNhanVien"));
                nhanVien.setDiaChi(rs.getString("diaChi"));
                nhanVien.setSoDienThoai(rs.getString("soDienThoai"));
                nhanVien.setEmail(rs.getString("email"));
                
                // Chuẩn hóa vai trò: trim và chuẩn hóa giá trị
                String vaiTro = rs.getString("vaiTro");
                if (vaiTro != null) {
                    vaiTro = vaiTro.trim();
                    // Chuẩn hóa: "Nhân viên" hoặc "Quản lý" (xử lý cả tiếng Việt có dấu và không dấu)
                    if (vaiTro.equalsIgnoreCase("Nhân viên") || vaiTro.equalsIgnoreCase("Nhan vien")) {
                        vaiTro = "Nhân viên";
                    } else if (vaiTro.equalsIgnoreCase("Quản lý") || vaiTro.equalsIgnoreCase("Quan ly")) {
                        vaiTro = "Quản lý";
                    }
                    // Set vai trò (validation sẽ được thực hiện trong setVaiTro)
                    nhanVien.setVaiTro(vaiTro);
                }

                taiKhoan.setNhanVien(nhanVien);
            }

            return taiKhoan;
        } catch (Exception e) {
            throw new SQLException("Lỗi khi map dữ liệu từ ResultSet: " + e.getMessage(), e);
        }
    }

    private String taoMaTaiKhoan() {
        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_LAY_MA_CUOI); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String maCuoi = rs.getString("tenDangNhap");
                String phanSo = maCuoi.substring(3); // Bỏ "TDN"
                int soTiepTheo = Integer.parseInt(phanSo) + 1;
                return String.format("TDN%05d", soTiepTheo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TDN00001";
    }

    public boolean tonTai(String tenDangNhap) {
        return findById(tenDangNhap).isPresent();
    }

    public Optional<String> tonTaiTaiKhoanKhiLogin(String tenDangNhap, String mk) {
        String sql = "select tk.tenDangNhap, tk.matKhau, tk.maNhanVien, nv.vaiTro "
                + " from taikhoan tk "
                + " JOIN nhanvien nv on tk.maNhanVien = nv.maNhanVien "
                + " WHERE tk.tenDangNhap = ? ";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement pre = con.prepareStatement(sql)) {

            pre.setString(1, tenDangNhap);

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    String matKhauHash = rs.getString("matKhau");
                    // Xác thực mật khẩu bằng PasswordUtil
                    if (vn.edu.iuh.fit.iuhpharmacitymanagement.util.PasswordUtil.verify(mk, matKhauHash)) {
                        return Optional.ofNullable(rs.getString("vaiTro"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean tonTaiTheoMaNhanVien(String maNhanVien) {
        return timTheoMaNhanVien(maNhanVien).isPresent();
    }

    public int dem() {
        String sql = "SELECT COUNT(*) as total FROM TaiKhoan";

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //chk
    public boolean isTenDangNhap(String tenDN) {
        String sql = "select * From taikhoan "
                + " where tenDangNhap = ? ";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement pre = con.prepareStatement(sql);
            pre.setString(1, tenDN);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method để reset mật khẩu về mật khẩu mặc định (dùng để fix mật khẩu bị
     * hash 2 lần)
     *
     * @param tenDangNhap tên đăng nhập
     * @param matKhauMoi mật khẩu mới (plain text)
     * @return true nếu thành công
     */
    public boolean resetMatKhau(String tenDangNhap, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection(); PreparedStatement pre = con.prepareStatement(sql)) {
            // Hash mật khẩu mới
            String matKhauHash = vn.edu.iuh.fit.iuhpharmacitymanagement.util.PasswordUtil.encode(matKhauMoi);
            pre.setString(1, matKhauHash);
            pre.setString(2, tenDangNhap);

            return pre.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<String> findPassByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM taikhoan WHERE tenDangNhap = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                return Optional.of(rs.getString("matKhau"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Xóa tài khoản theo mã nhân viên
     * @param maNhanVien mã nhân viên
     * @return true nếu xóa thành công
     */
    public boolean deleteByMaNhanVien(String maNhanVien) {
        String sql = "DELETE FROM TaiKhoan WHERE maNhanVien = ?";
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maNhanVien);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa tài khoản theo tên đăng nhập
     * @param tenDangNhap tên đăng nhập
     * @return true nếu xóa thành công
     */
    public boolean delete(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tenDangNhap);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.NhanVienDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import java.util.List;
import java.util.Optional;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.TaiKhoanDAO;

public class NhanVienBUS {

    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;

    public NhanVienBUS() {
        this.nhanVienDAO = new NhanVienDAO();
        this.taiKhoanDAO = new TaiKhoanDAO();
    }

    public boolean taoNhanVien(NhanVien nhanVien) throws Exception {
        // Kiểm tra email đã tồn tại
        if (nhanVienDAO.existsByEmail(nhanVien.getEmail())) {
            throw new Exception("Email '" + nhanVien.getEmail() + "' đã được sử dụng.");
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (nhanVienDAO.existsByPhone(nhanVien.getSoDienThoai())) {
            throw new Exception("Số điện thoại '" + nhanVien.getSoDienThoai() + "' đã được sử dụng.");
        }

        return nhanVienDAO.insert(nhanVien);
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) throws Exception {
        // Kiểm tra nhân viên có tồn tại không
        NhanVien nhanVienCu = nhanVienDAO.findById(nhanVien.getMaNhanVien()).orElse(null);
        if (nhanVienCu == null) {
            throw new Exception("Không tìm thấy nhân viên với mã: " + nhanVien.getMaNhanVien());
        }

        // Kiểm tra email đã được sử dụng bởi nhân viên khác chưa
        if (!nhanVienCu.getEmail().equals(nhanVien.getEmail()) && nhanVienDAO.existsByEmail(nhanVien.getEmail())) {
            throw new Exception("Email '" + nhanVien.getEmail() + "' đã được sử dụng bởi nhân viên khác.");
        }

        // Kiểm tra số điện thoại đã được sử dụng bởi nhân viên khác chưa
        if (!nhanVienCu.getSoDienThoai().equals(nhanVien.getSoDienThoai()) && nhanVienDAO.existsByPhone(nhanVien.getSoDienThoai())) {
            throw new Exception("Số điện thoại '" + nhanVien.getSoDienThoai() + "' đã được sử dụng bởi nhân viên khác.");
        }

        return nhanVienDAO.update(nhanVien);
    }

    public NhanVien timNhanVienTheoMa(String maNhanVien) {
        return nhanVienDAO.findById(maNhanVien).orElse(null);
    }

    public List<NhanVien> layTatCaNhanVien() {
        return nhanVienDAO.findAll();
    }

    public List<NhanVien> timNhanVienTheoTuKhoa(String tuKhoa) {
        return nhanVienDAO.findByKeyword(tuKhoa);
    }

    public String layMaNhanVienMoi() {
        String maCuoi = nhanVienDAO.getLastMaNhanVien();
        if (maCuoi == null) {
            return "NV00001";
        }

        String phanSo = maCuoi.substring(2); // Bỏ "NV"
        int soTiepTheo = Integer.parseInt(phanSo) + 1;
        return String.format("NV%05d", soTiepTheo);
    }

    public int demSoLuongNhanVien() {
        return nhanVienDAO.count();
    }

    public boolean kiemTraEmailTonTai(String email) {
        return nhanVienDAO.existsByEmail(email);
    }

    public boolean kiemTraSoDienThoaiTonTai(String soDienThoai) {
        return nhanVienDAO.existsByPhone(soDienThoai);
    }

    public boolean xoaNhanVien(String maNhanVien) {
        return nhanVienDAO.delete(maNhanVien);
    }

    public NhanVien xacThucNguoiDung(String username, String password) {
        //Tạo đối tượng TaiKhoanDAO và gọi phương thức non-static
        Optional<String> optVaiTro = this.taiKhoanDAO.tonTaiTaiKhoanKhiLogin(username, password);

        if (optVaiTro.isPresent()) {
            // Nếu tài khoản hợp lệ, lấy thông tin chi tiết của nhân viên
            //Tên đăng nhập cũng chính là mã nhân viên
            return this.nhanVienDAO.findById(username).orElse(null);
        }

        // 4. Nếu tài khoản không hợp lệ, trả về null
        return null;
    }
}

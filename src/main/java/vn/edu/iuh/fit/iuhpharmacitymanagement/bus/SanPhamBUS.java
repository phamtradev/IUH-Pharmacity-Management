package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;

import java.util.List;
import java.util.Optional;

public class SanPhamBUS {

    private final SanPhamDAO sanPhamDAO;

    public SanPhamBUS(SanPhamDAO sanPhamDAO) {
        this.sanPhamDAO = sanPhamDAO;

    }

    public boolean taoSanPham(SanPham sanPham) {
        return sanPhamDAO.insert(sanPham);
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        return sanPhamDAO.update(sanPham);
    }

    public Optional<SanPham> timSanPhamTheoMa(String maSanPham) {
        return sanPhamDAO.findById(maSanPham);
    }

    public SanPham laySanPhamTheoMa(String maSanPham) {
        return sanPhamDAO.findById(maSanPham)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với mã = " + maSanPham));
    }

    public List<SanPham> layTatCaSanPham() {
        return sanPhamDAO.findAll();
    }

    public List<SanPham> timSanPhamTheoTen(String tenSanPham) {
        return sanPhamDAO.findByName(tenSanPham);
    }

    public List<SanPham> timSanPhamTheoLoai(LoaiSanPham loaiSanPham) {
        return sanPhamDAO.findByLoai(loaiSanPham);
    }

    public List<SanPham> laySanPhamHoatDong() {
        return sanPhamDAO.findByHoatDong(true);
    }

    public boolean kiemTraTonTaiSanPham(String maSanPham) {
        return sanPhamDAO.findById(maSanPham).isPresent();
    }

    public int demTongSoSanPham() {
        return sanPhamDAO.findAll().size();
    }

    public int demSanPhamHoatDong() {
        return sanPhamDAO.findByHoatDong(true).size();
    }

    public String layMaSanPhamCuoi() {
        return sanPhamDAO.getLastMaSanPham();
    }

    public Optional<SanPham> timSanPhamTheoSoDangKy(String soDangKy) {
        return sanPhamDAO.findBySoDangKy(soDangKy);
    }

    /**
     * Kiểm tra xem nhà cung cấp có được phép nhập sản phẩm với số đăng ký này không
     * 
     * BUSINESS RULE: Một số đăng ký chỉ được nhập bởi 1 nhà cung cấp duy nhất
     * 
     * @param soDangKy     Số đăng ký sản phẩm
     * @param maNhaCungCap Mã nhà cung cấp muốn nhập
     * @return true nếu được phép nhập, false nếu bị conflict
     */
    public boolean kiemTraNhaCungCapCoTheNhapSoDangKy(String soDangKy, String maNhaCungCap) {
        // Lấy danh sách nhà cung cấp đã từng nhập số đăng ký này
        List<String> danhSachNCCDaNhap = sanPhamDAO.getMaNhaCungCapBySoDangKy(soDangKy);

        // Nếu chưa có nhà cung cấp nào nhập → OK
        if (danhSachNCCDaNhap.isEmpty()) {
            System.out.println("✅ [BUS] Số đăng ký '" + soDangKy + "' chưa được nhập bởi ai → OK");
            return true;
        }

        // Nếu đã có nhà cung cấp nhập
        if (danhSachNCCDaNhap.contains(maNhaCungCap)) {
            // Cùng nhà cung cấp → OK
            System.out.println(
                    "✅ [BUS] Nhà cung cấp " + maNhaCungCap + " đã từng nhập số đăng ký '" + soDangKy + "' → OK");
            return true;
        } else {
            // Khác nhà cung cấp → CONFLICT!
            System.out.println("❌ [BUS] CONFLICT! Số đăng ký '" + soDangKy + "' đã được nhập bởi " +
                    danhSachNCCDaNhap + ", không thể cho NCC " + maNhaCungCap + " nhập!");
            return false;
        }
    }

    /**
     * Lấy tên nhà cung cấp đã nhập số đăng ký này (để hiển thị trong thông báo lỗi)
     * 
     * @param soDangKy Số đăng ký sản phẩm
     * @return Chuỗi tên nhà cung cấp, hoặc null nếu chưa có ai nhập
     */
    public String layTenNhaCungCapDaNhapSoDangKy(String soDangKy) {
        List<String> danhSachMaNCC = sanPhamDAO.getMaNhaCungCapBySoDangKy(soDangKy);

        if (danhSachMaNCC.isEmpty()) {
            return null;
        }

        // Lấy tên nhà cung cấp từ mã (cần NhaCungCapDAO)
        // Tạm thời trả về mã, sau này có thể join để lấy tên
        return danhSachMaNCC.get(0);
    }
}

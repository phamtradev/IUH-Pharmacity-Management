package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhaCungCapBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;

import java.util.List;
import java.util.Optional;

public class SanPhamBUS {

    private final SanPhamDAO sanPhamDAO;
    private final NhaCungCapBUS nhaCungCapBUS = new NhaCungCapBUS();

    public SanPhamBUS(SanPhamDAO sanPhamDAO) {
        this.sanPhamDAO = sanPhamDAO;
        
    }

    public boolean taoSanPham(SanPham sanPham) {
        return sanPhamDAO.insert(sanPham);
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        return sanPhamDAO.update(sanPham);
    }
    
    private String lastErrorMessage;

    public boolean xoaSanPham(String maSanPham) {
        try {
            // Check product exists
            Optional<SanPham> spOpt = sanPhamDAO.findById(maSanPham);
            if (spOpt.isEmpty()) {
                lastErrorMessage = "Không tìm thấy sản phẩm với mã: " + maSanPham;
                return false;
            }
            SanPham sp = spOpt.get();

            // Only allow delete if product is not active (hoatDong == false)
            if (sp.isHoatDong()) {
                lastErrorMessage = "Chỉ xóa được khi sản phẩm ở trạng thái 'Ngưng bán'. Vui lòng đưa sản phẩm về trạng thái 'Ngưng bán' trước khi xóa.";
                return false;
            }

            // Check if product already linked to any supplier (via soDangKy)
            String soDangKy = sp.getSoDangKy();
            if (soDangKy != null && !soDangKy.trim().isEmpty()) {
                List<String> danhSachMaNCC = sanPhamDAO.getMaNhaCungCapBySoDangKy(soDangKy);
                if (danhSachMaNCC != null && !danhSachMaNCC.isEmpty()) {
                    // Build friendly list of supplier names (limit to first few)
                    StringBuilder names = new StringBuilder();
                    int limit = Math.min(danhSachMaNCC.size(), 3);
                    for (int i = 0; i < limit; i++) {
                        try {
                            NhaCungCap ncc = nhaCungCapBUS.layNhaCungCapTheoMa(danhSachMaNCC.get(i));
                            if (ncc != null) {
                                if (names.length() > 0) names.append(", ");
                                names.append(ncc.getTenNhaCungCap()).append(" (").append(ncc.getMaNhaCungCap()).append(")");
                            }
                        } catch (Exception ex) {
                            if (names.length() > 0) names.append(", ");
                            names.append(danhSachMaNCC.get(i));
                        }
                    }
                    if (danhSachMaNCC.size() > limit) {
                        names.append(", ...");
                    }
                    lastErrorMessage = "Sản phẩm đã được nhập bởi nhà cung cấp: " + names.toString() + ". Vui lòng xóa các nhập liên quan trước khi xóa sản phẩm.";
                    return false;
                }
            }

            boolean ok = sanPhamDAO.delete(maSanPham);
            if (!ok) {
                lastErrorMessage = "Xóa không thành công (không có bản ghi bị xóa).";
            } else {
                lastErrorMessage = null;
            }
            return ok;
        } catch (java.sql.SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if (e.getErrorCode() == 547 || msg.contains("REFERENCE") || msg.contains("REFERENCES")) {
                // foreign key violation
                lastErrorMessage = "Không thể xóa sản phẩm vì còn dữ liệu liên quan (đơn hàng / lô hàng / khuyến mãi...). Vui lòng xóa các bản ghi liên quan trước.";
            } else {
                lastErrorMessage = "Lỗi khi xóa sản phẩm: " + msg;
            }
            System.err.println("SanPhamBUS.xoaSanPham SQLException: " + msg);
            return false;
        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            System.err.println("SanPhamBUS.xoaSanPham Exception: " + e.getMessage());
            return false;
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
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
     * @param soDangKy Số đăng ký sản phẩm
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
            System.out.println("✅ [BUS] Nhà cung cấp " + maNhaCungCap + " đã từng nhập số đăng ký '" + soDangKy + "' → OK");
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

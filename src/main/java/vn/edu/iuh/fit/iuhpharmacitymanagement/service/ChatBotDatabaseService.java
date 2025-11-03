package vn.edu.iuh.fit.iuhpharmacitymanagement.service;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;

import java.util.List;
import java.util.Optional;

/**
 * Service cung cấp dữ liệu từ database cho ChatBot
 * Cho phép AI truy vấn thông tin sản phẩm, tồn kho, v.v.
 */
public class ChatBotDatabaseService {
    
    private final SanPhamDAO sanPhamDAO;
    private final LoHangDAO loHangDAO;
    
    public ChatBotDatabaseService() {
        this.sanPhamDAO = new SanPhamDAO();
        this.loHangDAO = new LoHangDAO();
    }
    
    /**
     * Tìm kiếm sản phẩm theo tên (tìm gần đúng)
     */
    public String timKiemSanPham(String tenSanPham) {
        try {
            List<SanPham> danhSach = sanPhamDAO.findByName(tenSanPham);
            
            if (danhSach.isEmpty()) {
                return "Không tìm thấy sản phẩm nào với tên: " + tenSanPham;
            }
            
            StringBuilder result = new StringBuilder();
            result.append("Tìm thấy ").append(danhSach.size()).append(" sản phẩm:\n\n");
            
            for (SanPham sp : danhSach) {
                result.append("📦 ").append(sp.getTenSanPham()).append("\n");
                result.append("   - Mã: ").append(sp.getMaSanPham()).append("\n");
                result.append("   - Giá bán: ").append(String.format("%,.0f", sp.getGiaBan())).append(" VNĐ\n");
                result.append("   - Hoạt chất: ").append(sp.getHoatChat()).append("\n");
                result.append("   - Nhà sản xuất: ").append(sp.getNhaSanXuat()).append("\n");
                result.append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi tìm kiếm sản phẩm: " + e.getMessage();
        }
    }
    
    /**
     * Kiểm tra tồn kho của sản phẩm theo tên
     */
    public String kiemTraTonKho(String tenSanPham) {
        try {
            List<SanPham> danhSachSP = sanPhamDAO.findByName(tenSanPham);
            
            if (danhSachSP.isEmpty()) {
                return "Không tìm thấy sản phẩm: " + tenSanPham;
            }
            
            StringBuilder result = new StringBuilder();
            result.append("📊 Thông tin tồn kho:\n\n");
            
            for (SanPham sp : danhSachSP) {
                List<LoHang> danhSachLoHang = loHangDAO.findByMaSanPham(sp.getMaSanPham());
                
                // Đếm số lô hàng còn hạn (HSD > hôm nay + 6 tháng)
                java.time.LocalDate ngayGioiHan = java.time.LocalDate.now().plusMonths(6);
                long soLoHangHoatDong = danhSachLoHang.stream()
                        .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan))
                        .count();
                
                int tongTonKho = danhSachLoHang.stream()
                        .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan)) // Chỉ tính lô còn hạn (HSD > ngày giới hạn)
                        .mapToInt(LoHang::getTonKho)
                        .sum();
                
                result.append("🔹 ").append(sp.getTenSanPham()).append("\n");
                result.append("   - Mã SP: ").append(sp.getMaSanPham()).append("\n");
                result.append("   - Số lô hàng: ").append(soLoHangHoatDong).append(" lô\n");
                result.append("   - Tổng tồn kho: ").append(tongTonKho).append(" ");
                
                if (sp.getDonViTinh() != null) {
                    result.append(sp.getDonViTinh().getTenDonVi());
                }
                result.append("\n");
                
                // Hiển thị chi tiết các lô hàng
                if (!danhSachLoHang.isEmpty() && soLoHangHoatDong > 0) {
                    result.append("   - Chi tiết các lô hàng:\n");
                    for (LoHang lh : danhSachLoHang) {
                        if (lh.getHanSuDung().isAfter(ngayGioiHan)) {
                            result.append("     • ").append(lh.getTenLoHang())
                                  .append(": ").append(lh.getTonKho())
                                  .append(" (HSD: ").append(lh.getHanSuDung()).append(")\n");
                        }
                    }
                } else {
                    result.append("   - Chưa có lô hàng nào\n");
                }
                result.append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi kiểm tra tồn kho: " + e.getMessage();
        }
    }
    
    /**
     * Đếm số lô hàng của sản phẩm theo tên
     */
    public String demSoLoHang(String tenSanPham) {
        try {
            List<SanPham> danhSachSP = sanPhamDAO.findByName(tenSanPham);
            
            if (danhSachSP.isEmpty()) {
                return "Không tìm thấy sản phẩm: " + tenSanPham;
            }
            
            StringBuilder result = new StringBuilder();
            result.append("📦 Thông tin số lượng lô hàng:\n\n");
            
            for (SanPham sp : danhSachSP) {
                List<LoHang> danhSachLoHang = loHangDAO.findByMaSanPham(sp.getMaSanPham());
                
                // Đếm số lô hàng còn hạn (HSD > hôm nay + 6 tháng)
                java.time.LocalDate ngayGioiHan = java.time.LocalDate.now().plusMonths(6);
                long soLoHangConHan = danhSachLoHang.stream()
                        .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan))
                        .count();
                
                // Đếm tổng số lô hàng (cả hoạt động và không hoạt động)
                int tongSoLoHang = danhSachLoHang.size();
                
                result.append("🔹 ").append(sp.getTenSanPham()).append("\n");
                result.append("   - Mã SP: ").append(sp.getMaSanPham()).append("\n");
                result.append("   - Tổng số lô hàng: ").append(tongSoLoHang).append(" lô\n");
                result.append("   - Lô còn hạn: ").append(soLoHangConHan).append(" lô\n");
                
                if (tongSoLoHang > soLoHangConHan) {
                    result.append("   - Lô hết hạn: ").append(tongSoLoHang - soLoHangConHan).append(" lô\n");
                }
                
                result.append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi đếm số lô hàng: " + e.getMessage();
        }
    }
    
    /**
     * Lấy thông tin chi tiết sản phẩm theo mã
     */
    public String layThongTinSanPham(String maSanPham) {
        try {
            Optional<SanPham> spOpt = sanPhamDAO.findById(maSanPham);
            
            if (!spOpt.isPresent()) {
                return "Không tìm thấy sản phẩm với mã: " + maSanPham;
            }
            
            SanPham sp = spOpt.get();
            StringBuilder result = new StringBuilder();
            
            result.append("📋 Thông tin chi tiết sản phẩm:\n\n");
            result.append("🔹 Tên: ").append(sp.getTenSanPham()).append("\n");
            result.append("🔹 Mã: ").append(sp.getMaSanPham()).append("\n");
            result.append("🔹 Số đăng ký: ").append(sp.getSoDangKy()).append("\n");
            result.append("🔹 Hoạt chất: ").append(sp.getHoatChat()).append("\n");
            result.append("🔹 Liều dùng: ").append(sp.getLieuDung()).append("\n");
            result.append("🔹 Đóng gói: ").append(sp.getCachDongGoi()).append("\n");
            result.append("🔹 Nhà sản xuất: ").append(sp.getNhaSanXuat()).append("\n");
            result.append("🔹 Quốc gia: ").append(sp.getQuocGiaSanXuat()).append("\n");
            result.append("🔹 Giá nhập: ").append(String.format("%,.0f", sp.getGiaNhap())).append(" VNĐ\n");
            result.append("🔹 Giá bán: ").append(String.format("%,.0f", sp.getGiaBan())).append(" VNĐ\n");
            result.append("🔹 Loại: ").append(sp.getLoaiSanPham()).append("\n");
            result.append("🔹 Trạng thái: ").append(sp.isHoatDong() ? "Đang hoạt động" : "Ngừng hoạt động").append("\n");
            
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy thông tin sản phẩm: " + e.getMessage();
        }
    }
    
    /**
     * Lấy danh sách sản phẩm sắp hết hạn
     */
    public String laySanPhamSapHetHan() {
        try {
            List<LoHang> danhSach = loHangDAO.timSanPhamHetHan();
            
            if (danhSach.isEmpty()) {
                return "Hiện không có sản phẩm nào sắp hết hạn (trong vòng 6 tháng).";
            }
            
            StringBuilder result = new StringBuilder();
            result.append("⚠️ Danh sách sản phẩm sắp hết hạn (").append(danhSach.size()).append(" lô):\n\n");
            
            for (LoHang lh : danhSach) {
                SanPham sp = lh.getSanPham();
                result.append("🔸 ").append(sp.getTenSanPham()).append("\n");
                result.append("   - Lô: ").append(lh.getTenLoHang()).append("\n");
                result.append("   - HSD: ").append(lh.getHanSuDung()).append("\n");
                result.append("   - Tồn kho: ").append(lh.getTonKho());
                if (sp.getDonViTinh() != null) {
                    result.append(" ").append(sp.getDonViTinh().getTenDonVi());
                }
                result.append("\n\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy danh sách sản phẩm sắp hết hạn: " + e.getMessage();
        }
    }
    
    /**
     * Thống kê tổng quan
     */
    public String layThongKeTongQuan() {
        try {
            int tongSanPham = sanPhamDAO.findAll().size();
            int tongLoHang = loHangDAO.count();
            List<LoHang> sapHetHan = loHangDAO.timSanPhamHetHan();
            
            StringBuilder result = new StringBuilder();
            result.append("📊 Thống kê tổng quan:\n\n");
            result.append("🔹 Tổng số sản phẩm: ").append(tongSanPham).append("\n");
            result.append("🔹 Tổng số lô hàng: ").append(tongLoHang).append("\n");
            result.append("🔹 Lô hàng sắp hết hạn: ").append(sapHetHan.size()).append("\n");
            
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy thống kê: " + e.getMessage();
        }
    }
}


package vn.edu.iuh.fit.iuhpharmacitymanagement.service;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.PhuongThucThanhToan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Map;

/**
 * Service cung cấp dữ liệu từ database cho ChatBot Cho phép AI truy vấn thông
 * tin sản phẩm, tồn kho, v.v.
 */
public class ChatBotDatabaseService {

    private final SanPhamDAO sanPhamDAO;
    private final LoHangDAO loHangDAO;
    private final DonHangDAO donHangDAO;
    private final NumberFormat currencyFormat;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ChatBotDatabaseService() {
        this.sanPhamDAO = new SanPhamDAO();
        this.loHangDAO = new LoHangDAO();
        this.donHangDAO = new DonHangDAO();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
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
            // Giá nhập hiển thị theo FIFO từ lô hàng (nếu có), fallback về trường giaNhap của sản phẩm
            double giaNhapHienThi = sp.getGiaNhap();
            try {
                List<LoHang> dsLo = loHangDAO.findByMaSanPham(sp.getMaSanPham());
                LocalDate today = LocalDate.now();
                giaNhapHienThi = dsLo.stream()
                        .filter(lh -> lh.getTonKho() > 0 && lh.getHanSuDung() != null && !lh.getHanSuDung().isBefore(today))
                        .sorted(Comparator.comparing(LoHang::getHanSuDung).thenComparing(LoHang::getMaLoHang))
                        .map(lh -> lh.getGiaNhapLo() > 0 ? lh.getGiaNhapLo() : sp.getGiaNhap())
                        .findFirst()
                        .orElse(sp.getGiaNhap());
            } catch (Exception ignored) {
                // Nếu có lỗi, giữ nguyên giá nhập từ sản phẩm
            }
            result.append("🔹 Giá nhập (FIFO): ").append(String.format("%,.0f", giaNhapHienThi)).append(" VNĐ\n");
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
     * Lấy thông tin các lô sắp hết hạn theo tên sản phẩm (tìm gần đúng)
     * Dùng cho câu hỏi kiểu: "sản phẩm XXX có lô nào sắp hết hạn không?"
     */
    public String layLoSapHetHanTheoTenSanPham(String tenSanPham) {
        try {
            List<SanPham> danhSachSP = sanPhamDAO.findByName(tenSanPham);

            if (danhSachSP.isEmpty()) {
                return "Không tìm thấy sản phẩm nào với tên: " + tenSanPham;
            }

            LocalDate gioiHan = LocalDate.now().plusMonths(6);
            StringBuilder result = new StringBuilder();
            result.append("⚠️ Thông tin lô sắp hết hạn theo sản phẩm:\n\n");

            boolean coItNhatMotLo = false;

            for (SanPham sp : danhSachSP) {
                List<LoHang> danhSachLo = loHangDAO.findByMaSanPham(sp.getMaSanPham());

                // Lọc các lô trong vòng 6 tháng tới và còn tồn kho
                List<LoHang> loSapHetHan = new ArrayList<>();
                for (LoHang lh : danhSachLo) {
                    if (lh.getHanSuDung() != null
                            && !lh.getHanSuDung().isAfter(gioiHan) // HSD <= ngày giới hạn
                            && lh.getTonKho() > 0) {
                        loSapHetHan.add(lh);
                    }
                }

                if (loSapHetHan.isEmpty()) {
                    result.append("🔹 ").append(sp.getTenSanPham()).append(" (Mã: ")
                            .append(sp.getMaSanPham()).append(")\n");
                    result.append("   → Hiện chưa có lô nào sắp hết hạn trong vòng 6 tháng.\n\n");
                    continue;
                }

                coItNhatMotLo = true;
                result.append("🔹 ").append(sp.getTenSanPham()).append(" (Mã: ")
                        .append(sp.getMaSanPham()).append(")\n");
                result.append("   • Số lô sắp hết hạn: ").append(loSapHetHan.size()).append("\n");
                result.append("   • Chi tiết các lô:\n");

                for (LoHang lh : loSapHetHan) {
                    result.append("     • Lô: ").append(lh.getTenLoHang())
                            .append(" | HSD: ").append(lh.getHanSuDung())
                            .append(" | Tồn kho: ").append(lh.getTonKho());
                    if (sp.getDonViTinh() != null) {
                        result.append(" ").append(sp.getDonViTinh().getTenDonVi());
                    }
                    result.append("\n");
                }
                result.append("\n");
            }

            if (!coItNhatMotLo) {
                return "Không có lô nào sắp hết hạn (trong vòng 6 tháng) cho các sản phẩm khớp với: " + tenSanPham;
            }

            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi kiểm tra lô sắp hết hạn theo sản phẩm: " + e.getMessage();
        }
    }

    /**
     * Thông tin các lô / sản phẩm cần xuất hủy
     * Gồm: 
     *  - Các lô đã hết hạn tính đến hôm nay (HSD <= hôm nay, còn tồn kho)
     *  - Hàng từ đơn trả cần hủy.
     */
    public String layThongTinDonCanXuatHuy() {
        try {
            // Toàn bộ lô sắp hết hạn (<= 6 tháng, tồn kho > 0, trạng thái còn hoạt động)
            List<LoHang> loSapHetHan = loHangDAO.timSanPhamHetHan();

            // Lọc lại: chỉ giữ những lô đã hết hạn tính đến hôm nay (cần xuất hủy thực tế)
            LocalDate homNay = LocalDate.now();
            List<LoHang> loCanHuyTheoHSD = new ArrayList<>();
            for (LoHang lh : loSapHetHan) {
                if (lh.getHanSuDung() != null && !lh.getHanSuDung().isAfter(homNay)) {
                    loCanHuyTheoHSD.add(lh);
                }
            }

            // Các lô cần hủy do trả hàng
            List<Map<String, Object>> loTuDonTra = loHangDAO.findForDisposalFromReturns();

            int tongSo = loCanHuyTheoHSD.size() + loTuDonTra.size();
            if (tongSo == 0) {
                return "Hiện không có sản phẩm hay đơn nào cần xuất hủy.";
            }

            StringBuilder result = new StringBuilder();
            result.append("🗑️ Thông tin sản phẩm cần xuất hủy:\n\n");
            result.append("🔹 Tổng số mục cần xử lý: ").append(tongSo).append("\n");
            result.append("   • Từ lô đã hết hạn (tính đến hôm nay): ").append(loCanHuyTheoHSD.size()).append("\n");
            result.append("   • Từ đơn trả hàng: ").append(loTuDonTra.size()).append("\n\n");

            if (!loCanHuyTheoHSD.isEmpty()) {
                result.append("📦 Các lô đã hết hạn (còn tồn kho):\n");
                for (LoHang lh : loCanHuyTheoHSD) {
                    SanPham sp = lh.getSanPham();
                    result.append("   • ").append(sp != null ? sp.getTenSanPham() : "Không rõ sản phẩm")
                            .append(" | Lô: ").append(lh.getTenLoHang())
                            .append(" | HSD: ").append(lh.getHanSuDung())
                            .append(" | Tồn kho: ").append(lh.getTonKho());
                    if (sp != null && sp.getDonViTinh() != null) {
                        result.append(" ").append(sp.getDonViTinh().getTenDonVi());
                    }
                    result.append("\n");
                }
                result.append("\n");
            }

            if (!loTuDonTra.isEmpty()) {
                result.append("📥 Sản phẩm cần hủy do khách trả hàng:\n");
                for (Map<String, Object> item : loTuDonTra) {
                    Object loObj = item.get("loHang");
                    String lyDo = String.valueOf(item.get("lyDo"));
                    if (loObj instanceof LoHang) {
                        LoHang lh = (LoHang) loObj;
                        SanPham sp = lh.getSanPham();
                        result.append("   • ").append(sp != null ? sp.getTenSanPham() : "Không rõ sản phẩm")
                                .append(" | Lô: ").append(lh.getTenLoHang())
                                .append(" | HSD: ").append(lh.getHanSuDung())
                                .append(" | Tồn kho: ").append(lh.getTonKho());
                        if (sp != null && sp.getDonViTinh() != null) {
                            result.append(" ").append(sp.getDonViTinh().getTenDonVi());
                        }
                        if (lyDo != null && !lyDo.trim().isEmpty() && !"null".equalsIgnoreCase(lyDo.trim())) {
                            result.append(" | Lý do: ").append(lyDo.trim());
                        }
                        result.append("\n");
                    }
                }
            }

            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy thông tin đơn cần xuất hủy: " + e.getMessage();
        }
    }

    /**
     * Thống kê tổng quan
     */
    public String layThongKeTongQuan() {
        try {
            int tongSanPham = sanPhamDAO.findAll().size();
            int tongLoHang = loHangDAO.count();
            int tongDonHang = donHangDAO.count();
            double tongDoanhThu = donHangDAO.sumThanhTien();

            LocalDate homNay = LocalDate.now();
            int donHangHomNay = donHangDAO.countByDate(homNay);
            double doanhThuHomNay = donHangDAO.sumThanhTienByDate(homNay);
            List<DonHang> hoaDonHomNay = donHangDAO.findByDate(homNay);
            List<LoHang> sapHetHan = loHangDAO.timSanPhamHetHan();

            StringBuilder result = new StringBuilder();
            result.append("📊 Thống kê tổng quan:\n\n");
            result.append("🔹 Tổng số sản phẩm: ").append(tongSanPham).append("\n");
            result.append("🔹 Tổng số lô hàng: ").append(tongLoHang).append("\n");
            result.append("🔹 Tổng số đơn hàng: ").append(tongDonHang).append("\n");
            result.append("🔹 Tổng doanh thu: ").append(formatCurrency(tongDoanhThu)).append("\n\n");

            result.append("📅 Hôm nay (").append(homNay.format(DATE_FORMAT)).append("):\n");
            result.append("   • Số đơn đã bán: ").append(donHangHomNay).append("\n");
            result.append("   • Doanh thu hôm nay: ").append(formatCurrency(doanhThuHomNay)).append("\n");
            if (hoaDonHomNay.isEmpty()) {
                result.append("   • Chưa có hóa đơn nào.\n");
            } else {
                result.append("   • Danh sách mã hóa đơn: ");
                List<String> maDon = new ArrayList<>();
                for (DonHang dh : hoaDonHomNay) {
                    maDon.add(dh.getMaDonHang());
                }
                result.append(String.join(", ", maDon));
                result.append("\n");
            }

            result.append("\n⚠️ Lô hàng sắp hết hạn: ").append(sapHetHan.size()).append(" lô.");
            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy thống kê: " + e.getMessage();
        }
    }

    public String layThongTinBanHangHomNay() {
        LocalDate homNay = LocalDate.now();
        try {
            List<DonHang> hoaDonHomNay = donHangDAO.findByDate(homNay);
            int soDon = hoaDonHomNay.size();
            double doanhThu = donHangDAO.sumThanhTienByDate(homNay);

            if (hoaDonHomNay.isEmpty()) {
                return "Hôm nay (" + homNay.format(DATE_FORMAT) + ") chưa phát sinh hóa đơn nào.";
            }

            StringBuilder result = new StringBuilder();
            result.append("📅 Kết quả bán hàng hôm nay (").append(homNay.format(DATE_FORMAT)).append("):\n\n");
            result.append("🔹 Số đơn đã bán: ").append(soDon).append("\n");
            result.append("🔹 Doanh thu: ").append(formatCurrency(doanhThu)).append("\n");
            result.append("🔹 Danh sách hóa đơn:\n");
            for (DonHang dh : hoaDonHomNay) {
                result.append("   • ").append(dh.getMaDonHang());

                KhachHang kh = dh.getKhachHang();
                if (kh != null) {
                    String thongTinKh = kh.getTenKhachHang();
                    if (thongTinKh == null || thongTinKh.isBlank()) {
                        thongTinKh = kh.getMaKhachHang();
                    }
                    if (thongTinKh != null && !thongTinKh.isBlank()) {
                        result.append(" | KH: ").append(thongTinKh);
                    }
                }

                NhanVien nv = dh.getNhanVien();
                if (nv != null) {
                    String thongTinNv = nv.getTenNhanVien();
                    if ((thongTinNv == null || thongTinNv.isBlank()) && nv.getMaNhanVien() != null) {
                        thongTinNv = nv.getMaNhanVien();
                    }
                    if (thongTinNv != null && !thongTinNv.isBlank()) {
                        result.append(" | NV: ").append(thongTinNv);
                    }
                }

                result.append(" | ").append(formatPaymentMethod(dh.getPhuongThucThanhToan()));
                result.append(" | Giá trị: ").append(formatCurrency(dh.getThanhTien())).append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy dữ liệu bán hàng hôm nay: " + e.getMessage();
        }
    }

    public String layThongTinSanPhamTheoTen(String tenSanPham) {
        try {
            List<SanPham> danhSach = sanPhamDAO.findByName(tenSanPham);

            if (danhSach.isEmpty()) {
                return "Không tìm thấy sản phẩm nào với tên: " + tenSanPham;
            }

            StringBuilder result = new StringBuilder();
            result.append("📘 Thông tin thuốc tìm được:\n\n");

            for (SanPham sp : danhSach) {
                List<LoHang> danhSachLo = loHangDAO.findByMaSanPham(sp.getMaSanPham());
                int soLo = danhSachLo.size();
                int tongSoLuong = 0;
                for (LoHang lo : danhSachLo) {
                    tongSoLuong += lo.getTonKho();
                }
                //append
                result.append("🔹 ").append(sp.getTenSanPham()).append("\n");
                result.append("   - Mã SP: ").append(sp.getMaSanPham()).append("\n");
                result.append("   - Giá bán: ").append(formatCurrency(sp.getGiaBan())).append("\n");
                result.append("   - Hoạt chất: ").append(sp.getHoatChat()).append("\n");
                result.append("   - Nhà sản xuất: ").append(sp.getNhaSanXuat()).append("\n");
                result.append("   - Số lô hiện có: ").append(soLo).append("\n");
                result.append("   - Tồn kho: ").append(tongSoLuong);
                if (sp.getDonViTinh() != null) {
                    result.append(" ").append(sp.getDonViTinh().getTenDonVi());
                }
                result.append("\n");
                //kiem tra empty
                if (!danhSachLo.isEmpty()) {
                    result.append("   - Chi tiết lô hàng:\n");
                    for (LoHang lo : danhSachLo) {
                        result.append("     • ").append(lo.getTenLoHang())
                                .append(": ").append(lo.getTonKho())
                                .append(" (HSD: ").append(lo.getHanSuDung()).append(")\n");
                    }
                }
                result.append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "Lỗi khi lấy thông tin sản phẩm: " + e.getMessage();
        }
    }

    private String formatCurrency(double value) {
        synchronized (currencyFormat) {
            currencyFormat.setMaximumFractionDigits(0);
            return currencyFormat.format(value);
        }
    }

    private String formatPaymentMethod(PhuongThucThanhToan method) {
        if (method == null) {
            return "PTTT: Chưa xác định";
        }
        switch (method) {
            case TIEN_MAT:
                return "PTTT: Tiền mặt";
            case CHUYEN_KHOAN_NGAN_HANG:
                return "PTTT: Chuyển khoản NH";
            default:
                return "PTTT: " + method.name();
        }
    }
}

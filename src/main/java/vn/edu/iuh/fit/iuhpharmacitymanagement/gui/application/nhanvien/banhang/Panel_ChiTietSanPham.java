/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.GiaBanTheoLoService;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPham extends javax.swing.JPanel {
    
    private SanPham sanPham;
    private DecimalFormat currencyFormat;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblTenSP;
    private LoHangBUS loHangBUS;
    private Panel_ChonLo panelChonLo;
    private List<LoHang> danhSachLoHang;
    private double cachedTongTien = 0; // Cache giá trị tổng tiền để detect thay đổi
    private javax.swing.JPanel containerLoHang; // Container chứa nhiều lô (hiển thị dọc)
    private javax.swing.JScrollPane scrollPaneLoHang; // ScrollPane để kiểm soát scrollbar
    private boolean daThongBaoCongDon = false; // Flag để tracking đã thông báo cộng dồn chưa
    private vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai khuyenMaiDuocApDung; // Khuyến mãi được áp dụng cho sản phẩm này (nullable)
    private double phanTramGiamGia = 0.0; // % giảm giá (dạng thập phân: 0.1 = 10%)
    private Double soTienGiamGiaThucTe = null; // Số tiền giảm giá thực tế (chỉ cho số lượng tối đa), null nếu áp dụng cho toàn bộ
    private final GiaBanTheoLoService giaBanTheoLoService = new GiaBanTheoLoService();

    public Panel_ChiTietSanPham() {
        this.currencyFormat = new DecimalFormat("#,###");
        this.loHangBUS = new LoHangBUS();
        initComponents();
    }
    
    public Panel_ChiTietSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
        this.currencyFormat = new DecimalFormat("#,###");
        this.loHangBUS = new LoHangBUS();
        this.daThongBaoCongDon = false; // Reset flag khi chọn sản phẩm mới
        initComponents();
        loadSanPhamData();
        loadLoHangData();
    }
    
    private void loadSanPhamData() {
        if (sanPham != null) {
            // Set tên sản phẩm với đơn vị tính (sử dụng HTML để hiển thị 2 dòng)
            String donViTinh = sanPham.getDonViTinh() != null ? 
                sanPham.getDonViTinh().getTenDonVi() : "";
            
            String htmlText = "<html><div style='text-align: left;'>" +
                "<div style='font-size: 14px; font-weight: normal;'>" + sanPham.getTenSanPham() + "</div>" +
                (donViTinh.isEmpty() ? "" : 
                    "<div style='font-size: 11px; color: #666666; margin-top: 2px;'>Đơn vị: " + donViTinh + "</div>") +
                "</div></html>";
            
            lblTenSP.setText(htmlText);
            
            // Set giảm giá mặc định là 0%
            txtDiscount.setText("0%");
            
            // Load hình ảnh nếu có
            if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
                try {
                    // Thử load từ đường dẫn tuyệt đối (nếu file được chọn từ JFileChooser)
                    java.io.File imageFile = new java.io.File(sanPham.getHinhAnh());
                    ImageIcon icon = null;
                    
                    if (imageFile.exists()) {
                        // File tồn tại với đường dẫn tuyệt đối
                        icon = new ImageIcon(sanPham.getHinhAnh());
                    } else {
                        // Thử load từ resources
                        java.net.URL imgURL = getClass().getResource("/img/" + sanPham.getHinhAnh());
                        if (imgURL != null) {
                            icon = new ImageIcon(imgURL);
                        }
                    }
                    
                    if (icon != null && icon.getIconWidth() > 0) {
                        java.awt.Image img = icon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
                        lblHinh.setIcon(new ImageIcon(img));
                        lblHinh.setText("");
                    } else {
                        lblHinh.setText("IMG");
                    }
                } catch (Exception e) {
                    lblHinh.setText("IMG");
                }
            } else {
                lblHinh.setText("IMG");
            }

            // Cập nhật lại tổng tiền sau khi dữ liệu sản phẩm và lô sẵn sàng
            updateTongTien();
        }
    }
    
    private void updateTongTien() {
        if (sanPham == null) {
            return;
        }

        Map<LoHang, Integer> phanBo = getMapLoHangVaSoLuong();
        PricingComputation pricing = tinhTongTienTheoPhanBo(phanBo);

        double oldTongTien = cachedTongTien;
        cachedTongTien = pricing.tongTien;
        txtTongTien.setText(currencyFormat.format(pricing.tongTien) + " đ");

        // Đơn giá chưa VAT
        if (txtDonGiaChuaVAT != null) {
            if (pricing.donGiaDonChuaVAT > 0) {
                txtDonGiaChuaVAT.setText(currencyFormat.format(pricing.donGiaDonChuaVAT) + " đ");
            } else {
                txtDonGiaChuaVAT.setText("-");
            }
            txtDonGiaChuaVAT.setToolTipText(pricing.tooltip);
        }

        // Đơn giá đã VAT
        if (pricing.donGiaDon > 0) {
            txtDonGia.setText(currencyFormat.format(pricing.donGiaDon) + " đ");
        } else {
            txtDonGia.setText("-");
        }
        txtDonGia.setToolTipText(pricing.tooltip);

        firePropertyChange("tongTien", oldTongTien, pricing.tongTien);
    }

    public int getSoLuong() {
        return (int) spinnerSoLuong.getValue();
    }
    
    /**
     * Set số lượng cho sản phẩm (dùng khi cộng dồn số lượng)
     * @param soLuong số lượng mới
     */
    public void setSoLuong(int soLuong) {
        if (soLuong >= 1 && soLuong <= 1000) {
            spinnerSoLuong.setValue(soLuong);
            spinnerSoLuongStateChanged(null); // Trigger update tổng tiền
        }
    }
    
    public SanPham getSanPham() {
        return sanPham;
    }
    
    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
        loadSanPhamData();
        loadLoHangData();
    }
    
    public double getTongTien() {
        if (sanPham == null) {
            return 0;
        }
        Map<LoHang, Integer> phanBo = getMapLoHangVaSoLuong();
        return tinhTongTienTheoPhanBo(phanBo).tongTien;
    }
    
    /**
     * Load danh sách lô hàng của sản phẩm
     */
    private void loadLoHangData() {
        if (sanPham != null) {
            // Lấy danh sách lô hàng của sản phẩm
            danhSachLoHang = loHangBUS.getLoHangBySanPham(sanPham);
            
            // Nếu có lô hàng, hiển thị lô đầu tiên
            if (!danhSachLoHang.isEmpty()) {
                // Tìm lô có tồn kho > 0 và còn hạn sử dụng (ưu tiên FIFO - HSD gần nhất)
                LoHang loHangHopLe = danhSachLoHang.stream()
                    .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
                    .sorted((lh1, lh2) -> lh1.getHanSuDung().compareTo(lh2.getHanSuDung()))
                    .findFirst()
                    .orElse(danhSachLoHang.get(0)); // Nếu không có lô hợp lệ, lấy lô đầu tiên
                
                // Cập nhật hiển thị lô đầu tiên vào container
                containerLoHang.removeAll();
                panelChonLo = new Panel_ChonLo();
                panelChonLo.setLoHang(loHangHopLe);
                containerLoHang.add(panelChonLo);
                containerLoHang.revalidate();
                containerLoHang.repaint();

                // Sau khi đã có danh sách lô và lô hiển thị, cập nhật lại tổng tiền
                // để đơn giá/tổng tiền sử dụng đúng giá theo từng lô (thay vì giá fallback của sản phẩm)
                updateTongTien();
            }
        }
    }
    
    /**
     * Tính tổng tồn kho của tất cả các lô còn hiệu lực
     * @return tổng tồn kho
     */
    private int tinhTongTonKho() {
        if (danhSachLoHang == null || danhSachLoHang.isEmpty()) {
            return 0;
        }
        
        return danhSachLoHang.stream()
            .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
            .mapToInt(LoHang::getTonKho)
            .sum();
    }
    
    /**
     * Phân bổ số lượng yêu cầu vào các lô hàng (theo FIFO)
     * @param soLuongYeuCau số lượng cần
     * @return Map<LoHang, Integer> - Map lô hàng và số lượng lấy từ lô đó
     */
    private java.util.Map<LoHang, Integer> phanBoLoHang(int soLuongYeuCau) {
        java.util.Map<LoHang, Integer> mapLoHangVaSoLuong = new java.util.LinkedHashMap<>();
        
        if (danhSachLoHang == null || danhSachLoHang.isEmpty()) {
            return mapLoHangVaSoLuong;
        }
        
        // Lấy danh sách lô hợp lệ và sắp xếp theo FIFO
        List<LoHang> danhSachLoHopLe = danhSachLoHang.stream()
            .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
            .sorted((lh1, lh2) -> lh1.getHanSuDung().compareTo(lh2.getHanSuDung()))
            .collect(java.util.stream.Collectors.toList());
        
        int soLuongConLai = soLuongYeuCau;
        
        for (LoHang loHang : danhSachLoHopLe) {
            if (soLuongConLai <= 0) break;
            
            int soLuongLay = Math.min(loHang.getTonKho(), soLuongConLai);
            mapLoHangVaSoLuong.put(loHang, soLuongLay);
            soLuongConLai -= soLuongLay;
        }
        
        return mapLoHangVaSoLuong;
    }
    
    /**
     * Tìm lô phù hợp với số lượng yêu cầu
     * - Nếu số lượng <= tổng tồn kho: Hiển thị các lô cần lấy
     * - Nếu số lượng > tổng tồn kho: Báo lỗi
     * @param soLuongYeuCau số lượng cần
     */
    private void timVaChuyenLoPhiHop(int soLuongYeuCau) {
        if (danhSachLoHang == null || danhSachLoHang.isEmpty()) {
            return;
        }
        
        // Tính tổng tồn kho
        int tongTonKho = tinhTongTonKho();
        String donViTinh = sanPham.getDonViTinh() != null ? 
            sanPham.getDonViTinh().getTenDonVi() : "sản phẩm";
        
        // Kiểm tra xem số lượng yêu cầu có vượt quá tổng tồn kho không
        if (soLuongYeuCau > tongTonKho) {
            // Báo lỗi
            raven.toast.Notifications.getInstance().show(
                raven.toast.Notifications.Type.ERROR,
                raven.toast.Notifications.Location.TOP_CENTER,
                "❌ Không đủ hàng! Sản phẩm '" + sanPham.getTenSanPham() + 
                "' chỉ còn " + tongTonKho + " " + donViTinh + " trong kho."
            );
            
            // Reset về tổng tồn kho
            spinnerSoLuong.setValue(tongTonKho);
            return;
        }
        
        // Phân bổ số lượng vào các lô
        java.util.Map<LoHang, Integer> mapLoHangVaSoLuong = phanBoLoHang(soLuongYeuCau);
        
        // Thông báo khi cộng dồn vào lô thứ 2 trở đi (chỉ thông báo 1 lần)
        if (mapLoHangVaSoLuong.size() >= 2 && !daThongBaoCongDon) {
            // Tạo thông báo chi tiết về các lô được sử dụng
            StringBuilder message = new StringBuilder("📦 Đang lấy hàng từ " + mapLoHangVaSoLuong.size() + " lô:\n");
            int index = 1;
            for (java.util.Map.Entry<LoHang, Integer> entry : mapLoHangVaSoLuong.entrySet()) {
                LoHang loHang = entry.getKey();
                int soLuongLay = entry.getValue();
                message.append(String.format("  Lô %d: %d %s (HSD: %s)\n", 
                    index++, 
                    soLuongLay, 
                    donViTinh,
                    loHang.getHanSuDung()));
            }
            
            raven.toast.Notifications.getInstance().show(
                raven.toast.Notifications.Type.INFO,
                raven.toast.Notifications.Location.TOP_CENTER,
                message.toString()
            );
            
            // Đánh dấu đã thông báo
            daThongBaoCongDon = true;
        }
        
        // Cập nhật hiển thị
        capNhatHienThiLoHang(mapLoHangVaSoLuong);
    }
    
    /**
     * Cập nhật hiển thị các lô hàng dựa trên map phân bổ
     * @param mapLoHangVaSoLuong Map<LoHang, Integer> - Map lô hàng và số lượng lấy
     */
    private void capNhatHienThiLoHang(java.util.Map<LoHang, Integer> mapLoHangVaSoLuong) {
        if (mapLoHangVaSoLuong == null || mapLoHangVaSoLuong.isEmpty()) {
            return;
        }
        
        containerLoHang.removeAll();
        
        // Thêm các Panel_ChonLo cho mỗi lô trong map
        for (java.util.Map.Entry<LoHang, Integer> entry : mapLoHangVaSoLuong.entrySet()) {
            LoHang loHang = entry.getKey();
            int soLuongLay = entry.getValue();
            
            Panel_ChonLo panel = new Panel_ChonLo();
            panel.setLoHang(loHang);
            
            // Nếu có nhiều lô, hiển thị số lượng lấy
            if (mapLoHangVaSoLuong.size() > 1) {
                panel.hienThiSoLuongLay(soLuongLay);
            }
            
            // Thêm tooltip hiển thị số lượng lấy
            panel.setToolTipText(String.format("Lấy %d/%d từ lô %s (HSD: %s)", 
                soLuongLay, 
                loHang.getTonKho(),
                loHang.getMaLoHang(),
                loHang.getHanSuDung()));
            
            // Đặt kích thước cố định cho panel để căn chỉnh đều
            panel.setMaximumSize(new java.awt.Dimension(150, 80));
            panel.setPreferredSize(new java.awt.Dimension(150, 80));
            
            containerLoHang.add(panel);
            
            // Thêm khoảng cách 5px giữa các lô (nếu không phải lô cuối)
            if (mapLoHangVaSoLuong.size() > 1) {
                containerLoHang.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 5)));
            }
        }
        // Kiểm soát scrollbar: chỉ hiển thị khi có >= 2 lô
        if (mapLoHangVaSoLuong.size() >= 2) {
            scrollPaneLoHang.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        } else {
            scrollPaneLoHang.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        }
        
        // Cập nhật UI
        containerLoHang.revalidate();
        containerLoHang.repaint();
        
        // Cập nhật panel chính
        this.revalidate();
        this.repaint();
    }
    
    public List<LoHang> getDanhSachLoHang() {
        return danhSachLoHang;
    }
    
    /**
     * Lấy lô hàng đã chọn (lô đầu tiên nếu có nhiều lô)
     * @return LoHang đầu tiên trong container
     */
    public LoHang getLoHangDaChon() {
        if (containerLoHang != null && containerLoHang.getComponentCount() > 0) {
            java.awt.Component firstComponent = containerLoHang.getComponent(0);
            if (firstComponent instanceof Panel_ChonLo) {
                return ((Panel_ChonLo) firstComponent).getLoHang();
            }
        }
        return null;
    }
    
    /**
     * Lấy map phân bổ lô hàng và số lượng (dùng khi thanh toán)
     * @return Map<LoHang, Integer> - Map lô hàng và số lượng lấy từ lô đó
     */
    public java.util.Map<LoHang, Integer> getMapLoHangVaSoLuong() {
        return phanBoLoHang(getSoLuong());
    }

    private PricingComputation tinhTongTienTheoPhanBo(Map<LoHang, Integer> phanBo) {
        PricingComputation result = new PricingComputation();
        if (sanPham == null) {
            return result;
        }

        if (phanBo == null || phanBo.isEmpty()) {
            // Không có thông tin lô hàng (ví dụ: sản phẩm chưa có lô hoặc lỗi dữ liệu),
            // fallback về đơn giá theo cấu hình lãi chuẩn để đảm bảo nhất quán.
            double donGiaChuaVAT = giaBanTheoLoService.tinhDonGiaChuaVAT(null, sanPham);
            double donGiaCoVAT = giaBanTheoLoService.tinhDonGiaCoVAT(null, sanPham);
            int soLuong = getSoLuong();
            result.tongTienChuaVAT = donGiaChuaVAT * soLuong;
            result.tongTien = donGiaCoVAT * soLuong;
            result.donGiaDonChuaVAT = donGiaChuaVAT;
            result.donGiaDon = donGiaCoVAT;
            result.soLo = 1;
            result.multiLot = false;
            result.tooltip = "<html>1 lô (mặc định) • "
                    + soLuong + " × " + currencyFormat.format(donGiaCoVAT) + " = "
                    + currencyFormat.format(result.tongTien) + " đ</html>";
            return result;
        }

        double tongTienCoVAT = 0;
        double tongTienChuaVAT = 0;
        int tongSoLuong = 0;
        StringBuilder tooltip = new StringBuilder("<html>");
        int index = 1;

        for (Map.Entry<LoHang, Integer> entry : phanBo.entrySet()) {
            LoHang loHang = entry.getKey();
            int soLuong = entry.getValue();
            double donGiaChuaVAT = giaBanTheoLoService.tinhDonGiaChuaVAT(loHang, sanPham);
            double donGiaCoVAT = giaBanTheoLoService.tinhDonGiaCoVAT(loHang, sanPham);
            double thanhTienChuaVAT = donGiaChuaVAT * soLuong;
            double thanhTienCoVAT = donGiaCoVAT * soLuong;

            tongTienChuaVAT += thanhTienChuaVAT;
            tongTienCoVAT += thanhTienCoVAT;
            tongSoLuong += soLuong;

            tooltip.append("Lô ").append(index++).append(": ")
                    .append(loHang != null ? loHang.getMaLoHang() : "N/A")
                    .append(" • ")
                    .append(soLuong).append(" × ")
                    .append(currencyFormat.format(donGiaChuaVAT)).append(" / ")
                    .append(currencyFormat.format(donGiaCoVAT)).append(" = ")
                    .append(currencyFormat.format(thanhTienCoVAT)).append(" đ<br>");
        }
        tooltip.append("</html>");

        result.tongTien = tongTienCoVAT;
        result.tongTienChuaVAT = tongTienChuaVAT;
        result.soLo = phanBo.size();
        result.multiLot = phanBo.size() > 1;
        result.tooltip = tooltip.toString();
        if (tongSoLuong > 0) {
            result.donGiaDon = tongTienCoVAT / tongSoLuong;
            result.donGiaDonChuaVAT = tongTienChuaVAT / tongSoLuong;
        }

        return result;
    }

    private static class PricingComputation {

        double tongTien;
        double tongTienChuaVAT;
        double donGiaDon;
        double donGiaDonChuaVAT;
        boolean multiLot;
        int soLo;
        String tooltip;
    }
    
    /**
     * Set % giảm giá cho sản phẩm (hiển thị trong txtDiscount)
     * @param phanTramGiamGia % giảm giá (dạng thập phân: 0.1 = 10%)
     * @param tenKhuyenMai Tên khuyến mãi (hiển thị phía dưới %)
     */
    public void setGiamGia(double phanTramGiamGia, String tenKhuyenMai) {
        // Lưu giá trị vào biến instance
        this.phanTramGiamGia = phanTramGiamGia;
        
        // Reset số tiền giảm giá thực tế khi set % giảm giá mới (trừ khi đã được set riêng)
        // Nếu phanTramGiamGia = 0, reset về null
        if (phanTramGiamGia == 0) {
            this.soTienGiamGiaThucTe = null;
        }
        
        if (phanTramGiamGia > 0) {
            // Hiển thị % giảm giá + tên khuyến mãi
            double phanTram = phanTramGiamGia * 100;
            txtDiscount.setText("<html><div style='text-align: center;'>" +
                "<div style='font-size: 14px; font-weight: bold; color: #ff0000;'>-" + 
                String.format("%.0f", phanTram) + "%</div>" +
                (tenKhuyenMai != null && !tenKhuyenMai.isEmpty() ? 
                    "<div style='font-size: 10px; color: #ff6600; margin-top: 2px;'>" + tenKhuyenMai + "</div>" : "") +
                "</div></html>");
        } else {
            txtDiscount.setText("0%");
        }
    }
    
    /**
     * Set % giảm giá cho sản phẩm (không có tên khuyến mãi)
     * @param phanTramGiamGia % giảm giá (dạng thập phân: 0.1 = 10%)
     */
    public void setGiamGia(double phanTramGiamGia) {
        setGiamGia(phanTramGiamGia, null);
        // Reset số tiền giảm giá thực tế khi set % giảm giá mới
        this.soTienGiamGiaThucTe = null;
    }
    
    /**
     * Lấy % giảm giá đã lưu
     * @return phần trăm giảm giá (dạng thập phân: 0.1 = 10%), hoặc 0 nếu không có
     */
    public double getGiamGia() {
        return this.phanTramGiamGia;
    }
    
    /**
     * Lấy số tiền giảm giá
     * - Nếu có soTienGiamGiaThucTe (giới hạn số lượng tối đa) → trả về số tiền thực tế
     * - Nếu không → tính theo công thức: Tổng tiền × % giảm giá
     * @return số tiền giảm giá
     */
    public double getSoTienGiamGia() {
        // Nếu có số tiền giảm giá thực tế (chỉ cho số lượng tối đa), dùng nó
        if (soTienGiamGiaThucTe != null) {
            return soTienGiamGiaThucTe;
        }
        // Nếu không, tính theo % giảm giá cho toàn bộ số lượng
        return getTongTien() * getGiamGia();
    }
    
    /**
     * Set số tiền giảm giá thực tế (chỉ cho số lượng tối đa)
     * @param soTienGiamGia Số tiền giảm giá thực tế, hoặc null nếu áp dụng cho toàn bộ
     */
    public void setSoTienGiamGiaThucTe(Double soTienGiamGia) {
        this.soTienGiamGiaThucTe = soTienGiamGia;
    }
    
    /**
     * Lấy khuyến mãi được áp dụng cho sản phẩm này
     * @return KhuyenMai hoặc null nếu không có (giảm giá thủ công)
     */
    public vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai getKhuyenMaiDuocApDung() {
        return khuyenMaiDuocApDung;
    }
    
    /**
     * Set khuyến mãi được áp dụng cho sản phẩm này
     * @param khuyenMai KhuyenMai hoặc null (nếu giảm giá thủ công)
     */
    public void setKhuyenMaiDuocApDung(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai khuyenMai) {
        this.khuyenMaiDuocApDung = khuyenMai;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogChonLo = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        btnXacNhan = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        pnChuaLo = new javax.swing.JPanel();
        spinnerSoLuong = new javax.swing.JSpinner();
        txtDonGia = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JLabel();

        dialogChonLo.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogChonLo.setTitle("Chọn lô");
        dialogChonLo.setType(java.awt.Window.Type.POPUP);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(651, 285));

        btnXacNhan.setText("Xác nhận");
        ButtonStyles.apply(btnXacNhan, ButtonStyles.Type.PRIMARY);
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        scrollPane.setBorder(null);

        pnChuaLo.setBackground(new java.awt.Color(255, 255, 255));
        pnChuaLo.setLayout(new javax.swing.BoxLayout(pnChuaLo, javax.swing.BoxLayout.Y_AXIS));
        scrollPane.setViewportView(pnChuaLo);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(501, 501, 501)
                        .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout dialogChonLoLayout = new javax.swing.GroupLayout(dialogChonLo.getContentPane());
        dialogChonLo.getContentPane().setLayout(dialogChonLoLayout);
        dialogChonLoLayout.setHorizontalGroup(
            dialogChonLoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogChonLoLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        dialogChonLoLayout.setVerticalGroup(
            dialogChonLoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogChonLoLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 232, 232)));
        setMaximumSize(new java.awt.Dimension(32767, 100));
        setMinimumSize(new java.awt.Dimension(800, 100));
        setPreferredSize(new java.awt.Dimension(1150, 100));
        setRequestFocusEnabled(false);
        
        // Sử dụng GridBagLayout để các cột thẳng hàng
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh sản phẩm
        lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(80, 80));
        lblHinh.setMinimumSize(new java.awt.Dimension(80, 80));
        lblHinh.setMaximumSize(new java.awt.Dimension(80, 80));
        lblHinh.setBackground(new java.awt.Color(240, 240, 240));
        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        lblHinh.setOpaque(true);
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("");
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        add(lblHinh, gbc);

        // 2. Tên sản phẩm
        lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSP.setText("");
        lblTenSP.setPreferredSize(new java.awt.Dimension(220, 100));
        lblTenSP.setMinimumSize(new java.awt.Dimension(150, 100));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Container chứa các Panel_ChonLo (hiển thị dọc khi có nhiều lô)
        containerLoHang = new javax.swing.JPanel();
        containerLoHang.setBackground(java.awt.Color.WHITE);
        containerLoHang.setLayout(new javax.swing.BoxLayout(containerLoHang, javax.swing.BoxLayout.Y_AXIS));
        
        // Thêm Panel_ChonLo mặc định vào container
        panelChonLo = new Panel_ChonLo();
        containerLoHang.add(panelChonLo);
        
        // Wrap container trong JScrollPane để có thể cuộn khi có nhiều lô
        scrollPaneLoHang = new javax.swing.JScrollPane(containerLoHang);
        scrollPaneLoHang.setPreferredSize(new java.awt.Dimension(170, 80)); // 170px (150 + scrollbar), cao 80px
        scrollPaneLoHang.setMinimumSize(new java.awt.Dimension(170, 80));
        scrollPaneLoHang.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // Bỏ viền
        scrollPaneLoHang.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER); // Ẩn ban đầu
        scrollPaneLoHang.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneLoHang.getVerticalScrollBar().setUnitIncrement(10); // Cuộn mượt hơn
        
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(scrollPaneLoHang, gbc);

        // 4. Số lượng với nút +/- - Cột riêng
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 22));
        
        // Nút giảm
        javax.swing.JButton btnGiam = new javax.swing.JButton("-");
        btnGiam.setFont(new java.awt.Font("Segoe UI", 1, 16));
        btnGiam.setPreferredSize(new java.awt.Dimension(35, 35));
        ButtonStyles.apply(btnGiam, ButtonStyles.Type.DANGER);
        btnGiam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGiam.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue > 1) {
                int newValue = currentValue - 1;
                spinnerSoLuong.setValue(newValue);
                
                // Tìm và chuyển sang lô phù hợp
                timVaChuyenLoPhiHop(newValue);
                
                spinnerSoLuongStateChanged(null);
            }
        });
        
        // TextField EDITABLE để nhập số lượng trực tiếp
        javax.swing.JTextField txtSoLuong = new javax.swing.JTextField("1");
        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 16));
        txtSoLuong.setPreferredSize(new java.awt.Dimension(60, 35));
        txtSoLuong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSoLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));
        
        // Cập nhật spinner để đồng bộ với textfield (ẩn spinner)
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        spinnerSoLuong.setVisible(false); // Ẩn spinner, chỉ dùng để lưu giá trị
        
        // Listener để cập nhật textfield khi spinner thay đổi (từ nút +/-)
        spinnerSoLuong.addChangeListener(evt -> {
            txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
        });
        
        // Listener để cập nhật spinner khi người dùng EDIT trực tiếp vào textfield
        txtSoLuong.addActionListener(evt -> {
            try {
                int value = Integer.parseInt(txtSoLuong.getText().trim());
                if (value >= 1 && value <= 1000) {
                    spinnerSoLuong.setValue(value);
                    
                    // Tìm và chuyển sang lô phù hợp
                    timVaChuyenLoPhiHop(value);
                    
                    spinnerSoLuongStateChanged(null);
                } else {
                    // Nếu ngoài phạm vi, reset về giá trị hiện tại
                    txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                    javax.swing.JOptionPane.showMessageDialog(this, 
                        "Số lượng phải từ 1 đến 1000!", 
                        "Lỗi", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                // Nếu nhập sai định dạng, reset về giá trị hiện tại
                txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập số nguyên hợp lệ!", 
                    "Lỗi", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Listener khi mất focus (blur) - tự động cập nhật
        txtSoLuong.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    int value = Integer.parseInt(txtSoLuong.getText().trim());
                    if (value >= 1 && value <= 1000) {
                        spinnerSoLuong.setValue(value);
                        
                        // Tìm và chuyển sang lô phù hợp
                        timVaChuyenLoPhiHop(value);
                        
                        spinnerSoLuongStateChanged(null);
                    } else {
                        txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                    }
                } catch (NumberFormatException e) {
                    txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                }
            }
        });
        
        // Nút tăng
        javax.swing.JButton btnTang = new javax.swing.JButton("+");
        btnTang.setFont(new java.awt.Font("Segoe UI", 1, 16));
        btnTang.setPreferredSize(new java.awt.Dimension(35, 35));
        ButtonStyles.apply(btnTang, ButtonStyles.Type.SUCCESS);
        btnTang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTang.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue < 1000) {
                int newValue = currentValue + 1;
                spinnerSoLuong.setValue(newValue);
                
                // Tìm và chuyển sang lô phù hợp
                timVaChuyenLoPhiHop(newValue);
                
                spinnerSoLuongStateChanged(null);
            }
        });
        
        pnSpinner.add(btnGiam);
        pnSpinner.add(txtSoLuong);
        pnSpinner.add(btnTang);
        pnSpinner.setPreferredSize(new java.awt.Dimension(150, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(150, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 5. Giảm giá - Cột riêng
        txtDiscount.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDiscount.setForeground(new java.awt.Color(255, 0, 0));
        txtDiscount.setText("");
        txtDiscount.setPreferredSize(new java.awt.Dimension(70, 100));
        txtDiscount.setMinimumSize(new java.awt.Dimension(70, 100));
        txtDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtDiscount.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDiscount, gbc);

        // 6. Đơn giá (chưa VAT)
        txtDonGiaChuaVAT = new javax.swing.JLabel();
        txtDonGiaChuaVAT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDonGiaChuaVAT.setText("");
        txtDonGiaChuaVAT.setPreferredSize(new java.awt.Dimension(100, 100));
        txtDonGiaChuaVAT.setMinimumSize(new java.awt.Dimension(100, 100));
        txtDonGiaChuaVAT.setMaximumSize(new java.awt.Dimension(100, 100));
        txtDonGiaChuaVAT.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGiaChuaVAT.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtDonGiaChuaVAT, gbc);

        // 7. Đơn giá (đã VAT)
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDonGia.setText("");
        txtDonGia.setPreferredSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 8. Tổng tiền
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(0, 120, 215));
        txtTongTien.setText("");
        txtTongTien.setPreferredSize(new java.awt.Dimension(120, 100));
        txtTongTien.setMinimumSize(new java.awt.Dimension(120, 100));
        txtTongTien.setMaximumSize(new java.awt.Dimension(120, 100));
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTien.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 7;
        gbc.weightx = 0.0;
        add(txtTongTien, gbc);

        // 9. Nút Xóa (Chức năng)
        javax.swing.JPanel pnXoa = new javax.swing.JPanel();
        pnXoa.setBackground(java.awt.Color.WHITE);
        pnXoa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        javax.swing.JButton btnXoa = new javax.swing.JButton();
        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 13));
        btnXoa.setPreferredSize(new java.awt.Dimension(60, 35));
        btnXoa.setMinimumSize(new java.awt.Dimension(60, 35));
        btnXoa.setMaximumSize(new java.awt.Dimension(60, 35));
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnXoa.add(btnXoa);
        pnXoa.setPreferredSize(new java.awt.Dimension(70, 100));
        pnXoa.setMinimumSize(new java.awt.Dimension(70, 100));
        gbc.gridx = 8;
        gbc.weightx = 0.0;
        add(pnXoa, gbc);
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // Xóa panel này khỏi container cha
        java.awt.Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
            
            // Fire property change SAU KHI xóa để GD_BanHang cập nhật tổng tiền
            firePropertyChange("sanPhamXoa", false, true);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void spinnerSoLuongStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongStateChanged
        updateTongTien();
    }//GEN-LAST:event_spinnerSoLuongStateChanged

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
//        List<BatchDTO> batchDTOs = getSelectedBatchDTO();
//        for (BatchDTO batchDTO : batchDTOs) {
//            pnListBatch.add(new PnSelectBatch(batchDTO, spinnerSoLuong));
//        }
//        pnListBatch.revalidate();
//        pnListBatch.repaint();
//        dialogChonLo.dispose();
//
//        int value = 0;
//        for (Component component : pnListBatch.getComponents()) {
//            if (component instanceof PnSelectBatch) {
//                PnSelectBatch pnSelectBatch = (PnSelectBatch) component;
//                value += pnSelectBatch.getBatchDTO().getQuantity();
//            }
//        }
//        spinnerSoLuong.setValue(value);
    }//GEN-LAST:event_btnXacNhanActionPerformed

//    private List<BatchDTO> getSelectedBatchDTO() {
//        List<BatchDTO> batchDTOs = new ArrayList<>();
//
//        for (Component component : pnChuaLo.getComponents()) {
//            if (component instanceof JPanel) {
//                JPanel panelContainer = (JPanel) component;
//
//                PnChonLo pnChonLo = null;
//                JSpinner spinner = null;
//
//                for (Component child : panelContainer.getComponents()) {
//                    if (child instanceof PnChonLo) {
//                        pnChonLo = (PnChonLo) child;
//                    } else if (child instanceof JSpinner) {
//                        spinner = (JSpinner) child;
//                    }
//                }
//
//                if (pnChonLo.getBtnTenLo().isSelected()) {
//                    Batch batch = (Batch) pnChonLo.getBatch();
//                    BatchDTO batchDTO = new BatchDTO();
//                    batchDTO.setName(batch.getName());
//                    batchDTO.setExpirationDate(batch.getExpirationDate());
//                    batchDTO.setStock(batch.getStock());
//                    batchDTO.setQuantity((int) spinner.getValue());
//                    batchDTOs.add(batchDTO);
//                }
//            }
//        }
//        return batchDTOs;  // Trả về null nếu không có gì được chọn
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JDialog dialogChonLo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnChuaLo;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JSpinner spinnerSoLuong;
    private javax.swing.JLabel txtDiscount;
    private javax.swing.JLabel txtDonGiaChuaVAT;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}

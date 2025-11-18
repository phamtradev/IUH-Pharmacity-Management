/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyxuathuy;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.HangHongBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietHangHongBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.HangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietHangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyXuatHuy extends javax.swing.JPanel {

    private LoHangBUS loHangBUS;
    private ChiTietDonTraHangBUS chiTietDonTraHangBUS;
    private HangHongBUS hangHongBUS;
    private ChiTietHangHongBUS chiTietHangHongBUS;

    /**
     * Creates new form TabHoaDon
     */
    public GD_QuanLyXuatHuy() {
        this.loHangBUS = new LoHangBUS();
        this.chiTietDonTraHangBUS = new ChiTietDonTraHangBUS();
        this.hangHongBUS = new HangHongBUS();
        this.chiTietHangHongBUS = new ChiTietHangHongBUS();
        initComponents();
        applyButtonStyles();
        fillContent();

        loadUserData();
    }

    private void applyButtonStyles() {
        ButtonStyles.apply(btnTaoPhieu, ButtonStyles.Type.PRIMARY);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnContent = new javax.swing.JPanel();
        pnLeft = new javax.swing.JPanel();
        btnTaoPhieu = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtEmpName = new javax.swing.JTextField();
        headerPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnMid.setMinimumSize(new java.awt.Dimension(200, 200));
        pnMid.setOpaque(false);

        pnContent.setBackground(new java.awt.Color(255, 255, 255));
        pnContent.setLayout(new javax.swing.BoxLayout(pnContent, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(pnContent);

        javax.swing.GroupLayout pnMidLayout = new javax.swing.GroupLayout(pnMid);
        pnMid.setLayout(pnMidLayout);
        pnMidLayout.setHorizontalGroup(
                pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE));
        pnMidLayout.setVerticalGroup(
                pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE));

        add(pnMid, java.awt.BorderLayout.CENTER);

        pnLeft.setBackground(new java.awt.Color(255, 255, 255));
        pnLeft.setPreferredSize(new java.awt.Dimension(485, 650));

        btnTaoPhieu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTaoPhieu.setText("Tạo phiếu");
        btnTaoPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoPhieuActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setText("Tổng tiền:");

        txtTongTien.setEditable(false);
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongTien.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66,
                                        Short.MAX_VALUE)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 162,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 57,
                                                Short.MAX_VALUE)
                                        .addComponent(txtTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, 45,
                                                Short.MAX_VALUE))
                                .addGap(0, 0, 0)));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Tên nhân viên lập:");
        jLabel5.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel5AncestorAdded(evt);
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtEmpName.setEditable(false);
        txtEmpName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmpName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtEmpName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, 162,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 57,
                                                Short.MAX_VALUE)
                                        .addComponent(txtEmpName, javax.swing.GroupLayout.DEFAULT_SIZE, 45,
                                                Short.MAX_VALUE))
                                .addGap(0, 0, 0)));

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
                pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnLeftLayout.createSequentialGroup()
                                .addContainerGap(49, Short.MAX_VALUE)
                                .addGroup(pnLeftLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 165,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(45, Short.MAX_VALUE)));
        pnLeftLayout.setVerticalGroup(
                pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnLeftLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 517,
                                        Short.MAX_VALUE)
                                .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)));

        add(pnLeft, java.awt.BorderLayout.EAST);

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new java.awt.BorderLayout());
        add(headerPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void fillContent() {
        // Tạo tiêu đề "DANH SÁCH THÔNG TIN XUẤT HỦY"
        createTitleHeader();
        // Tạo header cho danh sách sản phẩm
        createProductListHeader();
        // Tự động load sản phẩm hết hạn khi mở màn hình
        loadSanPhamHetHan();
    }

    private void createTitleHeader() {
        // Tạo panel với FlowLayout giống DANH SÁCH THÔNG TIN THUỐC
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        titlePanel.setPreferredSize(new java.awt.Dimension(1200, 50));
        titlePanel.setMinimumSize(new java.awt.Dimension(800, 50));
        titlePanel.setMaximumSize(new java.awt.Dimension(32767, 50));

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN XUẤT HỦY");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // Bold, size 16
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng

        titlePanel.add(lblTitle);

        pnContent.add(titlePanel);
    }

    private void createProductListHeader() {
        javax.swing.JPanel headerProductPanel = new javax.swing.JPanel();
        headerProductPanel.setBackground(new java.awt.Color(240, 248, 255));
        headerProductPanel
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerProductPanel.setPreferredSize(new java.awt.Dimension(1000, 50));
        headerProductPanel.setMinimumSize(new java.awt.Dimension(700, 50));
        headerProductPanel.setMaximumSize(new java.awt.Dimension(32767, 50));

        // Sử dụng GridBagLayout giống Panel_ChiTietSanPhamXuatHuy
        headerProductPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 5, 10, 5);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Hình", 70, javax.swing.SwingConstants.CENTER), gbc);

        // 2. Tên sản phẩm + Thông tin lô
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        headerProductPanel.add(createHeaderLabel("Tên sản phẩm / Lô hàng", 200, javax.swing.SwingConstants.LEFT), gbc);

        // 3. Lý do
        gbc.gridx = 2;
        gbc.weightx = 0.08;
        headerProductPanel.add(createHeaderLabel("Lý do", 80, javax.swing.SwingConstants.LEFT), gbc);

        // 4. Đơn vị
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Đơn vị", 50, javax.swing.SwingConstants.CENTER), gbc);

        // 5. Số lượng
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("SL", 60, javax.swing.SwingConstants.CENTER), gbc);

        // 6. Đơn giá
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Đơn giá", 75, javax.swing.SwingConstants.RIGHT), gbc);

        // 7. Tổng tiền
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Tổng tiền", 85, javax.swing.SwingConstants.RIGHT), gbc);

        // Thêm header vào pnContent
        pnContent.add(headerProductPanel);
    }

    private javax.swing.JLabel createHeaderLabel(String text, int width, int alignment) {
        javax.swing.JLabel label = new javax.swing.JLabel(text);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        label.setForeground(new java.awt.Color(52, 58, 64));
        label.setPreferredSize(new java.awt.Dimension(width, 50));
        label.setMinimumSize(new java.awt.Dimension(width, 50));
        label.setHorizontalAlignment(alignment);
        label.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        return label;
    }

    private void loadUserData() {
        NhanVien currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            txtEmpName.setText(currentUser.getTenNhanVien());
        } else {
            txtEmpName.setText("Không xác định");
            System.err.println("Er: khong co gnuoi dung trong Session!");
        }
    }

    /**
     * Đếm tổng số đơn cần xuất hủy (cho Dashboard)
     *
     * @return Tổng số lô hàng hết hạn + số sản phẩm từ đơn trả hàng
     */
    public int demSoDonCanXuatHuy() {
        try {
            List<LoHang> danhSachLoHangHetHan = loHangBUS.layTatCaLoHangHetHan();
            List<ChiTietDonTraHang> danhSachHangTra = chiTietDonTraHangBUS.layTatCaChiTietCanHuy();

            return danhSachLoHangHetHan.size() + danhSachHangTra.size();
        } catch (Exception e) {
            System.err.println("Lỗi khi đếm số đơn cần xuất hủy: " + e.getMessage());
            return 0;
        }
    }

    private void btnTaoPhieuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaoPhieuActionPerformed
        try {
            // 1. Kiểm tra có sản phẩm nào trong danh sách không
            List<Panel_ChiTietSanPhamXuatHuy> danhSachPanel = new ArrayList<>();
            for (Component comp : pnContent.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamXuatHuy) {
                    danhSachPanel.add((Panel_ChiTietSanPhamXuatHuy) comp);
                }
            }

            if (danhSachPanel.isEmpty()) {
                Notifications.getInstance().show(
                        Notifications.Type.WARNING,
                        "Không có sản phẩm nào để xuất hủy!");
                return;
            }

            // 2. Xác nhận với người dùng
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn tạo phiếu xuất hủy?\n" +
                            "Tổng giá trị: " + txtTongTien.getText() + "\n" +
                            "Số lượng sản phẩm: " + danhSachPanel.size(),
                    "Xác nhận tạo phiếu",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // 3. Lấy thông tin nhân viên hiện tại
            NhanVien currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        "Không thể xác định nhân viên đang đăng nhập!");
                return;
            }

            // 4. Tạo đối tượng HangHong (phiếu xuất hủy)
            HangHong hangHong = new HangHong();
            hangHong.setNgayNhap(LocalDate.now());

            // Tính tổng tiền
            double tongTien = 0;
            for (Panel_ChiTietSanPhamXuatHuy panel : danhSachPanel) {
                tongTien += panel.getTongTienHuy();
            }
            hangHong.setThanhTien(tongTien);
            hangHong.setNhanVien(currentUser);

            // 5. Lưu HangHong vào database (mã sẽ được tự động generate)
            boolean successHangHong = hangHongBUS.taoHangHong(hangHong);

            if (!successHangHong) {
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        "Lỗi khi lưu phiếu xuất hủy vào database!");
                return;
            }

            // 6. Tạo danh sách ChiTietHangHong và track các đơn trả cần cập nhật
            // Sử dụng Map để GỘP các panel cùng lô hàng
            java.util.Map<String, ChiTietHangHong> mapChiTiet = new java.util.HashMap<>();
            java.util.Set<String> danhSachDonTraDaXuLy = new java.util.HashSet<>();
            // Track chi tiết đơn trả đã xuất hủy (maDonTra + maSanPham)
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> danhSachChiTietDonTraDaXuatHuy = new java.util.ArrayList<>();

            System.out.println("DEBUG: Bắt đầu xử lý " + danhSachPanel.size() + " panel...");

            for (Panel_ChiTietSanPhamXuatHuy panel : danhSachPanel) {
                LoHang loHang = panel.getLoHang();
                if (loHang == null) {
                    System.err.println("Warning: Panel không có thông tin lô hàng");
                    continue;
                }

                String maLoHang = loHang.getMaLoHang();
                int soLuong = panel.getSoLuongHuy();
                double donGia = panel.getDonGia();
                double thanhTienPanel = panel.getTongTienHuy();
                String lyDoXuatHuy = panel.getLyDoXuatHuy();
                if (lyDoXuatHuy == null || lyDoXuatHuy.trim().isEmpty()) {
                    lyDoXuatHuy = "Chưa rõ lý do";
                }

                // Tạo key duy nhất: maLoHang + lyDoXuatHuy
                // → Chỉ gộp khi CÙNG lô hàng VÀ CÙNG lý do
                String keyUnique = maLoHang + "___" + lyDoXuatHuy;

                // Nếu lô hàng này (với lý do này) đã tồn tại trong map → CỘNG DỒN số lượng
                if (mapChiTiet.containsKey(keyUnique)) {
                    ChiTietHangHong chiTietCu = mapChiTiet.get(keyUnique);
                    int soLuongCu = chiTietCu.getSoLuong();
                    int soLuongMoi = soLuongCu + soLuong;
                    chiTietCu.setSoLuong(soLuongMoi);
                    chiTietCu.setThanhTien(chiTietCu.getThanhTien() + thanhTienPanel);
                    System.out.println("DEBUG: Gộp lô " + maLoHang + " (lý do: " + lyDoXuatHuy + ") - Số lượng "
                            + soLuongCu + " + " + soLuong + " = " + soLuongMoi);
                } else {
                    // Lô hàng mới (hoặc lý do mới) → Tạo chi tiết mới
                    ChiTietHangHong chiTiet = new ChiTietHangHong();
                    chiTiet.setSoLuong(soLuong);
                    chiTiet.setDonGia(donGia);
                    chiTiet.setThanhTien(thanhTienPanel);
                    chiTiet.setHangHong(hangHong);
                    chiTiet.setLoHang(loHang);
                    chiTiet.setLyDoXuatHuy(lyDoXuatHuy);

                    mapChiTiet.put(keyUnique, chiTiet);
                    System.out.println(
                            "DEBUG: Thêm lô mới " + maLoHang + " (lý do: " + lyDoXuatHuy + ") - Số lượng: " + soLuong);
                }

                // Track đơn trả cần cập nhật
                if (panel.getChiTietDonTra() != null) {
                    vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTietDonTra = panel
                            .getChiTietDonTra();
                    String maDonTra = chiTietDonTra.getDonTraHang().getMaDonTraHang();
                    danhSachDonTraDaXuLy.add(maDonTra);
                    // Lưu chi tiết đơn trả để cập nhật trạng thái sau
                    danhSachChiTietDonTraDaXuatHuy.add(chiTietDonTra);
                }
            }

            // Chuyển map thành list và lưu vào database
            List<ChiTietHangHong> chiTietList = new ArrayList<>(mapChiTiet.values());
            System.out.println("DEBUG: Sau khi gộp, còn " + chiTietList.size() + " chi tiết duy nhất");

            for (ChiTietHangHong chiTiet : chiTietList) {
                System.out.println(
                        "DEBUG: Insert lô " + chiTiet.getLoHang().getMaLoHang() + " - SL: " + chiTiet.getSoLuong());
                boolean successChiTiet = chiTietHangHongBUS.taoChiTietHangHong(chiTiet);
                if (!successChiTiet) {
                    System.err.println("Lỗi khi lưu chi tiết hàng hỏng: " + chiTiet.getLoHang().getMaLoHang());
                } else {
                    System.out.println("DEBUG: Insert thành công!");
                }
            }

            // 6.3. Giảm tồn kho của các lô hàng đã xuất hủy
            for (ChiTietHangHong chiTiet : chiTietList) {
                LoHang loHang = chiTiet.getLoHang();
                String maLoHang = loHang.getMaLoHang();
                int soLuongXuatHuy = chiTiet.getSoLuong();
                String lyDoXuatHuy = chiTiet.getLyDoXuatHuy();

                boolean updateTonKhoSuccess;

                // Phân biệt 2 trường hợp:
                // 1. Lô hết hạn (lý do chứa "Hết hạn") → Giảm toàn bộ tồn kho về 0
                // 2. Hàng hư từ đơn trả → Giảm theo số lượng xuất hủy
                if (lyDoXuatHuy != null && lyDoXuatHuy.contains("Hết hạn")) {
                    // Lô hết hạn → Giảm toàn bộ tồn kho về 0
                    int tonKhoHienTai = loHang.getTonKho();
                    updateTonKhoSuccess = loHangBUS.updateTonKho(maLoHang, -tonKhoHienTai);

                    if (updateTonKhoSuccess) {
                        System.out.println(
                                "✓ Đã giảm tồn kho lô HẾT HẠN '" + maLoHang + "' từ " + tonKhoHienTai + " → 0");
                    } else {
                        System.err.println("✗ Lỗi khi giảm tồn kho lô hết hạn '" + maLoHang + "'");
                    }
                } else {
                    // Hàng hư từ đơn trả → Giảm theo số lượng xuất hủy
                    updateTonKhoSuccess = loHangBUS.updateTonKho(maLoHang, -soLuongXuatHuy);

                    if (updateTonKhoSuccess) {
                        System.out.println("✓ Đã giảm tồn kho lô HÀNG HƯ '" + maLoHang + "' xuống " + soLuongXuatHuy
                                + " sản phẩm");
                    } else {
                        System.err.println("✗ Lỗi khi giảm tồn kho lô hàng hư '" + maLoHang + "'");
                    }
                }
            }

            // 6.4. Set trạng thái lô hàng = false (ngừng hoạt động) sau khi xuất hủy
            for (ChiTietHangHong chiTiet : chiTietList) {
                try {
                    LoHang loHang = chiTiet.getLoHang();
                    String maLoHang = loHang.getMaLoHang();

                    // Set trạng thái = false (ngừng hoạt động)
                    loHang.setTrangThai(false);

                    // Cập nhật vào database
                    boolean updateTrangThaiSuccess = loHangBUS.capNhatLoHang(loHang);
                    if (updateTrangThaiSuccess) {
                        System.out.println("✓ Đã cập nhật trạng thái NGỪNG HOẠT ĐỘNG cho lô hàng: " + maLoHang);
                    } else {
                        System.err.println("✗ Lỗi khi cập nhật trạng thái lô hàng: " + maLoHang);
                    }
                } catch (Exception e) {
                    System.err.println("✗ Lỗi khi cập nhật trạng thái lô hàng " + chiTiet.getLoHang().getMaLoHang()
                            + ": " + e.getMessage());
                }
            }

            // 6.5. Cập nhật trạng thái CHI TIẾT đơn trả hàng đã xuất hủy
            if (!danhSachChiTietDonTraDaXuatHuy.isEmpty()) {
                vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS chiTietDonTraBUS = new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS();

                for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTietDonTra : danhSachChiTietDonTraDaXuatHuy) {
                    try {
                        // Cập nhật trạng thái chi tiết thành "Đã xuất hủy"
                        chiTietDonTra.setTrangThaiXuLy("Đã xuất hủy");
                        boolean updated = chiTietDonTraBUS.capNhatTrangThaiChiTiet(chiTietDonTra);
                        if (updated) {
                            System.out.println("✓ Đã cập nhật trạng thái 'Đã xuất hủy' cho chi tiết: " +
                                    chiTietDonTra.getSanPham().getMaSanPham());
                        } else {
                            System.err.println("✗ Lỗi khi cập nhật trạng thái chi tiết: " +
                                    chiTietDonTra.getSanPham().getMaSanPham());
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi cập nhật trạng thái chi tiết: " + e.getMessage());
                    }
                }
            }

            // 6.6. Cập nhật trạng thái ĐƠN trả hàng (nếu tất cả chi tiết đã xử lý)
            if (!danhSachDonTraDaXuLy.isEmpty()) {
                vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS donTraBUS = new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS();
                vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS chiTietDonTraBUS = new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS();

                for (String maDonTra : danhSachDonTraDaXuLy) {
                    try {
                        // Kiểm tra xem còn chi tiết nào chưa xử lý không
                        List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> chiTietConLai = chiTietDonTraBUS
                                .layChiTietTheoMaDonTra(maDonTra);

                        // Đếm số chi tiết chưa xử lý (chưa xử lý hoặc đã duyệt xuất hủy)
                        long soChiTietChuaXuLy = chiTietConLai.stream()
                                .filter(ct -> "Chưa xử lý".equals(ct.getTrangThaiXuLy())
                                        || "Đã duyệt xuất hủy".equals(ct.getTrangThaiXuLy()))
                                .count();

                        // Nếu không còn chi tiết nào chưa xử lý → cập nhật trạng thái đơn thành "Đã xử
                        // lý"
                        if (soChiTietChuaXuLy == 0) {
                            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTra = donTraBUS
                                    .timDonTraTheoMa(maDonTra);
                            if (donTra != null) {
                                donTra.setTrangThaiXuLy("Đã xử lý");
                                boolean updated = donTraBUS.capNhatDonTraHang(donTra);
                                if (updated) {
                                    System.out.println("✓ Đã cập nhật trạng thái 'Đã xử lý' cho đơn trả: " + maDonTra);
                                } else {
                                    System.err.println("✗ Lỗi khi cập nhật trạng thái đơn trả: " + maDonTra);
                                }
                            }
                        } else {
                            System.out.println(
                                    "⚠ Đơn trả " + maDonTra + " còn " + soChiTietChuaXuLy + " chi tiết chưa xử lý");
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi cập nhật trạng thái đơn trả " + maDonTra + ": " + e.getMessage());
                    }
                }
            }

            // 7. Hiển thị preview hóa đơn (không xuất PDF nữa)
            hienThiPhieuXuatHuy(hangHong, chiTietList);

            // 8. Thông báo thành công
            Notifications.getInstance().show(
                    Notifications.Type.SUCCESS,
                    "Tạo phiếu xuất hủy thành công! Mã: " + hangHong.getMaHangHong());

            // 9. Xóa trắng tất cả các sản phẩm đã tạo phiếu
            xoaTrangDanhSachSanPham();

        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(
                    Notifications.Type.ERROR,
                    "Lỗi khi tạo phiếu xuất hủy: " + e.getMessage());
        }
    }// GEN-LAST:event_btnTaoPhieuActionPerformed

    // Load tự động các sản phẩm hết hạn khi mở màn hình (còn <= 6 tháng HSD)
    private void loadSanPhamHetHan() {
        // Xóa các sản phẩm cũ
        pnContent.removeAll();
        // Thêm lại 2 header
        createTitleHeader();
        createProductListHeader();

        java.awt.Component[] components = pnContent.getComponents();
        for (java.awt.Component comp : components) {
            if (comp instanceof Panel_ChiTietSanPhamXuatHuy || comp instanceof javax.swing.JLabel) {
                pnContent.remove(comp);
            }
        }

        try {
            System.out.println("DEBUG GUI: Đang tải sản phẩm xuất hủy");

            // 1. Lấy danh sách lô hàng hết hạn từ LoHangBUS
            List<LoHang> danhSachLoHangHetHan = loHangBUS.layTatCaLoHangHetHan();
            System.out.println("DEBUG GUI: Tìm thấy " + danhSachLoHangHetHan.size() + " lô hàng hết hạn");

            // 2. Lấy danh sách hàng bị trả từ ChiTietDonTraHangBUS
            List<ChiTietDonTraHang> danhSachHangTra = chiTietDonTraHangBUS.layTatCaChiTietCanHuy();
            System.out.println("DEBUG GUI: Tìm thấy " + danhSachHangTra.size() + " sản phẩm từ đơn trả hàng");

            if (danhSachLoHangHetHan.isEmpty() && danhSachHangTra.isEmpty()) {
                JLabel lblEmpty = new JLabel("Hiện không có sản phẩm nào cần xuất hủy.");
                lblEmpty.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 16));
                lblEmpty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                pnContent.add(lblEmpty);
            } else {
                // Load các lô hàng hết hạn (số lượng KHÔNG thể chỉnh)
                for (LoHang loHang : danhSachLoHangHetHan) {
                    SanPham sanPham = loHang.getSanPham();

                    Panel_ChiTietSanPhamXuatHuy panel = new Panel_ChiTietSanPhamXuatHuy();

                    panel.setTenSanPham(sanPham.getTenSanPham());
                    panel.setHinhAnh(sanPham.getHinhAnh()); // Load hình ảnh
                    // Hiển thị thông tin lô hàng
                    panel.setLoHang(
                            loHang.getTenLoHang(),
                            loHang.getHanSuDung().toString(),
                            loHang.getTonKho());

                    // Lưu đối tượng LoHang vào panel
                    panel.setLoHangObject(loHang);

                    panel.setDonVi(sanPham.getDonViTinh().getTenDonVi());
                    panel.setDonGia(sanPham.getGiaNhap());
                    panel.setSoLuongHuy(loHang.getTonKho());
                    panel.setLyDoXuatHuy("Hết hạn sử dụng (còn <= 6 tháng)");
                    panel.setSoLuongEditable(false); // KHÔNG thể chỉnh số lượng

                    pnContent.add(panel);
                }

                // Load các sản phẩm từ đơn trả hàng (số lượng KHÔNG thể chỉnh)
                for (ChiTietDonTraHang chiTiet : danhSachHangTra) {
                    SanPham sanPham = chiTiet.getSanPham();

                    // TÌM LÔ HÀNG THẬT của sản phẩm này (ưu tiên lô cũ nhất, có tồn kho)
                    String maSanPham = sanPham.getMaSanPham();
                    List<LoHang> danhSachLo = loHangBUS.layDanhSachLoHangTheoSanPham(maSanPham);

                    LoHang loHangChon = null;
                    for (LoHang lo : danhSachLo) {
                        if (lo.getTonKho() > 0) {
                            loHangChon = lo;
                            break; // Lấy lô đầu tiên có tồn kho (đã sort theo HSD)
                        }
                    }

                    // Nếu không tìm thấy lô nào có tồn kho → BỎ QUA (không hiển thị)
                    if (loHangChon == null) {
                        System.err.println("WARNING: Không tìm thấy lô hàng có tồn kho cho sản phẩm: "
                                + sanPham.getTenSanPham() + " (Đơn trả: " + chiTiet.getDonTraHang().getMaDonTraHang()
                                + ")");
                        continue;
                    }

                    Panel_ChiTietSanPhamXuatHuy panel = new Panel_ChiTietSanPhamXuatHuy();

                    panel.setTenSanPham(sanPham.getTenSanPham());
                    String hinhAnh = sanPham.getHinhAnh();
                    System.out.println(
                            "DEBUG: Đang load hình ảnh cho sản phẩm " + sanPham.getTenSanPham() + ": " + hinhAnh);
                    panel.setHinhAnh(hinhAnh); // Load hình ảnh
                    // Hiển thị thông tin lô hàng THẬT + thông tin đơn trả
                    panel.setLoHang(
                            loHangChon.getTenLoHang() + " (Đơn trả: " + chiTiet.getDonTraHang().getMaDonTraHang() + ")",
                            loHangChon.getHanSuDung() != null ? loHangChon.getHanSuDung().toString() : "N/A",
                            chiTiet.getSoLuong());

                    // Lưu lô hàng THẬT vào panel
                    panel.setLoHangObject(loHangChon);

                    // LƯU THÔNG TIN CHI TIẾT ĐƠN TRẢ VÀO PANEL
                    panel.setChiTietDonTra(chiTiet);

                    panel.setDonVi(sanPham.getDonViTinh().getTenDonVi());
                    panel.setDonGia(chiTiet.getDonGia());
                    panel.setSoLuongHuy(chiTiet.getSoLuong());
                    panel.setLyDoXuatHuy(chiTiet.getLyDoTra()); // Lấy lý do từ đơn trả hàng
                    panel.setSoLuongEditable(false); // KHÔNG thể chỉnh số lượng

                    pnContent.add(panel);
                }
            }

            // Vẽ lại giao diện
            pnContent.revalidate();
            pnContent.repaint();
            updateTongTien();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách sản phẩm hết hạn: " + e.getMessage());
        }
    }

    /**
     * Cập nhật tổng tiền xuất hủy
     */
    public void updateTongTien() {
        double tongTien = 0;
        for (Component comp : pnContent.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamXuatHuy) {
                Panel_ChiTietSanPhamXuatHuy panel = (Panel_ChiTietSanPhamXuatHuy) comp;
                tongTien += panel.getTongTienHuy();
            }
        }
        txtTongTien.setText(String.format("%,.0f ₫", tongTien));
    }

    /**
     * Lấy panel chứa danh sách sản phẩm xuất hủy
     */
    public javax.swing.JPanel getPnContent() {
        return pnContent;
    }

    private void jLabel5AncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_jLabel5AncestorAdded
        // TODO add your handling code here:
    }// GEN-LAST:event_jLabel5AncestorAdded

    /**
     * Hiển thị preview phiếu xuất hủy
     */
    private void hienThiPhieuXuatHuy(HangHong hangHong, java.util.List<ChiTietHangHong> danhSachChiTiet) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Phiếu xuất hủy - " + hangHong.getMaHangHong());
        dialog.setModal(true);
        dialog.setSize(1100, 750);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(true);

        // Scroll pane chính để cuộn được khi nội dung dài
        javax.swing.JScrollPane mainScrollPane = new javax.swing.JScrollPane();
        mainScrollPane.setBorder(null);
        mainScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout(10, 10));
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Format
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,###");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // === HEADER ===
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));
        headerPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("PHIẾU XUẤT HỦY");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setForeground(new java.awt.Color(220, 53, 69));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        headerPanel.add(javax.swing.Box.createVerticalStrut(20));

        // Thông tin phiếu
        headerPanel.add(createInfoLabel("Mã phiếu: ", hangHong.getMaHangHong(), true));
        headerPanel.add(
                createInfoLabel("Ngày lập: ", dateFormat.format(java.sql.Date.valueOf(hangHong.getNgayNhap())), false));
        headerPanel.add(createInfoLabel("Nhân viên: ", hangHong.getNhanVien().getTenNhanVien(), false));

        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        // === CHI TIẾT SẢN PHẨM ===
        String[] columnNames = { "STT", "Tên sản phẩm", "Lô", "HSD", "ĐV", "SL", "Đơn giá", "Thành tiền", "Lý do" };
        javax.swing.table.DefaultTableModel modelDetail = new javax.swing.table.DefaultTableModel(columnNames, 0);

        int stt = 1;
        for (ChiTietHangHong chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            if (loHang == null || loHang.getSanPham() == null)
                continue;

            SanPham sp = loHang.getSanPham();
            String hsd = loHang.getHanSuDung() != null ? loHang.getHanSuDung().toString() : "N/A";

            // Lấy lý do từ ChiTietHangHong
            String lyDo = chiTiet.getLyDoXuatHuy();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                // Fallback nếu không có lý do
                lyDo = getLyDoFromLoHang(loHang);
            }

            modelDetail.addRow(new Object[] {
                    stt++,
                    sp.getTenSanPham(),
                    loHang.getTenLoHang() != null ? loHang.getTenLoHang() : loHang.getMaLoHang(),
                    hsd,
                    sp.getDonViTinh() != null ? sp.getDonViTinh().getTenDonVi() : "",
                    chiTiet.getSoLuong(),
                    currencyFormat.format(chiTiet.getDonGia()) + " ₫",
                    currencyFormat.format(chiTiet.getThanhTien()) + " ₫",
                    lyDo
            });
        }

        javax.swing.JTable tableDetail = new javax.swing.JTable(modelDetail);
        tableDetail.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        tableDetail.setRowHeight(30);
        tableDetail.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(tableDetail);
        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        // === FOOTER ===
        javax.swing.JPanel footerPanel = new javax.swing.JPanel();
        footerPanel.setLayout(new java.awt.BorderLayout());
        footerPanel.setBackground(java.awt.Color.WHITE);
        footerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));

        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTongTien = new javax.swing.JLabel(
                "TỔNG GIÁ TRỊ XUẤT HỦY:  " + currencyFormat.format(hangHong.getThanhTien()) + " ₫");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTongTien.setForeground(new java.awt.Color(220, 53, 69));
        tongTienPanel.add(lblTongTien);

        footerPanel.add(tongTienPanel, java.awt.BorderLayout.NORTH);

        // === NÚT ĐÓNG VÀ IN HÓA ĐƠN ===
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(java.awt.Color.WHITE);
        buttonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Nút In Hóa Đơn
        javax.swing.JButton btnInHoaDon = new javax.swing.JButton("📄 In Hóa Đơn");
        btnInHoaDon.setPreferredSize(new java.awt.Dimension(180, 45));
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        ButtonStyles.apply(btnInHoaDon, ButtonStyles.Type.SUCCESS);
        btnInHoaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInHoaDon.addActionListener(e -> {
            // Đóng dialog preview
            dialog.dispose();
            // Hiển thị hóa đơn xuất hủy (giống hóa đơn bán hàng)
            hienThiHoaDonXuatHuy(hangHong, danhSachChiTiet);
        });

        javax.swing.JButton btnDong = new javax.swing.JButton("Đóng");
        btnDong.setPreferredSize(new java.awt.Dimension(150, 45));
        btnDong.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnDong.setBackground(new java.awt.Color(108, 117, 125));
        btnDong.setForeground(java.awt.Color.WHITE);
        btnDong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDong.addActionListener(e -> {
            dialog.dispose();
        });

        buttonPanel.add(btnInHoaDon);
        buttonPanel.add(btnDong);
        footerPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);

        // Thêm mainPanel vào scroll pane
        mainScrollPane.setViewportView(mainPanel);
        dialog.add(mainScrollPane);
        dialog.setVisible(true);
    }

    private javax.swing.JPanel createInfoLabel(String title, String value, boolean bold) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));
        panel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel(title);
        lblTitle.setFont(new java.awt.Font("Segoe UI", bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 14));

        javax.swing.JLabel lblValue = new javax.swing.JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        panel.add(lblTitle);
        panel.add(lblValue);

        return panel;
    }

    private String getLyDoFromLoHang(LoHang loHang) {
        if (loHang.getMaLoHang().startsWith("HANGTRA_")) {
            return "Hàng trả lại";
        }

        if (loHang.getHanSuDung() != null) {
            LocalDate hsd = loHang.getHanSuDung();
            LocalDate now = LocalDate.now();
            long monthsUntilExpiry = java.time.temporal.ChronoUnit.MONTHS.between(now, hsd);

            if (monthsUntilExpiry <= 0) {
                return "Hết hạn sử dụng";
            } else if (monthsUntilExpiry <= 6) {
                return "Gần hết hạn (" + monthsUntilExpiry + " tháng)";
            }
        }
        return "Khác";
    }

    /**
     * Hiển thị hóa đơn xuất hủy (UI giống hóa đơn bán hàng, có barcode)
     */
    private void hienThiHoaDonXuatHuy(HangHong hangHong, java.util.List<ChiTietHangHong> danhSachChiTiet) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa Đơn Xuất Hủy");
        dialog.setModal(true);
        dialog.setSize(650, 900);
        dialog.setLocationRelativeTo(null);

        // Scroll pane cho toàn bộ hóa đơn
        javax.swing.JScrollPane mainScrollPane = new javax.swing.JScrollPane();
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Format cho số tiền và ngày tháng
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,###");
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // ========== HEADER - THÔNG TIN CỬA HÀNG ==========
        javax.swing.JLabel lblStoreName = new javax.swing.JLabel("IUH PHARMACITY");
        lblStoreName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblStoreName.setForeground(new java.awt.Color(0, 120, 215));
        lblStoreName.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStoreName);
        mainPanel.add(javax.swing.Box.createVerticalStrut(3));

        javax.swing.JLabel lblAddress = new javax.swing.JLabel("12 Nguyen Van Bao, Ward 4, Go Vap District, HCMC");
        lblAddress.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblAddress.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblAddress);
        mainPanel.add(javax.swing.Box.createVerticalStrut(2));

        javax.swing.JLabel lblContact = new javax.swing.JLabel("Hotline: 1800 6928 | Email: cskh@pharmacity.vn");
        lblContact.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblContact.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblContact);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== TIÊU ĐỀ HÓA ĐƠN ==========
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("PHIEU XUAT HUY");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(javax.swing.Box.createVerticalStrut(8));

        // ========== BARCODE MÃ PHIẾU XUẤT HỦY ==========
        try {
            java.awt.image.BufferedImage barcodeImage = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                    .taoBarcode(hangHong.getMaHangHong());
            java.awt.image.BufferedImage barcodeWithText = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                    .addTextBelow(barcodeImage, hangHong.getMaHangHong());

            javax.swing.JLabel lblBarcode = new javax.swing.JLabel(new javax.swing.ImageIcon(barcodeWithText));
            lblBarcode.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            mainPanel.add(lblBarcode);
        } catch (Exception ex) {
            System.err.println("Lỗi tạo barcode: " + ex.getMessage());
        }
        mainPanel.add(javax.swing.Box.createVerticalStrut(2));

        String ngayLap = hangHong.getNgayNhap().format(dateFormatter);
        javax.swing.JLabel lblDate = new javax.swing.JLabel("Ngay lap: " + ngayLap);
        lblDate.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblDate.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblDate);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== THÔNG TIN NHÂN VIÊN ==========
        javax.swing.JPanel infoPanel = new javax.swing.JPanel();
        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.Y_AXIS));
        infoPanel.setBackground(java.awt.Color.WHITE);
        infoPanel.setMaximumSize(new java.awt.Dimension(600, 80));

        NhanVien nhanVien = hangHong.getNhanVien();
        javax.swing.JPanel employeePanel = new javax.swing.JPanel();
        employeePanel.setLayout(new javax.swing.BoxLayout(employeePanel, javax.swing.BoxLayout.Y_AXIS));
        employeePanel.setBackground(java.awt.Color.WHITE);
        employeePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));

        javax.swing.JLabel lblEmployeeTitle = new javax.swing.JLabel("THONG TIN NHAN VIEN");
        lblEmployeeTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        lblEmployeeTitle.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        employeePanel.add(lblEmployeeTitle);
        employeePanel.add(javax.swing.Box.createVerticalStrut(3));

        if (nhanVien != null) {
            javax.swing.JLabel lblEmployeeName = new javax.swing.JLabel("Ho ten: " + nhanVien.getTenNhanVien());
            lblEmployeeName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
            lblEmployeeName.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            employeePanel.add(lblEmployeeName);

            if (nhanVien.getSoDienThoai() != null) {
                javax.swing.JLabel lblEmployeePhone = new javax.swing.JLabel("SDT: " + nhanVien.getSoDienThoai());
                lblEmployeePhone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
                lblEmployeePhone.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                employeePanel.add(lblEmployeePhone);
            }
        }

        infoPanel.add(employeePanel);
        mainPanel.add(infoPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(10));

        // ========== BẢNG SẢN PHẨM ==========
        String[] columnNames = { "STT", "Ten san pham", "Lo", "SL", "Don gia", "Thanh tien", "Ly do" };
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        table.getTableHeader().setBackground(new java.awt.Color(240, 240, 240));
        table.setGridColor(new java.awt.Color(220, 220, 220));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(30); // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên sản phẩm
        table.getColumnModel().getColumn(2).setPreferredWidth(80); // Lô
        table.getColumnModel().getColumn(3).setPreferredWidth(35); // SL
        table.getColumnModel().getColumn(4).setPreferredWidth(80); // Đơn giá
        table.getColumnModel().getColumn(5).setPreferredWidth(85); // Thành tiền
        table.getColumnModel().getColumn(6).setPreferredWidth(120); // Lý do

        // Center align cho các cột số
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Right align cho các cột tiền
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        // Thêm dữ liệu vào bảng
        int stt = 1;
        for (ChiTietHangHong chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            if (loHang == null || loHang.getSanPham() == null)
                continue;

            SanPham sp = loHang.getSanPham();
            String tenLo = loHang.getTenLoHang() != null ? loHang.getTenLoHang() : loHang.getMaLoHang();
            String lyDo = chiTiet.getLyDoXuatHuy();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                lyDo = getLyDoFromLoHang(loHang);
            }

            tableModel.addRow(new Object[] {
                    stt++,
                    sp.getTenSanPham(),
                    tenLo,
                    chiTiet.getSoLuong(),
                    currencyFormat.format(chiTiet.getDonGia()) + " đ",
                    currencyFormat.format(chiTiet.getThanhTien()) + " đ",
                    lyDo
            });
        }

        javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane(table);
        tableScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        tableScrollPane.setPreferredSize(new java.awt.Dimension(580, 250));
        tableScrollPane.setMaximumSize(new java.awt.Dimension(600, 250));
        mainPanel.add(tableScrollPane);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== BẢNG THANH TOÁN ==========
        javax.swing.JPanel paymentPanel = new javax.swing.JPanel();
        paymentPanel.setLayout(new javax.swing.BoxLayout(paymentPanel, javax.swing.BoxLayout.Y_AXIS));
        paymentPanel.setBackground(java.awt.Color.WHITE);
        paymentPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        paymentPanel.setMaximumSize(new java.awt.Dimension(450, 200));

        // Helper method để tạo row thanh toán
        java.util.function.BiConsumer<String, String> addPaymentRow = (label, value) -> {
            javax.swing.JPanel rowPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
            rowPanel.setBackground(java.awt.Color.WHITE);
            rowPanel.setMaximumSize(new java.awt.Dimension(450, 25));

            javax.swing.JLabel lblLeft = new javax.swing.JLabel(label);
            lblLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));

            javax.swing.JLabel lblRight = new javax.swing.JLabel(value);
            lblRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
            lblRight.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

            rowPanel.add(lblLeft, java.awt.BorderLayout.WEST);
            rowPanel.add(lblRight, java.awt.BorderLayout.EAST);

            paymentPanel.add(rowPanel);
            paymentPanel.add(javax.swing.Box.createVerticalStrut(3));
        };

        // Tổng giá trị xuất hủy
        addPaymentRow.accept("Tong gia tri xuat huy:", currencyFormat.format(hangHong.getThanhTien()) + " đ");

        // Đường kẻ trước THÀNH TIỀN
        javax.swing.JPanel separatorPanel = new javax.swing.JPanel();
        separatorPanel.setBackground(new java.awt.Color(200, 200, 200));
        separatorPanel.setMaximumSize(new java.awt.Dimension(450, 1));
        paymentPanel.add(separatorPanel);
        paymentPanel.add(javax.swing.Box.createVerticalStrut(5));

        // THÀNH TIỀN (in đậm, màu đỏ)
        javax.swing.JPanel thanhTienPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        thanhTienPanel.setBackground(java.awt.Color.WHITE);
        thanhTienPanel.setMaximumSize(new java.awt.Dimension(450, 30));

        javax.swing.JLabel lblThanhTienLeft = new javax.swing.JLabel("TONG GIA TRI XUAT HUY:");
        lblThanhTienLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

        javax.swing.JLabel lblThanhTienRight = new javax.swing.JLabel(
                currencyFormat.format(hangHong.getThanhTien()) + " đ");
        lblThanhTienRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblThanhTienRight.setForeground(new java.awt.Color(220, 53, 69));
        lblThanhTienRight.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

        thanhTienPanel.add(lblThanhTienLeft, java.awt.BorderLayout.WEST);
        thanhTienPanel.add(lblThanhTienRight, java.awt.BorderLayout.EAST);

        paymentPanel.add(thanhTienPanel);
        paymentPanel.add(javax.swing.Box.createVerticalStrut(5));

        // Đường kẻ sau THÀNH TIỀN
        javax.swing.JPanel separator2Panel = new javax.swing.JPanel();
        separator2Panel.setBackground(new java.awt.Color(200, 200, 200));
        separator2Panel.setMaximumSize(new java.awt.Dimension(450, 1));
        paymentPanel.add(separator2Panel);

        mainPanel.add(paymentPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(15));

        // ========== FOOTER ==========
        javax.swing.JLabel lblFooter1 = new javax.swing.JLabel("Cam on ban da su dung he thong!");
        lblFooter1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblFooter1.setForeground(new java.awt.Color(0, 120, 215));
        lblFooter1.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblFooter1);

        // Thêm mainPanel vào scrollPane
        mainScrollPane.setViewportView(mainPanel);

        dialog.add(mainScrollPane);
        dialog.setVisible(true);
    }

    /**
     * Xóa trắng tất cả các sản phẩm trong danh sách (sau khi tạo phiếu thành công)
     */
    private void xoaTrangDanhSachSanPham() {
        // Cập nhật lại tổng tiền = 0
        txtTongTien.setText("0 ₫");

        // Load lại danh sách sản phẩm cần xuất hủy (loadSanPhamHetHan đã xóa và tạo lại
        // header)
        loadSanPhamHetHan();

        System.out.println("DEBUG: Đã xóa và load lại danh sách sản phẩm sau khi tạo phiếu");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaoPhieu;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnLeft;
    private javax.swing.JPanel pnMid;
    private javax.swing.JTextField txtEmpName;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables

}

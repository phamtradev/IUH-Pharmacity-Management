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
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.XuatPhieuXuatHuyPDF;
import raven.toast.Notifications;

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
        fillContent();

        loadUserData();
    }

    private void loadUserData() {
        // Lấy nhân viên hiện tại từ Session
        NhanVien currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            txtEmpName.setText(currentUser.getTenNhanVien());
        } else { // không đăng nhập thì hiện này để test
            txtEmpName.setText("Không xác định");
            System.err.println("Er: khong co gnuoi dung trong Session!");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
        );
        pnMidLayout.setVerticalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        );

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
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

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
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtEmpName, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txtEmpName, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 517, Short.MAX_VALUE)
                .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

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
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
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
        headerProductPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerProductPanel.setPreferredSize(new java.awt.Dimension(1200, 50));
        headerProductPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        headerProductPanel.setMaximumSize(new java.awt.Dimension(32767, 50));

        // Sử dụng GridBagLayout giống Panel_ChiTietSanPhamXuatHuy
        headerProductPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Hình", 80, javax.swing.SwingConstants.CENTER), gbc);

        // 2. Tên sản phẩm + Thông tin lô
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        headerProductPanel.add(createHeaderLabel("Tên sản phẩm / Lô hàng", 250, javax.swing.SwingConstants.LEFT), gbc);

        // 3. Lý do
        gbc.gridx = 2;
        gbc.weightx = 0.08;
        headerProductPanel.add(createHeaderLabel("Lý do", 50, javax.swing.SwingConstants.LEFT), gbc);

        // 4. Đơn vị
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Đơn vị", 60, javax.swing.SwingConstants.CENTER), gbc);

        // 5. Số lượng
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("SL", 70, javax.swing.SwingConstants.CENTER), gbc);

        // 6. Đơn giá
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Đơn giá", 85, javax.swing.SwingConstants.RIGHT), gbc);

        // 7. Tổng tiền
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Tổng tiền", 95, javax.swing.SwingConstants.RIGHT), gbc);

        // 8. Thao tác
        gbc.gridx = 7;
        gbc.weightx = 0.0;
        headerProductPanel.add(createHeaderLabel("Thao tác", 60, javax.swing.SwingConstants.CENTER), gbc);

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

    private void btnTaoPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoPhieuActionPerformed
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
                    "Không có sản phẩm nào để xuất hủy!"
                );
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
                    "Không thể xác định nhân viên đang đăng nhập!"
                );
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
                    "Lỗi khi lưu phiếu xuất hủy vào database!"
                );
                return;
            }
            
            // 6. Tạo danh sách ChiTietHangHong và track các đơn trả cần cập nhật
            List<ChiTietHangHong> chiTietList = new ArrayList<>();
            java.util.Set<String> danhSachDonTraDaXuLy = new java.util.HashSet<>();
            
            for (Panel_ChiTietSanPhamXuatHuy panel : danhSachPanel) {
                ChiTietHangHong chiTiet = new ChiTietHangHong();
                chiTiet.setSoLuong(panel.getSoLuongHuy());
                chiTiet.setDonGia(panel.getDonGia());
                chiTiet.setThanhTien(panel.getTongTienHuy());
                chiTiet.setHangHong(hangHong);
                
                // Lấy lý do xuất hủy từ panel
                String lyDoXuatHuy = panel.getLyDoXuatHuy();
                if (lyDoXuatHuy != null && !lyDoXuatHuy.trim().isEmpty()) {
                    chiTiet.setLyDoXuatHuy(lyDoXuatHuy);
                } else {
                    chiTiet.setLyDoXuatHuy("Chưa rõ lý do");
                }
                
                // Lấy LoHang từ panel
                LoHang loHang = panel.getLoHang();
                if (loHang != null) {
                    chiTiet.setLoHang(loHang);
                } else {
                    System.err.println("Warning: Panel không có thông tin lô hàng");
                }
                
                chiTietList.add(chiTiet);
                
                // Lưu chi tiết vào database
                boolean successChiTiet = chiTietHangHongBUS.taoChiTietHangHong(chiTiet);
                if (!successChiTiet) {
                    System.err.println("Lỗi khi lưu chi tiết hàng hỏng");
                }
                
                // Track đơn trả cần cập nhật
                if (panel.getChiTietDonTra() != null) {
                    String maDonTra = panel.getChiTietDonTra().getDonTraHang().getMaDonTraHang();
                    danhSachDonTraDaXuLy.add(maDonTra);
                }
            }
            
            // 6.5. Cập nhật trạng thái các đơn trả hàng đã xuất hủy
            if (!danhSachDonTraDaXuLy.isEmpty()) {
                vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS donTraBUS = 
                    new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS();
                
                for (String maDonTra : danhSachDonTraDaXuLy) {
                    try {
                        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTra = 
                            donTraBUS.timDonTraTheoMa(maDonTra);
                        if (donTra != null) {
                            donTra.setTrangThaiXuLy("Đã xử lý");
                            donTraBUS.capNhatDonTraHang(donTra);
                            System.out.println("Đã cập nhật trạng thái đơn trả: " + maDonTra);
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi cập nhật trạng thái đơn trả " + maDonTra + ": " + e.getMessage());
                    }
                }
            }
            
            // 7. Hiển thị preview hóa đơn
            hienThiPhieuXuatHuy(hangHong, chiTietList);
            
            // 8. Xuất PDF phiếu xuất hủy
            String pdfPath = XuatPhieuXuatHuyPDF.xuatPhieuXuatHuyTuDong(hangHong, chiTietList);
            
            if (pdfPath != null) {
                Notifications.getInstance().show(
                    Notifications.Type.SUCCESS, 
                    "Tạo phiếu xuất hủy thành công! Mã: " + hangHong.getMaHangHong()
                );
                
                // 9. Xóa trắng tất cả các sản phẩm đã tạo phiếu
                xoaTrangDanhSachSanPham();
                
            } else {
                Notifications.getInstance().show(
                    Notifications.Type.WARNING, 
                    "Đã lưu phiếu vào database nhưng không thể xuất PDF!"
                );
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(
                Notifications.Type.ERROR, 
                "Lỗi khi tạo phiếu xuất hủy: " + e.getMessage()
            );
        }
    }//GEN-LAST:event_btnTaoPhieuActionPerformed

    //Load tự động các sản phẩm hết hạn khi mở màn hình (còn <= 6 tháng HSD)
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
                        loHang.getTonKho()
                    ); 
                    
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
                    
                    Panel_ChiTietSanPhamXuatHuy panel = new Panel_ChiTietSanPhamXuatHuy();
                    
                    panel.setTenSanPham(sanPham.getTenSanPham());
                    panel.setHinhAnh(sanPham.getHinhAnh()); // Load hình ảnh
                    // Hiển thị thông tin đơn trả (không có lô hàng và HSD vì đây là hàng đã bán)
                    panel.setLoHang(
                        "Đơn trả: " + chiTiet.getDonTraHang().getMaDonTraHang(), 
                        "N/A", // Không có HSD vì là hàng trả
                        chiTiet.getSoLuong()
                    ); 
                    
                    // Tạo LoHang giả cho hàng trả (để tránh NullPointerException)
                    LoHang loHangGia = new LoHang();
                    // Dùng mã LH00000 (mã đặc biệt cho hàng trả, không tồn tại trong DB)
                    loHangGia.setMaLoHang("LH00000");
                    loHangGia.setTenLoHang("Đơn trả: " + chiTiet.getDonTraHang().getMaDonTraHang());
                    loHangGia.setTonKho(chiTiet.getSoLuong());
                    loHangGia.setSanPham(sanPham); // QUAN TRỌNG: Set SanPham vào LoHang
                    loHangGia.setHanSuDung(null); // Hàng trả không có HSD
                    
                    panel.setLoHangObject(loHangGia);
                    
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

    private void jLabel5AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel5AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5AncestorAdded

    /**
     * Hiển thị preview phiếu xuất hủy
     */
    private void hienThiPhieuXuatHuy(HangHong hangHong, java.util.List<ChiTietHangHong> danhSachChiTiet) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Phiếu xuất hủy");
        dialog.setModal(true);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(null);
        
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
        headerPanel.add(createInfoLabel("Ngày lập: ", dateFormat.format(java.sql.Date.valueOf(hangHong.getNgayNhap())), false));
        headerPanel.add(createInfoLabel("Nhân viên: ", hangHong.getNhanVien().getTenNhanVien(), false));
        
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        
        // === CHI TIẾT SẢN PHẨM ===
        String[] columnNames = {"STT", "Tên sản phẩm", "Lô", "HSD", "ĐV", "SL", "Đơn giá", "Thành tiền", "Lý do"};
        javax.swing.table.DefaultTableModel modelDetail = new javax.swing.table.DefaultTableModel(columnNames, 0);
        
        int stt = 1;
        for (ChiTietHangHong chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            if (loHang == null || loHang.getSanPham() == null) continue;
            
            SanPham sp = loHang.getSanPham();
            String hsd = loHang.getHanSuDung() != null ? loHang.getHanSuDung().toString() : "N/A";
            
            // Lấy lý do từ ChiTietHangHong
            String lyDo = chiTiet.getLyDoXuatHuy();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                // Fallback nếu không có lý do
                lyDo = getLyDoFromLoHang(loHang);
            }
            
            modelDetail.addRow(new Object[]{
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
        
        javax.swing.JLabel lblTongTien = new javax.swing.JLabel("TỔNG GIÁ TRỊ XUẤT HỦY:  " + currencyFormat.format(hangHong.getThanhTien()) + " ₫");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTongTien.setForeground(new java.awt.Color(220, 53, 69));
        tongTienPanel.add(lblTongTien);
        
        footerPanel.add(tongTienPanel, java.awt.BorderLayout.NORTH);
        
        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
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
     * Xóa trắng tất cả các sản phẩm trong danh sách (sau khi tạo phiếu thành công)
     */
    private void xoaTrangDanhSachSanPham() {
        // Cập nhật lại tổng tiền = 0
        txtTongTien.setText("0 ₫");
        
        // Load lại danh sách sản phẩm cần xuất hủy (loadSanPhamHetHan đã xóa và tạo lại header)
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

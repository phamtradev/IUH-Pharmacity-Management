/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Optional;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 *
 * @author PhamTra
 */
public class GD_BanHang extends javax.swing.JPanel {

    static int transactionNumber = 1;
    private SanPhamBUS sanPhamBUS;
    private Panel_DonHang panelDonHang;

    /**
     * Creates new form LapHoaDonForm
     */
    public GD_BanHang() {
        // Khởi tạo BUS
        sanPhamBUS = new SanPhamBUS(new SanPhamDAO());
        
        lookAndFeelSet();
        initComponents();
        customUI();
        addPanelThanhToan();
        addHeaderRow();
    }

    private void addPanelThanhToan() {
        // Tạo panel wrapper để panel thanh toán chỉ nằm ngang với phần giữa
        javax.swing.JPanel wrapperPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrapperPanel.setBackground(java.awt.Color.WHITE);

        panelDonHang = new Panel_DonHang();
        panelDonHang.setGdBanHang(this); // Set reference đến form cha
        
        // KHÔNG cần listener cho "khuyenMaiChanged" vì capNhatTongTien() đã gọi tuDongApDungKhuyenMai()
        // Nếu thêm listener này sẽ gây ra việc tính toán lại không cần thiết
        
        wrapperPanel.add(pnMi, java.awt.BorderLayout.CENTER);
        wrapperPanel.add(panelDonHang, java.awt.BorderLayout.EAST);

        // Xóa pnMi khỏi vị trí cũ và thêm wrapper
        remove(pnMi);
        add(wrapperPanel, java.awt.BorderLayout.CENTER);
    }

    private void addHeaderRow() {
        // Tạo panel header với GridBagLayout giống HỆT Panel_ChiTietSanPham
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 245, 245));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        headerPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        headerPanel.setPreferredSize(new java.awt.Dimension(1000, 50));

        // Sử dụng GridBagLayout với constraints giống Panel_ChiTietSanPham
        headerPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8); // Giống Panel_ChiTietSanPham
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh - 80x100px (giống lblHinh)
        javax.swing.JLabel lblHeaderImg = new javax.swing.JLabel("Hình ảnh");
        lblHeaderImg.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderImg.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderImg, gbc);

        // 2. Tên sản phẩm - 180px (giống lblTenSP)
        javax.swing.JLabel lblHeaderName = new javax.swing.JLabel("Tên sản phẩm");
        lblHeaderName.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderName.setPreferredSize(new java.awt.Dimension(180, 30));
        lblHeaderName.setMinimumSize(new java.awt.Dimension(180, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        headerPanel.add(lblHeaderName, gbc);

        // 3. Lô hàng - 100px (giống jLabel1)
        javax.swing.JLabel lblHeaderBatch = new javax.swing.JLabel("Lô hàng");
        lblHeaderBatch.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderBatch.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderBatch.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderBatch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderBatch, gbc);

        // 4. Số lượng - 150px (giống pnSpinner với nút +/-)
        javax.swing.JLabel lblHeaderQty = new javax.swing.JLabel("Số lượng");
        lblHeaderQty.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderQty.setPreferredSize(new java.awt.Dimension(150, 30));
        lblHeaderQty.setMinimumSize(new java.awt.Dimension(150, 30));
        lblHeaderQty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderQty, gbc);

        // 5. Giảm giá - 70px (giống txtDiscount)
        javax.swing.JLabel lblHeaderDiscount = new javax.swing.JLabel("Giảm giá");
        lblHeaderDiscount.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderDiscount.setPreferredSize(new java.awt.Dimension(70, 30));
        lblHeaderDiscount.setMinimumSize(new java.awt.Dimension(70, 30));
        lblHeaderDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderDiscount, gbc);

        // 6. Đơn giá - 100px (giống txtDonGia)
        javax.swing.JLabel lblHeaderPrice = new javax.swing.JLabel("Đơn giá");
        lblHeaderPrice.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderPrice, gbc);

        // 7. Tổng tiền - 120px (giống txtTongTien)
        javax.swing.JLabel lblHeaderTotal = new javax.swing.JLabel("Tổng tiền");
        lblHeaderTotal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderTotal.setPreferredSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setMinimumSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderTotal, gbc);

        // 8. Chức năng - 70px (giống pnXoa)
        javax.swing.JLabel lblHeaderAction = new javax.swing.JLabel("Chức năng");
        lblHeaderAction.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderAction.setPreferredSize(new java.awt.Dimension(70, 30));
        lblHeaderAction.setMinimumSize(new java.awt.Dimension(70, 30));
        lblHeaderAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 7;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderAction, gbc);

        containerPanel.add(headerPanel);
    }

    private void customUI() {
        txtTimSanPham.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vui lòng quét mã vạch hoặc nhập số đăng ký");
        // Thêm viền cho text field
        txtTimSanPham.putClientProperty(FlatClientProperties.OUTLINE, "default");
        txtTimSanPham.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:1;"
                + "borderColor:#CCCCCC"
        );

        // Style cho button Thêm
        btnMa.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:0;"
                + "focusWidth:0"
        );

        // Style cho button Xóa trắng
        btnXoa.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:0;"
                + "focusWidth:0"
        );
        
        // Tự động focus vào ô tìm kiếm khi load form (QUAN TRỌNG cho máy quét barcode)
        javax.swing.SwingUtilities.invokeLater(() -> {
            txtTimSanPham.requestFocusInWindow();
        });
        
        // Chọn toàn bộ text khi focus vào (để máy quét ghi đè text cũ)
        txtTimSanPham.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    txtTimSanPham.selectAll();
                });
            }
        });
    }

    private void lookAndFeelSet() {
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 10);
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put("TabbedPane.tabHeight", 45);
        UIManager.put("ToggleButton.selectedBackground", new Color(81, 154, 244));
        UIManager.put("ToggleButton.selectedForeground", Color.WHITE);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMi = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtTimSanPham = new javax.swing.JTextField();
        btnMa = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        scrollPaneProducts = new javax.swing.JScrollPane();
        containerPanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(0, 0, 0));
        setLayout(new java.awt.BorderLayout());

        pnMi.setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jPanel1.setToolTipText("");

        txtTimSanPham.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimSanPhamKeyPressed(evt);
            }
        });

        btnMa.setBackground(new java.awt.Color(115, 165, 71));
        btnMa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMa.setForeground(new java.awt.Color(255, 255, 255));
        btnMa.setText("Thêm");
        btnMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(220, 53, 69));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa trắng");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(txtTimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMa, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTimSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btnMa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 300));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 500));
        jPanel2.setLayout(new java.awt.BorderLayout());

        // Container panel để chứa các Panel_ChiTietSanPham
        containerPanel.setBackground(new java.awt.Color(255, 255, 255));
        containerPanel.setLayout(new javax.swing.BoxLayout(containerPanel, javax.swing.BoxLayout.Y_AXIS));

        scrollPaneProducts.setViewportView(containerPanel);
        scrollPaneProducts.setBorder(null);
        scrollPaneProducts.getVerticalScrollBar().setUnitIncrement(16);

        jPanel2.add(scrollPaneProducts, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout pnMiLayout = new javax.swing.GroupLayout(pnMi);
        pnMi.setLayout(pnMiLayout);
        pnMiLayout.setHorizontalGroup(
            pnMiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnMiLayout.setVerticalGroup(
            pnMiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(jPanel1, java.awt.BorderLayout.NORTH);
        add(pnMi, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaActionPerformed
        timSanPham();
    }//GEN-LAST:event_btnMaActionPerformed

    //get ma vach
    //bat enter goi timSanPham()
    private void txtTimSanPhamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimSanPhamKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            timSanPham();
        }
    }//GEN-LAST:event_txtTimSanPhamKeyPressed
    
    /**
     * Tìm sản phẩm theo số đăng ký (hỗ trợ máy quét barcode)
     */
    private void timSanPham() {
        // Lấy và làm sạch input (trim, loại bỏ ký tự đặc biệt có thể có từ barcode scanner)
        String soDangKy = txtTimSanPham.getText().trim().replaceAll("[\\r\\n\\t]", "");
        
        // Cập nhật lại textfield với giá trị đã làm sạch
        txtTimSanPham.setText(soDangKy);
        
        // Debug: In ra console để kiểm tra
        System.out.println("🔍 Đang tìm sản phẩm với số đăng ký: '" + soDangKy + "' (length: " + soDangKy.length() + ")");
        
        // Kiểm tra input rỗng
        if (soDangKy.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Vui lòng nhập số đăng ký sản phẩm hoặc quét mã vạch");
            txtTimSanPham.requestFocus();
            return;
        }
        
        // Tìm sản phẩm theo số đăng ký
        Optional<SanPham> sanPhamOpt = sanPhamBUS.timSanPhamTheoSoDangKy(soDangKy);
        
        if (sanPhamOpt.isPresent()) {
            SanPham sanPham = sanPhamOpt.get();
            
            System.out.println("✅ Tìm thấy sản phẩm: " + sanPham.getTenSanPham());
            
            // Kiểm tra sản phẩm có đang hoạt động không
            if (!sanPham.isHoatDong()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                    "Sản phẩm '" + sanPham.getTenSanPham() + "' đã ngưng bán");
                txtTimSanPham.setText("");
                txtTimSanPham.requestFocus();
                return;
            }
            
            // Thêm sản phẩm vào giỏ hàng
            themSanPhamVaoGioHang(sanPham);
            
            // Thông báo thành công
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 
                "✓ Đã thêm: " + sanPham.getTenSanPham());
            
            // Xóa text field và focus lại (quan trọng cho lần quét tiếp theo)
            txtTimSanPham.setText("");
            txtTimSanPham.requestFocusInWindow();
        } else {
            System.out.println("❌ KHÔNG tìm thấy sản phẩm với số đăng ký: '" + soDangKy + "'");
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 
                "❌ Không tìm thấy sản phẩm: " + soDangKy);
            txtTimSanPham.selectAll();
            txtTimSanPham.requestFocusInWindow();
        }
    }
    
    /**
     * Thêm sản phẩm vào giỏ hàng (container panel)
     * - Nếu sản phẩm đã có trong giỏ → cộng dồn số lượng
     * - Nếu chưa có → tạo mới
     * - Luôn kiểm tra tồn kho từ TẤT CẢ lô hàng (FIFO)
     */
    private void themSanPhamVaoGioHang(SanPham sanPham) {
        // 1. Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Panel_ChiTietSanPham panelDaTonTai = null;
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                if (panel.getSanPham().getMaSanPham().equals(sanPham.getMaSanPham())) {
                    panelDaTonTai = panel;
                    break;
                }
            }
        }
        
        // 2. Lấy TẤT CẢ lô hàng của sản phẩm và áp dụng FIFO
        vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS loHangBUS = 
            new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS();
        java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang> danhSachLoHangGoc = 
            loHangBUS.getLoHangBySanPham(sanPham);
        
        // FIFO: Lọc + Sắp xếp theo HẠN SỬ DỤNG TĂNG DẦN (hết hạn sớm nhất → bán trước)
        java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang> danhSachLoHangFIFO = 
            danhSachLoHangGoc.stream()
                .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai()) // ① Lọc: Còn hàng + Còn hạn
                .sorted(java.util.Comparator.comparing(
                    vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang::getHanSuDung)) // ② FIFO: Sắp xếp
                .collect(java.util.stream.Collectors.toList());
        
        // Tính tổng tồn kho từ các lô FIFO
        int tongTonKho = danhSachLoHangFIFO.stream()
            .mapToInt(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang::getTonKho)
            .sum();
        
        // DEBUG: In ra thứ tự lô hàng theo FIFO
        if (!danhSachLoHangFIFO.isEmpty()) {
            System.out.println("📦 FIFO - Thứ tự bán lô hàng cho: " + sanPham.getTenSanPham());
            for (int i = 0; i < danhSachLoHangFIFO.size(); i++) {
                vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang lh = danhSachLoHangFIFO.get(i);
                System.out.println("  " + (i+1) + ". " + lh.getTenLoHang() + 
                    " | HSD: " + lh.getHanSuDung() + 
                    " | Tồn: " + lh.getTonKho());
            }
        }
        
        // 3. Xác định số lượng cần thêm
        int soLuongCanThem = 1; // Mặc định thêm 1
        int soLuongHienTai = 0;
        
        if (panelDaTonTai != null) {
            soLuongHienTai = panelDaTonTai.getSoLuong();
            soLuongCanThem = soLuongHienTai + 1; // Cộng dồn
        }
        
        // 4. Kiểm tra tồn kho
        if (tongTonKho <= 0) {
            Notifications.getInstance().show(
                Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "❌ Sản phẩm '" + sanPham.getTenSanPham() + "' đã HẾT HÀNG!"
            );
            return;
        }
        
        if (soLuongCanThem > tongTonKho) {
            Notifications.getInstance().show(
                Notifications.Type.WARNING, 
                Notifications.Location.TOP_CENTER,
                "⚠️ Chỉ còn " + tongTonKho + " " + 
                (sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getTenDonVi() : "sản phẩm") + 
                " '" + sanPham.getTenSanPham() + "' trong kho!"
            );
            return;
        }
        
        // 5. Thêm hoặc cộng dồn
        if (panelDaTonTai != null) {
            // Sản phẩm đã có → cộng dồn số lượng
            final Panel_ChiTietSanPham panelFinal = panelDaTonTai; // Final để dùng trong lambda
            panelFinal.setSoLuong(soLuongCanThem);
            
            // Highlight panel để người dùng biết đã cộng dồn
            panelFinal.setBackground(new java.awt.Color(200, 255, 200)); // Màu xanh nhạt
            javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
                panelFinal.setBackground(java.awt.Color.WHITE);
            });
            timer.setRepeats(false);
            timer.start();
            
            System.out.println("✅ Cộng dồn: " + sanPham.getTenSanPham() + 
                " | SL: " + soLuongHienTai + " → " + soLuongCanThem);
        } else {
            // Sản phẩm chưa có → tạo panel mới
            Panel_ChiTietSanPham panelChiTiet = new Panel_ChiTietSanPham(sanPham);
            
            // Thêm listener để cập nhật tổng tiền khi có thay đổi
            panelChiTiet.addPropertyChangeListener("tongTien", evt -> capNhatTongTien());
            panelChiTiet.addPropertyChangeListener("sanPhamXoa", evt -> capNhatTongTien());
            
            containerPanel.add(panelChiTiet);
            containerPanel.revalidate();
            containerPanel.repaint();
            
            System.out.println("✅ Thêm mới: " + sanPham.getTenSanPham() + " | SL: 1");
        }
        
        // 6. Cập nhật tổng tiền
        capNhatTongTien();
    }
    
    /**
     * Cập nhật tổng tiền đơn hàng từ tất cả các sản phẩm
     * LOGIC MỚI: Hỗ trợ áp dụng CẢ 2 loại khuyến mãi đồng thời
     */
    public void capNhatTongTien() {
        double tongTienHang = 0;
        double tongGiamGiaSanPham = 0;
        double giamGiaHoaDon = 0;
        
        // Thu thập danh sách sản phẩm và số lượng trong giỏ hàng
        java.util.Map<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham, Integer> danhSachSanPham = 
            new java.util.HashMap<>();
        
        // Đếm số lượng sản phẩm trong giỏ hàng (không tính header)
        int soLuongSanPham = 0;
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                soLuongSanPham++;
            }
        }
        
        // Nếu giỏ hàng rỗng (chỉ còn header), reset tất cả về 0
        if (soLuongSanPham == 0) {
            if (panelDonHang != null) {
                panelDonHang.resetThanhToan();
            }
            return;
        }
        
        // Duyệt qua tất cả các Panel_ChiTietSanPham để tính tổng tiền và thu thập sản phẩm
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                tongTienHang += panel.getTongTien();
                
                // Thêm vào danh sách sản phẩm
                danhSachSanPham.put(panel.getSanPham(), panel.getSoLuong());
                
                // Reset giảm giá sản phẩm về 0
                panel.setGiamGia(0);
            }
        }
        
        // Tự động tìm và áp dụng TẤT CẢ khuyến mãi phù hợp (có thể cả 2 loại)
        panelDonHang.tuDongApDungKhuyenMai(tongTienHang, danhSachSanPham);
        
        // Lấy DANH SÁCH khuyến mãi đã chọn từ Panel_DonHang (sau khi tự động áp dụng)
        java.util.Map<vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai, 
                       vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai> danhSachKhuyenMai = 
            panelDonHang.getDanhSachKhuyenMaiDaChon();
        
        // ========== XỬ LÝ KHUYẾN MÃI SẢN PHẨM ==========
        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai kmSanPham = 
            danhSachKhuyenMai.get(vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai.SAN_PHAM);
        
        if (kmSanPham != null) {
            // Lấy danh sách sản phẩm trong chương trình khuyến mãi
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham> danhSachCTKM = 
                panelDonHang.getChiTietKhuyenMaiSanPhamBUS().timTheoMaKhuyenMai(kmSanPham.getMaKhuyenMai());
            
            // Duyệt qua từng sản phẩm trong giỏ hàng
            for (Component comp : containerPanel.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPham) {
                    Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                    
                    // Kiểm tra xem sản phẩm có trong chương trình khuyến mãi không
                    for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham ctkm : danhSachCTKM) {
                        if (ctkm.getSanPham().getMaSanPham().equals(panel.getSanPham().getMaSanPham())) {
                            // Sản phẩm có trong chương trình khuyến mãi
                            // getGiamGia() đã trả về dạng thập phân (0.1 = 10%), không cần chia 100
                            double giamGia = panel.getTongTien() * kmSanPham.getGiamGia();
                            tongGiamGiaSanPham += giamGia;
                            
                            // Cập nhật giảm giá cho panel (hiển thị % giảm giá + TÊN KHUYẾN MÃI)
                            panel.setGiamGia(kmSanPham.getGiamGia(), kmSanPham.getTenKhuyenMai());
                            break;
                        }
                    }
                }
            }
        }
        
        // ========== XỬ LÝ KHUYẾN MÃI ĐƠN HÀNG ==========
        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai kmDonHang = 
            danhSachKhuyenMai.get(vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai.DON_HANG);
        
        if (kmDonHang != null) {
            // Kiểm tra điều kiện giá tối thiểu
            if (tongTienHang >= kmDonHang.getGiaToiThieu()) {
                // getGiamGia() đã trả về dạng thập phân (0.1 = 10%), không cần chia 100
                giamGiaHoaDon = tongTienHang * kmDonHang.getGiamGia();
                
                // Áp dụng giới hạn giá tối đa (nếu có)
                if (kmDonHang.getGiaToiDa() > 0 && giamGiaHoaDon > kmDonHang.getGiaToiDa()) {
                    giamGiaHoaDon = kmDonHang.getGiaToiDa();
                }
            }
        }
        
        // DEBUG: In ra console để kiểm tra
        System.out.println("\n====== DEBUG capNhatTongTien ======");
        System.out.println("Tong tien hang: " + tongTienHang);
        System.out.println("Tong giam gia san pham: " + tongGiamGiaSanPham);
        System.out.println("Giam gia hoa don: " + giamGiaHoaDon);
        System.out.println("Danh sach khuyen mai da chon:");
        java.util.Map<vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai, 
                       vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai> danhSachKMDebug = 
            panelDonHang.getDanhSachKhuyenMaiDaChon();
        for (java.util.Map.Entry<vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai, 
                                   vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai> entry : danhSachKMDebug.entrySet()) {
            System.out.println("  - " + entry.getKey() + ": " + entry.getValue().getTenKhuyenMai() + 
                             " (Loai: " + entry.getValue().getLoaiKhuyenMai() + ", Giam: " + entry.getValue().getGiamGia() + "%)");
        }
        System.out.println("===================================\n");
        
        // Cập nhật vào Panel_DonHang
        if (panelDonHang != null) {
            panelDonHang.updateTongTienHang(tongTienHang);
            panelDonHang.updateDiscountProduct(tongGiamGiaSanPham);
            panelDonHang.updateDiscountOrder(giamGiaHoaDon);
        }
    }

    private JPanel createTabTitle(JTabbedPane tabbedPane, String title, Component tabComponent) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        // Label hiển thị tên tab
        JLabel label = new JLabel(title);
        panel.add(label);

        // Nút close
        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 1, 0, 0));
        closeButton.setPreferredSize(new Dimension(15, 15));

        // Hành động khi nhấn nút close
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = tabbedPane.indexOfComponent(tabComponent);
                if (index != -1 && tabbedPane.getTabCount() != 1) {
                    tabbedPane.remove(index);  // Xóa tab tương ứng
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(closeButton);

        return panel;
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        // Xóa nội dung text field và đặt focus
        txtTimSanPham.setText("");
        txtTimSanPham.requestFocus();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa trắng thành công !");
    }
    
    /**
     * Lấy danh sách sản phẩm trong giỏ hàng
     */
    public java.util.List<Panel_ChiTietSanPham> getDanhSachSanPhamTrongGio() {
        java.util.List<Panel_ChiTietSanPham> danhSach = new java.util.ArrayList<>();
        
        // Duyệt qua tất cả components trong containerPanel
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                danhSach.add((Panel_ChiTietSanPham) comp);
            }
        }
        
        return danhSach;
    }
    
    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void xoaToanBoGioHang() {
        // Lấy danh sách Panel_ChiTietSanPham
        java.util.List<Panel_ChiTietSanPham> danhSach = getDanhSachSanPhamTrongGio();
        
        // Xóa từng panel
        for (Panel_ChiTietSanPham panel : danhSach) {
            containerPanel.remove(panel);
        }
        
        // Cập nhật giao diện
        containerPanel.revalidate();
        containerPanel.repaint();
        
        // Cập nhật tổng tiền (sẽ về 0)
        capNhatTongTien();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMa;
    private javax.swing.JButton btnXoa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel pnMi;
    private javax.swing.JScrollPane scrollPaneProducts;
    private javax.swing.JPanel containerPanel;
    private javax.swing.JTextField txtTimSanPham;
    // End of variables declaration//GEN-END:variables

}

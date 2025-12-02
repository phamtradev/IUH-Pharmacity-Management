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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

/**
 *
 * @author PhamTra
 */
public class GD_BanHang extends javax.swing.JPanel {

    static int transactionNumber = 1;
    private SanPhamBUS sanPhamBUS;
    private Panel_DonHang panelDonHang;
    
    // TextField và Button tìm đơn hàng
    private javax.swing.JTextField txtTimDonHang;
    private javax.swing.JButton btnTimDonHang;

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
        javax.swing.JPanel wrapperPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrapperPanel.setBackground(java.awt.Color.WHITE);

        panelDonHang = new Panel_DonHang();
        panelDonHang.setGdBanHang(this);

        wrapperPanel.add(pnMi, java.awt.BorderLayout.CENTER);
        wrapperPanel.add(panelDonHang, java.awt.BorderLayout.EAST);

        remove(pnMi);
        add(wrapperPanel, java.awt.BorderLayout.CENTER);
    }

    private void addHeaderRow() {
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 245, 245));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        headerPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        headerPanel.setPreferredSize(new java.awt.Dimension(1000, 50));

        headerPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8); // Giống Panel_ChiTietSanPham
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        javax.swing.JLabel lblHeaderImg = new javax.swing.JLabel("Hình ảnh");
        lblHeaderImg.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderImg.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderImg, gbc);

        javax.swing.JLabel lblHeaderName = new javax.swing.JLabel("Tên sản phẩm");
        lblHeaderName.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderName.setPreferredSize(new java.awt.Dimension(180, 30));
        lblHeaderName.setMinimumSize(new java.awt.Dimension(180, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        headerPanel.add(lblHeaderName, gbc);

        javax.swing.JLabel lblHeaderBatch = new javax.swing.JLabel("Lô hàng");
        lblHeaderBatch.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderBatch.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderBatch.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderBatch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderBatch, gbc);

        javax.swing.JLabel lblHeaderQty = new javax.swing.JLabel("Số lượng");
        lblHeaderQty.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderQty.setPreferredSize(new java.awt.Dimension(150, 30));
        lblHeaderQty.setMinimumSize(new java.awt.Dimension(150, 30));
        lblHeaderQty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderQty, gbc);

        javax.swing.JLabel lblHeaderDiscount = new javax.swing.JLabel("Giảm giá");
        lblHeaderDiscount.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderDiscount.setPreferredSize(new java.awt.Dimension(70, 30));
        lblHeaderDiscount.setMinimumSize(new java.awt.Dimension(70, 30));
        lblHeaderDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderDiscount, gbc);

        javax.swing.JLabel lblHeaderPrice = new javax.swing.JLabel("Đơn giá");
        lblHeaderPrice.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderPrice, gbc);

        javax.swing.JLabel lblHeaderTotal = new javax.swing.JLabel("Tổng tiền");
        lblHeaderTotal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderTotal.setPreferredSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setMinimumSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderTotal, gbc);

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
        txtTimDonHang.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vui lòng quét mã vạch hoặc nhập mã hóa đơn");
        txtTimDonHang.putClientProperty(FlatClientProperties.OUTLINE, "default");
        txtTimDonHang.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:1;"
                + "borderColor:#CCCCCC"
        );
        txtTimDonHang.setToolTipText("Quét barcode hoặc nhập mã đơn hàng rồi nhấn Enter");
        
        ButtonStyles.apply(btnTimDonHang, ButtonStyles.Type.PRIMARY);
        
        txtTimSanPham.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vui lòng quét mã vạch hoặc nhập số đăng ký");
        txtTimSanPham.putClientProperty(FlatClientProperties.OUTLINE, "default");
        txtTimSanPham.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;"
                + "borderWidth:1;"
                + "borderColor:#CCCCCC"
        );

        ButtonStyles.apply(btnMa, ButtonStyles.Type.SUCCESS);

        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            txtTimSanPham.requestFocusInWindow();
        });
        
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
        txtTimDonHang = new javax.swing.JTextField();
        btnTimDonHang = new javax.swing.JButton();
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

        txtTimDonHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimDonHangActionPerformed(evt);
            }
        });
        txtTimDonHang.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    timDonHang();
                }
            }
        });

        btnTimDonHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTimDonHang.setText("Tìm");
        btnTimDonHang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimDonHangActionPerformed(evt);
            }
        });

        txtTimSanPham.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimSanPhamKeyPressed(evt);
            }
        });

        btnMa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMa.setText("Thêm");
        btnMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
                .addGap(15, 15, 15)
                .addComponent(txtTimSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnMa, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(txtTimDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTimDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTimDonHang, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btnTimDonHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(btnMa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 300));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 500));
        jPanel2.setLayout(new java.awt.BorderLayout());

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
    
    private void timSanPham() {
        String soDangKy = txtTimSanPham.getText().trim().replaceAll("[\\r\\n\\t]", "");
        txtTimSanPham.setText(soDangKy);

        if (soDangKy.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Vui lòng nhập số đăng ký sản phẩm hoặc quét mã vạch");
            txtTimSanPham.requestFocus();
            return;
        }
        
        Optional<SanPham> sanPhamOpt = sanPhamBUS.timSanPhamTheoSoDangKy(soDangKy);
        
        if (sanPhamOpt.isPresent()) {
            SanPham sanPham = sanPhamOpt.get();

            if (!sanPham.isHoatDong()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                    "Sản phẩm '" + sanPham.getTenSanPham() + "' đã ngưng bán");
                txtTimSanPham.setText("");
                txtTimSanPham.requestFocus();
                return;
            }
            
            themSanPhamVaoGioHang(sanPham);
            
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 
                "✓ Đã thêm: " + sanPham.getTenSanPham());
            
            txtTimSanPham.setText("");
            txtTimSanPham.requestFocusInWindow();
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 
                "❌ Không tìm thấy sản phẩm: " + soDangKy);
            txtTimSanPham.selectAll();
            txtTimSanPham.requestFocusInWindow();
        }
    }
    
    private void themSanPhamVaoGioHang(SanPham sanPham) {
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
        
        LoHangBUS loHangBUS = new LoHangBUS();
        List<LoHang> danhSachLoHangGoc = loHangBUS.getLoHangBySanPham(sanPham);
        
        List<LoHang> danhSachLoHangFIFO = danhSachLoHangGoc.stream()
                .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
                .sorted(Comparator.comparing(LoHang::getHanSuDung))
                .collect(Collectors.toList());
        
        int tongTonKho = danhSachLoHangFIFO.stream()
            .mapToInt(LoHang::getTonKho)
            .sum();

        int soLuongCanThem = 1;
        int soLuongHienTai = 0;
        
        if (panelDaTonTai != null) {
            soLuongHienTai = panelDaTonTai.getSoLuong();
            soLuongCanThem = soLuongHienTai + 1; // Cộng dồn
        }
        
        if (tongTonKho <= 0) {
            Notifications.getInstance().show(
                Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "❌ Sản phẩm '" + sanPham.getTenSanPham() + "' đã hết hàng"
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
        
        if (panelDaTonTai != null) {
            final Panel_ChiTietSanPham panelFinal = panelDaTonTai;
            panelFinal.setSoLuong(soLuongCanThem);
            
            panelFinal.setBackground(new java.awt.Color(200, 255, 200));
            javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
                panelFinal.setBackground(java.awt.Color.WHITE);
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            Panel_ChiTietSanPham panelChiTiet = new Panel_ChiTietSanPham(sanPham);
            
            panelChiTiet.addPropertyChangeListener("tongTien", evt -> capNhatTongTien());
            panelChiTiet.addPropertyChangeListener("sanPhamXoa", evt -> capNhatTongTien());
            
            containerPanel.add(panelChiTiet);
            containerPanel.revalidate();
            containerPanel.repaint();
        }
        
        capNhatTongTien();
    }
    
    public void capNhatTongTien() {
        double tongTienHang = 0;
        double tongGiamGiaSanPham = 0;
        double giamGiaHoaDon = 0;
        
        Map<SanPham, Integer> danhSachSanPham = new HashMap<>();
        
        int soLuongSanPham = 0;
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                soLuongSanPham++;
            }
        }
        
        if (soLuongSanPham == 0) {
            if (panelDonHang != null) {
                panelDonHang.resetThanhToan();
            }
            return;
        }
        
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                tongTienHang += panel.getTongTien();
                
                danhSachSanPham.put(panel.getSanPham(), panel.getSoLuong());
                
                panel.setGiamGia(0);
                panel.setSoTienGiamGiaThucTe(null);
            }
        }
        
        List<KhuyenMai> danhSachKMSanPham = panelDonHang.getKhuyenMaiBUS().getKhuyenMaiConHan()
                .stream()
                .filter(km -> km.getLoaiKhuyenMai() == LoaiKhuyenMai.SAN_PHAM)
                .collect(Collectors.toList());
        
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                KhuyenMai kmTotNhat = null;
                double giamGiaMax = 0;
                
                for (KhuyenMai km : danhSachKMSanPham) {
                    List<ChiTietKhuyenMaiSanPham> danhSachCTKM =
                        panelDonHang.getChiTietKhuyenMaiSanPhamBUS()
                            .timTheoMaKhuyenMai(km.getMaKhuyenMai());
                    
                    for (ChiTietKhuyenMaiSanPham ctkm : danhSachCTKM) {
                        if (ctkm.getSanPham().getMaSanPham().equals(panel.getSanPham().getMaSanPham())) {
                            int soLuongMua = panel.getSoLuong();
                            int soLuongToiDa = km.getSoLuongToiDa();
                            
                            double tongTienApDung;
                            int soLuongApDung;
                            if (soLuongToiDa > 0) {
                                soLuongApDung = Math.min(soLuongMua, soLuongToiDa);
                                // Khuyến mãi áp dụng trên đơn giá đã bao gồm VAT
                                double giaBanCoVAT = panel.getSanPham().getGiaBan() * (1 + panel.getSanPham().getThueVAT());
                                tongTienApDung = giaBanCoVAT * soLuongApDung;
                            } else {
                                soLuongApDung = soLuongMua;
                                tongTienApDung = panel.getTongTien();
                            }
                            
                            double giamGia = tongTienApDung * km.getGiamGia();
                            
                            if (giamGia > giamGiaMax) {
                                giamGiaMax = giamGia;
                                kmTotNhat = km;
                            }
                            break;
                        }
                    }
                }
                
                if (kmTotNhat != null) {
                    tongGiamGiaSanPham += giamGiaMax;
                    
                    int soLuongMua = panel.getSoLuong();
                    int soLuongToiDa = kmTotNhat.getSoLuongToiDa();
                    
                    if (soLuongToiDa > 0 && soLuongMua > soLuongToiDa) {
                        panel.setSoTienGiamGiaThucTe(giamGiaMax);
                        panel.setGiamGia(kmTotNhat.getGiamGia(), kmTotNhat.getTenKhuyenMai());
                    } else {
                        panel.setSoTienGiamGiaThucTe(null);
                        panel.setGiamGia(kmTotNhat.getGiamGia(), kmTotNhat.getTenKhuyenMai());
                    }
                    
                    panel.setKhuyenMaiDuocApDung(kmTotNhat);
                } else {
                    panel.setKhuyenMaiDuocApDung(null);
                    panel.setSoTienGiamGiaThucTe(null);
                }
            }
        }

        List<KhuyenMai> danhSachKMDonHang = panelDonHang.getKhuyenMaiBUS().getKhuyenMaiConHan()
                .stream()
                .filter(km -> km.getLoaiKhuyenMai() == LoaiKhuyenMai.DON_HANG)
                .collect(Collectors.toList());
        
        KhuyenMai kmDonHangTotNhat = null;
        double giamGiaDonHangMax = 0;
        
        for (KhuyenMai km : danhSachKMDonHang) {
            if (tongTienHang >= km.getGiaToiThieu()) {
                if (km.getGiaToiDa() > 0 && tongTienHang > km.getGiaToiDa()) {
                    continue;
                }
                
                double giamGia = tongTienHang * km.getGiamGia();

                if (giamGia > giamGiaDonHangMax) {
                    giamGiaDonHangMax = giamGia;
                    kmDonHangTotNhat = km;
                }
            }
        }
        
        if (kmDonHangTotNhat != null) {
            giamGiaHoaDon = giamGiaDonHangMax;
        }
        
        Map<LoaiKhuyenMai, KhuyenMai> danhSachKMDaChon = new HashMap<>();
        
        if (kmDonHangTotNhat != null) {
            danhSachKMDaChon.put(LoaiKhuyenMai.DON_HANG, kmDonHangTotNhat);
        }
        
        if (panelDonHang != null) {
            panelDonHang.updateTongTienHang(tongTienHang);
            panelDonHang.updateDiscountProduct(tongGiamGiaSanPham, null);
            panelDonHang.updateDiscountOrder(giamGiaHoaDon, kmDonHangTotNhat);
        }
    }

    private JPanel createTabTitle(JTabbedPane tabbedPane, String title, Component tabComponent) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(title);
        panel.add(label);

        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 1, 0, 0));
        closeButton.setPreferredSize(new Dimension(15, 15));

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
        txtTimSanPham.setText("");
        
        xoaToanBoGioHang();
        
        txtTimSanPham.requestFocus();
        
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa trắng thành công !");
    }
    
    private void txtTimDonHangActionPerformed(java.awt.event.ActionEvent evt) {
        timDonHang();
    }
    
    private void btnTimDonHangActionPerformed(java.awt.event.ActionEvent evt) {
        timDonHang();
    }
    
    private void timDonHang() {
        if (panelDonHang != null) {
            panelDonHang.timDonHangTuGDBanHang(txtTimDonHang.getText().trim());
            txtTimDonHang.setText("");
            txtTimSanPham.requestFocus();
        }
    }
    
    public List<Panel_ChiTietSanPham> getDanhSachSanPhamTrongGio() {
        List<Panel_ChiTietSanPham> danhSach = new ArrayList<>();
        
        for (Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                danhSach.add((Panel_ChiTietSanPham) comp);
            }
        }
        
        return danhSach;
    }
    
    public void xoaToanBoGioHang() {
        List<Panel_ChiTietSanPham> danhSach = getDanhSachSanPhamTrongGio();
        
        for (Panel_ChiTietSanPham panel : danhSach) {
            containerPanel.remove(panel);
        }
        
        containerPanel.revalidate();
        containerPanel.repaint();
        
        if (panelDonHang != null) {
            panelDonHang.resetMaDonHangHienTai();
        }
        
        capNhatTongTien();
    }
    
    public void themSanPhamVaoGio(SanPham sanPham, 
                                   LoHang loHang,
                                   int soLuong) {
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
        
        if (loHang == null) {
            Notifications.getInstance().show(
                Notifications.Type.WARNING, 
                Notifications.Location.TOP_CENTER,
                "⚠️ Lô hàng không tồn tại cho sản phẩm '" + sanPham.getTenSanPham() + "'!"
            );
            return;
        }
        
        if (soLuong <= 0) {
            Notifications.getInstance().show(
                Notifications.Type.WARNING, 
                Notifications.Location.TOP_CENTER,
                "⚠️ Số lượng phải lớn hơn 0!"
            );
            return;
        }
        
        if (panelDaTonTai != null) {
            int soLuongMoi = panelDaTonTai.getSoLuong() + soLuong;
            panelDaTonTai.setSoLuong(soLuongMoi);
            
            final Panel_ChiTietSanPham panelFinal = panelDaTonTai;
            panelFinal.setBackground(new java.awt.Color(200, 255, 200));
            javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
                panelFinal.setBackground(java.awt.Color.WHITE);
            });
            timer.setRepeats(false);
            timer.start();
            
        } else {
            Panel_ChiTietSanPham panelChiTiet = new Panel_ChiTietSanPham(sanPham);
            panelChiTiet.setSoLuong(soLuong); // Set số lượng từ đơn hàng
            
            panelChiTiet.addPropertyChangeListener("tongTien", evt -> capNhatTongTien());
            panelChiTiet.addPropertyChangeListener("sanPhamXoa", evt -> capNhatTongTien());
            
            containerPanel.add(panelChiTiet);
            containerPanel.revalidate();
            containerPanel.repaint();

            javax.swing.SwingUtilities.invokeLater(() -> {
                java.awt.Rectangle bounds = panelChiTiet.getBounds();
                scrollPaneProducts.getViewport().scrollRectToVisible(bounds);
            });
        }
        
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
    
    public javax.swing.JPanel getContainerPanel() {
        return containerPanel;
    }

}

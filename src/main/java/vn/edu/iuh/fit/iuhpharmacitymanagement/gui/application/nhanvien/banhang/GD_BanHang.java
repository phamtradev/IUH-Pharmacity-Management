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
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 *
 * @author PhamTra
 */
public class GD_BanHang extends javax.swing.JPanel {

    static int transactionNumber = 1;

    /**
     * Creates new form LapHoaDonForm
     */
    public GD_BanHang() {
        lookAndFeelSet();
        initComponents();
        customUI();
        addPanelThanhToan();
        addHeaderRow();
        addSampleProduct();
    }

    private void addPanelThanhToan() {
        // Tạo panel wrapper để panel thanh toán chỉ nằm ngang với phần giữa
        javax.swing.JPanel wrapperPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrapperPanel.setBackground(java.awt.Color.WHITE);

        Panel_DonHang panelThanhToan = new Panel_DonHang();
        wrapperPanel.add(pnMi, java.awt.BorderLayout.CENTER);
        wrapperPanel.add(panelThanhToan, java.awt.BorderLayout.EAST);

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

        // 4. Số lượng - 100px (giống pnSpinner)
        javax.swing.JLabel lblHeaderQty = new javax.swing.JLabel("Số lượng");
        lblHeaderQty.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderQty.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderQty.setMinimumSize(new java.awt.Dimension(100, 30));
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

    private void addSampleProduct() {
        // Thêm một panel chi tiết sản phẩm mẫu vào container
        Panel_ChiTietSanPham panelChiTiet = new Panel_ChiTietSanPham();
        containerPanel.add(panelChiTiet);
        containerPanel.revalidate();
        containerPanel.repaint();
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

    @SuppressWarnings("unchecked")
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
//        // TODO add your handling code here:
//        String sdk = txtTimSanPham.getText().trim();
//        searchProduct(sdk);
    }//GEN-LAST:event_btnMaActionPerformed

    private void txtTimSanPhamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimSanPhamKeyPressed
//        // TODO add your handling code here:
//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            String sdk = txtTimSanPham.getText().trim();
//            searchProduct(sdk);
//            txtTimSanPham.requestFocus();
//        }
    }//GEN-LAST:event_txtTimSanPhamKeyPressed

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

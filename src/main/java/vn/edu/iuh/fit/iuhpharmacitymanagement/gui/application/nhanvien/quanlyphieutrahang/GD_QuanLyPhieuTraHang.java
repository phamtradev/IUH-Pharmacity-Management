/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang;

import com.formdev.flatlaf.FlatClientProperties;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyPhieuTraHang extends javax.swing.JPanel {

    public GD_QuanLyPhieuTraHang() {
        initComponents();
        applyButtonStyles();
        applyFontStyles();

        txtSearchOrder.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã hóa đơn");
        // Ensure split pane divider is visible and panels have size
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setOneTouchExpandable(true);
        pnHaveOrder.setPreferredSize(new java.awt.Dimension(400, 300));
        pnHaveOrder.setMinimumSize(new java.awt.Dimension(200, 150));
        pnHaveReturn.setPreferredSize(new java.awt.Dimension(400, 300));
        pnHaveReturn.setMinimumSize(new java.awt.Dimension(200, 150));
        // Keep an initial balanced layout
        jSplitPane1.setResizeWeight(0.5);

        // Set divider to 50% after component is displayed
        addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                    removeHierarchyListener(this);
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            jSplitPane1.setDividerLocation(0.5);
                        }
                    });
                }
            }
        });

        // Add header rows for both panels
        addHeaderRowForOrder();
        addHeaderRowForReturn();
    }

    private void applyButtonStyles() {
        ButtonStyles.apply(btnTaoPhieu, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnOpenModalAddUnit, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnTraTatCa, ButtonStyles.Type.WARNING);
        ButtonStyles.apply(btnNhapLyDoChoTatCa, ButtonStyles.Type.INFO);
        ButtonStyles.apply(btnXoaTatCa, ButtonStyles.Type.DANGER);
    }

    /**
     * Áp dụng FontStyles cho tất cả component
     */
    private void applyFontStyles() {
        // Font cho các button chính
        FontStyles.apply(btnTaoPhieu, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnOpenModalAddUnit, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnTraTatCa, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnNhapLyDoChoTatCa, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnXoaTatCa, FontStyles.Type.BUTTON_MEDIUM);

        // Font cho text field tìm kiếm
        FontStyles.apply(txtSearchOrder, FontStyles.Type.INPUT_FIELD);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMid = new javax.swing.JPanel();
        pnHaveContent = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnHaveOrder = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnHaveReturn = new javax.swing.JPanel();
        pnLeft = new javax.swing.JPanel();
        btnTaoPhieu = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCusPhone = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtReturnTotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCusName = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtOrderId = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtEmpName = new javax.swing.JTextField();
        headerPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnOpenModalAddUnit = new javax.swing.JButton();
        txtSearchOrder = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        pnMid.setMinimumSize(new java.awt.Dimension(200, 200));
        pnMid.setOpaque(false);
        pnMid.setLayout(new javax.swing.BoxLayout(pnMid, javax.swing.BoxLayout.LINE_AXIS));

        pnHaveContent.setBackground(new java.awt.Color(255, 255, 255));
        pnHaveContent.setLayout(new javax.swing.BoxLayout(pnHaveContent, javax.swing.BoxLayout.Y_AXIS));

        jSplitPane1.setBackground(new java.awt.Color(255, 255, 255));
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setToolTipText("");

        pnHaveOrder.setBackground(new java.awt.Color(255, 255, 255));
        pnHaveOrder.setLayout(new javax.swing.BoxLayout(pnHaveOrder, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(pnHaveOrder);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setToolTipText("");

        pnHaveReturn.setBackground(new java.awt.Color(255, 255, 255));
        pnHaveReturn.setLayout(new javax.swing.BoxLayout(pnHaveReturn, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane3.setViewportView(pnHaveReturn);

        jSplitPane1.setRightComponent(jScrollPane3);

        pnHaveContent.add(jSplitPane1);

        pnMid.add(pnHaveContent);

        add(pnMid, java.awt.BorderLayout.CENTER);

        pnLeft.setBackground(new java.awt.Color(255, 255, 255));
        pnLeft.setPreferredSize(new java.awt.Dimension(485, 650));

        btnTaoPhieu = new javax.swing.JButton();
        btnTaoPhieu.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTaoPhieu.setText("Tạo phiếu");
        btnTaoPhieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoPhieuActionPerformed(evt);
            }
        });
        btnTaoPhieu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTaoPhieuKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() | java.awt.Font.BOLD,
                jLabel3.getFont().getSize() + 2));
        jLabel3.setText("Số điện thoại:");

        txtCusPhone.setEditable(false);
        txtCusPhone.setFont(txtCusPhone.getFont().deriveFont(txtCusPhone.getFont().getSize() + 3f));
        txtCusPhone.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 156,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusPhone)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtCusPhone)
                                .addContainerGap()));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD,
                jLabel9.getFont().getSize() + 2));
        jLabel9.setText("Cần trả khách:");

        txtReturnTotal.setEditable(false);
        txtReturnTotal.setFont(txtReturnTotal.getFont().deriveFont(txtReturnTotal.getFont().getSize() + 3f));
        txtReturnTotal.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtReturnTotal)));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtReturnTotal)
                                .addContainerGap()));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 57, Short.MAX_VALUE));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD,
                jLabel2.getFont().getSize() + 2));
        jLabel2.setText("Tên khách hàng:");
        jLabel2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel2AncestorAdded(evt);
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtCusName.setEditable(false);
        txtCusName.setFont(txtCusName.getFont().deriveFont(txtCusName.getFont().getSize() + 3f));
        txtCusName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCusName, javax.swing.GroupLayout.PREFERRED_SIZE, 216,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtCusName)
                                .addContainerGap()));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() | java.awt.Font.BOLD,
                jLabel4.getFont().getSize() + 2));
        jLabel4.setText("Mã hóa đơn:");
        jLabel4.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel4AncestorAdded(evt);
            }

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtOrderId.setEditable(false);
        txtOrderId.setFont(txtOrderId.getFont().deriveFont(txtOrderId.getFont().getSize() + 3f));
        txtOrderId.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 216,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtOrderId)
                                .addContainerGap()));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD,
                jLabel5.getFont().getSize() + 2));
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
        txtEmpName.setFont(txtEmpName.getFont().deriveFont(txtEmpName.getFont().getSize() + 3f));
        txtEmpName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtEmpName)));
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtEmpName)
                                .addContainerGap()));

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
                pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnLeftLayout.createSequentialGroup()
                                .addContainerGap(62, Short.MAX_VALUE)
                                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnLeftLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 165,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(39, Short.MAX_VALUE)));
        pnLeftLayout.setVerticalGroup(
                pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnLeftLayout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86,
                                        Short.MAX_VALUE)
                                .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)));

        add(pnLeft, java.awt.BorderLayout.EAST);

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(590, 100));

        btnOpenModalAddUnit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnOpenModalAddUnit.setText("Tìm kiếm");
        btnOpenModalAddUnit.setMaximumSize(new java.awt.Dimension(150, 40));
        btnOpenModalAddUnit.setMinimumSize(new java.awt.Dimension(150, 40));
        btnOpenModalAddUnit.setPreferredSize(new java.awt.Dimension(150, 40));
        btnOpenModalAddUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenModalAddUnitActionPerformed(evt);
            }
        });

        txtSearchOrder.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchOrder.setMinimumSize(new java.awt.Dimension(300, 40));
        txtSearchOrder.setPreferredSize(new java.awt.Dimension(300, 40));
        txtSearchOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchOrderActionPerformed(evt);
            }
        });

        btnTraTatCa = new javax.swing.JButton();
        btnTraTatCa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTraTatCa.setText("Trả tất cả");
        btnTraTatCa.setMaximumSize(new java.awt.Dimension(180, 40));
        btnTraTatCa.setMinimumSize(new java.awt.Dimension(180, 40));
        btnTraTatCa.setPreferredSize(new java.awt.Dimension(180, 40));
        btnTraTatCa.setVisible(false);
        btnTraTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraTatCaActionPerformed(evt);
            }
        });

        btnNhapLyDoChoTatCa = new javax.swing.JButton();
        btnNhapLyDoChoTatCa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhapLyDoChoTatCa.setText("Nhập lý do cho tất cả");
        btnNhapLyDoChoTatCa.setMaximumSize(new java.awt.Dimension(200, 40));
        btnNhapLyDoChoTatCa.setMinimumSize(new java.awt.Dimension(200, 40));
        btnNhapLyDoChoTatCa.setPreferredSize(new java.awt.Dimension(200, 40));
        btnNhapLyDoChoTatCa.setVisible(false);
        btnNhapLyDoChoTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhapLyDoChoTatCaActionPerformed(evt);
            }
        });

        btnXoaTatCa = new javax.swing.JButton();
        btnXoaTatCa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoaTatCa.setText("Xóa tất cả");
        btnXoaTatCa.setMaximumSize(new java.awt.Dimension(180, 40));
        btnXoaTatCa.setMinimumSize(new java.awt.Dimension(180, 40));
        btnXoaTatCa.setPreferredSize(new java.awt.Dimension(180, 40));
        btnXoaTatCa.setVisible(false);
        btnXoaTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTatCaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtSearchOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 600,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnOpenModalAddUnit, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTraTatCa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnNhapLyDoChoTatCa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXoaTatCa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearchOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnOpenModalAddUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnTraTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnNhapLyDoChoTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnXoaTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))));

        headerPanel.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoPhieuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaoPhieuActionPerformed
        taoPhieuTraHang();
    }// GEN-LAST:event_btnTaoPhieuActionPerformed

    private void jLabel2AncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_jLabel2AncestorAdded
        // TODO add your handling code here:
    }// GEN-LAST:event_jLabel2AncestorAdded

    private void txtSearchOrderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSearchOrderActionPerformed
        // Xử lý tìm kiếm hóa đơn khi nhấn Enter
        String maDonHang = txtSearchOrder.getText().trim();
        if (!maDonHang.isEmpty()) {
            timKiemVaHienThiDonHang(maDonHang);
        } else {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Vui lòng nhập mã hóa đơn!");
        }
    }// GEN-LAST:event_txtSearchOrderActionPerformed

    private void btnOpenModalAddUnitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOpenModalAddUnitActionPerformed
        // Nút tìm kiếm
        String maDonHang = txtSearchOrder.getText().trim();
        if (!maDonHang.isEmpty()) {
            timKiemVaHienThiDonHang(maDonHang);
        } else {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Vui lòng nhập mã hóa đơn!");
        }
    }// GEN-LAST:event_btnOpenModalAddUnitActionPerformed

    private void jLabel4AncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_jLabel4AncestorAdded
        // TODO add your handling code here:
    }// GEN-LAST:event_jLabel4AncestorAdded

    private void jLabel5AncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_jLabel5AncestorAdded
        // TODO add your handling code here:
    }// GEN-LAST:event_jLabel5AncestorAdded

    private void btnTaoPhieuKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_btnTaoPhieuKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            taoPhieuTraHang();
        }
    }// GEN-LAST:event_btnTaoPhieuKeyPressed

    private void btnTraTatCaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTraTatCaActionPerformed
        // Kiểm tra xem có đơn hàng không
        if (currentDonHang == null) {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Vui lòng tìm kiếm hóa đơn trước!");
            return;
        }

        // Đếm số sản phẩm đã mua (bỏ qua header)
        int soLuongSanPham = 0;
        for (java.awt.Component comp : pnHaveOrder.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamDaMua) {
                soLuongSanPham++;
            }
        }

        if (soLuongSanPham == 0) {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Không có sản phẩm nào để trả!");
            return;
        }

        // Chuyển tất cả sản phẩm sang danh sách trả
        for (java.awt.Component comp : pnHaveOrder.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamDaMua) {
                Panel_ChiTietSanPhamDaMua productPanel = (Panel_ChiTietSanPhamDaMua) comp;
                onProductClicked(productPanel);
            }
        }

        // Hiện 2 nút sau khi bấm Trả tất cả
        btnNhapLyDoChoTatCa.setVisible(true);
        btnXoaTatCa.setVisible(true);

        raven.toast.Notifications.getInstance().show(
                raven.toast.Notifications.Type.SUCCESS,
                "Đã thêm tất cả " + soLuongSanPham + " sản phẩm vào danh sách trả!");
    }// GEN-LAST:event_btnTraTatCaActionPerformed

    private void btnNhapLyDoChoTatCaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNhapLyDoChoTatCaActionPerformed
        // Kiểm tra xem có sản phẩm trả không
        int soLuongSanPhamTra = 0;
        for (java.awt.Component comp : pnHaveReturn.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                soLuongSanPhamTra++;
            }
        }

        if (soLuongSanPhamTra == 0) {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Không có sản phẩm nào trong danh sách trả!");
            return;
        }

        // Hiển thị dialog để nhập lý do
        Dialog_NhapLyDoTraHang dialog = new Dialog_NhapLyDoTraHang(null, true);
        dialog.setVisible(true);

        // Lấy lý do từ dialog
        String lyDo = dialog.getLyDo();

        if (lyDo != null && !lyDo.trim().isEmpty()) {
            // Áp dụng lý do cho tất cả sản phẩm trả
            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                    panel.setLyDoTraHang(lyDo);
                }
            }

            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.SUCCESS,
                    "Đã áp dụng lý do cho tất cả " + soLuongSanPhamTra + " sản phẩm!");
        }
    }// GEN-LAST:event_btnNhapLyDoChoTatCaActionPerformed

    private void btnXoaTatCaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaTatCaActionPerformed
        // Kiểm tra xem có sản phẩm trả không
        int soLuongSanPhamTra = 0;
        for (java.awt.Component comp : pnHaveReturn.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                soLuongSanPhamTra++;
            }
        }

        if (soLuongSanPhamTra == 0) {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Không có sản phẩm nào trong danh sách trả!");
            return;
        }

        // Xác nhận xóa
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa tất cả " + soLuongSanPhamTra + " sản phẩm khỏi danh sách trả?",
                "Xác nhận xóa tất cả",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Xóa tất cả sản phẩm trả (giữ header)
            java.awt.Component headerReturn = pnHaveReturn.getComponent(0);
            pnHaveReturn.removeAll();
            pnHaveReturn.add(headerReturn);
            pnHaveReturn.revalidate();
            pnHaveReturn.repaint();

            // Cập nhật lại thông tin
            capNhatTongTienTra();

            // Ẩn 2 nút khi không còn sản phẩm
            btnNhapLyDoChoTatCa.setVisible(false);
            btnXoaTatCa.setVisible(false);

            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.SUCCESS,
                    "Đã xóa tất cả sản phẩm khỏi danh sách trả!");
        }
    }// GEN-LAST:event_btnXoaTatCaActionPerformed

    private vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang currentDonHang;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpenModalAddUnit;
    private javax.swing.JButton btnTaoPhieu;
    private javax.swing.JButton btnTraTatCa;
    private javax.swing.JButton btnNhapLyDoChoTatCa;
    private javax.swing.JButton btnXoaTatCa;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnHaveContent;
    private javax.swing.JPanel pnHaveOrder;
    private javax.swing.JPanel pnHaveReturn;
    private javax.swing.JPanel pnLeft;
    private javax.swing.JPanel pnMid;
    private javax.swing.JTextField txtCusName;
    private javax.swing.JTextField txtCusPhone;
    private javax.swing.JTextField txtEmpName;
    private javax.swing.JTextField txtOrderId;
    private javax.swing.JTextField txtReturnTotal;
    private javax.swing.JTextField txtSearchOrder;
    // End of variables declaration//GEN-END:variables

    private void addHeaderRowForOrder() {
        // Tạo panel header cho danh sách sản phẩm đã mua
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 245, 245));
        headerPanel
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        headerPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        headerPanel.setPreferredSize(new java.awt.Dimension(1000, 50));

        // Sử dụng GridBagLayout với constraints giống Panel_ChiTietSanPhamDaMua
        headerPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh - 80px
        javax.swing.JLabel lblHeaderImg = new javax.swing.JLabel("Hình ảnh");
        lblHeaderImg.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderImg.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderImg, gbc);

        // 2. Tên sản phẩm - 180px
        javax.swing.JLabel lblHeaderName = new javax.swing.JLabel("Tên sản phẩm");
        lblHeaderName.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderName.setPreferredSize(new java.awt.Dimension(180, 30));
        lblHeaderName.setMinimumSize(new java.awt.Dimension(180, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        headerPanel.add(lblHeaderName, gbc);

        // 3. Đơn vị - 80px
        javax.swing.JLabel lblHeaderUnit = new javax.swing.JLabel("Đơn vị");
        lblHeaderUnit.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderUnit.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderUnit.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderUnit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderUnit, gbc);

        // 4. Số lượng - 80px
        javax.swing.JLabel lblHeaderQuantity = new javax.swing.JLabel("Số lượng");
        lblHeaderQuantity.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderQuantity.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderQuantity.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderQuantity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderQuantity, gbc);

        // 5. Đơn giá - 100px
        javax.swing.JLabel lblHeaderPrice = new javax.swing.JLabel("Đơn giá");
        lblHeaderPrice.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderPrice, gbc);

        // 6. Tổng tiền - 120px
        javax.swing.JLabel lblHeaderTotal = new javax.swing.JLabel("Tổng tiền");
        lblHeaderTotal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderTotal.setPreferredSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setMinimumSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderTotal, gbc);

        // 7. Trạng thái - 50px
        javax.swing.JLabel lblHeaderStatus = new javax.swing.JLabel("Trạng thái");
        lblHeaderStatus.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderStatus.setPreferredSize(new java.awt.Dimension(50, 30));
        lblHeaderStatus.setMinimumSize(new java.awt.Dimension(50, 30));
        lblHeaderStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderStatus, gbc);

        pnHaveOrder.add(headerPanel);
    }

    private void addHeaderRowForReturn() {
        // Tạo panel header cho danh sách sản phẩm trả hàng
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 245, 245));
        headerPanel
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        headerPanel.setMinimumSize(new java.awt.Dimension(800, 50));
        headerPanel.setPreferredSize(new java.awt.Dimension(1000, 50));

        // Sử dụng GridBagLayout với constraints giống Panel_ChiTietSanPham
        headerPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh - 80px
        javax.swing.JLabel lblHeaderImg = new javax.swing.JLabel("Hình ảnh");
        lblHeaderImg.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderImg.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderImg, gbc);

        // 2. Tên sản phẩm - 180px
        javax.swing.JLabel lblHeaderName = new javax.swing.JLabel("Tên sản phẩm");
        lblHeaderName.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderName.setPreferredSize(new java.awt.Dimension(180, 30));
        lblHeaderName.setMinimumSize(new java.awt.Dimension(180, 30));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        headerPanel.add(lblHeaderName, gbc);

        // 3. Đơn vị - 80px
        javax.swing.JLabel lblHeaderUnit = new javax.swing.JLabel("Đơn vị");
        lblHeaderUnit.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderUnit.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderUnit.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderUnit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderUnit, gbc);

        // 4. Số lượng - 80px
        javax.swing.JLabel lblHeaderQuantity = new javax.swing.JLabel("Số lượng");
        lblHeaderQuantity.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderQuantity.setPreferredSize(new java.awt.Dimension(80, 30));
        lblHeaderQuantity.setMinimumSize(new java.awt.Dimension(80, 30));
        lblHeaderQuantity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderQuantity, gbc);

        // 5. Đơn giá - 100px
        javax.swing.JLabel lblHeaderPrice = new javax.swing.JLabel("Đơn giá");
        lblHeaderPrice.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setMinimumSize(new java.awt.Dimension(100, 30));
        lblHeaderPrice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderPrice, gbc);

        // 6. Tổng tiền trả - 120px
        javax.swing.JLabel lblHeaderTotal = new javax.swing.JLabel("Tổng tiền trả");
        lblHeaderTotal.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderTotal.setPreferredSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setMinimumSize(new java.awt.Dimension(120, 30));
        lblHeaderTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderTotal, gbc);

        // 7. Thao tác - 70px
        javax.swing.JLabel lblHeaderAction = new javax.swing.JLabel("Thao tác");
        lblHeaderAction.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblHeaderAction.setPreferredSize(new java.awt.Dimension(70, 30));
        lblHeaderAction.setMinimumSize(new java.awt.Dimension(70, 30));
        lblHeaderAction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        headerPanel.add(lblHeaderAction, gbc);

        pnHaveReturn.add(headerPanel);
    }

    private void onProductClicked(Panel_ChiTietSanPhamDaMua productPanel) {
        String tenSP = productPanel.getTenSanPham();
        String donVi = productPanel.getDonVi();
        int soLuongDaMua = productPanel.getSoLuong();

        System.out.println("DEBUG onProductClicked: Tên SP = '" + tenSP + "', Đơn vị = '" + donVi
                + "', Số lượng đã mua = " + soLuongDaMua);

        // Kiểm tra sản phẩm đã có trong danh sách trả hàng chưa
        for (java.awt.Component comp : pnHaveReturn.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                Panel_ChiTietSanPhamTraHang existingPanel = (Panel_ChiTietSanPhamTraHang) comp;
                if (existingPanel.getTenSanPham().equals(tenSP)) {
                    // Sản phẩm đã tồn tại trong danh sách trả hàng
                    // Hiển thị thông báo
                    raven.toast.Notifications.getInstance().show(
                            raven.toast.Notifications.Type.ERROR,
                            raven.toast.Notifications.Location.TOP_CENTER,
                            3000,
                            "Sản phẩm '" + tenSP + "' đã có trong danh sách trả hàng!");

                    // Làm panel chớp màu đỏ để người dùng dễ nhận biết
                    highlightDuplicatePanel(existingPanel);
                    return; // Dừng lại, không thêm sản phẩm
                }
            }
        }

        // Tạo panel trả hàng từ sản phẩm đã mua
        Panel_ChiTietSanPhamTraHang returnProduct = new Panel_ChiTietSanPhamTraHang();

        // Copy thông tin từ sản phẩm đã mua
        returnProduct.setTenSanPham(tenSP);
        returnProduct.setDonVi(donVi);
        returnProduct.setSoLuongToiDa(soLuongDaMua); // Set số lượng tối đa trước
        returnProduct.setSoLuongTra(soLuongDaMua); // Mặc định trả hết
        returnProduct.setDonGia(productPanel.getDonGia());

        // Copy hình ảnh nếu có - tìm sản phẩm từ database để lấy đường dẫn hình ảnh
        try {
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO sanPhamDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO();
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham> dsSanPham = sanPhamDAO.findAll();

            boolean found = false;
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham sp : dsSanPham) {
                if (sp.getTenSanPham().equals(tenSP)) {
                    String hinhAnh = sp.getHinhAnh();
                    System.out.println("DEBUG: Tìm thấy SP trong DB - Hình: " + hinhAnh);
                    if (hinhAnh != null && !hinhAnh.isEmpty()) {
                        // Chuyển đổi đường dẫn tương đối thành đường dẫn đầy đủ
                        String fullPath = "src/main/resources/img/" + hinhAnh;
                        returnProduct.setHinhAnh(fullPath);
                        System.out.println("DEBUG: Đã set hình cho panel trả hàng với đường dẫn: " + fullPath);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("DEBUG: KHÔNG tìm thấy sản phẩm '" + tenSP + "' trong DB");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load hình ảnh: " + e.getMessage());
            e.printStackTrace();
        }

        // Thiết lập listener để cập nhật tổng tiền khi xóa
        returnProduct.setDeleteListener(() -> capNhatTongTienTra());

        // Thêm vào danh sách trả hàng
        pnHaveReturn.add(returnProduct);

        // Thêm spacing giữa các sản phẩm
        pnHaveReturn.add(javax.swing.Box.createVerticalStrut(5));

        pnHaveReturn.revalidate();
        pnHaveReturn.repaint();

        // Scroll đến cuối danh sách
        javax.swing.SwingUtilities.invokeLater(() -> {
            java.awt.Rectangle bounds = returnProduct.getBounds();
            jScrollPane3.getViewport().scrollRectToVisible(bounds);
        });

        // Có thể thêm hiệu ứng visual để người dùng biết đã chọn
        productPanel.setBackground(new java.awt.Color(200, 255, 200)); // Màu xanh nhạt
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            productPanel.setBackground(new java.awt.Color(255, 255, 255));
        });
        timer.setRepeats(false);
        timer.start();

        // Cập nhật tổng tiền trả
        capNhatTongTienTra();
    }

    /**
     * Tìm kiếm và hiển thị đơn hàng theo mã
     */
    private void timKiemVaHienThiDonHang(String maDonHang) {
        try {
            // Tìm đơn hàng từ database
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO donHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO();

            java.util.Optional<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang> optDonHang = donHangDAO
                    .findById(maDonHang);

            if (!optDonHang.isPresent()) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Không tìm thấy hóa đơn: " + maDonHang);
                // Ẩn 3 nút
                btnTraTatCa.setVisible(false);
                btnNhapLyDoChoTatCa.setVisible(false);
                btnXoaTatCa.setVisible(false);
                return;
            }

            currentDonHang = optDonHang.get();

            // Kiểm tra xem đơn hàng này đã có đơn trả chưa
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonTraHangDAO donTraHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonTraHangDAO();

            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang> danhSachDonTra = donTraHangDAO
                    .findByDonHang(maDonHang);

            // Kiểm tra trạng thái đơn trả
            boolean daTonTaiDonTraChuaXuLy = false;
            boolean daTonTaiDonTraDaXuLy = false;

            if (danhSachDonTra != null && !danhSachDonTra.isEmpty()) {
                for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTra : danhSachDonTra) {
                    String trangThai = donTra.getTrangThaiXuLy();
                    if (trangThai != null && trangThai.equals("Đã xử lý")) {
                        daTonTaiDonTraDaXuLy = true;
                        break;
                    } else if (trangThai == null || trangThai.equals("Chưa xử lý")) {
                        daTonTaiDonTraChuaXuLy = true;
                    }
                }
            }

            // Nếu đơn hàng đã được tạo phiếu trả và đã xử lý
            if (daTonTaiDonTraDaXuLy) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.INFO,
                        raven.toast.Notifications.Location.TOP_CENTER,
                        "Đơn hàng này đã được tạo phiếu trả và đã được xử lý bởi quản lý!");
                // Reset form
                txtOrderId.setText("");
                txtCusName.setText("");
                txtCusPhone.setText("");
                txtReturnTotal.setText("0");
                currentDonHang = null;

                // Ẩn 3 nút
                btnTraTatCa.setVisible(false);
                btnNhapLyDoChoTatCa.setVisible(false);
                btnXoaTatCa.setVisible(false);

                // Xóa danh sách sản phẩm
                java.awt.Component header = pnHaveOrder.getComponent(0);
                pnHaveOrder.removeAll();
                pnHaveOrder.add(header);
                pnHaveOrder.revalidate();
                pnHaveOrder.repaint();

                java.awt.Component headerReturn = pnHaveReturn.getComponent(0);
                pnHaveReturn.removeAll();
                pnHaveReturn.add(headerReturn);
                pnHaveReturn.revalidate();
                pnHaveReturn.repaint();

                return;
            }

            // Nếu đơn hàng đã được tạo phiếu trả nhưng chưa xử lý
            if (daTonTaiDonTraChuaXuLy) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.WARNING,
                        raven.toast.Notifications.Location.TOP_CENTER,
                        "Đơn hàng này đã được tạo phiếu trả nhưng chưa được xử lý bởi quản lý!");
                // Reset form
                txtOrderId.setText("");
                txtCusName.setText("");
                txtCusPhone.setText("");
                txtReturnTotal.setText("0");
                currentDonHang = null;

                // Ẩn 3 nút
                btnTraTatCa.setVisible(false);
                btnNhapLyDoChoTatCa.setVisible(false);
                btnXoaTatCa.setVisible(false);

                // Xóa danh sách sản phẩm
                java.awt.Component header = pnHaveOrder.getComponent(0);
                pnHaveOrder.removeAll();
                pnHaveOrder.add(header);
                pnHaveOrder.revalidate();
                pnHaveOrder.repaint();

                java.awt.Component headerReturn = pnHaveReturn.getComponent(0);
                pnHaveReturn.removeAll();
                pnHaveReturn.add(headerReturn);
                pnHaveReturn.revalidate();
                pnHaveReturn.repaint();

                return;
            }

            // Hiển thị thông tin đơn hàng
            txtOrderId.setText(currentDonHang.getMaDonHang());

            // Hiển thị thông tin khách hàng
            if (currentDonHang.getKhachHang() != null) {
                String tenKH = currentDonHang.getKhachHang().getTenKhachHang();
                String sdtKH = currentDonHang.getKhachHang().getSoDienThoai();
                txtCusName.setText(tenKH != null && !tenKH.trim().isEmpty() ? tenKH : "Khách vãng lai");
                txtCusPhone.setText(sdtKH != null && !sdtKH.trim().isEmpty() ? sdtKH : "N/A");
                System.out.println("DEBUG: Khách hàng - Tên: " + tenKH + ", SĐT: " + sdtKH);
            } else {
                txtCusName.setText("Khách vãng lai");
                txtCusPhone.setText("N/A");
                System.out.println("DEBUG: Không có thông tin khách hàng");
            }

            // Hiển thị tên nhân viên hiện tại
            vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager sessionManager = vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager
                    .getInstance();
            if (sessionManager.getCurrentUser() != null) {
                String tenNV = sessionManager.getCurrentUser().getTenNhanVien();
                String sdtNV = sessionManager.getCurrentUser().getSoDienThoai();
                txtEmpName.setText(tenNV);
                System.out.println("DEBUG: Nhân viên lập: " + tenNV + " - SĐT: " + sdtNV);
            } else {
                System.out.println("DEBUG: Không có thông tin nhân viên trong session");
            }

            // Load chi tiết đơn hàng
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO chiTietDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO();

            java.util.ArrayList<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang> chiTietList = chiTietDAO
                    .findByIdList(maDonHang);

            // Xóa các sản phẩm cũ (giữ lại header)
            java.awt.Component header = pnHaveOrder.getComponent(0);
            pnHaveOrder.removeAll();
            pnHaveOrder.add(header);

            // Xóa các sản phẩm trả cũ (giữ lại header)
            java.awt.Component headerReturn = pnHaveReturn.getComponent(0);
            pnHaveReturn.removeAll();
            pnHaveReturn.add(headerReturn);

            // Hiển thị các sản phẩm đã mua
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang chiTiet : chiTietList) {
                Panel_ChiTietSanPhamDaMua productPanel = new Panel_ChiTietSanPhamDaMua();

                vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang loHang = chiTiet.getLoHang();
                vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham sanPham = loHang != null ? loHang.getSanPham()
                        : null;

                if (sanPham != null) {
                    productPanel.setTenSanPham(sanPham.getTenSanPham());
                    productPanel
                            .setDonVi(sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getTenDonVi() : "N/A");
                    productPanel.setSoLuong(chiTiet.getSoLuong());
                    productPanel.setDonGia(chiTiet.getDonGia());
                    productPanel.setProductClickListener(this::onProductClicked);

                    // Thiết lập hình ảnh sản phẩm
                    String hinhAnh = sanPham.getHinhAnh();
                    System.out
                            .println("DEBUG: Sản phẩm '" + sanPham.getTenSanPham() + "' - Đường dẫn hình: " + hinhAnh);
                    if (hinhAnh != null && !hinhAnh.isEmpty()) {
                        // Chuyển đổi đường dẫn tương đối thành đường dẫn đầy đủ
                        String fullPath = "src/main/resources/img/" + hinhAnh;
                        productPanel.setHinhAnh(fullPath);
                        System.out.println("DEBUG: Đã gọi setHinhAnh() với đường dẫn: " + fullPath);
                    } else {
                        System.out.println("DEBUG: Không có hình ảnh cho sản phẩm này");
                    }

                    pnHaveOrder.add(productPanel);
                }
            }

            pnHaveOrder.revalidate();
            pnHaveOrder.repaint();
            pnHaveReturn.revalidate();
            pnHaveReturn.repaint();

            // Reset tổng tiền
            txtReturnTotal.setText("0 ₫");

            // Hiển thị nút Trả tất cả khi tìm kiếm thành công (2 nút còn lại sẽ hiện sau
            // khi bấm Trả tất cả)
            btnTraTatCa.setVisible(true);
            btnNhapLyDoChoTatCa.setVisible(false);
            btnXoaTatCa.setVisible(false);

            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.SUCCESS,
                    "Tải hóa đơn thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.ERROR,
                    "Lỗi khi tải hóa đơn: " + e.getMessage());
        }
    }

    /**
     * Cập nhật tổng tiền trả
     */
    private void capNhatTongTienTra() {
        double tongTien = 0;

        // Duyệt qua tất cả các panel trả hàng
        for (java.awt.Component comp : pnHaveReturn.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                try {
                    int soLuong = panel.getSoLuongTra();
                    double donGia = panel.getDonGia();
                    tongTien += soLuong * donGia;
                } catch (Exception e) {
                    System.err.println("Error calculating return total: " + e.getMessage());
                }
            }
        }

        txtReturnTotal.setText(String.format("%,.0f ₫", tongTien));
    }

    /**
     * Tạo phiếu trả hàng và xem trước PDF
     */
    private void taoPhieuTraHang() {
        try {
            // Kiểm tra đã có đơn hàng chưa
            if (currentDonHang == null) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.WARNING,
                        "Vui lòng tìm kiếm hóa đơn trước!");
                return;
            }

            // Kiểm tra có sản phẩm trả không
            boolean coSanPhamTra = false;
            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    coSanPhamTra = true;
                    break;
                }
            }

            if (!coSanPhamTra) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.WARNING,
                        "Vui lòng chọn ít nhất một sản phẩm để trả!");
                return;
            }

            // Kiểm tra tất cả sản phẩm trả đều có lý do
            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                    if (panel.getLyDoTraHang() == null || panel.getLyDoTraHang().trim().isEmpty()) {
                        raven.toast.Notifications.getInstance().show(
                                raven.toast.Notifications.Type.WARNING,
                                "Vui lòng nhập lý do trả cho tất cả sản phẩm!");
                        return;
                    }
                }
            }

            // Tạo đơn trả hàng
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang = new vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang();

            // Tạo mã đơn trả hàng mới
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonTraHangDAO donTraHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonTraHangDAO();

            // Tính tổng tiền
            double tongTien = 0;
            String tongTienStr = txtReturnTotal.getText().replaceAll("[^0-9]", "");
            if (!tongTienStr.isEmpty()) {
                tongTien = Double.parseDouble(tongTienStr);
            }

            donTraHang.setNgayTraHang(java.time.LocalDate.now());
            donTraHang.setThanhTien(tongTien);
            donTraHang.setDonHang(currentDonHang);
            donTraHang.setNhanVien(
                    vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager.getInstance().getCurrentUser());

            // Lưu đơn trả hàng vào database
            boolean insertSuccess = donTraHangDAO.insert(donTraHang);

            if (!insertSuccess) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Lỗi khi lưu đơn trả hàng vào database!");
                return;
            }

            // Debug: Kiểm tra mã đơn trả đã được tạo
            System.out.println("=== DEBUG: Đơn trả hàng đã lưu ===");
            System.out.println("Mã đơn trả: " + donTraHang.getMaDonTraHang());
            System.out.println("Ngày trả: " + donTraHang.getNgayTraHang());
            System.out.println("Thành tiền: " + donTraHang.getThanhTien());
            System.out.println("===================================");

            // Kiểm tra mã đơn trả hàng đã được tạo chưa
            if (donTraHang.getMaDonTraHang() == null || donTraHang.getMaDonTraHang().trim().isEmpty()) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Không thể tạo mã đơn trả hàng!");
                return;
            }

            // Tạo danh sách chi tiết đơn trả hàng
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> chiTietList = new java.util.ArrayList<>();

            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO sanPhamDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO();

            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;

                    // Tìm sản phẩm từ tên
                    String tenSanPham = panel.getTenSanPham();
                    java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham> dsSanPham = sanPhamDAO
                            .findAll();

                    vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham sanPham = null;
                    for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham sp : dsSanPham) {
                        if (sp.getTenSanPham().equals(tenSanPham)) {
                            sanPham = sp;
                            break;
                        }
                    }

                    if (sanPham != null) {
                        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet = new vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang();

                        chiTiet.setSoLuong(panel.getSoLuongTra());
                        chiTiet.setDonGia(panel.getDonGia());
                        chiTiet.setLyDoTra(panel.getLyDoTraHang());
                        chiTiet.setThanhTien(panel.getSoLuongTra() * panel.getDonGia());
                        chiTiet.setSanPham(sanPham);
                        chiTiet.setDonTraHang(donTraHang);

                        chiTietList.add(chiTiet);
                    }
                }
            }

            // Lưu chi tiết đơn trả hàng
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonTraHangDAO chiTietDonTraHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonTraHangDAO();

            System.out.println("=== DEBUG: Lưu chi tiết đơn trả hàng ===");
            System.out.println("Số lượng chi tiết cần lưu: " + chiTietList.size());

            int successCount = 0;
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : chiTietList) {
                System.out.println("  - Đang lưu chi tiết: " + chiTiet.getSanPham().getTenSanPham());
                System.out.println("    Mã đơn trả: " + chiTiet.getDonTraHang().getMaDonTraHang());
                System.out.println("    Mã sản phẩm: " + chiTiet.getSanPham().getMaSanPham());
                System.out.println("    Số lượng: " + chiTiet.getSoLuong());
                System.out.println("    Đơn giá: " + chiTiet.getDonGia());
                System.out.println("    Lý do: " + chiTiet.getLyDoTra());

                boolean success = chiTietDonTraHangDAO.insert(chiTiet);
                if (success) {
                    successCount++;
                    System.out.println("    ✓ Lưu thành công!");
                } else {
                    System.out.println("    ✗ Lưu THẤT BẠI!");
                }
            }

            System.out.println("Kết quả: " + successCount + "/" + chiTietList.size() + " chi tiết được lưu thành công");
            System.out.println("==========================================");

            if (successCount == 0) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Không thể lưu chi tiết đơn trả hàng! Vui lòng kiểm tra lại.");
                return;
            } else if (successCount < chiTietList.size()) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.WARNING,
                        "Chỉ lưu được " + successCount + "/" + chiTietList.size() + " chi tiết!");
            }

            // Hiển thị hóa đơn trả hàng (không xuất PDF nữa)
            hienThiHoaDonTraHang(donTraHang, chiTietList);

            // Thông báo thành công
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.SUCCESS,
                    "Tạo phiếu trả hàng thành công! Mã: " + donTraHang.getMaDonTraHang());

            // Reset form
            resetForm();

        } catch (Exception e) {
            e.printStackTrace();
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.ERROR,
                    "Lỗi khi tạo phiếu trả hàng: " + e.getMessage());
        }
    }

    /**
     * Hiển thị preview hóa đơn trả hàng
     */
    private void hienThiHoaDonTraHang(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang,
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> danhSachChiTiet) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa đơn trả hàng");
        dialog.setModal(true);
        dialog.setSize(900, 650);
        dialog.setLocationRelativeTo(null); // Hiển thị giữa màn hình

        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout(10, 10));
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Format cho số tiền và ngày tháng
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,###");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");

        // === HEADER ===
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));
        headerPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("HÓA ĐƠN TRẢ HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        headerPanel.add(javax.swing.Box.createVerticalStrut(20));

        // Thông tin đơn trả hàng
        javax.swing.JPanel infoPanel = new javax.swing.JPanel(new java.awt.GridLayout(6, 2, 10, 8));
        infoPanel.setBackground(java.awt.Color.WHITE);

        infoPanel.add(createInfoLabel("Mã hóa đơn: ", donTraHang.getMaDonTraHang(), true));

        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien nhanVien = donTraHang.getNhanVien();
        String tenNhanVien = nhanVien != null ? nhanVien.getTenNhanVien() + " (" + nhanVien.getMaNhanVien() + ")"
                : "N/A";
        infoPanel.add(createInfoLabel("Nhân viên: ", tenNhanVien, false));

        // SĐT nhân viên
        String sdtNhanVien = nhanVien != null && nhanVien.getSoDienThoai() != null ? nhanVien.getSoDienThoai() : "N/A";
        infoPanel.add(createInfoLabel("SĐT NV: ", sdtNhanVien, false));

        String ngayTra = dateFormat.format(java.sql.Date.valueOf(donTraHang.getNgayTraHang()));
        infoPanel.add(createInfoLabel("Ngày trả: ", ngayTra, false));

        // Ngày đặt hàng gốc
        String ngayDat = currentDonHang != null && currentDonHang.getNgayDatHang() != null
                ? dateFormat.format(java.sql.Date.valueOf(currentDonHang.getNgayDatHang()))
                : "N/A";
        infoPanel.add(createInfoLabel("Ngày đặt: ", ngayDat, false));

        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang khachHang = currentDonHang != null
                ? currentDonHang.getKhachHang()
                : null;
        String tenKhachHang = khachHang != null ? khachHang.getTenKhachHang() : "Khách vãng lai";
        infoPanel.add(createInfoLabel("Khách hàng: ", tenKhachHang, true));

        // SĐT khách hàng
        String sdtKhachHang = khachHang != null && khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai()
                : "N/A";
        infoPanel.add(createInfoLabel("SĐT: ", sdtKhachHang, false));

        String phuongThuc = currentDonHang != null && currentDonHang.getPhuongThucThanhToan() != null
                ? currentDonHang.getPhuongThucThanhToan().toString()
                : "TIEN_MAT";
        infoPanel.add(createInfoLabel("Phương thức: ", phuongThuc, false));

        headerPanel.add(infoPanel);
        headerPanel.add(javax.swing.Box.createVerticalStrut(10));

        // Đường phân cách
        javax.swing.JSeparator separator1 = new javax.swing.JSeparator();
        headerPanel.add(separator1);

        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        // === BODY - Bảng chi tiết sản phẩm ===
        javax.swing.JPanel bodyPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        bodyPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblChiTiet = new javax.swing.JLabel("Chi tiết sản phẩm");
        lblChiTiet.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblChiTiet.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bodyPanel.add(lblChiTiet, java.awt.BorderLayout.NORTH);

        String[] columnNames = { "STT", "Tên sản phẩm", "Đơn vị", "Số lượng", "Đơn giá", "Thành tiền" };
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        table.getTableHeader().setBackground(new java.awt.Color(240, 240, 240));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(300); // Tên SP
        table.getColumnModel().getColumn(2).setPreferredWidth(80); // Đơn vị
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // Số lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Đơn giá
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Thành tiền

        // Thêm dữ liệu vào bảng
        int stt = 1;
        for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : danhSachChiTiet) {
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham sanPham = chiTiet.getSanPham();

            tableModel.addRow(new Object[] {
                    stt++,
                    sanPham.getTenSanPham(),
                    sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getTenDonVi() : "Tuýp",
                    chiTiet.getSoLuong(),
                    currencyFormat.format(chiTiet.getDonGia()) + " đ",
                    currencyFormat.format(chiTiet.getThanhTien()) + " đ"
            });
        }

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        bodyPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(bodyPanel, java.awt.BorderLayout.CENTER);

        // === FOOTER - Tổng tiền ===
        javax.swing.JPanel footerPanel = new javax.swing.JPanel();
        footerPanel.setLayout(new javax.swing.BoxLayout(footerPanel, javax.swing.BoxLayout.Y_AXIS));
        footerPanel.setBackground(java.awt.Color.WHITE);
        footerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 0, 0));

        javax.swing.JSeparator separator2 = new javax.swing.JSeparator();
        footerPanel.add(separator2);
        footerPanel.add(javax.swing.Box.createVerticalStrut(15));

        // Thông tin khuyến mãi (nếu có)
        if (currentDonHang != null && currentDonHang.getKhuyenMai() != null) {
            javax.swing.JPanel kmPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            kmPanel.setBackground(java.awt.Color.WHITE);

            javax.swing.JLabel lblKMText = new javax.swing.JLabel("Khuyến mãi: ");
            lblKMText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

            javax.swing.JLabel lblKM = new javax.swing.JLabel(currentDonHang.getKhuyenMai().getTenKhuyenMai());
            lblKM.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            lblKM.setForeground(new java.awt.Color(255, 102, 0));

            kmPanel.add(lblKMText);
            kmPanel.add(lblKM);
            footerPanel.add(kmPanel);
        }

        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTongTienText = new javax.swing.JLabel("Tổng hóa đơn: ");
        lblTongTienText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        javax.swing.JLabel lblTongTien = new javax.swing.JLabel(
                currencyFormat.format(donTraHang.getThanhTien()) + " đ");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        lblTongTien.setForeground(new java.awt.Color(220, 53, 69));

        tongTienPanel.add(lblTongTienText);
        tongTienPanel.add(lblTongTien);

        footerPanel.add(tongTienPanel);

        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Tạo JPanel với label thông tin
     */
    private javax.swing.JPanel createInfoLabel(String title, String value, boolean bold) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        panel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel(title);
        lblTitle.setFont(new java.awt.Font("Segoe UI",
                bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 14));

        javax.swing.JLabel lblValue = new javax.swing.JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        panel.add(lblTitle);
        panel.add(lblValue);

        return panel;
    }

    /**
     * Xem trước PDF
     */
    private void xemTruocPDF(String pdfPath) {
        try {
            java.io.File file = new java.io.File(pdfPath);
            if (file.exists()) {
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(file);
                } else {
                    raven.toast.Notifications.getInstance().show(
                            raven.toast.Notifications.Type.INFO,
                            "PDF đã được lưu tại: " + pdfPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    "Không thể mở PDF. File đã được lưu tại: " + pdfPath);
        }
    }

    /**
     * Reset form sau khi tạo phiếu thành công
     */
    private void resetForm() {
        txtSearchOrder.setText("");
        txtOrderId.setText("");
        txtCusName.setText("");
        txtCusPhone.setText("");
        txtReturnTotal.setText("0 ₫");

        // Ẩn 3 nút
        btnTraTatCa.setVisible(false);
        btnNhapLyDoChoTatCa.setVisible(false);
        btnXoaTatCa.setVisible(false);

        // Xóa sản phẩm đã mua (giữ header)
        java.awt.Component headerOrder = pnHaveOrder.getComponent(0);
        pnHaveOrder.removeAll();
        pnHaveOrder.add(headerOrder);
        pnHaveOrder.revalidate();
        pnHaveOrder.repaint();

        // Xóa sản phẩm trả (giữ header)
        java.awt.Component headerReturn = pnHaveReturn.getComponent(0);
        pnHaveReturn.removeAll();
        pnHaveReturn.add(headerReturn);
        pnHaveReturn.revalidate();
        pnHaveReturn.repaint();

        currentDonHang = null;
    }

    /**
     * Làm panel chớp màu đỏ và scroll đến vị trí panel để người dùng biết sản phẩm
     * đã tồn tại trong danh sách trả hàng
     */
    private void highlightDuplicatePanel(Panel_ChiTietSanPhamTraHang panel) {
        // Scroll đến vị trí panel trước khi chớp
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Lấy vị trí của panel
            java.awt.Rectangle bounds = panel.getBounds();

            // Scroll để panel hiển thị ở giữa viewport (nếu có thể)
            java.awt.Rectangle visibleRect = jScrollPane3.getViewport().getViewRect();
            int centerY = bounds.y - (visibleRect.height / 2) + (bounds.height / 2);

            // Đảm bảo không scroll quá đầu hoặc cuối
            centerY = Math.max(0, centerY);
            int maxY = pnHaveReturn.getHeight() - visibleRect.height;
            centerY = Math.min(centerY, Math.max(0, maxY));

            // Scroll mượt đến vị trí
            jScrollPane3.getViewport().setViewPosition(new java.awt.Point(0, centerY));
        });

        // Lưu màu nền gốc
        java.awt.Color originalColor = panel.getBackground();

        // Màu đỏ cảnh báo
        java.awt.Color highlightColor = new java.awt.Color(255, 82, 82); // Đỏ tươi

        // Tạo Timer để chớp 2 lần (delay 100ms để scroll xong mới chớp)
        javax.swing.Timer timer = new javax.swing.Timer(200, null);
        final int[] blinkCount = { 0 };

        timer.addActionListener(e -> {
            if (blinkCount[0] < 4) { // 4 lần = 2 lần chớp (bật-tắt-bật-tắt)
                if (blinkCount[0] % 2 == 0) {
                    // Lần chẵn: đổi sang màu đỏ
                    panel.setBackground(highlightColor);
                } else {
                    // Lần lẻ: trở về màu gốc
                    panel.setBackground(originalColor);
                }
                blinkCount[0]++;
            } else {
                // Kết thúc animation, đảm bảo về màu gốc
                panel.setBackground(originalColor);
                timer.stop();
            }
        });

        // Delay 100ms để scroll xong mới chớp
        javax.swing.Timer startTimer = new javax.swing.Timer(100, e -> {
            timer.start();
        });
        startTimer.setRepeats(false);
        startTimer.start();
    }

}

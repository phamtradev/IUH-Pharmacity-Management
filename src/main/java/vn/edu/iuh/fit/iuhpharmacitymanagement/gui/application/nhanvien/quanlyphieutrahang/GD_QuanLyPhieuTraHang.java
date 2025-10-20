/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.JTextField;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyPhieuTraHang extends javax.swing.JPanel {

    public GD_QuanLyPhieuTraHang() {
        initComponents();

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
        
        // Add sample purchased products
        addSamplePurchasedProducts();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() | java.awt.Font.BOLD, jLabel3.getFont().getSize()+2));
        jLabel3.setText("Số điện thoại:");

        txtCusPhone.setEditable(false);
        txtCusPhone.setFont(txtCusPhone.getFont().deriveFont(txtCusPhone.getFont().getSize()+3f));
        txtCusPhone.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCusPhone))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCusPhone)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+2));
        jLabel9.setText("Cần trả khách:");

        txtReturnTotal.setEditable(false);
        txtReturnTotal.setFont(txtReturnTotal.getFont().deriveFont(txtReturnTotal.getFont().getSize()+3f));
        txtReturnTotal.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtReturnTotal))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtReturnTotal)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD, jLabel2.getFont().getSize()+2));
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
        txtCusName.setFont(txtCusName.getFont().deriveFont(txtCusName.getFont().getSize()+3f));
        txtCusName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCusName, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCusName)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() | java.awt.Font.BOLD, jLabel4.getFont().getSize()+2));
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
        txtOrderId.setFont(txtOrderId.getFont().deriveFont(txtOrderId.getFont().getSize()+3f));
        txtOrderId.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtOrderId)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize()+2));
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
        txtEmpName.setFont(txtEmpName.getFont().deriveFont(txtEmpName.getFont().getSize()+3f));
        txtEmpName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEmpName))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtEmpName)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(btnTaoPhieu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        add(pnLeft, java.awt.BorderLayout.EAST);

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(590, 100));

        btnOpenModalAddUnit.setBackground(new java.awt.Color(115, 165, 71));
        btnOpenModalAddUnit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnOpenModalAddUnit.setForeground(new java.awt.Color(255, 255, 255));
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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(txtSearchOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 926, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnOpenModalAddUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpenModalAddUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        headerPanel.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoPhieuActionPerformed
        createOrder();
    }//GEN-LAST:event_btnTaoPhieuActionPerformed

    private void createOrder() {

    }

    private void setTxtEmpty(JTextField... textFields) {

    }

    private void clearPnOrderDetail() {

    }

    private void jLabel2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2AncestorAdded

    private void txtSearchOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchOrderActionPerformed

    }//GEN-LAST:event_txtSearchOrderActionPerformed

    private void btnOpenModalAddUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenModalAddUnitActionPerformed

    }//GEN-LAST:event_btnOpenModalAddUnitActionPerformed

    private void jLabel4AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel4AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4AncestorAdded

    private void jLabel5AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel5AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5AncestorAdded

    private void btnTaoPhieuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTaoPhieuKeyPressed

    }//GEN-LAST:event_btnTaoPhieuKeyPressed
    private double tongTienHang;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpenModalAddUnit;
    private javax.swing.JButton btnTaoPhieu;
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
    
    // Thêm các biến để lưu trữ dữ liệu
    private java.util.List<Object> orderItems = new java.util.ArrayList<>();
    private java.util.List<Object> returnItems = new java.util.ArrayList<>();
    
    // Method để thêm sản phẩm vào danh sách trả hàng
    private void addItemToReturn(Object item) {
        returnItems.add(item);
        updateReturnPanel();
    }
    
    // Method để cập nhật panel trả hàng
    private void updateReturnPanel() {
        // Lưu lại header (component đầu tiên)
        java.awt.Component header = null;
        if (pnHaveReturn.getComponentCount() > 0) {
            header = pnHaveReturn.getComponent(0);
        }
        
        pnHaveReturn.removeAll();
        
        // Thêm lại header nếu có
        if (header != null) {
            pnHaveReturn.add(header);
        }
        
        for (Object item : returnItems) {
            // Tạo component hiển thị sản phẩm cần trả
            // Bạn có thể tùy chỉnh component này theo ý muốn
        }
        pnHaveReturn.revalidate();
        pnHaveReturn.repaint();
    }
    
    // Method để load sản phẩm từ hóa đơn
    private void loadOrderItems(String orderId) {
        // Load sản phẩm từ database dựa trên orderId
        // orderItems.clear();
        // ... code load từ database
        updateOrderPanel();
    }
    
    // Method để cập nhật panel hóa đơn
    private void updateOrderPanel() {
        pnHaveOrder.removeAll();
        for (Object item : orderItems) {
            // Tạo component hiển thị sản phẩm đã mua với click listener
            // Bạn có thể tùy chỉnh component này theo ý muốn
        }
        pnHaveOrder.revalidate();
        pnHaveOrder.repaint();
    }

    private void addHeaderRowForOrder() {
        // Tạo panel header cho danh sách sản phẩm đã mua
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setBackground(new java.awt.Color(245, 245, 245));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
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
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
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

    private void addSamplePurchasedProducts() {
        // Thêm các sản phẩm mẫu đã mua
        Panel_ChiTietSanPhamDaMua product1 = new Panel_ChiTietSanPhamDaMua();
        product1.setTenSanPham("Terpinzoat");
        product1.setDonVi("Hộp");
        product1.setSoLuong(3);
        product1.setDonGia(16500);
        product1.setProductClickListener(this::onProductClicked);
        pnHaveOrder.add(product1);

        Panel_ChiTietSanPhamDaMua product2 = new Panel_ChiTietSanPhamDaMua();
        product2.setTenSanPham("Methorphan");
        product2.setDonVi("Hộp");
        product2.setSoLuong(3);
        product2.setDonGia(27500);
        product2.setProductClickListener(this::onProductClicked);
        pnHaveOrder.add(product2);

        Panel_ChiTietSanPhamDaMua product3 = new Panel_ChiTietSanPhamDaMua();
        product3.setTenSanPham("Povidine");
        product3.setDonVi("Lọ");
        product3.setSoLuong(1);
        product3.setDonGia(13200);
        product3.setProductClickListener(this::onProductClicked);
        pnHaveOrder.add(product3);

        pnHaveOrder.revalidate();
        pnHaveOrder.repaint();
    }

    private void onProductClicked(Panel_ChiTietSanPhamDaMua productPanel) {
        // Tạo panel trả hàng từ sản phẩm đã mua
        Panel_ChiTietSanPhamTraHang returnProduct = new Panel_ChiTietSanPhamTraHang();
        
        // Copy thông tin từ sản phẩm đã mua
        returnProduct.setTenSanPham(productPanel.getTenSanPham());
        returnProduct.setDonVi(productPanel.getDonVi());
        returnProduct.setSoLuongTra(productPanel.getSoLuong()); // Mặc định trả hết
        returnProduct.setDonGia(productPanel.getDonGia());
        
        // Debug: In ra console để kiểm tra
        System.out.println("Adding product to return: " + productPanel.getTenSanPham());
        System.out.println("Current components in pnHaveReturn: " + pnHaveReturn.getComponentCount());
        
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
        
        // Debug: Kiểm tra sau khi thêm
        System.out.println("After adding - components in pnHaveReturn: " + pnHaveReturn.getComponentCount());
        
        // Có thể thêm hiệu ứng visual để người dùng biết đã chọn
        productPanel.setBackground(new java.awt.Color(200, 255, 200)); // Màu xanh nhạt
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            productPanel.setBackground(new java.awt.Color(255, 255, 255));
        });
        timer.setRepeats(false);
        timer.start();
    }

}

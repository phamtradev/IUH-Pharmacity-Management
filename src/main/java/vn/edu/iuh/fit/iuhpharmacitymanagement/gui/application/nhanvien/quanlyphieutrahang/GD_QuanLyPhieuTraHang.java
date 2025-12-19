/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang;

import com.formdev.flatlaf.FlatClientProperties;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.connectDB.ConnectDB;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
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

        txtSearchOrder.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vui lòng quét hoặc nhập mã hóa đơn");
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

        // Setup barcode scanner for order search
        setupBarcodeScanner();
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

    //Phàaan Logic code
    private void applyButtonStyles() {
        ButtonStyles.apply(btnTaoPhieu, ButtonStyles.Type.PRIMARY);
        FontStyles.apply(btnTaoPhieu, FontStyles.Type.BUTTON_MEDIUM);
        ButtonStyles.apply(btnOpenModalAddUnit, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnOpenModalAddUnit, FontStyles.Type.BUTTON_MEDIUM);
        ButtonStyles.apply(btnTraTatCa, ButtonStyles.Type.WARNING);
        FontStyles.apply(btnTraTatCa, FontStyles.Type.BUTTON_MEDIUM);
        ButtonStyles.apply(btnNhapLyDoChoTatCa, ButtonStyles.Type.INFO);
        FontStyles.apply(btnNhapLyDoChoTatCa, FontStyles.Type.BUTTON_MEDIUM);
        ButtonStyles.apply(btnXoaTatCa, ButtonStyles.Type.DANGER);
        FontStyles.apply(btnXoaTatCa, FontStyles.Type.BUTTON_MEDIUM);
    }

    /**
     * Thiết lập barcode scanner listener cho textfield tìm kiếm đơn hàng Hỗ trợ
     * cả quét barcode (tự động xử lý) và nhập thủ công (xử lý khi nhấn Enter)
     */
    private void setupBarcodeScanner() {
        // Biến để theo dõi trạng thái xử lý (tránh xử lý nhiều lần)
        final java.util.concurrent.atomic.AtomicBoolean isProcessing = new java.util.concurrent.atomic.AtomicBoolean(false);
        final java.util.concurrent.atomic.AtomicBoolean isClearing = new java.util.concurrent.atomic.AtomicBoolean(false);
        final javax.swing.Timer[] barcodeTimer = new javax.swing.Timer[1]; // Mảng để có thể thay đổi trong lambda

        // Theo dõi thời gian giữa các lần gõ để phân biệt quét vs nhập thủ công
        final long[] lastKeyTime = new long[1];
        lastKeyTime[0] = System.currentTimeMillis();
        final int[] keyCount = new int[1];
        keyCount[0] = 0;
        final long[] firstKeyTime = new long[1];
        firstKeyTime[0] = 0;

        // KeyListener để theo dõi tốc độ gõ phím
        txtSearchOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                long currentTime = System.currentTimeMillis();
                long timeSinceLastKey = currentTime - lastKeyTime[0];

                // Ghi nhận thời gian ký tự đầu tiên
                if (firstKeyTime[0] == 0) {
                    firstKeyTime[0] = currentTime;
                }

                // Nếu khoảng cách giữa các lần gõ < 50ms → có thể là quét barcode
                if (timeSinceLastKey < 50) {
                    keyCount[0]++;
                } else if (timeSinceLastKey > 200) {
                    // Nếu khoảng cách > 200ms → rõ ràng là nhập thủ công, reset counter
                    keyCount[0] = 1;
                    firstKeyTime[0] = currentTime;
                } else {
                    // Khoảng cách 50-200ms → có thể là gõ nhanh, tăng counter
                    keyCount[0]++;
                }

                lastKeyTime[0] = currentTime;
            }
        });

        // DocumentListener để bắt mọi thay đổi text
        txtSearchOrder.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void handleTextChange() {
                // Bỏ qua nếu đang clear textfield
                if (isClearing.get()) {
                    return;
                }

                // Hủy timer cũ nếu có
                if (barcodeTimer[0] != null && barcodeTimer[0].isRunning()) {
                    barcodeTimer[0].stop();
                }

                // Tạo timer mới: đợi 200ms không có thay đổi → kiểm tra xem có phải quét không
                barcodeTimer[0] = new javax.swing.Timer(200, evt -> {
                    String scannedText = txtSearchOrder.getText().trim();

                    // Loại bỏ ký tự đặc biệt từ barcode scanner (\r, \n, \t)
                    scannedText = scannedText.replaceAll("[\\r\\n\\t]", "");

                    // Cập nhật lại textfield với giá trị đã làm sạch (nếu cần)
                    if (!scannedText.equals(txtSearchOrder.getText().trim()) && !isClearing.get()) {
                        isClearing.set(true);
                        txtSearchOrder.setText(scannedText);
                        isClearing.set(false);
                    }

                    // Phân biệt quét barcode vs nhập thủ công:
                    // - Quét barcode: nhiều ký tự (>= 5) được nhập rất nhanh (keyCount >= 5 và thời gian tổng < 500ms)
                    // - Nhập thủ công: ít ký tự hoặc gõ chậm → không tự động xử lý, chờ Enter
                    long totalTime = firstKeyTime[0] > 0 ? (System.currentTimeMillis() - firstKeyTime[0]) : 0;
                    boolean isBarcodeScan = scannedText.length() >= 5 && keyCount[0] >= 5 && totalTime < 500;

                    if (!scannedText.isEmpty() && !isProcessing.get() && !isClearing.get() && isBarcodeScan) {
                        isProcessing.set(true);
                        // Tự động tìm kiếm đơn hàng khi quét barcode
                        timKiemVaHienThiDonHang(scannedText);
                        isProcessing.set(false);

                        // Clear textfield sau khi xử lý để sẵn sàng quét tiếp
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            javax.swing.Timer clearTimer = new javax.swing.Timer(500, e -> {
                                isClearing.set(true);
                                txtSearchOrder.setText("");
                                isClearing.set(false);
                                keyCount[0] = 0; // Reset counter
                                firstKeyTime[0] = 0; // Reset first key time
                            });
                            clearTimer.setRepeats(false);
                            clearTimer.start();
                        });
                    }

                    // Reset counter sau một khoảng thời gian (nếu không phải quét)
                    if (!isBarcodeScan) {
                        keyCount[0] = 0;
                        firstKeyTime[0] = 0;
                    }
                });
                barcodeTimer[0].setRepeats(false);
                barcodeTimer[0].start();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                // Khi xóa text, reset counter (người dùng đang chỉnh sửa)
                keyCount[0] = 0;
                firstKeyTime[0] = 0;
                lastKeyTime[0] = System.currentTimeMillis();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                handleTextChange();
            }
        });
    }

    private void btnTaoPhieuActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTaoPhieuActionPerformed
        taoPhieuTraHang();
    }// GEN-LAST:event_btnTaoPhieuActionPerformed

    private void jLabel2AncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_jLabel2AncestorAdded
        // TODO add your handling code here:
    }// GEN-LAST:event_jLabel2AncestorAdded

    private void txtSearchOrderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSearchOrderActionPerformed
        // Xử lý tìm kiếm hóa đơn khi nhấn Enter (nhập thủ công)
        String maDonHang = txtSearchOrder.getText().trim();

        // Loại bỏ ký tự đặc biệt
        maDonHang = maDonHang.replaceAll("[\\r\\n\\t]", "");

        if (!maDonHang.isEmpty()) {
            timKiemVaHienThiDonHang(maDonHang);
            // Clear textfield sau khi xử lý để sẵn sàng tìm kiếm tiếp
            javax.swing.SwingUtilities.invokeLater(() -> {
                txtSearchOrder.setText("");
            });
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
        // Lưu danh sách sản phẩm trước để tính lại giá hoàn cho tất cả
        java.util.List<Panel_ChiTietSanPhamDaMua> danhSachSanPham = new java.util.ArrayList<>();
        for (java.awt.Component comp : pnHaveOrder.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamDaMua) {
                Panel_ChiTietSanPhamDaMua productPanel = (Panel_ChiTietSanPhamDaMua) comp;
                danhSachSanPham.add(productPanel);
            }
        }

        // Thêm tất cả sản phẩm vào danh sách trả (trả toàn bộ = true)
        for (Panel_ChiTietSanPhamDaMua productPanel : danhSachSanPham) {
            onProductClicked(productPanel);
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
    private String maDonTraTam = null; // Lưu mã đơn trả tạm
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

        // Tính giá hoàn đúng theo nghiệp vụ (phân bổ giảm giá hóa đơn khi trả một phần)
        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang chiTiet = productPanel.getChiTietDonHang();
        boolean isTraToanBo = kiemTraTraToanBoHoaDon(soLuongDaMua);
        double donGiaHoan = tinhGiaHoanDonVi(chiTiet, isTraToanBo);
        returnProduct.setDonGia(donGiaHoan);

        // Lưu chi tiết đơn hàng gốc để lấy thông tin giảm giá
        returnProduct.setChiTietDonHang(chiTiet);

        // Copy hình ảnh nếu có - tìm sản phẩm từ database để lấy đường dẫn hình ảnh
        try {
            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO sanPhamDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO();
            java.util.List<SanPham> dsSanPham = sanPhamDAO.findAll();

            boolean found = false;
            for (SanPham sp : dsSanPham) {
                if (sp.getTenSanPham().equals(tenSP)) {
                    String hinhAnh = sp.getHinhAnh();
                    if (hinhAnh != null && !hinhAnh.isEmpty()) {
                        // Chuyển đổi đường dẫn tương đối thành đường dẫn đầy đủ
                        String fullPath = "src/main/resources/img/" + hinhAnh;
                        returnProduct.setHinhAnh(fullPath);
                    }
                    found = true;
                    break;
                }
            }
        } catch (Exception e) {
            // Bỏ qua lỗi load hình, không chặn thêm sản phẩm trả hàng
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
            // Kiểm tra xem có đơn đang hiển thị trên giao diện không
            if (maDonTraTam != null && !maDonTraTam.trim().isEmpty()) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Đơn của bạn đang hiển thị trên giao diện. Vui lòng hoàn tất hoặc hủy đơn hiện tại trước khi tìm kiếm đơn mới!");
                return;
            }

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
            } else {
                txtCusName.setText("Khách vãng lai");
                txtCusPhone.setText("N/A");
            }

            // Hiển thị tên nhân viên hiện tại
            vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager sessionManager = vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager
                    .getInstance();
            if (sessionManager.getCurrentUser() != null) {
                String tenNV = sessionManager.getCurrentUser().getTenNhanVien();
                txtEmpName.setText(tenNV);
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
                SanPham sanPham = loHang != null ? loHang.getSanPham()
                        : null;

                if (sanPham != null) {
                    productPanel.setTenSanPham(sanPham.getTenSanPham());
                    productPanel.setDonVi(sanPham.getDonViTinh() != null
                            ? sanPham.getDonViTinh().getTenDonVi()
                            : "N/A");
                    productPanel.setSoLuong(chiTiet.getSoLuong());
                    productPanel.setDonGia(chiTiet.getDonGia());
                    // Set tổng tiền từ thanhTien (đã có VAT và khuyến mãi)
                    productPanel.setTongTien(chiTiet.getThanhTien());
                    productPanel.setChiTietDonHang(chiTiet); // Lưu chi tiết đơn hàng để tính tiền hoàn
                    productPanel.setProductClickListener(this::onProductClicked);

                    // Thiết lập hình ảnh sản phẩm
                    String hinhAnh = sanPham.getHinhAnh();
                    if (hinhAnh != null && !hinhAnh.isEmpty()) {
                        // Chuyển đổi đường dẫn tương đối thành đường dẫn đầy đủ
                        String fullPath = "src/main/resources/img/" + hinhAnh;
                        productPanel.setHinhAnh(fullPath);
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
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.ERROR,
                    "Lỗi khi tải hóa đơn: " + e.getMessage());
        }
    }

    /**
     * Tính giá hoàn đơn vị cho sản phẩm theo đúng nghiệp vụ
     *
     * NGUYÊN TẮC: 1) CHỈ hoàn đúng giá đã giảm trong 2 trường hợp: - Trả TOÀN
     * BỘ hóa đơn → hoàn đúng số tiền khách đã trả - Giảm giá GẮN TRỰC TIẾP trên
     * sản phẩm (line-item discount)
     *
     * 2) KHÔNG được hoàn đúng giá giảm nếu: - Khuyến mãi áp dụng THEO HÓA ĐƠN →
     * Phải phân bổ tiền giảm theo tỷ lệ giá trị sản phẩm
     *
     * 3) Số tiền hoàn = số tiền KHÁCH ĐÃ THỰC TRẢ, không dùng giá gốc
     *
     * @param chiTiet Chi tiết đơn hàng gốc
     * @param isTraToanBo true nếu trả toàn bộ hóa đơn, false nếu trả một phần
     * @return Giá hoàn đơn vị (đã tính giảm giá đúng)
     */
    private double tinhGiaHoanDonVi(
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang chiTiet,
            boolean isTraToanBo) {
        if (chiTiet == null) {
            return 0;
        }

        // Lấy thông tin từ chi tiết đơn hàng
        double thanhTienThucTe = chiTiet.getThanhTien(); // Số tiền khách đã thực trả
        int soLuong = chiTiet.getSoLuong();
        double giamGiaSanPham = chiTiet.getGiamGiaSanPham(); // Giảm giá gắn trực tiếp trên sản phẩm
        double giamGiaHoaDonPhanBo = chiTiet.getGiamGiaHoaDonPhanBo(); // Giảm giá hóa đơn phân bổ

        // Tính giá đơn vị thực tế khách đã trả
        double donGiaThucTe = thanhTienThucTe / soLuong;

        // TRƯỜNG HỢP 1: Trả TOÀN BỘ hóa đơn → hoàn đúng số tiền khách đã trả
        if (isTraToanBo) {
            return donGiaThucTe;
        }

        // TRƯỜNG HỢP 2: Giảm giá GẮN TRỰC TIẾP trên sản phẩm (line-item discount)
        // → Hoàn đúng giá đã giảm (giá gốc - giảm giá sản phẩm)
        if (giamGiaSanPham > 0 && giamGiaHoaDonPhanBo == 0) {
            // Chỉ có giảm giá sản phẩm, không có giảm giá hóa đơn
            // Giá hoàn = giá gốc - giảm giá sản phẩm
            double donGiaGoc = chiTiet.getDonGia();
            double tongTienGoc = donGiaGoc * soLuong;
            double thanhTienSauGiamGiaSP = tongTienGoc - giamGiaSanPham;
            return thanhTienSauGiamGiaSP / soLuong;
        }

        // TRƯỜNG HỢP 3: Có giảm giá hóa đơn (có thể kèm giảm giá sản phẩm)
        // → Phải phân bổ tiền giảm hóa đơn theo tỷ lệ giá trị sản phẩm SAU giảm giá sản phẩm
        if (giamGiaHoaDonPhanBo > 0) {
            // BƯỚC 1: Tính giá trị sản phẩm này SAU giảm giá sản phẩm
            // Ví dụ: Yoosun sau giảm sản phẩm = 21.840
            double donGiaGoc = chiTiet.getDonGia();
            double tongTienGoc = donGiaGoc * soLuong;
            double thanhTienSauGiamGiaSP = tongTienGoc - giamGiaSanPham;

            // BƯỚC 2: Tính tổng giá trị toàn bộ hóa đơn SAU giảm giá sản phẩm
            // Ví dụ: Tổng 2 sản phẩm sau giảm sản phẩm = 56.784
            double tongTienHangSauGiamGiaSP = 0;
            double tongGiamGiaHoaDon = 0;
            if (currentDonHang != null) {
                try {
                    vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO chiTietDAO
                            = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO();
                    java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang> danhSachChiTiet = chiTietDAO.findByIdList(currentDonHang.getMaDonHang());

                    for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang ct : danhSachChiTiet) {
                        double tongTienGocCT = ct.getDonGia() * ct.getSoLuong();
                        double thanhTienSauGiamSPCT = tongTienGocCT - ct.getGiamGiaSanPham();
                        tongTienHangSauGiamGiaSP += thanhTienSauGiamSPCT;
                    }

                    // Tính tổng giảm giá hóa đơn
                    // Ví dụ: Tổng giảm giá hóa đơn = 7.098
                    if (tongTienHangSauGiamGiaSP > 0 && currentDonHang.getKhuyenMai() != null
                            && currentDonHang.getKhuyenMai().getGiamGia() > 0) {
                        tongGiamGiaHoaDon = tongTienHangSauGiamGiaSP * currentDonHang.getKhuyenMai().getGiamGia();
                    }
                } catch (Exception e) {
                    // Nếu lỗi, dùng giá thực tế đã trả
                    return donGiaThucTe;
                }
            }

            // BƯỚC 3: Tính tỷ lệ giá trị sản phẩm này so với tổng hóa đơn
            // Ví dụ: Yoosun chiếm = 21.840 / 56.784 ≈ 38.45%
            double tyLe = tongTienHangSauGiamGiaSP > 0
                    ? (thanhTienSauGiamGiaSP / tongTienHangSauGiamGiaSP)
                    : 0;

            // BƯỚC 4: Phân bổ giảm giá hóa đơn cho sản phẩm này
            // Ví dụ: Phần giảm của Yoosun = 7.098 × 38.45% ≈ 2.729
            double giamGiaHoaDonPhanBoMoi = tongGiamGiaHoaDon * tyLe;

            // BƯỚC 5: Tính tiền hoàn = giá trị sau giảm sản phẩm - phần giảm hóa đơn được phân bổ
            // Ví dụ: Tiền hoàn = 21.840 - 2.729 = 19.111
            double thanhTienHoan = thanhTienSauGiamGiaSP - giamGiaHoaDonPhanBoMoi;
            double donGiaHoan = Math.max(0, thanhTienHoan / soLuong);

            return donGiaHoan;
        }

        // Trường hợp không có giảm giá → hoàn giá gốc
        return donGiaThucTe;
    }

    /**
     * Kiểm tra xem có phải trả toàn bộ hóa đơn không
     *
     * @param soLuongDangThem Số lượng sản phẩm đang được thêm vào danh sách trả
     */
    private boolean kiemTraTraToanBoHoaDon(int soLuongDangThem) {
        if (currentDonHang == null) {
            return false;
        }

        try {
            // Đếm số sản phẩm đã mua
            int tongSoLuongDaMua = 0;
            for (java.awt.Component comp : pnHaveOrder.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamDaMua) {
                    Panel_ChiTietSanPhamDaMua panel = (Panel_ChiTietSanPhamDaMua) comp;
                    tongSoLuongDaMua += panel.getSoLuong();
                }
            }

            // Đếm số lượng đang trả (trước khi thêm sản phẩm mới)
            int tongSoLuongDangTra = 0;
            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                    tongSoLuongDangTra += panel.getSoLuongTra();
                }
            }

            // Nếu số lượng trả (sau khi thêm) = số lượng đã mua → trả toàn bộ
            return (tongSoLuongDangTra + soLuongDangThem) == tongSoLuongDaMua;
        } catch (Exception e) {
            return false;
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
                int soLuong = panel.getSoLuongTra();
                double donGia = panel.getDonGia();
                tongTien += soLuong * donGia;
            }
        }

        txtReturnTotal.setText(String.format("%,.0f ₫", tongTien));
    }

    /**
     * Tạo mã đơn trả hàng tạm để hiển thị trong preview Mã này sẽ được tạo lại
     * chính xác khi lưu vào database
     *
     * @return Mã đơn trả hàng tạm
     */
    private String taoMaDonTraHangTam() {
        LocalDate ngayHienTai = LocalDate.now();
        String ngayThangNam = String.format("%02d%02d%04d",
                ngayHienTai.getDayOfMonth(),
                ngayHienTai.getMonthValue(),
                ngayHienTai.getYear());

        String prefixHienTai = "DT" + ngayThangNam;

        try (Connection con = ConnectDB.getConnection(); PreparedStatement stmt = con.prepareStatement(
                "SELECT TOP 1 maDonTra FROM DonTraHang WHERE maDonTra LIKE ? ORDER BY maDonTra DESC")) {

            stmt.setString(1, prefixHienTai + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maCuoi = rs.getString("maDonTra");
                try {
                    // Lấy 4 số cuối: DTddmmyyyyxxxx -> xxxx
                    String soSTT = maCuoi.substring(12);
                    int so = Integer.parseInt(soSTT) + 1;
                    return prefixHienTai + String.format("%04d", so);
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    System.err.println("Mã đơn trả hàng không hợp lệ: " + maCuoi + ". Tạo mã mới.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu chưa có đơn nào trong ngày, tạo mã đầu tiên
        return prefixHienTai + "0001";
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

            // Tạo đơn trả hàng TẠM (KHÔNG lưu vào database)
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang = new vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang();

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

            // Tạo mã đơn trả hàng tạm để hiển thị trong preview
            String maDonTraTam = taoMaDonTraHangTam();
            donTraHang.setMaDonTraHang(maDonTraTam);

            // Lưu mã đơn trả tạm vào biến instance để dùng khi hủy
            this.maDonTraTam = maDonTraTam;

            // Tạo danh sách chi tiết TẠM (chưa lưu vào DB)
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> chiTietList = new java.util.ArrayList<>();
            boolean allDetailsValid = true;

            vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO sanPhamDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO();

            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;

                    // Tìm sản phẩm từ tên
                    String tenSanPham = panel.getTenSanPham();
                    java.util.List<SanPham> dsSanPham = sanPhamDAO.findAll();

                    SanPham sanPham = null;
                    for (SanPham sp : dsSanPham) {
                        if (sp.getTenSanPham().equals(tenSanPham)) {
                            sanPham = sp;
                            break;
                        }
                    }

                    if (sanPham == null) {
                        allDetailsValid = false;
                        raven.toast.Notifications.getInstance().show(
                                raven.toast.Notifications.Type.ERROR,
                                "Không tìm thấy sản phẩm: " + tenSanPham);
                        continue;
                    }

                    vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet = new vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang();

                    chiTiet.setSoLuong(panel.getSoLuongTra());
                    chiTiet.setDonGia(panel.getDonGia());
                    chiTiet.setLyDoTra(panel.getLyDoTraHang());
                    // Tổng tiền sau giảm giá = số lượng * đơn giá (đơn giá đã là giá sau giảm)
                    chiTiet.setThanhTien(panel.getSoLuongTra() * panel.getDonGia());
                    chiTiet.setSanPham(sanPham);
                    chiTiet.setDonTraHang(donTraHang);

                    chiTietList.add(chiTiet);
                }
            }

            if (!allDetailsValid || chiTietList.isEmpty()) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Không thể tạo danh sách chi tiết đơn trả hàng! Vui lòng kiểm tra lại.");
                return;
            }

            // Hiển thị dialog xác nhận (KHÔNG lưu vào DB, chỉ preview)
            hienThiDialogXacNhan(donTraHang, chiTietList);

        } catch (Exception e) {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.ERROR,
                    "Lỗi khi tạo phiếu trả hàng: " + e.getMessage());
        }
    }

    /**
     * Hiển thị dialog xác nhận đơn trả hàng
     */
    private void hienThiDialogXacNhan(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang,
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> danhSachChiTiet) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Xác Nhận Đơn Trả Hàng");
        dialog.setModal(true);
        dialog.setSize(900, 650);
        dialog.setLocationRelativeTo(null);

        javax.swing.JPanel mainPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,###");

        // Header
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));
        headerPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("XÁC NHẬN ĐƠN TRẢ HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setForeground(new java.awt.Color(220, 53, 69));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        headerPanel.add(javax.swing.Box.createVerticalStrut(10));

        String maDonHienTai = donTraHang.getMaDonTraHang() != null ? donTraHang.getMaDonTraHang() : "TẠM THỜI";
        javax.swing.JLabel lblMaDon = new javax.swing.JLabel("Mã phiếu: " + maDonHienTai);
        lblMaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblMaDon.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        headerPanel.add(lblMaDon);

        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        // Bảng
        String[] columnNames = {"STT", "Tên sản phẩm", "Số lượng", "Đơn giá", "Giảm giá sản phẩm", "Giảm giá hóa đơn", "Tổng tiền", "Lý do"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        table.setRowHeight(30);

        int stt = 1;
        for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : danhSachChiTiet) {
            // Tìm panel tương ứng để lấy giảm giá sản phẩm và giảm giá hóa đơn
            double giamGiaSanPham = 0;
            double giamGiaHoaDon = 0;
            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                    if (panel.getTenSanPham().equals(chiTiet.getSanPham().getTenSanPham())) {
                        giamGiaSanPham = panel.getGiamGiaSanPham();
                        giamGiaHoaDon = panel.getGiamGiaHoaDon();
                        break;
                    }
                }
            }

            // Tổng tiền sau giảm giá = số lượng * đơn giá (đơn giá đã là giá sau giảm)
            double tongTienSauGiamGia = chiTiet.getSoLuong() * chiTiet.getDonGia();

            tableModel.addRow(new Object[]{
                stt++,
                chiTiet.getSanPham().getTenSanPham(),
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                giamGiaSanPham > 0 ? "-" + currencyFormat.format(giamGiaSanPham) + " đ" : "0 đ",
                giamGiaHoaDon > 0 ? "-" + currencyFormat.format(giamGiaHoaDon) + " đ" : "0 đ",
                currencyFormat.format(tongTienSauGiamGia) + " đ",
                chiTiet.getLyDoTra()
            });
        }

        mainPanel.add(new javax.swing.JScrollPane(table), java.awt.BorderLayout.CENTER);

        // Footer
        javax.swing.JPanel footerPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
        footerPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(java.awt.Color.WHITE);
        javax.swing.JLabel lblTongTien = new javax.swing.JLabel(
                "Tổng tiền trả: " + currencyFormat.format(donTraHang.getThanhTien()) + " đ");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        lblTongTien.setForeground(new java.awt.Color(220, 53, 69));
        tongTienPanel.add(lblTongTien);
        footerPanel.add(tongTienPanel, java.awt.BorderLayout.NORTH);

        // Buttons
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JButton btnInHoaDon = new javax.swing.JButton("In Hóa Đơn");
        btnInHoaDon.setPreferredSize(new java.awt.Dimension(150, 40));
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnInHoaDon.setBackground(new java.awt.Color(0, 120, 215));
        btnInHoaDon.setForeground(java.awt.Color.WHITE);
        btnInHoaDon.addActionListener(e -> {
            if (danhSachChiTiet == null || danhSachChiTiet.isEmpty()) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.WARNING,
                        "Không có dữ liệu sản phẩm để xuất PDF!");
                return;
            }

            try {
                // 1. Lưu đơn trả hàng và chi tiết vào database trước
                boolean luuThanhCong = luuDonTraHangVaoDB(donTraHang, danhSachChiTiet);
                if (!luuThanhCong) {
                    raven.toast.Notifications.getInstance().show(
                            raven.toast.Notifications.Type.ERROR,
                            "Lỗi khi lưu đơn trả hàng vào database!");
                    return;
                }

                // 2. Tạo PDF sau khi đã lưu thành công
                byte[] pdfData = taoHoaDonTraPdf(donTraHang, danhSachChiTiet);
                String pdfPath = hienThiPdfTamThoiTra(pdfData, donTraHang);

                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.SUCCESS,
                        raven.toast.Notifications.Location.TOP_CENTER,
                        3000,
                        pdfPath != null
                                ? "Đã lưu và mở hóa đơn trả hàng PDF tại: " + pdfPath
                                : "Đã lưu và mở hóa đơn trả hàng PDF tạm thời!");

                dialog.dispose();
                maDonTraTam = null;
                resetForm();
            } catch (Exception ex) {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        "Lỗi khi lưu đơn trả hàng: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        javax.swing.JButton btnHuyBo = new javax.swing.JButton("Hủy Bỏ");
        btnHuyBo.setPreferredSize(new java.awt.Dimension(120, 40));
        btnHuyBo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        btnHuyBo.setBackground(new java.awt.Color(220, 53, 69));
        btnHuyBo.setForeground(java.awt.Color.WHITE);
        btnHuyBo.addActionListener(e -> {
            // Đóng dialog và xóa mã đơn tạm (chưa lưu vào DB nên không cần xóa)
            maDonTraTam = null;
            dialog.dispose();
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.INFO,
                    "Đã hủy. Dữ liệu form vẫn được giữ để nhập lại.");
        });

        javax.swing.JButton btnDong = new javax.swing.JButton("Đóng");
        btnDong.setPreferredSize(new java.awt.Dimension(120, 40));
        btnDong.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        btnDong.addActionListener(e -> {
            dialog.dispose();
            // Xóa mã đơn tạm và reset form khi đóng (xác nhận hoàn tất)
            maDonTraTam = null;
            resetForm();
        });

        buttonPanel.add(btnInHoaDon);
        buttonPanel.add(btnHuyBo);
        buttonPanel.add(btnDong);
        footerPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Lưu đơn trả hàng và chi tiết vào database
     *
     * @param donTraHang Đơn trả hàng cần lưu
     * @param danhSachChiTiet Danh sách chi tiết cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    private boolean luuDonTraHangVaoDB(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang,
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> danhSachChiTiet) {
        try {
            // Tạo mã đơn trả hàng mới (thay thế mã tạm)
            String maDonTraMoi = taoMaDonTraHangTam();
            donTraHang.setMaDonTraHang(maDonTraMoi);

            // Set trạng thái mặc định nếu chưa có
            if (donTraHang.getTrangThaiXuLy() == null || donTraHang.getTrangThaiXuLy().trim().isEmpty()) {
                donTraHang.setTrangThaiXuLy("Chưa xử lý");
            }

            // 1. Lưu đơn trả hàng vào database
            vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS donTraHangBUS
                    = new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS();
            boolean savedDonTra = donTraHangBUS.taoDonTraHang(donTraHang);

            if (!savedDonTra) {
                System.err.println("Lỗi khi lưu đơn trả hàng!");
                return false;
            }

            // 2. Lưu chi tiết đơn trả hàng
            vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS chiTietBUS
                    = new vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS();

            boolean allDetailsSaved = true;
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : danhSachChiTiet) {
                // Cập nhật mã đơn trả hàng mới cho chi tiết
                chiTiet.setDonTraHang(donTraHang);

                // Set trạng thái mặc định nếu chưa có
                if (chiTiet.getTrangThaiXuLy() == null || chiTiet.getTrangThaiXuLy().trim().isEmpty()) {
                    chiTiet.setTrangThaiXuLy("Chưa xử lý");
                }

                boolean chiTietSaved = chiTietBUS.themChiTietDonTraHang(chiTiet);
                if (!chiTietSaved) {
                    allDetailsSaved = false;
                    System.err.println("Lỗi khi lưu chi tiết đơn trả hàng cho sản phẩm: "
                            + (chiTiet.getSanPham() != null ? chiTiet.getSanPham().getTenSanPham() : "N/A"));
                }
            }

            if (!allDetailsSaved) {
                System.err.println("Có lỗi khi lưu một số chi tiết đơn trả hàng!");
                return false;
            }

            System.out.println("Đã lưu thành công đơn trả hàng: " + maDonTraMoi);
            return true;

        } catch (Exception ex) {
            System.err.println("Lỗi khi lưu đơn trả hàng vào database: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Hiển thị preview hóa đơn trả hàng (copy từ GD_QuanLyTraHang - có barcode)
     */
    private void hienThiPreviewHoaDon(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang,
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> danhSachChiTiet) {
        // Copy toàn bộ code từ GD_QuanLyTraHang.hienThiHoaDonTraHang()
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa Đơn Trả Hàng");
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
        javax.swing.JLabel lblTitleInvoice = new javax.swing.JLabel("PHIEU TRA HANG");
        lblTitleInvoice.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTitleInvoice.setForeground(new java.awt.Color(220, 53, 69)); // Màu đỏ cho trả hàng
        lblTitleInvoice.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitleInvoice);
        mainPanel.add(javax.swing.Box.createVerticalStrut(8));

        // ========== BARCODE MÃ ĐƠN TRẢ HÀNG ==========
        try {
            java.awt.image.BufferedImage barcodeImage = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                    .taoBarcode(donTraHang.getMaDonTraHang());
            java.awt.image.BufferedImage barcodeWithText = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                    .addTextBelow(barcodeImage, donTraHang.getMaDonTraHang());

            javax.swing.JLabel lblBarcode = new javax.swing.JLabel(new javax.swing.ImageIcon(barcodeWithText));
            lblBarcode.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            mainPanel.add(lblBarcode);
        } catch (Exception ex) {
            // Bỏ qua lỗi tạo barcode, không chặn hiển thị hóa đơn
        }
        mainPanel.add(javax.swing.Box.createVerticalStrut(2));

        String ngayTra = donTraHang.getNgayTraHang().format(dateFormatter);
        javax.swing.JLabel lblDate = new javax.swing.JLabel("Ngay tra: " + ngayTra);
        lblDate.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblDate.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblDate);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== THÔNG TIN KHÁCH HÀNG VÀ NHÂN VIÊN (2 CỘT) ==========
        javax.swing.JPanel infoTablePanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        infoTablePanel.setBackground(java.awt.Color.WHITE);
        infoTablePanel.setMaximumSize(new java.awt.Dimension(600, 80));

        // THÔNG TIN KHÁCH HÀNG (Cột trái)
        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang khachHang = currentDonHang != null
                ? currentDonHang.getKhachHang()
                : null;
        javax.swing.JPanel customerPanel = new javax.swing.JPanel();
        customerPanel.setLayout(new javax.swing.BoxLayout(customerPanel, javax.swing.BoxLayout.Y_AXIS));
        customerPanel.setBackground(java.awt.Color.WHITE);
        customerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));

        javax.swing.JLabel lblCustomerTitle = new javax.swing.JLabel("THONG TIN KHACH HANG");
        lblCustomerTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        lblCustomerTitle.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        customerPanel.add(lblCustomerTitle);
        customerPanel.add(javax.swing.Box.createVerticalStrut(3));

        String tenKH = khachHang != null ? khachHang.getTenKhachHang() : "Vang lai";
        javax.swing.JLabel lblCustomerName = new javax.swing.JLabel("Ho ten: " + tenKH);
        lblCustomerName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblCustomerName.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        customerPanel.add(lblCustomerName);

        if (khachHang != null && khachHang.getSoDienThoai() != null) {
            javax.swing.JLabel lblCustomerPhone = new javax.swing.JLabel("SDT: " + khachHang.getSoDienThoai());
            lblCustomerPhone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
            lblCustomerPhone.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            customerPanel.add(lblCustomerPhone);
        }

        // THÔNG TIN NHÂN VIÊN (Cột phải)
        vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien nhanVien = donTraHang.getNhanVien();
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

        infoTablePanel.add(customerPanel);
        infoTablePanel.add(employeePanel);
        mainPanel.add(infoTablePanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(10));

        // ========== BẢNG SẢN PHẨM (GIỐNG HÓA ĐƠN BÁN HÀNG) ==========
        String[] columnNames = {"STT", "Ten san pham", "SL", "Don gia", "Giam gia san pham", "Giam gia hoa don", "Tong tien"};
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

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên SP
        table.getColumnModel().getColumn(2).setPreferredWidth(40); // SL
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // Đơn giá
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Giảm giá sản phẩm
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Giảm giá hóa đơn
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Tổng tiền

        // Thêm dữ liệu vào bảng
        int stt = 1;
        for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : danhSachChiTiet) {
            SanPham sanPham = chiTiet.getSanPham();

            // Tìm panel tương ứng để lấy giảm giá sản phẩm và giảm giá hóa đơn
            double giamGiaSanPham = 0;
            double giamGiaHoaDon = 0;
            for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                    Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                    if (panel.getTenSanPham().equals(sanPham.getTenSanPham())) {
                        giamGiaSanPham = panel.getGiamGiaSanPham();
                        giamGiaHoaDon = panel.getGiamGiaHoaDon();
                        break;
                    }
                }
            }

            // Tổng tiền sau giảm giá = số lượng * đơn giá (đơn giá đã là giá sau giảm)
            double tongTienSauGiamGia = chiTiet.getSoLuong() * chiTiet.getDonGia();

            tableModel.addRow(new Object[]{
                stt++,
                sanPham.getTenSanPham(),
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                giamGiaSanPham > 0 ? "-" + currencyFormat.format(giamGiaSanPham) + " đ" : "0 đ",
                giamGiaHoaDon > 0 ? "-" + currencyFormat.format(giamGiaHoaDon) + " đ" : "0 đ",
                currencyFormat.format(tongTienSauGiamGia) + " đ"
            });
        }

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        mainPanel.add(scrollPane);

        mainPanel.add(javax.swing.Box.createVerticalStrut(10));

        // ========== FOOTER - Tổng tiền ==========
        javax.swing.JSeparator separator2 = new javax.swing.JSeparator();
        mainPanel.add(separator2);
        mainPanel.add(javax.swing.Box.createVerticalStrut(10));

        // Thông tin khuyến mãi (nếu có)
        if (currentDonHang != null && currentDonHang.getKhuyenMai() != null) {
            javax.swing.JPanel kmPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            kmPanel.setBackground(java.awt.Color.WHITE);

            javax.swing.JLabel lblKMText = new javax.swing.JLabel("Khuyen mai: ");
            lblKMText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));

            javax.swing.JLabel lblKM = new javax.swing.JLabel(currentDonHang.getKhuyenMai().getTenKhuyenMai());
            lblKM.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
            lblKM.setForeground(new java.awt.Color(255, 102, 0));

            kmPanel.add(lblKMText);
            kmPanel.add(lblKM);
            mainPanel.add(kmPanel);
        }

        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblTongTienText = new javax.swing.JLabel("Tong hoa don tra: ");
        lblTongTienText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

        javax.swing.JLabel lblTongTien = new javax.swing.JLabel(
                currencyFormat.format(donTraHang.getThanhTien()) + " đ");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblTongTien.setForeground(new java.awt.Color(220, 53, 69));

        tongTienPanel.add(lblTongTienText);
        tongTienPanel.add(lblTongTien);

        mainPanel.add(tongTienPanel);

        // Thêm vào scroll pane
        mainScrollPane.setViewportView(mainPanel);
        dialog.add(mainScrollPane);
        dialog.setVisible(true);
    }

    /**
     * Tạo PDF hóa đơn trả hàng
     */
    private byte[] taoHoaDonTraPdf(vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang,
            java.util.List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang> danhSachChiTiet)
            throws IOException {

        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,###");
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        double tongTienHang = 0;
        for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : danhSachChiTiet) {
            tongTienHang += chiTiet.getThanhTien();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PdfWriter writer = new PdfWriter(baos); PdfDocument pdfDoc = new PdfDocument(writer); Document document = new Document(pdfDoc, PageSize.A5)) {

            document.setMargins(24, 24, 24, 24);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            document.add(new Paragraph("IUH PHARMACITY")
                    .setFont(fontBold)
                    .setFontSize(16)
                    .setFontColor(ColorConstants.BLUE)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("12 Nguyen Van Bao, Ward 4, Go Vap District, HCMC")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Hotline: 1800 6928 | Email: cskh@pharmacity.vn")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));

            String maPhieu = (donTraHang != null && donTraHang.getMaDonTraHang() != null)
                    ? donTraHang.getMaDonTraHang()
                    : "UNKNOWN";
            try {
                BufferedImage barcodeRaw = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil.taoBarcode(maPhieu);
                BufferedImage barcodeWithText = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                        .addTextBelow(barcodeRaw, maPhieu);
                ByteArrayOutputStream barcodeStream = new ByteArrayOutputStream();
                ImageIO.write(barcodeWithText, "png", barcodeStream);
                Image barcodeImage = new Image(ImageDataFactory.create(barcodeStream.toByteArray()))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setAutoScale(false)
                        .setWidth(150);
                document.add(barcodeImage);
            } catch (Exception ex) {
                document.add(new Paragraph(""));
            }

            // Hiển thị mã phiếu với chú thích tạm thời
//            document.add(new Paragraph("Ma phieu: " + maPhieu)
//                    .setFont(font)
//                    .setFontSize(10)
//                    .setTextAlignment(TextAlignment.CENTER)
//                    .setFontColor(ColorConstants.DARK_GRAY));
//            document.add(new Paragraph(""));
            document.add(new Paragraph("PHIEU TRA HANG")
                    .setFont(fontBold)
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.RED));

            LocalDate ngayTra = (donTraHang != null && donTraHang.getNgayTraHang() != null)
                    ? donTraHang.getNgayTraHang()
                    : LocalDate.now();
            document.add(new Paragraph("Ngay lap: " + ngayTra.format(dateFormatter))
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));

            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();
            infoTable.addCell(new Cell()
                    .add(new Paragraph("THONG TIN KHACH HANG").setFont(fontBold).setFontSize(9))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
            infoTable.addCell(new Cell()
                    .add(new Paragraph("THONG TIN NHAN VIEN").setFont(fontBold).setFontSize(9))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));

            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang donHang = donTraHang != null
                    ? donTraHang.getDonHang()
                    : null;
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang khachHang = donHang != null
                    ? donHang.getKhachHang()
                    : null;
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien nhanVien = donTraHang != null
                    ? donTraHang.getNhanVien()
                    : null;

            String khachHangInfo = (khachHang != null ? "Ho ten: " + khachHang.getTenKhachHang() : "Ho ten: Vang lai")
                    + (khachHang != null && khachHang.getSoDienThoai() != null
                    ? "\nSDT: " + khachHang.getSoDienThoai()
                    : "");

            String nhanVienInfo = (nhanVien != null ? "Ho ten: " + nhanVien.getTenNhanVien() : "Ho ten: N/A")
                    + (nhanVien != null && nhanVien.getSoDienThoai() != null
                    ? "\nSDT: " + nhanVien.getSoDienThoai()
                    : "");

            infoTable.addCell(new Cell().add(new Paragraph(khachHangInfo).setFont(font).setFontSize(9)));
            infoTable.addCell(new Cell().add(new Paragraph(nhanVienInfo).setFont(font).setFontSize(9)));
            document.add(infoTable);
            document.add(new Paragraph("\n"));

            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{5, 20, 7, 10, 12, 12, 12, 22}))
                    .useAllAvailableWidth();
            String[] headers = {"STT", "Ten san pham", "SL", "Don gia", "Giam gia san pham", "Giam gia hoa don", "Tong tien", "Ly do tra"};
            for (String header : headers) {
                itemsTable.addHeaderCell(new Cell()
                        .add(new Paragraph(header).setFont(fontBold).setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            int stt = 1;
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang chiTiet : danhSachChiTiet) {
                SanPham sanPham = chiTiet.getSanPham();
                String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
                String soLuong = String.valueOf(chiTiet.getSoLuong());
                String donGia = currencyFormat.format(chiTiet.getDonGia()) + " đ";

                // Tìm panel tương ứng để lấy giảm giá sản phẩm và giảm giá hóa đơn
                double giamGiaSanPham = 0;
                double giamGiaHoaDon = 0;
                for (java.awt.Component comp : pnHaveReturn.getComponents()) {
                    if (comp instanceof Panel_ChiTietSanPhamTraHang) {
                        Panel_ChiTietSanPhamTraHang panel = (Panel_ChiTietSanPhamTraHang) comp;
                        if (panel.getTenSanPham().equals(tenSP)) {
                            giamGiaSanPham = panel.getGiamGiaSanPham();
                            giamGiaHoaDon = panel.getGiamGiaHoaDon();
                            break;
                        }
                    }
                }
                String giamGiaSanPhamStr = giamGiaSanPham > 0 ? "-" + currencyFormat.format(giamGiaSanPham) + " đ" : "0 đ";
                String giamGiaHoaDonStr = giamGiaHoaDon > 0 ? "-" + currencyFormat.format(giamGiaHoaDon) + " đ" : "0 đ";

                // Tổng tiền sau giảm giá = số lượng * đơn giá (đơn giá đã là giá sau giảm)
                double tongTienSauGiamGia = chiTiet.getSoLuong() * chiTiet.getDonGia();
                String tongTienStr = currencyFormat.format(tongTienSauGiamGia) + " đ";

                String lyDo = chiTiet.getLyDoTra() != null ? chiTiet.getLyDoTra() : "";

                itemsTable.addCell(new Cell().add(new Paragraph(String.valueOf(stt++))
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER)));
                itemsTable.addCell(new Cell().add(new Paragraph(tenSP).setFont(font).setFontSize(9)));
                itemsTable.addCell(new Cell().add(new Paragraph(soLuong)
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER)));
                itemsTable.addCell(new Cell().add(new Paragraph(donGia)
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.RIGHT)));
                itemsTable.addCell(new Cell().add(new Paragraph(giamGiaSanPhamStr)
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.RIGHT)));
                itemsTable.addCell(new Cell().add(new Paragraph(giamGiaHoaDonStr)
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.RIGHT)));
                itemsTable.addCell(new Cell().add(new Paragraph(tongTienStr)
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.RIGHT)));
                itemsTable.addCell(new Cell().add(new Paragraph(lyDo).setFont(font).setFontSize(9)));
            }

            document.add(itemsTable);
            document.add(new Paragraph("\n"));

            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setWidth(UnitValue.createPercentValue(80));
            summaryTable.addCell(new Cell().setBorder(null)
                    .add(new Paragraph("Tong gia tri hang doi:")
                            .setFont(font)
                            .setFontSize(9)));
            summaryTable.addCell(new Cell().setBorder(null)
                    .add(new Paragraph(currencyFormat.format(tongTienHang) + " đ")
                            .setFont(fontBold)
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.RIGHT)));
            summaryTable.addCell(new Cell().setBorder(null)
                    .add(new Paragraph("So tien da hoan:")
                            .setFont(fontBold)
                            .setFontSize(9)));
            summaryTable.addCell(new Cell().setBorder(null)
                    .add(new Paragraph(currencyFormat.format(donTraHang.getThanhTien()) + " đ")
                            .setFont(fontBold)
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.RIGHT)));

            document.add(summaryTable);
            document.add(new Paragraph("\nCam on quy khach da tin tuong Pharmacity!")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Vui long kiem tra ky thong tin truoc khi ky xac nhan.")
                    .setFont(font)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            return baos.toByteArray();
        }
    }

    /**
     * Ghi tạm file PDF trả hàng và mở cho người dùng
     */
    private String hienThiPdfTamThoiTra(byte[] pdfData, vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang donTraHang)
            throws IOException {

        if (pdfData == null || pdfData.length == 0) {
            throw new IOException("Dữ liệu PDF rỗng.");
        }

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate ngayTra = (donTraHang != null && donTraHang.getNgayTraHang() != null)
                ? donTraHang.getNgayTraHang()
                : LocalDate.now();
        String datePart = ngayTra.format(formatter);

        String maDonTra = donTraHang != null ? donTraHang.getMaDonTraHang() : "";
        if (maDonTra == null) {
            maDonTra = "";
        }
        String numericCode = maDonTra.replaceAll("\\D", "");
        String last4 = numericCode.length() >= 4
                ? numericCode.substring(numericCode.length() - 4)
                : (numericCode.isEmpty() ? String.format("%04d", (int) (Math.random() * 10000)) : numericCode);

        String baseFileName = String.format("phieu-tra-%s-%s", datePart, last4);
        Path tempDir = Path.of(System.getProperty("java.io.tmpdir"));
        Path tempFile = tempDir.resolve(baseFileName + ".pdf");

        int counter = 1;
        while (Files.exists(tempFile)) {
            tempFile = tempDir.resolve(baseFileName + "-" + counter++ + ".pdf");
        }

        Files.write(tempFile, pdfData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
        tempFile.toFile().deleteOnExit();

        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new IOException("Máy tính không hỗ trợ mở PDF tự động.");
        }

        Desktop.getDesktop().open(tempFile.toFile());
        return tempFile.toAbsolutePath().toString();
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
     * Làm panel chớp màu đỏ và scroll đến vị trí panel để người dùng biết sản
     * phẩm đã tồn tại trong danh sách trả hàng
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
        final int[] blinkCount = {0};

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
                // timer.stop();
                // timer.stop();
                // timer.stop();
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

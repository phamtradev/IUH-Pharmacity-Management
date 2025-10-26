/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhaCungCapBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyPhieuNhapHang extends javax.swing.JPanel {
    private SanPhamBUS sanPhamBUS;
    private SanPhamDAO sanPhamDAO;
    private LoHangBUS loHangBUS;
    private NhaCungCapBUS nhaCungCapBUS;
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;

    public GD_QuanLyPhieuNhapHang() {
        sanPhamDAO = new SanPhamDAO();
        sanPhamBUS = new SanPhamBUS(sanPhamDAO);
        loHangBUS = new LoHangBUS();
        currencyFormat = new DecimalFormat("#,###");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        initComponents();
        lookAndFeelSet();
        setupPanelSanPham();

        txtSearchProduct.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí hoặc quét mã vạch");
        txtSearchSupplier.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại nhà cung cấp");
    }
    
    private void setupPanelSanPham() {
        // Setup panel chứa danh sách sản phẩm
        pnSanPham.setLayout(new BoxLayout(pnSanPham, BoxLayout.Y_AXIS));
        pnSanPham.setBackground(Color.WHITE);
        
        // Tạo header cho danh sách sản phẩm
        javax.swing.JPanel headerPanel = createHeaderPanel();
        pnSanPham.add(headerPanel);
        pnSanPham.add(Box.createVerticalStrut(5));
    }
    
    private javax.swing.JPanel createHeaderPanel() {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setMaximumSize(new java.awt.Dimension(32767, 40));
        panel.setPreferredSize(new java.awt.Dimension(1000, 40));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(5, 8, 5, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        
        // Headers: Hình → Tên SP → Lô hàng/HSD → Số lượng → Đơn giá → Tổng tiền → Xóa
        String[] headers = {"Hình", "Tên sản phẩm", "Lô hàng / HSD", "Số lượng", "Đơn giá nhập", "Tổng tiền", ""};
        double[] widths = {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        int[] minWidths = {80, 200, 170, 120, 130, 130, 90};
        
        for (int i = 0; i < headers.length; i++) {
            javax.swing.JLabel label = new javax.swing.JLabel(headers[i]);
            label.setFont(new java.awt.Font("Segoe UI", 1, 13));
            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            label.setPreferredSize(new java.awt.Dimension(minWidths[i], 40));
            label.setMinimumSize(new java.awt.Dimension(minWidths[i], 40));
            
            gbc.gridx = i;
            gbc.weightx = widths[i];
            panel.add(label, gbc);
        }
        
        return panel;
    }
    
    private void themSanPhamVaoPanelNhap(SanPham sanPham) {
        Panel_ChiTietSanPhamNhap panelSP = new Panel_ChiTietSanPhamNhap(sanPham);
        
        // Listener để cập nhật tổng tiền
        panelSP.addPropertyChangeListener("tongTien", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTongTienHang();
            }
        });
        
        pnSanPham.add(panelSP);
        pnSanPham.add(Box.createVerticalStrut(2));
        pnSanPham.revalidate();
        pnSanPham.repaint();
        
        updateTongTienHang();
    }
    
    private void themSanPhamVaoPanelNhap(SanPham sanPham, int soLuong, double donGiaNhap, Date hanDung, String loHang) {
        Panel_ChiTietSanPhamNhap panelSP = new Panel_ChiTietSanPhamNhap(sanPham, soLuong, donGiaNhap, hanDung, loHang);
        
        // Listener để cập nhật tổng tiền
        panelSP.addPropertyChangeListener("tongTien", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTongTienHang();
            }
        });
        
        pnSanPham.add(panelSP);
        pnSanPham.add(Box.createVerticalStrut(2));
        pnSanPham.revalidate();
        pnSanPham.repaint();
        
        updateTongTienHang();
    }
    
    private void updateTongTienHang() {
        double tongTien = 0;
        
        // Duyệt qua các panel chi tiết sản phẩm
        for (Component comp : pnSanPham.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamNhap) {
                Panel_ChiTietSanPhamNhap panel = (Panel_ChiTietSanPhamNhap) comp;
                tongTien += panel.getTongTien();
            }
        }
        
        txtTotalPrice.setText(currencyFormat.format(tongTien) + " đ");
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

        pnMid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnSanPham = new javax.swing.JPanel();
        pnLeft = new javax.swing.JPanel();
        btnConfirmPurchase = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblSupplierId = new javax.swing.JLabel();
        txtSupplierId = new javax.swing.JTextField();
        txtSearchSupplier = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lblSupplierId1 = new javax.swing.JLabel();
        txtSupplierName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        lblSupplierId2 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JTextField();
        headerPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtSearchProduct = new javax.swing.JTextField();
        btnMa = new javax.swing.JButton();
        btnImportExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnMid.setMinimumSize(new java.awt.Dimension(200, 200));
        pnMid.setOpaque(false);
        pnMid.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pnSanPham = new javax.swing.JPanel();
        pnSanPham.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(pnSanPham);

        javax.swing.GroupLayout pnMidLayout = new javax.swing.GroupLayout(pnMid);
        pnMid.setLayout(pnMidLayout);
        pnMidLayout.setHorizontalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1433, Short.MAX_VALUE)
        );
        pnMidLayout.setVerticalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );

        add(pnMid, java.awt.BorderLayout.CENTER);

        pnLeft.setBackground(new java.awt.Color(255, 255, 255));
        pnLeft.setPreferredSize(new java.awt.Dimension(485, 650));

        btnConfirmPurchase.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnConfirmPurchase.setText("Nhập hàng ( F8 )");
        btnConfirmPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmPurchaseActionPerformed(evt);
            }
        });
        btnConfirmPurchase.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnConfirmPurchaseKeyPressed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblSupplierId.setFont(lblSupplierId.getFont().deriveFont(lblSupplierId.getFont().getStyle() | java.awt.Font.BOLD, lblSupplierId.getFont().getSize()+2));
        lblSupplierId.setText("Mã nhà cung cấp:");

        txtSupplierId.setEditable(false);
        txtSupplierId.setFont(txtSupplierId.getFont().deriveFont(txtSupplierId.getFont().getSize()+3f));
        txtSupplierId.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupplierId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierId, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtSupplierId)
                        .addContainerGap())))
        );

        txtSearchSupplier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchSupplierActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        lblSupplierId1.setFont(lblSupplierId1.getFont().deriveFont(lblSupplierId1.getFont().getStyle() | java.awt.Font.BOLD, lblSupplierId1.getFont().getSize()+2));
        lblSupplierId1.setText("Tên nhà cung cấp:");

        txtSupplierName.setEditable(false);
        txtSupplierName.setFont(txtSupplierName.getFont().deriveFont(txtSupplierName.getFont().getSize()+3f));
        txtSupplierName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupplierId1)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(txtSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierId1, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(txtSupplierName)
                        .addContainerGap())))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        lblSupplierId2.setFont(lblSupplierId2.getFont().deriveFont(lblSupplierId2.getFont().getStyle() | java.awt.Font.BOLD, lblSupplierId2.getFont().getSize()+2));
        lblSupplierId2.setText("Tổng tiền:");

        txtTotalPrice.setEditable(false);
        txtTotalPrice.setFont(txtTotalPrice.getFont().deriveFont(txtTotalPrice.getFont().getSize()+3f));
        txtTotalPrice.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupplierId2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierId2, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotalPrice)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnLeftLayout.createSequentialGroup()
                        .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSearchSupplier, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(50, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnLeftLayout.createSequentialGroup()
                        .addComponent(btnConfirmPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(txtSearchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 425, Short.MAX_VALUE)
                .addComponent(btnConfirmPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        add(pnLeft, java.awt.BorderLayout.EAST);

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        txtSearchProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchProductActionPerformed(evt);
            }
        });
        txtSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchProductKeyPressed(evt);
            }
        });

        btnMa.setBackground(new java.awt.Color(115, 165, 71));
        btnMa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMa.setForeground(new java.awt.Color(255, 255, 255));
        btnMa.setText("Nhập");
        btnMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaActionPerformed(evt);
            }
        });

        btnImportExcel.setBackground(new java.awt.Color(0, 123, 255));
        btnImportExcel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnImportExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnImportExcel.setText("Import Excel");
        btnImportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImportExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        headerPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchProductActionPerformed
        btnMaActionPerformed(evt); // Nhấn Enter sẽ thực hiện chức năng "Nhập"
    }//GEN-LAST:event_txtSearchProductActionPerformed

    private void btnMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaActionPerformed
        String textTim = txtSearchProduct.getText().trim();
        if (textTim.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm hoặc quét mã vạch!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tìm sản phẩm theo mã hoặc số đăng ký
        SanPham sanPham = null;
        try {
            // Thử tìm theo mã sản phẩm
            sanPham = sanPhamBUS.laySanPhamTheoMa(textTim);
        } catch (Exception e) {
            // Nếu không tìm thấy theo mã, thử tìm theo số đăng ký
            var optional = sanPhamBUS.timSanPhamTheoSoDangKy(textTim);
            if (optional.isPresent()) {
                sanPham = optional.get();
            }
        }
        
        if (sanPham == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã: " + textTim, "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Thêm sản phẩm vào panel
        themSanPhamVaoPanelNhap(sanPham);
        txtSearchProduct.setText("");
        txtSearchProduct.requestFocus();
    }//GEN-LAST:event_btnMaActionPerformed

    private void btnImportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportExcelActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx", "xls"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            importFromExcel(file);
        }
    }//GEN-LAST:event_btnImportExcelActionPerformed
    
    private void importFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int errorCount = 0;
            int newBatchCount = 0; // Đếm số lô hàng mới được tạo
            boolean nhaCungCapDaTao = false; // Đánh dấu nhà cung cấp đã được tạo
            StringBuilder errors = new StringBuilder();
            
            // Đọc header row để lấy vị trí các cột
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                JOptionPane.showMessageDialog(this, "File Excel không có dòng tiêu đề!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tìm index của các cột theo header (sản phẩm + nhà cung cấp)
            int colMaSP = -1, colTenSP = -1, colSoLuong = -1, colDonGia = -1, 
                colHanDung = -1, colSoLo = -1;
            int colMaNCC = -1, colTenNCC = -1, colDiaChi = -1, 
                colSDT = -1, colEmail = -1, colMaSoThue = -1;
            
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) continue;
                String header = getCellValueAsString(cell).trim().toLowerCase();
                
                // Các cột sản phẩm
                if (header.contains("mã") && header.contains("sản phẩm")) {
                    colMaSP = i;
                } else if (header.contains("tên") && header.contains("sản phẩm")) {
                    colTenSP = i;
                } else if (header.contains("số lượng")) {
                    colSoLuong = i;
                } else if (header.contains("đơn giá") && header.contains("nhập")) {
                    colDonGia = i;
                } else if (header.contains("hạn") && (header.contains("dùng") || header.contains("sử dụng"))) {
                    colHanDung = i;
                } else if (header.contains("số lô") || header.contains("lô hàng")) {
                    colSoLo = i;
                }
                // Các cột nhà cung cấp
                else if (header.contains("mã") && header.contains("ncc")) {
                    colMaNCC = i;
                } else if (header.contains("tên") && header.contains("ncc")) {
                    colTenNCC = i;
                } else if (header.contains("địa chỉ")) {
                    colDiaChi = i;
                } else if (header.contains("sđt") || (header.contains("số") && header.contains("điện thoại"))) {
                    colSDT = i;
                } else if (header.contains("email")) {
                    colEmail = i;
                } else if (header.contains("mã") && header.contains("thuế")) {
                    colMaSoThue = i;
                }
            }
            
            // Kiểm tra các cột bắt buộc
            if (colMaSP == -1 || colSoLuong == -1 || colDonGia == -1) {
                JOptionPane.showMessageDialog(this, 
                    "File Excel thiếu các cột bắt buộc: Mã sản phẩm, Số lượng, Đơn giá nhập!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Đọc thông tin nhà cung cấp từ dòng đầu tiên (nếu có)
            NhaCungCap nhaCungCap = null;
            if (sheet.getLastRowNum() > 0) {
                Row firstDataRow = sheet.getRow(1);
                if (firstDataRow != null) {
                    try {
                        nhaCungCap = xuLyThongTinNhaCungCap(firstDataRow, 
                            colMaNCC, colTenNCC, colDiaChi, colSDT, colEmail, colMaSoThue);
                        if (nhaCungCap != null) {
                            nhaCungCapDaTao = true;
                            // Điền thông tin nhà cung cấp vào form
                            txtSupplierId.setText(nhaCungCap.getMaNhaCungCap());
                            txtSupplierName.setText(nhaCungCap.getTenNhaCungCap());
                            System.out.println("✓ Đã tải thông tin nhà cung cấp: " + nhaCungCap.getTenNhaCungCap());
                        }
                    } catch (Exception ex) {
                        errors.append("Lỗi xử lý thông tin nhà cung cấp: ").append(ex.getMessage()).append("\n");
                    }
                }
            }
            
            // Đọc dữ liệu sản phẩm từ dòng 1 trở đi
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    // Đọc dữ liệu từ Excel theo header
                    String maSP = getCellValueAsString(row.getCell(colMaSP));
                    if (maSP == null || maSP.isEmpty()) continue;
                    
                    int soLuong = (int) getCellValueAsNumber(row.getCell(colSoLuong));
                    double donGiaNhap = getCellValueAsNumber(row.getCell(colDonGia));
                    
                    Date hanDung = null;
                    if (colHanDung != -1) {
                        hanDung = getCellValueAsDate(row.getCell(colHanDung));
                    }
                    if (hanDung == null) {
                        // Mặc định 2 năm sau nếu không có
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.YEAR, 2);
                        hanDung = cal.getTime();
                    }
                    
                    String soLo = null;
                    if (colSoLo != -1) {
                        soLo = getCellValueAsString(row.getCell(colSoLo));
                    }
                    
                    // Tìm sản phẩm
                    SanPham sanPham = null;
                    try {
                        sanPham = sanPhamBUS.laySanPhamTheoMa(maSP);
                    } catch (Exception ex) {
                        // Thử tìm theo số đăng ký
                        var optional = sanPhamBUS.timSanPhamTheoSoDangKy(maSP);
                        if (optional.isPresent()) {
                            sanPham = optional.get();
                        }
                    }
                    
                    if (sanPham == null) {
                        errors.append("Dòng ").append(i + 1).append(": Không tìm thấy sản phẩm ").append(maSP).append("\n");
                        errorCount++;
                        continue;
                    }
                    
                    // Nếu có số lô, tự động tạo lô mới nếu chưa tồn tại
                    if (soLo != null && !soLo.trim().isEmpty()) {
                        try {
                            boolean loMoiDuocTao = taLoHangTuDongNeuChua(sanPham, soLo.trim(), hanDung, soLuong);
                            if (loMoiDuocTao) {
                                newBatchCount++;
                                System.out.println("✓ Đã tạo lô hàng mới: " + soLo.trim() + " cho sản phẩm " + sanPham.getTenSanPham());
                            }
                        } catch (Exception ex) {
                            errors.append("Dòng ").append(i + 1).append(": Lỗi tạo lô hàng - ").append(ex.getMessage()).append("\n");
                            errorCount++;
                            continue;
                        }
                    }
                    
                    // Thêm sản phẩm vào panel
                    themSanPhamVaoPanelNhap(sanPham, soLuong, donGiaNhap, hanDung, soLo);
                    successCount++;
                    
                } catch (Exception e) {
                    errors.append("Dòng ").append(i + 1).append(": ").append(e.getMessage()).append("\n");
                    errorCount++;
                }
            }
            
            // Hiển thị kết quả
            String message = "✓ Import thành công " + successCount + " sản phẩm";
            if (nhaCungCapDaTao && nhaCungCap != null) {
                message += "\n✓ Đã tải thông tin nhà cung cấp: " + nhaCungCap.getTenNhaCungCap();
            }
            if (newBatchCount > 0) {
                message += "\n✓ Đã tạo mới " + newBatchCount + " lô hàng vào CSDL";
            }
            if (errorCount > 0) {
                message += "\n⚠ Có " + errorCount + " lỗi:\n" + errors.toString();
            }
            JOptionPane.showMessageDialog(this, message, "Kết quả import", 
                errorCount > 0 ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return "";
        }
    }
    
    private double getCellValueAsNumber(Cell cell) {
        if (cell == null) return 0;
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().replaceAll("[^0-9.]", ""));
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }
    
    private Date getCellValueAsDate(Cell cell) {
        if (cell == null) {
            // Mặc định 2 năm sau
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 2);
            return cal.getTime();
        }
        
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return dateFormat.parse(cell.getStringCellValue());
            } catch (Exception e) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 2);
                return cal.getTime();
            }
        }
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        return cal.getTime();
    }
    
    /**
     * Xử lý thông tin nhà cung cấp từ Excel:
     * - Nếu có mã NCC: tìm kiếm trong DB, nếu không có thì tạo mới
     * - Nếu không có mã nhưng có tên: tìm theo tên, nếu không có thì tạo mới
     * - Nếu tạo mới thì lưu vào database
     * @return NhaCungCap object hoặc null nếu không có thông tin
     */
    private NhaCungCap xuLyThongTinNhaCungCap(Row row, int colMaNCC, int colTenNCC, 
            int colDiaChi, int colSDT, int colEmail, int colMaSoThue) throws Exception {
        
        // Đọc thông tin từ Excel
        String maNCC = (colMaNCC != -1) ? getCellValueAsString(row.getCell(colMaNCC)) : null;
        String tenNCC = (colTenNCC != -1) ? getCellValueAsString(row.getCell(colTenNCC)) : null;
        String diaChi = (colDiaChi != -1) ? getCellValueAsString(row.getCell(colDiaChi)) : null;
        String sdt = (colSDT != -1) ? getCellValueAsString(row.getCell(colSDT)) : null;
        String email = (colEmail != -1) ? getCellValueAsString(row.getCell(colEmail)) : null;
        String maSoThue = (colMaSoThue != -1) ? getCellValueAsString(row.getCell(colMaSoThue)) : null;
        
        // Nếu không có thông tin gì về nhà cung cấp
        if ((maNCC == null || maNCC.trim().isEmpty()) && 
            (tenNCC == null || tenNCC.trim().isEmpty())) {
            return null;
        }
        
        // Khởi tạo NhaCungCapBUS nếu chưa có
        if (nhaCungCapBUS == null) {
            nhaCungCapBUS = new NhaCungCapBUS();
        }
        
        NhaCungCap ncc = null;
        
        // Trường hợp 1: Có mã NCC - tìm theo mã
        if (maNCC != null && !maNCC.trim().isEmpty()) {
            try {
                ncc = nhaCungCapBUS.layNhaCungCapTheoMa(maNCC.trim());
                System.out.println("✓ Tìm thấy nhà cung cấp theo mã: " + maNCC);
                return ncc;
            } catch (Exception e) {
                // Không tìm thấy theo mã, tiếp tục xử lý tạo mới
                System.out.println("→ Không tìm thấy NCC theo mã " + maNCC + ", sẽ tạo mới");
            }
        }
        
        // Trường hợp 2: Có tên NCC - tìm theo tên hoặc SĐT
        if (tenNCC != null && !tenNCC.trim().isEmpty()) {
            // Tìm theo tên
            ncc = nhaCungCapBUS.layNhaCungCapTheoTen(tenNCC.trim());
            if (ncc != null) {
                System.out.println("✓ Tìm thấy nhà cung cấp theo tên: " + tenNCC);
                return ncc;
            }
            
            // Tìm theo SĐT (nếu có)
            if (sdt != null && !sdt.trim().isEmpty()) {
                ncc = nhaCungCapBUS.layNhaCungCapTheoSoDienThoai(sdt.trim());
                if (ncc != null) {
                    System.out.println("✓ Tìm thấy nhà cung cấp theo SĐT: " + sdt);
                    return ncc;
                }
            }
        }
        
        // Trường hợp 3: Không tìm thấy -> Tạo mới
        if (tenNCC == null || tenNCC.trim().isEmpty()) {
            throw new Exception("Không thể tạo nhà cung cấp mới: thiếu tên nhà cung cấp");
        }
        
        // Validate thông tin trước khi tạo
        if (sdt != null && !sdt.trim().isEmpty() && !sdt.trim().matches(NhaCungCap.SO_DIEN_THOAI_REGEX)) {
            throw new Exception("Số điện thoại không đúng định dạng: " + sdt);
        }
        
        if (email != null && !email.trim().isEmpty() && !email.trim().matches(NhaCungCap.EMAIL_REGEX)) {
            throw new Exception("Email không đúng định dạng: " + email);
        }
        
        // Tạo nhà cung cấp mới
        NhaCungCap nccMoi = new NhaCungCap();
        
        try {
            nccMoi.setTenNhaCungCap(tenNCC.trim());
            
            // Set các trường optional (chỉ set nếu có giá trị)
            if (diaChi != null && !diaChi.trim().isEmpty()) {
                nccMoi.setDiaChi(diaChi.trim());
            }
            
            if (sdt != null && !sdt.trim().isEmpty()) {
                nccMoi.setSoDienThoai(sdt.trim());
            }
            
            if (email != null && !email.trim().isEmpty()) {
                nccMoi.setEmail(email.trim());
            }
            
            if (maSoThue != null && !maSoThue.trim().isEmpty()) {
                nccMoi.setMaSoThue(maSoThue.trim());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi validate thông tin nhà cung cấp: " + e.getMessage());
        }
        
        // Lưu vào database
        System.out.println("→ Đang lưu nhà cung cấp mới vào database...");
        boolean success = nhaCungCapBUS.taoNhaCungCap(nccMoi);
        if (!success) {
            throw new Exception("Không thể lưu nhà cung cấp mới vào database: " + tenNCC + ". Kiểm tra console để biết chi tiết lỗi.");
        }
        
        System.out.println("✓ Đã tạo nhà cung cấp mới: " + nccMoi.getTenNhaCungCap() + " (Mã: " + nccMoi.getMaNhaCungCap() + ")");
        return nccMoi;
    }
    
    /**
     * Tự động tạo lô hàng mới nếu chưa tồn tại trong database
     * @param sanPham Sản phẩm cần tạo lô
     * @param tenLoHang Tên lô hàng từ Excel
     * @param hanDung Hạn sử dụng từ Excel
     * @param tonKho Tồn kho ban đầu (số lượng nhập)
     * @return true nếu tạo lô mới, false nếu lô đã tồn tại
     */
    private boolean taLoHangTuDongNeuChua(SanPham sanPham, String tenLoHang, Date hanDung, int tonKho) throws Exception {
        if (sanPham == null || tenLoHang == null || tenLoHang.trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra xem lô hàng đã tồn tại chưa (theo tên lô và sản phẩm)
        List<LoHang> danhSachLo = loHangBUS.getLoHangBySanPham(sanPham);
        boolean daTonTai = danhSachLo.stream()
                .anyMatch(lo -> lo.getTenLoHang().equalsIgnoreCase(tenLoHang));
        
        if (daTonTai) {
            // Lô đã tồn tại, không cần tạo mới
            return false;
        }
        
        // Tạo lô hàng mới
        LoHang loHangMoi = new LoHang();
        loHangMoi.setTenLoHang(tenLoHang);
        
        // Convert Date sang LocalDate cho hanSuDung
        java.time.LocalDate hanSuDung = hanDung.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        loHangMoi.setHanSuDung(hanSuDung);
        
        // Tồn kho ban đầu = số lượng nhập từ Excel
        loHangMoi.setTonKho(tonKho);
        
        // Trạng thái mặc định: đang hoạt động
        loHangMoi.setTrangThai(true);
        
        // Gán sản phẩm
        loHangMoi.setSanPham(sanPham);
        
        // Lưu vào database
        boolean thanhCong = loHangBUS.themLoHang(loHangMoi);
        
        if (!thanhCong) {
            throw new Exception("Không thể lưu lô hàng '" + tenLoHang + "' vào database");
        }
        
        return true; // Đã tạo lô mới thành công
    }

    private void btnConfirmPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmPurchaseActionPerformed
        // TODO: Logic tạo phiếu nhập hàng
    }//GEN-LAST:event_btnConfirmPurchaseActionPerformed

    private void txtSearchSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchSupplierActionPerformed

    }//GEN-LAST:event_txtSearchSupplierActionPerformed

    private void txtSearchProductKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchProductKeyPressed

    }//GEN-LAST:event_txtSearchProductKeyPressed

    private void btnConfirmPurchaseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnConfirmPurchaseKeyPressed

    }//GEN-LAST:event_btnConfirmPurchaseKeyPressed

    private double tongTienHang;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmPurchase;
    private javax.swing.JButton btnImportExcel;
    private javax.swing.JButton btnMa;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSupplierId;
    private javax.swing.JLabel lblSupplierId1;
    private javax.swing.JLabel lblSupplierId2;
    private javax.swing.JPanel pnLeft;
    private javax.swing.JPanel pnMid;
    private javax.swing.JPanel pnSanPham;
    private javax.swing.JTextField txtSearchProduct;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtSupplierId;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JTextField txtTotalPrice;
    // End of variables declaration//GEN-END:variables

}

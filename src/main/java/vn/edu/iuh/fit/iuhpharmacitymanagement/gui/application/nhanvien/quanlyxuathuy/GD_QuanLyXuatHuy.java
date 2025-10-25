/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyxuathuy;

import java.awt.Component;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyXuatHuy extends javax.swing.JPanel {

    /**
     * Creates new form TabHoaDon
     */
    public GD_QuanLyXuatHuy() {
        initComponents();
        fillContent();
    }

    @SuppressWarnings("unchecked")
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
        headerProductPanel.add(createHeaderLabel("Lý do", 90, javax.swing.SwingConstants.LEFT), gbc);

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

    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Tạo font chữ
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");

        font.setFontHeightInPoints((short) 14);

        // Tạo CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        return cellStyle;
    }

    private void btnTaoPhieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoPhieuActionPerformed
        // TODO: Logic lưu phiếu xuất hủy vào database (làm sau)
        JOptionPane.showMessageDialog(this,
                "Chức năng lưu phiếu xuất hủy sẽ được thực hiện sau!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnTaoPhieuActionPerformed

    /**
     * Load tự động các sản phẩm hết hạn khi mở màn hình
     */
    private void loadSanPhamHetHan() {
        // TODO: Truy vấn database để lấy danh sách sản phẩm hết hạn
        // Tạm thời dùng dữ liệu mẫu
        themSanPhamMau();
    }

    /**
     * Thêm sản phẩm mẫu (test)
     */
    private void themSanPhamMau() {
        // Sản phẩm 1
        Panel_ChiTietSanPhamXuatHuy panel1 = new Panel_ChiTietSanPhamXuatHuy();
        panel1.setTenSanPham("Paracetamol 500mg");
        panel1.setLoHang("LO001", "15/10/2024", 150);
        panel1.setDonVi("Viên");
        panel1.setDonGia(5000);
        panel1.setSoLuongHuy(10);
        panel1.setLyDoXuatHuy("Hết hạn sử dụng");
        pnContent.add(panel1);

        // Sản phẩm 2
        Panel_ChiTietSanPhamXuatHuy panel2 = new Panel_ChiTietSanPhamXuatHuy();
        panel2.setTenSanPham("Vitamin C 1000mg");
        panel2.setLoHang("LO002", "20/09/2024", 80);
        panel2.setDonVi("Viên");
        panel2.setDonGia(8000);
        panel2.setSoLuongHuy(5);
        panel2.setLyDoXuatHuy("Hết hạn sử dụng");
        pnContent.add(panel2);

        // Sản phẩm 3
        Panel_ChiTietSanPhamXuatHuy panel3 = new Panel_ChiTietSanPhamXuatHuy();
        panel3.setTenSanPham("Amoxicillin 500mg");
        panel3.setLoHang("LO003", "05/08/2024", 200);
        panel3.setDonVi("Viên");
        panel3.setDonGia(12000);
        panel3.setSoLuongHuy(15);
        panel3.setLyDoXuatHuy("Hết hạn sử dụng");
        pnContent.add(panel3);

        pnContent.revalidate();
        pnContent.repaint();

        // Cập nhật tổng tiền
        updateTongTien();
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

    private void setTxtEmpty(JTextField... labels) {

    }

    private void clearPnOrderDetail() {
        setTxtEmpty(txtTongTien);
        pnContent.removeAll();
        pnContent.revalidate();
        pnContent.repaint();
    }

    public void changeTongTienHoaDon() {

    }
    private void jLabel5AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel5AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5AncestorAdded

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

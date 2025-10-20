/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPhamDaMua extends javax.swing.JPanel {

    private ProductClickListener clickListener;
    
    public interface ProductClickListener {
        void onProductClicked(Panel_ChiTietSanPhamDaMua productPanel);
    }

    public Panel_ChiTietSanPhamDaMua() {
        initComponents();
        setupClickable();
    }

    public void setProductClickListener(ProductClickListener listener) {
        this.clickListener = listener;
    }

    private void setupClickable() {
        // Làm cho toàn bộ panel có thể click được
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.onProductClicked(Panel_ChiTietSanPhamDaMua.this);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new java.awt.Color(240, 248, 255)); // Màu xanh nhạt khi hover
                setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new java.awt.Color(255, 255, 255)); // Trở về màu trắng
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
    }

    // Getters và Setters cho dữ liệu sản phẩm
    public String getTenSanPham() {
        return lblTenSP.getText();
    }

    public void setTenSanPham(String ten) {
        lblTenSP.setText(ten);
    }

    public String getDonVi() {
        return lblDonVi.getText();
    }

    public void setDonVi(String donVi) {
        lblDonVi.setText(donVi);
    }

    public int getSoLuong() {
        return (int) spinnerSoLuong.getValue();
    }

    public void setSoLuong(int soLuong) {
        spinnerSoLuong.setValue(soLuong);
    }

    public double getDonGia() {
        return Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9.]", ""));
    }

    public void setDonGia(double donGia) {
        txtDonGia.setText(String.format("%,.0f ₫", donGia));
        updateTongTien();
    }

    public double getTongTien() {
        return Double.parseDouble(txtTongTien.getText().replaceAll("[^0-9.]", ""));
    }

    private void updateTongTien() {
        try {
            int soLuong = (int) spinnerSoLuong.getValue();
            double donGia = getDonGia();
            double tongTien = soLuong * donGia;
            txtTongTien.setText(String.format("%,.0f ₫", tongTien));
        } catch (Exception e) {
            txtTongTien.setText("0 ₫");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 232, 232)));
        setMaximumSize(new java.awt.Dimension(32767, 100));
        setMinimumSize(new java.awt.Dimension(800, 100));
        setPreferredSize(new java.awt.Dimension(1000, 100));
        setRequestFocusEnabled(false);
        
        // Sử dụng GridBagLayout để các cột thẳng hàng
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 8, 10, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh sản phẩm
        javax.swing.JLabel lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(80, 80));
        lblHinh.setMinimumSize(new java.awt.Dimension(80, 80));
        lblHinh.setMaximumSize(new java.awt.Dimension(80, 80));
        lblHinh.setBackground(new java.awt.Color(240, 240, 240));
        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        lblHinh.setOpaque(true);
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("IMG");
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        add(lblHinh, gbc);

        // 2. Tên sản phẩm
        lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTenSP.setText("Tên sản phẩm");
        lblTenSP.setPreferredSize(new java.awt.Dimension(180, 80));
        lblTenSP.setMinimumSize(new java.awt.Dimension(180, 80));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Đơn vị
        lblDonVi = new javax.swing.JLabel();
        lblDonVi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblDonVi.setText("Hộp");
        lblDonVi.setPreferredSize(new java.awt.Dimension(80, 80));
        lblDonVi.setMinimumSize(new java.awt.Dimension(80, 80));
        lblDonVi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDonVi.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(lblDonVi, gbc);

        // 4. Số lượng (Spinner) - Chỉ hiển thị, không cho chỉnh sửa
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        spinnerSoLuong = new javax.swing.JSpinner();
        spinnerSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 14));
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        spinnerSoLuong.setPreferredSize(new java.awt.Dimension(60, 35));
        spinnerSoLuong.setEnabled(false); // Không cho chỉnh sửa
        pnSpinner.add(spinnerSoLuong);
        pnSpinner.setPreferredSize(new java.awt.Dimension(80, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(80, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 5. Đơn giá
        txtDonGia = new javax.swing.JLabel();
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDonGia.setText("16,500 ₫");
        txtDonGia.setPreferredSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 6. Tổng tiền
        txtTongTien = new javax.swing.JLabel();
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 16));
        txtTongTien.setForeground(new java.awt.Color(0, 120, 215));
        txtTongTien.setText("49,500 ₫");
        txtTongTien.setPreferredSize(new java.awt.Dimension(120, 100));
        txtTongTien.setMinimumSize(new java.awt.Dimension(120, 100));
        txtTongTien.setMaximumSize(new java.awt.Dimension(120, 100));
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTien.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtTongTien, gbc);

        // 7. Icon trạng thái (checkmark)
        javax.swing.JLabel lblStatus = new javax.swing.JLabel();
        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblStatus.setForeground(new java.awt.Color(255, 193, 7));
        lblStatus.setText("✓");
        lblStatus.setPreferredSize(new java.awt.Dimension(50, 100));
        lblStatus.setMinimumSize(new java.awt.Dimension(50, 100));
        lblStatus.setMaximumSize(new java.awt.Dimension(50, 100));
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(lblStatus, gbc);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblDonVi;
    private javax.swing.JSpinner spinnerSoLuong;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}

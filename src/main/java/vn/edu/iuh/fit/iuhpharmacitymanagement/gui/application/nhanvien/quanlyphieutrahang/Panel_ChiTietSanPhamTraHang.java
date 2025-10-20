/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang;


/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPhamTraHang extends javax.swing.JPanel {

    private String lyDoTraHang = "";

    public Panel_ChiTietSanPhamTraHang() {
        initComponents();
    }

    public int getSoLuongTra() {
        return (int) spinnerSoLuongTra.getValue();
    }

    public void setSoLuongTra(int soLuong) {
        spinnerSoLuongTra.setValue(soLuong);
    }

    public void setTenSanPham(String ten) {
        // Tìm và cập nhật label tên sản phẩm
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof javax.swing.JLabel) {
                javax.swing.JLabel label = (javax.swing.JLabel) getComponent(i);
                if (label.getText().equals("Tên sản phẩm")) {
                    label.setText(ten);
                    break;
                }
            }
        }
    }

    public void setDonVi(String donVi) {
        // Tìm và cập nhật label đơn vị
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof javax.swing.JLabel) {
                javax.swing.JLabel label = (javax.swing.JLabel) getComponent(i);
                if (label.getText().equals("Đơn vị")) {
                    label.setText(donVi);
                    break;
                }
            }
        }
    }

    public void setDonGia(double donGia) {
        txtDonGia.setText(String.format("%,.0f ₫", donGia));
        updateTongTienTra();
    }

    public String getLyDoTraHang() {
        return lyDoTraHang;
    }

    public void setLyDoTraHang(String lyDo) {
        this.lyDoTraHang = lyDo;
        if (lyDo != null && !lyDo.trim().isEmpty()) {
            btnLyDo.setText("✓ Lý do");
            btnLyDo.setBackground(new java.awt.Color(40, 167, 69));
        } else {
            btnLyDo.setText("Lý do");
            btnLyDo.setBackground(new java.awt.Color(0, 123, 255));
        }
    }

    private void updateTongTienTra() {
        try {
            int soLuong = (int) spinnerSoLuongTra.getValue();
            double donGia = Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9.]", ""));
            double tongTien = soLuong * donGia;
            txtTongTienTra.setText(String.format("%,.0f ₫", tongTien));
        } catch (Exception e) {
            txtTongTienTra.setText("0 ₫");
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
        javax.swing.JLabel lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSP.setText("Tên sản phẩm");
        lblTenSP.setPreferredSize(new java.awt.Dimension(180, 80));
        lblTenSP.setMinimumSize(new java.awt.Dimension(180, 80));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Đơn vị
        javax.swing.JLabel lblDonVi = new javax.swing.JLabel();
        lblDonVi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDonVi.setText("Đơn vị");
        lblDonVi.setPreferredSize(new java.awt.Dimension(80, 80));
        lblDonVi.setMinimumSize(new java.awt.Dimension(80, 80));
        lblDonVi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDonVi.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(lblDonVi, gbc);

        // 4. Số lượng (Spinner) - Cột riêng
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        spinnerSoLuongTra = new javax.swing.JSpinner();
        spinnerSoLuongTra.setFont(new java.awt.Font("Segoe UI", 0, 14));
        spinnerSoLuongTra.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        spinnerSoLuongTra.setPreferredSize(new java.awt.Dimension(60, 35));
        spinnerSoLuongTra.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSoLuongTraStateChanged(evt);
            }
        });
        pnSpinner.add(spinnerSoLuongTra);
        pnSpinner.setPreferredSize(new java.awt.Dimension(80, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(80, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 4. Đơn giá
        txtDonGia = new javax.swing.JLabel();
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDonGia.setText("100,000 đ");
        txtDonGia.setPreferredSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 5. Tổng tiền trả
        txtTongTienTra = new javax.swing.JLabel();
        txtTongTienTra.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtTongTienTra.setForeground(new java.awt.Color(255, 0, 0));
        txtTongTienTra.setText("100,000 đ");
        txtTongTienTra.setPreferredSize(new java.awt.Dimension(120, 100));
        txtTongTienTra.setMinimumSize(new java.awt.Dimension(120, 100));
        txtTongTienTra.setMaximumSize(new java.awt.Dimension(120, 100));
        txtTongTienTra.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTienTra.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtTongTienTra, gbc);

        // 6. Panel chứa 2 nút (Lý do và Xóa) theo chiều dọc
        javax.swing.JPanel pnButtons = new javax.swing.JPanel();
        pnButtons.setBackground(java.awt.Color.WHITE);
        pnButtons.setLayout(new javax.swing.BoxLayout(pnButtons, javax.swing.BoxLayout.Y_AXIS));
        pnButtons.setPreferredSize(new java.awt.Dimension(70, 100));
        pnButtons.setMinimumSize(new java.awt.Dimension(70, 100));
        
        // Nút Lý do
        btnLyDo = new javax.swing.JButton();
        btnLyDo.setText("Lý do");
        btnLyDo.setFont(new java.awt.Font("Segoe UI", 0, 12));
        btnLyDo.setBackground(new java.awt.Color(0, 123, 255));
        btnLyDo.setForeground(java.awt.Color.WHITE);
        btnLyDo.setPreferredSize(new java.awt.Dimension(60, 30));
        btnLyDo.setMinimumSize(new java.awt.Dimension(60, 30));
        btnLyDo.setMaximumSize(new java.awt.Dimension(60, 30));
        btnLyDo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnLyDo.setFocusPainted(false);
        btnLyDo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLyDo.setBorderPainted(false);
        btnLyDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLyDoActionPerformed(evt);
            }
        });
        
        // Nút Xóa
        javax.swing.JButton btnXoa = new javax.swing.JButton();
        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 12));
        btnXoa.setBackground(new java.awt.Color(220, 53, 69));
        btnXoa.setForeground(java.awt.Color.WHITE);
        btnXoa.setPreferredSize(new java.awt.Dimension(60, 30));
        btnXoa.setMinimumSize(new java.awt.Dimension(60, 30));
        btnXoa.setMaximumSize(new java.awt.Dimension(60, 30));
        btnXoa.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setBorderPainted(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        
        // Thêm 2 nút vào panel theo chiều dọc
        pnButtons.add(javax.swing.Box.createVerticalGlue());
        pnButtons.add(btnLyDo);
        pnButtons.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 10)));
        pnButtons.add(btnXoa);
        pnButtons.add(javax.swing.Box.createVerticalGlue());
        
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(pnButtons, gbc);
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // Xóa panel này khỏi container cha
        java.awt.Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void spinnerSoLuongTraStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongTraStateChanged
        // Tính lại tổng tiền trả khi thay đổi số lượng
        updateTongTienTra();
    }//GEN-LAST:event_spinnerSoLuongTraStateChanged

    private void btnLyDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLyDoActionPerformed
        // Tìm Frame cha để hiển thị dialog
        java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
        java.awt.Frame frame = null;
        
        if (window instanceof java.awt.Frame) {
            frame = (java.awt.Frame) window;
        }
        
        // Tạo và hiển thị dialog
        Dialog_NhapLyDoTraHang dialog = new Dialog_NhapLyDoTraHang(frame, true);
        dialog.setLyDo(lyDoTraHang);
        dialog.setVisible(true);
        
        // Nếu người dùng xác nhận, lưu lý do
        if (dialog.isDaXacNhan()) {
            setLyDoTraHang(dialog.getLyDo());
        }
    }//GEN-LAST:event_btnLyDoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner spinnerSoLuongTra;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTienTra;
    private javax.swing.JButton btnLyDo;
    // End of variables declaration//GEN-END:variables
}

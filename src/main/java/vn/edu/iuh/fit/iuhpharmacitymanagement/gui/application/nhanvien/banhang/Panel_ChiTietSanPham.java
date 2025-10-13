/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import java.awt.Container;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPham extends javax.swing.JPanel {

    public Panel_ChiTietSanPham() {
        initComponents();
    }

    public int getSoLuong() {
        return (int) spinnerSoLuong.getValue();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogChonLo = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        btnXacNhan = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        pnChuaLo = new javax.swing.JPanel();
        spinnerSoLuong = new javax.swing.JSpinner();
        txtDonGia = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JLabel();

        dialogChonLo.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialogChonLo.setTitle("Chọn lô");
        dialogChonLo.setType(java.awt.Window.Type.POPUP);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(651, 285));

        btnXacNhan.setText("Xác nhận");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        scrollPane.setBorder(null);

        pnChuaLo.setBackground(new java.awt.Color(255, 255, 255));
        pnChuaLo.setLayout(new javax.swing.BoxLayout(pnChuaLo, javax.swing.BoxLayout.Y_AXIS));
        scrollPane.setViewportView(pnChuaLo);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(501, 501, 501)
                        .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout dialogChonLoLayout = new javax.swing.GroupLayout(dialogChonLo.getContentPane());
        dialogChonLo.getContentPane().setLayout(dialogChonLoLayout);
        dialogChonLoLayout.setHorizontalGroup(
            dialogChonLoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogChonLoLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        dialogChonLoLayout.setVerticalGroup(
            dialogChonLoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogChonLoLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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

        // 3. Panel chọn lô
        Panel_ChonLo panelChonLo = new Panel_ChonLo();
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(panelChonLo, gbc);

        // 4. Số lượng (Spinner) - Cột riêng
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        spinnerSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 14));
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        spinnerSoLuong.setPreferredSize(new java.awt.Dimension(60, 35));
        spinnerSoLuong.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSoLuongStateChanged(evt);
            }
        });
        pnSpinner.add(spinnerSoLuong);
        pnSpinner.setPreferredSize(new java.awt.Dimension(80, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(80, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 5. Giảm giá - Cột riêng
        txtDiscount.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDiscount.setForeground(new java.awt.Color(255, 0, 0));
        txtDiscount.setText("-5.00%");
        txtDiscount.setPreferredSize(new java.awt.Dimension(70, 100));
        txtDiscount.setMinimumSize(new java.awt.Dimension(70, 100));
        txtDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtDiscount.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDiscount, gbc);

        // 6. Đơn giá
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDonGia.setText("100,000 đ");
        txtDonGia.setPreferredSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(100, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 7. Tổng tiền
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(0, 120, 215));
        txtTongTien.setText("95,000 đ");
        txtTongTien.setPreferredSize(new java.awt.Dimension(120, 100));
        txtTongTien.setMinimumSize(new java.awt.Dimension(120, 100));
        txtTongTien.setMaximumSize(new java.awt.Dimension(120, 100));
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTien.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(txtTongTien, gbc);

        // 8. Nút Xóa (Chức năng)
        javax.swing.JPanel pnXoa = new javax.swing.JPanel();
        pnXoa.setBackground(java.awt.Color.WHITE);
        pnXoa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        javax.swing.JButton btnXoa = new javax.swing.JButton();
        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 13));
        btnXoa.setBackground(new java.awt.Color(220, 53, 69));
        btnXoa.setForeground(java.awt.Color.WHITE);
        btnXoa.setPreferredSize(new java.awt.Dimension(60, 35));
        btnXoa.setMinimumSize(new java.awt.Dimension(60, 35));
        btnXoa.setMaximumSize(new java.awt.Dimension(60, 35));
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setBorderPainted(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnXoa.add(btnXoa);
        pnXoa.setPreferredSize(new java.awt.Dimension(70, 100));
        pnXoa.setMinimumSize(new java.awt.Dimension(70, 100));
        gbc.gridx = 7;
        gbc.weightx = 0.0;
        add(pnXoa, gbc);
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

    private void spinnerSoLuongStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongStateChanged

    }//GEN-LAST:event_spinnerSoLuongStateChanged

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
//        List<BatchDTO> batchDTOs = getSelectedBatchDTO();
//        for (BatchDTO batchDTO : batchDTOs) {
//            pnListBatch.add(new PnSelectBatch(batchDTO, spinnerSoLuong));
//        }
//        pnListBatch.revalidate();
//        pnListBatch.repaint();
//        dialogChonLo.dispose();
//
//        int value = 0;
//        for (Component component : pnListBatch.getComponents()) {
//            if (component instanceof PnSelectBatch) {
//                PnSelectBatch pnSelectBatch = (PnSelectBatch) component;
//                value += pnSelectBatch.getBatchDTO().getQuantity();
//            }
//        }
//        spinnerSoLuong.setValue(value);
    }//GEN-LAST:event_btnXacNhanActionPerformed

//    private List<BatchDTO> getSelectedBatchDTO() {
//        List<BatchDTO> batchDTOs = new ArrayList<>();
//
//        for (Component component : pnChuaLo.getComponents()) {
//            if (component instanceof JPanel) {
//                JPanel panelContainer = (JPanel) component;
//
//                PnChonLo pnChonLo = null;
//                JSpinner spinner = null;
//
//                for (Component child : panelContainer.getComponents()) {
//                    if (child instanceof PnChonLo) {
//                        pnChonLo = (PnChonLo) child;
//                    } else if (child instanceof JSpinner) {
//                        spinner = (JSpinner) child;
//                    }
//                }
//
//                if (pnChonLo.getBtnTenLo().isSelected()) {
//                    Batch batch = (Batch) pnChonLo.getBatch();
//                    BatchDTO batchDTO = new BatchDTO();
//                    batchDTO.setName(batch.getName());
//                    batchDTO.setExpirationDate(batch.getExpirationDate());
//                    batchDTO.setStock(batch.getStock());
//                    batchDTO.setQuantity((int) spinner.getValue());
//                    batchDTOs.add(batchDTO);
//                }
//            }
//        }
//        return batchDTOs;  // Trả về null nếu không có gì được chọn
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JDialog dialogChonLo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnChuaLo;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JSpinner spinnerSoLuong;
    private javax.swing.JLabel txtDiscount;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}

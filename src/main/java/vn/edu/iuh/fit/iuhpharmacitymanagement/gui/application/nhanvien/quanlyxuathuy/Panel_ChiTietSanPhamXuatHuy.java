/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyxuathuy;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonTraHang;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPhamXuatHuy extends javax.swing.JPanel {

    private String lyDoXuatHuy = "";
    private LoHang loHang = null; // Lưu thông tin lô hàng
    private ChiTietDonTraHang chiTietDonTra = null; // Lưu thông tin chi tiết đơn trả (nếu là từ đơn trả)
    private javax.swing.JLabel lblHinh; // Lưu reference đến label hình ảnh

    public Panel_ChiTietSanPhamXuatHuy() {
        initComponents();
    }

    public int getSoLuongHuy() {
        return (int) spinnerSoLuongHuy.getValue();
    }

    public void setSoLuongHuy(int soLuong) {
        spinnerSoLuongHuy.setValue(soLuong);    
    }

    public void setTenSanPham(String ten) {
        lblTenSP.setText(ten);
    }
    
    public void setHinhAnh(String hinhAnh) {
        if (lblHinh != null) {
            if (hinhAnh != null && !hinhAnh.trim().isEmpty()) {
                try {
                    // Thử đường dẫn gốc trước (tuyệt đối)
                    java.io.File file = new java.io.File(hinhAnh);
                    if (file.exists()) {
                        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(hinhAnh);
                        java.awt.Image img = icon.getImage();
                        java.awt.Image scaledImg = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
                        lblHinh.setIcon(new javax.swing.ImageIcon(scaledImg));
                        lblHinh.setText("");
                        return;
                    }
                    
                    // Thử load từ resources folder trong classpath (ưu tiên)
                    try {
                        java.net.URL imageURL = getClass().getResource("/img/" + hinhAnh);
                        if (imageURL != null) {
                            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imageURL);
                            java.awt.Image img = icon.getImage();
                            java.awt.Image scaledImg = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
                            lblHinh.setIcon(new javax.swing.ImageIcon(scaledImg));
                            lblHinh.setText("");
                            return;
                        }
                    } catch (Exception ex) {
                        System.err.println("Không thể load hình từ classpath: " + ex.getMessage());
                    }
                    
                    // Nếu không tìm thấy, thử với relative path từ thư mục gốc project
                    file = new java.io.File("src/main/resources/img/" + hinhAnh);
                    if (file.exists()) {
                        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(file.getAbsolutePath());
                        java.awt.Image img = icon.getImage();
                        java.awt.Image scaledImg = img.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
                        lblHinh.setIcon(new javax.swing.ImageIcon(scaledImg));
                        lblHinh.setText("");
                        return;
                    }
                    
                    // Nếu không tìm thấy ở đâu cả
                    System.err.println("Không tìm thấy hình ảnh: " + hinhAnh);
                    lblHinh.setIcon(null);
                    lblHinh.setText("IMG");
                } catch (Exception e) {
                    System.err.println("Lỗi khi load hình ảnh: " + e.getMessage());
                    lblHinh.setIcon(null);
                    lblHinh.setText("IMG");
                }
            } else {
                lblHinh.setIcon(null);
                lblHinh.setText("IMG");
            }
        }
    }
    
    public void setLoHang(String tenLo, String hanSuDung, int tonKho) {
        if (lblLoHang != null) {
            // Nếu đã có prefix "Đơn trả:" thì không thêm "Lô:" nữa
            if (tenLo.startsWith("Đơn trả:")) {
                lblLoHang.setText(tenLo);
            } else {
                lblLoHang.setText("Lô: " + tenLo);
            }
        }
        if (lblHanSuDung != null) {
            // Nếu HSD là "N/A" thì ẩn label hoặc hiển thị đặc biệt
            if ("N/A".equals(hanSuDung) || hanSuDung == null || hanSuDung.isEmpty()) {
                lblHanSuDung.setText(""); // Ẩn HSD
            } else {
                lblHanSuDung.setText("HSD: " + hanSuDung);
            }
        }
        if (lblTonKho != null) {
            lblTonKho.setText("Tồn: " + tonKho);
        }
    }

    public void setDonVi(String donVi) {
        lblDonVi.setText(donVi);
    }

    public void setDonGia(double donGia) {
        txtDonGia.setText(String.format("%,.0f ₫", donGia));
        updateTongTienHuy();
    }
    
    public double getDonGia() {
        try {
            return Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public double getTongTienHuy() {
        try {
            return Double.parseDouble(txtTongTienHuy.getText().replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getLyDoXuatHuy() {
        return lyDoXuatHuy;
    }

    public void setLyDoXuatHuy(String lyDo) {
        this.lyDoXuatHuy = lyDo;
        if (lblLyDo != null) {
            if (lyDo == null || lyDo.trim().isEmpty()) {
                lblLyDo.setText("<html><div style='width:70px;word-wrap:break-word;'>(Chưa có lý do)</div></html>");
                lblLyDo.setForeground(new java.awt.Color(108, 117, 125));
            } else {
                lblLyDo.setText("<html><div style='width:70px;word-wrap:break-word;'>" + lyDo + "</div></html>");
                lblLyDo.setForeground(new java.awt.Color(33, 37, 41));
            }
        }
    }
    
    // Method để enable/disable spinner số lượng
    public void setSoLuongEditable(boolean editable) {
        if (spinnerSoLuongHuy != null) {
            spinnerSoLuongHuy.setEnabled(editable);
        }
    }
    
    // Getter và Setter cho LoHang
    public LoHang getLoHang() {
        return loHang;
    }
    
    public void setLoHangObject(LoHang loHang) {
        this.loHang = loHang;
    }
    
    public ChiTietDonTraHang getChiTietDonTra() {
        return chiTietDonTra;
    }
    
    public void setChiTietDonTra(ChiTietDonTraHang chiTietDonTra) {
        this.chiTietDonTra = chiTietDonTra;
    }

    private void updateTongTienHuy() {
        try {
            int soLuong = (int) spinnerSoLuongHuy.getValue();
            double donGia = Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9.]", ""));
            double tongTien = soLuong * donGia;
            txtTongTienHuy.setText(String.format("%,.0f ₫", tongTien));
        } catch (Exception e) {
            txtTongTienHuy.setText("0 ₫");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 232, 232)));
        setMaximumSize(new java.awt.Dimension(32767, 100));
        setMinimumSize(new java.awt.Dimension(700, 100));
        setPreferredSize(new java.awt.Dimension(1000, 100));
        setRequestFocusEnabled(false);
        
        // Sử dụng GridBagLayout để các cột thẳng hàng
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(10, 5, 10, 5);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh sản phẩm
        lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(70, 70));
        lblHinh.setMinimumSize(new java.awt.Dimension(70, 70));
        lblHinh.setMaximumSize(new java.awt.Dimension(70, 70));
        lblHinh.setBackground(new java.awt.Color(240, 240, 240));
        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        lblHinh.setOpaque(true);
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("IMG");
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        add(lblHinh, gbc);

        // 2. Tên sản phẩm + Thông tin lô hàng
        javax.swing.JPanel pnProductInfo = new javax.swing.JPanel();
        pnProductInfo.setBackground(java.awt.Color.WHITE);
        pnProductInfo.setLayout(new java.awt.GridBagLayout());
        pnProductInfo.setPreferredSize(new java.awt.Dimension(200, 80));
        pnProductInfo.setMinimumSize(new java.awt.Dimension(180, 80));
        
        java.awt.GridBagConstraints gbcProduct = new java.awt.GridBagConstraints();
        gbcProduct.anchor = java.awt.GridBagConstraints.WEST;
        gbcProduct.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbcProduct.weightx = 1.0;
        
        // Tên sản phẩm - dòng đầu tiên
        lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // Bold và lớn hơn
        lblTenSP.setText("");
        gbcProduct.gridx = 0;
        gbcProduct.gridy = 0;
        gbcProduct.insets = new java.awt.Insets(5, 0, 2, 0);
        pnProductInfo.add(lblTenSP, gbcProduct);
        
        // Panel chứa thông tin lô (Lô, HSD, Tồn) - hiển thị ngang
        javax.swing.JPanel pnLoInfo = new javax.swing.JPanel();
        pnLoInfo.setBackground(java.awt.Color.WHITE);
        pnLoInfo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        
        lblLoHang = new javax.swing.JLabel();
        lblLoHang.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblLoHang.setForeground(new java.awt.Color(102, 102, 102));
        lblLoHang.setText("");
        pnLoInfo.add(lblLoHang);
        
        javax.swing.JLabel lblSeparator1 = new javax.swing.JLabel(" | ");
        lblSeparator1.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblSeparator1.setForeground(new java.awt.Color(200, 200, 200));
        pnLoInfo.add(lblSeparator1);
        
        lblHanSuDung = new javax.swing.JLabel();
        lblHanSuDung.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblHanSuDung.setForeground(new java.awt.Color(220, 53, 69)); // Màu đỏ cho HSD (hết hạn)
        lblHanSuDung.setText("");
        pnLoInfo.add(lblHanSuDung);
        
        javax.swing.JLabel lblSeparator2 = new javax.swing.JLabel(" | ");
        lblSeparator2.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblSeparator2.setForeground(new java.awt.Color(200, 200, 200));
        pnLoInfo.add(lblSeparator2);
        
        lblTonKho = new javax.swing.JLabel();
        lblTonKho.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblTonKho.setForeground(new java.awt.Color(102, 102, 102));
        lblTonKho.setText("");
        pnLoInfo.add(lblTonKho);
        
        gbcProduct.gridy = 1;
        gbcProduct.insets = new java.awt.Insets(0, 0, 5, 0);
        pnProductInfo.add(pnLoInfo, gbcProduct);
        
        gbc.gridx = 1;
        gbc.weightx = 0.25;
        add(pnProductInfo, gbc);

        // 3. Lý do xuất hủy - dùng JTextArea để wrap text
        javax.swing.JPanel pnLyDo = new javax.swing.JPanel();
        pnLyDo.setBackground(java.awt.Color.WHITE);
        pnLyDo.setLayout(new java.awt.BorderLayout(5, 0));
        
        lblLyDo = new javax.swing.JLabel();
        lblLyDo.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblLyDo.setForeground(new java.awt.Color(108, 117, 125));
        lblLyDo.setText("<html><div style='width:70px;word-wrap:break-word;'>(Chưa có lý do)</div></html>");
        lblLyDo.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        pnLyDo.add(lblLyDo, java.awt.BorderLayout.CENTER);
        
        pnLyDo.setPreferredSize(new java.awt.Dimension(80, 80));
        pnLyDo.setMinimumSize(new java.awt.Dimension(70, 80));
        gbc.gridx = 2;
        gbc.weightx = 0.08;
        add(pnLyDo, gbc);

        // 4. Đơn vị
        lblDonVi = new javax.swing.JLabel();
        lblDonVi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDonVi.setText("");
        lblDonVi.setPreferredSize(new java.awt.Dimension(50, 80));
        lblDonVi.setMinimumSize(new java.awt.Dimension(50, 80));
        lblDonVi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDonVi.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(lblDonVi, gbc);

        // 5. Số lượng (Spinner) - Cột riêng
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        spinnerSoLuongHuy = new javax.swing.JSpinner();
        spinnerSoLuongHuy.setFont(new java.awt.Font("Segoe UI", 0, 14));
        spinnerSoLuongHuy.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        spinnerSoLuongHuy.setPreferredSize(new java.awt.Dimension(55, 35));
        spinnerSoLuongHuy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerSoLuongHuyStateChanged(evt);
            }
        });
        pnSpinner.add(spinnerSoLuongHuy);
        pnSpinner.setPreferredSize(new java.awt.Dimension(60, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(60, 100));
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 6. Đơn giá
        txtDonGia = new javax.swing.JLabel();
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDonGia.setText("");
        txtDonGia.setPreferredSize(new java.awt.Dimension(75, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(75, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(75, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 7. Tổng tiền hủy
        txtTongTienHuy = new javax.swing.JLabel();
        txtTongTienHuy.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtTongTienHuy.setForeground(new java.awt.Color(220, 53, 69)); // Màu đỏ cho xuất hủy
        txtTongTienHuy.setText("");
        txtTongTienHuy.setPreferredSize(new java.awt.Dimension(85, 100));
        txtTongTienHuy.setMinimumSize(new java.awt.Dimension(85, 100));
        txtTongTienHuy.setMaximumSize(new java.awt.Dimension(85, 100));
        txtTongTienHuy.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTienHuy.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(txtTongTienHuy, gbc);
    }// </editor-fold>//GEN-END:initComponents


    private void spinnerSoLuongHuyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongHuyStateChanged
        // Tính lại tổng tiền hủy khi thay đổi số lượng
        updateTongTienHuy();
        
        // Cập nhật tổng tiền của form cha
        java.awt.Container parent = this.getParent();
        if (parent != null) {
            java.awt.Container topParent = parent;
            while (topParent != null && !(topParent instanceof GD_QuanLyXuatHuy)) {
                topParent = topParent.getParent();
            }
            
            if (topParent instanceof GD_QuanLyXuatHuy) {
                ((GD_QuanLyXuatHuy) topParent).updateTongTien();
            }
        }
    }//GEN-LAST:event_spinnerSoLuongHuyStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner spinnerSoLuongHuy;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTienHuy;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblLyDo;
    private javax.swing.JLabel lblDonVi;
    private javax.swing.JLabel lblLoHang;
    private javax.swing.JLabel lblHanSuDung;
    private javax.swing.JLabel lblTonKho;
    // End of variables declaration//GEN-END:variables
}


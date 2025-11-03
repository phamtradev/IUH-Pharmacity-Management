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
    private PanelDeleteListener deleteListener;
    private int soLuongToiDa = 1000; // Số lượng tối đa đã mua
    private boolean isResettingValue = false; // Flag để tránh vòng lặp validation

    public Panel_ChiTietSanPhamTraHang() {
        initComponents();
    }
    
    // Interface để thông báo khi panel bị xóa
    public interface PanelDeleteListener {
        void onPanelDeleted();
    }
    
    // Setter cho listener
    public void setDeleteListener(PanelDeleteListener listener) {
        this.deleteListener = listener;
    }

    public int getSoLuongTra() {
        return (int) spinnerSoLuongTra.getValue();
    }

    public void setSoLuongTra(int soLuong) {
        // Kiểm tra số lượng không vượt quá số lượng tối đa
        if (soLuong > soLuongToiDa) {
            System.out.println("WARN setSoLuongTra: Số lượng " + soLuong + " vượt quá tối đa " + soLuongToiDa + ", tự động điều chỉnh");
            spinnerSoLuongTra.setValue(soLuongToiDa);
        } else {
            spinnerSoLuongTra.setValue(soLuong);
        }
    }
    
    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }
    
    public void setSoLuongToiDa(int soLuong) {
        this.soLuongToiDa = soLuong;
        System.out.println("DEBUG setSoLuongToiDa: Đã set số lượng tối đa = " + soLuong);
        // Lấy giá trị hiện tại của spinner
        int currentValue = (int) spinnerSoLuongTra.getValue();
        // Cập nhật model của spinner với giới hạn RẤT LỚN (Integer.MAX_VALUE)
        // để KHÔNG tự động chặn, mà để listener xử lý validation
        int newValue = Math.min(currentValue, soLuong);
        spinnerSoLuongTra.setModel(new javax.swing.SpinnerNumberModel(newValue, 1, Integer.MAX_VALUE, 1));
    }

    public String getTenSanPham() {
        return lblTenSP.getText();
    }

    public void setTenSanPham(String ten) {
        System.out.println("DEBUG Panel_ChiTietSanPhamTraHang.setTenSanPham: '" + ten + "'");
        lblTenSP.setText(ten);
    }
    
    public String getDonVi() {
        return lblDonVi.getText();
    }

    public void setDonVi(String donVi) {
        System.out.println("DEBUG Panel_ChiTietSanPhamTraHang.setDonVi: '" + donVi + "'");
        lblDonVi.setText(donVi);
    }
    
    public void setHinhAnh(String imagePath) {
        System.out.println("DEBUG Panel_ChiTietSanPhamTraHang.setHinhAnh: '" + imagePath + "'");
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                java.io.File imgFile = new java.io.File(imagePath);
                System.out.println("DEBUG: File exists? " + imgFile.exists() + " - Absolute path: " + imgFile.getAbsolutePath());
                if (imgFile.exists()) {
                    javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(imagePath);
                    java.awt.Image image = imageIcon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
                    lblHinh.setIcon(new javax.swing.ImageIcon(image));
                    lblHinh.setText("");
                    System.out.println("DEBUG: Đã set icon thành công");
                } else {
                    lblHinh.setText("No Img");
                    System.out.println("DEBUG: File không tồn tại");
                }
            } catch (Exception e) {
                lblHinh.setText("Error");
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("DEBUG: imagePath null hoặc rỗng");
            lblHinh.setText("No Img");
        }
    }
    
    public double getDonGia() {
        try {
            return Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
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
        lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(80, 80));
        lblHinh.setMinimumSize(new java.awt.Dimension(80, 80));
        lblHinh.setMaximumSize(new java.awt.Dimension(80, 80));
        lblHinh.setBackground(new java.awt.Color(240, 240, 240));
        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        lblHinh.setOpaque(true);
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("");
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        add(lblHinh, gbc);

        // 2. Tên sản phẩm
        lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSP.setText("");
        lblTenSP.setPreferredSize(new java.awt.Dimension(180, 80));
        lblTenSP.setMinimumSize(new java.awt.Dimension(180, 80));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Đơn vị
        lblDonVi = new javax.swing.JLabel();
        lblDonVi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDonVi.setText("");
        lblDonVi.setPreferredSize(new java.awt.Dimension(80, 80));
        lblDonVi.setMinimumSize(new java.awt.Dimension(80, 80));
        lblDonVi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDonVi.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(lblDonVi, gbc);

        // 4. Số lượng (Spinner) - Cột riêng - Có thể chỉnh sửa
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 22));
        spinnerSoLuongTra = new javax.swing.JSpinner();
        spinnerSoLuongTra.setFont(new java.awt.Font("Segoe UI", 0, 14));
        // Set max = Integer.MAX_VALUE để không tự động chặn, để listener xử lý validation
        spinnerSoLuongTra.setModel(new javax.swing.SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        spinnerSoLuongTra.setPreferredSize(new java.awt.Dimension(60, 35));
        spinnerSoLuongTra.setEnabled(true); // Cho phép chỉnh sửa số lượng
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
        txtDonGia.setText("");
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
        txtTongTienTra.setText("");
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
            
            // Thông báo cho listener để cập nhật tổng tiền
            if (deleteListener != null) {
                deleteListener.onPanelDeleted();
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void spinnerSoLuongTraStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongTraStateChanged
        // Nếu đang trong quá trình reset value, bỏ qua validation
        if (isResettingValue) {
            System.out.println("DEBUG: Đang reset value, bỏ qua validation");
            return;
        }
        
        // Kiểm tra số lượng trả không vượt quá số lượng đã mua
        int soLuongNhap = (int) spinnerSoLuongTra.getValue();
        
        System.out.println("DEBUG spinnerStateChanged: Số lượng nhập = " + soLuongNhap + ", Tối đa = " + soLuongToiDa);
        
        if (soLuongNhap > soLuongToiDa) {
            // Hiển thị thông báo lỗi
            raven.toast.Notifications.getInstance().show(
                raven.toast.Notifications.Type.ERROR,
                raven.toast.Notifications.Location.TOP_CENTER,
                3000,
                "Số lượng trả (" + soLuongNhap + ") không được lớn hơn số lượng đã mua (" + soLuongToiDa + ")"
            );
            
            System.out.println("DEBUG: Đang reset spinner về " + soLuongToiDa);
            
            // Reset về số lượng tối đa
            isResettingValue = true; // Đánh dấu đang reset
            spinnerSoLuongTra.setValue(soLuongToiDa);
            isResettingValue = false; // Hoàn tất reset
            updateTongTienTra();
            return;
        }
        
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
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblDonVi;
    // End of variables declaration//GEN-END:variables
}

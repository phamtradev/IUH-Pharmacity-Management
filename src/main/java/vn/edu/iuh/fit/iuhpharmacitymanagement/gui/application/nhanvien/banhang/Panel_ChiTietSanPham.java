/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPham extends javax.swing.JPanel {
    
    private SanPham sanPham;
    private DecimalFormat currencyFormat;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblTenSP;
    private LoHangBUS loHangBUS;
    private Panel_ChonLo panelChonLo;
    private List<LoHang> danhSachLoHang;
    private double cachedTongTien = 0; // Cache giá trị tổng tiền để detect thay đổi

    public Panel_ChiTietSanPham() {
        this.currencyFormat = new DecimalFormat("#,###");
        this.loHangBUS = new LoHangBUS();
        initComponents();
    }
    
    public Panel_ChiTietSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
        this.currencyFormat = new DecimalFormat("#,###");
        this.loHangBUS = new LoHangBUS();
        initComponents();
        loadSanPhamData();
        loadLoHangData();
    }
    
    private void loadSanPhamData() {
        if (sanPham != null) {
            // Set tên sản phẩm với đơn vị tính (sử dụng HTML để hiển thị 2 dòng)
            String donViTinh = sanPham.getDonViTinh() != null ? 
                sanPham.getDonViTinh().getTenDonVi() : "";
            
            String htmlText = "<html><div style='text-align: left;'>" +
                "<div style='font-size: 14px; font-weight: normal;'>" + sanPham.getTenSanPham() + "</div>" +
                (donViTinh.isEmpty() ? "" : 
                    "<div style='font-size: 11px; color: #666666; margin-top: 2px;'>Đơn vị: " + donViTinh + "</div>") +
                "</div></html>";
            
            lblTenSP.setText(htmlText);
            
            // Set đơn giá
            txtDonGia.setText(currencyFormat.format(sanPham.getGiaBan()) + " đ");
            
            // Set giảm giá mặc định là 0%
            txtDiscount.setText("0%");
            
            // Set tổng tiền ban đầu (cachedTongTien = 0 để lần đầu fire property change)
            updateTongTien();
            
            // Load hình ảnh nếu có
            if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
                try {
                    // Thử load từ đường dẫn tuyệt đối (nếu file được chọn từ JFileChooser)
                    java.io.File imageFile = new java.io.File(sanPham.getHinhAnh());
                    ImageIcon icon = null;
                    
                    if (imageFile.exists()) {
                        // File tồn tại với đường dẫn tuyệt đối
                        icon = new ImageIcon(sanPham.getHinhAnh());
                    } else {
                        // Thử load từ resources
                        java.net.URL imgURL = getClass().getResource("/img/" + sanPham.getHinhAnh());
                        if (imgURL != null) {
                            icon = new ImageIcon(imgURL);
                        }
                    }
                    
                    if (icon != null && icon.getIconWidth() > 0) {
                        // Scale image to fit label
                        java.awt.Image img = icon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
                        lblHinh.setIcon(new ImageIcon(img));
                        lblHinh.setText("");
                    } else {
                        lblHinh.setText("IMG");
                    }
                } catch (Exception e) {
                    lblHinh.setText("IMG");
                    e.printStackTrace();
                }
            } else {
                lblHinh.setText("IMG");
            }
        }
    }
    
    private void updateTongTien() {
        if (sanPham != null) {
            int soLuong = (int) spinnerSoLuong.getValue();
            double oldTongTien = cachedTongTien; // Sử dụng giá trị cached
            double tongTien = sanPham.getGiaBan() * soLuong;
            txtTongTien.setText(currencyFormat.format(tongTien) + " đ");
            
            // Cập nhật cache
            cachedTongTien = tongTien;
            
            // Fire property change để notify parent
            firePropertyChange("tongTien", oldTongTien, tongTien);
        }
    }

    public int getSoLuong() {
        return (int) spinnerSoLuong.getValue();
    }
    
    /**
     * Set số lượng cho sản phẩm (dùng khi cộng dồn số lượng)
     * @param soLuong số lượng mới
     */
    public void setSoLuong(int soLuong) {
        if (soLuong >= 1 && soLuong <= 1000) {
            spinnerSoLuong.setValue(soLuong);
            spinnerSoLuongStateChanged(null); // Trigger update tổng tiền
        }
    }
    
    public SanPham getSanPham() {
        return sanPham;
    }
    
    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
        loadSanPhamData();
        loadLoHangData();
    }
    
    public double getTongTien() {
        if (sanPham != null) {
            return sanPham.getGiaBan() * getSoLuong();
        }
        return 0;
    }
    
    /**
     * Load danh sách lô hàng của sản phẩm
     */
    private void loadLoHangData() {
        if (sanPham != null) {
            // Lấy danh sách lô hàng của sản phẩm
            danhSachLoHang = loHangBUS.getLoHangBySanPham(sanPham);
            
            // Nếu có lô hàng, hiển thị lô đầu tiên (hoặc lô có tồn kho lớn nhất)
            if (!danhSachLoHang.isEmpty()) {
                // Tìm lô có tồn kho > 0 và còn hạn sử dụng
                LoHang loHangHopLe = danhSachLoHang.stream()
                    .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
                    .findFirst()
                    .orElse(danhSachLoHang.get(0)); // Nếu không có lô hợp lệ, lấy lô đầu tiên
                
                // Cập nhật panel chọn lô
                if (panelChonLo != null) {
                    panelChonLo.setLoHang(loHangHopLe);
                }
            }
        }
    }
    
    public List<LoHang> getDanhSachLoHang() {
        return danhSachLoHang;
    }
    
    public LoHang getLoHangDaChon() {
        if (panelChonLo != null) {
            return panelChonLo.getLoHang();
        }
        return null;
    }
    
    /**
     * Set % giảm giá cho sản phẩm (hiển thị trong txtDiscount)
     * @param phanTramGiamGia % giảm giá (dạng thập phân: 0.1 = 10%)
     * @param tenKhuyenMai Tên khuyến mãi (hiển thị phía dưới %)
     */
    public void setGiamGia(double phanTramGiamGia, String tenKhuyenMai) {
        if (phanTramGiamGia > 0) {
            // Hiển thị % giảm giá + tên khuyến mãi
            double phanTram = phanTramGiamGia * 100;
            txtDiscount.setText("<html><div style='text-align: center;'>" +
                "<div style='font-size: 14px; font-weight: bold; color: #ff0000;'>-" + 
                String.format("%.0f", phanTram) + "%</div>" +
                (tenKhuyenMai != null && !tenKhuyenMai.isEmpty() ? 
                    "<div style='font-size: 10px; color: #ff6600; margin-top: 2px;'>" + tenKhuyenMai + "</div>" : "") +
                "</div></html>");
        } else {
            txtDiscount.setText("0%");
        }
    }
    
    /**
     * Set % giảm giá cho sản phẩm (không có tên khuyến mãi)
     * @param phanTramGiamGia % giảm giá (dạng thập phân: 0.1 = 10%)
     */
    public void setGiamGia(double phanTramGiamGia) {
        setGiamGia(phanTramGiamGia, null);
    }
    
    /**
     * Lấy % giảm giá từ txtDiscount (parse từ text)
     * @return phần trăm giảm giá (dạng thập phân: 0.1 = 10%), hoặc 0 nếu không có
     */
    public double getGiamGia() {
        try {
            String text = txtDiscount.getText();
            
            // Loại bỏ HTML tags nếu có
            text = text.replaceAll("<[^>]*>", "").trim();
            
            // Loại bỏ ký tự %, - và khoảng trắng
            text = text.replace("%", "").replace("-", "").trim();
            
            // Tách lấy phần đầu tiên (trước dấu cách) - là số % giảm giá
            String[] parts = text.split("\\s+");
            if (parts.length > 0) {
                double phanTram = Double.parseDouble(parts[0]);
                return phanTram / 100.0; // Chuyển % sang dạng thập phân
            }
        } catch (Exception e) {
            // Nếu parse lỗi, return 0
        }
        return 0.0;
    }
    
    /**
     * Lấy số tiền giảm giá (= Tổng tiền × % giảm giá)
     * @return số tiền giảm giá
     */
    public double getSoTienGiamGia() {
        return getTongTien() * getGiamGia();
    }

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
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Panel chọn lô
        panelChonLo = new Panel_ChonLo();
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(panelChonLo, gbc);

        // 4. Số lượng với nút +/- - Cột riêng
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 22));
        
        // Nút giảm
        javax.swing.JButton btnGiam = new javax.swing.JButton("-");
        btnGiam.setFont(new java.awt.Font("Segoe UI", 1, 16));
        btnGiam.setPreferredSize(new java.awt.Dimension(35, 35));
        btnGiam.setFocusPainted(false);
        btnGiam.setBackground(new java.awt.Color(220, 53, 69));
        btnGiam.setForeground(java.awt.Color.WHITE);
        btnGiam.setBorderPainted(false);
        btnGiam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGiam.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue > 1) {
                spinnerSoLuong.setValue(currentValue - 1);
                spinnerSoLuongStateChanged(null);
            }
        });
        
        // TextField EDITABLE để nhập số lượng trực tiếp
        javax.swing.JTextField txtSoLuong = new javax.swing.JTextField("1");
        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 16));
        txtSoLuong.setPreferredSize(new java.awt.Dimension(60, 35));
        txtSoLuong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtSoLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));
        
        // Cập nhật spinner để đồng bộ với textfield (ẩn spinner)
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1000, 1));
        spinnerSoLuong.setVisible(false); // Ẩn spinner, chỉ dùng để lưu giá trị
        
        // Listener để cập nhật textfield khi spinner thay đổi (từ nút +/-)
        spinnerSoLuong.addChangeListener(evt -> {
            txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
        });
        
        // Listener để cập nhật spinner khi người dùng EDIT trực tiếp vào textfield
        txtSoLuong.addActionListener(evt -> {
            try {
                int value = Integer.parseInt(txtSoLuong.getText().trim());
                if (value >= 1 && value <= 1000) {
                    spinnerSoLuong.setValue(value);
                    spinnerSoLuongStateChanged(null);
                } else {
                    // Nếu ngoài phạm vi, reset về giá trị hiện tại
                    txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                    javax.swing.JOptionPane.showMessageDialog(this, 
                        "Số lượng phải từ 1 đến 1000!", 
                        "Lỗi", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                // Nếu nhập sai định dạng, reset về giá trị hiện tại
                txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Vui lòng nhập số nguyên hợp lệ!", 
                    "Lỗi", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Listener khi mất focus (blur) - tự động cập nhật
        txtSoLuong.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    int value = Integer.parseInt(txtSoLuong.getText().trim());
                    if (value >= 1 && value <= 1000) {
                        spinnerSoLuong.setValue(value);
                        spinnerSoLuongStateChanged(null);
                    } else {
                        txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                    }
                } catch (NumberFormatException e) {
                    txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
                }
            }
        });
        
        // Nút tăng
        javax.swing.JButton btnTang = new javax.swing.JButton("+");
        btnTang.setFont(new java.awt.Font("Segoe UI", 1, 16));
        btnTang.setPreferredSize(new java.awt.Dimension(35, 35));
        btnTang.setFocusPainted(false);
        btnTang.setBackground(new java.awt.Color(40, 167, 69));
        btnTang.setForeground(java.awt.Color.WHITE);
        btnTang.setBorderPainted(false);
        btnTang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTang.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue < 1000) {
                spinnerSoLuong.setValue(currentValue + 1);
                spinnerSoLuongStateChanged(null);
            }
        });
        
        pnSpinner.add(btnGiam);
        pnSpinner.add(txtSoLuong);
        pnSpinner.add(btnTang);
        pnSpinner.setPreferredSize(new java.awt.Dimension(150, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(150, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 5. Giảm giá - Cột riêng
        txtDiscount.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDiscount.setForeground(new java.awt.Color(255, 0, 0));
        txtDiscount.setText("");
        txtDiscount.setPreferredSize(new java.awt.Dimension(70, 100));
        txtDiscount.setMinimumSize(new java.awt.Dimension(70, 100));
        txtDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtDiscount.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDiscount, gbc);

        // 6. Đơn giá
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDonGia.setText("");
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
        txtTongTien.setText("");
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
            
            // Fire property change SAU KHI xóa để GD_BanHang cập nhật tổng tiền
            firePropertyChange("sanPhamXoa", false, true);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void spinnerSoLuongStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongStateChanged
        updateTongTien();
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

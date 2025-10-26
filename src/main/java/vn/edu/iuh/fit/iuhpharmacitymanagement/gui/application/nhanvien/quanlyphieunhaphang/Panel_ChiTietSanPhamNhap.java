/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;
import java.awt.Component;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPhamNhap extends javax.swing.JPanel {
    
    private SanPham sanPham;
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblTenSP;
    private JSpinner spinnerHanDung;
    private javax.swing.JButton btnChonLo;
    private javax.swing.JPanel pnLo; // Panel chứa spinner HSD + button chọn lô HOẶC thẻ lô
    
    // Các label cho thẻ lô (giống giao diện bán hàng)
    private javax.swing.JLabel lblTheLo_TenLo;
    private javax.swing.JLabel lblTheLo_HSD;
    private javax.swing.JLabel lblTheLo_TonKho;
    private double cachedTongTien = 0; // Cache giá trị tổng tiền để detect thay đổi
    private LoHangBUS loHangBUS;
    private List<LoHang> danhSachLoHang;
    private LoHang loHangDaChon = null;
    private ButtonGroup buttonGroup;
    private String tenLoHangTuExcel = null; // Lưu tên lô từ Excel

    public Panel_ChiTietSanPhamNhap() {
        this.currencyFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        initComponents();
    }
    
    /**
     * Constructor khi KHÔNG có thông tin lô từ Excel
     * Hiển thị Spinner HSD + Button "Chọn lô"
     */
    public Panel_ChiTietSanPhamNhap(SanPham sanPham) {
        this.sanPham = sanPham;
        this.currencyFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.tenLoHangTuExcel = null; // Không có lô từ Excel
        initComponents();
        loadSanPhamData();
        loadLoHangData();
    }
    
    /**
     * Constructor khi CÓ thông tin lô từ Excel
     * Hiển thị label tên lô, ẩn Spinner HSD và Button "Chọn lô"
     */
    public Panel_ChiTietSanPhamNhap(SanPham sanPham, int soLuong, double donGiaNhap, Date hanDung, String tenLoHang) {
        this.sanPham = sanPham;
        this.currencyFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.tenLoHangTuExcel = tenLoHang; // Lưu tên lô từ Excel
        initComponents();
        loadSanPhamData();
        loadLoHangData();
        
        // Set các giá trị từ Excel
        spinnerSoLuong.setValue(soLuong);
        txtDonGia.setText(currencyFormat.format(donGiaNhap) + " đ");
        spinnerHanDung.setValue(hanDung);
        
        // Tìm lô hàng theo tên (nếu đã tồn tại)
        if (tenLoHang != null && !tenLoHang.trim().isEmpty()) {
            loHangDaChon = danhSachLoHang.stream()
                    .filter(lo -> lo.getTenLoHang().equalsIgnoreCase(tenLoHang.trim()))
                    .findFirst()
                    .orElse(null);
            
            // Cập nhật thông tin HSD và Tồn kho trên thẻ lô
            if (loHangDaChon != null) {
                updateTheLo(loHangDaChon);
            }
        }
        
        // Cập nhật tổng tiền
        updateTongTien();
    }
    
    /**
     * Cập nhật thông tin HSD và Tồn kho lên thẻ lô
     */
    private void updateTheLo(LoHang loHang) {
        if (loHang != null && lblTheLo_HSD != null && lblTheLo_TonKho != null) {
            // Cập nhật HSD
            lblTheLo_HSD.setText("HSD: " + loHang.getHanSuDung().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            // Cập nhật tồn kho
            lblTheLo_TonKho.setText("Tồn: " + loHang.getTonKho());
            
            // Đổi màu nếu hết hạn hoặc sắp hết hạn
            if (!loHang.isTrangThai()) {
                lblTheLo_HSD.setForeground(new java.awt.Color(220, 53, 69)); // Đỏ nếu hết hạn
            }
        }
    }
    
    private void loadSanPhamData() {
        if (sanPham != null) {
            // Set tên sản phẩm
            lblTenSP.setText(sanPham.getTenSanPham());
            
            // Set đơn giá nhập (mặc định = 0, để người dùng nhập)
            txtDonGia.setText("0 đ");
            
            // Set tổng tiền ban đầu
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
                        java.awt.Image img = icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
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
            double oldTongTien = cachedTongTien;
            
            // Lấy đơn giá nhập từ txtDonGia
            double donGia = 0;
            try {
                String donGiaStr = txtDonGia.getText().replace(" đ", "").replace(",", "");
                donGia = Double.parseDouble(donGiaStr);
            } catch (Exception e) {
                donGia = 0;
            }
            
            double tongTien = donGia * soLuong;
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
    
    public SanPham getSanPham() {
        return sanPham;
    }
    
    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
        loadSanPhamData();
        loadLoHangData();
    }
    
    public double getTongTien() {
        return cachedTongTien;
    }
    
    public double getDonGiaNhap() {
        try {
            String donGiaStr = txtDonGia.getText().replace(" đ", "").replace(",", "");
            return Double.parseDouble(donGiaStr);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public Date getHanDung() {
        return (Date) spinnerHanDung.getValue();
    }
    
    /**
     * Load danh sách lô hàng của sản phẩm
     */
    private void loadLoHangData() {
        if (sanPham != null) {
            danhSachLoHang = loHangBUS.getLoHangBySanPham(sanPham);
        }
    }
    
    public List<LoHang> getDanhSachLoHang() {
        return danhSachLoHang;
    }
    
    public LoHang getLoHangDaChon() {
        return loHangDaChon;
    }
    
    public String getTenLoHangTuExcel() {
        return tenLoHangTuExcel;
    }
    
    /**
     * Hiển thị dialog chọn lô hàng
     */
    private void showDialogChonLo() {
        if (danhSachLoHang == null || danhSachLoHang.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Sản phẩm chưa có lô hàng nào!",
                "Thông báo",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Clear panel chứa lô
        pnChuaLo.removeAll();
        buttonGroup = new ButtonGroup();
        
        // Add các lô hàng vào panel
        for (LoHang loHang : danhSachLoHang) {
            javax.swing.JPanel pnLoItem = new javax.swing.JPanel();
            pnLoItem.setBackground(java.awt.Color.WHITE);
            pnLoItem.setLayout(new java.awt.BorderLayout());
            pnLoItem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(230, 230, 230)));
            pnLoItem.setMaximumSize(new java.awt.Dimension(32767, 60));
            pnLoItem.setPreferredSize(new java.awt.Dimension(500, 60));
            
            // Toggle button cho lô
            JToggleButton btnLo = new JToggleButton();
            btnLo.setText(String.format("<html>%s - HSD: %s - Tồn: %d</html>", 
                loHang.getMaLoHang(),
                dateFormat.format(loHang.getHanSuDung()),
                loHang.getTonKho()));
            btnLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
            btnLo.setFocusPainted(false);
            btnLo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            btnLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btnLo.putClientProperty("loHang", loHang);
            
            buttonGroup.add(btnLo);
            pnLoItem.add(btnLo, java.awt.BorderLayout.CENTER);
            pnChuaLo.add(pnLoItem);
        }
        
        pnChuaLo.revalidate();
        pnChuaLo.repaint();
        
        // Hiển thị dialog
        dialogChonLo.pack();
        dialogChonLo.setLocationRelativeTo(this);
        dialogChonLo.setVisible(true);
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
        setMaximumSize(new java.awt.Dimension(32767, 120));
        setMinimumSize(new java.awt.Dimension(800, 120));
        setPreferredSize(new java.awt.Dimension(1000, 120));
        setRequestFocusEnabled(false);
        
        // Sử dụng GridBagLayout để các cột thẳng hàng
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(15, 10, 15, 10);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh sản phẩm
        lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(100, 100));
        lblHinh.setMinimumSize(new java.awt.Dimension(100, 100));
        lblHinh.setMaximumSize(new java.awt.Dimension(100, 100));
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
        lblTenSP.setPreferredSize(new java.awt.Dimension(250, 100));
        lblTenSP.setMinimumSize(new java.awt.Dimension(200, 100));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Cột "Lô hàng" - Hiển thị tùy theo nguồn dữ liệu
        pnLo = new javax.swing.JPanel();
        pnLo.setBackground(java.awt.Color.WHITE);
        pnLo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));
        pnLo.setPreferredSize(new java.awt.Dimension(180, 100));
        pnLo.setMinimumSize(new java.awt.Dimension(180, 100));
        
        // Kiểm tra có lô từ Excel không
        if (tenLoHangTuExcel != null && !tenLoHangTuExcel.trim().isEmpty()) {
            // CÓ lô từ Excel → hiển thị THẺ LÔ đẹp (giống giao diện bán hàng)
            javax.swing.JPanel pnTheLo = new javax.swing.JPanel();
            pnTheLo.setBackground(java.awt.Color.WHITE);
            pnTheLo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            pnTheLo.setPreferredSize(new java.awt.Dimension(160, 80));
            pnTheLo.setLayout(new javax.swing.BoxLayout(pnTheLo, javax.swing.BoxLayout.Y_AXIS));
            
            // Tên lô
            lblTheLo_TenLo = new javax.swing.JLabel("Lô: " + tenLoHangTuExcel);
            lblTheLo_TenLo.setFont(new java.awt.Font("Segoe UI", 1, 13));
            lblTheLo_TenLo.setForeground(new java.awt.Color(51, 51, 51));
            lblTheLo_TenLo.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_TenLo.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
            
            // HSD (sẽ cập nhật sau khi tìm lô)
            lblTheLo_HSD = new javax.swing.JLabel("HSD: --/--/----");
            lblTheLo_HSD.setFont(new java.awt.Font("Segoe UI", 0, 12));
            lblTheLo_HSD.setForeground(new java.awt.Color(102, 102, 102));
            lblTheLo_HSD.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_HSD.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 2, 8));
            
            // Tồn kho (sẽ cập nhật sau khi tìm lô)
            lblTheLo_TonKho = new javax.swing.JLabel("Tồn: --");
            lblTheLo_TonKho.setFont(new java.awt.Font("Segoe UI", 0, 12));
            lblTheLo_TonKho.setForeground(new java.awt.Color(34, 139, 34));
            lblTheLo_TonKho.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_TonKho.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8));
            
            pnTheLo.add(lblTheLo_TenLo);
            pnTheLo.add(lblTheLo_HSD);
            pnTheLo.add(lblTheLo_TonKho);
            
            pnLo.add(pnTheLo);
            
            // Khởi tạo spinner và button nhưng ẩn đi (để không bị null)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 6);
            Date initDate = calendar.getTime();
            calendar.add(Calendar.YEAR, -1);
            Date earliestDate = calendar.getTime();
            calendar.add(Calendar.YEAR, 10);
            Date latestDate = calendar.getTime();
            
            spinnerHanDung = new JSpinner(new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.DAY_OF_MONTH));
            JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHanDung, "dd/MM/yyyy");
            spinnerHanDung.setEditor(editor);
            spinnerHanDung.setVisible(false);
            
            btnChonLo = new javax.swing.JButton("Chọn lô");
            btnChonLo.setVisible(false);
            
        } else {
            // KHÔNG có lô từ Excel → hiển thị Spinner HSD + Button "Chọn lô"
            javax.swing.JLabel lblHSD = new javax.swing.JLabel("HSD:");
            lblHSD.setFont(new java.awt.Font("Segoe UI", 0, 13));
            
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 6); // Mặc định 6 tháng sau
            Date initDate = calendar.getTime();
            calendar.add(Calendar.YEAR, -1); // Min: 1 năm trước
            Date earliestDate = calendar.getTime();
            calendar.add(Calendar.YEAR, 10); // Max: 10 năm sau
            Date latestDate = calendar.getTime();
            
            spinnerHanDung = new JSpinner(new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.DAY_OF_MONTH));
            JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHanDung, "dd/MM/yyyy");
            spinnerHanDung.setEditor(editor);
            spinnerHanDung.setPreferredSize(new java.awt.Dimension(120, 35));
            spinnerHanDung.setFont(new java.awt.Font("Segoe UI", 0, 13));
            
            btnChonLo = new javax.swing.JButton("Chọn lô");
            btnChonLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
            btnChonLo.setPreferredSize(new java.awt.Dimension(100, 35));
            btnChonLo.setFocusPainted(false);
            btnChonLo.setBackground(new java.awt.Color(0, 120, 215));
            btnChonLo.setForeground(java.awt.Color.WHITE);
            btnChonLo.setBorderPainted(false);
            btnChonLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btnChonLo.addActionListener(evt -> showDialogChonLo());
            
            pnLo.add(lblHSD);
            pnLo.add(spinnerHanDung);
            pnLo.add(btnChonLo);
            
            // Khởi tạo các label thẻ lô nhưng không dùng
            lblTheLo_TenLo = new javax.swing.JLabel();
            lblTheLo_HSD = new javax.swing.JLabel();
            lblTheLo_TonKho = new javax.swing.JLabel();
        }
        
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(pnLo, gbc);

        // 4. Số lượng với nút +/-
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 32));
        
        // Nút giảm
        javax.swing.JButton btnGiam = new javax.swing.JButton("-");
        btnGiam.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnGiam.setPreferredSize(new java.awt.Dimension(45, 45));
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
        
        // Label hiển thị số lượng
        javax.swing.JLabel lblSoLuong = new javax.swing.JLabel("1");
        lblSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblSoLuong.setPreferredSize(new java.awt.Dimension(70, 45));
        lblSoLuong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));
        
        // Cập nhật spinner để đồng bộ với label (ẩn spinner)
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10000, 1));
        spinnerSoLuong.setVisible(false); // Ẩn spinner, chỉ dùng để lưu giá trị
        
        // Listener để cập nhật label khi spinner thay đổi
        spinnerSoLuong.addChangeListener(evt -> {
            lblSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
        });
        
        // Nút tăng
        javax.swing.JButton btnTang = new javax.swing.JButton("+");
        btnTang.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnTang.setPreferredSize(new java.awt.Dimension(45, 45));
        btnTang.setFocusPainted(false);
        btnTang.setBackground(new java.awt.Color(40, 167, 69));
        btnTang.setForeground(java.awt.Color.WHITE);
        btnTang.setBorderPainted(false);
        btnTang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTang.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue < 10000) {
                spinnerSoLuong.setValue(currentValue + 1);
                spinnerSoLuongStateChanged(null);
            }
        });
        
        pnSpinner.add(btnGiam);
        pnSpinner.add(lblSoLuong);
        pnSpinner.add(btnTang);
        pnSpinner.setPreferredSize(new java.awt.Dimension(180, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(180, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 5. Đơn giá nhập (có thể chỉnh sửa)
        txtDonGia = new javax.swing.JLabel();
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDonGia.setText("0 đ");
        txtDonGia.setPreferredSize(new java.awt.Dimension(120, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(120, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(120, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        txtDonGia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtDonGia.setToolTipText("Click để chỉnh sửa đơn giá");
        txtDonGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDonGiaMouseClicked(evt);
            }
        });
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 6. Tổng tiền
        txtTongTien = new javax.swing.JLabel();
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 16));
        txtTongTien.setForeground(new java.awt.Color(0, 120, 215));
        txtTongTien.setText("0 đ");
        txtTongTien.setPreferredSize(new java.awt.Dimension(130, 100));
        txtTongTien.setMinimumSize(new java.awt.Dimension(130, 100));
        txtTongTien.setMaximumSize(new java.awt.Dimension(130, 100));
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTien.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtTongTien, gbc);

        // 7. Nút Xóa
        javax.swing.JPanel pnXoa = new javax.swing.JPanel();
        pnXoa.setBackground(java.awt.Color.WHITE);
        pnXoa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 32));
        javax.swing.JButton btnXoa = new javax.swing.JButton();
        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnXoa.setBackground(new java.awt.Color(220, 53, 69));
        btnXoa.setForeground(java.awt.Color.WHITE);
        btnXoa.setPreferredSize(new java.awt.Dimension(70, 45));
        btnXoa.setMinimumSize(new java.awt.Dimension(70, 45));
        btnXoa.setMaximumSize(new java.awt.Dimension(70, 45));
        btnXoa.setFocusPainted(false);
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setBorderPainted(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnXoa.add(btnXoa);
        pnXoa.setPreferredSize(new java.awt.Dimension(90, 100));
        pnXoa.setMinimumSize(new java.awt.Dimension(90, 100));
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(pnXoa, gbc);
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // Fire property change trước khi xóa để cập nhật tổng tiền
        firePropertyChange("tongTien", getTongTien(), 0.0);
        
        // Xóa panel này khỏi container cha
        java.awt.Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void spinnerSoLuongStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerSoLuongStateChanged
        updateTongTien();
    }//GEN-LAST:event_spinnerSoLuongStateChanged

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        // Lấy lô đã chọn
        if (buttonGroup != null && buttonGroup.getSelection() != null) {
            Component[] components = pnChuaLo.getComponents();
            for (Component comp : components) {
                if (comp instanceof javax.swing.JPanel) {
                    javax.swing.JPanel panel = (javax.swing.JPanel) comp;
                    Component[] children = panel.getComponents();
                    for (Component child : children) {
                        if (child instanceof JToggleButton) {
                            JToggleButton btn = (JToggleButton) child;
                            if (btn.isSelected()) {
                                loHangDaChon = (LoHang) btn.getClientProperty("loHang");
                                
                                // Cập nhật hạn dùng từ lô đã chọn
                                if (loHangDaChon != null) {
                                    spinnerHanDung.setValue(loHangDaChon.getHanSuDung());
                                }
                                
                                break;
                            }
                        }
                    }
                }
            }
            
            dialogChonLo.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một lô hàng!",
                "Thông báo",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void txtDonGiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDonGiaMouseClicked
        String input = javax.swing.JOptionPane.showInputDialog(this,
            "Nhập đơn giá nhập:",
            "Cập nhật đơn giá",
            javax.swing.JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                double donGia = Double.parseDouble(input.trim());
                if (donGia >= 0) {
                    txtDonGia.setText(currencyFormat.format(donGia) + " đ");
                    updateTongTien();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                        "Đơn giá phải lớn hơn hoặc bằng 0!",
                        "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập số hợp lệ!",
                    "Lỗi",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtDonGiaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JDialog dialogChonLo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnChuaLo;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JSpinner spinnerSoLuong;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}

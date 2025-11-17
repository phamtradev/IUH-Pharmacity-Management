package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyxuathuy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietHangHongBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietHangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.HangHong;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

/**
 * Panel hiển thị 1 dòng thông tin phiếu xuất hủy
 * Dành cho màn hình nhân viên - CHỈ XEM phiếu đã duyệt
 */
public class Panel_PhieuXuatHuyRow extends JPanel {
    
    private HangHong phieu;
    private ChiTietHangHongBUS chiTietBUS;
    
    private JLabel lblMaPhieu;
    private JLabel lblNgayLap;
    private JLabel lblNhanVien;
    private JLabel lblTongTien;
    private JButton btnXemChiTiet;
    
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public Panel_PhieuXuatHuyRow(HangHong phieu, ChiTietHangHongBUS chiTietBUS) {
        this.phieu = phieu;
        this.chiTietBUS = chiTietBUS;
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        setPreferredSize(new Dimension(1200, 60));
        setMinimumSize(new Dimension(800, 60));
        setMaximumSize(new Dimension(32767, 60));
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        
        // 1. Mã phiếu
        gbc.gridx = 0;
        gbc.weightx = 0.15;
        lblMaPhieu = createLabel("", 120, SwingConstants.CENTER, false);
        lblMaPhieu.setForeground(new Color(0, 123, 255));
        add(lblMaPhieu, gbc);
        
        // 2. Ngày lập
        gbc.gridx = 1;
        gbc.weightx = 0.12;
        lblNgayLap = createLabel("", 100, SwingConstants.CENTER, false);
        add(lblNgayLap, gbc);
        
        // 3. Nhân viên
        gbc.gridx = 2;
        gbc.weightx = 0.20;
        lblNhanVien = createLabel("", 150, SwingConstants.LEFT, false);
        add(lblNhanVien, gbc);
        
        // 4. Tổng tiền
        gbc.gridx = 3;
        gbc.weightx = 0.15;
        lblTongTien = createLabel("", 120, SwingConstants.RIGHT, true);
        lblTongTien.setForeground(new Color(220, 53, 69));
        add(lblTongTien, gbc);
        
        // 5. Nút xem chi tiết
        gbc.gridx = 4;
        gbc.weightx = 0.15;
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.WHITE);
        
        btnXemChiTiet = new JButton("Xem Chi Tiết");
        btnXemChiTiet.setPreferredSize(new Dimension(110, 32));
        ButtonStyles.apply(btnXemChiTiet, ButtonStyles.Type.INFO);
        btnXemChiTiet.addActionListener(e -> xemChiTietPhieu());
        
        actionPanel.add(btnXemChiTiet);
        add(actionPanel, gbc);
    }
    
    private JLabel createLabel(String text, int width, int alignment, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new java.awt.Font("Segoe UI", bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(width, 50));
        label.setMinimumSize(new Dimension(width, 50));
        label.setHorizontalAlignment(alignment);
        return label;
    }

    private JPanel createInfoRow(String title, String value, boolean bold) {
        JPanel panel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new java.awt.Font("Segoe UI", bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        panel.add(lblTitle);
        panel.add(lblValue);

        return panel;
    }
    
    private void loadData() {
        lblMaPhieu.setText(phieu.getMaHangHong());
        lblNgayLap.setText(dateFormat.format(java.sql.Date.valueOf(phieu.getNgayNhap())));
        
        if (phieu.getNhanVien() != null) {
            lblNhanVien.setText(phieu.getNhanVien().getTenNhanVien());
        } else {
            lblNhanVien.setText("N/A");
        }
        
        lblTongTien.setText(currencyFormat.format(phieu.getThanhTien()) + " ₫");
    }
    
    /**
     * Xem chi tiết phiếu xuất hủy
     */
    private void xemChiTietPhieu() {
        try {
            // Lấy danh sách chi tiết
            List<ChiTietHangHong> danhSachChiTiet = chiTietBUS.layChiTietTheoMaHangHong(phieu.getMaHangHong());
            
            if (danhSachChiTiet.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy chi tiết phiếu!", 
                    "Lỗi", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            hienThiDialogChiTiet(phieu, danhSachChiTiet);
            
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải chi tiết phiếu: " + e.getMessage(), 
                "Lỗi", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Hiển thị dialog chi tiết phiếu xuất hủy
     */
    private void hienThiDialogChiTiet(HangHong phieu, List<ChiTietHangHong> danhSachChiTiet) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Chi tiết phiếu xuất hủy - " + phieu.getMaHangHong());
        dialog.setModal(true);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // === HEADER ===
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("PHIẾU XUẤT HỦY");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setForeground(new Color(220, 53, 69));
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        headerPanel.add(javax.swing.Box.createVerticalStrut(20));
        
        headerPanel.add(createInfoRow("Mã phiếu: ", phieu.getMaHangHong(), true));
        headerPanel.add(createInfoRow("Ngày lập: ", dateFormat.format(java.sql.Date.valueOf(phieu.getNgayNhap())), false));
        headerPanel.add(createInfoRow("Nhân viên: ", phieu.getNhanVien() != null ? phieu.getNhanVien().getTenNhanVien() : "N/A", false));
        
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        
        // === CHI TIẾT ===
        String[] columnNames = {"STT", "Tên sản phẩm", "Lô hàng", "HSD", "ĐV", "SL", "Đơn giá", "Thành tiền", "Lý do"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        int stt = 1;
        for (ChiTietHangHong chiTiet : danhSachChiTiet) {
            if (chiTiet.getLoHang() == null || chiTiet.getLoHang().getSanPham() == null) continue;
            
            String tenSP = chiTiet.getLoHang().getSanPham().getTenSanPham();
            String tenLo = chiTiet.getLoHang().getTenLoHang() != null ? chiTiet.getLoHang().getTenLoHang() : chiTiet.getLoHang().getMaLoHang();
            String hsd = chiTiet.getLoHang().getHanSuDung() != null ? chiTiet.getLoHang().getHanSuDung().toString() : "N/A";
            String donVi = chiTiet.getLoHang().getSanPham().getDonViTinh() != null ? chiTiet.getLoHang().getSanPham().getDonViTinh().getTenDonVi() : "";
            
            model.addRow(new Object[]{
                stt++,
                tenSP,
                tenLo,
                hsd,
                donVi,
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " ₫",
                currencyFormat.format(chiTiet.getThanhTien()) + " ₫",
                chiTiet.getLyDoXuatHuy() != null ? chiTiet.getLyDoXuatHuy() : ""
            });
        }
        
        javax.swing.JTable table = new javax.swing.JTable(model);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        
        // === FOOTER ===
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new java.awt.BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel tongTienPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(Color.WHITE);
        
        JLabel lblTongTien = new JLabel("TỔNG GIÁ TRỊ:  " + currencyFormat.format(phieu.getThanhTien()) + " ₫");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTongTien.setForeground(new Color(220, 53, 69));
        tongTienPanel.add(lblTongTien);
        
        footerPanel.add(tongTienPanel, java.awt.BorderLayout.NORTH);
        
        // Nút xuất PDF
        JPanel btnPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setPreferredSize(new Dimension(120, 35));
        ButtonStyles.apply(btnDong, ButtonStyles.Type.SECONDARY);
        btnDong.addActionListener(e -> dialog.dispose());
        
        btnPanel.add(btnDong);
        
        footerPanel.add(btnPanel, java.awt.BorderLayout.SOUTH);
        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlytrahang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS;
import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.UIManager;
import raven.toast.Notifications;
import javax.swing.JButton;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyTraHang extends javax.swing.JPanel {

    private final DonTraHangBUS donTraHangBUS;
    private TableDesign tableDesign;

    public GD_QuanLyTraHang() {
        donTraHangBUS = new DonTraHangBUS();
        initComponents();
        setUIManager();
        fillTable();
    }

    private void setUIManager() {
        txtOrder.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã phiếu trả");
        UIManager.put("Button.arc", 10);
        jDateFrom.setDate(Date.valueOf(LocalDate.now()));
        jDateTo.setDate(Date.valueOf(LocalDate.now()));
        
        // Style cho button Xem chi tiết - màu xanh nước biển, kích thước nhỏ
        btnView.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#17A2B8;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#138496;"
                + "pressedBackground:#0F6674;"
                + "arc:10;"
                + "borderWidth:0");
    }

    private void fillTable() {
        String[] headers = {"Mã phiếu trả", "Mã hóa đơn", "Ngày trả", "Nhân viên", "Tổng tiền", "Trạng thái", "Thao tác"};
        List<Integer> tableWidths = Arrays.asList(150, 150, 120, 200, 150, 120, 120);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        
        // Tùy chỉnh renderer cho cột Thao tác
        setupActionColumn();
        
        // Lấy dữ liệu hoặc tạo dữ liệu giả lập
        List<DonTraHang> danhSach = layDuLieuDonTraHang();
        fillContent(danhSach);
    }

    
    private void fillContent(List<DonTraHang> danhSach) {
        tableDesign.getModelTable().setRowCount(0);
        for (DonTraHang dth : danhSach) {
            String maDonHang = dth.getDonHang() != null ? dth.getDonHang().getMaDonHang() : "N/A";
            String tenNV = dth.getNhanVien() != null ? dth.getNhanVien().getMaNhanVien() : "N/A";
            
            // Tạo trạng thái ngẫu nhiên cho demo
            String trangThai = "Chưa xử lý";
            
            tableDesign.getModelTable().addRow(new Object[]{
                dth.getMaDonTraHang(), 
                maDonHang,
                formatDate(dth.getngayTraHang()),
                tenNV,
                formatToVND(dth.getThanhTien()),
                trangThai,
                "Xem chi tiết" // Placeholder, sẽ được thay bằng button
            });
        }
    }
    
    private List<DonTraHang> searchDonTraHang(LocalDate dateFrom, LocalDate dateTo, String tenNV, String maPhieuTra) {
        List<DonTraHang> all = donTraHangBUS.layTatCaDonTraHang();
        List<DonTraHang> result = new ArrayList<>();
        
        for (DonTraHang dth : all) {
            LocalDate ngayTra = dth.getngayTraHang();
            
            // Lọc theo ngày
            if (ngayTra.isBefore(dateFrom) || ngayTra.isAfter(dateTo)) {
                continue;
            }
            
            // Lọc theo nhân viên
            if (!tenNV.isEmpty()) {
                if (dth.getNhanVien() == null || 
                    !dth.getNhanVien().getMaNhanVien().toLowerCase().contains(tenNV.toLowerCase())) {
                    continue;
                }
            }
            
            // Lọc theo mã phiếu trả
            if (!maPhieuTra.isEmpty()) {
                String maDonTraHang = dth.getMaDonTraHang() != null ? dth.getMaDonTraHang() : "";
                if (!maDonTraHang.toLowerCase().contains(maPhieuTra.toLowerCase())) {
                    continue;
                }
            }
            
            result.add(dth);
        }
        
        return result;
    }
    
    private String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
    
    @SuppressWarnings("deprecation")
    private String formatToVND(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
    
    // Tạo dữ liệu giả lập cho đơn trả hàng
    private List<DonTraHang> layDuLieuDonTraHang() {
        try {
            List<DonTraHang> danhSach = donTraHangBUS.layTatCaDonTraHang();
            if (danhSach != null && !danhSach.isEmpty()) {
                return danhSach;
            }
        } catch (Exception e) {
            System.err.println("Không lấy được dữ liệu từ database, tạo dữ liệu giả lập...");
        }
        
        // Tạo dữ liệu giả lập
        List<DonTraHang> danhSachGiaLap = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (int i = 1; i <= 5; i++) {
            try {
                DonTraHang dth = new DonTraHang();
                String maDTH = "DT" + today.minusDays(i).format(DateTimeFormatter.ofPattern("ddMMyyyy")) 
                        + String.format("%04d", i);
                dth.setMaDonTraHang(maDTH);
                dth.setngayTraHang(today.minusDays(i));
                dth.setThanhTien(500000 + (i * 100000));
                
                // Tạo nhân viên giả
                NhanVien nv = new NhanVien();
                nv.setMaNhanVien("NV" + String.format("%05d", i));
                dth.setNhanVien(nv);
                
                // Tạo đơn hàng giả
                DonHang dh = new DonHang();
                String maDH = "DH" + today.format(DateTimeFormatter.ofPattern("ddMMyyyy")) 
                        + String.format("%04d", i);
                dh.setMaDonHang(maDH);
                dth.setDonHang(dh);
                
                danhSachGiaLap.add(dth);
            } catch (Exception e) {
                System.err.println("Lỗi tạo dữ liệu giả lập: " + e.getMessage());
            }
        }
        
        return danhSachGiaLap;
    }
    
    // Thiết lập cột Thao tác với button
    private void setupActionColumn() {
        JTable table = tableDesign.getTable();
        
        // Đảm bảo có thể click vào button
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                
                // Kiểm tra nếu click vào cột Thao tác (cột index 6)
                if (col == 6 && row >= 0) {
                    String maDonTraHang = (String) table.getValueAt(row, 0);
                    System.out.println("Clicked on row " + row + ", maDonTraHang: " + maDonTraHang);
                    xemChiTietDonTraHang(maDonTraHang);
                }
            }
        });
        
        // Renderer cho cột Thao tác
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JButton btnAction = new JButton("Xem chi tiết");
                btnAction.putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:#17A2B8;"
                        + "foreground:#000000;"
                        + "hoverBackground:#138496;"
                        + "pressedBackground:#0F6674;"
                        + "arc:8;"
                        + "borderWidth:0");
                btnAction.setFocusPainted(false);
                btnAction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                return btnAction;
            }
        });
    }
    
    // Xem chi tiết đơn trả hàng
    private void xemChiTietDonTraHang(String maDonTraHang) {
        // Tạo dialog chi tiết
        showChiTietDonTraHangDialog(maDonTraHang);
    }
    
    // Hiển thị dialog chi tiết đơn trả hàng
    private void showChiTietDonTraHangDialog(String maDonTraHang) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Xem đơn trả");
        dialog.setSize(1400, 700);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        
        // Main panel
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout(0, 10));
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // === HEADER PANEL - Thông tin đơn trả hàng ===
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setLayout(new java.awt.GridLayout(3, 4, 20, 15));
        headerPanel.setBackground(java.awt.Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Giả lập dữ liệu
        String nguoiTao = "Tra";
        String thoiGianTao = "12/12/2024 06:37";
        String trangThai = "Chờ xử lý";
        String maHoaDon = "HD" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "0001";
        
        // Row 1
        addInfoField(headerPanel, "Mã phiếu trả", maDonTraHang);
        addInfoField(headerPanel, "Người tạo", nguoiTao);
        
        // Row 2
        addInfoField(headerPanel, "Thời gian tạo", thoiGianTao);
        addInfoField(headerPanel, "Trạng thái", trangThai);
        
        // Row 3
        addInfoField(headerPanel, "Mã hóa đơn", maHoaDon);
        headerPanel.add(new javax.swing.JLabel()); // Empty cell
        
        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);
        
        // === TABLE PANEL - Chi tiết sản phẩm trả ===
        String[] columns = {"Mã hàng", "Tên hàng", "Đơn vị tính", "Số lượng", 
                           "Giá trả hàng", "Lý do trả", "Lý do xử lý", "Thao tác"};
        List<Integer> columnWidths = Arrays.asList(100, 180, 100, 80, 120, 150, 150, 150);
        
        TableDesign tableChiTiet = new TableDesign(columns, columnWidths);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(tableChiTiet.getTable());
        scrollPane.setBorder(BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1));
        
        // Giả lập dữ liệu chi tiết sản phẩm
        giaDinhDuLieuChiTiet(tableChiTiet);
        
        // Thiết lập cột Thao tác với 2 button
        setupActionColumnForDetail(tableChiTiet.getTable());
        
        mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
        
        // === BOTTOM PANEL - Button Xác nhận ===
        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(java.awt.Color.WHITE);
        
        javax.swing.JButton btnXacNhan = new javax.swing.JButton("Xác nhận");
        btnXacNhan.setPreferredSize(new java.awt.Dimension(120, 40));
        btnXacNhan.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#17C3B2;"
                + "foreground:#000000;"
                + "hoverBackground:#14A895;"
                + "arc:10;"
                + "borderWidth:0");
        btnXacNhan.addActionListener(e -> {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xác nhận đơn trả!");
            dialog.dispose();
        });
        
        bottomPanel.add(btnXacNhan);
        mainPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    // Thêm field thông tin vào panel
    private void addInfoField(javax.swing.JPanel panel, String label, String value) {
        javax.swing.JPanel fieldPanel = new javax.swing.JPanel();
        fieldPanel.setLayout(new java.awt.BorderLayout(5, 0));
        fieldPanel.setBackground(java.awt.Color.WHITE);
        
        javax.swing.JLabel lblLabel = new javax.swing.JLabel(label);
        lblLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblLabel.setForeground(new java.awt.Color(80, 80, 80));
        
        javax.swing.JLabel lblValue = new javax.swing.JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        
        fieldPanel.add(lblLabel, java.awt.BorderLayout.NORTH);
        fieldPanel.add(lblValue, java.awt.BorderLayout.CENTER);
        
        panel.add(fieldPanel);
    }
    
    // Giả định dữ liệu chi tiết sản phẩm
    private void giaDinhDuLieuChiTiet(TableDesign tableDesign) {
        String[][] duLieuGia = {
            {"SP00001", "Terpinzoat", "Hộp", "2", "33,000 đ", "Dị ứng", "", ""},
            {"SP00002", "Paracetamol 500mg", "Viên", "10", "50,000 đ", "Hết hạn", "", ""},
            {"SP00003", "Vitamin C", "Hộp", "1", "120,000 đ", "Lỗi sản phẩm", "", ""}
        };
        
        for (String[] row : duLieuGia) {
            tableDesign.getModelTable().addRow(row);
        }
    }
    
    // Thiết lập cột Thao tác cho bảng chi tiết với 2 button
    private void setupActionColumnForDetail(JTable table) {
        // Renderer cho cột Thao tác
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                javax.swing.JPanel panel = new javax.swing.JPanel();
                panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));
                panel.setBackground(java.awt.Color.WHITE);
                
                // Button Thêm
                JButton btnThem = new JButton("Thêm");
                btnThem.putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:#28A745;"
                        + "foreground:#FFFFFF;"
                        + "hoverBackground:#218838;"
                        + "arc:5;"
                        + "borderWidth:0");
                btnThem.setPreferredSize(new java.awt.Dimension(60, 30));
                btnThem.setFocusPainted(false);
                
                // Button Hủy
                JButton btnHuy = new JButton("Hủy");
                btnHuy.putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:#DC3545;"
                        + "foreground:#FFFFFF;"
                        + "hoverBackground:#C82333;"
                        + "arc:5;"
                        + "borderWidth:0");
                btnHuy.setPreferredSize(new java.awt.Dimension(60, 30));
                btnHuy.setFocusPainted(false);
                
                panel.add(btnThem);
                panel.add(btnHuy);
                
                return panel;
            }
        });
        
        // Editor cho cột Thao tác
        table.getColumnModel().getColumn(7).setCellEditor(new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()) {
            private javax.swing.JPanel panel;
            private int currentRow;
            
            {
                panel = new javax.swing.JPanel();
                panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));
                
                JButton btnThem = new JButton("Thêm");
                btnThem.putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:#28A745;"
                        + "foreground:#FFFFFF;"
                        + "hoverBackground:#218838;"
                        + "arc:5;"
                        + "borderWidth:0");
                btnThem.setPreferredSize(new java.awt.Dimension(60, 30));
                btnThem.setFocusPainted(false);
                btnThem.addActionListener(e -> {
                    fireEditingStopped();
                    Notifications.getInstance().show(Notifications.Type.INFO, 
                        "Thêm sản phẩm tại dòng " + (currentRow + 1));
                });
                
                JButton btnHuy = new JButton("Hủy");
                btnHuy.putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:#DC3545;"
                        + "foreground:#FFFFFF;"
                        + "hoverBackground:#C82333;"
                        + "arc:5;"
                        + "borderWidth:0");
                btnHuy.setPreferredSize(new java.awt.Dimension(60, 30));
                btnHuy.setFocusPainted(false);
                btnHuy.addActionListener(e -> {
                    fireEditingStopped();
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Hủy sản phẩm tại dòng " + (currentRow + 1));
                });
                
                panel.add(btnThem);
                panel.add(btnHuy);
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                currentRow = row;
                return panel;
            }
        });
        
        // Tăng chiều cao hàng để button hiển thị đẹp hơn
        table.setRowHeight(40);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modalOrderDetail = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        scrollTableDetail = new javax.swing.JScrollPane();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtOrder = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        txtExport = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();

        modalOrderDetail.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalOrderDetail.setTitle("Chi tiết phiếu trả hàng");
        modalOrderDetail.setMinimumSize(new java.awt.Dimension(960, 512));
        modalOrderDetail.setModal(true);
        modalOrderDetail.setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        jPanel1.add(scrollTableDetail);

        javax.swing.GroupLayout modalOrderDetailLayout = new javax.swing.GroupLayout(modalOrderDetail.getContentPane());
        modalOrderDetail.getContentPane().setLayout(modalOrderDetailLayout);
        modalOrderDetailLayout.setHorizontalGroup(
            modalOrderDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        modalOrderDetailLayout.setVerticalGroup(
            modalOrderDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(204, 204, 0));
        setMinimumSize(new java.awt.Dimension(1226, 278));
        setPreferredSize(new java.awt.Dimension(1226, 278));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setPreferredSize(new java.awt.Dimension(1226, 278));
        pnAll.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        headerPanel.setMinimumSize(new java.awt.Dimension(1190, 104));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(590, 100));

        txtOrder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtOrder.setMinimumSize(new java.awt.Dimension(300, 40));
        txtOrder.setPreferredSize(new java.awt.Dimension(300, 40));

        btnSearch.setBackground(new java.awt.Color(115, 165, 71));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Tìm kiếm");
        btnSearch.setMaximumSize(new java.awt.Dimension(150, 40));
        btnSearch.setMinimumSize(new java.awt.Dimension(150, 40));
        btnSearch.setPreferredSize(new java.awt.Dimension(150, 40));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jDateTo.setBackground(new java.awt.Color(255, 255, 255));
        jDateTo.setDateFormatString("dd/MM/yyyy");
        jDateTo.setFocusable(false);
        jDateTo.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jDateTo.setPreferredSize(new java.awt.Dimension(100, 22));

        jDateFrom.setBackground(new java.awt.Color(255, 255, 255));
        jDateFrom.setDateFormatString("dd/MM/yyyy");
        jDateFrom.setFocusable(false);
        jDateFrom.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jDateFrom.setPreferredSize(new java.awt.Dimension(100, 22));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setText("-->");

        txtExport.setBackground(new java.awt.Color(115, 165, 71));
        txtExport.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtExport.setForeground(new java.awt.Color(255, 255, 255));
        txtExport.setText("Xuất excel");
        txtExport.setMaximumSize(new java.awt.Dimension(150, 40));
        txtExport.setMinimumSize(new java.awt.Dimension(150, 40));
        txtExport.setPreferredSize(new java.awt.Dimension(150, 40));
        txtExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExportActionPerformed(evt);
            }
        });

        btnView.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnView.setText("XEM CHI TIẾT");
        btnView.setBorder(null);
        btnView.setBorderPainted(false);
        btnView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnView.setFocusPainted(false);
        btnView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnView.setPreferredSize(new java.awt.Dimension(130, 40));
        btnView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 115, Short.MAX_VALUE)
                    .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(txtExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtExport, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        headerPanel.add(jPanel5, java.awt.BorderLayout.CENTER);

        pnAll.add(headerPanel, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMinimumSize(new java.awt.Dimension(1226, 174));
        jPanel4.setPreferredSize(new java.awt.Dimension(1226, 174));
        jPanel4.setLayout(new java.awt.BorderLayout());

        // Thêm tiêu đề "DANH SÁCH THÔNG TIN ĐƠN TRẢ HÀNG"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN ĐƠN TRẢ HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng
        titlePanel.add(lblTitle);
        jPanel4.add(titlePanel, java.awt.BorderLayout.NORTH);

        jPanel4.add(scrollTable, java.awt.BorderLayout.CENTER);

        pnAll.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(pnAll);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // Lấy thông tin từ form
        java.util.Date date1 = jDateFrom.getDate();
        java.util.Date date2 = jDateTo.getDate();
        
        if (date1 == null || date2 == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn khoảng thời gian!");
            return;
        }
        
        LocalDate dateFrom = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateTo = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (dateFrom.isAfter(dateTo)) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Ngày bắt đầu phải trước ngày kết thúc!");
            return;
        }
        
        String maPhieuTra = txtOrder.getText().trim();
        
        // Tìm kiếm theo mã phiếu trả
        List<DonTraHang> result = searchDonTraHang(dateFrom, dateTo, "", maPhieuTra);
        fillContent(result);
        
        if (result.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy phiếu trả hàng nào!");
        } else {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Tìm thấy " + result.size() + " phiếu trả hàng!");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExportActionPerformed
        boolean success = vn.edu.iuh.fit.iuhpharmacitymanagement.util.XuatFileExcel.xuatExcelVoiDialogVaTieuDe(
            tableDesign.getTable(), 
            "DanhSachTraHang",
            "Trả Hàng", 
            "DANH SÁCH THÔNG TIN TRẢ HÀNG"
        );
        if (success) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file Excel thành công!");
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Xuất file Excel thất bại!");
        }
    }//GEN-LAST:event_txtExportActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();
        
        if(selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn phiếu trả hàng cần xem chi tiết!");
        } else {
            String maDTH = (String) table.getValueAt(selectedRow, 0);
            
            // TODO: Hiển thị chi tiết phiếu trả hàng
            Notifications.getInstance().show(Notifications.Type.INFO, "Xem chi tiết phiếu: " + maDTH);
            
            // Có thể mở modal ở đây sau khi implement đầy đủ
            // modalOrderDetail.setLocationRelativeTo(null);
            // modalOrderDetail.setVisible(true);
        }
    }//GEN-LAST:event_btnViewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnView;
    private javax.swing.JPanel headerPanel;
    private com.toedter.calendar.JDateChooser jDateFrom;
    private com.toedter.calendar.JDateChooser jDateTo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JDialog modalOrderDetail;
    private javax.swing.JPanel pnAll;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JScrollPane scrollTableDetail;
    private javax.swing.JButton txtExport;
    private javax.swing.JTextField txtOrder;
    // End of variables declaration//GEN-END:variables

}

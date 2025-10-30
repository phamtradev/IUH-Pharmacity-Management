/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Dashboard cho nhân viên - Hiển thị thông tin tổng quan về hoạt động bán hàng
 * @author PhamTra
 */
public class GD_DashBoard extends javax.swing.JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private DecimalFormat currencyFormat;
    
    // Dashboard cards
    private DashboardCard cardDoanhThu;
    private DashboardCard cardDonHang;
    private DashboardCard cardKhachHang;
    private DashboardCard cardDonXuatHuy;

    public GD_DashBoard() {
        currencyFormat = new DecimalFormat("#,###");
        
        initComponents();
        setupDashboard();
        loadDashboardData();
    }

    /**
     * Thiết lập giao diện dashboard
     */
    private void setupDashboard() {
        // Tạo panel chứa các cards thống kê
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setLayout(new GridLayout(1, 4, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Card 1: Doanh thu hôm nay
        cardDoanhThu = new DashboardCard(
                "DOANH THU HÔM NAY",
                "0 đ",
                "Tổng doanh thu trong ngày",
                new Color(76, 175, 80),
                Color.WHITE
        );

        // Card 2: Đơn hàng hôm nay
        cardDonHang = new DashboardCard(
                "ĐƠN HÀNG HÔM NAY",
                "0",
                "Tổng số đơn đã bán",
                new Color(33, 150, 243),
                Color.WHITE
        );

        // Card 3: Khách hàng mới
        cardKhachHang = new DashboardCard(
                "KHÁCH HÀNG MỚI",
                "0",
                "Khách hàng đăng ký mới",
                new Color(255, 152, 0),
                Color.WHITE
        );

        // Card 4: Đơn xuất hủy
        cardDonXuatHuy = new DashboardCard(
                "ĐƠN XUẤT HỦY",
                "0",
                "Đơn xuất hủy hôm nay",
                new Color(244, 67, 54),
                Color.WHITE
        );

        statsPanel.add(cardDoanhThu);
        statsPanel.add(cardDonHang);
        statsPanel.add(cardKhachHang);
        statsPanel.add(cardDonXuatHuy);

        // Thêm stats panel vào đầu
        pnAll.add(statsPanel, BorderLayout.NORTH);
        
        // Setup bảng đơn hàng gần đây
        setupRecentOrdersTable();
    }

    /**
     * Thiết lập bảng hiển thị đơn hàng gần đây
     */
    private void setupRecentOrdersTable() {
        String[] headers = {"Mã hóa đơn", "Ngày tạo", "Nhân viên", "Khách hàng", "Tổng tiền"};
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Màu selection nổi bật khi click
        table.setSelectionBackground(new Color(64, 158, 255));  // Màu xanh dương đậm
        table.setSelectionForeground(Color.WHITE);  // Chữ màu trắng
        
        table.setGridColor(new Color(230, 230, 230));
        
        scrollTable.setViewportView(table);
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        
        // Thêm tiêu đề cho bảng
        JLabel lblTableTitle = new JLabel("ĐƠN HÀNG GẦN ĐÂY");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTableTitle.setForeground(new Color(51, 51, 51));
        lblTableTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JPanel tableTitlePanel = new JPanel(new BorderLayout());
        tableTitlePanel.setBackground(Color.WHITE);
        tableTitlePanel.add(lblTableTitle, BorderLayout.WEST);
        
        jPanel4.add(tableTitlePanel, BorderLayout.NORTH);
    }

    /**
     * Tải dữ liệu dashboard (dữ liệu mẫu)
     */
    private void loadDashboardData() {
        try {
            // Dữ liệu mẫu cho các cards
            double tongDoanhThu = 15750000; // 15.750.000 đ
            int soDonHang = 23;
            int soKhachHang = 18;
            int soDonXuatHuy = 5;

            // Cập nhật cards
            cardDoanhThu.updateValue(currencyFormat.format(tongDoanhThu) + " đ");
            cardDonHang.updateValue(String.valueOf(soDonHang) + " đơn");
            cardKhachHang.updateValue(String.valueOf(soKhachHang) + " khách");
            cardDonXuatHuy.updateValue(String.valueOf(soDonXuatHuy) + " đơn");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải dữ liệu dashboard: " + e.getMessage(),
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Làm mới dữ liệu dashboard
     */
    public void refreshDashboard() {
        loadDashboardData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnAll = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();

        setBackground(new java.awt.Color(240, 240, 240));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        pnAll.setBackground(new java.awt.Color(240, 240, 240));
        pnAll.setLayout(new java.awt.BorderLayout(0, 10));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        jPanel4.setLayout(new java.awt.BorderLayout());

        scrollTable.setBorder(null);
        jPanel4.add(scrollTable, java.awt.BorderLayout.CENTER);

        pnAll.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(pnAll);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnAll;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane scrollTable;
    // End of variables declaration//GEN-END:variables

}

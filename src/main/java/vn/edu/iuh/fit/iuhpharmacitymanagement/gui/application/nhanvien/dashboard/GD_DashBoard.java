/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyxuathuy.GD_QuanLyXuatHuy;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.Chart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.ModelChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager;

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
    private DashboardCard cardDonNhapHang;
    private DashboardCard cardDonXuatHuy;
    
    // Business logic
    private DonHangBUS donHangBUS;
    private DonNhapHangBUS donNhapHangBUS;
    private SanPhamDAO sanPhamDAO;
    private LoHangDAO loHangDAO;
    
    // Current user
    private NhanVien currentUser;
    
    // Chart
    private Chart chart;
    
    // Alert panel
    private JPanel alertPanel;
    
    // Animation
    private Animator fadeInAnimator;

    public GD_DashBoard() {
        currencyFormat = new DecimalFormat("#,###");
        donHangBUS = new DonHangBUS();
        donNhapHangBUS = new DonNhapHangBUS();
        sanPhamDAO = new SanPhamDAO();
        loHangDAO = new LoHangDAO();
        
        // Lấy thông tin nhân viên đang đăng nhập
        currentUser = SessionManager.getInstance().getCurrentUser();
        
        initComponents();
        setupDashboard();
        loadDashboardData();
        startFadeInAnimation();
    }

    /**
     * Thiết lập giao diện dashboard
     */
    private void setupDashboard() {
        pnAll.removeAll();
        pnAll.setLayout(new BorderLayout(0, 10));
        
        // Panel chứa tất cả content (để thêm padding)
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ========== HEADER: Lời chào ==========
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // ========== CARDS: Thống kê ==========
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // ========== ROW: Chart + Alert ==========
        JPanel chartAlertRow = new JPanel(new GridLayout(1, 2, 20, 0));
        chartAlertRow.setBackground(new Color(240, 240, 240));
        chartAlertRow.setOpaque(false);
        
        // Chart panel
        JPanel chartPanel = createChartPanel();
        chartAlertRow.add(chartPanel);
        
        // Alert panel
        alertPanel = createAlertPanel();
        chartAlertRow.add(alertPanel);
        
        contentPanel.add(chartAlertRow);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // ========== TABLE: Đơn hàng gần đây ==========
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel);
        
        pnAll.add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo header panel với lời chào
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        // Lời chào
        String timeGreeting = getTimeGreeting();
        String userName = currentUser != null ? currentUser.getTenNhanVien() : "Nhân viên";
        JLabel lblGreeting = new JLabel(timeGreeting + ", " + userName + "!");
        lblGreeting.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGreeting.setForeground(new Color(51, 51, 51));
        
        // Ngày hiện tại
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateText = "Hôm nay là " + getDayOfWeekVietnamese(today) + ", " + today.format(formatter);
        JLabel lblDate = new JLabel(dateText);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setForeground(new Color(102, 102, 102));
        
        panel.add(lblGreeting, BorderLayout.WEST);
        panel.add(lblDate, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Lấy lời chào theo thời gian
     */
    private String getTimeGreeting() {
        int hour = java.time.LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "Chào buổi sáng";
        } else if (hour >= 12 && hour < 18) {
            return "Chào buổi chiều";
        } else {
            return "Chào buổi tối";
        }
    }
    
    /**
     * Lấy tên ngày trong tuần tiếng Việt
     */
    private String getDayOfWeekVietnamese(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY: return "Thứ Hai";
            case TUESDAY: return "Thứ Ba";
            case WEDNESDAY: return "Thứ Tư";
            case THURSDAY: return "Thứ Năm";
            case FRIDAY: return "Thứ Sáu";
            case SATURDAY: return "Thứ Bảy";
            case SUNDAY: return "Chủ Nhật";
            default: return "";
        }
    }
    
    /**
     * Tạo stats panel với các cards
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setLayout(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        // Card 1: Doanh thu cá nhân hôm nay
        cardDoanhThu = new DashboardCard(
                "DOANH THU CÁ NHÂN",
                "0 đ",
                "Doanh thu hôm nay",
                new Color(76, 175, 80),
                Color.WHITE,
                "10.svg"  // Icon đơn hàng
        );

        // Card 2: Đơn hàng cá nhân hôm nay
        cardDonHang = new DashboardCard(
                "ĐƠN HÀNG CÁ NHÂN",
                "0",
                "Số đơn đã bán",
                new Color(33, 150, 243),
                Color.WHITE,
                "10.svg"  // Icon đơn hàng
        );

        // Card 3: Đơn nhập hàng trong tuần
        cardDonNhapHang = new DashboardCard(
                "ĐƠN NHẬP HÀNG",
                "0",
                "Trong tuần này",
                new Color(255, 152, 0),
                Color.WHITE,
                "10.svg"  // Icon đơn hàng
        );

        // Card 4: Số đơn cần xuất hủy
        cardDonXuatHuy = new DashboardCard(
                "SỐ ĐƠN CẦN XUẤT HỦY",
                "0",
                "Cần xử lý",
                new Color(244, 67, 54),
                Color.WHITE,
                "8.svg"  // Icon trả hàng
        );

        statsPanel.add(cardDoanhThu);
        statsPanel.add(cardDonHang);
        statsPanel.add(cardDonNhapHang);
        statsPanel.add(cardDonXuatHuy);

        return statsPanel;
    }
    
    /**
     * Tạo chart panel
     */
    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel lblTitle = new JLabel("DOANH THU 7 NGÀY GẦN NHẤT (CÁ NHÂN)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(51, 51, 51));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Chart
        chart = new Chart();
        chart.setPreferredSize(new Dimension(600, 600));
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(chart, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Tạo alert panel - Thông báo sản phẩm sắp hết hàng
     */
    private JPanel createAlertPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel lblTitle = new JLabel("⚠️ CẢNH BÁO TỒN KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(244, 67, 54));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        panel.add(lblTitle, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Tạo table panel
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel lblTableTitle = new JLabel("10 ĐƠN HÀNG GẦN NHẤT (CÁ NHÂN)");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTableTitle.setForeground(new Color(51, 51, 51));
        lblTableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Setup table
        setupRecentOrdersTable();
        
        panel.add(lblTableTitle, BorderLayout.NORTH);
        panel.add(scrollTable, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Thiết lập bảng hiển thị đơn hàng gần đây
     */
    private void setupRecentOrdersTable() {
        String[] headers = {"Mã hóa đơn", "Ngày tạo", "Khách hàng", "Tổng tiền"};
        
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(50);  // Tăng height để dễ đọc hơn
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.getTableHeader().setForeground(new Color(51, 51, 51));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));  // Tăng height header
        
        // Màu selection nổi bật khi click
        table.setSelectionBackground(new Color(64, 158, 255));
        table.setSelectionForeground(Color.WHITE);
        
        table.setGridColor(new Color(235, 235, 235));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        
        scrollTable.setViewportView(table);
        scrollTable.setBorder(null);
        scrollTable.setBackground(Color.WHITE);
    }

    /**
     * Tải dữ liệu dashboard
     */
    private void loadDashboardData() {
        try {
            LocalDate today = LocalDate.now();
            String maNhanVien = currentUser != null ? currentUser.getMaNhanVien() : "";
            
            // ========== LẤY DỮ LIỆU ĐƠN HÀNG CÁ NHÂN ==========
            List<DonHang> allOrders = donHangBUS.layTatCaDonHang();
            
            // Lọc đơn hàng của nhân viên này hôm nay
            List<DonHang> myOrdersToday = allOrders.stream()
                    .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(today))
                    .filter(dh -> dh.getNhanVien() != null && 
                            dh.getNhanVien().getMaNhanVien().equals(maNhanVien))
                    .collect(Collectors.toList());
            
            // Tính doanh thu và số đơn cá nhân
            double tongDoanhThu = myOrdersToday.stream()
                    .mapToDouble(DonHang::getThanhTien)
                    .sum();
            int soDonHang = myOrdersToday.size();
            
            // ========== LẤY DỮ LIỆU ĐƠN NHẬP HÀNG ==========
            // Đếm số đơn nhập hàng trong tuần
            int soDonNhapHangTrongTuan = donNhapHangBUS.demDonNhapHangTrongTuan();
            
            // ========== LẤY DỮ LIỆU SỐ ĐƠN CẦN XUẤT HỦY ==========
            // Tạo instance của GD_QuanLyXuatHuy để lấy số lượng đơn cần xuất hủy
            GD_QuanLyXuatHuy gdXuatHuy = new GD_QuanLyXuatHuy();
            int soDonCanXuatHuy = gdXuatHuy.demSoDonCanXuatHuy();

            // ========== CẬP NHẬT CARDS ==========
            cardDoanhThu.updateValue(currencyFormat.format(tongDoanhThu) + " đ");
            cardDonHang.updateValue(String.valueOf(soDonHang) + " đơn");
            cardDonNhapHang.updateValue(String.valueOf(soDonNhapHangTrongTuan) + " đơn");
            cardDonXuatHuy.updateValue(String.valueOf(soDonCanXuatHuy) + " đơn");
            
            // ========== LOAD BIỂU ĐỒ 7 NGÀY ==========
            loadChartData();
            
            // ========== LOAD DANH SÁCH SẢN PHẨM SẮP HẾT ==========
            loadLowStockProducts();
            
            // ========== LOAD BẢNG ĐƠN HÀNG GẦN ĐÂY ==========
            loadRecentOrders(maNhanVien);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi tải dữ liệu dashboard: " + e.getMessage(),
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load dữ liệu biểu đồ doanh thu 7 ngày gần nhất (cá nhân)
     */
    private void loadChartData() {
        chart.clear();
        chart.clearLegends();
        chart.addLegend("Doanh thu", new Color(76, 175, 80));
        
        LocalDate today = LocalDate.now();
        String maNhanVien = currentUser != null ? currentUser.getMaNhanVien() : "";
        List<DonHang> allOrders = donHangBUS.layTatCaDonHang();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            
            // Lọc đơn hàng của nhân viên này trong ngày
            double sumPrice = allOrders.stream()
                    .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(date))
                    .filter(dh -> dh.getNhanVien() != null && 
                            dh.getNhanVien().getMaNhanVien().equals(maNhanVien))
                    .mapToDouble(DonHang::getThanhTien)
                    .sum();
            
            chart.addData(new ModelChart(date.getDayOfMonth() + "/" + date.getMonthValue(),
                    new double[]{sumPrice}));
        }
        chart.start();
    }
    
    /**
     * Load danh sách sản phẩm sắp hết hàng
     */
    private void loadLowStockProducts() {
        try {
            // Lấy tất cả sản phẩm
            List<SanPham> allProducts = sanPhamDAO.findAll();
            LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
            
            // Panel chứa danh sách cảnh báo
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.WHITE);
            
            int count = 0;
            for (SanPham sp : allProducts) {
                if (count >= 5) break;  // Chỉ hiển thị 5 sản phẩm
                
                // Tính tồn kho
                List<LoHang> loHangs = loHangDAO.findByMaSanPham(sp.getMaSanPham());
                int tonKho = loHangs.stream()
                        .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan))
                        .mapToInt(LoHang::getTonKho)
                        .sum();
                
                // Chỉ hiển thị sản phẩm tồn kho < 10
                if (tonKho < 10 && tonKho >= 0) {
                    JPanel itemPanel = createLowStockItem(sp.getTenSanPham(), tonKho);
                    contentPanel.add(itemPanel);
                    contentPanel.add(Box.createVerticalStrut(8));
                    count++;
                }
            }
            
            if (count == 0) {
                JLabel lblNoAlert = new JLabel("✓ Tất cả sản phẩm đều đủ hàng");
                lblNoAlert.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lblNoAlert.setForeground(new Color(76, 175, 80));
                contentPanel.add(lblNoAlert);
            }
            
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(null);
            scrollPane.setBackground(Color.WHITE);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            alertPanel.add(scrollPane, BorderLayout.CENTER);
            alertPanel.revalidate();
            alertPanel.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Tạo item cho sản phẩm sắp hết hàng
     */
    private JPanel createLowStockItem(String tenSanPham, int tonKho) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(255, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 205, 210), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Icon cảnh báo
        JLabel lblIcon = new JLabel("⚠️");
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        
        // Tên sản phẩm
        JLabel lblName = new JLabel(tenSanPham);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblName.setForeground(new Color(51, 51, 51));
        
        // Số lượng tồn
        JLabel lblStock = new JLabel("Còn: " + tonKho);
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblStock.setForeground(new Color(244, 67, 54));
        
        panel.add(lblIcon, BorderLayout.WEST);
        panel.add(lblName, BorderLayout.CENTER);
        panel.add(lblStock, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Load bảng đơn hàng gần đây (cá nhân)
     */
    private void loadRecentOrders(String maNhanVien) {
        try {
            tableModel.setRowCount(0);
            
            List<DonHang> allOrders = donHangBUS.layTatCaDonHang();
            
            // Lọc đơn hàng của nhân viên này, sắp xếp theo ngày mới nhất
            List<DonHang> myOrders = allOrders.stream()
                    .filter(dh -> dh.getNhanVien() != null && 
                            dh.getNhanVien().getMaNhanVien().equals(maNhanVien))
                    .sorted((a, b) -> b.getNgayDatHang().compareTo(a.getNgayDatHang()))
                    .limit(10)  // Chỉ lấy 10 đơn gần nhất
                    .collect(Collectors.toList());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (DonHang dh : myOrders) {
                String tenKhach = dh.getKhachHang() != null ? 
                        dh.getKhachHang().getTenKhachHang() : "Khách lẻ";
                
                tableModel.addRow(new Object[]{
                    dh.getMaDonHang(),
                    dh.getNgayDatHang().format(formatter),
                    tenKhach,
                    currencyFormat.format(dh.getThanhTien()) + " đ"
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Animation fade in khi load dashboard
     */
    private void startFadeInAnimation() {
        setOpaque(false);
        
        TimingTargetAdapter target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                float alpha = fraction;
                setOpaque(alpha >= 1.0f);
                repaint();
            }
        };
        
        fadeInAnimator = new Animator(600, target);
        fadeInAnimator.setResolution(0);
        fadeInAnimator.setAcceleration(0.3f);
        fadeInAnimator.setDeceleration(0.5f);
        fadeInAnimator.start();
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

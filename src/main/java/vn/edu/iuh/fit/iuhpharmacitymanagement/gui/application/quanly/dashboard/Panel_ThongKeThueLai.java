/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard;

import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.Chart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.ModelChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.dashboard.DashboardCard;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.ThongKeThueLaiService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.DinhDangSo;

import java.awt.*;
import java.time.LocalDate;
import java.util.Map;
import javax.swing.*;

/**
 * Panel thống kê thuế và lãi cho dashboard quản lý
 * @author PhamTra
 */
public class Panel_ThongKeThueLai extends javax.swing.JPanel {

    private final ThongKeThueLaiService thongKeService;
    private Chart chartThue;
    private Chart chartLai;
    private String currentPeriod = "7 ngày qua"; // "7 ngày qua", "Tháng này", "12 tháng"
    
    // Dashboard cards
    private DashboardCard cardThueThu;
    private DashboardCard cardThueTra;
    private DashboardCard cardLoiNhuan;
    private DashboardCard cardTySuat;
    
    // Chart panels (comboPeriod được khai báo trong NetBeans generated code)
    private JComboBox<String> comboPeriod;

    public Panel_ThongKeThueLai() {
        thongKeService = new ThongKeThueLaiService();
        // Khởi tạo charts trước
        chartThue = new Chart();
        chartLai = new Chart();
        initComponents();
        setupDashboard();
        // Load dữ liệu trong background để không block UI
        loadDataInBackground();
    }

    /**
     * Thiết lập giao diện dashboard theo style của dashboard nhân viên
     */
    private void setupDashboard() {
        pnAll.removeAll();
        pnAll.setLayout(new BorderLayout(0, 10));
        
        // Panel chứa tất cả content (để thêm padding)
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ========== CARDS: Thống kê ==========
        JPanel statsPanel = createStatsPanel();
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // ========== ROW: Chart Thuế + Chart Lãi ==========
        JPanel chartRow = new JPanel(new GridLayout(1, 2, 20, 0));
        chartRow.setBackground(new Color(240, 240, 240));
        chartRow.setOpaque(false);
        
        // Chart thuế panel
        JPanel chartThuePanel = createChartPanel("THỐNG KÊ THUẾ GTGT", true);
        chartRow.add(chartThuePanel);
        
        // Chart lãi panel
        JPanel chartLaiPanel = createChartPanel("THỐNG KÊ LỢI NHUẬN", false);
        chartRow.add(chartLaiPanel);
        
        contentPanel.add(chartRow);
        
        pnAll.add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo stats panel với các cards
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setLayout(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        // Card 1: Thuế GTGT đã thu
        cardThueThu = new DashboardCard(
                "THUẾ GTGT ĐÃ THU",
                "0 đ",
                "Tháng này",
                new Color(76, 175, 80),
                Color.WHITE,
                null  // Không dùng icon, dùng emoji trong description
        );

        // Card 2: Thuế GTGT đã trả
        cardThueTra = new DashboardCard(
                "THUẾ GTGT ĐÃ TRẢ",
                "0 đ",
                "Tháng này",
                new Color(244, 67, 54),
                Color.WHITE,
                null
        );

        // Card 3: Tổng lợi nhuận
        cardLoiNhuan = new DashboardCard(
                "TỔNG LỢI NHUẬN",
                "0 đ",
                "Tháng này",
                new Color(33, 150, 243),
                Color.WHITE,
                null
        );

        // Card 4: Tỷ suất lợi nhuận
        cardTySuat = new DashboardCard(
                "TỶ SUẤT LỢI NHUẬN",
                "0%",
                "Tháng này",
                new Color(255, 152, 0),
                Color.WHITE,
                null
        );

        statsPanel.add(cardThueThu);
        statsPanel.add(cardThueTra);
        statsPanel.add(cardLoiNhuan);
        statsPanel.add(cardTySuat);

        return statsPanel;
    }
    
    /**
     * Tạo chart panel với style giống dashboard nhân viên
     */
    private JPanel createChartPanel(String title, boolean hasCombo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Thanh tiêu đề + nút chọn chế độ xem (nếu có)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(51, 51, 51));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Bộ lọc chế độ xem (chỉ cho chart thuế)
        if (hasCombo) {
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            filterPanel.setOpaque(false);
            JLabel lblFilter = new JLabel("Xem theo:");
            lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            comboPeriod = new JComboBox<>(new String[] { "7 ngày qua", "Tháng này", "12 tháng" });
            comboPeriod.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            comboPeriod.addActionListener(e -> {
                if (comboPeriod.getSelectedItem() != null) {
                    currentPeriod = (String) comboPeriod.getSelectedItem();
                    loadDataCharts();
                }
            });
            filterPanel.add(lblFilter);
            filterPanel.add(comboPeriod);
            topBar.add(filterPanel, BorderLayout.EAST);
        }

        topBar.add(lblTitle, BorderLayout.WEST);
        
        // Sử dụng panel chart đã khai báo
        JPanel chartContainer = hasCombo ? pnChartThue : pnChartLai;
        chartContainer.setLayout(new BorderLayout());
        chartContainer.setBackground(Color.WHITE);
        
        // Thêm chart vào container
        if (hasCombo) {
            chartContainer.removeAll();
            chartContainer.add(chartThue, BorderLayout.CENTER);
        } else {
            chartContainer.removeAll();
            chartContainer.add(chartLai, BorderLayout.CENTER);
        }
        
        panel.add(topBar, BorderLayout.NORTH);
        panel.add(chartContainer, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Load dữ liệu trong background để không block UI
     */
    private void loadDataInBackground() {
        // Hiển thị loading
        cardThueThu.updateValue("Đang tải...");
        cardThueTra.updateValue("Đang tải...");
        cardLoiNhuan.updateValue("Đang tải...");
        cardTySuat.updateValue("Đang tải...");

        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                LocalDate today = LocalDate.now();
                LocalDate monthStart = today.withDayOfMonth(1);
                LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());

                // Tính toán số liệu tháng này
                double tongThueThu = thongKeService.tinhTongThueThu(monthStart, monthEnd);
                double tongThueTra = thongKeService.tinhTongThueTra(monthStart, monthEnd);
                double tongLoiNhuan = thongKeService.tinhTongLoiNhuan(monthStart, monthEnd);
                double tySuatLoiNhuan = thongKeService.tinhTySuatLoiNhuan(monthStart, monthEnd);

                // Cập nhật UI trong EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    cardThueThu.updateValue(DinhDangSo.dinhDangTien(tongThueThu));
                    cardThueTra.updateValue(DinhDangSo.dinhDangTien(tongThueTra));
                    cardLoiNhuan.updateValue(DinhDangSo.dinhDangTien(tongLoiNhuan));
                    cardTySuat.updateValue(String.format("%.2f%%", tySuatLoiNhuan));
                });

                return null;
            }

            @Override
            protected void done() {
                // Load biểu đồ sau khi load xong data cards
                loadDataCharts();
            }
        };
        worker.execute();
    }

    private void loadDataCharts() {
        if (currentPeriod.equals("7 ngày qua")) {
            loadChart7Days();
        } else if (currentPeriod.equals("Tháng này")) {
            loadChartThisMonth();
        } else if (currentPeriod.equals("12 tháng")) {
            loadChart12Months();
        }
    }

    private void loadChart7Days() {
        chartThue.clear();
        chartLai.clear();

        // Load trong background để không block UI
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Map<String, Map<String, Double>> thueData = thongKeService.layThueTheoNgay();
                Map<String, Map<String, Double>> laiData = thongKeService.layLaiTheoNgay();

                // Cập nhật chart trong EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    chartThue.clearLegends();
                    chartThue.addLegend("Thuế thu", new Color(76, 175, 80));
                    chartThue.addLegend("Thuế trả", new Color(244, 67, 54));
                    chartThue.addLegend("Thuế ròng", new Color(33, 150, 243));
                    
                    chartLai.clearLegends();
                    chartLai.addLegend("Doanh thu", new Color(76, 175, 80));
                    chartLai.addLegend("Giá vốn", new Color(244, 67, 54));
                    chartLai.addLegend("Lợi nhuận", new Color(33, 150, 243));
                    
                    for (Map.Entry<String, Map<String, Double>> entry : thueData.entrySet()) {
                        String label = entry.getKey();
                        Map<String, Double> values = entry.getValue();
                        chartThue.addData(new ModelChart(label,
                                new double[]{
                                        values.get("thueThu"),
                                        values.get("thueTra"),
                                        values.get("thueRong")
                                }));
                    }

                    for (Map.Entry<String, Map<String, Double>> entry : laiData.entrySet()) {
                        String label = entry.getKey();
                        Map<String, Double> values = entry.getValue();
                        chartLai.addData(new ModelChart(label,
                                new double[]{
                                        values.get("doanhThu"),
                                        values.get("giaVon"),
                                        values.get("loiNhuan")
                                }));
                    }

                    chartThue.start();
                    chartLai.start();
                });

                return null;
            }
        };
        worker.execute();
    }

    private void loadChartThisMonth() {
        chartThue.clear();
        chartLai.clear();

        // Load trong background để không block UI
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                LocalDate today = LocalDate.now();
                int currentDay = today.getDayOfMonth();

                // Tính toán tất cả dữ liệu trước
                double[] thueThuData = new double[currentDay];
                double[] thueTraData = new double[currentDay];
                double[] thueRongData = new double[currentDay];
                double[] doanhThuData = new double[currentDay];
                double[] giaVonData = new double[currentDay];
                double[] loiNhuanData = new double[currentDay];

                for (int day = 1; day <= currentDay; day++) {
                    LocalDate date = LocalDate.of(today.getYear(), today.getMonth(), day);
                    thueThuData[day - 1] = thongKeService.tinhTongThueThu(date, date);
                    thueTraData[day - 1] = thongKeService.tinhTongThueTra(date, date);
                    thueRongData[day - 1] = thongKeService.tinhThueRong(date, date);
                    doanhThuData[day - 1] = thongKeService.tinhTongDoanhThu(date, date);
                    giaVonData[day - 1] = thongKeService.tinhTongGiaVon(date, date);
                    loiNhuanData[day - 1] = thongKeService.tinhTongLoiNhuan(date, date);
                }

                // Cập nhật chart trong EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    chartThue.clearLegends();
                    chartThue.addLegend("Thuế thu", new Color(76, 175, 80));
                    chartThue.addLegend("Thuế trả", new Color(244, 67, 54));
                    chartThue.addLegend("Thuế ròng", new Color(33, 150, 243));
                    
                    chartLai.clearLegends();
                    chartLai.addLegend("Doanh thu", new Color(76, 175, 80));
                    chartLai.addLegend("Giá vốn", new Color(244, 67, 54));
                    chartLai.addLegend("Lợi nhuận", new Color(33, 150, 243));
                    
                    for (int day = 1; day <= currentDay; day++) {
                        String label = String.valueOf(day);
                        chartThue.addData(new ModelChart(label, 
                                new double[]{thueThuData[day - 1], thueTraData[day - 1], thueRongData[day - 1]}));
                        chartLai.addData(new ModelChart(label, 
                                new double[]{doanhThuData[day - 1], giaVonData[day - 1], loiNhuanData[day - 1]}));
                    }
                    chartThue.start();
                    chartLai.start();
                });

                return null;
            }
        };
        worker.execute();
    }

    private void loadChart12Months() {
        chartThue.clear();
        chartLai.clear();

        // Load trong background
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Map<String, Map<String, Double>> thueData = thongKeService.layThueTheoThang();
                Map<String, Map<String, Double>> laiData = thongKeService.layLaiTheoThang();

                // Cập nhật chart trong EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    chartThue.clearLegends();
                    chartThue.addLegend("Thuế thu", new Color(76, 175, 80));
                    chartThue.addLegend("Thuế trả", new Color(244, 67, 54));
                    chartThue.addLegend("Thuế ròng", new Color(33, 150, 243));
                    
                    chartLai.clearLegends();
                    chartLai.addLegend("Doanh thu", new Color(76, 175, 80));
                    chartLai.addLegend("Giá vốn", new Color(244, 67, 54));
                    chartLai.addLegend("Lợi nhuận", new Color(33, 150, 243));
                    
                    for (Map.Entry<String, Map<String, Double>> entry : thueData.entrySet()) {
                        String label = entry.getKey();
                        Map<String, Double> values = entry.getValue();
                        chartThue.addData(new ModelChart(label,
                                new double[]{
                                        values.get("thueThu"),
                                        values.get("thueTra"),
                                        values.get("thueRong")
                                }));
                    }

                    for (Map.Entry<String, Map<String, Double>> entry : laiData.entrySet()) {
                        String label = entry.getKey();
                        Map<String, Double> values = entry.getValue();
                        chartLai.addData(new ModelChart(label,
                                new double[]{
                                        values.get("doanhThu"),
                                        values.get("giaVon"),
                                        values.get("loiNhuan")
                                }));
                    }

                    chartThue.start();
                    chartLai.start();
                });

                return null;
            }
        };
        worker.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnAll = new javax.swing.JPanel();
        pnChartThue = new javax.swing.JPanel();
        pnChartLai = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        pnAll.setBackground(new java.awt.Color(240, 240, 240));
        pnAll.setLayout(new java.awt.BorderLayout(0, 10));

        pnChartThue.setBackground(new java.awt.Color(255, 255, 255));
        pnChartThue.setLayout(new java.awt.BorderLayout());

        pnChartLai.setBackground(new java.awt.Color(255, 255, 255));
        pnChartLai.setLayout(new java.awt.BorderLayout());

        add(pnAll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnAll;
    private javax.swing.JPanel pnChartThue;
    private javax.swing.JPanel pnChartLai;
    // End of variables declaration//GEN-END:variables
}

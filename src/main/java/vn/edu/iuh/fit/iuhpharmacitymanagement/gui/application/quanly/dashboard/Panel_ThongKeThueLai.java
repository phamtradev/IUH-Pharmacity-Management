/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard;

import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.Chart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.ModelChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.service.ThongKeThueLaiService;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.DinhDangSo;

import java.awt.Color;
import java.time.LocalDate;
import java.util.Map;

/**
 * Panel thống kê thuế và lãi cho dashboard quản lý
 * @author PhamTra
 */
public class Panel_ThongKeThueLai extends javax.swing.JPanel {

    private final ThongKeThueLaiService thongKeService;
    private Chart chartThue;
    private Chart chartLai;
    private String currentPeriod = "7 ngày qua"; // "7 ngày qua", "Tháng này", "12 tháng"

    public Panel_ThongKeThueLai() {
        thongKeService = new ThongKeThueLaiService();
        initComponents();
        initIcons();
        initCharts();
        // Load dữ liệu trong background để không block UI
        loadDataInBackground();
    }

    private void initIcons() {
        // Dùng emoji và màu sắc thay vì icon để tránh lỗi
        try {
            // Icon thuế thu - màu xanh lá
            lblIconThueThu.setText("💰");
            lblIconThueThu.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 40));
            lblIconThueThu.setForeground(new java.awt.Color(76, 175, 80));
            lblIconThueThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            
            // Icon thuế trả - màu đỏ
            lblIconThueTra.setText("💸");
            lblIconThueTra.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 40));
            lblIconThueTra.setForeground(new java.awt.Color(244, 67, 54));
            lblIconThueTra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            
            // Icon lợi nhuận - màu xanh dương
            lblIconLoiNhuan.setText("📈");
            lblIconLoiNhuan.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 40));
            lblIconLoiNhuan.setForeground(new java.awt.Color(33, 150, 243));
            lblIconLoiNhuan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            
            // Icon tỷ suất - màu cam
            lblIconTySuat.setText("📊");
            lblIconTySuat.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 40));
            lblIconTySuat.setForeground(new java.awt.Color(255, 152, 0));
            lblIconTySuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        } catch (Exception e) {
            // Nếu không hỗ trợ emoji, dùng text đơn giản
            lblIconThueThu.setText("T");
            lblIconThueTra.setText("T");
            lblIconLoiNhuan.setText("L");
            lblIconTySuat.setText("%");
        }
    }

    private void initCharts() {
        // Khởi tạo chart thuế
        chartThue = new Chart();
        chartThue.addLegend("Thuế thu", new Color(76, 175, 80));
        chartThue.addLegend("Thuế trả", new Color(244, 67, 54));
        chartThue.addLegend("Thuế ròng", new Color(33, 150, 243));
        pnChartThue.add(chartThue, java.awt.BorderLayout.CENTER);

        // Khởi tạo chart lãi
        chartLai = new Chart();
        chartLai.addLegend("Doanh thu", new Color(76, 175, 80));
        chartLai.addLegend("Giá vốn", new Color(244, 67, 54));
        chartLai.addLegend("Lợi nhuận", new Color(33, 150, 243));
        pnChartLai.add(chartLai, java.awt.BorderLayout.CENTER);

        // Không load chart ngay, sẽ load sau khi load data cards
    }

    /**
     * Load dữ liệu trong background để không block UI
     */
    private void loadDataInBackground() {
        // Hiển thị loading
        txtThueThu.setText("Đang tải...");
        txtThueTra.setText("Đang tải...");
        txtLoiNhuan.setText("Đang tải...");
        txtTySuat.setText("Đang tải...");

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
                    txtThueThu.setText(DinhDangSo.dinhDangTien(tongThueThu));
                    txtThueTra.setText(DinhDangSo.dinhDangTien(tongThueTra));
                    txtLoiNhuan.setText(DinhDangSo.dinhDangTien(tongLoiNhuan));
                    txtTySuat.setText(String.format("%.2f%%", tySuatLoiNhuan));
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

        jPanel1 = new javax.swing.JPanel();
        pnContent = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lblIconThueThu = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtThueThu = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lblIconThueTra = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtThueTra = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        lblIconLoiNhuan = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtLoiNhuan = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        lblIconTySuat = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtTySuat = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtThueRong = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        lblChartThue = new javax.swing.JLabel();
        comboPeriod = new javax.swing.JComboBox<>();
        pnChartThue = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        lblChartLai = new javax.swing.JLabel();
        pnChartLai = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(255, 255, 255)));
        jPanel1.setMinimumSize(new java.awt.Dimension(1130, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(1130, 800));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 20));

        pnContent.setBackground(new java.awt.Color(255, 255, 255));
        pnContent.setLayout(new java.awt.BorderLayout(0, 20));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 150));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel2.setLayout(new java.awt.GridLayout(2, 2, 16, 8));

        // Card 1: Thuế thu
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        jPanel3.setPreferredSize(new java.awt.Dimension(370, 120));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel5.setLayout(new java.awt.BorderLayout());

        lblIconThueThu.setBackground(new java.awt.Color(255, 255, 255));
        lblIconThueThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconThueThu.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        jPanel5.add(lblIconThueThu, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMinimumSize(new java.awt.Dimension(120, 110));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Thuế GTGT đã thu");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));

        txtThueThu.setFont(new java.awt.Font("Roboto Mono", 1, 32)); // NOI18N
        txtThueThu.setForeground(new java.awt.Color(76, 175, 80)); // Màu xanh lá
        txtThueThu.setText("0 đ");
        txtThueThu.setPreferredSize(new java.awt.Dimension(100, 16));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtThueThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtThueThu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3);

        // Card 2: Thuế trả
        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        jPanel7.setPreferredSize(new java.awt.Dimension(370, 120));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel9.setLayout(new java.awt.BorderLayout());

        lblIconThueTra.setBackground(new java.awt.Color(255, 255, 255));
        lblIconThueTra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconThueTra.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        jPanel9.add(lblIconThueTra, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel9, java.awt.BorderLayout.WEST);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setMinimumSize(new java.awt.Dimension(120, 110));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Thuế GTGT đã trả");
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 16));

        txtThueTra.setFont(new java.awt.Font("Roboto Mono", 1, 32)); // NOI18N
        txtThueTra.setForeground(new java.awt.Color(244, 67, 54)); // Màu đỏ
        txtThueTra.setText("0 đ");
        txtThueTra.setPreferredSize(new java.awt.Dimension(100, 16));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtThueTra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtThueTra)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel7);

        // Card 3: Lợi nhuận
        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        jPanel11.setPreferredSize(new java.awt.Dimension(370, 120));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel13.setLayout(new java.awt.BorderLayout());

        lblIconLoiNhuan.setBackground(new java.awt.Color(255, 255, 255));
        lblIconLoiNhuan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconLoiNhuan.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        jPanel13.add(lblIconLoiNhuan, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel13, java.awt.BorderLayout.WEST);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setMinimumSize(new java.awt.Dimension(120, 110));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Tổng lợi nhuận");
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 16));

        txtLoiNhuan.setFont(new java.awt.Font("Roboto Mono", 1, 32)); // NOI18N
        txtLoiNhuan.setForeground(new java.awt.Color(33, 150, 243)); // Màu xanh dương
        txtLoiNhuan.setText("0 đ");
        txtLoiNhuan.setPreferredSize(new java.awt.Dimension(100, 16));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtLoiNhuan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLoiNhuan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel11);

        // Card 4: Tỷ suất lợi nhuận
        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        jPanel15.setPreferredSize(new java.awt.Dimension(370, 120));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel17.setLayout(new java.awt.BorderLayout());

        lblIconTySuat.setBackground(new java.awt.Color(255, 255, 255));
        lblIconTySuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconTySuat.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        jPanel17.add(lblIconTySuat, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17, java.awt.BorderLayout.WEST);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setMinimumSize(new java.awt.Dimension(120, 110));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Tỷ suất lợi nhuận");
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 16));

        txtTySuat.setFont(new java.awt.Font("Roboto Mono", 1, 32)); // NOI18N
        txtTySuat.setForeground(new java.awt.Color(255, 152, 0)); // Màu cam
        txtTySuat.setText("0%");
        txtTySuat.setPreferredSize(new java.awt.Dimension(100, 16));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTySuat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTySuat)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel15);

        pnContent.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        // Panel biểu đồ thuế
        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.BorderLayout());

        lblChartThue.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        lblChartThue.setText("THỐNG KÊ THUẾ GTGT");
        lblChartThue.setPreferredSize(new java.awt.Dimension(37, 30));

        comboPeriod = new javax.swing.JComboBox<>();
        comboPeriod.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboPeriod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7 ngày qua", "Tháng này", "12 tháng" }));
        comboPeriod.setMinimumSize(new java.awt.Dimension(140, 26));
        comboPeriod.setPreferredSize(new java.awt.Dimension(140, 22));
        comboPeriod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboPeriodItemStateChanged(evt);
            }
        });

        javax.swing.JPanel headerThue = new javax.swing.JPanel(new java.awt.BorderLayout());
        headerThue.setBackground(new java.awt.Color(255, 255, 255));
        headerThue.add(lblChartThue, java.awt.BorderLayout.WEST);
        headerThue.add(comboPeriod, java.awt.BorderLayout.EAST);

        jPanel21.add(headerThue, java.awt.BorderLayout.PAGE_START);

        pnChartThue.setBackground(new java.awt.Color(255, 255, 255));
        pnChartThue.setLayout(new java.awt.BorderLayout());
        jPanel21.add(pnChartThue, java.awt.BorderLayout.CENTER);

        pnContent.add(jPanel21, java.awt.BorderLayout.CENTER);

        // Panel biểu đồ lãi
        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new java.awt.BorderLayout());

        lblChartLai.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        lblChartLai.setText("THỐNG KÊ LỢI NHUẬN");
        lblChartLai.setPreferredSize(new java.awt.Dimension(37, 30));

        javax.swing.JPanel headerLai = new javax.swing.JPanel(new java.awt.BorderLayout());
        headerLai.setBackground(new java.awt.Color(255, 255, 255));
        headerLai.add(lblChartLai, java.awt.BorderLayout.WEST);

        jPanel22.add(headerLai, java.awt.BorderLayout.PAGE_START);

        pnChartLai.setBackground(new java.awt.Color(255, 255, 255));
        pnChartLai.setLayout(new java.awt.BorderLayout());
        jPanel22.add(pnChartLai, java.awt.BorderLayout.CENTER);

        pnContent.add(jPanel22, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(pnContent, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void comboPeriodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboPeriodItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            currentPeriod = (String) comboPeriod.getSelectedItem();
            loadDataCharts();
        }
    }//GEN-LAST:event_comboPeriodItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboPeriod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblChartLai;
    private javax.swing.JLabel lblChartThue;
    private javax.swing.JLabel lblIconLoiNhuan;
    private javax.swing.JLabel lblIconThueThu;
    private javax.swing.JLabel lblIconThueTra;
    private javax.swing.JLabel lblIconTySuat;
    private javax.swing.JPanel pnChartLai;
    private javax.swing.JPanel pnChartThue;
    private javax.swing.JPanel pnContent;
    private javax.swing.JLabel txtLoiNhuan;
    private javax.swing.JLabel txtThueRong;
    private javax.swing.JLabel txtThueThu;
    private javax.swing.JLabel txtThueTra;
    private javax.swing.JLabel txtTySuat;
    // End of variables declaration//GEN-END:variables
}


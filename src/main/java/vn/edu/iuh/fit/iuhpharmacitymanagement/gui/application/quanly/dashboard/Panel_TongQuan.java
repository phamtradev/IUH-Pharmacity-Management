/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO.ProductSalesSummary;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonTraHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.ModelChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.DinhDangSo;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.ResizeImage;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author PhamTra
 */
public class Panel_TongQuan extends javax.swing.JPanel {

    private final DonHangBUS donHangBUS;
    private final DonNhapHangBUS donNhapHangBUS;
    private final DonTraHangBUS donTraHangBUS;
    private final ChiTietDonHangBUS chiTietDonHangBUS;

    private String currentChartType = "Doanh thu"; // "Doanh thu" hoặc "Sản phẩm"
    
    // Cache dữ liệu để tránh load lại nhiều lần
    private List<DonHang> cachedAllOrders = null;
    private List<ChiTietDonHang> cachedAllOrderDetails = null;
    private boolean isDataLoaded = false;

    public Panel_TongQuan() {
        donHangBUS = new DonHangBUS();
        donTraHangBUS = new DonTraHangBUS();
        donNhapHangBUS = new DonNhapHangBUS();
        chiTietDonHangBUS = new ChiTietDonHangBUS();
        initComponents();
        
        // Load dữ liệu trong background thread để không block UI
        loadDataInBackground();
        
        initChart();
        initHeader();
        chart.start();
    }
    
    /**
     * Load dữ liệu trong background thread để không block UI
     */
    private void loadDataInBackground() {
        new Thread(() -> {
            try {
                // Load dữ liệu đơn hàng và chi tiết đơn hàng một lần
                cachedAllOrders = donHangBUS.layTatCaDonHang();
                cachedAllOrderDetails = chiTietDonHangBUS.layTatCaChiTietDonHang();
                isDataLoaded = true;
                
                // Sau khi load xong, cập nhật lại chart nếu đã được khởi tạo
                javax.swing.SwingUtilities.invokeLater(() -> {
                    if (chart != null) {
                        changeDateSelect();
                    }
                });
            } catch (Exception e) {
                System.err.println("Lỗi khi load dữ liệu: " + e.getMessage());
                e.printStackTrace();
                // Fallback: load lại khi cần
                isDataLoaded = false;
            }
        }).start();
    }

    private void initHeader() {
        try {
            lblIconReturn.setIcon(new FlatSVGIcon("img/9.svg", 100, 100));  //Trả hàng
            lblIconOrder.setIcon(new FlatSVGIcon("img/1.svg", 100, 100));
            lblIconCompare.setIcon(new FlatSVGIcon("img/25.svg", 100, 100));
        } catch (Exception e) {
            System.out.println("Không thể tải icon: " + e.getMessage());
        }

        LocalDate today = LocalDate.now();

        // Lấy dữ liệu đơn hàng
        List<DonHang> danhSachDonHang = donHangBUS.layTatCaDonHang().stream()
                .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(today))
                .collect(Collectors.toList());

        int soLuongDonHang = danhSachDonHang.size();
        double tongTienDonHang = danhSachDonHang.stream()
                .mapToDouble(DonHang::getThanhTien)
                .sum();

        // Lấy dữ liệu đơn trả hàng
        List<DonTraHang> danhSachTraHang = donTraHangBUS.layTatCaDonTraHang().stream()
                .filter(dth -> dth.getNgayTraHang() != null && dth.getNgayTraHang().equals(today))
                .collect(Collectors.toList());

        int soLuongTraHang = danhSachTraHang.size();
        double tongTienTraHang = danhSachTraHang.stream()
                .mapToDouble(DonTraHang::getThanhTien)
                .sum();

        // Lấy dữ liệu đơn nhập hàng
        List<DonNhapHang> danhSachNhapHang = donNhapHangBUS.layTatCaDonNhapHang().stream()
                .filter(dnh -> dnh.getNgayNhap() != null && dnh.getNgayNhap().equals(today))
                .collect(Collectors.toList());

        int soLuongNhapHang = danhSachNhapHang.size();
        double tongTienNhapHang = danhSachNhapHang.stream()
                .mapToDouble(DonNhapHang::getThanhTien)
                .sum();

        txtQuantityOrder.setText(soLuongDonHang + " đơn hàng");
        txtSumPriceOrder.setText(DinhDangSo.dinhDangTien(tongTienDonHang));

        txtQuantityReturn.setText(soLuongTraHang + " đơn trả hàng");
        txtSumPriceReturn.setText(DinhDangSo.dinhDangTien(tongTienTraHang));

        txtQuantityPurchase.setText(soLuongNhapHang + " đơn nhập hàng");
        txtSumPricePurchase.setText(DinhDangSo.dinhDangTien(tongTienNhapHang));
    }

    private void initChart() {
        lblChart.setText("THỐNG KÊ DOANH THU 7 NGÀY GẦN NHẤT ( THEO NGÀY )");
        updateChartLegend();
        loadDataChart7Days();
    }

    private void updateChartLegend() {
        chart.clearLegends();
        if (currentChartType.equals("Doanh thu")) {
            chart.addLegend("Doanh thu", new Color(135, 189, 245));
        } else {
            chart.addLegend("Số lượng sản phẩm", new Color(245, 189, 135));
        }
    }

    private void loadDataChart7Days() {
        chart.clear();
        LocalDate today = LocalDate.now();
        
        // Sử dụng cache nếu đã load, nếu chưa thì load trực tiếp
        List<DonHang> allOrders = isDataLoaded ? cachedAllOrders : donHangBUS.layTatCaDonHang();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);

            double sumPrice = allOrders.stream()
                    .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(date))
                    .mapToDouble(DonHang::getThanhTien)
                    .sum();

            chart.addData(new ModelChart(date.getDayOfMonth() + "/" + date.getMonthValue(),
                    new double[]{sumPrice}));
        }
        chart.start();
    }

    private void loadDataChartToday() {
        chart.clear();
        LocalDate today = LocalDate.now();
        
        // Sử dụng cache nếu đã load
        List<DonHang> allOrders = isDataLoaded ? cachedAllOrders : donHangBUS.layTatCaDonHang();
        List<DonHang> todayOrders = allOrders.stream()
                .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(today))
                .collect(Collectors.toList());

        // Nhóm theo giờ (giả sử dữ liệu chỉ có ngày, hiển thị tổng cho mỗi giờ)
        for (int hour = 0; hour < 24; hour++) {
            // Vì không có giờ trong entity, hiển thị 0 hoặc phân bổ đều
            double avgPrice = todayOrders.isEmpty() ? 0
                    : todayOrders.stream().mapToDouble(DonHang::getThanhTien).sum() / 24.0;
            chart.addData(new ModelChart(hour + "h", new double[]{avgPrice}));
        }
        chart.start();
    }

    private void loadDataChartYesterday() {
        chart.clear();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        // Sử dụng cache nếu đã load
        List<DonHang> allOrders = isDataLoaded ? cachedAllOrders : donHangBUS.layTatCaDonHang();
        List<DonHang> yesterdayOrders = allOrders.stream()
                .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(yesterday))
                .collect(Collectors.toList());

        // Nhóm theo giờ (giả sử dữ liệu chỉ có ngày, hiển thị tổng cho mỗi giờ)
        for (int hour = 0; hour < 24; hour++) {
            double avgPrice = yesterdayOrders.isEmpty() ? 0
                    : yesterdayOrders.stream().mapToDouble(DonHang::getThanhTien).sum() / 24.0;
            chart.addData(new ModelChart(hour + "h", new double[]{avgPrice}));
        }
        chart.start();
    }

    private void loadDataChartThisMonth() {
        chart.clear();
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
        
        // Sử dụng cache nếu đã load
        List<DonHang> allOrders = isDataLoaded ? cachedAllOrders : donHangBUS.layTatCaDonHang();

        for (int day = 1; day <= lastDay.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(today.getYear(), today.getMonth(), day);

            double sumPrice = allOrders.stream()
                    .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(date))
                    .mapToDouble(DonHang::getThanhTien)
                    .sum();

            chart.addData(new ModelChart(day + "", new double[]{sumPrice}));
        }
        chart.start();
    }

    private void loadDataChartLastMonth() {
        chart.clear();
        LocalDate today = LocalDate.now();
        LocalDate firstDayLastMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayLastMonth = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        
        // Sử dụng cache nếu đã load
        List<DonHang> allOrders = isDataLoaded ? cachedAllOrders : donHangBUS.layTatCaDonHang();

        for (int day = 1; day <= lastDayLastMonth.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(firstDayLastMonth.getYear(), firstDayLastMonth.getMonth(), day);

            double sumPrice = allOrders.stream()
                    .filter(dh -> dh.getNgayDatHang() != null && dh.getNgayDatHang().equals(date))
                    .mapToDouble(DonHang::getThanhTien)
                    .sum();

            chart.addData(new ModelChart(day + "", new double[]{sumPrice}));
        }
        chart.start();
    }

    private void changeDateSelect() {
        LocalDateTime now = LocalDateTime.now();
        String typeLabel = currentChartType.equals("Doanh thu") ? "DOANH THU" : "SẢN PHẨM BÁN CHẠY";
        updateChartLegend(); // Cập nhật legend khi thay đổi

        switch (comboDate.getSelectedIndex()) {
            case 0 -> {
                lblChart.setText("THỐNG KÊ " + typeLabel + " 7 NGÀY GẦN NHẤT ( THEO NGÀY )");
                if (currentChartType.equals("Doanh thu")) {
                    loadDataChart7Days();
                } else {
                    loadProductChart7Days();
                }
            }
            case 1 -> {
                if (currentChartType.equals("Doanh thu")) {
                    lblChart.setText("THỐNG KÊ " + typeLabel + " HÔM NAY ( THEO GIỜ )");
                    loadDataChartToday();
                } else {
                    lblChart.setText("THỐNG KÊ " + typeLabel + " HÔM NAY");
                    loadProductChartToday();
                }
            }
            case 2 -> {
                if (currentChartType.equals("Doanh thu")) {
                    lblChart.setText("THỐNG KÊ " + typeLabel + " HÔM QUA ( THEO GIỜ )");
                    loadDataChartYesterday();
                } else {
                    lblChart.setText("THỐNG KÊ " + typeLabel + " HÔM QUA");
                    loadProductChartYesterday();
                }
            }
            case 3 -> {
                lblChart.setText("THỐNG KÊ " + typeLabel + " THÁNG " + now.getMonthValue() + " ( THEO NGÀY )");
                if (currentChartType.equals("Doanh thu")) {
                    loadDataChartThisMonth();
                } else {
                    loadProductChartThisMonth();
                }
            }
            case 4 -> {
                lblChart.setText("THỐNG KÊ " + typeLabel + " THÁNG " + now.minusMonths(1).getMonthValue() + " ( THEO NGÀY )");
                if (currentChartType.equals("Doanh thu")) {
                    loadDataChartLastMonth();
                } else {
                    loadProductChartLastMonth();
                }
            }
            default -> {
                lblChart.setText("THỐNG KÊ " + typeLabel + " 7 NGÀY GẦN NHẤT ( THEO NGÀY )");
                if (currentChartType.equals("Doanh thu")) {
                    loadDataChart7Days();
                } else {
                    loadProductChart7Days();
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: DO NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnContent = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblIconOrder = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        txtSumPriceOrder = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtQuantityOrder = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lblIconReturn = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtSumPriceReturn = new javax.swing.JLabel();
        txtQuantityReturn = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lblIconCompare = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        txtQuantityPurchase = new javax.swing.JLabel();
        txtSumPricePurchase = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblChart1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lblChart = new javax.swing.JLabel();
        comboDate = new javax.swing.JComboBox<>();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(255, 255, 255)));
        jPanel1.setMinimumSize(new java.awt.Dimension(1130, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(1130, 800));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 6));

        pnContent.setBackground(new java.awt.Color(255, 255, 255));
        pnContent.setLayout(new java.awt.BorderLayout(0, 20));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 150));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 130));
        jPanel2.setLayout(new java.awt.GridLayout(1, 3, 16, 8));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)));
        jPanel3.setPreferredSize(new java.awt.Dimension(370, 110));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel7.setLayout(new java.awt.BorderLayout());

        lblIconOrder.setBackground(new java.awt.Color(255, 255, 255));
        lblIconOrder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        lblIconOrder.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(lblIconOrder, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMinimumSize(new java.awt.Dimension(120, 110));

        txtSumPriceOrder.setFont(new java.awt.Font("Roboto Mono", 1, 36)); // NOI18N
        txtSumPriceOrder.setForeground(new java.awt.Color(51, 51, 51));
        txtSumPriceOrder.setText("50");
        txtSumPriceOrder.setPreferredSize(new java.awt.Dimension(100, 16));

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Doanh thu");

        txtQuantityOrder.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        txtQuantityOrder.setForeground(new java.awt.Color(51, 51, 51));
        txtQuantityOrder.setText("50 phiếu hóa đơn");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSumPriceOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtQuantityOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtQuantityOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSumPriceOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)));
        jPanel8.setPreferredSize(new java.awt.Dimension(370, 100));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setMinimumSize(new java.awt.Dimension(120, 110));
        jPanel9.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel9.setLayout(new java.awt.BorderLayout());

        lblIconReturn.setBackground(new java.awt.Color(255, 255, 255));
        lblIconReturn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconReturn.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        lblIconReturn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(lblIconReturn, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel9, java.awt.BorderLayout.WEST);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setMinimumSize(new java.awt.Dimension(120, 110));

        jLabel9.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Trả hàng");

        txtSumPriceReturn.setFont(new java.awt.Font("Roboto Mono", 1, 36)); // NOI18N
        txtSumPriceReturn.setForeground(new java.awt.Color(51, 51, 51));
        txtSumPriceReturn.setText("50");
        txtSumPriceReturn.setPreferredSize(new java.awt.Dimension(100, 16));

        txtQuantityReturn.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        txtQuantityReturn.setForeground(new java.awt.Color(51, 51, 51));
        txtQuantityReturn.setText("30 đơn trả hàng");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSumPriceReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtQuantityReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(txtQuantityReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSumPriceReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel8);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(232, 232, 232)));
        jPanel11.setPreferredSize(new java.awt.Dimension(370, 100));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setMinimumSize(new java.awt.Dimension(120, 110));
        jPanel12.setPreferredSize(new java.awt.Dimension(120, 110));
        jPanel12.setLayout(new java.awt.BorderLayout());

        lblIconCompare.setBackground(new java.awt.Color(255, 255, 255));
        lblIconCompare.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconCompare.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        lblIconCompare.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel12.add(lblIconCompare, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel12, java.awt.BorderLayout.WEST);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setMinimumSize(new java.awt.Dimension(120, 110));

        txtQuantityPurchase.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        txtQuantityPurchase.setForeground(new java.awt.Color(51, 51, 51));
        txtQuantityPurchase.setText("30 đơn nhập hàng");

        txtSumPricePurchase.setFont(new java.awt.Font("Roboto Mono", 1, 36)); // NOI18N
        txtSumPricePurchase.setForeground(new java.awt.Color(51, 51, 51));
        txtSumPricePurchase.setText("50");
        txtSumPricePurchase.setPreferredSize(new java.awt.Dimension(100, 16));

        jLabel10.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Nhập hàng");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSumPricePurchase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtQuantityPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(txtQuantityPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSumPricePurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel11);

        jPanel4.add(jPanel2, java.awt.BorderLayout.CENTER);

        lblChart1.setBackground(new java.awt.Color(255, 255, 255));
        lblChart1.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        lblChart1.setText("KẾT QUẢ BÁN HÀNG HÔM NAY");
        lblChart1.setPreferredSize(new java.awt.Dimension(37, 30));
        jPanel4.add(lblChart1, java.awt.BorderLayout.PAGE_START);

        pnContent.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        jPanel5.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setPreferredSize(new java.awt.Dimension(1188, 40));
        jPanel15.setLayout(new java.awt.BorderLayout());

        lblChart.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        lblChart.setText("Thống kê");
        lblChart.setMaximumSize(new java.awt.Dimension(600, 40));
        lblChart.setMinimumSize(new java.awt.Dimension(600, 40));
        lblChart.setPreferredSize(new java.awt.Dimension(600, 40));
        jPanel15.add(lblChart, java.awt.BorderLayout.WEST);

        // Panel chứa 2 combobox
        javax.swing.JPanel comboPanel = new javax.swing.JPanel();
        comboPanel.setBackground(new java.awt.Color(255, 255, 255));
        comboPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));

        comboChartType = new javax.swing.JComboBox<>();
        comboChartType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboChartType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Doanh thu", "Sản phẩm" }));
        comboChartType.setMinimumSize(new java.awt.Dimension(140, 26));
        comboChartType.setPreferredSize(new java.awt.Dimension(140, 22));
        comboChartType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboChartTypeItemStateChanged(evt);
            }
        });
        comboPanel.add(comboChartType);

        comboDate = new javax.swing.JComboBox<>();
        comboDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7 ngày qua", "Hôm nay", "Hôm qua", "Tháng này", "Tháng trước" }));
        comboDate.setMinimumSize(new java.awt.Dimension(140, 26));
        comboDate.setPreferredSize(new java.awt.Dimension(140, 22));
        comboDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboDateItemStateChanged(evt);
            }
        });
        comboPanel.add(comboDate);

        jPanel15.add(comboPanel, java.awt.BorderLayout.EAST);

        jPanel5.add(jPanel15, java.awt.BorderLayout.PAGE_START);
        
        chart = new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.Chart();
        jPanel5.add(chart, java.awt.BorderLayout.CENTER);

        pnContent.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(pnContent, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void comboDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboDateItemStateChanged
        changeDateSelect();
    }//GEN-LAST:event_comboDateItemStateChanged

    private void comboChartTypeItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            currentChartType = (String) comboChartType.getSelectedItem();
            changeDateSelect(); // Reload chart với loại mới
        }
    }

    // ==================== THỐNG KÊ SẢN PHẨM BÁN CHẠY ====================
    private void loadProductChart7Days() {
        chart.clear();
        LocalDate today = LocalDate.now();
        LocalDate dateFrom = today.minusDays(6);
        
        // Lấy top 10 sản phẩm bán chạy trong 7 ngày qua
        List<ProductSalesSummary> topProducts = chiTietDonHangBUS.layTopSanPhamBanChay(
            dateFrom, today, null, 10
        );
        
        if (topProducts.isEmpty()) {
            chart.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
        } else {
            // Hiển thị top sản phẩm bán chạy theo doanh thu (giá)
            for (ProductSalesSummary product : topProducts) {
                String fullProductName = product.getProductName(); // Tên đầy đủ cho tooltip
                // Rút gọn tên sản phẩm nếu quá dài để hiển thị trên chart
                String displayName = fullProductName;
                if (displayName.length() > 15) {
                    displayName = displayName.substring(0, 12) + "...";
                }
                chart.addData(new ModelChart(displayName, new double[]{product.getTotalRevenue()}, fullProductName));
            }
        }
        chart.start();
    }

    private void loadProductChartToday() {
        chart.clear();
        LocalDate today = LocalDate.now();
        
        // Lấy top 10 sản phẩm bán chạy hôm nay
        List<ProductSalesSummary> topProducts = chiTietDonHangBUS.layTopSanPhamBanChay(
            today, today, null, 10
        );
        
        if (topProducts.isEmpty()) {
            chart.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
        } else {
            // Hiển thị top sản phẩm bán chạy theo doanh thu (giá)
            for (ProductSalesSummary product : topProducts) {
                String fullProductName = product.getProductName(); // Tên đầy đủ cho tooltip
                String displayName = fullProductName;
                if (displayName.length() > 15) {
                    displayName = displayName.substring(0, 12) + "...";
                }
                chart.addData(new ModelChart(displayName, new double[]{product.getTotalRevenue()}, fullProductName));
            }
        }
        chart.start();
    }

    private void loadProductChartYesterday() {
        chart.clear();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        // Lấy top 10 sản phẩm bán chạy hôm qua
        List<ProductSalesSummary> topProducts = chiTietDonHangBUS.layTopSanPhamBanChay(
            yesterday, yesterday, null, 10
        );
        
        if (topProducts.isEmpty()) {
            chart.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
        } else {
            // Hiển thị top sản phẩm bán chạy theo doanh thu (giá)
            for (ProductSalesSummary product : topProducts) {
                String fullProductName = product.getProductName(); // Tên đầy đủ cho tooltip
                String displayName = fullProductName;
                if (displayName.length() > 15) {
                    displayName = displayName.substring(0, 12) + "...";
                }
                chart.addData(new ModelChart(displayName, new double[]{product.getTotalRevenue()}, fullProductName));
            }
        }
        chart.start();
    }

    private void loadProductChartThisMonth() {
        chart.clear();
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
        
        // Lấy top 15 sản phẩm bán chạy trong tháng này
        List<ProductSalesSummary> topProducts = chiTietDonHangBUS.layTopSanPhamBanChay(
            firstDay, today, null, 15
        );
        
        if (topProducts.isEmpty()) {
            chart.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
        } else {
            // Hiển thị top sản phẩm bán chạy theo doanh thu (giá)
            for (ProductSalesSummary product : topProducts) {
                String fullProductName = product.getProductName(); // Tên đầy đủ cho tooltip
                String displayName = fullProductName;
                if (displayName.length() > 15) {
                    displayName = displayName.substring(0, 12) + "...";
                }
                chart.addData(new ModelChart(displayName, new double[]{product.getTotalRevenue()}, fullProductName));
            }
        }
        chart.start();
    }

    private void loadProductChartLastMonth() {
        chart.clear();
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate firstDay = lastMonth.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = lastMonth.with(TemporalAdjusters.lastDayOfMonth());
        
        // Lấy top 15 sản phẩm bán chạy trong tháng trước
        List<ProductSalesSummary> topProducts = chiTietDonHangBUS.layTopSanPhamBanChay(
            firstDay, lastDay, null, 15
        );
        
        if (topProducts.isEmpty()) {
            chart.addData(new ModelChart("Không có dữ liệu", new double[]{0}));
        } else {
            // Hiển thị top sản phẩm bán chạy theo doanh thu (giá)
            for (ProductSalesSummary product : topProducts) {
                String fullProductName = product.getProductName(); // Tên đầy đủ cho tooltip
                String displayName = fullProductName;
                if (displayName.length() > 15) {
                    displayName = displayName.substring(0, 12) + "...";
                }
                chart.addData(new ModelChart(displayName, new double[]{product.getTotalRevenue()}, fullProductName));
            }
        }
        chart.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.Chart chart;
    private javax.swing.JComboBox<String> comboChartType;
    private javax.swing.JComboBox<String> comboDate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lblChart;
    private javax.swing.JLabel lblChart1;
    private javax.swing.JLabel lblIconCompare;
    private javax.swing.JLabel lblIconOrder;
    private javax.swing.JLabel lblIconReturn;
    private javax.swing.JPanel pnContent;
    private javax.swing.JLabel txtQuantityOrder;
    private javax.swing.JLabel txtQuantityPurchase;
    private javax.swing.JLabel txtQuantityReturn;
    private javax.swing.JLabel txtSumPriceOrder;
    private javax.swing.JLabel txtSumPricePurchase;
    private javax.swing.JLabel txtSumPriceReturn;
    // End of variables declaration//GEN-END:variables
}

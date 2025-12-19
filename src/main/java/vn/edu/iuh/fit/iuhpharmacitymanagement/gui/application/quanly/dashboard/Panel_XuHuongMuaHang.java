/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.dashboard;

import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.Chart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.barchart.ModelChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO.CategorySalesSummary;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonHangDAO.ProductSalesSummary;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.piechart.PieChart;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.piechart.PieChartItem;

import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

/**
 *
 * @author PhamTra
 */
public class Panel_XuHuongMuaHang extends javax.swing.JPanel {

    private static final int TOP_PRODUCT_LIMIT = 5;
    private static final Color[] PIE_COLORS = new Color[]{
        new Color(90, 159, 255),
        new Color(255, 112, 147),
        new Color(0, 188, 170),
        new Color(255, 202, 40),
        new Color(156, 39, 176),
        new Color(121, 85, 72)
    };
    private static final DateTimeFormatter DATE_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ChiTietDonHangBUS chiTietDonHangBUS;
    private Chart chart;
    private PieChart pieChartRevenue;
    private PieChart pieChartQuantity;
    private PieChart pieChartTrend;
    private final Map<String, LoaiSanPham> productTypeOptions = new LinkedHashMap<>();

    public Panel_XuHuongMuaHang() {
        chiTietDonHangBUS = new ChiTietDonHangBUS();
        initChart();
        initPieCharts();
        initComponents();
        initProductTypeFilter();
        initDefaultDates();
        LocalDate today = LocalDate.now();
        reloadDashboard(today.withDayOfMonth(1), today);
        applyStyles();
    }

    private void applyStyles() {
        ButtonStyles.apply(btnSearch, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnSearch, FontStyles.Type.BUTTON_MEDIUM);
    }

    private void initChart() {
        chart = new Chart();
        chart.addLegend("Doanh thu", new Color(90, 159, 255));
    }

    private void initPieCharts() {
        pieChartRevenue = createPieChart();
        pieChartQuantity = createPieChart();
        pieChartTrend = createPieChart();
    }

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setEmptyMessage("Không có dữ liệu trong khoảng thời gian này");
        return pieChart;
    }

    private void initDefaultDates() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        jDateFrom.setDate(java.util.Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        jDateTo.setDate(java.util.Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private void initProductTypeFilter() {
        productTypeOptions.clear();
        productTypeOptions.put("Tất cả", null);
        for (LoaiSanPham type : LoaiSanPham.values()) {
            productTypeOptions.put(toDisplayName(type), type);
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
                productTypeOptions.keySet().toArray(new String[0])
        );
        comboProductType.setModel(model);
        comboProductType.setSelectedIndex(0);
    }

    private String toDisplayName(LoaiSanPham type) {
        switch (type) {
            case THUOC:
                return "Thuốc";
            case THUOC_KE_DON:
                return "Thuốc kê đơn";
            case VAT_TU_Y_TE:
                return "Vật tư y tế";
            case THUC_PHAM_CHUC_NANG:
                return "Thực phẩm chức năng";
            case CHAM_SOC_TRE_EM:
                return "Chăm sóc trẻ em";
            case THIET_BI_Y_TE:
                return "Thiết bị y tế";
            default:
                return type.name();
        }
    }

    private LoaiSanPham getSelectedProductType() {
        Object selected = comboProductType.getSelectedItem();
        if (selected == null) {
            return null;
        }
        return productTypeOptions.getOrDefault(selected.toString(), null);
    }

    private String formatProductLabel(String name, String fallback) {
        if (name != null && !name.isBlank()) {
            return name.trim();
        }
        if (fallback != null && !fallback.isBlank()) {
            return fallback.trim();
        }
        return "Không tên";
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
        jPanel4 = new javax.swing.JPanel();
        chartSummaryPanel = new javax.swing.JPanel();
        panelRevenueChart = new javax.swing.JPanel();
        panelQuantityChart = new javax.swing.JPanel();
        panelTrendChart = new javax.swing.JPanel();
        lblXuHuong = new javax.swing.JLabel();
        lbdt = new javax.swing.JLabel();
        lbt5 = new javax.swing.JLabel();
        lbtl = new javax.swing.JLabel();
        pnContent = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        lblChartProduct = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        comboProductType = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(255, 255, 255)));
        jPanel1.setMinimumSize(new java.awt.Dimension(1130, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(1130, 800));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 6));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(1118, 430));
        jPanel4.setLayout(new java.awt.BorderLayout(0, 12));

        lblXuHuong.setBackground(new java.awt.Color(255, 255, 255));
        lblXuHuong.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        lblXuHuong.setText("XU HƯỚNG MUA HÀNG");
        lblXuHuong.setPreferredSize(new java.awt.Dimension(37, 30));
        jPanel4.add(lblXuHuong, java.awt.BorderLayout.PAGE_START);

        chartSummaryPanel.setOpaque(false);
        chartSummaryPanel.setLayout(new java.awt.GridLayout(1, 3, 40, 0));

        panelRevenueChart.setOpaque(false);
        panelRevenueChart.setLayout(new java.awt.BorderLayout(0, 8));
        panelRevenueChart.add(pieChartRevenue, java.awt.BorderLayout.CENTER);

        lbdt.setBackground(new java.awt.Color(255, 255, 255));
        lbdt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbdt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbdt.setText("Doanh Thu Theo Loại");
        panelRevenueChart.add(lbdt, java.awt.BorderLayout.PAGE_END);

        chartSummaryPanel.add(panelRevenueChart);

        panelQuantityChart.setOpaque(false);
        panelQuantityChart.setLayout(new java.awt.BorderLayout(0, 8));
        panelQuantityChart.add(pieChartQuantity, java.awt.BorderLayout.CENTER);

        lbtl.setBackground(new java.awt.Color(255, 255, 255));
        lbtl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbtl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbtl.setText("Số Lượng Theo Loại");
        panelQuantityChart.add(lbtl, java.awt.BorderLayout.PAGE_END);

        chartSummaryPanel.add(panelQuantityChart);

        panelTrendChart.setOpaque(false);
        panelTrendChart.setLayout(new java.awt.BorderLayout(0, 8));
        panelTrendChart.add(pieChartTrend, java.awt.BorderLayout.CENTER);

        lbt5.setBackground(new java.awt.Color(255, 255, 255));
        lbt5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbt5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbt5.setText("Xu Hướng Mua");
        panelTrendChart.add(lbt5, java.awt.BorderLayout.PAGE_END);

        chartSummaryPanel.add(panelTrendChart);

        jPanel4.add(chartSummaryPanel, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        pnContent.setBackground(new java.awt.Color(255, 255, 255));
        pnContent.setLayout(new java.awt.BorderLayout(0, 20));

        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setPreferredSize(new java.awt.Dimension(1188, 40));
        jPanel16.setLayout(new java.awt.BorderLayout());

        lblChartProduct.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        lblChartProduct.setText("TOP 5 SẢN PHẨM BÁN CHẠY");
        lblChartProduct.setMaximumSize(new java.awt.Dimension(600, 40));
        lblChartProduct.setMinimumSize(new java.awt.Dimension(600, 40));
        lblChartProduct.setPreferredSize(new java.awt.Dimension(600, 40));
        jPanel16.add(lblChartProduct, java.awt.BorderLayout.WEST);

        jPanel14.add(jPanel16, java.awt.BorderLayout.PAGE_START);
        
        // Thêm chart thực sự
        jPanel14.add(chart, java.awt.BorderLayout.CENTER);

        pnContent.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel1.add(pnContent, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        jPanel5.setPreferredSize(new java.awt.Dimension(590, 100));

        btnSearch.setBackground(new java.awt.Color(115, 165, 71));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setText("Ngày bắt đầu");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel6.setText("Ngày kết thúc");

        comboProductType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        comboProductType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel1.setText("Loại sản phẩm");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboProductType, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(42, 42, 42)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(567, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1))
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(comboProductType, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addComponent(jDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        add(jPanel5, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        if (jDateFrom.getDate() == null || jDateTo.getDate() == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, "Vui lòng chọn ngày bắt đầu và ngày kết thúc");
            return;
        }

        LocalDate dateFrom = jDateFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateTo = jDateTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (dateFrom.isAfter(dateTo)) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, "Ngày bắt đầu phải trước ngày kết thúc");
            return;
        }

        reloadDashboard(dateFrom, dateTo);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Đã tải dữ liệu xu hướng mua hàng");
    }//GEN-LAST:event_btnSearchActionPerformed

    private void reloadDashboard(LocalDate dateFrom, LocalDate dateTo) {
        loadCategoryCharts(dateFrom, dateTo);
        loadTopProducts(dateFrom, dateTo);
        updateSummaryTitle(dateFrom, dateTo);
    }

    private void loadCategoryCharts(LocalDate dateFrom, LocalDate dateTo) {
        List<CategorySalesSummary> summaries = chiTietDonHangBUS.layThongKeBanHangTheoLoai(dateFrom, dateTo);
        applyPieData(pieChartRevenue, summaries, CategorySalesSummary::getTotalRevenue);
        applyPieData(pieChartQuantity, summaries, summary -> summary.getTotalQuantity());
        applyPieData(pieChartTrend, summaries, CategorySalesSummary::getOrderCount);
    }

    private void applyPieData(PieChart chart, List<CategorySalesSummary> summaries, ToDoubleFunction<CategorySalesSummary> valueExtractor) {
        List<PieChartItem> items = new ArrayList<>();
        int index = 0;

        for (CategorySalesSummary summary : summaries) {
            double value = valueExtractor.applyAsDouble(summary);
            if (value <= 0) {
                continue;
            }
            String label = summary.getCategory() == null ? "Khác" : toDisplayName(summary.getCategory());
            items.add(new PieChartItem(label, value, PIE_COLORS[index % PIE_COLORS.length]));
            index++;
        }

        if (items.isEmpty()) {
            chart.clear();
        } else {
            chart.setData(items);
        }
    }

    private void updateSummaryTitle(LocalDate dateFrom, LocalDate dateTo) {
        String title = String.format(
                "XU HƯỚNG MUA HÀNG TỪ %s ĐẾN %s",
                DATE_DISPLAY_FORMATTER.format(dateFrom),
                DATE_DISPLAY_FORMATTER.format(dateTo)
        );
        lblXuHuong.setText(title);
    }

    private void loadTopProducts(LocalDate dateFrom, LocalDate dateTo) {
        chart.clear();

        LoaiSanPham selectedType = getSelectedProductType();
        List<ProductSalesSummary> topProducts = chiTietDonHangBUS.layTopSanPhamBanChay(
                dateFrom,
                dateTo,
                selectedType,
                TOP_PRODUCT_LIMIT
        );

        for (ProductSalesSummary summary : topProducts) {
            String label = formatProductLabel(summary.getProductName(), summary.getProductId());
            chart.addData(new ModelChart(label, new double[]{summary.getTotalRevenue()}));
        }

        if (topProducts.isEmpty()) {
            lblChartProduct.setText(String.format(
                    "KHÔNG CÓ SẢN PHẨM NÀO TỪ NGÀY %s ĐẾN NGÀY %s",
                    DATE_DISPLAY_FORMATTER.format(dateFrom),
                    DATE_DISPLAY_FORMATTER.format(dateTo)
            ));
            return;
        }

        chart.start();
        lblChartProduct.setText(String.format(
                "TOP %d SẢN PHẨM BÁN CHẠY TỪ NGÀY %s ĐẾN NGÀY %s",
                Math.min(topProducts.size(), TOP_PRODUCT_LIMIT),
                DATE_DISPLAY_FORMATTER.format(dateFrom),
                DATE_DISPLAY_FORMATTER.format(dateTo)
        ));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel chartSummaryPanel;
    private javax.swing.JComboBox<String> comboProductType;
    private com.toedter.calendar.JDateChooser jDateFrom;
    private com.toedter.calendar.JDateChooser jDateTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lbdt;
    private javax.swing.JLabel lblChartProduct;
    private javax.swing.JLabel lblXuHuong;
    private javax.swing.JLabel lbt5;
    private javax.swing.JLabel lbtl;
    private javax.swing.JPanel panelQuantityChart;
    private javax.swing.JPanel panelRevenueChart;
    private javax.swing.JPanel panelTrendChart;
    private javax.swing.JPanel pnContent;
    // End of variables declaration//GEN-END:variables
}

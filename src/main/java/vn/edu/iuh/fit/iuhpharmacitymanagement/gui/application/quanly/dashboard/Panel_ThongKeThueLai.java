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
import com.toedter.calendar.JDateChooser;
import raven.toast.Notifications;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import javax.swing.*;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

/**
 * Panel thống kê thuế và lãi cho dashboard quản lý
 *
 * @author PhamTra
 */
public class Panel_ThongKeThueLai extends javax.swing.JPanel {

    private final ThongKeThueLaiService thongKeService;
    private Chart chartThue;
    private Chart chartLai;
    private String currentPeriod = "7 ngày qua"; // "7 ngày qua", "Tháng này", "12 tháng"
    private String currentCardPeriod = "Tháng này"; // Khoảng thời gian cho cards: "Hôm nay", "7 ngày qua", "Tháng này", "Năm này"
    private String currentDateMode = "Theo ngày"; // "Theo ngày", "Theo tháng", "Theo năm"

    // Dashboard cards
    private DashboardCard cardThueThu;
    private DashboardCard cardThueTra;
    private DashboardCard cardLoiNhuan;
    private DashboardCard cardTySuat;

    // Chart panels (comboPeriod được khai báo trong NetBeans generated code)
    private JComboBox<String> comboPeriod;
    private JComboBox<String> comboCardPeriod;

    // Date pickers
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JButton btnSearch;
    private JPanel datePickerPanel;
    private JLabel lblFrom;
    private JLabel lblTo;

    public Panel_ThongKeThueLai() {
        thongKeService = new ThongKeThueLaiService();
        // Khởi tạo charts trước
        chartThue = new Chart();
        chartLai = new Chart();
        initComponents();
        setupDashboard();
        // Load dữ liệu trong background để không block UI
        loadDataInBackground();
        applyStyles();
    }

    private void applyStyles() {
        ButtonStyles.apply(btnSearch, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnSearch, FontStyles.Type.BUTTON_MEDIUM);
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

        // ========== HEADER: Tiêu đề + Combobox chọn khoảng thời gian ==========
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(15));

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
     * Tạo header panel với tab và date picker (gọn gàng)
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Tab panel
        ButtonGroup tabGroup = new ButtonGroup();
        JToggleButton btnTheoNgay = new JToggleButton("Theo ngày");
        JToggleButton btnTheoThang = new JToggleButton("Theo tháng");
        JToggleButton btnTheoNam = new JToggleButton("Theo năm");

        btnTheoNgay.setSelected(true);
        btnTheoNgay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTheoThang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTheoNam.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnTheoNgay.setPreferredSize(new Dimension(90, 28));
        btnTheoThang.setPreferredSize(new Dimension(90, 28));
        btnTheoNam.setPreferredSize(new Dimension(90, 28));

        tabGroup.add(btnTheoNgay);
        tabGroup.add(btnTheoThang);
        tabGroup.add(btnTheoNam);

        btnTheoNgay.addActionListener(e -> {
            currentDateMode = "Theo ngày";
            updateDatePickerLabels();
        });
        btnTheoThang.addActionListener(e -> {
            currentDateMode = "Theo tháng";
            updateDatePickerLabels();
        });
        btnTheoNam.addActionListener(e -> {
            currentDateMode = "Theo năm";
            updateDatePickerLabels();
        });

        panel.add(btnTheoNgay);
        panel.add(btnTheoThang);
        panel.add(btnTheoNam);

        // Date picker panel
        datePickerPanel = createDatePickerPanel();
        datePickerPanel.setVisible(true);
        panel.add(datePickerPanel);

        // Cập nhật labels ban đầu
        updateDatePickerLabels();

        return panel;
    }

    /**
     * Tạo panel chứa date picker (gọn gàng)
     */
    private JPanel createDatePickerPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        // Ngày bắt đầu
        lblFrom = new JLabel("Ngày bắt đầu:");
        lblFrom.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateFrom.setPreferredSize(new Dimension(140, 35));
        dateFrom.setDate(new Date()); // Mặc định là hôm nay

        // Arrow
        JLabel lblArrow = new JLabel("-->");
        lblArrow.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Ngày kết thúc
        lblTo = new JLabel("Ngày kết thúc:");
        lblTo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateTo.setPreferredSize(new Dimension(140, 35));
        dateTo.setDate(new Date()); // Mặc định là hôm nay

        // Nút tìm kiếm
        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(115, 165, 71));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSearch.setPreferredSize(new Dimension(120, 35));
        btnSearch.addActionListener(e -> searchByDateRange());

        panel.add(lblFrom);
        panel.add(dateFrom);
        panel.add(lblArrow);
        panel.add(lblTo);
        panel.add(dateTo);
        panel.add(btnSearch);

        return panel;
    }

    /**
     * Cập nhật label và format của date picker dựa trên mode
     */
    private void updateDatePickerLabels() {
        datePickerPanel.setVisible(true);

        switch (currentDateMode) {
            case "Theo ngày":
                lblFrom.setText("Ngày bắt đầu:");
                lblTo.setText("Ngày kết thúc:");
                dateFrom.setDateFormatString("dd/MM/yyyy");
                dateTo.setDateFormatString("dd/MM/yyyy");
                break;
            case "Theo tháng":
                lblFrom.setText("Tháng bắt đầu:");
                lblTo.setText("Tháng kết thúc:");
                dateFrom.setDateFormatString("MM/yyyy");
                dateTo.setDateFormatString("MM/yyyy");
                break;
            case "Theo năm":
                lblFrom.setText("Năm bắt đầu:");
                lblTo.setText("Năm kết thúc:");
                dateFrom.setDateFormatString("yyyy");
                dateTo.setDateFormatString("yyyy");
                break;
        }

        // Repaint để cập nhật UI
        datePickerPanel.revalidate();
        datePickerPanel.repaint();
    }

    /**
     * Tìm kiếm theo khoảng thời gian đã chọn
     */
    private void searchByDateRange() {
        if (dateFrom.getDate() == null || dateTo.getDate() == null) {
            String message = "Vui lòng chọn đầy đủ ";
            switch (currentDateMode) {
                case "Theo ngày":
                    message += "ngày bắt đầu và ngày kết thúc";
                    break;
                case "Theo tháng":
                    message += "tháng bắt đầu và tháng kết thúc";
                    break;
                case "Theo năm":
                    message += "năm bắt đầu và năm kết thúc";
                    break;
            }
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER, message);
            return;
        }

        LocalDate from, to;
        String periodDesc;

        switch (currentDateMode) {
            case "Theo ngày":
                from = dateFrom.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                to = dateTo.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                periodDesc = String.format("%s - %s",
                        from.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        to.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                break;
            case "Theo tháng":
                // Lấy ngày đầu tháng và cuối tháng
                LocalDate fromDate = dateFrom.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate toDate = dateTo.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                from = fromDate.withDayOfMonth(1);
                to = toDate.withDayOfMonth(toDate.lengthOfMonth());
                periodDesc = String.format("%s - %s",
                        fromDate.format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy")),
                        toDate.format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy")));
                break;
            case "Theo năm":
                // Lấy ngày đầu năm và cuối năm
                LocalDate fromYear = dateFrom.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate toYear = dateTo.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                from = fromYear.withDayOfYear(1);
                to = toYear.withDayOfYear(toYear.lengthOfYear());
                periodDesc = String.format("%s - %s",
                        String.valueOf(fromYear.getYear()),
                        String.valueOf(toYear.getYear()));
                break;
            default:
                from = dateFrom.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                to = dateTo.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                periodDesc = String.format("%s - %s",
                        from.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        to.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (from.isAfter(to)) {
            String message = "Thời gian bắt đầu phải trước thời gian kết thúc";
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER, message);
            return;
        }

        // Load data với khoảng thời gian đã chọn
        loadDataByDateRange(from, to, periodDesc);
    }


    /**
     * Load data theo khoảng thời gian tùy chỉnh
     */
    private void loadDataByDateRange(LocalDate from, LocalDate to, String periodDesc) {
        // Hiển thị loading
        cardThueThu.updateValue("Đang tải...");
        cardThueTra.updateValue("Đang tải...");
        cardLoiNhuan.updateValue("Đang tải...");
        cardTySuat.updateValue("Đang tải...");

        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Tính toán số liệu
                double tongThueThu = thongKeService.tinhTongThueThu(from, to);
                double tongThueTra = thongKeService.tinhTongThueTra(from, to);
                double tongLoiNhuan = thongKeService.tinhTongLoiNhuan(from, to);
                double tySuatLoiNhuan = thongKeService.tinhTySuatLoiNhuan(from, to);

                // Cập nhật UI trong EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    cardThueThu.updateValue(DinhDangSo.dinhDangTien(tongThueThu));
                    cardThueThu.updateDescription(periodDesc);
                    cardThueTra.updateValue(DinhDangSo.dinhDangTien(tongThueTra));
                    cardThueTra.updateDescription(periodDesc);
                    cardLoiNhuan.updateValue(DinhDangSo.dinhDangTien(tongLoiNhuan));
                    cardLoiNhuan.updateDescription(periodDesc);
                    cardTySuat.updateValue(String.format("%.2f%%", tySuatLoiNhuan));
                    cardTySuat.updateDescription(periodDesc);
                });

                return null;
            }
            
            @Override
            protected void done() {
                // Nếu user đang ở chế độ "Theo năm" thì load chart theo năm sau khi cập nhật các cards
                if ("Theo năm".equals(currentDateMode)) {
                    loadChartByYear(from, to);
                } else {
                    // Ngược lại, load các chart mặc định
                    loadDataCharts();
                }
            }
        };
        worker.execute();
    }

    /**
     * Load chart data aggregated by year for a custom date range.
     */
    private void loadChartByYear(LocalDate from, LocalDate to) {
        chartThue.clear();
        chartLai.clear();

        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                java.util.Map<String, java.util.Map<String, Double>> thueData = new java.util.LinkedHashMap<>();
                java.util.Map<String, java.util.Map<String, Double>> laiData = new java.util.LinkedHashMap<>();

                int startYear = from.getYear();
                int endYear = to.getYear();
                for (int year = startYear; year <= endYear; year++) {
                    LocalDate yFrom = LocalDate.of(year, 1, 1);
                    LocalDate yTo = LocalDate.of(year, 12, 31);
                    // clamp to requested range
                    if (yFrom.isBefore(from)) yFrom = from;
                    if (yTo.isAfter(to)) yTo = to;

                    String label = String.valueOf(year);
                    java.util.Map<String, Double> yearThue = new java.util.HashMap<>();
                    yearThue.put("thueThu", thongKeService.tinhTongThueThu(yFrom, yTo));
                    yearThue.put("thueTra", thongKeService.tinhTongThueTra(yFrom, yTo));
                    yearThue.put("thueRong", thongKeService.tinhThueRong(yFrom, yTo));
                    thueData.put(label, yearThue);

                    java.util.Map<String, Double> yearLai = new java.util.HashMap<>();
                    yearLai.put("doanhThu", thongKeService.tinhTongDoanhThu(yFrom, yTo));
                    yearLai.put("giaVon", thongKeService.tinhTongGiaVon(yFrom, yTo));
                    yearLai.put("loiNhuan", thongKeService.tinhTongLoiNhuan(yFrom, yTo));
                    laiData.put(label, yearLai);
                }

                javax.swing.SwingUtilities.invokeLater(() -> {
                    chartThue.clearLegends();
                    chartThue.addLegend("Thuế thu", new Color(76, 175, 80));
                    chartThue.addLegend("Thuế trả", new Color(244, 67, 54));
                    chartThue.addLegend("Thuế ròng", new Color(33, 150, 243));

                    chartLai.clearLegends();
                    chartLai.addLegend("Doanh thu", new Color(76, 175, 80));
                    chartLai.addLegend("Giá vốn", new Color(244, 67, 54));
                    chartLai.addLegend("Lợi nhuận", new Color(33, 150, 243));

                    for (java.util.Map.Entry<String, java.util.Map<String, Double>> entry : thueData.entrySet()) {
                        String label = entry.getKey();
                        java.util.Map<String, Double> values = entry.getValue();
                        chartThue.addData(new ModelChart(label,
                                new double[]{
                                    values.getOrDefault("thueThu", 0.0),
                                    values.getOrDefault("thueTra", 0.0),
                                    values.getOrDefault("thueRong", 0.0)
                                }));
                    }

                    for (java.util.Map.Entry<String, java.util.Map<String, Double>> entry : laiData.entrySet()) {
                        String label = entry.getKey();
                        java.util.Map<String, Double> values = entry.getValue();
                        chartLai.addData(new ModelChart(label,
                                new double[]{
                                    values.getOrDefault("doanhThu", 0.0),
                                    values.getOrDefault("giaVon", 0.0),
                                    values.getOrDefault("loiNhuan", 0.0)
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
                currentCardPeriod,
                new Color(76, 175, 80),
                Color.WHITE,
                null // Không dùng icon, dùng emoji trong description
        );

        // Card 2: Thuế GTGT đã trả
        cardThueTra = new DashboardCard(
                "THUẾ GTGT ĐÃ TRẢ",
                "0 đ",
                currentCardPeriod,
                new Color(244, 67, 54),
                Color.WHITE,
                null
        );

        // Card 3: Tổng lợi nhuận
        cardLoiNhuan = new DashboardCard(
                "TỔNG LỢI NHUẬN",
                "0 đ",
                currentCardPeriod,
                new Color(33, 150, 243),
                Color.WHITE,
                null
        );

        // Card 4: Tỷ suất lợi nhuận
        cardTySuat = new DashboardCard(
                "TỶ SUẤT LỢI NHUẬN",
                "0%",
                currentCardPeriod,
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
            comboPeriod = new JComboBox<>(new String[]{"7 ngày qua", "Tháng này", "12 tháng"});
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
                LocalDate from, to;

                // Xác định khoảng thời gian dựa trên lựa chọn
                switch (currentCardPeriod) {
                    case "Hôm nay":
                        from = today;
                        to = today;
                        break;
                    case "7 ngày qua":
                        from = today.minusDays(6);
                        to = today;
                        break;
                    case "Tháng này":
                        from = today.withDayOfMonth(1);
                        to = today.withDayOfMonth(today.lengthOfMonth());
                        break;
                    case "Năm này":
                        from = today.withDayOfYear(1);
                        to = today.withDayOfYear(today.lengthOfYear());
                        break;
                    default:
                        from = today.withDayOfMonth(1);
                        to = today.withDayOfMonth(today.lengthOfMonth());
                        break;
                }

                // Tính toán số liệu
                double tongThueThu = thongKeService.tinhTongThueThu(from, to);
                double tongThueTra = thongKeService.tinhTongThueTra(from, to);
                double tongLoiNhuan = thongKeService.tinhTongLoiNhuan(from, to);
                double tySuatLoiNhuan = thongKeService.tinhTySuatLoiNhuan(from, to);

                // Cập nhật UI trong EDT
                javax.swing.SwingUtilities.invokeLater(() -> {
                    cardThueThu.updateValue(DinhDangSo.dinhDangTien(tongThueThu));
                    cardThueThu.updateDescription(currentCardPeriod);
                    cardThueTra.updateValue(DinhDangSo.dinhDangTien(tongThueTra));
                    cardThueTra.updateDescription(currentCardPeriod);
                    cardLoiNhuan.updateValue(DinhDangSo.dinhDangTien(tongLoiNhuan));
                    cardLoiNhuan.updateDescription(currentCardPeriod);
                    cardTySuat.updateValue(String.format("%.2f%%", tySuatLoiNhuan));
                    cardTySuat.updateDescription(currentCardPeriod);
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

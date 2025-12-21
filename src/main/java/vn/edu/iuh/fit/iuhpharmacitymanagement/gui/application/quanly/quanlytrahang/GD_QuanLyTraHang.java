/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlytrahang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonTraHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonTraHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
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
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyTraHang extends javax.swing.JPanel {

    private final DonTraHangBUS donTraHangBUS;
    private final ChiTietDonTraHangBUS chiTietDonTraHangBUS;
    private final LoHangBUS loHangBUS;
    private final LoHangDAO loHangDAO;
    private TableDesign tableDesign;

    public GD_QuanLyTraHang() {
        donTraHangBUS = new DonTraHangBUS();
        chiTietDonTraHangBUS = new ChiTietDonTraHangBUS();
        loHangBUS = new LoHangBUS();
        loHangDAO = new LoHangDAO();
        initComponents();
        setUIManager();
        fillTable();
    }

    private void setUIManager() {
        txtOrder.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập hoặc quét mã phiếu trả hàng");
        UIManager.put("Button.arc", 10);
        jDateFrom.setDate(Date.valueOf(LocalDate.now()));
        jDateTo.setDate(Date.valueOf(LocalDate.now()));

        FontStyles.apply(btnView, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnSearch, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(txtExport, FontStyles.Type.BUTTON_MEDIUM);

        btnView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        txtExport.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ButtonStyles.apply(btnView, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnSearch, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(txtExport, ButtonStyles.Type.SUCCESS);

        // Thiết lập barcode scanner cho textfield tìm kiếm
        setupBarcodeScanner();
    }

    /**
     * Thiết lập barcode scanner listener cho textfield tìm kiếm phiếu trả hàng
     * Hỗ trợ cả quét barcode (tự động xử lý) và nhập thủ công (xử lý khi nhấn
     * Enter)
     */
    private void setupBarcodeScanner() {
        // Biến để theo dõi trạng thái xử lý (tránh xử lý nhiều lần)
        final java.util.concurrent.atomic.AtomicBoolean isProcessing = new java.util.concurrent.atomic.AtomicBoolean(false);
        final java.util.concurrent.atomic.AtomicBoolean isClearing = new java.util.concurrent.atomic.AtomicBoolean(false);
        final javax.swing.Timer[] barcodeTimer = new javax.swing.Timer[1]; // Mảng để có thể thay đổi trong lambda

        // Theo dõi thời gian giữa các lần gõ để phân biệt quét vs nhập thủ công
        final long[] lastKeyTime = new long[1];
        lastKeyTime[0] = System.currentTimeMillis();
        final int[] keyCount = new int[1];
        keyCount[0] = 0;
        final long[] firstKeyTime = new long[1];
        firstKeyTime[0] = 0;

        // KeyListener để theo dõi tốc độ gõ phím
        txtOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                long currentTime = System.currentTimeMillis();
                long timeSinceLastKey = currentTime - lastKeyTime[0];

                // Ghi nhận thời gian ký tự đầu tiên
                if (firstKeyTime[0] == 0) {
                    firstKeyTime[0] = currentTime;
                }

                // Nếu khoảng cách giữa các lần gõ < 50ms → có thể là quét barcode
                if (timeSinceLastKey < 50) {
                    keyCount[0]++;
                } else if (timeSinceLastKey > 200) {
                    // Nếu khoảng cách > 200ms → rõ ràng là nhập thủ công, reset counter
                    keyCount[0] = 1;
                    firstKeyTime[0] = currentTime;
                } else {
                    // Khoảng cách 50-200ms → có thể là gõ nhanh, tăng counter
                    keyCount[0]++;
                }

                lastKeyTime[0] = currentTime;
            }
        });

        // DocumentListener để bắt mọi thay đổi text
        txtOrder.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void handleTextChange() {
                // Bỏ qua nếu đang clear textfield
                if (isClearing.get()) {
                    return;
                }

                // Hủy timer cũ nếu có
                if (barcodeTimer[0] != null && barcodeTimer[0].isRunning()) {
                    barcodeTimer[0].stop();
                }

                // Tạo timer mới: đợi 200ms không có thay đổi → kiểm tra xem có phải quét không
                barcodeTimer[0] = new javax.swing.Timer(200, evt -> {
                    String scannedText = txtOrder.getText().trim();

                    // Loại bỏ ký tự đặc biệt từ barcode scanner (\r, \n, \t)
                    scannedText = scannedText.replaceAll("[\\r\\n\\t]", "");

                    // Cập nhật lại textfield với giá trị đã làm sạch (nếu cần)
                    if (!scannedText.equals(txtOrder.getText().trim()) && !isClearing.get()) {
                        isClearing.set(true);
                        txtOrder.setText(scannedText);
                        isClearing.set(false);
                    }

                    // Phân biệt quét barcode vs nhập thủ công:
                    // - Quét barcode: nhiều ký tự (>= 5) được nhập rất nhanh (keyCount >= 5 và thời gian tổng < 500ms)
                    // - Nhập thủ công: ít ký tự hoặc gõ chậm → không tự động xử lý, chờ Enter
                    long totalTime = firstKeyTime[0] > 0 ? (System.currentTimeMillis() - firstKeyTime[0]) : 0;
                    boolean isBarcodeScan = scannedText.length() >= 5 && keyCount[0] >= 5 && totalTime < 500;

                    if (!scannedText.isEmpty() && !isProcessing.get() && !isClearing.get() && isBarcodeScan) {
                        isProcessing.set(true);
                        // Tự động tìm kiếm phiếu trả hàng khi quét barcode
                        timKiemVaHienThiPhieuTra(scannedText);
                        isProcessing.set(false);

                        // Clear textfield sau khi xử lý để sẵn sàng quét tiếp
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            javax.swing.Timer clearTimer = new javax.swing.Timer(500, e -> {
                                isClearing.set(true);
                                txtOrder.setText("");
                                isClearing.set(false);
                                keyCount[0] = 0; // Reset counter
                                firstKeyTime[0] = 0; // Reset first key time
                            });
                            clearTimer.setRepeats(false);
                            clearTimer.start();
                        });
                    }

                    // Reset counter sau một khoảng thời gian (nếu không phải quét)
                    if (!isBarcodeScan) {
                        keyCount[0] = 0;
                        firstKeyTime[0] = 0;
                    }
                });
                barcodeTimer[0].setRepeats(false);
                barcodeTimer[0].start();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                // Khi xóa text, reset counter (người dùng đang chỉnh sửa)
                keyCount[0] = 0;
                firstKeyTime[0] = 0;
                lastKeyTime[0] = System.currentTimeMillis();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                handleTextChange();
            }
        });
    }

    /**
     * Tìm kiếm và hiển thị phiếu trả hàng theo mã
     */
    private void timKiemVaHienThiPhieuTra(String maPhieuTra) {
        if (maPhieuTra == null || maPhieuTra.trim().isEmpty()) {
            return;
        }

        // Lấy ngày từ form
        java.util.Date date1 = jDateFrom.getDate();
        java.util.Date date2 = jDateTo.getDate();

        if (date1 == null || date2 == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn khoảng thời gian!");
            return;
        }

        LocalDate dateFrom = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateTo = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Tìm kiếm theo mã phiếu trả
        List<DonTraHang> result = searchDonTraHang(dateFrom, dateTo, "", maPhieuTra);
        fillContent(result);

        if (result.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy phiếu trả hàng với mã: " + maPhieuTra);
        } else {
            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    "Tìm thấy phiếu trả hàng: " + maPhieuTra);
        }
    }

    private void fillTable() {
        String[] headers = {"Mã phiếu trả", "Mã hóa đơn", "Ngày trả", "Nhân viên", "Tổng tiền", "Trạng thái"};
        List<Integer> tableWidths = Arrays.asList(150, 150, 120, 200, 150, 120);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        // (No action column)

        // Lấy dữ liệu hoặc tạo dữ liệu giả lập
        List<DonTraHang> danhSach = layDuLieuDonTraHang();
        fillContent(danhSach);
    }

    private void fillContent(List<DonTraHang> danhSach) {
        tableDesign.getModelTable().setRowCount(0);
        for (DonTraHang dth : danhSach) {
            String maDonHang = dth.getDonHang() != null ? dth.getDonHang().getMaDonHang() : "N/A";
            String tenNV = dth.getNhanVien() != null ? dth.getNhanVien().getMaNhanVien() : "N/A";

            // Lấy trạng thái từ đơn trả hàng
            String trangThai = dth.getTrangThaiXuLy() != null ? dth.getTrangThaiXuLy() : "Chưa xử lý";

            tableDesign.getModelTable().addRow(new Object[]{
                dth.getMaDonTraHang(),
                maDonHang,
                formatDate(dth.getNgayTraHang()),
                tenNV,
                formatToVND(dth.getThanhTien()),
                trangThai
            });
        }
    }

    private List<DonTraHang> searchDonTraHang(LocalDate dateFrom, LocalDate dateTo, String tenNV, String maPhieuTra) {
        List<DonTraHang> all = donTraHangBUS.layTatCaDonTraHang();
        List<DonTraHang> result = new ArrayList<>();

        for (DonTraHang dth : all) {
            LocalDate ngayTra = dth.getNgayTraHang();

            // Lọc theo ngày
            if (ngayTra.isBefore(dateFrom) || ngayTra.isAfter(dateTo)) {
                continue;
            }

            // Lọc theo nhân viên
            if (!tenNV.isEmpty()) {
                if (dth.getNhanVien() == null
                        || !dth.getNhanVien().getMaNhanVien().toLowerCase().contains(tenNV.toLowerCase())) {
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
        if (date == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    @SuppressWarnings("deprecation")
    private String formatToVND(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    // Lấy dữ liệu đơn trả hàng từ database
    private List<DonTraHang> layDuLieuDonTraHang() {
        try {
            List<DonTraHang> danhSach = donTraHangBUS.layTatCaDonTraHang();
            if (danhSach != null && !danhSach.isEmpty()) {
                return danhSach;
            }
        } catch (Exception e) {
            System.err.println("Không lấy được dữ liệu từ database: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    // Thiết lập cột Thao tác với button
    // (Action column removed)

    // Xem chi tiết đơn trả hàng
    private void xemChiTietDonTraHang(String maDonTraHang) {
        // Tạo dialog chi tiết
        showChiTietDonTraHangDialog(maDonTraHang);
    }

    // Hiển thị dialog chi tiết đơn trả hàng
    private void showChiTietDonTraHangDialog(String maDonTraHang) {
        try {
            // Lấy thông tin đơn trả hàng từ database
            DonTraHang donTraHang = donTraHangBUS.layDonTraHangTheoMa(maDonTraHang);
            if (donTraHang == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Không tìm thấy đơn trả hàng với mã: " + maDonTraHang);
                return;
            }

            // Lấy chi tiết đơn trả hàng
            List<ChiTietDonTraHang> chiTietList = chiTietDonTraHangBUS.layChiTietTheoMaDonTra(maDonTraHang);

            // Debug: Kiểm tra số lượng chi tiết
            System.out.println("=== DEBUG: Chi tiết đơn trả hàng ===");
            System.out.println("Mã đơn trả: " + maDonTraHang);
            System.out.println("Số lượng chi tiết: " + (chiTietList != null ? chiTietList.size() : 0));
            if (chiTietList != null) {
                for (ChiTietDonTraHang ct : chiTietList) {
                    System.out.println(
                            "  - Sản phẩm: " + (ct.getSanPham() != null ? ct.getSanPham().getMaSanPham() : "NULL"));
                    System.out.println(
                            "    Tên: " + (ct.getSanPham() != null ? ct.getSanPham().getTenSanPham() : "NULL"));
                    System.out.println("    Số lượng: " + ct.getSoLuong());
                    System.out.println("    Đơn giá: " + ct.getDonGia());
                    System.out.println("    Lý do: " + ct.getLyDoTra());
                }
            }
            System.out.println("=====================================");

            javax.swing.JDialog dialog = new javax.swing.JDialog();
            dialog.setTitle("Xem đơn trả - " + maDonTraHang);
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
            headerPanel.setLayout(new java.awt.GridLayout(4, 2, 20, 15));
            headerPanel.setBackground(java.awt.Color.WHITE);
            headerPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            // Lấy thông tin từ database
            String nguoiTao = donTraHang.getNhanVien() != null ? donTraHang.getNhanVien().getTenNhanVien() : "N/A";
            String thoiGianTao = formatDate(donTraHang.getNgayTraHang());
            String maHoaDon = donTraHang.getDonHang() != null ? donTraHang.getDonHang().getMaDonHang() : "N/A";
            String tenKhachHang = "N/A";
            String soDienThoai = "N/A";

            // Lấy thông tin khách hàng nếu có
            if (donTraHang.getDonHang() != null && donTraHang.getDonHang().getKhachHang() != null) {
                KhachHang kh = donTraHang.getDonHang().getKhachHang();
                tenKhachHang = kh.getTenKhachHang() != null ? kh.getTenKhachHang() : "N/A";
                soDienThoai = kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "N/A";
            }

            // Row 1
            addInfoField(headerPanel, "Mã phiếu trả", maDonTraHang);
            addInfoField(headerPanel, "Người tạo", nguoiTao);

            // Row 2
            addInfoField(headerPanel, "Thời gian tạo", thoiGianTao);

            // Hiển thị trạng thái từ database (chỉ đọc, không cho sửa)
            String trangThai = donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy() : "Chưa xử lý";
            addInfoField(headerPanel, "Trạng thái", trangThai);

            // Row 3
            addInfoField(headerPanel, "Mã hóa đơn", maHoaDon);
            addInfoField(headerPanel, "Tên khách hàng", tenKhachHang);

            // Row 4
            addInfoField(headerPanel, "Số điện thoại", soDienThoai);
            addInfoField(headerPanel, "Tổng tiền", formatToVND(donTraHang.getThanhTien()));

            mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

            // === TABLE PANEL - Chi tiết sản phẩm trả ===
            String[] columns = {"Mã hàng", "Tên hàng", "Đơn vị tính", "Số lượng",
                "Giá trả hàng", "Thành tiền", "Lý do trả", "Thao tác"};
            List<Integer> columnWidths = Arrays.asList(100, 200, 100, 80, 120, 120, 200, 200);
            // Chỉ cho phép edit cột "Thao tác" (cột cuối cùng)
            List<Boolean> canEdit = Arrays.asList(false, false, false, false, false, false, false, true);

            TableDesign tableChiTiet = new TableDesign(columns, columnWidths, canEdit);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(tableChiTiet.getTable());
            scrollPane.setBorder(BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220), 1));

            // Điền dữ liệu chi tiết từ database
            fillChiTietData(tableChiTiet, chiTietList);

            // Hiển thị thông báo nếu không có chi tiết
            if (chiTietList == null || chiTietList.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        "Đơn trả hàng này chưa có chi tiết sản phẩm!");
            }

            // Tăng chiều cao hàng
            tableChiTiet.getTable().setRowHeight(45);

            // Set để theo dõi các hàng đã được xử lý
            java.util.Set<Integer> processedRows = new java.util.HashSet<>();

            // Thiết lập renderer và editor cho cột Thao tác
            setupActionColumnForChiTiet(tableChiTiet.getTable(), chiTietList, donTraHang, processedRows);

            mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

            // === BOTTOM PANEL - Buttons ===
            javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
            bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));
            bottomPanel.setBackground(java.awt.Color.WHITE);

            javax.swing.JButton btnDong = new javax.swing.JButton("Đóng");
            btnDong.setPreferredSize(new java.awt.Dimension(120, 40));
            ButtonStyles.apply(btnDong, ButtonStyles.Type.SECONDARY);
            btnDong.addActionListener(e -> {
                dialog.dispose();
                // Refresh lại bảng ngoài để cập nhật trạng thái mới nhất (không hiện
                // notification)
                reloadTableData();
            });

            bottomPanel.add(btnDong);
            mainPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);

            dialog.add(mainPanel);
            dialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Lỗi khi hiển thị chi tiết đơn trả hàng: " + e.getMessage());
        }
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

    // Điền dữ liệu chi tiết đơn trả hàng vào bảng
    private void fillChiTietData(TableDesign tableDesign, List<ChiTietDonTraHang> chiTietList) {
        tableDesign.getModelTable().setRowCount(0); // Xóa dữ liệu cũ

        if (chiTietList == null || chiTietList.isEmpty()) {
            return;
        }

        for (ChiTietDonTraHang ct : chiTietList) {
            String maSP = ct.getSanPham() != null ? ct.getSanPham().getMaSanPham() : "N/A";
            String tenSP = ct.getSanPham() != null ? ct.getSanPham().getTenSanPham() : "N/A";
            String donVi = "N/A";
            if (ct.getSanPham() != null && ct.getSanPham().getDonViTinh() != null) {
                donVi = ct.getSanPham().getDonViTinh().getTenDonVi();
            }
            String soLuong = String.valueOf(ct.getSoLuong());
            String donGia = formatToVND(ct.getDonGia());
            String thanhTien = formatToVND(ct.getThanhTien());
            String lyDoTra = ct.getLyDoTra() != null ? ct.getLyDoTra() : "N/A";

            // Không lưu trạng thái ở chi tiết nữa, mặc định là button
            tableDesign.getModelTable().addRow(new Object[]{
                maSP, tenSP, donVi, soLuong, donGia, thanhTien, lyDoTra, ""
            });
        }
    }

    // Thiết lập cột Thao tác cho bảng chi tiết với 2 button
    private void setupActionColumnForChiTiet(JTable table, List<ChiTietDonTraHang> chiTietList, DonTraHang donTraHang,
            java.util.Set<Integer> processedRows) {
        // Renderer cho cột Thao tác (index 7)
        table.getColumnModel().getColumn(7).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
                panel.setBackground(isSelected ? table.getSelectionBackground() : java.awt.Color.WHITE);
                panel.setOpaque(true);

                // Lấy trạng thái chi tiết từ database
                ChiTietDonTraHang chiTietHienTai = chiTietList.get(row);
                String trangThaiChiTiet = chiTietHienTai.getTrangThaiXuLy();
                String trangThaiDon = donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy()
                        : "Chờ duyệt";

                // Kiểm tra nếu chi tiết này đã được xử lý
                if (processedRows.contains(row)
                        || "Đã thêm vào kho".equals(trangThaiChiTiet)
                        || "Đã duyệt xuất hủy".equals(trangThaiChiTiet)
                        || "Đã xuất hủy".equals(trangThaiChiTiet)
                        || "Chờ xuất hủy".equals(trangThaiDon)
                        || "Đã xử lý".equals(trangThaiDon)) {
                    // Không cho phép edit nếu đã xử lý hoặc đã duyệt
                    String labelText = "✓ Đã xử lý";
                    javax.swing.JLabel lblStatus = new javax.swing.JLabel(labelText);
                    lblStatus.setForeground(new java.awt.Color(40, 167, 69));
                    lblStatus.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                    panel.add(lblStatus);
                } else {
                    // Tạo 2 button
                    JButton btnThemVaoKho = new JButton("Thêm vào kho");
                    btnThemVaoKho.setPreferredSize(new java.awt.Dimension(110, 32));
                    btnThemVaoKho.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
                    ButtonStyles.apply(btnThemVaoKho, ButtonStyles.Type.SUCCESS);
                    btnThemVaoKho.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                    JButton btnXuatHuy = new JButton("Xuất hủy");
                    btnXuatHuy.setPreferredSize(new java.awt.Dimension(90, 32));
                    btnXuatHuy.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
                    ButtonStyles.apply(btnXuatHuy, ButtonStyles.Type.DANGER);
                    btnXuatHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                    panel.add(btnThemVaoKho);
                    panel.add(btnXuatHuy);
                }

                return panel;
            }
        });

        // Editor cho cột Thao tác - quan trọng để button có thể click được
        table.getColumnModel().getColumn(7)
                .setCellEditor(new javax.swing.DefaultCellEditor(new javax.swing.JCheckBox()) {
                    private JPanel panel;
                    private JButton btnThemVaoKho;
                    private JButton btnXuatHuy;
                    private int currentRow;

                    @Override
                    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                            int row, int column) {
                        this.currentRow = row;

                        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
                        panel.setBackground(java.awt.Color.WHITE);

                        // Lấy trạng thái chi tiết từ database
                        ChiTietDonTraHang chiTietHienTai = chiTietList.get(currentRow);
                        String trangThaiChiTiet = chiTietHienTai.getTrangThaiXuLy();
                        String trangThaiDon = donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy()
                                : "Chờ duyệt";

                        // Kiểm tra nếu chi tiết này đã được xử lý
                        if (processedRows.contains(currentRow)
                                || "Đã thêm vào kho".equals(trangThaiChiTiet)
                                || "Đã duyệt xuất hủy".equals(trangThaiChiTiet)
                                || "Đã xuất hủy".equals(trangThaiChiTiet)
                                || "Chờ xuất hủy".equals(trangThaiDon)
                                || "Đã xử lý".equals(trangThaiDon)) {
                            // Không cho phép edit nếu đã xử lý hoặc đã duyệt
                            String labelText = "✓ Đã xử lý";
                            javax.swing.JLabel lblStatus = new javax.swing.JLabel(labelText);
                            lblStatus.setForeground(new java.awt.Color(40, 167, 69));
                            lblStatus.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                            panel.add(lblStatus);
                        } else {
                            // Tạo 2 button với action listener
                            btnThemVaoKho = new JButton("Thêm vào kho");
                            btnThemVaoKho.setPreferredSize(new java.awt.Dimension(110, 32));
                            btnThemVaoKho.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
                            ButtonStyles.apply(btnThemVaoKho, ButtonStyles.Type.SUCCESS);
                            btnThemVaoKho.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                            btnThemVaoKho.setFocusable(false);

                            btnXuatHuy = new JButton("Xuất hủy");
                            btnXuatHuy.setPreferredSize(new java.awt.Dimension(90, 32));
                            btnXuatHuy.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
                            ButtonStyles.apply(btnXuatHuy, ButtonStyles.Type.DANGER);
                            btnXuatHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                            btnXuatHuy.setFocusable(false);

                            // Add action listeners
                            btnThemVaoKho.addActionListener(e -> {
                                ChiTietDonTraHang chiTiet = chiTietList.get(currentRow);
                                xuLyThemVaoKho(chiTiet, table, currentRow, donTraHang, processedRows,
                                        chiTietList.size());
                                fireEditingStopped();
                            });

                            btnXuatHuy.addActionListener(e -> {
                                ChiTietDonTraHang chiTiet = chiTietList.get(currentRow);
                                xuLyXuatHuy(chiTiet, table, currentRow, donTraHang);
                                fireEditingStopped();
                            });

                            panel.add(btnThemVaoKho);
                            panel.add(btnXuatHuy);
                        }

                        return panel;
                    }

                    @Override
                    public Object getCellEditorValue() {
                        return table.getValueAt(currentRow, 7);
                    }

                });
    }

    // Xử lý thêm sản phẩm trả về vào kho
    private void xuLyThemVaoKho(ChiTietDonTraHang chiTiet, JTable table, int row, DonTraHang donTraHangParam,
            java.util.Set<Integer> processedRows, int totalItems) {
        try {
            // Lấy đơn trả hàng từ chi tiết để đảm bảo đúng
            DonTraHang donTraHang = chiTiet.getDonTraHang();
            if (donTraHang == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Không tìm thấy thông tin đơn trả hàng!");
                return;
            }

            String maSanPham = chiTiet.getSanPham().getMaSanPham();
            String tenSanPham = chiTiet.getSanPham().getTenSanPham();
            int soLuong = chiTiet.getSoLuong();

            // Xác nhận từ người dùng
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    null,
                    String.format("Bạn có chắc muốn thêm %d sản phẩm '%s' vào kho?",
                            soLuong, tenSanPham),
                    "Xác nhận thêm vào kho",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE);

            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                return; // Người dùng chọn NO hoặc đóng dialog
            }

            // Lấy danh sách lô hàng của sản phẩm
            List<LoHang> danhSachLoHangGoc = loHangDAO.findByMaSanPham(maSanPham);

            if (danhSachLoHangGoc.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        "Không tìm thấy lô hàng cho sản phẩm này!\nVui lòng tạo lô hàng mới trước.");
                return;
            }

            LocalDate today = LocalDate.now();

            // LỌC BỎ TẤT CẢ CÁC LÔ HẾT HẠN (HSD < today) - KHÔNG PHÂN BIỆT ĐÃ XUẤT HỦY HAY
            // CHƯA
            // CHỈ GIỮ LẠI CÁC LÔ CÒN HẠN (HSD >= today) VÀ ĐANG HOẠT ĐỘNG
            List<LoHang> danhSachLoHang = new ArrayList<>();
            int soLoHetHan = 0;

            System.out.println("=== LỌC LÔ HÀNG ===");
            System.out.println("Ngày hôm nay: " + today);
            System.out.println("Tổng số lô hàng ban đầu: " + danhSachLoHangGoc.size());

            for (LoHang lh : danhSachLoHangGoc) {
                LocalDate hanSuDung = lh.getHanSuDung();
                boolean hetHan = hanSuDung.isBefore(today); // HSD < today = hết hạn
                boolean conHan = hanSuDung.isAfter(today) || hanSuDung.isEqual(today); // HSD >= today = còn hạn
                boolean trangThai = lh.isTrangThai();

                // NẾU LÔ HẾT HẠN VÀ ĐANG HOẠT ĐỘNG -> SET TRẠNG THÁI = FALSE VÀ CẬP NHẬT VÀO
                // DATABASE
                if (hetHan && trangThai) {
                    System.out.println(String.format("  ⚠️ PHÁT HIỆN LÔ HẾT HẠN - Lô: %s | HSD: %s | Tồn kho: %d",
                            lh.getMaLoHang(), hanSuDung, lh.getTonKho()));

                    // Set trạng thái = false (hết hạn)
                    lh.setTrangThai(false);

                    // Cập nhật vào database
                    boolean updateSuccess = loHangDAO.update(lh);
                    if (updateSuccess) {
                        System.out.println(String.format("  ✓ ĐÃ CẬP NHẬT TRẠNG THÁI HẾT HẠN VÀO DATABASE - Lô: %s",
                                lh.getMaLoHang()));
                    } else {
                        System.err.println(
                                String.format("  ✗ LỖI: Không thể cập nhật trạng thái hết hạn vào database - Lô: %s",
                                        lh.getMaLoHang()));
                    }

                    soLoHetHan++;
                    System.out.println(String.format(
                            "  ✗ BỎ QUA - Lô: %s | HSD: %s | Tồn kho: %d | Lý do: Hết hạn (đã cập nhật trạng thái)",
                            lh.getMaLoHang(), hanSuDung, lh.getTonKho()));
                } // CHỈ GIỮ LẠI CÁC LÔ CÒN HẠN VÀ ĐANG HOẠT ĐỘNG
                else if (conHan && trangThai) {
                    danhSachLoHang.add(lh);
                    System.out.println(
                            String.format("  ✓ GIỮ LẠI - Lô: %s | HSD: %s | Tồn kho: %d (Còn hạn, đang hoạt động)",
                                    lh.getMaLoHang(), hanSuDung, lh.getTonKho()));
                } else {
                    soLoHetHan++;
                    String lyDo = !trangThai ? "Ngừng hoạt động" : "Không xác định";
                    System.out.println(String.format("  ✗ BỎ QUA - Lô: %s | HSD: %s | Tồn kho: %d | Lý do: %s",
                            lh.getMaLoHang(), hanSuDung, lh.getTonKho(), lyDo));
                }
            }

            System.out.println("Số lô hết hạn/ngừng hoạt động (đã loại bỏ): " + soLoHetHan);
            System.out.println("Số lô còn hạn và đang hoạt động: " + danhSachLoHang.size());

            if (danhSachLoHang.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        String.format("❌ KHÔNG THỂ THÊM SẢN PHẨM '%s' VÀO KHO!\n\n"
                                + "Không còn lô hàng nào CÒN HẠN và đang hoạt động.\n"
                                + "Tất cả các lô hàng đều đã hết hạn hoặc ngừng hoạt động.\n\n"
                                + "⚠️ BẮT BUỘC: Vui lòng XUẤT HỦY các lô hàng hết hạn trước, sau đó tạo lô hàng mới với hạn sử dụng hợp lệ.",
                                tenSanPham));
                return;
            }

            // Tìm lô hàng có ngày hết hạn gần nhất (FIFO - First In First Out)
            // Tất cả lô trong danhSachLoHang đã được đảm bảo là còn hạn và đang hoạt động
            LoHang loHangPhongHop = null;

            System.out.println("=== CHỌN LÔ HÀNG PHÙ HỢP (FIFO) ===");

            for (LoHang lh : danhSachLoHang) {
                LocalDate hanSuDung = lh.getHanSuDung();

                System.out.println(String.format("  - Lô: %s | HSD: %s | Tồn kho: %d",
                        lh.getMaLoHang(), hanSuDung, lh.getTonKho()));

                // Ưu tiên lô có ngày hết hạn gần nhất (FIFO - First In First Out)
                if (loHangPhongHop == null || hanSuDung.isBefore(loHangPhongHop.getHanSuDung())) {
                    loHangPhongHop = lh;
                    System.out.println("    -> ✓ Chọn lô này làm lô phù hợp");
                }
            }

            // Đảm bảo đã chọn được lô hàng (nên không bao giờ null vì đã kiểm tra isEmpty ở
            // trên)
            if (loHangPhongHop == null) {
                System.err.println("❌ LỖI NGHIÊM TRỌNG: Không thể chọn lô hàng!");
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "❌ LỖI HỆ THỐNG!\n\nKhông thể chọn lô hàng. Vui lòng liên hệ quản trị viên.");
                return;
            }

            // VALIDATION CUỐI CÙNG: Đảm bảo lô được chọn thực sự còn hạn
            LocalDate hanSuDungCuoiCung = loHangPhongHop.getHanSuDung();
            boolean conHanCuoiCung = hanSuDungCuoiCung.isAfter(today) || hanSuDungCuoiCung.isEqual(today);

            if (!conHanCuoiCung) {
                System.err.println("❌ LỖI NGHIÊM TRỌNG: Lô hàng đã hết hạn nhưng vẫn được chọn!");
                System.err.println("Mã lô: " + loHangPhongHop.getMaLoHang());
                System.err.println("Hạn sử dụng: " + hanSuDungCuoiCung);
                System.err.println("Ngày hôm nay: " + today);
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        String.format("❌ LỖI HỆ THỐNG!\n\n"
                                + "Lô hàng '%s' đã HẾT HẠN (HSD: %s) nhưng vẫn được chọn.\n"
                                + "Vui lòng liên hệ quản trị viên để xử lý.",
                                loHangPhongHop.getMaLoHang(), hanSuDungCuoiCung));
                return;
            }

            // Log thông tin trước khi cập nhật
            System.out.println("=== THÊM SẢN PHẨM VÀO KHO ===");
            System.out.println("Mã lô hàng: " + loHangPhongHop.getMaLoHang());
            System.out.println("Tên lô hàng: " + loHangPhongHop.getTenLoHang());
            System.out.println("Hạn sử dụng: " + loHangPhongHop.getHanSuDung() + " (Còn hạn: CÓ)");
            System.out.println("Tồn kho hiện tại: " + loHangPhongHop.getTonKho());
            System.out.println("Số lượng thêm: " + soLuong);

            // Cập nhật tồn kho
            boolean success = loHangBUS.updateTonKho(loHangPhongHop.getMaLoHang(), soLuong);

            if (success) {
                // CẬP NHẬT TRẠNG THÁI CHI TIẾT THÀNH "Đã thêm vào kho"
                // Điều này đảm bảo khi mở lại dialog, chi tiết này sẽ hiển thị trạng thái đã xử
                // lý
                chiTiet.setTrangThaiXuLy("Đã thêm vào kho");
                boolean updated = chiTietDonTraHangBUS.capNhatTrangThaiChiTiet(chiTiet);

                if (updated) {
                    System.out.println("✓ Đã cập nhật trạng thái chi tiết thành 'Đã thêm vào kho'");

                    // Đánh dấu hàng này đã được xử lý (cho phiên làm việc hiện tại)
                    processedRows.add(row);

                    // Cập nhật trạng thái CHỈ cho hàng hiện tại trong dialog
                    table.setValueAt("✓ Đã xử lý", row, 7);
                    table.repaint();

                    // Kiểm tra xem còn chi tiết nào chưa xử lý không
                    List<ChiTietDonTraHang> chiTietConLai = chiTietDonTraHangBUS.layChiTietTheoMaDonTra(
                            donTraHang.getMaDonTraHang());

                    // Đếm số chi tiết chưa xử lý
                    long soChiTietChuaXuLy = chiTietConLai.stream()
                            .filter(ct -> "Chưa xử lý".equals(ct.getTrangThaiXuLy()))
                            .count();

                    // Nếu không còn chi tiết nào chưa xử lý → cập nhật trạng thái đơn thành "Đã xử
                    // lý"
                    if (soChiTietChuaXuLy == 0) {
                        donTraHang.setTrangThaiXuLy("Đã xử lý");
                        donTraHangBUS.capNhatDonTraHang(donTraHang);
                        System.out.println("✓ Đã cập nhật trạng thái đơn trả thành 'Đã xử lý'");
                    }

                    // Refresh bảng ngoài để cập nhật trạng thái
                    reloadTableData();

                    // Lấy lại thông tin lô hàng sau khi cập nhật để hiển thị tồn kho mới
                    LoHang loHangSauCapNhat = loHangDAO.findById(loHangPhongHop.getMaLoHang()).orElse(loHangPhongHop);

                    Notifications.getInstance().show(Notifications.Type.SUCCESS,
                            String.format("Đã thêm %d sản phẩm vào lô '%s'\nTồn kho mới: %d",
                                    soLuong, loHangPhongHop.getTenLoHang(), loHangSauCapNhat.getTonKho()));

                    System.out.println("✓ Cập nhật thành công! Tồn kho mới: " + loHangSauCapNhat.getTonKho());
                } else {
                    System.err.println("✗ Lỗi: Không thể cập nhật trạng thái chi tiết!");
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            "Đã thêm vào kho nhưng không thể cập nhật trạng thái chi tiết!");
                }
            } else {
                System.err.println("✗ Lỗi: Không thể cập nhật tồn kho!");
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Không thể cập nhật tồn kho cho sản phẩm!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Lỗi: " + e.getMessage());
        }
    }

    // Xử lý xuất hủy sản phẩm - Duyệt đơn trả hàng để chờ xuất hủy
    private void xuLyXuatHuy(ChiTietDonTraHang chiTiet, JTable table, int row, DonTraHang donTraHangParam) {
        try {
            // Lấy đơn trả hàng từ chi tiết để đảm bảo đúng
            DonTraHang donTraHang = chiTiet.getDonTraHang();
            if (donTraHang == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Không tìm thấy thông tin đơn trả hàng!");
                return;
            }

            String tenSanPham = chiTiet.getSanPham().getTenSanPham();
            int soLuong = chiTiet.getSoLuong();
            String maDonTra = donTraHang.getMaDonTraHang();

            // Xác nhận từ người dùng
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    null,
                    String.format("Bạn có chắc muốn duyệt xuất hủy %d sản phẩm '%s'?\nĐơn trả: %s",
                            soLuong, tenSanPham, maDonTra),
                    "Xác nhận duyệt xuất hủy",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE);

            if (confirm != javax.swing.JOptionPane.YES_OPTION) {
                return; // Người dùng chọn NO hoặc đóng dialog
            }

            // CẬP NHẬT TRẠNG THÁI CHI TIẾT (không phải đơn) thành "Đã duyệt xuất hủy"
            chiTiet.setTrangThaiXuLy("Đã duyệt xuất hủy");
            boolean success = chiTietDonTraHangBUS.capNhatTrangThaiChiTiet(chiTiet);

            if (success) {
                System.out.println("✓ Đã cập nhật trạng thái chi tiết thành 'Đã duyệt xuất hủy'");

                // Cập nhật hiển thị trong bảng
                table.setValueAt("✓ Đã duyệt", row, 7);
                table.repaint();

                // Kiểm tra xem còn chi tiết nào chưa xử lý không
                List<ChiTietDonTraHang> chiTietConLai = chiTietDonTraHangBUS.layChiTietTheoMaDonTra(maDonTra);

                // Đếm số chi tiết chưa xử lý
                long soChiTietChuaXuLy = chiTietConLai.stream()
                        .filter(ct -> "Chưa xử lý".equals(ct.getTrangThaiXuLy()))
                        .count();

                // Nếu không còn chi tiết nào chưa xử lý → cập nhật trạng thái đơn thành "Chờ
                // xuất hủy"
                if (soChiTietChuaXuLy == 0) {
                    donTraHang.setTrangThaiXuLy("Chờ xuất hủy");
                    donTraHangBUS.capNhatDonTraHang(donTraHang);
                    System.out.println("✓ Đã cập nhật trạng thái đơn trả thành 'Chờ xuất hủy'");
                }

                // Refresh bảng ngoài để cập nhật trạng thái
                reloadTableData();

                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        String.format(
                                "Đã duyệt xuất hủy sản phẩm '%s'. Sản phẩm sẽ hiển thị ở phiếu xuất hủy của nhân viên.",
                                tenSanPham));
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Lỗi khi cập nhật trạng thái chi tiết!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Lỗi: " + e.getMessage());
        }
    }

    /**
     * Làm mới dữ liệu bảng đơn trả hàng (hiển thị notification)
     */
    public void refreshData() {
        reloadTableData();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã làm mới dữ liệu!");
    }

    /**
     * Reload dữ liệu bảng (không hiển thị notification)
     */
    private void reloadTableData() {
        List<DonTraHang> danhSach = layDuLieuDonTraHang();
        fillContent(danhSach);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        modalOrderDetailLayout.setVerticalGroup(
                modalOrderDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        setBackground(new java.awt.Color(204, 204, 0));
        setMinimumSize(new java.awt.Dimension(1226, 278));
        setPreferredSize(new java.awt.Dimension(1226, 278));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setPreferredSize(new java.awt.Dimension(1226, 278));
        pnAll.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel
                .setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        headerPanel.setMinimumSize(new java.awt.Dimension(1190, 104));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(590, 100));

        cboStatus = new javax.swing.JComboBox<>();
        cboStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Chưa xử lý", "Đã xử lý", "Chờ xuất hủy" }));
        cboStatus.setPreferredSize(new java.awt.Dimension(150, 40));
        cboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStatusActionPerformed(evt);
            }
        });

        txtOrder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtOrder.setMinimumSize(new java.awt.Dimension(350, 40));
        txtOrder.setPreferredSize(new java.awt.Dimension(350, 40));
        txtOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderActionPerformed(evt);
            }
        });

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
        txtExport.setText("Xuất Excel");
        txtExport.setMaximumSize(new java.awt.Dimension(150, 40));
        txtExport.setMinimumSize(new java.awt.Dimension(150, 40));
        txtExport.setPreferredSize(new java.awt.Dimension(150, 40));
        txtExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExportActionPerformed(evt);
            }
        });

        btnView.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnView.setText("Xem chi tiết");
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
                                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20,
                                        Short.MAX_VALUE)
                                .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 210,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 210,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65,
                                        Short.MAX_VALUE)
                                .addComponent(txtExport, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                        .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel5Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(txtOrder,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(btnSearch,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtExport,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 41,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(jLabel2,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(12, 12, 12))
                                                        .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        headerPanel.add(jPanel5, java.awt.BorderLayout.CENTER);

        pnAll.add(headerPanel, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMinimumSize(new java.awt.Dimension(1226, 174));
        jPanel4.setPreferredSize(new java.awt.Dimension(1226, 174));
        jPanel4.setLayout(new java.awt.BorderLayout());

        // Thêm tiêu đề "DANH SÁCH THÔNG TIN ĐƠN TRẢ HÀNG"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSearchActionPerformed
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
            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    "Tìm thấy " + result.size() + " phiếu trả hàng!");
        }
    }// GEN-LAST:event_btnSearchActionPerformed

    private void txtExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtExportActionPerformed
        boolean success = vn.edu.iuh.fit.iuhpharmacitymanagement.util.XuatFileExcel.xuatExcelVoiDialogVaTieuDe(
                tableDesign.getTable(),
                "DanhSachTraHang",
                "Trả Hàng",
                "DANH SÁCH THÔNG TIN TRẢ HÀNG");
        if (success) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file Excel thành công!");
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Xuất file Excel thất bại!");
        }
    }// GEN-LAST:event_txtExportActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnViewActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn phiếu trả hàng cần xem chi tiết!");
        } else {
            String maDTH = (String) table.getValueAt(selectedRow, 0);
            xemChiTietDonTraHang(maDTH);
        }
    }// GEN-LAST:event_btnViewActionPerformed

    private void txtOrderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtOrderActionPerformed
        // Xử lý tìm kiếm phiếu trả hàng khi nhấn Enter (nhập thủ công)
        String maPhieuTra = txtOrder.getText().trim();

        // Loại bỏ ký tự đặc biệt
        maPhieuTra = maPhieuTra.replaceAll("[\\r\\n\\t]", "");

        if (!maPhieuTra.isEmpty()) {
            timKiemVaHienThiPhieuTra(maPhieuTra);
            // Clear textfield sau khi xử lý để sẵn sàng tìm kiếm tiếp
            javax.swing.SwingUtilities.invokeLater(() -> {
                txtOrder.setText("");
            });
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Vui lòng nhập mã phiếu trả hàng!");
        }
    }// GEN-LAST:event_txtOrderActionPerformed

    private void cboStatusActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboStatusActionPerformed
        try {
            String selected = (String) cboStatus.getSelectedItem();
            List<DonTraHang> all = layDuLieuDonTraHang();
            if (selected == null || "Tất cả".equals(selected)) {
                fillContent(all);
                return;
            }
            List<DonTraHang> filtered = new ArrayList<>();
            for (DonTraHang d : all) {
                String trangThai = d.getTrangThaiXuLy() != null ? d.getTrangThaiXuLy() : "Chưa xử lý";
                if (selected.equals(trangThai)) {
                    filtered.add(d);
                }
            }
            fillContent(filtered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// GEN-LAST:event_cboStatusActionPerformed

    /**
     * Hiển thị preview hóa đơn trả hàng (UI giống hóa đơn bán hàng)
     */
    private void hienThiHoaDonTraHang(DonTraHang donTraHang, List<ChiTietDonTraHang> chiTietList) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa Đơn Trả Hàng");
        dialog.setModal(true);
        dialog.setSize(650, 900);
        dialog.setLocationRelativeTo(null);

        // Scroll pane cho toàn bộ hóa đơn
        javax.swing.JScrollPane mainScrollPane = new javax.swing.JScrollPane();
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Format cho số tiền và ngày tháng
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("#,###");
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // ========== HEADER - THÔNG TIN CỬA HÀNG ==========
        javax.swing.JLabel lblStoreName = new javax.swing.JLabel("IUH PHARMACITY");
        lblStoreName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblStoreName.setForeground(new java.awt.Color(0, 120, 215));
        lblStoreName.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStoreName);
        mainPanel.add(javax.swing.Box.createVerticalStrut(3));

        javax.swing.JLabel lblAddress = new javax.swing.JLabel("12 Nguyen Van Bao, Ward 4, Go Vap District, HCMC");
        lblAddress.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblAddress.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblAddress);
        mainPanel.add(javax.swing.Box.createVerticalStrut(2));

        javax.swing.JLabel lblContact = new javax.swing.JLabel("Hotline: 1800 6928 | Email: cskh@pharmacity.vn");
        lblContact.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblContact.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblContact);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== TIÊU ĐỀ HÓA ĐƠN ==========
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("PHIEU TRA HANG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblTitle.setForeground(new java.awt.Color(220, 53, 69)); // Màu đỏ cho trả hàng
        lblTitle.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(javax.swing.Box.createVerticalStrut(8));

        // ========== BARCODE MÃ ĐƠN TRẢ HÀNG ==========
        try {
            java.awt.image.BufferedImage barcodeImage = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                    .taoBarcode(donTraHang.getMaDonTraHang());
            java.awt.image.BufferedImage barcodeWithText = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil
                    .addTextBelow(barcodeImage, donTraHang.getMaDonTraHang());

            javax.swing.JLabel lblBarcode = new javax.swing.JLabel(new javax.swing.ImageIcon(barcodeWithText));
            lblBarcode.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            mainPanel.add(lblBarcode);
        } catch (Exception ex) {
            System.err.println("Lỗi tạo barcode: " + ex.getMessage());
        }
        mainPanel.add(javax.swing.Box.createVerticalStrut(2));

        String ngayTra = donTraHang.getNgayTraHang().format(dateFormatter);
        javax.swing.JLabel lblDate = new javax.swing.JLabel("Ngay tra: " + ngayTra);
        lblDate.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblDate.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblDate);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== THÔNG TIN KHÁCH HÀNG VÀ NHÂN VIÊN (2 CỘT) ==========
        javax.swing.JPanel infoTablePanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 10, 0));
        infoTablePanel.setBackground(java.awt.Color.WHITE);
        infoTablePanel.setMaximumSize(new java.awt.Dimension(600, 80));

        // THÔNG TIN KHÁCH HÀNG (Cột trái)
        KhachHang khachHang = donTraHang.getDonHang() != null ? donTraHang.getDonHang().getKhachHang() : null;
        javax.swing.JPanel customerPanel = new javax.swing.JPanel();
        customerPanel.setLayout(new javax.swing.BoxLayout(customerPanel, javax.swing.BoxLayout.Y_AXIS));
        customerPanel.setBackground(java.awt.Color.WHITE);
        customerPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        javax.swing.JLabel lblCustomerTitle = new javax.swing.JLabel("THONG TIN KHACH HANG");
        lblCustomerTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        lblCustomerTitle.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        customerPanel.add(lblCustomerTitle);
        customerPanel.add(javax.swing.Box.createVerticalStrut(3));

        String tenKH = khachHang != null ? khachHang.getTenKhachHang() : "Vang lai";
        javax.swing.JLabel lblCustomerName = new javax.swing.JLabel("Ho ten: " + tenKH);
        lblCustomerName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblCustomerName.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        customerPanel.add(lblCustomerName);

        if (khachHang != null && khachHang.getSoDienThoai() != null) {
            javax.swing.JLabel lblCustomerPhone = new javax.swing.JLabel("SDT: " + khachHang.getSoDienThoai());
            lblCustomerPhone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
            lblCustomerPhone.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            customerPanel.add(lblCustomerPhone);
        }

        // Mã hóa đơn gốc
        String maHoaDonGoc = donTraHang.getDonHang() != null ? donTraHang.getDonHang().getMaDonHang() : "N/A";
        javax.swing.JLabel lblOrderCode = new javax.swing.JLabel("Ma HD goc: " + maHoaDonGoc);
        lblOrderCode.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblOrderCode.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        customerPanel.add(lblOrderCode);

        // THÔNG TIN NHÂN VIÊN (Cột phải)
        NhanVien nhanVien = donTraHang.getNhanVien();
        javax.swing.JPanel employeePanel = new javax.swing.JPanel();
        employeePanel.setLayout(new javax.swing.BoxLayout(employeePanel, javax.swing.BoxLayout.Y_AXIS));
        employeePanel.setBackground(java.awt.Color.WHITE);
        employeePanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        javax.swing.JLabel lblEmployeeTitle = new javax.swing.JLabel("NHAN VIEN XU LY");
        lblEmployeeTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        lblEmployeeTitle.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        employeePanel.add(lblEmployeeTitle);
        employeePanel.add(javax.swing.Box.createVerticalStrut(3));

        if (nhanVien != null) {
            javax.swing.JLabel lblEmployeeName = new javax.swing.JLabel("Ho ten: " + nhanVien.getTenNhanVien());
            lblEmployeeName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
            lblEmployeeName.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            employeePanel.add(lblEmployeeName);

            if (nhanVien.getSoDienThoai() != null) {
                javax.swing.JLabel lblEmployeePhone = new javax.swing.JLabel("SDT: " + nhanVien.getSoDienThoai());
                lblEmployeePhone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
                lblEmployeePhone.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                employeePanel.add(lblEmployeePhone);
            }
        }

        infoTablePanel.add(customerPanel);
        infoTablePanel.add(employeePanel);
        mainPanel.add(infoTablePanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(10));

        // ========== BẢNG SẢN PHẨM TRẢ ==========
        String[] columnNames = {"STT", "Ten san pham", "SL", "Don gia", "Thanh tien", "Ly do"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        table.getTableHeader().setBackground(new java.awt.Color(240, 240, 240));
        table.setGridColor(new java.awt.Color(220, 220, 220));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(30); // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên sản phẩm
        table.getColumnModel().getColumn(2).setPreferredWidth(35); // SL
        table.getColumnModel().getColumn(3).setPreferredWidth(80); // Đơn giá
        table.getColumnModel().getColumn(4).setPreferredWidth(85); // Thành tiền
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Lý do

        // Center align cho các cột số
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // Right align cho các cột tiền
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        // Thêm dữ liệu vào bảng
        int stt = 1;
        for (ChiTietDonTraHang chiTiet : chiTietList) {
            SanPham sanPham = chiTiet.getSanPham();
            String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
            String lyDo = chiTiet.getLyDoTra() != null ? chiTiet.getLyDoTra() : "";

            tableModel.addRow(new Object[]{
                stt++,
                tenSP,
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                currencyFormat.format(chiTiet.getThanhTien()) + " đ",
                lyDo
            });
        }

        javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        tableScrollPane.setPreferredSize(new java.awt.Dimension(580, 250));
        tableScrollPane.setMaximumSize(new java.awt.Dimension(600, 250));
        mainPanel.add(tableScrollPane);
        mainPanel.add(javax.swing.Box.createVerticalStrut(12));

        // ========== BẢNG THANH TOÁN ==========
        javax.swing.JPanel paymentPanel = new javax.swing.JPanel();
        paymentPanel.setLayout(new javax.swing.BoxLayout(paymentPanel, javax.swing.BoxLayout.Y_AXIS));
        paymentPanel.setBackground(java.awt.Color.WHITE);
        paymentPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        paymentPanel.setMaximumSize(new java.awt.Dimension(450, 150));

        // Đường kẻ trước TỔNG TIỀN TRẢ
        javax.swing.JPanel separatorPanel = new javax.swing.JPanel();
        separatorPanel.setBackground(new java.awt.Color(200, 200, 200));
        separatorPanel.setMaximumSize(new java.awt.Dimension(450, 1));
        paymentPanel.add(separatorPanel);
        paymentPanel.add(javax.swing.Box.createVerticalStrut(10));

        // TỔNG TIỀN TRẢ (in đậm, màu đỏ)
        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        tongTienPanel.setBackground(java.awt.Color.WHITE);
        tongTienPanel.setMaximumSize(new java.awt.Dimension(450, 30));

        javax.swing.JLabel lblTongTienLeft = new javax.swing.JLabel("TONG TIEN TRA:");
        lblTongTienLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

        javax.swing.JLabel lblTongTienRight = new javax.swing.JLabel(
                currencyFormat.format(donTraHang.getThanhTien()) + " đ");
        lblTongTienRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblTongTienRight.setForeground(new java.awt.Color(220, 53, 69)); // Màu đỏ
        lblTongTienRight.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

        tongTienPanel.add(lblTongTienLeft, java.awt.BorderLayout.WEST);
        tongTienPanel.add(lblTongTienRight, java.awt.BorderLayout.EAST);

        paymentPanel.add(tongTienPanel);
        paymentPanel.add(javax.swing.Box.createVerticalStrut(10));

        // Đường kẻ sau TỔNG TIỀN TRẢ
        javax.swing.JPanel separator2Panel = new javax.swing.JPanel();
        separator2Panel.setBackground(new java.awt.Color(200, 200, 200));
        separator2Panel.setMaximumSize(new java.awt.Dimension(450, 1));
        paymentPanel.add(separator2Panel);

        mainPanel.add(paymentPanel);
        mainPanel.add(javax.swing.Box.createVerticalStrut(15));

        // ========== FOOTER ==========
        javax.swing.JLabel lblFooter1 = new javax.swing.JLabel("Cam on quy khach!");
        lblFooter1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblFooter1.setForeground(new java.awt.Color(0, 120, 215));
        lblFooter1.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblFooter1);
        mainPanel.add(javax.swing.Box.createVerticalStrut(3));

        javax.swing.JLabel lblFooter2 = new javax.swing.JLabel("Phieu tra hang chi co gia tri trong ngay lap");
        lblFooter2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 8));
        lblFooter2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        mainPanel.add(lblFooter2);

        // Thêm mainPanel vào scrollPane
        mainScrollPane.setViewportView(mainPanel);

        dialog.add(mainScrollPane);
        dialog.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox<String> cboStatus;
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

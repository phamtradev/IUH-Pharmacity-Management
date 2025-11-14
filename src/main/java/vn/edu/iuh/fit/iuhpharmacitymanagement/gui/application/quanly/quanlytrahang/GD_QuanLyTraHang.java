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
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;
import java.awt.Component;
import java.awt.FlowLayout;

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
        applyButtonStyles();
        applyFontStyles();
        fillTable();
    }

    private void setUIManager() {
        txtOrder.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã phiếu trả");
        UIManager.put("Button.arc", 10);
        jDateFrom.setDate(Date.valueOf(LocalDate.now()));
        jDateTo.setDate(Date.valueOf(LocalDate.now()));

    }

    private void applyButtonStyles() {
        // Apply ButtonStyles cho các button chính
        ButtonStyles.apply(btnSearch, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnView, ButtonStyles.Type.INFO);
        ButtonStyles.apply(txtExport, ButtonStyles.Type.WARNING);
    }

    private void applyFontStyles() {
        // Font cho các button chính
        FontStyles.apply(btnSearch, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnView, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(txtExport, FontStyles.Type.BUTTON_MEDIUM);

        // Font cho text field
        FontStyles.apply(txtOrder, FontStyles.Type.INPUT_FIELD);
    }

    private void fillTable() {
        String[] headers = { "Mã phiếu trả", "Mã hóa đơn", "Ngày trả", "Nhân viên", "Tổng tiền", "Trạng thái",
                "Thao tác" };
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

            // Lấy trạng thái từ đơn trả hàng
            String trangThai = dth.getTrangThaiXuLy() != null ? dth.getTrangThaiXuLy() : "Chưa xử lý";

            tableDesign.getModelTable().addRow(new Object[] {
                    dth.getMaDonTraHang(),
                    maDonHang,
                    formatDate(dth.getNgayTraHang()),
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
            LocalDate ngayTra = dth.getNgayTraHang();

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
        if (date == null)
            return "N/A";
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
                ButtonStyles.apply(btnAction, ButtonStyles.Type.INFO);
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
            String[] columns = { "Mã hàng", "Tên hàng", "Đơn vị tính", "Số lượng",
                    "Giá trả hàng", "Thành tiền", "Lý do trả", "Thao tác" };
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
            tableDesign.getModelTable().addRow(new Object[] {
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

                // Kiểm tra trạng thái đơn trả hàng hoặc hàng đã được xử lý
                String trangThaiDon = donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy()
                        : "Chờ duyệt";

                // Kiểm tra nếu hàng này đã được xử lý hoặc đơn đã được xử lý/xuất hủy
                if (processedRows.contains(row) || "Đã xử lý".equals(trangThaiDon)
                        || "Chờ xuất hủy".equals(trangThaiDon)) {
                    // Hiển thị label thông báo đã xử lý hoặc đã duyệt
                    String labelText = "Đã xử lý".equals(trangThaiDon) ? "✓ Đã xử lý" : "✓ Đã duyệt";
                    javax.swing.JLabel lblStatus = new javax.swing.JLabel(labelText);
                    lblStatus.setForeground(new java.awt.Color(40, 167, 69)); // Màu xanh lá
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

                        // Kiểm tra trạng thái đơn trả hàng hoặc hàng đã được xử lý
                        String trangThaiDon = donTraHang.getTrangThaiXuLy() != null ? donTraHang.getTrangThaiXuLy()
                                : "Chờ duyệt";

                        // Kiểm tra nếu hàng này đã được xử lý hoặc đơn đã được xử lý/xuất hủy
                        if (processedRows.contains(currentRow) || "Đã xử lý".equals(trangThaiDon)
                                || "Chờ xuất hủy".equals(trangThaiDon)) {
                            // Không cho phép edit nếu đã xử lý hoặc đã duyệt
                            String labelText = "Đã xử lý".equals(trangThaiDon) ? "✓ Đã xử lý" : "✓ Đã duyệt";
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
                }
                // CHỈ GIỮ LẠI CÁC LÔ CÒN HẠN VÀ ĐANG HOẠT ĐỘNG
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
                        String.format("❌ KHÔNG THỂ THÊM SẢN PHẨM '%s' VÀO KHO!\n\n" +
                                "Không còn lô hàng nào CÒN HẠN và đang hoạt động.\n" +
                                "Tất cả các lô hàng đều đã hết hạn hoặc ngừng hoạt động.\n\n" +
                                "⚠️ BẮT BUỘC: Vui lòng XUẤT HỦY các lô hàng hết hạn trước, sau đó tạo lô hàng mới với hạn sử dụng hợp lệ.",
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
                        String.format("❌ LỖI HỆ THỐNG!\n\n" +
                                "Lô hàng '%s' đã HẾT HẠN (HSD: %s) nhưng vẫn được chọn.\n" +
                                "Vui lòng liên hệ quản trị viên để xử lý.",
                                loHangPhongHop.getMaLoHang(), hanSuDungCuoiCung));
                return;
            }

            // Log thông tin trước khi cập nhật
            System.out.println("=== THÊM SẢN PHẨM VÀO KHO ===");
            System.out.println("Mã lô hàng: " + loHangPhongHop.getMaLoHang());
            System.out.println("Tên lô hàng: " + loHangPhongHop.getTenLoHang());
            System.out.println("Hạn sử dụng: " + loHangPhongHop.getHanSuDung() + " (Còn hạn: CÓ)");
            System.out.println("Tồn kho hiện tại: " + loHangPhongHop.getTonKho());

            // Cập nhật tồn kho
            boolean success = loHangBUS.updateTonKho(loHangPhongHop.getMaLoHang(), soLuong);

            if (success) {
                // Đánh dấu hàng này đã được xử lý
                processedRows.add(row);

                // Cập nhật trạng thái CHỈ cho hàng hiện tại trong dialog
                table.setValueAt("✓ Đã xử lý", row, 7);
                table.repaint();

                // Kiểm tra xem tất cả các item đã được xử lý chưa
                boolean tatCaDaXuLy = (processedRows.size() == totalItems);

                // Chỉ cập nhật trạng thái đơn trả hàng khi TẤT CẢ các item đã được xử lý
                if (tatCaDaXuLy) {
                    donTraHang.setTrangThaiXuLy("Đã xử lý");
                    donTraHangBUS.capNhatDonTraHang(donTraHang);
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
                System.err.println("✗ Lỗi: Không thể cập nhật tồn kho!");
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Lỗi khi cập nhật tồn kho! Vui lòng thử lại.");
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

            // Cập nhật trạng thái đơn trả hàng thành "Chờ xuất hủy"
            donTraHang.setTrangThaiXuLy("Chờ xuất hủy");
            boolean success = donTraHangBUS.capNhatDonTraHang(donTraHang);

            if (success) {
                // Cập nhật hiển thị trong bảng
                table.setValueAt("✓ Đã duyệt", row, 7);
                table.repaint();

                // Refresh bảng ngoài để cập nhật trạng thái
                reloadTableData();

                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        String.format(
                                "Đã duyệt xuất hủy đơn '%s'. Sản phẩm sẽ hiển thị ở phiếu xuất hủy của nhân viên.",
                                maDonTra));
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Lỗi khi cập nhật trạng thái đơn trả hàng!");
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
                                .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 115,
                                        Short.MAX_VALUE)
                                .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 210,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 210,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
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
                                                                .addGap(12, 12, 12))))
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

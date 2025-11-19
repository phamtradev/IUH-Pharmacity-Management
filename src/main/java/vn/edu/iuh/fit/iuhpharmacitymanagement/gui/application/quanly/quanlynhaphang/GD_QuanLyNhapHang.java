/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhaphang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonNhapHangBUS;
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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonNhapHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.DinhDangSo;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.DinhDangNgay;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyNhapHang extends javax.swing.JPanel {

    private final DonNhapHangBUS donNhapHangBUS;
    private final ChiTietDonNhapHangBUS chiTietDonNhapHangBUS;
    private TableDesign tableDesign;

    public GD_QuanLyNhapHang() {
        donNhapHangBUS = new DonNhapHangBUS();
        chiTietDonNhapHangBUS = new ChiTietDonNhapHangBUS();
        initComponents();
        setUIManager();
        fillTable();
    }

    private void setUIManager() {
        txtOrderId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã đơn nhập");
        UIManager.put("Button.arc", 10);
        jDateFrom.setDate(Date.valueOf(LocalDate.now()));
        jDateTo.setDate(Date.valueOf(LocalDate.now()));

        // Style cho button Xem chi tiết - màu xanh nước biển, kích thước nhỏ
        ButtonStyles.apply(btnView, ButtonStyles.Type.INFO);
    }

    private void fillTable() {
        String[] headers = {"Mã đơn nhập", "Ngày nhập", "Nhà cung cấp", "Nhân viên", "Thành tiền"};
        List<Integer> tableWidths = Arrays.asList(200, 200, 250, 250, 200);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        List<DonNhapHang> danhSach = donNhapHangBUS.layTatCaDonNhapHang();
        fillContent(danhSach);
    }

    private void fillContent(List<DonNhapHang> danhSach) {
        tableDesign.getModelTable().setRowCount(0);
        for (DonNhapHang dnh : danhSach) {
            String tenNCC = dnh.getNhaCungCap() != null ? dnh.getNhaCungCap().getMaNhaCungCap() : "N/A";
            String tenNV = dnh.getNhanVien() != null ? dnh.getNhanVien().getMaNhanVien() : "N/A";

            tableDesign.getModelTable().addRow(new Object[]{
                dnh.getMaDonNhapHang(),
                formatDate(dnh.getNgayNhap()),
                tenNCC,
                tenNV,
                formatToVND(dnh.getThanhTien())
            });
        }
    }

    private List<DonNhapHang> searchDonNhapHang(LocalDate dateFrom, LocalDate dateTo, String maDonNhap) {
        List<DonNhapHang> all = donNhapHangBUS.layTatCaDonNhapHang();
        List<DonNhapHang> result = new ArrayList<>();

        for (DonNhapHang dnh : all) {
            LocalDate ngayNhap = dnh.getNgayNhap();

            // Lọc theo ngày
            if (ngayNhap.isBefore(dateFrom) || ngayNhap.isAfter(dateTo)) {
                continue;
            }

            // Lọc theo mã đơn nhập
            if (!maDonNhap.isEmpty()) {
                String maDN = dnh.getMaDonNhapHang() != null ? dnh.getMaDonNhapHang() : "";
                if (!maDN.toLowerCase().contains(maDonNhap.toLowerCase())) {
                    continue;
                }
            }

            result.add(dnh);
        }

        return result;
    }

    // Helper methods for formatting
    private String formatDate(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private String formatToVND(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
        return formatter.format(amount);
    }

    private void exportToExcel() {
        boolean success = vn.edu.iuh.fit.iuhpharmacitymanagement.util.XuatFileExcel.xuatExcelVoiDialogVaTieuDe(
                tableDesign.getTable(),
                "DanhSachNhapHang",
                "Nhập Hàng",
                "DANH SÁCH THÔNG TIN NHẬP HÀNG"
        );
        if (success) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file Excel thành công!");
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Xuất file Excel thất bại!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modalOrderDetail = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        scrollTableDetail = new javax.swing.JScrollPane();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtOrderId = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        txtOrder = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();

        modalOrderDetail.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalOrderDetail.setTitle("Chi tiết hóa đơn");
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

        txtOrderId.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtOrderId.setMinimumSize(new java.awt.Dimension(300, 40));
        txtOrderId.setPreferredSize(new java.awt.Dimension(300, 40));

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

        txtOrder.setBackground(new java.awt.Color(115, 165, 71));
        txtOrder.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtOrder.setForeground(new java.awt.Color(255, 255, 255));
        txtOrder.setText("Xuất excel");
        txtOrder.setMaximumSize(new java.awt.Dimension(150, 40));
        txtOrder.setMinimumSize(new java.awt.Dimension(150, 40));
        txtOrder.setPreferredSize(new java.awt.Dimension(150, 40));
        txtOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrderActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        pnAll.add(headerPanel, java.awt.BorderLayout.NORTH);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMinimumSize(new java.awt.Dimension(1226, 174));
        jPanel4.setPreferredSize(new java.awt.Dimension(1226, 174));
        jPanel4.setLayout(new java.awt.BorderLayout());

        // Thêm tiêu đề "DANH SÁCH THÔNG TIN ĐƠN NHẬP HÀNG"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN ĐƠN NHẬP HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng
        titlePanel.add(lblTitle);
        jPanel4.add(titlePanel, java.awt.BorderLayout.NORTH);

        jPanel4.add(scrollTable, java.awt.BorderLayout.CENTER);

        pnAll.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(pnAll);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        java.util.Date date1 = jDateFrom.getDate();
        java.util.Date date2 = jDateTo.getDate();

        if (date1 == null || date2 == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn ngày bắt đầu và ngày kết thúc");
            return;
        }

        LocalDate localDateStart = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateEnd = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (localDateStart.isAfter(localDateEnd)) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Ngày bắt đầu phải trước ngày kết thúc");
            return;
        }

        String maDonNhap = txtOrderId.getText().trim();

        List<DonNhapHang> danhSach = searchDonNhapHang(localDateStart, localDateEnd, maDonNhap);
        fillContent(danhSach);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderActionPerformed
        exportToExcel();
    }//GEN-LAST:event_txtOrderActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn đơn nhập hàng cần xem chi tiết!");
        } else {
            String maDonNhap = (String) table.getValueAt(selectedRow, 0);
            showChiTietDonNhapHangDialog(maDonNhap);
        }
    }//GEN-LAST:event_btnViewActionPerformed

    /**
     * Hiển thị dialog chi tiết đơn nhập hàng với đầy đủ thông tin
     */
    private void showChiTietDonNhapHangDialog(String maDonNhap) {
        try {
            // Lấy thông tin đơn nhập hàng
            DonNhapHang donNhapHang = donNhapHangBUS.layDonNhapHangTheoMa(maDonNhap);
            if (donNhapHang == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không tìm thấy đơn nhập hàng!");
                return;
            }

            // Lấy chi tiết đơn nhập hàng từ BUS (load explicit từ database)
            List<ChiTietDonNhapHang> chiTietList = chiTietDonNhapHangBUS.layChiTietDonNhapHangTheoMaDonNhapHang(maDonNhap);

            // Tạo dialog mới
            javax.swing.JDialog dialog = new javax.swing.JDialog();
            dialog.setTitle("Chi tiết đơn nhập hàng - " + maDonNhap);
            dialog.setSize(1200, 700);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);

            // Main panel
            javax.swing.JPanel mainPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
            mainPanel.setBackground(java.awt.Color.WHITE);
            mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // ===================== HEADER PANEL =====================
            javax.swing.JPanel headerPanel = new javax.swing.JPanel();
            headerPanel.setLayout(new java.awt.GridLayout(0, 2, 20, 10));
            headerPanel.setBackground(java.awt.Color.WHITE);
            headerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createTitledBorder(
                            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(23, 162, 184), 2),
                            "THÔNG TIN ĐƠN NHẬP HÀNG",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14),
                            new java.awt.Color(23, 162, 184)
                    ),
                    javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));

            // Thêm thông tin header
            addInfoField(headerPanel, "Mã đơn nhập:", donNhapHang.getMaDonNhapHang());
            addInfoField(headerPanel, "Ngày nhập:", DinhDangNgay.dinhDangNgay(donNhapHang.getNgayNhap()));

            String tenNCC = "N/A";
            if (donNhapHang.getNhaCungCap() != null) {
                tenNCC = donNhapHang.getNhaCungCap().getTenNhaCungCap() != null
                        ? donNhapHang.getNhaCungCap().getTenNhaCungCap()
                        : donNhapHang.getNhaCungCap().getMaNhaCungCap();
            }
            addInfoField(headerPanel, "Nhà cung cấp:", tenNCC);

            String tenNV = "N/A";
            if (donNhapHang.getNhanVien() != null) {
                tenNV = donNhapHang.getNhanVien().getTenNhanVien() != null
                        ? donNhapHang.getNhanVien().getTenNhanVien()
                        : donNhapHang.getNhanVien().getMaNhanVien();
            }
            addInfoField(headerPanel, "Nhân viên:", tenNV);

            mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

            // ===================== TABLE CHI TIẾT =====================
            String[] headers = {"STT", "Mã lô hàng", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            List<Integer> tableWidths = Arrays.asList(50, 150, 300, 100, 150, 150);
            TableDesign tableDetail = new TableDesign(headers, tableWidths);

            // Thêm dữ liệu vào bảng
            int stt = 1;
            if (chiTietList != null && !chiTietList.isEmpty()) {
                for (ChiTietDonNhapHang ct : chiTietList) {
                    String maLoHang = ct.getLoHang() != null ? ct.getLoHang().getMaLoHang() : "N/A";
                    String tenSanPham = "N/A";

                    if (ct.getLoHang() != null && ct.getLoHang().getSanPham() != null) {
                        tenSanPham = ct.getLoHang().getSanPham().getTenSanPham();
                    }

                    tableDetail.getModelTable().addRow(new Object[]{
                        stt++,
                        maLoHang,
                        tenSanPham,
                        ct.getSoLuong(),
                        DinhDangSo.dinhDangTien(ct.getDonGia()),
                        DinhDangSo.dinhDangTien(ct.getThanhTien())
                    });
                }
            }

            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(tableDetail.getTable());
            scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
            mainPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

            // ===================== BOTTOM PANEL (Tổng tiền + Button Đóng) =====================
            javax.swing.JPanel bottomPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
            bottomPanel.setBackground(java.awt.Color.WHITE);

            // Panel tổng tiền (bên trái)
            javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
            tongTienPanel.setBackground(java.awt.Color.WHITE);

            javax.swing.JLabel lblTongTienTitle = new javax.swing.JLabel("TỔNG TIỀN:");
            lblTongTienTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
            lblTongTienTitle.setForeground(new java.awt.Color(220, 53, 69));

            javax.swing.JLabel lblTongTienValue = new javax.swing.JLabel(DinhDangSo.dinhDangTien(donNhapHang.getThanhTien()));
            lblTongTienValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
            lblTongTienValue.setForeground(new java.awt.Color(220, 53, 69));

            tongTienPanel.add(lblTongTienTitle);
            tongTienPanel.add(lblTongTienValue);

            bottomPanel.add(tongTienPanel, java.awt.BorderLayout.WEST);

            // Panel button Đóng (bên phải)
            javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));
            buttonPanel.setBackground(java.awt.Color.WHITE);

            // Nút Đóng
            javax.swing.JButton btnDong = new javax.swing.JButton("ĐÓNG");
            btnDong.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            btnDong.setPreferredSize(new java.awt.Dimension(120, 40));
            btnDong.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:#6C757D;"
                    + "foreground:#FFFFFF;"
                    + "hoverBackground:#5A6268;"
                    + "pressedBackground:#545B62;"
                    + "arc:10;"
                    + "borderWidth:0");
            btnDong.addActionListener(e -> dialog.dispose());

            buttonPanel.add(btnDong);

            bottomPanel.add(buttonPanel, java.awt.BorderLayout.EAST);

            mainPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);

            // Thêm main panel vào dialog
            dialog.add(mainPanel);
            dialog.setVisible(true);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Lỗi khi hiển thị chi tiết đơn nhập hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method để thêm field thông tin
     */
    private void addInfoField(javax.swing.JPanel panel, String label, String value) {
        javax.swing.JPanel fieldPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        fieldPanel.setBackground(java.awt.Color.WHITE);

        javax.swing.JLabel lblLabel = new javax.swing.JLabel(label);
        lblLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblLabel.setPreferredSize(new java.awt.Dimension(140, 25));

        javax.swing.JLabel lblValue = new javax.swing.JLabel(value != null ? value : "N/A");
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        fieldPanel.add(lblLabel);
        fieldPanel.add(lblValue);

        panel.add(fieldPanel);
    }


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
    private javax.swing.JTextField txtOrderId;
    private javax.swing.JButton txtOrder;
    // End of variables declaration//GEN-END:variables

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhaphang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonNhapHangBUS;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
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
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.ResizeImage;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyNhapHang extends javax.swing.JPanel {

    private final DonNhapHangBUS donNhapHangBUS;
    private TableDesign tableDesign;
    private TableDesign tableDetail;

    public GD_QuanLyNhapHang() {
        donNhapHangBUS = new DonNhapHangBUS();
        initComponents();
        setUIManager();
        fillTable();
    }

    private void setUIManager() {
        txtEmp.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhân viên");
        txtCus.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhà cung cấp");
        UIManager.put("Button.arc", 10);
        jDateFrom.setDate(Date.valueOf(LocalDate.now()));
        jDateTo.setDate(Date.valueOf(LocalDate.now()));
        
        // Style cho button Xem chi tiết - màu xanh nước biển, kích thước nhỏ
        btnView.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#17A2B8;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#138496;"
                + "pressedBackground:#0F6674;"
                + "arc:10;"
                + "borderWidth:0");
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
    
    private List<DonNhapHang> searchDonNhapHang(LocalDate dateFrom, LocalDate dateTo, String tenNCC, String tenNV) {
        List<DonNhapHang> all = donNhapHangBUS.layTatCaDonNhapHang();
        List<DonNhapHang> result = new ArrayList<>();
        
        for (DonNhapHang dnh : all) {
            LocalDate ngayNhap = dnh.getNgayNhap();
            
            // Lọc theo ngày
            if (ngayNhap.isBefore(dateFrom) || ngayNhap.isAfter(dateTo)) {
                continue;
            }
            
            // Lọc theo nhà cung cấp
            if (!tenNCC.isEmpty()) {
                String maNCC = dnh.getNhaCungCap() != null ? dnh.getNhaCungCap().getMaNhaCungCap() : "";
                if (!maNCC.toLowerCase().contains(tenNCC.toLowerCase())) {
                    continue;
                }
            }
            
            // Lọc theo nhân viên
            if (!tenNV.isEmpty()) {
                String maNV = dnh.getNhanVien() != null ? dnh.getNhanVien().getMaNhanVien() : "";
                if (!maNV.toLowerCase().contains(tenNV.toLowerCase())) {
                    continue;
                }
            }
            
            result.add(dnh);
        }
        
        return result;
    }
    
    // Helper methods for formatting
    private String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
    
    private String formatToVND(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
        return formatter.format(amount);
    }
    
    private void exportToExcel() {
        Notifications.getInstance().show(Notifications.Type.INFO, "Chức năng xuất Excel đang được phát triển");
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
        txtCus = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        txtEmp = new javax.swing.JTextField();
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

        txtCus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCus.setMinimumSize(new java.awt.Dimension(300, 40));
        txtCus.setPreferredSize(new java.awt.Dimension(300, 40));

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

        txtEmp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmp.setMinimumSize(new java.awt.Dimension(300, 40));
        txtEmp.setPreferredSize(new java.awt.Dimension(300, 40));

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
                .addComponent(txtEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(txtCus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));
        jPanel4.add(scrollTable);

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
        
        String txtSupplier = txtCus.getText().trim();
        String txtEmployee = txtEmp.getText().trim();

        List<DonNhapHang> danhSach = searchDonNhapHang(localDateStart, localDateEnd, txtSupplier, txtEmployee);
        fillContent(danhSach);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderActionPerformed
        exportToExcel();
    }//GEN-LAST:event_txtOrderActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();
        if(selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn đơn nhập hàng cần xem chi tiết!");
        } else {
            String maDonNhap = (String) table.getValueAt(selectedRow, 0);

            String[] headers = {"Mã lô hàng", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            List<Integer> tableWidths = Arrays.asList(200, 300, 150, 200, 200);
            tableDetail = new TableDesign(headers, tableWidths);
            scrollTableDetail.setViewportView(tableDetail.getTable());
            scrollTableDetail.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

            DonNhapHang dnh = donNhapHangBUS.layDonNhapHangTheoMa(maDonNhap);
            List<ChiTietDonNhapHang> chiTietList = dnh.getChiTietDonNhapHang();

            // Xóa dữ liệu cũ trong bảng
            tableDetail.getModelTable().setRowCount(0);

            if (chiTietList != null && !chiTietList.isEmpty()) {
                for (ChiTietDonNhapHang ct : chiTietList) {
                    String maLoHang = ct.getLoHang() != null ? ct.getLoHang().getMaLoHang() : "N/A";
                    String tenSP = "N/A";
                    if (ct.getLoHang() != null && ct.getLoHang().getSanPham() != null) {
                        tenSP = ct.getLoHang().getSanPham().getTenSanPham();
                    }
                    
                    tableDetail.getModelTable().addRow(new Object[]{
                        maLoHang,
                        tenSP,
                        ct.getSoLuong(),
                        formatToVND(ct.getDonGia()),
                        formatToVND(ct.getThanhTien())
                    });
                }
            }

            modalOrderDetail.setLocationRelativeTo(null);
            modalOrderDetail.setVisible(true);
        } 
    }//GEN-LAST:event_btnViewActionPerformed


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
    private javax.swing.JTextField txtCus;
    private javax.swing.JTextField txtEmp;
    private javax.swing.JButton txtOrder;
    // End of variables declaration//GEN-END:variables

    

}

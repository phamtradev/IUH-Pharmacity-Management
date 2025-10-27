/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlythuchi;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.*;

/**
 *
 * @author daoducdanh
 */
public class GD_BaoCaoThuChi extends javax.swing.JPanel {

    private final DonHangBUS donHangBUS;
    private final DonNhapHangBUS donNhapHangBUS;
    private final DonTraHangBUS donTraHangBUS;
    private final HangHongBUS hangHongBUS;
    private final ChiTietDonHangBUS chiTietDonHangBUS;
    private final ChiTietHangHongBUS chiTietHangHongBUS;
    private final BaoCaoBUS baoCaoBUS;
    private final SanPhamBUS sanPhamBUS;
    private TableDesign tableDesign;
    private TableDesign tableDetail;

    public GD_BaoCaoThuChi() {
        donHangBUS = new DonHangBUS();
        donNhapHangBUS = new DonNhapHangBUS();
        donTraHangBUS = new DonTraHangBUS();
        hangHongBUS = new HangHongBUS();
        chiTietDonHangBUS = new ChiTietDonHangBUS();
        chiTietHangHongBUS = new ChiTietHangHongBUS();
        baoCaoBUS = new BaoCaoBUS();
        sanPhamBUS = new SanPhamBUS(new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO());
        initComponents();
        setUIManager();
        fillTable();
    }

    private void setUIManager() {
        UIManager.put("Button.arc", 10);
        jDateFrom.setDate(Date.valueOf(LocalDate.now()));
        jDateTo.setDate(Date.valueOf(LocalDate.now()));
        
        // Style cho button Xem chi tiết - màu xanh nước biển
        btnView.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, ""
                + "background:#17A2B8;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#138496;"
                + "pressedBackground:#0F6674;"
                + "arc:10;"
                + "borderWidth:0");
    }

    private void fillTable() {
        String[] headers = {"Mã phiếu", "Ngày lập", "Loại thu chi", "Người lập", "Giá trị"};
        List<Integer> tableWidths = Arrays.asList(240, 240, 240, 240, 240);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        BaoCaoBUS.BaoCaoThuChi r = baoCaoBUS.layBaoCaoTheoThoiGian(LocalDate.now(), LocalDate.now(), (String) comboboxType.getSelectedItem());
        fillContent(r);
    }

    private void fillContent(BaoCaoBUS.BaoCaoThuChi report) {
        tableDesign.getModelTable().setRowCount(0);
        report.getReports().forEach(obj -> {
            if (obj instanceof DonHang donHang) {
                tableDesign.getModelTable().addRow(new Object[]{
                    donHang.getMaDonHang(), 
                    DinhDangNgay.dinhDangNgay(donHang.getNgayDatHang()),
                    "Bán hàng", 
                    donHang.getNhanVien() != null ? donHang.getNhanVien().getMaNhanVien() : "N/A", 
                    DinhDangSo.dinhDangTien(donHang.getThanhTien())
                });
            } else if (obj instanceof DonTraHang donTraHang) {
                tableDesign.getModelTable().addRow(new Object[]{
                    donTraHang.getMaDonTraHang(), 
                    DinhDangNgay.dinhDangNgay(donTraHang.getngayTraHang()),
                    "Trả hàng", 
                    donTraHang.getNhanVien() != null ? donTraHang.getNhanVien().getMaNhanVien() : "N/A",
                    "-" + DinhDangSo.dinhDangTien(donTraHang.getThanhTien())
                });
            } else if (obj instanceof DonNhapHang donNhapHang) {
                tableDesign.getModelTable().addRow(new Object[]{
                    donNhapHang.getMaDonNhapHang(), 
                    DinhDangNgay.dinhDangNgay(donNhapHang.getNgayNhap()),
                    "Nhập hàng", 
                    donNhapHang.getNhanVien() != null ? donNhapHang.getNhanVien().getMaNhanVien() : "N/A",
                    "-" + DinhDangSo.dinhDangTien(donNhapHang.getThanhTien())
                });
            } else if (obj instanceof HangHong hangHong) {
                tableDesign.getModelTable().addRow(new Object[]{
                    hangHong.getMaHangHong(), 
                    DinhDangNgay.dinhDangNgay(hangHong.getNgayNhap()),
                    "Xuất hủy", 
                    hangHong.getNhanVien() != null ? hangHong.getNhanVien().getMaNhanVien() : "N/A",
                    "-" + DinhDangSo.dinhDangTien(hangHong.getThanhTien())
                });
            }
        });
        
        report.getOrderType().forEach((key, value) -> {
            if (!value.isEmpty()) {
                Integer qty = value.keySet().iterator().next();
                Double price = value.values().iterator().next();
                if (key.equals("Bán hàng")) {
                    orderQty.setText(qty.toString());
                    orderPrice.setText(DinhDangSo.dinhDangTien(price));
                } else if (key.equals("Trả hàng")) {
                    returnQty.setText(qty.toString());
                    if(qty > 0)
                        returnPrice.setText("-" + DinhDangSo.dinhDangTien(price));
                    else
                        returnPrice.setText(DinhDangSo.dinhDangTien(price));
                } else if (key.equals("Nhập hàng")) {
                    purchaseQty.setText(qty.toString());
                    if(qty > 0)
                        purchasePrice.setText("-" + DinhDangSo.dinhDangTien(price));
                    else
                        purchasePrice.setText(DinhDangSo.dinhDangTien(price));
                } else if (key.equals("Xuất hủy")) {
                    damageQty.setText(qty.toString());
                    if(qty > 0)
                        damagePrice.setText("-" + DinhDangSo.dinhDangTien(price));
                    else
                        damagePrice.setText(DinhDangSo.dinhDangTien(price));
                }
            }
        });
        
        String[] headers = {"Loại thu chi", "Số lượng", "Tổng giá trị"};
        List<Integer> tableWidths = Arrays.asList(200, 150, 200);
        TableDesign table = new TableDesign(headers, tableWidths);
        scrollDoanhSo.setViewportView(table.getTable());
        scrollDoanhSo.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        table.getModelTable().setRowCount(0);
        table.getModelTable().addRow(new Object[]{"Bán hàng", orderQty.getText(), orderPrice.getText()});
        table.getModelTable().addRow(new Object[]{"Nhập hàng", purchaseQty.getText(), purchasePrice.getText()});
        table.getModelTable().addRow(new Object[]{"Trả hàng", returnQty.getText(), returnPrice.getText()});
        table.getModelTable().addRow(new Object[]{"Xuất hủy", damageQty.getText(), damagePrice.getText()});

        txtTongDoanhThu.setText(DinhDangSo.dinhDangTien(report.getProfit()));
        profit.setText(DinhDangSo.dinhDangTien(report.getProfit()));
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
        modalDetail = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        scrollTableDetail = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        purchaseQty = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        orderQty = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        purchasePrice = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        orderPrice = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        returnQty = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        returnPrice = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        damageQty = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        damagePrice = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        profit = new javax.swing.JLabel();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        jDateTo = new com.toedter.calendar.JDateChooser();
        jDateFrom = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        txtOrder = new javax.swing.JButton();
        comboboxType = new javax.swing.JComboBox<>();
        btnView = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        scrollDoanhSo = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        lblTongDoanhThu = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );

        modalDetail.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalDetail.setTitle("Chi tiết phiếu nhập hàng");
        modalDetail.setMinimumSize(new java.awt.Dimension(1311, 700));
        modalDetail.setModal(true);
        modalDetail.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
        jPanel3.add(scrollTableDetail);

        javax.swing.GroupLayout modalDetailLayout = new javax.swing.GroupLayout(modalDetail.getContentPane());
        modalDetail.getContentPane().setLayout(modalDetailLayout);
        modalDetailLayout.setHorizontalGroup(
            modalDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        modalDetailLayout.setVerticalGroup(
            modalDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        jPanel2.setPreferredSize(new java.awt.Dimension(1416, 200));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Nhập hàng:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setText("SL:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Bán hàng:");

        purchaseQty.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        purchaseQty.setText("0");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("SL:");

        orderQty.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        orderQty.setText("0");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Tổng giá trị:");

        purchasePrice.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        purchasePrice.setText("0 đ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel10.setText("Tổng giá trị:");

        orderPrice.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        orderPrice.setText("0 đ");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Trả hàng:");

        returnQty.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        returnQty.setText("0");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("|");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel15.setText("Tổng giá trị:");

        returnPrice.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        returnPrice.setText("0 đ");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel17.setText("Xuất hủy:");

        damageQty.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        damageQty.setText("0");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Tổng giá trị:");

        damagePrice.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        damagePrice.setText("0 đ");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel21.setText("SL:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setText("|");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setText("|");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setText("SL:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("|");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel26.setText("Tổng lợi nhuận:");

        profit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        profit.setText("0 đ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(orderQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(purchaseQty, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(orderPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(purchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel21)
                                        .addGap(18, 18, 18)
                                        .addComponent(damageQty, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel25)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel19)
                                        .addGap(18, 18, 18)
                                        .addComponent(damagePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel24)
                                        .addGap(18, 18, 18)
                                        .addComponent(returnQty, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel14)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(returnPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(305, 305, 305))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(profit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(purchaseQty)
                    .addComponent(jLabel8)
                    .addComponent(purchasePrice)
                    .addComponent(returnQty)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel22)
                    .addComponent(jLabel12)
                    .addComponent(jLabel24)
                    .addComponent(returnPrice))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(orderQty)
                    .addComponent(jLabel10)
                    .addComponent(orderPrice)
                    .addComponent(jLabel23)
                    .addComponent(jLabel17)
                    .addComponent(jLabel21)
                    .addComponent(damageQty)
                    .addComponent(jLabel25)
                    .addComponent(jLabel19)
                    .addComponent(damagePrice))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(profit))
                .addContainerGap(36, Short.MAX_VALUE))
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

        btnSearch.setBackground(new java.awt.Color(115, 165, 71));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Tra cứu");
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

        comboboxType.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        comboboxType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Bán hàng", "Trả hàng", "Nhập hàng", "Xuất hủy" }));

        btnView.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnView.setText("XEM CHI TIẾT");
        btnView.setBorder(null);
        btnView.setBorderPainted(false);
        btnView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnView.setFocusPainted(false);
        btnView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnView.setPreferredSize(new java.awt.Dimension(100, 90));
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
                .addComponent(btnView, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(comboboxType, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboboxType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        headerPanel.add(jPanel5, java.awt.BorderLayout.CENTER);

        pnAll.add(headerPanel, java.awt.BorderLayout.NORTH);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setMinimumSize(new java.awt.Dimension(1226, 174));
        jPanel4.setPreferredSize(new java.awt.Dimension(1226, 174));
        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel4.add(scrollTable, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMaximumSize(new java.awt.Dimension(1416, 370));
        jPanel6.setMinimumSize(new java.awt.Dimension(1416, 370));
        jPanel6.setPreferredSize(new java.awt.Dimension(1416, 370));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel7.setMaximumSize(new java.awt.Dimension(1416, 300));
        jPanel7.setMinimumSize(new java.awt.Dimension(1416, 300));
        jPanel7.setPreferredSize(new java.awt.Dimension(1416, 300));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        scrollDoanhSo.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.add(scrollDoanhSo);

        jPanel6.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 20, 20);
        flowLayout1.setAlignOnBaseline(true);
        jPanel8.setLayout(flowLayout1);

        lblTongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTongDoanhThu.setForeground(new java.awt.Color(255, 0, 0));
        lblTongDoanhThu.setText("Tổng doanh thu:");
        jPanel8.add(lblTongDoanhThu);

        txtTongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        txtTongDoanhThu.setForeground(new java.awt.Color(255, 0, 0));
        txtTongDoanhThu.setText("0 đ");
        jPanel8.add(txtTongDoanhThu);

        jPanel6.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel6, java.awt.BorderLayout.PAGE_START);
        jPanel4.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        pnAll.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(pnAll);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
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
        BaoCaoBUS.BaoCaoThuChi report = baoCaoBUS.layBaoCaoTheoThoiGian(localDateStart, localDateEnd, (String) comboboxType.getSelectedItem());
        fillContent(report);
        Notifications.getInstance().show(Notifications.Type.SUCCESS, "Tra cứu báo cáo thu chi thành công!");
    }//GEN-LAST:event_btnSearchActionPerformed

    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Tạo font chữ
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);  // Sử dụng setBold thay vì setBoldweight
        font.setFontHeightInPoints((short) 14);

        // Tạo CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        return cellStyle;
    }
    
    private void openFile(String filePath) {
        try {
            java.io.File file = new java.io.File(filePath);
            if (file.exists()) {
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể mở file tự động!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi mở file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void txtOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrderActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn lưu file Excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                TableModel model = tableDesign.getTable().getModel();
                Workbook workbook;

                // Khởi tạo workbook cho định dạng xlsx
                if (filePath.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook();
                } else {
                    workbook = new HSSFWorkbook();
                }

                Sheet sheet = workbook.createSheet("Sheet1");

                // Tạo dòng tiêu đề
                CellStyle cellStyle = createStyleForHeader(sheet);
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell headerCell = headerRow.createCell(i);
                    headerCell.setCellValue(model.getColumnName(i));
                    headerCell.setCellStyle(cellStyle);
                }

                int r = 0;
                // Tạo dòng dữ liệu
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row dataRow = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell dataCell = dataRow.createCell(j);
                        Object value = model.getValueAt(i, j);
                        if (value != null) {
                            dataCell.setCellValue(value.toString());
                        }
                    }
                    r = i;
                }
                
                Row dataRowBan = sheet.createRow(r + 1);
                Cell dataCellBan = dataRowBan.createCell(0);
                Cell dataCellBan1 = dataRowBan.createCell(1);
                Cell dataCellBan2 = dataRowBan.createCell(2);
                dataCellBan.setCellValue("Bán hàng:");
                dataCellBan1.setCellValue("Số lượng: " + orderQty.getText());
                dataCellBan2.setCellValue("Tổng giá trị:" + orderPrice.getText());
                
                Row dataRowNhap = sheet.createRow(r + 2);
                Cell dataCellNhap = dataRowNhap.createCell(0);
                Cell dataCellNhap1 = dataRowNhap.createCell(1);
                Cell dataCellNhap2 = dataRowNhap.createCell(2);
                dataCellNhap.setCellValue("Nhập hàng:");
                dataCellNhap1.setCellValue("Số lượng: " + purchaseQty.getText());
                dataCellNhap2.setCellValue("Tổng giá trị:" + purchasePrice.getText());
                
                Row dataRowTra = sheet.createRow(r + 3);
                Cell dataCellTra = dataRowTra.createCell(0);
                Cell dataCellTra1 = dataRowTra.createCell(1);
                Cell dataCellTra2 = dataRowTra.createCell(2);
                dataCellTra.setCellValue("Trả hàng:");
                dataCellTra1.setCellValue("Số lượng: " + returnQty.getText());
                dataCellTra2.setCellValue("Tổng giá trị:" + returnPrice.getText());
                
                Row dataRowHuy = sheet.createRow(r + 4);
                Cell dataCellHuy = dataRowHuy.createCell(0);
                Cell dataCellHuy1 = dataRowHuy.createCell(1);
                Cell dataCellHuy2 = dataRowHuy.createCell(2);
                dataCellHuy.setCellValue("Xuất hủy:");
                dataCellHuy1.setCellValue("Số lượng: " + damageQty.getText());
                dataCellHuy2.setCellValue("Tổng giá trị:" + damagePrice.getText());
                
                Row dataRowTong = sheet.createRow(r + 5);
                Cell dataCellTong = dataRowTong.createCell(0);
                dataCellTong.setCellValue("Tổng lợi nhuận: " + profit.getText());

                // Điều chỉnh kích thước cột
                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Ghi dữ liệu ra file
                try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    workbook.write(fileOut);
                } finally {
                    workbook.close();
                }

                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file Excel thành công!");
                openFile(filePath);
            } catch (IOException ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi xuất file Excel: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_txtOrderActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();
        if(selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn phiếu cần xem chi tiết!");
            return;
        }
        String loaiPhieu = (String) table.getValueAt(selectedRow, 2);
        String maPhieu = (String) table.getValueAt(selectedRow, 0);

        if(loaiPhieu.equalsIgnoreCase("Bán hàng")) {
            String[] headers = {"Mã lô hàng", "Tên sản phẩm", "Số lượng", "Đơn giá", "Giảm giá", "Thành tiền"};
            List<Integer> tableWidths = Arrays.asList(150, 300, 100, 150, 100, 150);
            tableDetail = new TableDesign(headers, tableWidths);
            scrollTableDetail.setViewportView(tableDetail.getTable());
            scrollTableDetail.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

            List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangBUS.layDanhSachChiTietTheoMaDonHang(maPhieu);

            // Xóa dữ liệu cũ trong bảng
            tableDetail.getModelTable().setRowCount(0);

            // Thêm chi tiết đơn hàng vào bảng
            for (ChiTietDonHang ct : chiTietDonHangs) {
                String maLoHang = ct.getLoHang() != null ? ct.getLoHang().getMaLoHang() : "";
                String tenSanPham = "N/A";
                if (ct.getLoHang() != null && ct.getLoHang().getSanPham() != null) {
                    tenSanPham = ct.getLoHang().getSanPham().getTenSanPham();
                }

                tableDetail.getModelTable().addRow(new Object[]{
                    maLoHang,
                    tenSanPham,
                    ct.getSoLuong(),
                    DinhDangSo.dinhDangTien(ct.getDonGia()),
                    DinhDangSo.dinhDangPhanTramTrucTiep(ct.getGiamGia() * 100),
                    DinhDangSo.dinhDangTien(ct.getThanhTien())
                });
            }
        }

        if(loaiPhieu.equalsIgnoreCase("Trả hàng")) {
            String[] headers = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng trả", "Đơn giá", "Thành tiền", "Lý do trả"};
            List<Integer> tableWidths = Arrays.asList(150, 250, 120, 150, 150, 250);
            tableDetail = new TableDesign(headers, tableWidths);
            scrollTableDetail.setViewportView(tableDetail.getTable());
            scrollTableDetail.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

            DonTraHang dth = donTraHangBUS.layDonTraHangTheoMa(maPhieu);
            List<ChiTietDonTraHang> chiTietList = dth.getChiTietDonTraHang();

            // Xóa dữ liệu cũ trong bảng
            tableDetail.getModelTable().setRowCount(0);

            if (chiTietList != null && !chiTietList.isEmpty()) {
                for (ChiTietDonTraHang ct : chiTietList) {
                    String maSanPham = ct.getSanPham() != null ? ct.getSanPham().getMaSanPham() : "N/A";
                    String tenSanPham = "N/A";
                    if (ct.getSanPham() != null) {
                        SanPham sp = sanPhamBUS.laySanPhamTheoMa(maSanPham);
                        if (sp != null) {
                            tenSanPham = sp.getTenSanPham();
                        }
                    }
                    
                    tableDetail.getModelTable().addRow(new Object[]{
                        maSanPham,
                        tenSanPham,
                        ct.getSoLuong(),
                        DinhDangSo.dinhDangTien(ct.getDonGia()),
                        DinhDangSo.dinhDangTien(ct.getThanhTien()),
                        ct.getLyDoTra()
                    });
                }
            }
        }

        if(loaiPhieu.equalsIgnoreCase("Nhập hàng")) {
            String[] headers = {"Mã lô hàng", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            List<Integer> tableWidths = Arrays.asList(200, 300, 150, 200, 200);
            tableDetail = new TableDesign(headers, tableWidths);
            scrollTableDetail.setViewportView(tableDetail.getTable());
            scrollTableDetail.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

            DonNhapHang dnh = donNhapHangBUS.layDonNhapHangTheoMa(maPhieu);
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
                        DinhDangSo.dinhDangTien(ct.getDonGia()),
                        DinhDangSo.dinhDangTien(ct.getThanhTien())
                    });
                }
            }
        }

        if(loaiPhieu.equalsIgnoreCase("Xuất hủy")) {
            String[] headers = {"Mã lô hàng", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
            List<Integer> tableWidths = Arrays.asList(200, 300, 150, 200, 200);
            tableDetail = new TableDesign(headers, tableWidths);
            scrollTableDetail.setViewportView(tableDetail.getTable());
            scrollTableDetail.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

            HangHong hh = hangHongBUS.layHangHongTheoMa(maPhieu);
            List<ChiTietHangHong> chiTietList = hh.getChiTietHangHong();

            // Xóa dữ liệu cũ trong bảng
            tableDetail.getModelTable().setRowCount(0);

            if (chiTietList != null && !chiTietList.isEmpty()) {
                for (ChiTietHangHong ct : chiTietList) {
                    String maLoHang = ct.getLoHang() != null ? ct.getLoHang().getMaLoHang() : "N/A";
                    String tenSP = "N/A";
                    if (ct.getLoHang() != null && ct.getLoHang().getSanPham() != null) {
                        tenSP = ct.getLoHang().getSanPham().getTenSanPham();
                    }
                    
                    tableDetail.getModelTable().addRow(new Object[]{
                        maLoHang,
                        tenSP,
                        ct.getSoLuong(),
                        DinhDangSo.dinhDangTien(ct.getDonGia()),
                        DinhDangSo.dinhDangTien(ct.getThanhTien())
                    });
                }
            }
        }

        modalDetail.setLocationRelativeTo(null);
        modalDetail.setVisible(true);
    }//GEN-LAST:event_btnViewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox<String> comboboxType;
    private javax.swing.JLabel damagePrice;
    private javax.swing.JLabel damageQty;
    private javax.swing.JPanel headerPanel;
    private com.toedter.calendar.JDateChooser jDateFrom;
    private com.toedter.calendar.JDateChooser jDateTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblTongDoanhThu;
    private javax.swing.JDialog modalDetail;
    private javax.swing.JLabel orderPrice;
    private javax.swing.JLabel orderQty;
    private javax.swing.JPanel pnAll;
    private javax.swing.JLabel profit;
    private javax.swing.JLabel purchasePrice;
    private javax.swing.JLabel purchaseQty;
    private javax.swing.JLabel returnPrice;
    private javax.swing.JLabel returnQty;
    private javax.swing.JScrollPane scrollDoanhSo;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JScrollPane scrollTableDetail;
    private javax.swing.JButton txtOrder;
    private javax.swing.JLabel txtTongDoanhThu;
    // End of variables declaration//GEN-END:variables

}

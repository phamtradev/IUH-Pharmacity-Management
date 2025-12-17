/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang;

import com.formdev.flatlaf.FlatClientProperties;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import raven.toast.Notifications;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhaCungCapBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhanVienBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonViTinhBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyPhieuNhapHang extends javax.swing.JPanel {

    private SanPhamBUS sanPhamBUS;
    private SanPhamDAO sanPhamDAO;
    private NhaCungCapBUS nhaCungCapBUS;
    private DonNhapHangBUS donNhapHangBUS;
    private ChiTietDonNhapHangBUS chiTietDonNhapHangBUS;
    private DonViTinhBUS donViTinhBUS;
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    private NhanVien nhanVienHienTai; // Nhân viên đang đăng nhập
    private NhaCungCap nhaCungCapHienTai; // Nhà cung cấp được chọn/load từ Excel

    // Key gom nhóm dòng Excel: 1 lô = 1 (SĐK, HSD, Giá nhập) - KHÔNG phụ thuộc tên lô trong Excel
    private static class LoKey {

        private final String soDangKy;
        private final LocalDate hanSuDung;
        private final double donGia;

        public LoKey(String soDangKy, LocalDate hanSuDung, double donGia) {
            this.soDangKy = soDangKy != null ? soDangKy.trim() : "";
            this.hanSuDung = hanSuDung;
            this.donGia = donGia;
        }

        public String getSoDangKy() {
            return soDangKy;
        }

        public LocalDate getHanSuDung() {
            return hanSuDung;
        }

        public double getDonGia() {
            return donGia;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof LoKey)) {
                return false;
            }
            LoKey other = (LoKey) o;
            return Double.compare(other.donGia, donGia) == 0
                    && java.util.Objects.equals(soDangKy, other.soDangKy)
                    && java.util.Objects.equals(hanSuDung, other.hanSuDung);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(soDangKy, hanSuDung, donGia);
        }
    }

    public GD_QuanLyPhieuNhapHang() {
        sanPhamDAO = new SanPhamDAO();
        sanPhamBUS = new SanPhamBUS(sanPhamDAO);
        donNhapHangBUS = new DonNhapHangBUS();
        chiTietDonNhapHangBUS = new ChiTietDonNhapHangBUS();
        donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
        currencyFormat = new DecimalFormat("#,###");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // TODO: Lấy nhân viên hiện tại từ session/login
        // Tạm thời lấy nhân viên đầu tiên từ DB
        try {
            NhanVienBUS nhanVienBUS = new NhanVienBUS();
            List<NhanVien> danhSachNV = nhanVienBUS.layTatCaNhanVien();
            if (danhSachNV != null && !danhSachNV.isEmpty()) {
                nhanVienHienTai = danhSachNV.get(0);
            } else {
                throw new RuntimeException("Không tìm thấy nhân viên nào trong hệ thống!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi lấy thông tin nhân viên: " + e.getMessage());
            throw new RuntimeException(e);
        }

        initComponents();
        applyButtonStyles();
        lookAndFeelSet();
        setupPanelSanPham();
        setupSupplierSearchListener();

        txtSearchProduct.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí hoặc quét mã vạch");
        txtSearchSupplier.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại nhà cung cấp");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnSanPham = new javax.swing.JPanel();
        pnLeft = new javax.swing.JPanel();
        btnConfirmPurchase = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblSupplierId = new javax.swing.JLabel();
        txtSupplierId = new javax.swing.JTextField();
        txtSearchSupplier = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        lblSupplierId1 = new javax.swing.JLabel();
        txtSupplierName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        lblSupplierId2 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JTextField();
        headerPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtSearchProduct = new javax.swing.JTextField();
        btnMa = new javax.swing.JButton();
        btnImportExcel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnMid.setMinimumSize(new java.awt.Dimension(200, 200));
        pnMid.setOpaque(false);
        pnMid.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pnSanPham = new javax.swing.JPanel();
        pnSanPham.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(pnSanPham);

        javax.swing.GroupLayout pnMidLayout = new javax.swing.GroupLayout(pnMid);
        pnMid.setLayout(pnMidLayout);
        pnMidLayout.setHorizontalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1433, Short.MAX_VALUE)
        );
        pnMidLayout.setVerticalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );

        add(pnMid, java.awt.BorderLayout.CENTER);

        pnLeft.setBackground(new java.awt.Color(255, 255, 255));
        pnLeft.setPreferredSize(new java.awt.Dimension(100, 120));

        btnConfirmPurchase.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnConfirmPurchase.setText("Nhập hàng");
        btnConfirmPurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmPurchaseActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lblSupplierId.setFont(lblSupplierId.getFont().deriveFont(lblSupplierId.getFont().getStyle() | java.awt.Font.BOLD, lblSupplierId.getFont().getSize()+2));
        lblSupplierId.setText("Mã nhà cung cấp:");

        txtSupplierId.setEditable(false);
        txtSupplierId.setFont(txtSupplierId.getFont().deriveFont(txtSupplierId.getFont().getSize()+3f));
        txtSupplierId.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupplierId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierId, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtSupplierId)
                        .addContainerGap())))
        );

        txtSearchSupplier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchSupplierActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        lblSupplierId1.setFont(lblSupplierId1.getFont().deriveFont(lblSupplierId1.getFont().getStyle() | java.awt.Font.BOLD, lblSupplierId1.getFont().getSize()+2));
        lblSupplierId1.setText("Tên nhà cung cấp:");

        txtSupplierName.setEditable(false);
        txtSupplierName.setFont(txtSupplierName.getFont().deriveFont(txtSupplierName.getFont().getSize()+3f));
        txtSupplierName.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupplierId1)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(txtSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierId1, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(txtSupplierName)
                        .addContainerGap())))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        lblSupplierId2.setFont(lblSupplierId2.getFont().deriveFont(lblSupplierId2.getFont().getStyle() | java.awt.Font.BOLD, lblSupplierId2.getFont().getSize()+2));
        lblSupplierId2.setText("Tổng tiền:");

        txtTotalPrice.setEditable(false);
        txtTotalPrice.setFont(txtTotalPrice.getFont().deriveFont(txtTotalPrice.getFont().getSize()+3f));
        txtTotalPrice.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSupplierId2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierId2, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTotalPrice)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(txtSearchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConfirmPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchSupplier)
                    .addComponent(btnConfirmPurchase, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(pnLeft, java.awt.BorderLayout.SOUTH);

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));

        txtSearchProduct.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchProductActionPerformed(evt);
            }
        });
        txtSearchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchProductKeyPressed(evt);
            }
        });

        btnMa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMa.setText("Nhập");
        btnMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaActionPerformed(evt);
            }
        });

        btnImportExcel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnImportExcel.setText("Import Excel");
        btnImportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(txtSearchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnImportExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearchProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        headerPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchProductActionPerformed
        btnMaActionPerformed(evt); // Nhấn Enter sẽ thực hiện chức năng "Nhập"
    }//GEN-LAST:event_txtSearchProductActionPerformed

    private void btnMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaActionPerformed
        String textTim = txtSearchProduct.getText().trim();
        if (textTim.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Vui lòng nhập mã sản phẩm hoặc quét mã vạch!");
            return;
        }

        // Tìm sản phẩm theo mã hoặc số đăng ký
        SanPham sanPham = null;
        try {
            // Thử tìm theo mã sản phẩm
            sanPham = sanPhamBUS.laySanPhamTheoMa(textTim);
        } catch (Exception e) {
            // Nếu không tìm thấy theo mã, thử tìm theo số đăng ký
            var optional = sanPhamBUS.timSanPhamTheoSoDangKy(textTim);
            if (optional.isPresent()) {
                sanPham = optional.get();
            }
        }

        if (sanPham == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Không tìm thấy sản phẩm với mã: " + textTim);
            return;
        }

        // ═══════════════════════════════════════════════════════════════════
        // KIỂM TRA BUSINESS RULE: Số đăng ký chỉ được nhập bởi 1 nhà cung cấp
        // ═══════════════════════════════════════════════════════════════════
        if (nhaCungCapHienTai != null && nhaCungCapHienTai.getMaNhaCungCap() != null) {
            String soDangKy = sanPham.getSoDangKy();
            String maNCC = nhaCungCapHienTai.getMaNhaCungCap();

            boolean coTheNhap = sanPhamBUS.kiemTraNhaCungCapCoTheNhapSoDangKy(soDangKy, maNCC);

            if (!coTheNhap) {
                // Lấy mã NCC đã nhập để hiển thị
                String nccDaNhap = sanPhamBUS.layTenNhaCungCapDaNhapSoDangKy(soDangKy);
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "❌ KHÔNG THỂ THÊM!\n"
                        + "Sản phẩm '" + sanPham.getTenSanPham() + "' (SDK: " + soDangKy + ")\n"
                        + "đã được nhập bởi nhà cung cấp " + nccDaNhap + ".\n"
                        + "Không thể nhập từ nhà cung cấp khác!");
                return;
            }
        } else {
            // Chưa có nhà cung cấp → Cảnh báo
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "⚠ Vui lòng chọn/import nhà cung cấp trước khi thêm sản phẩm!");
            return;
        }

        // Thêm sản phẩm vào panel
        themSanPhamVaoPanelNhap(sanPham);
        txtSearchProduct.setText("");
        txtSearchProduct.requestFocus();
    }//GEN-LAST:event_btnMaActionPerformed

    private void btnImportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportExcelActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx", "xls"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            importFromExcel(file);
        }
    }//GEN-LAST:event_btnImportExcelActionPerformed

    private void applyButtonStyles() {
        ButtonStyles.apply(btnMa, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnImportExcel, ButtonStyles.Type.INFO);
        ButtonStyles.apply(btnConfirmPurchase, ButtonStyles.Type.PRIMARY);
    }

    private void setupPanelSanPham() {
        // Setup panel chứa danh sách sản phẩm
        pnSanPham.setLayout(new BoxLayout(pnSanPham, BoxLayout.Y_AXIS));
        pnSanPham.setBackground(Color.WHITE);

        // Tạo header cho danh sách sản phẩm
        javax.swing.JPanel headerPanel = createHeaderPanel();
        pnSanPham.add(headerPanel);
        pnSanPham.add(Box.createVerticalStrut(5));
    }

    /**
     * Thiết lập listener cho textfield tìm kiếm nhà cung cấp Tự động tìm và
     * hiển thị thông tin NCC khi nhập số điện thoại
     */
    private void setupSupplierSearchListener() {
        // Khởi tạo NhaCungCapBUS nếu chưa có
        if (nhaCungCapBUS == null) {
            nhaCungCapBUS = new NhaCungCapBUS();
        }

        // Thêm DocumentListener để tìm kiếm real-time
        txtSearchSupplier.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private javax.swing.Timer searchTimer;

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                scheduleSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                scheduleSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                scheduleSearch();
            }

            // Debounce: chờ người dùng gõ xong 300ms mới search
            private void scheduleSearch() {
                if (searchTimer != null) {
                    searchTimer.stop();
                }

                searchTimer = new javax.swing.Timer(300, evt -> {
                    searchSupplierByPhone();
                });
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
    }

    /**
     * Tìm kiếm nhà cung cấp theo số điện thoại Tự động điền mã và tên nhà cung
     * cấp khi tìm thấy
     */
    private void searchSupplierByPhone() {
        // Nếu ô nhập liệu bị khóa (đang mode Import E thì k chạy tìm kiếm để tránh việc nó tự xóa dữ liệu vừa điền
        if (!txtSearchSupplier.isEditable()) {
            return;
        }

        String soDienThoai = txtSearchSupplier.getText().trim();

        // Nếu rỗng → xóa thông tin hiện tại
        if (soDienThoai.isEmpty()) {
            txtSupplierId.setText("");
            txtSupplierName.setText("");
            nhaCungCapHienTai = null;
            return;
        }

        // Tìm nhà cung cấp theo số điện thoại
        try {
            NhaCungCap ncc = nhaCungCapBUS.layNhaCungCapTheoSoDienThoai(soDienThoai);

            if (ncc != null) {
                txtSupplierId.setText(ncc.getMaNhaCungCap() != null ? ncc.getMaNhaCungCap() : "");
                txtSupplierName.setText(ncc.getTenNhaCungCap() != null ? ncc.getTenNhaCungCap() : "");
                nhaCungCapHienTai = ncc;

                txtSearchSupplier.setBackground(new Color(220, 255, 220));
            } else {
                txtSupplierId.setText("");
                txtSupplierName.setText("");
                nhaCungCapHienTai = null;
                txtSearchSupplier.setBackground(new Color(255, 255, 200));
            }
        } catch (Exception e) {
            txtSupplierId.setText("");
            txtSupplierName.setText("");
            nhaCungCapHienTai = null;
            txtSearchSupplier.setBackground(Color.WHITE);
        }
    }

    private javax.swing.JPanel createHeaderPanel() {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagLayout headerLayout = (java.awt.GridBagLayout) panel.getLayout();
        headerLayout.columnWidths = Panel_ChiTietSanPhamNhap.COLUMN_MIN_WIDTHS.clone();
        headerLayout.columnWeights = Panel_ChiTietSanPhamNhap.COLUMN_WEIGHTS.clone();
        panel.setBackground(new Color(240, 240, 240));
        panel.setMaximumSize(new java.awt.Dimension(32767, 40));
        panel.setPreferredSize(new java.awt.Dimension(1000, 40));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(5, 8, 5, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // Headers phù hợp với từng cột hiển thị trên Panel_ChiTietSanPhamNhap
        String[] headers = {"Hình", "Tên sản phẩm", "Lô hàng / HSD", "Số lượng", "Đơn vị tính",
            "Đơn giá nhập", "Tạm tính", "CK (%)", "CK (đ)", "Thuế (%)", "Thuế (đ)", "Thành tiền", ""};
        double[] widths = Panel_ChiTietSanPhamNhap.COLUMN_WEIGHTS;
        int[] minWidths = Panel_ChiTietSanPhamNhap.COLUMN_MIN_WIDTHS;

        for (int i = 0; i < headers.length; i++) {
            javax.swing.JLabel label = new javax.swing.JLabel(headers[i]);
            label.setFont(new java.awt.Font("Segoe UI", 1, 13));
            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            label.setPreferredSize(new java.awt.Dimension(minWidths[i], 40));
            label.setMinimumSize(new java.awt.Dimension(minWidths[i], 40));

            gbc.gridx = i;
            gbc.weightx = widths[i];
            panel.add(label, gbc);
        }

        return panel;
    }

    private void themSanPhamVaoPanelNhap(SanPham sanPham) {
        Panel_ChiTietSanPhamNhap panelSP = new Panel_ChiTietSanPhamNhap(sanPham);

        // Listener để cập nhật tổng tiền
        panelSP.addPropertyChangeListener("tongTien", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTongTienHang();
            }
        });

        pnSanPham.add(panelSP);
        pnSanPham.add(Box.createVerticalStrut(2));
        pnSanPham.revalidate();
        pnSanPham.repaint();

        updateTongTienHang();
    }

    private void themSanPhamVaoPanelNhap(SanPham sanPham, int soLuong, double donGiaNhap, Date hanDung, String loHang,
            Double tyLeChietKhauExcel, Double thueGTGTExcel, DonViTinh donViTinhTuExcel) throws Exception {

        if (kiemTraSanPhamDaTonTai(sanPham.getMaSanPham())) {
            throw new Exception("Sản phẩm '" + sanPham.getTenSanPham() + "' đã có trong danh sách nhập");
        }

        // Lấy số điện thoại nhà cung cấp hiện tại
        String soDienThoaiNCC = (nhaCungCapHienTai != null) ? nhaCungCapHienTai.getSoDienThoai() : null;

        Panel_ChiTietSanPhamNhap panelSP = new Panel_ChiTietSanPhamNhap(
                sanPham,
                soLuong,
                donGiaNhap,
                hanDung,
                loHang,
                soDienThoaiNCC,
                tyLeChietKhauExcel,
                thueGTGTExcel,
                donViTinhTuExcel);

        // Listener để cập nhật tổng tiền
        panelSP.addPropertyChangeListener("tongTien", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTongTienHang();
            }
        });

        pnSanPham.add(panelSP);
        pnSanPham.add(Box.createVerticalStrut(2));
        pnSanPham.revalidate();
        pnSanPham.repaint();

        updateTongTienHang();
    }

    /**
     * Kiểm tra sản phẩm đã tồn tại trong panel chưa
     *
     * @param maSanPham Mã sản phẩm cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    private boolean kiemTraSanPhamDaTonTai(String maSanPham) {
        for (Component comp : pnSanPham.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamNhap) {
                Panel_ChiTietSanPhamNhap panel = (Panel_ChiTietSanPhamNhap) comp;
                if (panel.getSanPham().getMaSanPham().equals(maSanPham)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateTongTienHang() {
        double tongTien = 0;

        // Duyệt qua các panel chi tiết sản phẩm
        for (Component comp : pnSanPham.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPhamNhap) {
                Panel_ChiTietSanPhamNhap panel = (Panel_ChiTietSanPhamNhap) comp;
                tongTien += panel.getTongTien();
            }
        }

        txtTotalPrice.setText(currencyFormat.format(tongTien) + " đ");
    }

    private void lookAndFeelSet() {
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("Button.arc", 10);
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put("TabbedPane.tabHeight", 45);
        UIManager.put("ToggleButton.selectedBackground", new Color(81, 154, 244));
        UIManager.put("ToggleButton.selectedForeground", Color.WHITE);
    }

    private void importFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int errorCount = 0;
            boolean nhaCungCapDaTao = false; // Đánh dấu nhà cung cấp đã được tạo
            StringBuilder errors = new StringBuilder();

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "File Excel không có dòng tiêu đề!");
                return;
            }

            int colMaSP = -1, colSoLuong = -1, colDonViTinh = -1, colDonGia = -1,
                    colHanDung = -1, colLoHang = -1, colChietKhau = -1, colThueGTGT = -1;
            int colMaNCC = -1, colTenNCC = -1, colDiaChi = -1,
                    colSDT = -1, colEmail = -1, colMaSoThue = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) {
                    continue;
                }

                // Chuẩn hóa: chữ thường + bỏ dấu tiếng Việt (để "mã" thành "ma")
                String headerOrigin = getCellValueAsString(cell).trim().toLowerCase();
                String header = java.text.Normalizer.normalize(headerOrigin, java.text.Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "").replace("đ", "d");

                // 1. Check các cột định danh trước
                if (header.contains("so dang ky") || header.contains("sdk") || header.contains("ma sp")) {
                    colMaSP = i;
                } // 2. Mã số thuế (Check trước để không bị nhận nhầm là Thuế GTGT)
                else if (header.contains("ma") && header.contains("thue")) {
                    colMaSoThue = i;
                } // 3. Thuế GTGT (Chỉ nhận nếu không phải là Mã số thuế)
                else if (header.contains("thue") || header.contains("vat")) {
                    colThueGTGT = i;
                } // 4. Các cột còn lại
                else if (header.contains("so luong") || header.equals("sl")) {
                    colSoLuong = i;
                } else if (header.contains("don vi") || header.contains("dvt")) {
                    colDonViTinh = i;
                } else if (header.contains("don gia") || header.contains("gia nhap")) {
                    colDonGia = i;
                } else if (header.contains("han") && (header.contains("dung") || header.contains("su dung"))) {
                    colHanDung = i;
                } else if (header.contains("lo") && header.contains("hang")) {
                    colLoHang = i;
                } else if (header.contains("chiet khau") || header.equals("ck")) {
                    colChietKhau = i;
                } // 5. Thông tin NCC
                else if (header.contains("sdt") || header.contains("dien thoai")) {
                    colSDT = i;
                } else if (header.contains("ten") && header.contains("ncc")) {
                    colTenNCC = i;
                } else if (header.contains("ma") && header.contains("ncc")) {
                    colMaNCC = i;
                } else if (header.contains("dia chi")) {
                    colDiaChi = i;
                } else if (header.contains("email")) {
                    colEmail = i;
                }
            }

            if (colMaSP == -1 || colSoLuong == -1 || colDonGia == -1) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "File Excel thiếu các cột bắt buộc: Số đăng ký, Số lượng, Đơn giá nhập!");
                return;
            }

            NhaCungCap nhaCungCap = null;
            if (sheet.getLastRowNum() > 0) {
                Row firstDataRow = sheet.getRow(1);
                if (firstDataRow != null) {
                    try {
                        if (colTenNCC != -1) {
                            getCellValueAsString(firstDataRow.getCell(colTenNCC));
                        }
                        if (colSDT != -1) {
                            getCellValueAsString(firstDataRow.getCell(colSDT));
                        }
                        if (colDiaChi != -1) {
                            getCellValueAsString(firstDataRow.getCell(colDiaChi));
                        }
                        if (colEmail != -1) {
                            getCellValueAsString(firstDataRow.getCell(colEmail));
                        }

                        nhaCungCap = xuLyThongTinNhaCungCap(firstDataRow,
                                colMaNCC, colTenNCC, colDiaChi, colSDT, colEmail, colMaSoThue);
                        if (nhaCungCap != null) {
                            nhaCungCapDaTao = true;
                            nhaCungCapHienTai = nhaCungCap; // Lưu vào biến instance

                            // Điền thông tin mã và tên nhà cung cấp vào form
                            if (nhaCungCap.getMaNhaCungCap() != null) {
                                txtSupplierId.setText(nhaCungCap.getMaNhaCungCap());
                            } else {
                                txtSupplierId.setText("(chưa có - sẽ tự sinh)");
                            }
                            txtSupplierName.setText(nhaCungCap.getTenNhaCungCap());

                            //Thêm số điện thoại ncc và k cho sửa
                            txtSearchSupplier.setEditable(false);
                            if (nhaCungCap.getSoDienThoai() != null) {
                                txtSearchSupplier.setText(nhaCungCap.getSoDienThoai());
                            }
                        }
                    } catch (Exception ex) {
                        errors.append("Lỗi xử lý thông tin nhà cung cấp: ").append(ex.getMessage()).append("\n");
                    }
                }
            }

            // Gom nhóm các dòng Excel theo key (SĐK, HSD, Giá nhập) - KHÔNG phụ thuộc tên lô
            Map<LoKey, Integer> gopSoLuong = new HashMap<>();
            Map<LoKey, SanPham> sanPhamTheoKey = new HashMap<>();
            Map<LoKey, DonViTinh> donViTheoKey = new HashMap<>();
            Map<LoKey, String> tenLoTheoKey = new HashMap<>();
            Map<LoKey, Double> ckTheoKey = new HashMap<>();
            Map<LoKey, Double> thueTheoKey = new HashMap<>();

            // Đọc dữ liệu sản phẩm từ dòng 1 trở đi, validate và gom nhóm
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                try {
                    String maSP = getCellValueAsString(row.getCell(colMaSP));
                    if (maSP == null || maSP.isEmpty()) {
                        continue;
                    }

                    int soLuong = (int) getCellValueAsNumber(row.getCell(colSoLuong));
                    String tenDonViTinhExcel = null;
                    if (colDonViTinh != -1) {
                        tenDonViTinhExcel = getCellValueAsString(row.getCell(colDonViTinh));
                        if (tenDonViTinhExcel != null) {
                            tenDonViTinhExcel = tenDonViTinhExcel.trim();
                        }
                    }
                    double donGiaNhap = getCellValueAsNumber(row.getCell(colDonGia));

                    Date hanDung = null;
                    if (colHanDung != -1) {
                        hanDung = getCellValueAsDate(row.getCell(colHanDung));
                    }
                    if (hanDung == null) {
                        // Mặc định 2 năm sau nếu không có
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.YEAR, 2);
                        hanDung = cal.getTime();
                    }

                    // Kiểm tra hạn sử dụng dưới 6 tháng
                    LocalDate hsdLocal = new java.sql.Date(hanDung.getTime()).toLocalDate();
                    LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
                    if (hsdLocal.isBefore(ngayGioiHan) || hsdLocal.isEqual(ngayGioiHan)) {
                        String tenSP = maSP;
                        try {
                            SanPham spTemp = sanPhamBUS.laySanPhamTheoMa(maSP);
                            if (spTemp != null) {
                                tenSP = spTemp.getTenSanPham();
                            }
                        } catch (Exception ex) {
                            // Nếu không tìm thấy sản phẩm, dùng mã SP
                        }
                        String errorMsg = String.format("Dòng %d: Hạn sử dụng của sản phẩm '%s' (HSD: %s) dưới 6 tháng. Yêu cầu HSD phải lớn hơn %s",
                                i + 1, tenSP, hsdLocal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                ngayGioiHan.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        errors.append(errorMsg).append("\n");
                        errorCount++;
                        Notifications.getInstance().show(
                                Notifications.Type.ERROR,
                                Notifications.Location.TOP_CENTER,
                                errorMsg);
                        continue; // Bỏ qua dòng này
                    }

                    String tenLoHang = null;
                    if (colLoHang != -1) {
                        tenLoHang = getCellValueAsString(row.getCell(colLoHang));
                    }

                    Double tyLeChietKhauExcel = colChietKhau != -1
                            ? getCellValueAsNumber(row.getCell(colChietKhau))
                            : null;
                    Double thueGTGTExcel = colThueGTGT != -1
                            ? getCellValueAsNumber(row.getCell(colThueGTGT))
                            : null;

                    if (tyLeChietKhauExcel != null && tyLeChietKhauExcel > 100) {
                        throw new Exception("Phần trăm chiết khấu không hợp lệ (>100%)");
                    }
                    if (thueGTGTExcel != null && thueGTGTExcel > 100) {
                        throw new Exception("Phần trăm thuế GTGT không hợp lệ (>100%)");
                    }

                    // Tìm sản phẩm
                    SanPham sanPham = null;
                    try {
                        sanPham = sanPhamBUS.laySanPhamTheoMa(maSP);
                    } catch (Exception ex) {
                        // Thử tìm theo số đăng ký
                        var optional = sanPhamBUS.timSanPhamTheoSoDangKy(maSP);
                        if (optional.isPresent()) {
                            sanPham = optional.get();
                        }
                    }

                    DonViTinh donViTinhExcel = null;
                    if (tenDonViTinhExcel != null && !tenDonViTinhExcel.isEmpty()) {
                        if (donViTinhBUS == null) {
                            donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
                        }
                        try {
                            Optional<DonViTinh> optionalDonViTinh = donViTinhBUS.timDonViTinhTheoTen(tenDonViTinhExcel);
                            if (optionalDonViTinh.isPresent()) {
                                donViTinhExcel = optionalDonViTinh.get();
                            } else {
                                throw new Exception("Không tìm thấy đơn vị tính '" + tenDonViTinhExcel + "'");
                            }
                        } catch (Exception ex) {
                            throw new Exception("Không tìm thấy đơn vị tính '" + tenDonViTinhExcel + "'");
                        }
                    }

                    if (sanPham == null) {
                        errors.append("Dòng ").append(i + 1).append(": Không tìm thấy sản phẩm ").append(maSP).append("\n");
                        errorCount++;
                        continue;
                    }

                    if (donViTinhExcel != null) {
                        sanPham.setDonViTinh(donViTinhExcel);
                    }

                    if (nhaCungCap != null && nhaCungCap.getMaNhaCungCap() != null) {
                        String soDangKy = sanPham.getSoDangKy();
                        String maNCC = nhaCungCap.getMaNhaCungCap();

                        boolean coTheNhap = sanPhamBUS.kiemTraNhaCungCapCoTheNhapSoDangKy(soDangKy, maNCC);

                        if (!coTheNhap) {
                            // Lấy mã NCC đã nhập để hiển thị
                            String nccDaNhap = sanPhamBUS.layTenNhaCungCapDaNhapSoDangKy(soDangKy);
                            errors.append("Dòng ").append(i + 1)
                                    .append(": Sản phẩm '").append(sanPham.getTenSanPham())
                                    .append("' (SDK: ").append(soDangKy)
                                    .append(") đã được nhập bởi nhà cung cấp ")
                                    .append(nccDaNhap)
                                    .append(". Không thể nhập từ nhà cung cấp khác!\n");
                            errorCount++;
                            continue;
                        }
                    }

                    // Gom nhóm theo key: SĐK + HSD + Giá nhập
                    String soDangKy = sanPham.getSoDangKy() != null ? sanPham.getSoDangKy() : maSP;
                    // hsdLocal đã được tạo trong validation ở trên
                    LoKey key = new LoKey(soDangKy, hsdLocal, donGiaNhap);

                    gopSoLuong.merge(key, soLuong, Integer::sum);
                    sanPhamTheoKey.putIfAbsent(key, sanPham);
                    if (donViTinhExcel != null) {
                        donViTheoKey.putIfAbsent(key, donViTinhExcel);
                    }
                    if (tenLoHang != null && !tenLoHang.trim().isEmpty()) {
                        tenLoTheoKey.putIfAbsent(key, tenLoHang.trim());
                    }
                    if (tyLeChietKhauExcel != null) {
                        ckTheoKey.putIfAbsent(key, tyLeChietKhauExcel);
                    }
                    if (thueGTGTExcel != null) {
                        thueTheoKey.putIfAbsent(key, thueGTGTExcel);
                    }

                    successCount++;

                } catch (Exception e) {
                    errors.append("Dòng ").append(i + 1).append(": ").append(e.getMessage()).append("\n");
                    errorCount++;
                }
            }

            // Sau khi gom nhóm xong, tạo panel theo từng key
            for (Map.Entry<LoKey, Integer> entry : gopSoLuong.entrySet()) {
                LoKey key = entry.getKey();
                int tongSoLuong = entry.getValue();
                SanPham sp = sanPhamTheoKey.get(key);
                if (sp == null) {
                    continue;
                }
                DonViTinh dvt = donViTheoKey.get(key);
                String tenLo = tenLoTheoKey.getOrDefault(key, null);
                Double ck = ckTheoKey.get(key);
                Double thue = thueTheoKey.get(key);

                Date hanDungGop = Date.from(
                        key.getHanSuDung().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());

                try {
                    themSanPhamVaoPanelNhap(sp, tongSoLuong, key.getDonGia(), hanDungGop, tenLo, ck, thue, dvt);
                } catch (Exception ex) {
                    errors.append("Lỗi khi thêm sản phẩm vào danh sách (SDK ")
                            .append(key.getSoDangKy()).append("): ")
                            .append(ex.getMessage()).append("\n");
                    errorCount++;
                }
            }

            String message = "Import thành công " + successCount + " dòng sản phẩm";
            if (nhaCungCapDaTao && nhaCungCap != null) {
                message += "\nĐã tải thông tin nhà cung cấp: " + nhaCungCap.getTenNhaCungCap();
                if (nhaCungCap.getMaNhaCungCap() == null) {
                    message += " (chưa có trong DB, sẽ tạo khi nhập)";
                }
            }
            if (errorCount > 0) {
                message += "\n\nCó " + errorCount + " lỗi:\n" + errors.toString();
            }

            Notifications.getInstance().show(
                    errorCount > 0 ? Notifications.Type.WARNING : Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    message);

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi đọc file Excel: " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private double getCellValueAsNumber(Cell cell) {
        if (cell == null) {
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().replaceAll("[^0-9.]", ""));
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    private Date getCellValueAsDate(Cell cell) {
        if (cell == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 2);
            return cal.getTime();
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date;
        } else if (cell.getCellType() == CellType.STRING) {
            String dateStr = cell.getStringCellValue();
            try {
                Date date = dateFormat.parse(dateStr);
                return date;
            } catch (Exception e) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 2);
                return cal.getTime();
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        return cal.getTime();
    }

    /**
     * Xử lý thông tin nhà cung cấp từ Excel: - Tìm NCC trong DB theo tên hoặc
     * SĐT - Nếu không tìm thấy: TẠO OBJECT TẠM (chưa lưu vào DB) - Sẽ lưu vào
     * DB khi bấm nút "Nhập hàng"
     *
     * @return NhaCungCap object (có thể chưa có mã nếu chưa lưu DB)
     */
    private NhaCungCap xuLyThongTinNhaCungCap(Row row, int colMaNCC, int colTenNCC,
            int colDiaChi, int colSDT, int colEmail, int colMaSoThue) throws Exception {

        // Đọc thông tin từ Excel
        String maNCC = (colMaNCC != -1) ? getCellValueAsString(row.getCell(colMaNCC)) : null;
        String tenNCC = (colTenNCC != -1) ? getCellValueAsString(row.getCell(colTenNCC)) : null;
        String diaChi = (colDiaChi != -1) ? getCellValueAsString(row.getCell(colDiaChi)) : null;

        // sửa lại đọc sdt do bên excel nhập ssdt nó mất số 0 ở đầu
        String sdt = null;
        if (colSDT != -1) {
            Cell cellSDT = row.getCell(colSDT);

            if (cellSDT != null) {
                if (cellSDT.getCellType() == CellType.NUMERIC) {
                    sdt = String.format("%.0f", cellSDT.getNumericCellValue());
                } else {
                    sdt = cellSDT.getStringCellValue().trim();
                }
            }
        }

// Chuẩn hóa: chỉ giữ số
        if (sdt != null) {
            sdt = sdt.replaceAll("\\D", "");
        }

// Excel mất số 0 → thêm lại
        if (sdt != null && sdt.length() == 9) {
            sdt = "0" + sdt;
        }

        String email = (colEmail != -1) ? getCellValueAsString(row.getCell(colEmail)) : null;
        String maSoThue = (colMaSoThue != -1) ? getCellValueAsString(row.getCell(colMaSoThue)) : null;

        // Nếu không có thông tin gì về nhà cung cấp
        if ((maNCC == null || maNCC.trim().isEmpty())
                && (tenNCC == null || tenNCC.trim().isEmpty())) {
            return null;
        }

        // Khởi tạo NhaCungCapBUS nếu chưa có
        if (nhaCungCapBUS == null) {
            nhaCungCapBUS = new NhaCungCapBUS();
        }

        NhaCungCap ncc = null;

        if (sdt != null && !sdt.trim().isEmpty()) {
            ncc = nhaCungCapBUS.layNhaCungCapTheoSoDienThoai(sdt.trim());

            if (ncc != null) {
                if (tenNCC != null && !tenNCC.trim().isEmpty()
                        && !tenNCC.trim().equalsIgnoreCase(ncc.getTenNhaCungCap())) {
                }

                return ncc;
            }

        } else if (tenNCC != null && !tenNCC.trim().isEmpty()) {
            ncc = nhaCungCapBUS.layNhaCungCapTheoTen(tenNCC.trim());

            if (ncc != null) {
                return ncc;
            }

        } else {
//            throw new Exception("Phải có ít nhất TÊN hoặc SĐT nhà cung cấp!");
        }

//Tạm bỏ qua lỗi
//        if (sdt != null && !sdt.trim().isEmpty() && !sdt.trim().matches(NhaCungCap.SO_DIEN_THOAI_REGEX)) {
//            throw new Exception("Số điện thoại không đúng định dạng: " + sdt);
//        }
        if (sdt != null && !sdt.trim().matches(NhaCungCap.SO_DIEN_THOAI_REGEX)) {
            System.out.println("sdt ncc chua dung chuan: " + sdt + " (cho pphep nhap tam)");
        }

        if (email != null && !email.trim().isEmpty() && !email.trim().matches(NhaCungCap.EMAIL_REGEX)) {
            throw new Exception("Email không đúng định dạng: " + email);
        }

        if (email != null && !email.trim().isEmpty()) {
            NhaCungCap nccTrungEmail = nhaCungCapBUS.layNhaCungCapTheoEmail(email.trim());
            if (nccTrungEmail != null) {
                // Kiểm tra xem có phải cùng một nhà cung cấp không (theo SĐT hoặc tên)
                boolean laCungNCC = false;
                if (sdt != null && !sdt.trim().isEmpty()
                        && nccTrungEmail.getSoDienThoai() != null
                        && nccTrungEmail.getSoDienThoai().equals(sdt.trim())) {
                    laCungNCC = true;
                } else if (tenNCC != null && !tenNCC.trim().isEmpty()
                        && nccTrungEmail.getTenNhaCungCap() != null
                        && nccTrungEmail.getTenNhaCungCap().equalsIgnoreCase(tenNCC.trim())) {
                    laCungNCC = true;
                }

                if (!laCungNCC) {
                    throw new Exception("Email '" + email.trim() + "' đã được sử dụng bởi nhà cung cấp khác: "
                            + nccTrungEmail.getTenNhaCungCap() + " (" + nccTrungEmail.getMaNhaCungCap() + ")");
                }
            }
        }

        String tenNCCMoi = (tenNCC != null && !tenNCC.trim().isEmpty())
                ? tenNCC.trim()
                : ("NCC_" + (sdt != null ? sdt.trim() : "UNKNOWN"));

        NhaCungCap nccTam = new NhaCungCap();

        try {
            // Không set mã - sẽ tự sinh khi lưu
            nccTam.setTenNhaCungCap(tenNCCMoi);

            // Set các trường optional
            if (diaChi != null && !diaChi.trim().isEmpty()) {
                nccTam.setDiaChi(diaChi.trim());
            }

            if (sdt != null && !sdt.trim().isEmpty()) {
                nccTam.setSoDienThoai(sdt.trim());
            }

            if (email != null && !email.trim().isEmpty()) {
                nccTam.setEmail(email.trim());
            }

            if (maSoThue != null && !maSoThue.trim().isEmpty()) {
                nccTam.setMaSoThue(maSoThue.trim());
            }
        } catch (Exception e) {
            throw new Exception("Lỗi validate thông tin nhà cung cấp: " + e.getMessage());
        }
        //kiểm tra sdt có nhập vào không
        System.out.println("NCC Excel: " + tenNCC + " | SDT=" + sdt);
        return nccTam;
    }

    private void btnConfirmPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmPurchaseActionPerformed
        try {
            List<Panel_ChiTietSanPhamNhap> danhSachPanel = new ArrayList<>();
            for (Component comp : pnSanPham.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamNhap) {
                    danhSachPanel.add((Panel_ChiTietSanPhamNhap) comp);
                }
            }

            if (danhSachPanel.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Chưa có sản phẩm nào để nhập!");
                return;
            }

            if (nhaCungCapHienTai == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Chưa có thông tin nhà cung cấp! Vui lòng import từ Excel hoặc chọn nhà cung cấp.");
                return;
            }

            // Nếu NCC chưa có mã (chưa lưu DB), tạo mới ngay bây giờ
            if (nhaCungCapHienTai.getMaNhaCungCap() == null) {
                if (nhaCungCapHienTai.getEmail() != null && !nhaCungCapHienTai.getEmail().trim().isEmpty()) {
                    NhaCungCap nccTrungEmail = nhaCungCapBUS.layNhaCungCapTheoEmail(nhaCungCapHienTai.getEmail().trim());
                    if (nccTrungEmail != null) {
                        boolean laCungNCC = false;
                        if (nhaCungCapHienTai.getSoDienThoai() != null && !nhaCungCapHienTai.getSoDienThoai().trim().isEmpty()
                                && nccTrungEmail.getSoDienThoai() != null
                                && nccTrungEmail.getSoDienThoai().equals(nhaCungCapHienTai.getSoDienThoai().trim())) {
                            laCungNCC = true;
                        } else if (nhaCungCapHienTai.getTenNhaCungCap() != null && !nhaCungCapHienTai.getTenNhaCungCap().trim().isEmpty()
                                && nccTrungEmail.getTenNhaCungCap() != null
                                && nccTrungEmail.getTenNhaCungCap().equalsIgnoreCase(nhaCungCapHienTai.getTenNhaCungCap().trim())) {
                            laCungNCC = true;
                        }

                        if (!laCungNCC) {
                            Notifications.getInstance().show(Notifications.Type.ERROR,
                                    Notifications.Location.TOP_CENTER,
                                    "Email '" + nhaCungCapHienTai.getEmail().trim() + "' đã được sử dụng bởi nhà cung cấp khác: "
                                    + nccTrungEmail.getTenNhaCungCap() + " (" + nccTrungEmail.getMaNhaCungCap() + ")");
                            return;
                        }
                    }
                }

                boolean nccCreated = nhaCungCapBUS.taoNhaCungCap(nhaCungCapHienTai);
                if (!nccCreated) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lỗi khi tạo nhà cung cấp mới!");
                    return;
                }
                NhaCungCap nccDaLuu = nhaCungCapBUS.layNhaCungCapTheoTen(nhaCungCapHienTai.getTenNhaCungCap());
                if (nccDaLuu == null) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lỗi: Không thể đọc lại nhà cung cấp vừa tạo!");
                    return;
                }
                nhaCungCapHienTai = nccDaLuu;
                txtSupplierId.setText(nccDaLuu.getMaNhaCungCap());
            }

            // Tạo đơn nhập hàng TẠM (KHÔNG lưu vào database)
            DonNhapHang donNhapHang = new DonNhapHang();
            donNhapHang.setNgayNhap(java.time.LocalDate.now());
            donNhapHang.setNhanVien(nhanVienHienTai);
            donNhapHang.setNhaCungCap(nhaCungCapHienTai);

            // Tạo mã đơn nhập hàng tạm để hiển thị trong preview
            String maDonNhapTam = taoMaDonNhapHangTam();
            donNhapHang.setMaDonNhapHang(maDonNhapTam);

            double tongThanhToan = 0;
            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                tongThanhToan += panel.getThanhTienSauThue();
            }
            donNhapHang.setThanhTien(tongThanhToan);

            // ✅ BƯỚC 1: Tạo các lô mới trước (nếu có panel nào chọn tạo lô mới)
            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                // Kiểm tra xem panel này có cần tạo lô mới không
                if (panel.getTenLoMoi() != null && panel.getLoHangDaChon() == null) {
                    LoHang loMoiTao = panel.taoLoMoiThucSu();
                    if (loMoiTao != null) {
                        System.out.println("✅ Đã tạo lô mới: " + loMoiTao.getMaLoHang() + " - " + loMoiTao.getTenLoHang());
                    } else {
                        // Lỗi khi tạo lô mới
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                Notifications.Location.TOP_CENTER,
                                "Lỗi khi tạo lô mới cho sản phẩm: " + panel.getSanPham().getTenSanPham() + ". Vui lòng thử lại.");
                        return;
                    }
                }
            }

            // Tạo danh sách chi tiết TẠM (chưa lưu vào DB)
            List<ChiTietDonNhapHang> danhSachChiTiet = new ArrayList<>();
            boolean allDetailsValid = true;

            Map<String, String> mapLoHangDaChon = new HashMap<>();
            Set<String> setSanPhamDaXuLy = new HashSet<>();

            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                SanPham sanPham = panel.getSanPham();
                int soLuong = panel.getSoLuong();
                double donGia = panel.getDonGiaNhap();
                double tongTruocChietKhau = panel.getTongTienTruocChietKhau();
                double tienChietKhau = panel.getTienChietKhau();
                double thanhTienTinhThue = panel.getThanhTienTinhThue();
                double tienThue = panel.getTienThue();
                double thanhTienSauThue = panel.getThanhTienSauThue();

                String maSanPham = sanPham.getMaSanPham();
                if (setSanPhamDaXuLy.contains(maSanPham)) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Không thể nhập trùng sản phẩm '" + sanPham.getTenSanPham() + "'! Vui lòng xóa sản phẩm trùng.");
                    allDetailsValid = false;
                    continue;
                }

                // ✅ Kiểm tra số đăng ký và nhà cung cấp
                if (nhaCungCapHienTai != null && nhaCungCapHienTai.getMaNhaCungCap() != null) {
                    String soDangKy = sanPham.getSoDangKy();
                    if (soDangKy != null && !soDangKy.trim().isEmpty()) {
                        boolean coTheNhap = sanPhamBUS.kiemTraNhaCungCapCoTheNhapSoDangKy(soDangKy, nhaCungCapHienTai.getMaNhaCungCap());
                        if (!coTheNhap) {
                            // Lấy số điện thoại NCC đã nhập để hiển thị
                            List<String> danhSachSDT = sanPhamDAO.getSoDienThoaiNCCBySoDangKy(soDangKy);
                            String sdtNCCDaNhap = danhSachSDT != null && !danhSachSDT.isEmpty() ? danhSachSDT.get(0) : "không xác định";
                            String sdtNCCHienTai = nhaCungCapHienTai.getSoDienThoai() != null ? nhaCungCapHienTai.getSoDienThoai() : "không xác định";
                            Notifications.getInstance().show(Notifications.Type.ERROR,
                                    Notifications.Location.TOP_CENTER,
                                    "Số đăng ký '" + soDangKy + "' đã được nhập bởi nhà cung cấp có số điện thoại: " + sdtNCCDaNhap + ". Bạn đang nhập từ nhà cung cấp có số điện thoại: " + sdtNCCHienTai + ". Không thể nhập từ nhiều nhà cung cấp khác nhau!");
                            allDetailsValid = false;
                            continue;
                        }
                    }
                }

                // ✅ Nếu chưa có lô nhưng có thông tin lô mới tạm → tạo lô ngay lúc xác nhận
                LoHang loHang = panel.getLoHangDaChon();
                if (loHang == null && panel.coThongTinLoMoi()) {
                    loHang = panel.taoLoMoiThucSu();
                }

                if (loHang == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Vui lòng chọn lô hàng cho sản phẩm: " + sanPham.getTenSanPham());
                    allDetailsValid = false;
                    continue;
                }

                if (loHang.getSanPham() == null
                        || !loHang.getSanPham().getMaSanPham().equals(sanPham.getMaSanPham())) {
                    System.out.println("✗ Lô '" + loHang.getTenLoHang() + "' không thuộc sản phẩm: " + sanPham.getTenSanPham());
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lô không đúng sản phẩm! Vui lòng chọn lại.");
                    allDetailsValid = false;
                    continue;
                }

                String maLoHang = loHang.getMaLoHang();
                if (mapLoHangDaChon.containsKey(maLoHang)) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lô này đã được chọn! Vui lòng chọn lô khác.");
                    allDetailsValid = false;
                    continue;
                }

                mapLoHangDaChon.put(maLoHang, sanPham.getTenSanPham());
                setSanPhamDaXuLy.add(maSanPham);

                LocalDate hsd = loHang.getHanSuDung();
                LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
                if (hsd.isBefore(ngayGioiHan) || hsd.isEqual(ngayGioiHan)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "HSD của lô '" + loHang.getTenLoHang() + "' phải lớn hơn 6 tháng!");
                    allDetailsValid = false;
                    continue;
                }

                // Tạo chi tiết TẠM (chưa lưu vào DB)
                ChiTietDonNhapHang chiTiet = new ChiTietDonNhapHang();
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(donGia);
                chiTiet.setThanhTien(thanhTienSauThue);
                chiTiet.setDonNhapHang(donNhapHang);
                chiTiet.setLoHang(loHang);
                try {
                    chiTiet.setTyLeChietKhau(panel.getTyLeChietKhau());
                    chiTiet.setTienChietKhau(tienChietKhau);
                    chiTiet.setThanhTienTinhThue(thanhTienTinhThue);
                    chiTiet.setThueSuat(panel.getThueGTGT());
                    chiTiet.setTienThue(tienThue);
                    chiTiet.setThanhTienSauThue(thanhTienSauThue);
                } catch (Exception ex) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lỗi dữ liệu chiết khấu / thuế cho sản phẩm: " + sanPham.getTenSanPham()
                            + " - " + ex.getMessage());
                    allDetailsValid = false;
                    continue;
                }

                danhSachChiTiet.add(chiTiet);
            }

            if (allDetailsValid && !danhSachChiTiet.isEmpty()) {
                // Hiển thị hóa đơn với đơn hàng TẠM (chưa lưu DB)
                boolean isDonHangCancelled = hienThiHoaDon(donNhapHang, danhSachChiTiet, danhSachPanel);

                // CHỈ reset khi đã lưu (không hủy)
                if (!isDonHangCancelled) {
                    xoaToanBoSanPham();
                    nhaCungCapHienTai = null;
                    txtSupplierId.setText("");
                    txtSupplierName.setText("");
                    txtTotalPrice.setText("0 đ");
                    txtSearchSupplier.setText("");
                    txtSearchSupplier.setEditable(true);

                    Notifications.getInstance().show(Notifications.Type.SUCCESS,
                            Notifications.Location.TOP_CENTER,
                            "Nhập hàng thành công! Mã đơn nhập: " + donNhapHang.getMaDonNhapHang());
                }
            } else {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Có lỗi khi kiểm tra thông tin đơn nhập!");
            }

        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi tạo phiếu nhập hàng: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnConfirmPurchaseActionPerformed

    private void txtSearchSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchSupplierActionPerformed

    }//GEN-LAST:event_txtSearchSupplierActionPerformed

    private void txtSearchProductKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchProductKeyPressed

    }//GEN-LAST:event_txtSearchProductKeyPressed

    /**
     * Xóa toàn bộ sản phẩm khỏi panel
     */
    private void xoaToanBoSanPham() {
        // Xóa tất cả component trừ header
        Component[] components = pnSanPham.getComponents();
        for (Component comp : components) {
            if (comp instanceof Panel_ChiTietSanPhamNhap) {
                pnSanPham.remove(comp);
            }
        }
        pnSanPham.revalidate();
        pnSanPham.repaint();
    }

    /**
     * Tạo mã đơn nhập hàng tạm để hiển thị trong preview Mã này sẽ được tạo lại
     * chính xác khi lưu vào database
     *
     * @return Mã đơn nhập hàng tạm
     */
    private String taoMaDonNhapHangTam() {
        try {
            return donNhapHangBUS.duKienMaDonNhapHangTiepTheo();
        } catch (Exception ex) {
            System.err.println("Không thể lấy mã đơn nhập từ BUS, fallback cục bộ: " + ex.getMessage());
            LocalDate ngayHienTai = LocalDate.now();
            String ngayThangNam = String.format("%02d%02d%04d",
                    ngayHienTai.getDayOfMonth(),
                    ngayHienTai.getMonthValue(),
                    ngayHienTai.getYear());
            return "DNH" + ngayThangNam + "0001";
        }
    }

    /**
     * Hiển thị hóa đơn nhập hàng
     *
     * @param donNhapHang Đơn nhập hàng (tạm, chưa lưu DB)
     * @param danhSachChiTiet Danh sách chi tiết (tạm, chưa lưu DB)
     * @param danhSachPanel Danh sách panel sản phẩm để lưu sau
     * @return true nếu hủy đơn, false nếu đã lưu
     */
    private boolean hienThiHoaDon(DonNhapHang donNhapHang, List<ChiTietDonNhapHang> danhSachChiTiet, List<Panel_ChiTietSanPhamNhap> danhSachPanel) {
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);
        javax.swing.JDialog dialog;
        if (parentWindow instanceof java.awt.Frame) {
            dialog = new javax.swing.JDialog((java.awt.Frame) parentWindow, "Phiếu nhập hàng", true);
        } else if (parentWindow instanceof java.awt.Dialog) {
            dialog = new javax.swing.JDialog((java.awt.Dialog) parentWindow, "Phiếu nhập hàng", true);
        } else {
            dialog = new javax.swing.JDialog();
            dialog.setTitle("Phiếu nhập hàng");
            dialog.setModal(true);
        }

        dialog.setSize(1300, 900);//dialog.setSize(680, 900);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setAlwaysOnTop(true);

        javax.swing.JScrollPane mainScrollPane = new javax.swing.JScrollPane();
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        String ngayNhap = (donNhapHang != null && donNhapHang.getNgayNhap() != null)
                ? dateFormat.format(java.sql.Date.valueOf(donNhapHang.getNgayNhap()))
                : dateFormat.format(new java.util.Date());
        String maDon = donNhapHang != null && donNhapHang.getMaDonNhapHang() != null
                ? donNhapHang.getMaDonNhapHang()
                : "CHUA_CAP";
        String tenNhanVien = nhanVienHienTai != null
                ? nhanVienHienTai.getMaNhanVien() + " - " + nhanVienHienTai.getTenNhanVien()
                : "N/A";
        String tenNcc = nhaCungCapHienTai != null ? nhaCungCapHienTai.getTenNhaCungCap() : "N/A";
        String dienThoai = (nhaCungCapHienTai != null && nhaCungCapHienTai.getSoDienThoai() != null)
                ? nhaCungCapHienTai.getSoDienThoai()
                : "N/A";
        String diaChiNcc = (nhaCungCapHienTai != null && nhaCungCapHienTai.getDiaChi() != null)
                ? nhaCungCapHienTai.getDiaChi()
                : "Chưa cập nhật";

        // ===== HEADER CHUNG =====
        javax.swing.JLabel lblStoreName = new javax.swing.JLabel("IUH PHARMACITY");
        lblStoreName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblStoreName.setForeground(new Color(0, 120, 215));
        lblStoreName.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStoreName);
        mainPanel.add(Box.createVerticalStrut(4));

        javax.swing.JLabel lblAddress = new javax.swing.JLabel("12 Nguyen Van Bao, Ward 4, Go Vap District, HCMC");
        lblAddress.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblAddress);

        javax.swing.JLabel lblContact = new javax.swing.JLabel("Hotline: 1800 6928 | Email: cskh@pharmacity.vn");
        lblContact.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblContact.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblContact);
        mainPanel.add(Box.createVerticalStrut(12));

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("PHIẾU NHẬP HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(8));

        // ===== BARCODE MÃ PHIẾU =====
        if (donNhapHang != null && donNhapHang.getMaDonNhapHang() != null) {
            try {
                BufferedImage barcodeRaw = BarcodeUtil.taoBarcode(donNhapHang.getMaDonNhapHang());
                BufferedImage barcodeWithText = BarcodeUtil.addTextBelow(barcodeRaw, donNhapHang.getMaDonNhapHang());
                javax.swing.JLabel lblBarcode = new javax.swing.JLabel(new javax.swing.ImageIcon(barcodeWithText));
                lblBarcode.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(lblBarcode);
                mainPanel.add(Box.createVerticalStrut(6));
            } catch (Exception ex) {
                System.err.println("Không thể tạo barcode phiếu nhập: " + ex.getMessage());
            }
        }

        javax.swing.JLabel lblNgayLap = new javax.swing.JLabel("Ngày lập: " + ngayNhap);
        lblNgayLap.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        lblNgayLap.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblNgayLap);
        mainPanel.add(Box.createVerticalStrut(12));

        // ===== THÔNG TIN ĐƠN / NHÀ CUNG CẤP =====
        javax.swing.JPanel infoPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 16, 0));
        infoPanel.setBackground(Color.WHITE);

        javax.swing.JPanel orderInfoPanel = createInfoSectionPanel(
                "THÔNG TIN ĐƠN NHẬP",
                "Mã phiếu: " + maDon,
                "Nhân viên: " + tenNhanVien,
                "Ngày lập: " + ngayNhap
        );
        javax.swing.JPanel supplierInfoPanel = createInfoSectionPanel(
                "NHÀ CUNG CẤP",
                "Tên NCC: " + tenNcc,
                "Điện thoại: " + dienThoai,
                "Địa chỉ: " + diaChiNcc
        );
        infoPanel.add(orderInfoPanel);
        infoPanel.add(supplierInfoPanel);
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createVerticalStrut(18));

        // ===== BẢNG CHI TIẾT =====
        javax.swing.JLabel lblChiTiet = new javax.swing.JLabel("CHI TIẾT SẢN PHẨM NHẬP");
        lblChiTiet.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblChiTiet.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblChiTiet);
        mainPanel.add(Box.createVerticalStrut(6));

        String[] columnNames = {"STT", "Tên sản phẩm", "Đơn vị tính", "Số lô", "Số lượng",
            "Đơn giá", "Tạm tính", "CK (%)", "CK (đ)", "Thuế (%)", "Thuế (đ)", "Thành tiền"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        java.text.DecimalFormat percentFormat = new java.text.DecimalFormat("#0.##");
        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.setGridColor(new Color(220, 220, 220));
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);//180
        table.getColumnModel().getColumn(2).setPreferredWidth(70);//90
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        table.getColumnModel().getColumn(6).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getColumnModel().getColumn(8).setPreferredWidth(110);
        table.getColumnModel().getColumn(9).setPreferredWidth(60);
        table.getColumnModel().getColumn(10).setPreferredWidth(110);
        table.getColumnModel().getColumn(11).setPreferredWidth(120);

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);

        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);

        int stt = 1;
        double tongTamTinh = 0;
        double tongTienChietKhau = 0;
        double tongSauChietKhau = 0;
        double tongTienThue = 0;
        double tongThanhToan = 0;
        for (ChiTietDonNhapHang chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            SanPham sanPham = loHang.getSanPham();
            double tamTinh = chiTiet.getDonGia() * chiTiet.getSoLuong();
            double tyLeCK = chiTiet.getTyLeChietKhau();
            double tienCK = chiTiet.getTienChietKhau();
            double sauCK = chiTiet.getThanhTienTinhThue() > 0
                    ? chiTiet.getThanhTienTinhThue()
                    : Math.max(tamTinh - tienCK, 0);
            double thueSuat = chiTiet.getThueSuat();
            double tienThue = chiTiet.getTienThue();
            double thanhToan = chiTiet.getThanhTienSauThue() > 0
                    ? chiTiet.getThanhTienSauThue()
                    : chiTiet.getThanhTien();

            tongTamTinh += tamTinh;
            tongTienChietKhau += tienCK;
            tongSauChietKhau += sauCK;
            tongTienThue += tienThue;
            tongThanhToan += thanhToan;

            tableModel.addRow(new Object[]{
                stt++,
                sanPham.getTenSanPham(),
                sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getTenDonVi() : "Hộp",
                //loHang.getTenLoHang(),
                loHang.getMaLoHang(),
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                currencyFormat.format(tamTinh) + " đ",
                percentFormat.format(tyLeCK) + " %",
                currencyFormat.format(tienCK) + " đ",
                percentFormat.format(thueSuat) + " %",
                currencyFormat.format(tienThue) + " đ",
                currencyFormat.format(thanhToan) + " đ"
            });
        }

        javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        tableScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableScrollPane.setPreferredSize(new java.awt.Dimension(580, 280));
        tableScrollPane.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 320));
        mainPanel.add(tableScrollPane);
        mainPanel.add(Box.createVerticalStrut(16));

        // ===== TỔNG TIỀN =====
        javax.swing.JPanel summaryPanel = new javax.swing.JPanel();
        summaryPanel.setLayout(new javax.swing.BoxLayout(summaryPanel, javax.swing.BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryPanel.setMaximumSize(new java.awt.Dimension(450, 200));

        java.util.function.BiConsumer<String, String> addSummaryRow = (label, value) -> {
            javax.swing.JPanel row = new javax.swing.JPanel(new java.awt.BorderLayout());
            row.setBackground(Color.WHITE);
            row.setMaximumSize(new java.awt.Dimension(450, 26));

            javax.swing.JLabel lblLeft = new javax.swing.JLabel(label);
            lblLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

            javax.swing.JLabel lblRight = new javax.swing.JLabel(value);
            lblRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            lblRight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

            row.add(lblLeft, java.awt.BorderLayout.WEST);
            row.add(lblRight, java.awt.BorderLayout.EAST);
            summaryPanel.add(row);
            summaryPanel.add(Box.createVerticalStrut(4));
        };

        addSummaryRow.accept("Tạm tính:", currencyFormat.format(tongTamTinh) + " đ");
        addSummaryRow.accept("Chiết khấu:", "-" + currencyFormat.format(tongTienChietKhau) + " đ");
        addSummaryRow.accept("Sau chiết khấu:", currencyFormat.format(tongSauChietKhau) + " đ");
        addSummaryRow.accept("Thuế GTGT:", "+" + currencyFormat.format(tongTienThue) + " đ");
        addSummaryRow.accept("Tổng tiền phiếu nhập:", currencyFormat.format(tongThanhToan) + " đ");

        javax.swing.JPanel highlightRow = new javax.swing.JPanel(new java.awt.BorderLayout());
        highlightRow.setBackground(Color.WHITE);
        highlightRow.setMaximumSize(new java.awt.Dimension(450, 30));

        javax.swing.JLabel lblTong = new javax.swing.JLabel("THÀNH TIỀN:");
        lblTong.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        javax.swing.JLabel lblTongValue = new javax.swing.JLabel(currencyFormat.format(tongThanhToan) + " đ");
        lblTongValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        lblTongValue.setForeground(new Color(0, 123, 255));
        lblTongValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        highlightRow.add(lblTong, java.awt.BorderLayout.WEST);
        highlightRow.add(lblTongValue, java.awt.BorderLayout.EAST);
        summaryPanel.add(Box.createVerticalStrut(6));
        summaryPanel.add(highlightRow);

        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createVerticalStrut(18));

        javax.swing.JLabel lblFooter = new javax.swing.JLabel("Cảm ơn bạn đã sử dụng hệ thống quầy nhập hàng!");
        lblFooter.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 11));
        lblFooter.setForeground(new Color(0, 120, 215));
        lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblFooter);
        mainPanel.add(Box.createVerticalStrut(18));

        // ===== NÚT HÀNH ĐỘNG =====
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 14, 0));
        buttonPanel.setBackground(Color.WHITE);
        final boolean[] isDonHangCancelled = {true};
        final boolean[] isDonHangSaved = {false};

        javax.swing.JButton btnInHoaDon = new javax.swing.JButton("📄 In phiếu nhập");
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        btnInHoaDon.setPreferredSize(new java.awt.Dimension(200, 44));
        ButtonStyles.apply(btnInHoaDon, ButtonStyles.Type.PRIMARY);
        btnInHoaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInHoaDon.addActionListener(e -> {
            if (luuDonNhapHangVaoDB(donNhapHang, danhSachChiTiet)) {
                isDonHangSaved[0] = true;
                isDonHangCancelled[0] = false;
                inHoaDonNhapHang(donNhapHang, danhSachChiTiet);
                dialog.dispose();
            }
        });

        javax.swing.JButton btnHuyBo = new javax.swing.JButton("❌ Hủy bỏ");
        btnHuyBo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        btnHuyBo.setPreferredSize(new java.awt.Dimension(200, 44));
        ButtonStyles.apply(btnHuyBo, ButtonStyles.Type.DANGER);
        btnHuyBo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuyBo.addActionListener(e -> {
            isDonHangCancelled[0] = true;
            dialog.dispose();
        });

        buttonPanel.add(btnInHoaDon);
        buttonPanel.add(btnHuyBo);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        mainScrollPane.setViewportView(mainPanel);
        dialog.add(mainScrollPane);
        dialog.setVisible(true);

        return isDonHangCancelled[0];
    }

    /**
     * Lưu đơn nhập hàng và chi tiết vào database, đồng thời tăng tồn kho
     *
     * @param donNhapHang Đơn nhập hàng cần lưu
     * @param danhSachChiTiet Danh sách chi tiết cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    private boolean luuDonNhapHangVaoDB(DonNhapHang donNhapHang, List<ChiTietDonNhapHang> danhSachChiTiet) {
        try {
            // 1. Lưu đơn nhập hàng vào database
            boolean savedDonNhap = donNhapHangBUS.taoDonNhapHang(donNhapHang);

            if (!savedDonNhap) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Lỗi khi lưu đơn nhập hàng!");
                return false;
            }

            // 2. Lưu chi tiết đơn nhập hàng và tăng tồn kho
            boolean allDetailsSaved = true;
            for (ChiTietDonNhapHang chiTiet : danhSachChiTiet) {
                // Lưu chi tiết (sẽ tự động tăng tồn kho trong BUS)
                boolean chiTietSaved = chiTietDonNhapHangBUS.themChiTietDonNhapHang(chiTiet);
                if (!chiTietSaved) {
                    allDetailsSaved = false;
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lỗi khi lưu chi tiết đơn nhập hàng cho lô: "
                            + (chiTiet.getLoHang() != null ? chiTiet.getLoHang().getTenLoHang() : "N/A"));
                }
            }

            if (!allDetailsSaved) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Có lỗi khi lưu một số chi tiết đơn nhập!");
                return false;
            }

            return true;

        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi lưu đơn nhập hàng: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    private void inHoaDonNhapHang(DonNhapHang donNhapHang, List<ChiTietDonNhapHang> danhSachChiTiet) {
        if (donNhapHang == null || danhSachChiTiet == null || danhSachChiTiet.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Không có dữ liệu để in phiếu nhập!");
            return;
        }

        try {
            byte[] pdfData = taoHoaDonNhapPdf(donNhapHang, danhSachChiTiet);
            hienThiPdfTamThoiNhap(pdfData, donNhapHang);
            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Đã mở phiếu nhập PDF tạm thời để xem/in.");
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Không thể tạo phiếu nhập PDF: " + ex.getMessage());
        }
    }

    private byte[] taoHoaDonNhapPdf(DonNhapHang donNhapHang, List<ChiTietDonNhapHang> danhSachChiTiet) throws IOException {
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        currencyFormat.setRoundingMode(RoundingMode.DOWN);//k làm tròn như đã bàn
        DecimalFormat percentFormat = new DecimalFormat("#,##0.##");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        double tongTamTinh = 0;
        double tongTienChietKhau = 0;
        double tongSauChietKhau = 0;
        double tongTienThue = 0;
        double tongThanhToan = 0;

//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PdfWriter writer = new PdfWriter(baos); PdfDocument pdfDoc = new PdfDocument(writer); Document document = new Document(pdfDoc, PageSize.A5)) {
//
//            document.setMargins(24, 24, 24, 24);
//theo như ks thì phiếu nhập nằm ngang với lại v cho đủ table 
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PdfWriter writer = new PdfWriter(baos); PdfDocument pdfDoc = new PdfDocument(writer); Document document = new Document(pdfDoc, PageSize.A4.rotate())) {

            document.setMargins(20, 20, 20, 20);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            document.add(new Paragraph("IUH PHARMACITY")
                    .setFont(fontBold)
                    .setFontSize(16)
                    .setFontColor(ColorConstants.BLUE)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("12 Nguyen Van Bao, Ward 4, Go Vap District, HCMC")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Hotline: 1800 6928 | Email: cskh@pharmacity.vn")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));

            try {
                String maPhieu = Optional.ofNullable(donNhapHang)
                        .map(DonNhapHang::getMaDonNhapHang)
                        .orElse("UNKNOWN");
                BufferedImage barcodeRaw = BarcodeUtil.taoBarcode(maPhieu);
                BufferedImage barcodeWithText = BarcodeUtil.addTextBelow(barcodeRaw, maPhieu);
                ByteArrayOutputStream barcodeStream = new ByteArrayOutputStream();
                ImageIO.write(barcodeWithText, "png", barcodeStream);
                Image barcodeImage = new Image(ImageDataFactory.create(barcodeStream.toByteArray()))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                        .setAutoScale(false)
                        .setWidth(150);
                document.add(barcodeImage);
            } catch (Exception ex) {
                document.add(new Paragraph(""));
            }

            document.add(new Paragraph("PHIEU NHAP HANG")
                    .setFont(fontBold)
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER));

            LocalDate ngayNhap = Optional.ofNullable(donNhapHang)
                    .map(DonNhapHang::getNgayNhap)
                    .orElse(LocalDate.now());

            document.add(new Paragraph("Ngay lap: " + ngayNhap.format(dateFormatter))
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));

            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth();
            infoTable.addCell(tieuDeThongTinNhap("THONG TIN NHA CUNG CAP", fontBold));
            infoTable.addCell(tieuDeThongTinNhap("THONG TIN NHAN VIEN", fontBold));

            NhaCungCap nhaCungCap = donNhapHang.getNhaCungCap();
            NhanVien nhanVien = donNhapHang.getNhanVien();

            String thongTinNCC = (nhaCungCap != null ? "Ten: " + nhaCungCap.getTenNhaCungCap() : "Ten: N/A")
                    + (nhaCungCap != null && nhaCungCap.getSoDienThoai() != null
                    ? "\nSDT: " + nhaCungCap.getSoDienThoai()
                    : "")
                    + (nhaCungCap != null && nhaCungCap.getDiaChi() != null
                    ? "\nDia chi: " + nhaCungCap.getDiaChi()
                    : "");

            String thongTinNV = (nhanVien != null ? "Ho ten: " + nhanVien.getTenNhanVien() : "Ho ten: N/A")
                    + (nhanVien != null && nhanVien.getSoDienThoai() != null
                    ? "\nSDT: " + nhanVien.getSoDienThoai()
                    : "");

            infoTable.addCell(noiDungThongTinNhap(font, thongTinNCC));
            infoTable.addCell(noiDungThongTinNhap(font, thongTinNV));

            document.add(infoTable);
            document.add(new Paragraph("\n"));

//            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{6, 30, 16, 10, 16, 22}))
//                    .useAllAvailableWidth();
            //String[] headerLabels = {"STT", "Ten san pham", "So lo", "SL", "Don gia", "Thanh toan"};
            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{4, 8, 24, 10, 13, 7, 8, 15, 6, 12, 12, 6, 8, 12}))
                    .useAllAvailableWidth();
            String[] headerLabels = {"STT", "Ma hang", "Ten hang hoa", "So lo", "Han dung", "DVT", "SL", "Don gia", "% CK", "Thanh tien CK", "Thanh tien tinh thue", "% Thue", "Thue GTGT", "Thanh tien sau thue"};
            for (String label : headerLabels) {
                itemsTable.addHeaderCell(
                        new com.itextpdf.layout.element.Cell()
                                .add(new Paragraph(label)
                                        .setFont(fontBold)
                                        .setFontSize(9)
                                        .setTextAlignment(TextAlignment.CENTER))
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setPadding(3)
                                .setKeepTogether(false) // tự căn chỉ xuống dòng
                );
            }

            int stt = 1;
            for (ChiTietDonNhapHang chiTiet : danhSachChiTiet) {
                LoHang loHang = chiTiet.getLoHang();
                SanPham sanPham = loHang != null ? loHang.getSanPham() : null;
                String maSP = sanPham != null ? sanPham.getMaSanPham() : "";
                String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
                String soLo = loHang != null ? loHang.getMaLoHang() : "-";
                String hsd = loHang != null && loHang.getHanSuDung() != null
                        ? loHang.getHanSuDung().format(dateFormatter)
                        : "-";
                String dvt = (sanPham != null && sanPham.getDonViTinh() != null)
                        ? sanPham.getDonViTinh().getTenDonVi()
                        : "-";

                int sl = chiTiet.getSoLuong();
                double donGiaVal = chiTiet.getDonGia();
                double tamTinh = donGiaVal * sl;
                double tyLeCK = chiTiet.getTyLeChietKhau();
                double tienCK = chiTiet.getTienChietKhau();
                double thanhTienTinhThue = chiTiet.getThanhTienTinhThue() > 0
                        ? chiTiet.getThanhTienTinhThue()
                        : Math.max(tamTinh - tienCK, 0);
                double thueSuat = chiTiet.getThueSuat();
                double tienThue = chiTiet.getTienThue() > 0 ? chiTiet.getTienThue() : thanhTienTinhThue * (thueSuat / 100.0);
                double thanhTienSauThue = chiTiet.getThanhTienSauThue() > 0
                        ? chiTiet.getThanhTienSauThue()
                        : thanhTienTinhThue + tienThue;

                // Cộng dồn tổng
                tongTamTinh += tamTinh;
                tongTienChietKhau += tienCK;
                tongTienThue += tienThue;
                tongThanhToan += thanhTienSauThue;

                itemsTable.addCell(taoCellNhap(String.valueOf(stt++), font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(maSP, font, TextAlignment.LEFT));
                itemsTable.addCell(taoCellNhap(tenSP, font, TextAlignment.LEFT));
                itemsTable.addCell(taoCellNhap(soLo, font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(hsd, font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(dvt, font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(String.valueOf(sl), font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(currencyFormat.format(donGiaVal), font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(percentFormat.format(tyLeCK) + " %", font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(currencyFormat.format(tienCK), font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(currencyFormat.format(thanhTienTinhThue), font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(percentFormat.format(thueSuat) + " %", font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(currencyFormat.format(tienThue), font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(currencyFormat.format(thanhTienSauThue), font, TextAlignment.RIGHT));
            }

            document.add(itemsTable);
            document.add(new Paragraph("\n"));

            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER.CENTER)
                    .setWidth(UnitValue.createPercentValue(80));

            addSummaryRowNhap(summaryTable, "Tạm tính:",
                    currencyFormat.format(tongTamTinh) + " đ", font, font);
            addSummaryRowNhap(summaryTable, "Chiết khấu:",
                    "-" + currencyFormat.format(tongTienChietKhau) + " đ", font, font);
            addSummaryRowNhap(summaryTable, "Thuế GTGT:",
                    "+" + currencyFormat.format(tongTienThue) + " đ", font, font);
            addSummaryRowNhap(summaryTable, "Tổng thanh toán:",
                    currencyFormat.format(tongThanhToan) + " đ", fontBold, fontBold);

            document.add(summaryTable);

            document.add(new Paragraph("\nCam on quy doi tac da hop tac cung Pharmacity!")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Vui long kiem tra thong tin phieu nhap truoc khi ky xac nhan.")
                    .setFont(font)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            return baos.toByteArray();
        }
    }

    private com.itextpdf.layout.element.Cell tieuDeThongTinNhap(String text, PdfFont font) {
        return new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private com.itextpdf.layout.element.Cell noiDungThongTinNhap(PdfFont font, String text) {
        return new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9))
                .setTextAlignment(TextAlignment.LEFT);
    }

    private com.itextpdf.layout.element.Cell taoCellNhap(String text, PdfFont font, TextAlignment alignment) {
        return new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9))
                .setTextAlignment(alignment);
    }

    private void addSummaryRowNhap(Table table, String label, String value, PdfFont labelFont, PdfFont valueFont) {
        table.addCell(new com.itextpdf.layout.element.Cell().setBorder(null)
                .add(new Paragraph(label).setFont(labelFont).setFontSize(9)));
        table.addCell(new com.itextpdf.layout.element.Cell().setBorder(null)
                .add(new Paragraph(value).setFont(valueFont).setFontSize(9).setTextAlignment(TextAlignment.RIGHT)));
    }

    private void hienThiPdfTamThoiNhap(byte[] pdfData, DonNhapHang donNhapHang) throws IOException {
        if (pdfData == null || pdfData.length == 0) {
            throw new IOException("Dữ liệu PDF rỗng.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate ngayLap = Optional.ofNullable(donNhapHang)
                .map(DonNhapHang::getNgayNhap)
                .orElse(LocalDate.now());
        String datePart = ngayLap.format(formatter);

        String maDonNhap = Optional.ofNullable(donNhapHang)
                .map(DonNhapHang::getMaDonNhapHang)
                .orElse("");
        String numericCode = maDonNhap.replaceAll("\\D", "");
        String last4 = numericCode.length() >= 4
                ? numericCode.substring(numericCode.length() - 4)
                : (numericCode.isEmpty() ? String.format("%04d", (int) (Math.random() * 10000)) : numericCode);

        String baseFileName = String.format("phieu-nhap-%s-%s", datePart, last4);
        Path tempDir = Path.of(System.getProperty("java.io.tmpdir"));
        Path tempFile = tempDir.resolve(baseFileName + ".pdf");

        int counter = 1;
        while (Files.exists(tempFile)) {
            tempFile = tempDir.resolve(baseFileName + "-" + counter++ + ".pdf");
        }

        Files.write(tempFile, pdfData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
        tempFile.toFile().deleteOnExit();

        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new IOException("Máy tính không hỗ trợ mở PDF tự động.");
        }

        Desktop.getDesktop().open(tempFile.toFile());
    }

    /**
     * Tạo panel hiển thị nhóm thông tin (tiêu đề + nhiều dòng) theo chuẩn hóa
     * đơn
     */
    private javax.swing.JPanel createInfoSectionPanel(String title, String... lines) {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        javax.swing.JLabel lblTitle = new javax.swing.JLabel(title);
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(4));

        for (String line : lines) {
            javax.swing.JLabel lblLine = new javax.swing.JLabel(line);
            lblLine.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
            lblLine.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lblLine);
        }
        return panel;
    }

    /**
     * Tạo JPanel với label thông tin (giữ lại cho các nơi khác đang dùng)
     */
    private javax.swing.JPanel createInfoLabel(String title, String value, boolean bold) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel(title);
        lblTitle.setFont(new java.awt.Font("Segoe UI",
                bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 14));

        javax.swing.JLabel lblValue = new javax.swing.JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        panel.add(lblTitle);
        panel.add(lblValue);

        return panel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmPurchase;
    private javax.swing.JButton btnImportExcel;
    private javax.swing.JButton btnMa;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSupplierId;
    private javax.swing.JLabel lblSupplierId1;
    private javax.swing.JLabel lblSupplierId2;
    private javax.swing.JPanel pnLeft;
    private javax.swing.JPanel pnMid;
    private javax.swing.JPanel pnSanPham;
    private javax.swing.JTextField txtSearchProduct;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtSupplierId;
    private javax.swing.JTextField txtSupplierName;
    private javax.swing.JTextField txtTotalPrice;
    // End of variables declaration//GEN-END:variables

}

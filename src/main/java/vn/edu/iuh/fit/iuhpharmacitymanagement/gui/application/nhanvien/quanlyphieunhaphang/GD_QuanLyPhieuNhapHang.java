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
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    private NhanVien nhanVienHienTai; // Nhân viên đang đăng nhập
    private NhaCungCap nhaCungCapHienTai; // Nhà cung cấp được chọn/load từ Excel

    public GD_QuanLyPhieuNhapHang() {
        sanPhamDAO = new SanPhamDAO();
        sanPhamBUS = new SanPhamBUS(sanPhamDAO);
        donNhapHangBUS = new DonNhapHangBUS();
        chiTietDonNhapHangBUS = new ChiTietDonNhapHangBUS();
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
        panel.setBackground(new Color(240, 240, 240));
        panel.setMaximumSize(new java.awt.Dimension(32767, 40));
        panel.setPreferredSize(new java.awt.Dimension(1000, 40));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(5, 8, 5, 8);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // Headers: Hình → Tên SP → Lô hàng/HSD → Số lượng → Đơn giá → Tổng tiền → Xóa
        String[] headers = {"Hình", "Tên sản phẩm", "Lô hàng / HSD", "Số lượng", "Đơn giá nhập", "Tổng tiền", ""};
        double[] widths = {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        int[] minWidths = {80, 200, 170, 120, 130, 130, 90};

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

    private void themSanPhamVaoPanelNhap(SanPham sanPham, int soLuong, double donGiaNhap, Date hanDung, String loHang) throws Exception {

        if (kiemTraSanPhamDaTonTai(sanPham.getMaSanPham())) {
            throw new Exception("Sản phẩm '" + sanPham.getTenSanPham() + "' đã có trong danh sách nhập");
        }

        // Lấy số điện thoại nhà cung cấp hiện tại
        String soDienThoaiNCC = (nhaCungCapHienTai != null) ? nhaCungCapHienTai.getSoDienThoai() : null;

        Panel_ChiTietSanPhamNhap panelSP = new Panel_ChiTietSanPhamNhap(sanPham, soLuong, donGiaNhap, hanDung, loHang, soDienThoaiNCC);

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
        pnLeft.setPreferredSize(new java.awt.Dimension(485, 650));

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
                .addContainerGap(71, Short.MAX_VALUE)
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnLeftLayout.createSequentialGroup()
                        .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSearchSupplier, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(50, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnLeftLayout.createSequentialGroup()
                        .addComponent(btnConfirmPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(txtSearchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 425, Short.MAX_VALUE)
                .addComponent(btnConfirmPurchase, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        add(pnLeft, java.awt.BorderLayout.EAST);

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

            int colMaSP = -1, colSoLuong = -1, colDonGia = -1,
                    colHanDung = -1, colLoHang = -1;
            int colMaNCC = -1, colTenNCC = -1, colDiaChi = -1,
                    colSDT = -1, colEmail = -1, colMaSoThue = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) {
                    continue;
                }
                String header = getCellValueAsString(cell).trim().toLowerCase();

                if ((header.contains("số") && header.contains("đăng ký"))
                        || (header.contains("so") && header.contains("dang ky"))) {
                    colMaSP = i;
                } else if (header.contains("số lượng") || header.contains("so luong")) {
                    colSoLuong = i;
                } else if (header.contains("đơn giá") || header.contains("don gia")) {
                    colDonGia = i;
                } else if (header.contains("hạn") && (header.contains("dùng") || header.contains("sử dụng") || header.contains("su dung"))) {
                    colHanDung = i;
                } else if (header.contains("lô") && header.contains("hàng")) {
                    colLoHang = i; // Lưu index cột "Lô hàng"
                } // ═══════════════════════════════════════════════════════════════
                // CÁC CỘT NHÀ CUNG CẤP - ƯU TIÊN KIỂM TRA SĐT TRƯỚC (tránh nhầm với "Tên NCC")
                // ═══════════════════════════════════════════════════════════════
                else if (header.contains("sđt") || header.contains("sdt")) {
                    colSDT = i;
                } else if ((header.contains("số") || header.contains("so")) && header.contains("điện thoại")) {
                    // "Số điện thoại" hoặc "So dien thoai"
                    if (colSDT == -1) {
                        colSDT = i;
                    }
                } else if (header.contains("tên") && header.contains("ncc")) {
                    colTenNCC = i;
                } else if (header.contains("mã") && header.contains("ncc")) {
                    colMaNCC = i;
                } else if (header.contains("địa chỉ") || header.contains("dia chi")) {
                    colDiaChi = i;
                } else if (header.contains("email")) {
                    colEmail = i;
                } else if (header.contains("mã") && header.contains("thuế")) {
                    colMaSoThue = i;
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
                            // Điền thông tin nhà cung cấp vào form
                            if (nhaCungCap.getMaNhaCungCap() != null) {
                                txtSupplierId.setText(nhaCungCap.getMaNhaCungCap());
                            } else {
                                txtSupplierId.setText("(chưa có - sẽ tự sinh)");
                            }
                            txtSupplierName.setText(nhaCungCap.getTenNhaCungCap());
                        }
                    } catch (Exception ex) {
                        errors.append("Lỗi xử lý thông tin nhà cung cấp: ").append(ex.getMessage()).append("\n");
                    }
                }
            }

            // Đọc dữ liệu sản phẩm từ dòng 1 trở đi
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

                    String tenLoHang = null;
                    if (colLoHang != -1) {
                        tenLoHang = getCellValueAsString(row.getCell(colLoHang));
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

                    if (sanPham == null) {
                        errors.append("Dòng ").append(i + 1).append(": Không tìm thấy sản phẩm ").append(maSP).append("\n");
                        errorCount++;
                        continue;
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

                    try {
                        LoHangDAO loHangDAO = new LoHangDAO();
                        List<LoHang> dsLoHang = loHangDAO.findByMaSanPham(sanPham.getMaSanPham());

                        // Tìm lô hàng có cùng số đăng ký và cùng hạn sử dụng
                        List<LoHang> loTrungKhop = new ArrayList<>();
                        for (LoHang lo : dsLoHang) {
                            boolean trungSDK = maSP.equalsIgnoreCase(lo.getSanPham().getSoDangKy());

                            boolean trungHSD = false;
                            if (hanDung != null && lo.getHanSuDung() != null) {
                                LocalDate hsdExcel = new java.sql.Date(hanDung.getTime()).toLocalDate();
                                LocalDate hsdLoHang = lo.getHanSuDung();
                                trungHSD = hsdExcel.equals(hsdLoHang);
                            }

                            if (trungSDK && trungHSD) {
                                loTrungKhop.add(lo);
                            }
                        }

                        if (loTrungKhop.size() == 1) {
                        } else if (loTrungKhop.size() > 1) {
                        } else if (!dsLoHang.isEmpty()) {
                        }

                    } catch (Exception ex) {
                    }

                    // Thêm sản phẩm vào panel
                    // Truyền tên lô từ Excel (nếu có) để tự động điền vào form tạo lô mới
                    themSanPhamVaoPanelNhap(sanPham, soLuong, donGiaNhap, hanDung, tenLoHang);
                    successCount++;

                } catch (Exception e) {
                    errors.append("Dòng ").append(i + 1).append(": ").append(e.getMessage()).append("\n");
                    errorCount++;
                }
            }

            String message = "Import thành công " + successCount + " sản phẩm";
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
        String sdt = (colSDT != -1) ? getCellValueAsString(row.getCell(colSDT)) : null;
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

        }
        else if (tenNCC != null && !tenNCC.trim().isEmpty()) {
            ncc = nhaCungCapBUS.layNhaCungCapTheoTen(tenNCC.trim());

            if (ncc != null) {
                return ncc;
            }

        }
        else {
            throw new Exception("Phải có ít nhất TÊN hoặc SĐT nhà cung cấp!");
        }

        if (sdt != null && !sdt.trim().isEmpty() && !sdt.trim().matches(NhaCungCap.SO_DIEN_THOAI_REGEX)) {
            throw new Exception("Số điện thoại không đúng định dạng: " + sdt);
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

            double tongTien = 0;
            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                tongTien += panel.getTongTien();
            }
            donNhapHang.setThanhTien(tongTien);

            // Tạo danh sách chi tiết TẠM (chưa lưu vào DB)
            List<ChiTietDonNhapHang> danhSachChiTiet = new ArrayList<>();
            boolean allDetailsValid = true;

            Map<String, String> mapLoHangDaChon = new HashMap<>();
            Set<String> setSanPhamDaXuLy = new HashSet<>();

            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                SanPham sanPham = panel.getSanPham();
                int soLuong = panel.getSoLuong();
                double donGia = panel.getDonGiaNhap();
                double thanhTien = panel.getTongTien();

                String maSanPham = sanPham.getMaSanPham();
                if (setSanPhamDaXuLy.contains(maSanPham)) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Không thể nhập trùng sản phẩm '" + sanPham.getTenSanPham() + "'! Vui lòng xóa sản phẩm trùng.");
                    allDetailsValid = false;
                    continue;
                }

                LoHang loHang = panel.getLoHangDaChon();

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
                chiTiet.setThanhTien(thanhTien);
                chiTiet.setDonNhapHang(donNhapHang);
                chiTiet.setLoHang(loHang);

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
     * Hiển thị hóa đơn nhập hàng
     * @param donNhapHang Đơn nhập hàng (tạm, chưa lưu DB)
     * @param danhSachChiTiet Danh sách chi tiết (tạm, chưa lưu DB)
     * @param danhSachPanel Danh sách panel sản phẩm để lưu sau
     * @return true nếu hủy đơn, false nếu đã lưu
     */
    private boolean hienThiHoaDon(DonNhapHang donNhapHang, List<ChiTietDonNhapHang> danhSachChiTiet, List<Panel_ChiTietSanPhamNhap> danhSachPanel) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa đơn");
        dialog.setModal(true);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);

        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new java.awt.BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // === HEADER ===
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);

        javax.swing.JLabel lblTitle = new javax.swing.JLabel("HÓA ĐƠN NHẬP HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(20));

        // Thông tin đơn nhập
        javax.swing.JPanel infoPanel = new javax.swing.JPanel(new java.awt.GridLayout(4, 2, 10, 8));
        infoPanel.setBackground(Color.WHITE);

        infoPanel.add(createInfoLabel("Mã hóa đơn nhập: ", donNhapHang.getMaDonNhapHang(), true));
        infoPanel.add(createInfoLabel("Nhân viên: ", nhanVienHienTai.getMaNhanVien(), false));

        String ngayNhap = dateFormat.format(java.sql.Date.valueOf(donNhapHang.getNgayNhap()));
        infoPanel.add(createInfoLabel("Ngày lập phiếu nhập: ", ngayNhap, false));

        String dienThoai = nhaCungCapHienTai.getSoDienThoai() != null
                ? nhaCungCapHienTai.getSoDienThoai() : "";
        infoPanel.add(createInfoLabel("Nhà cung cấp: ", nhaCungCapHienTai.getTenNhaCungCap(), true));
        infoPanel.add(createInfoLabel("Điện thoại: ", dienThoai, false));
        infoPanel.add(new javax.swing.JLabel()); // Empty cell

        headerPanel.add(infoPanel);
        headerPanel.add(Box.createVerticalStrut(10));

        // Đường phân cách
        javax.swing.JSeparator separator1 = new javax.swing.JSeparator();
        headerPanel.add(separator1);

        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        // === BODY - Bảng chi tiết sản phẩm ===
        javax.swing.JPanel bodyPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        bodyPanel.setBackground(Color.WHITE);

        javax.swing.JLabel lblChiTiet = new javax.swing.JLabel("Chi tiết sản phẩm nhập");
        lblChiTiet.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblChiTiet.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bodyPanel.add(lblChiTiet, java.awt.BorderLayout.NORTH);

        String[] columnNames = {"Tên sản phẩm", "Đơn vị tính", "Số lô", "Số lượng", "Đơn giá", "Thành tiền"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(250); // Tên SP
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Đơn vị
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Số lô
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Số lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Đơn giá
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Thành tiền

        // Thêm dữ liệu vào bảng
        for (ChiTietDonNhapHang chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            SanPham sanPham = loHang.getSanPham();

            tableModel.addRow(new Object[]{
                sanPham.getTenSanPham(),
                sanPham.getDonViTinh() != null ? sanPham.getDonViTinh() : "Hộp",
                loHang.getTenLoHang(),
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                currencyFormat.format(chiTiet.getThanhTien()) + " đ"
            });
        }

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        bodyPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(bodyPanel, java.awt.BorderLayout.CENTER);

        // === FOOTER - Tổng tiền ===
        javax.swing.JPanel footerPanel = new javax.swing.JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        javax.swing.JSeparator separator2 = new javax.swing.JSeparator();
        footerPanel.add(separator2);
        footerPanel.add(Box.createVerticalStrut(15));

        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(Color.WHITE);

        javax.swing.JLabel lblTongTienText = new javax.swing.JLabel("Tổng hóa đơn: ");
        lblTongTienText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        javax.swing.JLabel lblTongTien = new javax.swing.JLabel(currencyFormat.format(donNhapHang.getThanhTien()) + " đ");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        lblTongTien.setForeground(new Color(220, 53, 69));

        tongTienPanel.add(lblTongTienText);
        tongTienPanel.add(lblTongTien);

        footerPanel.add(tongTienPanel);
        footerPanel.add(Box.createVerticalStrut(10));

        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Biến để theo dõi trạng thái
        final boolean[] isDonHangCancelled = {true}; // Mặc định là hủy
        final boolean[] isDonHangSaved = {false}; // Chưa lưu

        // Nút In (lưu vào DB + tăng tồn kho)
        javax.swing.JButton btnInHoaDon = new javax.swing.JButton("📄 In phiếu nhập");
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        btnInHoaDon.setPreferredSize(new java.awt.Dimension(200, 42));
        ButtonStyles.apply(btnInHoaDon, ButtonStyles.Type.PRIMARY);
        btnInHoaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInHoaDon.addActionListener(e -> {
            // Lưu vào DB và tăng tồn kho
            if (luuDonNhapHangVaoDB(donNhapHang, danhSachChiTiet)) {
                isDonHangSaved[0] = true;
                isDonHangCancelled[0] = false;
                // In hóa đơn
                inHoaDonNhapHang(donNhapHang, danhSachChiTiet);
                // Đóng dialog
                dialog.dispose();
            }
        });

        // Nút Hủy bỏ
        javax.swing.JButton btnHuyBo = new javax.swing.JButton("❌ Hủy bỏ");
        btnHuyBo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        btnHuyBo.setPreferredSize(new java.awt.Dimension(200, 42));
        ButtonStyles.apply(btnHuyBo, ButtonStyles.Type.DANGER);
        btnHuyBo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuyBo.addActionListener(e -> {
            isDonHangCancelled[0] = true;
            dialog.dispose();
        });

        buttonPanel.add(btnInHoaDon);
        buttonPanel.add(btnHuyBo);
        footerPanel.add(buttonPanel);

        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);

        // Trả về true nếu hủy, false nếu đã lưu
        return isDonHangCancelled[0];
    }

    /**
     * Lưu đơn nhập hàng và chi tiết vào database, đồng thời tăng tồn kho
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
                            "Lỗi khi lưu chi tiết đơn nhập hàng cho lô: " + 
                            (chiTiet.getLoHang() != null ? chiTiet.getLoHang().getTenLoHang() : "N/A"));
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        double tongTienHang = 0;
        for (ChiTietDonNhapHang chiTiet : danhSachChiTiet) {
            tongTienHang += chiTiet.getDonGia() * chiTiet.getSoLuong();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(baos);
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc, PageSize.A5)) {

            document.setMargins(24, 24, 24, 24);
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

            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{6, 30, 16, 12, 18, 18}))
                    .useAllAvailableWidth();
            String[] headerLabels = {"STT", "Ten san pham", "So lo", "SL", "Don gia", "Thanh tien"};
            for (String label : headerLabels) {
                itemsTable.addHeaderCell(new com.itextpdf.layout.element.Cell()
                        .add(new Paragraph(label)
                                .setFont(fontBold)
                                .setFontSize(9)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            int stt = 1;
            for (ChiTietDonNhapHang chiTiet : danhSachChiTiet) {
                LoHang loHang = chiTiet.getLoHang();
                SanPham sanPham = loHang != null ? loHang.getSanPham() : null;
                String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
                String soLo = loHang != null ? loHang.getTenLoHang() : "-";
                String donGia = currencyFormat.format(chiTiet.getDonGia()) + " đ";
                String thanhTien = currencyFormat.format(chiTiet.getThanhTien()) + " đ";

                itemsTable.addCell(taoCellNhap(String.valueOf(stt++), font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(tenSP, font, TextAlignment.LEFT));
                itemsTable.addCell(taoCellNhap(soLo, font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(String.valueOf(chiTiet.getSoLuong()), font, TextAlignment.CENTER));
                itemsTable.addCell(taoCellNhap(donGia, font, TextAlignment.RIGHT));
                itemsTable.addCell(taoCellNhap(thanhTien, font, TextAlignment.RIGHT));
            }

            document.add(itemsTable);
            document.add(new Paragraph("\n"));

            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER.CENTER)
                    .setWidth(UnitValue.createPercentValue(80));

            addSummaryRowNhap(summaryTable, "Tong tien hang:",
                    currencyFormat.format(tongTienHang) + " đ", font, fontBold);
            addSummaryRowNhap(summaryTable, "Thanh tien:",
                    currencyFormat.format(donNhapHang.getThanhTien()) + " đ", fontBold, fontBold);

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
     * Tạo JPanel với label thông tin
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

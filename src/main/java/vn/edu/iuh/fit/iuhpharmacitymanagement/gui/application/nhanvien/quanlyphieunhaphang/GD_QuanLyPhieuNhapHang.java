/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang;

import com.formdev.flatlaf.FlatClientProperties;
import raven.toast.Notifications;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
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
        lookAndFeelSet();
        setupPanelSanPham();
        setupSupplierSearchListener();

        txtSearchProduct.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí hoặc quét mã vạch");
        txtSearchSupplier.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại nhà cung cấp");
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
     * Thiết lập listener cho textfield tìm kiếm nhà cung cấp
     * Tự động tìm và hiển thị thông tin NCC khi nhập số điện thoại
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
     * Tìm kiếm nhà cung cấp theo số điện thoại
     * Tự động điền mã và tên nhà cung cấp khi tìm thấy
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
                // Tìm thấy → hiển thị thông tin
                txtSupplierId.setText(ncc.getMaNhaCungCap() != null ? ncc.getMaNhaCungCap() : "");
                txtSupplierName.setText(ncc.getTenNhaCungCap() != null ? ncc.getTenNhaCungCap() : "");
                nhaCungCapHienTai = ncc;
                
                // Đổi màu nền sang xanh nhạt để báo hiệu tìm thấy
                txtSearchSupplier.setBackground(new Color(220, 255, 220)); // Light green
            } else {
                // Không tìm thấy → xóa thông tin, đổi màu nền sang vàng nhạt
                txtSupplierId.setText("");
                txtSupplierName.setText("");
                nhaCungCapHienTai = null;
                txtSearchSupplier.setBackground(new Color(255, 255, 200)); // Light yellow
            }
        } catch (Exception e) {
            // Lỗi khi tìm kiếm
            txtSupplierId.setText("");
            txtSupplierName.setText("");
            nhaCungCapHienTai = null;
            txtSearchSupplier.setBackground(Color.WHITE);
            System.err.println("Lỗi khi tìm nhà cung cấp: " + e.getMessage());
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
        
        // ✅ KIỂM TRA TRÙNG LẶP: Nếu sản phẩm đã tồn tại → bỏ qua
        if (kiemTraSanPhamDaTonTai(sanPham.getMaSanPham())) {
            System.out.println("⚠ Sản phẩm " + sanPham.getMaSanPham() + " đã tồn tại trong danh sách → BỎ QUA");
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

    @SuppressWarnings("unchecked")
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

        btnMa.setBackground(new java.awt.Color(115, 165, 71));
        btnMa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMa.setForeground(new java.awt.Color(255, 255, 255));
        btnMa.setText("Nhập");
        btnMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaActionPerformed(evt);
            }
        });

        btnImportExcel.setBackground(new java.awt.Color(0, 123, 255));
        btnImportExcel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnImportExcel.setForeground(new java.awt.Color(255, 255, 255));
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
                    "❌ KHÔNG THỂ THÊM!\n" +
                    "Sản phẩm '" + sanPham.getTenSanPham() + "' (SDK: " + soDangKy + ")\n" +
                    "đã được nhập bởi nhà cung cấp " + nccDaNhap + ".\n" +
                    "Không thể nhập từ nhà cung cấp khác!");
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
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int errorCount = 0;
            boolean nhaCungCapDaTao = false; // Đánh dấu nhà cung cấp đã được tạo
            StringBuilder errors = new StringBuilder();
            
            // Đọc header row để lấy vị trí các cột
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "File Excel không có dòng tiêu đề!");
                return;
            }
            
            // Tìm index của các cột theo header (sản phẩm + nhà cung cấp)
            int colMaSP = -1, colSoLuong = -1, colDonGia = -1, 
                colHanDung = -1, colLoHang = -1;
            int colMaNCC = -1, colTenNCC = -1, colDiaChi = -1, 
                colSDT = -1, colEmail = -1, colMaSoThue = -1;
            
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) continue;
                String header = getCellValueAsString(cell).trim().toLowerCase();
                
                System.out.println("→ Cột " + i + ": [" + header + "]"); // Debug
                
                // Các cột sản phẩm
                if ((header.contains("số") && header.contains("đăng ký")) || 
                    (header.contains("so") && header.contains("dang ky"))) {
                    colMaSP = i;
                } else if (header.contains("số lượng") || header.contains("so luong")) {
                    colSoLuong = i;
                } else if (header.contains("đơn giá") || header.contains("don gia")) {
                    colDonGia = i;
                } else if (header.contains("hạn") && (header.contains("dùng") || header.contains("sử dụng") || header.contains("su dung"))) {
                    colHanDung = i;
                } else if (header.contains("lô") && header.contains("hàng")) {
                    colLoHang = i; // Lưu index cột "Lô hàng"
                }
                // ═══════════════════════════════════════════════════════════════
                // CÁC CỘT NHÀ CUNG CẤP - ƯU TIÊN KIỂM TRA SĐT TRƯỚC (tránh nhầm với "Tên NCC")
                // ═══════════════════════════════════════════════════════════════
                else if (header.contains("sđt") || header.contains("sdt")) {
                    colSDT = i;
                } else if ((header.contains("số") || header.contains("so")) && header.contains("điện thoại")) {
                    // "Số điện thoại" hoặc "So dien thoai"
                    if (colSDT == -1) colSDT = i;
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
            
            // Kiểm tra các cột bắt buộc
            if (colMaSP == -1 || colSoLuong == -1 || colDonGia == -1) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "File Excel thiếu các cột bắt buộc: Số đăng ký, Số lượng, Đơn giá nhập!");
                return;
            }
            
            // Đọc thông tin nhà cung cấp từ dòng đầu tiên (nếu có)
            NhaCungCap nhaCungCap = null;
            if (sheet.getLastRowNum() > 0) {
                Row firstDataRow = sheet.getRow(1);
                if (firstDataRow != null) {
                    try {
                        System.out.println("\n═══ ĐỌC THÔNG TIN NHÀ CUNG CẤP TỪ EXCEL ═══");
                        System.out.println("→ Index cột Tên NCC: " + colTenNCC);
                        System.out.println("→ Index cột SĐT: " + colSDT);
                        System.out.println("→ Index cột Địa chỉ: " + colDiaChi);
                        System.out.println("→ Index cột Email: " + colEmail);
                        
                        if (colTenNCC != -1) {
                            String tenNCCDebug = getCellValueAsString(firstDataRow.getCell(colTenNCC));
                            System.out.println("→ Tên NCC từ Excel: [" + tenNCCDebug + "]");
                        }
                        if (colSDT != -1) {
                            String sdtDebug = getCellValueAsString(firstDataRow.getCell(colSDT));
                            System.out.println("→ SĐT từ Excel: [" + sdtDebug + "]");
                        }
                        if (colDiaChi != -1) {
                            String diaChiDebug = getCellValueAsString(firstDataRow.getCell(colDiaChi));
                            System.out.println("→ Địa chỉ từ Excel: [" + diaChiDebug + "]");
                        }
                        if (colEmail != -1) {
                            String emailDebug = getCellValueAsString(firstDataRow.getCell(colEmail));
                            System.out.println("→ Email từ Excel: [" + emailDebug + "]");
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
                if (row == null) continue;
                
                try {
                    // Đọc dữ liệu từ Excel theo header
                    String maSP = getCellValueAsString(row.getCell(colMaSP));
                    if (maSP == null || maSP.isEmpty()) continue;
                    
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
                    
                    // Đọc tên lô từ cột "Lô hàng" (nếu có)
                    String tenLoHang = null;
                    if (colLoHang != -1) {
                        tenLoHang = getCellValueAsString(row.getCell(colLoHang));
                        System.out.println("📦 [EXCEL] Tên lô từ cột 'Lô hàng': [" + tenLoHang + "]");
                    } else {
                        System.out.println("⚠ [EXCEL] Không có cột 'Lô hàng'");
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
                    
                    // ═══════════════════════════════════════════════════════════════════
                    // KIỂM TRA BUSINESS RULE: Số đăng ký chỉ được nhập bởi 1 nhà cung cấp
                    // ═══════════════════════════════════════════════════════════════════
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
                    
                    // ═══════════════════════════════════════════════════════════════════
                    // KIỂM TRA XEM CÓ LÔ HÀNG NÀO TRÙNG HOÀN TOÀN (số đăng ký + hạn sử dụng)
                    // ═══════════════════════════════════════════════════════════════════
                    String maLoHangTuDong = null;
                    try {
                        // Lấy danh sách lô hàng của sản phẩm này
                        LoHangDAO loHangDAO = new LoHangDAO();
                        List<LoHang> dsLoHang = loHangDAO.findByMaSanPham(sanPham.getMaSanPham());
                        
                        System.out.println("→ Kiểm tra tự động chọn lô cho SP: " + sanPham.getTenSanPham());
                        System.out.println("   - Số đăng ký từ Excel: " + maSP);
                        System.out.println("   - HSD từ Excel: " + (hanDung != null ? new SimpleDateFormat("dd/MM/yyyy").format(hanDung) : "null"));
                        System.out.println("   - Tìm thấy " + dsLoHang.size() + " lô hàng");
                        
                        // Tìm lô hàng có CÙNG số đăng ký VÀ CÙNG hạn sử dụng
                        List<LoHang> loTrungKhop = new ArrayList<>();
                        for (LoHang lo : dsLoHang) {
                            boolean trungSDK = maSP.equalsIgnoreCase(lo.getSanPham().getSoDangKy());
                            
                            // So sánh ngày: Chuyển java.util.Date → LocalDate
                            boolean trungHSD = false;
                            if (hanDung != null && lo.getHanSuDung() != null) {
                                // Chuyển Date → LocalDate
                                LocalDate hsdExcel = new java.sql.Date(hanDung.getTime()).toLocalDate();
                                LocalDate hsdLoHang = lo.getHanSuDung();
                                trungHSD = hsdExcel.equals(hsdLoHang);
                            }
                            
                            System.out.println("   - Lô " + lo.getMaLoHang() + ": SDK=" + trungSDK + ", HSD=" + trungHSD);
                            
                            if (trungSDK && trungHSD) {
                                loTrungKhop.add(lo);
                            }
                        }
                        
                        if (loTrungKhop.size() == 1) {
                            // Chỉ có 1 lô trùng hoàn toàn → TỰ ĐỘNG CHỌN
                            maLoHangTuDong = loTrungKhop.get(0).getMaLoHang();
                        } else if (loTrungKhop.size() > 1) {
                            // Có nhiều lô trùng → Để user chọn
                            System.out.println("   ⚠ Có " + loTrungKhop.size() + " lô trùng → Để user chọn");
                        } else if (!dsLoHang.isEmpty()) {
                            // Có lô cùng sản phẩm nhưng khác HSD → Để user chọn
                            System.out.println("   ⚠ Có lô cùng SP nhưng khác HSD → Để user chọn");
                        } else {
                            // Không có lô nào → Tạo mới
                            System.out.println("   ℹ Không có lô nào → Sẽ tạo lô mới");
                        }
                        
                    } catch (Exception ex) {
                        System.err.println("Lỗi kiểm tra lô hàng: " + ex.getMessage());
                        ex.printStackTrace();
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
            
            // Hiển thị kết quả
            String message = "✓ Import thành công " + successCount + " sản phẩm";
            if (nhaCungCapDaTao && nhaCungCap != null) {
                message += "\n✓ Đã tải thông tin nhà cung cấp: " + nhaCungCap.getTenNhaCungCap();
                if (nhaCungCap.getMaNhaCungCap() == null) {
                    message += " (chưa có trong DB, sẽ tạo khi nhập)";
                }
            }
            if (errorCount > 0) {
                message += "\n\n⚠ Có " + errorCount + " lỗi:\n" + errors.toString();
            }
            
            Notifications.getInstance().show(
                errorCount > 0 ? Notifications.Type.WARNING : Notifications.Type.SUCCESS, 
                Notifications.Location.TOP_CENTER,
                message);
            
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "Lỗi đọc file Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
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
        if (cell == null) return 0;
        
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
            // Mặc định 2 năm sau
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 2);
            return cal.getTime();
        }
        
        System.out.println("📅 [EXCEL] Cell Type: " + cell.getCellType());
        System.out.println("📅 [EXCEL] Cell Value: " + cell);
        
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            System.out.println("📅 [EXCEL] Đọc DATE từ NUMERIC cell: " + dateFormat.format(date));
            return date;
        } else if (cell.getCellType() == CellType.STRING) {
            String dateStr = cell.getStringCellValue();
            System.out.println("📅 [EXCEL] Đọc STRING từ cell: '" + dateStr + "'");
            try {
                Date date = dateFormat.parse(dateStr);
                System.out.println("📅 [EXCEL] Parse thành DATE: " + dateFormat.format(date));
                return date;
            } catch (Exception e) {
                System.out.println("❌ [EXCEL] Parse FAILED: " + e.getMessage());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 2);
                return cal.getTime();
            }
        }
        
        System.out.println("⚠️ [EXCEL] Cell type không hợp lệ, dùng default 2 năm sau");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        return cal.getTime();
    }
    
    /**
     * Xử lý thông tin nhà cung cấp từ Excel:
     * - Tìm NCC trong DB theo tên hoặc SĐT
     * - Nếu không tìm thấy: TẠO OBJECT TẠM (chưa lưu vào DB)
     * - Sẽ lưu vào DB khi bấm nút "Nhập hàng"
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
        if ((maNCC == null || maNCC.trim().isEmpty()) && 
            (tenNCC == null || tenNCC.trim().isEmpty())) {
            return null;
        }
        
        // Khởi tạo NhaCungCapBUS nếu chưa có
        if (nhaCungCapBUS == null) {
            nhaCungCapBUS = new NhaCungCapBUS();
        }
        
        NhaCungCap ncc = null;
        
        // ═══════════════════════════════════════════════════════════════
        // LOGIC MỚI: Ưu tiên tìm theo SĐT TRƯỚC (key chính xác nhất)
        // ═══════════════════════════════════════════════════════════════
        
        // ✅ ƯU TIÊN 1: Tìm theo SĐT (nếu có SĐT trong Excel)
        if (sdt != null && !sdt.trim().isEmpty()) {
            System.out.println("→ Tìm nhà cung cấp theo SĐT: " + sdt);
            ncc = nhaCungCapBUS.layNhaCungCapTheoSoDienThoai(sdt.trim());
            
        if (ncc != null) {
                // Cảnh báo nếu tên trong Excel khác với DB (nhưng vẫn cho phép)
                if (tenNCC != null && !tenNCC.trim().isEmpty() && 
                    !tenNCC.trim().equalsIgnoreCase(ncc.getTenNhaCungCap())) {
                    System.out.println("⚠ LƯU Ý: Tên NCC trong Excel là '" + tenNCC + 
                        "', nhưng trong DB là '" + ncc.getTenNhaCungCap() + "' → Sử dụng thông tin từ DB");
            }
            
            return ncc;
        }
        
            // Không tìm thấy theo SĐT → Sẽ tạo mới (xử lý ở cuối hàm)
            System.out.println("→ Không tìm thấy NCC với SĐT: " + sdt + " → Sẽ tạo mới");
        }
        
        // ✅ ƯU TIÊN 2: Nếu KHÔNG CÓ SĐT, thử tìm theo TÊN
        else if (tenNCC != null && !tenNCC.trim().isEmpty()) {
            System.out.println("→ Không có SĐT trong Excel, tìm theo tên: " + tenNCC);
            ncc = nhaCungCapBUS.layNhaCungCapTheoTen(tenNCC.trim());
            
            if (ncc != null) {
                return ncc;
            }
            
            System.out.println("→ Không tìm thấy NCC với tên: " + tenNCC + " → Sẽ tạo mới");
        }
        
        // ⚠️ Nếu KHÔNG CÓ TÊN và KHÔNG CÓ SĐT → Báo lỗi
        else {
            throw new Exception("Phải có ít nhất TÊN hoặc SĐT nhà cung cấp!");
        }
        
        // ═══════════════════════════════════════════════════════════════
        // Không tìm thấy → TẠO MỚI NHÀ CUNG CẤP
        // ═══════════════════════════════════════════════════════════════
        
        System.out.println("→ Không tìm thấy nhà cung cấp trong database → Tạo mới");
        System.out.println("   - Tên NCC: " + (tenNCC != null ? tenNCC : "(trống)"));
        System.out.println("   - SĐT: " + (sdt != null ? sdt : "(trống)"));
        
        // ✅ Validate dữ liệu trước khi tạo
        if (sdt != null && !sdt.trim().isEmpty() && !sdt.trim().matches(NhaCungCap.SO_DIEN_THOAI_REGEX)) {
            throw new Exception("Số điện thoại không đúng định dạng: " + sdt);
        }
        
        if (email != null && !email.trim().isEmpty() && !email.trim().matches(NhaCungCap.EMAIL_REGEX)) {
            throw new Exception("Email không đúng định dạng: " + email);
        }
        
        // ✅ Tên NCC: Nếu không có → Dùng SĐT làm tên tạm
        String tenNCCMoi = (tenNCC != null && !tenNCC.trim().isEmpty()) 
            ? tenNCC.trim() 
            : ("NCC_" + (sdt != null ? sdt.trim() : "UNKNOWN"));
        
        // Tạo nhà cung cấp TẠM (chưa lưu DB)
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
        
        // KHÔNG LƯU VÀO DB - chỉ return object
        System.out.println("✓ Đã tạo object NCC tạm: " + nccTam.getTenNhaCungCap() + " (chưa lưu DB)");
        return nccTam;
    }

    private void btnConfirmPurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmPurchaseActionPerformed
        try {
            System.out.println("\n=== BẮT ĐẦU XỬ LÝ NHẬP HÀNG ===");
            
            // Kiểm tra có sản phẩm nào không
            List<Panel_ChiTietSanPhamNhap> danhSachPanel = new ArrayList<>();
            for (Component comp : pnSanPham.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPhamNhap) {
                    danhSachPanel.add((Panel_ChiTietSanPhamNhap) comp);
                }
            }
            
            System.out.println("→ Số sản phẩm: " + danhSachPanel.size());
            
            if (danhSachPanel.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Chưa có sản phẩm nào để nhập!");
                return;
            }
            
            // Kiểm tra nhà cung cấp
            System.out.println("→ Kiểm tra NCC: " + (nhaCungCapHienTai != null ? nhaCungCapHienTai.getTenNhaCungCap() : "NULL"));
            if (nhaCungCapHienTai == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Chưa có thông tin nhà cung cấp! Vui lòng import từ Excel hoặc chọn nhà cung cấp.");
                return;
            }
            
            // Nếu NCC chưa có mã (chưa lưu DB), tạo mới ngay bây giờ
            if (nhaCungCapHienTai.getMaNhaCungCap() == null) {
                System.out.println("→ NCC chưa có trong DB, đang tạo mới...");
                boolean nccCreated = nhaCungCapBUS.taoNhaCungCap(nhaCungCapHienTai);
                if (!nccCreated) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Lỗi khi tạo nhà cung cấp mới!");
                    return;
                }
                // Đọc lại để lấy mã vừa sinh
                NhaCungCap nccDaLuu = nhaCungCapBUS.layNhaCungCapTheoTen(nhaCungCapHienTai.getTenNhaCungCap());
                if (nccDaLuu == null) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Lỗi: Không thể đọc lại nhà cung cấp vừa tạo!");
                    return;
                }
                nhaCungCapHienTai = nccDaLuu;
                System.out.println("✓ Đã tạo NCC mới: " + nccDaLuu.getMaNhaCungCap() + " - " + nccDaLuu.getTenNhaCungCap());
                
                // Cập nhật mã NCC lên form
                txtSupplierId.setText(nccDaLuu.getMaNhaCungCap());
            }
            
            // Tạo đơn nhập hàng
            DonNhapHang donNhapHang = new DonNhapHang();
            donNhapHang.setNgayNhap(java.time.LocalDate.now());
            donNhapHang.setNhanVien(nhanVienHienTai);
            donNhapHang.setNhaCungCap(nhaCungCapHienTai);
            
            // Tính tổng tiền
            double tongTien = 0;
            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                tongTien += panel.getTongTien();
            }
            donNhapHang.setThanhTien(tongTien);
            
            // Lưu đơn nhập hàng vào DB
            boolean savedDonNhap = donNhapHangBUS.taoDonNhapHang(donNhapHang);
            
            if (!savedDonNhap) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi lưu đơn nhập hàng!");
                return;
            }
            
            System.out.println("✓ Đã lưu đơn nhập hàng: " + donNhapHang.getMaDonNhapHang());
            
            // Lưu chi tiết đơn nhập hàng
            List<ChiTietDonNhapHang> danhSachChiTiet = new ArrayList<>();
            boolean allDetailsSaved = true;
            
            // Map để kiểm tra lô đã được chọn
            java.util.Map<String, String> mapLoHangDaChon = new java.util.HashMap<>();
            
            // ✅ Map để kiểm tra sản phẩm trùng lặp (1 đơn nhập không được có 2 dòng cùng mã SP)
            java.util.Set<String> setSanPhamDaXuLy = new java.util.HashSet<>();
            
            for (Panel_ChiTietSanPhamNhap panel : danhSachPanel) {
                SanPham sanPham = panel.getSanPham();
                int soLuong = panel.getSoLuong();
                double donGia = panel.getDonGiaNhap();
                double thanhTien = panel.getTongTien();
                
                // ✅ VALIDATE: Kiểm tra sản phẩm đã tồn tại trong đơn nhập này chưa
                String maSanPham = sanPham.getMaSanPham();
                if (setSanPhamDaXuLy.contains(maSanPham)) {
                    System.out.println("✗ Sản phẩm '" + sanPham.getTenSanPham() + "' đã có trong đơn nhập này!");
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Không thể nhập trùng sản phẩm '" + sanPham.getTenSanPham() + "'! Vui lòng xóa sản phẩm trùng.");
                    allDetailsSaved = false;
                    continue;
                }
                
                // ✅ LẤY LÔ ĐÃ CHỌN TỪ PANEL (User phải chọn thủ công qua nút "Chọn lô")
                LoHang loHang = panel.getLoHangDaChon();
                
                // Nếu chưa chọn lô → Bắt buộc phải chọn
                if (loHang == null) {
                    System.out.println("✗ Chưa chọn lô cho sản phẩm: " + sanPham.getTenSanPham());
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng chọn lô hàng cho sản phẩm: " + sanPham.getTenSanPham());
                    allDetailsSaved = false;
                    continue;
                }
                
                // ✅ VALIDATE: Kiểm tra lô phải cùng sản phẩm
                if (loHang.getSanPham() == null || 
                    !loHang.getSanPham().getMaSanPham().equals(sanPham.getMaSanPham())) {
                    System.out.println("✗ Lô '" + loHang.getTenLoHang() + "' không thuộc sản phẩm: " + sanPham.getTenSanPham());
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                            Notifications.Location.TOP_CENTER,
                        "Lô không đúng sản phẩm! Vui lòng chọn lại.");
                    allDetailsSaved = false;
                    continue;
                }
                
                // ✅ VALIDATE: Kiểm tra lô đã được chọn ở panel khác chưa
                String maLoHang = loHang.getMaLoHang();
                if (mapLoHangDaChon.containsKey(maLoHang)) {
                    String tenSPTruoc = mapLoHangDaChon.get(maLoHang);
                    System.out.println("✗ Lô '" + loHang.getTenLoHang() + "' đã được chọn cho sản phẩm: " + tenSPTruoc);
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Lô này đã được chọn! Vui lòng chọn lô khác.");
                    allDetailsSaved = false;
                    continue;
                }
                
                // Đánh dấu lô và sản phẩm đã được xử lý
                mapLoHangDaChon.put(maLoHang, sanPham.getTenSanPham());
                setSanPhamDaXuLy.add(maSanPham);
                        
                        // Kiểm tra HSD phải > 6 tháng
                LocalDate hsd = loHang.getHanSuDung();
                        LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
                        if (hsd.isBefore(ngayGioiHan) || hsd.isEqual(ngayGioiHan)) {
                    System.out.println("✗ HSD không hợp lệ (≤ 6 tháng) cho lô: " + loHang.getTenLoHang());
                            Notifications.getInstance().show(Notifications.Type.WARNING,
                                Notifications.Location.TOP_CENTER,
                        "HSD của lô '" + loHang.getTenLoHang() + "' phải lớn hơn 6 tháng!");
                    allDetailsSaved = false;
                    continue;
                }
                
                // Tạo chi tiết đơn nhập
                ChiTietDonNhapHang chiTiet = new ChiTietDonNhapHang();
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(donGia);
                chiTiet.setThanhTien(thanhTien);
                chiTiet.setDonNhapHang(donNhapHang);
                chiTiet.setLoHang(loHang);
                
                boolean chiTietSaved = chiTietDonNhapHangBUS.themChiTietDonNhapHang(chiTiet);
                if (chiTietSaved) {
                    danhSachChiTiet.add(chiTiet);
                    System.out.println("✓ Đã lưu chi tiết: " + sanPham.getTenSanPham() + " - Lô: " + loHang.getTenLoHang());
                } else {
                    allDetailsSaved = false;
                    System.out.println("✗ Lỗi lưu chi tiết: " + sanPham.getTenSanPham());
                }
            }
            
            if (allDetailsSaved && !danhSachChiTiet.isEmpty()) {
                // Hiển thị hóa đơn
                hienThiHoaDon(donNhapHang, danhSachChiTiet);
                
                // Clear form sau khi lưu thành công
                xoaToanBoSanPham();
                nhaCungCapHienTai = null;
                txtSupplierId.setText("");
                txtSupplierName.setText("");
                txtTotalPrice.setText("0 đ");
                
                Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                    Notifications.Location.TOP_CENTER,
                    "Nhập hàng thành công! Mã đơn nhập: " + donNhapHang.getMaDonNhapHang());
            } else {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Có lỗi khi lưu một số chi tiết đơn nhập!");
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
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
     */
    private void hienThiHoaDon(DonNhapHang donNhapHang, List<ChiTietDonNhapHang> danhSachChiTiet) {
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
        
        String dienThoai = nhaCungCapHienTai.getSoDienThoai() != null ? 
                nhaCungCapHienTai.getSoDienThoai() : "";
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
        
        mainPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
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

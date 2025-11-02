/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlylohang;

import com.formdev.flatlaf.FlatClientProperties;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyLoHang extends javax.swing.JPanel {

    private final LoHangBUS loHangBUS;
    private final SanPhamBUS sanPhamBUS;
    private TableDesign tableDesign;
    private List<SanPham> danhSachSanPham;

    public GD_QuanLyLoHang() {
        this.loHangBUS = new LoHangBUS();
        this.sanPhamBUS = new SanPhamBUS(new SanPhamDAO());
        initComponents();
        
        styleButton(btnAdd, "THÊM");
        styleButton(btnUpdate, "SỬA");
        styleButton(btnDelete, "Xóa");
        styleButton(btnSearch, "Tìm kiếm");
        
        setUIManager();
        loadSanPham();
        fillTable();
    }
    
    private void styleButton(javax.swing.JButton button, String text) {
        button.setText(text);
        button.setFont(new java.awt.Font("Segoe UI", 1, 14));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        button.setPreferredSize(new java.awt.Dimension(95, 40));

        String style = ""
                + "arc:10;"
                + "borderWidth:0;"
                + "focusWidth:0;";

        if (text.equalsIgnoreCase("THÊM")) {
            style += ""
                    + "background:#28A745;" // Xanh lá
                    + "foreground:#FFFFFF;"
                    + "hoverBackground:#218838;"
                    + "pressedBackground:#1E7E34;";
            button.putClientProperty(FlatClientProperties.STYLE, style);
        } else if (text.equalsIgnoreCase("SỬA")) {
            style += ""
                    + "background:#007BFF;" // Xanh dương
                    + "foreground:#FFFFFF;"
                    + "hoverBackground:#0069D9;"
                    + "pressedBackground:#0056B3;";
            button.putClientProperty(FlatClientProperties.STYLE, style);
        } else if (text.equalsIgnoreCase("XÓA")) {
            style += ""
                    + "background:#DC3545;" // Đỏ
                    + "foreground:#FFFFFF;"
                    + "hoverBackground:#C82333;"
                    + "pressedBackground:#BD2130;";
            button.putClientProperty(FlatClientProperties.STYLE, style);
        } else if (text.equalsIgnoreCase("Tìm kiếm")) {
            button.setPreferredSize(new java.awt.Dimension(150, 40));
            style += ""
                    + "background:#28A745;" // Xanh lá giống nút THÊM
                    + "foreground:#FFFFFF;"
                    + "hoverBackground:#218838;"
                    + "pressedBackground:#1E7E34;";
            button.putClientProperty(FlatClientProperties.STYLE, style);
        }
    }

    private void setUIManager() {
        txtBarcodeAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Quét mã vạch sản phẩm (Số đăng ký)");
        txtBarcodeEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Quét mã vạch sản phẩm (Số đăng ký)");
            txtBatchNameAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên lô hàng");
        txtBatchNameEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên lô hàng");
        txtStockAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số lượng tồn kho");
        txtStockEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số lượng tồn kho");
        txtSearchBatch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo mã lô hàng, tên lô hàng");
        
        // Thêm viền cho các TextField
        txtBarcodeAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtBarcodeEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtBatchNameAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtBatchNameEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtStockAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtStockEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSearchBatch.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        UIManager.put("Button.arc", 10);
    }
    
    // Load danh sách sản phẩm
    private void loadSanPham() {
        try {
            danhSachSanPham = sanPhamBUS.layTatCaSanPham();
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
        }
    }
    
    // Tìm sản phẩm từ mã vạch (số đăng ký) và hiển thị tên
    private SanPham findProductByBarcode(String barcode, javax.swing.JLabel lblProductName) {
        if (barcode == null || barcode.trim().isEmpty()) {
            lblProductName.setText("Chưa quét mã");
            lblProductName.setForeground(new java.awt.Color(150, 150, 150));
            return null;
        }
        
        try {
            String soDangKy = barcode.trim();
            
            // 🔍 DEBUG: In ra console để kiểm tra
            System.out.println("=================================");
            System.out.println("🔍 ĐANG TÌM KIẾM SẢN PHẨM:");
            System.out.println("   - Input gốc: '" + barcode + "' (length: " + barcode.length() + ")");
            System.out.println("   - Sau trim: '" + soDangKy + "' (length: " + soDangKy.length() + ")");
            System.out.println("   - Byte array: " + java.util.Arrays.toString(soDangKy.getBytes()));
            
            // Tìm sản phẩm theo số đăng ký (như bán hàng)
            java.util.Optional<SanPham> optionalSP = sanPhamBUS.timSanPhamTheoSoDangKy(soDangKy);
            
            if (optionalSP.isPresent()) {
                SanPham sanPham = optionalSP.get();
                System.out.println("✅ TÌM THẤY: " + sanPham.getTenSanPham());
                System.out.println("   - Mã SP: " + sanPham.getMaSanPham());
                System.out.println("   - Số ĐK: " + sanPham.getSoDangKy());
                System.out.println("=================================");
                
                lblProductName.setText("✓ " + sanPham.getTenSanPham());
                lblProductName.setForeground(new java.awt.Color(34, 139, 34)); // Màu xanh lá
                return sanPham;
            } else {
                System.out.println("❌ KHÔNG TÌM THẤY sản phẩm với số ĐK: '" + soDangKy + "'");
                System.out.println("   - Hãy kiểm tra database xem có sản phẩm nào với số đăng ký này không");
                System.out.println("=================================");
                
                lblProductName.setText("❌ Không tìm thấy SP (số ĐK: " + soDangKy + ")");
                lblProductName.setForeground(new java.awt.Color(220, 53, 69)); // Màu đỏ
                return null;
            }
        } catch (Exception e) {
            System.out.println("❌ LỖI KHI TÌM KIẾM: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=================================");
            
            lblProductName.setText("❌ Lỗi: " + e.getMessage());
            lblProductName.setForeground(new java.awt.Color(220, 53, 69));
            return null;
        }
    }
    
    // Biến lưu sản phẩm đã chọn (thay thế cho combobox)
    private SanPham selectedProductAdd = null;
    private SanPham selectedProductEdit = null;
    
    // ComboBox lọc trạng thái lô hàng
    private javax.swing.JComboBox<String> cboFilterStatus;

    private void fillTable() {
        String[] headers = {"Mã lô hàng", "Tên lô hàng", "Hạn sử dụng", "Tồn kho", "Trạng thái"};
        List<Integer> tableWidths = Arrays.asList(150, 300, 200, 150, 150);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        
        // Load dữ liệu lô hàng từ database
        loadBatchData();
    }
    
    private void loadBatchData() {
        try {
            // Xóa dữ liệu cũ trong bảng
            tableDesign.getModelTable().setRowCount(0);
            
            // Lấy danh sách lô hàng từ BUS (đã tự động cập nhật trạng thái hết hạn)
            List<LoHang> danhSachLH = loHangBUS.getAllLoHang();
            
            // Thêm từng lô hàng vào bảng
            for (LoHang lh : danhSachLH) {
                // Kiểm tra và cập nhật trạng thái hết hạn: HSD <= 6 tháng → Set hết hạn
                LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
                boolean isExpired = lh.getHanSuDung().isBefore(ngayGioiHan) || 
                                    lh.getHanSuDung().isEqual(ngayGioiHan);
                String trangThai = isExpired ? "Hết hạn" : "Còn hạn";
                
                // Nếu trạng thái thay đổi, cập nhật vào database
                if (isExpired && lh.isTrangThai()) {
                    lh.setTrangThai(false);
                    try {
                        loHangBUS.capNhatLoHang(lh);
                    } catch (Exception ex) {
                        System.err.println("Lỗi khi cập nhật trạng thái lô hàng: " + ex.getMessage());
                    }
                }
                
                tableDesign.getModelTable().addRow(new Object[]{
                    lh.getMaLoHang(),
                    lh.getTenLoHang(),
                    lh.getHanSuDung(),
                    lh.getTonKho(),
                    trangThai
                });
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi tải dữ liệu lô hàng: " + e.getMessage());
        }
    }
    
    /**
     * Phương thức public để refresh dữ liệu lô hàng từ bên ngoài
     * Được gọi khi có thay đổi từ module khác (ví dụ: import Excel từ phiếu nhập)
     */
    public void refreshData() {
        loadBatchData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modalAddBatch = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtBarcodeAdd = new javax.swing.JTextField();
        lblProductNameAdd = new javax.swing.JLabel();
        txtBatchNameAdd = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        dateExpiryAdd = new com.toedter.calendar.JDateChooser();
        txtStockAdd = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        btnAddBatch = new javax.swing.JButton();
        btnExitModalAdd = new javax.swing.JButton();
        modalEditBatch = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtBarcodeEdit = new javax.swing.JTextField();
        lblProductNameEdit = new javax.swing.JLabel();
        txtBatchNameEdit = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        dateExpiryEdit = new com.toedter.calendar.JDateChooser();
        txtStockEdit = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        btnEditBatch = new javax.swing.JButton();
        btnExitModalEdit = new javax.swing.JButton();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtSearchBatch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        actionPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();

        modalAddBatch.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalAddBatch.setTitle("Thêm lô hàng");
        modalAddBatch.setBackground(new java.awt.Color(255, 255, 255));
        modalAddBatch.setMinimumSize(new java.awt.Dimension(650, 650));
        modalAddBatch.setModal(true);
        modalAddBatch.setResizable(false);
        modalAddBatch.setSize(new java.awt.Dimension(650, 650));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("Mã vạch sản phẩm");

        txtBarcodeAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBarcodeAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarcodeAddKeyReleased(evt);
            }
        });

        lblProductNameAdd.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblProductNameAdd.setForeground(new java.awt.Color(150, 150, 150));
        lblProductNameAdd.setText("Chưa quét mã");

        txtBatchNameAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel18.setText("Tên lô hàng");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setText("Hạn sử dụng");

        dateExpiryAdd.setDateFormatString("dd/MM/yyyy");
        dateExpiryAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtStockAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setText("Tồn kho");

        btnAddBatch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAddBatch.setText("Thêm");
        btnAddBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBatchActionPerformed(evt);
            }
        });

        btnExitModalAdd.setBackground(new java.awt.Color(92, 107, 192));
        btnExitModalAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExitModalAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnExitModalAdd.setText("Thoát");
        btnExitModalAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitModalAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25)
                    .addComponent(txtBarcodeAdd, 0, 540, Short.MAX_VALUE)
                    .addComponent(lblProductNameAdd)
                    .addComponent(jLabel18)
                    .addComponent(txtBatchNameAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                    .addComponent(jLabel22)
                    .addComponent(dateExpiryAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24)
                    .addComponent(txtStockAdd)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAddBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBarcodeAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProductNameAdd)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBatchNameAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateExpiryAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStockAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout modalAddBatchLayout = new javax.swing.GroupLayout(modalAddBatch.getContentPane());
        modalAddBatch.getContentPane().setLayout(modalAddBatchLayout);
        modalAddBatchLayout.setHorizontalGroup(
            modalAddBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalAddBatchLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        modalAddBatchLayout.setVerticalGroup(
            modalAddBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalAddBatchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        modalEditBatch.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalEditBatch.setTitle("Sửa lô hàng");
        modalEditBatch.setBackground(new java.awt.Color(255, 255, 255));
        modalEditBatch.setMinimumSize(new java.awt.Dimension(650, 650));
        modalEditBatch.setModal(true);
        modalEditBatch.setResizable(false);
        modalEditBatch.setSize(new java.awt.Dimension(650, 650));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel29.setText("Mã vạch sản phẩm");

        txtBarcodeEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBarcodeEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarcodeEditKeyReleased(evt);
            }
        });

        lblProductNameEdit.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblProductNameEdit.setForeground(new java.awt.Color(150, 150, 150));
        lblProductNameEdit.setText("Chưa quét mã");

        txtBatchNameEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Tên lô hàng");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel26.setText("Hạn sử dụng");

        dateExpiryEdit.setDateFormatString("dd/MM/yyyy");
        dateExpiryEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtStockEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel28.setText("Tồn kho");

        btnEditBatch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditBatch.setText("Cập nhật");
        btnEditBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditBatchActionPerformed(evt);
            }
        });

        btnExitModalEdit.setBackground(new java.awt.Color(92, 107, 192));
        btnExitModalEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExitModalEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnExitModalEdit.setText("Thoát");
        btnExitModalEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitModalEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29)
                    .addComponent(txtBarcodeEdit, 0, 540, Short.MAX_VALUE)
                    .addComponent(lblProductNameEdit)
                    .addComponent(jLabel19)
                    .addComponent(txtBatchNameEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                    .addComponent(jLabel26)
                    .addComponent(dateExpiryEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28)
                    .addComponent(txtStockEdit)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnEditBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBarcodeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProductNameEdit)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBatchNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateExpiryEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStockEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditBatch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout modalEditBatchLayout = new javax.swing.GroupLayout(modalEditBatch.getContentPane());
        modalEditBatch.getContentPane().setLayout(modalEditBatchLayout);
        modalEditBatchLayout.setHorizontalGroup(
            modalEditBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalEditBatchLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        modalEditBatchLayout.setVerticalGroup(
            modalEditBatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalEditBatchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBackground(new java.awt.Color(204, 204, 0));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        headerPanel.setLayout(new java.awt.BorderLayout());

        // Panel bên trái chứa nút Thêm, Sửa và Xóa
        actionPanel.setBackground(new java.awt.Color(255, 255, 255));
        actionPanel.setPreferredSize(new java.awt.Dimension(320, 60));
        actionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 10));

        btnAdd.setBackground(new java.awt.Color(115, 165, 71));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setPreferredSize(new java.awt.Dimension(95, 40));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        actionPanel.add(btnAdd);

        btnUpdate.setBackground(new java.awt.Color(255, 193, 7));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 0, 0));
        btnUpdate.setText("Sửa");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setPreferredSize(new java.awt.Dimension(95, 40));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        actionPanel.add(btnUpdate);

        btnDelete.setBackground(new java.awt.Color(244, 67, 54));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setFocusPainted(false);
        btnDelete.setPreferredSize(new java.awt.Dimension(95, 40));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        actionPanel.add(btnDelete);

        headerPanel.add(actionPanel, java.awt.BorderLayout.WEST);

        // Panel bên phải chứa ComboBox, TextField và nút Tìm kiếm
        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(650, 60));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 10));

        // ComboBox lọc trạng thái
        cboFilterStatus = new javax.swing.JComboBox<>();
        cboFilterStatus.setFont(new java.awt.Font("Segoe UI", 0, 14));
        cboFilterStatus.setPreferredSize(new java.awt.Dimension(150, 40));
        cboFilterStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Còn hạn", "Hết hạn" }));
        cboFilterStatus.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        cboFilterStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterBatchData();
            }
        });
        jPanel5.add(cboFilterStatus);

        txtSearchBatch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchBatch.setPreferredSize(new java.awt.Dimension(250, 40));
        txtSearchBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchBatchActionPerformed(evt);
            }
        });
        txtSearchBatch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchBatchKeyReleased(evt);
            }
        });
        jPanel5.add(txtSearchBatch);

        btnSearch.setBackground(new java.awt.Color(115, 165, 71));
        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Tìm kiếm");
        btnSearch.setPreferredSize(new java.awt.Dimension(120, 40));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jPanel5.add(btnSearch);

        headerPanel.add(jPanel5, java.awt.BorderLayout.EAST);

        pnAll.add(headerPanel, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        // Thêm tiêu đề "Danh sách thông tin lô hàng"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN LÔ HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng
        titlePanel.add(lblTitle);
        jPanel3.add(titlePanel, java.awt.BorderLayout.NORTH);

        jPanel3.add(scrollTable, java.awt.BorderLayout.CENTER);

        pnAll.add(jPanel3, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Phương thức lọc và tìm kiếm kết hợp
    private void filterBatchData() {
        String keyword = txtSearchBatch.getText().trim().toLowerCase();
        String filterStatus = (String) cboFilterStatus.getSelectedItem();
        
        try {
            tableDesign.getModelTable().setRowCount(0);
            List<LoHang> danhSachLH = loHangBUS.getAllLoHang();
            
            for (LoHang lh : danhSachLH) {
                // Kiểm tra và cập nhật trạng thái hết hạn: HSD <= 6 tháng → Set hết hạn
                LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
                boolean isExpired = lh.getHanSuDung().isBefore(ngayGioiHan) || 
                                    lh.getHanSuDung().isEqual(ngayGioiHan);
                String trangThai = isExpired ? "Hết hạn" : "Còn hạn";
                
                // Nếu trạng thái thay đổi, cập nhật vào database
                if (isExpired && lh.isTrangThai()) {
                    lh.setTrangThai(false);
                    try {
                        loHangBUS.capNhatLoHang(lh);
                    } catch (Exception ex) {
                        System.err.println("Lỗi khi cập nhật trạng thái lô hàng: " + ex.getMessage());
                    }
                }
                
                // Lọc theo trạng thái
                boolean matchStatus = true;
                if (filterStatus.equals("Còn hạn")) {
                    matchStatus = trangThai.equals("Còn hạn");
                } else if (filterStatus.equals("Hết hạn")) {
                    matchStatus = trangThai.equals("Hết hạn");
                }
                
                // Lọc theo từ khóa tìm kiếm
                boolean matchKeyword = true;
                if (!keyword.isEmpty()) {
                    String maLH = lh.getMaLoHang().toLowerCase();
                    String tenLH = lh.getTenLoHang().toLowerCase();
                    matchKeyword = maLH.contains(keyword) || tenLH.contains(keyword);
                }
                
                // Thêm vào bảng nếu thỏa mãn cả 2 điều kiện
                if (matchStatus && matchKeyword) {
                    tableDesign.getModelTable().addRow(new Object[]{
                        lh.getMaLoHang(),
                        lh.getTenLoHang(),
                        lh.getHanSuDung(),
                        lh.getTonKho(),
                        trangThai
                    });
                }
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }
    
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        filterBatchData();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchBatchActionPerformed
        btnSearchActionPerformed(evt);
    }//GEN-LAST:event_txtSearchBatchActionPerformed

    private void txtSearchBatchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchBatchKeyReleased
        filterBatchData();
    }//GEN-LAST:event_txtSearchBatchKeyReleased

    private void btnExitModalAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitModalAddActionPerformed
        clearData(txtBatchNameAdd, txtStockAdd, txtBarcodeAdd);
        dateExpiryAdd.setDate(null);
        selectedProductAdd = null;
        lblProductNameAdd.setText("Chưa quét mã");
        lblProductNameAdd.setForeground(new java.awt.Color(150, 150, 150));
        modalAddBatch.dispose();
    }//GEN-LAST:event_btnExitModalAddActionPerformed

    private void btnAddBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBatchActionPerformed
        try {
            // Lấy dữ liệu từ form
            String tenLH = txtBatchNameAdd.getText().trim();
            Date hanSD = dateExpiryAdd.getDate();
            String tonKhoStr = txtStockAdd.getText().trim();
            SanPham sanPham = selectedProductAdd;
            
            // Validate dữ liệu
            if (sanPham == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng quét mã vạch sản phẩm!");
                txtBarcodeAdd.requestFocus();
                return;
            }
            
            if (tenLH.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập tên lô hàng!");
                txtBatchNameAdd.requestFocus();
                return;
            }
            
            // ✅ KIỂM TRA TÊN LÔ HÀNG TRÙNG
            if (loHangBUS.isTenLoHangExists(tenLH)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Tên lô hàng \"" + tenLH + "\" đã tồn tại! Vui lòng nhập tên khác.");
                txtBatchNameAdd.requestFocus();
                txtBatchNameAdd.selectAll();
                return;
            }
            
            if (hanSD == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn hạn sử dụng!");
                return;
            }
            
            // Kiểm tra hạn sử dụng phải > 6 tháng kể từ hôm nay
            LocalDate hanSuDung = hanSD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
            if (hanSuDung.isBefore(ngayGioiHan) || hanSuDung.isEqual(ngayGioiHan)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Hạn sử dụng phải lớn hơn 6 tháng kể từ ngày hiện tại!");
                return;
            }
            
            if (tonKhoStr.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập số lượng tồn kho!");
                txtStockAdd.requestFocus();
                return;
            }
            
            int tonKho;
            try {
                tonKho = Integer.parseInt(tonKhoStr);
                if (tonKho <= 0) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Tồn kho phải lớn hơn 0!");
                    txtStockAdd.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Tồn kho phải là số nguyên!");
                txtStockAdd.requestFocus();
                return;
            }
            
            // ✅ KIỂM TRA LÔ HÀNG CÓ CÙNG SẢN PHẨM VÀ HẠN SỬ DỤNG → CỘNG DỒN
            java.util.Optional<LoHang> loHangCuOpt = loHangBUS.findByMaSanPhamAndHanSuDung(
                sanPham.getMaSanPham(), hanSuDung);
            
            if (loHangCuOpt.isPresent()) {
                // Đã có lô hàng cùng sản phẩm và hạn sử dụng → Cộng dồn
                LoHang loHangCu = loHangCuOpt.get();
                
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    modalAddBatch,
                    String.format(
                        "Đã tồn tại lô hàng \"%s\" với cùng sản phẩm và hạn sử dụng (HSD: %s).\n" +
                        "Tồn kho hiện tại: %d\n" +
                        "Bạn có muốn cộng dồn %d vào lô này không?\n" +
                        "→ Tồn kho mới sẽ là: %d",
                        loHangCu.getTenLoHang(),
                        loHangCu.getHanSuDung(),
                        loHangCu.getTonKho(),
                        tonKho,
                        loHangCu.getTonKho() + tonKho
                    ),
                    "Xác nhận cộng dồn lô hàng",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    // Cộng dồn vào lô cũ
                    boolean success = loHangBUS.updateTonKho(loHangCu.getMaLoHang(), tonKho);
                    
                    if (success) {
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                            String.format("Đã cộng dồn %d vào lô \"%s\"! Tồn kho mới: %d", 
                                tonKho, loHangCu.getTenLoHang(), loHangCu.getTonKho() + tonKho));
                        
                        // Clear form và đóng modal
                        clearData(txtBatchNameAdd, txtStockAdd, txtBarcodeAdd);
                        dateExpiryAdd.setDate(null);
                        selectedProductAdd = null;
                        lblProductNameAdd.setText("Chưa quét mã");
                        lblProductNameAdd.setForeground(new java.awt.Color(150, 150, 150));
                        modalAddBatch.dispose();
                        
                        // Refresh bảng
                        loadBatchData();
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi cộng dồn lô hàng!");
                    }
                }
                return; // Kết thúc xử lý
            }
            
            // Không có lô cùng sản phẩm + HSD → Tạo lô mới
            // Tự động xác định trạng thái dựa trên hạn sử dụng
            boolean trangThai = !hanSuDung.isBefore(LocalDate.now());
            
            // Tạo đối tượng LoHang
            LoHang lh = new LoHang();
            lh.setTenLoHang(tenLH);
            lh.setHanSuDung(hanSuDung);
            lh.setTonKho(tonKho);
            lh.setTrangThai(trangThai);
            lh.setSanPham(sanPham);
            
            // Thêm vào database
            boolean success = loHangBUS.themLoHang(lh);
            
            if (success) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm lô hàng thành công!");
                
                // Xóa dữ liệu form
                clearData(txtBatchNameAdd, txtStockAdd, txtBarcodeAdd);
                dateExpiryAdd.setDate(null);
                selectedProductAdd = null;
                lblProductNameAdd.setText("Chưa quét mã");
                lblProductNameAdd.setForeground(new java.awt.Color(150, 150, 150));
                
                // Đóng modal và reload dữ liệu
                modalAddBatch.dispose();
                loadBatchData();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm lô hàng thất bại!");
            }
            
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnAddBatchActionPerformed

    private void btnEditBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditBatchActionPerformed
        try {
            if (batchIdEdit == null || batchIdEdit.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn lô hàng cần sửa!");
                return;
            }
            
            // Lấy dữ liệu từ form
            String tenLH = txtBatchNameEdit.getText().trim();
            Date hanSD = dateExpiryEdit.getDate();
            String tonKhoStr = txtStockEdit.getText().trim();
            SanPham sanPham = selectedProductEdit;
            
            // Validate dữ liệu
            if (sanPham == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng quét mã vạch sản phẩm!");
                txtBarcodeEdit.requestFocus();
                return;
            }
            
            if (tenLH.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập tên lô hàng!");
                txtBatchNameEdit.requestFocus();
                return;
            }
            
            if (hanSD == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn hạn sử dụng!");
                return;
            }
            
            if (tonKhoStr.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập số lượng tồn kho!");
                txtStockEdit.requestFocus();
                return;
            }
            
            int tonKho;
            try {
                tonKho = Integer.parseInt(tonKhoStr);
                if (tonKho < 0) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Tồn kho phải lớn hơn hoặc bằng 0!");
                    txtStockEdit.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Tồn kho phải là số nguyên!");
                txtStockEdit.requestFocus();
                return;
            }
            
            // Chuyển đổi Date sang LocalDate
            LocalDate hanSuDung = hanSD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Tự động xác định trạng thái dựa trên hạn sử dụng
            boolean trangThai = !hanSuDung.isBefore(LocalDate.now());
            
            // Tạo đối tượng LoHang với thông tin cập nhật
            LoHang lh = new LoHang();
            lh.setMaLoHang(batchIdEdit);
            lh.setTenLoHang(tenLH);
            lh.setHanSuDung(hanSuDung);
            lh.setTonKho(tonKho);
            lh.setTrangThai(trangThai);
            lh.setSanPham(sanPham);
            
            // Cập nhật vào database
            boolean success = loHangBUS.capNhatLoHang(lh);
            
            if (success) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật lô hàng thành công!");
                
                // Xóa dữ liệu form và đóng modal
                clearData(txtBatchNameEdit, txtStockEdit, txtBarcodeEdit);
                dateExpiryEdit.setDate(null);
                selectedProductEdit = null;
                lblProductNameEdit.setText("Chưa quét mã");
                lblProductNameEdit.setForeground(new java.awt.Color(150, 150, 150));
                batchIdEdit = null;
                modalEditBatch.dispose();
                
                // Reload dữ liệu
                loadBatchData();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật lô hàng thất bại!");
            }
            
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnEditBatchActionPerformed

    private void btnExitModalEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitModalEditActionPerformed
        clearData(txtBatchNameEdit, txtStockEdit, txtBarcodeEdit);
        dateExpiryEdit.setDate(null);
        selectedProductEdit = null;
        lblProductNameEdit.setText("Chưa quét mã");
        lblProductNameEdit.setForeground(new java.awt.Color(150, 150, 150));
        modalEditBatch.dispose();
    }//GEN-LAST:event_btnExitModalEditActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn lô hàng cần sửa!");
            return;
        }

        try {
            // Lấy thông tin từ bảng
            String maLH = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
            String tenLH = tableDesign.getTable().getValueAt(selectedRow, 1).toString();
            LocalDate hanSD = (LocalDate) tableDesign.getTable().getValueAt(selectedRow, 2);
            String tonKho = tableDesign.getTable().getValueAt(selectedRow, 3).toString();

            // Lấy thông tin lô hàng từ database để có đầy đủ thông tin sản phẩm
            List<LoHang> danhSachLH = loHangBUS.getAllLoHang();
            LoHang loHangEdit = null;
            for (LoHang lh : danhSachLH) {
                if (lh.getMaLoHang().equals(maLH)) {
                    loHangEdit = lh;
                    break;
                }
            }

            if (loHangEdit == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không tìm thấy thông tin lô hàng!");
                return;
            }

            // Hiển thị lên modal edit
            batchIdEdit = maLH;
            txtBatchNameEdit.setText(tenLH);
            dateExpiryEdit.setDate(Date.from(hanSD.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            txtStockEdit.setText(tonKho);

            // Set sản phẩm đã chọn
            if (loHangEdit.getSanPham() != null) {
                String maSP = loHangEdit.getSanPham().getMaSanPham();
                txtBarcodeEdit.setText(maSP);
                selectedProductEdit = loHangEdit.getSanPham();
                lblProductNameEdit.setText("✓ " + loHangEdit.getSanPham().getTenSanPham());
                lblProductNameEdit.setForeground(new java.awt.Color(34, 139, 34));
            }

            modalEditBatch.setLocationRelativeTo(this);
            modalEditBatch.setVisible(true);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi tải thông tin lô hàng: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        modalAddBatch.setLocationRelativeTo(this);
        modalAddBatch.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void txtBarcodeAddKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeAddKeyReleased
        // Loại bỏ ký tự đặc biệt từ barcode scanner (\r, \n, \t)
        String barcode = txtBarcodeAdd.getText().trim().replaceAll("[\\r\\n\\t]", "");
        txtBarcodeAdd.setText(barcode); // Cập nhật lại textfield với giá trị đã làm sạch
        selectedProductAdd = findProductByBarcode(barcode, lblProductNameAdd);
    }//GEN-LAST:event_txtBarcodeAddKeyReleased

    private void txtBarcodeEditKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarcodeEditKeyReleased
        // Loại bỏ ký tự đặc biệt từ barcode scanner (\r, \n, \t)
        String barcode = txtBarcodeEdit.getText().trim().replaceAll("[\\r\\n\\t]", "");
        txtBarcodeEdit.setText(barcode); // Cập nhật lại textfield với giá trị đã làm sạch
        selectedProductEdit = findProductByBarcode(barcode, lblProductNameEdit);
    }//GEN-LAST:event_txtBarcodeEditKeyReleased

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn lô hàng cần xóa!");
            return;
        }

        // Lấy thông tin từ bảng
        String maLH = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenLH = tableDesign.getTable().getValueAt(selectedRow, 1).toString();

        // Hiển thị hộp thoại xác nhận
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa lô hàng \"" + tenLH + "\" (Mã: " + maLH + ")?",
            "Xác nhận xóa",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                // Gọi BUS để xóa lô hàng
                boolean isDeleted = loHangBUS.xoaLoHang(maLH);
                
                if (isDeleted) {
                    // Xóa khỏi bảng
                    tableDesign.getModelTable().removeRow(selectedRow);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xóa lô hàng thành công!");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa lô hàng thất bại!");
                }
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    public void clearData(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private String batchIdEdit;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddBatch;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEditBatch;
    private javax.swing.JButton btnExitModalAdd;
    private javax.swing.JButton btnExitModalEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private com.toedter.calendar.JDateChooser dateExpiryAdd;
    private com.toedter.calendar.JDateChooser dateExpiryEdit;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblProductNameAdd;
    private javax.swing.JLabel lblProductNameEdit;
    private javax.swing.JDialog modalAddBatch;
    private javax.swing.JDialog modalEditBatch;
    private javax.swing.JPanel pnAll;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTextField txtBarcodeAdd;
    private javax.swing.JTextField txtBarcodeEdit;
    private javax.swing.JTextField txtBatchNameAdd;
    private javax.swing.JTextField txtBatchNameEdit;
    private javax.swing.JTextField txtSearchBatch;
    private javax.swing.JTextField txtStockAdd;
    private javax.swing.JTextField txtStockEdit;
    // End of variables declaration//GEN-END:variables

}


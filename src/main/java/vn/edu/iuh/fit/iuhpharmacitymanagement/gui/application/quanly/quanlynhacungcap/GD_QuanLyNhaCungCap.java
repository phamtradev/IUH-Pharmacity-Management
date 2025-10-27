/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhacungcap;

import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import java.util.List;
import raven.toast.Notifications;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhaCungCapBUS;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyNhaCungCap extends javax.swing.JPanel {

    private final NhaCungCapBUS nhaCungCapBUS;
    private TableDesign tableDesign;

    public GD_QuanLyNhaCungCap() {
        this.nhaCungCapBUS = new NhaCungCapBUS();
        initComponents();
        
        //sì tai cho các nút
        styleButton(btnThem, "THÊM");
        styleButton(btnSua, "SỬA");
        styleButton(btnXoa, "Xóa");
        
        styleButton(btnTimKiem, "Tìm kiếm");
        
        setUIManager();
        fillTable();
        setupModalSize();
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
    
    private void setupModalSize() {
        // Tăng kích thước modal lên 900x800
        modelSupplierAdd.setSize(900, 800);
        modelSupplierEdit.setSize(900, 800);
        
        // Thêm tiêu đề đẹp cho modal
        modelSupplierAdd.setTitle("THÊM NHÀ CUNG CẤP MỚI");
        modelSupplierEdit.setTitle("CẬP NHẬT THÔNG TIN NHÀ CUNG CẤP");
        
        // Loại bỏ viền thừa của panel trong modal
        jPanel7.putClientProperty(FlatClientProperties.STYLE, "background:#F8F9FA;border:0,0,0,0");
        jPanel8.putClientProperty(FlatClientProperties.STYLE, "background:#F8F9FA;border:0,0,0,0");
        
        // Style cho buttons trong modal Add
        btnSupplierAdd.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#28A745;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#218838;"
                + "pressedBackground:#1E7E34;"
                + "arc:10;"
                + "borderWidth:0");
        
        btnSupplierExit.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#DC3545;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#C82333;"
                + "pressedBackground:#BD2130;"
                + "arc:10;"
                + "borderWidth:0");
        
        // Style cho buttons trong modal Edit
        btnSupplierEdit.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#007BFF;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#0069D9;"
                + "pressedBackground:#0056B3;"
                + "arc:10;"
                + "borderWidth:0");
        
        btnSupplierExit2.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#DC3545;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#C82333;"
                + "pressedBackground:#BD2130;"
                + "arc:10;"
                + "borderWidth:0");
    }

    private void setUIManager() {
        // Placeholder cho search
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo tên, email, số điện thoại");

        // Placeholder cho Add form
        txtSupNameAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhà cung cấp");
        txtSupPhoneAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số điện thoại (10 chữ số)");
        txtSupAddressAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập địa chỉ");
        txtSupEmailAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập email");
        txtSupTaxCodeAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mã số thuế (10 hoặc 13 ký tự)");

        // Placeholder cho Edit form
        txtSupNameEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhà cung cấp");
        txtSupPhoneEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số điện thoại (10 chữ số)");
        txtSupAddressEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập địa chỉ");
        txtSupEmailEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập email");
        txtSupTaxCodeEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mã số thuế (10 hoặc 13 ký tự)");

        // Thêm viền cho các TextField
        txtTimKiem.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupNameAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupPhoneAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupAddressAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupEmailAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupTaxCodeAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupNameEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupPhoneEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupAddressEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupEmailEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSupTaxCodeEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        UIManager.put("Button.arc", 10);
    }

    private void fillTable() {
        String[] headers = {"Mã NCC", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại", "Email", "Mã số thuế"};
        List<Integer> tableWidths = Arrays.asList(100, 250, 200, 120, 200, 120);
        tableDesign = new TableDesign(headers, tableWidths);
        SupplierScroll.setViewportView(tableDesign.getTable());
        SupplierScroll.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        // Load sample data
        loadSampleData();
    }

    private void loadSampleData() {
        DefaultTableModel model = tableDesign.getModelTable();
        model.setRowCount(0);
        
        try {
            List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap> danhSachNCC = nhaCungCapBUS.layTatCaNhaCungCap();
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap ncc : danhSachNCC) {
                model.addRow(new Object[]{
                    ncc.getMaNhaCungCap(),
                    ncc.getTenNhaCungCap(),
                    ncc.getDiaChi(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail(),
                    ncc.getMaSoThue()
                });
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Lỗi khi load dữ liệu: " + e.getMessage());
        }
    }

    // Helper method để clear nhiều text field cùng lúc
    public void clearData(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modelSupplierAdd = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtSupPhoneAdd = new javax.swing.JTextField();
        txtSupNameAdd = new javax.swing.JTextField();
        txtSupAddressAdd = new javax.swing.JTextField();
        txtSupTaxCodeAdd = new javax.swing.JTextField();
        txtSupEmailAdd = new javax.swing.JTextField();
        btnSupplierAdd = new javax.swing.JButton();
        btnSupplierExit = new javax.swing.JButton();
        modelSupplierEdit = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtSupPhoneEdit = new javax.swing.JTextField();
        txtSupNameEdit = new javax.swing.JTextField();
        txtSupAddressEdit = new javax.swing.JTextField();
        txtSupTaxCodeEdit = new javax.swing.JTextField();
        txtSupEmailEdit = new javax.swing.JTextField();
        btnSupplierEdit = new javax.swing.JButton();
        btnSupplierExit2 = new javax.swing.JButton();
        pnAll = new javax.swing.JPanel();
        pnlTieuDe = new javax.swing.JPanel();
        pnlTimKiem = new javax.swing.JPanel();
        pnlKhungTimKiem = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        pnlChinhSua = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        pnlThongTin = new javax.swing.JPanel();
        SupplierScroll = new javax.swing.JScrollPane();

        modelSupplierAdd.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modelSupplierAdd.setModal(true);
        modelSupplierAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modelSupplierAddMouseClicked(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Tên nhà cung cấp :");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setText("SDT :");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("Địa chỉ :");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel15.setText("Mã số Thuế :");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setText("Email :");

        txtSupPhoneAdd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupPhoneAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupPhoneAddActionPerformed(evt);
            }
        });

        txtSupNameAdd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupNameAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupNameAddActionPerformed(evt);
            }
        });

        txtSupAddressAdd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupAddressAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupAddressAddActionPerformed(evt);
            }
        });

        txtSupTaxCodeAdd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupTaxCodeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupTaxCodeAddActionPerformed(evt);
            }
        });

        txtSupEmailAdd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupEmailAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupEmailAddActionPerformed(evt);
            }
        });

        btnSupplierAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSupplierAdd.setText("Thêm");
        btnSupplierAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierAddActionPerformed(evt);
            }
        });

        btnSupplierExit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSupplierExit.setText("Thoát");
        btnSupplierExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupAddressAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                            .addComponent(txtSupNameAdd))
                        .addGap(84, 84, 84)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtSupEmailAdd)
                            .addComponent(txtSupPhoneAdd)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(111, 715, Short.MAX_VALUE))
                    .addComponent(txtSupTaxCodeAdd)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSupplierAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSupplierExit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(115, 115, 115))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupNameAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupPhoneAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupAddressAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupEmailAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSupTaxCodeAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSupplierAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSupplierExit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout modelSupplierAddLayout = new javax.swing.GroupLayout(modelSupplierAdd.getContentPane());
        modelSupplierAdd.getContentPane().setLayout(modelSupplierAddLayout);
        modelSupplierAddLayout.setHorizontalGroup(
            modelSupplierAddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modelSupplierAddLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        modelSupplierAddLayout.setVerticalGroup(
            modelSupplierAddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        modelSupplierEdit.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modelSupplierEdit.setModal(true);
        modelSupplierEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                modelSupplierEditKeyReleased(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel17.setText("Tên nhà cung cấp :");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel18.setText("SDT :");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Địa chỉ :");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel20.setText("Mã số Thuế :");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel21.setText("Email :");

        txtSupPhoneEdit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupPhoneEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupPhoneEditActionPerformed(evt);
            }
        });

        txtSupNameEdit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupNameEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupNameEditActionPerformed(evt);
            }
        });

        txtSupAddressEdit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupAddressEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupAddressEditActionPerformed(evt);
            }
        });

        txtSupTaxCodeEdit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupTaxCodeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupTaxCodeEditActionPerformed(evt);
            }
        });

        txtSupEmailEdit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupEmailEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupEmailEditActionPerformed(evt);
            }
        });

        btnSupplierEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSupplierEdit.setText("Sửa");
        btnSupplierEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierEditActionPerformed(evt);
            }
        });

        btnSupplierExit2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSupplierExit2.setText("Thoát");
        btnSupplierExit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierExit2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSupplierEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSupplierExit2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSupTaxCodeEdit)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(318, 318, 318))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSupAddressEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSupNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(78, 78, 78)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupEmailEdit)
                            .addComponent(txtSupPhoneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(77, 77, 77))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupPhoneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupAddressEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupEmailEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSupTaxCodeEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSupplierEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSupplierExit2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(71, 71, 71))))
        );

        javax.swing.GroupLayout modelSupplierEditLayout = new javax.swing.GroupLayout(modelSupplierEdit.getContentPane());
        modelSupplierEdit.getContentPane().setLayout(modelSupplierEditLayout);
        modelSupplierEditLayout.setHorizontalGroup(
            modelSupplierEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        modelSupplierEditLayout.setVerticalGroup(
            modelSupplierEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(204, 204, 0));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setLayout(new java.awt.BorderLayout());

        pnlTieuDe.setBackground(new java.awt.Color(255, 255, 255));
        pnlTieuDe.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        pnlTieuDe.setLayout(new java.awt.BorderLayout());

        pnlTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pnlTimKiem.setPreferredSize(new java.awt.Dimension(590, 100));
        pnlTimKiem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 24));

        pnlKhungTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pnlKhungTimKiem.setPreferredSize(new java.awt.Dimension(584, 50));
        pnlKhungTimKiem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKiem.setMinimumSize(new java.awt.Dimension(300, 40));
        txtTimKiem.setPreferredSize(new java.awt.Dimension(300, 40));
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        pnlKhungTimKiem.add(txtTimKiem);

        btnTimKiem.setBackground(new java.awt.Color(115, 165, 71));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.setMaximumSize(new java.awt.Dimension(150, 40));
        btnTimKiem.setMinimumSize(new java.awt.Dimension(150, 40));
        btnTimKiem.setPreferredSize(new java.awt.Dimension(150, 40));
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });
        pnlKhungTimKiem.add(btnTimKiem);

        pnlTimKiem.add(pnlKhungTimKiem);

        pnlTieuDe.add(pnlTimKiem, java.awt.BorderLayout.CENTER);

        pnlChinhSua.setBackground(new java.awt.Color(255, 255, 255));
        pnlChinhSua.setPreferredSize(new java.awt.Dimension(600, 100));
        pnlChinhSua.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 30));

        btnThem.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnThem.setText("THÊM");
        btnThem.setBorder(null);
        btnThem.setBorderPainted(false);
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.setFocusPainted(false);
        btnThem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThem.setPreferredSize(new java.awt.Dimension(100, 90));
        btnThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        pnlChinhSua.add(btnThem);

        btnSua.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnSua.setText("SỬA");
        btnSua.setBorder(null);
        btnSua.setBorderPainted(false);
        btnSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSua.setFocusPainted(false);
        btnSua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSua.setPreferredSize(new java.awt.Dimension(100, 90));
        btnSua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        pnlChinhSua.add(btnSua);

        btnXoa.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnXoa.setText("XÓA");
        btnXoa.setBorder(null);
        btnXoa.setBorderPainted(false);
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setFocusPainted(false);
        btnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXoa.setPreferredSize(new java.awt.Dimension(100, 90));
        btnXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnlChinhSua.add(btnXoa);

        pnlTieuDe.add(pnlChinhSua, java.awt.BorderLayout.WEST);

        pnAll.add(pnlTieuDe, java.awt.BorderLayout.NORTH);

        pnlThongTin.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1226, Short.MAX_VALUE)
            .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(SupplierScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1226, Short.MAX_VALUE))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
            .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(SupplierScroll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
        );

        pnAll.add(pnlThongTin, java.awt.BorderLayout.CENTER);

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

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        performSearch();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        performSearch();
    }//GEN-LAST:event_txtTimKiemActionPerformed
    
    private void performSearch() {
        String keyword = txtTimKiem.getText().trim();
        
        // Xóa dữ liệu cũ trong bảng
        tableDesign.getModelTable().setRowCount(0);
        
        try {
            List<vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap> danhSachNCC;
            
            // Nếu từ khóa rỗng, load tất cả
            if (keyword.isEmpty()) {
                danhSachNCC = nhaCungCapBUS.layTatCaNhaCungCap();
            } else {
                // Tìm kiếm theo tên, số điện thoại, email
                danhSachNCC = nhaCungCapBUS.timKiemTheoText(keyword);
            }
            
            // Thêm kết quả tìm kiếm vào bảng
            for (vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap ncc : danhSachNCC) {
                tableDesign.getModelTable().addRow(new Object[]{
                    ncc.getMaNhaCungCap(),
                    ncc.getTenNhaCungCap(),
                    ncc.getDiaChi(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail(),
                    ncc.getMaSoThue()
                });
            }
            
            // Hiển thị thông báo nếu không tìm thấy
            if (danhSachNCC.isEmpty() && !keyword.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, 2000, "Không tìm thấy nhà cung cấp nào!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Vui lòng chọn nhà cung cấp cần sửa!");
            return;
        }

        // Lấy thông tin từ bảng
        String maNCC = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenNCC = tableDesign.getTable().getValueAt(selectedRow, 1).toString();
        String diaChi = tableDesign.getTable().getValueAt(selectedRow, 2).toString();
        String sdt = tableDesign.getTable().getValueAt(selectedRow, 3).toString();
        String email = tableDesign.getTable().getValueAt(selectedRow, 4).toString();
        String maSoThue = tableDesign.getTable().getValueAt(selectedRow, 5).toString();

        // Hiển thị lên modal edit
        supplierIdEdit = maNCC;
        txtSupNameEdit.setText(tenNCC);
        txtSupPhoneEdit.setText(sdt);
        txtSupAddressEdit.setText(diaChi);
        txtSupEmailEdit.setText(email);
        txtSupTaxCodeEdit.setText(maSoThue);

        modelSupplierEdit.setLocationRelativeTo(this);
        modelSupplierEdit.setVisible(true);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        modelSupplierAdd.setLocationRelativeTo(this);
        modelSupplierAdd.setVisible(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void modelSupplierAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modelSupplierAddMouseClicked
        // Đóng modal khi click ra ngoài
        if (evt.getSource() == modelSupplierAdd) {
            modelSupplierAdd.dispose();
        }
    }//GEN-LAST:event_modelSupplierAddMouseClicked

    private void txtSupPhoneEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupPhoneEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupPhoneEditActionPerformed

    private void txtSupNameEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupNameEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupNameEditActionPerformed

    private void txtSupAddressEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupAddressEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupAddressEditActionPerformed

    private void txtSupTaxCodeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupTaxCodeEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupTaxCodeEditActionPerformed

    private void txtSupEmailEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupEmailEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupEmailEditActionPerformed

    private void btnSupplierEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierEditActionPerformed
        try {
            // Lấy dữ liệu từ form
            String tenNCC = txtSupNameEdit.getText().trim();
            String sdt = txtSupPhoneEdit.getText().trim();
            String diaChi = txtSupAddressEdit.getText().trim();
            String email = txtSupEmailEdit.getText().trim();
            String maSoThue = txtSupTaxCodeEdit.getText().trim();
            
            // Validation
            if (tenNCC.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Vui lòng nhập tên nhà cung cấp!");
                txtSupNameEdit.requestFocus();
                return;
            }
            
            if (sdt.isEmpty() || !sdt.matches("^0[0-9]{9}$")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số!");
                txtSupPhoneEdit.requestFocus();
                return;
            }
            
            if (diaChi.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Vui lòng nhập địa chỉ!");
                txtSupAddressEdit.requestFocus();
                return;
            }
            
            if (email.isEmpty() || !email.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Email không đúng định dạng!");
                txtSupEmailEdit.requestFocus();
                return;
            }
            
            if (maSoThue.isEmpty() || !maSoThue.matches("^[0-9]{10}(-[0-9]{3})?$")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Mã số thuế phải gồm 10 số hoặc 13 ký tự dạng 0123456789-001!");
                txtSupTaxCodeEdit.requestFocus();
                return;
            }
            
            // Tạo đối tượng NhaCungCap
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap ncc = new vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap();
            ncc.setMaNhaCungCap(supplierIdEdit);
            ncc.setTenNhaCungCap(tenNCC);
            ncc.setSoDienThoai(sdt);
            ncc.setDiaChi(diaChi);
            ncc.setEmail(email);
            ncc.setMaSoThue(maSoThue);
            
            // Cập nhật trong database
            boolean success = nhaCungCapBUS.capNhatNhaCungCap(ncc);
            
            if (success) {
                // Cập nhật trên bảng
                int selectedRow = tableDesign.getTable().getSelectedRow();
                tableDesign.getTable().setValueAt(ncc.getTenNhaCungCap(), selectedRow, 1);
                tableDesign.getTable().setValueAt(ncc.getDiaChi(), selectedRow, 2);
                tableDesign.getTable().setValueAt(ncc.getSoDienThoai(), selectedRow, 3);
                tableDesign.getTable().setValueAt(ncc.getEmail(), selectedRow, 4);
                tableDesign.getTable().setValueAt(ncc.getMaSoThue(), selectedRow, 5);
                
                // Clear form và đóng modal
                clearData(txtSupNameEdit, txtSupPhoneEdit, txtSupEmailEdit, txtSupAddressEdit, txtSupTaxCodeEdit);
                modelSupplierEdit.dispose();
                
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500, "Cập nhật nhà cung cấp thành công!");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Cập nhật nhà cung cấp thất bại!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSupplierEditActionPerformed

    private void modelSupplierEditKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_modelSupplierEditKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_modelSupplierEditKeyReleased

    private void btnSupplierExit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierExit2ActionPerformed
        clearData(txtSupNameEdit, txtSupPhoneEdit, txtSupEmailEdit, txtSupAddressEdit, txtSupTaxCodeEdit);
        modelSupplierEdit.dispose();
    }//GEN-LAST:event_btnSupplierExit2ActionPerformed

    private void btnSupplierExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierExitActionPerformed
        clearData(txtSupNameAdd, txtSupPhoneAdd, txtSupEmailAdd, txtSupAddressAdd, txtSupTaxCodeAdd);
        modelSupplierAdd.dispose();
    }//GEN-LAST:event_btnSupplierExitActionPerformed

    private void btnSupplierAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierAddActionPerformed
        try {
            // Lấy dữ liệu từ form
            String tenNCC = txtSupNameAdd.getText().trim();
            String sdt = txtSupPhoneAdd.getText().trim();
            String diaChi = txtSupAddressAdd.getText().trim();
            String email = txtSupEmailAdd.getText().trim();
            String maSoThue = txtSupTaxCodeAdd.getText().trim();
            
            // Validation
            if (tenNCC.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Vui lòng nhập tên nhà cung cấp!");
                txtSupNameAdd.requestFocus();
                return;
            }
            
            if (sdt.isEmpty() || !sdt.matches("^0[0-9]{9}$")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số!");
                txtSupPhoneAdd.requestFocus();
                return;
            }
            
            if (diaChi.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Vui lòng nhập địa chỉ!");
                txtSupAddressAdd.requestFocus();
                return;
            }
            
            if (email.isEmpty() || !email.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Email không đúng định dạng!");
                txtSupEmailAdd.requestFocus();
                return;
            }
            
            if (maSoThue.isEmpty() || !maSoThue.matches("^[0-9]{10}(-[0-9]{3})?$")) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Mã số thuế phải gồm 10 số hoặc 13 ký tự dạng 0123456789-001!");
                txtSupTaxCodeAdd.requestFocus();
                return;
            }
            
            // Kiểm tra trùng số điện thoại
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap nccTrungSDT = nhaCungCapBUS.layNhaCungCapTheoSoDienThoai(sdt);
            if (nccTrungSDT != null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Số điện thoại đã tồn tại!");
                txtSupPhoneAdd.requestFocus();
                return;
            }
            
            // Tạo đối tượng NhaCungCap
            vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap ncc = new vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhaCungCap();
            ncc.setTenNhaCungCap(tenNCC);
            ncc.setSoDienThoai(sdt);
            ncc.setDiaChi(diaChi);
            ncc.setEmail(email);
            ncc.setMaSoThue(maSoThue);
            
            // Thêm vào database
            boolean success = nhaCungCapBUS.taoNhaCungCap(ncc);
            
            if (success) {
                // Thêm vào bảng
                tableDesign.getModelTable().addRow(new Object[]{
                    ncc.getMaNhaCungCap(),
                    ncc.getTenNhaCungCap(),
                    ncc.getDiaChi(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail(),
                    ncc.getMaSoThue()
                });
                
                // Clear form và đóng modal
                clearData(txtSupNameAdd, txtSupPhoneAdd, txtSupEmailAdd, txtSupAddressAdd, txtSupTaxCodeAdd);
                modelSupplierAdd.dispose();
                
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500, "Thêm nhà cung cấp thành công!");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Thêm nhà cung cấp thất bại!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSupplierAddActionPerformed

    private void txtSupEmailAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupEmailAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupEmailAddActionPerformed

    private void txtSupTaxCodeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupTaxCodeAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupTaxCodeAddActionPerformed

    private void txtSupAddressAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupAddressAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupAddressAddActionPerformed

    private void txtSupNameAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupNameAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupNameAddActionPerformed

    private void txtSupPhoneAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupPhoneAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupPhoneAddActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 2500, "Vui lòng chọn nhà cung cấp cần xóa!");
            return;
        }

        // Lấy thông tin từ bảng
        String maNCC = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenNCC = tableDesign.getTable().getValueAt(selectedRow, 1).toString();

        // Hiển thị dialog xác nhận
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa nhà cung cấp \"" + tenNCC + "\" không?", 
            "Xác nhận xóa", 
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Gọi BUS để xóa nhà cung cấp
            boolean success = nhaCungCapBUS.xoaNhaCungCap(maNCC);
            
            if (success) {
                // Xóa trên giao diện
                tableDesign.getModelTable().removeRow(selectedRow);
                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500, "Đã xóa nhà cung cấp thành công!");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 3000, "Xóa nhà cung cấp thất bại!");
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private String supplierIdEdit;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane SupplierScroll;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSupplierAdd;
    private javax.swing.JButton btnSupplierEdit;
    private javax.swing.JButton btnSupplierExit;
    private javax.swing.JButton btnSupplierExit2;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JDialog modelSupplierAdd;
    private javax.swing.JDialog modelSupplierEdit;
    private javax.swing.JPanel pnAll;
    private javax.swing.JPanel pnlChinhSua;
    private javax.swing.JPanel pnlKhungTimKiem;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JPanel pnlTieuDe;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JTextField txtSupAddressAdd;
    private javax.swing.JTextField txtSupAddressEdit;
    private javax.swing.JTextField txtSupEmailAdd;
    private javax.swing.JTextField txtSupEmailEdit;
    private javax.swing.JTextField txtSupNameAdd;
    private javax.swing.JTextField txtSupNameEdit;
    private javax.swing.JTextField txtSupPhoneAdd;
    private javax.swing.JTextField txtSupPhoneEdit;
    private javax.swing.JTextField txtSupTaxCodeAdd;
    private javax.swing.JTextField txtSupTaxCodeEdit;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlykhachhang;

import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhachHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.KhachHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyKhachHang extends javax.swing.JPanel {

    private final KhachHangBUS khachHangBUS; // ok
    private TableDesign tableDesign;

    public GD_QuanLyKhachHang() {
        this.khachHangBUS = new KhachHangBUS(new KhachHangDAO(), new DonHangDAO());
        initComponents();
        setUIManager();
        applyButtonStyles();
        applyFontStyles();
        fillTable();
    }

    private void setUIManager() {
        txtCusAddressAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Địa chỉ");
        txtCusAddressEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Địa chỉ");
        txtCusEmailAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email");
        txtCusEmailEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email");
        txtCusNameAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên khách hàng");
        txtCusNameEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên khách hàng");
        txtCusPhoneAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại");
        txtCusPhoneEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại");
        txtSearchCus.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Tìm kiếm theo tên, số điện thoại, email");

        // Thêm viền cho các TextField
        txtCusAddressAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusAddressEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusEmailAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusEmailEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusNameAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusNameEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusPhoneAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusPhoneEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtSearchCus.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        UIManager.put("Button.arc", 10);
    }

    private void applyButtonStyles() {
        ButtonStyles.apply(btnAddCustomer, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnExitModalAdd, ButtonStyles.Type.SECONDARY);
        ButtonStyles.apply(btnEditCustomer, ButtonStyles.Type.WARNING);
        ButtonStyles.apply(btnExitModalEdit, ButtonStyles.Type.SECONDARY);
        ButtonStyles.apply(btnSearch, ButtonStyles.Type.INFO);
        ButtonStyles.apply(btnAdd, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnUpdate, ButtonStyles.Type.WARNING);
        ButtonStyles.apply(btnDelete, ButtonStyles.Type.DANGER);
    }

    private void applyFontStyles() {
        // Font cho các button chính
        FontStyles.apply(btnAddCustomer, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnEditCustomer, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnDelete, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnSearch, FontStyles.Type.BUTTON_MEDIUM);

        // Font cho text field tìm kiếm
        FontStyles.apply(txtSearchCus, FontStyles.Type.INPUT_FIELD);

        // Font cho các text field trong modal thêm
        FontStyles.apply(txtCusNameAdd, FontStyles.Type.INPUT_FIELD);
        FontStyles.apply(txtCusAddressAdd, FontStyles.Type.INPUT_FIELD);
        FontStyles.apply(txtCusEmailAdd, FontStyles.Type.INPUT_FIELD);
        FontStyles.apply(txtCusPhoneAdd, FontStyles.Type.INPUT_FIELD);

        // Font cho các text field trong modal sửa
        FontStyles.apply(txtCusNameEdit, FontStyles.Type.INPUT_FIELD);
        FontStyles.apply(txtCusAddressEdit, FontStyles.Type.INPUT_FIELD);
        FontStyles.apply(txtCusEmailEdit, FontStyles.Type.INPUT_FIELD);
        FontStyles.apply(txtCusPhoneEdit, FontStyles.Type.INPUT_FIELD);

        // Font cho các button trong modal
        FontStyles.apply(btnExitModalAdd, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnExitModalEdit, FontStyles.Type.BUTTON_MEDIUM);
    }

    private void fillTable() {
        String[] headers = { "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Địa chỉ", "Email", "Giới tính" };
        List<Integer> tableWidths = Arrays.asList(150, 250, 150, 250, 200, 100);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        // Load dữ liệu khách hàng từ database
        loadCustomerData();
    }

    private void loadCustomerData() {
        try {
            // Xóa dữ liệu cũ trong bảng
            tableDesign.getModelTable().setRowCount(0);

            // Lấy danh sách khách hàng từ BUS
            List<KhachHang> danhSachKH = khachHangBUS.getAllKhachHang();

            // Thêm từng khách hàng vào bảng
            for (KhachHang kh : danhSachKH) {
                tableDesign.getModelTable().addRow(new Object[] {
                        kh.getMaKhachHang(),
                        kh.getTenKhachHang(),
                        kh.getSoDienThoai(),
                        kh.getDiaChi(),
                        kh.getEmail(),
                        kh.getGioiTinh()
                });
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Lỗi khi tải dữ liệu khách hàng: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modalAddCustomer = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        txtCusNameAdd = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtCusPhoneAdd = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        comboSexAdd = new javax.swing.JComboBox<>();
        txtCusEmailAdd = new javax.swing.JTextField();
        txtCusAddressAdd = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btnAddCustomer = new javax.swing.JButton();
        btnExitModalAdd = new javax.swing.JButton();
        modalEditCustomer = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        txtCusNameEdit = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtCusPhoneEdit = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        comboSexEdit = new javax.swing.JComboBox<>();
        txtCusEmailEdit = new javax.swing.JTextField();
        txtCusAddressEdit = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        btnEditCustomer = new javax.swing.JButton();
        btnExitModalEdit = new javax.swing.JButton();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtSearchCus = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        actionPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();

        modalAddCustomer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalAddCustomer.setTitle("Thêm khách hàng");
        modalAddCustomer.setBackground(new java.awt.Color(255, 255, 255));
        modalAddCustomer.setMinimumSize(new java.awt.Dimension(650, 650));
        modalAddCustomer.setModal(true);
        modalAddCustomer.setResizable(false);
        modalAddCustomer.setSize(new java.awt.Dimension(650, 650));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtCusNameAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel18.setText("Tên khách hàng");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setText("Số điện thoại");

        txtCusPhoneAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setText("Giới tính");

        comboSexAdd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        txtCusEmailAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtCusAddressAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setText("Địa chỉ");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("Email");

        btnAddCustomer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAddCustomer.setText("Thêm");
        btnAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomerActionPerformed(evt);
            }
        });

        btnExitModalAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
                                .addGroup(jPanel2Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel18)
                                        .addComponent(txtCusNameAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 540,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel22)
                                        .addComponent(txtCusPhoneAdd)
                                        .addComponent(jLabel23)
                                        .addComponent(comboSexAdd, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel24)
                                        .addComponent(txtCusAddressAdd)
                                        .addComponent(jLabel25)
                                        .addComponent(txtCusEmailAdd)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout
                                                .createSequentialGroup()
                                                .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(50, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusNameAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusPhoneAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboSexAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusAddressAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusEmailAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(40, Short.MAX_VALUE)));

        javax.swing.GroupLayout modalAddCustomerLayout = new javax.swing.GroupLayout(modalAddCustomer.getContentPane());
        modalAddCustomer.getContentPane().setLayout(modalAddCustomerLayout);
        modalAddCustomerLayout.setHorizontalGroup(
                modalAddCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modalAddCustomerLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)));
        modalAddCustomerLayout.setVerticalGroup(
                modalAddCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modalAddCustomerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        modalEditCustomer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalEditCustomer.setTitle("Sửa khách hàng");
        modalEditCustomer.setBackground(new java.awt.Color(255, 255, 255));
        modalEditCustomer.setMinimumSize(new java.awt.Dimension(650, 650));
        modalEditCustomer.setModal(true);
        modalEditCustomer.setResizable(false);
        modalEditCustomer.setSize(new java.awt.Dimension(650, 650));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        txtCusNameEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Tên khách hàng");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel26.setText("Số điện thoại");

        txtCusPhoneEdit.setEditable(false);
        txtCusPhoneEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel27.setText("Giới tính");

        comboSexEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        txtCusEmailEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtCusAddressEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel28.setText("Địa chỉ");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel29.setText("Email");

        btnEditCustomer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditCustomer.setText("Cập nhật");
        btnEditCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCustomerActionPerformed(evt);
            }
        });

        btnExitModalEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
                                .addGroup(jPanel4Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel19)
                                        .addComponent(txtCusNameEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 540,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel26)
                                        .addComponent(txtCusPhoneEdit)
                                        .addComponent(jLabel27)
                                        .addComponent(comboSexEdit, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel28)
                                        .addComponent(txtCusAddressEdit)
                                        .addComponent(jLabel29)
                                        .addComponent(txtCusEmailEdit)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout
                                                .createSequentialGroup()
                                                .addComponent(btnEditCustomer, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(50, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusPhoneEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboSexEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusAddressEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCusEmailEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEditCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(30, Short.MAX_VALUE)));

        javax.swing.GroupLayout modalEditCustomerLayout = new javax.swing.GroupLayout(
                modalEditCustomer.getContentPane());
        modalEditCustomer.getContentPane().setLayout(modalEditCustomerLayout);
        modalEditCustomerLayout.setHorizontalGroup(
                modalEditCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modalEditCustomerLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)));
        modalEditCustomerLayout.setVerticalGroup(
                modalEditCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(modalEditCustomerLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        setBackground(new java.awt.Color(204, 204, 0));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel
                .setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        headerPanel.setLayout(new java.awt.BorderLayout());

        // Panel bên trái chứa nút Thêm, Sửa và Xóa
        actionPanel.setBackground(new java.awt.Color(255, 255, 255));
        actionPanel.setPreferredSize(new java.awt.Dimension(320, 60));
        actionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 6, 10));

        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new java.awt.Dimension(95, 40));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        actionPanel.add(btnAdd);

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setPreferredSize(new java.awt.Dimension(95, 40));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        actionPanel.add(btnUpdate);

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new java.awt.Dimension(95, 40));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        actionPanel.add(btnDelete);

        headerPanel.add(actionPanel, java.awt.BorderLayout.WEST);

        // Panel bên phải chứa TextField và nút Tìm kiếm
        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(590, 60));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 6, 10));

        txtSearchCus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSearchCus.setPreferredSize(new java.awt.Dimension(300, 40));
        txtSearchCus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchCusActionPerformed(evt);
            }
        });
        txtSearchCus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchCusKeyReleased(evt);
            }
        });
        jPanel5.add(txtSearchCus);

        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
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

        // Thêm tiêu đề "Danh sách thông tin khách hàng"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN KHÁCH HÀNG");
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
                        .addComponent(pnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSearchActionPerformed

    }// GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchCusActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSearchCusActionPerformed

    }// GEN-LAST:event_txtSearchCusActionPerformed

    private void txtSearchCusKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtSearchCusKeyReleased

    }// GEN-LAST:event_txtSearchCusKeyReleased

    private void btnExitModalAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnExitModalAddActionPerformed
        // TODO add your handling code here:
        clearData(txtCusNameAdd, txtCusPhoneAdd, txtCusEmailAdd, txtCusAddressAdd);
        modalAddCustomer.dispose();
    }// GEN-LAST:event_btnExitModalAddActionPerformed

    private void btnAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddCustomerActionPerformed
        try {
            // Lấy dữ liệu từ form
            String tenKH = txtCusNameAdd.getText().trim();
            String sdt = txtCusPhoneAdd.getText().trim();
            String diaChi = txtCusAddressAdd.getText().trim();
            String email = txtCusEmailAdd.getText().trim();
            String gioiTinh = (String) comboSexAdd.getSelectedItem();

            // Validate dữ liệu
            if (tenKH.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập tên khách hàng!");
                txtCusNameAdd.requestFocus();
                return;
            }

            if (sdt.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập số điện thoại!");
                txtCusPhoneAdd.requestFocus();
                return;
            }

            if (diaChi.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập địa chỉ!");
                txtCusAddressAdd.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập email!");
                txtCusEmailAdd.requestFocus();
                return;
            }

            // Tạo đối tượng KhachHang
            KhachHang kh = new KhachHang();
            kh.setTenKhachHang(tenKH);
            kh.setSoDienThoai(sdt);
            kh.setDiaChi(diaChi);
            kh.setEmail(email);
            kh.setGioiTinh(gioiTinh);

            // Gọi BUS để thêm khách hàng
            boolean isAdded = khachHangBUS.themKhachHang(kh);

            if (isAdded) {
                // Thêm vào bảng
                tableDesign.getModelTable().addRow(new Object[] {
                        kh.getMaKhachHang(),
                        kh.getTenKhachHang(),
                        kh.getSoDienThoai(),
                        kh.getDiaChi(),
                        kh.getEmail(),
                        kh.getGioiTinh()
                });

                // Xóa dữ liệu form và đóng modal
                clearData(txtCusNameAdd, txtCusPhoneAdd, txtCusAddressAdd, txtCusEmailAdd);
                comboSexAdd.setSelectedIndex(0);
                modalAddCustomer.dispose();

                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm khách hàng thành công!");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm khách hàng thất bại!");
            }
        } catch (Exception e) {
            // Hiển thị lỗi từ BUS hoặc entity validation
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
        }
    }// GEN-LAST:event_btnAddCustomerActionPerformed

    private void btnEditCustomerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEditCustomerActionPerformed
        try {
            // Kiểm tra xem có khách hàng nào được chọn không
            if (customerIdEdit == null || customerIdEdit.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn khách hàng cần sửa!");
                return;
            }

            // Lấy dữ liệu từ form
            String tenKH = txtCusNameEdit.getText().trim();
            String sdt = txtCusPhoneEdit.getText().trim();
            String diaChi = txtCusAddressEdit.getText().trim();
            String email = txtCusEmailEdit.getText().trim();
            String gioiTinh = (String) comboSexEdit.getSelectedItem();

            // Validate dữ liệu
            if (tenKH.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập tên khách hàng!");
                txtCusNameEdit.requestFocus();
                return;
            }

            if (sdt.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập số điện thoại!");
                txtCusPhoneEdit.requestFocus();
                return;
            }

            if (diaChi.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập địa chỉ!");
                txtCusAddressEdit.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập email!");
                txtCusEmailEdit.requestFocus();
                return;
            }

            // Tạo đối tượng KhachHang với dữ liệu mới
            KhachHang kh = new KhachHang();
            kh.setMaKhachHang(customerIdEdit);
            kh.setTenKhachHang(tenKH);
            kh.setSoDienThoai(sdt);
            kh.setDiaChi(diaChi);
            kh.setEmail(email);
            kh.setGioiTinh(gioiTinh);

            // Gọi BUS để cập nhật khách hàng
            boolean isUpdated = khachHangBUS.capNhatKhachHang(kh);

            if (isUpdated) {
                // Cập nhật lại dữ liệu trong bảng
                int selectedRow = tableDesign.getTable().getSelectedRow();
                if (selectedRow != -1) {
                    tableDesign.getModelTable().setValueAt(tenKH, selectedRow, 1);
                    tableDesign.getModelTable().setValueAt(sdt, selectedRow, 2);
                    tableDesign.getModelTable().setValueAt(diaChi, selectedRow, 3);
                    tableDesign.getModelTable().setValueAt(email, selectedRow, 4);
                    tableDesign.getModelTable().setValueAt(gioiTinh, selectedRow, 5);
                }

                // Xóa dữ liệu form và đóng modal
                clearData(txtCusNameEdit, txtCusPhoneEdit, txtCusAddressEdit, txtCusEmailEdit);
                comboSexEdit.setSelectedIndex(0);
                customerIdEdit = null;
                modalEditCustomer.dispose();

                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật khách hàng thành công!");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật khách hàng thất bại!");
            }
        } catch (Exception e) {
            // Hiển thị lỗi từ BUS hoặc entity validation
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
        }
    }// GEN-LAST:event_btnEditCustomerActionPerformed

    private void btnExitModalEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnExitModalEditActionPerformed
        clearData(txtCusNameEdit, txtCusPhoneEdit, txtCusEmailEdit, txtCusAddressEdit);
        modalEditCustomer.dispose();
    }// GEN-LAST:event_btnExitModalEditActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }

        // Lấy thông tin từ bảng
        String maKH = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenKH = tableDesign.getTable().getValueAt(selectedRow, 1).toString();
        String sdt = tableDesign.getTable().getValueAt(selectedRow, 2).toString();
        String diaChi = tableDesign.getTable().getValueAt(selectedRow, 3).toString();
        String email = tableDesign.getTable().getValueAt(selectedRow, 4).toString();
        String gioiTinh = tableDesign.getTable().getValueAt(selectedRow, 5).toString();

        // Hiển thị lên modal edit
        customerIdEdit = maKH;
        txtCusNameEdit.setText(tenKH);
        txtCusPhoneEdit.setText(sdt);
        txtCusAddressEdit.setText(diaChi);
        txtCusEmailEdit.setText(email);
        comboSexEdit.setSelectedItem(gioiTinh);

        modalEditCustomer.setLocationRelativeTo(this);
        modalEditCustomer.setVisible(true);
    }// GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddActionPerformed
        modalAddCustomer.setLocationRelativeTo(this);
        modalAddCustomer.setVisible(true);
    }// GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }

        // Lấy thông tin từ bảng
        String maKH = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenKH = tableDesign.getTable().getValueAt(selectedRow, 1).toString();

        // Hiển thị hộp thoại xác nhận
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa khách hàng \"" + tenKH + "\" (Mã: " + maKH + ")?",
                "Xác nhận xóa",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                // Gọi BUS để xóa khách hàng
                boolean isDeleted = khachHangBUS.xoaKhachHang(maKH);

                if (isDeleted) {
                    // Xóa khỏi bảng
                    tableDesign.getModelTable().removeRow(selectedRow);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa khách hàng thành công !");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa khách hàng thất bại!");
                }
            } catch (Exception e) {
                // Hiển thị lỗi từ BUS (ví dụ: khách hàng đã có đơn hàng)
                Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            }
        }
    }// GEN-LAST:event_btnDeleteActionPerformed

    public void clearData(javax.swing.JTextField... fields) {
        for (javax.swing.JTextField field : fields) {
            field.setText("");
        }
    }

    private String customerIdEdit;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddCustomer;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEditCustomer;
    private javax.swing.JButton btnExitModalAdd;
    private javax.swing.JButton btnExitModalEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> comboSexAdd;
    private javax.swing.JComboBox<String> comboSexEdit;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JDialog modalAddCustomer;
    private javax.swing.JDialog modalEditCustomer;
    private javax.swing.JPanel pnAll;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTextField txtCusAddressAdd;
    private javax.swing.JTextField txtCusAddressEdit;
    private javax.swing.JTextField txtCusEmailAdd;
    private javax.swing.JTextField txtCusEmailEdit;
    private javax.swing.JTextField txtCusNameAdd;
    private javax.swing.JTextField txtCusNameEdit;
    private javax.swing.JTextField txtCusPhoneAdd;
    private javax.swing.JTextField txtCusPhoneEdit;
    private javax.swing.JTextField txtSearchCus;
    // End of variables declaration//GEN-END:variables

}

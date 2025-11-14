/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhachhang;

import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
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

    private final KhachHangBUS khachHangBUS;
    private TableDesign tableDesign;

    public GD_QuanLyKhachHang() {
        this.khachHangBUS = new KhachHangBUS(new KhachHangDAO(), new DonHangDAO());
        initComponents();

        applyButtonStyles();
        applyFontStyles();
        setUIManager();
        fillTable();
    }

    /**
     * Áp dụng ButtonStyles cho tất cả button
     */
    private void applyButtonStyles() {
        ButtonStyles.apply(btnThem, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnSua, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        ButtonStyles.apply(btnTimKiem, ButtonStyles.Type.INFO);
    }

    /**
     * Áp dụng FontStyles cho tất cả component
     */
    private void applyFontStyles() {
        // Font cho các button
        FontStyles.apply(btnThem, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnSua, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnXoa, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnTimKiem, FontStyles.Type.BUTTON_MEDIUM);

        // Font cho text field tìm kiếm
        FontStyles.apply(txtTimKiem, FontStyles.Type.INPUT_FIELD);
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
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo tên, số điện thoại, email");

        // Thêm viền cho các TextField
        txtCusAddressAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusAddressEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusEmailAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusEmailEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusNameAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusNameEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusPhoneAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtCusPhoneEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtTimKiem.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        UIManager.put("Button.arc", 10);
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
    @SuppressWarnings("unchecked")
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
        scrollTable = new javax.swing.JScrollPane();

        modalAddCustomer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalAddCustomer.setTitle("Thêm khách hàng");
        modalAddCustomer.setBackground(new java.awt.Color(255, 255, 255));
        modalAddCustomer.setMinimumSize(new java.awt.Dimension(1071, 340));
        modalAddCustomer.setModal(true);
        modalAddCustomer.setResizable(false);
        modalAddCustomer.setSize(new java.awt.Dimension(1074, 360));

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
        btnAddCustomer.setText("Xác nhận");
        btnAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomerActionPerformed(evt);
            }
        });

        btnExitModalAdd.setBackground(new java.awt.Color(92, 107, 192));
        btnExitModalAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExitModalAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnExitModalAdd.setText("Hủy bỏ");
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
                                .addContainerGap(33, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel18)
                                                                .addComponent(txtCusNameAdd,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 361,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel22)
                                                                .addComponent(txtCusPhoneAdd,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 361,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel23)
                                                                .addComponent(comboSexAdd,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 247,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel24)
                                                                .addComponent(txtCusAddressAdd,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 484,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel2Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel25)
                                                                .addComponent(txtCusEmailAdd)))))
                                .addContainerGap(33, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(48, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCusNameAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel22)
                                                        .addComponent(jLabel23))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(comboSexAdd)
                                                        .addComponent(txtCusPhoneAdd,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 46,
                                                                Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel24)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCusAddressAdd, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCusEmailAdd, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(79, Short.MAX_VALUE)));

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
        modalEditCustomer.setMinimumSize(new java.awt.Dimension(1071, 340));
        modalEditCustomer.setModal(true);
        modalEditCustomer.setResizable(false);
        modalEditCustomer.setSize(new java.awt.Dimension(1074, 360));

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
        btnEditCustomer.setText("Xác nhận");
        btnEditCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCustomerActionPerformed(evt);
            }
        });

        btnExitModalEdit.setBackground(new java.awt.Color(92, 107, 192));
        btnExitModalEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExitModalEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnExitModalEdit.setText("Hủy bỏ");
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
                                .addContainerGap(33, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(btnEditCustomer, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel19)
                                                                .addComponent(txtCusNameEdit,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 361,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel26)
                                                                .addComponent(txtCusPhoneEdit,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 361,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel27)
                                                                .addComponent(comboSexEdit,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 247,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel28)
                                                                .addComponent(txtCusAddressEdit,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 484,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel4Layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(jLabel29)
                                                                .addComponent(txtCusEmailEdit)))))
                                .addContainerGap(33, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap(48, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCusNameEdit, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel26)
                                                        .addComponent(jLabel27))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(comboSexEdit)
                                                        .addComponent(txtCusPhoneEdit,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 46,
                                                                Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel28)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCusAddressEdit, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel29)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCusEmailEdit, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEditCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(79, Short.MAX_VALUE)));

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

        pnlTieuDe.setBackground(new java.awt.Color(255, 255, 255));
        pnlTieuDe.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        pnlTieuDe.setLayout(new java.awt.BorderLayout());

        pnlTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pnlTimKiem.setPreferredSize(new java.awt.Dimension(590, 100));
        pnlTimKiem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 24));

        pnlKhungTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pnlKhungTimKiem.setPreferredSize(new java.awt.Dimension(584, 50));
        pnlKhungTimKiem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimKiem.setPreferredSize(new java.awt.Dimension(300, 40));
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });
        pnlKhungTimKiem.add(txtTimKiem);

        btnTimKiem.setBackground(new java.awt.Color(115, 165, 71));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("Tìm kiếm");
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

        btnThem.setBackground(new java.awt.Color(76, 201, 102));
        btnThem.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
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

        btnSua.setBackground(new java.awt.Color(30, 140, 217));
        btnSua.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
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

        btnXoa.setBackground(new java.awt.Color(30, 140, 217));
        btnXoa.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
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

        pnAll.add(pnlTieuDe, java.awt.BorderLayout.PAGE_START);

        pnlThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnlThongTin.setLayout(new java.awt.BorderLayout());

        // Thêm tiêu đề "DANH SÁCH THÔNG TIN KHÁCH HÀNG"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(
                new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN KHÁCH HÀNG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng
        titlePanel.add(lblTitle);
        pnlThongTin.add(titlePanel, java.awt.BorderLayout.NORTH);

        pnlThongTin.add(scrollTable, java.awt.BorderLayout.CENTER);

        pnAll.add(pnlThongTin, java.awt.BorderLayout.CENTER);

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

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimKiemActionPerformed

    }// GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtTimKiemActionPerformed

    }// GEN-LAST:event_txtTimKiemActionPerformed

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtTimKiemKeyReleased

    }// GEN-LAST:event_txtTimKiemKeyReleased

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

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaActionPerformed
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
    }// GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
        modalAddCustomer.setLocationRelativeTo(this);
        modalAddCustomer.setVisible(true);
    }// GEN-LAST:event_btnThemActionPerformed

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
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa trắng thành công !");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa khách hàng thất bại!");
                }
            } catch (Exception e) {
                // Hiển thị lỗi từ BUS (ví dụ: khách hàng đã có đơn hàng)
                Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            }
        }
    }// GEN-LAST:event_btnDeleteActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
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
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa trắng thành công !");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa khách hàng thất bại!");
                }
            } catch (Exception e) {
                // Hiển thị lỗi từ BUS (ví dụ: khách hàng đã có đơn hàng)
                Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            }
        }
    }// GEN-LAST:event_btnXoaActionPerformed

    public void clearData(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private String customerIdEdit;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCustomer;
    private javax.swing.JButton btnEditCustomer;
    private javax.swing.JButton btnExitModalAdd;
    private javax.swing.JButton btnExitModalEdit;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> comboSexAdd;
    private javax.swing.JComboBox<String> comboSexEdit;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JDialog modalAddCustomer;
    private javax.swing.JDialog modalEditCustomer;
    private javax.swing.JPanel pnAll;
    private javax.swing.JPanel pnlChinhSua;
    private javax.swing.JPanel pnlKhungTimKiem;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JPanel pnlTieuDe;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTextField txtCusAddressAdd;
    private javax.swing.JTextField txtCusAddressEdit;
    private javax.swing.JTextField txtCusEmailAdd;
    private javax.swing.JTextField txtCusEmailEdit;
    private javax.swing.JTextField txtCusNameAdd;
    private javax.swing.JTextField txtCusNameEdit;
    private javax.swing.JTextField txtCusPhoneAdd;
    private javax.swing.JTextField txtCusPhoneEdit;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

}

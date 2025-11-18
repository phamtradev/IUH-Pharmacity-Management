/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhanvien;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Image;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhanVienBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.TaiKhoanBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.TaiKhoan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.PasswordUtil;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import raven.toast.Notifications;

/**
 *
 * 
 * @author PhamTra
 */
public class GD_QuanLyNhanVien extends javax.swing.JPanel {

    private final NhanVienBUS nhanVienBUS;
    private final TaiKhoanBUS taiKhoanBUS;
    private TableDesign tableDesign;
    private String employeeIdUpdate;
    private String addEmployeeImagePath;
    private String updateEmployeeImagePath;

    public GD_QuanLyNhanVien() {
        nhanVienBUS = new NhanVienBUS();
        taiKhoanBUS = new TaiKhoanBUS();
        initComponents();
        addEmployeeImagePath = null;
        updateEmployeeImagePath = null;

        // Style cho các nút sử dụng ButtonStyles
        ButtonStyles.apply(btnThem, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnSua, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        ButtonStyles.apply(btnTimKiem, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnDatLaiMatKhau, ButtonStyles.Type.WARNING);
        
        // Style cho các button trong dialog
        ButtonStyles.apply(btnAddNewEmployee, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnCancelAddEmployee, ButtonStyles.Type.SECONDARY);
        ButtonStyles.apply(btnUpdateEmployee, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnCancelUpdateEmployee, ButtonStyles.Type.DANGER);
        ButtonStyles.apply(btnCofirmResetPassword, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnCancelResetPassword, ButtonStyles.Type.DANGER);

        setUIManager();
        fillTable();
        addIconFeature();
        displayImageSelection(addEmployeeImagePath, txtAddEmployeeImagePath, lblAddEmployeePreview);
        displayImageSelection(updateEmployeeImagePath, txtEmployeeImagePath, lblUpdateEmployeePreview);
    }

    /**
     * Validation chi tiết cho form thêm nhân viên
     */
    private static final String CCCD_REGEX = "^\\d{12}$";

    private boolean validateAddEmployeeForm(String name, String address, String email, String phone, String password, String cccd) {
        // Validate tên nhân viên
        if (name == null || name.trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Tên nhân viên không được để trống!");
            txtAddEmployeeName.requestFocus();
            return false;
        }

        // Validate địa chỉ
        if (address == null || address.trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Địa chỉ không được để trống!");
            txtAddEmployeeAddress.requestFocus();
            return false;
        }

        // Validate email
        if (email == null || !email.trim().matches(NhanVien.EMAIL_REGEX)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Email không đúng định dạng (phải chứa @ và dấu . hợp lệ)!");
            txtAddEmployeeEmail.requestFocus();
            return false;
        }

        // Validate số điện thoại
        if (phone == null || !phone.trim().matches(NhanVien.SO_DIEN_THOAI_REGEX)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số!");
            txtAddEmployeePhone.requestFocus();
            return false;
        }

        if (cccd == null || !cccd.trim().matches(CCCD_REGEX)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "CCCD phải gồm đúng 12 chữ số!");
            txtAddEmployeeCccd.requestFocus();
            return false;
        }

        // Validate mật khẩu
        if (password == null || password.trim().length() < 6) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Mật khẩu phải có ít nhất 6 ký tự!");
            txtAddPassword.requestFocus();
            return false;
        }

        // Kiểm tra email đã tồn tại
        if (nhanVienBUS.kiemTraEmailTonTai(email.trim())) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Email đã được sử dụng bởi nhân viên khác!");
            txtAddEmployeeEmail.requestFocus();
            return false;
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (nhanVienBUS.kiemTraSoDienThoaiTonTai(phone.trim())) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Số điện thoại đã được sử dụng bởi nhân viên khác!");
            txtAddEmployeePhone.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Validation chi tiết cho form cập nhật nhân viên
     */
    private boolean validateUpdateEmployeeForm(String name, String address, String email, String phone, String cccd) {
        // Validate tên nhân viên
        if (name == null || name.trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Tên nhân viên không được để trống!");
            txtEmployeeName.requestFocus();
            return false;
        }

        // Validate địa chỉ
        if (address == null || address.trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Địa chỉ không được để trống!");
            txtEmployeeAddress.requestFocus();
            return false;
        }

        // Validate email
        if (email == null || !email.trim().matches(NhanVien.EMAIL_REGEX)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Email không đúng định dạng (phải chứa @ và dấu . hợp lệ)!");
            txtEmployeeEmail.requestFocus();
            return false;
        }

        // Validate số điện thoại
        if (phone == null || !phone.trim().matches(NhanVien.SO_DIEN_THOAI_REGEX)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số!");
            txtEmployeePhone.requestFocus();
            return false;
        }

        if (cccd == null || !cccd.trim().matches(CCCD_REGEX)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "CCCD phải gồm đúng 12 chữ số!");
            txtEmployeeCccd.requestFocus();
            return false;
        }

        return true;
    }

    private void setUIManager() {
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo tên, số điện thoại, email");

        txtAddEmployeeName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhân viên");
        txtAddEmployeeAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập địa chỉ");
        txtAddEmployeeEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập email");
        txtAddEmployeePhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số điện thoại");
        txtAddEmployeeCccd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập CCCD");
        txtAddPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");

        txtEmployeeAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập địa chỉ mới");
        txtEmployeeEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập email mới");
        txtEmployeeName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên mới");
        txtEmployeePhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số điện thoại mới");
        txtEmployeeCccd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập CCCD mới");

        txtResetPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu mới");

        UIManager.put("Button.arc", 10);
    }

    private void fillTable() {
        String[] headers = { "Mã nhân viên", "Tên nhân viên", "CCCD", "Email", "Số điện thoại", "Địa chỉ", "Vai trò", "Giới tính" };
        List<Integer> tableWidths = Arrays.asList(80, 140, 120, 200, 120, 200, 100, 100);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollEmployee.setViewportView(tableDesign.getTable());
        scrollEmployee.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        List<NhanVien> employees = nhanVienBUS.layTatCaNhanVien();
        fillContent(employees);
    }

    private void addIconFeature() {
        // TODO: Thêm icon cho các button nếu cần
    }

    private void fillContent(List<NhanVien> employees) {
        tableDesign.getModelTable().setRowCount(0);
        for (NhanVien nv : employees) {
            tableDesign.getModelTable().addRow(new Object[] {
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getCccd(),
                    nv.getEmail(),
                    nv.getSoDienThoai(),
                    nv.getDiaChi(),
                    nv.getVaiTro(),
                    nv.getGioiTinh()
            });
        }
    }

    private void selectComboValue(javax.swing.JComboBox<String> comboBox, String value) {
        if (value == null) {
            comboBox.setSelectedIndex(0);
            return;
        }
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equalsIgnoreCase(value)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modalAddEmployee = new javax.swing.JDialog();
        panelAddEmployee = new javax.swing.JPanel();
        lblAddEmployeeAddress = new javax.swing.JLabel();
        lblAddEmployeeEmail = new javax.swing.JLabel();
        lblAddEmployeePhone = new javax.swing.JLabel();
        lblAddEmployeeName = new javax.swing.JLabel();
        txtAddEmployeeName = new javax.swing.JTextField();
        txtAddEmployeeEmail = new javax.swing.JTextField();
        txtAddEmployeeAddress = new javax.swing.JTextField();
        txtAddEmployeePhone = new javax.swing.JTextField();
        lblAddEmployeeCccd = new javax.swing.JLabel();
        txtAddEmployeeCccd = new javax.swing.JTextField();
        lblAddEmployeeRole = new javax.swing.JLabel();
        cboAddEmployeeRole = new javax.swing.JComboBox<>();
        lblAddEmployeeGender = new javax.swing.JLabel();
        cboAddEmployeeGender = new javax.swing.JComboBox<>();
        lblAddEmployeeImage = new javax.swing.JLabel();
        txtAddEmployeeImagePath = new javax.swing.JTextField();
        btnChooseAddEmployeeImage = new javax.swing.JButton();
        btnAddNewEmployee = new javax.swing.JButton();
        btnCancelAddEmployee = new javax.swing.JButton();
        lblAddPassword = new javax.swing.JLabel();
        txtAddPassword = new javax.swing.JPasswordField();
        modalUpdateEmployee = new javax.swing.JDialog();
        panelUpdateEmployee = new javax.swing.JPanel();
        lblEmployeeAddress = new javax.swing.JLabel();
        lblEmployeeEmail = new javax.swing.JLabel();
        lblEmployeePhone = new javax.swing.JLabel();
        lblEmployeeName = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        txtEmployeeEmail = new javax.swing.JTextField();
        txtEmployeeAddress = new javax.swing.JTextField();
        txtEmployeePhone = new javax.swing.JTextField();
        lblEmployeeCccd = new javax.swing.JLabel();
        txtEmployeeCccd = new javax.swing.JTextField();
        lblEmployeeRole = new javax.swing.JLabel();
        cboEmployeeRole = new javax.swing.JComboBox<>();
        lblEmployeeGender = new javax.swing.JLabel();
        cboEmployeeGender = new javax.swing.JComboBox<>();
        btnUpdateEmployee = new javax.swing.JButton();
        btnCancelUpdateEmployee = new javax.swing.JButton();
        modalResetPassword = new javax.swing.JDialog();
        panelAllResetPass = new javax.swing.JPanel();
        lblResetPassword = new javax.swing.JLabel();
        txtResetPassword = new javax.swing.JPasswordField();
        btnCofirmResetPassword = new javax.swing.JButton();
        btnCancelResetPassword = new javax.swing.JButton();
        lblEmpIDResetPassword = new javax.swing.JLabel();
        lblEmpNameResetPassword = new javax.swing.JLabel();
        txtEmployeeNameRsPass = new javax.swing.JTextField();
        txtEmployeeIDRsPass = new javax.swing.JTextField();
        pnAll = new javax.swing.JPanel();
        pnlTieuDe = new javax.swing.JPanel();
        pnlTimKiem = new javax.swing.JPanel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        btnDatLaiMatKhau = new javax.swing.JButton();
        pnlChinhSua = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0));
        pnlThongTin = new javax.swing.JPanel();
        scrollEmployee = new javax.swing.JScrollPane();

        modalAddEmployee.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalAddEmployee.setTitle("Thêm nhân viên");
        modalAddEmployee.setMinimumSize(new java.awt.Dimension(1100, 620));
        modalAddEmployee.setPreferredSize(new java.awt.Dimension(1100, 620));
        modalAddEmployee.setModal(true);
        modalAddEmployee.setResizable(false);

        panelAddEmployee.setBackground(new java.awt.Color(255, 255, 255));

        lblAddEmployeeAddress.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeAddress.setText("Địa chỉ:");

        lblAddEmployeeEmail.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeEmail.setText("Email:");

        lblAddEmployeePhone.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeePhone.setText("Số điện thoại:");

        lblAddEmployeeName.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeName.setText("Tên nhân viên:");

        lblAddEmployeeCccd.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeCccd.setText("CCCD:");

        lblAddEmployeeRole.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeRole.setText("Vai trò:");

        lblAddEmployeeGender.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeGender.setText("Giới tính:");

        cboAddEmployeeRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "Quản lý" }));

        cboAddEmployeeGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", "Khác" }));

        lblAddEmployeeImage.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddEmployeeImage.setText("Hình ảnh:");

        txtAddEmployeeImagePath.setEditable(false);
        txtAddEmployeeImagePath.setVisible(false);

        btnChooseAddEmployeeImage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnChooseAddEmployeeImage.setText("Chọn hình ảnh");
        btnChooseAddEmployeeImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseAddEmployeeImageActionPerformed(evt);
            }
        });

        lblAddEmployeePreview = new javax.swing.JLabel();
        lblAddEmployeePreview.setBackground(new java.awt.Color(245, 245, 245));
        lblAddEmployeePreview.setBorder(javax.swing.BorderFactory.createDashedBorder(new java.awt.Color(204, 204, 204)));
        lblAddEmployeePreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddEmployeePreview.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddEmployeePreview.setOpaque(true);
        lblAddEmployeePreview.setPreferredSize(new java.awt.Dimension(200, 200));
        lblAddEmployeePreview.setText("Chưa chọn");

        txtAddEmployeeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddEmployeeNameActionPerformed(evt);
            }
        });

        txtAddEmployeeEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddEmployeeEmailActionPerformed(evt);
            }
        });

        txtAddEmployeeAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddEmployeeAddressActionPerformed(evt);
            }
        });

        txtAddEmployeePhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddEmployeePhoneActionPerformed(evt);
            }
        });

        btnAddNewEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAddNewEmployee.setText("Thêm");
        btnAddNewEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewEmployeeActionPerformed(evt);
            }
        });

        btnCancelAddEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelAddEmployee.setText("Thoát");
        btnCancelAddEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAddEmployeeActionPerformed(evt);
            }
        });

        lblAddPassword.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblAddPassword.setText("Mật khẩu");

        javax.swing.GroupLayout panelAddEmployeeLayout = new javax.swing.GroupLayout(panelAddEmployee);
        panelAddEmployee.setLayout(panelAddEmployeeLayout);
        panelAddEmployeeLayout.setHorizontalGroup(
                panelAddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(panelAddEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                        .addComponent(lblAddEmployeeImage)
                                        .addComponent(lblAddEmployeePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 240,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnChooseAddEmployeeImage, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(60, 60, 60)
                                .addGroup(panelAddEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panelAddEmployeeLayout
                                                .createSequentialGroup()
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblAddEmployeeName)
                                                        .addComponent(txtAddEmployeeName,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblAddEmployeeAddress)
                                                        .addComponent(txtAddEmployeeAddress)
                                                        .addComponent(lblAddEmployeeCccd)
                                                        .addComponent(txtAddEmployeeCccd)
                                                        .addComponent(lblAddEmployeeGender)
                                                        .addComponent(cboAddEmployeeGender, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(40, 40, 40)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblAddEmployeeEmail)
                                                        .addComponent(txtAddEmployeeEmail,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 320,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblAddEmployeePhone)
                                                        .addComponent(txtAddEmployeePhone)
                                                        .addComponent(lblAddEmployeeRole)
                                                        .addComponent(cboAddEmployeeRole, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(lblAddPassword)
                                        .addComponent(txtAddPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 680,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                panelAddEmployeeLayout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnAddNewEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnCancelAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(60, 60, 60)));
        panelAddEmployeeLayout.setVerticalGroup(
                panelAddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(panelAddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                                .addComponent(lblAddEmployeeImage)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblAddEmployeePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 240,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnChooseAddEmployeeImage, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblAddEmployeeName)
                                                        .addComponent(lblAddEmployeeEmail))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtAddEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtAddEmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblAddEmployeeAddress)
                                                        .addComponent(lblAddEmployeePhone))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtAddEmployeeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtAddEmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblAddEmployeeCccd)
                                                        .addComponent(lblAddEmployeeRole))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtAddEmployeeCccd, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cboAddEmployeeRole, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addComponent(lblAddEmployeeGender)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cboAddEmployeeGender, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblAddPassword)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAddPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(30, 30, 30)
                                .addGroup(panelAddEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancelAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAddNewEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(50, Short.MAX_VALUE)));

        btnAddNewEmployee.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout modalAddEmployeeLayout = new javax.swing.GroupLayout(modalAddEmployee.getContentPane());
        modalAddEmployee.getContentPane().setLayout(modalAddEmployeeLayout);
        modalAddEmployeeLayout.setHorizontalGroup(
                modalAddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelAddEmployee, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        modalAddEmployeeLayout.setVerticalGroup(
                modalAddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelAddEmployee, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        modalUpdateEmployee.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalUpdateEmployee.setTitle("Cập nhật thông tin nhân viên");
        modalUpdateEmployee.setMinimumSize(new java.awt.Dimension(790, 800));
        modalUpdateEmployee.setPreferredSize(new java.awt.Dimension(790, 800));
        modalUpdateEmployee.setModal(true);
        modalUpdateEmployee.setResizable(false);

        panelUpdateEmployee.setBackground(new java.awt.Color(255, 255, 255));

        lblEmployeeAddress.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeAddress.setText("Địa chỉ:");

        lblEmployeeEmail.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeEmail.setText("Email:");

        lblEmployeePhone.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeePhone.setText("Số điện thoại:");

        lblEmployeeName.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeName.setText("Tên nhân viên:");

        lblEmployeeCccd.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeCccd.setText("CCCD:");

        lblEmployeeRole.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeRole.setText("Vai trò:");

        lblEmployeeGender.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeGender.setText("Giới tính:");

        cboEmployeeRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân viên", "Quản lý" }));

        cboEmployeeGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", "Khác" }));

        txtEmployeeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeNameActionPerformed(evt);
            }
        });

        txtEmployeeEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeEmailActionPerformed(evt);
            }
        });

        txtEmployeeAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeAddressActionPerformed(evt);
            }
        });

        txtEmployeePhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeePhoneActionPerformed(evt);
            }
        });

        btnUpdateEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdateEmployee.setText("Sửa");
        btnUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmployeeActionPerformed(evt);
            }
        });

        btnCancelUpdateEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelUpdateEmployee.setText("Thoát");
        btnCancelUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelUpdateEmployeeActionPerformed(evt);
            }
        });

        lblEmployeeImage = new javax.swing.JLabel();
        lblEmployeeImage.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmployeeImage.setText("Hình ảnh:");

        txtEmployeeImagePath = new javax.swing.JTextField();
        txtEmployeeImagePath.setEditable(false);
        txtEmployeeImagePath.setVisible(false);

        btnChooseUpdateEmployeeImage = new javax.swing.JButton();
        btnChooseUpdateEmployeeImage.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnChooseUpdateEmployeeImage.setText("Chọn hình ảnh");
        btnChooseUpdateEmployeeImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseUpdateEmployeeImageActionPerformed(evt);
            }
        });

        lblUpdateEmployeePreview = new javax.swing.JLabel();
        lblUpdateEmployeePreview.setBackground(new java.awt.Color(245, 245, 245));
        lblUpdateEmployeePreview
                .setBorder(javax.swing.BorderFactory.createDashedBorder(new java.awt.Color(204, 204, 204)));
        lblUpdateEmployeePreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUpdateEmployeePreview.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblUpdateEmployeePreview.setOpaque(true);
        lblUpdateEmployeePreview.setPreferredSize(new java.awt.Dimension(200, 200));
        lblUpdateEmployeePreview.setText("Chưa chọn");

        javax.swing.GroupLayout panelUpdateEmployeeLayout = new javax.swing.GroupLayout(panelUpdateEmployee);
        panelUpdateEmployee.setLayout(panelUpdateEmployeeLayout);
        panelUpdateEmployeeLayout.setHorizontalGroup(
                panelUpdateEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUpdateEmployeeLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(lblEmployeeImage)
                                        .addComponent(lblUpdateEmployeePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnChooseUpdateEmployeeImage,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 160,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(panelUpdateEmployeeLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panelUpdateEmployeeLayout
                                                .createSequentialGroup()
                                                .addGroup(panelUpdateEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(lblEmployeeName)
                                                        .addComponent(txtEmployeeName,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                                        .addComponent(lblEmployeeAddress)
                                                        .addComponent(txtEmployeeAddress)
                                                        .addComponent(lblEmployeeCccd)
                                                        .addComponent(txtEmployeeCccd)
                                                        .addComponent(lblEmployeeGender)
                                                        .addComponent(cboEmployeeGender, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(83, 83, 83)
                                                .addGroup(panelUpdateEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(lblEmployeeEmail)
                                                        .addComponent(txtEmployeeEmail,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblEmployeePhone)
                                                        .addComponent(txtEmployeePhone,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblEmployeeRole)
                                                        .addComponent(cboEmployeeRole, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(55, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                panelUpdateEmployeeLayout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 87,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnCancelUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(55, 55, 55)));
        panelUpdateEmployeeLayout.setVerticalGroup(
                panelUpdateEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUpdateEmployeeLayout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(lblEmployeeImage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUpdateEmployeePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(btnChooseUpdateEmployeeImage, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEmployeeName)
                                        .addComponent(lblEmployeeEmail))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEmployeeAddress)
                                        .addComponent(lblEmployeePhone))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtEmployeeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEmployeeCccd)
                                        .addComponent(lblEmployeeRole))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtEmployeeCccd, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cboEmployeeRole, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(lblEmployeeGender)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboEmployeeGender, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancelUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdateEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(48, Short.MAX_VALUE)));

        javax.swing.GroupLayout modalUpdateEmployeeLayout = new javax.swing.GroupLayout(
                modalUpdateEmployee.getContentPane());
        modalUpdateEmployee.getContentPane().setLayout(modalUpdateEmployeeLayout);
        modalUpdateEmployeeLayout.setHorizontalGroup(
                modalUpdateEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelUpdateEmployee, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        modalUpdateEmployeeLayout.setVerticalGroup(
                modalUpdateEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelUpdateEmployee, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        modalResetPassword.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalResetPassword.setTitle("Đặt lại mật khẩu");
        modalResetPassword.setMinimumSize(new java.awt.Dimension(715, 400));
        modalResetPassword.setModal(true);
        modalResetPassword.setResizable(false);

        panelAllResetPass.setBackground(new java.awt.Color(255, 255, 255));

        lblResetPassword.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblResetPassword.setText("Nhập mật khẩu mới:");

        btnCofirmResetPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCofirmResetPassword.setText("Xác nhận");
        btnCofirmResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCofirmResetPasswordActionPerformed(evt);
            }
        });

        btnCancelResetPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelResetPassword.setText("Thoát");
        btnCancelResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelResetPasswordActionPerformed(evt);
            }
        });

        lblEmpIDResetPassword.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmpIDResetPassword.setText("Mã nhân viên:");

        lblEmpNameResetPassword.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEmpNameResetPassword.setText("Tên nhân viên:");

        txtEmployeeNameRsPass.setEditable(false);

        txtEmployeeIDRsPass.setEditable(false);
        txtEmployeeIDRsPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeIDRsPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAllResetPassLayout = new javax.swing.GroupLayout(panelAllResetPass);
        panelAllResetPass.setLayout(panelAllResetPassLayout);
        panelAllResetPassLayout.setHorizontalGroup(
                panelAllResetPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAllResetPassLayout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addGroup(panelAllResetPassLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelAllResetPassLayout.createSequentialGroup()
                                                .addComponent(btnCofirmResetPassword,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 87,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnCancelResetPassword,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 87,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelAllResetPassLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtResetPassword)
                                                .addGroup(panelAllResetPassLayout.createSequentialGroup()
                                                        .addGroup(panelAllResetPassLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblEmpIDResetPassword)
                                                                .addComponent(txtEmployeeIDRsPass,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(lblResetPassword))
                                                        .addGap(47, 47, 47)
                                                        .addGroup(panelAllResetPassLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblEmpNameResetPassword)
                                                                .addComponent(txtEmployeeNameRsPass,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(85, Short.MAX_VALUE)));
        panelAllResetPassLayout.setVerticalGroup(
                panelAllResetPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAllResetPassLayout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addGroup(panelAllResetPassLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEmpIDResetPassword)
                                        .addComponent(lblEmpNameResetPassword))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelAllResetPassLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtEmployeeIDRsPass, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEmployeeNameRsPass, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblResetPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addGroup(panelAllResetPassLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancelResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCofirmResetPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(39, Short.MAX_VALUE)));

        javax.swing.GroupLayout modalResetPasswordLayout = new javax.swing.GroupLayout(
                modalResetPassword.getContentPane());
        modalResetPassword.getContentPane().setLayout(modalResetPasswordLayout);
        modalResetPasswordLayout.setHorizontalGroup(
                modalResetPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelAllResetPass, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        modalResetPasswordLayout.setVerticalGroup(
                modalResetPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panelAllResetPass, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        setBackground(new java.awt.Color(204, 204, 0));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setLayout(new java.awt.BorderLayout());

        pnlTieuDe.setBackground(new java.awt.Color(255, 255, 255));
        pnlTieuDe.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        pnlTieuDe.setLayout(new java.awt.BorderLayout());

        pnlTimKiem.setBackground(new java.awt.Color(255, 255, 255));
        pnlTimKiem.setPreferredSize(new java.awt.Dimension(1000, 100));
        // pnlTimKiem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0,
        // 24));
        pnlTimKiem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 24));

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimKiem.setMinimumSize(new java.awt.Dimension(300, 40));
        txtTimKiem.setPreferredSize(new java.awt.Dimension(300, 40));
        pnlTimKiem.add(txtTimKiem);

        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.setMaximumSize(new java.awt.Dimension(150, 40));
        btnTimKiem.setMinimumSize(new java.awt.Dimension(150, 40));
        btnTimKiem.setPreferredSize(new java.awt.Dimension(150, 40));
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });
        pnlTimKiem.add(btnTimKiem);
        // for (int i=1; i<=57; i++) {
        // JLabel space = new JLabel("\t");
        // pnlKhungTimKiem.add(space);
        // }

        btnDatLaiMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnDatLaiMatKhau.setText("Đặt lại mật khẩu");
        btnDatLaiMatKhau.setMaximumSize(new java.awt.Dimension(200, 40));
        btnDatLaiMatKhau.setMinimumSize(new java.awt.Dimension(200, 40));
        btnDatLaiMatKhau.setPreferredSize(new java.awt.Dimension(200, 40));
        btnDatLaiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatLaiMatKhauActionPerformed(evt);
            }
        });
        pnlTimKiem.add(btnDatLaiMatKhau);

        pnlTieuDe.add(pnlTimKiem, java.awt.BorderLayout.CENTER);

        pnlChinhSua.setBackground(new java.awt.Color(255, 255, 255));
        pnlChinhSua.setPreferredSize(new java.awt.Dimension(320, 60));
        pnlChinhSua.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 25));

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.setFocusPainted(false);
        btnThem.setPreferredSize(new java.awt.Dimension(95, 40));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        pnlChinhSua.add(btnThem);

        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSua.setFocusPainted(false);
        btnSua.setPreferredSize(new java.awt.Dimension(95, 40));
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        pnlChinhSua.add(btnSua);

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setFocusPainted(false);
        btnXoa.setPreferredSize(new java.awt.Dimension(95, 40));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnlChinhSua.add(btnXoa);

        pnlTieuDe.add(pnlChinhSua, java.awt.BorderLayout.WEST);

        pnAll.add(pnlTieuDe, java.awt.BorderLayout.NORTH);
        pnAll.add(filler2, java.awt.BorderLayout.PAGE_END);

        pnlThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnlThongTin.setLayout(new java.awt.BorderLayout());
        
        // Thêm tiêu đề "DANH SÁCH THÔNG TIN NHÂN VIÊN"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN NHÂN VIÊN");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng
        titlePanel.add(lblTitle);
        pnlThongTin.add(titlePanel, java.awt.BorderLayout.NORTH);
        
        pnlThongTin.add(scrollEmployee, java.awt.BorderLayout.CENTER);

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

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuaActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn nhân viên muốn cập nhật thông tin");
        } else {
            employeeIdUpdate = (String) table.getValueAt(selectedRow, 0);

            NhanVien selectedEmployee = nhanVienBUS.timNhanVienTheoMa(employeeIdUpdate);
            if (selectedEmployee == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không tìm thấy thông tin nhân viên đã chọn");
                return;
            }

            txtEmployeeName.setText(selectedEmployee.getTenNhanVien());
            txtEmployeeEmail.setText(selectedEmployee.getEmail());
            txtEmployeePhone.setText(selectedEmployee.getSoDienThoai());
            txtEmployeeAddress.setText(selectedEmployee.getDiaChi());
            txtEmployeeCccd.setText(selectedEmployee.getCccd());
            selectComboValue(cboEmployeeRole, selectedEmployee.getVaiTro());
            selectComboValue(cboEmployeeGender, selectedEmployee.getGioiTinh());

            // Lưu lại đường dẫn ảnh hiện tại của nhân viên để có thể cập nhật
            updateEmployeeImagePath = selectedEmployee.getAnhNhanVien();
            displayImageSelection(updateEmployeeImagePath, txtEmployeeImagePath, lblUpdateEmployeePreview);

            modalUpdateEmployee.setLocationRelativeTo(null);
            modalUpdateEmployee.setVisible(true);

            if (table.getCellEditor() != null) {
                table.getCellEditor().stopCellEditing();
            }
        }
    }// GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
        // Reset đường dẫn ảnh khi mở form thêm mới
        addEmployeeImagePath = null;
        displayImageSelection(addEmployeeImagePath, txtAddEmployeeImagePath, lblAddEmployeePreview);
        modalAddEmployee.setLocationRelativeTo(null);
        modalAddEmployee.setVisible(true);
    }// GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }

        String maNhanVien = (String) table.getValueAt(selectedRow, 0);
        String tenNhanVien = (String) table.getValueAt(selectedRow, 1);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa nhân viên \"" + tenNhanVien + "\" không?",
                "Xác nhận xóa",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                boolean result = nhanVienBUS.xoaNhanVien(maNhanVien);
                if (result) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xóa nhân viên thành công!");
                    fillTable();
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa nhân viên thất bại!");
                }
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
            }
        }
    }// GEN-LAST:event_btnXoaActionPerformed

    private void txtAddEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAddEmployeeNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtAddEmployeeNameActionPerformed

    private void txtAddEmployeeEmailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAddEmployeeEmailActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtAddEmployeeEmailActionPerformed

    private void txtAddEmployeeAddressActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAddEmployeeAddressActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtAddEmployeeAddressActionPerformed

    private void txtAddEmployeePhoneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAddEmployeePhoneActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtAddEmployeePhoneActionPerformed

    private void btnAddNewEmployeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddNewEmployeeActionPerformed
        String name = txtAddEmployeeName.getText().trim();
        String address = txtAddEmployeeAddress.getText().trim();
        String email = txtAddEmployeeEmail.getText().trim();
        String phone = txtAddEmployeePhone.getText().trim();
        String password = new String(txtAddPassword.getPassword());
        String cccd = txtAddEmployeeCccd.getText().trim();
        String role = (String) cboAddEmployeeRole.getSelectedItem();
        String gender = (String) cboAddEmployeeGender.getSelectedItem();

        // Validation trước khi thêm
        if (!validateAddEmployeeForm(name, address, email, phone, password, cccd)) {
            return; // Dừng lại nếu validation fail
        }

        // Hỏi người dùng có muốn chọn ảnh hay không nếu chưa chọn
        if (addEmployeeImagePath == null || addEmployeeImagePath.isBlank()) {
            int choice = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Bạn chưa chọn hình ảnh cho nhân viên.\nBạn có muốn chọn hình ảnh bây giờ không?",
                    "Chọn hình ảnh",
                    javax.swing.JOptionPane.YES_NO_OPTION);
            if (choice == javax.swing.JOptionPane.YES_OPTION) {
                addEmployeeImagePath = openImageChooser(null);
                displayImageSelection(addEmployeeImagePath, txtAddEmployeeImagePath, lblAddEmployeePreview);
            }
        }

        try {
            NhanVien emp = new NhanVien(null, name, address, phone, email, role);
            emp.setGioiTinh(gender);
            emp.setCccd(cccd);
            // Lưu đường dẫn ảnh (có thể là null nếu không chọn) vào nhân viên
            emp.setAnhNhanVien(addEmployeeImagePath);

            String passHash = PasswordUtil.encode(password);
            TaiKhoan acc = new TaiKhoan(null, passHash, emp);
            acc.setNhanVien(emp);

            nhanVienBUS.taoNhanVien(emp);
            taiKhoanBUS.taoTaiKhoan(acc);

            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm nhân viên mới thành công.");

            // Clear form
            txtAddEmployeeAddress.setText("");
            txtAddEmployeeEmail.setText("");
            txtAddEmployeeName.setText("");
            txtAddEmployeePhone.setText("");
            txtAddEmployeeCccd.setText("");
            txtAddPassword.setText("");
            cboAddEmployeeGender.setSelectedIndex(0);
            cboAddEmployeeRole.setSelectedIndex(0);
            txtTimKiem.setText("");
            addEmployeeImagePath = null;
            displayImageSelection(addEmployeeImagePath, txtAddEmployeeImagePath, lblAddEmployeePreview);

            modalAddEmployee.dispose();
            fillContent(nhanVienBUS.layTatCaNhanVien());
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
        }
    }// GEN-LAST:event_btnAddNewEmployeeActionPerformed

    private void btnChooseAddEmployeeImageActionPerformed(java.awt.event.ActionEvent evt) {
        addEmployeeImagePath = openImageChooser(addEmployeeImagePath);
        displayImageSelection(addEmployeeImagePath, txtAddEmployeeImagePath, lblAddEmployeePreview);
    }

    private void btnChooseUpdateEmployeeImageActionPerformed(java.awt.event.ActionEvent evt) {
        updateEmployeeImagePath = openImageChooser(updateEmployeeImagePath);
        displayImageSelection(updateEmployeeImagePath, txtEmployeeImagePath, lblUpdateEmployeePreview);
    }

    private void btnCancelAddEmployeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelAddEmployeeActionPerformed
        modalAddEmployee.dispose();
    }// GEN-LAST:event_btnCancelAddEmployeeActionPerformed

    private void txtEmployeeNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtEmployeeNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtEmployeeNameActionPerformed

    private void txtEmployeeEmailActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtEmployeeEmailActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtEmployeeEmailActionPerformed

    private void txtEmployeeAddressActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtEmployeeAddressActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtEmployeeAddressActionPerformed

    private void txtEmployeePhoneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtEmployeePhoneActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtEmployeePhoneActionPerformed

    private void btnUpdateEmployeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateEmployeeActionPerformed
        String name = txtEmployeeName.getText().trim();
        String address = txtEmployeeAddress.getText().trim();
        String email = txtEmployeeEmail.getText().trim();
        String phone = txtEmployeePhone.getText().trim();
        String cccd = txtEmployeeCccd.getText().trim();
        String role = (String) cboEmployeeRole.getSelectedItem();
        String gender = (String) cboEmployeeGender.getSelectedItem();

        // Validation trước khi cập nhật
        if (!validateUpdateEmployeeForm(name, address, email, phone, cccd)) {
            return; // Dừng lại nếu validation fail
        }

        try {
            NhanVien employee = nhanVienBUS.timNhanVienTheoMa(employeeIdUpdate);

            // Kiểm tra email trùng với nhân viên khác (trừ chính nó)
            if (!employee.getEmail().equals(email) && nhanVienBUS.kiemTraEmailTonTai(email)) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Email đã được sử dụng bởi nhân viên khác!");
                txtEmployeeEmail.requestFocus();
                return;
            }

            // Kiểm tra số điện thoại trùng với nhân viên khác (trừ chính nó)
            if (!employee.getSoDienThoai().equals(phone) && nhanVienBUS.kiemTraSoDienThoaiTonTai(phone)) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        "Số điện thoại đã được sử dụng bởi nhân viên khác!");
                txtEmployeePhone.requestFocus();
                return;
            }

            employee.setTenNhanVien(name);
            employee.setDiaChi(address);
            employee.setEmail(email);
            employee.setSoDienThoai(phone);
            employee.setCccd(cccd);
            employee.setGioiTinh(gender);
            employee.setVaiTro(role);
            employee.setAnhNhanVien(updateEmployeeImagePath);
            nhanVienBUS.capNhatNhanVien(employee);

            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thông tin nhân viên thành công.");

            // Clear form
            txtEmployeeName.setText("");
            txtEmployeeEmail.setText("");
            txtEmployeeAddress.setText("");
            txtEmployeePhone.setText("");
            txtEmployeeCccd.setText("");
            cboEmployeeGender.setSelectedIndex(0);
            cboEmployeeRole.setSelectedIndex(0);
            txtTimKiem.setText("");
            updateEmployeeImagePath = null;
            displayImageSelection(updateEmployeeImagePath, txtEmployeeImagePath, lblUpdateEmployeePreview);

            modalUpdateEmployee.dispose();
            fillContent(nhanVienBUS.layTatCaNhanVien());
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
        }
    }// GEN-LAST:event_btnUpdateEmployeeActionPerformed

    private void btnCancelUpdateEmployeeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelUpdateEmployeeActionPerformed
        modalUpdateEmployee.dispose();
    }// GEN-LAST:event_btnCancelUpdateEmployeeActionPerformed

    private void btnCancelResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelResetPasswordActionPerformed
        modalResetPassword.dispose();
    }// GEN-LAST:event_btnCancelResetPasswordActionPerformed

    private void txtEmployeeIDRsPassActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtEmployeeIDRsPassActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtEmployeeIDRsPassActionPerformed

    private void btnCofirmResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCofirmResetPasswordActionPerformed
        String newPass = new String(txtResetPassword.getPassword());

        // Validation mật khẩu mới
        if (newPass == null || newPass.trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Mật khẩu mới không được để trống!");
            txtResetPassword.requestFocus();
            return;
        }

        if (newPass.trim().length() < 6) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Mật khẩu mới phải có ít nhất 6 ký tự!");
            txtResetPassword.requestFocus();
            return;
        }

        try {
            employeeIdUpdate = txtEmployeeIDRsPass.getText().trim();
            TaiKhoan acc = taiKhoanBUS.layTaiKhoanTheoMaNhanVien(employeeIdUpdate);

            // Sử dụng phương thức resetMatKhau để tránh hash 2 lần
            boolean success = taiKhoanBUS.resetMatKhau(acc.getTenDangNhap(), newPass);

            if (!success) {
                throw new Exception("Không thể đặt lại mật khẩu. Vui lòng kiểm tra lại.");
            }

            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đặt lại mật khẩu thành công.");
            txtResetPassword.setText("");
            txtTimKiem.setText("");
            modalResetPassword.dispose();
            fillContent(nhanVienBUS.layTatCaNhanVien());
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
        }
    }// GEN-LAST:event_btnCofirmResetPasswordActionPerformed

    private void btnDatLaiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDatLaiMatKhauActionPerformed
        JTable table = tableDesign.getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn nhân viên muốn đặt lại mật khẩu.");
        } else {
            employeeIdUpdate = (String) table.getValueAt(selectedRow, 0);

            txtEmployeeIDRsPass.setText((String) table.getValueAt(selectedRow, 0));
            txtEmployeeNameRsPass.setText((String) table.getValueAt(selectedRow, 1));

            modalResetPassword.setLocationRelativeTo(null);
            modalResetPassword.setVisible(true);
        }
    }// GEN-LAST:event_btnDatLaiMatKhauActionPerformed


    private String openImageChooser(String currentPath) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn hình ảnh nhân viên");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (PNG, JPG, JPEG)", "png", "jpg", "jpeg"));

        if (currentPath != null && !currentPath.isBlank()) {
            fileChooser.setSelectedFile(new File(currentPath));
        }

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            return fileToOpen.getAbsolutePath();
        }

        // Nếu người dùng bấm Cancel thì giữ nguyên giá trị cũ
        return currentPath;
    }

    private void displayImageSelection(String imagePath, javax.swing.JTextField pathField, javax.swing.JLabel previewLabel) {
        if (pathField != null) {
            if (imagePath == null || imagePath.isBlank()) {
                pathField.setText("Chưa chọn");
            } else {
                pathField.setText(imagePath);
            }
        }
        updateImagePreview(imagePath, previewLabel);
    }

    private void updateImagePreview(String imagePath, javax.swing.JLabel previewLabel) {
        if (previewLabel == null) {
            return;
        }

        final int previewWidth = 200;
        final int previewHeight = 200;

        if (imagePath == null || imagePath.isBlank()) {
            previewLabel.setIcon(null);
            previewLabel.setText("Chưa chọn");
            return;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            previewLabel.setIcon(null);
            previewLabel.setText("Không tìm thấy ảnh");
            return;
        }

        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(previewWidth, previewHeight, Image.SCALE_SMOOTH);
        previewLabel.setText("");
        previewLabel.setIcon(new ImageIcon(scaledImage));
    }

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTimKiemActionPerformed
        String keyword = txtTimKiem.getText().trim();

        List<NhanVien> employees = nhanVienBUS.timNhanVienTheoTuKhoa(keyword);
        if (employees.size() < 1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Không tìm thấy nhân viên phù hợp");
        } else {
            fillContent(employees);
        }
    }// GEN-LAST:event_btnTimKiemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboAddEmployeeGender;
    private javax.swing.JComboBox<String> cboAddEmployeeRole;
    private javax.swing.JComboBox<String> cboEmployeeGender;
    private javax.swing.JComboBox<String> cboEmployeeRole;
    private javax.swing.JButton btnAddNewEmployee;
    private javax.swing.JButton btnChooseAddEmployeeImage;
    private javax.swing.JButton btnChooseUpdateEmployeeImage;
    private javax.swing.JButton btnCancelAddEmployee;
    private javax.swing.JButton btnCancelResetPassword;
    private javax.swing.JButton btnCancelUpdateEmployee;
    private javax.swing.JButton btnCofirmResetPassword;
    private javax.swing.JButton btnDatLaiMatKhau;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnUpdateEmployee;
    private javax.swing.JButton btnXoa;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel lblAddEmployeeAddress;
    private javax.swing.JLabel lblAddEmployeeCccd;
    private javax.swing.JLabel lblAddEmployeeEmail;
    private javax.swing.JLabel lblAddEmployeeGender;
    private javax.swing.JLabel lblAddEmployeePreview;
    private javax.swing.JLabel lblAddEmployeeImage;
    private javax.swing.JLabel lblAddEmployeeName;
    private javax.swing.JLabel lblAddEmployeePhone;
    private javax.swing.JLabel lblAddEmployeeRole;
    private javax.swing.JLabel lblAddPassword;
    private javax.swing.JLabel lblEmpIDResetPassword;
    private javax.swing.JLabel lblEmpNameResetPassword;
    private javax.swing.JLabel lblEmployeeAddress;
    private javax.swing.JLabel lblEmployeeCccd;
    private javax.swing.JLabel lblEmployeeEmail;
    private javax.swing.JLabel lblEmployeeGender;
    private javax.swing.JLabel lblEmployeeImage;
    private javax.swing.JLabel lblEmployeeName;
    private javax.swing.JLabel lblEmployeePhone;
    private javax.swing.JLabel lblEmployeeRole;
    private javax.swing.JLabel lblUpdateEmployeePreview;
    private javax.swing.JLabel lblResetPassword;
    private javax.swing.JDialog modalAddEmployee;
    private javax.swing.JDialog modalResetPassword;
    private javax.swing.JDialog modalUpdateEmployee;
    private javax.swing.JPanel panelAddEmployee;
    private javax.swing.JPanel panelAllResetPass;
    private javax.swing.JPanel panelUpdateEmployee;
    private javax.swing.JPanel pnAll;
    private javax.swing.JPanel pnlChinhSua;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JPanel pnlTieuDe;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JScrollPane scrollEmployee;
    private javax.swing.JTextField txtAddEmployeeAddress;
    private javax.swing.JTextField txtAddEmployeeCccd;
    private javax.swing.JTextField txtAddEmployeeEmail;
    private javax.swing.JTextField txtAddEmployeeImagePath;
    private javax.swing.JTextField txtAddEmployeeName;
    private javax.swing.JTextField txtAddEmployeePhone;
    private javax.swing.JPasswordField txtAddPassword;
    private javax.swing.JTextField txtEmployeeAddress;
    private javax.swing.JTextField txtEmployeeCccd;
    private javax.swing.JTextField txtEmployeeEmail;
    private javax.swing.JTextField txtEmployeeIDRsPass;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtEmployeeImagePath;
    private javax.swing.JTextField txtEmployeeNameRsPass;
    private javax.swing.JTextField txtEmployeePhone;
    private javax.swing.JPasswordField txtResetPassword;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}

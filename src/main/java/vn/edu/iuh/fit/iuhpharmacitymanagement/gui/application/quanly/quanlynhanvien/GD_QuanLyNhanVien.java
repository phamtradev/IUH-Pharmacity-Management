/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlynhanvien;

import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
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

    public GD_QuanLyNhanVien() {
        nhanVienBUS = new NhanVienBUS();
        taiKhoanBUS = new TaiKhoanBUS();
        initComponents();

        // Style cho các nút sử dụng ButtonStyles
        ButtonStyles.apply(btnThem, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnSua, ButtonStyles.Type.PRIMARY);
        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        ButtonStyles.apply(btnTimKiem, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnDatLaiMatKhau, ButtonStyles.Type.WARNING);

        setUIManager();
        fillTable();
        addIconFeature();
    }

    /**
     * Validation chi tiết cho form thêm nhân viên
     */
    private boolean validateAddEmployeeForm(String name, String address, String email, String phone, String password) {
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
    private boolean validateUpdateEmployeeForm(String name, String address, String email, String phone) {
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

        return true;
    }

    private void setUIManager() {
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm theo tên, số điện thoại, email");

        txtAddEmployeeName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhân viên");
        txtAddEmployeeAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập địa chỉ");
        txtAddEmployeeEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập email");
        txtAddEmployeePhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số điện thoại");
        txtAddPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");

        txtEmployeeAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập địa chỉ mới");
        txtEmployeeEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập email mới");
        txtEmployeeName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên mới");
        txtEmployeePhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số điện thoại mới");

        txtResetPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu mới");

        UIManager.put("Button.arc", 10);
    }

    private void fillTable() {
        String[] headers = { "Mã nhân viên", "Tên nhân viên", "Email", "Số điện thoại", "Địa chỉ" };
        List<Integer> tableWidths = Arrays.asList(50, 100, 200, 50, 150, 50);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollEmployee.setViewportView(tableDesign.getTable());
        scrollEmployee.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        List<NhanVien> employees = nhanVienBUS.layTatCaNhanVien();
        fillContent(employees);
    }

    private void addIconFeature() {
        // TODO: Thêm icon cho các button nếu cần
    }

    /**
     * Đổ dữ liệu nhân viên vào bảng
     */
    private void fillContent(List<NhanVien> employees) {
        tableDesign.getModelTable().setRowCount(0);
        for (NhanVien nv : employees) {
            tableDesign.getModelTable().addRow(new Object[] {
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getEmail(),
                    nv.getSoDienThoai(),
                    nv.getDiaChi()
            });
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
        modalAddEmployee.setMinimumSize(new java.awt.Dimension(810, 470));
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

        btnCancelAddEmployee.setBackground(new java.awt.Color(92, 107, 192));
        btnCancelAddEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelAddEmployee.setForeground(new java.awt.Color(255, 255, 255));
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
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddEmployeeLayout
                                .createSequentialGroup()
                                .addGroup(panelAddEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnAddNewEmployee, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnCancelAddEmployee,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 93,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelAddEmployeeLayout
                                                .createSequentialGroup()
                                                .addGap(54, 54, 54)
                                                .addGroup(panelAddEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtAddPassword,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 683,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                                                .addGroup(panelAddEmployeeLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(lblAddEmployeeAddress)
                                                                        .addComponent(lblAddEmployeeName)
                                                                        .addComponent(txtAddEmployeeName,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                300, Short.MAX_VALUE)
                                                                        .addComponent(txtAddEmployeeAddress))
                                                                .addGap(83, 83, 83)
                                                                .addGroup(panelAddEmployeeLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(txtAddEmployeeEmail,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                300, Short.MAX_VALUE)
                                                                        .addComponent(lblAddEmployeeEmail)
                                                                        .addComponent(lblAddEmployeePhone)
                                                                        .addComponent(txtAddEmployeePhone)))
                                                        .addComponent(lblAddPassword))))
                                .addGap(55, 55, 55)));
        panelAddEmployeeLayout.setVerticalGroup(
                panelAddEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAddEmployeeLayout.createSequentialGroup()
                                .addGap(47, 47, 47)
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
                                        .addComponent(txtAddEmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtAddEmployeeAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(lblAddPassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtAddPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addGroup(panelAddEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancelAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAddNewEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(118, Short.MAX_VALUE)));

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
        modalUpdateEmployee.setMinimumSize(new java.awt.Dimension(792, 370));
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

        btnUpdateEmployee.setBackground(new java.awt.Color(78, 94, 186));
        btnUpdateEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnUpdateEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateEmployee.setText("Sửa");
        btnUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateEmployeeActionPerformed(evt);
            }
        });

        btnCancelUpdateEmployee.setBackground(new java.awt.Color(236, 82, 113));
        btnCancelUpdateEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelUpdateEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelUpdateEmployee.setText("Thoát");
        btnCancelUpdateEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelUpdateEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUpdateEmployeeLayout = new javax.swing.GroupLayout(panelUpdateEmployee);
        panelUpdateEmployee.setLayout(panelUpdateEmployeeLayout);
        panelUpdateEmployeeLayout.setHorizontalGroup(
                panelUpdateEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUpdateEmployeeLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblEmployeeAddress)
                                        .addComponent(lblEmployeeName)
                                        .addComponent(txtEmployeeName, javax.swing.GroupLayout.DEFAULT_SIZE, 300,
                                                Short.MAX_VALUE)
                                        .addComponent(txtEmployeeAddress))
                                .addGap(83, 83, 83)
                                .addGroup(panelUpdateEmployeeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtEmployeeEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblEmployeeEmail)
                                        .addComponent(lblEmployeePhone)
                                        .addComponent(txtEmployeePhone, javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelUpdateEmployeeLayout.createSequentialGroup()
                                                .addGroup(panelUpdateEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblEmployeeAddress)
                                                        .addComponent(lblEmployeePhone))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelUpdateEmployeeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtEmployeePhone,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtEmployeeAddress,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(34, 34, 34)
                                                .addComponent(btnCancelUpdateEmployee,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 38,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnCofirmResetPassword.setBackground(new java.awt.Color(78, 94, 186));
        btnCofirmResetPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCofirmResetPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnCofirmResetPassword.setText("Xác nhận");
        btnCofirmResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCofirmResetPasswordActionPerformed(evt);
            }
        });

        btnCancelResetPassword.setBackground(new java.awt.Color(236, 82, 113));
        btnCancelResetPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelResetPassword.setForeground(new java.awt.Color(255, 255, 255));
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
        pnlTimKiem.add(btnTimKiem);
        // for (int i=1; i<=57; i++) {
        // JLabel space = new JLabel("\t");
        // pnlKhungTimKiem.add(space);
        // }

        btnDatLaiMatKhau.setBackground(new java.awt.Color(115, 165, 71));
        btnDatLaiMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnDatLaiMatKhau.setForeground(new java.awt.Color(255, 255, 255));
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

        btnThem.setBackground(new java.awt.Color(115, 165, 71));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
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

        btnSua.setBackground(new java.awt.Color(255, 193, 7));
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

        btnXoa.setBackground(new java.awt.Color(220, 60, 60));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
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

            txtEmployeeName.setText((String) table.getValueAt(selectedRow, 1));
            txtEmployeeEmail.setText((String) table.getValueAt(selectedRow, 2));
            txtEmployeePhone.setText((String) table.getValueAt(selectedRow, 3));
            txtEmployeeAddress.setText((String) table.getValueAt(selectedRow, 4));

            modalUpdateEmployee.setLocationRelativeTo(null);
            modalUpdateEmployee.setVisible(true);

            if (table.getCellEditor() != null) {
                table.getCellEditor().stopCellEditing();
            }
        }
    }// GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnThemActionPerformed
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

        // Validation trước khi thêm
        if (!validateAddEmployeeForm(name, address, email, phone, password)) {
            return; // Dừng lại nếu validation fail
        }

        try {
            NhanVien emp = new NhanVien(null, name, address, phone, email, "Nhân viên");

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
            txtAddPassword.setText("");
            txtTimKiem.setText("");

            modalAddEmployee.dispose();
            fillContent(nhanVienBUS.layTatCaNhanVien());
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
        }
    }// GEN-LAST:event_btnAddNewEmployeeActionPerformed

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

        // Validation trước khi cập nhật
        if (!validateUpdateEmployeeForm(name, address, email, phone)) {
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
            nhanVienBUS.capNhatNhanVien(employee);

            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thông tin nhân viên thành công.");

            // Clear form
            txtEmployeeName.setText("");
            txtEmployeeEmail.setText("");
            txtEmployeeAddress.setText("");
            txtEmployeePhone.setText("");
            txtTimKiem.setText("");

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
    private javax.swing.JButton btnAddNewEmployee;
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
    private javax.swing.JLabel lblAddEmployeeEmail;
    private javax.swing.JLabel lblAddEmployeeName;
    private javax.swing.JLabel lblAddEmployeePhone;
    private javax.swing.JLabel lblAddPassword;
    private javax.swing.JLabel lblEmpIDResetPassword;
    private javax.swing.JLabel lblEmpNameResetPassword;
    private javax.swing.JLabel lblEmployeeAddress;
    private javax.swing.JLabel lblEmployeeEmail;
    private javax.swing.JLabel lblEmployeeName;
    private javax.swing.JLabel lblEmployeePhone;
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
    private javax.swing.JTextField txtAddEmployeeEmail;
    private javax.swing.JTextField txtAddEmployeeName;
    private javax.swing.JTextField txtAddEmployeePhone;
    private javax.swing.JPasswordField txtAddPassword;
    private javax.swing.JTextField txtEmployeeAddress;
    private javax.swing.JTextField txtEmployeeEmail;
    private javax.swing.JTextField txtEmployeeIDRsPass;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JTextField txtEmployeeNameRsPass;
    private javax.swing.JTextField txtEmployeePhone;
    private javax.swing.JPasswordField txtResetPassword;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}

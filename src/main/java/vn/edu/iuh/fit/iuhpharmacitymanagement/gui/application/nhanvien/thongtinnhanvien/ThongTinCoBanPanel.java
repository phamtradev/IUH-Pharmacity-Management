/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.thongtinnhanvien;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.TaiKhoanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.TaiKhoan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.session.SessionManager;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.UserSession;

/**
 *
 * @author User
 */
public class ThongTinCoBanPanel extends javax.swing.JPanel {

    /**
     * Creates new form ThongTinCoBanPanel
     */
    public ThongTinCoBanPanel() {
        initComponents();

        btnDoiMatKhau.setVisible(true);
        txtMkMoi.setText("Nhập mật khẩu mới");
        txtMkMoi.setEchoChar((char) 0);
        txtXacNhanMkMoi.setText("Xác nhận mật khẩu mới");
        txtXacNhanMkMoi.setEchoChar((char) 0);

    }

    public void loadAndConfigure(boolean isManager, NhanVien nv) {
        styleComponents();

        if (nv != null) {
            populateData(nv);
            //configureForRole(isManager, nv);
        } else {
            // Xử lý khi không có dữ liệu nhân viên
            lblTenNV.setText("Không có dữ liệu người dùng");
            lblMa.setText("");
        }
    }

    private void populateData(NhanVien nv) {
        // Cập nhật Avatar tạm thời
        String ten = nv.getTenNhanVien();
        if (ten != null && !ten.isEmpty()) {
            String[] parts = ten.split(" ");
            String lastName = parts[parts.length - 1];
            lblAvatar.setText(String.valueOf(lastName.charAt(0)).toUpperCase());
        } else {
            lblAvatar.setText("?");
        }

        lblTenNV.setText(ten);
        lblMa.setText(nv.getMaNhanVien());
        // txtVaiTro.setText(nv.getVaiTro());
        txtPhone.setText(nv.getSoDienThoai());
        txtEmail.setText(nv.getEmail());
        txtAddress.setText(nv.getDiaChi());
    }

    private void configureForRole(boolean isManager, NhanVien nv) {
//        // Hiển thị hoặc ẩn nút đổi mật khẩu
//        btnDoiMatKhau.setVisible(isManager);
    }

//    private void configureForRole(boolean isManager, NhanVien nv) {
//        // Hiển thị hoặc ẩn nút đổi mật khẩu
//        btnDoiMatKhau.setVisible(isManager);
//    }
    public void setTenNhanVien(String ten) {
        lblTenNV.setText(ten);
        //lấy từ đầu của tên làm avt
        if (ten != null && !ten.isEmpty()) {
            String[] parts = ten.split(" ");
            String lastPart = "?";
            for (int i = parts.length - 1; i >= 0; i--) {
                if (parts[i].matches(".*[a-zA-Z]+.*")) {
                    lastPart = parts[i];
                    break;
                }
            }
            lblAvatar.setText(String.valueOf(lastPart.charAt(0)).toUpperCase());
        } else {
            lblAvatar.setText("?");
        }
    }

    public void setMa(String maNV) {
        txtPhone.setText(maNV);
    }

    public void setSoDienThoai(String sdt) {
        txtPhone.setText(sdt);
    }

    public void setEmail(String email) {
        txtEmail.setText(email);
    }

    public void setDiaChi(String diaChi) {
        txtAddress.setText(diaChi);
    }

    private void styleComponents() {
        //Avatar - Ẩn đi
        lblAvatar.setVisible(false);

        //tên nhân viên - Tăng kích thước
        lblTenNV.setFont(new java.awt.Font("Segoe UI", 1, 32));
        lblTenNV.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        //Mã - Tăng kích thước
        lblMa.setFont(new java.awt.Font("Segoe UI", 0, 18));
        lblMa.setForeground(java.awt.Color.GRAY);
        lblMa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        //Các ô thông tin - Tăng kích thước font
        //styleReadOnlyTextField(txtVaiTro);
        styleReadOnlyTextField(txtPhone);
        styleReadOnlyTextField(txtEmail);
        txtAddress.setBackground(this.getBackground());
        txtAddress.setFont(new java.awt.Font("Segoe UI", 0, 18));
        txtAddress.setForeground(new java.awt.Color(51, 51, 51));
        scrAddress.setOpaque(false);
        scrAddress.getViewport().setOpaque(false);
        scrAddress.setBorder(null);

        //Các Icon
        // setIcon(lblIconVaiTro, "/img/icons/phone.svg", "👤");
        setIcon(lblIconPhone, "/img/icons/phone.svg", "📞");
        setIcon(lblIconEmail, "/img/icons/email.svg", "📧");
        setIcon(lblIconAddress, "/img/icons/address.svg", "🏠");

        //label tiêu đề - Tăng kích thước
        //lblVaiTroTitle.setText("Chức vụ:");
        //lblVaiTroTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblPhoneTitle.setText("Điện thoại:");
        lblPhoneTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblEmailTitle.setText("Email:");
        lblEmailTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblAddressTitle.setText("Địa chỉ:");
        lblAddressTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));

        //Style nút đổi mật khẩu - Tăng kích thước
        btnDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 16));
        btnDoiMatKhau.setPreferredSize(new java.awt.Dimension(200, 45));
        btnDoiMatKhau.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, ""
                + "arc: 10;"
                + "background: $Component.accentColor;"
                + "foreground: #FFFFFF;");
    }

    private void styleReadOnlyTextField(javax.swing.JTextField textField) {
        textField.setEditable(false);
        textField.setBorder(null);
        textField.setBackground(this.getBackground());
        textField.setFont(new java.awt.Font("Segoe UI", 0, 18));
        textField.setForeground(new java.awt.Color(51, 51, 51));
    }

    private void styleChangePasswordDialog() {
        txtMkCu.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu cũ của bạn");
        txtMkMoi.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu mới");
        txtXacNhanMkMoi.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Xác nhận mật khẩu mới");

        //Con mắt trong flatlaf vip pro
        txtMkCu.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        txtMkMoi.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        txtXacNhanMkMoi.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");

        //Style cho nút
        btnXNDoiMatKhau.putClientProperty(FlatClientProperties.STYLE, "background: $Component.accentColor; foreground: #FFFFFF;");

        //styel cho Jlabel quay lại
        lblQuayLai.setText("<html><u>&lt; Quay lại</u></html>");
        lblQuayLai.setForeground(new java.awt.Color(0, 102, 204));
        lblQuayLai.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    private void setIcon(javax.swing.JLabel label, String path, String fallbackText) {
        try {
            java.net.URL iconUrl = getClass().getResource(path);
            if (iconUrl != null) {
                label.setText("");
                label.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon(iconUrl).derive(24, 24));
            } else {
                System.err.println("Không tìm thấy icon: " + path);
                label.setText(fallbackText);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load icon: " + path);
            label.setText(fallbackText);
        }
    }

//    private void styleReadOnlyTextField(javax.swing.JTextField textField) {
//    textField.setEditable(false);
//    textField.setBorder(null);
//    textField.setBackground(this.getBackground());
//    textField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // Font lớn hơn
//    textField.setForeground(new java.awt.Color(51, 51, 51)); // Màu chữ đậm hơn
//}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jdiaDoiPass = new javax.swing.JDialog();
        pnlDoiMatKhau = new javax.swing.JPanel();
        lblDialogTitle = new javax.swing.JLabel();
        lblOldPassTitle = new javax.swing.JLabel();
        txtMkCu = new javax.swing.JPasswordField();
        lblNewPassTitle = new javax.swing.JLabel();
        txtMkMoi = new javax.swing.JPasswordField();
        lblConfirmPassTitle = new javax.swing.JLabel();
        txtXacNhanMkMoi = new javax.swing.JPasswordField();
        pnlNut = new javax.swing.JPanel();
        lblQuayLai = new javax.swing.JLabel();
        btnXNDoiMatKhau = new javax.swing.JButton();
        btnDoiMatKhau1 = new javax.swing.JButton();
        lblAvatar = new javax.swing.JLabel();
        lblTenNV = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblIconPhone = new javax.swing.JLabel();
        lblPhoneTitle = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        lblIconEmail = new javax.swing.JLabel();
        lblEmailTitle = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblIconAddress = new javax.swing.JLabel();
        lblAddressTitle = new javax.swing.JLabel();
        scrAddress = new javax.swing.JScrollPane();
        txtAddress = new javax.swing.JTextArea();
        btnDoiMatKhau = new javax.swing.JButton();

        pnlDoiMatKhau.setBackground(new java.awt.Color(255, 255, 255));
        pnlDoiMatKhau.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        pnlDoiMatKhau.setLayout(new java.awt.GridBagLayout());

        lblDialogTitle.setText("ĐỔI MẬT KHẨU");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 25, 0);
        pnlDoiMatKhau.add(lblDialogTitle, gridBagConstraints);

        lblOldPassTitle.setText("Mật khẩu cũ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlDoiMatKhau.add(lblOldPassTitle, gridBagConstraints);

        txtMkCu.setMinimumSize(new java.awt.Dimension(300, 22));
        txtMkCu.setPreferredSize(new java.awt.Dimension(64, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        pnlDoiMatKhau.add(txtMkCu, gridBagConstraints);

        lblNewPassTitle.setText("Mật khẩu mới");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlDoiMatKhau.add(lblNewPassTitle, gridBagConstraints);

        txtMkMoi.setPreferredSize(new java.awt.Dimension(300, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        pnlDoiMatKhau.add(txtMkMoi, gridBagConstraints);

        lblConfirmPassTitle.setText("Xác nhận mật khẩu mới");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlDoiMatKhau.add(lblConfirmPassTitle, gridBagConstraints);

        txtXacNhanMkMoi.setPreferredSize(new java.awt.Dimension(300, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        pnlDoiMatKhau.add(txtXacNhanMkMoi, gridBagConstraints);

        pnlNut.setOpaque(false);
        pnlNut.setLayout(new java.awt.BorderLayout(50, 0));

        lblQuayLai.setText("Quay lại");
        pnlNut.add(lblQuayLai, java.awt.BorderLayout.LINE_START);

        btnXNDoiMatKhau.setText("Xác nhận đổi mật khẩu");
        pnlNut.add(btnXNDoiMatKhau, java.awt.BorderLayout.LINE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlDoiMatKhau.add(pnlNut, gridBagConstraints);

        javax.swing.GroupLayout jdiaDoiPassLayout = new javax.swing.GroupLayout(jdiaDoiPass.getContentPane());
        jdiaDoiPass.getContentPane().setLayout(jdiaDoiPassLayout);
        jdiaDoiPassLayout.setHorizontalGroup(
            jdiaDoiPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdiaDoiPassLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDoiMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jdiaDoiPassLayout.setVerticalGroup(
            jdiaDoiPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdiaDoiPassLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDoiMatKhau, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnDoiMatKhau1.setText("Đổi mật khẩu");
        btnDoiMatKhau1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMatKhau1ActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setLayout(new java.awt.GridBagLayout());

        lblAvatar.setText("lblAv");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(lblAvatar, gridBagConstraints);

        lblTenNV.setText("ten");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        add(lblTenNV, gridBagConstraints);

        lblMa.setText("ma");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(lblMa, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(jSeparator1, gridBagConstraints);

        lblIconPhone.setText("conPhone");
        lblIconPhone.setPreferredSize(new java.awt.Dimension(30, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(lblIconPhone, gridBagConstraints);

        lblPhoneTitle.setText("phoneTitle");
        lblPhoneTitle.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        add(lblPhoneTitle, gridBagConstraints);

        txtPhone.setText("txtPhone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        add(txtPhone, gridBagConstraints);

        lblIconEmail.setText("lblIconEmail");
        lblIconEmail.setPreferredSize(new java.awt.Dimension(30, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(lblIconEmail, gridBagConstraints);

        lblEmailTitle.setText("lblEmailTitle");
        lblEmailTitle.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        add(lblEmailTitle, gridBagConstraints);

        txtEmail.setText("txtEmail");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        add(txtEmail, gridBagConstraints);

        lblIconAddress.setText("lblIconAddress");
        lblIconAddress.setPreferredSize(new java.awt.Dimension(30, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        add(lblIconAddress, gridBagConstraints);

        lblAddressTitle.setText("lblAddressTitle");
        lblAddressTitle.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 10);
        add(lblAddressTitle, gridBagConstraints);

        txtAddress.setEditable(false);
        txtAddress.setColumns(20);
        txtAddress.setLineWrap(true);
        txtAddress.setRows(3);
        txtAddress.setWrapStyleWord(true);
        scrAddress.setViewportView(txtAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        add(scrAddress, gridBagConstraints);

        btnDoiMatKhau.setText("Đổi mật khẩu");
        btnDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMatKhauActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        add(btnDoiMatKhau, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhauActionPerformed

        styleChangePasswordDialog();

        // Xóa dữ liệu cũ
        txtMkCu.setText("");
        txtMkMoi.setText("");
        txtXacNhanMkMoi.setText("");

        jdiaDoiPass.setTitle("Đổi Mật Khẩu");
        jdiaDoiPass.pack();
        jdiaDoiPass.setLocationRelativeTo(this);
        jdiaDoiPass.setVisible(true);
    }//GEN-LAST:event_btnDoiMatKhauActionPerformed


    private void btnDoiMatKhau1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhau1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDoiMatKhau1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnDoiMatKhau1;
    private javax.swing.JButton btnXNDoiMatKhau;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JDialog jdiaDoiPass;
    private javax.swing.JLabel lblAddressTitle;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblConfirmPassTitle;
    private javax.swing.JLabel lblDialogTitle;
    private javax.swing.JLabel lblEmailTitle;
    private javax.swing.JLabel lblIconAddress;
    private javax.swing.JLabel lblIconEmail;
    private javax.swing.JLabel lblIconPhone;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblNewPassTitle;
    private javax.swing.JLabel lblOldPassTitle;
    private javax.swing.JLabel lblPhoneTitle;
    private javax.swing.JLabel lblQuayLai;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JPanel pnlDoiMatKhau;
    private javax.swing.JPanel pnlNut;
    private javax.swing.JScrollPane scrAddress;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtMkCu;
    private javax.swing.JPasswordField txtMkMoi;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JPasswordField txtXacNhanMkMoi;
    // End of variables declaration//GEN-END:variables
}

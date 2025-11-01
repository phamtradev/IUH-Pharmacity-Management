/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.thongtinnhanvien;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;

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
        txtVaiTro.setText(nv.getVaiTro());
        txtPhone.setText(nv.getSoDienThoai());
        txtEmail.setText(nv.getEmail());
        txtAddress.setText(nv.getDiaChi());
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
        styleReadOnlyTextField(txtVaiTro);
        styleReadOnlyTextField(txtPhone);
        styleReadOnlyTextField(txtEmail);
        txtAddress.setBackground(this.getBackground());
        txtAddress.setFont(new java.awt.Font("Segoe UI", 0, 18));
        txtAddress.setForeground(new java.awt.Color(51, 51, 51));
        scrAddress.setOpaque(false);
        scrAddress.getViewport().setOpaque(false);
        scrAddress.setBorder(null);

        //Các Icon
        setIcon(lblIconVaiTro, "/img/icons/phone.svg", "👤");
        setIcon(lblIconPhone, "/img/icons/phone.svg", "📞");
        setIcon(lblIconEmail, "/img/icons/email.svg", "📧");
        setIcon(lblIconAddress, "/img/icons/address.svg", "🏠");

        //label tiêu đề - Tăng kích thước
        lblVaiTroTitle.setText("Chức vụ:");
        lblVaiTroTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
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

    // Phương thức phụ an toàn để đặt icon
    private void setIcon(javax.swing.JLabel label, String path, String fallbackText) {
        try {
            java.net.URL iconUrl = getClass().getResource(path);
            if (iconUrl != null) {
                label.setText("");
                label.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon(iconUrl).derive(24, 24));
            } else {
                // Nếu không tìm thấy file, dùng text thay thế
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

        lblAvatar = new javax.swing.JLabel();
        lblTenNV = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblIconVaiTro = new javax.swing.JLabel();
        lblVaiTroTitle = new javax.swing.JLabel();
        txtVaiTro = new javax.swing.JTextField();
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
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 8, 0);
        add(lblTenNV, gridBagConstraints);

        lblMa.setText("ma");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        add(lblMa, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(jSeparator1, gridBagConstraints);

        lblIconVaiTro.setText("lblIconVaiTro");
        lblIconVaiTro.setPreferredSize(new java.awt.Dimension(35, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 15);
        add(lblIconVaiTro, gridBagConstraints);

        lblVaiTroTitle.setText("lblVaiTroTitle");
        lblVaiTroTitle.setPreferredSize(new java.awt.Dimension(130, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 20);
        add(lblVaiTroTitle, gridBagConstraints);

        txtVaiTro.setText("txtVaiTro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        add(txtVaiTro, gridBagConstraints);

        lblIconPhone.setText("conPhone");
        lblIconPhone.setPreferredSize(new java.awt.Dimension(35, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 15);
        add(lblIconPhone, gridBagConstraints);

        lblPhoneTitle.setText("phoneTitle");
        lblPhoneTitle.setPreferredSize(new java.awt.Dimension(130, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 20);
        add(lblPhoneTitle, gridBagConstraints);

        txtPhone.setText("txtPhone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        add(txtPhone, gridBagConstraints);

        lblIconEmail.setText("lblIconEmail");
        lblIconEmail.setPreferredSize(new java.awt.Dimension(35, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 15);
        add(lblIconEmail, gridBagConstraints);

        lblEmailTitle.setText("lblEmailTitle");
        lblEmailTitle.setPreferredSize(new java.awt.Dimension(130, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 20);
        add(lblEmailTitle, gridBagConstraints);

        txtEmail.setText("txtEmail");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        add(txtEmail, gridBagConstraints);

        lblIconAddress.setText("lblIconAddress");
        lblIconAddress.setPreferredSize(new java.awt.Dimension(35, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 15);
        add(lblIconAddress, gridBagConstraints);

        lblAddressTitle.setText("lblAddressTitle");
        lblAddressTitle.setPreferredSize(new java.awt.Dimension(130, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 20);
        add(lblAddressTitle, gridBagConstraints);

        txtAddress.setEditable(false);
        txtAddress.setColumns(20);
        txtAddress.setLineWrap(true);
        txtAddress.setRows(3);
        txtAddress.setWrapStyleWord(true);
        scrAddress.setViewportView(txtAddress);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 12, 0);
        add(scrAddress, gridBagConstraints);

        btnDoiMatKhau.setText("Đổi mật khẩu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        add(btnDoiMatKhau, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblAddressTitle;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblEmailTitle;
    private javax.swing.JLabel lblIconAddress;
    private javax.swing.JLabel lblIconEmail;
    private javax.swing.JLabel lblIconPhone;
    private javax.swing.JLabel lblIconVaiTro;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblPhoneTitle;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblVaiTroTitle;
    private javax.swing.JScrollPane scrAddress;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtVaiTro;
    // End of variables declaration//GEN-END:variables
}

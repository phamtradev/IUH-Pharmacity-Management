/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.thongtinnhanvien;

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

        jdiaDoiPass = new javax.swing.JDialog();
        txtMkCu = new javax.swing.JTextField();
        btnXNDoiMatKhau = new javax.swing.JButton();
        chkHienDoiMK = new javax.swing.JCheckBox();
        txtXacNhanMkMoi = new javax.swing.JPasswordField();
        txtMkMoi = new javax.swing.JPasswordField();
        lblQuayLai = new javax.swing.JLabel();
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

        txtMkCu.setText("Nhập mật khẩu cũ");
        txtMkCu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMkCuFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMkCuFocusLost(evt);
            }
        });
        txtMkCu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMkCuActionPerformed(evt);
            }
        });

        btnXNDoiMatKhau.setText("Đổi mật khẩu");
        btnXNDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXNDoiMatKhauActionPerformed(evt);
            }
        });

        chkHienDoiMK.setText("Hiện mật khẩu");
        chkHienDoiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkHienDoiMKActionPerformed(evt);
            }
        });

        txtXacNhanMkMoi.setText("jPasswordField1");
        txtXacNhanMkMoi.setPreferredSize(new java.awt.Dimension(148, 23));
        txtXacNhanMkMoi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtXacNhanMkMoiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtXacNhanMkMoiFocusLost(evt);
            }
        });
        txtXacNhanMkMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtXacNhanMkMoiActionPerformed(evt);
            }
        });

        txtMkMoi.setText("jPasswordField1");
        txtMkMoi.setPreferredSize(new java.awt.Dimension(148, 23));
        txtMkMoi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMkMoiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMkMoiFocusLost(evt);
            }
        });
        txtMkMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMkMoiActionPerformed(evt);
            }
        });

        lblQuayLai.setText("Quay lại");
        lblQuayLai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuayLaiMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jdiaDoiPassLayout = new javax.swing.GroupLayout(jdiaDoiPass.getContentPane());
        jdiaDoiPass.getContentPane().setLayout(jdiaDoiPassLayout);
        jdiaDoiPassLayout.setHorizontalGroup(
            jdiaDoiPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdiaDoiPassLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(jdiaDoiPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMkMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMkCu, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jdiaDoiPassLayout.createSequentialGroup()
                        .addComponent(lblQuayLai)
                        .addGap(52, 52, 52)
                        .addComponent(btnXNDoiMatKhau))
                    .addComponent(txtXacNhanMkMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkHienDoiMK))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jdiaDoiPassLayout.setVerticalGroup(
            jdiaDoiPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdiaDoiPassLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(txtMkCu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(txtMkMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(txtXacNhanMkMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(chkHienDoiMK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jdiaDoiPassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXNDoiMatKhau)
                    .addComponent(lblQuayLai))
                .addGap(22, 22, 22))
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
        jdiaDoiPass.setLocationRelativeTo(this);
        jdiaDoiPass.setPreferredSize(new Dimension(440, 300));
        addPlayholder(txtMkCu);
        addPlayholder(txtMkMoi);
        addPlayholder(txtXacNhanMkMoi);
        jdiaDoiPass.setVisible(true);
        jdiaDoiPass.requestFocusInWindow();
    }//GEN-LAST:event_btnDoiMatKhauActionPerformed
    public void addPlayholder(JTextField txt) {
        Font font = txt.getFont();
        font = font.deriveFont(Font.ITALIC);
        txt.setFont(font);
        txt.setForeground(Color.GRAY);
        txt.setBackground(Color.WHITE);
    }

    public void removePlayholder(JTextField txt) {
        Font font = txt.getFont();
        font = font.deriveFont(Font.PLAIN);
        txt.setForeground(Color.BLACK);
        txt.setFont(font);
    }

    private void txtMkCuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMkCuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMkCuActionPerformed

    private void btnDoiMatKhau1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhau1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDoiMatKhau1ActionPerformed

    private void btnXNDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXNDoiMatKhauActionPerformed
        javax.swing.JFrame parentFrame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);

        NhanVien nv = SessionManager.getInstance().getCurrentUser();

        //chk o day chua chk dc
        if (txtMkCu.getText().trim().isEmpty() || txtMkCu.getText() == null) {
            Notifications.getInstance().setJFrame(parentFrame);
            Notifications.getInstance().show(Notifications.Type.ERROR, "Vui lòng nhập mật khẩu cũ!");
            txtMkCu.requestFocus();
            return;
        }
//         if (!txtMkCu.getText().trim().equals(tk.getMatKhau())) {
//            Notifications.getInstance().setJFrame(parentFrame);
//            Notifications.getInstance().show(Notifications.Type.ERROR, "Mật khẩu cũ không chính xác!");
//            txtMkCu.requestFocus();
//            return;
//        }
        if (String.valueOf(txtMkMoi.getPassword()).trim().isBlank()) {
            Notifications.getInstance().setJFrame(parentFrame);
            Notifications.getInstance().show(Notifications.Type.ERROR, "Vui lòng nhập mật khẩu mới!");
            txtMkMoi.requestFocus();
            return;
        }
        if (String.valueOf(txtXacNhanMkMoi.getPassword()).trim().isBlank()) {
            Notifications.getInstance().setJFrame(parentFrame);
            Notifications.getInstance().show(Notifications.Type.ERROR, "Vui lòng xác nhận mật khẩu mới!");
            txtXacNhanMkMoi.requestFocus();
            return;
        }
        if (!String.valueOf(txtMkMoi.getPassword()).trim().equals(String.valueOf(txtXacNhanMkMoi.getPassword()).trim())) {
            Notifications.getInstance().setJFrame(parentFrame);
            Notifications.getInstance().show(Notifications.Type.ERROR, "Mật khẩu xác nhận chưa trùng khớp!");
            txtXacNhanMkMoi.requestFocus();
            return;
        }
        //o entity taikhoan
        String MAT_KHAU_SAI = "Mật khẩu phải từ 6 ký tự trở lên, có ít nhất 1 chữ số và 1 ký tự đặc biệt (@#$^*)";
        String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[@#$^*]).{6,}$";
        if (!String.valueOf(txtMkMoi.getPassword()).matches(REGEX_PASSWORD)) {
            Notifications.getInstance().setJFrame(parentFrame);
            Notifications.getInstance().show(Notifications.Type.ERROR, MAT_KHAU_SAI);
            txtMkMoi.requestFocus();
            return;
        }

        if (new TaiKhoanDAO().resetMatKhau(nv.getMaNhanVien().toLowerCase(), String.valueOf(txtMkMoi.getPassword()))) {
            Notifications.getInstance().setJFrame(parentFrame);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật mật khẩu thành công!");
            jdiaDoiPass.dispose();
        }

    }//GEN-LAST:event_btnXNDoiMatKhauActionPerformed

    private void txtMkCuFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMkCuFocusGained
        if (txtMkCu.getText().equals("Nhập mật khẩu cũ")) {
            txtMkCu.setText("");
            removePlayholder(txtMkCu);
        }
    }//GEN-LAST:event_txtMkCuFocusGained

    private void txtMkCuFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMkCuFocusLost
        if (txtMkCu.getText().equals("")) {
            txtMkCu.setText("Nhập mật khẩu cũ");
            addPlayholder(txtMkCu);
        }
    }//GEN-LAST:event_txtMkCuFocusLost

    private void txtXacNhanMkMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtXacNhanMkMoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtXacNhanMkMoiActionPerformed

    private void txtMkMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMkMoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMkMoiActionPerformed

    private void txtMkMoiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMkMoiFocusGained
        if (String.valueOf(txtMkMoi.getPassword()).equals("Nhập mật khẩu mới")) {
            txtMkMoi.setText("");
            txtMkMoi.setEchoChar('*');
            removePlayholder(txtMkMoi);
        }
    }//GEN-LAST:event_txtMkMoiFocusGained

    private void txtMkMoiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMkMoiFocusLost
        if (String.valueOf(txtMkMoi.getPassword()).equals("")) {
            txtMkMoi.setEchoChar((char) 0);
            txtMkMoi.setText("Nhập mật khẩu mới");
            addPlayholder(txtMkMoi);
        }
    }//GEN-LAST:event_txtMkMoiFocusLost

    private void txtXacNhanMkMoiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtXacNhanMkMoiFocusGained
        if (String.valueOf(txtXacNhanMkMoi.getPassword()).equals("Xác nhận mật khẩu mới")) {
            txtXacNhanMkMoi.setText("");
            txtXacNhanMkMoi.setEchoChar('*');
            removePlayholder(txtXacNhanMkMoi);
        }
    }//GEN-LAST:event_txtXacNhanMkMoiFocusGained

    private void txtXacNhanMkMoiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtXacNhanMkMoiFocusLost
        if (String.valueOf(txtXacNhanMkMoi.getPassword()).equals("")) {
            txtXacNhanMkMoi.setEchoChar((char) 0);
            txtXacNhanMkMoi.setText("Xác nhận mật khẩu mới");
            addPlayholder(txtXacNhanMkMoi);
        }
    }//GEN-LAST:event_txtXacNhanMkMoiFocusLost

    private void chkHienDoiMKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkHienDoiMKActionPerformed
        if (chkHienDoiMK.isSelected()) {
            txtMkMoi.setEchoChar((char) 0);
            txtXacNhanMkMoi.setEchoChar((char) 0);
        } else {
            String mkMoi = String.valueOf(txtMkMoi.getPassword());
            if (mkMoi.isEmpty() || mkMoi.equals("Nhập mật khẩu mới")) {
                txtMkMoi.setEchoChar((char) 0);
            } else {
                txtMkMoi.setEchoChar('\u2022');
            }
            String xnMKMoi = String.valueOf(txtXacNhanMkMoi.getPassword());
            if (xnMKMoi.isEmpty() || xnMKMoi.equals("Xác nhận mật khẩu mới")) {
                txtXacNhanMkMoi.setEchoChar((char) 0);
            } else {
                txtXacNhanMkMoi.setEchoChar('\u2022');
            }
        }
        txtMkMoi.repaint();
        txtXacNhanMkMoi.repaint();
    }//GEN-LAST:event_chkHienDoiMKActionPerformed

    private void lblQuayLaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuayLaiMouseClicked
        jdiaDoiPass.dispose();
        txtMkCu.setText("Nhập mật khẩu cũ");
        txtMkMoi.setEchoChar((char)0);
        txtMkMoi.setText("Nhập mật khẩu mới");
        txtXacNhanMkMoi.setEchoChar((char)0);
        txtXacNhanMkMoi.setText("Xác nhận mật khẩu mới");
    }//GEN-LAST:event_lblQuayLaiMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnDoiMatKhau1;
    private javax.swing.JButton btnXNDoiMatKhau;
    private javax.swing.JCheckBox chkHienDoiMK;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JDialog jdiaDoiPass;
    private javax.swing.JLabel lblAddressTitle;
    private javax.swing.JLabel lblAvatar;
    private javax.swing.JLabel lblEmailTitle;
    private javax.swing.JLabel lblIconAddress;
    private javax.swing.JLabel lblIconEmail;
    private javax.swing.JLabel lblIconPhone;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblPhoneTitle;
    private javax.swing.JLabel lblQuayLai;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JScrollPane scrAddress;
    private javax.swing.JTextArea txtAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtMkCu;
    private javax.swing.JPasswordField txtMkMoi;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JPasswordField txtXacNhanMkMoi;
    // End of variables declaration//GEN-END:variables
}

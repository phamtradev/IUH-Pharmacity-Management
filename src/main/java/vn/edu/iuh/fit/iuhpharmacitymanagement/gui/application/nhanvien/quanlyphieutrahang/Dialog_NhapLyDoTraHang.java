/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieutrahang;

import javax.swing.*;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

/**
 * Dialog để nhập lý do trả hàng
 * @author PhamTra
 */
public class Dialog_NhapLyDoTraHang extends javax.swing.JDialog {

    private String lyDo = "";
    private boolean daXacNhan = false;

    public Dialog_NhapLyDoTraHang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
        txtLyDo.setText(lyDo);
    }

    public boolean isDaXacNhan() {
        return daXacNhan;
    }

    private void initComponents() {
        setTitle("Nhập lý do trả hàng");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new java.awt.Dimension(500, 300));

        // Main panel
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setBackground(java.awt.Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new java.awt.BorderLayout(10, 10));

        // Title label
        javax.swing.JLabel lblTitle = new javax.swing.JLabel();
        lblTitle.setText("Nhập lý do trả hàng");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, java.awt.BorderLayout.NORTH);

        // Text area panel
        javax.swing.JPanel textPanel = new javax.swing.JPanel();
        textPanel.setBackground(java.awt.Color.WHITE);
        textPanel.setLayout(new java.awt.BorderLayout(5, 5));

        javax.swing.JLabel lblLyDo = new javax.swing.JLabel();
        lblLyDo.setText("Lý do:");
        lblLyDo.setFont(new java.awt.Font("Segoe UI", 0, 14));
        textPanel.add(lblLyDo, java.awt.BorderLayout.NORTH);

        txtLyDo = new javax.swing.JTextArea();
        txtLyDo.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        txtLyDo.setRows(6);
        txtLyDo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(txtLyDo);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        textPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(textPanel, java.awt.BorderLayout.CENTER);

        // Button panel
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        buttonPanel.setBackground(java.awt.Color.WHITE);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        btnHuy = new javax.swing.JButton();
        btnHuy.setText("Hủy");
        btnHuy.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnHuy.setPreferredSize(new java.awt.Dimension(100, 35));
        ButtonStyles.apply(btnHuy, ButtonStyles.Type.SECONDARY);
        btnHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        btnXacNhan = new javax.swing.JButton();
        btnXacNhan.setText("Xác nhận");
        btnXacNhan.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnXacNhan.setPreferredSize(new java.awt.Dimension(100, 35));
        ButtonStyles.apply(btnXacNhan, ButtonStyles.Type.PRIMARY);
        btnXacNhan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnXacNhan);

        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack();
    }

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {
        lyDo = txtLyDo.getText().trim();
        if (lyDo.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập lý do trả hàng!");
            txtLyDo.requestFocus();
            return;
        }
        daXacNhan = true;
        dispose();
    }

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {
        daXacNhan = false;
        dispose();
    }

    // Variables declaration
    private javax.swing.JTextArea txtLyDo;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnHuy;
    // End of variables declaration
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlydonvitinh;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonViTinhBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;
import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLyDonViTinh extends javax.swing.JPanel {

    private final DonViTinhBUS donViTinhBUS;
    private TableDesign tableDesign;

    public GD_QuanLyDonViTinh() {
        this.donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
        initComponents();

        // Áp dụng ButtonStyles và FontStyles
        applyStyles();

        setUIManager();
        setupModalStyle();
        fillTable();
    }

    private void applyStyles() {
        // Buttons chính
        ButtonStyles.apply(btnThem, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnThem, FontStyles.Type.BUTTON_MEDIUM);
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.setPreferredSize(new java.awt.Dimension(95, 40));

        ButtonStyles.apply(btnSua, ButtonStyles.Type.PRIMARY);
        FontStyles.apply(btnSua, FontStyles.Type.BUTTON_MEDIUM);
        btnSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSua.setPreferredSize(new java.awt.Dimension(95, 40));

        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        FontStyles.apply(btnXoa, FontStyles.Type.BUTTON_MEDIUM);
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.setPreferredSize(new java.awt.Dimension(95, 40));

        ButtonStyles.apply(btnTimKiem, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnTimKiem, FontStyles.Type.BUTTON_MEDIUM);
        btnTimKiem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimKiem.setPreferredSize(new java.awt.Dimension(150, 40));
    }

    private void setupModalStyle() {
        // Cấu hình Modal Add
        modalAddUnit.setTitle("THÊM ĐƠN VỊ TÍNH MỚI");
        modalAddUnit.setSize(750, 300);

        // Cấu hình Modal Edit
        modalEditUnit.setTitle("CẬP NHẬT ĐƠN VỊ TÍNH");
        modalEditUnit.setSize(750, 300);

        // Loại bỏ viền thừa của panel trong modal
        jPanel2.putClientProperty(FlatClientProperties.STYLE, "background:#F8F9FA;border:0,0,0,0");
        jPanel3.putClientProperty(FlatClientProperties.STYLE, "background:#F8F9FA;border:0,0,0,0");

        // Style cho text fields
        txtNameUnitAdd.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtNameUnitEdit.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        txtTimKiem.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        // Style cho buttons trong modal Add - giống modal Quản lý Sản phẩm
        btnAddUnit.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#5C6BC0;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#4A5AB3;"
                + "pressedBackground:#3949AB;"
                + "arc:10;"
                + "borderWidth:0");

        btnExitModalAdd.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10");

        // Style cho buttons trong modal Edit - giống modal Quản lý Sản phẩm
        btnEditUnit.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:#5C6BC0;"
                + "foreground:#FFFFFF;"
                + "hoverBackground:#4A5AB3;"
                + "pressedBackground:#3949AB;"
                + "arc:10;"
                + "borderWidth:0");

        btnExitModalEdit.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10");
    }

    private void setUIManager() {
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên đơn vị tính");
        txtNameUnitAdd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên đơn vị tính");
        txtNameUnitEdit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên đơn vị tính mới");
    }

    private void fillTable() {
        String[] headers = {"Mã đơn vị tính", "Tên"};
        List<Integer> tableWidths = Arrays.asList(200, 800);
        tableDesign = new TableDesign(headers, tableWidths);
        scrollTable.setViewportView(tableDesign.getTable());
        scrollTable.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        List<DonViTinh> donViTinhs = donViTinhBUS.layTatCaDonViTinh();
        fillContent(donViTinhs);
    }

    private void fillContent(List<DonViTinh> donViTinhs) {
        tableDesign.getModelTable().setRowCount(0);
        for (DonViTinh dvt : donViTinhs) {
            tableDesign.getModelTable().addRow(new Object[]{dvt.getMaDonVi(),
                dvt.getTenDonVi(), null});
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modalAddUnit = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNameUnitAdd = new javax.swing.JTextField();
        btnAddUnit = new javax.swing.JButton();
        btnExitModalAdd = new javax.swing.JButton();
        modalEditUnit = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNameUnitEdit = new javax.swing.JTextField();
        btnEditUnit = new javax.swing.JButton();
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

        modalAddUnit.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalAddUnit.setTitle("Thêm đơn vị tính");
        modalAddUnit.setMinimumSize(new java.awt.Dimension(738, 260));
        modalAddUnit.setModal(true);
        modalAddUnit.setResizable(false);
        modalAddUnit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modalAddUnitMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(738, 248));
        jPanel2.setMinimumSize(new java.awt.Dimension(738, 248));
        jPanel2.setPreferredSize(new java.awt.Dimension(738, 248));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Tên đơn vị tính");

        txtNameUnitAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnAddUnit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnAddUnit.setText("THÊM");
        btnAddUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUnitActionPerformed(evt);
            }
        });

        btnExitModalAdd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExitModalAdd.setText("THOÁT");
        btnExitModalAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitModalAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNameUnitAdd)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(509, Short.MAX_VALUE)
                        .addComponent(btnAddUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNameUnitAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExitModalAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout modalAddUnitLayout = new javax.swing.GroupLayout(modalAddUnit.getContentPane());
        modalAddUnit.getContentPane().setLayout(modalAddUnitLayout);
        modalAddUnitLayout.setHorizontalGroup(
            modalAddUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalAddUnitLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        modalAddUnitLayout.setVerticalGroup(
            modalAddUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalAddUnitLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        modalEditUnit.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalEditUnit.setTitle("Thêm đơn vị tính");
        modalEditUnit.setMinimumSize(new java.awt.Dimension(738, 260));
        modalEditUnit.setModal(true);
        modalEditUnit.setResizable(false);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(738, 248));
        jPanel3.setMinimumSize(new java.awt.Dimension(738, 248));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setText("Tên đơn vị tính");

        txtNameUnitEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnEditUnit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnEditUnit.setText("Sửa");
        btnEditUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUnitActionPerformed(evt);
            }
        });

        btnExitModalEdit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnExitModalEdit.setText("Thoát");
        btnExitModalEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitModalEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNameUnitEdit)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(509, Short.MAX_VALUE)
                        .addComponent(btnEditUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNameUnitEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExitModalEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout modalEditUnitLayout = new javax.swing.GroupLayout(modalEditUnit.getContentPane());
        modalEditUnit.getContentPane().setLayout(modalEditUnitLayout);
        modalEditUnitLayout.setHorizontalGroup(
            modalEditUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalEditUnitLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        modalEditUnitLayout.setVerticalGroup(
            modalEditUnitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modalEditUnitLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        pnlThongTin.setBackground(new java.awt.Color(255, 255, 255));
        pnlThongTin.setLayout(new java.awt.BorderLayout());
        
        // Thêm tiêu đề "DANH SÁCH THÔNG TIN ĐƠN VỊ TÍNH"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN ĐƠN VỊ TÍNH");
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
            .addComponent(pnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitModalAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitModalAddActionPerformed
        txtNameUnitAdd.setText("");
        modalAddUnit.dispose();
    }//GEN-LAST:event_btnExitModalAddActionPerformed

    private void btnAddUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUnitActionPerformed
        String name = txtNameUnitAdd.getText().trim();
        if (!name.equals("")) {
            try {
                if (donViTinhBUS.kiemTraTonTaiTheoTen(name)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Tên đơn vị tính đã tồn tại trong hệ thống.");
                    return;
                }
                donViTinhBUS.taoDonViTinh(new DonViTinh(null, name));
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm đơn vị tính thành công");
                txtNameUnitAdd.setText("");
                modalAddUnit.dispose();
                fillContent(donViTinhBUS.layTatCaDonViTinh());
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi thêm đơn vị tính: " + e.getMessage());
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Dữ liệu nhập không được rỗng.");
        }

    }//GEN-LAST:event_btnAddUnitActionPerformed

    private void btnEditUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUnitActionPerformed
        String name = txtNameUnitEdit.getText().trim();
        if (!name.equals("")) {
            try {
                DonViTinh donViTinh = donViTinhBUS.layDonViTinhTheoMa(unitIdEdit);

                // Kiểm tra tên đơn vị tính đã tồn tại chưa (trừ chính nó)
                if (donViTinhBUS.kiemTraTonTaiTheoTen(name) && !donViTinh.getTenDonVi().equals(name)) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Tên đơn vị tính đã tồn tại trong hệ thống.");
                    return;
                }

                donViTinh.setTenDonVi(name);
                donViTinhBUS.capNhatDonViTinh(donViTinh);

                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thay đổi đơn vị tính thành công");
                txtNameUnitEdit.setText("");
                modalEditUnit.dispose();
                fillContent(donViTinhBUS.layTatCaDonViTinh());
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi sửa đơn vị tính: " + e.getMessage());
            }
        } else {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Tên đơn vị tính không được rỗng.");
        }
    }//GEN-LAST:event_btnEditUnitActionPerformed

    private void btnExitModalEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitModalEditActionPerformed
        txtNameUnitEdit.setText("");
        modalEditUnit.dispose();
    }//GEN-LAST:event_btnExitModalEditActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        try {
            String name = txtTimKiem.getText().trim();
            List<DonViTinh> donViTinhs = donViTinhBUS.timDonViTinhTheoTenGanDung(name);
            fillContent(donViTinhs);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow != -1) {
            unitIdEdit = (String) tableDesign.getTable().getValueAt(selectedRow, 0);
            txtNameUnitEdit.setText((String) tableDesign.getTable().getValueAt(selectedRow, 1));
            modalEditUnit.setLocationRelativeTo(null);
            modalEditUnit.setVisible(true);
            if (tableDesign.getTable().getCellEditor() != null) {
                tableDesign.getTable().getCellEditor().stopCellEditing();
            }
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        modalAddUnit.setLocationRelativeTo(null);
        modalAddUnit.setVisible(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn đơn vị tính cần xóa!");
            return;
        }

        String maDonViTinh = (String) tableDesign.getTable().getValueAt(selectedRow, 0);
        String tenDonViTinh = (String) tableDesign.getTable().getValueAt(selectedRow, 1);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa đơn vị tính \"" + tenDonViTinh + "\" không?",
                "Xác nhận xóa",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                boolean result = donViTinhBUS.xoaDonViTinh(maDonViTinh);
                if (result) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xóa đơn vị tính thành công!");
                    fillTable();
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Xóa đơn vị tính thất bại!");
                }
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void modalAddUnitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modalAddUnitMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_modalAddUnitMouseClicked

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        try {
            String name = txtTimKiem.getText().trim();
            List<DonViTinh> donViTinhs = donViTinhBUS.timDonViTinhTheoTenGanDung(name);
            fillContent(donViTinhs);
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private String unitIdEdit;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddUnit;
    private javax.swing.JButton btnEditUnit;
    private javax.swing.JButton btnExitModalAdd;
    private javax.swing.JButton btnExitModalEdit;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JDialog modalAddUnit;
    private javax.swing.JDialog modalEditUnit;
    private javax.swing.JPanel pnAll;
    private javax.swing.JPanel pnlChinhSua;
    private javax.swing.JPanel pnlKhungTimKiem;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JPanel pnlTieuDe;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTextField txtNameUnitAdd;
    private javax.swing.JTextField txtNameUnitEdit;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlysanpham;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.Image;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import raven.toast.Notifications;

/**
 *
 * @author PhamTra
 */
public class GD_QuanLySanPham extends javax.swing.JPanel {

    private String productEdit;
    private ImageIcon imageProductAdd;
    private ImageIcon imageProductEdit;
    private TableDesign tableDesign;
    private boolean isManager = false;

    public GD_QuanLySanPham() {
        this(false); // Mặc định là nhân viên
    }
    
    public GD_QuanLySanPham(boolean isManager) {
        this.isManager = isManager;
        initComponents();
        setUIManager();
        fillTable();
        addIconFeature();
        
        // Hiển thị button xóa chỉ khi là quản lý
        if (isManager) {
            btnDelete.setVisible(true);
        }
    }

    private void setUIManager() {
        try {
            // Placeholder cho TextField tìm kiếm
            txtSearchNamePD.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên sản phẩm hoặc số đăng ký");
            try {
                // TextField txtSearchSDKPD có thể chưa được generate, kiểm tra trước
                java.lang.reflect.Field field = this.getClass().getDeclaredField("txtSearchSDKPD");
                field.setAccessible(true);
                javax.swing.JTextField txtSearchSDKPD = (javax.swing.JTextField) field.get(this);
                if (txtSearchSDKPD != null) {
                    txtSearchSDKPD.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng ký");
                }
            } catch (Exception e) {
                // Bỏ qua nếu field chưa tồn tại
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi thiết lập UI Manager: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Placeholder cho modal THÊM sản phẩm
        txtCountryOfOrigin.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập xuất xứ");
        txtProductActiveIngre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập hoạt chất");
        txtProductDosage.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập liều lượng");
        txtProductManufacturer.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhà sản xuất");
        txtProductName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên sản phẩm");
        txtProductPakaging.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập quy trình đóng gói");
        txtProductPurchasePrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập giá nhập vào");
        txtProductRegisNumber.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí");
        txtProductSellingPrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập giá bán");
        
        // Placeholder cho modal SỬA sản phẩm
        txtCountryOfOrigin1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập xuất xứ");
        txtProductActiveIngre1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập hoạt chất");
        txtProductDosage1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập liều lượng");
        txtProductManufacturer1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhà sản xuất");
        txtProductName1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên sản phẩm");
        txtProductPakaging1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập quy trình đóng gói");
        txtProductPurchasePrice1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập giá nhập vào");
        txtProductRegisNumber1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí");
        txtProductSellingPrice1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập giá bán");
        
        UIManager.put("Button.arc", 10);
    }

    private void fillTable() {
        String[] headers = {"Mã sản phẩm", "Tên sản phẩm", "Đơn vị tính", "Số đăng kí", "Xuất xứ", "Loại sản phẩm", "Giá mua", "Giá bán", "Trạng thái"};
        List<Integer> tableWidths = Arrays.asList(120, 300, 100, 120, 100, 110, 80, 80, 120);
        tableDesign = new TableDesign(headers, tableWidths);
        ProductScrollPane.setViewportView(tableDesign.getTable());
        ProductScrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        //addEventBtnEditInTable();

    }

    private void updateTabQuantity() {

    }

    private void addIconFeature() {}

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modelEditProduct = new javax.swing.JDialog();
        tabEdit = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        lblImageEdit = new javax.swing.JLabel();
        btnAddImage1 = new javax.swing.JButton();
        txtProductName1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtProductManufacturer1 = new javax.swing.JTextField();
        txtCountryOfOrigin1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtProductRegisNumber1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtProductPurchasePrice1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtProductActiveIngre1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtProductSellingPrice1 = new javax.swing.JTextField();
        txtProductDosage1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtProductPakaging1 = new javax.swing.JTextField();
        btnExitProductADD1 = new javax.swing.JButton();
        btnEditProduct = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        cbbProductTypeEdit = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        comboUnitEdit = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        ScrollPaneTab3 = new javax.swing.JScrollPane();
        modelProductAdd = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        btnAddImage = new javax.swing.JButton();
        txtProductName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtProductManufacturer = new javax.swing.JTextField();
        txtCountryOfOrigin = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtProductRegisNumber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtProductPurchasePrice = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtProductActiveIngre = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtProductSellingPrice = new javax.swing.JTextField();
        txtProductDosage = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtProductPakaging = new javax.swing.JTextField();
        btnExitProductADD = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        cbbProductTypeAdd = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        comboUnitAdd = new javax.swing.JComboBox<>();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtSearchNamePD = new javax.swing.JTextField();
        optionPdType = new javax.swing.JComboBox<>();
        optionPdStatus = new javax.swing.JComboBox<>();
        btnOpenModalAddSup = new javax.swing.JButton();
        actionPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        ProductScrollPane = new javax.swing.JScrollPane();

        modelEditProduct.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modelEditProduct.setTitle("Sửa sản phẩm");
        modelEditProduct.setMinimumSize(new java.awt.Dimension(1350, 850));
        modelEditProduct.setModal(true);

        tabEdit.setBackground(new java.awt.Color(255, 255, 255));
        tabEdit.setMaximumSize(new java.awt.Dimension(1350, 850));
        tabEdit.setMinimumSize(new java.awt.Dimension(1350, 850));
        tabEdit.setPreferredSize(new java.awt.Dimension(1350, 850));
        tabEdit.setRequestFocusEnabled(false);
        tabEdit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabEditStateChanged(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setMaximumSize(new java.awt.Dimension(1350, 850));
        jPanel8.setMinimumSize(new java.awt.Dimension(1350, 850));
        jPanel8.setPreferredSize(new java.awt.Dimension(1350, 850));

        lblImageEdit.setBackground(new java.awt.Color(255, 255, 255));
        lblImageEdit.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAddImage1.setBackground(new java.awt.Color(92, 107, 192));
        btnAddImage1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnAddImage1.setForeground(new java.awt.Color(255, 255, 255));
        btnAddImage1.setText("Chọn Ảnh");
        btnAddImage1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImage1ActionPerformed(evt);
            }
        });

        txtProductName1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel10.setText("Tên sản phẩm");

        txtProductManufacturer1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductManufacturer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductManufacturer1ActionPerformed(evt);
            }
        });

        txtCountryOfOrigin1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtCountryOfOrigin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCountryOfOrigin1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setText("Liều lượng");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel17.setText("Số đăng ký");

        txtProductRegisNumber1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel18.setText("Giá nhập");

        txtProductPurchasePrice1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductPurchasePrice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductPurchasePrice1ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel19.setText("Hoạt Chất");

        txtProductActiveIngre1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductActiveIngre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductActiveIngre1ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel20.setText("Giá bán");

        txtProductSellingPrice1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductSellingPrice1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductSellingPrice1ActionPerformed(evt);
            }
        });

        txtProductDosage1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductDosage1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductDosage1ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel21.setText("Nhà sản xuất");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel22.setText("Loai sản phẩm");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel23.setText("Quy trình đóng gói");

        txtProductPakaging1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductPakaging1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductPakaging1ActionPerformed(evt);
            }
        });

        btnExitProductADD1.setText("Thoát");
        btnExitProductADD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitProductADD1ActionPerformed(evt);
            }
        });

        btnEditProduct.setBackground(new java.awt.Color(92, 107, 192));
        btnEditProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnEditProduct.setText("Sửa");
        btnEditProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditProductActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel24.setText("Xuất xứ");

        cbbProductTypeEdit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbbProductTypeEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbProductTypeEditActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel25.setText("Đơn vị tính");

        comboUnitEdit.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(134, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnEditProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExitProductADD1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(btnAddImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(134, 134, 134))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lblImageEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductName1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductRegisNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductActiveIngre1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductDosage1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductManufacturer1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCountryOfOrigin1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbbProductTypeEdit, 0, 345, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductPurchasePrice1)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductSellingPrice1)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductPakaging1)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboUnitEdit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductName1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                            .addComponent(cbbProductTypeEdit))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductRegisNumber1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductPurchasePrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductActiveIngre1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductSellingPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductPakaging1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductDosage1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboUnitEdit)
                            .addComponent(txtProductManufacturer1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCountryOfOrigin1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExitProductADD1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lblImageEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(286, 286, 286))))
        );

        tabEdit.addTab("Thông tin sản phẩm", jPanel8);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPaneTab3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1350, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPaneTab3, javax.swing.GroupLayout.DEFAULT_SIZE, 815, Short.MAX_VALUE)
        );

        tabEdit.addTab("Thông tin số lượng", jPanel11);

        javax.swing.GroupLayout modelEditProductLayout = new javax.swing.GroupLayout(modelEditProduct.getContentPane());
        modelEditProduct.getContentPane().setLayout(modelEditProductLayout);
        modelEditProductLayout.setHorizontalGroup(
            modelEditProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        modelEditProductLayout.setVerticalGroup(
            modelEditProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        modelProductAdd.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modelProductAdd.setTitle("Thêm sản phẩm");
        modelProductAdd.setMinimumSize(new java.awt.Dimension(1350, 820));
        modelProductAdd.setModal(true);
        modelProductAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modelProductAddMouseClicked(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setMaximumSize(new java.awt.Dimension(1350, 820));
        jPanel7.setMinimumSize(new java.awt.Dimension(1350, 820));
        jPanel7.setPreferredSize(new java.awt.Dimension(1350, 820));

        lblImage.setBackground(new java.awt.Color(255, 255, 255));
        lblImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAddImage.setBackground(new java.awt.Color(92, 107, 192));
        btnAddImage.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnAddImage.setForeground(new java.awt.Color(255, 255, 255));
        btnAddImage.setText("Chọn Ảnh");
        btnAddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImageActionPerformed(evt);
            }
        });

        txtProductName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductNameActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setText("Tên sản phẩm");

        txtProductManufacturer.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductManufacturer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductManufacturerActionPerformed(evt);
            }
        });

        txtCountryOfOrigin.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtCountryOfOrigin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCountryOfOriginActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel5.setText("Liều lượng");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel6.setText("Số đăng ký");

        txtProductRegisNumber.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductRegisNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductRegisNumberActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("Giá nhập");

        txtProductPurchasePrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductPurchasePrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductPurchasePriceActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel8.setText("Hoạt Chất");

        txtProductActiveIngre.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductActiveIngre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductActiveIngreActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel9.setText("Giá bán");

        txtProductSellingPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductSellingPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductSellingPriceActionPerformed(evt);
            }
        });

        txtProductDosage.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductDosage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductDosageActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel11.setText("Nhà sản xuất");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setText("Loai sản phẩm");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel13.setText("Quy trình đóng gói");

        txtProductPakaging.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtProductPakaging.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductPakagingActionPerformed(evt);
            }
        });

        btnExitProductADD.setText("Thoát");
        btnExitProductADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitProductADDActionPerformed(evt);
            }
        });

        btnAddProduct.setBackground(new java.awt.Color(92, 107, 192));
        btnAddProduct.setForeground(new java.awt.Color(255, 255, 255));
        btnAddProduct.setText("Thêm");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel15.setText("Xuất xứ");

        cbbProductTypeAdd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbbProductTypeAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbProductTypeAddActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel14.setText("Đơn vị tính");

        comboUnitAdd.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(134, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExitProductADD, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(btnAddImage, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(134, 134, 134)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductRegisNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductActiveIngre, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductDosage, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductManufacturer, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCountryOfOrigin, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbbProductTypeAdd, 0, 345, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductPurchasePrice)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductSellingPrice)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductPakaging)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboUnitAdd, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductName, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                            .addComponent(cbbProductTypeAdd))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductRegisNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProductActiveIngre, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductPakaging, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductDosage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboUnitAdd)
                            .addComponent(txtProductManufacturer, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCountryOfOrigin, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnExitProductADD, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddImage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(286, 286, 286))))
        );

        javax.swing.GroupLayout modelProductAddLayout = new javax.swing.GroupLayout(modelProductAdd.getContentPane());
        modelProductAdd.getContentPane().setLayout(modelProductAddLayout);
        modelProductAddLayout.setHorizontalGroup(
            modelProductAddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        modelProductAddLayout.setVerticalGroup(
            modelProductAddLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setBackground(new java.awt.Color(204, 204, 0));

        pnAll.setBackground(new java.awt.Color(255, 255, 255));
        pnAll.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(232, 232, 232)));
        headerPanel.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(590, 100));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 25));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMinimumSize(new java.awt.Dimension(600, 50));
        jPanel6.setPreferredSize(new java.awt.Dimension(900, 50));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        txtSearchNamePD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchNamePD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        txtSearchNamePD.setMinimumSize(new java.awt.Dimension(300, 40));
        txtSearchNamePD.setPreferredSize(new java.awt.Dimension(300, 40));
        txtSearchNamePD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchNamePDActionPerformed(evt);
            }
        });
        jPanel6.add(txtSearchNamePD);

        optionPdType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        optionPdType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Thuốc", "Vật tư y tế", "Thực phẩm chức năng", "Chăm sóc trẻ em", "Thiết bị y tế" }));
        optionPdType.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        optionPdType.setMinimumSize(new java.awt.Dimension(170, 40));
        optionPdType.setPreferredSize(new java.awt.Dimension(170, 40));
        optionPdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionPdTypeActionPerformed(evt);
            }
        });
        jPanel6.add(optionPdType);

        optionPdStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        optionPdStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đang hoạt động", "Ngưng bán" }));
        optionPdStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        optionPdStatus.setMinimumSize(new java.awt.Dimension(170, 40));
        optionPdStatus.setPreferredSize(new java.awt.Dimension(170, 40));
        optionPdStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionPdStatusActionPerformed(evt);
            }
        });
        jPanel6.add(optionPdStatus);

        btnOpenModalAddSup.setBackground(new java.awt.Color(115, 165, 71));
        btnOpenModalAddSup.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnOpenModalAddSup.setForeground(new java.awt.Color(255, 255, 255));
        btnOpenModalAddSup.setText("Tìm kiếm");
        btnOpenModalAddSup.setMaximumSize(new java.awt.Dimension(150, 40));
        btnOpenModalAddSup.setMinimumSize(new java.awt.Dimension(150, 40));
        btnOpenModalAddSup.setPreferredSize(new java.awt.Dimension(150, 40));
        btnOpenModalAddSup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenModalAddSupActionPerformed(evt);
            }
        });
        jPanel6.add(btnOpenModalAddSup);

        jPanel5.add(jPanel6);

        headerPanel.add(jPanel5, java.awt.BorderLayout.CENTER);

        actionPanel.setBackground(new java.awt.Color(255, 255, 255));
        actionPanel.setPreferredSize(new java.awt.Dimension(320, 60));
        actionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 25));

        btnAdd.setBackground(new java.awt.Color(115, 165, 71));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setPreferredSize(new java.awt.Dimension(95, 40));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        actionPanel.add(btnAdd);

        btnUpdate.setBackground(new java.awt.Color(255, 193, 7));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 0, 0));
        btnUpdate.setText("Sửa");
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setPreferredSize(new java.awt.Dimension(95, 40));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        actionPanel.add(btnUpdate);

        btnDelete.setBackground(new java.awt.Color(220, 60, 60));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setFocusPainted(false);
        btnDelete.setPreferredSize(new java.awt.Dimension(95, 40));
        btnDelete.setVisible(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        actionPanel.add(btnDelete);

        headerPanel.add(actionPanel, java.awt.BorderLayout.WEST);

        pnAll.add(headerPanel, java.awt.BorderLayout.NORTH);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        // Thêm tiêu đề "Danh sách thông tin thuốc"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN THUỐC");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 16));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255)); // Chữ màu trắng
        titlePanel.add(lblTitle);
        jPanel4.add(titlePanel, java.awt.BorderLayout.NORTH);

        jPanel4.add(ProductScrollPane, java.awt.BorderLayout.CENTER);

        pnAll.add(jPanel4, java.awt.BorderLayout.CENTER);

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

    private void btnOpenModalAddSupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenModalAddSupActionPerformed

    }//GEN-LAST:event_btnOpenModalAddSupActionPerformed

    private void txtSearchNamePDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchNamePDActionPerformed

    }//GEN-LAST:event_txtSearchNamePDActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }

        // Lấy thông tin từ bảng
        String maSP = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenSP = tableDesign.getTable().getValueAt(selectedRow, 1).toString();
        String donViTinh = tableDesign.getTable().getValueAt(selectedRow, 2).toString();
        String soDangKy = tableDesign.getTable().getValueAt(selectedRow, 3).toString();
        String xuatXu = tableDesign.getTable().getValueAt(selectedRow, 4).toString();
        String loaiSP = tableDesign.getTable().getValueAt(selectedRow, 5).toString();
        String giaMua = tableDesign.getTable().getValueAt(selectedRow, 6).toString();
        String giaBan = tableDesign.getTable().getValueAt(selectedRow, 7).toString();

        // Hiển thị lên modal edit
        productEdit = maSP;
        txtProductName1.setText(tenSP);
        txtProductRegisNumber1.setText(soDangKy);
        txtCountryOfOrigin1.setText(xuatXu);
        txtProductPurchasePrice1.setText(giaMua);
        txtProductSellingPrice1.setText(giaBan);

        updateTabQuantity();
        modelEditProduct.setLocationRelativeTo(this);
        modelEditProduct.setVisible(true);

    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        
        String maSanPham = tableDesign.getTable().getValueAt(selectedRow, 0).toString();
        String tenSanPham = tableDesign.getTable().getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa sản phẩm \"" + tenSanPham + "\" không?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Thêm logic xóa sản phẩm từ database
            Notifications.getInstance().show(Notifications.Type.INFO, "Chức năng xóa sản phẩm đang được phát triển!");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        modelProductAdd.setLocationRelativeTo(this);
        modelProductAdd.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    public String getImageFileName(ImageIcon imageIcon) {
        if (imageIcon != null && imageIcon.getDescription() != null) {
            File imageFile = new File(imageIcon.getDescription());
            return imageFile.getName();
        }
        return null;
    }

    private void btnAddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImageActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn hình ảnh");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPG Images", "jpg"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            String imagePath = fileToOpen.getAbsolutePath();
            imageProductAdd = new ImageIcon(imagePath);
            imageProductAdd.setDescription(imagePath);
            Image imagePro = imageProductAdd.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon img = new ImageIcon(imagePro);
            lblImage.setIcon(img);
        }

    }//GEN-LAST:event_btnAddImageActionPerformed

    private void txtProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameActionPerformed

    private void txtProductManufacturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductManufacturerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductManufacturerActionPerformed

    private void txtCountryOfOriginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCountryOfOriginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCountryOfOriginActionPerformed

    private void txtProductRegisNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductRegisNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductRegisNumberActionPerformed

    private void txtProductPurchasePriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductPurchasePriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductPurchasePriceActionPerformed

    private void txtProductActiveIngreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductActiveIngreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductActiveIngreActionPerformed

    private void txtProductSellingPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductSellingPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductSellingPriceActionPerformed

    private void txtProductDosageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDosageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductDosageActionPerformed

    private void txtProductPakagingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductPakagingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductPakagingActionPerformed

    private String removeExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            return filename;
        }
        return filename.substring(0, dotIndex);
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            // Không có đuôi hoặc đuôi trống
            return "";
        }
        return filename.substring(dotIndex + 1); // Trả về đuôi tệp
    }

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed

    }//GEN-LAST:event_btnAddProductActionPerformed

    private void modelProductAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modelProductAddMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_modelProductAddMouseClicked

    private void cbbProductTypeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbProductTypeAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbProductTypeAddActionPerformed

    private void btnExitProductADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitProductADDActionPerformed

    }//GEN-LAST:event_btnExitProductADDActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed

    }//GEN-LAST:event_btnEditActionPerformed

    private void optionPdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionPdTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_optionPdTypeActionPerformed

    private void optionPdStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionPdStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_optionPdStatusActionPerformed

    private void tabEditStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabEditStateChanged

    }//GEN-LAST:event_tabEditStateChanged

    private void cbbProductTypeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbProductTypeEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbProductTypeEditActionPerformed

    private void btnEditProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProductActionPerformed

    }//GEN-LAST:event_btnEditProductActionPerformed

    private void btnExitProductADD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitProductADD1ActionPerformed

    }//GEN-LAST:event_btnExitProductADD1ActionPerformed

    private void txtProductPakaging1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductPakaging1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductPakaging1ActionPerformed

    private void txtProductDosage1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDosage1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductDosage1ActionPerformed

    private void txtProductSellingPrice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductSellingPrice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductSellingPrice1ActionPerformed

    private void txtProductActiveIngre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductActiveIngre1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductActiveIngre1ActionPerformed

    private void txtProductPurchasePrice1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductPurchasePrice1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductPurchasePrice1ActionPerformed

    private void txtCountryOfOrigin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCountryOfOrigin1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCountryOfOrigin1ActionPerformed

    private void txtProductManufacturer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductManufacturer1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductManufacturer1ActionPerformed

    private void btnAddImage1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImage1ActionPerformed
        // TODO add your handling code here:        JFileChooser fileChooser = new JFileChooser();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn hình ảnh");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPG Images", "jpg"));

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            String imagePath = fileToOpen.getAbsolutePath();
            imageProductEdit = new ImageIcon(imagePath);
            imageProductEdit.setDescription(imagePath);
            Image imagePro = imageProductEdit.getImage().getScaledInstance(265, 265, Image.SCALE_SMOOTH);
            ImageIcon img = new ImageIcon(imagePro);
            lblImageEdit.setIcon(img);
        }
    }//GEN-LAST:event_btnAddImage1ActionPerformed

    //private String unitIdEdit;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ProductScrollPane;
    private javax.swing.JScrollPane ScrollPaneTab3;
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddImage;
    private javax.swing.JButton btnAddImage1;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnEditProduct;
    private javax.swing.JButton btnExitProductADD;
    private javax.swing.JButton btnExitProductADD1;
    private javax.swing.JButton btnOpenModalAddSup;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JComboBox<String> cbbProductTypeAdd;
    private javax.swing.JComboBox<String> cbbProductTypeEdit;
    private javax.swing.JComboBox<String> comboUnitAdd;
    private javax.swing.JComboBox<String> comboUnitEdit;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblImageEdit;
    private javax.swing.JDialog modelEditProduct;
    private javax.swing.JDialog modelProductAdd;
    private javax.swing.JComboBox<String> optionPdStatus;
    private javax.swing.JComboBox<String> optionPdType;
    private javax.swing.JPanel pnAll;
    private javax.swing.JTabbedPane tabEdit;
    private javax.swing.JTextField txtCountryOfOrigin;
    private javax.swing.JTextField txtCountryOfOrigin1;
    private javax.swing.JTextField txtProductActiveIngre;
    private javax.swing.JTextField txtProductActiveIngre1;
    private javax.swing.JTextField txtProductDosage;
    private javax.swing.JTextField txtProductDosage1;
    private javax.swing.JTextField txtProductManufacturer;
    private javax.swing.JTextField txtProductManufacturer1;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductName1;
    private javax.swing.JTextField txtProductPakaging;
    private javax.swing.JTextField txtProductPakaging1;
    private javax.swing.JTextField txtProductPurchasePrice;
    private javax.swing.JTextField txtProductPurchasePrice1;
    private javax.swing.JTextField txtProductRegisNumber;
    private javax.swing.JTextField txtProductRegisNumber1;
    private javax.swing.JTextField txtProductSellingPrice;
    private javax.swing.JTextField txtProductSellingPrice1;
    private javax.swing.JTextField txtSearchNamePD;
    // End of variables declaration//GEN-END:variables

}

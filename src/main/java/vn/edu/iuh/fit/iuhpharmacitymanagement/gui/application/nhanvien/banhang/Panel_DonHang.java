/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.KeyEvent;
import java.util.Optional;
import javax.swing.JLabel;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietKhuyenMaiSanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhachHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhuyenMaiBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.KhachHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import java.util.Map;

/**
 *
 * @author PhamTra
 */
public class Panel_DonHang extends javax.swing.JPanel {

    private KhachHangBUS khachHangBUS;
    private KhachHang khachHangHienTai;
    private KhuyenMaiBUS khuyenMaiBUS;
    private ChiTietKhuyenMaiSanPhamBUS chiTietKhuyenMaiSanPhamBUS;
    private KhuyenMai khuyenMaiDaChon;
    private JLabel lblTenKhuyenMaiHoaDon;
    private JLabel lblTenKhuyenMaiSanPham;
    private JLabel lblThongTinKhuyenMai; // Label hiển thị thông tin khuyến mãi đang áp dụng

    /**
     * Creates new form TabHoaDon
     */
    public Panel_DonHang() {
        // Khởi tạo BUS
        khachHangBUS = new KhachHangBUS(new KhachHangDAO(), new DonHangDAO());
        khuyenMaiBUS = new KhuyenMaiBUS();
        chiTietKhuyenMaiSanPhamBUS = new ChiTietKhuyenMaiSanPhamBUS(new ChiTietKhuyenMaiSanPhamDAO());
        
        initComponents();
        customizeTextFields();
        addPromotionLabels();
        addPriceSuggestButtons();
    }

    private void customizeTextFields() {
        // Placeholder text
        txtTienKhachDua.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "0 đ");
        txtTimKhachHang.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại khách hàng");
        txtTienKhachDua.setEditable(true);
        txtTienKhachDua.setEnabled(true);

        // Style cho tất cả text fields - thêm viền rõ ràng
        String textFieldStyle = "arc:8;borderWidth:1;borderColor:#CCCCCC";

        txtTimKhachHang.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtTenKhachHang.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtTongTienHang.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtDiscountProduct.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtDiscountOrder.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtTongHoaDon.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtTienKhachDua.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);
        txtTienTraKhach.putClientProperty(FlatClientProperties.STYLE, textFieldStyle);

        // Làm nổi bật các trường quan trọng
        txtTongHoaDon.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;borderWidth:2;borderColor:#519AF4;background:#F0F8FF");
        txtTienKhachDua.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;borderWidth:1;borderColor:#73A547;background:#FFFFFF");
    }

    private void clearOrderDetails() {
//        List<PnOrderDetail> orderDetails = getAllPnOrderDetailThuoc();
//        for (PnOrderDetail orderDetail : orderDetails) {
//            pnContent.remove(orderDetail);
//        }
//        pnContent.revalidate();
//        pnContent.repaint();
//        orderDetails.clear();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnMid = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnContent = new javax.swing.JPanel();
        pnLeft = new javax.swing.JPanel();
        txtTimKhachHang = new javax.swing.JTextField();
        btnBanHang = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDiscountOrder = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTongTienHang = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTongHoaDon = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtTienTraKhach = new javax.swing.JTextField();
        pnPriceSuggest = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtDiscountProduct = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        setLayout(new java.awt.BorderLayout());

        pnMid.setMinimumSize(new java.awt.Dimension(200, 200));
        pnMid.setOpaque(false);

        pnContent.setBackground(new java.awt.Color(255, 255, 255));
        pnContent.setLayout(new javax.swing.BoxLayout(pnContent, javax.swing.BoxLayout.Y_AXIS));

        pnLeft.setBackground(new java.awt.Color(255, 255, 255));

        txtTimKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtTimKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKhachHangActionPerformed(evt);
            }
        });

        btnBanHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBanHang.setText("Bán Hàng");
        btnBanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanHangActionPerformed(evt);
            }
        });
        btnBanHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBanHangKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
        jLabel1.setText("Tổng giảm giá hóa đơn:");
        jLabel1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtDiscountOrder.setEditable(false);
        txtDiscountOrder.setFont(txtDiscountOrder.getFont().deriveFont(txtDiscountOrder.getFont().getSize()+3f));
        txtDiscountOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountOrder.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDiscountOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txtDiscountOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() | java.awt.Font.BOLD, jLabel3.getFont().getSize()+2));
        jLabel3.setText("Tổng tiền hàng:");

        txtTongTienHang.setEditable(false);
        txtTongTienHang.setFont(txtTongTienHang.getFont().deriveFont(txtTongTienHang.getFont().getSize()+3f));
        txtTongTienHang.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongTienHang.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTongTienHang, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTongTienHang, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, jLabel7.getFont().getSize()+2));
        jLabel7.setText("Tiền khách đưa:");

        txtTienKhachDua.setFont(txtTienKhachDua.getFont().deriveFont(txtTienKhachDua.getFont().getSize()+3f));
        txtTienKhachDua.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTienKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTienKhachDuaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateTienTraKhach();
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addComponent(txtTienKhachDua, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+2));
        jLabel9.setText("Tổng hóa đơn:");

        txtTongHoaDon.setEditable(false);
        txtTongHoaDon.setFont(txtTongHoaDon.getFont().deriveFont(txtTongHoaDon.getFont().getSize()+3f));
        txtTongHoaDon.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongHoaDon.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTongHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txtTongHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD, jLabel11.getFont().getSize()+2));
        jLabel11.setText("Tiền trả khách:");

        txtTienTraKhach.setEditable(false);
        txtTienTraKhach.setFont(txtTienTraKhach.getFont().deriveFont(txtTienTraKhach.getFont().getSize()+3f));
        txtTienTraKhach.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTienTraKhach.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTienTraKhach, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txtTienTraKhach, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pnPriceSuggest.setBackground(new java.awt.Color(255, 255, 255));
        pnPriceSuggest.setMaximumSize(new java.awt.Dimension(398, 10));
        pnPriceSuggest.setMinimumSize(new java.awt.Dimension(398, 10));
        pnPriceSuggest.setPreferredSize(new java.awt.Dimension(398, 10));
        pnPriceSuggest.setLayout(new java.awt.GridLayout(2, 3, 10, 6));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(jLabel14.getFont().deriveFont(jLabel14.getFont().getStyle() | java.awt.Font.BOLD, jLabel14.getFont().getSize()+2));
        jLabel14.setText("Tổng giảm giá sản phẩm:");

        txtDiscountProduct.setEditable(false);
        txtDiscountProduct.setFont(txtDiscountProduct.getFont().deriveFont(txtDiscountProduct.getFont().getSize()+3f));
        txtDiscountProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountProduct.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDiscountProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(txtDiscountProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD, jLabel2.getFont().getSize()+2));
        jLabel2.setText("Tên khách hàng:");
        jLabel2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel2AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setFont(txtTenKhachHang.getFont().deriveFont(txtTenKhachHang.getFont().getSize()+3f));
        txtTenKhachHang.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTenKhachHang.setText("Vãng lai");
        txtTenKhachHang.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(txtTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        javax.swing.GroupLayout pnLeftLayout = new javax.swing.GroupLayout(pnLeft);
        pnLeft.setLayout(pnLeftLayout);
        pnLeftLayout.setHorizontalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTimKhachHang)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnPriceSuggest, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnLeftLayout.setVerticalGroup(
            pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnLeftLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(txtTimKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnPriceSuggest, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        pnPriceSuggest.getAccessibleContext().setAccessibleName("");

        pnContent.add(pnLeft);

        jScrollPane1.setViewportView(pnContent);

        javax.swing.GroupLayout pnMidLayout = new javax.swing.GroupLayout(pnMid);
        pnMid.setLayout(pnMidLayout);
        pnMidLayout.setHorizontalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        pnMidLayout.setVerticalGroup(
            pnMidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
        );

        add(pnMid, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKhachHangActionPerformed
        timKhachHang();
    }//GEN-LAST:event_txtTimKhachHangActionPerformed
    
    /**
     * Tìm khách hàng theo số điện thoại
     */
    private void timKhachHang() {
        String soDienThoai = txtTimKhachHang.getText().trim();
        
        // Kiểm tra input rỗng
        if (soDienThoai.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Vui lòng nhập số điện thoại khách hàng");
            txtTimKhachHang.requestFocus();
            return;
        }
        
        // Kiểm tra định dạng số điện thoại (10 số, bắt đầu bằng 0)
        if (!soDienThoai.matches("^0[0-9]{9}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và gồm 10 chữ số");
            txtTimKhachHang.selectAll();
            txtTimKhachHang.requestFocus();
            return;
        }
        
        // Tìm khách hàng theo số điện thoại
        Optional<KhachHang> khachHangOpt = khachHangBUS.timKhachHangTheoSoDienThoai(soDienThoai);
        
        if (khachHangOpt.isPresent()) {
            khachHangHienTai = khachHangOpt.get();
            
            // Hiển thị thông tin khách hàng
            txtTenKhachHang.setText(khachHangHienTai.getTenKhachHang());
            
            // Thông báo thành công
            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 
                "Đã tìm thấy khách hàng: " + khachHangHienTai.getTenKhachHang());
            
            // Xóa text field tìm kiếm
            txtTimKhachHang.setText("");
        } else {
            // Reset về khách vãng lai
            khachHangHienTai = null;
            txtTenKhachHang.setText("Vãng lai");
            
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, 
                "Không tìm thấy khách hàng với SĐT: " + soDienThoai + ". Khách hàng vãng lai");
            
            txtTimKhachHang.selectAll();
            txtTimKhachHang.requestFocus();
        }
    }
    
    /**
     * Lấy khách hàng hiện tại
     */
    public KhachHang getKhachHangHienTai() {
        return khachHangHienTai;
    }

    private void createOrder() {
//        List<OrderDTO> orderDTOs = createListOrderDetail();
//        try {
//            if (orderBUS.createOrder(CurrentEmployee.getEmployee(), customer, promotionOrder, orderDTOs)) {
//                lapHoaDonForm.removeAndAddNewTab(this);
//                MessageDialog.info(null, "Lập hóa đơn thành công");
//            } else {
//                MessageDialog.info(null, "Lập hóa đơn thất bại");
//            }
//        } catch (Exception e) {
//            MessageDialog.error(null, e.getMessage());
//        }
    }

    private void btnBanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanHangActionPerformed
        createOrder();

    }//GEN-LAST:event_btnBanHangActionPerformed

    private void jLabel1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1AncestorAdded

    private void jLabel2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel2AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2AncestorAdded

    private void txtTienKhachDuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienKhachDuaKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTienKhachDuaKeyPressed

    private void btnBanHangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBanHangKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F8) {
            createOrder();
        }
    }//GEN-LAST:event_btnBanHangKeyPressed

    private double tongTienHang;
    private double discountProduct;
    private double discountOrder;
    
    /**
     * Cập nhật tổng tiền hàng
     */
    public void updateTongTienHang(double tongTien) {
        this.tongTienHang = tongTien;
        txtTongTienHang.setText(String.format("%,.0f đ", tongTien));
        
        // Cập nhật label hiển thị chi tiết (nếu có khuyến mãi)
        if (khuyenMaiDaChon != null) {
            String htmlText = String.format("<html>Tổng tiền hàng:<br/><small style='color:#009600;'>(%s áp dụng)</small></html>", 
                khuyenMaiDaChon.getTenKhuyenMai());
            jLabel3.setText(htmlText);
        } else {
            jLabel3.setText("Tổng tiền hàng:");
        }
        
        updateTongHoaDon();
    }
    
    /**
     * Cập nhật tổng giảm giá sản phẩm
     */
    public void updateDiscountProduct(double discount) {
        this.discountProduct = discount;
        txtDiscountProduct.setText(String.format("%,.0f đ", discount));
        updateTongHoaDon();
    }
    
    /**
     * Cập nhật tổng giảm giá hóa đơn
     */
    public void updateDiscountOrder(double discount) {
        this.discountOrder = discount;
        txtDiscountOrder.setText(String.format("%,.0f đ", discount));
        updateTongHoaDon();
    }
    
    /**
     * Cập nhật tổng hóa đơn (tự động tính)
     */
    private void updateTongHoaDon() {
        double tongHoaDon = tongTienHang - discountProduct - discountOrder;
        txtTongHoaDon.setText(String.format("%,.0f đ", tongHoaDon));
        updateTienTraKhach();
        
        // Cập nhật các nút tiền mệnh giá gợi ý dựa trên tổng hóa đơn mới
        addPriceSuggestButtons();
    }
    
    /**
     * Cập nhật tiền trả khách khi khách đưa tiền
     */
    private void updateTienTraKhach() {
        try {
            String tienKhachDuaStr = txtTienKhachDua.getText().replaceAll("[^0-9]", "");
            if (!tienKhachDuaStr.isEmpty()) {
                double tienKhachDua = Double.parseDouble(tienKhachDuaStr);
                double tongHoaDon = tongTienHang - discountProduct - discountOrder;
                double tienTraKhach = tienKhachDua - tongHoaDon;
                
                if (tienTraKhach >= 0) {
                    txtTienTraKhach.setText(String.format("%,.0f đ", tienTraKhach));
                } else {
                    txtTienTraKhach.setText("0 đ");
                }
            } else {
                txtTienTraKhach.setText("0 đ");
            }
        } catch (NumberFormatException e) {
            txtTienTraKhach.setText("0 đ");
        }
    }
    
    /**
     * Lấy tổng tiền hàng
     */
    public double getTongTienHang() {
        return tongTienHang;
    }
    
    /**
     * Lấy tổng giảm giá sản phẩm
     */
    public double getDiscountProduct() {
        return discountProduct;
    }
    
    /**
     * Lấy tổng giảm giá hóa đơn
     */
    public double getDiscountOrder() {
        return discountOrder;
    }
    
    /**
     * Lấy tổng hóa đơn
     */
    public double getTongHoaDon() {
        return tongTienHang - discountProduct - discountOrder;
    }
    
    /**
     * Lấy tiền khách đưa
     */
    public double getTienKhachDua() {
        try {
            String tienKhachDuaStr = txtTienKhachDua.getText().replaceAll("[^0-9]", "");
            return tienKhachDuaStr.isEmpty() ? 0 : Double.parseDouble(tienKhachDuaStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Lấy tiền trả khách
     */
    public double getTienTraKhach() {
        try {
            String tienTraKhachStr = txtTienTraKhach.getText().replaceAll("[^0-9]", "");
            return tienTraKhachStr.isEmpty() ? 0 : Double.parseDouble(tienTraKhachStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    
    /**
     * Thêm label hiển thị tên khuyến mãi vào UI
     * Sẽ được gọi sau khi initComponents() hoàn tất
     */
    private void addPromotionLabels() {
        // Label cho khuyến mãi hóa đơn (nằm dưới giảm giá đơn hàng)
        lblTenKhuyenMaiHoaDon = new JLabel();
        lblTenKhuyenMaiHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 10));
        lblTenKhuyenMaiHoaDon.setForeground(new java.awt.Color(0, 150, 0));
        lblTenKhuyenMaiHoaDon.setText("");
        
        // Label cho khuyến mãi sản phẩm (nằm dưới giảm giá sản phẩm)
        lblTenKhuyenMaiSanPham = new JLabel();
        lblTenKhuyenMaiSanPham.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 10));
        lblTenKhuyenMaiSanPham.setForeground(new java.awt.Color(0, 150, 0));
        lblTenKhuyenMaiSanPham.setText("");
        
        // Label hiển thị thông tin khuyến mãi đang áp dụng
        lblThongTinKhuyenMai = new JLabel();
        lblThongTinKhuyenMai.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC | java.awt.Font.BOLD, 10));
        lblThongTinKhuyenMai.setForeground(new java.awt.Color(0, 120, 215));
        lblThongTinKhuyenMai.setText("");
    }
    
    /**
     * Lấy khuyến mãi đã chọn
     */
    public KhuyenMai getKhuyenMaiDaChon() {
        return khuyenMaiDaChon;
    }
    
    /**
     * Lấy ChiTietKhuyenMaiSanPhamBUS
     */
    public ChiTietKhuyenMaiSanPhamBUS getChiTietKhuyenMaiSanPhamBUS() {
        return chiTietKhuyenMaiSanPhamBUS;
    }
    
    /**
     * Tự động kiểm tra và áp dụng khuyến mãi tốt nhất dựa trên giỏ hàng
     * @param tongTienHang Tổng tiền hàng hiện tại
     * @param danhSachSanPham Map sản phẩm và số lượng trong giỏ
     */
    public void tuDongApDungKhuyenMai(double tongTienHang, Map<SanPham, Integer> danhSachSanPham) {
        // Tìm khuyến mãi tốt nhất
        KhuyenMai khuyenMaiTotNhat = khuyenMaiBUS.timKhuyenMaiTotNhat(tongTienHang, danhSachSanPham);
        
        // Nếu không có sản phẩm trong giỏ hoặc không có khuyến mãi nào phù hợp
        if (tongTienHang == 0 || khuyenMaiTotNhat == null) {
            // Reset về không áp dụng khuyến mãi
            if (khuyenMaiDaChon != null) {
                khuyenMaiDaChon = null;
                
                // Xóa thông tin khuyến mãi trên label
                if (lblThongTinKhuyenMai != null) {
                    lblThongTinKhuyenMai.setText("");
                }
            }
            return;
        }
        
        // Nếu khuyến mãi tốt nhất khác với khuyến mãi đang chọn
        if (khuyenMaiDaChon == null || !khuyenMaiDaChon.getMaKhuyenMai().equals(khuyenMaiTotNhat.getMaKhuyenMai())) {
            khuyenMaiDaChon = khuyenMaiTotNhat;
            String tenKM = khuyenMaiTotNhat.getTenKhuyenMai();
            
            // Hiển thị thông báo
            String loaiKM = khuyenMaiTotNhat.getLoaiKhuyenMai() == LoaiKhuyenMai.DON_HANG 
                ? "giảm " + khuyenMaiTotNhat.getGiamGia() + "% trên tổng hóa đơn"
                : "giảm " + khuyenMaiTotNhat.getGiamGia() + "% cho các sản phẩm áp dụng";
            
            // Cập nhật label hiển thị thông tin khuyến mãi
            if (lblThongTinKhuyenMai != null) {
                lblThongTinKhuyenMai.setText("🎉 Đang áp dụng: " + tenKM);
            }
            
            Notifications.getInstance().show(
                Notifications.Type.SUCCESS, 
                Notifications.Location.TOP_CENTER,
                "✨ Tự động áp dụng: " + tenKM + " (" + loaiKM + ")"
            );
            
            // Notify để cập nhật lại tổng tiền
            firePropertyChange("khuyenMaiChanged", null, khuyenMaiTotNhat);
        }
    }
    
    /**
     * Lấy KhuyenMaiBUS
     */
    public KhuyenMaiBUS getKhuyenMaiBUS() {
        return khuyenMaiBUS;
    }
    
    /**
     * Thêm các nút tiền mệnh giá gợi ý vào panel (CHỈ HIỂN THỊ KHI CÓ SẢN PHẨM)
     * Các nút này giúp người bán nhanh chóng chọn số tiền khách đưa
     * 
     * LOGIC DỰ ĐOÁN THÔNG MINH - PHÙ HỢP VỚI MỨC GIÁ:
     * - Ví dụ hóa đơn 1.500đ → hiển thị: 2k, 3k, 5k, 10k, 20k, 50k
     * - Ví dụ hóa đơn 44.000đ → hiển thị: 44k, 45k, 50k, 100k, 200k, 500k
     * - Ví dụ hóa đơn 567.000đ → hiển thị: 567k, 570k, 600k, 1tr, 2tr, 5tr
     * - Chưa có sản phẩm (0đ) → ẨN HẾT (không hiển thị nút nào)
     */
    private void addPriceSuggestButtons() {
        // Xóa tất cả components cũ trong panel (nếu có)
        pnPriceSuggest.removeAll();
        
        // Lấy tổng hóa đơn hiện tại
        double tongHoaDon = parseCurrency(txtTongHoaDon.getText());
        
        if (tongHoaDon <= 0) {
            // ẨN HẾT CÁC NÚT KHI CHƯA CÓ SẢN PHẨM
            // Không làm gì cả, để panel trống
        } else {
            // TÍNH TOÁN 6 NÚT DỰ ĐOÁN THÔNG MINH (CHỈ KHI CÓ SẢN PHẨM)
            java.util.List<Long> suggestions = new java.util.ArrayList<>();
            long tongHoaDonRound = (long) Math.ceil(tongHoaDon);
            
            // NÚT 1: Đúng bằng tổng hóa đơn (làm tròn lên nghìn)
            long exact = (long) (Math.ceil(tongHoaDonRound / 1000.0) * 1000);
            suggestions.add(exact);
            
            // NÚT 2-3: Các mức tiền gần kề (+1k, +2k, +5k tùy mức giá)
            if (exact < 10000) {
                // Dưới 10k: +1k, +2k
                addIfNotExists(suggestions, exact + 1000);
                addIfNotExists(suggestions, exact + 2000);
            } else if (exact < 50000) {
                // 10k-50k: +1k, +5k
                addIfNotExists(suggestions, exact + 1000);
                addIfNotExists(suggestions, exact + 5000);
            } else if (exact < 100000) {
                // 50k-100k: +5k, +10k
                addIfNotExists(suggestions, exact + 5000);
                addIfNotExists(suggestions, exact + 10000);
            } else {
                // Trên 100k: +10k, +20k
                addIfNotExists(suggestions, exact + 10000);
                addIfNotExists(suggestions, exact + 20000);
            }
            
            // NÚT 4-6: Các mệnh giá tiêu chuẩn PHÙ HỢP với mức giá
            long[] allDenominations = {
                2000, 5000, 10000, 20000, 50000, 
                100000, 200000, 500000, 
                1000000, 2000000, 5000000, 10000000
            };
            
            for (long denom : allDenominations) {
                if (suggestions.size() >= 6) break;
                if (denom > exact && !suggestions.contains(denom)) {
                    suggestions.add(denom);
                }
            }
            
            // Đảm bảo có đủ 6 nút (nếu thiếu thì thêm mệnh giá gấp đôi)
            if (suggestions.size() < 6) {
                long lastAmount = suggestions.get(suggestions.size() - 1);
                while (suggestions.size() < 6) {
                    lastAmount *= 2;
                    if (!suggestions.contains(lastAmount)) {
                        suggestions.add(lastAmount);
                    }
                }
            }
            
            // Chỉ lấy 6 nút đầu tiên
            for (int i = 0; i < Math.min(6, suggestions.size()); i++) {
                addPriceSuggestButton(suggestions.get(i));
            }
        }
        
        pnPriceSuggest.revalidate();
        pnPriceSuggest.repaint();
    }
    
    /**
     * Thêm giá trị vào list nếu chưa tồn tại
     */
    private void addIfNotExists(java.util.List<Long> list, long value) {
        if (!list.contains(value)) {
            list.add(value);
        }
    }
    
    /**
     * Thêm một nút tiền mệnh giá vào panel
     * 
     * @param amount số tiền hiển thị trên nút
     */
    private void addPriceSuggestButton(long amount) {
        javax.swing.JButton btn = new javax.swing.JButton();
        
        // Format số tiền: 10.000, 20.000, ...
        String giaText = String.format("%,d đ", amount).replace(",", ".");
        btn.setText(giaText);
        
        // Style cho nút
        btn.setFont(btn.getFont().deriveFont(java.awt.Font.BOLD, 13f));
        btn.putClientProperty(FlatClientProperties.STYLE, 
            "arc:8;borderWidth:1;borderColor:#519AF4;background:#F0F8FF");
        
        // Thêm action listener
        btn.addActionListener(e -> {
            // Lấy tổng hóa đơn hiện tại
            double tongHoaDon = parseCurrency(txtTongHoaDon.getText());
            
            if (tongHoaDon <= 0) {
                Notifications.getInstance().show(
                    Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Chưa có sản phẩm trong giỏ hàng"
                );
                return;
            }
            
            // Set số tiền khách đưa
            txtTienKhachDua.setText(String.format("%,d", amount).replace(",", "."));
            
            // Cập nhật tiền trả khách
            updateTienTraKhach();
        });
        
        pnPriceSuggest.add(btn);
    }
    
    /**
     * Chuyển đổi chuỗi tiền tệ sang số double
     * Ví dụ: "1.000.000 đ" -> 1000000.0
     * 
     * @param currencyText chuỗi tiền tệ (có thể chứa dấu chấm phân cách nghìn và ký tự "đ")
     * @return giá trị số tương ứng
     */
    private double parseCurrency(String currencyText) {
        if (currencyText == null || currencyText.trim().isEmpty()) {
            return 0;
        }
        
        try {
            // Loại bỏ dấu chấm phân cách nghìn, ký tự "đ" và khoảng trắng
            String cleaned = currencyText.replace(".", "")
                                        .replace("đ", "")
                                        .replace(",", "")
                                        .trim();
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Reset tất cả thông tin thanh toán về trạng thái ban đầu
     * Được gọi khi giỏ hàng rỗng (xóa hết sản phẩm)
     */
    public void resetThanhToan() {
        // Reset các biến
        tongTienHang = 0;
        discountProduct = 0;
        discountOrder = 0;
        khuyenMaiDaChon = null;
        
        // Reset tất cả các text field về 0
        txtTongTienHang.setText("0 đ");
        txtDiscountProduct.setText("0 đ");
        txtDiscountOrder.setText("0 đ");
        txtTongHoaDon.setText("0 đ");
        txtTienKhachDua.setText("");
        txtTienTraKhach.setText("0 đ");
        
        // Reset các nút tiền mệnh giá về mặc định
        addPriceSuggestButtons();
        
        // Reset thông tin khách hàng về vãng lai
        khachHangHienTai = null;
        txtTenKhachHang.setText("Vãng lai");
        txtTimKhachHang.setText("");
        
        // Xóa thông tin khuyến mãi
        if (lblThongTinKhuyenMai != null) {
            lblThongTinKhuyenMai.setText("");
        }
        if (lblTenKhuyenMaiHoaDon != null) {
            lblTenKhuyenMaiHoaDon.setText("");
        }
        if (lblTenKhuyenMaiSanPham != null) {
            lblTenKhuyenMaiSanPham.setText("");
        }
        
        // Reset label tổng tiền hàng về text mặc định
        jLabel3.setText("Tổng tiền hàng:");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBanHang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnLeft;
    private javax.swing.JPanel pnMid;
    private javax.swing.JPanel pnPriceSuggest;
    private javax.swing.JTextField txtDiscountOrder;
    private javax.swing.JTextField txtDiscountProduct;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienTraKhach;
    private javax.swing.JTextField txtTimKhachHang;
    private javax.swing.JTextField txtTongHoaDon;
    private javax.swing.JTextField txtTongTienHang;
    // End of variables declaration//GEN-END:variables
}

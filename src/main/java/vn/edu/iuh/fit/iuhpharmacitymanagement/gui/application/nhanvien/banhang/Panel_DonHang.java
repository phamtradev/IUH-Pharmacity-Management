/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.banhang;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import raven.toast.Notifications;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietKhuyenMaiSanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhachHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhuyenMaiBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.NhanVienBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.PhuongThucThanhToan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.KhachHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.UserSession;
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
    private KhuyenMai khuyenMaiDaChon; // @deprecated - giữ lại để tương thích
    private Map<LoaiKhuyenMai, KhuyenMai> danhSachKhuyenMaiDaChon; // Map lưu các khuyến mãi đang áp dụng
    private JLabel lblTenKhuyenMaiHoaDon;
    private DonHangBUS donHangBUS;
    private ChiTietDonHangBUS chiTietDonHangBUS;
    private NhanVienBUS nhanVienBUS;
    private LoHangBUS loHangBUS;
    private GD_BanHang gdBanHang; // Reference đến form cha để lấy container panel

    /**
     * Creates new form TabHoaDon
     */
    public Panel_DonHang() {
        // Khởi tạo BUS
        khachHangBUS = new KhachHangBUS(new KhachHangDAO(), new DonHangDAO());
        khuyenMaiBUS = new KhuyenMaiBUS();
        chiTietKhuyenMaiSanPhamBUS = new ChiTietKhuyenMaiSanPhamBUS(new ChiTietKhuyenMaiSanPhamDAO());
        donHangBUS = new DonHangBUS();
        chiTietDonHangBUS = new ChiTietDonHangBUS();
        nhanVienBUS = new NhanVienBUS();
        loHangBUS = new LoHangBUS();
        danhSachKhuyenMaiDaChon = new java.util.HashMap<>(); // Khởi tạo Map
        
        initComponents();
        customizeTextFields();
        addPromotionLabels();
        addPriceSuggestButtons();
    }
    
    /**
     * Set reference đến form cha (GD_BanHang)
     */
    public void setGdBanHang(GD_BanHang gdBanHang) {
        this.gdBanHang = gdBanHang;
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
        btnThanhToanQR = new javax.swing.JButton();
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

        btnThanhToanQR.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThanhToanQR.setText("💳 QR Banking");
        btnThanhToanQR.setVisible(false); // Ẩn mặc định, chỉ hiện khi có sản phẩm
        btnThanhToanQR.setFocusPainted(false);
        btnThanhToanQR.putClientProperty(FlatClientProperties.STYLE, 
            "arc:8;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:#0066CC;foreground:#FFFFFF");
        btnThanhToanQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanQRActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

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
        
        // Panel chứa label và textfield
        javax.swing.JPanel pnDiscountOrderTop = new javax.swing.JPanel();
        javax.swing.GroupLayout pnDiscountOrderTopLayout = new javax.swing.GroupLayout(pnDiscountOrderTop);
        pnDiscountOrderTop.setLayout(pnDiscountOrderTopLayout);
        pnDiscountOrderTop.setBackground(new java.awt.Color(255, 255, 255));
        pnDiscountOrderTopLayout.setHorizontalGroup(
            pnDiscountOrderTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDiscountOrderTopLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDiscountOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnDiscountOrderTopLayout.setVerticalGroup(
            pnDiscountOrderTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDiscountOrderTopLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnDiscountOrderTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(txtDiscountOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        
        jPanel1.add(pnDiscountOrderTop);

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
        jPanel8.setLayout(new BoxLayout(jPanel8, BoxLayout.Y_AXIS));

        jLabel14.setFont(jLabel14.getFont().deriveFont(jLabel14.getFont().getStyle() | java.awt.Font.BOLD, jLabel14.getFont().getSize()+2));
        jLabel14.setText("Tổng giảm giá sản phẩm:");

        txtDiscountProduct.setEditable(false);
        txtDiscountProduct.setFont(txtDiscountProduct.getFont().deriveFont(txtDiscountProduct.getFont().getSize()+3f));
        txtDiscountProduct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiscountProduct.setRequestFocusEnabled(false);
        
        // Panel chứa label và textfield
        javax.swing.JPanel pnDiscountProductTop = new javax.swing.JPanel();
        javax.swing.GroupLayout pnDiscountProductTopLayout = new javax.swing.GroupLayout(pnDiscountProductTop);
        pnDiscountProductTop.setLayout(pnDiscountProductTopLayout);
        pnDiscountProductTop.setBackground(new java.awt.Color(255, 255, 255));
        pnDiscountProductTopLayout.setHorizontalGroup(
            pnDiscountProductTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDiscountProductTopLayout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtDiscountProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnDiscountProductTopLayout.setVerticalGroup(
            pnDiscountProductTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnDiscountProductTopLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnDiscountProductTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(txtDiscountProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        
        jPanel8.add(pnDiscountProductTop);

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
                    .addGroup(pnLeftLayout.createSequentialGroup()
                        .addComponent(btnThanhToanQR, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGroup(pnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThanhToanQR, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
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
     * Tìm đơn hàng từ GD_BanHang (được gọi từ bên ngoài)
     * @param maDonHang mã đơn hàng cần tìm
     */
    public void timDonHangTuGDBanHang(String maDonHang) {
        // Kiểm tra input rỗng
        if (maDonHang == null || maDonHang.trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Vui lòng nhập mã đơn hàng hoặc quét mã");
            return;
        }
        
        maDonHang = maDonHang.trim();
        
        // Tìm đơn hàng theo mã
        List<DonHang> ketQuaTimKiem = donHangBUS.timKiemTheoMa(maDonHang);
        Optional<DonHang> donHangOpt = ketQuaTimKiem.isEmpty() ? Optional.empty() : Optional.of(ketQuaTimKiem.get(0));
        
        if (!donHangOpt.isPresent()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Không tìm thấy đơn hàng với mã: " + maDonHang);
            return;
        }
        
        DonHang donHang = donHangOpt.get();
        
        // Load thông tin khách hàng từ đơn hàng
        khachHangHienTai = donHang.getKhachHang();
        if (khachHangHienTai != null) {
            txtTenKhachHang.setText(khachHangHienTai.getTenKhachHang());
            txtTimKhachHang.setText("");
        } else {
            txtTenKhachHang.setText("Vãng lai");
        }
        
        // Lấy danh sách sản phẩm từ đơn hàng
        List<ChiTietDonHang> danhSachChiTiet = chiTietDonHangBUS.layDanhSachChiTietTheoMaDonHang(maDonHang);
        
        if (danhSachChiTiet.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, 
                "Đơn hàng không có sản phẩm");
            return;
        }
        
        // Hiển thị sản phẩm vào danh sách bán hàng
        hienThiSanPhamTuDonHang(danhSachChiTiet);
        
        // Thông báo thành công
        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 
            "Đã tìm thấy đơn hàng " + maDonHang + " với " + danhSachChiTiet.size() + " sản phẩm");
    }
    
    /**
     * Hiển thị danh sách sản phẩm từ đơn hàng vào giỏ hàng
     */
    private void hienThiSanPhamTuDonHang(List<ChiTietDonHang> danhSachChiTiet) {
        if (gdBanHang == null) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 
                "Lỗi hệ thống: Không thể truy cập giỏ hàng!");
            return;
        }
        
        // Duyệt qua từng sản phẩm và thêm vào giỏ hàng
        for (ChiTietDonHang chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            if (loHang == null || loHang.getSanPham() == null) {
                continue;
            }
            
            SanPham sanPham = loHang.getSanPham();
            int soLuong = chiTiet.getSoLuong();
            
            // Thêm sản phẩm vào giỏ hàng thông qua GD_BanHang
            gdBanHang.themSanPhamVaoGio(sanPham, loHang, soLuong);
        }
    }
    
    /**
     * Lấy khách hàng hiện tại
     */
    public KhachHang getKhachHangHienTai() {
        return khachHangHienTai;
    }

    private void createOrder() {
        try {
            // 1. Kiểm tra giỏ hàng có sản phẩm không
            if (gdBanHang == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "Lỗi hệ thống: Không thể truy cập giỏ hàng!");
                return;
            }
            
            List<Panel_ChiTietSanPham> danhSachSanPham = gdBanHang.getDanhSachSanPhamTrongGio();
            if (danhSachSanPham == null || danhSachSanPham.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Giỏ hàng trống! Vui lòng thêm sản phẩm trước khi thanh toán.");
                return;
            }
            
            // 2. Kiểm tra tiền khách đưa
            double tongHoaDon = parseCurrency(txtTongHoaDon.getText());
            double tienKhachDua = parseCurrency(txtTienKhachDua.getText());
            
            if (tienKhachDua < tongHoaDon) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Tiền khách đưa không đủ! Còn thiếu: " + String.format("%,.0f đ", tongHoaDon - tienKhachDua));
                return;
            }
            
            // 3. Lấy thông tin nhân viên hiện tại
            NhanVien nhanVien = UserSession.getInstance().getNhanVien();
            if (nhanVien == null) {
                // Fallback: lấy nhân viên đầu tiên từ DB
                List<NhanVien> danhSachNV = nhanVienBUS.layTatCaNhanVien();
                if (danhSachNV != null && !danhSachNV.isEmpty()) {
                    nhanVien = danhSachNV.get(0);
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Không tìm thấy thông tin nhân viên!");
                    return;
                }
            }
            
            // 4. Tạo đơn hàng
            DonHang donHang = new DonHang();
            donHang.setNgayDatHang(LocalDate.now());
            donHang.setThanhTien(tongHoaDon);
            donHang.setPhuongThucThanhToan(PhuongThucThanhToan.TIEN_MAT); // Mặc định tiền mặt
            donHang.setNhanVien(nhanVien);
            
            // Khách hàng (nếu có, không thì null)
            donHang.setKhachHang(khachHangHienTai);
            
            // Khuyến mãi ĐƠN HÀNG (nếu có, không thì null)
            KhuyenMai kmDonHang = danhSachKhuyenMaiDaChon.get(LoaiKhuyenMai.DON_HANG);
            donHang.setKhuyenMai(kmDonHang);
            
            // 5. Lưu đơn hàng vào database
            if (!donHangBUS.taoDonHang(donHang)) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "Không thể tạo đơn hàng! Vui lòng thử lại.");
                return;
            }
            
            // 6. Tạo danh sách chi tiết đơn hàng
            List<ChiTietDonHang> chiTietDonHangList = new ArrayList<>();
            
            // Lấy khuyến mãi sản phẩm (nếu có) để tính giảm giá
            KhuyenMai kmSanPham = danhSachKhuyenMaiDaChon.get(LoaiKhuyenMai.SAN_PHAM);
            List<ChiTietKhuyenMaiSanPham> danhSachCTKM = null;
            if (kmSanPham != null) {
                danhSachCTKM = chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(kmSanPham.getMaKhuyenMai());
            }
            
            // ===== BƯỚC 1: Tính tổng thành tiền sau giảm giá sản phẩm (để phân bổ giảm giá hóa đơn) =====
            double tongThanhTienSauGiamGiaSP = 0;
            for (Panel_ChiTietSanPham panel : danhSachSanPham) {
                double tongTienGoc = panel.getTongTien(); // đơn giá × số lượng
                double giamGiaSP = panel.getSoTienGiamGia();
                tongThanhTienSauGiamGiaSP += (tongTienGoc - giamGiaSP);
            }
            
            // Lấy giá trị giảm giá hóa đơn từ biến instance
            double giamGiaHoaDon = this.discountOrder;
            
            // ===== BƯỚC 2: Tạo chi tiết đơn hàng và phân bổ giảm giá =====
            for (Panel_ChiTietSanPham panel : danhSachSanPham) {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(donHang);
                chiTiet.setLoHang(panel.getLoHangDaChon());
                chiTiet.setSoLuong(panel.getSoLuong());
                chiTiet.setDonGia(panel.getSanPham().getGiaBan());
                
                // Lấy số tiền giảm giá sản phẩm từ panel
                double tongTienGoc = panel.getTongTien(); // Giá gốc = đơn giá × số lượng
                double giamGiaSP = panel.getSoTienGiamGia(); // Giảm giá sản phẩm
                double thanhTienSauGiamGiaSP = tongTienGoc - giamGiaSP;
                
                // Tính giảm giá hóa đơn phân bổ (theo tỷ lệ)
                double giamGiaHoaDonPhanBo = 0;
                if (tongThanhTienSauGiamGiaSP > 0 && giamGiaHoaDon > 0) {
                    giamGiaHoaDonPhanBo = (thanhTienSauGiamGiaSP / tongThanhTienSauGiamGiaSP) * giamGiaHoaDon;
                }
                
                // TỔNG GIẢM GIÁ = Giảm giá sản phẩm + Giảm giá hóa đơn phân bổ
                double tongGiamGia = giamGiaSP + giamGiaHoaDonPhanBo;
                
                // Debug log
                System.out.println("DEBUG - Sản phẩm: " + panel.getSanPham().getTenSanPham() + 
                    ", Giá gốc: " + tongTienGoc + 
                    ", Giảm giá SP: " + giamGiaSP +
                    ", Giảm giá HĐ phân bổ: " + giamGiaHoaDonPhanBo +
                    ", TỔNG giảm giá: " + tongGiamGia);
                
                // LƯU RIÊNG 2 LOẠI GIẢM GIÁ VÀO DATABASE
                chiTiet.setGiamGiaSanPham(giamGiaSP);  // CHỈ giảm giá sản phẩm
                chiTiet.setGiamGiaHoaDonPhanBo(giamGiaHoaDonPhanBo);  // Giảm giá hóa đơn phân bổ
                
                // Thành tiền = Giá gốc - Tổng giảm giá
                double thanhTien = tongTienGoc - tongGiamGia;
                chiTiet.setThanhTien(thanhTien);
                
                // Lưu chi tiết đơn hàng
                if (!chiTietDonHangBUS.themChiTietDonHang(chiTiet)) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Không thể lưu chi tiết đơn hàng!");
                    return;
                }
                
                // QUAN TRỌNG: Xử lý giảm tồn kho - tự động chuyển lô nếu cần
                int soLuongBan = panel.getSoLuong();
                List<LoHang> danhSachLoHang = panel.getDanhSachLoHang();
                
                if (danhSachLoHang == null || danhSachLoHang.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Lỗi: Không tìm thấy lô hàng cho sản phẩm " + panel.getSanPham().getTenSanPham());
                    return;
                }
                
                // Kiểm tra tổng tồn kho của tất cả các lô còn hiệu lực
                int tongTonKho = danhSachLoHang.stream()
                    .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
                    .mapToInt(LoHang::getTonKho)
                    .sum();
                
                if (tongTonKho < soLuongBan) {
                    String donViTinh = panel.getSanPham().getDonViTinh() != null ? 
                        panel.getSanPham().getDonViTinh().getTenDonVi() : "sản phẩm";
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "❌ Không đủ hàng! Sản phẩm '" + panel.getSanPham().getTenSanPham() + 
                        "' chỉ còn " + tongTonKho + " " + donViTinh + " trong kho.\n" +
                        "Vui lòng giảm số lượng hoặc xóa sản phẩm khỏi giỏ hàng!");
                    return;
                }
                
                // Giảm tồn kho từ các lô hàng (ưu tiên lô có hạn sử dụng gần nhất)
                int soLuongConLai = soLuongBan;
                List<LoHang> danhSachLoHopLe = danhSachLoHang.stream()
                    .filter(lh -> lh.getTonKho() > 0 && lh.isTrangThai())
                    .sorted((lh1, lh2) -> lh1.getHanSuDung().compareTo(lh2.getHanSuDung())) // FIFO
                    .collect(java.util.stream.Collectors.toList());
                
                for (LoHang loHang : danhSachLoHopLe) {
                    if (soLuongConLai <= 0) break;
                    
                    int tonKhoHienTai = loHang.getTonKho();
                    int soLuongTuLoNay = Math.min(tonKhoHienTai, soLuongConLai);
                    int tonKhoMoi = tonKhoHienTai - soLuongTuLoNay;
                    
                    try {
                        loHang.setTonKho(tonKhoMoi);
                        
                        // Cập nhật vào database
                        if (!loHangBUS.capNhatLoHang(loHang)) {
                            Notifications.getInstance().show(Notifications.Type.ERROR, 
                                Notifications.Location.TOP_CENTER,
                                "Không thể cập nhật tồn kho cho lô hàng: " + loHang.getMaLoHang());
                            return;
                        }
                        
                        // Ghi log
                        System.out.println("✓ Giảm tồn kho lô " + loHang.getMaLoHang() + 
                            ": " + tonKhoHienTai + " → " + tonKhoMoi + 
                            " (bán " + soLuongTuLoNay + ")");
                        
                        soLuongConLai -= soLuongTuLoNay;
                        
                    } catch (Exception e) {
                        Notifications.getInstance().show(Notifications.Type.ERROR, 
                            Notifications.Location.TOP_CENTER,
                            "Lỗi khi cập nhật tồn kho: " + e.getMessage());
                        return;
                    }
                }
                
                chiTietDonHangList.add(chiTiet);
            }
            
            // 7. Thông báo thành công
            String message = "Tạo đơn hàng thành công! Mã đơn: " + donHang.getMaDonHang();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                Notifications.Location.TOP_CENTER,
                message);
            
            // 8. Hiển thị dialog xác nhận hóa đơn
            hienThiXacNhanHoaDon(donHang, chiTietDonHangList, kmSanPham);
            
            // 10. Reset giỏ hàng
            gdBanHang.xoaToanBoGioHang();
            resetThanhToan();
            
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "Lỗi: " + e.getMessage());
        }
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
    
    private void btnThanhToanQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanQRActionPerformed
        hienThiQRBanking();
    }//GEN-LAST:event_btnThanhToanQRActionPerformed
    
    /**
     * Hiển thị dialog QR Banking để thanh toán
     */
    private void hienThiQRBanking() {
        // Kiểm tra có sản phẩm trong giỏ chưa
        if (tongTienHang <= 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Chưa có sản phẩm trong giỏ hàng");
            return;
        }
        
        // Tính tổng tiền cần thanh toán
        double tongThanhToan = tongTienHang - discountProduct - discountOrder;
        
        if (tongThanhToan <= 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, Notifications.Location.TOP_CENTER, 
                "Số tiền thanh toán phải lớn hơn 0");
            return;
        }
        
        // Tạo mã đơn hàng tạm (để hiển thị trong QR)
        String maDonHangTam = "DH" + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy")) + 
                             String.format("%04d", (int)(Math.random() * 10000));
        
        // Hiển thị dialog QR
        try {
            java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
            vn.edu.iuh.fit.iuhpharmacitymanagement.gui.dialog.Dialog_QRBanking dialog = 
                new vn.edu.iuh.fit.iuhpharmacitymanagement.gui.dialog.Dialog_QRBanking(
                    parentFrame, maDonHangTam, tongThanhToan);
            dialog.setVisible(true);
            
            // Sau khi đóng dialog, có thể xử lý thêm (nếu cần confirm thanh toán)
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, 
                "Vui lòng quét mã QR để hoàn tất thanh toán");
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, 
                "Lỗi khi hiển thị QR Code: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double tongTienHang;
    private double discountProduct;
    private double discountOrder;
    
    /**
     * Cập nhật tổng tiền hàng
     */
    public void updateTongTienHang(double tongTien) {
        this.tongTienHang = tongTien;
        txtTongTienHang.setText(String.format("%,.0f đ", tongTien));
        
        // Hiển thị/ẩn nút QR Banking dựa vào có sản phẩm hay không
        btnThanhToanQR.setVisible(tongTien > 0);
        
        updateTongHoaDon();
    }
    
    /**
     * Cập nhật tổng giảm giá sản phẩm
     * @param discount Tổng số tiền giảm giá
     * @param khuyenMai Khuyến mãi được áp dụng (có thể null nếu có nhiều khuyến mãi)
     */
    public void updateDiscountProduct(double discount, KhuyenMai khuyenMai) {
        this.discountProduct = discount;
        txtDiscountProduct.setText(String.format("%,.0f đ", discount));
        
        updateTongHoaDon();
    }
    
    /**
     * Cập nhật tổng giảm giá hóa đơn
     * @param discount Tổng số tiền giảm giá
     * @param khuyenMai Khuyến mãi được áp dụng (có thể null)
     */
    public void updateDiscountOrder(double discount, KhuyenMai khuyenMai) {
        this.discountOrder = discount;
        txtDiscountOrder.setText(String.format("%,.0f đ", discount));
        
        if (lblTenKhuyenMaiHoaDon != null) {
            if (khuyenMai != null && discount > 0) {
                lblTenKhuyenMaiHoaDon.setText("(" + khuyenMai.getTenKhuyenMai() + " - " + String.format("%.1f", khuyenMai.getGiamGia() * 100) + "%)");
            } else {
                lblTenKhuyenMaiHoaDon.setText("");
            }
        }
        
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
     * Tính tiền thối cho khách (tự động cập nhật khi thay đổi tiền khách đưa)
     */
    private void updateTienTraKhach() {
        try {
            // Lấy số tiền khách đưa, chỉ giữ lại số (bỏ dấu chấm, chữ)
            String tienKhachDuaStr = txtTienKhachDua.getText().replaceAll("[^0-9]", "");
            
            if (!tienKhachDuaStr.isEmpty()) {
                // Chuyển thành số
                double tienKhachDua = Double.parseDouble(tienKhachDuaStr);
                
                // Tính tổng hóa đơn = Tổng hàng - Giảm giá SP - Giảm giá HĐ
                double tongHoaDon = tongTienHang - discountProduct - discountOrder;
                
                // Tiền thối = Tiền khách đưa - Tổng hóa đơn
                double tienTraKhach = tienKhachDua - tongHoaDon;
                
                if (tienTraKhach >= 0) {
                    // Đủ tiền → Hiển thị tiền thối
                    txtTienTraKhach.setText(String.format("%,.0f đ", tienTraKhach));
                } else {
                    // Thiếu tiền → Hiển thị 0
                    txtTienTraKhach.setText("0 đ");
                }
            } else {
                // Chưa nhập → Hiển thị 0
                txtTienTraKhach.setText("0 đ");
            }
        } catch (NumberFormatException e) {
            // Lỗi → Hiển thị 0
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
        lblTenKhuyenMaiHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 11));
        lblTenKhuyenMaiHoaDon.setForeground(new java.awt.Color(0, 150, 0));
        lblTenKhuyenMaiHoaDon.setText("");
        lblTenKhuyenMaiHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        
        // Tạo panel wrapper để label nằm thẳng hàng với textfield
        // Panel cho khuyến mãi hóa đơn
        javax.swing.JPanel pnKhuyenMaiHoaDon = new javax.swing.JPanel();
        pnKhuyenMaiHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        javax.swing.GroupLayout layoutHoaDon = new javax.swing.GroupLayout(pnKhuyenMaiHoaDon);
        pnKhuyenMaiHoaDon.setLayout(layoutHoaDon);
        layoutHoaDon.setHorizontalGroup(
            layoutHoaDon.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(lblTenKhuyenMaiHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layoutHoaDon.setVerticalGroup(
            layoutHoaDon.createSequentialGroup()
            .addComponent(lblTenKhuyenMaiHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        
        // Thêm panel wrapper vào panel tương ứng
        jPanel1.add(pnKhuyenMaiHoaDon);  // Thêm vào panel giảm giá hóa đơn
    }
    
    /**
     * Lấy khuyến mãi đã chọn (backward compatibility)
     * @deprecated Sử dụng getDanhSachKhuyenMaiDaChon() thay thế
     */
    public KhuyenMai getKhuyenMaiDaChon() {
        return khuyenMaiDaChon;
    }
    
    /**
     * Lấy danh sách khuyến mãi đã chọn (Map theo loại)
     */
    public Map<LoaiKhuyenMai, KhuyenMai> getDanhSachKhuyenMaiDaChon() {
        return danhSachKhuyenMaiDaChon;
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
        // Tìm TẤT CẢ khuyến mãi áp dụng được (có thể cả 2 loại)
        Map<LoaiKhuyenMai, KhuyenMai> khuyenMaiApDung = khuyenMaiBUS.timKhuyenMaiApDung(tongTienHang, danhSachSanPham);
        
        // Nếu không có sản phẩm trong giỏ hoặc không có khuyến mãi nào phù hợp
        if (tongTienHang == 0 || khuyenMaiApDung.isEmpty()) {
            // Reset về không áp dụng khuyến mãi
            if (!danhSachKhuyenMaiDaChon.isEmpty()) {
                danhSachKhuyenMaiDaChon.clear();
                khuyenMaiDaChon = null; // backward compatibility
            }
            // Reset giảm giá của tất cả panel về 0
            apDungGiamGiaChoTatCaPanel(null);
            return;
        }
        
        // Kiểm tra xem có thay đổi khuyến mãi không
        boolean coThayDoi = false;
        boolean khuyenMaiSanPhamThayDoi = false;
        boolean khuyenMaiDonHangThayDoi = false;
        
        // Kiểm tra từng loại khuyến mãi riêng biệt
        for (Map.Entry<LoaiKhuyenMai, KhuyenMai> entry : khuyenMaiApDung.entrySet()) {
            LoaiKhuyenMai loai = entry.getKey();
            KhuyenMai kmMoi = entry.getValue();
            KhuyenMai kmCu = danhSachKhuyenMaiDaChon.get(loai);
            
            if (kmCu == null || !kmCu.getMaKhuyenMai().equals(kmMoi.getMaKhuyenMai())) {
                coThayDoi = true;
                if (loai == LoaiKhuyenMai.SAN_PHAM) {
                    khuyenMaiSanPhamThayDoi = true;
                } else if (loai == LoaiKhuyenMai.DON_HANG) {
                    khuyenMaiDonHangThayDoi = true;
                }
            }
        }
        
        // Kiểm tra xem có khuyến mãi bị loại bỏ không
        for (LoaiKhuyenMai loai : danhSachKhuyenMaiDaChon.keySet()) {
            if (!khuyenMaiApDung.containsKey(loai)) {
                coThayDoi = true;
                if (loai == LoaiKhuyenMai.SAN_PHAM) {
                    khuyenMaiSanPhamThayDoi = true;
                } else if (loai == LoaiKhuyenMai.DON_HANG) {
                    khuyenMaiDonHangThayDoi = true;
                }
            }
        }
        
        // Nếu có thay đổi, cập nhật khuyến mãi
        if (coThayDoi) {
            // CHỈ CẬP NHẬT khuyến mãi nào thay đổi, GIỮ NGUYÊN khuyến mãi không thay đổi
            if (khuyenMaiSanPhamThayDoi) {
                if (khuyenMaiApDung.containsKey(LoaiKhuyenMai.SAN_PHAM)) {
                    danhSachKhuyenMaiDaChon.put(LoaiKhuyenMai.SAN_PHAM, khuyenMaiApDung.get(LoaiKhuyenMai.SAN_PHAM));
                } else {
                    danhSachKhuyenMaiDaChon.remove(LoaiKhuyenMai.SAN_PHAM);
                }
            }
            
            if (khuyenMaiDonHangThayDoi) {
                if (khuyenMaiApDung.containsKey(LoaiKhuyenMai.DON_HANG)) {
                    danhSachKhuyenMaiDaChon.put(LoaiKhuyenMai.DON_HANG, khuyenMaiApDung.get(LoaiKhuyenMai.DON_HANG));
                } else {
                    danhSachKhuyenMaiDaChon.remove(LoaiKhuyenMai.DON_HANG);
                }
            }
            
            // Tạo text hiển thị thông báo (dựa trên danh sách đã chọn, không phải khuyenMaiApDung)
            StringBuilder thongBaoDisplay = new StringBuilder();
            
            KhuyenMai kmSanPham = danhSachKhuyenMaiDaChon.get(LoaiKhuyenMai.SAN_PHAM);
            KhuyenMai kmDonHang = danhSachKhuyenMaiDaChon.get(LoaiKhuyenMai.DON_HANG);
            
            // Tạo thông báo CHỈ cho khuyến mãi thay đổi
            if (khuyenMaiSanPhamThayDoi && khuyenMaiDonHangThayDoi) {
                // Cả 2 khuyến mãi đều thay đổi
                if (kmSanPham != null && kmDonHang != null) {
                    thongBaoDisplay.append("✨ Tự động áp dụng: ")
                        .append(kmSanPham.getTenKhuyenMai())
                        .append(" (giảm ").append(kmSanPham.getGiamGia())
                        .append("% sản phẩm) + ")
                        .append(kmDonHang.getTenKhuyenMai())
                        .append(" (giảm ").append(kmDonHang.getGiamGia())
                        .append("% đơn hàng)");
                } else if (kmSanPham != null) {
                    thongBaoDisplay.append("✨ Tự động áp dụng: ")
                        .append(kmSanPham.getTenKhuyenMai())
                        .append(" (giảm ").append(kmSanPham.getGiamGia())
                        .append("% cho các sản phẩm áp dụng)");
                } else if (kmDonHang != null) {
                    thongBaoDisplay.append("✨ Tự động áp dụng: ")
                        .append(kmDonHang.getTenKhuyenMai())
                        .append(" (giảm ").append(kmDonHang.getGiamGia())
                        .append("% trên tổng hóa đơn)");
                }
            } else if (khuyenMaiSanPhamThayDoi) {
                // Chỉ khuyến mãi sản phẩm thay đổi
                if (kmSanPham != null) {
                    thongBaoDisplay.append("✨ Tự động áp dụng: ")
                        .append(kmSanPham.getTenKhuyenMai())
                        .append(" (giảm ").append(kmSanPham.getGiamGia())
                        .append("% cho các sản phẩm áp dụng)");
                } else {
                    thongBaoDisplay.append("❌ Đã hủy khuyến mãi sản phẩm");
                }
            } else if (khuyenMaiDonHangThayDoi) {
                // Chỉ khuyến mãi đơn hàng thay đổi
                if (kmDonHang != null) {
                    thongBaoDisplay.append("✨ Tự động áp dụng: ")
                        .append(kmDonHang.getTenKhuyenMai())
                        .append(" (giảm ").append(kmDonHang.getGiamGia())
                        .append("% trên tổng hóa đơn)");
                } else {
                    thongBaoDisplay.append("❌ Đã hủy khuyến mãi đơn hàng");
                }
            }
            
            // Cập nhật backward compatibility
            if (kmDonHang != null) {
                khuyenMaiDaChon = kmDonHang; // backward compatibility: ưu tiên KM đơn hàng
            } else if (kmSanPham != null) {
                khuyenMaiDaChon = kmSanPham; // backward compatibility
            }
            
            // Hiển thị thông báo CHỈ KHI CÓ thay đổi
            if (thongBaoDisplay.length() > 0) {
                Notifications.getInstance().show(
                    Notifications.Type.SUCCESS, 
                    Notifications.Location.TOP_CENTER,
                    thongBaoDisplay.toString()
                );
            }
            
            // ÁP DỤNG GIẢM GIÁ CHO TẤT CẢ PANEL (nếu khuyến mãi sản phẩm thay đổi)
            if (khuyenMaiSanPhamThayDoi) {
                apDungGiamGiaChoTatCaPanel(kmSanPham);
            }
            
            // Notify để cập nhật lại tổng tiền (sử dụng danh sách đã chọn thực tế)
            firePropertyChange("khuyenMaiChanged", null, danhSachKhuyenMaiDaChon);
        } else {
            // Không có thay đổi khuyến mãi, NHƯNG có thể cần áp dụng lại (vì panel mới được thêm)
            // Chỉ áp dụng nếu có khuyến mãi sản phẩm
            KhuyenMai kmSanPham = danhSachKhuyenMaiDaChon.get(LoaiKhuyenMai.SAN_PHAM);
            if (kmSanPham != null) {
                apDungGiamGiaChoTatCaPanel(kmSanPham);
            }
        }
    }
    
    /**
     * Áp dụng giảm giá cho tất cả Panel_ChiTietSanPham dựa trên khuyến mãi sản phẩm
     * @param kmSanPham Khuyến mãi sản phẩm (null = reset về 0)
     */
    private void apDungGiamGiaChoTatCaPanel(KhuyenMai kmSanPham) {
        if (gdBanHang == null) return;
        
        // Lấy containerPanel từ GD_BanHang
        javax.swing.JPanel containerPanel = gdBanHang.getContainerPanel();
        if (containerPanel == null) return;
        
        if (kmSanPham == null) {
            // Reset tất cả về 0
            for (java.awt.Component comp : containerPanel.getComponents()) {
                if (comp instanceof Panel_ChiTietSanPham) {
                    Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                    panel.setGiamGia(0);
                }
            }
            return;
        }
        
        // Lấy danh sách sản phẩm trong khuyến mãi
        List<ChiTietKhuyenMaiSanPham> danhSachCTKM = 
            chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(kmSanPham.getMaKhuyenMai());
        
        // Áp dụng giảm giá cho từng panel
        for (java.awt.Component comp : containerPanel.getComponents()) {
            if (comp instanceof Panel_ChiTietSanPham) {
                Panel_ChiTietSanPham panel = (Panel_ChiTietSanPham) comp;
                
                // Kiểm tra xem sản phẩm có trong khuyến mãi không
                boolean coTrongKM = danhSachCTKM.stream()
                    .anyMatch(ctkm -> ctkm.getSanPham().getMaSanPham().equals(panel.getSanPham().getMaSanPham()));
                
                if (coTrongKM) {
                    panel.setGiamGia(kmSanPham.getGiamGia(), kmSanPham.getTenKhuyenMai());
                } else {
                    panel.setGiamGia(0);
                }
            }
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
        // Xóa hết nút cũ
        pnPriceSuggest.removeAll();
        
        // Lấy tổng hóa đơn (VD: "49.900 đ" → 49900.0)
        double tongHoaDon = parseCurrency(txtTongHoaDon.getText());
        
        if (tongHoaDon <= 0) {
            // Chưa có sản phẩm → Ẩn hết nút
        } else {
            // Danh sách 6 nút gợi ý
            java.util.List<Long> suggestions = new java.util.ArrayList<>();
            
            // Làm tròn lên (VD: 49900.5 → 49901) khong lam tron thi se kho tin
            long tongHoaDonRound = (long) Math.ceil(tongHoaDon);
            
            // NÚT 1: Làm tròn lên nghìn (VD: 49900 → 50000)
            long exact = (long) (Math.ceil(tongHoaDonRound / 1000.0) * 1000);
            suggestions.add(exact);
            
            // NÚT 2-3: Thêm các mức gần kề (thông minh theo mức giá)
            if (exact < 10000) {
                // Hóa đơn nhỏ: +1k, +2k
                addIfNotExists(suggestions, exact + 1000);
                addIfNotExists(suggestions, exact + 2000);
            } else if (exact < 50000) {
                // Hóa đơn vừa: +1k, +5k
                addIfNotExists(suggestions, exact + 1000);
                addIfNotExists(suggestions, exact + 5000);
            } else if (exact < 100000) {
                // Hóa đơn lớn: +5k, +10k
                addIfNotExists(suggestions, exact + 5000);
                addIfNotExists(suggestions, exact + 10000);
            } else {
                // Hóa đơn rất lớn: +10k, +20k
                addIfNotExists(suggestions, exact + 10000);
                addIfNotExists(suggestions, exact + 20000);
            }
            
            // NÚT 4-6: Thêm các mệnh giá tiêu chuẩn (2k, 5k, 10k, 20k, 50k, 100k,...)
            long[] allDenominations = {
                2000, 5000, 10000, 20000, 50000, 
                100000, 200000, 500000, 
                1000000, 2000000, 5000000, 10000000
            };
            
            // Duyệt qua các mệnh giá, thêm nếu > tổng HĐ và chưa có trong list
            for (long denom : allDenominations) {
                if (suggestions.size() >= 6) break;  // Đủ 6 nút → Dừng
                if (denom > exact && !suggestions.contains(denom)) {
                    suggestions.add(denom);
                }
            }
            
            // Nếu vẫn thiếu (hóa đơn quá lớn) → Gấp đôi số cuối cùng
            if (suggestions.size() < 6) {
                long lastAmount = suggestions.get(suggestions.size() - 1);
                while (suggestions.size() < 6) {
                    lastAmount *= 2;  // Gấp đôi (VD: 30tr → 60tr → 120tr)
                    if (!suggestions.contains(lastAmount)) {
                        suggestions.add(lastAmount);
                    }
                }
            }
            
            // Tạo 6 nút trên UI
            for (int i = 0; i < Math.min(6, suggestions.size()); i++) {
                addPriceSuggestButton(suggestions.get(i));
            }
        }
        
        // Cập nhật giao diện
        pnPriceSuggest.revalidate();
        pnPriceSuggest.repaint();
    }
    
    /**
     * Thêm giá trị vào list nếu chưa tồn tại (tránh trùng)
     */
    private void addIfNotExists(java.util.List<Long> list, long value) {
        // Kiểm tra chưa có trong list → Thêm vào
        if (!list.contains(value)) {
            list.add(value);
        }
    }
    
    /**
     * Tạo và thêm nút tiền vào panel
     * 
     * @param amount số tiền hiển thị trên nút
     */
    private void addPriceSuggestButton(long amount) {
        // Tạo nút mới
        javax.swing.JButton btn = new javax.swing.JButton();
        
        // Format số: 50000 → "50.000 đ" (dấu chấm phân cách nghìn)
        String giaText = String.format("%,d đ", amount).replace(",", ".");
        btn.setText(giaText);
        
        // Style nút: bo góc, viền xanh, nền xanh nhạt
        btn.setFont(btn.getFont().deriveFont(java.awt.Font.BOLD, 13f));
        btn.putClientProperty(FlatClientProperties.STYLE, 
            "arc:8;borderWidth:1;borderColor:#519AF4;background:#F0F8FF");
        
        // Khi click nút
        btn.addActionListener(e -> {
            double tongHoaDon = parseCurrency(txtTongHoaDon.getText());
            
            // Kiểm tra giỏ hàng rỗng
            if (tongHoaDon <= 0) {
                Notifications.getInstance().show(
                    Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Chưa có sản phẩm trong giỏ hàng"
                );
                return;
            }
            
            // Điền số tiền khách đưa
            txtTienKhachDua.setText(String.format("%,d", amount).replace(",", "."));
            
            // Tự động tính tiền thối
            updateTienTraKhach();
        });
        
        // Thêm nút vào panel
        pnPriceSuggest.add(btn);
    }
    
    /**
     * Chuyển chuỗi tiền thành số
     * VD: "1.000.000 đ" → 1000000.0
     * 
     * @param currencyText chuỗi tiền (có dấu chấm, ký tự "đ")
     * @return giá trị số
     */
    private double parseCurrency(String currencyText) {
        // Kiểm tra null hoặc rỗng
        if (currencyText == null || currencyText.trim().isEmpty()) {
            return 0;
        }
        
        try {
            // Loại bỏ ký tự không phải số: "49.900 đ" → "49900"
            String cleaned = currencyText.replace(".", "")    // Bỏ dấu chấm
                                        .replace("đ", "")      // Bỏ chữ đ
                                        .replace(",", "")      // Bỏ dấu phẩy
                                        .trim();               // Bỏ khoảng trắng
            // Chuyển thành số
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            // Lỗi format → Trả về 0
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
        if (lblTenKhuyenMaiHoaDon != null) {
            lblTenKhuyenMaiHoaDon.setText("");
        }
    }
    
    /**
     * Hiển thị dialog xác nhận hóa đơn (với nút In hóa đơn)
     */
    private void hienThiXacNhanHoaDon(DonHang donHang, List<ChiTietDonHang> danhSachChiTiet, KhuyenMai khuyenMaiSanPham) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Xác Nhận Hóa Đơn");
        dialog.setModal(true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(null);
        
        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Format cho số tiền và ngày tháng
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // === HEADER ===
        javax.swing.JPanel headerPanel = new javax.swing.JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("XÁC NHẬN HÓA ĐƠN");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(20));
        
        // Thông tin đơn hàng
        javax.swing.JPanel infoPanel = new javax.swing.JPanel(new java.awt.GridLayout(5, 2, 10, 8));
        infoPanel.setBackground(Color.WHITE);
        
        infoPanel.add(createInfoLabel("Mã hóa đơn: ", donHang.getMaDonHang(), true));
        
        NhanVien nhanVien = donHang.getNhanVien();
        String tenNhanVien = nhanVien != null ? nhanVien.getTenNhanVien() : "N/A";
        infoPanel.add(createInfoLabel("Nhân viên: ", tenNhanVien, false));
        
        String ngayDat = donHang.getNgayDatHang().format(dateFormatter);
        infoPanel.add(createInfoLabel("Ngày lập: ", ngayDat, false));
        
        KhachHang khachHang = donHang.getKhachHang();
        String tenKhachHang = khachHang != null ? khachHang.getTenKhachHang() : "Khách vãng lai";
        infoPanel.add(createInfoLabel("Khách hàng: ", tenKhachHang, true));
        
        String phuongThuc = donHang.getPhuongThucThanhToan() != null ? 
                donHang.getPhuongThucThanhToan().toString() : "TIEN_MAT";
        infoPanel.add(createInfoLabel("Phương thức: ", phuongThuc, false));
        
        headerPanel.add(infoPanel);
        headerPanel.add(Box.createVerticalStrut(10));
        
        // Đường phân cách
        javax.swing.JSeparator separator1 = new javax.swing.JSeparator();
        headerPanel.add(separator1);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // === BODY - Bảng chi tiết sản phẩm ===
        javax.swing.JPanel bodyPanel = new javax.swing.JPanel(new BorderLayout());
        bodyPanel.setBackground(Color.WHITE);
        
        javax.swing.JLabel lblChiTiet = new javax.swing.JLabel("Chi tiết sản phẩm");
        lblChiTiet.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblChiTiet.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bodyPanel.add(lblChiTiet, BorderLayout.NORTH);
        
        String[] columnNames = {"STT", "Tên sản phẩm", "Đơn vị", "Số lượng", "Đơn giá", "Giảm giá", "Thành tiền"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(250);  // Tên SP
        table.getColumnModel().getColumn(2).setPreferredWidth(70);   // Đơn vị
        table.getColumnModel().getColumn(3).setPreferredWidth(70);   // Số lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Đơn giá
        table.getColumnModel().getColumn(5).setPreferredWidth(200);  // Giảm giá (NEW)
        table.getColumnModel().getColumn(6).setPreferredWidth(120);  // Thành tiền
        
        // Tính tổng tiền hàng và tổng giảm giá sản phẩm
        double tongTienHangTruocGiamGia = 0;
        double tongGiamGiaSanPham = 0;
        for (ChiTietDonHang ct : danhSachChiTiet) {
            tongTienHangTruocGiamGia += ct.getDonGia() * ct.getSoLuong();
            tongGiamGiaSanPham += ct.getGiamGiaSanPham();
        }
        
        // Tính giảm giá hóa đơn
        double giamGiaHoaDon = 0;
        KhuyenMai khuyenMaiDonHang = donHang.getKhuyenMai();
        if (khuyenMaiDonHang != null && khuyenMaiDonHang.getGiamGia() > 0) {
            giamGiaHoaDon = (tongTienHangTruocGiamGia - tongGiamGiaSanPham) * khuyenMaiDonHang.getGiamGia();
        }
        
        // Thêm dữ liệu vào bảng
        int stt = 1;
        for (ChiTietDonHang chiTiet : danhSachChiTiet) {
            SanPham sanPham = chiTiet.getLoHang().getSanPham();
            
            // Tính giảm giá TỔNG = giảm giá sản phẩm + giảm giá hóa đơn phân bổ
            double giamGiaSanPham = chiTiet.getGiamGiaSanPham();
            
            // Phân bổ giảm giá hóa đơn theo tỷ lệ thành tiền (sau giảm giá sản phẩm)
            double giamGiaHoaDonPhanBo = 0;
            if (giamGiaHoaDon > 0 && tongTienHangTruocGiamGia - tongGiamGiaSanPham > 0) {
                double thanhTienSauGiamGiaSP = chiTiet.getDonGia() * chiTiet.getSoLuong() - giamGiaSanPham;
                giamGiaHoaDonPhanBo = (thanhTienSauGiamGiaSP / (tongTienHangTruocGiamGia - tongGiamGiaSanPham)) * giamGiaHoaDon;
            }
            
            double tongGiamGia = giamGiaSanPham + giamGiaHoaDonPhanBo;
            
            // Tính giảm giá với % và tên KM
            String giamGia;
            if (tongGiamGia > 0) {
                double tongTienGoc = chiTiet.getDonGia() * chiTiet.getSoLuong();
                double phanTramGiamGia = (tongGiamGia / tongTienGoc) * 100;
                
                giamGia = "-" + currencyFormat.format(tongGiamGia) + " đ" +
                          " (" + String.format("%.1f", phanTramGiamGia) + "%)";
            } else {
                giamGia = "0 đ";
            }
            
            tableModel.addRow(new Object[]{
                stt++,
                sanPham.getTenSanPham(),
                sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getTenDonVi() : "Tuýp",
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                giamGia,  // Tổng giảm giá (sản phẩm + hóa đơn phân bổ)
                currencyFormat.format(chiTiet.getThanhTien()) + " đ"
            });
        }
        
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        bodyPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        
        // === FOOTER - Tổng tiền + NÚT IN HÓA ĐƠN ===
        javax.swing.JPanel footerPanel = new javax.swing.JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        javax.swing.JSeparator separator2 = new javax.swing.JSeparator();
        footerPanel.add(separator2);
        footerPanel.add(Box.createVerticalStrut(15));
        
        // Tính toán và hiển thị tổng tiền (sử dụng biến đã tính ở trên)
        double tongTienHang = tongTienHangTruocGiamGia;
        
        // Tổng tiền hàng
        javax.swing.JPanel tongTienHangPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienHangPanel.setBackground(Color.WHITE);
        javax.swing.JLabel lblTongTienHangText = new javax.swing.JLabel("Tổng tiền hàng: ");
        lblTongTienHangText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        javax.swing.JLabel lblTongTienHang = new javax.swing.JLabel(currencyFormat.format(tongTienHang) + " đ");
        lblTongTienHang.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        tongTienHangPanel.add(lblTongTienHangText);
        tongTienHangPanel.add(lblTongTienHang);
        footerPanel.add(tongTienHangPanel);
        
        // Tính tổng giảm giá hóa đơn phân bổ (giamGiaHoaDon và tongGiamGiaSanPham đã được tính ở trên)
        double tongGiamGiaHoaDonPhanBo = 0;
        for (ChiTietDonHang ct : danhSachChiTiet) {
            tongGiamGiaHoaDonPhanBo += ct.getGiamGiaHoaDonPhanBo();
        }
        double tongGiamGia = tongGiamGiaSanPham + tongGiamGiaHoaDonPhanBo;
        
        // Giảm giá sản phẩm
        if (tongGiamGiaSanPham > 0) {
            javax.swing.JPanel giamGiaSPPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            giamGiaSPPanel.setBackground(Color.WHITE);
            javax.swing.JLabel lblGiamGiaSPText = new javax.swing.JLabel("Giảm giá sản phẩm: ");
            lblGiamGiaSPText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            javax.swing.JLabel lblGiamGiaSP = new javax.swing.JLabel("-" + currencyFormat.format(tongGiamGiaSanPham) + " đ");
            lblGiamGiaSP.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            lblGiamGiaSP.setForeground(new Color(220, 53, 69));
            giamGiaSPPanel.add(lblGiamGiaSPText);
            giamGiaSPPanel.add(lblGiamGiaSP);
            footerPanel.add(giamGiaSPPanel);
        }
        
        // Giảm giá hóa đơn
        if (tongGiamGiaHoaDonPhanBo > 0) {
            javax.swing.JPanel giamGiaHDPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            giamGiaHDPanel.setBackground(Color.WHITE);
            
            // Tính % giảm giá gốc
            double phanTramGoc = 0;
            if (tongTienHang > 0) {
                phanTramGoc = (tongGiamGiaHoaDonPhanBo / tongTienHang) * 100;
            }
            
            javax.swing.JLabel lblGiamGiaHDText = new javax.swing.JLabel("Giảm giá hóa đơn: ");
            lblGiamGiaHDText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            javax.swing.JLabel lblGiamGiaHD = new javax.swing.JLabel("-" + currencyFormat.format(tongGiamGiaHoaDonPhanBo) + " đ (" + String.format("%.0f", phanTramGoc) + "%)");
            lblGiamGiaHD.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            lblGiamGiaHD.setForeground(new Color(220, 53, 69));
            giamGiaHDPanel.add(lblGiamGiaHDText);
            giamGiaHDPanel.add(lblGiamGiaHD);
            footerPanel.add(giamGiaHDPanel);
        }
        
        // Tổng giảm giá (nếu có)
        if (tongGiamGia > 0) {
            javax.swing.JPanel tongGiamGiaPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            tongGiamGiaPanel.setBackground(Color.WHITE);
            javax.swing.JLabel lblTongGiamGiaText = new javax.swing.JLabel("TỔNG GIẢM GIÁ: ");
            lblTongGiamGiaText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            javax.swing.JLabel lblTongGiamGia = new javax.swing.JLabel("-" + currencyFormat.format(tongGiamGia) + " đ");
            lblTongGiamGia.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            lblTongGiamGia.setForeground(new Color(220, 53, 69));
            tongGiamGiaPanel.add(lblTongGiamGiaText);
            tongGiamGiaPanel.add(lblTongGiamGia);
            footerPanel.add(tongGiamGiaPanel);
        }
        
        // Đường kẻ phân cách
        javax.swing.JSeparator separator3 = new javax.swing.JSeparator();
        separator3.setForeground(new Color(100, 100, 100));
        footerPanel.add(Box.createVerticalStrut(5));
        footerPanel.add(separator3);
        footerPanel.add(Box.createVerticalStrut(5));
        
        // Thành tiền
        javax.swing.JPanel tongTienPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        tongTienPanel.setBackground(Color.WHITE);
        
        javax.swing.JLabel lblTongTienText = new javax.swing.JLabel("THÀNH TIỀN: ");
        lblTongTienText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        
        javax.swing.JLabel lblTongTien = new javax.swing.JLabel(currencyFormat.format(donHang.getThanhTien()) + " đ");
        lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        lblTongTien.setForeground(new Color(0, 120, 215));
        
        tongTienPanel.add(lblTongTienText);
        tongTienPanel.add(lblTongTien);
        
        footerPanel.add(tongTienPanel);
        footerPanel.add(Box.createVerticalStrut(20));
        
        // Nút "In Hóa Đơn"
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        javax.swing.JButton btnInHoaDon = new javax.swing.JButton("In Hóa Đơn");
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        btnInHoaDon.setPreferredSize(new java.awt.Dimension(200, 45));
        btnInHoaDon.setBackground(new Color(40, 167, 69));
        btnInHoaDon.setForeground(Color.WHITE);
        btnInHoaDon.setFocusPainted(false);
        btnInHoaDon.setBorderPainted(false);
        btnInHoaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        btnInHoaDon.addActionListener(e -> {
            // Đóng dialog xác nhận
            dialog.dispose();
            
            // Hiển thị hóa đơn bán hàng (preview UI không lưu PDF)
            hienThiHoaDonBanHang(donHang, danhSachChiTiet, khuyenMaiSanPham);
        });
        
        buttonPanel.add(btnInHoaDon);
        footerPanel.add(buttonPanel);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * Hiển thị preview hóa đơn bán hàng (UI giống PDF nhưng nhỏ gọn)
     */
    private void hienThiHoaDonBanHang(DonHang donHang, List<ChiTietDonHang> danhSachChiTiet, KhuyenMai khuyenMaiSanPham) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa Đơn Bán Hàng");
        dialog.setModal(true);
        dialog.setSize(650, 900);  // Tăng chiều cao để không bị scroll
        dialog.setLocationRelativeTo(null);
        
        // Scroll pane cho toàn bộ hóa đơn
        javax.swing.JScrollPane mainScrollPane = new javax.swing.JScrollPane();
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Panel chính
        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));  // Giảm padding
        
        // Format cho số tiền và ngày tháng
        DecimalFormat currencyFormat = new DecimalFormat("#,###");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // ========== HEADER - THÔNG TIN CỬA HÀNG ==========
        javax.swing.JLabel lblStoreName = new javax.swing.JLabel("IUH PHARMACITY");
        lblStoreName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));  // Giảm size
        lblStoreName.setForeground(new Color(0, 120, 215));
        lblStoreName.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblStoreName);
        mainPanel.add(Box.createVerticalStrut(3));  // Giảm khoảng cách
        
        javax.swing.JLabel lblAddress = new javax.swing.JLabel("12 Nguyen Van Bao, Ward 4, Go Vap District, HCMC");
        lblAddress.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));  // Giảm size
        lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblAddress);
        mainPanel.add(Box.createVerticalStrut(2));
        
        javax.swing.JLabel lblContact = new javax.swing.JLabel("Hotline: 1800 6928 | Email: cskh@pharmacity.vn");
        lblContact.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblContact.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblContact);
        mainPanel.add(Box.createVerticalStrut(12));  // Giảm khoảng cách
        
        // ========== TIÊU ĐỀ HÓA ĐƠN ==========
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("HOA DON BAN HANG");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));  // Giảm size
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(8));
        
        // ========== BARCODE MÃ ĐƠN HÀNG ==========
        try {
            BufferedImage barcodeImage = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil.taoBarcode(donHang.getMaDonHang());
            BufferedImage barcodeWithText = vn.edu.iuh.fit.iuhpharmacitymanagement.util.BarcodeUtil.addTextBelow(barcodeImage, donHang.getMaDonHang());
            
            javax.swing.JLabel lblBarcode = new javax.swing.JLabel(new javax.swing.ImageIcon(barcodeWithText));
            lblBarcode.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(lblBarcode);
        } catch (Exception ex) {
            System.err.println("Lỗi tạo barcode: " + ex.getMessage());
        }
        mainPanel.add(Box.createVerticalStrut(2));
        
        String ngayLap = donHang.getNgayDatHang().format(dateFormatter);
        javax.swing.JLabel lblDate = new javax.swing.JLabel("Ngay lap: " + ngayLap);
        lblDate.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblDate);
        mainPanel.add(Box.createVerticalStrut(12));
        
        // ========== THÔNG TIN KHÁCH HÀNG VÀ NHÂN VIÊN (2 CỘT) ==========
        javax.swing.JPanel infoTablePanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 10, 0));  // Giảm gap
        infoTablePanel.setBackground(Color.WHITE);
        infoTablePanel.setMaximumSize(new java.awt.Dimension(600, 80));  // Giảm chiều cao
        
        // THÔNG TIN KHÁCH HÀNG (Cột trái)
        KhachHang khachHang = donHang.getKhachHang();
        javax.swing.JPanel customerPanel = new javax.swing.JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        customerPanel.setBackground(Color.WHITE);
        customerPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));  // Giảm padding
        
        javax.swing.JLabel lblCustomerTitle = new javax.swing.JLabel("THONG TIN KHACH HANG");
        lblCustomerTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));  // Giảm size
        lblCustomerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        customerPanel.add(lblCustomerTitle);
        customerPanel.add(Box.createVerticalStrut(3));
        
        String tenKH = khachHang != null ? khachHang.getTenKhachHang() : "Vang lai";
        javax.swing.JLabel lblCustomerName = new javax.swing.JLabel("Ho ten: " + tenKH);
        lblCustomerName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
        lblCustomerName.setAlignmentX(Component.LEFT_ALIGNMENT);
        customerPanel.add(lblCustomerName);
        
        if (khachHang != null && khachHang.getSoDienThoai() != null) {
            javax.swing.JLabel lblCustomerPhone = new javax.swing.JLabel("SDT: " + khachHang.getSoDienThoai());
            lblCustomerPhone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
            lblCustomerPhone.setAlignmentX(Component.LEFT_ALIGNMENT);
            customerPanel.add(lblCustomerPhone);
        }
        
        // THÔNG TIN NHÂN VIÊN (Cột phải)
        NhanVien nhanVien = donHang.getNhanVien();
        javax.swing.JPanel employeePanel = new javax.swing.JPanel();
        employeePanel.setLayout(new BoxLayout(employeePanel, BoxLayout.Y_AXIS));
        employeePanel.setBackground(Color.WHITE);
        employeePanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        
        javax.swing.JLabel lblEmployeeTitle = new javax.swing.JLabel("THONG TIN NHAN VIEN");
        lblEmployeeTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        lblEmployeeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        employeePanel.add(lblEmployeeTitle);
        employeePanel.add(Box.createVerticalStrut(3));
        
        if (nhanVien != null) {
            javax.swing.JLabel lblEmployeeName = new javax.swing.JLabel("Ho ten: " + nhanVien.getTenNhanVien());
            lblEmployeeName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
            lblEmployeeName.setAlignmentX(Component.LEFT_ALIGNMENT);
            employeePanel.add(lblEmployeeName);
            
            if (nhanVien.getSoDienThoai() != null) {
                javax.swing.JLabel lblEmployeePhone = new javax.swing.JLabel("SDT: " + nhanVien.getSoDienThoai());
                lblEmployeePhone.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));
                lblEmployeePhone.setAlignmentX(Component.LEFT_ALIGNMENT);
                employeePanel.add(lblEmployeePhone);
            }
        }
        
        infoTablePanel.add(customerPanel);
        infoTablePanel.add(employeePanel);
        mainPanel.add(infoTablePanel);
        mainPanel.add(Box.createVerticalStrut(10));  // Giảm khoảng cách
        
        // ========== BẢNG SẢN PHẨM (GIỐNG PDF - CHỈ HIỂN THỊ GIẢM GIÁ SẢN PHẨM) ==========
        String[] columnNames = {"STT", "Ten san pham", "SL", "Don gia", "Giam gia", "Thanh tien"};
        javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        javax.swing.JTable table = new javax.swing.JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 9));  // Giảm size
        table.setRowHeight(28);  // Giảm row height
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 9));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setGridColor(new Color(220, 220, 220));
        
        // Set column widths (nhỏ gọn hơn)
        table.getColumnModel().getColumn(0).setPreferredWidth(30);   // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(180);  // Tên sản phẩm
        table.getColumnModel().getColumn(2).setPreferredWidth(35);   // SL
        table.getColumnModel().getColumn(3).setPreferredWidth(80);   // Đơn giá
        table.getColumnModel().getColumn(4).setPreferredWidth(90);   // Giảm giá (chỉ SP)
        table.getColumnModel().getColumn(5).setPreferredWidth(85);   // Thành tiền
        
        // Center align cho các cột số
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Right align cho các cột tiền
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        // ===== TÍNH TOÁN GIẢM GIÁ (TÁCH RIÊNG SẢN PHẨM VÀ HÓA ĐƠN) =====
        // Bước 1: Tính tổng tiền hàng trước giảm giá
        double tongTienHangTruocGiamGia = 0;
        for (ChiTietDonHang ct : danhSachChiTiet) {
            tongTienHangTruocGiamGia += ct.getDonGia() * ct.getSoLuong();
        }
        
        // Bước 2: Tính tổng giảm giá sản phẩm và giảm giá hóa đơn từ DB
        // ĐƠN GIẢN - KHÔNG CẦN TÍNH NGƯỢC!
        double tongGiamGiaSanPham = 0;
        double giamGiaHoaDon = 0;
        for (ChiTietDonHang ct : danhSachChiTiet) {
            double ggSP = ct.getGiamGiaSanPham();
            double ggHD = ct.getGiamGiaHoaDonPhanBo();
            tongGiamGiaSanPham += ggSP;  // Giảm giá sản phẩm
            giamGiaHoaDon += ggHD;  // Giảm giá hóa đơn phân bổ
            
            // Debug
            System.out.println("DEBUG HÓA ĐƠN - SP: " + ct.getLoHang().getSanPham().getTenSanPham() + 
                ", giamGiaSP: " + ggSP + ", giamGiaHD: " + ggHD);
        }
        System.out.println("DEBUG HÓA ĐƠN - TỔNG giảm giá SP: " + tongGiamGiaSanPham + ", TỔNG giảm giá HĐ: " + giamGiaHoaDon);
        
        // Thêm dữ liệu vào bảng - CHỈ HIỂN THỊ GIẢM GIÁ SẢN PHẨM
        int stt = 1;
        for (ChiTietDonHang chiTiet : danhSachChiTiet) {
            LoHang loHang = chiTiet.getLoHang();
            SanPham sanPham = loHang != null ? loHang.getSanPham() : null;
            
            // Tên sản phẩm (bỏ thông tin lô hàng để gọn)
            String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
            String tenSPFull = tenSP;
            
            // ĐƠN GIẢN - LẤY TRỰC TIẾP TỪ DB!
            double giamGiaSanPham = chiTiet.getGiamGiaSanPham();  // CHỈ giảm giá sản phẩm
            double tongTienGoc = chiTiet.getDonGia() * chiTiet.getSoLuong();
            
            // Hiển thị giảm giá sản phẩm với %
            String giamGia;
            if (giamGiaSanPham > 0) {
                double phanTramGiamGia = (giamGiaSanPham / tongTienGoc) * 100;
                giamGia = "-" + currencyFormat.format(giamGiaSanPham) + " đ" +
                          " (" + String.format("%.1f", phanTramGiamGia) + "%)";
            } else {
                giamGia = "0 đ";
            }
            
            tableModel.addRow(new Object[]{
                stt++,
                tenSPFull,
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                giamGia,  // CHỈ giảm giá sản phẩm (không bao gồm giảm giá hóa đơn)
                currencyFormat.format(chiTiet.getThanhTien()) + " đ"
            });
        }
        
        javax.swing.JScrollPane tableScrollPane = new javax.swing.JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tableScrollPane.setPreferredSize(new java.awt.Dimension(580, 250));  // Giảm size
        tableScrollPane.setMaximumSize(new java.awt.Dimension(600, 250));
        mainPanel.add(tableScrollPane);
        mainPanel.add(Box.createVerticalStrut(12));  // Giảm khoảng cách
        
        // ========== TÍNH TOÁN CÁC GIÁ TRỊ (GIỐNG PDF) - Sử dụng biến đã tính ở trên ==========
        double tongTienHang = tongTienHangTruocGiamGia;
        
        // Tổng giảm giá
        double tongGiamGia = tongGiamGiaSanPham + giamGiaHoaDon;
        
        // ========== BẢNG THANH TOÁN (GIỐNG PDF - PANEL PHẢI) ==========
        javax.swing.JPanel paymentPanel = new javax.swing.JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Căn giữa
        paymentPanel.setMaximumSize(new java.awt.Dimension(450, 300));  // Giảm chiều rộng để căn giữa đẹp hơn
        
        // Helper method để tạo row thanh toán
        java.util.function.BiConsumer<String, String> addPaymentRow = (label, value) -> {
            javax.swing.JPanel rowPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
            rowPanel.setBackground(Color.WHITE);
            rowPanel.setMaximumSize(new java.awt.Dimension(450, 25));  // Giảm chiều cao
            
            javax.swing.JLabel lblLeft = new javax.swing.JLabel(label);
            lblLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));  // Giảm size
            
            javax.swing.JLabel lblRight = new javax.swing.JLabel(value);
            lblRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
            lblRight.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
            
            rowPanel.add(lblLeft, BorderLayout.WEST);
            rowPanel.add(lblRight, BorderLayout.EAST);
            
            paymentPanel.add(rowPanel);
            paymentPanel.add(Box.createVerticalStrut(3));  // Giảm khoảng cách
        };
        
        // Tổng tiền hàng
        addPaymentRow.accept("Tong tien hang:", currencyFormat.format(tongTienHang) + " đ");
        
        // Giảm giá sản phẩm (LUÔN hiển thị)
        addPaymentRow.accept("Giam gia san pham:", 
            tongGiamGiaSanPham > 0 ? "-" + currencyFormat.format(tongGiamGiaSanPham) + " đ" : "0 đ");
        
        // Giảm giá hóa đơn (LUÔN hiển thị)
        KhuyenMai khuyenMaiDonHang = donHang.getKhuyenMai();
        if (giamGiaHoaDon > 0) {
            String labelText = "Giam gia hoa don:";
            if (khuyenMaiDonHang != null && khuyenMaiDonHang.getTenKhuyenMai() != null && !khuyenMaiDonHang.getTenKhuyenMai().trim().isEmpty()) {
                labelText += "\n(" + khuyenMaiDonHang.getTenKhuyenMai() + ")";
            }
            
            // Tính % giảm giá GÔC (so với tổng tiền hàng trước giảm giá)
            double phanTramGoc = 0;
            if (tongTienHangTruocGiamGia > 0) {
                phanTramGoc = (giamGiaHoaDon / tongTienHangTruocGiamGia) * 100;
            }
            
            String valueText = "-" + currencyFormat.format(giamGiaHoaDon) + " đ" +
                             " (" + String.format("%.0f", phanTramGoc) + "%)";
            addPaymentRow.accept(labelText, valueText);
        } else {
            // Hiển thị giảm giá hóa đơn = 0 khi không có khuyến mãi
            addPaymentRow.accept("Giam gia hoa don:", "0 đ");
        }
        
        // Tổng giảm giá (nếu có)
        if (tongGiamGia > 0) {
            paymentPanel.add(Box.createVerticalStrut(2));
            javax.swing.JPanel separatorPanel = new javax.swing.JPanel();
            separatorPanel.setBackground(new Color(200, 200, 200));
            separatorPanel.setMaximumSize(new java.awt.Dimension(450, 1));
            paymentPanel.add(separatorPanel);
            paymentPanel.add(Box.createVerticalStrut(5));
            
            javax.swing.JPanel tongGGPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
            tongGGPanel.setBackground(Color.WHITE);
            tongGGPanel.setMaximumSize(new java.awt.Dimension(450, 25));
            
            javax.swing.JLabel lblTongGGLeft = new javax.swing.JLabel("TONG GIAM GIA:");
            lblTongGGLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
            
            javax.swing.JLabel lblTongGGRight = new javax.swing.JLabel("-" + currencyFormat.format(tongGiamGia) + " đ");
            lblTongGGRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
            lblTongGGRight.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
            
            tongGGPanel.add(lblTongGGLeft, BorderLayout.WEST);
            tongGGPanel.add(lblTongGGRight, BorderLayout.EAST);
            
            paymentPanel.add(tongGGPanel);
            paymentPanel.add(Box.createVerticalStrut(5));
        }
        
        // Đường kẻ trước THÀNH TIỀN
        javax.swing.JPanel separator2Panel = new javax.swing.JPanel();
        separator2Panel.setBackground(new Color(200, 200, 200));
        separator2Panel.setMaximumSize(new java.awt.Dimension(450, 1));
        paymentPanel.add(separator2Panel);
        paymentPanel.add(Box.createVerticalStrut(5));
        
        // THÀNH TIỀN (in đậm, màu xanh)
        javax.swing.JPanel thanhTienPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        thanhTienPanel.setBackground(Color.WHITE);
        thanhTienPanel.setMaximumSize(new java.awt.Dimension(450, 30));
        
        javax.swing.JLabel lblThanhTienLeft = new javax.swing.JLabel("THANH TIEN:");
        lblThanhTienLeft.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        
        javax.swing.JLabel lblThanhTienRight = new javax.swing.JLabel(currencyFormat.format(donHang.getThanhTien()) + " đ");
        lblThanhTienRight.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblThanhTienRight.setForeground(new Color(0, 120, 215));
        lblThanhTienRight.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        
        thanhTienPanel.add(lblThanhTienLeft, BorderLayout.WEST);
        thanhTienPanel.add(lblThanhTienRight, BorderLayout.EAST);
        
        paymentPanel.add(thanhTienPanel);
        paymentPanel.add(Box.createVerticalStrut(5));
        
        // Đường kẻ sau THÀNH TIỀN
        javax.swing.JPanel separator3Panel = new javax.swing.JPanel();
        separator3Panel.setBackground(new Color(200, 200, 200));
        separator3Panel.setMaximumSize(new java.awt.Dimension(450, 1));
        paymentPanel.add(separator3Panel);
        paymentPanel.add(Box.createVerticalStrut(5));
        
        // Phương thức thanh toán
        String phuongThuc = donHang.getPhuongThucThanhToan() != null ? 
                           donHang.getPhuongThucThanhToan().toString() : "TIEN_MAT";
        addPaymentRow.accept("Phuong thuc:", phuongThuc);
        
        mainPanel.add(paymentPanel);
        mainPanel.add(Box.createVerticalStrut(15));  // Giảm khoảng cách
        
        // ========== FOOTER (GIỐNG PDF) ==========
        javax.swing.JLabel lblFooter1 = new javax.swing.JLabel("Cam on quy khach da mua hang tai Pharmacity!");
        lblFooter1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));  // Giảm size
        lblFooter1.setForeground(new Color(0, 120, 215));
        lblFooter1.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblFooter1);
        mainPanel.add(Box.createVerticalStrut(3));
        
        javax.swing.JLabel lblFooter2 = new javax.swing.JLabel("Vui long giu hoa don de doi/tra hang trong vong 7 ngay");
        lblFooter2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 8));  // Giảm size
        lblFooter2.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblFooter2);
        
        // Thêm mainPanel vào scrollPane
        mainScrollPane.setViewportView(mainPanel);
        
        dialog.add(mainScrollPane);
        dialog.setVisible(true);
    }
    
    /**
     * Tạo JPanel với label thông tin
     */
    private javax.swing.JPanel createInfoLabel(String title, String value, boolean bold) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        panel.setBackground(Color.WHITE);
        
        javax.swing.JLabel lblTitle = new javax.swing.JLabel(title);
        lblTitle.setFont(new java.awt.Font("Segoe UI", 
                bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 14));
        
        javax.swing.JLabel lblValue = new javax.swing.JLabel(value);
        lblValue.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        
        panel.add(lblTitle);
        panel.add(lblValue);
        
        return panel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBanHang;
    private javax.swing.JButton btnThanhToanQR;
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

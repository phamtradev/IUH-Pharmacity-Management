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
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.XuatHoaDonPDF;
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
            
            for (Panel_ChiTietSanPham panel : danhSachSanPham) {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(donHang);
                chiTiet.setLoHang(panel.getLoHangDaChon());
                chiTiet.setSoLuong(panel.getSoLuong());
                chiTiet.setDonGia(panel.getSanPham().getGiaBan());
                
                // Lấy số tiền giảm giá từ panel (đã bao gồm giảm giá từ khuyến mãi hoặc nhập tay)
                double tongTienGoc = panel.getTongTien(); // Giá gốc = đơn giá × số lượng
                double giamGiaSP = panel.getSoTienGiamGia(); // Lấy trực tiếp từ panel
                
                // Debug log
                System.out.println("DEBUG - Sản phẩm: " + panel.getSanPham().getTenSanPham() + 
                    ", Giá gốc: " + tongTienGoc + 
                    ", % Giảm: " + (panel.getGiamGia() * 100) + "%" +
                    ", Giảm giá: " + giamGiaSP);
                
                chiTiet.setGiamGia(giamGiaSP);
                
                // Thành tiền = Giá gốc - Giảm giá
                double thanhTien = tongTienGoc - giamGiaSP;
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
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        Notifications.Location.TOP_CENTER,
                        "Hết hàng! Sản phẩm '" + panel.getSanPham().getTenSanPham() + 
                        "' chỉ còn " + tongTonKho + " trong kho");
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
            
            // 7. Tự động xuất hóa đơn PDF vào folder resources/img/hoadonbanhang
            String pdfPath = XuatHoaDonPDF.xuatHoaDonTuDong(donHang, chiTietDonHangList, kmSanPham);
            
            // 8. Thông báo thành công
            String message = "Tạo đơn hàng thành công! Mã đơn: " + donHang.getMaDonHang();
            if (pdfPath != null) {
                message += "\nHóa đơn đã được lưu tại: " + pdfPath;
            }
            Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                Notifications.Location.TOP_CENTER,
                message);
            
            // 9. Hiển thị preview hóa đơn luôn
            hienThiHoaDon(donHang, chiTietDonHangList, kmSanPham);
            
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

    private double tongTienHang;
    private double discountProduct;
    private double discountOrder;
    
    /**
     * Cập nhật tổng tiền hàng
     */
    public void updateTongTienHang(double tongTien) {
        this.tongTienHang = tongTien;
        txtTongTienHang.setText(String.format("%,.0f đ", tongTien));
        
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
            
            // Notify để cập nhật lại tổng tiền (sử dụng danh sách đã chọn thực tế)
            firePropertyChange("khuyenMaiChanged", null, danhSachKhuyenMaiDaChon);
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
     * Hiển thị preview hóa đơn bán hàng
     */
    private void hienThiHoaDon(DonHang donHang, List<ChiTietDonHang> danhSachChiTiet, KhuyenMai khuyenMaiSanPham) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Hóa đơn bán hàng");
        dialog.setModal(true);
        dialog.setSize(900, 650);
        dialog.setLocationRelativeTo(null); // Hiển thị giữa màn hình
        
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
        
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("HÓA ĐƠN BÁN HÀNG");
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
        String sdtKhachHang = khachHang != null && khachHang.getSoDienThoai() != null ? 
                khachHang.getSoDienThoai() : "";
        infoPanel.add(createInfoLabel("Khách hàng: ", tenKhachHang, true));
        
        if (!sdtKhachHang.isEmpty()) {
            infoPanel.add(createInfoLabel("Số điện thoại: ", sdtKhachHang, false));
        } else {
            infoPanel.add(new javax.swing.JLabel()); // Empty cell
        }
        
        String phuongThuc = donHang.getPhuongThucThanhToan() != null ? 
                donHang.getPhuongThucThanhToan().toString() : "Tiền mặt";
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
        
        String[] columnNames = {"STT", "Tên sản phẩm", "Đơn vị", "Số lượng", "Đơn giá", "Thành tiền"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(300); // Tên SP
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Đơn vị
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Số lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Đơn giá
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Thành tiền
        
        // Thêm dữ liệu vào bảng
        int stt = 1;
        for (ChiTietDonHang chiTiet : danhSachChiTiet) {
            SanPham sanPham = chiTiet.getLoHang().getSanPham();
            
            tableModel.addRow(new Object[]{
                stt++,
                sanPham.getTenSanPham(),
                sanPham.getDonViTinh() != null ? sanPham.getDonViTinh().getTenDonVi() : "Hộp",
                chiTiet.getSoLuong(),
                currencyFormat.format(chiTiet.getDonGia()) + " đ",
                currencyFormat.format(chiTiet.getThanhTien()) + " đ"
            });
        }
        
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        bodyPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        
        // === FOOTER - Tổng tiền ===
        javax.swing.JPanel footerPanel = new javax.swing.JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        javax.swing.JSeparator separator2 = new javax.swing.JSeparator();
        footerPanel.add(separator2);
        footerPanel.add(Box.createVerticalStrut(15));
        
        // ========== TÍNH TOÁN CÁC GIÁ TRỊ ==========
        // 1. Tổng tiền hàng (trước giảm giá)
        double tongTienHang = 0;
        double tongGiamGiaSanPham = 0;
        
        for (ChiTietDonHang ct : danhSachChiTiet) {
            double tienSP = ct.getDonGia() * ct.getSoLuong();
            tongTienHang += tienSP;
            tongGiamGiaSanPham += ct.getGiamGia();
        }
        
        // 2. Giảm giá đơn hàng
        double giamGiaHoaDon = 0;
        KhuyenMai khuyenMaiDonHang = donHang.getKhuyenMai();
        if (khuyenMaiDonHang != null) {
            // Giảm giá đơn hàng = (Tổng tiền hàng - Giảm giá sản phẩm) × % (getGiamGia đã là decimal)
            giamGiaHoaDon = (tongTienHang - tongGiamGiaSanPham) * khuyenMaiDonHang.getGiamGia();
        }
        
        // 3. Tổng giảm giá
        double tongGiamGia = tongGiamGiaSanPham + giamGiaHoaDon;
        
        // ========== HIỂN THỊ CHI TIẾT ==========
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
        
        // Giảm giá sản phẩm (nếu có)
        if (tongGiamGiaSanPham > 0 && khuyenMaiSanPham != null) {
            javax.swing.JPanel giamGiaSPPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            giamGiaSPPanel.setBackground(Color.WHITE);
            
            javax.swing.JLabel lblGiamGiaSPText = new javax.swing.JLabel("Giảm giá sản phẩm: ");
            lblGiamGiaSPText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            
            // Lấy % từ khuyến mãi gốc - chỉ hiển thị số tiền và %
            String giamGiaText = "-" + currencyFormat.format(tongGiamGiaSanPham) + " đ";
            giamGiaText += " (" + String.format("%.1f", khuyenMaiSanPham.getGiamGia() * 100) + "%)";
            
            javax.swing.JLabel lblGiamGiaSP = new javax.swing.JLabel(giamGiaText);
            lblGiamGiaSP.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            lblGiamGiaSP.setForeground(new Color(0, 150, 0));
            
            giamGiaSPPanel.add(lblGiamGiaSPText);
            giamGiaSPPanel.add(lblGiamGiaSP);
            // BỎ label tên khuyến mãi - chỉ hiển thị % trong cột giảm giá
            footerPanel.add(giamGiaSPPanel);
        }
        
        // Giảm giá hóa đơn (nếu có)
        if (giamGiaHoaDon > 0 && khuyenMaiDonHang != null) {
            javax.swing.JPanel giamGiaHDPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            giamGiaHDPanel.setBackground(Color.WHITE);
            
            javax.swing.JLabel lblGiamGiaHDText = new javax.swing.JLabel("Giảm giá hóa đơn: ");
            lblGiamGiaHDText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            
            String giamGiaHDText = "-" + currencyFormat.format(giamGiaHoaDon) + " đ";
            giamGiaHDText += " (" + String.format("%.1f", khuyenMaiDonHang.getGiamGia() * 100) + "%)";
            
            javax.swing.JLabel lblGiamGiaHD = new javax.swing.JLabel(giamGiaHDText);
            lblGiamGiaHD.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            lblGiamGiaHD.setForeground(new Color(0, 150, 0));
            
            javax.swing.JLabel lblKMName = new javax.swing.JLabel("  (" + khuyenMaiDonHang.getTenKhuyenMai() + ")");
            lblKMName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
            lblKMName.setForeground(new Color(255, 102, 0));
            
            giamGiaHDPanel.add(lblGiamGiaHDText);
            giamGiaHDPanel.add(lblGiamGiaHD);
            giamGiaHDPanel.add(lblKMName);
            footerPanel.add(giamGiaHDPanel);
        }
        
        // Tổng giảm giá (nếu có)
        if (tongGiamGia > 0) {
            javax.swing.JPanel tongGiamGiaPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
            tongGiamGiaPanel.setBackground(Color.WHITE);
            tongGiamGiaPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
            tongGiamGiaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 0, 0, 0)
            ));
            
            javax.swing.JLabel lblTongGiamGiaText = new javax.swing.JLabel("TỔNG GIẢM GIÁ: ");
            lblTongGiamGiaText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            
            javax.swing.JLabel lblTongGiamGia = new javax.swing.JLabel("-" + currencyFormat.format(tongGiamGia) + " đ");
            lblTongGiamGia.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
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
        
        // Thành tiền (sau giảm giá)
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
        footerPanel.add(Box.createVerticalStrut(10));
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhuyenmai;

import com.formdev.flatlaf.FlatClientProperties;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhuyenMaiBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietKhuyenMaiSanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import raven.toast.Notifications;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Dialog để sửa khuyến mãi
 * @author PhamTra
 */
public class SuaKhuyenMaiDialog extends javax.swing.JDialog {

    private final KhuyenMaiBUS khuyenMaiBUS;
    private final SanPhamBUS sanPhamBUS;
    private final ChiTietKhuyenMaiSanPhamBUS chiTietKhuyenMaiSanPhamBUS;
    private final DonHangBUS donHangBUS;
    private boolean suaThanhCong = false;
    private KhuyenMai khuyenMai;
    private SanPham sanPhamHienTai = null;
    private LoaiKhuyenMai loaiKhuyenMaiBanDau; // Lưu loại khuyến mãi ban đầu
    private boolean daTungCoSanPham = false; // Đánh dấu khuyến mãi đã từng được gắn cho sản phẩm
    private boolean daDuocSuDungTrongDonHang = false; // Đánh dấu khuyến mãi đã được sử dụng trong đơn hàng
    
    // Components cho giá tối thiểu và tối đa (khuyến mãi đơn hàng)
    private javax.swing.JLabel lblGiaToiThieu;
    private javax.swing.JTextField txtGiaToiThieu;
    private javax.swing.JLabel lblGiaToiDa;
    private javax.swing.JTextField txtGiaToiDa;

    public SuaKhuyenMaiDialog(java.awt.Frame parent, boolean modal, KhuyenMai km) {
        super(parent, modal);
        this.khuyenMaiBUS = new KhuyenMaiBUS();
        this.sanPhamBUS = new SanPhamBUS(new SanPhamDAO());
        this.chiTietKhuyenMaiSanPhamBUS = new ChiTietKhuyenMaiSanPhamBUS(new ChiTietKhuyenMaiSanPhamDAO());
        this.donHangBUS = new DonHangBUS();
        this.khuyenMai = km;
        initComponents();
        setLocationRelativeTo(parent);
        setupUI();
        loadData();
    }

    private void setupUI() {
        // Placeholder text
        txtTenKhuyenMai.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên khuyến mãi");
        txtGiamGia.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 0.1 (10%)");
        txtGiaToiThieu.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 100000 (0 = không giới hạn)");
        txtGiaToiDa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 50000 (0 = không giới hạn)");
        txtSoDangKy.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng ký sản phẩm");
        
        // Thiết lập date chooser
        dateNgayBatDau.setMinSelectableDate(new Date());
        dateNgayKetThuc.setMinSelectableDate(new Date());
        
        // Thiết lập combo box - Mặc định "Đơn hàng"
        cboLoaiKhuyenMai.setModel(new DefaultComboBoxModel<>(new String[]{"Đơn hàng", "Sản phẩm"}));
        
        // Disable mã khuyến mãi
        txtMaKhuyenMai.setEnabled(false);
        
        // Set txtTenSanPham không thể chỉnh sửa
        txtTenSanPham.setEditable(false);
        txtTenSanPham.setFocusable(false);
        
        // Thêm document listener cho txtSoDangKy để tự động tìm sản phẩm
        txtSoDangKy.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timSanPhamTheoSoDangKy();
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timSanPhamTheoSoDangKy();
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timSanPhamTheoSoDangKy();
            }
        });
        
        // Thêm action listener cho combo box loại khuyến mãi
        cboLoaiKhuyenMai.addActionListener(e -> {
            String loaiKM = (String) cboLoaiKhuyenMai.getSelectedItem();
            
            // Kiểm tra nếu khuyến mãi đã được gắn cho sản phẩm và người dùng cố thay đổi loại
            if (daTungCoSanPham && loaiKhuyenMaiBanDau != null) {
                String loaiMoiStr = (String) cboLoaiKhuyenMai.getSelectedItem();
                LoaiKhuyenMai loaiMoi = loaiMoiStr.equals("Sản phẩm") ? LoaiKhuyenMai.SAN_PHAM : LoaiKhuyenMai.DON_HANG;
                
                // Nếu thay đổi loại khuyến mãi
                if (loaiMoi != loaiKhuyenMaiBanDau) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        "Không thể thay đổi loại khuyến mãi! Khuyến mãi này đã được gắn cho sản phẩm.");
                    // Đặt lại giá trị cũ
                    SwingUtilities.invokeLater(() -> {
                        cboLoaiKhuyenMai.setSelectedItem(
                            loaiKhuyenMaiBanDau == LoaiKhuyenMai.SAN_PHAM ? "Sản phẩm" : "Đơn hàng"
                        );
                    });
                    return;
                }
            }
            
            boolean isProductPromotion = "Sản phẩm".equals(loaiKM);
            boolean isOrderPromotion = "Đơn hàng".equals(loaiKM);
            
            // Hiển thị giá tối thiểu và tối đa cho khuyến mãi đơn hàng
            lblGiaToiThieu.setVisible(isOrderPromotion);
            txtGiaToiThieu.setVisible(isOrderPromotion);
            lblGiaToiDa.setVisible(isOrderPromotion);
            txtGiaToiDa.setVisible(isOrderPromotion);
            
            // Hiển thị số đăng ký cho khuyến mãi sản phẩm
            lblSoDangKy.setVisible(isProductPromotion);
            txtSoDangKy.setVisible(isProductPromotion);
            lblTenSanPham.setVisible(isProductPromotion);
            txtTenSanPham.setVisible(isProductPromotion);
            
            // Resize dialog
            pack();
        });
    }
    
    private void timSanPhamTheoSoDangKy() {
        String soDangKy = txtSoDangKy.getText().trim();
        if (soDangKy.isEmpty()) {
            txtTenSanPham.setText("");
            sanPhamHienTai = null;
            return;
        }
        
        // Tìm sản phẩm theo số đăng ký
        List<SanPham> danhSach = sanPhamBUS.layTatCaSanPham();
        sanPhamHienTai = danhSach.stream()
            .filter(sp -> sp.getSoDangKy().equalsIgnoreCase(soDangKy))
            .findFirst()
            .orElse(null);
        
        if (sanPhamHienTai != null) {
            txtTenSanPham.setText(sanPhamHienTai.getTenSanPham());
        } else {
            txtTenSanPham.setText("Không tìm thấy sản phẩm");
        }
    }

    private void loadData() {
        if (khuyenMai != null) {
            // Lưu loại khuyến mãi ban đầu
            loaiKhuyenMaiBanDau = khuyenMai.getLoaiKhuyenMai();
            
            txtMaKhuyenMai.setText(khuyenMai.getMaKhuyenMai());
            txtTenKhuyenMai.setText(khuyenMai.getTenKhuyenMai());
            
            // Convert LocalDate to Date
            Date ngayBD = Date.from(khuyenMai.getNgayBatDau().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date ngayKT = Date.from(khuyenMai.getNgayKetThuc().atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            dateNgayBatDau.setDate(ngayBD);
            dateNgayKetThuc.setDate(ngayKT);
            
            txtGiamGia.setText(String.valueOf(khuyenMai.getGiamGia()));
            
            cboLoaiKhuyenMai.setSelectedItem(
                khuyenMai.getLoaiKhuyenMai() == LoaiKhuyenMai.SAN_PHAM ? "Sản phẩm" : "Đơn hàng"
            );
            
            // Load giá tối thiểu và tối đa
            txtGiaToiThieu.setText(String.valueOf(khuyenMai.getGiaToiThieu()));
            txtGiaToiDa.setText(String.valueOf(khuyenMai.getGiaToiDa()));
            
            // Kiểm tra xem khuyến mãi đã được gắn cho sản phẩm chưa
            try {
                List<ChiTietKhuyenMaiSanPham> chiTietList = chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(khuyenMai.getMaKhuyenMai());
                System.out.println("DEBUG: Số lượng chi tiết khuyến mãi sản phẩm: " + chiTietList.size());
                
                if (!chiTietList.isEmpty()) {
                    daTungCoSanPham = true;
                    ChiTietKhuyenMaiSanPham chiTiet = chiTietList.get(0);
                    SanPham spPartial = chiTiet.getSanPham();
                    
                    if (spPartial != null && spPartial.getMaSanPham() != null) {
                        // DAO chỉ load mã sản phẩm, phải tìm lại sản phẩm đầy đủ từ SanPhamBUS
                        String maSanPham = spPartial.getMaSanPham();
                        System.out.println("DEBUG: Mã sản phẩm từ DB: " + maSanPham);
                        
                        // Tìm sản phẩm đầy đủ
                        SanPham sanPhamDayDu = sanPhamBUS.laySanPhamTheoMa(maSanPham);
                        
                        if (sanPhamDayDu != null) {
                            System.out.println("DEBUG: Sản phẩm đầy đủ tìm thấy: " + sanPhamDayDu.getSoDangKy() + " - " + sanPhamDayDu.getTenSanPham());
                            txtSoDangKy.setText(sanPhamDayDu.getSoDangKy());
                            txtTenSanPham.setText(sanPhamDayDu.getTenSanPham());
                            sanPhamHienTai = sanPhamDayDu;
                        } else {
                            System.err.println("ERROR: Không tìm thấy sản phẩm có mã: " + maSanPham);
                        }
                    } else {
                        System.err.println("ERROR: getSanPham() trả về null hoặc không có mã!");
                    }
                }
            } catch (Exception e) {
                System.err.println("ERROR khi load sản phẩm: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Kiểm tra xem khuyến mãi đã được sử dụng trong đơn hàng chưa
            try {
                List<DonHang> danhSachDonHang = donHangBUS.layTatCaDonHang();
                long soLuongDonHang = danhSachDonHang.stream()
                    .filter(dh -> dh.getKhuyenMai() != null && dh.getKhuyenMai().getMaKhuyenMai().equals(khuyenMai.getMaKhuyenMai()))
                    .count();
                
                if (soLuongDonHang > 0) {
                    daDuocSuDungTrongDonHang = true;
                    System.out.println("Khuyến mãi " + khuyenMai.getMaKhuyenMai() + " đã được sử dụng trong " + soLuongDonHang + " đơn hàng");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Update visibility sau khi load
            String loaiKM = (String) cboLoaiKhuyenMai.getSelectedItem();
            boolean isProductPromotion = "Sản phẩm".equals(loaiKM);
            boolean isOrderPromotion = "Đơn hàng".equals(loaiKM);
            
            lblGiaToiThieu.setVisible(isOrderPromotion);
            txtGiaToiThieu.setVisible(isOrderPromotion);
            lblGiaToiDa.setVisible(isOrderPromotion);
            txtGiaToiDa.setVisible(isOrderPromotion);
            
            lblSoDangKy.setVisible(isProductPromotion);
            txtSoDangKy.setVisible(isProductPromotion);
            lblTenSanPham.setVisible(isProductPromotion);
            txtTenSanPham.setVisible(isProductPromotion);
            
            // Nếu khuyến mãi đã được sử dụng trong đơn hàng, disable các field và hiển thị thông báo
            if (daDuocSuDungTrongDonHang) {
                txtTenKhuyenMai.setEnabled(false);
                dateNgayBatDau.setEnabled(false);
                dateNgayKetThuc.setEnabled(false);
                txtGiamGia.setEnabled(false);
                cboLoaiKhuyenMai.setEnabled(false);
                txtSoDangKy.setEnabled(false);
                btnCapNhat.setEnabled(false);
                
                // Hiển thị thông báo cảnh báo
                SwingUtilities.invokeLater(() -> {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        "Khuyến mãi này đã được sử dụng trong đơn hàng, không thể chỉnh sửa!");
                });
            }
        }
    }

    public boolean isSuaThanhCong() {
        return suaThanhCong;
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtMaKhuyenMai = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTenKhuyenMai = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateNgayBatDau = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dateNgayKetThuc = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cboLoaiKhuyenMai = new javax.swing.JComboBox<>();
        lblGiaToiThieu = new javax.swing.JLabel();
        txtGiaToiThieu = new javax.swing.JTextField();
        lblGiaToiDa = new javax.swing.JLabel();
        txtGiaToiDa = new javax.swing.JTextField();
        lblSoDangKy = new javax.swing.JLabel();
        txtSoDangKy = new javax.swing.JTextField();
        lblTenSanPham = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnCapNhat = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sửa khuyến mãi");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 193, 7));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CẬP NHẬT KHUYẾN MÃI");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Mã khuyến mãi:");

        txtMaKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tên khuyến mãi:");

        txtTenKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Ngày bắt đầu:");

        dateNgayBatDau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Ngày kết thúc:");

        dateNgayKetThuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Giảm giá (%):");

        txtGiamGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Loại khuyến mãi:");

        cboLoaiKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblGiaToiThieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGiaToiThieu.setText("Giá tối thiểu (đ):");

        txtGiaToiThieu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblGiaToiDa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGiaToiDa.setText("Giảm giá tối đa (đ):");

        txtGiaToiDa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblSoDangKy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoDangKy.setText("Số đăng ký:");

        txtSoDangKy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblTenSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSanPham.setText("Tên sản phẩm:");

        txtTenSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(lblGiaToiThieu)
                    .addComponent(lblGiaToiDa)
                    .addComponent(lblSoDangKy)
                    .addComponent(lblTenSanPham))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaKhuyenMai)
                    .addComponent(txtTenKhuyenMai)
                    .addComponent(dateNgayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateNgayKetThuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGiamGia)
                    .addComponent(cboLoaiKhuyenMai, 0, 350, Short.MAX_VALUE)
                    .addComponent(txtGiaToiThieu)
                    .addComponent(txtGiaToiDa)
                    .addComponent(txtSoDangKy)
                    .addComponent(txtTenSanPham)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtMaKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(dateNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(dateNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboLoaiKhuyenMai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiaToiThieu)
                    .addComponent(txtGiaToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiaToiDa)
                    .addComponent(txtGiaToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoDangKy)
                    .addComponent(txtSoDangKy, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenSanPham)
                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 30, 20, 30));

        btnCapNhat.setBackground(new java.awt.Color(255, 193, 7));
        btnCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCapNhat.setForeground(new java.awt.Color(0, 0, 0));
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setPreferredSize(new java.awt.Dimension(120, 40));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnHuy.setBackground(new java.awt.Color(220, 53, 69));
        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy.setForeground(new java.awt.Color(255, 255, 255));
        btnHuy.setText("Hủy");
        btnHuy.setPreferredSize(new java.awt.Dimension(120, 40));
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Kiểm tra xem khuyến mãi đã được sử dụng trong đơn hàng chưa
            if (daDuocSuDungTrongDonHang) {
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    "Không thể cập nhật khuyến mãi! Khuyến mãi này đã được sử dụng trong đơn hàng.");
                return;
            }
            
            // Validate input
            String tenKM = txtTenKhuyenMai.getText().trim();
            if (tenKM.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập tên khuyến mãi!");
                txtTenKhuyenMai.requestFocus();
                return;
            }

            Date ngayBD = dateNgayBatDau.getDate();
            if (ngayBD == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn ngày bắt đầu!");
                return;
            }

            Date ngayKT = dateNgayKetThuc.getDate();
            if (ngayKT == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn ngày kết thúc!");
                return;
            }

            String giamGiaStr = txtGiamGia.getText().trim();
            if (giamGiaStr.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập giảm giá!");
                txtGiamGia.requestFocus();
                return;
            }

            double giamGia;
            try {
                giamGia = Double.parseDouble(giamGiaStr);
                if (giamGia <= 0 || giamGia > 1) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Giảm giá phải từ 0 đến 1 (VD: 0.1 = 10%)!");
                    txtGiamGia.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Giảm giá phải là số!");
                txtGiamGia.requestFocus();
                return;
            }

            // Convert Date to LocalDate
            LocalDate ngayBatDau = ngayBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayKetThuc = ngayKT.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Kiểm tra ngày
            if (ngayKetThuc.isBefore(ngayBatDau)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            }

            String loaiKM = (String) cboLoaiKhuyenMai.getSelectedItem();
            
            // Kiểm tra nếu khuyến mãi đã được gắn cho sản phẩm và người dùng cố thay đổi loại
            if (daTungCoSanPham && loaiKhuyenMaiBanDau != null) {
                LoaiKhuyenMai loaiMoi = loaiKM.equals("Sản phẩm") ? LoaiKhuyenMai.SAN_PHAM : LoaiKhuyenMai.DON_HANG;
                if (loaiMoi != loaiKhuyenMaiBanDau) {
                    Notifications.getInstance().show(Notifications.Type.ERROR, 
                        "Không thể thay đổi loại khuyến mãi! Khuyến mãi này đã được gắn cho sản phẩm.");
                    return;
                }
            }
            
            // Nếu là khuyến mãi đơn hàng, lấy giá tối thiểu
            if (loaiKM.equals("Đơn hàng")) {
                String giaToiThieuStr = txtGiaToiThieu.getText().trim();
                if (!giaToiThieuStr.isEmpty()) {
                    try {
                        double giaToiThieu = Double.parseDouble(giaToiThieuStr);
                        if (giaToiThieu < 0) {
                            Notifications.getInstance().show(Notifications.Type.WARNING, "Giá tối thiểu phải >= 0!");
                            txtGiaToiThieu.requestFocus();
                            return;
                        }
                        khuyenMai.setGiaToiThieu(giaToiThieu);
                    } catch (NumberFormatException e) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, "Giá tối thiểu phải là số!");
                        txtGiaToiThieu.requestFocus();
                        return;
                    }
                } else {
                    khuyenMai.setGiaToiThieu(0); // Mặc định không yêu cầu giá tối thiểu
                }
                
                // Lấy giá giảm tối đa
                String giaToiDaStr = txtGiaToiDa.getText().trim();
                if (!giaToiDaStr.isEmpty()) {
                    try {
                        double giaToiDa = Double.parseDouble(giaToiDaStr);
                        if (giaToiDa < 0) {
                            Notifications.getInstance().show(Notifications.Type.WARNING, "Giá giảm tối đa phải >= 0!");
                            txtGiaToiDa.requestFocus();
                            return;
                        }
                        khuyenMai.setGiaToiDa(giaToiDa);
                    } catch (NumberFormatException e) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, "Giá giảm tối đa phải là số!");
                        txtGiaToiDa.requestFocus();
                        return;
                    }
                } else {
                    khuyenMai.setGiaToiDa(0); // Mặc định không giới hạn giá giảm tối đa
                }
            }
            
            // Nếu là khuyến mãi sản phẩm, kiểm tra đã nhập số đăng ký chưa
            if (loaiKM.equals("Sản phẩm")) {
                String soDangKy = txtSoDangKy.getText().trim();
                if (soDangKy.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập số đăng ký sản phẩm!");
                    txtSoDangKy.requestFocus();
                    return;
                }
                
                if (sanPhamHienTai == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Không tìm thấy sản phẩm với số đăng ký này!");
                    txtSoDangKy.requestFocus();
                    return;
                }
            }

            // Cập nhật đối tượng KhuyenMai
            khuyenMai.setTenKhuyenMai(tenKM);
            khuyenMai.setNgayBatDau(ngayBatDau);
            khuyenMai.setNgayKetThuc(ngayKetThuc);
            khuyenMai.setGiamGia(giamGia);
            khuyenMai.setLoaiKhuyenMai(loaiKM.equals("Sản phẩm") ? LoaiKhuyenMai.SAN_PHAM : LoaiKhuyenMai.DON_HANG);

            // Cập nhật vào database
            boolean success = khuyenMaiBUS.capNhatKhuyenMai(khuyenMai);
            if (success) {
                // Nếu là khuyến mãi sản phẩm, cập nhật bảng ChiTietKhuyenMaiSanPham
                if (loaiKM.equals("Sản phẩm") && sanPhamHienTai != null) {
                    try {
                        // Xóa chi tiết cũ
                        List<ChiTietKhuyenMaiSanPham> chiTietCu = chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(khuyenMai.getMaKhuyenMai());
                        for (ChiTietKhuyenMaiSanPham ct : chiTietCu) {
                            chiTietKhuyenMaiSanPhamBUS.xoaChiTietKhuyenMaiSanPham(ct.getSanPham().getMaSanPham(), khuyenMai.getMaKhuyenMai());
                        }
                        
                        // Thêm chi tiết mới
                        ChiTietKhuyenMaiSanPham ctkmsp = new ChiTietKhuyenMaiSanPham();
                        ctkmsp.setSanPham(sanPhamHienTai);
                        ctkmsp.setKhuyenMai(khuyenMai);
                        
                        boolean successDetail = chiTietKhuyenMaiSanPhamBUS.taoChiTietKhuyenMaiSanPham(ctkmsp);
                        if (!successDetail) {
                            Notifications.getInstance().show(Notifications.Type.WARNING, 
                                "Cập nhật khuyến mãi thành công nhưng không thể liên kết với sản phẩm!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Notifications.getInstance().show(Notifications.Type.WARNING, 
                            "Cập nhật khuyến mãi thành công nhưng lỗi khi liên kết sản phẩm: " + e.getMessage());
                    }
                }
                
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật khuyến mãi thành công!");
                suaThanhCong = true;
                dispose();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật khuyến mãi thất bại!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
        }
    }

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnHuy;
    private javax.swing.JComboBox<String> cboLoaiKhuyenMai;
    private com.toedter.calendar.JDateChooser dateNgayBatDau;
    private com.toedter.calendar.JDateChooser dateNgayKetThuc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblSoDangKy;
    private javax.swing.JLabel lblTenSanPham;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtMaKhuyenMai;
    private javax.swing.JTextField txtSoDangKy;
    private javax.swing.JTextField txtTenKhuyenMai;
    private javax.swing.JTextField txtTenSanPham;
    // End of variables declaration    
}

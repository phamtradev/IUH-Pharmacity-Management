/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhuyenmai;

import com.formdev.flatlaf.FlatClientProperties;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietKhuyenMaiSanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

import javax.swing.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialog để xem chi tiết khuyến mãi (read-only)
 * @author PhamTra
 */
public class XemChiTietKhuyenMaiDialog extends javax.swing.JDialog {

    private final ChiTietKhuyenMaiSanPhamBUS chiTietKhuyenMaiSanPhamBUS;
    private KhuyenMai khuyenMai;
    private List<SanPham> danhSachSanPham = new ArrayList<>(); // Danh sách sản phẩm của khuyến mãi
    
    // Components cho giá tối thiểu và tối đa (khuyến mãi đơn hàng)
    private javax.swing.JLabel lblGiaToiThieu;
    private javax.swing.JTextField txtGiaToiThieu;
    private javax.swing.JLabel lblGiaToiDa;
    private javax.swing.JTextField txtGiaToiDa;
    
    // Components cho số lượng tối đa (khuyến mãi sản phẩm)
    private javax.swing.JLabel lblSoLuongToiDa;
    private javax.swing.JTextField txtSoLuongToiDa;

    public XemChiTietKhuyenMaiDialog(java.awt.Frame parent, boolean modal, KhuyenMai km) {
        super(parent, modal);
        this.khuyenMai = km;
        this.chiTietKhuyenMaiSanPhamBUS = new ChiTietKhuyenMaiSanPhamBUS(new ChiTietKhuyenMaiSanPhamDAO());
        initComponents();
        setLocationRelativeTo(parent);
        setupUI();
        loadData();
    }

    private void setupUI() {
        // Disable tất cả các field để chỉ xem
        txtTenKhuyenMai.setEnabled(false);
        dateNgayBatDau.setEnabled(false);
        dateNgayKetThuc.setEnabled(false);
        txtGiamGia.setEnabled(false);
        cboLoaiKhuyenMai.setEnabled(false);
        txtGiaToiThieu.setEnabled(false);
        txtGiaToiDa.setEnabled(false);
        txtQuetMa.setEnabled(false);
        txtSoLuongToiDa.setEnabled(false);
        
        // Set txtDanhSachSanPham không thể chỉnh sửa
        txtDanhSachSanPham.setEditable(false);
        txtDanhSachSanPham.setFocusable(false);
        txtDanhSachSanPham.setLineWrap(true);
        txtDanhSachSanPham.setWrapStyleWord(true);
        
        // Thiết lập combo box loại khuyến mãi
        cboLoaiKhuyenMai.setModel(new DefaultComboBoxModel<>(new String[]{"Đơn hàng", "Sản phẩm"}));

        ButtonStyles.apply(btnHuy, ButtonStyles.Type.SECONDARY);
    }
    
    private void loadData() {
        if (khuyenMai != null) {
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
            
            // Load số lượng tối đa
            txtSoLuongToiDa.setText(String.valueOf(khuyenMai.getSoLuongToiDa()));
            
            // Load danh sách sản phẩm nếu là khuyến mãi sản phẩm
            if (khuyenMai.getLoaiKhuyenMai() == LoaiKhuyenMai.SAN_PHAM) {
                try {
                    List<ChiTietKhuyenMaiSanPham> chiTietList = chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(khuyenMai.getMaKhuyenMai());
                    danhSachSanPham.clear();
                    for (ChiTietKhuyenMaiSanPham chiTiet : chiTietList) {
                        SanPham sp = chiTiet.getSanPham();
                        // Sản phẩm đã được load đầy đủ từ JOIN query, không cần query lại
                        if (sp != null && sp.getMaSanPham() != null) {
                            danhSachSanPham.add(sp);
                        }
                    }
                    capNhatDanhSachSanPham();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // Update visibility sau khi load
            String loaiKM = (String) cboLoaiKhuyenMai.getSelectedItem();
            boolean isProductPromotion = "Sản phẩm".equals(loaiKM);
            boolean isOrderPromotion = "Đơn hàng".equals(loaiKM);
            
            lblGiaToiThieu.setVisible(isOrderPromotion);
            txtGiaToiThieu.setVisible(isOrderPromotion);
            lblGiaToiDa.setVisible(isOrderPromotion);
            txtGiaToiDa.setVisible(isOrderPromotion);
            
            lblChonSanPham.setVisible(isProductPromotion);
            txtQuetMa.setVisible(isProductPromotion);
            lblDanhSachSanPham.setVisible(isProductPromotion);
            scrollDanhSachSanPham.setVisible(isProductPromotion);
            lblSoLuongToiDa.setVisible(isProductPromotion);
            txtSoLuongToiDa.setVisible(isProductPromotion);
        }
    }
    
    /**
     * Cập nhật hiển thị danh sách sản phẩm
     */
    private void capNhatDanhSachSanPham() {
        if (danhSachSanPham.isEmpty()) {
            txtDanhSachSanPham.setText("Không có sản phẩm nào");
        } else {
            String danhSach = danhSachSanPham.stream()
                .map(sp -> {
                    // Xử lý tên sản phẩm - kiểm tra null, rỗng, hoặc chuỗi "null"
                    String tenSanPhamRaw = sp.getTenSanPham();
                    String tenSanPham = (tenSanPhamRaw != null && !tenSanPhamRaw.trim().isEmpty() && !"null".equalsIgnoreCase(tenSanPhamRaw.trim()))
                        ? tenSanPhamRaw.trim()
                        : "Chưa có tên";
                    
                    // Xử lý mã sản phẩm
                    String maSanPham = (sp.getMaSanPham() != null && !sp.getMaSanPham().trim().isEmpty() && !"null".equalsIgnoreCase(sp.getMaSanPham().trim()))
                        ? sp.getMaSanPham().trim()
                        : "N/A";
                    
                    // Xử lý số đăng ký
                    String soDangKyRaw = sp.getSoDangKy();
                    String soDangKy = (soDangKyRaw != null && !soDangKyRaw.trim().isEmpty() && !"null".equalsIgnoreCase(soDangKyRaw.trim()))
                        ? " - " + soDangKyRaw.trim()
                        : "";
                    
                    return tenSanPham + " (Mã: " + maSanPham + soDangKy + ")";
                })
                .collect(Collectors.joining("\n"));
            txtDanhSachSanPham.setText(danhSach);
        }
    }
    

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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
        lblChonSanPham = new javax.swing.JLabel();
        txtQuetMa = new javax.swing.JTextField();
        lblDanhSachSanPham = new javax.swing.JLabel();
        scrollDanhSachSanPham = new javax.swing.JScrollPane();
        txtDanhSachSanPham = new javax.swing.JTextArea();
        lblSoLuongToiDa = new javax.swing.JLabel();
        txtSoLuongToiDa = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnHuy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Xem chi tiết khuyến mãi");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(115, 165, 71));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CHI TIẾT KHUYẾN MÃI");

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

        lblChonSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblChonSanPham.setText("Quét mã sản phẩm:");

        txtQuetMa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtQuetMa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mã sản phẩm hoặc số đăng ký");

        lblDanhSachSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDanhSachSanPham.setText("Danh sách sản phẩm đã chọn:");

        txtDanhSachSanPham.setColumns(20);
        txtDanhSachSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDanhSachSanPham.setRows(3);
        scrollDanhSachSanPham.setViewportView(txtDanhSachSanPham);

        lblSoLuongToiDa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuongToiDa.setText("Số lượng tối đa:");

        txtSoLuongToiDa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(lblGiaToiThieu)
                    .addComponent(lblGiaToiDa)
                    .addComponent(lblChonSanPham)
                    .addComponent(lblDanhSachSanPham)
                    .addComponent(lblSoLuongToiDa))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenKhuyenMai)
                    .addComponent(dateNgayBatDau, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateNgayKetThuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGiamGia)
                    .addComponent(cboLoaiKhuyenMai, 0, 350, Short.MAX_VALUE)
                    .addComponent(txtGiaToiThieu)
                    .addComponent(txtGiaToiDa)
                    .addComponent(txtQuetMa)
                    .addComponent(scrollDanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(txtSoLuongToiDa)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
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
                    .addComponent(lblChonSanPham)
                    .addComponent(txtQuetMa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDanhSachSanPham)
                    .addComponent(scrollDanhSachSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoLuongToiDa)
                    .addComponent(txtSoLuongToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 30, 20, 30));

        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuy.setText("Đóng");
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
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
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
    private javax.swing.JTextField txtQuetMa;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblChonSanPham;
    private javax.swing.JLabel lblDanhSachSanPham;
    private javax.swing.JScrollPane scrollDanhSachSanPham;
    private javax.swing.JTextArea txtDanhSachSanPham;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtTenKhuyenMai;
    // End of variables declaration
}



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
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
    private List<SanPham> danhSachSanPhamDaChon = new ArrayList<>(); // Lưu danh sách sản phẩm đã chọn
    private LoaiKhuyenMai loaiKhuyenMaiBanDau; // Lưu loại khuyến mãi ban đầu
    private boolean daTungCoSanPham = false; // Đánh dấu khuyến mãi đã từng được gắn cho sản phẩm
    private boolean daDuocSuDungTrongDonHang = false; // Đánh dấu khuyến mãi đã được sử dụng trong đơn hàng
    private Timer timerQuetMa; // Timer để debounce khi quét mã
    
    // Components cho giá tối thiểu và tối đa (khuyến mãi đơn hàng)
    private javax.swing.JLabel lblGiaToiThieu;
    private javax.swing.JTextField txtGiaToiThieu;
    private javax.swing.JLabel lblGiaToiDa;
    private javax.swing.JTextField txtGiaToiDa;
    
    // Components cho số lượng tối đa (khuyến mãi sản phẩm)
    private javax.swing.JLabel lblSoLuongToiDa;
    private javax.swing.JTextField txtSoLuongToiDa;

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
        txtGiaToiThieu.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 100000");
        txtGiaToiDa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 50000");
        txtQuetMa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mã sản phẩm hoặc số đăng ký");
        txtSoLuongToiDa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 5");
        
        // Thiết lập date chooser
        dateNgayBatDau.setMinSelectableDate(new Date());
        dateNgayKetThuc.setMinSelectableDate(new Date());
        
        // Thiết lập combo box - Mặc định "Đơn hàng"
        cboLoaiKhuyenMai.setModel(new DefaultComboBoxModel<>(new String[]{"Đơn hàng", "Sản phẩm"}));
        
        // Disable mã khuyến mãi
        txtMaKhuyenMai.setEnabled(false);
        
        // Thiết lập JList cho danh sách sản phẩm
        listDanhSachSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDanhSachSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        // Thêm listener để cập nhật trạng thái nút Xóa khi chọn item
        listDanhSachSanPham.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                capNhatTrangThaiNutXoaTrang();
            }
        });
        
        // Thiết lập tự động quét mã khi nhập (với debounce)
        timerQuetMa = new Timer(500, e -> xuLyQuetMa()); // 500ms delay
        timerQuetMa.setRepeats(false); // Chỉ chạy một lần
        
        txtQuetMa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                kichHoatQuetMa();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                // Dừng timer khi xóa text để tránh quét với text rỗng
                if (timerQuetMa.isRunning()) {
                    timerQuetMa.stop();
                }
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                kichHoatQuetMa();
            }
            
            private void kichHoatQuetMa() {
                // Dừng timer cũ nếu có
                if (timerQuetMa.isRunning()) {
                    timerQuetMa.stop();
                }
                // Bắt đầu timer mới
                timerQuetMa.start();
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
            
            // Hiển thị chọn sản phẩm và số lượng tối đa cho khuyến mãi sản phẩm
            lblChonSanPham.setVisible(isProductPromotion);
            txtQuetMa.setVisible(isProductPromotion);
            lblDanhSachSanPham.setVisible(isProductPromotion);
            scrollDanhSachSanPham.setVisible(isProductPromotion);
            // Các nút xóa chỉ hiện khi quét mã xong, không phụ thuộc vào loại khuyến mãi
            // Logic hiển thị được xử lý trong capNhatDanhSachSanPham()
            lblSoLuongToiDa.setVisible(isProductPromotion);
            txtSoLuongToiDa.setVisible(isProductPromotion);
            
            // Cập nhật trạng thái nút xóa trắng
            capNhatTrangThaiNutXoaTrang();
            
            // Resize dialog
            pack();
        });

        ButtonStyles.apply(btnCapNhat, ButtonStyles.Type.WARNING);
        ButtonStyles.apply(btnHuy, ButtonStyles.Type.SECONDARY);
        ButtonStyles.apply(btnXoaSanPham, ButtonStyles.Type.DANGER);
        ButtonStyles.apply(btnXoaTrang, ButtonStyles.Type.WARNING);
    }
    
    /**
     * Xử lý quét mã sản phẩm
     */
    private void xuLyQuetMa() {
        String maQuet = txtQuetMa.getText().trim();
        if (maQuet.isEmpty()) {
            return;
        }
        
        try {
            // Tìm sản phẩm theo mã trước
            SanPham sp = null;
            if (sanPhamBUS.timSanPhamTheoMa(maQuet).isPresent()) {
                sp = sanPhamBUS.timSanPhamTheoMa(maQuet).get();
            } else if (sanPhamBUS.timSanPhamTheoSoDangKy(maQuet).isPresent()) {
                // Nếu không tìm thấy theo mã, tìm theo số đăng ký
                sp = sanPhamBUS.timSanPhamTheoSoDangKy(maQuet).get();
            }
            
            if (sp == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    "Không tìm thấy sản phẩm với mã/số đăng ký: " + maQuet);
                txtQuetMa.setText("");
                txtQuetMa.requestFocus();
                return;
            }
            
            // Lưu biến final để dùng trong lambda
            final SanPham sanPhamTimThay = sp;
            
            // Kiểm tra xem sản phẩm đã được thêm chưa
            boolean daCo = danhSachSanPhamDaChon.stream()
                .anyMatch(spDaChon -> spDaChon.getMaSanPham().equals(sanPhamTimThay.getMaSanPham()));
            
            if (daCo) {
                Notifications.getInstance().show(Notifications.Type.INFO, 
                    "Sản phẩm " + sanPhamTimThay.getTenSanPham() + " đã có trong danh sách!");
            } else {
                danhSachSanPhamDaChon.add(sanPhamTimThay);
                capNhatDanhSachSanPham();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                    "Đã thêm sản phẩm: " + sanPhamTimThay.getTenSanPham());
            }
            
            // Xóa text và focus lại để quét tiếp
            txtQuetMa.setText("");
            txtQuetMa.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                "Lỗi khi quét mã: " + e.getMessage());
        }
    }
    
    /**
     * Cập nhật hiển thị danh sách sản phẩm đã chọn
     */
    private void capNhatDanhSachSanPham() {
        DefaultListModel<String> model = new DefaultListModel<>();
        if (danhSachSanPhamDaChon.isEmpty()) {
            model.addElement("Chưa quét sản phẩm nào");
            listDanhSachSanPham.setEnabled(false);
            // Ẩn các nút xóa khi chưa có sản phẩm
            btnXoaSanPham.setVisible(false);
            btnXoaTrang.setVisible(false);
        } else {
            for (SanPham sp : danhSachSanPhamDaChon) {
                String soDangKy = sp.getSoDangKy() != null ? " - " + sp.getSoDangKy() : "";
                model.addElement(sp.getTenSanPham() + " (" + sp.getMaSanPham() + soDangKy + ")");
            }
            listDanhSachSanPham.setEnabled(true);
            // Hiện các nút xóa khi đã quét mã xong (có sản phẩm)
            btnXoaSanPham.setVisible(true);
            btnXoaTrang.setVisible(true);
        }
        listDanhSachSanPham.setModel(model);
        capNhatTrangThaiNutXoaTrang();
    }
    
    /**
     * Cập nhật trạng thái nút xóa trắng (hiển thị khi có sản phẩm)
     */
    private void capNhatTrangThaiNutXoaTrang() {
        boolean coSanPham = !danhSachSanPhamDaChon.isEmpty();
        btnXoaTrang.setEnabled(coSanPham);
        btnXoaSanPham.setEnabled(coSanPham && listDanhSachSanPham.getSelectedIndex() >= 0);
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
            
            // Load số lượng tối đa
            txtSoLuongToiDa.setText(String.valueOf(khuyenMai.getSoLuongToiDa()));
            
            // Kiểm tra xem khuyến mãi đã được gắn cho sản phẩm chưa
            try {
                List<ChiTietKhuyenMaiSanPham> chiTietList = chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(khuyenMai.getMaKhuyenMai());
                
                if (!chiTietList.isEmpty()) {
                    daTungCoSanPham = true;
                    // Load tất cả sản phẩm vào danh sách
                    danhSachSanPhamDaChon.clear();
                    for (ChiTietKhuyenMaiSanPham chiTiet : chiTietList) {
                    SanPham spPartial = chiTiet.getSanPham();
                    if (spPartial != null && spPartial.getMaSanPham() != null) {
                        // Tìm sản phẩm đầy đủ
                            SanPham sanPhamDayDu = sanPhamBUS.laySanPhamTheoMa(spPartial.getMaSanPham());
                        if (sanPhamDayDu != null) {
                                danhSachSanPhamDaChon.add(sanPhamDayDu);
                            }
                        }
                    }
                    capNhatDanhSachSanPham();
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
            
            lblChonSanPham.setVisible(isProductPromotion);
            txtQuetMa.setVisible(isProductPromotion);
            lblDanhSachSanPham.setVisible(isProductPromotion);
            scrollDanhSachSanPham.setVisible(isProductPromotion);
            lblSoLuongToiDa.setVisible(isProductPromotion);
            txtSoLuongToiDa.setVisible(isProductPromotion);
            
            // Nếu khuyến mãi đã được sử dụng trong đơn hàng, disable các field và hiển thị thông báo
            if (daDuocSuDungTrongDonHang) {
                txtTenKhuyenMai.setEnabled(false);
                dateNgayBatDau.setEnabled(false);
                dateNgayKetThuc.setEnabled(false);
                txtGiamGia.setEnabled(false);
                cboLoaiKhuyenMai.setEnabled(false);
                txtQuetMa.setEnabled(false);
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
        lblChonSanPham = new javax.swing.JLabel();
        txtQuetMa = new javax.swing.JTextField();
        lblDanhSachSanPham = new javax.swing.JLabel();
        scrollDanhSachSanPham = new javax.swing.JScrollPane();
        listDanhSachSanPham = new javax.swing.JList<>();
        btnXoaSanPham = new javax.swing.JButton();
        btnXoaTrang = new javax.swing.JButton();
        lblSoLuongToiDa = new javax.swing.JLabel();
        txtSoLuongToiDa = new javax.swing.JTextField();
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

        lblChonSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblChonSanPham.setText("Quét mã sản phẩm:");

        txtQuetMa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblDanhSachSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDanhSachSanPham.setText("Danh sách sản phẩm đã chọn:");

        listDanhSachSanPham.setModel(new javax.swing.DefaultListModel<>());
        scrollDanhSachSanPham.setViewportView(listDanhSachSanPham);
        
        btnXoaSanPham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoaSanPham.setText("Xóa");
        btnXoaSanPham.setPreferredSize(new java.awt.Dimension(120, 40));
        btnXoaSanPham.setVisible(false);
        btnXoaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSanPhamActionPerformed(evt);
            }
        });
        
        btnXoaTrang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoaTrang.setText("Xóa trắng");
        btnXoaTrang.setPreferredSize(new java.awt.Dimension(120, 40));
        btnXoaTrang.setVisible(false);
        btnXoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTrangActionPerformed(evt);
            }
        });

        lblSoLuongToiDa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuongToiDa.setText("Số lượng tối đa:");

        txtSoLuongToiDa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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
                    .addComponent(lblChonSanPham)
                    .addComponent(lblDanhSachSanPham)
                    .addComponent(lblSoLuongToiDa))
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
                    .addComponent(txtQuetMa)
                    .addComponent(scrollDanhSachSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(txtSoLuongToiDa)))
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

        btnCapNhat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setPreferredSize(new java.awt.Dimension(120, 40));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnHuy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
                .addContainerGap()
                .addComponent(btnXoaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(btnXoaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoaTrang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnXoaSanPhamActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = listDanhSachSanPham.getSelectedIndex();
        if (selectedIndex < 0 || danhSachSanPhamDaChon.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Hãy chọn sản phẩm cần xóa!");
            return;
        }
        
        // Xóa sản phẩm khỏi danh sách
        SanPham spXoa = danhSachSanPhamDaChon.remove(selectedIndex);
        capNhatDanhSachSanPham();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, 
            "Đã xóa sản phẩm: " + spXoa.getTenSanPham());
    }

    private void btnXoaTrangActionPerformed(java.awt.event.ActionEvent evt) {
        if (danhSachSanPhamDaChon.isEmpty()) {
            return;
        }
        
        int soLuong = danhSachSanPhamDaChon.size();
        danhSachSanPhamDaChon.clear();
        capNhatDanhSachSanPham();
        Notifications.getInstance().show(Notifications.Type.SUCCESS, 
            "Đã xóa " + soLuong + " sản phẩm khỏi danh sách!");
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
            
            // Nếu là khuyến mãi sản phẩm, kiểm tra đã chọn sản phẩm chưa
            if (loaiKM.equals("Sản phẩm")) {
                if (danhSachSanPhamDaChon.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng quét ít nhất 1 sản phẩm!");
                    txtQuetMa.requestFocus();
                    return;
                }
                
                // Kiểm tra conflict khuyến mãi cho từng sản phẩm (loại trừ khuyến mãi hiện tại)
                List<String> danhSachSanPhamConflict = new ArrayList<>();
                for (SanPham sp : danhSachSanPhamDaChon) {
                    KhuyenMai khuyenMaiConflict = chiTietKhuyenMaiSanPhamBUS.kiemTraConflictKhuyenMai(
                        sp.getMaSanPham(), ngayBatDau, ngayKetThuc, khuyenMai.getMaKhuyenMai());
                    if (khuyenMaiConflict != null) {
                        danhSachSanPhamConflict.add(sp.getTenSanPham() + " (đang có khuyến mãi: " + 
                            khuyenMaiConflict.getTenKhuyenMai() + " từ " + 
                            khuyenMaiConflict.getNgayBatDau() + " đến " + 
                            khuyenMaiConflict.getNgayKetThuc() + ")");
                    }
                }
                
                if (!danhSachSanPhamConflict.isEmpty()) {
                    StringBuilder message = new StringBuilder("Không thể cập nhật khuyến mãi! Các sản phẩm sau đang có khuyến mãi đang hoạt động hoặc trùng thời gian:\n");
                    for (String spConflict : danhSachSanPhamConflict) {
                        message.append("- ").append(spConflict).append("\n");
                    }
                    message.append("\nVui lòng xóa khuyến mãi cũ hoặc đợi hết hạn trước khi cập nhật khuyến mãi!");
                    Notifications.getInstance().show(Notifications.Type.ERROR, message.toString());
                    return;
                }
                
                // Lấy số lượng tối đa
                String soLuongToiDaStr = txtSoLuongToiDa.getText().trim();
                if (!soLuongToiDaStr.isEmpty()) {
                    try {
                        int soLuongToiDa = Integer.parseInt(soLuongToiDaStr);
                        if (soLuongToiDa < 0) {
                            Notifications.getInstance().show(Notifications.Type.WARNING, "Số lượng tối đa phải >= 0!");
                            txtSoLuongToiDa.requestFocus();
                            return;
                        }
                        khuyenMai.setSoLuongToiDa(soLuongToiDa);
                    } catch (NumberFormatException e) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, "Số lượng tối đa phải là số nguyên!");
                        txtSoLuongToiDa.requestFocus();
                    return;
                    }
                } else {
                    khuyenMai.setSoLuongToiDa(0); // Mặc định không giới hạn số lượng tối đa
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
                if (loaiKM.equals("Sản phẩm") && !danhSachSanPhamDaChon.isEmpty()) {
                    try {
                        // Xóa chi tiết cũ
                        List<ChiTietKhuyenMaiSanPham> chiTietCu = chiTietKhuyenMaiSanPhamBUS.timTheoMaKhuyenMai(khuyenMai.getMaKhuyenMai());
                        for (ChiTietKhuyenMaiSanPham ct : chiTietCu) {
                            chiTietKhuyenMaiSanPhamBUS.xoaChiTietKhuyenMaiSanPham(ct.getSanPham().getMaSanPham(), khuyenMai.getMaKhuyenMai());
                        }
                        
                        // Thêm chi tiết mới cho tất cả sản phẩm đã chọn
                        int soLuongThanhCong = 0;
                        int soLuongThatBai = 0;
                        
                        for (SanPham sp : danhSachSanPhamDaChon) {
                            try {
                        ChiTietKhuyenMaiSanPham ctkmsp = new ChiTietKhuyenMaiSanPham();
                                ctkmsp.setSanPham(sp);
                        ctkmsp.setKhuyenMai(khuyenMai);
                        
                        boolean successDetail = chiTietKhuyenMaiSanPhamBUS.taoChiTietKhuyenMaiSanPham(ctkmsp);
                                if (successDetail) {
                                    soLuongThanhCong++;
                                } else {
                                    soLuongThatBai++;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                soLuongThatBai++;
                            }
                        }
                        
                        if (soLuongThatBai > 0) {
                            Notifications.getInstance().show(Notifications.Type.WARNING, 
                                "Cập nhật khuyến mãi thành công! " + soLuongThanhCong + " sản phẩm được liên kết, " + 
                                soLuongThatBai + " sản phẩm không thể liên kết!");
                        } else {
                            Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                                "Cập nhật khuyến mãi thành công! Đã liên kết với " + soLuongThanhCong + " sản phẩm!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Notifications.getInstance().show(Notifications.Type.WARNING, 
                            "Cập nhật khuyến mãi thành công nhưng lỗi khi liên kết sản phẩm: " + e.getMessage());
                    }
                } else {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật khuyến mãi thành công!");
                }
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
    private javax.swing.JLabel lblChonSanPham;
    private javax.swing.JLabel lblDanhSachSanPham;
    private javax.swing.JScrollPane scrollDanhSachSanPham;
    private javax.swing.JList<String> listDanhSachSanPham;
    private javax.swing.JButton btnXoaSanPham;
    private javax.swing.JButton btnXoaTrang;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtMaKhuyenMai;
    private javax.swing.JTextField txtQuetMa;
    private javax.swing.JTextField txtTenKhuyenMai;
    // End of variables declaration    
}

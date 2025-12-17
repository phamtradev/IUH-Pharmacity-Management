/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonViTinhBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import com.toedter.calendar.JDateChooser;
import raven.toast.Notifications;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPhamNhap extends javax.swing.JPanel {

    public static final int[] COLUMN_MIN_WIDTHS = {110, 260, 200, 160, 120, 140, 140, 90, 120, 90, 120, 150, 90};
    public static final double[] COLUMN_WEIGHTS = {0.06, 0.22, 0.09, 0.07, 0.04, 0.08, 0.08, 0.05, 0.08, 0.05, 0.08, 0.08, 0.02};

    private SanPham sanPham;
    private DecimalFormat currencyFormat;
    private SimpleDateFormat dateFormat;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JButton btnChonLo;
    private javax.swing.JPanel pnLo; // Panel chứa button chọn lô HOẶC thẻ lô

    // Các label cho thẻ lô (hiển thị sau khi chọn)
    private javax.swing.JPanel pnTheLo;
    private javax.swing.JLabel lblTheLo_TenLo;
    private javax.swing.JLabel lblTheLo_HSD;
    private javax.swing.JLabel lblTheLo_TonKho;

    private double cachedTongTien = 0; // Tổng sau thuế
    private double cachedTongTienGoc = 0; // Tổng trước chiết khấu
    private double cachedTienChietKhau = 0;
    private double cachedThanhTienTinhThue = 0;
    private double cachedTienThue = 0;
    private double tyLeChietKhau = 0;
    private double thueGTGT = 0;
    private LoHangBUS loHangBUS;
    private SanPhamDAO sanPhamDAO;
    private vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonNhapHangDAO chiTietDonNhapHangDAO;
    private List<LoHang> danhSachLoHang;
    private LoHang loHangDaChon = null;
    private String tenLoHangTuExcel = null; // Lưu tên lô từ Excel
    // Giữ lại cho future use khi cần sử dụng SĐT NCC từ Excel
    @SuppressWarnings("unused")
    private String soDienThoaiNCCTuExcel = null;

    // Thông tin lô mới sẽ tạo
    private String tenLoMoi = null;
    private Date hsdLoMoi = null;
    private int soLuongLoMoi = 1;

    // Dữ liệu từ Excel để tự động điền vào form tạo lô mới
    private Date hsdTuExcel = null;
    private Integer soLuongTuExcel = null;
    private Double donGiaTuExcel = null; // Lưu đơn giá từ Excel
    private String donGiaRawValue = "0";

    private DonViTinhBUS donViTinhBUS;
    private List<DonViTinh> danhSachDonViTinh;
    private DonViTinh donViTinhTuExcel;
    private boolean dangDongBoDonViTinh = false;

    private javax.swing.JLabel lblTyLeChietKhau;
    private javax.swing.JLabel lblTienChietKhau;
    private javax.swing.JLabel lblThueSuat;
    private javax.swing.JLabel lblTienThue;
    private javax.swing.JLabel lblThanhTienSauThue;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JComboBox<DonViTinh> cboDonViTinh;

    public Panel_ChiTietSanPhamNhap() {
        this.currencyFormat = initCurrencyFormat();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.sanPhamDAO = new SanPhamDAO();
        this.chiTietDonNhapHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonNhapHangDAO();
        this.thueGTGT = sanPham != null ? sanPham.getThueVAT() * 100 : 0;
        this.donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
        this.danhSachDonViTinh = new ArrayList<>();
        this.donViTinhTuExcel = null;
        initComponents();
        loadDanhSachDonViTinh();
    }

    private void initComponents() {

        spinnerSoLuong = new javax.swing.JSpinner();
        txtDonGia = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JLabel();
        lblTyLeChietKhau = new javax.swing.JLabel();
        lblTienChietKhau = new javax.swing.JLabel();
        lblThueSuat = new javax.swing.JLabel();
        lblTienThue = new javax.swing.JLabel();
        lblThanhTienSauThue = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(200, 200, 200)));
        setMaximumSize(new java.awt.Dimension(32767, 120));
        setMinimumSize(new java.awt.Dimension(800, 120));
        setPreferredSize(new java.awt.Dimension(1000, 120));
        setRequestFocusEnabled(false);

        // Sử dụng GridBagLayout để các cột thẳng hàng
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagLayout gridBagLayout = (java.awt.GridBagLayout) getLayout();
        gridBagLayout.columnWidths = COLUMN_MIN_WIDTHS.clone();
        gridBagLayout.columnWeights = COLUMN_WEIGHTS.clone();

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(12, 6, 12, 6);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh sản phẩm
        lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[0], 100));
        lblHinh.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[0], 100));
        lblHinh.setMaximumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[0], 100));
        lblHinh.setBackground(new java.awt.Color(240, 240, 240));
        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        lblHinh.setOpaque(true);
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("");
        gbc.gridx = 0;
        gbc.weightx = COLUMN_WEIGHTS[0];
        add(lblHinh, gbc);

        // 2. Tên sản phẩm
        lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSP.setText("");
        lblTenSP.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[1], 100));
        lblTenSP.setMinimumSize(new java.awt.Dimension(200, 100));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblTenSP.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 12));
        gbc.gridx = 1;
        gbc.weightx = COLUMN_WEIGHTS[1];
        add(lblTenSP, gbc);

        // 3. Cột "Lô hàng" - Ban đầu hiển thị nút "Chọn lô"
        pnLo = new javax.swing.JPanel();
        pnLo.setBackground(java.awt.Color.WHITE);
        pnLo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));
        pnLo.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[2], 100));
        pnLo.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[2], 100));

        // Luôn tạo nút "Chọn lô" ban đầu
        btnChonLo = new javax.swing.JButton("Chọn lô");
        btnChonLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
        btnChonLo.setPreferredSize(new java.awt.Dimension(120, 40));
        ButtonStyles.apply(btnChonLo, ButtonStyles.Type.PRIMARY);
        btnChonLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChonLo.addActionListener(evt -> showDialogChonLo(false));

        pnLo.add(btnChonLo);

        gbc.gridx = 2;
        gbc.weightx = COLUMN_WEIGHTS[2];
        add(pnLo, gbc);

        // 4. Số lượng (nhập trực tiếp)
        javax.swing.JPanel pnSoLuong = new javax.swing.JPanel();
        pnSoLuong.setBackground(java.awt.Color.WHITE);
        pnSoLuong.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 28));

        txtSoLuong = new javax.swing.JTextField("1");
        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 18));
        txtSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSoLuong.setPreferredSize(new java.awt.Dimension(90, 44));
        txtSoLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));
        txtSoLuong.setToolTipText("Nhập số lượng (1 - 10.000)");
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && !Character.isISOControl(c)) {
                    evt.consume();
                }
            }
        });
        txtSoLuong.addActionListener(evt -> commitSoLuongTextInput());
        txtSoLuong.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                commitSoLuongTextInput();
            }
        });

        // Cập nhật spinner để đồng bộ với label (ẩn spinner)
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10000, 1));
        spinnerSoLuong.setVisible(false); // Ẩn spinner, chỉ dùng để lưu giá trị

        // Listener để cập nhật textfield khi spinner thay đổi
        spinnerSoLuong.addChangeListener(evt -> updateSoLuongDisplay());

        pnSoLuong.add(txtSoLuong);
        pnSoLuong.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[3], 100));
        pnSoLuong.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[3], 100));
        gbc.gridx = 3;
        gbc.weightx = COLUMN_WEIGHTS[3];
        add(pnSoLuong, gbc);

        // 5. Đơn vị tính
        javax.swing.JPanel pnDonViTinh = new javax.swing.JPanel();
        pnDonViTinh.setBackground(java.awt.Color.WHITE);
        pnDonViTinh.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 28));

        cboDonViTinh = new javax.swing.JComboBox<>();
        cboDonViTinh.setFont(new java.awt.Font("Segoe UI", 0, 14));
        cboDonViTinh.setPreferredSize(new java.awt.Dimension(110, 36));
        cboDonViTinh.setFocusable(false);
        cboDonViTinh.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DonViTinh) {
                    DonViTinh donViTinh = (DonViTinh) value;
                    setText(donViTinh.getTenDonVi());
                } else if (value == null) {
                    setText("Không có");
                }
                return this;
            }
        });
        cboDonViTinh.addActionListener(evt -> onDonViTinhChanged());

        pnDonViTinh.add(cboDonViTinh);
        pnDonViTinh.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[4], 100));
        pnDonViTinh.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[4], 100));
        gbc.gridx = 4;
        gbc.weightx = COLUMN_WEIGHTS[4];
        add(pnDonViTinh, gbc);

        // 6. Đơn giá nhập (có thể chỉnh sửa)
        txtDonGia = new javax.swing.JLabel();
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDonGia.setText(formatDonGiaDisplay(donGiaRawValue) + " đ");
        txtDonGia.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[5], 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[5], 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[5], 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        txtDonGia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtDonGia.setToolTipText("Click để chỉnh sửa đơn giá");
        txtDonGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDonGiaMouseClicked(evt);
            }
        });
        gbc.gridx = 5;
        gbc.weightx = COLUMN_WEIGHTS[5];
        add(txtDonGia, gbc);

        // 7. Tổng tiền
        txtTongTien = new javax.swing.JLabel();
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 16));
        txtTongTien.setForeground(new java.awt.Color(0, 120, 215));
        txtTongTien.setText("0 đ");
        txtTongTien.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[6], 100));
        txtTongTien.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[6], 100));
        txtTongTien.setMaximumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[6], 100));
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTongTien.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        txtTongTien.setToolTipText("Tổng tiền trước chiết khấu");
        gbc.gridx = 6;
        gbc.weightx = COLUMN_WEIGHTS[6];
        add(txtTongTien, gbc);

        // 8. Chiết khấu (%)
        lblTyLeChietKhau.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTyLeChietKhau.setForeground(new java.awt.Color(220, 53, 69));
        lblTyLeChietKhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTyLeChietKhau.setText("0 %");
        lblTyLeChietKhau.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblTyLeChietKhau.setToolTipText("Click để chỉnh phần trăm chiết khấu");
        lblTyLeChietKhau.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                capNhatTyLeChietKhau();
            }
        });
        lblTyLeChietKhau.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[7], 100));
        gbc.gridx = 7;
        gbc.weightx = COLUMN_WEIGHTS[7];
        add(lblTyLeChietKhau, gbc);

        // 9. Chiết khấu (đ)
        lblTienChietKhau.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTienChietKhau.setForeground(new java.awt.Color(220, 53, 69));
        lblTienChietKhau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTienChietKhau.setText("-0 đ");
        lblTienChietKhau.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[8], 100));
        gbc.gridx = 8;
        gbc.weightx = COLUMN_WEIGHTS[8];
        add(lblTienChietKhau, gbc);

        // 10. Thuế (%)
        lblThueSuat.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblThueSuat.setForeground(new java.awt.Color(0, 123, 255));
        lblThueSuat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThueSuat.setText("0 %");
        lblThueSuat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblThueSuat.setToolTipText("Click để chỉnh thuế GTGT");
        lblThueSuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                capNhatThueGTGT();
            }
        });
        lblThueSuat.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[9], 100));
        gbc.gridx = 9;
        gbc.weightx = COLUMN_WEIGHTS[9];
        add(lblThueSuat, gbc);

        // 11. Thuế (đ)
        lblTienThue.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTienThue.setForeground(new java.awt.Color(0, 123, 255));
        lblTienThue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTienThue.setText("+0 đ");
        lblTienThue.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[10], 100));
        gbc.gridx = 10;
        gbc.weightx = COLUMN_WEIGHTS[10];
        add(lblTienThue, gbc);

        // 12. Thành tiền
        lblThanhTienSauThue.setFont(new java.awt.Font("Segoe UI", 1, 15));
        lblThanhTienSauThue.setForeground(new java.awt.Color(34, 139, 34));
        lblThanhTienSauThue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThanhTienSauThue.setText("0 đ");
        lblThanhTienSauThue.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[11], 100));
        gbc.gridx = 11;
        gbc.weightx = COLUMN_WEIGHTS[11];
        add(lblThanhTienSauThue, gbc);

        // 13. Nút Xóa
        javax.swing.JPanel pnXoa = new javax.swing.JPanel();
        pnXoa.setBackground(java.awt.Color.WHITE);
        pnXoa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 32));
        javax.swing.JButton btnXoa = new javax.swing.JButton();
        btnXoa.setText("Xóa");
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14));
        ButtonStyles.apply(btnXoa, ButtonStyles.Type.DANGER);
        btnXoa.setPreferredSize(new java.awt.Dimension(70, 45));
        btnXoa.setMinimumSize(new java.awt.Dimension(70, 45));
        btnXoa.setMaximumSize(new java.awt.Dimension(70, 45));
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnXoa.add(btnXoa);
        pnXoa.setPreferredSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[12], 100));
        pnXoa.setMinimumSize(new java.awt.Dimension(COLUMN_MIN_WIDTHS[12], 100));
        gbc.gridx = 12;
        gbc.weightx = COLUMN_WEIGHTS[12];
        add(pnXoa, gbc);
    }// </editor-fold>//GEN-END:initComponents

    //Phần Logic code
    
    /**
     * Constructor khi KHÔNG có thông tin lô từ Excel Hiển thị Spinner HSD +
     * Button "Chọn lô"
     */
    public Panel_ChiTietSanPhamNhap(SanPham sanPham) {
        this.sanPham = sanPham;
        this.currencyFormat = initCurrencyFormat();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.sanPhamDAO = new SanPhamDAO();
        this.chiTietDonNhapHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonNhapHangDAO();
        this.tenLoHangTuExcel = null; // Không có lô từ Excel
        this.thueGTGT = sanPham != null ? sanPham.getThueVAT() * 100 : 0;
        this.donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
        this.danhSachDonViTinh = new ArrayList<>();
        this.donViTinhTuExcel = null;
        initComponents();
        loadDanhSachDonViTinh();
        loadSanPhamData();
        loadLoHangData();
    }

    /**
     * Constructor khi CÓ thông tin lô từ Excel Tự động chọn lô nếu có cùng Số
     * ĐK + HSD + SĐT NCC khớp, nếu không → báo lỗi
     */
    public Panel_ChiTietSanPhamNhap(SanPham sanPham, int soLuong, double donGiaNhap, Date hanDung, String tenLoHang,
            String soDienThoaiNCC, Double tyLeChietKhauExcel, Double thueGTGTExcel, DonViTinh donViTinhTuExcel) throws Exception {
        this.sanPham = sanPham;
        this.currencyFormat = initCurrencyFormat();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.sanPhamDAO = new SanPhamDAO();
        this.chiTietDonNhapHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietDonNhapHangDAO();
        this.tenLoHangTuExcel = tenLoHang;
        this.soDienThoaiNCCTuExcel = soDienThoaiNCC;
        this.thueGTGT = sanPham != null ? sanPham.getThueVAT() * 100 : 0;
        this.donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
        this.danhSachDonViTinh = new ArrayList<>();
        this.donViTinhTuExcel = donViTinhTuExcel;

        if (tyLeChietKhauExcel != null) {
            this.tyLeChietKhau = tyLeChietKhauExcel;
        }
        if (thueGTGTExcel != null) {
            this.thueGTGT = thueGTGTExcel;
        }

        this.hsdTuExcel = hanDung;
        this.soLuongTuExcel = soLuong;
        this.donGiaTuExcel = donGiaNhap;
        if (donViTinhTuExcel != null && this.sanPham != null) {
            this.sanPham.setDonViTinh(donViTinhTuExcel);
        }

        try {
            initComponents();
            loadDanhSachDonViTinh();
            loadSanPhamData();
            loadLoHangData();

            spinnerSoLuong.setValue(soLuong);
            setDonGiaFromDouble(donGiaNhap);

            LocalDate hsd = hanDung.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            if (soDienThoaiNCC != null && !soDienThoaiNCC.trim().isEmpty()) {
                List<String> danhSachSDT = sanPhamDAO.getSoDienThoaiNCCBySoDangKy(sanPham.getSoDangKy());

                if (!danhSachSDT.isEmpty() && !danhSachSDT.contains(soDienThoaiNCC.trim())) {
                    String errorMsg = "Sản phẩm '" + sanPham.getTenSanPham()
                            + "' có số đăng ký " + sanPham.getSoDangKy()
                            + " không thể nhập từ nhiều nhà cung cấp khác nhau";
                    throw new Exception(errorMsg);
                }
            }

            // Tìm lô phù hợp theo 3 điều kiện: SĐK + HSD + Giá nhập (KHÔNG phụ thuộc tên lô từ Excel)
            LoHang loPhuHop = null;
            try {
                vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO loHangDAO = new vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO();
                List<LoHang> dsLo = loHangDAO.findByMaSanPham(sanPham.getMaSanPham());
                String soDangKy = sanPham.getSoDangKy();

                System.out.println("===== DEBUG MATCH LÔ (Excel import) =====");
                System.out.println("Excel -> SDK=" + soDangKy + ", HSD=" + hsd + ", DonGia=" + donGiaNhap);

                for (LoHang lo : dsLo) {
                    if (lo.getSanPham() == null) {
                        continue;
                    }
                    // 1. Số đăng ký
                    if (soDangKy != null && !soDangKy.equalsIgnoreCase(lo.getSanPham().getSoDangKy())) {
                        continue;
                    }
                    // 2. Hạn sử dụng
                    if (lo.getHanSuDung() == null || !lo.getHanSuDung().equals(hsd)) {
                        continue;
                    }
                    // 3. Giá nhập (mỗi lô một giá)
                    boolean giaHopLe = isDonGiaHopLeChoLo(lo, donGiaNhap);
                    System.out.println("  >> Lô DB: " + lo.getMaLoHang()
                            + " | SDK=" + lo.getSanPham().getSoDangKy()
                            + " | HSD=" + lo.getHanSuDung()
                            + " | giaNhapLo=" + lo.getGiaNhapLo()
                            + " | giaExcel=" + donGiaNhap
                            + " | giaHopLe=" + giaHopLe);
                    if (!giaHopLe) {
                        continue; // Giá khác → không ghép
                    }

                    loPhuHop = lo;
                    break;
                }

                if (loPhuHop == null) {
                    System.out.println(">>> KHÔNG TÌM THẤY LÔ PHÙ HỢP (SDK + HSD + GIÁ)");
                } else {
                    System.out.println(">>> CHỌN LÔ: " + loPhuHop.getMaLoHang());
                }
                System.out.println("============================================");
            } catch (Exception ex) {
                System.err.println("[Panel_ChiTietSanPhamNhap] Lỗi khi tìm lô phù hợp: " + ex.getMessage());
            }

            if (loPhuHop != null) {
                loHangDaChon = loPhuHop;
                if (loHangDaChon.getSanPham() != null) {
                    this.sanPham = loHangDaChon.getSanPham();
                    loadSanPhamData();
                }
                // Clear thông tin lô mới nếu đã tìm thấy lô phù hợp
                tenLoMoi = null;
                hsdLoMoi = null;
                soLuongLoMoi = 1;
                updateLoInfo(); // Hiển thị thẻ lô
            } else {
                // Không tìm thấy lô trùng: Lưu thông tin từ Excel để người dùng nhập tên lô
                loHangDaChon = null;
                // Lưu thông tin lô mới từ Excel (chưa có tên lô, người dùng sẽ nhập sau)
                tenLoMoi = tenLoHangTuExcel; // Có thể null, người dùng sẽ nhập khi chọn "Tạo lô mới"
                hsdLoMoi = hsdTuExcel != null ? hsdTuExcel : hanDung;
                soLuongLoMoi = soLuongTuExcel != null ? soLuongTuExcel : 1;
                // Tự động mở dialog "Chọn lô" với tab "Tạo lô mới" để người dùng nhập tên lô
                updateLoInfo(); // Hiển thị nút "Chọn lô"
                // Tự động mở dialog sau khi initComponents xong
                javax.swing.SwingUtilities.invokeLater(() -> {
                    showDialogChonLoVoiTabTaoLoMoi();
                });
            }

            updateTongTien();
            syncDonViTinhSelection();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Cập nhật hiển thị: Nút "Chọn lô" → Thẻ lô đẹp
     */
    private void updateLoInfo() {
        pnLo.removeAll();

        if (loHangDaChon != null || (tenLoMoi != null && hsdLoMoi != null)) {
            pnTheLo = new javax.swing.JPanel();
            pnTheLo.setBackground(java.awt.Color.WHITE);
            pnTheLo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            // Chiều cao cố định cho tất cả lô (3 dòng: Tên + HSD + Tồn)
            pnTheLo.setPreferredSize(new java.awt.Dimension(160, 95));
            pnTheLo.setLayout(new javax.swing.BoxLayout(pnTheLo, javax.swing.BoxLayout.Y_AXIS));

            String tenLo, hsd;
            int tonKho = 0;

            if (loHangDaChon != null) {
                tenLo = loHangDaChon.getTenLoHang();
                hsd = loHangDaChon.getHanSuDung().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                tonKho = loHangDaChon.getTonKho();
            } else {
                tenLo = tenLoMoi != null ? tenLoMoi : "Chưa đặt tên";
                hsd = dateFormat.format(hsdLoMoi);
                tonKho = 0;
            }

            lblTheLo_TenLo = new javax.swing.JLabel("Lô: " + tenLo);
            lblTheLo_TenLo.setFont(new java.awt.Font("Segoe UI", 1, 13));
            lblTheLo_TenLo.setForeground(new java.awt.Color(51, 51, 51));
            lblTheLo_TenLo.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_TenLo.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));

            lblTheLo_HSD = new javax.swing.JLabel("HSD: " + hsd);
            lblTheLo_HSD.setFont(new java.awt.Font("Segoe UI", 0, 12));
            lblTheLo_HSD.setForeground(new java.awt.Color(102, 102, 102));
            lblTheLo_HSD.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_HSD.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8)); // Tăng padding bottom

            pnTheLo.add(lblTheLo_TenLo);
            pnTheLo.add(lblTheLo_HSD);

            lblTheLo_TonKho = new javax.swing.JLabel("Tồn: " + tonKho);
            lblTheLo_TonKho.setFont(new java.awt.Font("Segoe UI", 0, 12));
            lblTheLo_TonKho.setForeground(new java.awt.Color(34, 139, 34));
            lblTheLo_TonKho.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_TonKho.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8));
            pnTheLo.add(lblTheLo_TonKho);

            pnLo.add(pnTheLo);
        } else {
            btnChonLo = new javax.swing.JButton("Chọn lô");
            btnChonLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
            btnChonLo.setPreferredSize(new java.awt.Dimension(120, 40));
            ButtonStyles.apply(btnChonLo, ButtonStyles.Type.PRIMARY);
            btnChonLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btnChonLo.addActionListener(evt -> showDialogChonLo(false));

            pnLo.add(btnChonLo);
        }

        pnLo.revalidate();
        pnLo.repaint();
    }

    private void loadSanPhamData() {
        if (sanPham != null) {
            lblTenSP.setText(sanPham.getTenSanPham());

            if (donGiaTuExcel != null && donGiaTuExcel > 0) {
                setDonGiaFromDouble(donGiaTuExcel);
            } else {
                setDonGiaRawValue("0");
            }

            updateTongTien();

            if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
                try {
                    ImageIcon icon = null;
                    String hinhAnh = sanPham.getHinhAnh().trim();

                    java.io.File imageFile = new java.io.File(hinhAnh);
                    if (imageFile.exists() && imageFile.isFile()) {
                        icon = new ImageIcon(hinhAnh);
                    } else {
                        java.net.URL imgURL = getClass().getResource("/img/" + hinhAnh);
                        if (imgURL != null) {
                            icon = new ImageIcon(imgURL);
                        } else {
                            String imagePath = "src/main/resources/img/" + hinhAnh;
                            java.io.File fileSystemImage = new java.io.File(imagePath);
                            if (fileSystemImage.exists() && fileSystemImage.isFile()) {
                                icon = new ImageIcon(imagePath);
                            } else {
                                String projectRoot = System.getProperty("user.dir");
                                String absoluteImagePath = projectRoot + java.io.File.separator + "src"
                                        + java.io.File.separator + "main" + java.io.File.separator + "resources"
                                        + java.io.File.separator + "img" + java.io.File.separator + hinhAnh;
                                java.io.File absoluteFile = new java.io.File(absoluteImagePath);
                                if (absoluteFile.exists() && absoluteFile.isFile()) {
                                    icon = new ImageIcon(absoluteImagePath);
                                } else {
                                    String targetImagePath = projectRoot + java.io.File.separator + "target"
                                            + java.io.File.separator + "classes" + java.io.File.separator + "img"
                                            + java.io.File.separator + hinhAnh;
                                    java.io.File targetFile = new java.io.File(targetImagePath);
                                    if (targetFile.exists() && targetFile.isFile()) {
                                        icon = new ImageIcon(targetImagePath);
                                    }
                                }
                            }
                        }
                    }

                    if (icon != null && icon.getIconWidth() > 0) {
                        java.awt.Image img = icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                        lblHinh.setIcon(new ImageIcon(img));
                        lblHinh.setText("");
                    } else {
                        lblHinh.setText("IMG");
                    }
                } catch (Exception e) {
                    lblHinh.setText("IMG");
                }
            } else {
                lblHinh.setText("IMG");
            }
        }
    }

    /**
     * Kiểm tra đơn giá nhập có hợp lệ với lô hàng theo nghiệp vụ:
     * - Nếu lô chưa có chi tiết nhập → chấp nhận bất kỳ giá nào.
     * - Nếu lô đã có chi tiết nhập → tất cả chi tiết phải dùng cùng một đơn giá.
     * - Nếu giá nhập mới BẰNG giá cố định của lô → cho phép.
     */
    private boolean isDonGiaHopLeChoLo(LoHang loHang, double donGiaNhapMoi) {
        if (loHang == null) {
            return true;
        }
        
        // Kiểm tra giá nhập từ cột giaNhapLo (nếu có)
        double giaNhapLo = loHang.getGiaNhapLo();
        if (giaNhapLo > 0) {
            // Nếu giá nhập mới BẰNG giá cố định → cho phép
            return Math.abs(giaNhapLo - donGiaNhapMoi) < 0.0001;
        }
        
        // Nếu giaNhapLo = 0, kiểm tra giá từ ChiTietDonNhapHang
        java.util.Optional<Double> donGiaCuOpt = chiTietDonNhapHangDAO
                .findDonGiaByMaLoHang(loHang.getMaLoHang());
        
        if (donGiaCuOpt.isPresent()) {
            double donGiaCu = donGiaCuOpt.get();
            // Nếu giá nhập mới BẰNG giá đã có → cho phép
            return Math.abs(donGiaCu - donGiaNhapMoi) < 0.0001;
        }
        
        // Lô chưa có giá nhập nào → cho phép
        return true;
    }

    private void updateTongTien() {
        if (sanPham != null) {
            int soLuong = (int) spinnerSoLuong.getValue();
            double oldTongTien = cachedTongTien;

            // Lấy đơn giá nhập từ txtDonGia
            double donGia = getDonGiaNhap();

            double tongTien = donGia * soLuong;
            double tienChietKhau = tongTien * (tyLeChietKhau / 100.0);
            double thanhTienTinhThue = tongTien - tienChietKhau;
            double tienThue = thanhTienTinhThue * (thueGTGT / 100.0);
            double tongSauThue = thanhTienTinhThue + tienThue;

            txtTongTien.setText(currencyFormat.format(tongTien) + " đ");
            lblTyLeChietKhau.setText(String.format("%.1f %%", tyLeChietKhau));
            lblTienChietKhau.setText("-" + currencyFormat.format(tienChietKhau) + " đ");
            lblThueSuat.setText(String.format("%.1f %%", thueGTGT));
            lblTienThue.setText("+" + currencyFormat.format(tienThue) + " đ");
            lblThanhTienSauThue.setText(currencyFormat.format(tongSauThue) + " đ");

            // Cập nhật cache
            cachedTongTienGoc = tongTien;
            cachedTienChietKhau = tienChietKhau;
            cachedThanhTienTinhThue = thanhTienTinhThue;
            cachedTienThue = tienThue;
            cachedTongTien = tongSauThue;

            // Fire property change để notify parent
            firePropertyChange("tongTien", oldTongTien, tongSauThue);
        }
    }

    public int getSoLuong() {
        return (int) spinnerSoLuong.getValue();
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
        loadSanPhamData();
        loadLoHangData();
    }

    public double getTongTien() {
        return cachedTongTien;
    }

    public double getTongTienTruocChietKhau() {
        return cachedTongTienGoc;
    }

    public double getTienChietKhau() {
        return cachedTienChietKhau;
    }

    public double getThanhTienTinhThue() {
        return cachedThanhTienTinhThue;
    }

    public double getTienThue() {
        return cachedTienThue;
    }

    public double getThanhTienSauThue() {
        return cachedTongTien;
    }

    public double getTyLeChietKhau() {
        return tyLeChietKhau;
    }

    public double getThueGTGT() {
        return thueGTGT;
    }

    public double getDonGiaNhap() {
        try {
            return Double.parseDouble(donGiaRawValue);
        } catch (Exception e) {
            return 0;
        }
    }

    public Date getHanDung() {
        if (loHangDaChon != null) {
            // Chuyển LocalDate sang Date
            return java.sql.Date.valueOf(loHangDaChon.getHanSuDung());
        } else if (hsdLoMoi != null) {
            return hsdLoMoi;
        }
        return null;
    }

    /**
     * Load danh sách lô hàng của sản phẩm
     */
    private void loadLoHangData() {
        if (sanPham != null) {
            danhSachLoHang = loHangBUS.getLoHangBySanPham(sanPham);
        }
    }

    private void loadDanhSachDonViTinh() {
        if (donViTinhBUS == null) {
            donViTinhBUS = new DonViTinhBUS(new DonViTinhDAO());
        }
        try {
            danhSachDonViTinh = donViTinhBUS.layTatCaDonViTinh();
        } catch (Exception ex) {
            danhSachDonViTinh = new ArrayList<>();
        }
        capNhatComboDonViTinh();
    }

    private void capNhatComboDonViTinh() {
        if (cboDonViTinh == null) {
            return;
        }
        DefaultComboBoxModel<DonViTinh> model = new DefaultComboBoxModel<>();
        if (danhSachDonViTinh != null) {
            for (DonViTinh donVi : danhSachDonViTinh) {
                model.addElement(donVi);
            }
        }
        cboDonViTinh.setModel(model);
        cboDonViTinh.setEnabled(model.getSize() > 0);
        syncDonViTinhSelection();
    }

    private void syncDonViTinhSelection() {
        if (cboDonViTinh == null) {
            return;
        }
        DonViTinh donViUuTien = donViTinhTuExcel != null
                ? donViTinhTuExcel
                : (sanPham != null ? sanPham.getDonViTinh() : null);
        boolean giuLaiGiaTriExcel = donViTinhTuExcel != null;
        if (donViUuTien == null) {
            if (cboDonViTinh.getItemCount() > 0) {
                if (cboDonViTinh.getSelectedIndex() != 0) {
                    chonDonViTinhKhongPhatSuKien(0);
                }
                apDungDonViTinhDangChon(giuLaiGiaTriExcel);
            } else {
                cboDonViTinh.setSelectedIndex(-1);
            }
            return;
        }

        for (int i = 0; i < cboDonViTinh.getItemCount(); i++) {
            DonViTinh item = cboDonViTinh.getItemAt(i);
            if (item != null && isSameDonVi(item, donViUuTien)) {
                if (cboDonViTinh.getSelectedIndex() != i) {
                    chonDonViTinhKhongPhatSuKien(i);
                }
                apDungDonViTinhDangChon(giuLaiGiaTriExcel);
                return;
            }
        }

        cboDonViTinh.addItem(donViUuTien);
        chonDonViTinhKhongPhatSuKien(donViUuTien);
        apDungDonViTinhDangChon(giuLaiGiaTriExcel);
    }

    private boolean isSameDonVi(DonViTinh first, DonViTinh second) {
        if (first == null || second == null) {
            return false;
        }
        String maFirst = first.getMaDonVi();
        String maSecond = second.getMaDonVi();
        if (maFirst != null && maSecond != null) {
            return maFirst.equals(maSecond);
        }
        String tenFirst = first.getTenDonVi();
        String tenSecond = second.getTenDonVi();
        return tenFirst != null && tenSecond != null && tenFirst.equalsIgnoreCase(tenSecond);
    }

    private void onDonViTinhChanged() {
        apDungDonViTinhDangChon(dangDongBoDonViTinh);
    }

    private void apDungDonViTinhDangChon(boolean giuLaiGiaTriExcel) {
        if (sanPham == null || cboDonViTinh == null) {
            return;
        }
        DonViTinh donViTinh = (DonViTinh) cboDonViTinh.getSelectedItem();
        sanPham.setDonViTinh(donViTinh);
        if (!giuLaiGiaTriExcel) {
            donViTinhTuExcel = null;
        }
    }

    private void chonDonViTinhKhongPhatSuKien(int index) {
        if (cboDonViTinh == null) {
            return;
        }
        dangDongBoDonViTinh = true;
        try {
            cboDonViTinh.setSelectedIndex(index);
        } finally {
            dangDongBoDonViTinh = false;
        }
    }

    private void chonDonViTinhKhongPhatSuKien(DonViTinh item) {
        if (cboDonViTinh == null) {
            return;
        }
        dangDongBoDonViTinh = true;
        try {
            cboDonViTinh.setSelectedItem(item);
        } finally {
            dangDongBoDonViTinh = false;
        }
    }

    public DonViTinh getDonViTinhDaChon() {
        return cboDonViTinh != null ? (DonViTinh) cboDonViTinh.getSelectedItem() : null;
    }

    /**
     * Cho phép các màn hình khác (ví dụ Quản lý Đơn vị tính) báo Panel reload dữ liệu.
     */
    public void refreshDonViTinhTuQuanLy() {
        loadDanhSachDonViTinh();
    }

    public List<LoHang> getDanhSachLoHang() {
        return danhSachLoHang;
    }

    public LoHang getLoHangDaChon() {
        return loHangDaChon;
    }

    public String getTenLoHangTuExcel() {
        return tenLoHangTuExcel;
    }

    public String getTenLoMoi() {
        return tenLoMoi;
    }

    public int getSoLuongLoMoi() {
        return soLuongLoMoi;
    }

    /**
     * Kiểm tra panel có đang giữ thông tin lô mới (chưa tạo DB) hay không
     */
    public boolean coThongTinLoMoi() {
        return tenLoMoi != null && hsdLoMoi != null;
    }

    /**
     * Tạo lô mới thực sự trong database (chỉ gọi khi nhập hàng và in hóa đơn)
     * @return LoHang đã được tạo, hoặc null nếu có lỗi hoặc không có thông tin lô mới
     */
    public LoHang taoLoMoiThucSu() {
        // Chỉ tạo lô nếu có thông tin lô mới và chưa có lô đã chọn
        if (tenLoMoi == null || hsdLoMoi == null) {
            // Nếu đã có lô đã chọn thì không cần tạo lô mới
            if (loHangDaChon != null) {
                return loHangDaChon;
            }
            return null;
        }

        try {
            // Tạo mã lô mới
            String maLoMoiStr = loHangBUS.taoMaLoHangMoi();
            
            // Chuyển Date sang LocalDate
            LocalDate hsd = hsdLoMoi.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            // Tạo lô mới (constructor có thể throw Exception)
            LoHang loMoi;
            try {
                loMoi = new LoHang(
                        maLoMoiStr,
                        tenLoMoi,
                        LocalDate.now(), // Ngày sản xuất = hôm nay
                        hsd,
                        0, // Tồn kho ban đầu = 0, sẽ cộng khi lưu chi tiết nhập
                        true, // Trạng thái: đang bán
                        sanPham // Gắn sản phẩm
                );
            } catch (Exception e) {
                System.err.println("[Panel_ChiTietSanPhamNhap] Lỗi khi tạo đối tượng LoHang: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
            
            // Gán giá nhập chuẩn cho lô theo nghiệp vụ (mỗi lô 1 giá nhập)
            double donGiaNhapMoi = getDonGiaNhap();
            loMoi.setGiaNhapLo(donGiaNhapMoi);

            // Lưu lô mới vào DB
            boolean themThanhCong = loHangBUS.themLoHang(loMoi);
            if (!themThanhCong) {
                System.err.println("[Panel_ChiTietSanPhamNhap] Không thể lưu lô mới vào database");
                return null;
            }

            // Gán lô mới vừa tạo làm lô đã chọn
            loHangDaChon = loMoi;
            
            // Clear thông tin tạo mới
            tenLoMoi = null;
            hsdLoMoi = null;
            soLuongLoMoi = 1;

            // Reload danh sách lô (để hiển thị lô mới)
            loadLoHangData();
            
            // Cập nhật hiển thị
            updateLoInfo();

            return loMoi;
        } catch (Exception ex) {
            System.err.println("[Panel_ChiTietSanPhamNhap] Lỗi khi tạo lô mới: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Hiển thị dialog chọn lô hàng với tab "Tạo lô mới" được chọn sẵn (dùng khi import từ Excel)
     */
    private void showDialogChonLoVoiTabTaoLoMoi() {
        showDialogChonLo(true);
    }

    /**
     * Hiển thị dialog chọn lô hàng với 2 tab: Lô cũ & Tạo lô mới
     * @param autoSelectTabTaoLoMoi true nếu muốn tự động chọn tab "Tạo lô mới"
     */
    private void showDialogChonLo(boolean autoSelectTabTaoLoMoi) {
        // Tạo dialog
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle("Chọn lô hàng");
        dialog.setModal(true);
        dialog.setSize(750, 500); // Giảm kích thước sau khi bỏ phần NCC
        dialog.setLocationRelativeTo(null); // null = giữa màn hình

        // Tạo tabbed pane
        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();

        // === TAB 1: Chọn lô cũ ===
        javax.swing.JPanel tabChonLoCu = new javax.swing.JPanel(new java.awt.BorderLayout());
        tabChonLoCu.setBackground(java.awt.Color.WHITE);

        javax.swing.JPanel pnChuaLoCu = new javax.swing.JPanel();
        pnChuaLoCu.setLayout(new javax.swing.BoxLayout(pnChuaLoCu, javax.swing.BoxLayout.Y_AXIS));
        pnChuaLoCu.setBackground(java.awt.Color.WHITE);

        ButtonGroup bgLoCu = new ButtonGroup();

        if (danhSachLoHang != null && !danhSachLoHang.isEmpty()) {
            java.time.LocalDate today = java.time.LocalDate.now();
            for (LoHang loHang : danhSachLoHang) {
                // Chỉ hiển thị các lô còn hạn (HSD >= hôm nay)
                if (loHang.getHanSuDung() != null && loHang.getHanSuDung().isBefore(today)) {
                    continue;
                }
                javax.swing.JPanel pnLoItem = new javax.swing.JPanel();
                pnLoItem.setBackground(java.awt.Color.WHITE);
                pnLoItem.setLayout(new java.awt.BorderLayout());
                pnLoItem.setBorder(
                        javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(230, 230, 230)));
                pnLoItem.setMaximumSize(new java.awt.Dimension(32767, 60));
                pnLoItem.setPreferredSize(new java.awt.Dimension(600, 60));

                JToggleButton btnLo = new JToggleButton();
                btnLo.setText(String.format("<html><b>%s</b> - HSD: %s - Tồn: %d</html>",
                        loHang.getTenLoHang(),
                        loHang.getHanSuDung().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        loHang.getTonKho()));
                btnLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
                btnLo.setFocusPainted(false);
                btnLo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                btnLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                btnLo.putClientProperty("loHang", loHang);

                bgLoCu.add(btnLo);
                pnLoItem.add(btnLo, java.awt.BorderLayout.CENTER);
                pnChuaLoCu.add(pnLoItem);
            }
        } else {
            javax.swing.JLabel lblEmpty = new javax.swing.JLabel("Chưa có lô hàng CÒN HẠN nào cho sản phẩm này");
            lblEmpty.setFont(new java.awt.Font("Segoe UI", 2, 14));
            lblEmpty.setForeground(java.awt.Color.GRAY);
            lblEmpty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            pnChuaLoCu.add(lblEmpty);
        }

        javax.swing.JScrollPane scrollLoCu = new javax.swing.JScrollPane(pnChuaLoCu);
        scrollLoCu.setBorder(null);
        tabChonLoCu.add(scrollLoCu, java.awt.BorderLayout.CENTER);

        // === TAB 2: Tạo lô mới ===
        javax.swing.JPanel tabTaoLoMoi = new javax.swing.JPanel();
        tabTaoLoMoi.setBackground(java.awt.Color.WHITE);
        tabTaoLoMoi.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbcTab2 = new java.awt.GridBagConstraints();
        gbcTab2.insets = new java.awt.Insets(15, 20, 15, 20);
        gbcTab2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbcTab2.anchor = java.awt.GridBagConstraints.WEST;

        // Mã lô mới (TỰ ĐỘNG SINH - READONLY)
        javax.swing.JLabel lblMaLo = new javax.swing.JLabel("Mã lô:");
        lblMaLo.setFont(new java.awt.Font("Segoe UI", 0, 14));
        gbcTab2.gridx = 0;
        gbcTab2.gridy = 0;
        gbcTab2.weightx = 0.0;
        tabTaoLoMoi.add(lblMaLo, gbcTab2);

        String maLoMoi = loHangBUS.taoMaLoHangMoi();
        javax.swing.JTextField txtMaLoMoi = new javax.swing.JTextField(maLoMoi);
        txtMaLoMoi.setFont(new java.awt.Font("Segoe UI", 1, 14));
        txtMaLoMoi.setPreferredSize(new java.awt.Dimension(300, 35));
        txtMaLoMoi.setEditable(false); // Không cho sửa
        txtMaLoMoi.setBackground(new java.awt.Color(240, 240, 240));
        gbcTab2.gridx = 1;
        gbcTab2.weightx = 1.0;
        tabTaoLoMoi.add(txtMaLoMoi, gbcTab2);

        // Tên lô mới
        javax.swing.JLabel lblTenLo = new javax.swing.JLabel("Tên lô:");
        lblTenLo.setFont(new java.awt.Font("Segoe UI", 0, 14));
        gbcTab2.gridx = 0;
        gbcTab2.gridy = 1;
        gbcTab2.weightx = 0.0;
        tabTaoLoMoi.add(lblTenLo, gbcTab2);

        javax.swing.JTextField txtTenLoMoi = new javax.swing.JTextField(20);
        txtTenLoMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtTenLoMoi.setPreferredSize(new java.awt.Dimension(300, 35));

        // Tự động điền tên lô từ Excel (nếu có)
        if (tenLoHangTuExcel != null && !tenLoHangTuExcel.trim().isEmpty()) {
            txtTenLoMoi.setText(tenLoHangTuExcel);
        }

        gbcTab2.gridx = 1;
        gbcTab2.weightx = 1.0;
        tabTaoLoMoi.add(txtTenLoMoi, gbcTab2);

        // HSD
        javax.swing.JLabel lblHSD = new javax.swing.JLabel("Hạn sử dụng:");
        lblHSD.setFont(new java.awt.Font("Segoe UI", 0, 14));
        gbcTab2.gridx = 0;
        gbcTab2.gridy = 2;
        gbcTab2.weightx = 0.0;
        tabTaoLoMoi.add(lblHSD, gbcTab2);

        // Sử dụng JDateChooser thay vì JTextField
        JDateChooser dateHSDMoi = new JDateChooser();
        dateHSDMoi.setDateFormatString("dd/MM/yyyy");
        dateHSDMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        dateHSDMoi.setPreferredSize(new java.awt.Dimension(300, 35));

        // Tự động điền HSD từ Excel (nếu có)
        if (hsdTuExcel != null) {
            dateHSDMoi.setDate(hsdTuExcel);
        }

        gbcTab2.gridx = 1;
        gbcTab2.weightx = 1.0;
        tabTaoLoMoi.add(dateHSDMoi, gbcTab2);

        // Số lượng
        javax.swing.JLabel lblSoLuongMoi = new javax.swing.JLabel("Số lượng:");
        lblSoLuongMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        gbcTab2.gridx = 0;
        gbcTab2.gridy = 3;
        gbcTab2.weightx = 0.0;
        gbcTab2.weighty = 0.0;
        tabTaoLoMoi.add(lblSoLuongMoi, gbcTab2);

        javax.swing.JTextField txtSoLuongMoi = new javax.swing.JTextField(20);
        txtSoLuongMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtSoLuongMoi.setPreferredSize(new java.awt.Dimension(300, 35));

        // Tự động điền số lượng từ Excel (nếu có), nếu không thì lấy số lượng hiện tại từ panel chính
        if (soLuongTuExcel != null) {
            txtSoLuongMoi.setText(String.valueOf(soLuongTuExcel));
        } else {
            // Lấy số lượng hiện tại từ panel chính để người dùng biết và có thể chỉnh sửa
            int soLuongHienTai = getSoLuong();
            txtSoLuongMoi.setText(String.valueOf(soLuongHienTai));
        }

        gbcTab2.gridx = 1;
        gbcTab2.weightx = 1.0;
        tabTaoLoMoi.add(txtSoLuongMoi, gbcTab2);

        // ✅ ĐÃ XÓA: Phần thông tin nhà cung cấp (separator, số điện thoại, tên, địa
        // chỉ, email)
        // Lý do: Thông tin NCC đã được nhập trong file Excel rồi, không cần nhập lại ở
        // đây
        // Spacer
        gbcTab2.gridx = 0;
        gbcTab2.gridy = 4;
        gbcTab2.gridwidth = 2;
        gbcTab2.weighty = 1.0;
        tabTaoLoMoi.add(new javax.swing.JLabel(), gbcTab2);

        // Thêm tab vào tabbed pane
        tabbedPane.addTab("Chọn lô có sẵn", tabChonLoCu);
        tabbedPane.addTab("Tạo lô mới", tabTaoLoMoi);

        // Tự động chọn tab "Tạo lô mới" nếu được yêu cầu (khi import từ Excel)
        if (autoSelectTabTaoLoMoi) {
            tabbedPane.setSelectedIndex(1); // Tab "Tạo lô mới" là tab thứ 2 (index 1)
        }

        // === Nút Xác nhận ===
        javax.swing.JPanel pnBottom = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        pnBottom.setBackground(java.awt.Color.WHITE);

        javax.swing.JButton btnXacNhan = new javax.swing.JButton("Xác nhận");
        btnXacNhan.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnXacNhan.setPreferredSize(new java.awt.Dimension(120, 40));
        ButtonStyles.apply(btnXacNhan, ButtonStyles.Type.SUCCESS);
        btnXacNhan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnXacNhan.addActionListener(evt -> {
            int selectedTab = tabbedPane.getSelectedIndex();

            if (selectedTab == 0) {
                // Tab "Chọn lô cũ"
                if (bgLoCu.getSelection() != null) {
                    Component[] components = pnChuaLoCu.getComponents();
                    for (Component comp : components) {
                        if (comp instanceof javax.swing.JPanel) {
                            javax.swing.JPanel panel = (javax.swing.JPanel) comp;
                            Component[] children = panel.getComponents();
                            for (Component child : children) {
                                if (child instanceof JToggleButton) {
                                    JToggleButton btn = (JToggleButton) child;
                                    if (btn.isSelected()) {
                                        LoHang loChon = (LoHang) btn.getClientProperty("loHang");

                                        // Kiểm tra hạn sử dụng: HSD phải >= hôm nay
                                        if (loChon.getHanSuDung() != null) {
                                            LocalDate hsdLo = loChon.getHanSuDung();
                                            LocalDate today = LocalDate.now();
                                            if (hsdLo.isBefore(today)) {
                                                Notifications.getInstance().show(
                                                        Notifications.Type.ERROR,
                                                        Notifications.Location.TOP_CENTER,
                                                        "Lô " + loChon.getMaLoHang() 
                                                        + " đã hết hạn sử dụng (HSD: " 
                                                        + hsdLo.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                        + "). Vui lòng chọn lô khác hoặc tạo lô mới.");
                                                return;
                                            }
                                            
                                            // Kiểm tra HSD có khớp với HSD đã nhập không (từ Excel hoặc form)
                                            LocalDate hsdDaNhap = null;
                                            if (hsdTuExcel != null) {
                                                hsdDaNhap = hsdTuExcel.toInstant()
                                                        .atZone(java.time.ZoneId.systemDefault())
                                                        .toLocalDate();
                                            } else if (hsdLoMoi != null) {
                                                hsdDaNhap = hsdLoMoi.toInstant()
                                                        .atZone(java.time.ZoneId.systemDefault())
                                                        .toLocalDate();
                                            }
                                            
                                            // Nếu có HSD đã nhập, phải khớp với HSD của lô
                                            if (hsdDaNhap != null && !hsdLo.equals(hsdDaNhap)) {
                                                Notifications.getInstance().show(
                                                        Notifications.Type.ERROR,
                                                        Notifications.Location.TOP_CENTER,
                                                        "Hạn sử dụng không khớp! Lô " + loChon.getMaLoHang() 
                                                        + " có HSD: " 
                                                        + hsdLo.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                        + ", nhưng bạn đã nhập HSD: "
                                                        + hsdDaNhap.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                        + ". Vui lòng chọn lô khác hoặc tạo lô mới.");
                                                return;
                                            }
                                        }

                                        // Kiểm tra đơn giá nhập hiện tại có phù hợp với lô này không
                                        double donGiaHienTai = getDonGiaNhap();
                                        if (!isDonGiaHopLeChoLo(loChon, donGiaHienTai)) {
                                            // Lấy giá cố định của lô (ưu tiên giaNhapLo, sau đó từ ChiTietDonNhapHang)
                                            double donGiaCoDinh = loChon.getGiaNhapLo();
                                            if (donGiaCoDinh <= 0) {
                                                java.util.Optional<Double> donGiaCuOpt = chiTietDonNhapHangDAO
                                                        .findDonGiaByMaLoHang(loChon.getMaLoHang());
                                                if (donGiaCuOpt.isPresent()) {
                                                    donGiaCoDinh = donGiaCuOpt.get();
                                                }
                                            }
                                            
                                            String msg = "Lô " + loChon.getMaLoHang()
                                                    + " đang có giá nhập cố định là "
                                                    + (donGiaCoDinh > 0 ? currencyFormat.format(donGiaCoDinh) : "không xác định")
                                                    + " đ. Bạn đang nhập giá " + currencyFormat.format(donGiaHienTai)
                                                    + " đ. Mỗi lô chỉ được phép có 1 giá nhập. Vui lòng chọn lô khác hoặc tạo lô mới.";
                                            Notifications.getInstance().show(
                                                    Notifications.Type.ERROR,
                                                    Notifications.Location.TOP_CENTER,
                                                    msg);
                                            return;
                                        }

                                        loHangDaChon = loChon;
                                        tenLoMoi = null;
                                        hsdLoMoi = null;

                                        // ⚠️ QUAN TRỌNG: Cập nhật lại sanPham từ loHangDaChon để có đầy đủ thông tin
                                        if (loHangDaChon.getSanPham() != null) {
                                            this.sanPham = loHangDaChon.getSanPham();
                                            System.out.println(
                                                    "✅ [Panel] Đã cập nhật sanPham từ loHangDaChon (chọn từ dialog), hinhAnh = "
                                                            + this.sanPham.getHinhAnh());
                                            // Load lại dữ liệu sản phẩm (bao gồm hình ảnh)
                                            loadSanPhamData();
                                        }

                                        updateLoInfo();
                                        dialog.dispose();
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Vui lòng chọn một lô hàng!");
                }
            } else {
                // Tab "Tạo lô mới"
                String tenLo = txtTenLoMoi.getText().trim();
                if (tenLo.isEmpty()) {
                    Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Vui lòng nhập tên lô!");
                    return;
                }

                // Lấy và parse số lượng
                String soLuongText = txtSoLuongMoi.getText().trim();
                if (soLuongText.isEmpty()) {
                    Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Vui lòng nhập số lượng!");
                    return;
                }

                int soLuong;
                try {
                    soLuong = Integer.parseInt(soLuongText);
                    if (soLuong <= 0) {
                        Notifications.getInstance().show(
                                Notifications.Type.WARNING,
                                Notifications.Location.TOP_CENTER,
                                "Số lượng phải lớn hơn 0!");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Số lượng không hợp lệ! Vui lòng nhập số nguyên.");
                    return;
                }

                // Kiểm tra hạn sử dụng từ JDateChooser
                Date hsdDate = dateHSDMoi.getDate();
                if (hsdDate == null) {
                    Notifications.getInstance().show(
                            Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Vui lòng chọn hạn sử dụng!");
                    return;
                }

                LocalDate hsd = hsdDate.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);

                if (hsd.isBefore(ngayGioiHan) || hsd.isEqual(ngayGioiHan)) {
                    Notifications.getInstance().show(
                            Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Hạn sử dụng phải lớn hơn 6 tháng kể từ ngày hiện tại! Yêu cầu HSD phải lớn hơn " 
                            + ngayGioiHan.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    return;
                }

                // ✅ CHỈ LƯU THÔNG TIN LÔ MỚI TẠM THỜI (chưa tạo trong DB)
                // Lô sẽ được tạo thực sự khi bấm "Nhập hàng" và in hóa đơn
                try {
                    // Lưu thông tin lô mới vào biến tạm
                    tenLoMoi = tenLo;
                    hsdLoMoi = hsdDate;
                    soLuongLoMoi = soLuong;
                    
                    // Clear lô đã chọn (vì đang chọn tạo lô mới)
                    loHangDaChon = null;

                    // Cập nhật số lượng lên panel chính (THAY THẾ, không cộng dồn)
                    // Cập nhật text field TRƯỚC để tránh ChangeListener gây cộng dồn
                    if (txtSoLuong != null) {
                        txtSoLuong.setText(String.valueOf(soLuong));
                    }
                    // Sau đó mới setValue cho spinner (số lượng từ dialog, không cộng với số lượng cũ)
                    spinnerSoLuong.setValue(soLuong);
                    updateTongTien();

                    // Cập nhật hiển thị lô (sẽ hiển thị thông tin lô mới chưa tạo)
                    updateLoInfo();
                    dialog.dispose();

                    Notifications.getInstance().show(
                            Notifications.Type.INFO,
                            Notifications.Location.TOP_CENTER,
                            "Đã chọn tạo lô mới: " + tenLo + ". Lô sẽ được tạo khi bạn nhập hàng và in hóa đơn.");

                } catch (Exception ex) {
                    Notifications.getInstance().show(
                            Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Lỗi khi lưu thông tin lô mới: " + ex.getMessage());
                }
            }
        });

        pnBottom.add(btnXacNhan);

        // Layout dialog
        dialog.setLayout(new java.awt.BorderLayout());
        dialog.add(tabbedPane, java.awt.BorderLayout.CENTER);
        dialog.add(pnBottom, java.awt.BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnXoaActionPerformed
        // Fire property change trước khi xóa để cập nhật tổng tiền
        firePropertyChange("tongTien", getTongTien(), 0.0);

        // Xóa panel này khỏi container cha
        java.awt.Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
    }// GEN-LAST:event_btnXoaActionPerformed

    private void spinnerSoLuongStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_spinnerSoLuongStateChanged
        updateTongTien();
    }// GEN-LAST:event_spinnerSoLuongStateChanged

    private void txtDonGiaMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_txtDonGiaMouseClicked
        String input = javax.swing.JOptionPane.showInputDialog(this,
                "Nhập đơn giá nhập:",
                "Cập nhật đơn giá",
                javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                String sanitized = sanitizeCurrencyText(input);
                if (sanitized.isEmpty()) {
                    throw new NumberFormatException("empty");
                }
                double donGia = Double.parseDouble(sanitized);
                if (donGia >= 0) {
                    setDonGiaRawValue(sanitized);
                    updateTongTien();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Đơn giá phải lớn hơn hoặc bằng 0!",
                            "Lỗi",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập số hợp lệ!",
                        "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }// GEN-LAST:event_txtDonGiaMouseClicked

    private void capNhatTyLeChietKhau() {
        String input = javax.swing.JOptionPane.showInputDialog(this,
                "Nhập % chiết khấu (0 - 100):",
                tyLeChietKhau);
        if (input == null) {
            return;
        }
        try {
            double value = Double.parseDouble(input.trim());
            if (value < 0 || value > 100) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Phần trăm chiết khấu phải nằm trong khoảng 0 - 100!",
                        "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            tyLeChietKhau = value;
            updateTongTien();
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập số hợp lệ!",
                    "Lỗi",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatThueGTGT() {
        String input = javax.swing.JOptionPane.showInputDialog(this,
                "Nhập % thuế GTGT (0 - 100):",
                thueGTGT);
        if (input == null) {
            return;
        }
        try {
            double value = Double.parseDouble(input.trim());
            if (value < 0 || value > 100) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Phần trăm thuế phải nằm trong khoảng 0 - 100!",
                        "Lỗi",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            thueGTGT = value;
            updateTongTien();
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập số hợp lệ!",
                    "Lỗi",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void commitSoLuongTextInput() {
        if (txtSoLuong == null || spinnerSoLuong == null) {
            return;
        }
        String raw = txtSoLuong.getText() != null ? txtSoLuong.getText().trim() : "";
        if (raw.isEmpty()) {
            raw = "1";
        }
        int value;
        try {
            value = Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            value = (int) spinnerSoLuong.getValue();
        }
        
        // Thông báo nếu số lượng nhập <= 0
        if (value <= 0) {
            raven.toast.Notifications.getInstance().show(
                    raven.toast.Notifications.Type.WARNING,
                    raven.toast.Notifications.Location.TOP_CENTER,
                    "Số lượng nhập phải lớn hơn 0!");
        }
        
        value = Math.max(1, Math.min(10000, value));
        if (!String.valueOf(value).equals(raw)) {
            txtSoLuong.setText(String.valueOf(value));
        }
        if (!spinnerSoLuong.getValue().equals(value)) {
            spinnerSoLuong.setValue(value);
        }
        spinnerSoLuongStateChanged(null);
    }

    private void updateSoLuongDisplay() {
        if (txtSoLuong != null && spinnerSoLuong != null) {
            txtSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
        }
    }

    private DecimalFormat initCurrencyFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("#,##0.############", symbols);
        format.setGroupingUsed(true);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(12);
        return format;
    }

    private String sanitizeCurrencyText(String raw) {
        if (raw == null) {
            return "";
        }
        String cleaned = raw.trim()
                .replace("đ", "")
                .replace("Đ", "")
                .replace("\u00A0", "")
                .replace(" ", "");
        if (cleaned.isEmpty()) {
            return "";
        }

        boolean hasComma = cleaned.indexOf(',') >= 0;
        int firstDot = cleaned.indexOf('.');
        boolean allowDotAsDecimal = !hasComma && firstDot >= 0 && firstDot == cleaned.lastIndexOf('.')
                && cleaned.length() - firstDot - 1 <= 2;
        if (!allowDotAsDecimal) {
            cleaned = cleaned.replace(".", "");
        }

        StringBuilder digits = new StringBuilder();
        boolean decimalAdded = false;
        for (char c : cleaned.toCharArray()) {
            if (Character.isDigit(c)) {
                digits.append(c);
            } else if (c == ',' && !decimalAdded) {
                digits.append('.');
                decimalAdded = true;
            } else if (allowDotAsDecimal && c == '.' && !decimalAdded) {
                digits.append('.');
                decimalAdded = true;
            }
        }
        if (digits.length() == 0) {
            return "";
        }

        String result = digits.toString();
        if (result.charAt(0) == '.') {
            result = "0" + result;
        }
        if (result.endsWith(".")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private void setDonGiaFromDouble(double value) {
        setDonGiaRawValue(BigDecimal.valueOf(value).toPlainString());
    }

    private void setDonGiaRawValue(String numericValue) {
        String normalized = normalizeNumericString(numericValue);
        this.donGiaRawValue = normalized;
        if (txtDonGia != null) {
            txtDonGia.setText(formatDonGiaDisplay(normalized) + " đ");
        }
    }

    private String normalizeNumericString(String numericValue) {
        if (numericValue == null) {
            return "0";
        }
        String trimmed = numericValue.trim();
        if (trimmed.isEmpty()) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        boolean decimalAdded = false;
        for (char c : trimmed.toCharArray()) {
            if (Character.isDigit(c)) {
                builder.append(c);
            } else if (c == '.' && !decimalAdded) {
                builder.append('.');
                decimalAdded = true;
            }
        }
        if (builder.length() == 0) {
            return "0";
        }
        if (builder.charAt(0) == '.') {
            builder.insert(0, '0');
        }
        if (builder.charAt(builder.length() - 1) == '.') {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private String formatDonGiaDisplay(String numericValue) {
        if (numericValue == null || numericValue.isEmpty()) {
            return "0";
        }
        String working = numericValue.startsWith("-") ? numericValue.substring(1) : numericValue;
        String integerPart = working;
        String fractionalPart = "";
        int dotIndex = working.indexOf('.');
        if (dotIndex >= 0) {
            integerPart = working.substring(0, dotIndex);
            fractionalPart = working.substring(dotIndex + 1);
        }
        if (integerPart.isEmpty()) {
            integerPart = "0";
        }
        String groupedInteger = formatIntegerWithGrouping(integerPart);
        StringBuilder builder = new StringBuilder();
        if (numericValue.startsWith("-")) {
            builder.append("-");
        }
        builder.append(groupedInteger);
        if (!fractionalPart.isEmpty()) {
            builder.append(',');
            builder.append(fractionalPart);
        }
        return builder.toString();
    }

    private String formatIntegerWithGrouping(String digits) {
        if (digits == null || digits.isEmpty()) {
            return "0";
        }
        String normalized = digits.replaceFirst("^0+(?!$)", "");
        if (normalized.isEmpty()) {
            normalized = "0";
        }
        StringBuilder builder = new StringBuilder(normalized);
        for (int i = builder.length() - 3; i > 0; i -= 3) {
            builder.insert(i, '.');
        }
        return builder.toString();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner spinnerSoLuong;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}

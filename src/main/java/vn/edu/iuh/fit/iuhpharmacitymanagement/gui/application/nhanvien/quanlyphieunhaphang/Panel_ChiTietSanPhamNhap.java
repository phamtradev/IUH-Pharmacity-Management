/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlyphieunhaphang;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.LoHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.ButtonGroup;
import java.awt.Component;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;

/**
 *
 * @author PhamTra
 */
public class Panel_ChiTietSanPhamNhap extends javax.swing.JPanel {

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

    private double cachedTongTien = 0; // Cache giá trị tổng tiền để detect thay đổi
    private LoHangBUS loHangBUS;
    private SanPhamDAO sanPhamDAO;
    private List<LoHang> danhSachLoHang;
    private LoHang loHangDaChon = null;
    private String tenLoHangTuExcel = null; // Lưu tên lô từ Excel
    private String soDienThoaiNCCTuExcel = null; // Lưu SĐT NCC từ Excel

    // Thông tin lô mới sẽ tạo
    private String tenLoMoi = null;
    private Date hsdLoMoi = null;
    private int soLuongLoMoi = 1;

    // Dữ liệu từ Excel để tự động điền vào form tạo lô mới
    private Date hsdTuExcel = null;
    private Integer soLuongTuExcel = null;
    private Double donGiaTuExcel = null; // Lưu đơn giá từ Excel

    public Panel_ChiTietSanPhamNhap() {
        this.currencyFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.sanPhamDAO = new SanPhamDAO();
        initComponents();
    }

    /**
     * Constructor khi KHÔNG có thông tin lô từ Excel
     * Hiển thị Spinner HSD + Button "Chọn lô"
     */
    public Panel_ChiTietSanPhamNhap(SanPham sanPham) {
        this.sanPham = sanPham;
        this.currencyFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.sanPhamDAO = new SanPhamDAO();
        this.tenLoHangTuExcel = null; // Không có lô từ Excel
        initComponents();
        loadSanPhamData();
        loadLoHangData();
    }

    /**
     * Constructor khi CÓ thông tin lô từ Excel
     * Tự động chọn lô nếu có cùng Số ĐK + HSD + SĐT NCC khớp, nếu không → báo lỗi
     */
    public Panel_ChiTietSanPhamNhap(SanPham sanPham, int soLuong, double donGiaNhap, Date hanDung, String tenLoHang,
            String soDienThoaiNCC) throws Exception {
        System.out.println("→→ Panel Constructor: " + sanPham.getTenSanPham() + " | SL=" + soLuong + " | TênLô="
                + tenLoHang + " | SĐT NCC=" + soDienThoaiNCC);

        this.sanPham = sanPham;
        this.currencyFormat = new DecimalFormat("#,###");
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.loHangBUS = new LoHangBUS();
        this.sanPhamDAO = new SanPhamDAO();
        this.tenLoHangTuExcel = tenLoHang; // Lưu tên lô từ Excel
        this.soDienThoaiNCCTuExcel = soDienThoaiNCC; // Lưu SĐT NCC từ Excel

        // Lưu dữ liệu từ Excel để tự động điền vào form tạo lô mới
        this.hsdTuExcel = hanDung;
        this.soLuongTuExcel = soLuong;
        this.donGiaTuExcel = donGiaNhap; // Lưu đơn giá từ Excel

        try {
            initComponents();
            loadSanPhamData();
            loadLoHangData();

            // Set các giá trị từ Excel
            spinnerSoLuong.setValue(soLuong);
            txtDonGia.setText(currencyFormat.format(donGiaNhap) + " đ");

            // 🔍 TÌM LÔ HÀNG THEO SỐ ĐĂNG KÝ + HẠN SỬ DỤNG + SĐT NCC
            System.out.println("📅 [PANEL] Date từ Excel: " + hanDung);
            System.out.println("📅 [PANEL] Date format: " + dateFormat.format(hanDung));
            System.out.println("📞 [PANEL] SĐT NCC từ Excel: " + soDienThoaiNCC);

            LocalDate hsd = hanDung.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            System.out.println("📅 [PANEL] LocalDate sau convert: " + hsd);

            // ═══════════════════════════════════════════════════════════════════
            // KIỂM TRA SỐ ĐIỆN THOẠI NCC
            // ═══════════════════════════════════════════════════════════════════
            if (soDienThoaiNCC != null && !soDienThoaiNCC.trim().isEmpty()) {
                // Lấy danh sách SĐT của các NCC đã nhập sản phẩm này
                List<String> danhSachSDT = sanPhamDAO.getSoDienThoaiNCCBySoDangKy(sanPham.getSoDangKy());

                if (!danhSachSDT.isEmpty() && !danhSachSDT.contains(soDienThoaiNCC.trim())) {
                    // ❌ SĐT NCC KHÔNG KHỚP → BÁO LỖI
                    String errorMsg = "Sản phẩm '" + sanPham.getTenSanPham() +
                            "' có số đăng ký " + sanPham.getSoDangKy() +
                            " không thể nhập từ nhiều nhà cung cấp khác nhau";

                    System.out.println("❌ [PANEL] " + errorMsg);
                    throw new Exception(errorMsg);
                }
            }

            // ═══════════════════════════════════════════════════════════════════
            // TÌM LÔ HÀNG THEO SỐ ĐĂNG KÝ + HẠN SỬ DỤNG
            // ═══════════════════════════════════════════════════════════════════
            Optional<LoHang> loTrung = loHangBUS.timLoHangTheoSoDangKyVaHanSuDung(
                    sanPham.getSoDangKy(),
                    hsd);

            if (loTrung.isPresent()) {
                // ✅ TÌM THẤY LÔ TRÙNG → TỰ ĐỘNG CHỌN
                loHangDaChon = loTrung.get();

                // ⚠️ QUAN TRỌNG: Cập nhật lại sanPham từ loHangDaChon để có đầy đủ thông tin
                // (bao gồm hinhAnh từ JOIN)
                if (loHangDaChon.getSanPham() != null) {
                    this.sanPham = loHangDaChon.getSanPham();
                    System.out.println(
                            "✅ [Panel] Đã cập nhật sanPham từ loHangDaChon, hinhAnh = " + this.sanPham.getHinhAnh());
                    // Load lại dữ liệu sản phẩm (bao gồm hình ảnh)
                    loadSanPhamData();
                }

                updateLoInfo(); // Hiển thị thẻ lô
            } else {
                // ❌ KHÔNG TÌM THẤY LÔ → HIỂN thị nút "Chọn lô" (dữ liệu Excel sẽ tự động điền
                // vào form)
                System.out.println("→→ Không tìm thấy lô trùng, hiển thị nút 'Chọn lô' với dữ liệu từ Excel");
                // Không tự động tạo lô, để user bấm "Chọn lô" và chọn tab "Tạo lô mới"
                // Dữ liệu từ Excel (tenLoHangTuExcel, hsdTuExcel, soLuongTuExcel) đã được lưu
                // và sẽ tự động điền vào form khi mở dialog
            }

            // Cập nhật tổng tiền
            updateTongTien();
        } catch (Exception e) {
            System.out.println("✗✗ LỖI khởi tạo panel: " + e.getMessage());
            throw e; // ⚠️ THROW LẠI để method gọi bắt được
        }
    }

    /**
     * Cập nhật hiển thị: Nút "Chọn lô" → Thẻ lô đẹp
     */
    private void updateLoInfo() {
        // Clear panel lô
        pnLo.removeAll();

        if (loHangDaChon != null || (tenLoMoi != null && hsdLoMoi != null)) {
            // Đã chọn lô → Hiển thị THẺ LÔ đẹp
            pnTheLo = new javax.swing.JPanel();
            pnTheLo.setBackground(java.awt.Color.WHITE);
            pnTheLo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
            // Chiều cao cố định cho tất cả lô (3 dòng: Tên + HSD + Tồn)
            pnTheLo.setPreferredSize(new java.awt.Dimension(160, 95));
            pnTheLo.setLayout(new javax.swing.BoxLayout(pnTheLo, javax.swing.BoxLayout.Y_AXIS));

            String tenLo, hsd;
            int tonKho = 0;

            if (loHangDaChon != null) {
                // Lô cũ: hiển thị tồn kho của lô đó
                tenLo = loHangDaChon.getTenLoHang();
                hsd = loHangDaChon.getHanSuDung().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                tonKho = loHangDaChon.getTonKho(); // Tồn kho của lô này
            } else {
                // Lô mới: hiển thị tồn kho = 0
                tenLo = tenLoMoi + " (mới)";
                hsd = dateFormat.format(hsdLoMoi);
                tonKho = 0; // Lô mới chưa có tồn
            }

            // Tên lô
            lblTheLo_TenLo = new javax.swing.JLabel("Lô: " + tenLo);
            lblTheLo_TenLo.setFont(new java.awt.Font("Segoe UI", 1, 13));
            lblTheLo_TenLo.setForeground(new java.awt.Color(51, 51, 51));
            lblTheLo_TenLo.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_TenLo.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));

            // HSD
            lblTheLo_HSD = new javax.swing.JLabel("HSD: " + hsd);
            lblTheLo_HSD.setFont(new java.awt.Font("Segoe UI", 0, 12));
            lblTheLo_HSD.setForeground(new java.awt.Color(102, 102, 102));
            lblTheLo_HSD.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_HSD.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8)); // Tăng padding bottom

            pnTheLo.add(lblTheLo_TenLo);
            pnTheLo.add(lblTheLo_HSD);

            // Tồn kho (luôn hiển thị)
            lblTheLo_TonKho = new javax.swing.JLabel("Tồn: " + tonKho);
            lblTheLo_TonKho.setFont(new java.awt.Font("Segoe UI", 0, 12));
            lblTheLo_TonKho.setForeground(new java.awt.Color(34, 139, 34));
            lblTheLo_TonKho.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblTheLo_TonKho.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 8, 8));
            pnTheLo.add(lblTheLo_TonKho);

            pnLo.add(pnTheLo);
        } else {
            // Chưa chọn → Hiển thị NÚT "Chọn lô"
            btnChonLo = new javax.swing.JButton("Chọn lô");
            btnChonLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
            btnChonLo.setPreferredSize(new java.awt.Dimension(120, 40));
            ButtonStyles.apply(btnChonLo, ButtonStyles.Type.PRIMARY);
            btnChonLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btnChonLo.addActionListener(evt -> showDialogChonLo());

            pnLo.add(btnChonLo);
        }

        pnLo.revalidate();
        pnLo.repaint();
    }

    private void loadSanPhamData() {
        if (sanPham != null) {
            // Set tên sản phẩm
            lblTenSP.setText(sanPham.getTenSanPham());

            // Set đơn giá nhập: Nếu có đơn giá từ Excel thì dùng, không thì mặc định = 0
            if (donGiaTuExcel != null && donGiaTuExcel > 0) {
                txtDonGia.setText(currencyFormat.format(donGiaTuExcel) + " đ");
            } else {
                txtDonGia.setText("0 đ");
            }

            // Set tổng tiền ban đầu
            updateTongTien();

            // Load hình ảnh nếu có
            if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
                try {
                    ImageIcon icon = null;
                    String hinhAnh = sanPham.getHinhAnh().trim(); // Loại bỏ khoảng trắng thừa

                    System.out.println("🔍 [Panel_ChiTietSanPhamNhap] Đang load hình ảnh: '" + hinhAnh + "'");
                    System.out.println(
                            "🔍 [Panel_ChiTietSanPhamNhap] Working directory: " + System.getProperty("user.dir"));

                    // 1. Thử load từ đường dẫn tuyệt đối (nếu file được chọn từ JFileChooser)
                    java.io.File imageFile = new java.io.File(hinhAnh);
                    if (imageFile.exists() && imageFile.isFile()) {
                        icon = new ImageIcon(hinhAnh);
                        System.out.println("✅ Load hình từ đường dẫn tuyệt đối: " + hinhAnh);
                    } else {
                        // 2. Thử load từ resources (khi chạy từ JAR)
                        java.net.URL imgURL = getClass().getResource("/img/" + hinhAnh);
                        if (imgURL != null) {
                            icon = new ImageIcon(imgURL);
                            System.out.println("✅ Load hình từ resources: /img/" + hinhAnh);
                        } else {
                            // 3. Thử load từ file system với đường dẫn tương đối (khi chạy từ IDE)
                            String imagePath = "src/main/resources/img/" + hinhAnh;
                            java.io.File fileSystemImage = new java.io.File(imagePath);
                            if (fileSystemImage.exists() && fileSystemImage.isFile()) {
                                icon = new ImageIcon(imagePath);
                                System.out.println("✅ Load hình từ file system (relative): " + imagePath);
                            } else {
                                // 4. Thử load từ file system với đường dẫn tuyệt đối từ project root
                                String projectRoot = System.getProperty("user.dir");
                                String absoluteImagePath = projectRoot + java.io.File.separator + "src"
                                        + java.io.File.separator + "main" + java.io.File.separator + "resources"
                                        + java.io.File.separator + "img" + java.io.File.separator + hinhAnh;
                                java.io.File absoluteFile = new java.io.File(absoluteImagePath);
                                if (absoluteFile.exists() && absoluteFile.isFile()) {
                                    icon = new ImageIcon(absoluteImagePath);
                                    System.out.println("✅ Load hình từ file system (absolute): " + absoluteImagePath);
                                } else {
                                    // 5. Thử load từ target/classes/img/ (khi chạy từ IDE với Maven)
                                    String targetImagePath = projectRoot + java.io.File.separator + "target"
                                            + java.io.File.separator + "classes" + java.io.File.separator + "img"
                                            + java.io.File.separator + hinhAnh;
                                    java.io.File targetFile = new java.io.File(targetImagePath);
                                    if (targetFile.exists() && targetFile.isFile()) {
                                        icon = new ImageIcon(targetImagePath);
                                        System.out.println("✅ Load hình từ target/classes/img/: " + targetImagePath);
                                    } else {
                                        System.err.println("❌ Không tìm thấy hình ảnh ở bất kỳ đâu:");
                                        System.err.println("   - Đường dẫn tuyệt đối: " + hinhAnh);
                                        System.err.println("   - Resources: /img/" + hinhAnh);
                                        System.err.println("   - File system (relative): " + imagePath);
                                        System.err.println("   - File system (absolute): " + absoluteImagePath);
                                        System.err.println("   - Target/classes: " + targetImagePath);
                                    }
                                }
                            }
                        }
                    }

                    if (icon != null && icon.getIconWidth() > 0) {
                        // Scale image to fit label
                        java.awt.Image img = icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                        lblHinh.setIcon(new ImageIcon(img));
                        lblHinh.setText("");
                        System.out.println("✅ Đã hiển thị hình ảnh thành công!");
                    } else {
                        lblHinh.setText("IMG");
                        System.err.println("❌ Icon null hoặc không hợp lệ (width="
                                + (icon != null ? icon.getIconWidth() : "null") + ")");
                    }
                } catch (Exception e) {
                    lblHinh.setText("IMG");
                    System.err.println("❌ Lỗi khi load hình ảnh: " + sanPham.getHinhAnh() + " - " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                lblHinh.setText("IMG");
                System.out.println("⚠️ Sản phẩm không có hình ảnh (hinhAnh = null hoặc empty)");
            }
        }
    }

    private void updateTongTien() {
        if (sanPham != null) {
            int soLuong = (int) spinnerSoLuong.getValue();
            double oldTongTien = cachedTongTien;

            // Lấy đơn giá nhập từ txtDonGia
            double donGia = 0;
            try {
                String donGiaStr = txtDonGia.getText().replace(" đ", "").replace(",", "");
                donGia = Double.parseDouble(donGiaStr);
            } catch (Exception e) {
                donGia = 0;
            }

            double tongTien = donGia * soLuong;
            txtTongTien.setText(currencyFormat.format(tongTien) + " đ");

            // Cập nhật cache
            cachedTongTien = tongTien;

            // Fire property change để notify parent
            firePropertyChange("tongTien", oldTongTien, tongTien);
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

    public double getDonGiaNhap() {
        try {
            String donGiaStr = txtDonGia.getText().replace(" đ", "").replace(",", "");
            return Double.parseDouble(donGiaStr);
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
     * Hiển thị dialog chọn lô hàng với 2 tab: Lô cũ & Tạo lô mới
     */
    private void showDialogChonLo() {
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
            for (LoHang loHang : danhSachLoHang) {
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
            javax.swing.JLabel lblEmpty = new javax.swing.JLabel("Chưa có lô hàng nào cho sản phẩm này");
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

        // Thay JSpinner thành JTextField
        javax.swing.JTextField txtHSDMoi = new javax.swing.JTextField(20);
        txtHSDMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtHSDMoi.setPreferredSize(new java.awt.Dimension(300, 35));

        // Tự động điền HSD từ Excel (nếu có)
        if (hsdTuExcel != null) {
            txtHSDMoi.setText(dateFormat.format(hsdTuExcel));
        } else {
            // Placeholder
            txtHSDMoi.putClientProperty("JTextField.placeholderText", "dd/MM/yyyy");
        }

        gbcTab2.gridx = 1;
        gbcTab2.weightx = 1.0;
        tabTaoLoMoi.add(txtHSDMoi, gbcTab2);

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

        // Tự động điền số lượng từ Excel (nếu có)
        if (soLuongTuExcel != null) {
            txtSoLuongMoi.setText(String.valueOf(soLuongTuExcel));
        } else {
            txtSoLuongMoi.setText("1");
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
                                        loHangDaChon = (LoHang) btn.getClientProperty("loHang");
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
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Vui lòng chọn một lô hàng!",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                }
            } else {
                // Tab "Tạo lô mới"
                String tenLo = txtTenLoMoi.getText().trim();
                if (tenLo.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Vui lòng nhập tên lô!",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Lấy và parse số lượng
                String soLuongText = txtSoLuongMoi.getText().trim();
                if (soLuongText.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Vui lòng nhập số lượng!",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int soLuong;
                try {
                    soLuong = Integer.parseInt(soLuongText);
                    if (soLuong <= 0) {
                        javax.swing.JOptionPane.showMessageDialog(dialog,
                                "Số lượng phải lớn hơn 0!",
                                "Thông báo",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Số lượng không hợp lệ! Vui lòng nhập số nguyên.",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Kiểm tra và parse hạn sử dụng
                String hsdText = txtHSDMoi.getText().trim();
                if (hsdText.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Vui lòng nhập hạn sử dụng!",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Date hsdDate;
                try {
                    hsdDate = dateFormat.parse(hsdText);
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LocalDate hsd = hsdDate.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);

                if (hsd.isBefore(ngayGioiHan) || hsd.isEqual(ngayGioiHan)) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Hạn sử dụng phải lớn hơn 6 tháng kể từ ngày hiện tại!",
                            "Thông báo",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // ✅ TẠO LÔ MỚI NGAY LẬP TỨC với mã LHxxxxx
                try {
                    String maLoMoiStr = txtMaLoMoi.getText(); // Lấy mã đã generate

                    LoHang loMoi = new LoHang(
                            maLoMoiStr, // Mã lô đã tự generate
                            tenLo,
                            LocalDate.now(), // Ngày sản xuất = hôm nay
                            hsd,
                            soLuong, // Tồn kho ban đầu
                            true, // Trạng thái: đang bán
                            sanPham // Gắn sản phẩm
                    );

                    // Lưu lô mới vào DB
                    boolean themThanhCong = loHangBUS.themLoHang(loMoi);
                    if (!themThanhCong) {
                        javax.swing.JOptionPane.showMessageDialog(dialog,
                                "Lỗi khi tạo lô mới! Vui lòng thử lại.",
                                "Lỗi",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Gán lô mới vừa tạo làm lô đã chọn
                    loHangDaChon = loMoi;
                    tenLoMoi = null; // Clear thông tin tạo mới
                    hsdLoMoi = null;

                    // Cập nhật số lượng lên panel chính
                    spinnerSoLuong.setValue(soLuong);
                    updateTongTien();

                    // Reload danh sách lô (để hiển thị lô mới)
                    loadLoHangData();

                    updateLoInfo();
                    dialog.dispose();

                    javax.swing.JOptionPane.showMessageDialog(Panel_ChiTietSanPhamNhap.this,
                            "Đã tạo lô mới thành công: " + loMoi.getMaLoHang() + " - " + tenLo,
                            "Thành công",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(dialog,
                            "Lỗi khi tạo lô mới: " + ex.getMessage(),
                            "Lỗi",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
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
    private void initComponents() {

        spinnerSoLuong = new javax.swing.JSpinner();
        txtDonGia = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(232, 232, 232)));
        setMaximumSize(new java.awt.Dimension(32767, 120));
        setMinimumSize(new java.awt.Dimension(800, 120));
        setPreferredSize(new java.awt.Dimension(1000, 120));
        setRequestFocusEnabled(false);

        // Sử dụng GridBagLayout để các cột thẳng hàng
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.CENTER;
        gbc.insets = new java.awt.Insets(15, 10, 15, 10);
        gbc.gridy = 0;
        gbc.weighty = 1.0;

        // 1. Hình ảnh sản phẩm
        lblHinh = new javax.swing.JLabel();
        lblHinh.setPreferredSize(new java.awt.Dimension(100, 100));
        lblHinh.setMinimumSize(new java.awt.Dimension(100, 100));
        lblHinh.setMaximumSize(new java.awt.Dimension(100, 100));
        lblHinh.setBackground(new java.awt.Color(240, 240, 240));
        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));
        lblHinh.setOpaque(true);
        lblHinh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        lblHinh.setText("");
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        add(lblHinh, gbc);

        // 2. Tên sản phẩm
        lblTenSP = new javax.swing.JLabel();
        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSP.setText("");
        lblTenSP.setPreferredSize(new java.awt.Dimension(250, 100));
        lblTenSP.setMinimumSize(new java.awt.Dimension(200, 100));
        lblTenSP.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(lblTenSP, gbc);

        // 3. Cột "Lô hàng" - Ban đầu hiển thị nút "Chọn lô"
        pnLo = new javax.swing.JPanel();
        pnLo.setBackground(java.awt.Color.WHITE);
        pnLo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 30));
        pnLo.setPreferredSize(new java.awt.Dimension(180, 100));
        pnLo.setMinimumSize(new java.awt.Dimension(180, 100));

        // Luôn tạo nút "Chọn lô" ban đầu
        btnChonLo = new javax.swing.JButton("Chọn lô");
        btnChonLo.setFont(new java.awt.Font("Segoe UI", 0, 13));
        btnChonLo.setPreferredSize(new java.awt.Dimension(120, 40));
        ButtonStyles.apply(btnChonLo, ButtonStyles.Type.PRIMARY);
        btnChonLo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChonLo.addActionListener(evt -> showDialogChonLo());

        pnLo.add(btnChonLo);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        add(pnLo, gbc);

        // 4. Số lượng với nút +/-
        javax.swing.JPanel pnSpinner = new javax.swing.JPanel();
        pnSpinner.setBackground(java.awt.Color.WHITE);
        pnSpinner.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 32));

        // Nút giảm
        javax.swing.JButton btnGiam = new javax.swing.JButton("-");
        btnGiam.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnGiam.setPreferredSize(new java.awt.Dimension(45, 45));
        ButtonStyles.apply(btnGiam, ButtonStyles.Type.DANGER);
        btnGiam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGiam.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue > 1) {
                spinnerSoLuong.setValue(currentValue - 1);
                spinnerSoLuongStateChanged(null);
            }
        });

        // Label hiển thị số lượng
        javax.swing.JLabel lblSoLuong = new javax.swing.JLabel("1");
        lblSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblSoLuong.setPreferredSize(new java.awt.Dimension(70, 45));
        lblSoLuong.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoLuong.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 1));

        // Cập nhật spinner để đồng bộ với label (ẩn spinner)
        spinnerSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10000, 1));
        spinnerSoLuong.setVisible(false); // Ẩn spinner, chỉ dùng để lưu giá trị

        // Listener để cập nhật label khi spinner thay đổi
        spinnerSoLuong.addChangeListener(evt -> {
            lblSoLuong.setText(String.valueOf(spinnerSoLuong.getValue()));
        });

        // Nút tăng
        javax.swing.JButton btnTang = new javax.swing.JButton("+");
        btnTang.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnTang.setPreferredSize(new java.awt.Dimension(45, 45));
        ButtonStyles.apply(btnTang, ButtonStyles.Type.SUCCESS);
        btnTang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTang.addActionListener(evt -> {
            int currentValue = (int) spinnerSoLuong.getValue();
            if (currentValue < 10000) {
                spinnerSoLuong.setValue(currentValue + 1);
                spinnerSoLuongStateChanged(null);
            }
        });

        pnSpinner.add(btnGiam);
        pnSpinner.add(lblSoLuong);
        pnSpinner.add(btnTang);
        pnSpinner.setPreferredSize(new java.awt.Dimension(180, 100));
        pnSpinner.setMinimumSize(new java.awt.Dimension(180, 100));
        gbc.gridx = 3;
        gbc.weightx = 0.0;
        add(pnSpinner, gbc);

        // 5. Đơn giá nhập (có thể chỉnh sửa)
        txtDonGia = new javax.swing.JLabel();
        txtDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDonGia.setText("0 đ");
        txtDonGia.setPreferredSize(new java.awt.Dimension(120, 100));
        txtDonGia.setMinimumSize(new java.awt.Dimension(120, 100));
        txtDonGia.setMaximumSize(new java.awt.Dimension(120, 100));
        txtDonGia.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtDonGia.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        txtDonGia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtDonGia.setToolTipText("Click để chỉnh sửa đơn giá");
        txtDonGia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtDonGiaMouseClicked(evt);
            }
        });
        gbc.gridx = 4;
        gbc.weightx = 0.0;
        add(txtDonGia, gbc);

        // 6. Tổng tiền
        txtTongTien = new javax.swing.JLabel();
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 16));
        txtTongTien.setForeground(new java.awt.Color(0, 120, 215));
        txtTongTien.setText("0 đ");
        txtTongTien.setPreferredSize(new java.awt.Dimension(130, 100));
        txtTongTien.setMinimumSize(new java.awt.Dimension(130, 100));
        txtTongTien.setMaximumSize(new java.awt.Dimension(130, 100));
        txtTongTien.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        txtTongTien.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        add(txtTongTien, gbc);

        // 7. Nút Xóa
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
        pnXoa.setPreferredSize(new java.awt.Dimension(90, 100));
        pnXoa.setMinimumSize(new java.awt.Dimension(90, 100));
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        add(pnXoa, gbc);
    }// </editor-fold>//GEN-END:initComponents

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
                double donGia = Double.parseDouble(input.trim());
                if (donGia >= 0) {
                    txtDonGia.setText(currencyFormat.format(donGia) + " đ");
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner spinnerSoLuong;
    private javax.swing.JLabel txtDonGia;
    private javax.swing.JLabel txtTongTien;
    // End of variables declaration//GEN-END:variables
}

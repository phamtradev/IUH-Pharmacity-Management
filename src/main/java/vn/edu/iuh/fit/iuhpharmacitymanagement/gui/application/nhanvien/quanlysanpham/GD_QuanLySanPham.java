/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.nhanvien.quanlysanpham;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import vn.edu.iuh.fit.iuhpharmacitymanagement.common.TableDesign;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonViTinhDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonViTinh;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import raven.toast.Notifications;
import javax.swing.UIManager;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.FontStyles;

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

    // Business Logic
    private SanPhamBUS sanPhamBUS;
    private SanPhamDAO sanPhamDAO;
    private DonViTinhDAO donViTinhDAO;
    private LoHangDAO loHangDAO;
    private DecimalFormat currencyFormat;
    private DecimalFormat percentFormat;
    private SimpleDateFormat dateFormat;

    public GD_QuanLySanPham() {
        this(false); // Mặc định là nhân viên
    }

    public GD_QuanLySanPham(boolean isManager) {
        this.isManager = isManager;

        // Khởi tạo DAO và BUS
        sanPhamDAO = new SanPhamDAO();
        donViTinhDAO = new DonViTinhDAO();
        loHangDAO = new LoHangDAO();
        sanPhamBUS = new SanPhamBUS(sanPhamDAO);
        currencyFormat = new DecimalFormat("#,###");
        percentFormat = new DecimalFormat("#0.##");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        initComponents();

        // Áp dụng ButtonStyles và FontStyles
        applyStyles();

        styleTextField(txtSearchNamePD, "Nhập tên sản phẩm hoặc số đăng ký");

        initGlobalTextFieldStyle();
        setUIManager();
        loadComboBoxData();
        fillTable();
        addIconFeature();

        // Quyền theo vai trò
        if (isManager) {
            btnDelete.setVisible(true);
            btnUpdate.setVisible(true);
        } else {
            // Nhân viên không được sửa sản phẩm
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);
        }
    }

    private String formatCurrency(double value) {
        return currencyFormat.format(Math.round(value));
    }

    private void applyStyles() {
        // Buttons chính
        ButtonStyles.apply(btnAdd, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnAdd, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnAdd, FontStyles.Type.TEXT_MEDIUM);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new java.awt.Dimension(95, 40));

        ButtonStyles.apply(btnViewDetail, ButtonStyles.Type.PRIMARY);
        FontStyles.apply(btnViewDetail, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnViewDetail, FontStyles.Type.TEXT_MEDIUM);
        btnViewDetail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnViewDetail.setPreferredSize(new java.awt.Dimension(125, 40));

        ButtonStyles.apply(btnOpenModalAddSup, ButtonStyles.Type.SUCCESS);
        FontStyles.apply(btnOpenModalAddSup, FontStyles.Type.BUTTON_MEDIUM);
        FontStyles.apply(btnOpenModalAddSup, FontStyles.Type.TEXT_MEDIUM);
        btnOpenModalAddSup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOpenModalAddSup.setPreferredSize(new java.awt.Dimension(150, 40));

        FontStyles.toUpperCase(
                btnAdd,
                btnViewDetail,
                btnAddProduct,
                btnOpenModalAddSup
        );
    }

    private void styleTextField(javax.swing.JTextField textField, String hintText) {
        FontStyles.apply(textField, FontStyles.Type.INPUT_FIELD);
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, hintText);
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true); // nút clear (x)

        // Tạo padding bên trong cho đẹp
        textField.putClientProperty(FlatClientProperties.STYLE, ""
                + "margin:5,10,5,10;" // top, left, bottom, right
                + "arc:10;"
                + "borderWidth:1;"
                + "focusWidth:2;"
                + "borderColor:rgb(150,180,255);"
                + "focusColor:rgb(51,153,255);"
        );
    }

    private void initGlobalTextFieldStyle() {
        UIManager.put("Component.arc", 10); // Bo tròn cho tất cả component
        UIManager.put("TextComponent.arc", 10); // Cụ thể cho JTextField
        UIManager.put("Component.borderWidth", 1);
        UIManager.put("Component.borderColor", new java.awt.Color(150, 180, 255));

        // Focus - Khi click vào TextField
        UIManager.put("Component.focusColor", UIManager.getColor("Component.accentColor"));
        UIManager.put("Component.focusWidth", 2);
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
        txtProductPurchasePrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Giá nhập lấy từ lô (không chỉnh được)");
        txtProductRegisNumber.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí");
        txtProductSellingPrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập giá bán");
        if (txtProductVAT != null) {
            txtProductVAT.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập % VAT (ví dụ 10)");
        }

        // Placeholder cho modal SỬA sản phẩm
        txtCountryOfOrigin1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập xuất xứ");
        txtProductActiveIngre1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập hoạt chất");
        txtProductDosage1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập liều lượng");
        txtProductManufacturer1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên nhà sản xuất");
        txtProductName1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên sản phẩm");
        txtProductPakaging1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập quy trình đóng gói");
        txtProductPurchasePrice1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Giá nhập lấy từ lô (không chỉnh được)");
        txtProductRegisNumber1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập số đăng kí");
        txtProductSellingPrice1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập giá bán");
        if (txtProductVAT1 != null) {
            txtProductVAT1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập % VAT (ví dụ 10)");
        }

        UIManager.put("Button.arc", 10);
    }

    private void fillTable() {
        // Hiển thị giá nhập theo FIFO CHƯA bao gồm VAT trên bảng
        // (VAT sẽ hiển thị riêng ở cột Thuế VAT (%) và dùng cho tính toán khác)
        String[] headers = {"Mã sản phẩm", "Tên sản phẩm", "Đơn vị tính", "Số đăng kí", "Xuất xứ", "Loại sản phẩm", "Giá nhập", "Thuế VAT (%)", "Giá bán (đã VAT)", "Trạng thái"};
        List<Integer> tableWidths = Arrays.asList(120, 280, 110, 120, 110, 130, 130, 110, 140, 110);
        tableDesign = new TableDesign(headers, tableWidths);
        ProductScrollPane.setViewportView(tableDesign.getTable());
        ProductScrollPane.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        // Load dữ liệu
        loadTableData(sanPhamBUS.layTatCaSanPham());
    }

    private void loadTableData(List<SanPham> danhSachSanPham) {
        DefaultTableModel model = (DefaultTableModel) tableDesign.getTable().getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        for (SanPham sp : danhSachSanPham) {
            double vat = sp.getThueVAT();

            // Lấy giá nhập theo FIFO từ lô hàng còn tồn (SĐK này, lô còn hạn, còn tồn) - CHƯA VAT
            double giaNhapFIFO = layGiaNhapFIFO(sp);
            double giaNhapDaVAT = giaNhapFIFO * (1 + vat);
            double giaBanDaVAT = sp.getGiaBan() * (1 + vat);
            Object[] row = {
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                sp.getDonViTinh() != null ? sp.getDonViTinh().getTenDonVi() : "",
                sp.getSoDangKy(),
                sp.getQuocGiaSanXuat(),
                getLoaiSanPhamDisplay(sp.getLoaiSanPham()),
                // Cột giá nhập chỉ hiển thị GIÁ NHẬP CHƯA VAT (giá lô FIFO)
                formatCurrency(giaNhapFIFO),
                percentFormat.format(vat * 100) + " %",
                formatCurrency(giaBanDaVAT),
                sp.isHoatDong() ? "Đang bán" : "Ngưng bán"
            };
            model.addRow(row);
        }
    }

    /**
     * Lấy giá nhập theo FIFO cho 1 sản phẩm: - Ưu tiên lô còn tồn kho & còn hạn
     * (HSD >= hôm nay), lấy lô có HSD nhỏ nhất. - Nếu lô có giaNhapLo > 0 thì
     * dùng giá đó, ngược lại fallback về sp.getGiaNhap(). - Nếu không có lô nào
     * phù hợp thì trả về sp.getGiaNhap().
     */
    private double layGiaNhapFIFO(SanPham sp) {
        try {
            if (loHangDAO == null || sp == null || sp.getMaSanPham() == null) {
                return sp != null ? sp.getGiaNhap() : 0;
            }
            List<LoHang> dsLo = loHangDAO.findByMaSanPham(sp.getMaSanPham());
            if (dsLo == null || dsLo.isEmpty()) {
                return sp.getGiaNhap();
            }

            LocalDate today = LocalDate.now();
            return dsLo.stream()
                    // Chỉ lấy những lô:
                    // - Còn tồn kho > 0
                    // - Còn hoạt động (trangThai = true)
                    // - Còn hạn (HSD >= hôm nay hoặc HSD null)
                    // - ĐÃ CÓ giá nhập chuẩn trên cột giaNhapLo (> 0)
                    .filter(lh -> lh.getTonKho() > 0
                    && lh.isTrangThai()
                    && lh.getGiaNhapLo() > 0
                    && (lh.getHanSuDung() == null || !lh.getHanSuDung().isBefore(today)))
                    .sorted(Comparator.comparing(LoHang::getHanSuDung)
                            .thenComparing(LoHang::getMaLoHang))
                    .mapToDouble(LoHang::getGiaNhapLo)
                    .findFirst()
                    .orElse(sp.getGiaNhap());
        } catch (Exception ex) {
            System.err.println("[GD_QuanLySanPham] Lỗi khi lấy giá nhập FIFO: " + ex.getMessage());
            return sp.getGiaNhap();
        }
    }

    /**
     * Tính giá bán đề xuất theo "lãi chuẩn" dựa trên giá nhập (chưa VAT). Các
     * bậc lãi ví dụ: - 0 - 50.000 : lãi 30% - 50.001-100.000: lãi 25% -
     * 100.001-300.000: lãi 20% - > 300.000 : lãi 15%
     */
    private double tinhGiaBanTheoLaiChuan(double giaNhapChuaVAT) {
        if (giaNhapChuaVAT <= 0) {
            return 0;
        }

        double tyLeLai;
        if (giaNhapChuaVAT <= 50_000) {
            tyLeLai = 0.30;
        } else if (giaNhapChuaVAT <= 100_000) {
            tyLeLai = 0.25;
        } else if (giaNhapChuaVAT <= 300_000) {
            tyLeLai = 0.20;
        } else {
            tyLeLai = 0.15;
        }

        return giaNhapChuaVAT * (1 + tyLeLai);
    }

    private String getLoaiSanPhamDisplay(LoaiSanPham loai) {
        if (loai == null) {
            return "";
        }
        switch (loai) {
            case THUOC:
                return "Thuốc";
            case THUOC_KE_DON:
                return "Thuốc kê đơn";
            case VAT_TU_Y_TE:
                return "Vật tư y tế";
            case THUC_PHAM_CHUC_NANG:
                return "Thực phẩm chức năng";
            case CHAM_SOC_TRE_EM:
                return "Chăm sóc trẻ em";
            case THIET_BI_Y_TE:
                return "Thiết bị y tế";
            default:
                return "";
        }
    }

    private LoaiSanPham getLoaiSanPhamFromDisplay(String display) {
        switch (display) {
            case "Thuốc":
                return LoaiSanPham.THUOC;
            case "Thuốc kê đơn":
                return LoaiSanPham.THUOC_KE_DON;
            case "Vật tư y tế":
                return LoaiSanPham.VAT_TU_Y_TE;
            case "Thực phẩm chức năng":
                return LoaiSanPham.THUC_PHAM_CHUC_NANG;
            case "Chăm sóc trẻ em":
                return LoaiSanPham.CHAM_SOC_TRE_EM;
            case "Thiết bị y tế":
                return LoaiSanPham.THIET_BI_Y_TE;
            default:
                return null;
        }
    }

    private void loadComboBoxData() {
        // Load loại sản phẩm cho ComboBox thêm
        cbbProductTypeAdd.removeAllItems();
        cbbProductTypeAdd.addItem("Thuốc");
        cbbProductTypeAdd.addItem("Thuốc kê đơn");
        cbbProductTypeAdd.addItem("Vật tư y tế");
        cbbProductTypeAdd.addItem("Thực phẩm chức năng");
        cbbProductTypeAdd.addItem("Chăm sóc trẻ em");
        cbbProductTypeAdd.addItem("Thiết bị y tế");

        // Load loại sản phẩm cho ComboBox sửa
        cbbProductTypeEdit.removeAllItems();
        cbbProductTypeEdit.addItem("Thuốc");
        cbbProductTypeEdit.addItem("Thuốc kê đơn");
        cbbProductTypeEdit.addItem("Vật tư y tế");
        cbbProductTypeEdit.addItem("Thực phẩm chức năng");
        cbbProductTypeEdit.addItem("Chăm sóc trẻ em");
        cbbProductTypeEdit.addItem("Thiết bị y tế");

        // Load đơn vị tính
        try {
            List<DonViTinh> danhSachDonVi = donViTinhDAO.findAll();
            System.out.println("DEBUG: Số lượng đơn vị tính: " + danhSachDonVi.size());

            comboUnitAdd.removeAllItems();
            comboUnitEdit.removeAllItems();

            if (danhSachDonVi.isEmpty()) {
                System.out.println("CẢNH BÁO: Không có đơn vị tính nào trong database!");
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Không có đơn vị tính nào trong hệ thống. Vui lòng thêm đơn vị tính trước!");
            } else {
                for (DonViTinh dv : danhSachDonVi) {
                    System.out.println("DEBUG: Thêm đơn vị tính: " + dv.getTenDonVi());
                    comboUnitAdd.addItem(dv.getTenDonVi());
                    comboUnitEdit.addItem(dv.getTenDonVi());
                }
            }
        } catch (Exception e) {
            System.err.println("LỖI khi load đơn vị tính: " + e.getMessage());
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi tải đơn vị tính: " + e.getMessage());
        }
    }

    private JTextField txtTotalQuantity; // TextField hiển thị tổng tồn kho

    private void updateTabQuantity() {
        if (productEdit == null || productEdit.isEmpty()) {
            return;
        }

        // Lấy danh sách lô hàng của sản phẩm
        List<LoHang> danhSachLoHang = loHangDAO.findByMaSanPham(productEdit);

        // Tính tổng tồn kho (CHỈ TÍNH LÔ CÒN HẠN - HSD > hôm nay + 6 tháng)
        LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
        int tongTonKho = danhSachLoHang.stream()
                .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan)) // Lọc lô còn hạn (HSD > ngày giới hạn)
                .mapToInt(LoHang::getTonKho)
                .sum();

        // Kiểm tra có lô hàng còn hạn không (HSD > ngày hiện tại)
        LocalDate today = LocalDate.now();
        boolean coLoHangConHan = danhSachLoHang.stream()
                .anyMatch(lh -> lh.getHanSuDung().isAfter(today) && lh.getTonKho() > 0);

        // Tạo panel chính chứa tất cả
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Tạo panel hiển thị trạng thái sản phẩm
        JPanel statusPanel = createStatusPanel(coLoHangConHan);
        mainPanel.add(statusPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Tạo panel hiển thị tổng tồn kho ở đầu
        JPanel totalPanel = createTotalQuantityPanel(tongTonKho);
        mainPanel.add(totalPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Tạo panel chứa danh sách lô hàng
        if (danhSachLoHang.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có lô hàng nào cho sản phẩm này");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(Box.createVerticalStrut(50));
            mainPanel.add(lblEmpty);
        } else {
            // Thêm header
            JPanel headerPanel = createBatchHeaderPanel();
            mainPanel.add(headerPanel);
            mainPanel.add(Box.createVerticalStrut(10));

            // Thêm từng lô hàng
            for (LoHang loHang : danhSachLoHang) {
                JPanel batchPanel = createBatchPanel(loHang);
                mainPanel.add(batchPanel);
                mainPanel.add(Box.createVerticalStrut(5));
            }
        }

        // Set panel vào ScrollPane
        ScrollPaneTab3.setViewportView(mainPanel);
        ScrollPaneTab3.revalidate();
        ScrollPaneTab3.repaint();
    }

    private JPanel createStatusPanel(boolean coLoHangConHan) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        if (coLoHangConHan) {
            // Đang bán - màu xanh lá
            panel.setBackground(new Color(220, 255, 220)); // Light Green
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));

            JLabel lblStatus = new JLabel("✓ ĐANG BÁN");
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblStatus.setForeground(new Color(0, 128, 0)); // Dark Green
            panel.add(lblStatus);
        } else {
            // Ngưng bán - màu đỏ
            panel.setBackground(new Color(255, 220, 220)); // Light Red
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 20, 60), 2),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));

            JLabel lblStatus = new JLabel("✗ NGƯNG BÁN");
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblStatus.setForeground(new Color(178, 34, 34)); // Fire Brick Red
            panel.add(lblStatus);

            JLabel lblReason = new JLabel("(Không còn lô hàng còn hạn)");
            lblReason.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lblReason.setForeground(Color.GRAY);
            panel.add(lblReason);
        }

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return panel;
    }

    private JPanel createTotalQuantityPanel(int tongTonKho) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(new Color(240, 248, 255)); // Alice Blue
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Label "Tổng tồn kho:"
        JLabel lblTitle = new JLabel("Tổng tồn kho:");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(25, 25, 112)); // Midnight Blue
        panel.add(lblTitle);

        // TextField hiển thị tổng - Tạo mới hoặc cập nhật
        if (txtTotalQuantity == null) {
            txtTotalQuantity = new JTextField(String.valueOf(tongTonKho));
            txtTotalQuantity.setFont(new Font("Segoe UI", Font.BOLD, 20));
            txtTotalQuantity.setForeground(new Color(0, 100, 0)); // Dark Green
            txtTotalQuantity.setHorizontalAlignment(JTextField.CENTER);
            txtTotalQuantity.setEditable(false);
            txtTotalQuantity.setPreferredSize(new Dimension(150, 40));
            txtTotalQuantity.setBackground(Color.WHITE);
            txtTotalQuantity.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        } else {
            // Cập nhật giá trị nếu đã tồn tại
            txtTotalQuantity.setText(String.valueOf(tongTonKho));
        }
        panel.add(txtTotalQuantity);

        // Label đơn vị
        JLabel lblUnit = new JLabel("(sản phẩm)");
        lblUnit.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblUnit.setForeground(Color.GRAY);
        panel.add(lblUnit);

        return panel;
    }

    private JPanel createBatchHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 6, 10, 0));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        String[] headers = {"Mã lô", "Tên lô", "Hạn sử dụng", "Tồn kho", "Trạng thái", "Thao tác"};
        for (String header : headers) {
            JLabel lblHeader = new JLabel(header);
            lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
            headerPanel.add(lblHeader);
        }

        return headerPanel;
    }

    private JPanel createBatchPanel(LoHang loHang) {
        JPanel batchPanel = new JPanel();
        batchPanel.setLayout(new GridLayout(1, 6, 10, 0));
        batchPanel.setBackground(Color.WHITE);
        batchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        batchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Mã lô
        JLabel lblMaLo = new JLabel(loHang.getMaLoHang());
        lblMaLo.setHorizontalAlignment(SwingConstants.CENTER);
        batchPanel.add(lblMaLo);

        // Tên lô
        JLabel lblTenLo = new JLabel(loHang.getTenLoHang());
        lblTenLo.setHorizontalAlignment(SwingConstants.CENTER);
        batchPanel.add(lblTenLo);

        // Hạn sử dụng
        String hanSuDung = loHang.getHanSuDung() != null
                ? dateFormat.format(java.sql.Date.valueOf(loHang.getHanSuDung())) : "N/A";
        JLabel lblHanSuDung = new JLabel(hanSuDung);
        lblHanSuDung.setHorizontalAlignment(SwingConstants.CENTER);
        batchPanel.add(lblHanSuDung);

        // Tồn kho - Dùng JTextField để có thể edit trực tiếp
        JTextField txtTonKho = new JTextField(String.valueOf(loHang.getTonKho()));
        txtTonKho.setHorizontalAlignment(SwingConstants.CENTER);
        txtTonKho.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTonKho.setPreferredSize(new Dimension(80, 30));

        // Thêm listener để cập nhật khi user nhập xong (nhấn Enter hoặc mất focus)
        txtTonKho.addActionListener(e -> updateBatchQuantityFromTextField(loHang, txtTonKho));
        txtTonKho.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                updateBatchQuantityFromTextField(loHang, txtTonKho);
            }
        });

        batchPanel.add(txtTonKho);

        // Trạng thái
        JLabel lblTrangThai = new JLabel(loHang.isTrangThai() ? "Hoạt động" : "Ngừng");
        lblTrangThai.setHorizontalAlignment(SwingConstants.CENTER);
        lblTrangThai.setForeground(loHang.isTrangThai() ? new Color(34, 139, 34) : Color.RED);
        batchPanel.add(lblTrangThai);

        // Panel thao tác (tăng/giảm)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton btnDecrease = new JButton("-");
        btnDecrease.setPreferredSize(new Dimension(40, 30));
        btnDecrease.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDecrease.addActionListener(e -> updateBatchQuantity(loHang, -1, txtTonKho));

        JButton btnIncrease = new JButton("+");
        btnIncrease.setPreferredSize(new Dimension(40, 30));
        btnIncrease.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIncrease.addActionListener(e -> updateBatchQuantity(loHang, 1, txtTonKho));

        actionPanel.add(btnDecrease);
        actionPanel.add(btnIncrease);

        batchPanel.add(actionPanel);

        return batchPanel;
    }

    private void updateBatchQuantity(LoHang loHang, int delta, JTextField txtTonKho) {
        try {
            // Kiểm tra sản phẩm có đang hoạt động không (chỉ kiểm tra khi TĂNG số lượng)
            if (delta > 0) {
                SanPham sanPham = loHang.getSanPham();
                if (sanPham != null && !sanPham.isHoatDong()) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Không thể thêm số lượng! Sản phẩm đã ngừng hoạt động");
                    return;
                }
            }

            int currentQuantity = loHang.getTonKho();
            int newQuantity = currentQuantity + delta;

            // Kiểm tra số lượng không được âm
            if (newQuantity < 0) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Số lượng tồn kho không thể nhỏ hơn 0");
                return;
            }

            // Cập nhật vào database
            loHang.setTonKho(newQuantity);
            boolean success = loHangDAO.update(loHang);

            if (success) {
                // Cập nhật textfield hiển thị trong panel lô hàng
                txtTonKho.setText(String.valueOf(newQuantity));

                // Cập nhật tổng tồn kho trong TextField
                updateTotalQuantityTextField(loHang.getSanPham().getMaSanPham());

                // Cập nhật tổng tồn kho trong bảng sản phẩm
                updateProductTotalQuantityInTable(loHang.getSanPham().getMaSanPham());

                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Cập nhật số lượng thành công: " + loHang.getTenLoHang());
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Lỗi khi cập nhật số lượng");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi: " + e.getMessage());
        }
    }

    // Method mới: Cập nhật khi user nhập trực tiếp vào TextField
    private void updateBatchQuantityFromTextField(LoHang loHang, JTextField txtTonKho) {
        try {
            String input = txtTonKho.getText().trim();

            // Kiểm tra input rỗng
            if (input.isEmpty()) {
                txtTonKho.setText(String.valueOf(loHang.getTonKho())); // Reset về giá trị cũ
                return;
            }

            // Parse số lượng mới
            int newQuantity = Integer.parseInt(input);

            // Kiểm tra số lượng không được âm
            if (newQuantity < 0) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Số lượng tồn kho không thể nhỏ hơn 0");
                txtTonKho.setText(String.valueOf(loHang.getTonKho())); // Reset về giá trị cũ
                return;
            }

            // Nếu giá trị không thay đổi thì không cần cập nhật
            if (newQuantity == loHang.getTonKho()) {
                return;
            }

            // Kiểm tra sản phẩm có đang hoạt động không (CHỈ khi TĂNG số lượng)
            if (newQuantity > loHang.getTonKho()) {
                SanPham sanPham = loHang.getSanPham();
                if (sanPham != null && !sanPham.isHoatDong()) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            Notifications.Location.TOP_CENTER,
                            "Không thể thêm số lượng! Sản phẩm đã ngừng hoạt động");
                    txtTonKho.setText(String.valueOf(loHang.getTonKho())); // Reset về giá trị cũ
                    return;
                }
            }

            // Cập nhật vào database
            loHang.setTonKho(newQuantity);
            boolean success = loHangDAO.update(loHang);

            if (success) {
                // Cập nhật tổng tồn kho trong TextField
                updateTotalQuantityTextField(loHang.getSanPham().getMaSanPham());

                // Cập nhật tổng tồn kho trong bảng sản phẩm
                updateProductTotalQuantityInTable(loHang.getSanPham().getMaSanPham());

                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Cập nhật số lượng thành công: " + loHang.getTenLoHang() + " = " + newQuantity);
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Lỗi khi cập nhật số lượng");
                txtTonKho.setText(String.valueOf(loHang.getTonKho())); // Reset về giá trị cũ
            }
        } catch (NumberFormatException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Vui lòng nhập số nguyên hợp lệ!");
            txtTonKho.setText(String.valueOf(loHang.getTonKho())); // Reset về giá trị cũ
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi: " + e.getMessage());
            txtTonKho.setText(String.valueOf(loHang.getTonKho())); // Reset về giá trị cũ
            e.printStackTrace();
        }
    }

    private void updateTotalQuantityTextField(String maSanPham) {
        // Tính tổng tồn kho (CHỈ TÍNH LÔ CÒN HẠN - HSD > hôm nay + 6 tháng)
        List<LoHang> danhSachLoHang = loHangDAO.findByMaSanPham(maSanPham);
        LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
        int tongTonKho = danhSachLoHang.stream()
                .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan)) // Lọc lô còn hạn (HSD > ngày giới hạn)
                .mapToInt(LoHang::getTonKho)
                .sum();

        // Cập nhật vào TextField
        if (txtTotalQuantity != null) {
            txtTotalQuantity.setText(String.valueOf(tongTonKho));
        }
    }

    private void updateProductTotalQuantityInTable(String maSanPham) {
        // Bảng không còn hiển thị tồn kho nên bỏ qua việc cập nhật trực tiếp.
    }

    private void addIconFeature() {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
        jLabelVatEdit = new javax.swing.JLabel();
        txtProductVAT1 = new javax.swing.JTextField();
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
        jLabelVatAdd = new javax.swing.JLabel();
        txtProductVAT = new javax.swing.JTextField();
        pnAll = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtSearchNamePD = new javax.swing.JTextField();
        txtSearchSDKPD = new javax.swing.JTextField();
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
        modelEditProduct.setMinimumSize(new java.awt.Dimension(1350, 900));
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
        // Giá nhập lấy từ lô (FIFO) nên không cho chỉnh sửa tay
        txtProductPurchasePrice1.setEditable(false);
        txtProductPurchasePrice1.setBackground(new java.awt.Color(245, 245, 245));

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

        jLabelVatEdit.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabelVatEdit.setText("Thuế VAT (%)");

        txtProductVAT1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

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
                            .addComponent(jLabelVatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductVAT1)
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
                        .addComponent(jLabelVatEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductVAT1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(30, 30, 30)
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

        // Tab "Thông tin số lượng" đã bị xóa - chỉ xem thông tin sản phẩm
        // tabEdit.addTab("Thông tin số lượng", jPanel11);

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
        modelProductAdd.setMinimumSize(new java.awt.Dimension(1350, 900));
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
        // Giá nhập lấy từ lô (FIFO) nên không cho chỉnh sửa tay
        txtProductPurchasePrice.setEditable(false);
        txtProductPurchasePrice.setBackground(new java.awt.Color(245, 245, 245));

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

        jLabelVatAdd.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabelVatAdd.setText("Thuế VAT (%)");

        txtProductVAT.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

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
                            .addComponent(jLabelVatAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductVAT)
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
                        .addComponent(jLabelVatAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductVAT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(30, 30, 30)
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
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 16, 24));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setMinimumSize(new java.awt.Dimension(600, 50));
        jPanel6.setPreferredSize(new java.awt.Dimension(900, 50));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        txtSearchNamePD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchNamePD.setToolTipText("Nhập tên sản phẩm hoặc số đăng ký");
        txtSearchNamePD.setMinimumSize(new java.awt.Dimension(190, 40));
        txtSearchNamePD.setPreferredSize(new java.awt.Dimension(190, 40));
        txtSearchNamePD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchNamePDActionPerformed(evt);
            }
        });
        jPanel6.add(txtSearchNamePD);

        txtSearchSDKPD.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchSDKPD.setMinimumSize(new java.awt.Dimension(170, 40));
        txtSearchSDKPD.setName(""); // NOI18N
        txtSearchSDKPD.setPreferredSize(new java.awt.Dimension(170, 40));
        txtSearchSDKPD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchSDKPDActionPerformed(evt);
            }
        });

        jPanel6.add(txtSearchSDKPD);

        optionPdType.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        optionPdType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Thuốc", "Vật tư y tế", "Thực phẩm chức năng", "Chăm sóc trẻ em", "Thiết bị y tế" }));
        optionPdType.setMinimumSize(new java.awt.Dimension(170, 40));
        optionPdType.setPreferredSize(new java.awt.Dimension(170, 40));
        optionPdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionPdTypeActionPerformed(evt);
            }
        });
        jPanel6.add(optionPdType);

        optionPdStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        optionPdStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đang bán", "Ngưng bán" }));
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
        actionPanel.setPreferredSize(new java.awt.Dimension(470, 60));
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

        btnViewDetail = new javax.swing.JButton();
        btnViewDetail.setBackground(new java.awt.Color(23, 162, 184));
        btnViewDetail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnViewDetail.setForeground(new java.awt.Color(255, 255, 255));
        btnViewDetail.setText("Xem chi tiết");
        btnViewDetail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnViewDetail.setFocusPainted(false);
        btnViewDetail.setPreferredSize(new java.awt.Dimension(130, 40));
        btnViewDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailActionPerformed(evt);
            }
        });
        actionPanel.add(btnViewDetail);

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
        
        // Thêm tiêu đề "DANH SÁCH THÔNG TIN SẢN PHẨM"
        javax.swing.JPanel titlePanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 12));
        titlePanel.setBackground(new java.awt.Color(23, 162, 184)); // Màu xanh cyan
        javax.swing.JLabel lblTitle = new javax.swing.JLabel("DANH SÁCH THÔNG TIN SẢN PHẨM");
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
        timKiemSanPham();
    }//GEN-LAST:event_btnOpenModalAddSupActionPerformed

    private void txtSearchNamePDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchNamePDActionPerformed
        timKiemSanPham();
    }//GEN-LAST:event_txtSearchNamePDActionPerformed

    private void timKiemSanPham() {
        String tuKhoaTen = txtSearchNamePD.getText().trim().toLowerCase();
        String tuKhoaSDK = txtSearchSDKPD.getText().trim().toLowerCase();
        String loaiSPStr = (String) optionPdType.getSelectedItem();
        String trangThaiStr = (String) optionPdStatus.getSelectedItem();

        List<SanPham> ketQua = sanPhamBUS.layTatCaSanPham();

        // Lọc theo từ khóa (tên hoặc số đăng ký)
        if (!tuKhoaTen.isEmpty()) {
            ketQua = ketQua.stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(tuKhoaTen)
                    || sp.getSoDangKy().toLowerCase().contains(tuKhoaTen)) // Vẫn giữ logic cũ cho ô này
                    .collect(java.util.stream.Collectors.toList());
        }

        if (!tuKhoaSDK.isEmpty()) {
            ketQua = ketQua.stream()
                    .filter(sp -> sp.getSoDangKy().toLowerCase().contains(tuKhoaSDK))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (!"Tất cả".equals(loaiSPStr)) {
            LoaiSanPham loai = getLoaiSanPhamFromDisplay(loaiSPStr);
            ketQua = ketQua.stream()
                    .filter(sp -> sp.getLoaiSanPham() == loai)
                    .collect(java.util.stream.Collectors.toList());
        }

        // Lọc theo trạng thái
        if (!"Tất cả".equals(trangThaiStr)) {
            boolean hoatDong = "Đang bán".equals(trangThaiStr);
            ketQua = ketQua.stream()
                    .filter(sp -> sp.isHoatDong() == hoatDong)
                    .collect(java.util.stream.Collectors.toList());
        }

        loadTableData(ketQua);

        if (ketQua.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Không tìm thấy sản phẩm phù hợp");
        } else {
            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Tìm thấy " + ketQua.size() + " sản phẩm");
        }
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }

        // Lấy mã sản phẩm từ bảng
        String maSP = tableDesign.getTable().getValueAt(selectedRow, 0).toString();

        // Lấy thông tin chi tiết từ database
        Optional<SanPham> spOpt = sanPhamBUS.timSanPhamTheoMa(maSP);
        if (!spOpt.isPresent()) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Không tìm thấy sản phẩm");
            return;
        }

        SanPham sp = spOpt.get();
        productEdit = maSP;

        // Reset TextField tổng tồn kho để tạo mới
        txtTotalQuantity = null;

        // Hiển thị thông tin lên form
        txtProductName1.setText(sp.getTenSanPham());
        txtProductRegisNumber1.setText(sp.getSoDangKy());
        txtProductActiveIngre1.setText(sp.getHoatChat());
        txtProductDosage1.setText(sp.getLieuDung());
        txtProductPakaging1.setText(sp.getCachDongGoi());
        txtProductManufacturer1.setText(sp.getNhaSanXuat());
        txtCountryOfOrigin1.setText(sp.getQuocGiaSanXuat());
        // Giá nhập trong form sửa là GIÁ NHẬP FIFO (chưa VAT) để người quản lý dễ căn lãi
        double giaNhapFIFO = layGiaNhapFIFO(sp);
        txtProductPurchasePrice1.setText(String.valueOf((int) giaNhapFIFO));

        // Tính giá bán đề xuất theo lãi chuẩn nếu sản phẩm chưa có giá bán hợp lệ
        double giaBanHienTai = sp.getGiaBan();
        if (giaBanHienTai <= 0 && giaNhapFIFO > 0) {
            double giaBanDeXuat = tinhGiaBanTheoLaiChuan(giaNhapFIFO);
            txtProductSellingPrice1.setText(String.valueOf((int) Math.round(giaBanDeXuat)));
        } else {
            txtProductSellingPrice1.setText(String.valueOf((int) giaBanHienTai));
        }
        if (txtProductVAT1 != null) {
            txtProductVAT1.setText(percentFormat.format(sp.getThueVAT() * 100));
        }

        // Set loại sản phẩm
        if (sp.getLoaiSanPham() != null) {
            cbbProductTypeEdit.setSelectedItem(getLoaiSanPhamDisplay(sp.getLoaiSanPham()));
        }

        // Set đơn vị tính
        if (sp.getDonViTinh() != null) {
            comboUnitEdit.setSelectedItem(sp.getDonViTinh().getTenDonVi());
        }

        // Load hình ảnh nếu có
        loadProductImage(sp);

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

    private void btnViewDetailActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tableDesign.getTable().getSelectedRow();
        if (selectedRow == -1) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Vui lòng chọn sản phẩm để xem chi tiết!");
            return;
        }

        // Lấy mã sản phẩm từ bảng
        String maSP = tableDesign.getTable().getValueAt(selectedRow, 0).toString();

        // Lấy thông tin chi tiết từ database
        Optional<SanPham> spOpt = sanPhamBUS.timSanPhamTheoMa(maSP);
        if (!spOpt.isPresent()) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Không tìm thấy sản phẩm");
            return;
        }

        SanPham sp = spOpt.get();

        // Tạo dialog xem chi tiết
        showProductDetailDialog(sp);
    }

    private void showProductDetailDialog(SanPham sp) {
        // Tạo dialog
        JDialog detailDialog = new JDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết sản phẩm", true);
        detailDialog.setSize(900, 700);
        detailDialog.setLocationRelativeTo(this);

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(23, 162, 184));
        headerPanel.setPreferredSize(new Dimension(900, 60));
        JLabel lblTitle = new JLabel("THÔNG TIN CHI TIẾT SẢN PHẨM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // Body panel - chứa 2 cột: thông tin bên trái, hình ảnh bên phải
        JPanel bodyPanel = new JPanel(new BorderLayout(20, 0));
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Panel bên trái - Thông tin chi tiết
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        // Thêm thông tin sản phẩm
        addDetailRow(infoPanel, "Mã sản phẩm:", sp.getMaSanPham());
        addDetailRow(infoPanel, "Tên sản phẩm:", sp.getTenSanPham());
        addDetailRow(infoPanel, "Số đăng ký:", sp.getSoDangKy());
        addDetailRow(infoPanel, "Hoạt chất:", sp.getHoatChat());
        addDetailRow(infoPanel, "Liều dùng:", sp.getLieuDung());
        addDetailRow(infoPanel, "Cách đóng gói:", sp.getCachDongGoi());
        addDetailRow(infoPanel, "Nhà sản xuất:", sp.getNhaSanXuat());
        addDetailRow(infoPanel, "Quốc gia sản xuất:", sp.getQuocGiaSanXuat());
        // Giá nhập hiển thị chi tiết theo FIFO từ lô còn tồn
        double giaNhapFIFO = layGiaNhapFIFO(sp);
        addDetailRow(infoPanel, "Giá nhập (chưa VAT) (FIFO):", String.format("%,d VNĐ", (int) giaNhapFIFO));
        addDetailRow(infoPanel, "Giá bán (chưa VAT):", String.format("%,d VNĐ", (int) sp.getGiaBan()));
        addDetailRow(infoPanel, "Thuế VAT:", percentFormat.format(sp.getThueVAT() * 100) + " %");
        double giaNhapDaVAT = giaNhapFIFO * (1 + sp.getThueVAT());
        double giaBanDaVAT = sp.getGiaBan() * (1 + sp.getThueVAT());
        addDetailRow(infoPanel, "Giá nhập (đã VAT):", String.format("%,d VNĐ", (int) Math.round(giaNhapDaVAT)));
        addDetailRow(infoPanel, "Giá bán (đã VAT):", String.format("%,d VNĐ", (int) Math.round(giaBanDaVAT)));

        if (sp.getLoaiSanPham() != null) {
            addDetailRow(infoPanel, "Loại sản phẩm:", getLoaiSanPhamDisplay(sp.getLoaiSanPham()));
        }

        if (sp.getDonViTinh() != null) {
            addDetailRow(infoPanel, "Đơn vị tính:", sp.getDonViTinh().getTenDonVi());
        }

        // Tính tổng tồn kho (CHỈ TÍNH LÔ CÒN HẠN - HSD > hôm nay + 6 tháng)
        List<LoHang> danhSachLoHang = loHangDAO.findByMaSanPham(sp.getMaSanPham());
        LocalDate ngayGioiHan = LocalDate.now().plusMonths(6);
        int tongTonKho = danhSachLoHang.stream()
                .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan)) // Lọc lô còn hạn (HSD > ngày giới hạn)
                .mapToInt(LoHang::getTonKho)
                .sum();

        // Đếm số lượng lô hàng CÒN HẠN
        long soLuongLoHangConHan = danhSachLoHang.stream()
                .filter(lh -> lh.getHanSuDung().isAfter(ngayGioiHan)) // Chỉ đếm lô còn hạn (HSD > ngày giới hạn)
                .count();

        addDetailRow(infoPanel, "Tổng tồn kho:", String.valueOf(tongTonKho));
        addDetailRow(infoPanel, "Số lượng lô hàng:", String.valueOf(soLuongLoHangConHan));

        // Panel bên phải - Hình ảnh sản phẩm
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Hình ảnh sản phẩm"));
        imagePanel.setPreferredSize(new Dimension(200, 200));

        if (sp.getHinhAnh() != null && !sp.getHinhAnh().isEmpty()) {
            try {
                // Thử load từ resources
                java.net.URL imageUrl = getClass().getResource("/img/" + sp.getHinhAnh());

                if (imageUrl != null) {
                    ImageIcon imageIcon = new ImageIcon(imageUrl);
                    Image scaledImage = imageIcon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                    JLabel lblImage = new JLabel(new ImageIcon(scaledImage));
                    lblImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                    imagePanel.add(lblImage);
                    System.out.println("✅ Đã load hình ảnh: " + sp.getHinhAnh());
                } else {
                    // Thử load từ đường dẫn trực tiếp
                    String imagePath = "src/main/resources/img/" + sp.getHinhAnh();
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        ImageIcon imageIcon = new ImageIcon(imagePath);
                        Image scaledImage = imageIcon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                        JLabel lblImage = new JLabel(new ImageIcon(scaledImage));
                        lblImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                        imagePanel.add(lblImage);
                        System.out.println("✅ Đã load hình ảnh từ file: " + imagePath);
                    } else {
                        JLabel lblNoImage = new JLabel("Không tìm thấy hình ảnh");
                        lblNoImage.setForeground(Color.GRAY);
                        imagePanel.add(lblNoImage);
                        System.err.println("❌ Không tìm thấy file: " + imagePath);
                    }
                }
            } catch (Exception e) {
                JLabel lblError = new JLabel("Lỗi load hình ảnh");
                lblError.setForeground(Color.RED);
                imagePanel.add(lblError);
                System.err.println("❌ Lỗi load hình ảnh: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JLabel lblNoImage = new JLabel("Chưa có hình ảnh");
            lblNoImage.setForeground(Color.GRAY);
            imagePanel.add(lblNoImage);
        }

        // Thêm các panel vào bodyPanel
        bodyPanel.add(infoPanel, BorderLayout.CENTER);   // Thông tin bên trái
        bodyPanel.add(imagePanel, BorderLayout.EAST);    // Hình ảnh bên phải

        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnClose = new JButton("Đóng");
        btnClose.setPreferredSize(new Dimension(100, 35));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.addActionListener(e -> detailDialog.dispose());
        buttonPanel.add(btnClose);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        detailDialog.add(mainPanel);
        detailDialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblKey.setPreferredSize(new Dimension(180, 25));

        JLabel lblValue = new JLabel(value != null ? value : "N/A");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        rowPanel.add(lblKey, BorderLayout.WEST);
        rowPanel.add(lblValue, BorderLayout.CENTER);

        panel.add(rowPanel);
    }

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

    /**
     * Copy file hình ảnh vào thư mục resources/img
     *
     * @param imageIcon ImageIcon chứa đường dẫn file gốc
     * @return Tên file đã copy, hoặc null nếu thất bại
     */
    private String copyImageToResources(ImageIcon imageIcon) {
        if (imageIcon == null || imageIcon.getDescription() == null) {
            return null;
        }

        try {
            File sourceFile = new File(imageIcon.getDescription());
            if (!sourceFile.exists()) {
                System.err.println("File không tồn tại: " + sourceFile.getAbsolutePath());
                return null;
            }

            // Tạo thư mục đích nếu chưa tồn tại
            String projectPath = System.getProperty("user.dir");
            File destDir = new File(projectPath, "src/main/resources/img");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            // Tạo tên file duy nhất (thêm timestamp để tránh trùng)
            String originalFileName = sourceFile.getName();
            String fileName = System.currentTimeMillis() + "_" + originalFileName;
            File destFile = new File(destDir, fileName);

            // Copy file
            java.nio.file.Files.copy(
                    sourceFile.toPath(),
                    destFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("Đã copy hình ảnh: " + destFile.getAbsolutePath());
            return fileName;

        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi copy hình ảnh: " + e.getMessage());
            return null;
        }
    }

    /**
     * Load hình ảnh sản phẩm vào label
     */
    private void loadProductImage(SanPham sanPham) {
        if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
            try {
                // Thử load từ đường dẫn tuyệt đối (nếu file được chọn từ JFileChooser)
                File imageFile = new File(sanPham.getHinhAnh());
                ImageIcon icon = null;

                if (imageFile.exists()) {
                    // File tồn tại với đường dẫn tuyệt đối
                    icon = new ImageIcon(sanPham.getHinhAnh());
                } else {
                    // Thử load từ resources
                    java.net.URL imgURL = getClass().getResource("/img/" + sanPham.getHinhAnh());
                    if (imgURL != null) {
                        icon = new ImageIcon(imgURL);
                    }
                }

                if (icon != null && icon.getIconWidth() > 0) {
                    // Scale image to fit label
                    Image img = icon.getImage().getScaledInstance(265, 265, Image.SCALE_SMOOTH);
                    lblImageEdit.setIcon(new ImageIcon(img));

                    // Lưu icon gốc để có thể lấy tên file sau này
                    imageProductEdit = new ImageIcon(sanPham.getHinhAnh());
                    imageProductEdit.setDescription(sanPham.getHinhAnh());
                } else {
                    // Không load được hình, hiển thị placeholder
                    lblImageEdit.setIcon(null);
                    lblImageEdit.setText("No Image");
                }
            } catch (Exception e) {
                lblImageEdit.setIcon(null);
                lblImageEdit.setText("No Image");
                e.printStackTrace();
            }
        } else {
            // Không có hình ảnh
            lblImageEdit.setIcon(null);
            lblImageEdit.setText("No Image");
        }
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

    private void txtSearchSDKPDActionPerformed(java.awt.event.ActionEvent evt) {
        timKiemSanPham();
    }

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        themSanPham();
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void themSanPham() {
        try {
            // Validate input
            String tenSP = txtProductName.getText().trim();
            if (tenSP.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập tên sản phẩm");
                txtProductName.requestFocus();
                return;
            }

            String soDangKy = txtProductRegisNumber.getText().trim();
            if (soDangKy.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập số đăng ký");
                txtProductRegisNumber.requestFocus();
                return;
            }

            String hoatChat = txtProductActiveIngre.getText().trim();
            if (hoatChat.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập hoạt chất");
                txtProductActiveIngre.requestFocus();
                return;
            }

            String lieuDung = txtProductDosage.getText().trim();
            if (lieuDung.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập liều lượng");
                txtProductDosage.requestFocus();
                return;
            }

            String cachDongGoi = txtProductPakaging.getText().trim();
            if (cachDongGoi.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập quy trình đóng gói");
                txtProductPakaging.requestFocus();
                return;
            }

            String nhaSanXuat = txtProductManufacturer.getText().trim();
            if (nhaSanXuat.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập nhà sản xuất");
                txtProductManufacturer.requestFocus();
                return;
            }

            String quocGia = txtCountryOfOrigin.getText().trim();
            if (quocGia.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập xuất xứ");
                txtCountryOfOrigin.requestFocus();
                return;
            }

            String giaBanStr = txtProductSellingPrice.getText().trim();
            if (giaBanStr.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập giá bán");
                txtProductSellingPrice.requestFocus();
                return;
            }

            double giaBan;
            try {
                giaBan = Double.parseDouble(giaBanStr);
                if (giaBan <= 0) {
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Giá bán phải lớn hơn 0");
                    txtProductSellingPrice.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Giá bán không hợp lệ");
                txtProductSellingPrice.requestFocus();
                return;
            }

            // Validate VAT
            String vatStr = txtProductVAT.getText().trim();
            double vatPercent = 0;
            if (!vatStr.isEmpty()) {
                try {
                    vatPercent = Double.parseDouble(vatStr);
                    if (vatPercent < 0 || vatPercent > 100) {
                        Notifications.getInstance().show(Notifications.Type.WARNING,
                                Notifications.Location.TOP_CENTER,
                                "Thuế VAT phải trong khoảng 0 - 100%");
                        txtProductVAT.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Thuế VAT không hợp lệ");
                    txtProductVAT.requestFocus();
                    return;
                }
            }
            double thueVAT = vatPercent / 100.0;

            // Lấy loại sản phẩm
            String loaiSPStr = (String) cbbProductTypeAdd.getSelectedItem();
            LoaiSanPham loaiSP = getLoaiSanPhamFromDisplay(loaiSPStr);
            if (loaiSP == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng chọn loại sản phẩm");
                return;
            }

            // Lấy đơn vị tính
            String tenDonVi = (String) comboUnitAdd.getSelectedItem();
            if (tenDonVi == null || tenDonVi.trim().isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng chọn đơn vị tính");
                return;
            }

            DonViTinh donViTinh = null;
            try {
                donViTinh = donViTinhDAO.findByName(tenDonVi);
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Lỗi khi lấy đơn vị tính: " + ex.getMessage());
                ex.printStackTrace();
                return;
            }

            if (donViTinh == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Không tìm thấy đơn vị tính: " + tenDonVi);
                return;
            }

            // Tạo mã sản phẩm tự động
            String maSP = taoMaSanPham();

            // Tạo đối tượng sản phẩm
            SanPham sanPham = new SanPham();
            sanPham.setMaSanPham(maSP);
            sanPham.setTenSanPham(tenSP);
            sanPham.setSoDangKy(soDangKy);
            sanPham.setHoatChat(hoatChat);
            sanPham.setLieuDung(lieuDung);
            sanPham.setCachDongGoi(cachDongGoi);
            sanPham.setNhaSanXuat(nhaSanXuat);
            sanPham.setQuocGiaSanXuat(quocGia);
            // Giá nhập sẽ được lấy từ lô (FIFO), ở đây chỉ lưu 0 làm giá mặc định
            sanPham.setGiaNhap(0);
            sanPham.setGiaBan(giaBan);
            sanPham.setLoaiSanPham(loaiSP);
            sanPham.setDonViTinh(donViTinh);
            sanPham.setHoatDong(true); // Mặc định là đang hoạt động
            sanPham.setThueVAT(thueVAT);

            // Xử lý hình ảnh
            if (imageProductAdd != null) {
                String imageName = copyImageToResources(imageProductAdd);
                if (imageName != null) {
                    sanPham.setHinhAnh(imageName);
                }
            }

            // Thêm vào database
            boolean success = sanPhamBUS.taoSanPham(sanPham);

            if (success) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Thêm sản phẩm thành công: " + tenSP);

                // Reset form
                resetFormThemSanPham();

                // Đóng modal
                modelProductAdd.dispose();

                // Reload bảng
                loadTableData(sanPhamBUS.layTatCaSanPham());
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Thêm sản phẩm thất bại");
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String taoMaSanPham() {
        String maCuoi = sanPhamBUS.layMaSanPhamCuoi();
        if (maCuoi == null) {
            return "SP00001";
        }

        String phanSo = maCuoi.substring(2); // Bỏ "SP"
        int soTiepTheo = Integer.parseInt(phanSo) + 1;
        return String.format("SP%05d", soTiepTheo);
    }

    private void resetFormThemSanPham() {
        txtProductName.setText("");
        txtProductRegisNumber.setText("");
        txtProductActiveIngre.setText("");
        txtProductDosage.setText("");
        txtProductPakaging.setText("");
        txtProductManufacturer.setText("");
        txtCountryOfOrigin.setText("");
        txtProductPurchasePrice.setText("");
        txtProductSellingPrice.setText("");
        if (txtProductVAT != null) {
            txtProductVAT.setText("");
        }
        cbbProductTypeAdd.setSelectedIndex(0);
        comboUnitAdd.setSelectedIndex(0);
        lblImage.setIcon(null);
        imageProductAdd = null;
    }

    private void modelProductAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modelProductAddMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_modelProductAddMouseClicked

    private void cbbProductTypeAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbProductTypeAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbProductTypeAddActionPerformed

    private void btnExitProductADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitProductADDActionPerformed
        resetFormThemSanPham();
        modelProductAdd.dispose();
    }//GEN-LAST:event_btnExitProductADDActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed

    }//GEN-LAST:event_btnEditActionPerformed

    private void optionPdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionPdTypeActionPerformed
        timKiemSanPham();
    }//GEN-LAST:event_optionPdTypeActionPerformed

    private void optionPdStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionPdStatusActionPerformed
        timKiemSanPham();
    }//GEN-LAST:event_optionPdStatusActionPerformed

    private void tabEditStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabEditStateChanged

    }//GEN-LAST:event_tabEditStateChanged

    private void cbbProductTypeEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbProductTypeEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbProductTypeEditActionPerformed

    private void btnEditProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProductActionPerformed
        suaSanPham();
    }//GEN-LAST:event_btnEditProductActionPerformed

    private void suaSanPham() {
        try {
            if (productEdit == null || productEdit.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Không xác định được sản phẩm cần sửa");
                return;
            }

            // Validate input
            String tenSP = txtProductName1.getText().trim();
            if (tenSP.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập tên sản phẩm");
                txtProductName1.requestFocus();
                return;
            }

            String soDangKy = txtProductRegisNumber1.getText().trim();
            if (soDangKy.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập số đăng ký");
                txtProductRegisNumber1.requestFocus();
                return;
            }

            String hoatChat = txtProductActiveIngre1.getText().trim();
            if (hoatChat.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập hoạt chất");
                txtProductActiveIngre1.requestFocus();
                return;
            }

            String lieuDung = txtProductDosage1.getText().trim();
            if (lieuDung.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập liều lượng");
                txtProductDosage1.requestFocus();
                return;
            }

            String cachDongGoi = txtProductPakaging1.getText().trim();
            if (cachDongGoi.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập quy trình đóng gói");
                txtProductPakaging1.requestFocus();
                return;
            }

            String nhaSanXuat = txtProductManufacturer1.getText().trim();
            if (nhaSanXuat.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập nhà sản xuất");
                txtProductManufacturer1.requestFocus();
                return;
            }

            String quocGia = txtCountryOfOrigin1.getText().trim();
            if (quocGia.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập xuất xứ");
                txtCountryOfOrigin1.requestFocus();
                return;
            }

            String giaBanStr = txtProductSellingPrice1.getText().trim().replace(",", "");
            if (giaBanStr.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập giá bán");
                txtProductSellingPrice1.requestFocus();
                return;
            }

            double giaBan;
            try {
                giaBan = Double.parseDouble(giaBanStr);
                if (giaBan <= 0) {
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Giá bán phải lớn hơn 0");
                    txtProductSellingPrice1.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Giá bán không hợp lệ");
                txtProductSellingPrice1.requestFocus();
                return;
            }

            // Validate VAT
            String vatStr = txtProductVAT1 != null ? txtProductVAT1.getText().trim() : "";
            double vatPercent = 0;
            if (!vatStr.isEmpty()) {
                try {
                    vatPercent = Double.parseDouble(vatStr);
                    if (vatPercent < 0 || vatPercent > 100) {
                        Notifications.getInstance().show(Notifications.Type.WARNING,
                                Notifications.Location.TOP_CENTER,
                                "Thuế VAT phải trong khoảng 0 - 100%");
                        txtProductVAT1.requestFocus();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Notifications.getInstance().show(Notifications.Type.WARNING,
                            Notifications.Location.TOP_CENTER,
                            "Thuế VAT không hợp lệ");
                    txtProductVAT1.requestFocus();
                    return;
                }
            }
            double thueVAT = vatPercent / 100.0;

            // Lấy loại sản phẩm
            String loaiSPStr = (String) cbbProductTypeEdit.getSelectedItem();
            LoaiSanPham loaiSP = getLoaiSanPhamFromDisplay(loaiSPStr);
            if (loaiSP == null) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng chọn loại sản phẩm");
                return;
            }

            // Lấy đơn vị tính
            String tenDonVi = (String) comboUnitEdit.getSelectedItem();
            if (tenDonVi == null || tenDonVi.trim().isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng chọn đơn vị tính");
                return;
            }

            DonViTinh donViTinh = null;
            try {
                donViTinh = donViTinhDAO.findByName(tenDonVi);
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Lỗi khi lấy đơn vị tính: " + ex.getMessage());
                ex.printStackTrace();
                return;
            }

            if (donViTinh == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Không tìm thấy đơn vị tính: " + tenDonVi);
                return;
            }

            // Lấy sản phẩm hiện tại
            Optional<SanPham> spOpt = sanPhamBUS.timSanPhamTheoMa(productEdit);
            if (!spOpt.isPresent()) {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Không tìm thấy sản phẩm");
                return;
            }

            SanPham sanPham = spOpt.get();

            // Cập nhật thông tin
            sanPham.setTenSanPham(tenSP);
            sanPham.setSoDangKy(soDangKy);
            sanPham.setHoatChat(hoatChat);
            sanPham.setLieuDung(lieuDung);
            sanPham.setCachDongGoi(cachDongGoi);
            sanPham.setNhaSanXuat(nhaSanXuat);
            sanPham.setQuocGiaSanXuat(quocGia);
            // Giá nhập thực tế sẽ lấy từ lô (FIFO), ở đây chỉ lưu 0 làm giá mặc định
            sanPham.setGiaNhap(0);
            sanPham.setGiaBan(giaBan);
            sanPham.setLoaiSanPham(loaiSP);
            sanPham.setDonViTinh(donViTinh);
            sanPham.setThueVAT(thueVAT);

            // Tự động cập nhật trạng thái dựa trên lô hàng
            List<LoHang> danhSachLoHang = loHangDAO.findByMaSanPham(productEdit);
            LocalDate today = LocalDate.now();
            boolean coLoHangConHan = danhSachLoHang.stream()
                    .anyMatch(lh -> lh.getHanSuDung().isAfter(today) && lh.getTonKho() > 0);
            sanPham.setHoatDong(coLoHangConHan);

            // Xử lý hình ảnh nếu có thay đổi
            if (imageProductEdit != null) {
                // Kiểm tra xem có phải là hình mới được chọn không (có đường dẫn file gốc)
                File imageFile = new File(imageProductEdit.getDescription());
                if (imageFile.exists() && !imageFile.getAbsolutePath().contains("resources")) {
                    // Đây là hình mới, cần copy vào resources
                    String imageName = copyImageToResources(imageProductEdit);
                    if (imageName != null) {
                        sanPham.setHinhAnh(imageName);
                    }
                }
                // Nếu không có thay đổi hình, giữ nguyên hình cũ (không cần set lại)
            }

            // Cập nhật vào database
            boolean success = sanPhamBUS.capNhatSanPham(sanPham);

            if (success) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Cập nhật sản phẩm thành công: " + tenSP);

                // Đóng modal
                modelEditProduct.dispose();

                // Reload bảng
                loadTableData(sanPhamBUS.layTatCaSanPham());
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR,
                        Notifications.Location.TOP_CENTER,
                        "Cập nhật sản phẩm thất bại");
            }

        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void btnExitProductADD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitProductADD1ActionPerformed
        modelEditProduct.dispose();
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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEditProduct;
    private javax.swing.JButton btnExitProductADD;
    private javax.swing.JButton btnExitProductADD1;
    private javax.swing.JButton btnOpenModalAddSup;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnViewDetail;
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
    private javax.swing.JLabel jLabelVatAdd;
    private javax.swing.JLabel jLabelVatEdit;
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
    private javax.swing.JTextField txtProductVAT;
    private javax.swing.JTextField txtProductVAT1;
    private javax.swing.JTextField txtSearchNamePD;
    private javax.swing.JTextField txtSearchSDKPD;
    // End of variables declaration//GEN-END:variables

}

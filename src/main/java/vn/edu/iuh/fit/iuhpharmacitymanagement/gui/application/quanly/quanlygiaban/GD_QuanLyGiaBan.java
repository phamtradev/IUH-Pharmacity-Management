package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlygiaban;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.BangLaiChuanBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.BangLaiChuanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Giao diện quản lý giá bán cho quản lý: - Hiển thị giá nhập FIFO (chưa VAT),
 * VAT, giá bán hiện tại, giá bán đề xuất theo lãi chuẩn. - Cho phép áp dụng giá
 * bán đề xuất để cập nhật thẳng vào bảng SanPham.
 */
public class GD_QuanLyGiaBan extends JPanel {

    private final SanPhamBUS sanPhamBUS;
    private final LoHangDAO loHangDAO;
    private final BangLaiChuanBUS bangLaiChuanBUS;

    private static final String ALL_TYPES_DISPLAY = "Áp dụng cho tất cả";

    private JTable table;
    private DefaultTableModel model;
    private JButton btnReload;
    private JButton btnApplySelected;
    private final List<RowHighlight> rowHighlights = new ArrayList<>();

    // Bảng cấu hình lãi chuẩn
    private JTable tblLaiChuan;
    private DefaultTableModel modelLaiChuan;
    private JButton btnSaveLaiChuan;
    private JButton btnAddConfigRow;
    private JButton btnRemoveConfigRow;
    private JComboBox<String> cboLoaiConfig;
    private final Map<String, List<BangLaiChuan>> cachedConfigs = new LinkedHashMap<>();
    private String currentLoaiSelection = ALL_TYPES_DISPLAY;
    private boolean suppressAutoRecalc = false;

    public GD_QuanLyGiaBan() {
        this.sanPhamBUS = new SanPhamBUS(new SanPhamDAO());
        this.loHangDAO = new LoHangDAO();
        this.bangLaiChuanBUS = new BangLaiChuanBUS(new BangLaiChuanDAO());

        initComponents();
        loadBangLaiChuan();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Quản lý giá bán (theo lãi chuẩn)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, title.getFont().getSize() + 6f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Panel cấu hình lãi chuẩn (trên cùng)
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Cấu hình lãi chuẩn theo khoảng giá nhập"));

        JPanel configToolbar = new JPanel(new BorderLayout());
        JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JLabel lblLoai = new JLabel("Loại sản phẩm:");
        cboLoaiConfig = new JComboBox<>();
        cboLoaiConfig.setPrototypeDisplayValue("Chăm sóc trẻ em");
        cboLoaiConfig.addActionListener(e -> handleLoaiSelectionChanged());
        comboWrapper.add(lblLoai);
        comboWrapper.add(cboLoaiConfig);

        JPanel actionWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnAddConfigRow = new JButton("Thêm dòng");
        ButtonStyles.apply(btnAddConfigRow, ButtonStyles.Type.INFO);
        btnAddConfigRow.addActionListener(e -> addConfigRow());

        btnRemoveConfigRow = new JButton("Xóa dòng");
        ButtonStyles.apply(btnRemoveConfigRow, ButtonStyles.Type.DANGER);
        btnRemoveConfigRow.addActionListener(e -> removeSelectedConfigRows());

        actionWrapper.add(btnAddConfigRow);
        actionWrapper.add(btnRemoveConfigRow);
        configToolbar.add(comboWrapper, BorderLayout.WEST);
        configToolbar.add(actionWrapper, BorderLayout.EAST);
        configPanel.add(configToolbar, BorderLayout.NORTH);

        String[] laiColumns = {"Giá nhập từ", "Giá nhập đến (0 = không giới hạn)", "% lãi"};
        modelLaiChuan = new DefaultTableModel(laiColumns, 0);
        tblLaiChuan = new JTable(modelLaiChuan);
        tblLaiChuan.setRowHeight(24);
        registerAutoRecalculateForLaiChuanTable();
        JScrollPane spLai = new JScrollPane(tblLaiChuan);
        configPanel.add(spLai, BorderLayout.CENTER);

        btnSaveLaiChuan = new JButton("Lưu cấu hình lãi chuẩn");
        ButtonStyles.apply(btnSaveLaiChuan, ButtonStyles.Type.PRIMARY);
        btnSaveLaiChuan.addActionListener(e -> saveBangLaiChuan());
        JPanel pnlSave = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSave.add(btnSaveLaiChuan);
        configPanel.add(pnlSave, BorderLayout.SOUTH);

        add(configPanel, BorderLayout.NORTH);

        String[] columns = {
            "Mã SP", "Tên sản phẩm", "Giá nhập FIFO", "VAT (%)",
            "Giá bán hiện tại", "Giá bán đề xuất", "Giá bán (đã VAT)", "Biên lợi nhuận hiện tại (%)"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Cho phép chỉnh sửa cột "Giá bán đề xuất" để quản lý có thể tự sửa tay
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setDefaultRenderer(Object.class, new PriceRowRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buildLegendPanel(), BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnReload = new JButton("Tải lại");
        ButtonStyles.apply(btnReload, ButtonStyles.Type.PRIMARY);
        btnReload.addActionListener(e -> loadData());

        btnApplySelected = new JButton("Áp dụng giá đề xuất cho dòng chọn");
        ButtonStyles.apply(btnApplySelected, ButtonStyles.Type.SUCCESS);
        btnApplySelected.addActionListener(e -> applySuggestedPriceForSelected());

        bottomPanel.add(btnReload);
        bottomPanel.add(btnApplySelected);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void registerAutoRecalculateForLaiChuanTable() {
        modelLaiChuan.addTableModelListener(e -> {
            if (suppressAutoRecalc) {
                return;
            }
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                return;
            }
            persistCurrentSelection(false);
            SwingUtilities.invokeLater(() -> loadData(false));
        });
    }

    private void loadBangLaiChuan() {
        suppressAutoRecalc = true;
        try {
            cachedConfigs.clear();
            List<BangLaiChuan> ds = bangLaiChuanBUS.layTatCa();
            if (ds.isEmpty()) {
                List<BangLaiChuan> template = buildDefaultTemplate();
                cachedConfigs.put(ALL_TYPES_DISPLAY, cloneConfigList(template));
                for (LoaiSanPham loai : LoaiSanPham.values()) {
                    cachedConfigs.put(loaiSanPhamToDisplay(loai), cloneConfigList(template));
                }
            } else {
                Map<String, List<BangLaiChuan>> grouped = new LinkedHashMap<>();
                for (BangLaiChuan config : ds) {
                    String key = loaiSanPhamToDisplay(config.getLoaiSanPham());
                    grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(copyOf(config));
                }
                List<BangLaiChuan> fallbackSource = grouped.getOrDefault(ALL_TYPES_DISPLAY, buildDefaultTemplate());
                cachedConfigs.put(ALL_TYPES_DISPLAY, cloneConfigList(fallbackSource));
                for (LoaiSanPham loai : LoaiSanPham.values()) {
                    String display = loaiSanPhamToDisplay(loai);
                    List<BangLaiChuan> configs = grouped.get(display);
                    if (configs == null || configs.isEmpty()) {
                        cachedConfigs.put(display, cloneConfigList(fallbackSource));
                    } else {
                        cachedConfigs.put(display, cloneConfigList(configs));
                    }
                }
            }
            populateLoaiSanPhamCombo();
            currentLoaiSelection = (String) cboLoaiConfig.getSelectedItem();
            renderConfigTableForSelection();
        } finally {
            suppressAutoRecalc = false;
        }
    }

    private void handleLoaiSelectionChanged() {
        if (suppressAutoRecalc) {
            return;
        }
        String nextSelection = (String) cboLoaiConfig.getSelectedItem();
        if (nextSelection == null || nextSelection.equals(currentLoaiSelection)) {
            return;
        }
        persistCurrentSelection(false);
        currentLoaiSelection = nextSelection;
        renderConfigTableForSelection();
    }

    private void populateLoaiSanPhamCombo() {
        if (cboLoaiConfig == null) {
            return;
        }
        boolean previous = suppressAutoRecalc;
        suppressAutoRecalc = true;
        try {
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            comboModel.addElement(ALL_TYPES_DISPLAY);
            for (LoaiSanPham loai : LoaiSanPham.values()) {
                comboModel.addElement(loaiSanPhamToDisplay(loai));
            }
            cboLoaiConfig.setModel(comboModel);
            cboLoaiConfig.setSelectedItem(currentLoaiSelection);
        } finally {
            suppressAutoRecalc = previous;
        }
    }

    private void renderConfigTableForSelection() {
        List<BangLaiChuan> configs = cachedConfigs.getOrDefault(currentLoaiSelection, new ArrayList<>());
        boolean previous = suppressAutoRecalc;
        suppressAutoRecalc = true;
        try {
            modelLaiChuan.setRowCount(0);
            for (BangLaiChuan config : configs) {
                modelLaiChuan.addRow(new Object[]{
                    (long) config.getGiaNhapTu(),
                    (long) config.getGiaNhapDen(),
                    config.getTyLeLai() * 100
                });
            }
        } finally {
            suppressAutoRecalc = previous;
        }
    }

    private void addConfigRow() {
        modelLaiChuan.addRow(new Object[]{"", "", ""});
    }

    private void removeSelectedConfigRows() {
        int[] selected = tblLaiChuan.getSelectedRows();
        if (selected == null || selected.length == 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Vui lòng chọn ít nhất 1 dòng để xóa.");
            return;
        }
        suppressAutoRecalc = true;
        try {
            for (int i = selected.length - 1; i >= 0; i--) {
                modelLaiChuan.removeRow(selected[i]);
            }
        } finally {
            suppressAutoRecalc = false;
        }
        persistCurrentSelection(false);
        SwingUtilities.invokeLater(() -> loadData(false));
    }

    private boolean persistCurrentSelection(boolean strict) {
        if (currentLoaiSelection == null) {
            return true;
        }
        List<BangLaiChuan> configs = buildConfigFromCurrentTable(strict);
        if (configs == null) {
            return false;
        }
        cachedConfigs.put(currentLoaiSelection, configs);
        return true;
    }

    private List<BangLaiChuan> buildConfigFromCurrentTable(boolean strict) {
        List<BangLaiChuan> ds = new ArrayList<>();
        if (modelLaiChuan == null) {
            return ds;
        }
        for (int i = 0; i < modelLaiChuan.getRowCount(); i++) {
            String tuStr = valueToString(modelLaiChuan.getValueAt(i, 0));
            String denStr = valueToString(modelLaiChuan.getValueAt(i, 1));
            String laiStr = valueToString(modelLaiChuan.getValueAt(i, 2));
            if (tuStr.isEmpty() && denStr.isEmpty() && laiStr.isEmpty()) {
                continue;
            }

            Double tu = parseDoubleFlexible(tuStr);
            Double den = denStr.isEmpty() ? 0d : parseDoubleFlexible(denStr);
            Double lai = parseDoubleFlexible(laiStr);

            boolean invalid = tu == null || lai == null || (!denStr.isEmpty() && den == null);
            if (invalid) {
                if (strict) {
                    throw new NumberFormatException("Dòng " + (i + 1) + " chứa giá trị không hợp lệ.");
                } else {
                    continue;
                }
            }

            BangLaiChuan b = new BangLaiChuan(0, tu, den == null ? 0 : den, lai / 100.0);
            ds.add(b);
        }
        ds.sort(Comparator.comparingDouble(BangLaiChuan::getGiaNhapTu));
        return ds;
    }

    private List<BangLaiChuan> collectAllConfigs() {
        List<BangLaiChuan> result = new ArrayList<>();
        for (Map.Entry<String, List<BangLaiChuan>> entry : cachedConfigs.entrySet()) {
            LoaiSanPham loai = parseLoaiSanPham(entry.getKey());
            List<BangLaiChuan> configs = entry.getValue();
            if (configs == null) {
                continue;
            }
            for (BangLaiChuan config : configs) {
                result.add(new BangLaiChuan(0,
                        config.getGiaNhapTu(),
                        config.getGiaNhapDen(),
                        config.getTyLeLai(),
                        loai));
            }
        }
        return result;
    }

    private List<BangLaiChuan> buildDefaultTemplate() {
        List<BangLaiChuan> defaults = new ArrayList<>();
        defaults.add(new BangLaiChuan(0, 0, 50_000, 0.30));
        defaults.add(new BangLaiChuan(0, 50_001, 100_000, 0.25));
        defaults.add(new BangLaiChuan(0, 100_001, 300_000, 0.20));
        defaults.add(new BangLaiChuan(0, 300_001, 0, 0.15));
        return defaults;
    }

    private List<BangLaiChuan> cloneConfigList(List<BangLaiChuan> source) {
        List<BangLaiChuan> clone = new ArrayList<>();
        if (source == null) {
            return clone;
        }
        for (BangLaiChuan config : source) {
            BangLaiChuan copied = copyOf(config);
            if (copied != null) {
                clone.add(copied);
            }
        }
        clone.sort(Comparator.comparingDouble(BangLaiChuan::getGiaNhapTu));
        return clone;
    }

    private BangLaiChuan copyOf(BangLaiChuan src) {
        if (src == null) {
            return null;
        }
        return new BangLaiChuan(0, src.getGiaNhapTu(), src.getGiaNhapDen(), src.getTyLeLai());
    }

    private void saveBangLaiChuan() {
        try {
            if (!persistCurrentSelection(true)) {
                return;
            }
            List<BangLaiChuan> ds = collectAllConfigs();
            if (ds.isEmpty()) {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Vui lòng nhập ít nhất 1 cấu hình lãi chuẩn hợp lệ trước khi lưu.");
                return;
            }
            if (bangLaiChuanBUS.luuLaiTatCa(ds)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Đã lưu cấu hình lãi chuẩn.");
                loadBangLaiChuan();
                loadData();
                // Tự động áp dụng giá đề xuất cho tất cả sản phẩm sau khi tính xong
                applySuggestedPriceForAll();
            } else {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Lưu cấu hình lãi chuẩn thất bại.");
            }
        } catch (NumberFormatException nfe) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Dữ liệu cấu hình không hợp lệ: " + nfe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi lưu cấu hình lãi chuẩn: " + ex.getMessage());
        }
    }

    private void loadData() {
        loadData(true);
    }

    private void loadData(boolean showToast) {
        model.setRowCount(0);
        rowHighlights.clear();
        try {
            List<SanPham> danhSach = sanPhamBUS.layTatCaSanPham();
            List<BangLaiChuan> cauHinhDangSuDung = getCurrentConfigForPreview();
            for (SanPham sp : danhSach) {
                double giaNhapFIFO = layGiaNhapFIFO(sp);
                double vat = sp.getThueVAT();
                double giaBan = sp.getGiaBan();
                double giaBanDaVAT = giaBan * (1 + vat);
                double giaBanDeXuat = tinhGiaBanTheoLaiChuan(giaNhapFIFO, sp.getLoaiSanPham(), cauHinhDangSuDung);

                double bienLoiNhuan = 0;
                if (giaNhapFIFO > 0 && giaBan > 0) {
                    bienLoiNhuan = (giaBan - giaNhapFIFO) / giaNhapFIFO * 100.0;
                }

                RowHighlight highlight = determineRowHighlight(
                        giaNhapFIFO,
                        giaBan,
                        giaBanDeXuat,
                        bienLoiNhuan,
                        sp.getLoaiSanPham(),
                        cauHinhDangSuDung
                );

                Object[] row = {
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    formatCurrency(giaNhapFIFO),
                    String.format("%.0f", vat * 100),
                    formatCurrency(giaBan),
                    formatCurrency(giaBanDeXuat),
                    formatCurrency(giaBanDaVAT),
                    String.format("%.1f", bienLoiNhuan)
                };
                model.addRow(row);
                rowHighlights.add(highlight);
            }
            if (showToast) {
                Notifications.getInstance().show(Notifications.Type.INFO,
                        Notifications.Location.TOP_CENTER,
                        "Đã tải " + danhSach.size() + " sản phẩm.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi tải dữ liệu giá bán: " + ex.getMessage());
        }
    }

    private List<BangLaiChuan> getCurrentConfigForPreview() {
        persistCurrentSelection(false);
        List<BangLaiChuan> ds = collectAllConfigs();
        if (ds.isEmpty()) {
            // Nếu bảng trống (chưa cấu hình), lấy từ DB để vẫn hiển thị giá.
            ds = bangLaiChuanBUS.layTatCa();
        }
        return ds;
    }

    private List<BangLaiChuan> buildConfigFromTable(boolean strict) {
        List<BangLaiChuan> ds = new ArrayList<>();
        if (modelLaiChuan == null) {
            return ds;
        }
        for (int i = 0; i < modelLaiChuan.getRowCount(); i++) {
            String tuStr = valueToString(modelLaiChuan.getValueAt(i, 0));
            String denStr = valueToString(modelLaiChuan.getValueAt(i, 1));
            String laiStr = valueToString(modelLaiChuan.getValueAt(i, 2));
            if (tuStr.isEmpty() && denStr.isEmpty() && laiStr.isEmpty()) {
                continue;
            }

            Double tu = parseDoubleFlexible(tuStr);
            Double den = denStr.isEmpty() ? 0d : parseDoubleFlexible(denStr);
            Double lai = parseDoubleFlexible(laiStr);
            LoaiSanPham loaiSanPham = parseLoaiSanPham(valueToString(modelLaiChuan.getValueAt(i, 3)));

            boolean invalid = tu == null || lai == null || (!denStr.isEmpty() && den == null);
            if (invalid) {
                if (strict) {
                    throw new NumberFormatException("Dòng " + (i + 1) + " chứa giá trị không hợp lệ.");
                } else {
                    continue;
                }
            }

            BangLaiChuan b = new BangLaiChuan(0, tu, den == null ? 0 : den, lai / 100.0, loaiSanPham);
            ds.add(b);
        }
        ds.sort(Comparator.comparingDouble(BangLaiChuan::getGiaNhapTu));
        return ds;
    }

    private String valueToString(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private Double parseDoubleFlexible(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            return null;
        }
        normalized = normalized.replace(",", ".");
        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Tự động áp dụng giá đề xuất cho TẤT CẢ sản phẩm (dùng khi lưu cấu hình
     * lãi chuẩn mới)
     */
    private void applySuggestedPriceForAll() {
        int updated = 0;
        try {
            List<SanPham> danhSach = sanPhamBUS.layTatCaSanPham();
            List<BangLaiChuan> cauHinhDangSuDung = getCurrentConfigForPreview();
            for (SanPham sp : danhSach) {
                double giaNhapFIFO = layGiaNhapFIFO(sp);
                if (giaNhapFIFO <= 0) {
                    continue; // Bỏ qua sản phẩm chưa có giá nhập FIFO
                }
                double giaBanDeXuat = tinhGiaBanTheoLaiChuan(giaNhapFIFO, sp.getLoaiSanPham(), cauHinhDangSuDung);
                if (giaBanDeXuat <= 0) {
                    continue; // Bỏ qua nếu không tính được giá đề xuất
                }
                // Chỉ cập nhật nếu giá đề xuất khác giá hiện tại
                if (Math.abs(sp.getGiaBan() - giaBanDeXuat) > 0.01) {
                    sp.setGiaBan(giaBanDeXuat);
                    if (sanPhamBUS.capNhatSanPham(sp)) {
                        updated++;
                    }
                }
            }

            // Reload lại bảng để hiển thị giá mới
            loadData();

            if (updated > 0) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Đã tự động cập nhật giá bán cho " + updated + " sản phẩm theo lãi chuẩn mới.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi tự động áp dụng giá đề xuất: " + ex.getMessage());
        }
    }

    private void applySuggestedPriceForSelected() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows == null || selectedRows.length == 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    Notifications.Location.TOP_CENTER,
                    "Vui lòng chọn ít nhất 1 sản phẩm để áp dụng giá đề xuất.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Áp dụng giá bán đề xuất cho " + selectedRows.length + " sản phẩm?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int updated = 0;
        try {
            for (int viewRow : selectedRows) {
                int row = table.convertRowIndexToModel(viewRow);
                String maSp = (String) model.getValueAt(row, 0);
                // Cột 5: Giá bán đề xuất (có thể đã được chỉnh sửa)
                Object cellValue = model.getValueAt(row, 5);
                String giaBanDeXuatStr = cellValue.toString().replace(".", "").replace(",", "");
                double giaBanDeXuat = Double.parseDouble(giaBanDeXuatStr);

                // Lấy sản phẩm theo mã
                SanPham sp = sanPhamBUS.timSanPhamTheoMa(maSp).orElse(null);
                if (sp == null) {
                    continue;
                }

                sp.setGiaBan(giaBanDeXuat);
                if (sanPhamBUS.capNhatSanPham(sp)) {
                    updated++;
                }
            }

            loadData();

            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    Notifications.Location.TOP_CENTER,
                    "Đã cập nhật giá bán cho " + updated + " sản phẩm.");
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi áp dụng giá đề xuất: " + ex.getMessage());
        }
    }

    private String formatCurrency(double value) {
        if (value <= 0) {
            return "0";
        }
        return String.format("%,.0f", value);
    }

    // --- Logic dùng lại từ GD_QuanLySanPham (được rút gọn) ---
    private double layGiaNhapFIFO(SanPham sp) {
        try {
            if (loHangDAO == null || sp == null || sp.getMaSanPham() == null) {
                return 0;
            }
            List<LoHang> dsLo = loHangDAO.findByMaSanPham(sp.getMaSanPham());
            if (dsLo == null || dsLo.isEmpty()) {
                return 0;
            }

            LocalDate today = LocalDate.now();
            return dsLo.stream()
                    .filter(lh -> lh.getTonKho() > 0
                    && lh.isTrangThai()
                    && lh.getGiaNhapLo() > 0
                    && (lh.getHanSuDung() == null || !lh.getHanSuDung().isBefore(today)))
                    .sorted(Comparator.comparing(LoHang::getHanSuDung)
                            .thenComparing(LoHang::getMaLoHang))
                    .mapToDouble(LoHang::getGiaNhapLo)
                    .findFirst()
                    .orElse(0);
        } catch (Exception ex) {
            System.err.println("[GD_QuanLyGiaBan] Lỗi khi lấy giá nhập FIFO: " + ex.getMessage());
            return 0;
        }
    }

    private double tinhGiaBanTheoLaiChuan(double giaNhapChuaVAT, LoaiSanPham loaiSanPham, List<BangLaiChuan> cauHinhDangSuDung) {
        if (giaNhapChuaVAT <= 0) {
            return 0;
        }
        double tyLeLai = timTyLeLaiTheoCauHinh(giaNhapChuaVAT, loaiSanPham, cauHinhDangSuDung);
        if (tyLeLai <= 0) {
            return 0;
        }
        return giaNhapChuaVAT * (1 + tyLeLai);
    }

    private double timTyLeLaiTheoCauHinh(double giaNhap, LoaiSanPham loaiSanPham, List<BangLaiChuan> cauHinhDangSuDung) {
        if (cauHinhDangSuDung == null || cauHinhDangSuDung.isEmpty()) {
            return 0;
        }
        double fallback = 0;
        for (BangLaiChuan b : cauHinhDangSuDung) {
            double tu = b.getGiaNhapTu();
            double den = b.getGiaNhapDen();
            boolean matchTu = giaNhap >= tu;
            boolean matchDen = (den <= 0) || (giaNhap <= den);
            if (matchTu && matchDen) {
                if (loaiSanPham != null && loaiSanPham.equals(b.getLoaiSanPham())) {
                    return b.getTyLeLai();
                }
                if (b.getLoaiSanPham() == null) {
                    fallback = b.getTyLeLai();
                }
            }
        }
        return fallback;
    }

    private RowHighlight determineRowHighlight(double giaNhapFIFO,
            double giaBan,
            double giaBanDeXuat,
            double bienLoiNhuan,
            LoaiSanPham loaiSanPham,
            List<BangLaiChuan> cauHinhDangSuDung) {
        if (giaNhapFIFO <= 0) {
            return RowHighlight.GIA_NHAP_ZERO;
        }
        double tyLeLaiChuan = timTyLeLaiTheoCauHinh(giaNhapFIFO, loaiSanPham, cauHinhDangSuDung) * 100;
        if (tyLeLaiChuan > 0) {
            if (bienLoiNhuan < tyLeLaiChuan - 0.01) {
                return RowHighlight.BIEN_LOI_NHUAN_THAP;
            }
            if (bienLoiNhuan > tyLeLaiChuan + 0.01) {
                return RowHighlight.LOI_QUA_CAO;
            }
        }
        if (giaBanDeXuat > 0 && giaBan > 0 && giaBanDeXuat < giaBan - 0.01) {
            return RowHighlight.GIA_DE_XUAT_THAP;
        }
        return RowHighlight.NONE;
    }

    private String loaiSanPhamToDisplay(LoaiSanPham loaiSanPham) {
        if (loaiSanPham == null) {
            return ALL_TYPES_DISPLAY;
        }
        switch (loaiSanPham) {
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
                return ALL_TYPES_DISPLAY;
        }
    }

    private LoaiSanPham parseLoaiSanPham(String display) {
        if (display == null || display.isBlank() || ALL_TYPES_DISPLAY.equalsIgnoreCase(display.trim())) {
            return null;
        }
        switch (display.trim()) {
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

    private JPanel buildLegendPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Chú thích trạng thái"));
        panel.setBackground(Color.WHITE);
        panel.add(createLegendBadge(RowHighlight.BIEN_LOI_NHUAN_THAP, "Biên lợi nhuận hiện tại < chuẩn"));
        panel.add(createLegendBadge(RowHighlight.LOI_QUA_CAO, "Lãi quá cao so với chuẩn"));
        panel.add(createLegendBadge(RowHighlight.GIA_NHAP_ZERO, "Giá nhập bằng 0"));
        panel.add(createLegendBadge(RowHighlight.GIA_DE_XUAT_THAP, "Giá đề xuất thấp hơn giá hiện tại"));
        return panel;
    }

    private JComponent createLegendBadge(RowHighlight highlight, String text) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        container.setOpaque(false);
        JLabel icon = new JLabel("\u25CF");
        icon.setFont(icon.getFont().deriveFont(Font.BOLD, icon.getFont().getSize() + 6f));
        icon.setForeground(colorForHighlight(highlight));
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        container.add(icon);
        container.add(label);
        return container;
    }

    private Color colorForHighlight(RowHighlight highlight) {
        switch (highlight) {
            case BIEN_LOI_NHUAN_THAP:
                return new Color(214, 48, 49);
            case LOI_QUA_CAO:
                return new Color(255, 185, 0);
            case GIA_NHAP_ZERO:
                return new Color(230, 126, 34);
            case GIA_DE_XUAT_THAP:
                return new Color(32, 148, 243);
            default:
                return Color.WHITE;
        }
    }

    private class PriceRowRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                return c;
            }
            int modelRow = table.convertRowIndexToModel(row);
            RowHighlight highlight = RowHighlight.NONE;
            if (modelRow >= 0 && modelRow < rowHighlights.size()) {
                highlight = rowHighlights.get(modelRow);
            }
            c.setBackground(colorForHighlight(highlight));
            c.setForeground(Color.BLACK);
            return c;
        }
    }

    private enum RowHighlight {
        NONE,
        BIEN_LOI_NHUAN_THAP,
        LOI_QUA_CAO,
        GIA_NHAP_ZERO,
        GIA_DE_XUAT_THAP
    }
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlygiaban;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.BangLaiChuanBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.LoHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.BangLaiChuanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.LoHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Giao diện quản lý giá bán cho quản lý:
 * - Hiển thị giá nhập FIFO (chưa VAT), VAT, giá bán hiện tại, giá bán đề xuất theo lãi chuẩn.
 * - Cho phép áp dụng giá bán đề xuất để cập nhật thẳng vào bảng SanPham.
 */
public class GD_QuanLyGiaBan extends JPanel {

    private final SanPhamBUS sanPhamBUS;
    private final LoHangDAO loHangDAO;
    private final BangLaiChuanBUS bangLaiChuanBUS;

    private JTable table;
    private DefaultTableModel model;
    private JButton btnReload;
    private JButton btnApplySelected;

    // Bảng cấu hình lãi chuẩn
    private JTable tblLaiChuan;
    private DefaultTableModel modelLaiChuan;
    private JButton btnSaveLaiChuan;

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
        String[] laiColumns = {"Giá nhập từ", "Giá nhập đến (0 = không giới hạn)", "% lãi"};
        modelLaiChuan = new DefaultTableModel(laiColumns, 0);
        tblLaiChuan = new JTable(modelLaiChuan);
        tblLaiChuan.setRowHeight(24);
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
                "Giá bán hiện tại", "Giá bán (đã VAT)", "Giá bán đề xuất", "Biên lợi nhuận hiện tại (%)"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Cho phép chỉnh sửa cột "Giá bán đề xuất" để quản lý có thể tự sửa tay
                return column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

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

    private void loadBangLaiChuan() {
        modelLaiChuan.setRowCount(0);
        java.util.List<BangLaiChuan> ds = bangLaiChuanBUS.layTatCa();
        // Nếu chưa có dữ liệu, tạo mặc định
        if (ds.isEmpty()) {
            ds.add(new BangLaiChuan(0, 0, 50_000, 0.30));
            ds.add(new BangLaiChuan(0, 50_001, 100_000, 0.25));
            ds.add(new BangLaiChuan(0, 100_001, 300_000, 0.20));
            ds.add(new BangLaiChuan(0, 300_001, 0, 0.15));
        }
        for (BangLaiChuan b : ds) {
            modelLaiChuan.addRow(new Object[]{
                    (long) b.getGiaNhapTu(),
                    (long) b.getGiaNhapDen(),
                    b.getTyLeLai() * 100
            });
        }
    }

    private void saveBangLaiChuan() {
        try {
            java.util.List<BangLaiChuan> ds = new java.util.ArrayList<>();
            for (int i = 0; i < modelLaiChuan.getRowCount(); i++) {
                String tuStr = modelLaiChuan.getValueAt(i, 0).toString().trim();
                String denStr = modelLaiChuan.getValueAt(i, 1).toString().trim();
                String laiStr = modelLaiChuan.getValueAt(i, 2).toString().trim();
                if (tuStr.isEmpty() && denStr.isEmpty() && laiStr.isEmpty()) continue;

                double tu = Double.parseDouble(tuStr);
                double den = denStr.isEmpty() ? 0 : Double.parseDouble(denStr);
                double laiPercent = Double.parseDouble(laiStr);
                double tyLe = laiPercent / 100.0;

                BangLaiChuan b = new BangLaiChuan(0, tu, den, tyLe);
                ds.add(b);
            }

            if (bangLaiChuanBUS.luuLaiTatCa(ds)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                        Notifications.Location.TOP_CENTER,
                        "Đã lưu cấu hình lãi chuẩn.");
                loadData();
            } else {
                Notifications.getInstance().show(Notifications.Type.WARNING,
                        Notifications.Location.TOP_CENTER,
                        "Lưu cấu hình lãi chuẩn thất bại.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi lưu cấu hình lãi chuẩn: " + ex.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<SanPham> danhSach = sanPhamBUS.layTatCaSanPham();
            for (SanPham sp : danhSach) {
                double giaNhapFIFO = layGiaNhapFIFO(sp);
                double vat = sp.getThueVAT();
                double giaBan = sp.getGiaBan();
                double giaBanDaVAT = giaBan * (1 + vat);
                double giaBanDeXuat = tinhGiaBanTheoLaiChuan(giaNhapFIFO);

                double bienLoiNhuan = 0;
                if (giaNhapFIFO > 0 && giaBan > 0) {
                    bienLoiNhuan = (giaBan - giaNhapFIFO) / giaNhapFIFO * 100.0;
                }

                Object[] row = {
                        sp.getMaSanPham(),
                        sp.getTenSanPham(),
                        formatCurrency(giaNhapFIFO),
                        String.format("%.0f", vat * 100),
                        formatCurrency(giaBan),
                        formatCurrency(giaBanDaVAT),
                        formatCurrency(giaBanDeXuat),
                        String.format("%.1f", bienLoiNhuan)
                };
                model.addRow(row);
            }
            Notifications.getInstance().show(Notifications.Type.INFO,
                    Notifications.Location.TOP_CENTER,
                    "Đã tải " + danhSach.size() + " sản phẩm.");
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    Notifications.Location.TOP_CENTER,
                    "Lỗi khi tải dữ liệu giá bán: " + ex.getMessage());
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
                // Cột 6: Giá bán đề xuất
                String giaBanDeXuatStr = ((String) model.getValueAt(row, 6)).replace(".", "").replace(",", "");
                double giaBanDeXuat = Double.parseDouble(giaBanDeXuatStr);

                // Lấy sản phẩm theo mã
                SanPham sp = sanPhamBUS.timSanPhamTheoMa(maSp).orElse(null);
                if (sp == null) continue;

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
        if (value <= 0) return "0";
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

    private double tinhGiaBanTheoLaiChuan(double giaNhapChuaVAT) {
        double tyLeLai = bangLaiChuanBUS.timTyLeLaiChoGiaNhap(giaNhapChuaVAT);
        if (giaNhapChuaVAT <= 0 || tyLeLai <= 0) return 0;
        return giaNhapChuaVAT * (1 + tyLeLai);
    }
}



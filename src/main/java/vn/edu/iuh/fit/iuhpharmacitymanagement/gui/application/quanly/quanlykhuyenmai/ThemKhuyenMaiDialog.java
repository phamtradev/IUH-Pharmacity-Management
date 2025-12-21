/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.application.quanly.quanlykhuyenmai;

import com.formdev.flatlaf.FlatClientProperties;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.KhuyenMaiBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.SanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietKhuyenMaiSanPhamBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.SanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietKhuyenMaiSanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.gui.theme.ButtonStyles;
import raven.toast.Notifications;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dialog để thêm khuyến mãi
 * @author PhamTra
 */
public class ThemKhuyenMaiDialog extends javax.swing.JDialog {

    private final KhuyenMaiBUS khuyenMaiBUS;
    private final SanPhamBUS sanPhamBUS;
    private final ChiTietKhuyenMaiSanPhamBUS chiTietKhuyenMaiSanPhamBUS;
    private boolean themThanhCong = false;
    private List<SanPham> danhSachSanPhamDaChon = new ArrayList<>(); // Lưu danh sách sản phẩm đã chọn
    private boolean isValidatingDate = false; // Flag để tránh vòng lặp khi validate
    private Timer timerQuetMa; // Timer để debounce khi quét mã
    private Timer timerValidateTenKM; // Timer để debounce khi validate tên khuyến mãi
    
    // Components cho giá tối thiểu và tối đa (khuyến mãi đơn hàng)
    private javax.swing.JLabel lblGiaToiThieu;
    private javax.swing.JTextField txtGiaToiThieu;
    private javax.swing.JLabel lblGiaToiDa;
    private javax.swing.JTextField txtGiaToiDa;
    
    // Components cho số lượng tối đa (khuyến mãi sản phẩm)
    private javax.swing.JLabel lblSoLuongToiDa;
    private javax.swing.JTextField txtSoLuongToiDa;

    public ThemKhuyenMaiDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.khuyenMaiBUS = new KhuyenMaiBUS();
        this.sanPhamBUS = new SanPhamBUS(new SanPhamDAO());
        this.chiTietKhuyenMaiSanPhamBUS = new ChiTietKhuyenMaiSanPhamBUS(new ChiTietKhuyenMaiSanPhamDAO());
        initComponents();
        // Đặt vị trí dialog xuống dưới một chút để có thể đọc được thông báo lỗi
        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            int y = parent.getY() + (parent.getHeight() - getHeight()) / 2 + 100; // Thêm 100px xuống dưới
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }
        setupUI();
    }

    private void setupUI() {
        // Placeholder text
        txtTenKhuyenMai.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên khuyến mãi");
        txtGiamGia.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 0.1 (10%)");
        txtGiaToiThieu.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 100000");
        txtGiaToiDa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 50000");
        txtSoLuongToiDa.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VD: 5");
        
        // Thiết lập date chooser - chỉ ngăn chọn ngày quá khứ
        dateNgayBatDau.setMinSelectableDate(new Date());
        dateNgayKetThuc.setMinSelectableDate(new Date());
        
        // Mặc định đặt ngày bắt đầu là hôm nay
        dateNgayBatDau.setDate(new Date());
        
        // Thêm PropertyChangeListener cho date editor để validate ngay khi chọn ngày
        PropertyChangeListener listenerNgayBatDau = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Tránh trigger lại khi đang validate
                if (isValidatingDate) {
                    return;
                }
                // Chỉ validate khi ngày được set (không phải khi clear)
                if (evt.getNewValue() != null) {
                    // Sử dụng SwingUtilities.invokeLater để đảm bảo validate chạy sau khi date được set
                    SwingUtilities.invokeLater(() -> {
                        validateNgayBatDau();
                        // Validate lại ngày kết thúc nếu có để đảm bảo tính nhất quán
                        if (dateNgayKetThuc.getDate() != null) {
                            validateNgayKetThuc();
                        }
                    });
                }
            }
        };
        // Thêm listener cho cả JDateChooser và DateEditor để đảm bảo validate chạy
        dateNgayBatDau.addPropertyChangeListener("date", listenerNgayBatDau);
        if (dateNgayBatDau.getDateEditor() != null) {
            dateNgayBatDau.getDateEditor().addPropertyChangeListener("date", listenerNgayBatDau);
        }
        
        PropertyChangeListener listenerNgayKetThuc = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Tránh trigger lại khi đang validate
                if (isValidatingDate) {
                    return;
                }
                // Chỉ validate khi ngày được set (không phải khi clear)
                if (evt.getNewValue() != null) {
                    // Sử dụng SwingUtilities.invokeLater để đảm bảo validate chạy sau khi date được set
                    SwingUtilities.invokeLater(() -> {
                        validateNgayKetThuc();
                        // Validate lại ngày bắt đầu nếu có để đảm bảo tính nhất quán
                        if (dateNgayBatDau.getDate() != null) {
                            validateNgayBatDau();
                        }
                    });
                }
            }
        };
        // Thêm listener cho cả JDateChooser và DateEditor để đảm bảo validate chạy
        dateNgayKetThuc.addPropertyChangeListener("date", listenerNgayKetThuc);
        if (dateNgayKetThuc.getDateEditor() != null) {
            dateNgayKetThuc.getDateEditor().addPropertyChangeListener("date", listenerNgayKetThuc);
        }
        
        // Thiết lập combo box loại khuyến mãi - Mặc định "Đơn hàng"
        cboLoaiKhuyenMai.setModel(new DefaultComboBoxModel<>(new String[]{"Đơn hàng", "Sản phẩm"}));
        
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
        
        // Thiết lập validate tên khuyến mãi khi nhập (với debounce)
        timerValidateTenKM = new Timer(500, e -> validateTenKhuyenMai()); // 500ms delay
        timerValidateTenKM.setRepeats(false); // Chỉ chạy một lần
        
        txtTenKhuyenMai.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                kichHoatValidateTenKM();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                kichHoatValidateTenKM();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                kichHoatValidateTenKM();
            }
            
            private void kichHoatValidateTenKM() {
                // Dừng timer cũ nếu có
                if (timerValidateTenKM.isRunning()) {
                    timerValidateTenKM.stop();
                }
                // Bắt đầu timer mới
                timerValidateTenKM.start();
            }
        });

        ButtonStyles.apply(btnLuu, ButtonStyles.Type.SUCCESS);
        ButtonStyles.apply(btnHuy, ButtonStyles.Type.SECONDARY);
        ButtonStyles.apply(btnXoaSanPham, ButtonStyles.Type.DANGER);
        ButtonStyles.apply(btnXoaTrang, ButtonStyles.Type.WARNING);
        
        // Khởi tạo danh sách sản phẩm
        capNhatDanhSachSanPham();
        
        // Thêm action listener cho combo box loại khuyến mãi
        cboLoaiKhuyenMai.addActionListener(e -> {
            String loaiKM = (String) cboLoaiKhuyenMai.getSelectedItem();
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
            
            pack(); // Resize dialog
        });
        
        // Mặc định hiển thị các trường theo loại được chọn
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
        btnXoaSanPham.setVisible(isProductPromotion);
        btnXoaTrang.setVisible(isProductPromotion);
        lblSoLuongToiDa.setVisible(isProductPromotion);
        txtSoLuongToiDa.setVisible(isProductPromotion);
        
        // Cập nhật trạng thái nút xóa trắng
        capNhatTrangThaiNutXoaTrang();
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
                txtQuetMa.setText("");
                txtQuetMa.requestFocus();
                return;
            }
            
            // Kiểm tra sản phẩm đã có khuyến mãi còn hạn chưa
            List<KhuyenMai> danhSachKhuyenMaiConHan = layDanhSachKhuyenMaiConHanChoSanPham(sanPhamTimThay.getMaSanPham());
            
            if (!danhSachKhuyenMaiConHan.isEmpty()) {
                // Báo lỗi cho từng khuyến mãi (tránh thông báo quá dài)
                for (KhuyenMai km : danhSachKhuyenMaiConHan) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, 
                        Notifications.Location.TOP_CENTER,
                        "Sản phẩm '" + sanPhamTimThay.getTenSanPham() + "' đã áp dụng khuyến mãi: " + km.getTenKhuyenMai());
                }
                // Xóa trắng và focus lại
                txtQuetMa.setText("");
                txtQuetMa.requestFocus();
                return;
            }
            
            // Nếu không có khuyến mãi, thêm sản phẩm vào danh sách
            danhSachSanPhamDaChon.add(sanPhamTimThay);
            capNhatDanhSachSanPham();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                "Đã thêm sản phẩm: " + sanPhamTimThay.getTenSanPham());
            
            // Xóa text và focus lại để quét tiếp
            txtQuetMa.setText("");
            txtQuetMa.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                "Lỗi khi quét mã: " + e.getMessage());
            txtQuetMa.setText("");
            txtQuetMa.requestFocus();
        }
    }
    
    /**
     * Lấy danh sách khuyến mãi còn hạn (đang hoạt động) cho một sản phẩm
     * @param maSanPham Mã sản phẩm cần kiểm tra
     * @return Danh sách khuyến mãi còn hạn
     */
    private List<KhuyenMai> layDanhSachKhuyenMaiConHanChoSanPham(String maSanPham) {
        List<KhuyenMai> danhSachKhuyenMaiConHan = new ArrayList<>();
        
        try {
            // Lấy danh sách chi tiết khuyến mãi sản phẩm theo mã sản phẩm
            List<ChiTietKhuyenMaiSanPham> danhSachCTKM = chiTietKhuyenMaiSanPhamBUS.timTheoMaSanPham(maSanPham);
            
            if (danhSachCTKM == null || danhSachCTKM.isEmpty()) {
                return danhSachKhuyenMaiConHan; // Không có khuyến mãi nào
            }
            
            LocalDate homNay = LocalDate.now();
            
            // Lấy danh sách tất cả khuyến mãi để kiểm tra
            List<KhuyenMai> tatCaKhuyenMai = khuyenMaiBUS.getAllKhuyenMai();
            
            // Lọc các khuyến mãi còn hạn (đang hoạt động) cho sản phẩm này
            for (ChiTietKhuyenMaiSanPham ctkm : danhSachCTKM) {
                String maKhuyenMai = ctkm.getKhuyenMai().getMaKhuyenMai();
                
                // Tìm khuyến mãi trong danh sách
                for (KhuyenMai km : tatCaKhuyenMai) {
                    if (km.getMaKhuyenMai().equals(maKhuyenMai)) {
                        // Kiểm tra khuyến mãi còn hạn (đang hoạt động)
                        if (km.isTrangThai() 
                            && !homNay.isBefore(km.getNgayBatDau()) 
                            && !homNay.isAfter(km.getNgayKetThuc())) {
                            danhSachKhuyenMaiConHan.add(km);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return danhSachKhuyenMaiConHan;
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
    
    /**
     * Validate ngày bắt đầu - phải sau ngày hiện tại và nhỏ hơn ngày kết thúc (nếu có)
     */
    private void validateNgayBatDau() {
        Date selectedDate = dateNgayBatDau.getDate();
        if (selectedDate == null) {
            return; // Chưa chọn ngày
        }
        
        LocalDate selectedLocalDate = selectedDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        LocalDate today = LocalDate.now();
        
        // Nếu chọn trước ngày hiện tại (ngày bằng ngày hiện tại được phép)
        if (selectedLocalDate.isBefore(today)) {
            // Set flag để tránh trigger lại
            isValidatingDate = true;
            // Xóa ngày đã chọn
            dateNgayBatDau.setDate(null);
            isValidatingDate = false;
            
            // Hiển thị thông báo lỗi
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "Ngày bắt đầu không được nhỏ hơn ngày hiện tại!");
            return;
        }
        
        // Kiểm tra nếu đã có ngày kết thúc, ngày bắt đầu phải nhỏ hơn ngày kết thúc
        Date ngayKetThuc = dateNgayKetThuc.getDate();
        if (ngayKetThuc != null) {
            LocalDate ngayKetThucLocal = ngayKetThuc.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            // Nếu ngày bắt đầu >= ngày kết thúc
            if (selectedLocalDate.isAfter(ngayKetThucLocal) || selectedLocalDate.isEqual(ngayKetThucLocal)) {
                // Set flag để tránh trigger lại
                isValidatingDate = true;
                // Xóa ngày đã chọn
                dateNgayBatDau.setDate(null);
                isValidatingDate = false;
                
                // Hiển thị thông báo lỗi
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "Ngày bắt đầu phải nhỏ hơn ngày kết thúc!");
            }
        }
    }
    
    /**
     * Validate ngày kết thúc - phải sau ngày hiện tại và lớn hơn ngày bắt đầu (nếu có)
     */
    private void validateNgayKetThuc() {
        Date selectedDate = dateNgayKetThuc.getDate();
        if (selectedDate == null) {
            return; // Chưa chọn ngày
        }
        
        LocalDate selectedLocalDate = selectedDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        LocalDate today = LocalDate.now();
        
        // Nếu chọn trước ngày hiện tại (ngày bằng ngày hiện tại được phép)
        if (selectedLocalDate.isBefore(today)) {
            // Set flag để tránh trigger lại
            isValidatingDate = true;
            // Xóa ngày đã chọn
            dateNgayKetThuc.setDate(null);
            isValidatingDate = false;
            
            // Hiển thị thông báo lỗi
            Notifications.getInstance().show(Notifications.Type.ERROR, 
                Notifications.Location.TOP_CENTER,
                "Ngày kết thúc không được nhỏ hơn ngày hiện tại!");
            return;
        }
        
        // Kiểm tra nếu đã có ngày bắt đầu, ngày kết thúc phải lớn hơn ngày bắt đầu
        Date ngayBatDau = dateNgayBatDau.getDate();
        if (ngayBatDau != null) {
            LocalDate ngayBatDauLocal = ngayBatDau.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            // Nếu ngày kết thúc <= ngày bắt đầu
            if (selectedLocalDate.isBefore(ngayBatDauLocal) || selectedLocalDate.isEqual(ngayBatDauLocal)) {
                // Set flag để tránh trigger lại
                isValidatingDate = true;
                // Xóa ngày đã chọn
                dateNgayKetThuc.setDate(null);
                isValidatingDate = false;
                
                // Hiển thị thông báo lỗi
                Notifications.getInstance().show(Notifications.Type.ERROR, 
                    Notifications.Location.TOP_CENTER,
                    "Ngày kết thúc phải lớn hơn ngày bắt đầu!");
            }
        }
    }
    
    /**
     * Validate tên khuyến mãi - kiểm tra trùng tên
     */
    private void validateTenKhuyenMai() {
        String tenKM = txtTenKhuyenMai.getText().trim();
        if (tenKM.isEmpty()) {
            return; // Chưa nhập tên, không cần validate
        }
        
        try {
            // Kiểm tra tên khuyến mãi có trùng không
            if (khuyenMaiBUS.existsByName(tenKM)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, 
                    Notifications.Location.TOP_CENTER,
                    "Tên khuyến mãi '" + tenKM + "' đã tồn tại!");
                // Xóa trắng và focus lại
                txtTenKhuyenMai.setText("");
                txtTenKhuyenMai.requestFocus();
            }
        } catch (Exception e) {
            // Không hiển thị lỗi khi có exception (có thể do lỗi kết nối DB)
            // Chỉ log để debug
            e.printStackTrace();
        }
    }

    public boolean isThemThanhCong() {
        return themThanhCong;
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
        listDanhSachSanPham = new javax.swing.JList<>();
        btnXoaSanPham = new javax.swing.JButton();
        btnXoaTrang = new javax.swing.JButton();
        lblSoLuongToiDa = new javax.swing.JLabel();
        txtSoLuongToiDa = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnLuu = new javax.swing.JButton();
        btnHuy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thêm khuyến mãi");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(115, 165, 71));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÊM KHUYẾN MÃI MỚI");

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

        btnLuu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.setPreferredSize(new java.awt.Dimension(120, 40));
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
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
                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {
        try {
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

            // Ngày bắt đầu không được nhỏ hơn ngày hiện tại
            LocalDate today = LocalDate.now();
            if (ngayBatDau.isBefore(today)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Ngày bắt đầu không được nhỏ hơn ngày hiện tại!");
                return;
            }

            // Kiểm tra ngày
            if (ngayKetThuc.isBefore(ngayBatDau)) {
                Notifications.getInstance().show(Notifications.Type.WARNING, "Ngày kết thúc phải sau ngày bắt đầu!");
                return;
            }

            // Tạo đối tượng KhuyenMai
            KhuyenMai km = new KhuyenMai();
            km.setTenKhuyenMai(tenKM);
            km.setNgayBatDau(ngayBatDau);
            km.setNgayKetThuc(ngayKetThuc);
            km.setGiamGia(giamGia);
            km.setTrangThai(true);

            String loaiKM = (String) cboLoaiKhuyenMai.getSelectedItem();
            km.setLoaiKhuyenMai(loaiKM.equals("Sản phẩm") ? LoaiKhuyenMai.SAN_PHAM : LoaiKhuyenMai.DON_HANG);

            // Nếu là khuyến mãi đơn hàng, lấy giá tối thiểu và tối đa
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
                        km.setGiaToiThieu(giaToiThieu);
                    } catch (NumberFormatException e) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, "Giá tối thiểu phải là số!");
                        txtGiaToiThieu.requestFocus();
                        return;
                    }
                } else {
                    km.setGiaToiThieu(0); // Mặc định không yêu cầu giá tối thiểu
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
                        km.setGiaToiDa(giaToiDa);
                    } catch (NumberFormatException e) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, "Giá giảm tối đa phải là số!");
                        txtGiaToiDa.requestFocus();
                        return;
                    }
                } else {
                    km.setGiaToiDa(0); // Mặc định không giới hạn giá giảm tối đa
                }
            }

            // Nếu là khuyến mãi sản phẩm, kiểm tra đã chọn sản phẩm chưa
            if (loaiKM.equals("Sản phẩm")) {
                if (danhSachSanPhamDaChon.isEmpty()) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng quét ít nhất 1 sản phẩm!");
                    txtQuetMa.requestFocus();
                    return;
                }
                
                // Kiểm tra conflict khuyến mãi cho từng sản phẩm
                List<String> danhSachSanPhamConflict = new ArrayList<>();
                for (SanPham sp : danhSachSanPhamDaChon) {
                    KhuyenMai khuyenMaiConflict = chiTietKhuyenMaiSanPhamBUS.kiemTraConflictKhuyenMai(
                        sp.getMaSanPham(), ngayBatDau, ngayKetThuc, null);
                    if (khuyenMaiConflict != null) {
                        danhSachSanPhamConflict.add(sp.getTenSanPham() + " (đang có khuyến mãi: " + 
                            khuyenMaiConflict.getTenKhuyenMai() + " từ " + 
                            khuyenMaiConflict.getNgayBatDau() + " đến " + 
                            khuyenMaiConflict.getNgayKetThuc() + ")");
                    }
                }
                
                if (!danhSachSanPhamConflict.isEmpty()) {
                    StringBuilder message = new StringBuilder("Không thể thêm khuyến mãi! Các sản phẩm sau đang có khuyến mãi đang hoạt động hoặc trùng thời gian:\n");
                    for (String spConflict : danhSachSanPhamConflict) {
                        message.append("- ").append(spConflict).append("\n");
                    }
                    message.append("\nVui lòng xóa khuyến mãi cũ hoặc đợi hết hạn trước khi thêm khuyến mãi mới!");
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
                        km.setSoLuongToiDa(soLuongToiDa);
                    } catch (NumberFormatException e) {
                        Notifications.getInstance().show(Notifications.Type.WARNING, "Số lượng tối đa phải là số nguyên!");
                        txtSoLuongToiDa.requestFocus();
                    return;
                    }
                } else {
                    km.setSoLuongToiDa(0); // Mặc định không giới hạn số lượng tối đa
                }
            }

            // Thêm vào database
            boolean success = khuyenMaiBUS.themKhuyenMai(km);
            if (success) {
                // Nếu là khuyến mãi sản phẩm, thêm vào bảng ChiTietKhuyenMaiSanPham cho tất cả sản phẩm đã chọn
                if (loaiKM.equals("Sản phẩm") && !danhSachSanPhamDaChon.isEmpty()) {
                    int soLuongThanhCong = 0;
                    int soLuongThatBai = 0;
                    
                    for (SanPham sp : danhSachSanPhamDaChon) {
                    try {
                        ChiTietKhuyenMaiSanPham ctkmsp = new ChiTietKhuyenMaiSanPham();
                            ctkmsp.setSanPham(sp);
                        ctkmsp.setKhuyenMai(km);
                        
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
                            "Thêm khuyến mãi thành công! " + soLuongThanhCong + " sản phẩm được liên kết, " + 
                            soLuongThatBai + " sản phẩm không thể liên kết!");
                    } else {
                        Notifications.getInstance().show(Notifications.Type.SUCCESS, 
                            "Thêm khuyến mãi thành công! Đã liên kết với " + soLuongThanhCong + " sản phẩm!");
                    }
                } else {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm khuyến mãi thành công!");
                }
                
                themThanhCong = true;
                dispose();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm khuyến mãi thất bại!");
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Notifications.getInstance().show(Notifications.Type.ERROR, errorMessage);
            
            // Nếu tên đã tồn tại, xóa trắng và focus lại
            if (errorMessage != null && errorMessage.contains("đã tồn tại")) {
                txtTenKhuyenMai.setText("");
                txtTenKhuyenMai.requestFocus();
            }
        }
    }

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnLuu;
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
    private javax.swing.JList<String> listDanhSachSanPham;
    private javax.swing.JButton btnXoaSanPham;
    private javax.swing.JButton btnXoaTrang;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtTenKhuyenMai;
    // End of variables declaration
}



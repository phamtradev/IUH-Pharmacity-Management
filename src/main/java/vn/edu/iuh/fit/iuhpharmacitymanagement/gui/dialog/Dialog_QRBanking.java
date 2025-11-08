package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.dialog;

import com.formdev.flatlaf.FlatClientProperties;
import com.google.zxing.WriterException;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.QRBankingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

/**
 * Dialog hiển thị QR Code thanh toán ngân hàng
 *
 * @author YourName
 */
public class Dialog_QRBanking extends JDialog {

    private final String maDonHang;
    private final double soTien;
    private JLabel lblQRCode;
    private JLabel lblSoTien;
    private JLabel lblMaDonHang;
    private JLabel lblThongTinChuyenKhoan;
    private JTextField txtBarcodeScan; // TextField ẩn để nhận input từ barcode scanner
    private JButton btnDong;
    private JPanel barcodePanel; // Panel chứa barcode textfield (có thể ẩn/hiện)
    private static final DecimalFormat df = new DecimalFormat("#,### đ");

    // Biến lưu trạng thái thanh toán
    private boolean daThanhtoan = false;

    /**
     * Constructor
     *
     * @param parent Frame cha
     * @param maDonHang Mã đơn hàng
     * @param soTien Số tiền cần thanh toán
     */
    public Dialog_QRBanking(Frame parent, String maDonHang, double soTien) {
        super(parent, "Thanh Toán QR Banking", true);
        this.maDonHang = maDonHang;
        this.soTien = soTien;

        initComponents();
        generateQRCode();
        setupBarcodeScanner(); // Thiết lập barcode scanner listener

        setSize(450, 650);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // ========== HEADER ==========
        JPanel pnHeader = new JPanel();
        pnHeader.setLayout(new BoxLayout(pnHeader, BoxLayout.Y_AXIS));
        pnHeader.setBackground(new Color(0, 102, 204)); // Màu xanh ngân hàng
        pnHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("THANH TOÁN QR CODE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSoTien = new JLabel(df.format(soTien));
        lblSoTien.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblSoTien.setForeground(Color.WHITE);
        lblSoTien.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMaDonHang = new JLabel("Mã đơn: " + maDonHang);
        lblMaDonHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMaDonHang.setForeground(Color.WHITE);
        lblMaDonHang.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnHeader.add(lblTitle);
        pnHeader.add(Box.createVerticalStrut(10));
        pnHeader.add(lblSoTien);
        pnHeader.add(Box.createVerticalStrut(5));
        pnHeader.add(lblMaDonHang);

        // ========== QR CODE ==========
        JPanel pnCenter = new JPanel();
        pnCenter.setLayout(new BoxLayout(pnCenter, BoxLayout.Y_AXIS));
        pnCenter.setBackground(Color.WHITE);
        pnCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Container cho QR code với viền
        JPanel qrContainer = new JPanel(new BorderLayout());
        qrContainer.setBackground(Color.WHITE);
        qrContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        lblQRCode = new JLabel("Đang tạo QR Code...", SwingConstants.CENTER);
        lblQRCode.setPreferredSize(new Dimension(300, 300));
        qrContainer.add(lblQRCode, BorderLayout.CENTER);
        qrContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hướng dẫn
        JLabel lblHuongDan = new JLabel("<html><center>📱 Quét mã QR bằng máy quét barcode<br/><small style='color:#666;'>Hệ thống sẽ tự động xác nhận khi quét thành công</small></center></html>");
        lblHuongDan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHuongDan.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHuongDan.setForeground(new Color(51, 51, 51));

        pnCenter.add(qrContainer);
        pnCenter.add(Box.createVerticalStrut(20));
        pnCenter.add(lblHuongDan);

        // ========== THÔNG TIN CHUYỂN KHOẢN ==========
        JPanel pnInfo = new JPanel();
        pnInfo.setLayout(new BoxLayout(pnInfo, BoxLayout.Y_AXIS));
        pnInfo.setBackground(new Color(245, 245, 245));
        pnInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblInfoTitle = new JLabel("Thông tin chuyển khoản:");
        lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblThongTinChuyenKhoan = new JLabel(getThongTinChuyenKhoanHTML());
        lblThongTinChuyenKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblThongTinChuyenKhoan.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnInfo.add(lblInfoTitle);
        pnInfo.add(Box.createVerticalStrut(8));
        pnInfo.add(lblThongTinChuyenKhoan);

        // ========== FOOTER (BARCODE INPUT + BUTTONS) ==========
        JPanel pnFooter = new JPanel();
        pnFooter.setLayout(new BoxLayout(pnFooter, BoxLayout.Y_AXIS));
        pnFooter.setBackground(Color.WHITE);
        pnFooter.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // TextField để nhận input từ barcode scanner (hiện nhưng không viền)
        barcodePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        barcodePanel.setBackground(Color.WHITE);
        barcodePanel.setVisible(true); // hiện textfield
        
        
        
        
        txtBarcodeScan = new JTextField();
        txtBarcodeScan.setPreferredSize(new Dimension(200, 25));
        txtBarcodeScan.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtBarcodeScan.setBorder(null); // Bỏ viền

        
        
        barcodePanel.add(txtBarcodeScan);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(150, 40));
        btnDong.setFocusPainted(false);

        // Style FlatLaf
        btnDong.putClientProperty(FlatClientProperties.STYLE,
                "arc:8;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:#6c757d;foreground:#FFFFFF");

        btnDong.addActionListener(e -> dispose());

        buttonPanel.add(btnDong);

        // Thêm vào footer
        pnFooter.add(barcodePanel);
        pnFooter.add(Box.createVerticalStrut(10));
        pnFooter.add(buttonPanel);

        // ========== ADD TO DIALOG ==========
        add(pnHeader, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
        add(pnInfo, BorderLayout.SOUTH);
        add(pnFooter, BorderLayout.PAGE_END);
    }
    
    /**
     * Thiết lập barcode scanner listener Khi máy quét barcode quét QR code, nó
     * sẽ nhập text vào textfield Sử dụng DocumentListener để tự động xử lý khi
     * có input (không cần Enter)
     */
    private void setupBarcodeScanner() {
        // Focus vào textfield khi dialog mở
        SwingUtilities.invokeLater(() -> {
            txtBarcodeScan.requestFocusInWindow();
            System.out.println("🎯 [QR Dialog] Đã focus vào barcode textfield, sẵn sàng quét!");
        });

        // Biến để theo dõi trạng thái xử lý (tránh xử lý nhiều lần)
        final java.util.concurrent.atomic.AtomicBoolean isProcessing = new java.util.concurrent.atomic.AtomicBoolean(false);
        final javax.swing.Timer[] barcodeTimer = new javax.swing.Timer[1]; // Mảng để có thể thay đổi trong lambda

        // Thêm FocusListener để debug
        txtBarcodeScan.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                System.out.println("✅ [QR Dialog Focus] Textfield ĐÃ NHẬN được focus!");
            }

            // COMMENT: Tắt auto-refocus - cho phép mất focus khi click button
            // @Override
            // public void focusLost(java.awt.event.FocusEvent e) {
            //     System.out.println("⚠️ [QR Dialog Focus] Textfield MẤT focus! Đang lấy lại...");
            //     SwingUtilities.invokeLater(() -> txtBarcodeScan.requestFocusInWindow());
            // }
        });

        // Thêm KeyListener để debug mọi phím nhấn
        txtBarcodeScan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                System.out.println("⌨️ [QR Dialog] Key typed: " + evt.getKeyChar() + " (code: " + evt.getKeyCode() + ")");
            }

            @Override
            public void keyPressed(KeyEvent evt) {
                System.out.println("⌨️ [QR Dialog] Key pressed: " + KeyEvent.getKeyText(evt.getKeyCode()));
            }
        });

        // DocumentListener để bắt mọi thay đổi text (KHÔNG CẦN Enter)
        txtBarcodeScan.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void handleTextChange() {
                // Hủy timer cũ nếu có
                if (barcodeTimer[0] != null && barcodeTimer[0].isRunning()) {
                    barcodeTimer[0].stop();
                }

                // Tạo timer mới: đợi 300ms không có thay đổi → xử lý
                barcodeTimer[0] = new javax.swing.Timer(300, evt -> {
                    String scannedText = txtBarcodeScan.getText().trim();
                    System.out.println("📷 [QR Dialog Scanner] Text đã nhập xong: " + scannedText);

                    if (!scannedText.isEmpty() && !isProcessing.get()) {
                        isProcessing.set(true);
                        handleScannedData(scannedText);
                        isProcessing.set(false);

                        // Clear textfield sau khi xử lý
                        SwingUtilities.invokeLater(() -> txtBarcodeScan.setText(""));
                    }
                });
                barcodeTimer[0].setRepeats(false);
                barcodeTimer[0].start();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                System.out.println("📝 [QR Dialog] Text thêm vào: " + txtBarcodeScan.getText());
                handleTextChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                System.out.println("📝 [QR Dialog] Text bị xóa: " + txtBarcodeScan.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                System.out.println("📝 [QR Dialog] Text cập nhật: " + txtBarcodeScan.getText());
                handleTextChange();
            }
        });

        // ActionListener (trigger khi nhấn Enter) - backup cho barcode scanner có Enter
        txtBarcodeScan.addActionListener(evt -> {
            String scannedText = txtBarcodeScan.getText().trim();
            System.out.println("⏎ [QR Dialog Enter] Nhận được: " + scannedText);

            if (!scannedText.isEmpty() && !isProcessing.get()) {
                isProcessing.set(true);
                handleScannedData(scannedText);
                isProcessing.set(false);

                // Clear textfield
                SwingUtilities.invokeLater(() -> txtBarcodeScan.setText(""));
            }
        });

        // Đảm bảo textfield luôn có focus để nhận input từ scanner
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                txtBarcodeScan.requestFocusInWindow();
                System.out.println("🎯 [QR Dialog] Window focus - Đã focus lại vào textfield!");
            }
        });

        // Chặn mất focus khi nhấn Tab
        txtBarcodeScan.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Xử lý dữ liệu từ barcode scanner
     *
     * @param scannedData Dữ liệu đã quét được
     */
    private void handleScannedData(String scannedData) {
        System.out.println("🔍 DEBUG - Dữ liệu quét được: " + scannedData); // DEBUG
        System.out.println("🔍 DEBUG - Mã đơn hàng: " + maDonHang); // DEBUG

        // Kiểm tra xem dữ liệu quét có chứa mã đơn hàng không
        if (scannedData.contains(maDonHang)) {
            // Đánh dấu đã thanh toán
            daThanhtoan = true;
            QRBankingUtil.markAsPaid(maDonHang, soTien);

            System.out.println("✅ DEBUG - Đã đánh dấu thanh toán thành công!"); // DEBUG

            // Hiển thị thông báo thành công bằng Notifications
            SwingUtilities.invokeLater(() -> {
                // Hiển thị notification
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.SUCCESS,
                        raven.toast.Notifications.Location.TOP_CENTER,
                        5000, // 5 giây
                        "✅ THANH TOÁN THÀNH CÔNG!\n\n"
                        + "Mã đơn: " + maDonHang + "\n"
                        + "Số tiền: " + df.format(soTien) + " đ\n"
                        + "Phương thức: Chuyển khoản ngân hàng\n\n"
                        + "Đang tự động in hóa đơn..."
                );

                // Đợi 1.5 giây để người dùng nhìn thấy notification rồi tự động đóng
                javax.swing.Timer closeTimer = new javax.swing.Timer(1500, evt -> {
                    dispose(); // Đóng dialog QR
                });
                closeTimer.setRepeats(false);
                closeTimer.start();
            });
        } else {
            // Dữ liệu quét không đúng
            System.out.println("❌ DEBUG - Mã QR không hợp lệ!"); // DEBUG
            SwingUtilities.invokeLater(() -> {
                raven.toast.Notifications.getInstance().show(
                        raven.toast.Notifications.Type.ERROR,
                        raven.toast.Notifications.Location.TOP_CENTER,
                        3000,
                        "❌ MÃ QR KHÔNG HỢP LỆ!\n\n"
                        + "Vui lòng quét đúng mã QR thanh toán.\n"
                        + "Dữ liệu quét: " + scannedData.substring(0, Math.min(50, scannedData.length())) + "..."
                );

                // Focus lại vào textfield
                txtBarcodeScan.requestFocusInWindow();
            });
        }
    }

    /**
     * Kiểm tra xem đã thanh toán chưa
     *
     * @return true nếu đã thanh toán thành công
     */
    public boolean isDaThanhtoan() {
        return daThanhtoan;
    }

    private void generateQRCode() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo QR code với kích thước 300x300
                BufferedImage qrImage = QRBankingUtil.generatePharmacityQR(maDonHang, soTien, 300);
                lblQRCode.setIcon(new ImageIcon(qrImage));
                lblQRCode.setText(null);
            } catch (WriterException e) {
                lblQRCode.setText("<html><center>❌<br/>Không thể tạo QR Code<br/>" + e.getMessage() + "</center></html>");
                lblQRCode.setForeground(Color.RED);
                e.printStackTrace();
            }
        });
    }

    private String getThongTinChuyenKhoanHTML() {
        return "<html>"
                + "<div style='line-height: 1.6;'>"
                + "• <b>Ngân hàng:</b> MB Bank (970422)<br/>"
                + "• <b>Số tài khoản:</b> 0123456789<br/>"
                + "• <b>Chủ tài khoản:</b> PHARMACITY MANAGEMENT<br/>"
                + "• <b>Số tiền:</b> " + df.format(soTien) + "<br/>"
                + "• <b>Nội dung:</b> THANHTOAN " + maDonHang
                + "</div>"
                + "</html>";
    }

    /**
     * Test dialog
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Dialog_QRBanking dialog = new Dialog_QRBanking(null, "DH081120250001", 125000);
            dialog.setVisible(true);
        });
    }
}

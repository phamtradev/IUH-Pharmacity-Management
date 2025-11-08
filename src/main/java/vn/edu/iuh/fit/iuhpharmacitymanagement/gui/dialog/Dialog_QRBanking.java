package vn.edu.iuh.fit.iuhpharmacitymanagement.gui.dialog;

import com.formdev.flatlaf.FlatClientProperties;
import com.google.zxing.WriterException;
import vn.edu.iuh.fit.iuhpharmacitymanagement.util.QRBankingUtil;

import javax.swing.*;
import java.awt.*;
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
    private static final DecimalFormat df = new DecimalFormat("#,### đ");
    
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
        JLabel lblHuongDan = new JLabel("Quét mã QR để thanh toán");
        lblHuongDan.setFont(new Font("Segoe UI", Font.BOLD, 16));
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
        
        // ========== FOOTER (BUTTONS) ==========
        JPanel pnFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        pnFooter.setBackground(Color.WHITE);
        
        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(150, 40));
        btnDong.setFocusPainted(false);
        
        // Style FlatLaf
        btnDong.putClientProperty(FlatClientProperties.STYLE, 
            "arc:8;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:#6c757d;foreground:#FFFFFF");
        
        btnDong.addActionListener(e -> dispose());
        
        pnFooter.add(btnDong);
        
        // ========== ADD TO DIALOG ==========
        add(pnHeader, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
        add(pnInfo, BorderLayout.SOUTH);
        add(pnFooter, BorderLayout.PAGE_END);
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
        return "<html>" +
               "<div style='line-height: 1.6;'>" +
               "• <b>Ngân hàng:</b> MB Bank (970422)<br/>" +
               "• <b>Số tài khoản:</b> 0123456789<br/>" +
               "• <b>Chủ tài khoản:</b> PHARMACITY MANAGEMENT<br/>" +
               "• <b>Số tiền:</b> " + df.format(soTien) + "<br/>" +
               "• <b>Nội dung:</b> THANHTOAN " + maDonHang +
               "</div>" +
               "</html>";
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


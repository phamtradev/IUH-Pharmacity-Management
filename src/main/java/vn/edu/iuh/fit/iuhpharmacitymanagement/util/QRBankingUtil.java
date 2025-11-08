package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class để tạo QR code thanh toán đơn giản (demo)
 * 
 * @author PhamTraPhamTra
 */
public class QRBankingUtil {
    
    // Thông tin ngân hàng demo
    private static final String BANK_NAME = "MB Bank";
    private static final String ACCOUNT_NUMBER = "0123456789";
    private static final String ACCOUNT_NAME = "PHARMACITY STORE";
    
    /**
     * Tạo QR code thanh toán đơn giản cho demo
     * 
     * @param maDonHang Mã đơn hàng
     * @param amount Số tiền
     * @param size Kích thước QR (pixels - vuông)
     * @return BufferedImage QR code
     * @throws WriterException Nếu lỗi khi tạo QR
     */
    public static BufferedImage generatePharmacityQR(String maDonHang, double amount, int size) 
            throws WriterException {
        // Tạo nội dung QR đơn giản
        String content = String.format(
            "Ngân hàng: %s\n" +
            "Số TK: %s\n" +
            "Tên TK: %s\n" +
            "Số tiền: %,.0f VND\n" +
            "Nội dung: Thanh toan %s",
            BANK_NAME,
            ACCOUNT_NUMBER,
            ACCOUNT_NAME,
            amount,
            maDonHang
        );
        
        return generateQRCode(content, size, size);
    }
    
    /**
     * Generate QR code từ nội dung text
     * 
     * @param content Text cần mã hóa
     * @param width Chiều rộng QR code (pixels)
     * @param height Chiều cao QR code (pixels)
     * @return BufferedImage chứa QR code
     * @throws WriterException Nếu không tạo được QR
     */
    public static BufferedImage generateQRCode(String content, int width, int height) throws WriterException {
        // Cấu hình QR Code
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        
        // Tạo QR Code matrix
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        
        // Chuyển BitMatrix thành BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        
        return image;
    }
    
    /**
     * Lấy thông tin ngân hàng (để hiển thị)
     */
    public static String getBankInfo() {
        return BANK_NAME + " - " + ACCOUNT_NUMBER;
    }
    
    /**
     * Lấy tên ngân hàng
     */
    public static String getBankName() {
        return BANK_NAME;
    }
    
    /**
     * Lấy số tài khoản
     */
    public static String getAccountNumber() {
        return ACCOUNT_NUMBER;
    }
    
    /**
     * Lấy tên tài khoản
     */
    public static String getAccountName() {
        return ACCOUNT_NAME;
    }
    
    /**
     * Format số tiền
     */
    public static String formatMoney(double amount) {
        return String.format("%,.0f đ", amount);
    }
}

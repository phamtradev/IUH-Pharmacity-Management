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
 * Utility class để tạo QR code thanh toán ngân hàng (Mô phỏng VietQR)
 * 
 * @author PhamTraPhamTra
 */
public class QRBankingUtil {
    
    /**
     * Tạo payload text cho QR Banking (Mô phỏng đơn giản)
     * Format: bankCode|accountNumber|accountName|amount|description
     * 
     * @param bankCode Mã ngân hàng (VD: "970422" = MB Bank, "970415" = Vietinbank)
     * @param accountNumber Số tài khoản
     * @param accountName Tên chủ tài khoản
     * @param amount Số tiền cần thanh toán (VND)
     * @param description Nội dung chuyển khoản
     * @return Payload text
     */
    public static String createBankingPayload(String bankCode, String accountNumber, 
                                             String accountName, double amount, String description) {
        // Format đơn giản cho mô phỏng
        // Trong thực tế, VietQR sử dụng EMVCo QR Code chuẩn
        return String.format("BANK:%s|ACC:%s|NAME:%s|AMOUNT:%.0f|DESC:%s", 
                            bankCode, accountNumber, accountName, amount, description);
    }
    
    /**
     * Tạo payload mặc định cho PHARMACITY
     */
    public static String createPharmacityPayload(String maDonHang, double amount) {
        String bankCode = "970422"; // MB Bank
        String accountNumber = "0123456789";
        String accountName = "PHARMACITY MANAGEMENT";
        String description = "THANHTOAN " + maDonHang;
        
        return createBankingPayload(bankCode, accountNumber, accountName, amount, description);
    }
    
    /**
     * Generate QR code từ payload text
     * 
     * @param payload Text cần mã hóa
     * @param width Chiều rộng QR code (pixels)
     * @param height Chiều cao QR code (pixels)
     * @return BufferedImage chứa QR code
     * @throws WriterException Nếu không tạo được QR
     */
    public static BufferedImage generateQRCode(String payload, int width, int height) throws WriterException {
        // Cấu hình QR Code
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); // Margin nhỏ
        
        // Tạo QR Code matrix
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, width, height, hints);
        
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
     * Tạo QR code thanh toán cho Pharmacity
     * 
     * @param maDonHang Mã đơn hàng
     * @param amount Số tiền
     * @param size Kích thước QR (pixels - vuông)
     * @return BufferedImage QR code
     * @throws WriterException Nếu lỗi khi tạo QR
     */
    public static BufferedImage generatePharmacityQR(String maDonHang, double amount, int size) 
            throws WriterException {
        String payload = createPharmacityPayload(maDonHang, amount);
        return generateQRCode(payload, size, size);
    }
}


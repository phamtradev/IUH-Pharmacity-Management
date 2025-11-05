/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dohoainho
 */
public class BarcodeUtil {

    
    public static BufferedImage taoBarcode(String data) {
        int width = 200;
        int height = 30;

        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(data, BarcodeFormat.CODE_128, width, height, hints);

            return MatrixToImageWriter.toBufferedImage(matrix);

        } catch (Exception ex) {
            throw new RuntimeException("lỗi tạo bar", ex);
        }
    }
   public static BufferedImage addTextBelow(BufferedImage qrImage, String text) {
        int width = qrImage.getWidth();
        int height = qrImage.getHeight();
        int extraHeight = 20; // Tăng không gian cho text (từ 10 lên 20)

        BufferedImage combined = new BufferedImage(width, height + extraHeight,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = combined.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height + extraHeight);

        g.drawImage(qrImage, 0, 0, null);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10)); // Tăng font size (từ 5 lên 10) để text rõ hơn

        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = height + (extraHeight + fm.getAscent()) / 2 - 2; // Điều chỉnh vị trí (từ -5 lên -2)

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawString(text, x, y);
        g.dispose();

        return combined;
    }
   
   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import static java.awt.SystemColor.text;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
//import static vn.edu.iuh.fit.iuhpharmacitymanagement.util.XuatHoaDonTraHangPDF.generateFileName;

/**
 *
 * @author dohoainho
 */
public class QrCodeUtil {

    //chinh lai cho gson k truy cap va local date dc
    public static String chuyenObSangJon(Object o) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class,
                            (com.google.gson.JsonSerializer<LocalDate>) (src, typeOfSrc, context)
                            -> new com.google.gson.JsonPrimitive(src.toString()))
                    .create();

            return gson.toJson(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //moi con ở buff
    public static BufferedImage taoQRImg(String data) {
        int width = 300;
        int height = 80;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(data, BarcodeFormat.QR_CODE, width, height, hints);                    

            return MatrixToImageWriter.toBufferedImage(matrix);

        } catch (Exception ex) {
            throw new RuntimeException("lỗi tạo qr", ex);
        }
    }

    public static BufferedImage themThongTin(BufferedImage qrImage, String tt) {
        int width = qrImage.getWidth();
        int height = qrImage.getHeight();
        int extraHeight = 40;

        BufferedImage combined = new BufferedImage(width, height + extraHeight,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = combined.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height + extraHeight);

        g.drawImage(qrImage, 0, 0, null);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(tt)) / 2;
        int y = height + (extraHeight + fm.getAscent()) / 2 - 5;

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawString(tt, x, y);
        g.dispose();

        return combined;
    }

    //từ buff sang file 
    public static boolean luuQrRafile(BufferedImage image, String filePath) {
        try {
            ImageIO.write(image, "png", new File(filePath));
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("k lưu dc qr", ex);
        }
    }

    public boolean createQr(DonHang dh) {
        String text = chuyenObSangJon(dh);
        BufferedImage qrImg = taoQRImg(text);
        BufferedImage finalQR = themThongTin(qrImg, dh.getMaDonHang());   
         String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + "/src/main/resources/img/donhang_QR";
            
            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String fileName = dh.getMaDonHang()+".png";
            String fullPath = folderPath + "/" + fileName;
        if (luuQrRafile(finalQR, fullPath)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        DonHang dh = new DonHang("DH041120259999");
        System.out.println("vn.edu.iuh.fit.iuhpharmacitymanagement.util.QrCodeUtil.main()");
        System.out.println(new QrCodeUtil().createQr(dh));
    }
}

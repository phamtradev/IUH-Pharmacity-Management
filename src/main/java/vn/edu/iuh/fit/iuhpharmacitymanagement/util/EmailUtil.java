/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.text.DecimalFormat;

/**
 * Utility class để gửi email sử dụng SendGrid API
 * @author PhamTra
 */
public class EmailUtil {
    
    // Cấu hình email được load từ file properties
    private static String SENDGRID_API_KEY;
    private static String SENDER_EMAIL;
    private static String SENDER_NAME;
    
    // Static block để load config khi class được khởi tạo
    static {
        loadEmailConfig();
    }
    
    /**
     * Load cấu hình email từ file email.properties
     */
    private static void loadEmailConfig() {
        Properties props = new Properties();
        try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                System.err.println("❌ Không tìm thấy file email.properties!");
                // Set giá trị mặc định
                SENDGRID_API_KEY = "";
                SENDER_EMAIL = "";
                SENDER_NAME = "Pharmacity Management System";
                return;
            }
            
            // Load properties từ file
            props.load(input);
            
            SENDGRID_API_KEY = props.getProperty("sendgrid.api.key", "");
            SENDER_EMAIL = props.getProperty("sender.email", "");
            SENDER_NAME = props.getProperty("sender.name", "Pharmacity Management System");
            
            System.out.println("✅ Đã load cấu hình SendGrid email thành công!");
            
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi đọc file email.properties: " + e.getMessage());
            // Set giá trị mặc định
            SENDGRID_API_KEY = "";
            SENDER_EMAIL = "";
            SENDER_NAME = "Pharmacity Management System";
        }
    }
    
    /**
     * Gửi email khuyến mãi đến khách hàng sử dụng SendGrid
     * @param khachHang Thông tin khách hàng
     * @param khuyenMai Thông tin khuyến mãi
     * @return true nếu gửi thành công, false nếu thất bại
     */
    public static boolean guiEmailKhuyenMai(KhachHang khachHang, KhuyenMai khuyenMai) {
        try {
            // Kiểm tra cấu hình
            if (!kiemTraCauHinhEmail()) {
                System.err.println("❌ Chưa cấu hình SendGrid API Key!");
                return false;
            }
            
            // Kiểm tra email khách hàng
            if (khachHang.getEmail() == null || khachHang.getEmail().trim().isEmpty()) {
                System.err.println("❌ Khách hàng không có email!");
                return false;
            }
            
            // Tạo email từ SendGrid
            Email from = new Email(SENDER_EMAIL, SENDER_NAME);
            Email to = new Email(khachHang.getEmail(), khachHang.getTenKhachHang());
            String subject = "🎉 Chương trình khuyến mãi đặc biệt dành cho bạn!";
            
            // Tạo nội dung HTML
            String htmlContent = taoNoiDungEmailHTML(khachHang, khuyenMai);
            Content content = new Content("text/html", htmlContent);
            
            // Tạo mail object
            Mail mail = new Mail(from, subject, to, content);
            
            // Gửi email qua SendGrid
            SendGrid sg = new SendGrid(SENDGRID_API_KEY);
            Request request = new Request();
            
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            // Kiểm tra response
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Gửi email thành công đến: " + khachHang.getEmail());
                System.out.println("📧 Response code: " + response.getStatusCode());
                return true;
            } else {
                System.err.println("❌ Gửi email thất bại!");
                System.err.println("Status code: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
                return false;
            }
            
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tạo nội dung email HTML đẹp mắt
     */
    private static String taoNoiDungEmailHTML(KhachHang khachHang, KhuyenMai khuyenMai) {
        DecimalFormat df = new DecimalFormat("#,###");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='vi'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<style>");
        html.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }");
        html.append(".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 28px; }");
        html.append(".content { padding: 30px; }");
        html.append(".greeting { font-size: 18px; color: #333; margin-bottom: 20px; }");
        html.append(".promo-box { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; padding: 25px; border-radius: 8px; margin: 20px 0; text-align: center; }");
        html.append(".promo-box h2 { margin: 0 0 10px 0; font-size: 24px; }");
        html.append(".promo-code { background-color: white; color: #f5576c; padding: 15px 25px; border-radius: 5px; font-size: 24px; font-weight: bold; margin: 15px 0; display: inline-block; letter-spacing: 2px; }");
        html.append(".info-table { width: 100%; margin: 20px 0; border-collapse: collapse; }");
        html.append(".info-table td { padding: 12px; border-bottom: 1px solid #eee; }");
        html.append(".info-table td:first-child { font-weight: bold; color: #667eea; width: 40%; }");
        html.append(".footer { background-color: #f8f9fa; padding: 20px; text-align: center; color: #666; font-size: 14px; }");
        html.append(".button { display: inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        html.append("<div class='container'>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<h1>🎉 IUH PHARMACITY 🎉</h1>");
        html.append("<p style='margin: 10px 0 0 0; font-size: 16px;'>Chương trình khuyến mãi đặc biệt</p>");
        html.append("</div>");
        
        // Content
        html.append("<div class='content'>");
        
        // Greeting
        html.append("<div class='greeting'>");
        html.append("Xin chào <strong>").append(khachHang.getTenKhachHang()).append("</strong>,");
        html.append("</div>");
        
        html.append("<p style='color: #555; line-height: 1.6;'>");
        html.append("Chúng tôi rất vui được gửi đến bạn chương trình khuyến mãi đặc biệt dành riêng cho khách hàng thân thiết!");
        html.append("</p>");
        
        // Promo Box
        html.append("<div class='promo-box'>");
        html.append("<h2>").append(khuyenMai.getTenKhuyenMai()).append("</h2>");
        html.append("<p style='margin: 10px 0; font-size: 16px;'>Mã khuyến mãi của bạn:</p>");
        html.append("<div class='promo-code'>").append(khuyenMai.getMaKhuyenMai()).append("</div>");
        
        // Hiển thị giá trị khuyến mãi
        html.append("<p style='font-size: 20px; margin: 15px 0 0 0;'>🎁 Giảm <strong>")
            .append(df.format(khuyenMai.getGiamGia()))
            .append(khuyenMai.getLoaiKhuyenMai().toString().equals("PHAN_TRAM") ? "%" : " VNĐ")
            .append("</strong></p>");
        html.append("</div>");
        
        // Info Table
        html.append("<table class='info-table'>");
        html.append("<tr><td>📅 Ngày bắt đầu:</td><td>").append(khuyenMai.getNgayBatDau().format(dateFormatter)).append("</td></tr>");
        html.append("<tr><td>📅 Ngày kết thúc:</td><td>").append(khuyenMai.getNgayKetThuc().format(dateFormatter)).append("</td></tr>");
        html.append("<tr><td>📋 Loại khuyến mãi:</td><td>").append(khuyenMai.getLoaiKhuyenMai().toString()).append("</td></tr>");
        html.append("<tr><td>🎯 Trạng thái:</td><td>").append(khuyenMai.hienThiTrangThai()).append("</td></tr>");
        html.append("</table>");
        
        html.append("<p style='color: #555; line-height: 1.6; margin-top: 20px;'>");
        html.append("Hãy nhanh tay sử dụng mã khuyến mãi này để nhận ưu đãi tuyệt vời cho đơn hàng của bạn!");
        html.append("</p>");
        
        html.append("<div style='text-align: center;'>");
        html.append("<a href='#' class='button'>MUA SẮM NGAY</a>");
        html.append("</div>");
        
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p style='margin: 5px 0;'><strong>PHARMACITY MANAGEMENT SYSTEM</strong></p>");
        html.append("<p style='margin: 5px 0;'>Cảm ơn bạn đã tin tưởng và đồng hành cùng chúng tôi! 💙</p>");
        html.append("<p style='margin: 5px 0; font-size: 12px; color: #999;'>Email này được gửi tự động, vui lòng không trả lời.</p>");
        html.append("</div>");
        
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Kiểm tra cấu hình email có hợp lệ không
     */
    public static boolean kiemTraCauHinhEmail() {
        return SENDGRID_API_KEY != null && !SENDGRID_API_KEY.isEmpty() 
            && !SENDGRID_API_KEY.equals("your-sendgrid-api-key-here")
            && SENDER_EMAIL != null && !SENDER_EMAIL.isEmpty();
    }
    
    /**
     * Lấy thông tin email đã cấu hình (để hiển thị)
     */
    public static String getConfiguredEmail() {
        return SENDER_EMAIL;
    }
    
    /**
     * Kiểm tra kết nối SendGrid
     */
    public static boolean kiemTraKetNoi() {
        try {
            if (!kiemTraCauHinhEmail()) {
                return false;
            }
            
            // Kiểm tra API Key có hợp lệ không
            new SendGrid(SENDGRID_API_KEY);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kiểm tra kết nối SendGrid: " + e.getMessage());
            return false;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhachHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.text.DecimalFormat;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.NhanVienDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.TaiKhoanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.NhanVien;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.TaiKhoan;

/**
 * Utility class để gửi email sử dụng JavaMail API (SMTP)
 *
 * @author PhamTra
 */
public class EmailUtil {

    // Cấu hình email được load từ file properties
    private static String SMTP_HOST;
    private static String SMTP_PORT;
    private static String SMTP_AUTH;
    private static String SMTP_STARTTLS;
    private static String SENDER_EMAIL;
    private static String SENDER_PASSWORD;
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
                SMTP_HOST = "smtp.gmail.com";
                SMTP_PORT = "587";
                SMTP_AUTH = "true";
                SMTP_STARTTLS = "true";
                SENDER_EMAIL = "";
                SENDER_PASSWORD = "";
                SENDER_NAME = "Pharmacity Management System";
                return;
            }

            // Load properties từ file
            props.load(input);

            SMTP_HOST = props.getProperty("mail.smtp.host", "smtp.gmail.com");
            SMTP_PORT = props.getProperty("mail.smtp.port", "587");
            SMTP_AUTH = props.getProperty("mail.smtp.auth", "true");
            SMTP_STARTTLS = props.getProperty("mail.smtp.starttls.enable", "true");
            SENDER_EMAIL = props.getProperty("sender.email", "");
            SENDER_PASSWORD = props.getProperty("sender.password", "");
            SENDER_NAME = props.getProperty("sender.name", "Pharmacity Management System");

            System.out.println("✅ Đã load cấu hình JavaMail (SMTP) thành công!");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi đọc file email.properties: " + e.getMessage());
            // Set giá trị mặc định
            SMTP_HOST = "smtp.gmail.com";
            SMTP_PORT = "587";
            SMTP_AUTH = "true";
            SMTP_STARTTLS = "true";
            SENDER_EMAIL = "";
            SENDER_PASSWORD = "";
            SENDER_NAME = "Pharmacity Management System";
        }
    }

    /**
     * Gửi email khuyến mãi đến khách hàng sử dụng JavaMail (SMTP)
     *
     * @param khachHang Thông tin khách hàng
     * @param khuyenMai Thông tin khuyến mãi
     * @return true nếu gửi thành công, false nếu thất bại
     */
    public static boolean guiEmailKhuyenMai(KhachHang khachHang, KhuyenMai khuyenMai) {
        try {
            // Kiểm tra cấu hình
            if (!kiemTraCauHinhEmail()) {
                System.err.println("❌ Chưa cấu hình email SMTP!");
                return false;
            }

            // Kiểm tra email khách hàng
            if (khachHang.getEmail() == null || khachHang.getEmail().trim().isEmpty()) {
                System.err.println("❌ Khách hàng không có email!");
                return false;
            }

            // Cấu hình SMTP properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", SMTP_AUTH);
            props.put("mail.smtp.starttls.enable", SMTP_STARTTLS);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);

            // Tạo session với authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            // Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME, "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(khachHang.getEmail()));
            message.setSubject("🎉 Chương trình khuyến mãi đặc biệt dành cho bạn!");

            // Tạo nội dung HTML
            String htmlContent = taoNoiDungEmailHTML(khachHang, khuyenMai);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);

            System.out.println("✅ Gửi email thành công đến: " + khachHang.getEmail());
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("❌ Lỗi không xác định khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tạo nội dung email HTML đẹp mắt
     */
    private static String taoNoiDungEmailHTML(KhachHang khachHang, KhuyenMai khuyenMai) {
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

        // Hiển thị giá trị khuyến mãi (giamGia lưu dưới dạng thập phân: 0.2 = 20%)
        DecimalFormat percentFormat = new DecimalFormat("#.##%");
        String giamGiaText = percentFormat.format(khuyenMai.getGiamGia());

        html.append("<p style='font-size: 20px; margin: 15px 0 0 0;'>🎁 Giảm <strong>")
                .append(giamGiaText)
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
        return SENDER_EMAIL != null && !SENDER_EMAIL.isEmpty()
                && SENDER_PASSWORD != null && !SENDER_PASSWORD.isEmpty()
                && !SENDER_PASSWORD.equals("YOUR_APP_PASSWORD_HERE")
                && SMTP_HOST != null && !SMTP_HOST.isEmpty();
    }

    /**
     * Lấy thông tin email đã cấu hình (để hiển thị)
     */
    public static String getConfiguredEmail() {
        return SENDER_EMAIL;
    }

    /**
     * Kiểm tra kết nối SMTP Server
     */
    public static boolean kiemTraKetNoi() {
        try {
            if (!kiemTraCauHinhEmail()) {
                System.err.println("❌ Chưa cấu hình đầy đủ thông tin email!");
                return false;
            }

            // Cấu hình SMTP properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", SMTP_AUTH);
            props.put("mail.smtp.starttls.enable", SMTP_STARTTLS);
            props.put("mail.smtp.connectiontimeout", "5000");
            props.put("mail.smtp.timeout", "5000");

            // Tạo session và kiểm tra kết nối
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            // Thử kết nối đến SMTP server
            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_HOST, SENDER_EMAIL, SENDER_PASSWORD);
            transport.close();

            System.out.println("✅ Kết nối SMTP thành công!");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kiểm tra kết nối SMTP: " + e.getMessage());
            return false;
        }
    }

    public String ramdomPass(NhanVien nv) {
        // Tập ký tự sử dụng
        String chuSo = "0123456789";
        String kyTuDacBiet = "@#$^*";
        String chuCai = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String tatCaKyTu = chuSo + kyTuDacBiet + chuCai;

        Random random = new Random();
        StringBuilder matKhau = new StringBuilder();

        // Tiền tố theo vai trò
        if ("Nhân viên".equals(nv.getVaiTro())) {
            matKhau.append("nv");
        } else {
            matKhau.append("ql");
        }

        // Random chiều dài phần còn lại (tối thiểu 4 ký tự → tối thiểu 6 ký tự tổng)
        int soKyTuConLai = random.nextInt(5) + 4; // 4 đến 8 ký tự

        boolean coSo = false;
        boolean coKyTuDacBiet = false;

        // Random từng ký tự vào mật khẩu
        for (int i = 0; i < soKyTuConLai; i++) {
            char kyTu = tatCaKyTu.charAt(random.nextInt(tatCaKyTu.length()));
            matKhau.append(kyTu);

            if (chuSo.indexOf(kyTu) >= 0) {
                coSo = true;
            }
            if (kyTuDacBiet.indexOf(kyTu) >= 0) {
                coKyTuDacBiet = true;
            }
        }
        
        if (!coSo) {
            matKhau.append(chuSo.charAt(random.nextInt(chuSo.length())));
        }

        if (!coKyTuDacBiet) {
            matKhau.append(kyTuDacBiet.charAt(random.nextInt(kyTuDacBiet.length())));
        }       
        return matKhau.toString();
    }
    private String taoNoiDungEmailLayPassHTML(NhanVien nv, String pass) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='vi'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }");
        html.append(".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; ");
        html.append("overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }");
        html.append(".header { background: linear-gradient(135deg, #007bff 0%, #0056d2 100%); color: white; padding: 25px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 24px; }");
        html.append(".content { padding: 30px; font-size: 16px; color: #333; line-height: 1.6; }");
        html.append(".box { background-color: #f1f5ff; border-left: 4px solid #007bff; padding: 15px; margin: 15px 0; border-radius: 5px; }");
        html.append(".password { font-size: 22px; font-weight: bold; color: #d63384; margin: 10px 0; }");
        html.append(".footer { background: #f8f9fa; text-align: center; font-size: 14px; color: #666; padding: 15px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        html.append("<div class='container'>");

        html.append("<div class='header'>");
        html.append("<h1>Pharmacity Management System</h1>");
        html.append("<p class='sub'>Cấp lại mật khẩu đăng nhập</p>");
        html.append("</div>");

        html.append("<div class='content'>");
        html.append("<p>Xin chào <strong>").append(nv.getTenNhanVien()).append("</strong>,</p>");
        html.append("<p>Bạn vừa yêu cầu cấp lại mật khẩu đăng nhập hệ thống.</p>");

        html.append("<div class='box'>");
        html.append("<p><strong>Mật khẩu truy cập mới của bạn:</strong></p>");
       // pass = ramdomPass(nv);
        html.append("<div class='password'>").append(pass).append("</div>");
        html.append("</div>");

        html.append("<p>Vui lòng đăng nhập và thay đổi mật khẩu ngay sau khi truy cập để đảm bảo tính bảo mật tài khoản.</p>");

        html.append("<p>👉 Email đăng nhập: <strong>").append(nv.getEmail()).append("</strong></p>");
        html.append("<p>Nếu bạn không yêu cầu thao tác này, vui lòng liên hệ quản trị hệ thống ngay lập tức.</p>");
        html.append("</div>");

        html.append("<div class='footer'>");
        html.append("Email được gửi tự động – vui lòng không phản hồi.<br>");
        html.append("© Pharmacity Management System 2025");
        html.append("</div>");

        html.append("</div>");
        html.append("</body></html>");

        return html.toString();
    }
    TaiKhoanDAO taiKhoanDao = new TaiKhoanDAO();

    public boolean guiEmailCapPass(NhanVien nv) {
        try {
            // Kiểm tra cấu hình
            if (!kiemTraCauHinhEmail()) {
                System.err.println("❌ Chưa cấu hình email SMTP!");
                return false;
            }

            // Cấu hình SMTP properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", SMTP_AUTH);
            props.put("mail.smtp.starttls.enable", SMTP_STARTTLS);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);

            // Tạo session với authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            // Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME, "UTF-8"));
            //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(khachHang.getEmail()));
            message.setSubject("Pharmacity - Cấp lại mật khẩu tài khoản cho nhân viên "+nv.getTenNhanVien());

            // Tạo nội dung HTML     
            String pass = ramdomPass(nv);
            String htmlContent = taoNoiDungEmailLayPassHTML(new NhanVienDAO().findById("NV00021").get(), pass);
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(nv.getEmail(), false));
            // Gửi email
            Transport.send(message);
            //gửi xong upd
            if (taiKhoanDao.updatePass(taiKhoanDao.findById(nv.getMaNhanVien()).get(), nv, pass)) {
                return true;
            }
            return false;

        } catch (MessagingException e) {
            System.err.println("❌ Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("❌ Lỗi không xác định khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        new EmailUtil().guiEmailCapPass(new NhanVienDAO().findById("NV00021").get());
    }
    
}

package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Class tiện ích để xuất hóa đơn ra file PDF
 * @author PhamTra
 */
public class XuatHoaDonPDF {
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Màu sắc
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(0, 120, 215);
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(240, 240, 240);
    
    /**
     * Xuất hóa đơn ra file PDF
     * @param donHang đơn hàng cần xuất
     * @param chiTietDonHangList danh sách chi tiết đơn hàng
     * @param outputPath đường dẫn lưu file PDF
     * @param khuyenMaiSanPham khuyến mãi sản phẩm (có thể null)
     * @return true nếu xuất thành công, false nếu thất bại
     */
    public static boolean xuatHoaDon(DonHang donHang, List<ChiTietDonHang> chiTietDonHangList, String outputPath, KhuyenMai khuyenMaiSanPham) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            File file = new File(outputPath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Khởi tạo PDF writer và document
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            
            // Set margins
            document.setMargins(30, 30, 30, 30);
            
            // Tạo font hỗ trợ tiếng Việt
            // Sử dụng font Arial từ Windows hoặc font hệ thống
            PdfFont font;
            PdfFont boldFont;
            
            try {
                // Thử load font Arial từ Windows
                String arialPath = "C:/Windows/Fonts/arial.ttf";
                String arialBoldPath = "C:/Windows/Fonts/arialbd.ttf";
                
                if (new File(arialPath).exists()) {
                    font = PdfFontFactory.createFont(arialPath, PdfEncodings.IDENTITY_H);
                } else {
                    font = PdfFontFactory.createFont("Helvetica", PdfEncodings.IDENTITY_H);
                }
                
                if (new File(arialBoldPath).exists()) {
                    boldFont = PdfFontFactory.createFont(arialBoldPath, PdfEncodings.IDENTITY_H);
                } else {
                    boldFont = PdfFontFactory.createFont("Helvetica-Bold", PdfEncodings.IDENTITY_H);
                }
            } catch (Exception e) {
                // Fallback to default fonts
                font = PdfFontFactory.createFont("Helvetica", PdfEncodings.IDENTITY_H);
                boldFont = PdfFontFactory.createFont("Helvetica-Bold", PdfEncodings.IDENTITY_H);
            }
            
            // Header - Thông tin cửa hàng
            addStoreHeader(document, boldFont, font);
            
            // Tiêu đề hóa đơn
            addInvoiceTitle(document, boldFont, donHang);
            
            // Thông tin khách hàng và nhân viên
            addCustomerAndEmployeeInfo(document, font, boldFont, donHang);
            
            // Bảng chi tiết sản phẩm
            addProductTable(document, font, boldFont, chiTietDonHangList);
            
            // Thông tin thanh toán
            addPaymentInfo(document, font, boldFont, donHang, chiTietDonHangList, khuyenMaiSanPham);
            
            // Footer
            addFooter(document, font);
            
            // Đóng document
            document.close();
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Thêm header cửa hàng
     */
    private static void addStoreHeader(Document document, PdfFont boldFont, PdfFont font) {
        // Tên cửa hàng
        Paragraph storeName = new Paragraph("IUH PHARMACITY")
                .setFont(boldFont)
                .setFontSize(20)
                .setFontColor(HEADER_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(storeName);
        
        // Địa chỉ
        Paragraph address = new Paragraph("12 Nguyen Van Bao, Ward 4, Go Vap District, Ho Chi Minh City")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2);
        document.add(address);
        
        // Hotline
        Paragraph hotline = new Paragraph("Hotline: 1800 6928 | Email: cskh@pharmacity.vn")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(hotline);
    }
    
    /**
     * Thêm tiêu đề hóa đơn
     */
    private static void addInvoiceTitle(Document document, PdfFont boldFont, DonHang donHang) {
        Paragraph title = new Paragraph("HOA DON BAN HANG")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(title);
        
        // Mã hóa đơn
        Paragraph invoiceCode = new Paragraph("Ma hoa don: " + donHang.getMaDonHang())
                .setFont(boldFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2);
        document.add(invoiceCode);
        
        // Ngày lập
        Paragraph date = new Paragraph("Ngay lap: " + donHang.getNgayDatHang().format(DATE_FORMAT))
                .setFont(boldFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(date);
    }
    
    /**
     * Thêm thông tin khách hàng và nhân viên
     */
    private static void addCustomerAndEmployeeInfo(Document document, PdfFont font, PdfFont boldFont, DonHang donHang) {
        // Tạo bảng 2 cột
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        // Thông tin khách hàng (cột trái)
        KhachHang khachHang = donHang.getKhachHang();
        StringBuilder customerInfo = new StringBuilder();
        customerInfo.append("THONG TIN KHACH HANG\n");
        customerInfo.append("Ho ten: ").append(khachHang != null ? khachHang.getTenKhachHang() : "Vang lai").append("\n");
        if (khachHang != null) {
            customerInfo.append("Ma KH: ").append(khachHang.getMaKhachHang()).append("\n");
            customerInfo.append("SDT: ").append(khachHang.getSoDienThoai()).append("\n");
            if (khachHang.getDiaChi() != null && !khachHang.getDiaChi().isEmpty()) {
                customerInfo.append("Dia chi: ").append(khachHang.getDiaChi());
            }
        }
        
        Cell customerCell = new Cell()
                .add(new Paragraph(customerInfo.toString()).setFont(font).setFontSize(9))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        infoTable.addCell(customerCell);
        
        // Thông tin nhân viên (cột phải)
        NhanVien nhanVien = donHang.getNhanVien();
        StringBuilder employeeInfo = new StringBuilder();
        employeeInfo.append("THONG TIN NHAN VIEN\n");
        employeeInfo.append("Ho ten: ").append(nhanVien != null ? nhanVien.getTenNhanVien() : "").append("\n");
        if (nhanVien != null) {
            employeeInfo.append("Ma NV: ").append(nhanVien.getMaNhanVien()).append("\n");
            employeeInfo.append("SDT: ").append(nhanVien.getSoDienThoai());
        }
        
        Cell employeeCell = new Cell()
                .add(new Paragraph(employeeInfo.toString()).setFont(font).setFontSize(9))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        infoTable.addCell(employeeCell);
        
        document.add(infoTable);
    }
    
    /**
     * Thêm bảng chi tiết sản phẩm
     */
    private static void addProductTable(Document document, PdfFont font, PdfFont boldFont, List<ChiTietDonHang> chiTietDonHangList) {
        // Tạo bảng với 6 cột
        float[] columnWidths = {1f, 3f, 1.5f, 1.5f, 1.5f, 2f};
        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        // Header bảng
        String[] headers = {"STT", "Ten san pham", "So luong", "Don gia", "Giam gia", "Thanh tien"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(9))
                    .setBackgroundColor(TABLE_HEADER_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(8);
            table.addHeaderCell(headerCell);
        }
        
        // Dữ liệu sản phẩm
        int stt = 1;
        for (ChiTietDonHang chiTiet : chiTietDonHangList) {
            LoHang loHang = chiTiet.getLoHang();
            SanPham sanPham = loHang != null ? loHang.getSanPham() : null;
            
            // STT
            table.addCell(createCell(String.valueOf(stt++), font, TextAlignment.CENTER));
            
            // Tên sản phẩm + Lô hàng
            String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
            String loHangInfo = loHang != null ? "\n(Lo: " + loHang.getMaLoHang() + ")" : "";
            table.addCell(createCell(tenSP + loHangInfo, font, TextAlignment.LEFT));
            
            // Số lượng
            table.addCell(createCell(String.valueOf(chiTiet.getSoLuong()), font, TextAlignment.CENTER));
            
            // Đơn giá
            table.addCell(createCell(CURRENCY_FORMAT.format(chiTiet.getDonGia()) + " đ", font, TextAlignment.RIGHT));
            
            // Giảm giá
            String giamGia = chiTiet.getGiamGia() > 0 ? 
                    "-" + CURRENCY_FORMAT.format(chiTiet.getGiamGia()) + " đ" : "0 đ";
            table.addCell(createCell(giamGia, font, TextAlignment.RIGHT));
            
            // Thành tiền
            table.addCell(createCell(CURRENCY_FORMAT.format(chiTiet.getThanhTien()) + " đ", font, TextAlignment.RIGHT));
        }
        
        document.add(table);
    }
    
    /**
     * Thêm thông tin thanh toán
     */
    private static void addPaymentInfo(Document document, PdfFont font, PdfFont boldFont, DonHang donHang, List<ChiTietDonHang> chiTietDonHangList, KhuyenMai khuyenMaiSanPham) {
        // Tính tổng tiền hàng và tổng giảm giá sản phẩm
        double tongTienHang = 0;
        double tongGiamGiaSanPham = 0;
        
        for (ChiTietDonHang chiTiet : chiTietDonHangList) {
            tongTienHang += chiTiet.getDonGia() * chiTiet.getSoLuong();
            tongGiamGiaSanPham += chiTiet.getGiamGia();
        }
        
        // Tính giảm giá đơn hàng
        double giamGiaHoaDon = 0;
        KhuyenMai khuyenMaiDonHang = donHang.getKhuyenMai();
        if (khuyenMaiDonHang != null && khuyenMaiDonHang.getGiamGia() > 0) {
            giamGiaHoaDon = (tongTienHang - tongGiamGiaSanPham) * khuyenMaiDonHang.getGiamGia() / 100;
        }
        
        // Tổng giảm giá
        double tongGiamGia = tongGiamGiaSanPham + giamGiaHoaDon;
        
        // Tạo bảng thanh toán (căn phải)
        Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .setWidth(UnitValue.createPercentValue(50))
                .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT)
                .setMarginBottom(15);
        
        // Tổng tiền hàng
        paymentTable.addCell(createPaymentCell("Tong tien hang:", font, false));
        paymentTable.addCell(createPaymentCell(CURRENCY_FORMAT.format(tongTienHang) + " đ", font, true));
        
        // ========== KHUYẾN MÃI SẢN PHẨM ==========
        if (tongGiamGiaSanPham > 0 && khuyenMaiSanPham != null) {
            String tenKMSanPham = khuyenMaiSanPham.getTenKhuyenMai();
            String labelText = "Giam gia san pham:";
            if (tenKMSanPham != null && !tenKMSanPham.trim().isEmpty()) {
                labelText += "\n(" + tenKMSanPham + ")";
            }
            
            paymentTable.addCell(createPaymentCell(labelText, font, false));
            String giamGiaText = "-" + CURRENCY_FORMAT.format(tongGiamGiaSanPham) + " đ";
            giamGiaText += " (" + String.format("%.1f", khuyenMaiSanPham.getGiamGia() * 100) + "%)";
            paymentTable.addCell(createPaymentCell(giamGiaText, font, true));
        }
        
        // ========== KHUYẾN MÃI ĐƠN HÀNG ==========
        if (giamGiaHoaDon > 0 && khuyenMaiDonHang != null) {
            String tenKMDonHang = khuyenMaiDonHang.getTenKhuyenMai();
            String labelText = "Giam gia hoa don:";
            if (tenKMDonHang != null && !tenKMDonHang.trim().isEmpty()) {
                labelText += "\n(" + tenKMDonHang + ")";
            }
            
            paymentTable.addCell(createPaymentCell(labelText, font, false));
            String giamGiaHDText = "-" + CURRENCY_FORMAT.format(giamGiaHoaDon) + " đ";
            giamGiaHDText += " (" + String.format("%.1f", khuyenMaiDonHang.getGiamGia() * 100) + "%)";
            paymentTable.addCell(createPaymentCell(giamGiaHDText, font, true));
        }
        
        // ========== TỔNG GIẢM GIÁ ==========
        if (tongGiamGia > 0) {
            paymentTable.addCell(createPaymentCell("TONG GIAM GIA:", boldFont, false));
            paymentTable.addCell(createPaymentCell("-" + CURRENCY_FORMAT.format(tongGiamGia) + " đ", boldFont, true));
        }
        
        // Đường kẻ phân cách
        Cell separator1 = new Cell(1, 2)
                .add(new Paragraph(" ").setFontSize(1))
                .setBorder(Border.NO_BORDER)
                .setBorderTop(new com.itextpdf.layout.borders.SolidBorder(new DeviceRgb(200, 200, 200), 1))
                .setPadding(3);
        paymentTable.addCell(separator1);
        
        // Thành tiền (in đậm, lớn hơn)
        Cell thanhTienLabel = new Cell()
                .add(new Paragraph("THANH TIEN:").setFont(boldFont).setFontSize(13))
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setTextAlignment(TextAlignment.LEFT);
        paymentTable.addCell(thanhTienLabel);
        
        Cell thanhTienValue = new Cell()
                .add(new Paragraph(CURRENCY_FORMAT.format(donHang.getThanhTien()) + " đ")
                        .setFont(boldFont)
                        .setFontSize(13)
                        .setFontColor(HEADER_COLOR))
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setTextAlignment(TextAlignment.RIGHT);
        paymentTable.addCell(thanhTienValue);
        
        // Đường kẻ phân cách
        Cell separator2 = new Cell(1, 2)
                .add(new Paragraph(" ").setFontSize(1))
                .setBorder(Border.NO_BORDER)
                .setBorderTop(new com.itextpdf.layout.borders.SolidBorder(new DeviceRgb(200, 200, 200), 1))
                .setPadding(3);
        paymentTable.addCell(separator2);
        
        // Phương thức thanh toán
        paymentTable.addCell(createPaymentCell("Phuong thuc:", font, false));
        paymentTable.addCell(createPaymentCell(donHang.getPhuongThucThanhToan().toString(), font, true));
        
        document.add(paymentTable);
    }
    
    /**
     * Thêm footer
     */
    private static void addFooter(Document document, PdfFont font) {
        Paragraph footer = new Paragraph("\nCam on quy khach da mua hang tai Pharmacity!")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setFontColor(HEADER_COLOR);
        document.add(footer);
        
        Paragraph note = new Paragraph("Vui long giu hoa don de doi/tra hang trong vong 7 ngay")
                .setFont(font)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic();
        document.add(note);
    }
    
    /**
     * Tạo cell cho bảng
     */
    private static Cell createCell(String content, PdfFont font, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(content).setFont(font).setFontSize(9))
                .setTextAlignment(alignment)
                .setPadding(5);
    }
    
    /**
     * Tạo cell cho bảng thanh toán
     */
    private static Cell createPaymentCell(String content, PdfFont font, boolean isValue) {
        TextAlignment alignment = isValue ? TextAlignment.RIGHT : TextAlignment.LEFT;
        return new Cell()
                .add(new Paragraph(content).setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setTextAlignment(alignment);
    }
    
    /**
     * Tạo tên file PDF từ mã đơn hàng
     */
    public static String generateFileName(String maDonHang) {
        return "HoaDon_" + maDonHang + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf";
    }
    
    /**
     * Xuất hóa đơn tự động vào folder resources/img/hoadonbanhang
     * @param donHang đơn hàng cần xuất
     * @param chiTietDonHangList danh sách chi tiết đơn hàng
     * @param khuyenMaiSanPham khuyến mãi sản phẩm (có thể null)
     * @return đường dẫn file PDF đã lưu, hoặc null nếu thất bại
     */
    public static String xuatHoaDonTuDong(DonHang donHang, List<ChiTietDonHang> chiTietDonHangList, KhuyenMai khuyenMaiSanPham) {
        try {
            // Lấy đường dẫn tới thư mục resources
            String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + "/src/main/resources/img/hoadonbanhang";
            
            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            // Tạo tên file
            String fileName = generateFileName(donHang.getMaDonHang());
            String fullPath = folderPath + "/" + fileName;
            
            // Xuất hóa đơn
            boolean success = xuatHoaDon(donHang, chiTietDonHangList, fullPath, khuyenMaiSanPham);
            
            return success ? fullPath : null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


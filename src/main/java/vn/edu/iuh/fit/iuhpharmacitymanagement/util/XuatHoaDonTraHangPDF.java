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
 * Class tiện ích để xuất hóa đơn trả hàng ra file PDF
 * @author PhamTra
 */
public class XuatHoaDonTraHangPDF {
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Màu sắc
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(220, 53, 69); // Màu đỏ cho hóa đơn trả
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(240, 240, 240);
    
    /**
     * Xuất hóa đơn trả hàng ra file PDF
     * @param donTraHang đơn trả hàng cần xuất
     * @param chiTietDonTraHangList danh sách chi tiết đơn trả hàng
     * @param outputPath đường dẫn lưu file PDF
     * @return true nếu xuất thành công, false nếu thất bại
     */
    public static boolean xuatHoaDonTraHang(DonTraHang donTraHang, List<ChiTietDonTraHang> chiTietDonTraHangList, String outputPath) {
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
            
            // Tiêu đề hóa đơn trả
            addInvoiceTitle(document, boldFont, donTraHang);
            
            // Thông tin đơn hàng gốc và nhân viên
            addOrderAndEmployeeInfo(document, font, boldFont, donTraHang);
            
            // Bảng chi tiết sản phẩm trả
            addReturnProductTable(document, font, boldFont, chiTietDonTraHangList);
            
            // Thông tin thanh toán
            addPaymentInfo(document, font, boldFont, donTraHang);
            
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
        Paragraph storeName = new Paragraph("PHARMACITY")
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
     * Thêm tiêu đề hóa đơn trả
     */
    private static void addInvoiceTitle(Document document, PdfFont boldFont, DonTraHang donTraHang) {
        Paragraph title = new Paragraph("PHIEU TRA HANG")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR)
                .setMarginBottom(5);
        document.add(title);
        
        // Mã phiếu trả
        Paragraph returnCode = new Paragraph("Ma phieu tra: " + donTraHang.getMaDonTraHang())
                .setFont(boldFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2);
        document.add(returnCode);
        
        // Ngày trả
        Paragraph date = new Paragraph("Ngay tra: " + donTraHang.getNgayTraHang().format(DATE_FORMAT))
                .setFont(boldFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(date);
    }
    
    /**
     * Thêm thông tin đơn hàng gốc và nhân viên
     */
    private static void addOrderAndEmployeeInfo(Document document, PdfFont font, PdfFont boldFont, DonTraHang donTraHang) {
        // Tạo bảng 2 cột
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        // Thông tin đơn hàng gốc (cột trái)
        DonHang donHang = donTraHang.getDonHang();
        StringBuilder orderInfo = new StringBuilder();
        orderInfo.append("THONG TIN DON HANG GOC\n");
        if (donHang != null) {
            orderInfo.append("Ma don hang: ").append(donHang.getMaDonHang()).append("\n");
            // Kiểm tra và format ngày đặt hàng
            if (donHang.getNgayDatHang() != null) {
                orderInfo.append("Ngay dat: ").append(donHang.getNgayDatHang().format(DATE_FORMAT)).append("\n");
            } else {
                orderInfo.append("Ngay dat: N/A\n");
            }
            if (donHang.getKhachHang() != null) {
                orderInfo.append("Khach hang: ").append(donHang.getKhachHang().getTenKhachHang()).append("\n");
                String sdtKH = donHang.getKhachHang().getSoDienThoai();
                orderInfo.append("SDT: ").append(sdtKH != null && !sdtKH.trim().isEmpty() ? sdtKH : "N/A");
            } else {
                orderInfo.append("Khach hang: Khach vang lai\nSDT: N/A");
            }
        } else {
            orderInfo.append("Khong co thong tin");
        }
        
        Cell orderCell = new Cell()
                .add(new Paragraph(orderInfo.toString()).setFont(font).setFontSize(9))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        infoTable.addCell(orderCell);
        
        // Thông tin nhân viên xử lý (cột phải)
        NhanVien nhanVien = donTraHang.getNhanVien();
        StringBuilder employeeInfo = new StringBuilder();
        employeeInfo.append("NHAN VIEN XU LY\n");
        if (nhanVien != null) {
            employeeInfo.append("Ho ten: ").append(nhanVien.getTenNhanVien()).append("\n");
            employeeInfo.append("Ma NV: ").append(nhanVien.getMaNhanVien()).append("\n");
            String sdtNV = nhanVien.getSoDienThoai();
            employeeInfo.append("SDT: ").append(sdtNV != null && !sdtNV.trim().isEmpty() ? sdtNV : "null");
        } else {
            employeeInfo.append("Ho ten: N/A\nMa NV: N/A\nSDT: N/A");
        }
        
        Cell employeeCell = new Cell()
                .add(new Paragraph(employeeInfo.toString()).setFont(font).setFontSize(9))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        infoTable.addCell(employeeCell);
        
        document.add(infoTable);
    }
    
    /**
     * Thêm bảng chi tiết sản phẩm trả
     */
    private static void addReturnProductTable(Document document, PdfFont font, PdfFont boldFont, List<ChiTietDonTraHang> chiTietDonTraHangList) {
        // Tạo bảng với 6 cột
        float[] columnWidths = {1f, 3f, 1.5f, 1.5f, 2f, 3f};
        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        // Header bảng
        String[] headers = {"STT", "Ten san pham", "So luong", "Don gia", "Thanh tien", "Ly do tra"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(9))
                    .setBackgroundColor(TABLE_HEADER_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(8);
            table.addHeaderCell(headerCell);
        }
        
        // Dữ liệu sản phẩm trả
        int stt = 1;
        for (ChiTietDonTraHang chiTiet : chiTietDonTraHangList) {
            SanPham sanPham = chiTiet.getSanPham();
            
            // STT
            table.addCell(createCell(String.valueOf(stt++), font, TextAlignment.CENTER));
            
            // Tên sản phẩm
            String tenSP = sanPham != null ? sanPham.getTenSanPham() : "";
            table.addCell(createCell(tenSP, font, TextAlignment.LEFT));
            
            // Số lượng
            table.addCell(createCell(String.valueOf(chiTiet.getSoLuong()), font, TextAlignment.CENTER));
            
            // Đơn giá
            table.addCell(createCell(CURRENCY_FORMAT.format(chiTiet.getDonGia()) + " d", font, TextAlignment.RIGHT));
            
            // Thành tiền
            table.addCell(createCell(CURRENCY_FORMAT.format(chiTiet.getThanhTien()) + " d", font, TextAlignment.RIGHT));
            
            // Lý do trả
            String lyDo = chiTiet.getLyDoTra() != null && !chiTiet.getLyDoTra().isEmpty() ? 
                    chiTiet.getLyDoTra() : "Khong co ly do";
            table.addCell(createCell(lyDo, font, TextAlignment.LEFT));
        }
        
        document.add(table);
    }
    
    /**
     * Thêm thông tin thanh toán
     */
    private static void addPaymentInfo(Document document, PdfFont font, PdfFont boldFont, DonTraHang donTraHang) {
        // Tạo bảng thanh toán (căn phải)
        Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .setWidth(UnitValue.createPercentValue(50))
                .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT)
                .setMarginBottom(15);
        
        // Tổng tiền trả (in đậm, lớn hơn)
        Cell tongTienLabel = new Cell()
                .add(new Paragraph("TONG TIEN TRA:").setFont(boldFont).setFontSize(11))
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setTextAlignment(TextAlignment.LEFT);
        paymentTable.addCell(tongTienLabel);
        
        Cell tongTienValue = new Cell()
                .add(new Paragraph(CURRENCY_FORMAT.format(donTraHang.getThanhTien()) + " d")
                        .setFont(boldFont)
                        .setFontSize(11)
                        .setFontColor(HEADER_COLOR))
                .setBorder(Border.NO_BORDER)
                .setPadding(5)
                .setTextAlignment(TextAlignment.RIGHT);
        paymentTable.addCell(tongTienValue);
        
        document.add(paymentTable);
    }
    
    /**
     * Thêm footer
     */
    private static void addFooter(Document document, PdfFont font) {
        Paragraph footer = new Paragraph("\nCam on quy khach!")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setFontColor(HEADER_COLOR);
        document.add(footer);
        
        Paragraph note = new Paragraph("So tien tra lai se duoc chuyen khoan hoac hoan tra truc tiep trong vong 3-5 ngay lam viec")
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
     * Tạo tên file PDF từ mã đơn trả hàng
     */
    public static String generateFileName(String maDonTraHang) {
        return "PhieuTraHang_" + maDonTraHang + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf";
    }
    
    /**
     * Xuất hóa đơn trả tự động vào folder resources/img/phieutrahang
     * @param donTraHang đơn trả hàng cần xuất
     * @param chiTietDonTraHangList danh sách chi tiết đơn trả hàng
     * @return đường dẫn file PDF đã lưu, hoặc null nếu thất bại
     */
    public static String xuatHoaDonTraHangTuDong(DonTraHang donTraHang, List<ChiTietDonTraHang> chiTietDonTraHangList) {
        try {
            // Lấy đường dẫn tới thư mục resources
            String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + "/src/main/resources/img/phieutrahang";
            
            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            // Tạo tên file
            String fileName = generateFileName(donTraHang.getMaDonTraHang());
            String fullPath = folderPath + "/" + fileName;
            
            // Xuất hóa đơn
            boolean success = xuatHoaDonTraHang(donTraHang, chiTietDonTraHangList, fullPath);
            
            return success ? fullPath : null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


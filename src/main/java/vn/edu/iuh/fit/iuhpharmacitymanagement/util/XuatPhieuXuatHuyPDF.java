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
 * Class tiện ích để xuất phiếu xuất hủy ra file PDF
 * @author PhamTra
 */
public class XuatPhieuXuatHuyPDF {
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(220, 53, 69); // Màu đỏ cho xuất hủy
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(248, 249, 250);
    
    /**
     * Tạo tên file PDF theo mã hàng hỏng
     */
    private static String generateFileName(String maHangHong) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "PhieuXuatHuy_" + maHangHong + "_" + dateStr + ".pdf";
    }

    /**
     * Xuất phiếu xuất hủy ra file PDF
     * @param hangHong thông tin hàng hỏng
     * @param chiTietHangHongList danh sách chi tiết hàng hỏng
     * @param outputPath đường dẫn file output
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean xuatPhieuXuatHuy(HangHong hangHong, List<ChiTietHangHong> chiTietHangHongList, String outputPath) {
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
            
            // Tiêu đề phiếu xuất hủy
            addTitle(document, boldFont, hangHong);
            
            // Thông tin nhân viên
            addEmployeeInfo(document, font, boldFont, hangHong);
            
            // Bảng chi tiết sản phẩm xuất hủy
            addProductTable(document, font, boldFont, chiTietHangHongList);
            
            // Thông tin tổng cộng
            addTotalInfo(document, font, boldFont, hangHong);
            
            // Footer - Chữ ký
            addFooter(document, font, boldFont);
            
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
     * Thêm tiêu đề phiếu xuất hủy
     */
    private static void addTitle(Document document, PdfFont boldFont, HangHong hangHong) {
        Paragraph title = new Paragraph("PHIEU XUAT HUY")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR)
                .setMarginBottom(5);
        document.add(title);
        
        // Mã phiếu xuất hủy
        Paragraph code = new Paragraph("Ma phieu: " + hangHong.getMaHangHong())
                .setFont(boldFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2);
        document.add(code);
        
        // Ngày lập
        Paragraph date = new Paragraph("Ngay lap: " + hangHong.getNgayNhap().format(DATE_FORMAT))
                .setFont(boldFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(date);
    }
    
    /**
     * Thêm thông tin nhân viên
     */
    private static void addEmployeeInfo(Document document, PdfFont font, PdfFont boldFont, HangHong hangHong) {
        // Tạo bảng 1 cột
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        // Thông tin nhân viên
        NhanVien nhanVien = hangHong.getNhanVien();
        StringBuilder employeeInfo = new StringBuilder();
        employeeInfo.append("THONG TIN NHAN VIEN LAP PHIEU\n");
        employeeInfo.append("Ho ten: ").append(nhanVien != null ? nhanVien.getTenNhanVien() : "Khong xac dinh").append("\n");
        if (nhanVien != null) {
            employeeInfo.append("Ma NV: ").append(nhanVien.getMaNhanVien()).append("\n");
            employeeInfo.append("SDT: ").append(nhanVien.getSoDienThoai() != null ? nhanVien.getSoDienThoai() : "N/A");
        }
        
        Cell employeeCell = new Cell()
                .add(new Paragraph(employeeInfo.toString()).setFont(font).setFontSize(9))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        infoTable.addCell(employeeCell);
        
        document.add(infoTable);
    }
    
    /**
     * Thêm bảng chi tiết sản phẩm xuất hủy
     */
    private static void addProductTable(Document document, PdfFont font, PdfFont boldFont, List<ChiTietHangHong> chiTietHangHongList) {
        // Tiêu đề bảng
        Paragraph tableTitle = new Paragraph("CHI TIET SAN PHAM XUAT HUY")
                .setFont(boldFont)
                .setFontSize(12)
                .setMarginBottom(10);
        document.add(tableTitle);
        
        // Tạo bảng: STT | Tên SP | Lô hàng | HSD | Đơn vị | SL | Đơn giá | Thành tiền | Lý do
        float[] columnWidths = {1f, 3f, 2f, 2f, 1.5f, 1.5f, 2f, 2f, 2.5f};
        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        // Header row
        addTableHeaderCell(table, boldFont, "STT");
        addTableHeaderCell(table, boldFont, "Ten san pham");
        addTableHeaderCell(table, boldFont, "Lo hang");
        addTableHeaderCell(table, boldFont, "HSD");
        addTableHeaderCell(table, boldFont, "Don vi");
        addTableHeaderCell(table, boldFont, "SL");
        addTableHeaderCell(table, boldFont, "Don gia");
        addTableHeaderCell(table, boldFont, "Thanh tien");
        addTableHeaderCell(table, boldFont, "Ly do");
        
        // Data rows
        int stt = 1;
        for (ChiTietHangHong chiTiet : chiTietHangHongList) {
            LoHang loHang = chiTiet.getLoHang();
            
            // Null check cho loHang
            if (loHang == null) {
                System.err.println("Warning: LoHang null trong ChiTietHangHong, skip row");
                continue;
            }
            
            SanPham sanPham = loHang.getSanPham();
            
            // Null check cho sanPham
            if (sanPham == null) {
                System.err.println("Warning: SanPham null trong LoHang " + loHang.getMaLoHang() + ", skip row");
                continue;
            }
            
            // STT
            table.addCell(new Cell()
                    .add(new Paragraph(String.valueOf(stt++)).setFont(font).setFontSize(9))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
            
            // Tên sản phẩm
            table.addCell(new Cell()
                    .add(new Paragraph(sanPham.getTenSanPham()).setFont(font).setFontSize(9))
                    .setPadding(5));
            
            // Lô hàng
            String tenLoHang = loHang.getTenLoHang() != null ? loHang.getTenLoHang() : loHang.getMaLoHang();
            table.addCell(new Cell()
                    .add(new Paragraph(tenLoHang).setFont(font).setFontSize(9))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
            
            // HSD
            String hsd = loHang.getHanSuDung() != null ? loHang.getHanSuDung().toString() : "N/A";
            table.addCell(new Cell()
                    .add(new Paragraph(hsd).setFont(font).setFontSize(9))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
            
            // Đơn vị
            String donVi = (sanPham.getDonViTinh() != null && sanPham.getDonViTinh().getTenDonVi() != null) 
                    ? sanPham.getDonViTinh().getTenDonVi() : "";
            table.addCell(new Cell()
                    .add(new Paragraph(donVi).setFont(font).setFontSize(9))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
            
            // Số lượng
            table.addCell(new Cell()
                    .add(new Paragraph(String.valueOf(chiTiet.getSoLuong())).setFont(font).setFontSize(9))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5));
            
            // Đơn giá
            table.addCell(new Cell()
                    .add(new Paragraph(CURRENCY_FORMAT.format(chiTiet.getDonGia())).setFont(font).setFontSize(9))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setPadding(5));
            
            // Thành tiền
            table.addCell(new Cell()
                    .add(new Paragraph(CURRENCY_FORMAT.format(chiTiet.getThanhTien())).setFont(boldFont).setFontSize(9))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setPadding(5));
            
            // Lý do xuất hủy - Lấy từ ChiTietHangHong
            String lyDo = chiTiet.getLyDoXuatHuy();
            if (lyDo == null || lyDo.trim().isEmpty()) {
                // Fallback nếu không có lý do
                lyDo = getLyDoXuatHuy(loHang);
            }
            table.addCell(new Cell()
                    .add(new Paragraph(lyDo).setFont(font).setFontSize(8))
                    .setPadding(5));
        }
        
        document.add(table);
    }
    
    /**
     * Xác định lý do xuất hủy dựa trên thông tin lô hàng
     */
    private static String getLyDoXuatHuy(LoHang loHang) {
        if (loHang.getHanSuDung() != null) {
            LocalDate hsd = loHang.getHanSuDung();
            LocalDate now = LocalDate.now();
            long monthsUntilExpiry = java.time.temporal.ChronoUnit.MONTHS.between(now, hsd);
            
            if (monthsUntilExpiry <= 0) {
                return "Het han su dung";
            } else if (monthsUntilExpiry <= 6) {
                return "Gan het han (" + monthsUntilExpiry + " thang)";
            }
        }
        return "Hang tra lai";
    }
    
    /**
     * Thêm cell header cho bảng
     */
    private static void addTableHeaderCell(Table table, PdfFont boldFont, String text) {
        table.addCell(new Cell()
                .add(new Paragraph(text).setFont(boldFont).setFontSize(9))
                .setBackgroundColor(TABLE_HEADER_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8));
    }
    
    /**
     * Thêm thông tin tổng cộng
     */
    private static void addTotalInfo(Document document, PdfFont font, PdfFont boldFont, HangHong hangHong) {
        // Tạo bảng 2 cột cho tổng tiền
        Table totalTable = new Table(UnitValue.createPercentArray(new float[]{3f, 1f}))
                .useAllAvailableWidth()
                .setMarginBottom(20);
        
        // Tổng tiền
        totalTable.addCell(new Cell()
                .add(new Paragraph("TONG GIA TRI XUAT HUY:").setFont(boldFont).setFontSize(12))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5));
        
        totalTable.addCell(new Cell()
                .add(new Paragraph(CURRENCY_FORMAT.format(hangHong.getThanhTien()) + " VND")
                        .setFont(boldFont)
                        .setFontSize(12)
                        .setFontColor(HEADER_COLOR))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5));
        
        document.add(totalTable);
        
        // Ghi chú
        Paragraph note = new Paragraph("Ghi chu: Cac san pham tren da het han su dung hoac bi hong, can xuat huy theo quy dinh.")
                .setFont(font)
                .setFontSize(9)
                .setItalic()
                .setMarginBottom(30);
        document.add(note);
    }
    
    /**
     * Thêm footer - Chữ ký
     */
    private static void addFooter(Document document, PdfFont font, PdfFont boldFont) {
        // Tạo bảng 2 cột cho chữ ký
        Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{1f, 1f}))
                .useAllAvailableWidth();
        
        // Người lập phiếu
        Cell creatorCell = new Cell()
                .add(new Paragraph("Nguoi lap phieu")
                        .setFont(boldFont)
                        .setFontSize(11)
                        .setTextAlignment(TextAlignment.CENTER))
                .add(new Paragraph("\n\n\n(Ky va ghi ro ho ten)")
                        .setFont(font)
                        .setFontSize(9)
                        .setItalic()
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        signatureTable.addCell(creatorCell);
        
        // Người duyệt
        Cell approverCell = new Cell()
                .add(new Paragraph("Nguoi duyet")
                        .setFont(boldFont)
                        .setFontSize(11)
                        .setTextAlignment(TextAlignment.CENTER))
                .add(new Paragraph("\n\n\n(Ky va ghi ro ho ten)")
                        .setFont(font)
                        .setFontSize(9)
                        .setItalic()
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER)
                .setPadding(10);
        signatureTable.addCell(approverCell);
        
        document.add(signatureTable);
        
        // Thông tin bổ sung ở cuối trang
        Paragraph footerNote = new Paragraph("\nPhieu xuat huy nay la bang chung hop le de ghi nhan viec xuat huy san pham.")
                .setFont(font)
                .setFontSize(8)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        document.add(footerNote);
    }
    
    /**
     * Xuất phiếu xuất hủy tự động vào folder resources/img/phieuxuathuy
     * @param hangHong hàng hỏng cần xuất
     * @param chiTietHangHongList danh sách chi tiết hàng hỏng
     * @return đường dẫn file PDF đã lưu, hoặc null nếu thất bại
     */
    public static String xuatPhieuXuatHuyTuDong(HangHong hangHong, List<ChiTietHangHong> chiTietHangHongList) {
        try {
            // Lấy đường dẫn tới thư mục resources
            String projectPath = System.getProperty("user.dir");
            String folderPath = projectPath + "/src/main/resources/img/phieuxuathuy";
            
            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            // Tạo tên file
            String fileName = generateFileName(hangHong.getMaHangHong());
            String fullPath = folderPath + "/" + fileName;
            
            // Xuất phiếu xuất hủy
            boolean success = xuatPhieuXuatHuy(hangHong, chiTietHangHongList, fullPath);
            
            return success ? fullPath : null;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



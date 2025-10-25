package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class để xuất dữ liệu từ JTable ra file Excel
 * @author IUH-Pharmacity-Management
 */
public class XuatFileExcel {

    /**
     * Xuất dữ liệu từ JTable ra file Excel
     * @param table JTable chứa dữ liệu cần xuất
     * @param filePath Đường dẫn file Excel đích
     * @return true nếu xuất thành công, false nếu thất bại
     */
    public static boolean xuatExcel(JTable table, String filePath) {
        try {
            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            // Lấy model của table
            TableModel model = table.getModel();

            // Tạo style cho header
            CellStyle headerStyle = taoStyleHeader(workbook);
            
            // Tạo style cho data
            CellStyle dataStyle = taoStyleData(workbook);

            // Tạo header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(headerStyle);
            }

            // Tạo data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = dataRow.createCell(col);
                    Object value = model.getValueAt(row, col);
                    
                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                    
                    cell.setCellStyle(dataStyle);
                }
            }

            // Tự động điều chỉnh độ rộng cột
            for (int col = 0; col < model.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
                // Thêm padding
                sheet.setColumnWidth(col, sheet.getColumnWidth(col) + 1000);
            }

            // Ghi file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
            return true;

        } catch (IOException e) {
            System.out.println("Lỗi khi xuất file Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xuất dữ liệu từ JTable ra file Excel với tiêu đề tùy chỉnh
     * @param table JTable chứa dữ liệu cần xuất
     * @param filePath Đường dẫn file Excel đích
     * @param sheetName Tên sheet Excel
     * @param title Tiêu đề của bảng dữ liệu
     * @return true nếu xuất thành công, false nếu thất bại
     */
    public static boolean xuatExcelVoiTieuDe(JTable table, String filePath, String sheetName, String title) {
        try {
            // Tạo workbook mới
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            // Lấy model của table
            TableModel model = table.getModel();

            // Tạo style cho title
            CellStyle titleStyle = taoStyleTieuDe(workbook);
            
            // Tạo style cho header
            CellStyle headerStyle = taoStyleHeader(workbook);
            
            // Tạo style cho data
            CellStyle dataStyle = taoStyleData(workbook);

            int currentRow = 0;

            // Tạo title row
            if (title != null && !title.trim().isEmpty()) {
                Row titleRow = sheet.createRow(currentRow++);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue(title);
                titleCell.setCellStyle(titleStyle);
                
                // Merge cells cho title
                sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(
                    0, 0, 0, model.getColumnCount() - 1
                ));
                
                currentRow++; // Thêm dòng trống
            }

            // Tạo header row
            Row headerRow = sheet.createRow(currentRow++);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(headerStyle);
            }

            // Tạo data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(currentRow++);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = dataRow.createCell(col);
                    Object value = model.getValueAt(row, col);
                    
                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                    
                    cell.setCellStyle(dataStyle);
                }
            }

            // Tự động điều chỉnh độ rộng cột
            for (int col = 0; col < model.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
                // Thêm padding
                sheet.setColumnWidth(col, sheet.getColumnWidth(col) + 1000);
            }

            // Ghi file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
            return true;

        } catch (IOException e) {
            System.out.println("Lỗi khi xuất file Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tạo style cho tiêu đề
     */
    private static CellStyle taoStyleTieuDe(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Font
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        
        // Alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }

    /**
     * Tạo style cho header
     */
    private static CellStyle taoStyleHeader(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Background color
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Border
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // Font
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        
        // Alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }

    /**
     * Tạo style cho data
     */
    private static CellStyle taoStyleData(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // Border
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // Alignment
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }

    /**
     * Mở dialog chọn vị trí lưu file và xuất Excel
     * @param table JTable chứa dữ liệu cần xuất
     * @param defaultFileName Tên file mặc định
     * @return true nếu xuất thành công, false nếu thất bại hoặc hủy
     */
    public static boolean xuatExcelVoiDialog(JTable table, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        
        // Tạo tên file mặc định với timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = defaultFileName + "_" + timestamp + ".xlsx";
        fileChooser.setSelectedFile(new java.io.File(fileName));
        
        // Filter chỉ hiển thị file .xlsx
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "Excel Files (*.xlsx)";
            }
        });
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Đảm bảo file có đuôi .xlsx
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            
            return xuatExcel(table, filePath);
        }
        
        return false;
    }

    /**
     * Mở dialog chọn vị trí lưu file và xuất Excel với tiêu đề
     * @param table JTable chứa dữ liệu cần xuất
     * @param defaultFileName Tên file mặc định
     * @param sheetName Tên sheet Excel
     * @param title Tiêu đề của bảng dữ liệu
     * @return true nếu xuất thành công, false nếu thất bại hoặc hủy
     */
    public static boolean xuatExcelVoiDialogVaTieuDe(JTable table, String defaultFileName, String sheetName, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        
        // Tạo tên file mặc định với timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = defaultFileName + "_" + timestamp + ".xlsx";
        fileChooser.setSelectedFile(new java.io.File(fileName));
        
        // Filter chỉ hiển thị file .xlsx
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "Excel Files (*.xlsx)";
            }
        });
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Đảm bảo file có đuôi .xlsx
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            
            return xuatExcelVoiTieuDe(table, filePath, sheetName, title);
        }
        
        return false;
    }
}


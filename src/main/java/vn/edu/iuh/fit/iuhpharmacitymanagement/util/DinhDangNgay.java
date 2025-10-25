package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class để định dạng ngày tháng
 * @author IUH-Pharmacity-Management
 */
public class DinhDangNgay {
    
    // Các định dạng ngày tháng thông dụng
    private static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DD_MM_YYYY_HH_MM = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DD_MM_YYYY_HH_MM_SS = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DD_THANG_MM_NAM_YYYY = DateTimeFormatter.ofPattern("dd 'tháng' MM 'năm' yyyy", Locale.of("vi", "VN"));
    private static final DateTimeFormatter EEE_DD_MM_YYYY = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale.of("vi", "VN"));
    private static final DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("ddMMyyyy");

    /**
     * Định dạng ngày theo dạng dd/MM/yyyy
     * Ví dụ: 2024-10-25 -> 25/10/2024
     * @param date Ngày cần định dạng
     * @return Chuỗi ngày đã được định dạng
     */
    public static String dinhDangNgay(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DD_MM_YYYY);
    }

    /**
     * Định dạng ngày giờ theo dạng dd/MM/yyyy HH:mm
     * Ví dụ: 2024-10-25T14:30 -> 25/10/2024 14:30
     * @param dateTime Ngày giờ cần định dạng
     * @return Chuỗi ngày giờ đã được định dạng
     */
    public static String dinhDangNgayGio(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DD_MM_YYYY_HH_MM);
    }

    /**
     * Định dạng ngày giờ theo dạng dd/MM/yyyy HH:mm:ss
     * Ví dụ: 2024-10-25T14:30:45 -> 25/10/2024 14:30:45
     * @param dateTime Ngày giờ cần định dạng
     * @return Chuỗi ngày giờ đã được định dạng
     */
    public static String dinhDangNgayGioGiay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DD_MM_YYYY_HH_MM_SS);
    }

    /**
     * Định dạng ngày theo dạng yyyy-MM-dd (chuẩn ISO)
     * Ví dụ: 25/10/2024 -> 2024-10-25
     * @param date Ngày cần định dạng
     * @return Chuỗi ngày đã được định dạng
     */
    public static String dinhDangNgayChuanISO(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(YYYY_MM_DD);
    }

    /**
     * Định dạng ngày theo dạng "dd tháng MM năm yyyy"
     * Ví dụ: 2024-10-25 -> 25 tháng 10 năm 2024
     * @param date Ngày cần định dạng
     * @return Chuỗi ngày đã được định dạng
     */
    public static String dinhDangNgayDayDu(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DD_THANG_MM_NAM_YYYY);
    }

    /**
     * Định dạng ngày theo dạng "Thứ, dd/MM/yyyy"
     * Ví dụ: 2024-10-25 -> T6, 25/10/2024
     * @param date Ngày cần định dạng
     * @return Chuỗi ngày đã được định dạng
     */
    public static String dinhDangNgayCoThu(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(EEE_DD_MM_YYYY);
    }

    /**
     * Định dạng ngày theo dạng ddMMyyyy (không có dấu phân cách)
     * Ví dụ: 2024-10-25 -> 25102024
     * @param date Ngày cần định dạng
     * @return Chuỗi ngày đã được định dạng
     */
    public static String dinhDangNgayKhongDauPhach(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DDMMYYYY);
    }

    /**
     * Chuyển chuỗi ngày dd/MM/yyyy về LocalDate
     * Ví dụ: "25/10/2024" -> 2024-10-25
     * @param dateString Chuỗi ngày cần chuyển đổi
     * @return LocalDate hoặc null nếu lỗi
     */
    public static LocalDate chuyenChuoiSangNgay(String dateString) {
        try {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(dateString, DD_MM_YYYY);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi khi chuyển đổi chuỗi sang ngày: " + e.getMessage());
            return null;
        }
    }

    /**
     * Chuyển chuỗi ngày yyyy-MM-dd về LocalDate
     * Ví dụ: "2024-10-25" -> 2024-10-25
     * @param dateString Chuỗi ngày cần chuyển đổi
     * @return LocalDate hoặc null nếu lỗi
     */
    public static LocalDate chuyenChuoiSangNgayISO(String dateString) {
        try {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(dateString, YYYY_MM_DD);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi khi chuyển đổi chuỗi sang ngày: " + e.getMessage());
            return null;
        }
    }

    /**
     * Chuyển chuỗi ngày giờ về LocalDateTime
     * Ví dụ: "25/10/2024 14:30" -> 2024-10-25T14:30
     * @param dateTimeString Chuỗi ngày giờ cần chuyển đổi
     * @return LocalDateTime hoặc null nếu lỗi
     */
    public static LocalDateTime chuyenChuoiSangNgayGio(String dateTimeString) {
        try {
            if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
                return null;
            }
            return LocalDateTime.parse(dateTimeString, DD_MM_YYYY_HH_MM);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi khi chuyển đổi chuỗi sang ngày giờ: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy ngày hiện tại theo định dạng dd/MM/yyyy
     * @return Chuỗi ngày hiện tại
     */
    public static String layNgayHienTai() {
        return LocalDate.now().format(DD_MM_YYYY);
    }

    /**
     * Lấy ngày giờ hiện tại theo định dạng dd/MM/yyyy HH:mm:ss
     * @return Chuỗi ngày giờ hiện tại
     */
    public static String layNgayGioHienTai() {
        return LocalDateTime.now().format(DD_MM_YYYY_HH_MM_SS);
    }

    /**
     * So sánh hai ngày
     * @param date1 Ngày thứ nhất
     * @param date2 Ngày thứ hai
     * @return true nếu date1 sau date2
     */
    public static boolean laNgaySau(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isAfter(date2);
    }

    /**
     * So sánh hai ngày
     * @param date1 Ngày thứ nhất
     * @param date2 Ngày thứ hai
     * @return true nếu date1 trước date2
     */
    public static boolean laNgayTruoc(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.isBefore(date2);
    }

    /**
     * Tính số ngày giữa hai ngày
     * @param fromDate Ngày bắt đầu
     * @param toDate Ngày kết thúc
     * @return Số ngày giữa hai ngày
     */
    public static long tinhSoNgay(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate);
    }
}


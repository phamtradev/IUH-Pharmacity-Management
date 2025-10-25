package vn.edu.iuh.fit.iuhpharmacitymanagement.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class để định dạng số tiền
 * @author IUH-Pharmacity-Management
 */
public class DinhDangSo {
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");
    private static final NumberFormat VN_CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));

    /**
     * Định dạng số tiền theo dạng #,### VNĐ
     * Ví dụ: 1000000 -> 1,000,000 VNĐ
     * @param amount Số tiền cần định dạng
     * @return Chuỗi số tiền đã được định dạng
     */
    public static String dinhDangTien(double amount) {
        return CURRENCY_FORMAT.format(amount) + " VNĐ";
    }

    /**
     * Định dạng số tiền theo dạng #,###
     * Ví dụ: 1000000 -> 1,000,000
     * @param amount Số tiền cần định dạng
     * @return Chuỗi số tiền đã được định dạng
     */
    public static String dinhDangTienKhongDonVi(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    /**
     * Định dạng số tiền theo chuẩn Việt Nam (sử dụng NumberFormat)
     * Ví dụ: 1000000 -> 1.000.000 ₫
     * @param amount Số tiền cần định dạng
     * @return Chuỗi số tiền đã được định dạng
     */
    public static String dinhDangTienVietNam(double amount) {
        return VN_CURRENCY_FORMAT.format(amount);
    }

    /**
     * Định dạng số thập phân với tối đa 2 chữ số sau dấu phẩy
     * Ví dụ: 1000.5 -> 1,000.5
     * @param number Số cần định dạng
     * @return Chuỗi số đã được định dạng
     */
    public static String dinhDangSoThapPhan(double number) {
        return DECIMAL_FORMAT.format(number);
    }

    /**
     * Định dạng số nguyên với dấu phẩy phân cách hàng nghìn
     * Ví dụ: 1000 -> 1,000
     * @param number Số cần định dạng
     * @return Chuỗi số đã được định dạng
     */
    public static String dinhDangSoNguyen(long number) {
        return CURRENCY_FORMAT.format(number);
    }

    /**
     * Chuyển chuỗi số tiền đã định dạng về số double
     * Ví dụ: "1,000,000 VNĐ" -> 1000000.0
     * @param formattedAmount Chuỗi số tiền đã định dạng
     * @return Số tiền dạng double
     */
    public static double chuyenVeSo(String formattedAmount) {
        try {
            // Loại bỏ VNĐ, ₫, dấu phẩy và khoảng trắng
            String cleanAmount = formattedAmount.replaceAll("[VNĐ₫,\\s]", "");
            return Double.parseDouble(cleanAmount);
        } catch (NumberFormatException e) {
            System.out.println("Lỗi khi chuyển đổi chuỗi sang số: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Định dạng phần trăm
     * Ví dụ: 0.15 -> 15%
     * @param percent Số phần trăm (dạng thập phân)
     * @return Chuỗi phần trăm đã định dạng
     */
    public static String dinhDangPhanTram(double percent) {
        return String.format("%.2f%%", percent * 100);
    }

    /**
     * Định dạng phần trăm (input đã là phần trăm)
     * Ví dụ: 15 -> 15%
     * @param percent Số phần trăm
     * @return Chuỗi phần trăm đã định dạng
     */
    public static String dinhDangPhanTramTrucTiep(double percent) {
        return String.format("%.2f%%", percent);
    }

    /**
     * Làm tròn số tiền
     * @param amount Số tiền cần làm tròn
     * @param decimalPlaces Số chữ số thập phân
     * @return Số tiền đã được làm tròn
     */
    public static double lamTronSoTien(double amount, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(amount * multiplier) / multiplier;
    }
}


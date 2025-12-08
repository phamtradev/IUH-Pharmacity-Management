package vn.edu.iuh.fit.iuhpharmacitymanagement.service;

import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.ChiTietDonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.bus.DonNhapHangBUS;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.ChiTietDonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.DonNhapHang;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service tính toán thống kê thuế và lãi
 */
public class ThongKeThueLaiService {
    
    private final DonHangBUS donHangBUS;
    private final DonNhapHangBUS donNhapHangBUS;
    private final ChiTietDonHangBUS chiTietDonHangBUS;
    private final ChiTietDonNhapHangBUS chiTietDonNhapHangBUS;
    
    public ThongKeThueLaiService() {
        this.donHangBUS = new DonHangBUS();
        this.donNhapHangBUS = new DonNhapHangBUS();
        this.chiTietDonHangBUS = new ChiTietDonHangBUS();
        this.chiTietDonNhapHangBUS = new ChiTietDonNhapHangBUS();
    }
    
    /**
     * Tính tổng thuế GTGT đã thu từ bán hàng trong khoảng thời gian
     * Thuế thu = Tổng giá bán (đã VAT) - Tổng giá bán (chưa VAT)
     */
    public double tinhTongThueThu(LocalDate from, LocalDate to) {
        List<DonHang> donHangs = donHangBUS.layTatCaDonHang().stream()
                .filter(dh -> dh.getNgayDatHang() != null 
                        && !dh.getNgayDatHang().isBefore(from)
                        && !dh.getNgayDatHang().isAfter(to))
                .toList();
        
        double tongThueThu = 0;
        for (DonHang donHang : donHangs) {
            List<ChiTietDonHang> chiTiets = chiTietDonHangBUS.layDanhSachChiTietTheoMaDonHang(donHang.getMaDonHang());
            for (ChiTietDonHang chiTiet : chiTiets) {
                if (chiTiet.getLoHang() != null && chiTiet.getLoHang().getSanPham() != null) {
                    SanPham sp = chiTiet.getLoHang().getSanPham();
                    double thueVAT = sp.getThueVAT(); // Tỷ lệ thuế (0.1 = 10%)
                    
                    // Giá bán đã bao VAT = donGia
                    // Giá bán chưa VAT = donGia / (1 + thueVAT)
                    // Thuế = donGia - (donGia / (1 + thueVAT))
                    double giaBanCoVAT = chiTiet.getDonGia();
                    double giaBanChuaVAT = giaBanCoVAT / (1 + thueVAT);
                    double thue = giaBanCoVAT - giaBanChuaVAT;
                    
                    tongThueThu += thue * chiTiet.getSoLuong();
                }
            }
        }
        return tongThueThu;
    }
    
    /**
     * Tính tổng thuế GTGT đã trả từ nhập hàng trong khoảng thời gian
     */
    public double tinhTongThueTra(LocalDate from, LocalDate to) {
        List<DonNhapHang> donNhapHangs = donNhapHangBUS.layTatCaDonNhapHang().stream()
                .filter(dnh -> dnh.getNgayNhap() != null
                        && !dnh.getNgayNhap().isBefore(from)
                        && !dnh.getNgayNhap().isAfter(to))
                .toList();
        
        double tongThueTra = 0;
        for (DonNhapHang donNhapHang : donNhapHangs) {
            List<ChiTietDonNhapHang> chiTiets = chiTietDonNhapHangBUS.layChiTietDonNhapHangTheoMaDonNhapHang(donNhapHang.getMaDonNhapHang());
            for (ChiTietDonNhapHang chiTiet : chiTiets) {
                tongThueTra += chiTiet.getTienThue();
            }
        }
        return tongThueTra;
    }
    
    /**
     * Tính thuế ròng = Thuế thu - Thuế trả
     */
    public double tinhThueRong(LocalDate from, LocalDate to) {
        return tinhTongThueThu(from, to) - tinhTongThueTra(from, to);
    }
    
    /**
     * Tính tổng doanh thu (bán hàng) trong khoảng thời gian
     */
    public double tinhTongDoanhThu(LocalDate from, LocalDate to) {
        return donHangBUS.layTatCaDonHang().stream()
                .filter(dh -> dh.getNgayDatHang() != null
                        && !dh.getNgayDatHang().isBefore(from)
                        && !dh.getNgayDatHang().isAfter(to))
                .mapToDouble(DonHang::getThanhTien)
                .sum();
    }
    
    /**
     * Tính tổng giá vốn (nhập hàng) trong khoảng thời gian
     */
    public double tinhTongGiaVon(LocalDate from, LocalDate to) {
        return donNhapHangBUS.layTatCaDonNhapHang().stream()
                .filter(dnh -> dnh.getNgayNhap() != null
                        && !dnh.getNgayNhap().isBefore(from)
                        && !dnh.getNgayNhap().isAfter(to))
                .mapToDouble(DonNhapHang::getThanhTien)
                .sum();
    }
    
    /**
     * Tính tổng lợi nhuận gộp = Doanh thu - Giá vốn
     */
    public double tinhTongLoiNhuan(LocalDate from, LocalDate to) {
        return tinhTongDoanhThu(from, to) - tinhTongGiaVon(from, to);
    }
    
    /**
     * Tính tỷ suất lợi nhuận = (Lợi nhuận / Doanh thu) * 100
     */
    public double tinhTySuatLoiNhuan(LocalDate from, LocalDate to) {
        double doanhThu = tinhTongDoanhThu(from, to);
        if (doanhThu == 0) return 0;
        return (tinhTongLoiNhuan(from, to) / doanhThu) * 100;
    }
    
    /**
     * Lấy dữ liệu thuế theo tháng (12 tháng gần nhất)
     */
    public Map<String, Map<String, Double>> layThueTheoThang() {
        Map<String, Map<String, Double>> data = new HashMap<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            
            String monthKey = monthStart.getMonthValue() + "/" + monthStart.getYear();
            Map<String, Double> monthData = new HashMap<>();
            monthData.put("thueThu", tinhTongThueThu(monthStart, monthEnd));
            monthData.put("thueTra", tinhTongThueTra(monthStart, monthEnd));
            monthData.put("thueRong", tinhThueRong(monthStart, monthEnd));
            
            data.put(monthKey, monthData);
        }
        
        return data;
    }
    
    /**
     * Lấy dữ liệu lãi theo tháng (12 tháng gần nhất)
     */
    public Map<String, Map<String, Double>> layLaiTheoThang() {
        Map<String, Map<String, Double>> data = new HashMap<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            
            String monthKey = monthStart.getMonthValue() + "/" + monthStart.getYear();
            Map<String, Double> monthData = new HashMap<>();
            monthData.put("doanhThu", tinhTongDoanhThu(monthStart, monthEnd));
            monthData.put("giaVon", tinhTongGiaVon(monthStart, monthEnd));
            monthData.put("loiNhuan", tinhTongLoiNhuan(monthStart, monthEnd));
            monthData.put("tySuat", tinhTySuatLoiNhuan(monthStart, monthEnd));
            
            data.put(monthKey, monthData);
        }
        
        return data;
    }
    
    /**
     * Lấy dữ liệu thuế theo ngày (7 ngày gần nhất)
     */
    public Map<String, Map<String, Double>> layThueTheoNgay() {
        Map<String, Map<String, Double>> data = new HashMap<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            String dateKey = date.getDayOfMonth() + "/" + date.getMonthValue();
            
            Map<String, Double> dayData = new HashMap<>();
            dayData.put("thueThu", tinhTongThueThu(date, date));
            dayData.put("thueTra", tinhTongThueTra(date, date));
            dayData.put("thueRong", tinhThueRong(date, date));
            
            data.put(dateKey, dayData);
        }
        
        return data;
    }
    
    /**
     * Lấy dữ liệu lãi theo ngày (7 ngày gần nhất)
     */
    public Map<String, Map<String, Double>> layLaiTheoNgay() {
        Map<String, Map<String, Double>> data = new HashMap<>();
        LocalDate now = LocalDate.now();
        
        for (int i = 6; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            String dateKey = date.getDayOfMonth() + "/" + date.getMonthValue();
            
            Map<String, Double> dayData = new HashMap<>();
            dayData.put("doanhThu", tinhTongDoanhThu(date, date));
            dayData.put("giaVon", tinhTongGiaVon(date, date));
            dayData.put("loiNhuan", tinhTongLoiNhuan(date, date));
            dayData.put("tySuat", tinhTySuatLoiNhuan(date, date));
            
            data.put(dateKey, dayData);
        }
        
        return data;
    }
}


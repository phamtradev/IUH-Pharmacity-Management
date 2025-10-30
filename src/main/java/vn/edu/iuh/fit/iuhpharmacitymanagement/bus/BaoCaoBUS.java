/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.*;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic layer for Báo cáo thu chi
 *
 * @author PhamTra
 */
public class BaoCaoBUS {

    private DonHangDAO donHangDAO;
    private DonNhapHangDAO donNhapHangDAO;
    private DonTraHangDAO donTraHangDAO;
    private HangHongDAO hangHongDAO;

    public BaoCaoBUS() {
        this.donHangDAO = new DonHangDAO();
        this.donNhapHangDAO = new DonNhapHangDAO();
        this.donTraHangDAO = new DonTraHangDAO();
        this.hangHongDAO = new HangHongDAO();
    }

    /**
     * Lấy báo cáo thu chi theo thời gian và loại
     *
     * @param start Ngày bắt đầu
     * @param end Ngày kết thúc
     * @param type L oại báo cáo: "Tất cả", "Bán hàng", "Trả hàng", "Nhập hàng",
     * "Xuất hủy"
     * @return BaoCaoThuChi chứa danh sách giao dịch và thống kê
     */
    public BaoCaoThuChi layBaoCaoTheoThoiGian(LocalDate start, LocalDate end, String type) {
        List<DonHang> donHangs = new ArrayList<>();
        List<DonTraHang> donTraHangs = new ArrayList<>();
        List<DonNhapHang> donNhapHangs = new ArrayList<>();
        List<HangHong> hangHongs = new ArrayList<>();

        Map<String, Map<Integer, Double>> orderType = new LinkedHashMap<>();
        List<Object> objects = new ArrayList<>();

        if (type.equals("Tất cả")) {
            donHangs = layDonHangTheoNgay(start, end);
            donTraHangs = layDonTraHangTheoNgay(start, end);
            donNhapHangs = layDonNhapHangTheoNgay(start, end);
            hangHongs = layHangHongTheoNgay(start, end);
        } else if (type.equals("Bán hàng")) {
            donHangs = layDonHangTheoNgay(start, end);
        } else if (type.equals("Trả hàng")) {
            donTraHangs = layDonTraHangTheoNgay(start, end);
        } else if (type.equals("Nhập hàng")) {
            donNhapHangs = layDonNhapHangTheoNgay(start, end);
        } else if (type.equals("Xuất hủy")) {
            hangHongs = layHangHongTheoNgay(start, end);
        }

        objects.addAll(donHangs);
        objects.addAll(donTraHangs);
        objects.addAll(donNhapHangs);
        objects.addAll(hangHongs);

        // Sắp xếp theo ngày giảm dần
        objects.sort((o1, o2) -> {
            LocalDate date1 = layNgayGiaoDich(o1);
            LocalDate date2 = layNgayGiaoDich(o2);
            return date2.compareTo(date1);
        });

        // Tính toán thống kê
        Map<Integer, Double> donHangData = new HashMap<>();
        donHangData.put(donHangs.size(), donHangs.stream().mapToDouble(DonHang::getThanhTien).sum());
        orderType.put("Bán hàng", donHangData);

        Map<Integer, Double> donNhapHangData = new HashMap<>();
        donNhapHangData.put(donNhapHangs.size(), donNhapHangs.stream().mapToDouble(DonNhapHang::getThanhTien).sum());
        orderType.put("Nhập hàng", donNhapHangData);

        Map<Integer, Double> donTraHangData = new HashMap<>();
        donTraHangData.put(donTraHangs.size(), donTraHangs.stream().mapToDouble(DonTraHang::getThanhTien).sum());
        orderType.put("Trả hàng", donTraHangData);

        Map<Integer, Double> hangHongData = new HashMap<>();
        hangHongData.put(hangHongs.size(), hangHongs.stream().mapToDouble(HangHong::getThanhTien).sum());
        orderType.put("Xuất hủy", hangHongData);

        // Tính lợi nhuận: Thu - Chi
        double profit = 0;
        for (Map.Entry<String, Map<Integer, Double>> entry : orderType.entrySet()) {
            double totalValue = 0;
            for (double value : entry.getValue().values()) {
                totalValue += value;
            }
            if (entry.getKey().equals("Bán hàng")) {
                profit += totalValue; // Thu
            } else if (!entry.getKey().equals("Xuất hủy")) {
                profit -= totalValue; // Chi
            }
        }

        return new BaoCaoThuChi(objects, orderType, profit);
    }

    /**
     * Lấy danh sách đơn hàng theo khoảng thời gian
     */
    private List<DonHang> layDonHangTheoNgay(LocalDate start, LocalDate end) {
        List<DonHang> result = new ArrayList<>();
        List<DonHang> all = donHangDAO.findAll();
        for (DonHang dh : all) {
            if (!dh.getNgayDatHang().isBefore(start) && !dh.getNgayDatHang().isAfter(end)) {
                result.add(dh);
            }
        }
        return result;
    }

    /**
     * Lấy danh sách đơn nhập hàng theo khoảng thời gian
     */
    private List<DonNhapHang> layDonNhapHangTheoNgay(LocalDate start, LocalDate end) {
        List<DonNhapHang> result = new ArrayList<>();
        List<DonNhapHang> all = donNhapHangDAO.findAll();
        for (DonNhapHang dnh : all) {
            if (!dnh.getNgayNhap().isBefore(start) && !dnh.getNgayNhap().isAfter(end)) {
                result.add(dnh);
            }
        }
        return result;
    }

    /**
     * Lấy danh sách đơn trả hàng theo khoảng thời gian
     */
    private List<DonTraHang> layDonTraHangTheoNgay(LocalDate start, LocalDate end) {
        List<DonTraHang> result = new ArrayList<>();
        List<DonTraHang> all = donTraHangDAO.findAll();
        for (DonTraHang dth : all) {
            if (!dth.getNgayTraHang().isBefore(start) && !dth.getNgayTraHang().isAfter(end)) {
                result.add(dth);
            }
        }
        return result;
    }

    /**
     * Lấy danh sách hàng hỏng theo khoảng thời gian
     */
    private List<HangHong> layHangHongTheoNgay(LocalDate start, LocalDate end) {
        List<HangHong> result = new ArrayList<>();
        List<HangHong> all = hangHongDAO.findAll();
        for (HangHong hh : all) {
            if (!hh.getNgayNhap().isBefore(start) && !hh.getNgayNhap().isAfter(end)) {
                result.add(hh);
            }
        }
        return result;
    }

    /**
     * Lấy ngày giao dịch từ object
     */
    private LocalDate layNgayGiaoDich(Object obj) {
        if (obj instanceof DonHang donHang) {
            return donHang.getNgayDatHang();
        } else if (obj instanceof DonTraHang donTraHang) {
            return donTraHang.getNgayTraHang();
        } else if (obj instanceof DonNhapHang donNhapHang) {
            return donNhapHang.getNgayNhap();
        } else if (obj instanceof HangHong hangHong) {
            return hangHong.getNgayNhap();
        }
        return null;
    }

    /**
     * Inner class để chứa kết quả báo cáo
     */
    public static class BaoCaoThuChi {

        private List<Object> reports;
        private Map<String, Map<Integer, Double>> orderType;
        private double profit;

        public BaoCaoThuChi(List<Object> reports, Map<String, Map<Integer, Double>> orderType, double profit) {
            this.reports = reports;
            this.orderType = orderType;
            this.profit = profit;
        }

        public List<Object> getReports() {
            return reports;
        }

        public Map<String, Map<Integer, Double>> getOrderType() {
            return orderType;
        }

        public double getProfit() {
            return profit;
        }
    }
}

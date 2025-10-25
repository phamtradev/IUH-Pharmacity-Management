/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.KhuyenMaiDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author User
 */
public class KhuyenMaiBUS {

    private final KhuyenMaiDAO khuyenMaiDAO;
    private final DonHangDAO donHangDAO;

    public KhuyenMaiBUS() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.donHangDAO = new DonHangDAO();
    }

    //Lấy danh sách tất cả các chương trình khuyến mãi.
    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> danhSach = khuyenMaiDAO.findAll();
        // Cập nhật trạng thái cho từng khuyến mãi trước khi trả về
        danhSach.forEach(this::capNhatTrangThaiTuDong);
        return danhSach;
    }

    //Lấy danh sách các khuyến mãi còn hạn.
    public List<KhuyenMai> getKhuyenMaiConHan() {
        return getAllKhuyenMai().stream()
                .filter(KhuyenMai::isTrangThai)
                .collect(Collectors.toList());
    }

    //Thêm một chương trình khuyến mãi mới, kiểm tra các quy tắc nghiệp vụ.
    public boolean themKhuyenMai(KhuyenMai km) throws Exception {
        //Tên khuyến mãi không được trùng
        if (khuyenMaiDAO.existsByName(km.getTenKhuyenMai())) {
            throw new Exception("Tên khuyến mãi '" + km.getTenKhuyenMai() + "' đã tồn tại.");
        }
        return khuyenMaiDAO.insert(km);
    }

    //Cập nhật thông tin một chương trình khuyến mãi.
    public boolean capNhatKhuyenMai(KhuyenMai km) throws Exception {
        //Tên mới không được trùng với một khuyến mãi khác
        KhuyenMai kmTheoTen = khuyenMaiDAO.findByName(km.getTenKhuyenMai());
        if (kmTheoTen != null && !kmTheoTen.getMaKhuyenMai().equals(km.getMaKhuyenMai())) {
            throw new Exception("Tên khuyến mãi '" + km.getTenKhuyenMai() + "' đã tồn tại.");
        }

        return khuyenMaiDAO.update(km);
    }

    //Xóa một chương trình khuyến mãi.
    public boolean xoaKhuyenMai(String maKhuyenMai) throws Exception {
        //Không được xóa khuyến mãi nếu đã được áp dụng
        long soLuongDonHang = donHangDAO.countByKhuyenMai(maKhuyenMai);
        if (soLuongDonHang > 0) {
            throw new Exception("Không thể xóa khuyến mãi này vì đã được áp dụng cho " + soLuongDonHang + " đơn hàng.");
        }

        return khuyenMaiDAO.delete(maKhuyenMai);
    }

    //kiểm tra và cập nhật trạng thái của khuyến mãi.
    private void capNhatTrangThaiTuDong(KhuyenMai km) {
        LocalDate homNay = LocalDate.now();
        //ngày hôm nay sau ngày kết thúc và trạng thái đang là true -> hết hạn
        if (homNay.isAfter(km.getNgayKetThuc()) && km.isTrangThai()) {
            km.setTrangThai(false);
        } // Nếu ngày hôm nay trong khoảng khuyến mãi, trạng thái đang là false -> kích hoạt lại
        else if (!homNay.isAfter(km.getNgayKetThuc()) && !homNay.isBefore(km.getNgayBatDau()) && !km.isTrangThai()) {
            km.setTrangThai(true);
        }
    }
}

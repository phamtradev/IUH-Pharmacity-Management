/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.DonHangDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.KhuyenMaiDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.ChiTietKhuyenMaiSanPhamDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.KhuyenMai;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.SanPham;
import vn.edu.iuh.fit.iuhpharmacitymanagement.constant.LoaiKhuyenMai;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author User
 */
public class KhuyenMaiBUS {

    private final KhuyenMaiDAO khuyenMaiDAO;
    private final DonHangDAO donHangDAO;
    private final ChiTietKhuyenMaiSanPhamDAO chiTietKhuyenMaiSanPhamDAO;

    public KhuyenMaiBUS() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.donHangDAO = new DonHangDAO();
        this.chiTietKhuyenMaiSanPhamDAO = new ChiTietKhuyenMaiSanPhamDAO();
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
    
    /**
     * Tìm khuyến mãi tốt nhất (giảm giá nhiều nhất) cho đơn hàng
     * @param tongTienHang Tổng tiền hàng hiện tại
     * @param danhSachSanPham Map của sản phẩm và số lượng trong giỏ hàng
     * @return Khuyến mãi tốt nhất hoặc null nếu không có
     */
    public KhuyenMai timKhuyenMaiTotNhat(double tongTienHang, Map<SanPham, Integer> danhSachSanPham) {
        List<KhuyenMai> danhSachKhuyenMai = getKhuyenMaiConHan();
        
        KhuyenMai khuyenMaiTotNhat = null;
        double giamGiaLonNhat = 0;
        
        for (KhuyenMai km : danhSachKhuyenMai) {
            double giamGia = 0;
            
            if (km.getLoaiKhuyenMai() == LoaiKhuyenMai.DON_HANG) {
                // Khuyến mãi hóa đơn: Áp dụng cho tổng đơn hàng
                giamGia = tongTienHang * (km.getGiamGia() / 100.0);
            } else if (km.getLoaiKhuyenMai() == LoaiKhuyenMai.SAN_PHAM) {
                // Khuyến mãi sản phẩm: Kiểm tra từng sản phẩm trong giỏ
                var danhSachCTKM = chiTietKhuyenMaiSanPhamDAO.findByMaKhuyenMai(km.getMaKhuyenMai());
                
                for (var entry : danhSachSanPham.entrySet()) {
                    SanPham sp = entry.getKey();
                    int soLuong = entry.getValue();
                    
                    // Kiểm tra sản phẩm có trong chương trình khuyến mãi không
                    boolean coKhuyenMai = danhSachCTKM.stream()
                        .anyMatch(ctkm -> ctkm.getSanPham().getMaSanPham().equals(sp.getMaSanPham()));
                    
                    if (coKhuyenMai) {
                        double tongTienSP = sp.getGiaBan() * soLuong;
                        giamGia += tongTienSP * (km.getGiamGia() / 100.0);
                    }
                }
            }
            
            // So sánh và chọn khuyến mãi có giảm giá lớn nhất
            if (giamGia > giamGiaLonNhat) {
                giamGiaLonNhat = giamGia;
                khuyenMaiTotNhat = km;
            }
        }
        
        return khuyenMaiTotNhat;
    }
}

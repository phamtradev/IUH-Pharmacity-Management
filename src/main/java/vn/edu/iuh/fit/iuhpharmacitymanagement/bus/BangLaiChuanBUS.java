package vn.edu.iuh.fit.iuhpharmacitymanagement.bus;

import vn.edu.iuh.fit.iuhpharmacitymanagement.dao.BangLaiChuanDAO;
import vn.edu.iuh.fit.iuhpharmacitymanagement.entity.BangLaiChuan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * BUS cho cấu hình lãi chuẩn theo khoảng giá nhập.
 */
public class BangLaiChuanBUS {

    private final BangLaiChuanDAO dao;

    public BangLaiChuanBUS(BangLaiChuanDAO dao) {
        this.dao = dao;
    }

    /**
     * Lấy danh sách các cấu hình lãi chuẩn, đã sort theo giá nhập từ nhỏ đến lớn.
     */
    public List<BangLaiChuan> layTatCa() {
        List<BangLaiChuan> list = new ArrayList<>(dao.findAll());
        list.sort(Comparator.comparingDouble(BangLaiChuan::getGiaNhapTu));
        return list;
    }

    /**
     * Ghi đè toàn bộ cấu hình lãi chuẩn (xóa hết và insert lại).
     */
    public boolean luuLaiTatCa(List<BangLaiChuan> ds) {
        dao.deleteAll();
        boolean ok = true;
        for (BangLaiChuan b : ds) {
            if (!dao.insert(b)) {
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Tìm tỷ lệ lãi áp dụng cho 1 giá nhập.
     * Nếu không tìm thấy khoảng phù hợp thì trả về 0.
     */
    public double timTyLeLaiChoGiaNhap(double giaNhap) {
        if (giaNhap <= 0) return 0;
        List<BangLaiChuan> ds = layTatCa();
        for (BangLaiChuan b : ds) {
            double tu = b.getGiaNhapTu();
            double den = b.getGiaNhapDen();
            boolean matchTu = giaNhap >= tu;
            boolean matchDen = (den <= 0) || (giaNhap <= den);
            if (matchTu && matchDen) {
                return b.getTyLeLai();
            }
        }
        return 0;
    }
}



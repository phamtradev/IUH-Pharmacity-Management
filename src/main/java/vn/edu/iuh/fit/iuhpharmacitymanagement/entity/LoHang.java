package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import java.time.LocalDate;

public class LoHang {

    private String maLoHang;
    private String tenLoHang;
    private LocalDate ngaySanXuat;
    private LocalDate hanSuDung;
    private int tonKho;
    private boolean trangThai;
    private SanPham sanPham;

    
    public static final String MA_LO_HANG_SAI = "Mã lô hàng phải có dạng LHxxxxx (xxxxx là 5 chữ số)";
    public static final String TEN_LO_HANG_RONG = "Tên lô hàng không được rỗng";
    public static final String HAN_SU_DUNG_SAI = "Hạn sử dụng phải lớn hơn ngày sản xuất";
    public static final String TON_KHO_SAI = "Tồn kho phải lớn hơn hoặc bằng 0";
    public static final String SAN_PHAM_RONG = "Sản phẩm không được rỗng";
    
    public static final String MA_LO_HANG_REGEX = "^LH\\d{5}$";
    
    public LoHang() {
    }
    
    
    public LoHang(String maLoHang) {
        this.maLoHang = maLoHang;
    }
    
    public LoHang(String maLoHang,
                  String tenLoHang,
                  LocalDate ngaySanXuat,
                  LocalDate hanSuDung,
                  int tonKho,
                  boolean trangThai,
                  SanPham sanPham) throws Exception {
        setMaLoHang(maLoHang);
        setTenLoHang(tenLoHang);
        setNgaySanXuat(ngaySanXuat);
        setHanSuDung(hanSuDung);
        setTonKho(tonKho);
        setTrangThai(trangThai);
        setSanPham(sanPham);
    }

    // ====== Getter & Setter ======
    public String getMaLoHang() {
        return maLoHang;
    }

    public void setMaLoHang(String maLoHang) throws Exception {
        if (!maLoHang.matches(MA_LO_HANG_REGEX)) {
            throw new Exception(MA_LO_HANG_SAI);
        }
        this.maLoHang = maLoHang;
    }

    public String getTenLoHang() {
        return tenLoHang;
    }

    public void setTenLoHang(String tenLoHang) throws Exception {
        if (tenLoHang == null || tenLoHang.isBlank()) {
            throw new Exception(TEN_LO_HANG_RONG);
        }
        this.tenLoHang = tenLoHang;
    }

    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) throws Exception {
        if (this.ngaySanXuat != null && hanSuDung.isBefore(this.ngaySanXuat)) {
            throw new Exception(HAN_SU_DUNG_SAI);
        }
        this.hanSuDung = hanSuDung;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) throws Exception {
        if (tonKho < 0) {
            throw new Exception(TON_KHO_SAI);
        }
        this.tonKho = tonKho;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) throws Exception {
        if (sanPham == null) {
            throw new Exception(SAN_PHAM_RONG);
        }
        this.sanPham = sanPham;
    }

    @Override
    public String toString() {
        return "LoHang{" +
                "maLoHang='" + maLoHang + '\'' +
                ", tenLoHang='" + tenLoHang + '\'' +
                ", ngaySanXuat=" + ngaySanXuat +
                ", hanSuDung=" + hanSuDung +
                ", tonKho=" + tonKho +
                ", trangThai=" + (trangThai ? "Đang bán" : "Ngưng bán") +
                ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : "null") +
                '}';
    }
}

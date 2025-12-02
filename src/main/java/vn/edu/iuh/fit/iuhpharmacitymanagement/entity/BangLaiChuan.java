package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

public class BangLaiChuan {

    private int id;
    private double giaNhapTu;
    private double giaNhapDen; // có thể = 0 hoặc < 0 để hiểu là không giới hạn trên
    private double tyLeLai;    // dạng thập phân: 0.2 = 20%

    public BangLaiChuan() {
    }

    public BangLaiChuan(int id, double giaNhapTu, double giaNhapDen, double tyLeLai) {
        this.id = id;
        this.giaNhapTu = giaNhapTu;
        this.giaNhapDen = giaNhapDen;
        this.tyLeLai = tyLeLai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGiaNhapTu() {
        return giaNhapTu;
    }

    public void setGiaNhapTu(double giaNhapTu) {
        this.giaNhapTu = giaNhapTu;
    }

    public double getGiaNhapDen() {
        return giaNhapDen;
    }

    public void setGiaNhapDen(double giaNhapDen) {
        this.giaNhapDen = giaNhapDen;
    }

    public double getTyLeLai() {
        return tyLeLai;
    }

    public void setTyLeLai(double tyLeLai) {
        this.tyLeLai = tyLeLai;
    }

    @Override
    public String toString() {
        return "BangLaiChuan{" +
                "id=" + id +
                ", giaNhapTu=" + giaNhapTu +
                ", giaNhapDen=" + giaNhapDen +
                ", tyLeLai=" + tyLeLai +
                '}';
    }
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.iuh.fit.iuhpharmacitymanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

/**
 *
 * @author PhamTra
 */
@Entity
@Table(name = "san_pham")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SanPham {

    @Id
    @Column(name = "ma_san_pham")
    private String maSanPham;

    @Column(name = "ten_san_pham")
    @Nationalized
    private String tenSanPham;

    @Column(name = "so_dang_ky")
    @Nationalized
    private String soDangKy;

    @Column(name = "hoat_chat")
    @Nationalized
    private String hoatChat;

    @Column(name = "lieu_dung")
    @Nationalized
    private String lieuDung;

    @Column(name = "cach_dong_goi")
    @Nationalized
    private String cachDongGoi;

    @Column(name = "quoc_gia_san_xuat")
    @Nationalized
    private String quocGiaSanXuat;

    @Column(name = "nha_san_xuat")
    @Nationalized
    private String nhaSanXuat;

    @Column(name = "gia_nhap")
    private double giaNhap;

    @Column(name = "gia_ban")
    private double giaBan;

    @Column(name = "hoat_dong")
    private boolean hoatDong;

    @Column(name = "thue_vat")
    private double thueVAT = 0.1;

    @Column(name = "hinh_anh")
    private String hinhAnh;

}

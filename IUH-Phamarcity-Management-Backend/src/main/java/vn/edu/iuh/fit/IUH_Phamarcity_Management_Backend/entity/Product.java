package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.enums.ProductType;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "name")
    @Nationalized
    private String name;

    @Column(name = "registration_number")
    @Nationalized
    private String registrationNumber;

    @Column(name = "active_ingredient")
    @Nationalized
    private String activeIngredient;

    @Column(name = "dosage")
    @Nationalized
    private String dosage;

    @Column(name = "packaging")
    @Nationalized
    private String packaging;

    @Column(name = "country_of_origin")
    @Nationalized
    private String countryOfOrigin;

    @Column(name = "manufacturer")
    @Nationalized
    private String manufacturer;

    @Column(name = "purchase_price")
    private double purchasePrice;

    @Column(name = "selling_price")
    private double sellingPrice;

    @Column(name = "active")
    private boolean active;

    @Column(name = "vat")
    private double VAT = 0.1;

    @Column(name = "image")
    private String image;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING) //enum đươợc lưu dưới dạng chuỗi
    private ProductType productType;

    @OneToMany( mappedBy = "product" ,cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    private List<Batch> listBatch;

    @ManyToOne
    @JoinColumn(  name = "unit_id")
    private Unit unit;
}

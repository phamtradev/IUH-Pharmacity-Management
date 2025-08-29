package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Table(name = "batchs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Batch {

    @Id
    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "name")
    @Nationalized
    private String name;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "stock")
    private int stock;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

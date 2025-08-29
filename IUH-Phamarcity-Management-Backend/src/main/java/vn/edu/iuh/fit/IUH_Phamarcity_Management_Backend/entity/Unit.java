package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "units")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Unit {

    @Id
    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "name")
    @Nationalized
    private String name;
}

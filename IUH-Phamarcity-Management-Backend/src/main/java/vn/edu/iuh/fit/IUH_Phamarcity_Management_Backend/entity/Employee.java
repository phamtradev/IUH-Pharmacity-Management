package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "name")
    @Nationalized
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    @Nationalized //đảm bảo lưu được các ký tự unicode (chuỗi)
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Nationalized
    private String role;

}

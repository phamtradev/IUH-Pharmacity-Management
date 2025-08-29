package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}

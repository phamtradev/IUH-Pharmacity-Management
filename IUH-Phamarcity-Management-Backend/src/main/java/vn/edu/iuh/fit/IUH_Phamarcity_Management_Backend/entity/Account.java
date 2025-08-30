package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Tài khoản không được để trống")
    private String accountId;

    @Column(name = "password")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}

package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity.Employee;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String accountId;
    private Employee employee;
    private String message;
    private boolean success;
}

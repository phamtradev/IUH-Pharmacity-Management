package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Tài khoản không được để trống")
    private String accountId;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}

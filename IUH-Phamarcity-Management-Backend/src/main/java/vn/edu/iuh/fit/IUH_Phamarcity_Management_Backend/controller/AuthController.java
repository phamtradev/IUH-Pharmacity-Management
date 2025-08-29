package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.dto.*;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity.Account;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.service.AccountService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountService accountService;


    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/login")
    public ResponseEntity<RestResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        // xác thực thông tin đăng nhập - exception sẽ được GlobalExceptionHandler xử lý
        Account account = accountService.validateLogin(
            loginRequest.getAccountId(),
            loginRequest.getPassword()
        );

        // tạo response thành công với thông tin tài khoản
        LoginResponse loginData = new LoginResponse(
            account.getAccountId(),
            account.getEmployee(),
            "Đăng nhập thành công",
            true
        );

        RestResponse<LoginResponse> response = new RestResponse<>(
            HttpStatus.OK.value(),
            "Đăng nhập thành công",
            loginData
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<RestResponse<Object>> logout() {
        RestResponse<Object> response = new RestResponse<>(
            HttpStatus.OK.value(),
            "Đăng xuất thành công",
            null
        );
        return ResponseEntity.ok(response);
    }
}

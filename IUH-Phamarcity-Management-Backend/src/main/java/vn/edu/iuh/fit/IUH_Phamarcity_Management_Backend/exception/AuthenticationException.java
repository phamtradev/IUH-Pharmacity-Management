package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.exception;

/**
 * Exception được ném khi có lỗi xác thực người dùng
 * Ví dụ: sai tài khoản, sai mật khẩu
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

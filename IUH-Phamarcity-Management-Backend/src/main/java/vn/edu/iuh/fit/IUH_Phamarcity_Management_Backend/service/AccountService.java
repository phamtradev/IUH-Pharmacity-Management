package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.service;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity.Account;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.exception.*;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.repository.AccountRepository;

import java.util.Optional;

/**
 * Service xử lý các thao tác liên quan đến tài khoản người dùng
 * Bao gồm: xác thực, đổi mật khẩu, reset mật khẩu
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Constructor khởi tạo AccountService
     * @param accountRepository Repository để thao tác với database
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Lưu tài khoản vào database
     * @param account Tài khoản cần lưu
     * @return Tài khoản đã được lưu
     */
    public Account handleSaveAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Tìm tài khoản theo ID
     * @param accountId ID của tài khoản
     * @return Optional chứa tài khoản nếu tìm thấy
     */
    public Optional<Account> findByAccountId(String accountId) {
        return accountRepository.findById(accountId);
    }

    /**
     * Xác thực thông tin đăng nhập
     * @param accountId ID tài khoản
     * @param password Mật khẩu
     * @return Account nếu xác thực thành công
     * @throws InvalidInputException Khi dữ liệu đầu vào không hợp lệ
     * @throws AuthenticationException Khi thông tin đăng nhập không đúng
     */
    public Account validateLogin(String accountId, String password) {
        // Kiểm tra dữ liệu đầu vào
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new InvalidInputException("Tài khoản không được để trống");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Mật khẩu không được để trống");
        }

        // Tìm tài khoản
        Optional<Account> accountOpt = findByAccountId(accountId);
        if (accountOpt.isEmpty()) {
            throw new AuthenticationException("Tài khoản không tồn tại");
        }

        Account account = accountOpt.get();
        // Kiểm tra mật khẩu
        if (!account.getPassword().equals(password)) {
            throw new AuthenticationException("Mật khẩu không đúng");
        }

        return account;
    }
}

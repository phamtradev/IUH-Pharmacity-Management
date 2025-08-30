package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.service;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity.Account;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.exception.*;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account handleSaveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> findByAccountId(String accountId) {
        return accountRepository.findById(accountId);
    }

    /**
     * Xác thực thông tin đăng nhập
     * @param accountId ID tài khoản
     * @param password Mật khẩu
     * @return Account nếu xác thực thành công
     * @throws AuthenticationException Khi thông tin đăng nhập không đúng
     */
    public Account validateLogin(String accountId, String password) {
        // Validation đã được xử lý bởi @Valid annotation trong Controller

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

package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity.Account;

/**
 * Repository interface để thao tác với bảng accounts trong database
 * Kế thừa JpaRepository để có sẵn các phương thức CRUD cơ bản
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    // JpaRepository đã cung cấp sẵn các phương thức:
    // - save(Account account): Lưu tài khoản
    // - findById(String id): Tìm tài khoản theo ID
    // - findAll(): Lấy tất cả tài khoản
    // - deleteById(String id): Xóa tài khoản theo ID
    // - existsById(String id): Kiểm tra tài khoản có tồn tại không
}

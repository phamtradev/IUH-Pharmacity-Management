package vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.fit.IUH_Phamarcity_Management_Backend.entity.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}

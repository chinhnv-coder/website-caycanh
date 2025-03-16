package assignment.assignment.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import assignment.assignment.entity.Account;

public interface AccountDAO extends JpaRepository<Account, String>{
    Optional<Account> findByActivationCode(String activationCode);
    Optional<Account> findByResetToken(String resetToken);
}

package assignment.assignment.service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assignment.assignment.entity.Account;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private SessionService sessionService; 

    @Override
    public String getUsername() {
        return getCurrentUser().map(Account::getUsername).orElse(null);
    }

    @Override
    public List<String> getRoles() {
        return getCurrentUser().map(user -> user.getAdmin() ? List.of("ADMIN") : List.of("USER"))
                               .orElse(List.of());
    }

    @Override
    public boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }

    @Override
    public boolean hasAnyRoles(String... roles) {
        List<String> userRoles = getRoles();
        return Arrays.stream(roles).anyMatch(userRoles::contains);
    }

    // Phương thức lấy user từ session
    private Optional<Account> getCurrentUser() {
        return Optional.ofNullable(sessionService.get("user", Account.class));
    }
}

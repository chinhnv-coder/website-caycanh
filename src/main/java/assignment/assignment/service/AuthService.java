package assignment.assignment.service;

import java.util.List;

public interface AuthService {
    // Lấy tên đăng nhập
    String getUsername();

    // Lấy vai trò của người đăng nhập
    List<String> getRoles();

    // Kiểm tra đăng nhập hay chưa
    boolean isAuthenticated();

    // Kiểm trả vai trò của người đăng nhập
    boolean hasAnyRoles(String...roles);
} 


package assignment.assignment.intercepter; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import assignment.assignment.entity.Account;
import assignment.assignment.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionService sessionService; // Không cần constructor

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();

        // Lấy tài khoản từ session
        Account account = sessionService.get("user", Account.class);
        if (account == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        // Kiểm tra quyền admin khi truy cập trang quản trị
        if (uri.startsWith("/admin") && !Boolean.TRUE.equals(account.getAdmin())) {
            response.sendRedirect("/error/403");
            return false;
        }

        return true;
    }
}

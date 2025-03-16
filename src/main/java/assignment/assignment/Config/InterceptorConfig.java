package assignment.assignment.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import assignment.assignment.intercepter.SecurityInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    @Autowired
    SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/account/**", "/order/**", "/admin/**") // Các route cần kiểm tra đăng nhập
                .excludePathPatterns("/auth/login", 
                                    "/account/sign-up", 
                                    "/home/**", 
                                    "/account/activate", 
                                    "/account/forgot-password",
                                    "/account/reset-password" ); // Các route không cần kiểm tra
    }
}

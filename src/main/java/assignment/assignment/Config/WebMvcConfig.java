package assignment.assignment.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photos/**") // Đường dẫn URL
                .addResourceLocations("file:E:/javaspringboot/assignment/photos/"); // Thư mục thực tế
    }
}

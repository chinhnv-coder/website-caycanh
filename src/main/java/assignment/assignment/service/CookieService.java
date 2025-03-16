package assignment.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {
    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    // Đọc cookie từ request
    public Cookie get(String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    // Đọc giá trị của cookie từ request
    public String getValue(String name){
        Cookie cookie = get(name);
        return (cookie != null) ? cookie.getValue() : "";
    }

    // Tạo và gửi cookie về client
    public Cookie add(String name, String value, int hours){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(hours * 3600);
        cookie.setPath("/"); 
        response.addCookie(cookie);
        return cookie;
    }

    // Xóa cookie khỏi client
    public void remove(String name){
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}

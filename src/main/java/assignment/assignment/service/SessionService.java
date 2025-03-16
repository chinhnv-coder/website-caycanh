package assignment.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {
    @Autowired
    private HttpSession session;

    @Autowired
    private ObjectMapper objectMapper; // Dùng để hỗ trợ convert kiểu dữ liệu generic

    // Đọc giá trị của attribute trong session với hỗ trợ generic type
    public <T> T get(String name, Class<T> type) {
        Object value = session.getAttribute(name);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    // Đọc giá trị với kiểu dữ liệu phức tạp (generic type)
    public <T> T get(String name, TypeReference<T> typeRef) {
        Object value = session.getAttribute(name);
        if (value != null) {
            return objectMapper.convertValue(value, typeRef);
        }
        return null;
    }

    // Lấy giá trị, nếu không có thì trả về giá trị mặc định
    public <T> T getOrDefault(String name, TypeReference<T> typeRef, T defaultValue) {
        T value = get(name, typeRef);
        return value != null ? value : defaultValue;
    }

    // Lưu giá trị vào session
    public void set(String name, Object value) {
        session.setAttribute(name, value);
    }

    // Xóa attribute trong session
    public void remove(String name) {
        session.removeAttribute(name);
    }

    // Kiểm tra xem session có chứa attribute không
    public boolean contains(String name) {
        return session.getAttribute(name) != null;
    }

    // Xóa toàn bộ session (logout)
    public void clear() {
        session.invalidate();
    }
}

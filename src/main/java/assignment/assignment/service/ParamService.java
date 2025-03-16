package assignment.assignment.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ParamService {
    @Autowired 
    HttpServletRequest request;

    @Autowired
    ServletContext servletContext;

    // Đọc chuỗi giá trị của tham số
    public String getString(String name, String defaultValue){
        String value = request.getParameter(name);
        return (value != null) ? value : defaultValue;
    }

    // Đọc số nguyên giá trị của tham số 
    public int  getInt(String name, int defaultValue){
        try {
            String value = request.getParameter(name);
            return(value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Đọc số thực giá trị của tham số 
    public double getDouble(String name, double defaultValue){
        try {
            String value = request.getParameter(name);
            return(value != null) ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Đọc giá trị boolean của tham số 
    public boolean getBoolean(String name, boolean defaultValue){
        String value = request.getParameter(name);
        if(value != null){
            return value.equalsIgnoreCase("true") || value.equals("1");
        }
        return defaultValue;
    }

    // Đọc giá trị thời gian của tham số 
    public Date getDate(String name, String pattern){
        String value = request.getParameter(name);
        if(value != null){
           try {
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                formatter.setLenient(false);
                return formatter.parse(value);
           } catch (ParseException e) {
                throw new RuntimeException(" Loi dinh dang ngay: " + value);
           }
        } 
        return null;
    }

    // Lưu file upload vào thư mục 
    public File save(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs(); // Tạo thư mục nếu chưa có
            }
            
            File savedFile = new File(dir, file.getOriginalFilename());
            try {
                file.transferTo(savedFile);
                return savedFile; // Trả về file đã lưu
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file", e);
            }
        }
        return null;
    }
    
}























//  formatter.setLenient(false); Không chấp nhận ngày không hợp lệ
// Tạo đối tượng simpleDayFormat đảm bảo chuyển đổi chính xác, nghiêm ngặt và dễ bảo trì
// Spring tự động binding request parameter (từ URL hoặc form) vào biến khi dùng @RequestParam.

// Bước 1: Controller nhận file từ request bằng @RequestParam("file") MultipartFile file.
// Bước 2: Controller gọi ParamService.save(file, path) để xử lý lưu file.
// Bước 3: ParamService thực hiện logic lưu file vào thư mục chỉ định.
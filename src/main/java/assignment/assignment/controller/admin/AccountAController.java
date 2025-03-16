package assignment.assignment.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import assignment.assignment.DAO.AccountDAO;
import assignment.assignment.entity.Account;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin/account")
public class AccountAController {
    @Autowired
    AccountDAO accountDAO;

    @GetMapping("/index")
    public String index(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        List<Account> accounts = accountDAO.findAll();
        model.addAttribute("accounts", accounts); 
        model.addAttribute("view", "admin/userCRUD");
        return "admin/layout";
    }

    @GetMapping("/edit/{username}")
    public String edit(Model model, @PathVariable("username") String username) {
        Account account = accountDAO.findById(username).get();
        model.addAttribute("account", account);
        List<Account> accounts = accountDAO.findAll();
        model.addAttribute("accounts", accounts);
        model.addAttribute("view", "admin/userCRUD");
        return "admin/layout";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute Account account, 
                        @RequestParam("photoFile") MultipartFile photoFile, 
                        Model model,
                        RedirectAttributes redirectAttributes) {

        // Kiểm tra dữ diệu đầu vào
        if (account.getUsername().isEmpty() || account.getPassword().isEmpty() ||
        account.getEmail().isEmpty() || account.getFullname().isEmpty() ||
        account.getAdmin() == null || account.getActivated() == null) {
        redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin và chọn vai trò, trạng thái!");
        return "redirect:/admin/account/index";
        }

        // Kiểm tra định dạng email
        if (!account.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            redirectAttributes.addFlashAttribute("error", "Email không hợp lệ!");
            return "redirect:/admin/account/index";
        }
        if (!photoFile.isEmpty()) {
            String uploadDir = "E:\\javaspringboot\\assignment\\photos\\";
            String fileName = System.currentTimeMillis() + "-" + photoFile.getOriginalFilename();
            File savedFile = new File(uploadDir + fileName);
            try {
                photoFile.transferTo(savedFile);
                account.setPhoto(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/admin/account/index?error=upload_failed";
            }
        }
        accountDAO.save(account);
        redirectAttributes.addFlashAttribute("success", "Tạo tài khoản thành công!");
        // Trả về danh sách sản phẩm sau khi thêm
        List<Account> accounts = accountDAO.findAll();
        model.addAttribute("accounts", accounts);
        return "redirect:/admin/account/index";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Account account, 
                        @RequestParam("photoFile") MultipartFile photoFile, 
                        Model model,
                        RedirectAttributes redirectAttributes) {
        Account existingAccount = accountDAO.findById(account.getUsername()).orElse(null);
        if (existingAccount == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/admin/account/index";
        }
    
        // Không cho phép sửa username
        account.setUsername(existingAccount.getUsername());
    
        // Kiểm tra các trường bắt buộc
        if (account.getFullname().isEmpty() || account.getEmail().isEmpty() ||
            account.getAdmin() == null || account.getActivated() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin và chọn vai trò, trạng thái!");
            return "redirect:/admin/account/index";
        }
    
        // Kiểm tra định dạng email
        if (!account.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            redirectAttributes.addFlashAttribute("error", "Email không hợp lệ!");
            return "redirect:/admin/account/index";
        }

        // Nếu người dùng không nhập mật khẩu mới, giữ nguyên mật khẩu cũ
        if (account.getPassword() == null || account.getPassword().isEmpty()) {
            account.setPassword(existingAccount.getPassword());
        }

        if (!photoFile.isEmpty()) { // Nếu có ảnh mới được tải lên
            String uploadDir = "E:\\javaspringboot\\assignment\\photos\\";
            String fileName = System.currentTimeMillis() + "-" + photoFile.getOriginalFilename();
            File savedFile = new File(uploadDir + fileName);
            try {
                photoFile.transferTo(savedFile);
                account.setPhoto(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            account.setPhoto(existingAccount.getPhoto()); 
        }

        accountDAO.save(account);
        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
        // Trả về danh sách sản phẩm sau khi cập nhật
        List<Account> accounts = accountDAO.findAll();
        model.addAttribute("accounts", accounts);
        return "redirect:/admin/account/index";
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable("username") String username, RedirectAttributes redirectAttributes) {
        if (!accountDAO.existsById(username)) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/admin/account/index";
        }

        try {
            accountDAO.deleteById(username);
            redirectAttributes.addFlashAttribute("success", "Xóa tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa tài khoản do ràng buộc dữ liệu!");
        }

        return "redirect:/admin/account/index";
    }
}
    


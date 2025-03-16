package assignment.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import assignment.assignment.DAO.AccountDAO;
import assignment.assignment.entity.Account;
import assignment.assignment.service.AuthService;
import assignment.assignment.service.CartService;
import assignment.assignment.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    AccountDAO accountDAO;

    @Autowired
    AuthService authService;

    @Autowired
    SessionService sessionService;

    @Autowired
    CartService cartService;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("view", "account-page/login");
        return "layouts/layout";
    }

    @PostMapping("/login")
    public String login(RedirectAttributes redirectAttributes,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", required = false) String remember) {
        
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/auth/login";
        }

        Account account = accountDAO.findById(username).orElse(null);
        
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/auth/login";
        }

        if (account.getActivated() == null || !account.getActivated()) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản chưa được kích hoạt!");
            return "redirect:/auth/login";
        }

        if (!account.getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Sai mật khẩu!");
            return "redirect:/auth/login";
        }

        if (remember != null) {
            sessionService.set("user", account);
        }

        // Đăng nhập thành công
        redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
        return "redirect:/home";
    }
    
     // Xử lý đăng xuất
     @GetMapping("/logout")
     public String logout(RedirectAttributes redirectAttributes) {
         sessionService.remove("user"); 
        //  Account user = sessionService.get("user", Account.class);
        //  cartService.clear(user.getUsername());
         redirectAttributes.addFlashAttribute("success", "Đăng xuất thành công!");
         return "redirect:/home";
    }
}
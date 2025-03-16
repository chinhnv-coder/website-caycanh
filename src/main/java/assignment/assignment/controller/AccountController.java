package assignment.assignment.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import assignment.assignment.DAO.AccountDAO;
import assignment.assignment.entity.Account;
import assignment.assignment.service.MailService;
import assignment.assignment.service.SessionService;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountDAO accountDAO;

    @Autowired
    MailService mailService;

    @Autowired 
    SessionService sessionService;

    // ============= ĐĂNG KÝ TÀI KHOẢN =============
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("view", "account-page/signup");
        return "layouts/layout";
    }

    @PostMapping("/sign-up")
    public String signUp(RedirectAttributes redirectAttributes, 
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("fullname") String fullname,
                        @RequestParam("email") String email) {
        if (username.trim().isEmpty() || password.trim().isEmpty() || fullname.trim().isEmpty() || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/account/sign-up";
        }

        if (accountDAO.existsById(username)) {
            redirectAttributes.addFlashAttribute("error", "Username đã tồn tại!");
            return "redirect:/account/sign-up";
        }

        try {
            // Tạo tài khoản mới
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setFullname(fullname);
            account.setEmail(email);
            account.setActivated(false);
            account.setAdmin(false);
            account.setActivationCode(UUID.randomUUID().toString());
            
            accountDAO.save(account);

            // Gửi email kích hoạt tài khoản
            String activationLink = "http://localhost:8080/account/activate?code=" + account.getActivationCode();
            mailService.sendEmail(account.getEmail(), "Kích hoạt tài khoản", 
                "Nhấn vào link sau để kích hoạt tài khoản: " + activationLink);
            
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi đăng ký tài khoản, vui lòng thử lại!");
            return "redirect:/account/sign-up";
        }
    }

    // ============= KÍCH HOẠT TÀI KHOẢN =============
    @GetMapping("/activate")
    public String activateAccount(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        Optional<Account> accountOpt = accountDAO.findByActivationCode(code);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setActivated(true);
            account.setActivationCode(null);
            accountDAO.save(account);
            
            redirectAttributes.addFlashAttribute("success", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mã kích hoạt không hợp lệ hoặc đã được sử dụng!");
        }
        return "redirect:/auth/login";
    }
    
    // ============= CHỈNH SỬA HỒ SƠ =============
    @GetMapping("/edit-profile")
    public String editProfileForm(Model model) {
        Account account = sessionService.get("user", Account.class);
        if (account == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("account", account);
        model.addAttribute("view", "account-page/profile");
        return "layouts/layout";
    }
    
    @PostMapping("/edit-profile")
    public String editProfile(@ModelAttribute("account") Account account, RedirectAttributes redirectAttributes) {
        Account existingAccount = sessionService.get("user", Account.class);
        if (existingAccount == null) {
            return "redirect:/auth/login";
        }

        // Kiểm tra nếu fullname hoặc email rỗng
        if (account.getFullname().trim().isEmpty() || account.getEmail().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/account/edit-profile";
        }

        // Kiểm tra định dạng email
        if (!account.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            redirectAttributes.addFlashAttribute("error", "Email không hợp lệ!");
            return "redirect:/account/edit-profile";
        }

        // Nếu nhập mật khẩu mới thì kiểm tra độ dài, nếu không nhập thì giữ nguyên mật khẩu cũ
        if (!account.getPassword().isEmpty()) {
            if (account.getPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "redirect:/account/edit-profile";
            }
            existingAccount.setPassword(account.getPassword());
        }

        // Cập nhật thông tin tài khoản
        existingAccount.setFullname(account.getFullname());
        existingAccount.setEmail(account.getEmail());
        accountDAO.save(existingAccount);
        sessionService.set("user", existingAccount);

        // Thông báo thành công
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/account/edit-profile";
    }

    // ============= QUÊN MẬT KHẨU (GỬI EMAIL RESET) =============
    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("view", "account-page/forgotPassword");
        return "layouts/layout";
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String username, 
                                    @RequestParam String email, 
                                    RedirectAttributes redirectAttributes, 
                                    Model model) {
        Optional<Account> accountOpt = accountDAO.findById(username);

        if (username.trim().isEmpty() || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/account/forgot-password";
        }

        if (accountOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/account/forgot-password";
        }
        
        Account account = accountOpt.get();

        if (!account.getEmail().equalsIgnoreCase(email)) {
            redirectAttributes.addFlashAttribute("error", "Email không khớp với tài khoản!");
            return "redirect:/account/forgot-password";
        }

        String resetToken = UUID.randomUUID().toString();
        account.setResetToken(resetToken);
        accountDAO.save(account);

        // Gửi email đặt lại mật khẩu
        String resetLink = "http://localhost:8080/account/reset-password?token=" + resetToken;
        mailService.sendEmail(account.getEmail(), "Đặt lại mật khẩu", 
        "Nhấn vào link sau để đặt lại mật khẩu: " + resetLink);

        redirectAttributes.addFlashAttribute("success", "Vui lòng kiểm tra email để đặt lại mật khẩu!");
        return "redirect:/account/forgot-password";
    }

    // ============= RESET MẬT KHẨU =============
    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.trim().isEmpty()) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            model.addAttribute("view", "account-page/reset-password");
            return "layouts/layout";
        }

        Optional<Account> accountOpt = accountDAO.findByResetToken(token);
        if (accountOpt.isEmpty()) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            model.addAttribute("view", "account-page/reset-password");
            return "layouts/layout";
        }

        model.addAttribute("token", token);
        model.addAttribute("view", "account-page/reset-password");
        return "layouts/layout";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam(value = "token", required = false) String token, 
                                @RequestParam String newPassword, 
                                @RequestParam String confirmPassword, 
                                RedirectAttributes redirectAttributes) {
        if (token == null || token.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Token không hợp lệ!");
            return "redirect:/account/reset-password";
        }

        Optional<Account> accountOpt = accountDAO.findByResetToken(token);
        if (accountOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Token không hợp lệ!");
            return "redirect:/account/reset-password?token=" + token;
        }

        if (newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu không được để trống!");
            return "redirect:/account/reset-password?token=" + token;
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/account/reset-password?token=" + token;
        }

        Account account = accountOpt.get();
        account.setPassword(newPassword);
        account.setResetToken(null);
        accountDAO.save(account);

        redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được đặt lại thành công!");
        return "redirect:/auth/login";
    }

    // ============= ĐỔI MẬT KHẨU (CÓ XÁC THỰC) =============
    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("view", "account-page/changPassword");
        return "layouts/layout";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("username") String username, 
                            @RequestParam("currentPassword") String currentPassword, 
                            @RequestParam("newPassword") String newPassword,
                            @RequestParam("confirmPassword") String confirmPassword,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        // Kiểm tra user đã đăng nhập chưa
        Account account = sessionService.get("user", Account.class);
        if (account == null) {
            return "redirect:/auth/login";
        }

        // Kiểm tra input không được để trống
        if (username.trim().isEmpty() || currentPassword.trim().isEmpty() ||
            newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/account/change-password";
        }

        // Kiểm tra username có khớp với user đang đăng nhập không
        if (!account.getUsername().equals(username)) {
            redirectAttributes.addFlashAttribute("error", "Tên tài khoản không hợp lệ!");
            return "redirect:/account/change-password";
        }

        // Kiểm tra mật khẩu cũ có đúng không
        if (!account.getPassword().equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
            return "redirect:/account/change-password";
        }

        // Kiểm tra xác nhận mật khẩu mới có khớp không
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
            return "redirect:/account/change-password";
        }

        // Cập nhật mật khẩu mới        
        account.setPassword(newPassword);
        accountDAO.save(account);
        
        // Thông báo đổi mật khẩu thành công
        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/account/change-password";
    }
}               

package assignment.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.entity.Account;
import assignment.assignment.entity.Category;
import assignment.assignment.service.CartService;
import assignment.assignment.service.SessionService;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private CartService cartService;

    @Autowired
    private SessionService sessionService;
    
    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);

        // Lấy thông tin tài khoản từ session
        Account user = sessionService.get("user", Account.class);

        int totalQuantity = 0;
        if (user != null) {
            totalQuantity = cartService.getTotalQuantity(user.getUsername());
        }

        model.addAttribute("totalQuantity", totalQuantity);
    }
}

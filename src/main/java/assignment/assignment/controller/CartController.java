package assignment.assignment.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.DAO.ProductDAO;
import assignment.assignment.entity.Account;
import assignment.assignment.entity.CartItem;
import assignment.assignment.entity.Category;
import assignment.assignment.service.CartService;
import assignment.assignment.service.SessionService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    CartService cartService;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    SessionService sessionService;

    @GetMapping("/index")
    public String index(Model model) {
        Account user = sessionService.get("user", Account.class); 

        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);

        // Nếu user chưa đăng nhập, hiển thị giỏ hàng trống
        if (user == null) {
            model.addAttribute("cartItems", List.of()); // Giỏ hàng rỗng
            model.addAttribute("totalPrice", 0.0);
            model.addAttribute("totalQuantity", 0);
            model.addAttribute("isEmptyCart", true);
        } else {
            Collection<CartItem> cartItems = cartService.getAllItems(user.getUsername());
            model.addAttribute("cartItems", cartItems);

            double totalPrice = cartService.getAmount(user.getUsername());
            model.addAttribute("totalPrice", totalPrice);

            int totalQuantity = cartService.getTotalQuantity(user.getUsername());
            model.addAttribute("totalQuantity", totalQuantity);

            boolean isEmptyCart = cartItems.isEmpty();
            model.addAttribute("isEmptyCart", isEmptyCart);
        }

        model.addAttribute("view", "user-page/cart"); 
        return "layouts/layout";
    }

    @PostMapping("/add")
    public String add(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        Account user = sessionService.get("user", Account.class);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return "redirect:/auth/login"; 
        }

        cartService.add(user.getUsername(), id);
        redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm vào giỏ hàng thành công!");
        return "redirect:/cart/index";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Integer id, @RequestParam("qty") Integer qty, RedirectAttributes redirectAttributes) {
        Account user = sessionService.get("user", Account.class);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để cập nhật giỏ hàng!");
            return "redirect:/auth/login";
        }

        if (qty > 0) {
            cartService.update(user.getUsername(), id, qty);
        } else {
            cartService.remove(user.getUsername(), id);
        }
        return "redirect:/cart/index";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        Account user = sessionService.get("user", Account.class);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xóa sản phẩm khỏi giỏ hàng!");
            return "redirect:/auth/login";
        }
        cartService.remove(user.getUsername(), id);
        return "redirect:/cart/index";
    }

    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        Account user = sessionService.get("user", Account.class);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xóa toàn bộ giỏ hàng!");
            return "redirect:/auth/login";
        }
        cartService.clear(user.getUsername());
        return "redirect:/cart/index";
    }
}

package assignment.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import assignment.assignment.DAO.OrderDAO;
import assignment.assignment.entity.Account;
import assignment.assignment.entity.Order;
import assignment.assignment.service.CartService;
import assignment.assignment.service.SessionService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    SessionService sessionService;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    CartService cartService;

    // Hiển thị danh sách đơn hàng của user
    @GetMapping("/index")
    public String index(Model model) {
        Account user = sessionService.get("user", Account.class);

        // Kiểm tra nếu chưa đăng nhập thì yêu cầu đăng nhập
        if (user == null) {
            return "redirect:/auth/login";
        }

        double totalPrice = cartService.getAmount(user.getUsername());
        model.addAttribute("totalPrice", totalPrice);
        
        // Lấy danh sách đơn hàng của user đang đăng nhập
        List<Order> orders = orderDAO.findByAccount_Username(user.getUsername());
        model.addAttribute("orders", orders);

        model.addAttribute("view", "user-page/order");
        return "layouts/layout";
    }

    // Hiển thị chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Long orderId, Model model) {
        Account user = sessionService.get("user", Account.class);

        // Kiểm tra nếu chưa đăng nhập thì yêu cầu đăng nhập
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Lấy đơn hàng theo ID và kiểm tra có thuộc user đang đăng nhập không
        Order order = orderDAO.findByIdAndAccount_Username(orderId, user.getUsername());

        if (order == null) {
            return "redirect:/order/index"; // Nếu không tìm thấy đơn hàng, quay lại danh sách
        }

        model.addAttribute("order", order);
        model.addAttribute("view", "user-page/order-detail");
        return "layouts/layout"; 
    }
}
